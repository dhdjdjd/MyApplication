package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Util.PemUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LowCashActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_lcce;
    private EditText et_lcce2;
    private Button but_llc1;
    private Button but_llc2;
    private CheckBox cb_lcc1;
    private Context mContext = this;
    private String BType = "";
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET
    };

    String realname;
    String personid;
    int score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_low_cash);

        Toolbar tl_lcc1 = findViewById(R.id.tl_lcc1);
        tl_lcc1.setTitle("身份信息");
        setSupportActionBar(tl_lcc1);
        tl_lcc1.setNavigationIcon(R.drawable.bbnic1);
        tl_lcc1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*startActivity(new Intent(mContext,BeforeBActivity.class));*/
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        BType = bundle.getString("BType");

        et_lcce = findViewById(R.id.et_llce);
        et_lcce2 = findViewById(R.id.et_llce2);
        but_llc1 = findViewById(R.id.but_lcc1);
        but_llc2 = findViewById(R.id.but_llc2);
        cb_lcc1 = findViewById(R.id.cb_lcc1);
        but_llc1.setOnClickListener(this);
        but_llc2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_llc2:{
                // 创建构造器
                AlertDialog.Builder builder = new AlertDialog.Builder(LowCashActivity.this);
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
            case R.id.but_lcc1:
                realname = et_lcce.getText().toString();
                personid = et_lcce2.getText().toString();
                if(cb_lcc1.isChecked() != true){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setTitle("您未同意服务条款"); // 设置对话框的标题文本
                    builder1.setMessage("请勾选同意"); // 设置对话框的内容文本
                    // 设置对话框的肯定按钮文本及其点击监听器
                    builder1.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cb_lcc1.setChecked(true);
                        }
                    });
                    AlertDialog alert = builder1.create(); // 根据建造器构建提醒对话框对象
                    alert.show(); // 显示提醒对话框
                }else {
                    if (personid.length() != 18) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                        builder2.setTitle("您的证件号有误"); // 设置对话框的标题文本
                        builder2.setMessage("请重新输入一次"); // 设置对话框的内容文本
                        // 设置对话框的肯定按钮文本及其点击监听器
                        builder2.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                et_lcce2.setText("");
                            }
                        });
                        AlertDialog alert = builder2.create(); // 根据建造器构建提醒对话框对象
                        alert.show(); // 显示提醒对话框
                    }else {
                        System.out.println(realname);
                        System.out.println(personid);
                        SharedPreferences sharedPreferences = getSharedPreferences("riskcon",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("personid", personid);
                        String jsonString = "";
                        String uid =sharedPreferences.getString("uid","");
                        editor.commit();
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("realname", realname);
                            jsonObject.put("RealName", personid);
                            jsonObject.put("uid", uid);
                            jsonString = jsonObject.toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println(jsonString);
                        String pram = "?RealId=" + personid + "&RealName=" + realname + "&uid=" + uid;
                        // 创建一个POST方式的请求结构
                        RequestBody body = RequestBody.create(jsonString, MediaType.parse("application/json"));
                        OkHttpClient client = new OkHttpClient(); // 创建一个okhttp客户端对象
                        Request request = new Request.Builder().post(body).url("http://172.20.10.5:8880/training/check"+pram).build();
                        Call call = client.newCall(request); // 根据请求结构创建调用对象
                        // 加入HTTP请求队列。异步调用，并设置接口应答的回调方法
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) { // 请求失败
                                // 回到主线程操纵界面
                                runOnUiThread(() -> Toast.makeText(LowCashActivity.this, "调用HTTP接口报错："+e.getMessage(), Toast.LENGTH_SHORT).show());
                                //tv_result.setText("调用登录接口报错："+e.getMessage()));
                            }

                            @Override
                            public void onResponse(Call call, final Response response) throws IOException { // 请求成功
                                String resp = response.body().string();
                                // 回到主线程操纵界面
                                runOnUiThread(() -> {
                                    Toast.makeText(LowCashActivity.this, "调用HTTP接口返回：\n"+resp, Toast.LENGTH_SHORT).show();
                                    score = Integer.parseInt(resp);
                                    System.out.println(score);
                                    if(score >= 80){
                                        //System.out.println(BType);
                                        if (Objects.equals(BType,"CashBorrow")) {
                                            if (PemUtil.checkPermission(LowCashActivity.this, PERMISSIONS, 1)) {
                                                Intent sintent = new Intent(mContext, SuccessScoreActivity.class);
                                                Bundle sbundle = new Bundle();
                                                sbundle.putInt("Score", score);
                                                sbundle.putString("realname", realname);
                                                sbundle.putString("uid", personid);
                                                sintent.putExtras(sbundle);
                                                startActivity(sintent);
                                            }
                                        }else {
                                            if (Objects.equals(BType,"Others") ){
                                                Intent scintent = new Intent(mContext, SuccessCustomActivity.class);
                                                Bundle scbundle = new Bundle();
                                                scbundle.putInt("Score", score);
                                                scintent.putExtras(scbundle);
                                                startActivity(scintent);
                                            }
                                        }
                                    }else{
                                        Intent fintent = new Intent(mContext,FallureActivity.class);
                                        Bundle fbundle = new Bundle();
                                        fbundle.putInt("Score",score);
                                        fintent.putExtras(fbundle);
                                        startActivity(fintent);
                                    }

                                });

                                //tv_result.setText("调用登录接口返回：\n"+resp));
                            }
                        });

                    }
                }
                break;

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (!PemUtil.checkGrant(grantResults)) {
                    Intent sintent = new Intent(mContext, SuccessScoreActivity.class);
                    Bundle sbundle = new Bundle();
                    sbundle.putInt("Score", score);
                    sbundle.putString("realname", realname);
                    sbundle.putString("uid", personid);
                    sintent.putExtras(sbundle);
                    startActivity(sintent);
                }
                break;
            }
        }
    }
}