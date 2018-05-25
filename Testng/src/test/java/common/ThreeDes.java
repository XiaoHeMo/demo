package common;

import com.alibaba.fastjson.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author yunhua.he
 * @date 2017/11/30
 */
public class ThreeDes {
    private static final byte[] Key = "chcloudy".getBytes(); // 8字节的加密密匙

    private static final String Algorithm = "DES"; // 加密算法：DES,DESSede,Blowfish

    /**
     * 加密字符串,返回加密后的字符串
     *
     * @param src 待加密字符串
     */
    public static String encryptCode(String src) {
        if (src == null) {
            return null;
        }
        try {
            src = URLEncoder.encode(src, "UTF-8");
            SecretKey deskey = new SecretKeySpec(Key, Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            src = byte2Hex(c1.doFinal(src.getBytes()));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return src;
    }

    /**
     * 加密字符串,返回加密后的字符串 用于javascript 到java端传输
     *
     * @param src 待加密字符串
     */
    public static String encryptJSCode(String src) {
        if (src == null)
            return null;
        try {
            src = URLEncoder.encode(src, "UTF-8") + "%20%20%20%20%20%20%20%20";
            SecretKey deskey = new SecretKeySpec(Key, Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            src = byte2Hex(c1.doFinal(src.getBytes()));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return src;
    }

    /**
     * 解密从js传过来的des加密后的密文，此解密只用于js加密后的密文
     *
     * @param @param  src
     * @param @param  type
     * @param @return 设定文件CW
     * @return String 返回类型
     * @throws
     * @date 2014年12月2日 上午10:58:06
     * @Title: decryptCode
     * @Description: TODO
     */
    public static String decryptJSCode(String src) {
        if (src == null)
            return null;
        if (src.length() < 16)
            return src;
        String strSuffix = getRandomSuffix();
        String cipherText = src.toUpperCase().substring(0, src.length() - 16) + strSuffix;
        try {
            SecretKey deskey = new SecretKeySpec(Key, Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            byte[] b = hex2Byte(cipherText);
            if (b == null) {
                return null;
            }
            byte[] bytes = c1.doFinal(b);
            cipherText = new String(bytes);
            cipherText = cipherText.substring(0, cipherText.lastIndexOf("%7D") + 3);
            cipherText = URLDecoder.decode(cipherText, "UTF-8");

        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    /**
     * 解密字符串,返回解密后的原始字符串 字符串为NULL或者空 直接返回NULL
     *
     * @param src 待解密字符串
     */
    public static String decryptCode(String src) {
        if (src == null)
            return null;
        try {
            SecretKey deskey = new SecretKeySpec(Key, Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            byte[] b = hex2Byte(src);
            if (b == null) {
                return null;
            }
            byte[] bytes = c1.doFinal(b);
            src = URLDecoder.decode(new String(bytes), "UTF-8");

        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return src;
    }

    // byte2Hex
    private static String byte2Hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    // hex2Byte
    private static byte[] hex2Byte(String str) {
        if (str == null)
            return null;
        str = str.trim();
        int len = str.length();
        if (len == 0 || len % 2 == 1)
            return null;
        byte[] b = new byte[len / 2];
        try {
            for (int i = 0; i < str.length(); i += 2) {
                b[i / 2] = (byte) Integer.decode("0x" + str.substring(i, i + 2)).intValue();
            }
            return b;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getRandomSuffix() {
        String src = encryptCode("");
        return src.substring(src.length() - 16);
    }

    public static void main(String[] args) {
        JSONObject json = new JSONObject();
        json.put("pkgName", "com.changhong.cworld");
        json.put("userName", "15904897164");
        String enc = encryptCode(json.toString());
        String dec = decryptCode(enc);
        System.out.println("enc = " + enc);
        System.out.println("dec = " + dec);

        String JSenc = encryptJSCode(json.toString());
        String JSdec = decryptCode(JSenc);
        System.out.println("JSenc = " + JSenc);
        System.out.println("JSdec = " + JSdec);
    }
}
