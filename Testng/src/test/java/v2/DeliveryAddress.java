package v2;

import apiname.Apiname_v2;
import com.alibaba.fastjson.JSONObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import common.Data;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author yunhua.he
 * @date 2017/11/6
 */
public class DeliveryAddress {

    private String username = "cc_";
    private String password = "12345678";
    private String livemode = "0";
    private String openId ="";
    private String token ="";
    private int id;

    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = Data.url_v2;
    }

    @Test
    public void register_login() {
        username = Data.createData(0);
        //先注册
        Response res1 = MixOperate.register(username, password);
        Data.expectNot400("register",res1,username+"password"+password);
        res1.then().assertThat().statusCode(201).extract().response();
        //再登录
        Response res2 = MixOperate.login(username, password);
        Data.expectNot400("login", res2, username+"password"+password);
        res2.then().assertThat().statusCode(200).extract().response();
        token = res2.path("token");
        openId = res2.path("openId");
    }

    @Test(dependsOnMethods = "register_login")
    public void add_delivery() {
        JSONObject json = JSONObject.parseObject(Apiname_v2.addDelivery);
        Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/addDeliveryAddr?token={token}&openId={openid}&livemode={livemode}",token,openId,livemode);
        Data.expectNot400("addDelivery", res, json.toString());
        res.then().assertThat().statusCode(200).
                body("result.code", equalTo("200"), "result.msg", equalTo("success")).extract().response();
        String result = res.andReturn().asString();
        assertThat(result, matchesJsonSchemaInClasspath("json_schema_v2/common.json"));
        //添加一条记录,便于后续删除操作
        json.put("addrDetail","创新大道二街");
        json.put("linkmanName","小neinei");
        given().contentType(ContentType.JSON).body(json.toString()).
                when().post("/addDeliveryAddr?token={token}&openId={openid}&livemode={livemode}",token,openId,livemode);
    }

    @Test(dependsOnMethods = "add_delivery")
    public void get_delivery() {
        Response res = getDelivery(token, openId, livemode);
        Data.expectNot400("get_delivery", res, token+"<<>>"+openId);
        res.then().assertThat().statusCode(200).
                body("[0].addrDetail", equalTo("创新大道二街"),
                        "[0].linkmanName", equalTo("小neinei"),
                        "[0].linkmanTel", equalTo("13311111111"),
                        "[0].lProvince", equalTo("四川省"),
                        "[0].email", equalTo("qq@qq.com"),
                        "[0].isDefault", equalTo(0)).
                extract().response();
        id = res.path("[0].id");
        System.out.println("id=" + id);
        String result = res.andReturn().asString();
        assertThat(result, matchesJsonSchemaInClasspath("json_schema_v2/getDelivery.json"));
    }

    @Test(dependsOnMethods = "get_delivery")
    public void update_delivery() {
        JSONObject json = JSONObject.parseObject(Apiname_v2.updateDelivery);
        json.put("id", id);
        Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/updateDeliveryAddr?token={token}&openId={openid}&livemode={livemode}",token,openId,livemode);
        Data.expectNot400("update_delivery", res, json.toString());
        res.then().assertThat().statusCode(200).
                body("result.code", equalTo("200"), "result.msg",equalTo("success")).
                extract().response();
        String result = res.andReturn().asString();
        assertThat(result, matchesJsonSchemaInClasspath("json_schema_v2/common.json"));
        //不能修改非自己的id
        JSONObject json1 = JSONObject.parseObject(Apiname_v2.updateDelivery);
        json1.put("id", 1);
        Response res1 = given().contentType(ContentType.JSON).body(json1.toString()).when().post("/updateDeliveryAddr?token={token}&openId={openid}&livemode={livemode}",token,openId,livemode);
        Data.expect400("updateDeliveryAddr", res1, json1.toString());
        res1.then().assertThat().statusCode(400).body("code", equalTo(2000008));
    }

    @Test(dependsOnMethods = "update_delivery")
    public void getDelivery_afterUpdate() {
        Response res = getDelivery(token, openId, livemode);
        Data.expectNot400("getDelivery_afterUpdate", res, token+"<<>>"+openId);
        res.then().assertThat().statusCode(200).
                body("[0].addrDetail", equalTo("创新大道三街"),
                        "[0].linkmanName", equalTo("大neinei"),
                        "[0].linkmanTel", equalTo("13311111112"),
                        "[0].isDefault", is(0)).
                extract().response();
        String response = res.andReturn().asString();
        assertThat(response, matchesJsonSchemaInClasspath("json_schema_v2/getDelivery.json"));
    }

    @Test(dependsOnMethods = "getDelivery_afterUpdate")
    public void setDefault() {
         Response res = given().when().get("/setDefault?token={token}&id={id}&livemode={livemode}",token,id,livemode);
         Data.expectNot400("setDefault", res, token+"<<>>"+openId);
        res.then().assertThat().statusCode(200).body("status", equalTo(true)).extract().response();
    }

    @Test(dependsOnMethods = "setDefault")
    public void getDelivery_afterDefault() {
        Response res = getDelivery(token, openId,livemode);
        Data.expectNot400("getDelivery_afterDefault",res,token+"<<>>"+openId);
        res.then().assertThat().statusCode(200).
                body("[0].addrDetail", Matchers.equalTo("创新大道三街"),
                        "[0].linkmanName", Matchers.equalTo("大neinei"),
                        "[0].linkmanTel", Matchers.equalTo("13311111112"),
                        "[0].isDefault", is(1));
    }

    @Test(dependsOnMethods = "getDelivery_afterDefault")
    public void getDeliveryById() {
        Response res = given().contentType(ContentType.JSON).when().get("/getDeliveryAddr?token={token}&id={id}&livemode={livemode}",token,id,livemode);
        Data.expectNot400("getDeliveryAddr_id",res,token+"<<>>>"+id);
        res.then().assertThat().statusCode(200).
                body("data.addrDetail", Matchers.equalTo("创新大道三街"),
                        "data.linkmanName", equalTo("大neinei"),
                        "data.linkmanTel", equalTo("13311111112"),
                        "data.isDefault", is(1));
    }

    @Test(dependsOnMethods = "getDeliveryById")
    public void delete_delivery() {
        //删除非自己的id
        JSONObject json = JSONObject.parseObject(Apiname_v2.deleteDelivery);
        Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/deleteDeliveryAddr?token={token}&openId={openid}&livemode={livemode}",token,openId,livemode);
        Data.expect400("deleteDeliveryAddr",res,json.toString());
        res.then().assertThat().statusCode(400). body( "code", Matchers.equalTo(2000008));
        json.put("idList", id);
        Response res1 = given().contentType(ContentType.JSON).body(json.toString()).when().post("/deleteDeliveryAddr?token={token}&openId={openid}&livemode={livemode}",token,openId,livemode);
        Data.expectNot400("deleteDeliveryAddr",res1,json.toString());
        res1.then().assertThat().statusCode(200).
                body("result.code", equalTo("200"), "result.msg", equalTo("success")).
                extract().response();
        String response = res1.andReturn().asString();
        assertThat(response, matchesJsonSchemaInClasspath("json_schema_v2/common.json"));
    }


    @Test(dependsOnMethods = "delete_delivery")
    public void getDelivery_afterDelete() {
        Response res = given().contentType(ContentType.JSON).
                when().get("/getDeliveryAddr?token={token}&id={id}&livemode={livemode}",token,id,livemode);
        Data.expectNot400("getDelivery_afterDelete",res,token+"<<>>"+id);
        res.then().assertThat().statusCode(200). body(equalTo("{\"status\":true}"));
    }


    public Response getDelivery(String token,String openId,String livemode){
        Response res = given().when().get("/getDeliveryAddrList?token={token}&openId={openid}&livemode={livemode}",token,openId,livemode);
        return res;
    }
}
