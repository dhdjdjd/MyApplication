package com.example.myapplication.Util;

public class BMUtil {

    public static int matchStringByIndexOf(String parent, String child )
    {
        int count = 0;
        int index = 0;
        while( ( index = parent.indexOf(child, index) ) != -1 )
        {
            index = index+child.length();
            count++;
        }
        return count;//结果输出
    }

}
