package com.example.admin.projectt;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;


public class main_page extends AppCompatActivity{
    Intent intent;
    SharedPreferences chatData;
    Button post,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        post = (Button) findViewById(R.id.postBtm);
        logout = (Button) findViewById(R.id.btnLogout);
        chatData = getSharedPreferences("ATM_chatData", 0);
        if (chatData.getBoolean("chatState", false)) {
            post.setText("ChatRoom");
            intent = new Intent(main_page.this, ChatroomActivity.class);
        } else {
            intent = new Intent(main_page.this, SendRequest.class);
        }

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences setting = getSharedPreferences("LoginData", 0);
                setting.edit().putString("CONFIRM", "LOGOUT").apply();
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
