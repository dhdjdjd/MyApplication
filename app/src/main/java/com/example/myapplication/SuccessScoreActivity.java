package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.bean.AppInfo;
import com.example.myapplication.bean.CallLogRecord;
import com.example.myapplication.bean.Contact;
import com.example.myapplication.task.GetAddressTask;
import com.example.myapplication.Util.CommunicationUtil;
import com.example.myapplication.Util.DateUtil;
import com.example.myapplication.Util.NumTtanUtil;
import com.example.myapplication.Util.PemUtil;
import com.example.myapplication.Util.SwitchUtil;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SuccessScoreActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext = this;
    private Map<String, String> providerMap = new HashMap<>();
    //private TextView tv_location; // 声明一个文本视图对象
    private String mLocationDesc = ""; // 定位说明
    private LocationManager mLocationMgr; // 声明一个定位管理器对象
    private Handler mHandler = new Handler(Looper.myLooper()); // 声明一个处理器对象
    private boolean isLocationEnable = false; // 定位服务是否可用

    private List<CallLogRecord> callLogRecords;
    private List<AppInfo> appList;
    private List<Contact> contactList;
    private String sendLoc = "";


    private String realname;
    private String personid;

    private RadarChart radar;
    List<RadarEntry>list;
    List<RadarEntry>list2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_score);

        Toolbar tl_ssb = findViewById(R.id.tl_ssb);
        tl_ssb.setTitle("小额现金贷");
        setSupportActionBar(tl_ssb);
        tl_ssb.setNavigationIcon(R.drawable.bbnic1);
        tl_ssb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, LowCashActivity.class));
            }
        });

        /*Bundle sbundle = getIntent().getExtras();
        int iscore = sbundle.getInt("Score");
        realname = sbundle.getString("realname");
        personid = sbundle.getString("uid");
        String sscore = "" + iscore;*/
        TextView tv_ssb2 = findViewById(R.id.tv_ssb2);
        //tv_ssb2.setText(sscore);

        /*et_ssb1 = findViewById(R.id.et_ssb1);
        et_ssb2 = findViewById(R.id.et_ssb2);
        et_ssb3 = findViewById(R.id.et_ssb3);*/
        Button but_ssb1 = findViewById(R.id.but_ssb1);
        but_ssb1.setOnClickListener(this);


        radar = (RadarChart) findViewById(R.id.radar);
        list=new ArrayList<>();

        list.add(new RadarEntry(30));
        list.add(new RadarEntry(35));
        list.add(new RadarEntry(40));
        list.add(new RadarEntry(35));
        list.add(new RadarEntry(20));


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
                    return "贷前信用";
                }
                if (v==1){
                    return "资产估计";
                }
                if (v==2){
                    return "健康状况";
                }
                if (v==3){
                    return "收入状况";
                }
                if (v==4){
                    return "家庭开支";
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


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_ssb1:
                /*String money = et_ssb1.getText().toString();
                String Lmoney = et_ssb2.getText().toString();
                String Btime = et_ssb3.getText().toString();
                int Nmoney = Integer.parseInt(money);
                int Ntime = Integer.parseInt(Btime);
                String tmpLmoney = "";
                if (Nmoney <= 0 || Nmoney > 20000 || Ntime <= 0 || Ntime >24) {
                    Toast.makeText(mContext, "请输入正确金额或时间", Toast.LENGTH_SHORT).show();
                } else {
                    if (Objects.equals(Lmoney,NumTtanUtil.getChineseNumber(money)) ) {
                        Toast.makeText(mContext, "大写金额与小写金额不匹配", Toast.LENGTH_SHORT).show();
                    }else {
                        String jsonString = "";
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date curDate =  new Date(System.currentTimeMillis());
                        String nowtime =  formatter.format(curDate);
                        String projectid = "P0000001";
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("Realname", realname);
                            jsonObject.put("Realid", personid);
                            jsonObject.put("Money", money);
                            jsonObject.put("BTime", Btime);
                            jsonObject.put("Nowtime", nowtime);
                            jsonObject.put("Projectid", projectid);
                            jsonString = jsonObject.toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println(jsonString);
                        String pram = "?RealId=" + personid + "&RealName=" + realname + "&Projectid=" + projectid + "&Money=" +money + "&BTime=" + Btime +"&NowTime=" + nowtime;
                        // 创建一个POST方式的请求结构
                        System.out.println(pram);
                        RequestBody body = RequestBody.create(jsonString, MediaType.parse("application/json"));
                        OkHttpClient client = new OkHttpClient(); // 创建一个okhttp客户端对象
                        Request request = new Request.Builder().post(body).url("http://172.20.10.5:8880/business/bus"+pram).build();
                        Call call = client.newCall(request); // 根据请求结构创建调用对象
                        // 加入HTTP请求队列。异步调用，并设置接口应答的回调方法
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) { // 请求失败
                                // 回到主线程操纵界面
                                runOnUiThread(() -> Toast.makeText(SuccessScoreActivity.this, "调用HTTP接口报错："+e.getMessage(), Toast.LENGTH_SHORT).show());
                                //tv_result.setText("调用登录接口报错："+e.getMessage()));
                            }

                            @Override
                            public void onResponse(Call call, final Response response) throws IOException { // 请求成功
                                String resp = response.body().string();
                                // 回到主线程操纵界面
                                runOnUiThread(() -> {
                                    Toast.makeText(SuccessScoreActivity.this, "调用HTTP接口返回：\n"+resp, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(mContext,BorrowMidActivity.class));
                                });

                                //tv_result.setText("调用登录接口返回：\n"+resp));
                            }
                        });
                    }
                }*/
                startActivity(new Intent(this, SubmitActivity.class));
        }
    }
}
