package com.example.myapplication.Util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class looperpagerAdapter extends PagerAdapter {

    //集合来存储
    private List<Integer> mPics = new ArrayList<>();

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        //能够实现无限轮播
        int realPosition = position % mPics.size();

        //设置一个imageview来充当载体
        ImageView imageView = new ImageView(container.getContext());
//        //设置背景颜色
//        imageView.setBackgroundColor(mColors.get(position));

        //设置图片
        imageView.setImageResource(mPics.get(realPosition));

        //设置完成后，把图片添加到容器里边
        container.addView(imageView);
        return imageView;
    }


    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if (mPics != null) {
            return Integer.MAX_VALUE;
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setData(List<Integer> sColors) {
        this.mPics = sColors;
    }

    public int getDataResultSizse(){
        if (mPics != null) {
            return mPics.size();
        }
        return 0;
    }
}
