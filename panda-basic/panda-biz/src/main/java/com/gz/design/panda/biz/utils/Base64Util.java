//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.gz.design.panda.biz.utils;

import com.caiyi.financial.nirvana.cash.loan.common.constants.Constants;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64Util {
    public Base64Util() {
    }


    public static String base64Encoder(String reqinfoJson) {
        final Base64.Encoder encoder = Base64.getEncoder();
        byte[] textByte = new byte[0];
        try {
            textByte = reqinfoJson.getBytes(Constants.UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//编码
        String encodedText = encoder.encodeToString(textByte);
        encodedText = encodedText.replaceAll("\r\n", "");
        return encodedText;
    }

    public static String getBASE64(String s) {
        if(s == null) {
            return null;
        } else {
            try {
                return getBASE64(s.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException var2) {
                var2.printStackTrace();
                return null;
            }
        }
    }

    public static String getBASE64(byte[] b) {
        byte[] rb = org.apache.commons.codec.binary.Base64.encodeBase64(b);
        if(rb == null) {
            return null;
        } else {
            try {
                return new String(rb, "UTF-8");
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
                return null;
            }
        }
    }

    public static String getFromBASE64(String s) {
        if(s == null) {
            return null;
        } else {
            try {
                byte[] e = getBytesBASE64(s);
                return e == null?null:new String(e, "UTF-8");
            } catch (UnsupportedEncodingException var2) {
                var2.printStackTrace();
                return null;
            }
        }
    }

    public static byte[] getBytesBASE64(String s) {
        if(s == null) {
            return null;
        } else {
            try {
                byte[] e = org.apache.commons.codec.binary.Base64.decodeBase64(s.getBytes("UTF-8"));
                return e;
            } catch (Exception var2) {
                return null;
            }
        }
    }
}
