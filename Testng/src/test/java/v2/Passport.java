package v2;

import apiname.Apiname_v2;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import common.Data;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author yunhua.he
 * @date 2017/11/6
 */
public class Passport {

    public static String accessToken = Data.accessToken;
    public static String livemode ="0";
    public List<String> account = new ArrayList<String>();
    public List<String> openids = new ArrayList<String>();
    public List<String> ids = new ArrayList<String>();
    public String uid = null;
    public String appId = "test";
    public String app_id = "e31c3d871d27132a";
    public int pageSize = 10 ;
    public int pageNum = 1;
    public Integer formerTotal = null;
    public Integer formerAdded = null;
    public Integer latterAdded = null;

    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = Data.url_v2;
    }

    @Test
    public void dailyStatistics() {
        Response res = given().when().get("/dailyStatistics?accessToken={accessToken}&livemode={livemode}" ,accessToken,livemode);
        Data.expectNot400("dailyStatistics", res, "获取注册/新增/活跃用户数");
        res.then().assertThat().statusCode(200).
                body("recordDate", equalTo(Data.getDay("yesterday")),
                        "formerTotal", equalTo(res.path("total")),
                        "formerActivate", equalTo(res.path("activateUser")),
                        "formerAdded", equalTo(res.path("newlyAdded"))).
                extract().response();
        formerTotal = res.path("formerTotal");
        formerAdded = res.path("formerAdded");
    }

    @Test(dependsOnMethods = "dailyStatistics")
    public void findUserRegistByPage() {
        Response res = given().when().get("/findUserRegistByPage?pageNo={pageNo}&pageSize={pageSize}&accessToken={accessToken}&livemode={livemode}", pageNum, pageSize, accessToken, livemode);
        Data.expectNot400("findUserRegistByPage", res, "分页获取用户注册量");
        res.then().assertThat().statusCode(200).body("list[0].registUser", equalTo(formerAdded)).extract().response();
    }

    @Test(dependsOnMethods = "findUserRegistByPage")
    public void getIncrementsByTimeBucket() {
        String stime = Data.getDay("yesterday");
        String etime = Data.getDay("today");
        Response res = given().when().get("/getIncrementsByTimeBucket?livemode={livemode}&accessToken={accessToken}&stime={stime}&etime={etime}&appId={appId}",livemode , accessToken ,stime ,etime ,appId);
        Data.expectNot400("getIncrementsByTimeBucket", res, stime+"<<>>"+ etime);
        res.then().assertThat().statusCode(200).body(Data.getDay("yesterday"), notNullValue(),
                Data.getDay("today"), notNullValue()).extract().response();
        latterAdded = res.path(Data.getDay("today"));
    }

    @Test(dependsOnMethods = "getIncrementsByTimeBucket")
    public void getCountSso() {
        Response res = given().when().get("/getCountsso?accessToken={accessToken}&livemode={livemode}",accessToken ,livemode);
        Data.expectNot400("getCountSso", res, "sso");
        res.then().assertThat().statusCode(200).body("内部员工", notNullValue(), "普通员工", notNullValue()).extract().response();
        Integer userIn = res.path("内部员工");
        Integer userOut = res.path("普通员工");
        System.out.println(">>>>num= "+ userIn+userOut);
        //总和应该比某个appId下的今日新增和昨日总数之和大或相等
        assertThat(userIn+userOut, greaterThanOrEqualTo(formerTotal+latterAdded));
    }

    @Test(dependsOnMethods = "getCountSso")
    public void countUserByAppId() {
        Response res = given().contentType(ContentType.JSON).when().
                get("/countUserByAppId?accessToken={accessToken}&livemode={livemode}",accessToken ,livemode);
        Data.expectNot400("countUserByAppId",res,"appId");
        res.then().statusCode(200).body(notNullValue());
    }

    @Test(dependsOnMethods = "countUserByAppId")
    public void getAllAppName() {
        Response res = given().contentType(ContentType.JSON).when().
                get("/getAllAppName?accessToken={accessToken}&livemode={livemode}",accessToken ,livemode);
        Data.expectNot400("getAllAppName",res,"wu");
        res.then().statusCode(200).body(notNullValue());
    }

    //批量注册，2个内部员工，2个普通用户
    @Test(dependsOnMethods = "getAllAppName")
    public void registBatch() {
        String  registField = "phone";
        JSONArray array0 = new JSONArray();
        JSONArray array1 = new JSONArray();
        for (int i=0; i<4; i++) {
            account.add(i, Data.createData(1));
            JSONObject json = JSONObject.parseObject(Apiname_v2.registBatch);
            json.put("account", account.get(i));
            json.put("nickName", "普通用户" + i);
            if (i < 2) {
                array0.add(i, json);
            } else {
                array1.add(json);
            }
        }
            Response res = given().contentType(ContentType.JSON).body(array0.toString()).when().post("/registBatch?accessToken={accesstoken}&livemode={livemode}&userType=0&registField={registField}&appId={appId}", accessToken, livemode, registField , appId);
            Data.expectNot400("registBatch", res, array0.toString());
            res.then().assertThat().statusCode(200).body("success", hasSize(2));
            Response res1 = given().contentType(ContentType.JSON).body(array1.toString()).when().post("/registBatch?accessToken={accesstoken}&livemode={livemode}&userType=1&registField={registField}&appId={appId}", accessToken, livemode, registField , appId);
            Data.expectNot400("registBatch", res1, array0.toString());
            res1.then().assertThat().statusCode(200).body("success", hasSize(2));
    }

    //重置密码并登陆
    @Test(dependsOnMethods = "registBatch")
    public void resetPwd() {
        for (int i=0; i<4; i++) {
            JSONObject json = JSONObject.parseObject(Apiname_v2.resetPwd);
            json.put("password", "12345678");
            json.put("phone", account.get(i));
            Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/resetPwd?accessToken={accessToken}&livemode={livemode}",accessToken , livemode);
            Data.expectNot400("resetPwd", res, json.toString());
            res.then().assertThat().statusCode(200).body("msg", equalTo("密码重置成功"));
            //登录
            Response res1 = MixOperate.login(account.get(i), "12345678");
            Data.expectNot400("login_afterResetPwd",res1,account.get(i));
            res1.then().assertThat().statusCode(200).extract().response();
            openids.add(i, res1.path("openId").toString());
        }
    }

    @Test(dependsOnMethods = "resetPwd")
    public void isEmployee() {
        for (int i=0; i<4; i++) {
            Response res = given().when().get("/isEmployee?openId={openId}&accessToken={accessToken}&livemode={livemode}", openids.get(i), accessToken, livemode);
            Data.expectNot400("isEmployee", res, openids.get(i));
            if (i < 2) {
                res.then().assertThat().statusCode(200).body(equalTo("false"));
            } else {
                res.then().assertThat().statusCode(200).body(equalTo("true"));
            }
        }
    }

    @Test(dependsOnMethods = "isEmployee")
    public void getUserInfoByOpenIds() {
        JSONObject json = new JSONObject();
        json.put("openIds", openids);
        Response res = given().contentType(ContentType.JSON).when().body(json.toString()).post("/getUserInfoByOpenIds?accessToken={accesstoken}&livemode={livemode}",accessToken,livemode);
        Data.expectNot400("getUserInfoByOpenIds",res,json.toString());
        res.then().assertThat().statusCode(200).body("code", equalTo(200), "msg", equalTo("success"), "userlist.size()", is(4));
    }

    @Test(dependsOnMethods = "getUserInfoByOpenIds")
    public void getUidByOpenId() {
        Response res=given().contentType(ContentType.JSON).get("/getUidByOpenId?accessToken={accessToken}&livemode={livemode}&openId={openId}", accessToken, livemode, openids.get(0));
        Data.expectNot400("getUidByOpenId", res, openids.get(0));
        res.then().assertThat().statusCode(200).body("uid", notNullValue());
        uid = res.path("uid");
        System.out.print("uid:" + uid);
    }

    @Test(dependsOnMethods = "getUidByOpenId")
    public void updateUserInfoByUid() {
        JSONObject json = JSONObject.parseObject(Apiname_v2.updateUserInfoByUid);
        json.put("uid", uid);
        json.put("nickName", "新昵称");
        Response res = given().contentType(ContentType.JSON).when().body(json.toString()).post("/updateUserInfoByUid?accessToken={accessToken}&livemode={livemode}", accessToken, livemode);
        Data.expectNot400("updateUserInfoByUid", res, json.toString());
        res.then().statusCode(200).body("result.code", equalTo("200"), "result.msg", equalTo("success"));
        //更新后，根据openId获取用户信息
        List<String> openId = new ArrayList<>();
        openId.add(openids.get(0));
        JSONObject json2 = new JSONObject();
        json2.put("openIds", openId);
        Response res2 = given().contentType(ContentType.JSON).when().body(json2.toString()).post("/getUserInfoByOpenIds?accessToken={accesstoken}&livemode={livemode}", accessToken, livemode);
        Data.expectNot400("getUserInfoByOpenIds", res2, json2.toString());
        res2.then().assertThat().statusCode(200).body("code", equalTo(200),
                "msg", equalTo("success"), "userlist[0].nickName", equalTo("新昵称")).extract().response();
    }

    @Test(dependsOnMethods = "getUidByOpenId")
    public void getAllUsersInfo() {
        JSONObject json = JSONObject.parseObject(Apiname_v2.getAllUsersInfo);
        json.put("phone", "13303335448");
        json.put("province", "四川省");
        json.put("city", "哈尔滨市");
        json.put("activateStatus", "1");
        json.put("authenStatus", "0");
        json.put("accountStatus","1");
        json.put("sso","2");
        Response res = given().contentType(ContentType.JSON).when().body(json.toString()).post("/getAllUsersInfo?livemode={livemode}&accessToken={accessToken}&pageSize={pageSize}&pageNum={pageNum}", livemode, accessToken, pageSize, pageNum);
        Data.expectNot400("getAllUsersInfo", res, json.toString());
        res.then().statusCode(200).body("pageSize",equalTo(pageSize),"pageNum",equalTo(pageNum),
                "list[0].uid", equalTo("0220a76082dc42cd"), "list[0].registerTime", equalTo("2017-06-15 17:20:15"),
                "list[0].phone", equalTo("13303335448")).extract().response();
    }
}
