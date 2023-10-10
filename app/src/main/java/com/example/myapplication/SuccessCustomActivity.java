package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SuccessCustomActivity extends AppCompatActivity {
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_custom);

        Toolbar tl_scb = findViewById(R.id.tl_scb);
        tl_scb.setTitle("申请成功");
        setSupportActionBar(tl_scb);
        tl_scb.setNavigationIcon(R.drawable.bbnic1);
        tl_scb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,BeforeBActivity.class));
            }
        });

        Bundle scbundle = getIntent().getExtras();
        int iscore = scbundle.getInt("Score");
        System.out.println(iscore);
        String sccore = "" + iscore;
        TextView tv_scb2 = findViewById(R.id.tv_scb2);
        tv_scb2.setText(sccore);
    }
}