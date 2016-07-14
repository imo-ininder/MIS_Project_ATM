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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        final Intent intent = new Intent();
        final Button button = (Button) findViewById(R.id.testBtn1);
        final Button button2 = (Button) findViewById(R.id.testBtn2);
        final Button button3 = (Button) findViewById(R.id.testBtn3);
        final Button button4 = (Button) findViewById(R.id.btnLogout);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(main_page.this,SendRequest.class);
                startActivity(i);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putBoolean("flag", true);
                intent.putExtras(b);
                intent.setClass(main_page.this, RetrievePostService.class);
                startService(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopService(intent);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences setting = getSharedPreferences("LoginData",0);
                setting.edit().putString("CONFIRM","LOGOUT").commit();
                Intent i = new Intent(main_page.this,first_page.class);
                startActivity(i);
                main_page.this.finish();
            }
        });
    }
}
