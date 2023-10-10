package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmtest);

        btn_re = findViewById(R.id.btn_return);
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

        String jsonString = "";
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
        });
        /*btn_re.findViewById(R.id.btn_return);
        btn_re.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(BMTestActivity.this, Fragement_activity.class);
                startActivity(intent);}
        });*/


    }

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