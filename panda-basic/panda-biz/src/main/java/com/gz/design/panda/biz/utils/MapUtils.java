package com.gz.design.panda.biz.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MapUtils {
    public MapUtils() {
    }

    public static String getContent(Map<String, Object> sortedParams) {
        StringBuffer buffer = new StringBuffer();
        ArrayList keys = new ArrayList(sortedParams.keySet());
        Collections.sort(keys);

        for(int i = 0; i < keys.size(); ++i) {
            String key = (String)keys.get(i);
            Object value = sortedParams.get(key);
            buffer.append(key).append(value);
        }

        return buffer.toString();
    }

    public static Map<String, String> transStringToMap(String mapString, String separator, String pairSeparator) {
        Map<String, String> map = new HashMap<String, String>();
        String[] fSplit = mapString.split(separator);
        for (int i = 0; i < fSplit.length; i++) {
            if (fSplit[i]==null||fSplit[i].length()==0) {
                continue;
            }
            String[] sSplit = fSplit[i].split(pairSeparator);
            String value = fSplit[i].substring(fSplit[i].indexOf('=') + 1, fSplit[i].length());
            map.put(sSplit[0], value);
        }

        return map;
    }
}
