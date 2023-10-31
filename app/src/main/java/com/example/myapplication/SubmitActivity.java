package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Util.NumTtanUtil;
import com.example.myapplication.bean.AppInfo;
import com.example.myapplication.bean.CallLogRecord;
import com.example.myapplication.bean.Contact;

import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SubmitActivity extends AppCompatActivity implements View.OnClickListener {

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

    private EditText et_llce;
    private Button but_llc2;
    private EditText et_llce2;

    private EditText et_ssb1;
    private EditText et_ssb2;
    private Button but_ssb3;

    private String realname;
    private String personid;

    private int subMonth;

    DateFormat format= DateFormat.getDateTimeInstance();
    Calendar calendar= Calendar.getInstance(Locale.CHINA);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

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
        String sscore = "" + iscore;
        TextView tv_ssb2 = findViewById(R.id.tv_ssb2);
        tv_ssb2.setText(sscore);*/

        et_llce = findViewById(R.id.et_llce);
        but_llc2 = findViewById(R.id.but_llc2);
        but_llc2.setOnClickListener(this);
        et_llce2 = findViewById(R.id.et_llce2);
        et_ssb1 = findViewById(R.id.et_ssb1);
        et_ssb2 = findViewById(R.id.et_ssb2);
        but_ssb3 = findViewById(R.id.et_ssb3);
        Button but_ssb1 = findViewById(R.id.but_ssb1);
        but_ssb1.setOnClickListener(this);
        but_ssb3.setOnClickListener(this);
    }

    /**
     * 日期选择
     * @param activity
     * @param themeResId
     * @param tv
     * @param calendar
     */
    public static int showDatePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        final int[] subMonth = {0};
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作
                subMonth[0] = monthOfYear + 1 - calendar.get(Calendar.MONTH);
                tv.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
                //tv.setText("借款" + mouth + "月");
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
        return subMonth[0];
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_ssb3:{
                subMonth = showDatePickerDialog(this,  4, but_ssb3, calendar);
                break;
            }
            case R.id.but_llc2:{
                // 创建构造器
                AlertDialog.Builder builder = new AlertDialog.Builder(SubmitActivity.this);
                //builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("选择证件类型");
                // 设置内容,
                final String[] cities = {"中国居民身份证", "护照", "港澳通行证"};
                builder.setSingleChoiceItems(cities, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 传出数据？？？
                        //Toast.makeText(MainActivity.this, "选中的选项为: " + which, Toast.LENGTH_SHORT).show();
                        but_llc2.setText(cities[which]);
                    }
                });

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 不进行操作
                        but_llc2.setText("点击以选择您的证件类型");
                    }
                });

                // 显示dialog
                builder.create().show();
                break;
            }
            case R.id.but_ssb1:
                String money = et_ssb1.getText().toString();
                String Lmoney = et_ssb2.getText().toString();
                //String Btime = but_ssb3.getText().toString();
                int Nmoney = Integer.parseInt(money);
                int Ntime = subMonth;/*Integer.parseInt(Btime);*/
                String tmpLmoney = "";
                if (Nmoney <= 0 || Nmoney > 20000 || Ntime <= 0 || Ntime >24) {
                    Toast.makeText(mContext, "请输入正确金额或时间", Toast.LENGTH_SHORT).show();
                } else {
                    if (Objects.equals(Lmoney, NumTtanUtil.getChineseNumber(money)) ) {
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
                            jsonObject.put("BTime", Ntime);
                            jsonObject.put("Nowtime", nowtime);
                            jsonObject.put("Projectid", projectid);
                            jsonString = jsonObject.toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println(jsonString);
                        String pram = "?RealId=" + personid + "&RealName=" + realname + "&Projectid=" + projectid + "&Money=" +money + "&BTime=" + Ntime +"&NowTime=" + nowtime;
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
                                runOnUiThread(() -> Toast.makeText(SubmitActivity.this, "调用HTTP接口报错："+e.getMessage(), Toast.LENGTH_SHORT).show());
                                //tv_result.setText("调用登录接口报错："+e.getMessage()));
                            }

                            @Override
                            public void onResponse(Call call, final Response response) throws IOException { // 请求成功
                                String resp = response.body().string();
                                // 回到主线程操纵界面
                                runOnUiThread(() -> {
                                    Toast.makeText(SubmitActivity.this, "调用HTTP接口返回：\n"+resp, Toast.LENGTH_SHORT).show();
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