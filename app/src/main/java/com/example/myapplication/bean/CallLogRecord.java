package com.example.myapplication.bean;

public class CallLogRecord {
    public String number; // 手机号
    public String name; // 匹配通讯录的名称
    public String date; // 通话日期
    public String duration; // 通话时长，秒数
    public String type; // 1 来电，2拨出，3未接
    public String icc_id;
    public String GfromLoc;

    public CallLogRecord(){
        number="";
        name="";
        date="";
        duration="";
        type="";
        icc_id="";
        GfromLoc="";
    }
}
