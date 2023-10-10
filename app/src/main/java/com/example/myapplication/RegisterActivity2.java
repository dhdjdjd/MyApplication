package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Util.NetConst;

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

public class RegisterActivity2 extends AppCompatActivity {
    private final static String TAG = "OkhttpCallActivity";
    private final static String URL_STOCK = "https://hq.sinajs.cn/list=s_sh000001";
    private final static String URL_register = NetConst.HTTP_PREFIX + "register";
    private Button btn_ac;
    private EditText zhanghao;
    private EditText mima;
    private EditText youxiang;
    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        btn_ac=findViewById(R.id.btn_next);
        zhanghao=findViewById(R.id.ed_1);
        mima=findViewById(R.id.ed_2);
        youxiang=findViewById(R.id.ed_3);
        tv_result = findViewById(R.id.tv_result);
        findViewById(R.id.btn_next).setOnClickListener(v -> {
            postForm();
        });
    }
    private void postForm() {
        String username= zhanghao.getText().toString();
        String password = youxiang.getText().toString();
        String email = mima.getText().toString();
        int uidI = (int)Math.floor(Math.random()*1000);
        if(uidI<100) uidI+=100;
        String uid  = "0000000"+ String.valueOf(uidI);
        // 创建一个表单对象
        /*FormBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();*/
        String jsonString = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uid", uid);
            jsonObject.put("username", username);
            jsonObject.put("email", email);
            jsonObject.put("password",password);
            jsonString = jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 创建一个POST方式的请求结构
        RequestBody body = RequestBody.create(jsonString, MediaType.parse("application/json"));
        OkHttpClient client = new OkHttpClient(); // 创建一个okhttp客户端对象
        // 创建一个POST方式的请求结构
        Request request = new Request.Builder().post(body).url(URL_register).build();
        Call call = client.newCall(request); // 根据请求结构创建调用对象
        // 加入HTTP请求队列。异步调用，并设置接口应答的回调方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) { // 请求失败
                // 回到主线程操纵界面
                runOnUiThread(() -> tv_result.setText("调用登录接口报错："+e.getMessage()));
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException { // 请求成功
                String resp = response.body().string();
                // 回到主线程操纵界面
                runOnUiThread(() -> tv_result.setText("调用登录接口返回：\n"+resp));
                if( Objects.equals(resp,"20000") )
                {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                    finish();
                    Looper.loop();



                }
            }
        });
    }
}