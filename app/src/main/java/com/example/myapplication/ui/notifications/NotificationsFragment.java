package com.example.myapplication.ui.notifications;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.Modifyactivity;
import com.example.myapplication.R;

public class NotificationsFragment extends Fragment {
    protected AppCompatActivity mActivity;





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mActivity = (AppCompatActivity) getActivity();
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications,container,false);

        Toolbar tl_head = root.findViewById(R.id.t1head);
        tl_head.setTitle("个人中心"); // 设置工具栏的标题文本
        mActivity.setSupportActionBar(tl_head); // 使用tl_head替换系统自带的ActionBar
        tl_head.setTitleTextColor(Color.BLACK); // 设置工具栏的标题文字颜色
        //tl_head.setLogo(R.drawable.ic_app); // 设置工具栏的标志图片
        tl_head.setSubtitle("更好managemyself");// 设置工具栏的副标题文本
        tl_head.setSubtitleTextColor(Color.BLACK); // 设置工具栏的副标题文字颜色



        SharedPreferences sharedPreferences = mActivity.getSharedPreferences("riskcon",mActivity.MODE_PRIVATE);
        String username =sharedPreferences.getString("username","");
        TextView textView = root.findViewById(R.id.changedname);
        ImageView imageView = root.findViewById(R.id.avtar);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Modifyactivity.class);
                startActivity(intent);
            }
        });
        textView.setText(username);
        /*LinearLayout linearLayout1 = root.findViewById(R.id.lin_xiu);
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Modifyactivity.class);
                startActivity(intent);
            }
        });*/



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}