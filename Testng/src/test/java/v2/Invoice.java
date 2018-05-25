package v2;

import apiname.Apiname_v2;
import com.alibaba.fastjson.JSONObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import common.Data;
import org.junit.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static v2.MixOperate.get_invoice;
import static v2.MixOperate.register;

/**
 * @author yunhua.he
 * @date 2017/11/6
 */
public class Invoice {

    private String username = "dd_";
    private String password = "12345678";
    private String livemode = "0";
    private String openId = null;
    private String token = null;
    private int id;

    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = Data.url_v2;
    }

    @Test
    public void register_login() {
        //注册
        username = Data.createData(0);
        Response res = register(username, password);
        Data.expectNot400("register_login", res, username + "<<>>" + password);
        res.then().assertThat().statusCode(201).extract().response();
        //登录
        Response res1 = MixOperate.login(username, password);
        Data.expectNot400("register_login", res, username + "<<>>" + password);
        res1.then().assertThat().statusCode(200).body("token", notNullValue(), "openId", notNullValue()).extract().response();
        token = res1.path("token");
        openId = res1.path("openId");
    }

    @Test(dependsOnMethods = "register_login")
    public void createInvoice() {
        JSONObject json = JSONObject.parseObject(Apiname_v2.createInvoice);
        Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/createInvoice?token={token}&openId={openId}&livemode={livemode}",token,openId,livemode);
        Data.expectNot400("createInvoice", res, json.toString());
        res.then().assertThat().statusCode(201).
                body("code",is(0), "info",equalTo("成功"), "des",equalTo("成功")).
                extract().response();
        //再创建一条
        json.put("invoiceHeader","九州集团");
        json.put("invoiceType",1);
        json.put("companyName","九州航空公司");
        json.put("bank","交通银行");
        Response res1 = given().contentType(ContentType.JSON).body(json.toString()).when().post("/createInvoice?token={token}&openId={openId}&livemode={livemode}",token,openId,livemode);
        Data.expectNot400("createInvoice", res1, json.toString());
        res.then().assertThat().statusCode(201).
                body("code",is(0), "info",equalTo("成功"), "des",equalTo("成功")).
                extract().response();
        String result = res1.andReturn().asString();
        assertThat(result, matchesJsonSchemaInClasspath("json_schema_v2/invoice.json"));
    }

    @Test(dependsOnMethods = "createInvoice")
    public void getInvoice() {
        Response res = get_invoice(token ,openId);
        Data.expectNot400("getInvoice", res, token+"<>"+openId);
        res.then().assertThat().statusCode(200).
                body("list[0].invoiceHeader", equalTo("九州集团"),
                        "list[0].invoiceType", equalTo("1"),
                        "list[0].companyName", equalTo("九州航空公司"),
                        "list[0].bank", equalTo("交通银行"),
                        "list[1].invoiceHeader", equalTo("四川虹微电器股份有限公司"),
                        "list[1].invoiceType",  equalTo("0"),
                        "list[1].companyName", equalTo("中国石油"),
                        "list[1].bank", equalTo("中国大银行")).
                extract().response();
        id = res.path("list[0].id");
        String result = res.andReturn().asString();
        assertThat(result, matchesJsonSchemaInClasspath("json_schema_v2/getInvoice.json"));
    }

    @Test(dependsOnMethods = "getInvoice")
    public void updateInvoice() {
        JSONObject json = JSONObject.parseObject(Apiname_v2.updateInvoice);
        json.put("id", id);
        Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/updateInvoice?token={token}&livemode={livemode}",token,livemode);
        Data.expectNot400("updateInvoice",res,json.toString());
        res.then().assertThat().statusCode(200).
                body( "code",is(0), "info",equalTo("成功"), "des",equalTo("成功")).extract().response();
        String result = res.andReturn().asString();
        assertThat(result, matchesJsonSchemaInClasspath("json_schema_v2/invoice.json"));
        //修改非自己id
        JSONObject json1 = JSONObject.parseObject(Apiname_v2.updateInvoice);
        json1.put("id",1);
        Response res1 = given().contentType(ContentType.JSON).body(json1.toString()).when().post("/updateInvoice?token={token}&livemode={livemode}",token,livemode);
        Data.expect400("updateInvoice", res1, json1.toString());
        res1.then().assertThat().statusCode(400).body("code", equalTo(2000004));
    }

    @Test(dependsOnMethods = "updateInvoice")
    public void getInvoice_afterUpdate() {
        Response res = get_invoice(token ,openId);
        Data.expectNot400("getInvoice_afterUpdate", res, token+"<>"+openId);
        res.then().assertThat().statusCode(200).
                body("list[0].invoiceHeader", equalTo("东财科技"),
                        "list[0].invoiceType", equalTo("2"),
                        "list[0].companyName", equalTo("航空公司"),
                        "list[0].registerAddress", equalTo("三星路")).
                extract().response();
        String result = res.andReturn().asString();
        assertThat(result, matchesJsonSchemaInClasspath("json_schema_v2/getInvoice.json"));
    }

    @Test(dependsOnMethods = "getInvoice_afterUpdate")
    public void getInvoiceById() {
        Response res = given().when().get("/getInvoiceById?token={token}&livemode={livemode}&id={id}",token,livemode,id);
        Data.expectNot400("getInvoiceById", res, token +"<<>>"+id);
        res.then().assertThat().statusCode(200).
                body("data.id", equalTo(id),
                        "data.invoiceHeader", equalTo("东财科技"),
                        "data.invoiceType", equalTo("2"),
                        "data.companyName",  equalTo("航空公司"),
                        "data.registerAddress", equalTo("三星路")).
                extract().response();
        String result = res.andReturn().asString();
        assertThat(result, matchesJsonSchemaInClasspath("json_schema_v2/getInvoiceId.json"));
    }

    @Test(dependsOnMethods = "getInvoiceById")
    public void deleteInvoice() {
        JSONObject json = JSONObject.parseObject(Apiname_v2.deleteInvoice);
        json.put("id", 1);
        Response res = given().contentType(ContentType.JSON).body(json.toString()).when().post("/deleteInvoice?token={token}&livemode={livemode}",token,livemode);
        Data.expect400("deleteInvoice", res, json.toString());
        res.then().assertThat().statusCode(400).body("code",equalTo(2000004));
        //删除自己的发票
        JSONObject json1 = JSONObject.parseObject(Apiname_v2.deleteInvoice);
        json1.put("id",id);
        Response res1 = given().contentType(ContentType.JSON).body(json1.toString()).when().post("/deleteInvoice?token={token}&livemode={livemode}",token,livemode);
        Data.expectNot400("deleteInvoice",res1,json1.toString());
        res1.then().assertThat().statusCode(200).
                body( "code",is(0), "info",equalTo("成功"), "des",equalTo("成功")).
                extract().response();
        String response = res1.andReturn().asString();
        Assert.assertThat(response, matchesJsonSchemaInClasspath("json_schema_v2/invoice.json"));
    }

    @Test(dependsOnMethods = "deleteInvoice")
    public void getInvoice_afterDel() {
        Response res = given().contentType(ContentType.JSON).when().get("/getInvoiceById?token={token}&livemode={livemode}&id={id}",token,livemode,id);
        Data.expectNot400("getInvoice_del",res,token);
        res.then().assertThat().statusCode(200). body(equalTo("{\"status\":true}"));
        Response res1 = get_invoice(token ,openId);
        res1.then().assertThat().statusCode(200).body("list[0].id", not(id));
    }
}
