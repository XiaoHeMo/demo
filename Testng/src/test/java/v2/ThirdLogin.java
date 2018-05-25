package v2;

import apiname.Apiname_v2;
import com.alibaba.fastjson.JSONObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import common.Data;
import org.hamcrest.core.IsEqual;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;
import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static v2.MixOperate.register;

/**
 * @author yunhua.he
 * @date 2017/11/6
 */
public class ThirdLogin {
    public String connectId = null;
    public String unionId = null;
    public String token = null;
    public String openId = null;
    public String username = null;
    public String password = "12345678";
    public static final String accessToken =Data.accessToken;
    public static String livemode = "0";

    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = Data.url_v2;
    }

    @Test(priority = 1)
    public void register_login() {
        username = Data.createData(0);
        Response res = register(username, password);
        Data.expectNot400("register_ThirdLogin",res,username+"password"+password);
        res.then().assertThat().statusCode(201).extract().response();
    }

    //验证第三方是否与本地绑定
    @Test(dependsOnMethods = "register_login")
    public void isBind() {
        connectId = "a" + new Random().nextInt(99999999);
        JSONObject json = JSONObject.parseObject(Apiname_v2.isBind);
        json.put("connectid", connectId);
        Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/isbinded?accessToken={accessToken}&livemode={livemode}", accessToken, livemode);
        Data.expectNot400("isBind_no",res,json.toString());
        res.then().assertThat().statusCode(200).body(equalTo("false")).extract().response();
    }

    //第三方账号和本地账号进行绑定
    @Test(dependsOnMethods = "isBind")
    public void accountBind() {
        unionId = UUID.randomUUID().toString().replace("-", "");
        System.out.print("unionId =" + unionId);
        JSONObject json = JSONObject.parseObject(Apiname_v2.accountBind);
        json.put("connectid", connectId);
        json.put("unionid", unionId);
        json.put("username", username);
        json.put("password", password);
        json.put("source", "wechat");
        json.put("access_token", "1234567890987654321");
        Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/accountbind?accessToken={accessToken}&livemode={livemode}", accessToken, livemode);
        Data.expectNot400("accountBind", res, json.toString());
        res.then().assertThat().statusCode(200).
                body("openId", notNullValue(), "token", notNullValue()).
                extract().response();
        token = res.path("token");
        openId = res.path("openId");
        //通过openId查询全部绑定结果
        JSONObject json1 = JSONObject.parseObject(Apiname_v2.findBindAccount);
        json1.put("openId", openId);
        json1.put("source", "wechat");
        Response res2 = given().contentType(ContentType.JSON).body(json1.toString()).when().post("/findBindAccount?accessToken={accessToken}&livemode={livemode}", accessToken, livemode);
        Data.expectNot400("accountBind",res2,json1.toString());
        res2.then().assertThat().statusCode(200).
                body("[0].uid", notNullValue(),
                        "[0].connect_id", equalTo(connectId),
                        "[0].unionid", equalTo(unionId),
                        "[0].accessToken", equalTo("1234567890987654321"),
                        "[0].source",equalTo("wechat")).
                extract().response();
    }

    @Test(dependsOnMethods = "accountBind")
    public void isBind_yes() {
        JSONObject json = JSONObject.parseObject(Apiname_v2.isBind);
        json.put("connectid", connectId);
        Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/isbinded?accessToken={accessToken}&livemode={livemode}", accessToken, livemode);
        Data.expectNot400("isBind",res,json.toString());
        res.then().assertThat().statusCode(200).body(IsEqual.equalTo("true")).extract().response();
    }

    //第三方账号和openId进行绑定
    @Test(dependsOnMethods = "isBind_yes")
    public void toBind() {
        JSONObject json = JSONObject.parseObject(Apiname_v2.toBind);
        json.put("connectid", connectId+1);
        json.put("openid", openId);
        json.put("access_token","123123123123" );
        json.put("source","QQ" );
        Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/tobind?accessToken={accessToken}&livemode={livemode}", accessToken, livemode);
        Data.expectNot400("toBind", res, json.toString());
        res.then().assertThat().statusCode(200).
                body("openId", notNullValue(), "token", notNullValue()).
                extract().response();

        //再绑定一个第三方账号
        JSONObject json1 = JSONObject.parseObject(Apiname_v2.toBind);
        json1.put("connectid",connectId+2);
        json1.put("openid",openId );
        json1.put("access_token","111222333444" );
        json1.put("source","QQ" );
        Response res2 = given().contentType(ContentType.JSON).body(json1.toString()).when().post("/tobind?accessToken={accessToken}&livemode={livemode}", accessToken, livemode);
        Data.expectNot400("tobind",res2,json1.toString());
        res2.then().assertThat().statusCode(200).
                body("openId",notNullValue(), "token",notNullValue()).
                extract().response();
    }

    //通过openId查询所有绑定
    @Test(dependsOnMethods = "toBind")
    public void findBindAccount() {
        JSONObject json = JSONObject.parseObject(Apiname_v2.findBindAccount);
        json.put("openId",openId );
        json.put("source","QQ" );
        Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/findBindAccount?accessToken={accessToken}&livemode={livemode}", accessToken, livemode);
        Data.expectNot400("findBindAccount",res,json.toString());
        res.then().assertThat().statusCode(200).
                body("[1].id", notNullValue(),
                        "[1].uid", notNullValue(),
                        "[1].unionid", nullValue(),
                        "[1].connect_id", equalTo(connectId+2),
                        "[1].accessToken", equalTo("111222333444"),
                        "[1].source", equalTo("QQ"),
                        "[0].id",notNullValue(),
                        "[0].uid" ,notNullValue(),
                        "[0].unionid" , nullValue(),
                        "[0].connect_id",equalTo(connectId+1),
                        "[0].accessToken",equalTo("123123123123"),
                        "[0].source",equalTo("QQ")).
                extract().response();
    }

    //第三方账号登录
    @Test(dependsOnMethods = "findBindAccount")
    public void bindLogin() {
        JSONObject json = JSONObject.parseObject(Apiname_v2.bindLogin);
        json.put("connectid", connectId+1);
        Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/bindlogin?accessToken={accessToken}&livemode={livemode}", accessToken, livemode);
        Data.expectNot400("bindLogin",res,json.toString());
        res.then().assertThat().statusCode(200).
                body("token", notNullValue(), "openId", notNullValue()).
                extract().response();
        token = res.path("token");
        openId = res.path("openId");
    }

    //解除绑定
    @Test(dependsOnMethods = "bindLogin")
    public void removeBind() {
        JSONObject json = JSONObject.parseObject(Apiname_v2.removeBind);
        json.put("connectid",connectId + 1 );
        Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/removeBind?accessToken={accessToken}&livemode={livemode}", accessToken, livemode);
        Data.expectNot400("removeBind",res,json.toString());
        res.then().assertThat().statusCode(200).
                body("result.code", IsEqual.equalTo("200"), "result.msg", equalTo("success"));
    }

    @Test(dependsOnMethods = "removeBind")
    public void bindLogin_afterRemove() {
        JSONObject json = JSONObject.parseObject(Apiname_v2.bindLogin);
        json.put("connectid", connectId+1);
        Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/bindlogin?accessToken={accessToken}&livemode={livemode}", accessToken, livemode);
        Data.expectNot400("bindLogin",res,json.toString());
        res.then().assertThat().statusCode(400).
                body("code", equalTo(200021025), "info", equalTo("第三方账号未进行本地绑定")).
                extract().response();
    }



}

