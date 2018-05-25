package v2;

import apiname.Apiname_v2;
import com.alibaba.fastjson.JSONObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import common.Data;
import org.testng.annotations.BeforeMethod;

import static com.jayway.restassured.RestAssured.given;

/**
 * @author yunhua.he
 * @date 2017/11/6
 */
public class MixOperate {

    public static String accessToken = Data.accessToken;
    public static String livemode = "0";
    public static int pageSize = 100;
    public static int pageNum = 1;

    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = Data.url_v2;
    }

    public static Response register(String username, String password) {
        JSONObject json = JSONObject.parseObject(Apiname_v2.register);
        json.put("username", username);
        json.put("password", password);
        Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/register?accessToken={accesstoken}&livemode={livemode}", accessToken, livemode);
        return res;
    }

    public static Response login(String username, String password) {
        JSONObject json = JSONObject.parseObject(Apiname_v2.login);
        json.put("username", username);
        json.put("password", password);
        Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/login?accessToken={accesstoken}&livemode={livemode}", accessToken, livemode);
        return res;
    }

    public static Response get_invoice(String token, String openId) {
        Response res = given().contentType(ContentType.JSON).when().get("/getInvoice?pageSize={pageSize}&pageNum={pageNum}&token={token}&openId={openId}&livemode={livemode}",pageSize,pageNum,token,openId,livemode);
        return res;
    }
}