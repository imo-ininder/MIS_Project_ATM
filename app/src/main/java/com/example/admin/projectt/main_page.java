package com.example.admin.projectt;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;


public class main_page extends AppCompatActivity implements Constant{
    Intent intent;
    SharedPreferences chatData;
    Button post,logout,history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        post = (Button) findViewById(R.id.postBtm);
        logout = (Button) findViewById(R.id.btnLogout);
        history = (Button) findViewById(R.id.btnHistory);
        chatData = getSharedPreferences(CHAT_SHAREDPREFERENCES, 0);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1340);
        }
        if (chatData.getBoolean(CHAT_STATE, false)) {
            post.setText("ChatRoom");
            intent = new Intent(main_page.this, ChatroomActivity.class);
        } else {
            intent = new Intent(main_page.this, SendRequest.class);
        }

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(main_page.this,HistoryActivity.class);
                startActivity(i);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences setting = getSharedPreferences(LOGIN_SHAREDPREFERENCE, 0);
                setting.edit().putBoolean(LOGIN_STATE,false).apply();
                Intent i = new Intent(main_page.this, first_page.class);
                startActivity(i);
                main_page.this.finish();
            }
        });
    }
    public void unfinish(View v){
        Toast tos= Toast.makeText(this,"還沒做啦",Toast.LENGTH_SHORT);
        tos.show();
    }
    public void gotosettings(View v){
        Intent it =new Intent(this,settings_page.class);
        startActivity(it);
    }
    public void gotoabout(View v){
        Intent it =new Intent(this,about_page.class);
        startActivity(it);
    }
}
