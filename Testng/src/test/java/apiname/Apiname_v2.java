package apiname;

/**
 * @author yunhua.he
 * @date 2017/11/7
 * 请求报文
 */
public class Apiname_v2 {
    public static final String verifyAuthCode = "{" +
            " \"code\" : \"12345678\"," +
            " \"type\" : \"phone\", " +
            " \"value\": \"15129009797\"" +
            "}";

    public static final String registerByPhone = "{" +
            " \"phone\"  : \"15129007979\"," +
            " \"password\" : \"12345678\"," +
            " \"authCode\": \"88888888\"," +
            " \"pkgName\": \"changhong\"," +
            " \"macAddress\": \"11:22:33:44:55:66\"," +
            " \"deviceSn\": \"19990909\"," +
            " \"productName\": \"手机\"," +
            " \"productCode\": \"手机机芯123\"" +
            "}";
    public static final String login = "{" +
            " \"username\": \"a123_a123\"," +
            " \"password\": \"12345678\"," +
            " \"macAddress\": \"123.123.123.123\"," +
            " \"deviceSn\": \"1123456\"," +
            " \"tokenCode\": true," +
            " \"productName\": \"机器\"," +
            " \"productCode\": \"机器123\" " +
            "}";

    public static final String addUsername = "{" +
            " \"username\": \"a123_a123\"," +
            " \"openId\": \"19990909\" " +
            "}";

    public static final String bindEmail = "{" +
            " \"authCode\": \"12345678\"," +
            " \"openId\": \"19990909\"" +
            "}";

    public static final String register = "{" +
            " \"username\": \"a123_a123\"," +
            " \"password\": \"12345678\"," +
            " \"sex\": \"男\"," +
            " \"birthday\": \"19990909\"," +
            " \"qq\": \"123456@qq\"," +
            " \"macAddress\": \"11:22:33:44:55:66\"," +
            " \"deviceSn\": \"19990909\"," +
            " \"productName\": \"12234455\"," +
            " \"productCode\": \"长虹机芯\" " +
            "}";

    public static final String addDelivery = "{" +
            " \"lProvince\": \"四川省\"," +
            " \"lCity\": \"绵阳\"," +
            " \"lArea\": \"高新区\"," +
            " \"street\": \"园艺街\"," +
            " \"addrDetail\": \"创新大道一街\"," +
            " \"linkmanName\": \"neinei\"," +
            " \"linkmanTel\": \"13311111111\"," +
            " \"lPostcode\": \"621114\"," +
            " \"regionCodes\": \"0816\"," +
            " \"alias\": \"我就是老大\"," +
            " \"tel\": \"5890190\"," +
            " \"email\": \"qq@qq.com\"," +
            " \"isDefault\": 0" +
            "}";

    public static final String updateDelivery = "{" +
            "\"id\": \"1\"," +
            "\"lProvince\": \"四川省\"," +
            "\"lCity\": \"绵阳\"," +
            "\"lArea\": \"高新区1\"," +
            "\"street\": \"园艺街\"," +
            "\"addrDetail\": \"创新大道三街\"," +
            "\"linkmanName\": \"大neinei\"," +
            "\"linkmanTel\": \"13311111112\"," +
            "\"lPostcode\": \"621114\"," +
            "\"regionCodes\": \"0816\"," +
            "\"alias\": \"我就是老大\"," +
            "\"tel\": \"5890190\"," +
            "\"email\": \"qq@qq.com\"," +
            "\"isDefault\": 0 " +
            "}";

    public static final String deleteDelivery = "{" +
            " \"idList\": \"1\"" +
            "}";

    public static final String createInvoice = "{" +
            "\"invoiceHeader\": \"四川虹微电器股份有限公司\"," +
            "\"invoiceType\": 0," +
            "\"companyName\": \"中国石油\"," +
            "\"taxNo\": \"1234567890\"," +
            "\"registerAddress\": \"园艺街\"," +
            "\"registerTel\": \"13311111112\"," +
            "\"bank\": \"中国大银行\"," +
            "\"bankNo\": \"13311111112\"" +
            "}";

    public static final String updateInvoice = "{" +
            "\"id\": 1," +
            "\"invoiceHeader\": \"东财科技\"," +
            "\"invoiceType\": 2," +
            "\"companyName\": \"航空公司\"," +
            "\"taxNo\": \"1234567890\"," +
            "\"registerAddress\": \"三星路\"," +
            "\"registerTel\": \"13311111112\"," +
            "\"bank\": \"中国大银行\"," +
            "\"bankNo\": \"13311111112\" " +
            "}";

    public static final String deleteInvoice = "{\n" +
            " \"id\" : \"1\" " +
            "}";

    public static final String isBind = "{" +
            " \"connectid\" : \"x7JM199kj3qcxjV4\" \n" +
            "}";

    public  static final String accountBind = "{\n" +
            "  \"username\" : \"cb6914a41f6c65554cde8403b6afb14a\",\n" +
            "  \"password\" : \"cb6914a41f6c65554cde8403b6afb14a\",\n" +
            "  \"access_token\" : \"cb6914a41f6c65554cde8403b6afb14a\",\n" +
            "  \"source\" : \"x7JM199kj3qcxjV4\"\n" +
            "}";

    public static final String findBindAccount = "{\n" +
            " \"openId\" : \"x7JM199kj3qc\",\n" +
            " \"source\" :  \"x7JM199kj3qcxjV4\" \n" +
            "}";

    public static final String toBind = "{\n" +
            "    \"connectid\"   :     \"cb6914a41f6c65554cde8403b6afb14a\",\n" +
            "    \"openid\"   :     \"cb6914a41f6c65554cde8403b6afb14a\",\n" +
            "    \"access_token\"   :     \"cb6914a41f6c65554cde8403b6afb14a\",\n" +
            "    \"source\"    :     \"x7JM199kj3qcxjV4\"\n" +
            "}";

    public static final String bindLogin = "{\n" +
            "    \"connectid\"    :     \"apple\"\n" +
            "}";

    public static final String removeBind = "{\n" +
            "    \"connectid\"    :     \"apple\"\n" +
            "}";
    public static final String registBatch = "{\n" +
            "   \"account\"  : \"18212341234\",\n" +
            "   \"pkgName\":\"com.changhong.pkgName\",\n" +
            "   \"realName\":\"nashou\",\n" +
            "   \"nickName\":\"muyou\",\n" +
            "   \"iconURL\":\"头像url，上传到七牛那个url，选填\",\n" +
            "   \"province\":\"福建省\",\n" +
            "   \"city\":\"福州市\",\n" +
            "   \"county\":\"乡镇，选填\",\n" +
            "   \"location\":\"地址，选填\",\n" +
            "   \"macAddress\":\"12:12:12\",\n" +
            "   \"deviceSn\":\"设备sn地址，选填\",\n" +
            "   \"deviceType\":\"1\",\n" +
            "   \"fingerPrint\":\"指纹特征，选填\",\n" +
            "   \"fingerUrl\":\"指纹url，选填\",\n" +
            "   \"interests\":\"兴趣爱好，选填\",\n" +
            "   \"university\":\"毕业院校，选填\",\n" +
            "   \"major\":\"专业，选填\",\n" +
            "   \"personalInfo\":\"个人介绍，选填\",\n" +
            "   \"birthday\":\"2019-11-09\",\n" +
            "   \"bloodType\":\"A\",\n" +
            "   \"sex\":\"男\",\n" +
            "   \"qq\":\"1244\",\n" +
            "   \"alipay\":\"支付宝，选填\",\n" +
            "   \"sinaweibo\":\"微博，选填\",\n" +
            "   \"taobao\":\"淘宝号，选填\",\n" +
            "   \"weixin\":\"微信号，选填\"\n" +
            "}";

    public static final String resetPwd = "{\n" +
            "  \"phone\"  : \"18212341234\", \n" +
            "  \"password\"  : \" 11002299\" \n"  +
            "}";

    public static final String updateUserInfoByUid="{\n" +
            "    \"uid\"    :    \"21cfeff6de1d4400\",\n" +
            "    \"nickName\"   :   \"guagua\"\n" +
            "    \n" +
            "}";

    public static final String getAllUsersInfo="{\n" +
            "  \"phone\": \"18112341234\"\n" +
            "}";
}
