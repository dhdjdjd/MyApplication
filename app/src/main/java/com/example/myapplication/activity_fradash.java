package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Util.MyViewPager;
import com.example.myapplication.Util.looperpagerAdapter;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class activity_fradash extends AppCompatActivity implements MyViewPager.OnViewPagerTouchListener, ViewPager.OnPageChangeListener {

    private MyViewPager viewPager;
    boolean mIsTouch = false;


    private looperpagerAdapter mLooperPagerAdapter;
    private static List<Integer> sPics = new ArrayList<>();

    static{
        sPics.add(R.mipmap.p1);
        sPics.add(R.mipmap.p2);
        sPics.add(R.mipmap.p3);
        sPics.add(R.mipmap.p4);
        sPics.add(R.mipmap.p5);
    }

    private Handler handler;
    private LinearLayout pointContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fradash);
        initView();
        handler = new Handler();

    }



    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        //当绑定这个窗口时
        handler.post(mLooperTask);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacks(mLooperTask);
    }

    private Runnable mLooperTask = new Runnable() {
        @Override
        public void run() {
            if (!mIsTouch) {
                //切换viewpager里面的图片到下一个
                int currentItem = viewPager.getCurrentItem();
                /**
                 * 这里要注意一下，++必须放在前面
                 * 先进行自增运算，在进行赋值。
                 * 布尔值设置为true表示是自动切换时有滑动效果，设置为false时没有滑动效果。
                 */
                viewPager.setCurrentItem(++currentItem,true);
                //设置时间
            }
            handler.postDelayed(this,2000);
        }
    };


    //绑定控件
    private void initView() {
        //首先找到这个控件
        viewPager = this.findViewById(R.id.content_pager);
        //设置一个数据适配器
        mLooperPagerAdapter = new looperpagerAdapter();
        mLooperPagerAdapter.setData(sPics);
        //给viewpager添加适配器
        viewPager.setAdapter(mLooperPagerAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.SetOnViewPagerTouchListener(this);

        pointContainer = findViewById(R.id.points_container);

        //设置初始位置
        /**
         * 如果为true，就是要做动画，false就是不做动画
         */

        insertPoint();
        viewPager.setCurrentItem(mLooperPagerAdapter.getDataResultSizse() * 100, false);

    }
    private void insertPoint() {
        for (int i = 0; i < sPics.size(); i++) {
            View point = new View(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(40, 40);
            point.setBackground(getResources().getDrawable(R.drawable.shape_point_normal));
            layoutParams.leftMargin = 20;
            point.setLayoutParams(layoutParams);
            pointContainer.addView(point);
        }
    }



    @Override
    public void onPagerTouch(boolean isTouch) {
        mIsTouch = isTouch;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int realPosition;
        if (mLooperPagerAdapter.getDataResultSizse() != 0) {
            realPosition = position % mLooperPagerAdapter.getDataResultSizse();
        } else {
            realPosition = 0;
        }
        setSelectedPoint(realPosition);
    }

    private void setSelectedPoint(int realPosition) {
        for (int i = 0; i < pointContainer.getChildCount(); i++) {
            View childAt = pointContainer.getChildAt(i);
            if (i != realPosition) {
                //若当前图片为选中，则就是白色
                childAt.setBackgroundResource(R.drawable.shape_point_normal);
            } else {
                //当前图片位置和该图片的真实位置相同，就是选中了，就表现出红色
                childAt.setBackgroundResource(R.drawable.shape_point_selected);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}