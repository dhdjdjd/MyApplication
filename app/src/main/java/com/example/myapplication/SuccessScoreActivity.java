package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
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

    private EditText et_ssb1;
    private EditText et_ssb2;
    private EditText et_ssb3;

    private String realname;
    private String personid;

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

        Bundle sbundle = getIntent().getExtras();
        int iscore = sbundle.getInt("Score");
        realname = sbundle.getString("realname");
        personid = sbundle.getString("uid");
        String sscore = "" + iscore;
        TextView tv_ssb2 = findViewById(R.id.tv_ssb2);
        tv_ssb2.setText(sscore);

        et_ssb1 = findViewById(R.id.et_ssb1);
        et_ssb2 = findViewById(R.id.et_ssb2);
        et_ssb3 = findViewById(R.id.et_ssb3);
        Button but_ssb1 = findViewById(R.id.but_ssb1);
        but_ssb1.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_ssb1:
                String money = et_ssb1.getText().toString();
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
                }

        }
    }
}
