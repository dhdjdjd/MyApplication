package com.example.myapplication.Util;

import java.util.StringTokenizer;

public class StringHelper {
    public static String[] strToStrArray(String str, String separator) {
        return strToStrArrayManager(str, separator);
    }


    private static String[] strToStrArrayManager(String str, String separator) {

        StringTokenizer strTokens = new StringTokenizer(str, separator);
        String[] strArray = new String[strTokens.countTokens()];
        int i = 0;

        while (strTokens.hasMoreTokens()) {
            strArray[i] = strTokens.nextToken().trim();
            i++;
        }

        return strArray;
    }
}
