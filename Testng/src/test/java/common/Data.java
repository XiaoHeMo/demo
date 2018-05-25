package common;

import com.jayway.restassured.response.Response;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import static com.jayway.restassured.RestAssured.given;

/**
 * @author yunhua.he
 * @date 2017/11/6
 */
public class Data {
    private static String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");
    public static String accessToken = "ZTMxYzNkODcxZDI3MTMyYQ==:JGeNTtfbfExnMHze5LZLBlVjJJs=:eyJhcGlzIjpbImxvZ2luIiwicmVnaXN0ZXJCeUVtYWlsIiwicmVnaXN0ZXJCeVBob25lIiwicmVnaXN0ZXJCeUVtYWlsIiwicmVnaXN0ZXIiLCJhdXRoQ29kZSIsInZlcmlmeUF1dGhDb2RlIiwibG9naW4iLCJsb2dpbldlY2hhdCIsInJlc2V0UHdkQnlQaG9uZSIsInJlc2V0UHdkQnlFbWFpbCIsImlzRXhpc3RlZCIsImJic1JlZ2lzdGVyQnlQaG9uZSIsImxvZ291dCIsInJlZ2lzdGVyQnllbWFpbCIsImxvZ2luRnJvbUJCUyIsImNoZWNrVXNlciIsInN5bmNSZWdpc3RlciIsImJic1JlZ2lzdGVyQnlFbWFpbCIsImJic1JlZ2lzdGVyQnlFbWFpbCIsImNoZWNrVXNlciIsImdldENvdW50c0J5QXBwSWQiLCJsb2dpbkJ5T3BlbklkIiwiY3JlYXRlIiwiZ2V0VWlkQnlPcGVuSWQiLCJmaW5kIiwiY2hlY2siLCJnZXRDb21wYW55TGlzdCIsImdldERlbGl2ZXJ5QWRkckxpc3QiLCJnZXRJbnZvaWNlIiwicmVmcmVzaFRva2VuIiwicmVtb3ZlIiwiZnV6enlRdWVyeVVzZXJEZXRhaWwiLCJnZXRCeUZ1enp5VXNlck5hbWUiLCJiYXRjaFJlZ2lzdGVyIiwiZmluZEJpbmRBY2NvdW50IiwicmVtb3ZlQmluZCIsImJpbmRsb2dpbiIsImZpbmRVc2VyQnlEZXZpY2UiLCJyZWZyZXNoVG9rZW4iLCJnZXRVc2VyRGV0aWFsQnlOYW1lIiwiaXNiaW5kZWQiLCJhY2NvdW50YmluZCIsInRvYmluZCIsImZpbmRCaW5kQWNjb3VudCIsImdldFVzZXJJbmZvQnlPcGVuSWRzIiwiZ2V0VXNlckluZm9MaXN0IiwiaXNFbXBsb3llZSIsImdldFVzZXJEZXRpYWxCeVVpZCIsImdldEluY3JlbWVudHMiLCJnZXRJbmNyZW1lbnRzQnlUaW1lQnVja2V0IiwiZ2V0QWxsQXBwTmFtZSIsImdldEFsbFVzZXJzSW5mbyIsInJlZ2lzdEJhdGNoIiwiZ2V0RGVsaXZlcnlBZGRyTGlzdEJ5VWlkIiwiYWRkRGVsaXZlcnlBZGRyQnlVaWQiLCJ1cGRhdGVEZWxpdmVyeUFkZHJCeVVpZCIsImRlbGV0ZURlbGl2ZXJ5QWRkckJ5VWlkIiwic2V0RGVmYXVsdEJ5VWlkIiwiZ2V0Q29tcGFueUluZm9zIiwiZ2V0VXNlcmF1dGhlbkluZm8iLCJ1cGRhdGVBdXRoZW5zdGF0dXMiLCJ1cGRhdGVBY2NvdW50c3RhdHVzIiwidXBkYXRlVXNlckluZm9CeVVpZCIsImdldENvbXBhbnlJbmZvQnlVaWQiLCJnZXRDb3VudHNzbyIsImRhaWx5U3RhdGlzdGljcyIsImNvdW50VXNlckJ5QXBwSWQiLCJmaW5kVXNlclJlZ2lzdEJ5UGFnZSIsInJlc2V0UHdkIiwibG9naW5CeVNtcyIsImNyZWF0ZSIsInJlZ2lzdGVyQnlQaG9uZSIsImZpbmQiLCJmaW5kYnlDb21wYW55SWQiLCJjaGVjayIsImNoZWNrQnlPcGVuSWQiLCJ1cGRhdGUiLCJnZXRDb21wYW55TGlzdCIsInNlYXJjaENvbXBhbnlMaXN0IiwiZmluZEFsbCIsImZpbmRCeUNhdGFsb2ciLCJmaW5kQnlDYXRhbG9nQW5kS2V5IiwiZ2V0TGlzdCIsImdldEFsbEVtcGxveWVlc0J5Q29tcGFueSIsImNyZWF0ZUVtcGxveWVlIiwiZ2V0RW1wbG95ZWUiLCJzZWFyY2hFbXBsb3llZSIsImRlbGV0ZUVtcGxveWVlIiwiZGVsZXRlRW1wbG95ZWVzIiwiZGVsZXRlRW1wbG95ZWVCeVBob25lIiwiZGVsZXRlRW1wbG95ZWVzQnlQaG9uZXMiLCJ1cGRhdGVFbXBsb3llZSIsImluc2VydEVtcGxveWVlTGlzdCIsImZpbmRVc2VyQnlPcGVuSWQiLCJ1cGRhdGVVc2VyQnlPcGVuSWQiLCJ1cGRhdGVBdWRpdFN0YXRlIiwidXBkYXRlQ29tcGFueVN0YXRlIiwiZmluZEFsbENvbXBhbnkiXSwic2NvcGUiOiJVU0VSIiwiZXhwaXJlZFRpbWUiOjE1MTQ1NjMyMDAwMDB9";
    public static String url_v2 = "http://127.0.0.1:8089/userservice/v2/user";
    public static String url_v1 = "http://127.0.0.1:8089/userservice";
    public static String livemode = "0";

    public static String createData(Integer code) {
        Thread current = Thread.currentThread();
        long threadId = current.getId();
        String value = null;
        String is = "true";
        while("true".equals(is)) {
            switch (code) {
                case 0:
                    String username = "N_" + threadId + getRandomString(5);
                    value = String.valueOf(username);
                    break;
                case 1:
                    value = getTel();
                    break;
                case 2:
                    String email = "E_" + threadId + getRandomString(6) + "@test.com";
                    value = String.valueOf(email);
                    break;
                case 3:
                    String mac = threadId + getRandomString(1) + ":" + getRandomString(2) + ":" + getRandomString(2) + ":" + getRandomString(2) + ":" + getRandomString(2) + ":" + getRandomString(2);
                    value = String.valueOf(mac);
                    break;
                case 4:
                    String deviceSn = "sn_" + threadId + getRandomString(4);
                    value = String.valueOf(deviceSn);
                    break;
                default:
                    System.out.println("code=0：用户名，code=1：手机号，code=2：邮箱。其他：直接判断是否存在");
                    break;
            }
            Response res = given().when().get(url_v2 + "/v2/user/isExisted?accessToken={accessToken}&value={value}&livemode={livemode}", accessToken, value, livemode);
            is = res.toString(); //若用户是存在的，则继续循环，生成又一个账号，直到返回false，停止循环。
        }
        return value;
    }

    private static String getRandomString(int length) {
        String base = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    private static String getTel() {
        int index = getNum(0,telFirst.length-1);
        String first = telFirst[index];
        String second = String.valueOf(getNum(1, 888)+10000).substring(1);
        String third = String.valueOf(getNum(1,9100)+10000).substring(1);
        return first + second + third;
    }

    private static int getNum(int start, int end) {
        return (int)(Math.random()*(end-start+1)+start);
    }

    public static void expectNot400(String requestName,Response res,String inputData) {
        int code = res.statusCode();
        if (code != 200 && code != 201) {
            System.out.println("requestName:" + requestName);
            System.out.println("inputData: " + inputData);
            System.out.println("errCode: " + code);
            res.print();
        }
    }

    public static void expect400(String requestName,Response res,String inputData) {
        int code = res.statusCode();
        if (code != 400) {
            System.out.println("requestname:"+requestName);
            System.out.println("inputdata:"+inputData);
            System.out.println("statuscode:"+res.statusCode());
            res.print();
        }
    }

    public static String getDay(String day) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        Calendar cal2 = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(cal2.getTime());
        if ("yesterday".equals(day)) {
            return yesterday;
        }else {
            return today;
        }
    }

}
