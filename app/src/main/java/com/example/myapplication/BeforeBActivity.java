package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.adapter.RecyclerLinearAdapter;
import com.example.myapplication.bean.BorrowType;
import com.example.myapplication.widget.SpacesDecoration;

public class BeforeBActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_bactivity);
        Toolbar tl_bb1 = findViewById(R.id.tl_bb1);
        tl_bb1.setTitle("借款种类");
        tl_bb1.setTitleTextColor(Color.BLACK);
        setSupportActionBar(tl_bb1);
        tl_bb1.setNavigationIcon(R.drawable.bbnic1);
        tl_bb1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(BeforeBActivity.this, Fragement_activity.class);
                intent.putExtra("id",1);
                startActivity(intent);*/
                finish();
            }
        });
        initRecyclerLinear();
    }

    private void initRecyclerLinear() {
        // 从布局文件中获取名叫rv_linear的循环视图
        RecyclerView rv_linear = findViewById(R.id.rv_bblinear);
        // 创建一个垂直方向的线性布局管理器
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rv_linear.setLayoutManager(manager); // 设置循环视图的布局管理器
        // 构建一个公众号列表的线性适配器
        RecyclerLinearAdapter adapter = new RecyclerLinearAdapter(this, BorrowType.getDefaultList());
        rv_linear.setAdapter(adapter);  // 设置循环视图的线性适配器
        rv_linear.setItemAnimator(new DefaultItemAnimator());  // 设置循环视图的动画效果
        rv_linear.addItemDecoration(new SpacesDecoration(1));  // 设置循环视图的空白装饰
    }

    @Override
    public void onClick(View view) {
    }
}