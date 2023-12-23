package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class FallureActivity extends AppCompatActivity {
    private Context mContext = this;
    private Button btn;

    private RadarChart radar;
    List<RadarEntry> list;
    List<RadarEntry>list2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fallure);

        Toolbar tl_fub1 = findViewById(R.id.tl_fub);
        tl_fub1.setTitle("申请失败");
        setSupportActionBar(tl_fub1);

        Bundle sbundle = getIntent().getExtras();
        int iscore = sbundle.getInt("Score");
        String sscore = "" + iscore;
        TextView tv_fub2 = findViewById(R.id.tv_fub2);
        tv_fub2.setText(sscore);
        btn = findViewById(R.id.but_fub1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=null;
                intent=new Intent(mContext,Fragement_activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        radar = (RadarChart) findViewById(R.id.radar);
        list=new ArrayList<>();

        list.add(new RadarEntry(10));
        list.add(new RadarEntry(15));
        list.add(new RadarEntry(40));
        list.add(new RadarEntry(35));
        list.add(new RadarEntry(20));


        RadarDataSet radarDataSet=new RadarDataSet(list,"男性");
        radarDataSet.setColor(ContextCompat.getColor(this, R.color.pink));
        RadarData radarData=new RadarData(radarDataSet);
        radar.setData(radarData);

        //Y轴最小值不设置会导致数据中最小值默认成为Y轴最小值
        radar.getYAxis().setAxisMinimum(0);

        //大字的颜色（中心点和各顶点的连线）
        radar.setWebColor(Color.CYAN);
        //所有五边形的颜色
        radar.setWebColorInner(ContextCompat.getColor(this, R.color.gbule));
        //整个控件的背景颜色
        radar.setBackgroundColor(ContextCompat.getColor(this, R.color.tp));

        XAxis xAxis=radar.getXAxis();
        xAxis.setTextColor(ContextCompat.getColor(this, R.color.lbule));//X轴字体颜色
        xAxis.setTextSize(12);     //X轴字体大小
        //自定义X轴坐标描述（也就是五个顶点上的文字,默认是0、1、2、3、4）
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                if (v==0){
                    return "贷前信用";
                }
                if (v==1){
                    return "负载比率";
                }
                if (v==2){
                    return "健康状况";
                }
                if (v==3){
                    return "资产净值";
                }
                if (v==4){
                    return "家庭收入";
                }
                return "";
            }
        });


        //是否绘制雷达框上对每个点的数据的标注    和Y轴坐标点一般不同时存在 否则显得很挤  默认为true
        radarDataSet.setDrawValues(false);
        radarDataSet.setValueTextSize(8);  //数据值得字体大小（这里只是写在这）
        radarDataSet.setValueTextColor(Color.CYAN);//数据值得字体颜色（这里只是写在这）

        YAxis yAxis=radar.getYAxis();
        //是否绘制Y轴坐标点  和雷达框数据一般不同时存在 否则显得很挤 默认为true
        yAxis.setDrawLabels(true);
        yAxis.setTextColor(Color.GRAY);//Y轴坐标数据的颜色
        yAxis.setAxisMaximum(50);   //Y轴最大数值
        yAxis.setAxisMinimum(0);   //Y轴最小数值
        //Y轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        yAxis.setLabelCount(10,false);


        //对于右下角一串字母的操作
        radar.getDescription().setEnabled(false);                  //是否显示右下角描述
        radar.getDescription().setText("这是修改那串英文的方法");    //修改右下角字母的显示
        radar.getDescription().setTextSize(20);                    //字体大小
        radar.getDescription().setTextColor(Color.CYAN);             //字体颜色

        //图例
        Legend legend=radar.getLegend();
        legend.setEnabled(false);    //是否显示图例
        //legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);    //图例的位置
    }
}