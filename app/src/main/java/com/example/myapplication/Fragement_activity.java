package com.example.myapplication;

import android.os.Bundle;

import com.example.myapplication.databinding.ActivityFragementBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class Fragement_activity extends AppCompatActivity {

    private ActivityFragementBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFragementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_fragement);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        /*int id = getIntent().getIntExtra("id", 0);
                if (id == 1) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment_activity_fragement,new HomeFragment())
                            .addToBackStack(null)
                            .commit();*/
        /*}*/
    }
}
        /*Toolbar tl_head = findViewById(R.id.t1head);
        tl_head.setTitle("UU借条"); // 设置工具栏的标题文本
        setSupportActionBar(tl_head); // 使用tl_head替换系统自带的ActionBar
        tl_head.setTitleTextColor(Color.BLACK); // 设置工具栏的标题文字颜色
        //tl_head.setLogo(R.drawable.ic_app); // 设置工具栏的标志图片
        tl_head.setSubtitle("风险控制RiskControl");// 设置工具栏的副标题文本
        tl_head.setSubtitleTextColor(Color.BLACK); // 设置工具栏的副标题文字颜色
        tl_head.setBackgroundResource(R.color.white); // 设置工具栏的背景
        //tl_head.setNavigationIcon(R.drawable.ic_back); // 设置工具栏左边的导航图标
        // 给tl_head设置导航图标的点击监听器
        // setNavigationOnClickListener必须放到setSupportActionBar之后，不然不起作用
        View v_besf = findViewById(R.id.v_besf);
        v_besf.setBackgroundResource(R.drawable.shape_rect_gold);
        Button but_bow = findViewById(R.id.but_bow);
        but_bow.setBackgroundResource(R.drawable.shape_rect_white);
        but_bow.setTextColor(Color.parseColor("#4169E1"));
        tl_head.setNavigationOnClickListener(view -> {
            finish(); // 结束当前页面
        });
        //setSupportActionBar(tl_hand);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 从menu_overflow.xml中构建菜单界面布局
        getMenuInflater().inflate(R.menu.menu_overflow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId(); // 获取菜单项的编号
        if(id == R.id.menu_hlogin){
            startActivity(new Intent(this,MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }*/

