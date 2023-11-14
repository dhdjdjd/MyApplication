package com.example.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.myapplication.FallureActivity;
import com.example.myapplication.LowCashActivity;
import com.example.myapplication.MainActivity;

import com.example.myapplication.R;
import com.example.myapplication.SuccessScoreActivity;
import com.example.myapplication.bean.BorrowType;

import java.util.List;

public class RecyclerLinearAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final static String TAG = "RecyclerLinearAdapter";
    private Context mContext; // 声明一个上下文对象
    private List<BorrowType> mPublicList; // 公众号列表

    public RecyclerLinearAdapter(Context context, List<BorrowType> publicList) {
        mContext = context;
        mPublicList = publicList;
    }

    // 获取列表项的个数
    public int getItemCount() {
        return mPublicList.size();
    }

    // 创建列表项的视图持有者
    public ViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {
        // 根据布局文件item_linear.xml生成视图对象
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_linear, vg, false);
        return new ItemHolder(v);
    }

    // 绑定列表项的视图持有者
    public void onBindViewHolder(ViewHolder vh, final int position) {
        ItemHolder holder = (ItemHolder) vh;
        holder.iv_pic.setImageResource(mPublicList.get(position).pic_id);
        holder.tv_title.setText(mPublicList.get(position).title);
        holder.tv_desc.setText(mPublicList.get(position).desc);
        int pid = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemC lick(v, position);
                }*/
                switch (pid){
                    case 0:
                        Intent intent1 = new Intent(mContext, LowCashActivity.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("BType","CashBorrow");
                        intent1.putExtras(bundle1);
                        mContext.startActivity(intent1);
                        break;
                    /*case 1:
                        Intent intent2 = new Intent(mContext, SuccessScoreActivity.class);
                        mContext.startActivity(intent2);
                        break;
                    case 2:
                        Intent intent3 = new Intent(mContext, FallureActivity.class);
                        mContext.startActivity(intent3);
                        break;*/
                    default:
                        Intent intentn = new Intent(mContext, LowCashActivity.class);
                        Bundle bundlen = new Bundle();
                        //bundlen.putString("BType","Others");
                        bundlen.putString("BType","CashBorrow");
                        intentn.putExtras(bundlen);
                        mContext.startActivity(intentn);
                        break;

                }
            }

        });
    }

//    // 获取列表项的类型，这里的类型与onCreateViewHolder方法的viewType参数保持一致
//    public int getItemViewType(int position) {
//        return 0;
//    }
//
//    // 获取列表项的编号
//    public long getItemId(int position) {
//        return position;
//    }

    // 定义列表项的视图持有者
    public class ItemHolder extends ViewHolder {
        public ImageView iv_pic; // 声明列表项图标的图像视图
        public TextView tv_title; // 声明列表项标题的文本视图
        public TextView tv_desc; // 声明列表项描述的文本视图

        public ItemHolder(View v) {
            super(v);
            iv_pic = v.findViewById(R.id.iv_pic);
            tv_title = v.findViewById(R.id.tv_title);
            tv_desc = v.findViewById(R.id.tv_desc);
        }
    }

    /*private AdapterView.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onItemClick(View view, int position) {
        String desc = String.format("您点击了第%d项，内容是%s", position+1, mPublicList[position]);
        Toast.makeText(mContext, desc, Toast.LENGTH_SHORT).show();
    }*/

}
