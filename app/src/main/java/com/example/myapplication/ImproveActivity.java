package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;

public class ImproveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_improve);

        Toolbar tl_head = findViewById(R.id.t1head);
        tl_head.setTitle("提升额度"); // 设置工具栏的标题文本
        setSupportActionBar(tl_head); // 使用tl_head替换系统自带的ActionBar
        tl_head.setTitleTextColor(Color.BLACK); // 设置工具栏的标题文字颜色
        //tl_head.setLogo(R.drawable.ic_app); // 设置工具栏的标志图片
        tl_head.setSubtitle("天天攒额度");// 设置工具栏的副标题文本
        tl_head.setSubtitleTextColor(Color.BLACK); // 设置工具栏的副标题文字颜色
        tl_head.setBackgroundResource(R.color.white); // 设置工具栏的背景
    }
}