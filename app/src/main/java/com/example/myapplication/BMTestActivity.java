package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BMTestActivity extends AppCompatActivity {
    private Context mContext = this;
    private Button btn_re;
    int riskType = 0;
    TextView tv_bmt2;
    String uid;

    private RadarChart radar;
    List<RadarEntry> list;
    List<RadarEntry>list2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmtest);

        btn_re = findViewById(R.id.but_return);
        btn_re.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                finish();}
        });

        tv_bmt2 = findViewById(R.id.tv_bmt2);
        Toolbar tl_bmt = findViewById(R.id.tl_bmt);
        tl_bmt.setTitle("贷中信息收集");
        setSupportActionBar(tl_bmt);
        tl_bmt.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(mContext, LowCashActivity.class));
            }
        });

        radar = (RadarChart) findViewById(R.id.bmt_radar);
        list=new ArrayList<>();

        list.add(new RadarEntry(48));
        list.add(new RadarEntry(35));
        list.add(new RadarEntry(32));
        list.add(new RadarEntry(40));


        RadarDataSet radarDataSet=new RadarDataSet(list,"男性");
        //radarDataSet.setColor(ContextCompat.getColor(this, R.color.pink));
        radarDataSet.setDrawFilled(true);
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
                    return "应用列表";
                }
                if (v==1){
                    return "地理位置";
                }
                if (v==2){
                    return "通讯录";
                }
                if (v==3){
                    return "通话记录";
                }
                return "";
            }
        });


        //是否绘制雷达框上对每个点的数据的标注    和Y轴坐标点一般不同时存在 否则显得很挤  默认为true
        radarDataSet.setDrawValues(true);
        radarDataSet.setValueTextSize(10);  //数据值得字体大小（这里只是写在这）
        radarDataSet.setValueTextColor(ContextCompat.getColor(this, R.color.pink));//数据值得字体颜色（这里只是写在这）

        YAxis yAxis=radar.getYAxis();
        //是否绘制Y轴坐标点  和雷达框数据一般不同时存在 否则显得很挤 默认为true
        yAxis.setDrawLabels(false);
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

        //发送请求
        /*String jsonString = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uid", uid);
            jsonString = jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();

        }
        SharedPreferences sharedPreferences = getSharedPreferences("riskcon",MODE_PRIVATE);
        String uid =sharedPreferences.getString("uid","");
        System.out.println(jsonString);
        String pram = "?uid=" + uid;
        // 创建一个POST方式的请求结构
        System.out.println(pram);
        RequestBody body = RequestBody.create(jsonString, MediaType.parse("application/json"));
        OkHttpClient client = new OkHttpClient(); // 创建一个okhttp客户端对象
        Request request = new Request.Builder().post(body).url("http://172.20.10.5:8880/user/getScore"+pram).build();
        Call call = client.newCall(request); // 根据请求结构创建调用对象
        // 加入HTTP请求队列。异步调用，并设置接口应答的回调方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) { // 请求失败
                // 回到主线程操纵界面
                runOnUiThread(() -> Toast.makeText(BMTestActivity.this, "调用HTTP接口报错："+e.getMessage(), Toast.LENGTH_SHORT).show());
                //tv_result.setText("调用登录接口报错："+e.getMessage()));
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException { // 请求成功
                String resp = response.body().string();
                // 回到主线程操纵界面
                runOnUiThread(() -> {
                    Toast.makeText(BMTestActivity.this, "调用HTTP接口返回：\n"+resp, Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(mContext,BorrowMidActivity.class));
                    riskType = Integer.parseInt(resp);
                    switch (riskType) {
                        case 1:{
                            tv_bmt2.setText("正常");
                            break;
                        }
                        case 2:{
                            tv_bmt2.setText("风险");
                            tv_bmt2.setTextColor(Color.YELLOW);
                            break;
                        }
                        case 3:{
                            tv_bmt2.setText("危险");
                            tv_bmt2.setTextColor(Color.RED);
                            break;
                        }
                    }
                });

                //tv_result.setText("调用登录接口返回：\n"+resp));
            }
        });*/

        //本来就被注释了
        /*btn_re.findViewById(R.id.btn_return);
        btn_re.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(BMTestActivity.this, Fragement_activity.class);
                startActivity(intent);}
        });*/


    }
    //本来就被注释了
    /*@Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_return:{
                Intent intent = new Intent(this, Fragement_activity.class);
                startActivity(intent);
                break;
            }
        }
    }*/
}