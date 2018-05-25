package v1;

import apiname.Apiname_v1;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import common.Data;
import common.IdCardGenerator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author yunhua.he
 * @date 2018/3/1
 */

/**
 * 数组的三种定义方法
 *
 * 1.数组类型[] 数组名=new 数组类型[数组长度];
 * 2.数组类型[] 数组名={数组0,数组1,数组2,数组3,....};
 * 3.数组类型[] 数组名=new 数组类型[]{数组0,数组1,数组2,...};
 * */
public class Employee {

    private static String companyId = "fb62faecfc3c48b0";
    private static String phone = null;
    private static String email = null;
    private static String name = null;
    private static String accessToken = Data.accessToken;
    private static String livemode = Data.livemode;
    private static String password = "qq12345678";
    private static String authCode = "000000";
    private static String token = null;
    private static String openId =null;
    private static String idNumber = null;
    private static String id = null;
    private static String [] phoneArray = new String[5];
    private static String [] emailArray = {"", "", "", "", ""};
    private static String [] nameArray = new String[]{"", "", "", "", ""};
    private static List<String> idNumberList = new ArrayList<>();
    private static String ids = null;

    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = "http://tuapi.chiq-cloud.com/ent/v1";
    }

    @Test
    public void createEmployee() {
        phone = Data.createData(1);
        email = Data.createData(2);
        name = "园园"+Data.createData(0);
        JSONObject json = JSON.parseObject(Apiname_v1.createEmployee);
        json.put("phone", phone);
        json.put("email", email);
        json.put("name", name);
        json.put("companyId", companyId);
        Response res = given().contentType(ContentType.JSON).when().post("/employee/createEmployee?accessToken={0}&livemode={1}", accessToken, livemode);
        Data.expectNot400("createEmployee", res, json.toString());
        res.then().assertThat().statusCode(200).extract().response();
    }

    //企业员工去注册普通用户
    @Test(dependsOnMethods = "createEmployee")
    public void registerByPhone() {
        JSONObject json = JSON.parseObject(Apiname_v1.registerByPhone);
        json.put("phone", phone);
        json.put("password", password);
        json.put("authCode", authCode);
        Response res = given().contentType(ContentType.JSON).body(json).when().post("/company/user/registerByPhone?accessToken={0}&livemode={1}", accessToken,livemode);
        Data.expectNot400("registerByPhone", res, json.toString());
        res.then().assertThat().statusCode(200).body("openId", notNullValue(), "token", notNullValue()).extract().response();
        token = res.path("token");
        openId = res.path("openId");
        idNumber = json.getString("idNumber");
    }

    @Test(dependsOnMethods = "registerByPhone")
    public void searchEmployee() {
        Response res = given().when().get("/employee/searchEmployee?accessToken={0}&livemode={1}&key={2}&companyId={3}", accessToken, livemode, phone, companyId);
        Data.expectNot400("searchEmployee", res, companyId);
        res.then().assertThat().statusCode(200).
                body("[0].name", equalTo(name), "[0].companyId", equalTo(companyId),
                        "[0].phone", equalTo(phone), "[0].email", equalTo(email),
                        "[0].idNumber", equalTo(idNumber)).
                extract().response();
        id = res.path("[0].id").toString();
    }

    @Test(dependsOnMethods = "searchEmployee")
    public void updateEmployee() {
        JSONObject json = JSON.parseObject(Apiname_v1.updateEmployee);
        json.put("phone",phone);
        json.put("authCode", authCode);
        name = name+"up";
        json.put("name", name);
        json.put("sex", "女");
        Response res = given().contentType(ContentType.JSON).body(json).when().post("/employee/updateEmployee?accessToken={0}&livemode=0", accessToken);
        Data.expectNot400("updateEmployee", res, json.toString());
        res.then().assertThat().statusCode(200).extract().response();
    }

    @Test(dependsOnMethods = "updateEmployee")
    public void getEmployee() {
        Response res = given().when().get("/employee/getEmployee?accessToken={0}&livemode={1}&id={2}", accessToken, livemode, id);
        Data.expectNot400("getEmployee", res, id);
        res.then().assertThat().statusCode(200).
                body("phone", equalTo(phone), "email", equalTo(email),
                "name", equalTo(name), "sex", equalTo("女")).
                extract().response();
    }

    @Test(dependsOnMethods = "getEmployee")
    public void getUserByOpenId() {
        Response res = given().when().get("/company/user/findUserByOpenId?token={0}&livemode=0&openId={1}", token, openId);
        Data.expectNot400("getUserByOpenId", res, openId);
        res.then().assertThat().statusCode(200).
                body("phone", equalTo(phone), "email", equalTo(email),
                        "name", equalTo(name), "sex", equalTo("女")).
                extract().response();
    }

    @Test(dependsOnMethods = "getUserByOpenId")
    public void deleteEmployee() {
        Response res = given().when().get("/employee/deleteEmployee?accessToken={0}&livemode={1}&id={2}", accessToken, livemode, id);
        Data.expectNot400("deleteEmployee", res, id);
        res.then().assertThat().statusCode(200).extract().response();
    }

    @Test(dependsOnMethods = "deleteEmployee")
    public void insertEmployeeList() {
        JSONArray array = new JSONArray();
        for (int i = 0; i < 5; i++) {
            JSONObject json = JSON.parseObject(Apiname_v1.insertEmployeeList);
            phoneArray[i] = Data.createData(1);
            emailArray[i] = Data.createData(2);
            nameArray[i] = "袁"+Data.createData(3);
            idNumberList.add(i, IdCardGenerator.generate());
            json.put("phone",  phoneArray[i]);
            json.put("email", emailArray[i]);
            json.put("name", nameArray[i]);
            json.put("idNumber", idNumberList.get(i));
            json.put("companyId", companyId);
            array.add(json);
        }
        Response res = given().contentType(ContentType.JSON).body(array).when().post("/employee/insertEmployeeList?livemode=1&accessToken={0}", accessToken);
        Data.expectNot400("insertEmployeeList", res, array.toString());
        res.then().assertThat().statusCode(200).extract().response();
    }

    @Test(dependsOnMethods = "insertEmployeeList")
    public void getAllEmployees() {
        Response res = given().when().get("employee/getAllEmployees?accessToken={0}&pageSize=10&pageNo=1&livemode=0", accessToken);
        Data.expectNot400("getAllEmployees", res, "无");
        res.then().assertThat().statusCode(200).
                body("total", equalTo(5), "hasNextPage", equalTo(false), "list[0].phone", notNullValue(),
                        "list[0].email", notNullValue(), "list[0].name",notNullValue(),
                        "list[0].idNumber", notNullValue(), "list[0].companyId", equalTo(companyId)).
                extract().response();
        ids = res.path("list[0].id").toString() + res.path("list[1].id").toString();
    }

    @Test(dependsOnMethods = "getAllEmployees")
    public void deleteEmployees() {
        Response res = given().when().get("/employee/deleteEmployees?accessToken={0}&livemode=0&ids={1}", accessToken, ids);
        Data.expectNot400("deleteEmployees", res, ids);
        res.then().assertThat().statusCode(200).extract().response();
    }

    @Test(dependsOnMethods = "deleteEmployees")
    public void deleteEmployeeByPhones() {
        String phones = "";
        for(int i=2; i<4; i++) {
            if(i ==2) {phones = phoneArray[i];}
             else {phone = ","+phoneArray[i];}
        }
       Response res = given().when().get("/employee/deleteEmployeesByPhones?phones={0}&livemode=1&accessToken={2}", phones, accessToken);
       Data.expectNot400("deleteEmployeeByPhones", res, phones);
       res.then().assertThat().statusCode(200).extract().response();
    }
}
