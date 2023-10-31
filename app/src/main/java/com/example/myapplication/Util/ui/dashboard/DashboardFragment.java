package com.example.myapplication.Util.ui.dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.ImproveActivity;
import com.example.myapplication.R;
import com.example.myapplication.SuccessScoreActivity;


public class DashboardFragment extends Fragment {
    protected AppCompatActivity mActivity;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mActivity = (AppCompatActivity) getActivity();
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        View root = inflater.inflate(R.layout.fragment_dashboard,container,false);
        Toolbar tl_head = root.findViewById(R.id.t1head);
        tl_head.setTitle("发现生活"); // 设置工具栏的标题文本
        mActivity.setSupportActionBar(tl_head); // 使用tl_head替换系统自带的ActionBar
        tl_head.setTitleTextColor(Color.BLACK); // 设置工具栏的标题文字颜色
        //tl_head.setLogo(R.drawable.ic_app); // 设置工具栏的标志图片
        tl_head.setSubtitle("带你玩转生活");// 设置工具栏的副标题文本
        tl_head.setSubtitleTextColor(Color.BLACK); // 设置工具栏的副标题文字颜色
        tl_head.setBackgroundResource(R.color.white);
        LinearLayout linearLayout1 = root.findViewById(R.id.lin_achievement);
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SuccessScoreActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout linearLayout2 = root.findViewById(R.id.lin_money);
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ImproveActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}