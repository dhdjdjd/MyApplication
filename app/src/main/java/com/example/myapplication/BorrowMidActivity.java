package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.example.myapplication.bean.AppInfo;
import com.example.myapplication.bean.CallLogRecord;
import com.example.myapplication.bean.Contact;
import com.example.myapplication.task.GetAddressTask;
import com.example.myapplication.Util.BMUtil;
import com.example.myapplication.Util.CommunicationUtil;
import com.example.myapplication.Util.DateUtil;
import com.example.myapplication.Util.PemUtil;
import com.example.myapplication.Util.SwitchUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BorrowMidActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext = this;
    private Map<String, String> providerMap = new HashMap<>();
    //private TextView tv_location; // 声明一个文本视图对象
    private String mLocationDesc = ""; // 定位说明
    private LocationManager mLocationMgr; // 声明一个定位管理器对象
    private Handler mHandler = new Handler(Looper.myLooper()); // 声明一个处理器对象
    private boolean isLocationEnable = false; // 定位服务是否可用
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET
    };
    private List<CallLogRecord> callLogRecords;
    private List<AppInfo> appList;
    private List<Contact> contactList;
    private String sendLoc = "";

    int contactNum = 0;
    int appNum = 0;
    int calllogNum = 0;

    double sendLongitude = 0;
    double sendLatitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_mid);

        findViewById(R.id.but_bm1).setOnClickListener(this);

        Toolbar tl_bmb = findViewById(R.id.tl_bmb);
        tl_bmb.setTitle("贷中信息收集");
        setSupportActionBar(tl_bmb);
        tl_bmb.setNavigationIcon(R.drawable.bbnic1);
        tl_bmb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*startActivity(new Intent(mContext, LowCashActivity.class));*/
                finish();
            }
        });

        providerMap.put("gps", "卫星定位");
        providerMap.put("network", "网络定位");
        //tv_location = findViewById(R.id.tv_location);
        SwitchUtil.checkLocationIsOpen(this, "需要打开定位功能才能查看定位信息");

        callLogRecords = CommunicationUtil.getDataList(this);
        appList = CommunicationUtil.getAllAppInfo(this, false);
        contactList = CommunicationUtil.readAllContacts(getContentResolver());
        mHandler.removeCallbacks(mRefresh); // 移除定位刷新任务
        initLocation(); // 初始化定位服务
        //mHandler.postDelayed(mRefresh, 100); // 延迟100毫秒启动定位刷新任务
        for(CallLogRecord callLogRecord : callLogRecords){

            String str = callLogRecord.GfromLoc;
            System.out.println(str);
            if(str != null) {
                //int tmp = 0;
                calllogNum += matchStringByIndexOf(str, "缅甸");
                calllogNum += matchStringByIndexOf(str, "土耳其");
                calllogNum += matchStringByIndexOf(str, "新加坡");
                calllogNum += matchStringByIndexOf(str, "澳门");
                /*if (tmp <= 0) {
                    calllogNum++;
                }*/
                //calllogNum += matchStringByIndexOf(str, "未知");
            }
            else {
                calllogNum++;
            }
        }

        for(Contact contact : contactList){
            String str = contact.name;
            if(str != null) {
                contactNum += matchStringByIndexOf(str, "贷");
            }
        }

        for(AppInfo appInfo : appList){
            String str = appInfo.package_name;
            if(str != null) {
                appNum += matchStringByIndexOf(str, "贷");
                appNum += matchStringByIndexOf(str, "借");
                appNum += matchStringByIndexOf(str, "loan");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.removeCallbacks(mRefresh); // 移除定位刷新任务
        initLocation(); // 初始化定位服务
        mHandler.postDelayed(mRefresh, 100); // 延迟100毫秒启动定位刷新任务
    }

    private int matchStringByIndexOf(String parent, String child)
    {
        String[] array = parent.split(child);
        //System.out.println( "匹配个数为" + (array.length-1) );
        return array.length-1;
    }


    // 初始化定位服务
    private void initLocation() {
        // 从系统服务中获取定位管理器
        mLocationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria(); // 创建一个定位准则对象
        // 设置定位精确度。Criteria.ACCURACY_COARSE表示粗略，Criteria.ACCURACY_FIN表示精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(true); // 设置是否需要海拔信息
        criteria.setBearingRequired(true); // 设置是否需要方位信息
        criteria.setCostAllowed(true); // 设置是否允许运营商收费
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 设置对电源的需求
        // 获取定位管理器的最佳定位提供者
        String bestProvider = mLocationMgr.getBestProvider(criteria, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 实测发现部分手机的android11系统使用卫星定位会没返回
            bestProvider = "network";
        }
        if (mLocationMgr.isProviderEnabled(bestProvider)) { // 定位提供者当前可用
            //tv_location.setText("正在获取" + providerMap.get(bestProvider) + "对象");
            mLocationDesc = String.format("定位类型为%s", providerMap.get(bestProvider));
            beginLocation(bestProvider); // 开始定位
            isLocationEnable = true;
        } else { // 定位提供者暂不可用
            //tv_location.setText(providerMap.get(bestProvider) + "不可用");
            isLocationEnable = false;
        }
    }

    // 显示定位结果文本
    private void showLocation(Location location) {
        if (location != null) {
            // 创建一个根据经纬度查询详细地址的任务
            GetAddressTask task = new GetAddressTask(this, location, address -> {
                String desc = String.format("%s\n定位信息如下： " +
                                "\n\t定位时间为%s，" + "\n\t经度为%f，纬度为%f，" +
                                "\n\t高度为%d米，精度为%d米，" +
                                "\n\t详细地址为%s。",
                        mLocationDesc, DateUtil.formatDate(location.getTime()),
                        location.getLongitude(), location.getLatitude(),
                        Math.round(location.getAltitude()), Math.round(location.getAccuracy()),
                        address);
                //tv_location.setText(desc);
                sendLongitude = location.getLongitude();
                sendLatitude = location.getLatitude();
                sendLoc = desc;
            });
            task.start(); // 启动地址查询任务
        } else {
            //tv_location.setText(mLocationDesc + "\n暂未获取到定位对象");
        }
    }

    // 开始定位
    private void beginLocation(String method) {
        // 检查当前设备是否已经开启了定位功能
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "请授予定位权限并开启定位功能", Toast.LENGTH_SHORT).show();
            return;
        }
        // 设置定位管理器的位置变更监听器
        mLocationMgr.requestLocationUpdates(method, 300, 0, mLocationListener);
        // 获取最后一次成功定位的位置信息
        Location location = mLocationMgr.getLastKnownLocation(method);
        showLocation(location); // 显示定位结果文本
    }

    // 定义一个位置变更监听器
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location); // 显示定位结果文本
        }

        @Override
        public void onProviderDisabled(String arg0) {
        }

        @Override
        public void onProviderEnabled(String arg0) {
        }

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        }
    };

    // 定义一个刷新任务，若无法定位则每隔一秒就尝试定位
    private Runnable mRefresh = new Runnable() {
        @Override
        public void run() {
            if (!isLocationEnable) {
                initLocation(); // 初始化定位服务
                mHandler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationMgr.removeUpdates(mLocationListener); // 移除定位管理器的位置变更监听器
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_bm1:{
                String jsonString = "";
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date curDate =  new Date(System.currentTimeMillis());
                String nowtime =  formatter.format(curDate);
                String projectid = "P0000001";

                SharedPreferences sharedPreferences = getSharedPreferences("riskcon",MODE_PRIVATE);
                String uid =sharedPreferences.getString("uid","");

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("contactNum", contactNum);
                    jsonObject.put("appNum", appNum);
                    jsonObject.put("calllogNum", calllogNum);
                    //jsonObject.put("sendLoc", sendLoc);
                    jsonObject.put("sendLongitude", sendLongitude);
                    jsonObject.put("sendLatitude", sendLatitude);
                    jsonObject.put("uid",uid);
                    jsonString = jsonObject.toString();
                } catch (Exception e) {
                    e.printStackTrace();

                }
                System.out.println(jsonString);
                String pram = "?contactNum=" + contactNum + "&appNum=" + appNum + "&calllogNum=" + calllogNum + "&sendLongitude=" + sendLongitude + "&sendLatitude=" + sendLatitude +"&uid=" + uid;
                // 创建一个POST方式的请求结构
                System.out.println(pram);
                RequestBody body = RequestBody.create(jsonString, MediaType.parse("application/json"));
                OkHttpClient client = new OkHttpClient(); // 创建一个okhttp客户端对象
                Request request = new Request.Builder().post(body).url("http://172.20.10.5:8880/user/calculate"+pram).build();
                Call call = client.newCall(request); // 根据请求结构创建调用对象
                // 加入HTTP请求队列。异步调用，并设置接口应答的回调方法
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) { // 请求失败
                        // 回到主线程操纵界面
                        runOnUiThread(() -> Toast.makeText(BorrowMidActivity.this, "调用HTTP接口报错："+e.getMessage(), Toast.LENGTH_SHORT).show());
                        //tv_result.setText("调用登录接口报错："+e.getMessage()));
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException { // 请求成功
                        String resp = response.body().string();
                        // 回到主线程操纵界面
                        runOnUiThread(() -> {
                            Toast.makeText(BorrowMidActivity.this, "调用HTTP接口返回：\n"+resp, Toast.LENGTH_SHORT).show();
                            /*startActivity(new Intent(mContext,Fragement_activity.class));*/
                            Intent intent = new Intent(mContext, Fragement_activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        });

                        //tv_result.setText("调用登录接口返回：\n"+resp));
                    }
                });
            }
        }
    }
}