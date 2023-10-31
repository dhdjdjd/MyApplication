package com.example.myapplication;

import static com.example.myapplication.R.layout.activity_main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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


public class MainActivity extends AppCompatActivity  {

    private TextView b_rgr;
    private final static String TAG = "OkhttpCallActivity";
    private final static String URL_STOCK = "https://hq.sinajs.cn/list=s_sh000001";
    private final static String URL_LOGIN = NetConst.HTTP_PREFIX + "login";
    private EditText et_username; // 声明一个编辑框对象
    private EditText et_password; // 声明一个编辑框对象
    private TextView tv_result; // 声明一个文本视图对象
    private TextView btn_login;
    private TextView btn_l;
    private CheckBox cb_lcc1;
    String ok="进入成功";
    String fail="账号或密码错误";
    String Rsp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);


        et_username = findViewById(R.id.et_1);
        et_password = findViewById(R.id.et_2);
        tv_result = findViewById(R.id.tv_result);
        cb_lcc1 = findViewById(R.id.cb_lcc1);
        /*findViewById(R.id.btn_test).setOnClickListener(v ->{
            startActivity(new Intent(this, ChatBotActivity.class));
        });*/
        findViewById(R.id.btn_login).setOnClickListener(v -> {
            if(cb_lcc1.isChecked() != true) {
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
            }
            postForm();
        });



        b_rgr=findViewById(R.id.btn_register);
        b_rgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=null;
                intent=new Intent(MainActivity.this, activity_fradash.class);
                startActivity(intent);
            }
        });
        btn_login=findViewById(R.id.btn_t_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("riskcon",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", "username");
                editor.putString("uid", "00000012");
                editor.commit();
                Toast.makeText(getApplicationContext(),ok,Toast.LENGTH_SHORT).show();
                Intent intent=null;
                intent=new Intent(MainActivity.this, Fragement_activity.class);
                startActivity(intent);
            }
        });



    }
    private void postForm() {
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();
        // 创建一个表单对象
        /*FormBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();*/
        String jsonString = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonString = jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 创建一个POST方式的请求结构
        RequestBody body = RequestBody.create(jsonString, MediaType.parse("application/json"));
        OkHttpClient client = new OkHttpClient(); // 创建一个okhttp客户端对象
        // 创建一个POST方式的请求结构
        Request request = new Request.Builder().post(body).url(URL_LOGIN).build();
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
                System.out.println(resp);
                Rsp=resp;
                System.out.println(Rsp);
                // 回到主线程操纵界面
                runOnUiThread(() -> tv_result.setText("调用登录接口返回：\n"+resp));
                if(!Objects.equals(resp,"20000")  )
                {
                    SharedPreferences sharedPreferences = getSharedPreferences("riskcon",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username);
                    editor.putString("uid", Rsp);
                    editor.commit();

                    Intent intent=null;
                    intent=new Intent(MainActivity.this, Fragement_activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(),ok,Toast.LENGTH_SHORT).show();
                    Looper.loop();


                }
                if (Objects.equals(resp,"20000"))
                {
                Looper.prepare();
                Toast.makeText(getApplicationContext(),fail,Toast.LENGTH_SHORT).show();
                Looper.loop();
                 }

            }
        });
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}