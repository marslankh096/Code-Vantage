package com.demo.csvfilereader.utility;


public class NumberFormatUtility {
    private static String VALID_NUMBER = "0123456789";

    public static boolean isValidNumber(String str) {
        if (str == null || str.replace(" ", "").trim().isEmpty()) {
            return false;
        }
        if (str.length() <= 1 || !str.startsWith("0")) {
            for (String str2 : convertToArray(str)) {
                if (!VALID_NUMBER.contains(str2)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static String[] convertToArray(String str) {
        int length = str.length();
        String[] strArr = new String[length];
        int i = 0;
        while (i < length) {
            int i2 = i + 1;
            strArr[i] = str.substring(i, i2);
            i = i2;
        }
        return strArr;
    }
}
