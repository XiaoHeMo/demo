package apiname;

import common.IdCardGenerator;

/**
 * @author yunhua.he
 * @date 2017/11/30
 */
public class Apiname_v1 {

    public static final String oneKeyRegister = "{\n" +
                  " \"pkgName\"  : \"com.changhong.greet\",\n"  +
                  " \"phoneNum\" : \"18077775555\"\n" +
                  "}";

    public static final String createCompany = "{\n" +
            " \"name\" : \"园艺山公司\",\n "+
            "    \"safetyCertificate\": \"value\",\n" +
            "    \"industryType\": 1,\n" +
            "    \"brandNameCh\":\"亚信\",\n" +
            "    \"legalRepresentative\": \"\",\n" +
            "    \"companiesNumber\": 5,\n" +
            "    \"businessAddress\": \"四川省成都市武侯区\",\n" +
            "    \"licenseAgency\": \"工商局\",\n" +
            "    \"registerNumber\": \"253463\",\n" +
            "    \"registerCapital\": \"1000000\",\n" +
            "    \"officialWebsite\": \"www.asfds.com\",\n" +
            "    \"companyNature\": \"国企\",\n" +
            "    \"annualTurnover\": 1000000000,\n" +
            "    \"areaCover\": \"2344\",\n" +
            "    \"fax\": \"010-11111111\",\n" +
            "    \"businessLicenseDuplicate\": \"www.qiniu.com\",\n" +
            "    \"businessScope\": \"互联网\",\n" +
            "    \"idNumber\": \""+ IdCardGenerator.generate()+"\",\n" +
            "    \"idPhotocopy\": \"www.wewesdklo.com\",\n" +
            "    \"taxpayerNumber\": \"12345\",\n" +
            "    \"taxpayerType\": 2,\n" +
            "    \"taxCode\": \"32423\",\n" +
            "    \"taxRegistrationCertificate\": \"www.erwew.com\",\n" +
            "    \"bankName\": \"工商银行\",\n" +
            "    \"bankAccount\": \"2345678\",\n" +
            "    \"bankAccountLicense\": \"www.sewr.org\",\n" +
            "    \"commodityType\": \"value\",\n" +
            "    \"companyType\": 1,\n" +
            "    \"auditState\": 0,\n" +
            "    \"companyState\": 0,\n" +
            "    \"province\": \"四川省\",\n" +
            "    \"city\": \"成都市\",\n" +
            "    \"county\": \"武侯区\",\n" +
            "    \"phone\": \"15500003443\",\n" +
            "    \"email\": \"1245123@163.com\",\n" +
            "    \"location\": \"四川省成都市武侯区\",\n" +
            "    \"authCode\":\"88888888\",\n" +
            "    \"password\":\"123456\"\n" +
            "}";

    public static final String updateCompany = "{\n" +
            "  \"id\": 2643,\n" +
            "  \"companyId\": \"fb62faecfc3c48b0\",\n" +
            "  \"safetyCertificate\": null,\n" +
            "  \"industryType\": 4,\n" +
            "  \"legalRepresentative\": null,\n" +
            "  \"companiesNumber\": 200,\n" +
            "  \"businessAddress\": \"四川省成都市游仙区\",\n" +
            "  \"licenseAgency\": null,\n" +
            "  \"registerNumber\": null,\n" +
            "  \"registerCapital\": null,\n" +
            "  \"accountWarningLine\": null,\n" +
            "  \"rebateType\": null,\n" +
            "  \"officialWebsite\": \"www.changhong.com\",\n" +
            "  \"companyNature\": null,\n" +
            "  \"annualTurnover\": null,\n" +
            "  \"areaCover\": null,\n" +
            "  \"fax\": null,\n" +
            "  \"businessLicenseRegistrationNumber\": null,\n" +
            "  \"businessLicenseDuplicate\": null,\n" +
            "  \"establishDate\": null,\n" +
            "  \"businessScope\": null,\n" +
            "  \"idNumber\": \""+ IdCardGenerator.generate() +"\",\n" +
            "  \"idPhotocopy\": null,\n" +
            "  \"taxpayerNumber\": null,\n" +
            "  \"taxpayerType\": null,\n" +
            "  \"taxCode\": null,\n" +
            "  \"taxRegistrationCertificate\": null,\n" +
            "  \"organizationCode\": null,\n" +
            "  \"electronicCode\": null,\n" +
            "  \"bankName\": \"中国农业银行\",\n" +
            "  \"bankAccount\": null,\n" +
            "  \"bankAccountLicense\": null,\n" +
            "  \"commodityType\": null,\n" +
            "  \"brandNameCh\": \"ChiQ电视\",\n" +
            "  \"brandNameEn\": null,\n" +
            "  \"brandInitials\": null,\n" +
            "  \"logo\": null,\n" +
            "  \"trademarkRegistrationCertificate\": null,\n" +
            "  \"industryCredentials\": null,\n" +
            "  \"inspectionReport\": null,\n" +
            "  \"productionLicense\": null,\n" +
            "  \"companyType\": 2,\n" +
            "  \"auditState\": null\n" +
            "}";

    public static final String createEmployee = "{\n" +
            "\"phone\":\"13099811122\",\n" +
            "\"email\":\"3099811122@qq.com\",\n" +
            "\"employeeNumber\":\"20168161\",\n" +
            "\"idNumber\":\""+ IdCardGenerator.generate() +"\",\n" +
            "\"sex\":\"男\",\n" +
            "\"companyId\":\"wwe12232ewaseweaddds\",\n" +
            "\"name\":\"张三\"\n" +
            "}";

    public static final String registerByPhone = "{\n" +
            "    \"phone\"       :    \"13990109887\",\n" +
            "    \"password\"    :    \"1\",\n" +
            "    \"authCode\"    :    \"88888888\",\n" +
            "    \"pkgName\"     :    \"com.changhong.oil\"\n" +
            "}";

    public static final String updateEmployee = "{\n" +
            "\"phone\":\"13099811122\",\n" +
            "\"email\":\"3099811122@qq.com\",\n" +
            "\"employeeNumber\":\"20168161\",\n" +
            "\"idNumber\":\""+ IdCardGenerator.generate() +"\",\n" +
            "\"sex\":\"男\",\n" +
            "\"company\":\"长虹\",\n" +
            "\"name\":\"张三\"\n" +
            "}";

    public static final String insertEmployeeList ="{\n" +
            "      \n" +
            "      \"name\": \"李四\",\n" +
            "      \"sex\": \"男\",\n" +
            "      \"phone\": \"18080261170\",\n" +
            "      \"email\": \"si.li@changhong.com\",\n" +
            "      \"employeeNumber\": \"20168160\",\n" +
            "      \"idNumber\": \"610324199306099085\",\n" +
            "      \"companyId\":\"f512d2a1335d4d0f\",\n" +
            "      \"company\": \"长虹\"\n" +
            "    }";
}
