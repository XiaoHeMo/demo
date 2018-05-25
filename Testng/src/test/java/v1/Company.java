package v1;

import apiname.Apiname_v1;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import common.Data;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author yunhua.he
 * @date 2018/2/28
 */
public class Company {

    private static String accessToken = Data.accessToken;
    private static String livemode = Data.livemode;
    private static String phone = null;
    private static String email = null;
    private static String name = null;
    private static String token = null;
    private static String openId = null;
    private static String companyId = null;
    private static String uid = null;
    private static String id = null;

    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = "http://tuapi.chiq-cloud.com/ent/v1";
    }

    @Test
    public void check() {
        String isPhone = "false";
        String isEmail = "false";
        while (isPhone.equals("false") || isEmail.equals("false")) {
            phone = Data.createData(1);
            email = Data.createData(2);
            Response res = given().when().get("/company/{0}/check?accessToken={1}&livemode={2)", phone, accessToken, livemode);
            Data.expectNot400("check-phone", res, phone);
            isPhone = res.toString();
            Response res2 = given().when().get("/company/{0}/check?accessToken={1}&livemode={2}", email, accessToken, livemode);
            Data.expectNot400("check-email", res2, email);
            isEmail = res2.toString();
            System.out.println("phone="+phone+", email="+email);
        }
    }

    @Test(dependsOnMethods = "check")
    public void createCompany() {
        JSONObject json = JSONObject.parseObject(Apiname_v1.createCompany);
        name = "hyh"+Data.createData(0);
        json.put("phone", phone);
        json.put("email", email);
        json.put("name", name);
        Response res = given().contentType(ContentType.JSON).body(json).when().post("/company/create?accessToken={0}&livemode={1}", accessToken, livemode);
        Data.expectNot400("createCompany", res, json.toString());
        res.then().assertThat().
                statusCode(200).
                body("openId", notNullValue(), "token", notNullValue(), "companyId", notNullValue()).
                extract().response();
        token = res.path("token");
        openId = res.path("openId");
        companyId = res.path("companyId");
    }

    @Test(dependsOnMethods = "createCompany")
    public void checkByOpenId() {
        Response res = given().when().get("/company/user/{0}/checkByOpenId?accessToken={1}&livemode={2}", openId, accessToken, livemode);
        Data.expectNot400("checkByOpenId", res, openId);
        res.then().assertThat().
                statusCode(200).
                body("result", equalTo(true)).
                extract().response();
    }

    @Test(dependsOnMethods = "checkByOpenId")
    public void updateCompany() {
        JSONObject json = JSON.parseObject(Apiname_v1.updateCompany);
        json.put("companyId", companyId);
        Response res = given().contentType(ContentType.JSON).body(json).post("/company/{0}/update?accessToken={1}&livemode={2}", openId, accessToken, livemode);
        Data.expectNot400("updateCompany", res, json.toString());
        res.then().assertThat().statusCode(200).extract().response();
    }

    @Test(dependsOnMethods = "updateCompany")
    public void updateAuditState() {
        JSONObject json = new JSONObject();
        json.put("auditState", 1);
        json.put("companyId", companyId);
        Response res = given().contentType(ContentType.JSON).body(json).when().post("/company/updateAuditState?accessToken={0}&livemode={1}", accessToken, livemode);
        Data.expectNot400("updateAuditState", res, json.toString());
        res.then().assertThat().
                statusCode(200).
                body("result", equalTo("success")).
                extract().response();
    }

    @Test(dependsOnMethods = "updateAuditState")
    public void find() {
        Response res = given().when().get("/company/find?accessToken={0}&livemode={1}&openId={2}", accessToken, livemode, openId);
        Data.expectNot400("find", res, openId);
        res.then().assertThat().statusCode(200).
                body("companyId", equalTo(companyId), "auditState", equalTo("审核通过"),
                        "name", equalTo(name), "businessAddress", equalTo("四川省成都市游仙区")).
                extract().response();
        id = res.path("id");
        uid = res.path("uid");
    }

    @Test(dependsOnMethods = "find")
    public void updateCompanyState() {
        JSONObject json = new JSONObject();
        json.put("companyState", 1);
        json.put("companyId", companyId);
        Response res = given().contentType(ContentType.JSON).body(json).when().post("/company/updateCompanyState?accessToken={0}&livemode={1}", accessToken,livemode);
        res.then().assertThat().statusCode(200).extract().response();
    }

    @Test(dependsOnMethods = "updateCompanyState")
    public void findByCompanyId(){
        Response res = given().when().get("/company/findbyCompanyId?accessToken={0}&livemode={1}&companyId={2}", accessToken, livemode, companyId);
        Data.expectNot400("findByCompanyId", res, companyId);
        res.then().assertThat().
                statusCode(200).
                body("companyState", equalTo("冻结"), "bankName", equalTo("中国农业银行"),
                        "officialWebsite", equalTo("www.changhong.com"), "brandNameCh", equalTo("ChiQ电视")).
                extract().response();
    }

    @Test(dependsOnMethods = "findByCompanyId")
    public void getCompanyList() {
        Response res = given().when().get("/company/getCompanyList?accessToken={0}&livemode={1}&name={2}&companiesNumber=200" +
                "&businessAddress=四川省成都市游仙区&industryType=4&pageSize=10&pageNo=1", accessToken, livemode, name);
        Data.expectNot400("getCompanyList", res, name);
        res.then().assertThat().statusCode(200).body("list[0].name", equalTo(name)).extract().response();
    }

}
