package com.mj.xr.util;

public class StringUtil {
    /**
     * MyAge->my_age Age->age myAge->my_age
     * 将str转换成符合数据库表名规则的“表名”
     */
    public static String underlineCase(String str) {
        if (str == null) return null;
        int len = str.length();
        if (len == 0) return str;

        StringBuilder sb = new StringBuilder();
        sb.append(Character.toLowerCase(str.charAt(0)));
        for (int i = 1; i < len; i++) {
            char c = str.charAt(i);

            if (Character.isUpperCase(c)) {
                sb.append("_");
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
