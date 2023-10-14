package com.example.myapplication.Util;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;


public class ChatUtil {

    public static View getChatView(Context ctx, String name, String content, boolean isSelf) {
        int layoutId = isSelf ? R.layout.chat_me : R.layout.chat_other;
        View view = LayoutInflater.from(ctx).inflate(layoutId, null);
        //ImageView iv_portrait = view.findViewById(R.id.iv_portrait);
        TextView tv_content = view.findViewById(R.id.tv_content);
        //iv_portrait.setImageDrawable(getPortraitByName(ctx, name));
        if (isSelf){
            tv_content.setTextColor(Color.WHITE);
        }
        tv_content.setText(content);
        tv_content.setTextSize(16);
        tv_content.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams ll_params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll_params.gravity = isSelf ? Gravity.RIGHT : Gravity.LEFT;
        view.setLayoutParams(ll_params);
        return view;
    }

    public static TextView getHintView(Context ctx, String content, int margin) {
        TextView tv = new TextView(ctx);
        tv.setText(content);
        tv.setTextSize(12);
        tv.setTextColor(Color.GRAY);
        LinearLayout.LayoutParams tv_params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv_params.setMargins(margin, margin, margin, margin);
        tv_params.gravity = Gravity.CENTER;
        tv.setLayoutParams(tv_params);
        return tv;
    }
}
