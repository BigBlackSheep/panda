package com.gz.design.panda.biz.utils;

import com.alibaba.fastjson.JSON;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignUtil {
    private static final Logger logger = LoggerFactory.getLogger(SignUtil.class);

    //生成密钥对
    public static Map<String, String> generatorKey(String filePath) {
        Map<String, String> map = new HashMap<>();
        try {
            KeyPair pair = SignatureUtils.generateRsaKeyPair(2048);
            map.put("publicKey", Base64.encodeBase64String(pair.getPublic().getEncoded()));
            map.put("privateKey", Base64.encodeBase64String(pair.getPrivate().getEncoded()));
            // 将密钥对写入到文件
            FileWriter pubfw = new FileWriter(filePath + "/publicKey.keystore");
            FileWriter prifw = new FileWriter(filePath + "/privateKey.keystore");
            BufferedWriter pubbw = new BufferedWriter(pubfw);
            BufferedWriter pribw = new BufferedWriter(prifw);
            pubbw.write(map.get("publicKey"));
            pribw.write(map.get("privateKey"));
            pubbw.flush();
            pubbw.close();
            pubfw.close();
            pribw.flush();
            pribw.close();
            prifw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("生成密钥对失败:{}", ex.getMessage());
        }
        return map;
    }

    //生成签名
    public static String sign(String privateStr, Map dataMap) {
        String retval = "";
        try {
            String data = JSON.toJSONString(dataMap);
            PrivateKey privateKey = SignatureUtils.getRsaPkcs8PrivateKey(Base64.decodeBase64(privateStr));
            byte[] sign = SignatureUtils.sign(SignatureAlgorithm.SHA1WithRSA, privateKey, data);
            retval = Base64.encodeBase64String(sign);
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
            logger.error("生成签名失败:{}", ex.getMessage());
        }
        return retval;
    }

    //生成签名
    public static String sign(String privateStr, String data) {
        String retval = "";
        try {
            PrivateKey privateKey = SignatureUtils.getRsaPkcs8PrivateKey(Base64.decodeBase64(privateStr));
            byte[] sign = SignatureUtils.sign(SignatureAlgorithm.SHA1WithRSA, privateKey, data);
            retval = Base64.encodeBase64String(sign);
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
            logger.error("生成签名失败:{}", ex.getMessage());
        }
        return retval;
    }

    //签名验证
    public static boolean verify(String publicKeyStr, String sign, Map<String, String> dataMap) {
        boolean flag = false;
        try {
            String data = JSON.toJSONString(dataMap);
            PublicKey key = SignatureUtils.getRsaX509PublicKey(Base64.decodeBase64(publicKeyStr));
            flag = SignatureUtils.verify(SignatureAlgorithm.SHA1WithRSA, key, data, Base64.decodeBase64(sign));
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("签名验证失败:{}", ex.getMessage());
        }
        return flag;
    }

    public static boolean verify(String publicKeyStr, String sign, String data) {
        boolean flag = false;
        try {
            PublicKey key = SignatureUtils.getRsaX509PublicKey(Base64.decodeBase64(publicKeyStr));
            flag = SignatureUtils.verify(SignatureAlgorithm.SHA1WithRSA, key, data, Base64.decodeBase64(sign));
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("签名验证失败:{}", ex.getMessage());
        }
        return flag;
    }

    /**
     * 从文件中读取秘钥
     * @param FilePath 秘钥文件位置
     * @param fileName 秘钥文件名(带后缀名)
     * @return 秘钥字符串
     * @throws Exception
     */
    public static String loadKeyByFile(String FilePath,String fileName) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new FileReader(FilePath
                    + "/"+fileName));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            throw new Exception(fileName+"秘钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception(fileName+"秘钥输入流为空");
        }
    }


    public static void main(String[] args) {
//        //生成密钥对
        Map<String, String> map = generatorKey("D:\\Code\\key\\prod");
        String privateStr = map.get("privateKey");
        String publicKeyStr = map.get("publicKey");
        //加载密钥对
        //String privateStr = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC9Kl4VgjhmoINsUTNhWYBhBKlj7hA5xFEDV/kVdVTZTt1CTHsoM3miSi7WdzaJUhUVMbcnB0FcBL/D80ZQrre1L1eZbzTVZpQ1NOJbNjuiKXRIvUWaeGWh8KpXKZgWXWmgZn51+anHCWXEXbYMmlxGoMBdlzuAbpmmRdPNHzWLYZMMN6RYt8TZ+UZAzo/2gflO+r2VetIEKFSOkPgYVk1LCOhPtLdTFJHRMkH3JeZ51I8FuBZ69DDgSyI+LQxY9WqHNewzrHinucJSpgpwJrjEA5GfkN7vPA7MokytN9CQv0ubmBwuG240YpIMj7O74ZMDI8nq+amn0c37Pc7VCB3zAgMBAAECggEAOcG33EVYRy13MxlP4MddBUJRHevQFHM2cRjpOn4KnBcSQ23qpImi98RCrMTVZ/Qh78iwLUBv2lKw4nli3Lub57w+uOBVd1rMobo16reSWZ7inFr9534fOeSrfdV7VEjNOWZR8l96UQUzh1M9hHeU+ROX6HuGdV5Pl472bki7JoTq+CRgJGsFQ2Y+E4J+qpM+Cu1NpkwhNLdvyQuxMfMPGdkJuV33KG95WTPmhoBGFoolop9wExzLzyndTY7IJG4OLTq/GYX2gN0yDPBFgO8xiP1VFkeKcb8LiP/6FrSIMjN6kTZ09T4LUFK/h4wXY9w+pfcfLIL0BvxAVKMvGUVdcQKBgQD3tZe4VNkMMCeazWzoLp5QyWm/ZO75Y7YJdPyvYJsF/y08Y0l4qP1lyIunNcSxZFG8BFGP3mZJa32WqifFgXvzHY9CrUKWjjnmkCAytkG/hd1FGnTSbzeqDFheTpF/BRfZG55WjM3AQZVay6EIV2/CcHNv/OrJixpkLeArTkAxVwKBgQDDfynIO+qstXEiyuhyUMGlHdGRUjjL5inS76yQL/ZsNQHGwmIT7bwpPHLr/ViNJRCm4h6DtpC7hgb7h5WKjjc9L3QzNNP3qLcDQ8kHbk0H4FX3hjOoFFO/qXIW0HquSi47GEHziZI+/2PF2OzXP9wL2xqGDATjuINtyE5DpTlKxQKBgQDf1zMRGWtBfkprsvKVJXtemYN7tuPUbOxUw2iLuoqb8qmYBkigRmUlkwHPyeDCRFsvwd9KWLip4/6LJZvm+Elfufh5UkP39PIRjW/rI5NqVLdM+MlUV7g/bAR2xBzqYVnZkQGWSIqtDxa+A4CzHwAY/rDyI+Su14qzuFj4CTAs5QKBgBgoBF+rl6nTmbE05VgKxoLClnygbkgznoNj/iyq4IYqXpfSuQ/BVni58XFbkq+OHuqgEikwS94AB6jY7g961zSGQpN5OZ/mhwauP47dgEMy4yx8trFUIa0TIPfUtvQGUoQW0T9ohlumbwupbEqEp4oHftWFcq04xXSxRYpyRgwJAoGBAJ/dp42apsjl8tpd96qsJl3X4+937pMgnu+eBg3GL1f+ZzX7GKcL60TJuZzbwyRIYAjDkCZjtMYEQlPs2bhtjNHxsoDirKnxJs5ESjmW/q8sBLIgrrnLAwxAIjRX+57LU7F8qRLIWMgYn+wg9r1xAE69utWY5jhfd8gdF/TWoC9Z";
        //String publicKeyStr ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvSpeFYI4ZqCDbFEzYVmAYQSpY+4QOcRRA1f5FXVU2U7dQkx7KDN5okou1nc2iVIVFTG3JwdBXAS/w/NGUK63tS9XmW801WaUNTTiWzY7oil0SL1FmnhlofCqVymYFl1poGZ+dfmpxwllxF22DJpcRqDAXZc7gG6ZpkXTzR81i2GTDDekWLfE2flGQM6P9oH5Tvq9lXrSBChUjpD4GFZNSwjoT7S3UxSR0TJB9yXmedSPBbgWevQw4EsiPi0MWPVqhzXsM6x4p7nCUqYKcCa4xAORn5De7zwOzKJMrTfQkL9Lm5gcLhtuNGKSDI+zu+GTAyPJ6vmpp9HN+z3O1Qgd8wIDAQAB";
      //String privateStr = null;
        try {
            privateStr = loadKeyByFile("D:\\Code\\key\\prod","privateKey.keystore");
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*String publicKeyStr = null;*/
        try {
            publicKeyStr = loadKeyByFile("D:\\Code\\key\\prod","publicKey.keystore");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //待签名数据
        String par = "mchId2201orderId962748903417366921reqInfoct1536229681510curlthttps://www.youyuwo.com/orderAmount30000productId001productName永安借款人意外伤害保险C款产品userId73userName周杰伦号ts1536229681510";
        //生成签名
        String sign = sign(privateStr, par);
        System.out.println("-------->"+sign);
        //验签
        boolean isOk = verify(publicKeyStr, sign, par);
        System.err.println("ok:" + isOk);
    }

}
