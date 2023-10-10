package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class FunctionActivity extends AppCompatActivity {
    private Button b_f_1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
       /* b_f_1=findViewById(R.id.bu_f_1);
        String ok="进入成功";
        b_f_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),ok,Toast.LENGTH_SHORT).show();
                Intent intent=null;
                intent=new Intent(FunctionActivity.this,Main_pace.class);
                startActivity(intent);
            }
        });*/
        MyThread t1 = new MyThread();
        t1.start();

    }

    public class MyThread extends Thread {
        public void run(){
            try {
                Thread.sleep(1000);
                Intent intent=null;
                intent=new Intent(FunctionActivity.this, Fragement_activity.class);
                startActivity(intent);
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}