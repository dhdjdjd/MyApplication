package com.example.myapplication.Util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import org.jetbrains.annotations.NotNull;

public class MyViewPager extends ViewPager {

    private OnViewPagerTouchListener mTouchListener = null;

    public MyViewPager(@NonNull @NotNull Context context) {
        super(context);
    }

    public MyViewPager(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mTouchListener != null) {
                    mTouchListener.onPagerTouch(true);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mTouchListener != null) {
                    mTouchListener.onPagerTouch(false);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void SetOnViewPagerTouchListener(OnViewPagerTouchListener listener){
        this.mTouchListener = listener;
    }

    //设置一个接口进行轮播图是否=被触碰的监听
    public interface OnViewPagerTouchListener{
        void onPagerTouch(boolean isTouch);
    }


}

