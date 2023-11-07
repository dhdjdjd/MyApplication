package com.example.myapplication.Util.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.R;
import com.example.myapplication.Util.MyViewPager;
import com.example.myapplication.Util.looperpagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends Fragment implements MyViewPager.OnViewPagerTouchListener, ViewPager.OnPageChangeListener{
    protected AppCompatActivity mActivity;

    private View root;
    private MyViewPager viewPager;
    boolean mIsTouch = false;


    private looperpagerAdapter mLooperPagerAdapter;
    private static List<Integer> sPics = new ArrayList<>();

    static{
        sPics.add(R.mipmap.u560);
        sPics.add(R.mipmap.u561);
        sPics.add(R.mipmap.u562);
        sPics.add(R.mipmap.u563);
        sPics.add(R.mipmap.u564);
    }

    private Handler handler;
    private LinearLayout pointContainer;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mActivity = (AppCompatActivity) getActivity();

        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        root = inflater.inflate(R.layout.fragment_dashboard,container,false);
        viewPager = root.findViewById(R.id.content_pager);
        pointContainer = root.findViewById(R.id.points_container);
        initView();
        handler = new Handler();


        Toolbar tl_head = root.findViewById(R.id.t1head);
        tl_head.setTitle("发现生活"); // 设置工具栏的标题文本
        mActivity.setSupportActionBar(tl_head); // 使用tl_head替换系统自带的ActionBar
        tl_head.setTitleTextColor(Color.BLACK); // 设置工具栏的标题文字颜色
        //tl_head.setLogo(R.drawable.ic_app); // 设置工具栏的标志图片
        tl_head.setSubtitle("带你玩转生活");// 设置工具栏的副标题文本
        tl_head.setSubtitleTextColor(Color.BLACK); // 设置工具栏的副标题文字颜色

        return root;
    }



    private Runnable mLooperTask = new Runnable() {
        @Override
        public void run() {
            if (!mIsTouch) {
                //切换viewpager里面的图片到下一个
                int currentItem = viewPager.getCurrentItem();
                viewPager.setCurrentItem(++currentItem,true);
                //设置时间
            }
            handler.postDelayed(this,2000);
        }
    };


    //绑定控件
    private void initView() {
        //首先找到这个控件
        //设置一个数据适配器
        mLooperPagerAdapter = new looperpagerAdapter();
        mLooperPagerAdapter.setData(sPics);
        //给viewpager添加适配器
        viewPager.setAdapter(mLooperPagerAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.SetOnViewPagerTouchListener(this);
        //设置初始位置
        /**
         * 如果为true，就是要做动画，false就是不做动画
         */

        insertPoint();
        viewPager.setCurrentItem(mLooperPagerAdapter.getDataResultSizse() * 100, false);

    }
    private void insertPoint() {
        for (int i = 0; i < sPics.size(); i++) {
            View point = new View(mActivity);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(25, 25
            );
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}