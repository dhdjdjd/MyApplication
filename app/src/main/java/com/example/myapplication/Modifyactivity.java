package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Modifyactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyactivity);
        Toolbar tl_head = findViewById(R.id.t1head);
        tl_head.setTitle("个人信息"); // 设置工具栏的标题文本
        setSupportActionBar(tl_head); // 使用tl_head替换系统自带的ActionBar
        tl_head.setTitleTextColor(Color.BLACK); // 设置工具栏的标题文字颜色
        //tl_head.setLogo(R.drawable.ic_app); // 设置工具栏的标志图片
        tl_head.setSubtitle("修改查询信息");// 设置工具栏的副标题文本
        tl_head.setSubtitleTextColor(Color.BLACK); // 设置工具栏的副标题文字颜色
        tl_head.setBackgroundResource(R.color.white); // 设置工具栏的背景
        //tl_head.setNavigationIcon(R.drawable.ic_back); // 设置工具栏左边的导航图标
        // 给tl_head设置导航图标的点击监听器
        // setNavigationOnClickListener必须放到setSupportActionBar之后，不然不起作用
        SharedPreferences sharedPreferences = getSharedPreferences("riskcon",MODE_PRIVATE);
        String username =sharedPreferences.getString("username","");
        TextView textView = findViewById(R.id.text_name);
        textView.setText(username);
        String uid =sharedPreferences.getString("uid","");
        TextView textView1 = findViewById(R.id.text_code);
        textView1.setText(uid);
        LinearLayout linearLayout = findViewById(R.id.lin_user);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "用户号不能更改", Toast.LENGTH_SHORT).show();
            }
        });
        LinearLayout linearLayout1 = findViewById(R.id.lin_head);
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "暂不支持修改头像", Toast.LENGTH_SHORT).show();
            }
        });
        LinearLayout linearLayout2 = findViewById(R.id.lin_name);
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "账号不支持更改", Toast.LENGTH_SHORT).show();
            }
        });
        LinearLayout linearLayout3 = findViewById(R.id.lin_phone);
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "点击更改手机号", Toast.LENGTH_SHORT).show();
            }
        });

    }
}