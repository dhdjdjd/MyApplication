package com.example.myapplication.Util.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.myapplication.AnHome1Activity;
import com.example.myapplication.AnHome2Activity;
import com.example.myapplication.AnHome3Activity;
import com.example.myapplication.AnHome4Activity;
import com.example.myapplication.BMTestActivity;
import com.example.myapplication.BeforeBActivity;
import com.example.myapplication.BorrowMidActivity;
import com.example.myapplication.ChatBotActivity;
import com.example.myapplication.R;

public class HomeFragment extends Fragment {

    private Button btn_bow;
    private Button but_ss5;
    private Button but_ss6;
    private Button BUT_1;
    private Button BUT_2;
    private Button BUT_3;
    private Button BUT_4;
    protected AppCompatActivity mActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mActivity = (AppCompatActivity) getActivity();
        View root = inflater.inflate(R.layout.fragment_home, container, false);


       /* BUT_1=root.findViewById(R.id.but_ss1);
        BUT_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AnHome2Activity.class);
                startActivity(intent);
            }
        });
        BUT_2=root.findViewById(R.id.but_ss2);
        BUT_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AnHome1Activity.class);
                startActivity(intent);
            }
        });
        BUT_3=root.findViewById(R.id.but_ss3);
        BUT_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AnHome3Activity.class);
                startActivity(intent);
            }
        });
        BUT_4=root.findViewById(R.id.but_ss4);
        BUT_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatBotActivity.class);
                startActivity(intent);
            }
        });
        */
        chat = root.findViewById(R.id.AI);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatBotActivity.class);
                startActivity(intent);
            }
        });


        btn_bow=root.findViewById(R.id.v_besf);
        btn_bow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BeforeBActivity.class);
                startActivity(intent);
            }
        });

        te7= root.findViewById(R.id.te7);
        te7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AnHome2Activity.class);
                startActivity(intent);
            }
        });

        /*
        but_ss5=root.findViewById(R.id.but_ss5);
        but_ss5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BMTestActivity.class);
                startActivity(intent);
            }
        });
        but_ss6=root.findViewById(R.id.but_ss6);
        but_ss6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BorrowMidActivity.class);
                startActivity(intent);
            }
        });*/

        Toolbar tl_head = root.findViewById(R.id.t1head);
        tl_head.setTitle("贷易控");
        mActivity.setSupportActionBar(tl_head);
        tl_head.setTitleTextColor(Color.BLACK); // 设置工具栏的标题文字颜色
        //tl_head.setLogo(R.drawable.ic_app); // 设置工具栏的标志图片
        tl_head.setSubtitle("风险控制RiskControl");// 设置工具栏的副标题文本
        tl_head.setSubtitleTextColor(Color.BLACK); // 设置工具栏的副标题文字颜色
        //tl_head.setNavigationIcon(R.drawable.ic_back); // 设置工具栏左边的导航图标
        // 给tl_head设置导航图标的点击监听器
        // setNavigationOnClickListener必须放到setSupportActionBar之后，不然不起作用
        /*View v_besf = root.findViewById(R.id.v_besf);
        v_besf.setBackgroundResource(R.drawable.shape_rect_gold);
        Button but_bow = root.findViewById(R.id.but_bow);
        but_bow.setBackgroundResource(R.drawable.shape_rect_white);
        but_bow.setTextColor(Color.parseColor("#4169E1"));*/
        tl_head.setNavigationOnClickListener(view -> {
            mActivity.finish(); // 结束当前页面
        });
        //setSupportActionBar(tl_hand);
      return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}