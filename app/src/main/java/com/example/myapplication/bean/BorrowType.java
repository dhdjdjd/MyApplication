package com.example.myapplication.bean;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class BorrowType {
    public int pic_id; // 图片的资源编号
    public String title; // 标题
    public String desc; // 描述
    public boolean isPressed; // 是否按下
    public int id; // 商品信息编号
    private static int seq = 0; // 序号

    public BorrowType(int pic_id, String title, String desc) {
        this.pic_id = pic_id;
        this.title = title;
        this.desc = desc;
        this.isPressed = false;
        this.id = this.seq;
        this.seq++;
    }

    private static int[] newsImageArray = {R.drawable.bbpic4, R.drawable.bbpic2
            , R.drawable.bbpic3, R.drawable.bbpic1, R.drawable.bbpic5};
    private static String[] newsTitleArray = {
            "小额现金贷", "个人消费贷款", "个人教育贷款", "个人汽车贷款", "个人房屋贷款"};
    private static String[] newsDescArray = {
            "无需担保抵押、快速申请、即刻到账",
            "提交信息，获取额度、消费即用",
            "助力教育、更低门槛、更低利息",
            "先用后买、方便生活、优惠项目",
            "温馨港湾、利率优惠、月均轻松"};

    public static List<BorrowType> getDefaultList() {
        List<BorrowType> newsList = new ArrayList<BorrowType>();
        for (int i = 0; i < newsImageArray.length; i++) {
            newsList.add(new BorrowType(newsImageArray[i], newsTitleArray[i], newsDescArray[i]));
        }
        return newsList;
    }

}
