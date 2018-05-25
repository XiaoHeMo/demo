package v2;

import apiname.Apiname_v2;
import com.alibaba.fastjson.JSONObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import common.Data;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author yunhua.he
 * @date 2017/11/6
 */
public class AuthCode {

    private String accessToken = Data.accessToken;
    private String username = null;
    private String phone = null;
    private String email = null;
    private static String password = "123456";
    private static String newPassword = "QWE_123456";
    private static String livemode ="0";
    protected  String openId = null;
    protected  String token = null;

    @BeforeMethod()
    public void setUp() {
        RestAssured.baseURI = Data.url_v2;
    }

    @Test(groups = "register_phone", priority = 1)
    public void verifyAuthCode_false() {
        //随机生成合法的新手机号和邮箱
        phone = Data.createData(1);
        email = Data.createData(2);

        //手机验证码
        JSONObject json = JSONObject.parseObject(Apiname_v2.verifyAuthCode);
        json.put("code", "11111111");
        json.put("type", "phone");
        json.put("value", phone);
        Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/verifyAuthCode?accessToken={accesstoken}&livemode={livemode}", accessToken, livemode);
        Data.expectNot400("verifyAuthCode_false", res, json.toString());
        res.then().assertThat().statusCode(200).body(equalTo("false"));
        //邮箱验证码
        JSONObject json1 = JSONObject.parseObject(Apiname_v2.verifyAuthCode);
        json1.put("code", "11111111");
        json1.put("type", "email");
        json1.put("value", email);
        Response res1 = given().contentType(ContentType.JSON).body(json1.toString()).when().post("/verifyAuthCode?accessToken={accesstoken}&livemode={livemode}", accessToken, livemode);
        Data.expectNot400("verifyAuthCode_false",res1,json1.toString());
        res1.then().assertThat().statusCode(200).body(equalTo("false"));
    }

    @Test(dependsOnMethods = "verifyAuthCode_false", groups = "register_phone")
    public void verifyAuthCode_true() {
        JSONObject json = JSONObject.parseObject(Apiname_v2.verifyAuthCode);
        json.put("code", "88888888");
        json.put("type", "phone");
        json.put("value", phone);
        Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/verifyAuthCode?accessToken={accesstoken}&livemode={livemode}",accessToken, livemode);
        Data.expectNot400("verifyAuthCode_true", res, json.toString());
        res.then().assertThat().statusCode(200).body(equalTo("true"));

        JSONObject json1 = JSONObject.parseObject(Apiname_v2.verifyAuthCode);
        json1.put("code", "88888888");
        json1.put("type", "email");
        json1.put("value", email);
        Response res1 = given().contentType(ContentType.JSON).body(json1.toString()).when().post("/verifyAuthCode?accessToken={accesstoken}&livemode={livemode}",accessToken, livemode);
        Data.expectNot400("verifyAuthCode_true", res1, json.toString());
        res1.then().assertThat().statusCode(200).body(equalTo("true"));
    }

    @Test(groups = "register_phone", dependsOnMethods = "verifyAuthCode_true")
    public void isExisted_beforeRegister() {
        Response res = given().when().get("/isExisted?value={phone}&accessToken={accesstoken}&livemode={livemode}",phone, accessToken, livemode);
        Data.expectNot400("isExisted_beforeRegister",res,phone);
        res.then().assertThat().statusCode(200).body(equalTo("false"));
    }

    @Test(groups = "register_phone",dependsOnMethods ="isExisted_beforeRegister")
    public void registerByPhone() {
        JSONObject json = JSONObject.parseObject(Apiname_v2.registerByPhone);
        //修改请求报文中赋予的默认值
        json.put("phone", phone);
        json.put("password", password);
        Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/registerByPhone?accessToken={accesstoken}&livemode={livemode}",accessToken, livemode);
        Data.expectNot400("registerByPhone",res,json.toString());
        res.then().assertThat().statusCode(201).
                body("openId", notNullValue(), "token", notNullValue());
        String result = res.andReturn().asString();
        //验证返回内容是否符合规范
        assertThat(result, matchesJsonSchemaInClasspath("json_schema_v2/register.json"));
    }

    @Test(dependsOnMethods ="registerByPhone", groups = "register_phone")
    public void isExisted_afterRegister() {
        Response res = given().when().get("/isExisted?value={phone}&accessToken={accesstoken}&livemode={livemode}", phone, accessToken, livemode);
        Data.expectNot400("isExisted_afterRegister",res,phone);
        res.then().assertThat().statusCode(200).body(equalTo("true"));
    }

    @Test(groups = "register_phone", dependsOnMethods = "isExisted_afterRegister")
    public void login() {
        JSONObject json = JSONObject.parseObject(Apiname_v2.login);
        json.put("username", phone);
        json.put("password", password);
        Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/login?accessToken={accesstoken}&livemode={livemode}", accessToken, livemode);
        Data.expectNot400("login", res, json.toString());
        res.then().assertThat().statusCode(200).
                body("openId", notNullValue(), "token", notNullValue()).extract().response();
        String result = res.andReturn().asString();
        assertThat(result, matchesJsonSchemaInClasspath("json_schema_v2/login.json"));
        token = res.path("token");
        openId = res.path("openId");
    }

    @Test(groups = "register_phone",dependsOnMethods = "login")
    public void userSecurityLevel() {
        Response res = given().contentType(ContentType.JSON).when().get("/userSecurityLevel?token={token}&openId={openId}&livemode={livemode}",token, openId, livemode);
        Data.expectNot400("userSecurityLevel", res, token+"++openid++"+openId);
        res.then().assertThat().statusCode(200).body( "level", is(1));
    }

    @Test(dependsOnMethods ="userSecurityLevel", groups = "register_phone")
    public void addUserName() {
        username = Data.createData(0);
        JSONObject json = JSONObject.parseObject(Apiname_v2.addUsername);
        json.put("username", username);
        json.put("openId", openId);
        Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/addUsername?token={token}&livemode={livemode}", token, livemode);
        Data.expectNot400("addUsername", res, json.toString());
        res.then().assertThat().statusCode(200).
                body("result.code",equalTo("200"), "result.msg",equalTo("success"));
        String result = res.andReturn().asString();
        assertThat(result, matchesJsonSchemaInClasspath("json_schema_v2/common.json"));
    }

    @Test(groups = "register_phone", dependsOnMethods = "addUserName")
    public void bindEmail() {
        email = Data.createData(2);
        JSONObject json = JSONObject.parseObject(Apiname_v2.bindEmail);
        json.put("email", email);
        json.put("authCode", "88888888");
        json.put("openId", openId);
        Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/bindEmail?token={token}&livemode={livemode}", token, livemode);
        Data.expectNot400("bindEmail", res, json.toString());
        String result = res.andReturn().asString();
        assertThat(result, matchesJsonSchemaInClasspath("json_schema_v2/common.json"));
    }

    @Test(groups = "register_phone", dependsOnMethods = "bindEmail")
    public void userSecurityLevel_2() {
        Response res = given().contentType(ContentType.JSON).
                when().get("/userSecurityLevel?token={token}&openId={openId}&livemode={livemode}",token, openId, livemode);
        Data.expectNot400("userSecurityLevel_2",res,token+"openid"+openId);
        res.then().assertThat().statusCode(200).body( "level",is(2));
    }
}
