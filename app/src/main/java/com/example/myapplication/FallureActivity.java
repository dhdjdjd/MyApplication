package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FallureActivity extends AppCompatActivity {
    private Context mContext = this;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fallure);

        Toolbar tl_fub1 = findViewById(R.id.tl_fub);
        tl_fub1.setTitle("申请失败");
        setSupportActionBar(tl_fub1);

        Bundle sbundle = getIntent().getExtras();
        int iscore = sbundle.getInt("Score");
        String sscore = "" + iscore;
        TextView tv_fub2 = findViewById(R.id.tv_fub2);
        tv_fub2.setText(sscore);
        btn = findViewById(R.id.fail_but);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=null;
                intent=new Intent(mContext,Fragement_activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}