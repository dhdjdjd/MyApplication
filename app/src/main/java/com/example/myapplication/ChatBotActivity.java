package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Util.ChatUtil;
import com.example.myapplication.Util.DateUtil;
import com.example.myapplication.Util.Utils;
import com.example.myapplication.Util.ViewUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChatBotActivity extends AppCompatActivity{
    private EditText et_input; // 声明一个编辑框对象
    private ScrollView sv_chat; // 声明一个滚动视图对象
    private LinearLayout ll_show; // 声明一个聊天窗口的线性布局对象
    private int dip_margin; // 每条聊天记录的四周空白距离
    private int CHOOSE_CODE = 3; // 只在相册挑选图片的请求码

    private String mSelfName, mFriendName; // 自己名称，好友名称
    //private Socket mSocket; // 声明一个套接字对象
    private String mMinute = "00:00"; // 时间提示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 保持屏幕常亮
        mSelfName = getIntent().getStringExtra("self_name");
        mFriendName = getIntent().getStringExtra("friend_name");
        initView(); // 初始化视图
    }
    // 初始化视图
    private void initView() {
        dip_margin = Utils.dip2px(this, 5);
        TextView tv_title = findViewById(R.id.tv_title);
        et_input = findViewById(R.id.et_input);
        sv_chat = findViewById(R.id.sv_chat);
        ll_show = findViewById(R.id.ll_show);

        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_send).setOnClickListener(v -> sendMessage());
        /*tv_title.setText(mFriendName);*/
        tv_title.setText("AI答疑");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // 发送聊天消息
    private void sendMessage() {
        String content = et_input.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请输入聊天消息", Toast.LENGTH_SHORT).show();
            return;
        }
        et_input.setText("");
        ViewUtil.hideOneInputMethod(this, et_input); // 隐藏软键盘
        appendChatMsg(mSelfName, content, true); // 往聊天窗口添加文本消息
        appendChatMsg("ChatGpt","请稍侯",false);
        getAnsFromChatGPTAndSet(content);
    }

    // 往聊天窗口添加聊天消息
    private void appendChatMsg(String name, String content, boolean isSelf) {
        appendNowMinute(); // 往聊天窗口添加当前时间
        // 把单条消息的线性布局添加到聊天窗口上
        ll_show.addView(ChatUtil.getChatView(this, name, content, isSelf));
        // 延迟100毫秒后启动聊天窗口的滚动任务
        new Handler(Looper.myLooper()).postDelayed(() -> {
            sv_chat.fullScroll(ScrollView.FOCUS_DOWN); // 滚动到底部
        }, 100);
    }

    // 往聊天窗口添加当前时间
    private void appendNowMinute() {
        String nowMinute = DateUtil.getNowMinute();
        // 分钟数切换时才需要添加当前时间
        if (!mMinute.substring(0, 4).equals(nowMinute.substring(0, 4))) {
            mMinute = nowMinute;
            ll_show.addView(ChatUtil.getHintView(this, nowMinute, dip_margin));
        }
    }

    private void getAnsFromChatGPTAndSet(String question){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(300, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(300, TimeUnit.SECONDS)//设置写的超时时间
                .build();; // 创建一个okhttp客户端对象
        Request request = new Request.Builder().get().url("http://172.20.10.6:8088/chat/bot/ans?question=" + question).build();
        Call call = client.newCall(request); // 根据请求结构创建调用对象
        // 加入HTTP请求队列。异步调用，并设置接口应答的回调方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    appendChatMsg("ChatGpt", e.getMessage(),false);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String resp = response.body().string();
                runOnUiThread(() -> {
                    appendChatMsg("ChatGpt", resp,false);
                });
            }
        });
    }
}