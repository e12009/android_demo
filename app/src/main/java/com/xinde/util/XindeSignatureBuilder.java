package com.xinde.util;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/**
 * 信德数聚 签名工具类
 */
public class XindeSignatureBuilder {
    private String appSecret = null;
    private TreeMap<String, String> paramsMap = null;

    public XindeSignatureBuilder() {
        init( null);
    }

    public XindeSignatureBuilder(String appSecret) {
        init(appSecret);
    }

    private void init(String appSecret) {
        this.appSecret = appSecret;
        this.paramsMap = new TreeMap<>();
    }

    public XindeSignatureBuilder setAppSecret(String appSecret) {
        this.appSecret = appSecret;
        return this;
    }

    public XindeSignatureBuilder addParamStringPair(String key, String value) {
        this.paramsMap.put(key, value);
        return this;
    }

    public String build() {
        if (this.appSecret == null || this.paramsMap.isEmpty()) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder(this.appSecret);

        Set<String> keySet = this.paramsMap.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
            String key = it.next();
            String value = this.paramsMap.get(key);

            stringBuilder.append(key).append(value);

        }

        stringBuilder.append(this.appSecret);

        return XindeSignatureBuilder.SHA1SUM(stringBuilder.toString());
    }

    @Override
    public String toString() {
        return this.build();
    }

    private static String SHA1SUM(String str) {
        if(null == str || str.isEmpty()) {
            return null;
        }

        char hexDigits[] = {
                '0','1','2','3','4','5','6','7',
                '8','9','a','b','c','d','e','f'
        };

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j*2];
            int k = 0;
            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }

            return new String(buf);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

}
