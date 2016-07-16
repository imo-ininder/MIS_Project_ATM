package com.example.admin.projectt;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.widget.Button;

import java.io.File;

public class main_page extends AppCompatActivity{
    Intent serviceIntent,intent;
    SharedPreferences chatData;
    Button post,button2,button3,button4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        serviceIntent = new Intent();
        Bundle b = new Bundle();
        b.putBoolean("flag", true);
        chatData = getSharedPreferences("ATM_chatData",0);
        serviceIntent.putExtras(b);
        serviceIntent.setClass(main_page.this, RetrievePostService.class);
        post = (Button) findViewById(R.id.testBtn1);
        button2 = (Button) findViewById(R.id.testBtn2);
        button3 = (Button) findViewById(R.id.testBtn3);
        button4 = (Button) findViewById(R.id.btnLogout);
        if(chatData.getBoolean("chatState",false)){
            post.setText("ChatRoom");
            intent = new Intent(main_page.this, ChatroomActivity.class);
        }
        else {
            intent = new Intent(main_page.this, SendRequest.class);
        }
        post.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startService(serviceIntent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopService(serviceIntent);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences setting = getSharedPreferences("LoginData",0);
                setting.edit().putString("CONFIRM","LOGOUT").apply();
                Intent i = new Intent(main_page.this,first_page.class);
                startActivity(i);
                main_page.this.finish();
            }
        });
    }
}
