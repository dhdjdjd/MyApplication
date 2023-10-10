package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

public class AnHome4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_an_home4);

        Toolbar tl_ah1 = findViewById(R.id.tl_ah1);
        tl_ah1.setTitle("还款细则");
        setSupportActionBar(tl_ah1);
        tl_ah1.setNavigationIcon(R.drawable.bbnic1);
        tl_ah1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(mContext, LowCashActivity.class));
                finish();
            }
        });

        findViewById(R.id.but_ah1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}