package com.gz.design.panda.biz.utils;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;


public class SignKuBao {

    public static final String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC9Kl4VgjhmoINsUTNhWYBhBKlj7hA5xFEDV/kVdVTZTt1CTHsoM3miSi7WdzaJUhUVMbcnB0FcBL/D80ZQrre1L1eZbzTVZpQ1NOJbNjuiKXRIvUWaeGWh8KpXKZgWXWmgZn51+anHCWXEXbYMmlxGoMBdlzuAbpmmRdPNHzWLYZMMN6RYt8TZ+UZAzo/2gflO+r2VetIEKFSOkPgYVk1LCOhPtLdTFJHRMkH3JeZ51I8FuBZ69DDgSyI+LQxY9WqHNewzrHinucJSpgpwJrjEA5GfkN7vPA7MokytN9CQv0ubmBwuG240YpIMj7O74ZMDI8nq+amn0c37Pc7VCB3zAgMBAAECggEAOcG33EVYRy13MxlP4MddBUJRHevQFHM2cRjpOn4KnBcSQ23qpImi98RCrMTVZ/Qh78iwLUBv2lKw4nli3Lub57w+uOBVd1rMobo16reSWZ7inFr9534fOeSrfdV7VEjNOWZR8l96UQUzh1M9hHeU+ROX6HuGdV5Pl472bki7JoTq+CRgJGsFQ2Y+E4J+qpM+Cu1NpkwhNLdvyQuxMfMPGdkJuV33KG95WTPmhoBGFoolop9wExzLzyndTY7IJG4OLTq/GYX2gN0yDPBFgO8xiP1VFkeKcb8LiP/6FrSIMjN6kTZ09T4LUFK/h4wXY9w+pfcfLIL0BvxAVKMvGUVdcQKBgQD3tZe4VNkMMCeazWzoLp5QyWm/ZO75Y7YJdPyvYJsF/y08Y0l4qP1lyIunNcSxZFG8BFGP3mZJa32WqifFgXvzHY9CrUKWjjnmkCAytkG/hd1FGnTSbzeqDFheTpF/BRfZG55WjM3AQZVay6EIV2/CcHNv/OrJixpkLeArTkAxVwKBgQDDfynIO+qstXEiyuhyUMGlHdGRUjjL5inS76yQL/ZsNQHGwmIT7bwpPHLr/ViNJRCm4h6DtpC7hgb7h5WKjjc9L3QzNNP3qLcDQ8kHbk0H4FX3hjOoFFO/qXIW0HquSi47GEHziZI+/2PF2OzXP9wL2xqGDATjuINtyE5DpTlKxQKBgQDf1zMRGWtBfkprsvKVJXtemYN7tuPUbOxUw2iLuoqb8qmYBkigRmUlkwHPyeDCRFsvwd9KWLip4/6LJZvm+Elfufh5UkP39PIRjW/rI5NqVLdM+MlUV7g/bAR2xBzqYVnZkQGWSIqtDxa+A4CzHwAY/rDyI+Su14qzuFj4CTAs5QKBgBgoBF+rl6nTmbE05VgKxoLClnygbkgznoNj/iyq4IYqXpfSuQ/BVni58XFbkq+OHuqgEikwS94AB6jY7g961zSGQpN5OZ/mhwauP47dgEMy4yx8trFUIa0TIPfUtvQGUoQW0T9ohlumbwupbEqEp4oHftWFcq04xXSxRYpyRgwJAoGBAJ/dp42apsjl8tpd96qsJl3X4+937pMgnu+eBg3GL1f+ZzX7GKcL60TJuZzbwyRIYAjDkCZjtMYEQlPs2bhtjNHxsoDirKnxJs5ESjmW/q8sBLIgrrnLAwxAIjRX+57LU7F8qRLIWMgYn+wg9r1xAE69utWY5jhfd8gdF/TWoC9Z";
    private static final String key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvSpeFYI4ZqCDbFEzYVmAYQSpY+4QOcRRA1f5FXVU2U7dQkx7KDN5okou1nc2iVIVFTG3JwdBXAS/w/NGUK63tS9XmW801WaUNTTiWzY7oil0SL1FmnhlofCqVymYFl1poGZ+dfmpxwllxF22DJpcRqDAXZc7gG6ZpkXTzR81i2GTDDekWLfE2flGQM6P9oH5Tvq9lXrSBChUjpD4GFZNSwjoT7S3UxSR0TJB9yXmedSPBbgWevQw4EsiPi0MWPVqhzXsM6x4p7nCUqYKcCa4xAORn5De7zwOzKJMrTfQkL9Lm5gcLhtuNGKSDI+zu+GTAyPJ6vmpp9HN+z3O1Qgd8wIDAQAB";


    public void aa() {

        Map<String, Object> map =new HashMap<String, Object>();
        map.put("a","1");
        String sign = rsaSign(map, privateKey);
        System.out.println(sign);
    }





    public static String rsaSign(Map<String, Object> params, String privateKey) {
        return rsaSign((Map)params, privateKey, (String)null);
    }
   public static String rsaSign(Map<String, Object> params, String privateKey, String charset) {
        String content = MapUtils.getContent(params);
        return rsaSign(content, privateKey, charset);
    }

    public static String rsaSign(String content, String privateKey, String charset) {
        String result = null;

        try {
            if (StringUtils.isBlank(charset)) {
                charset = "UTF-8";
            }

            PrivateKey e = getPrivateKeyFromPKCS8("RSA", new ByteArrayInputStream(privateKey.getBytes()));
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(e);
            if (StringUtils.isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            byte[] signed = signature.sign();
            result = new String(Base64.encodeBase64(signed));
        } catch (IOException var7) {
            var7.printStackTrace();
        } catch (NoSuchAlgorithmException var8) {
            var8.printStackTrace();
        } catch (InvalidKeyException var9) {
            var9.printStackTrace();
        } catch (InvalidKeySpecException var10) {
            var10.printStackTrace();
        } catch (SignatureException var11) {
            var11.printStackTrace();
        }

        return result;
    }

    public static PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        PrivateKey privateKey = null;
        if (ins != null && StringUtils.isNotBlank(algorithm)) {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            byte[] encodedKey = StreamUtils.readText(ins).getBytes();
            encodedKey = Base64.decodeBase64(encodedKey);
            PKCS8EncodedKeySpec ps = new PKCS8EncodedKeySpec(encodedKey);
            privateKey = keyFactory.generatePrivate(ps);
        }

        return privateKey;
    }

}
