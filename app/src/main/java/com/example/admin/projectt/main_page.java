package com.example.admin.projectt;

import android.content.SharedPreferences;


import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.logging.Logger;


public class main_page extends AppCompatActivity implements Constant {
    Intent intent;
    SharedPreferences chatData;
    SharedPreferences settler;
    ImageView post,logout,history_btn,guide_btn,about_btn,setting_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        settler = getSharedPreferences("set", 0);
        post = (ImageView) findViewById(R.id.postBtm);
        logout = (ImageView) findViewById(R.id.btnLogout);
        history_btn = (ImageView) findViewById(R.id.button3);
        guide_btn = (ImageView) findViewById(R.id.button2);
        about_btn = (ImageView) findViewById(R.id.aboutBtn);
        setting_btn= (ImageView) findViewById(R.id.button6);
        chatData = getSharedPreferences(CHAT_SHAREDPREFERENCES, 0);

        checkIfIsInChatRoom();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1340);
        }

        history_btn.setOnClickListener(new View.OnClickListener() {
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
                setting.edit().putBoolean(LOGIN_STATE,false)
                              .putBoolean(LOGIN_RETRIEVE_SERVICE,false)
                              .apply();
                Intent i = new Intent(main_page.this, first_page.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
    public void gotogui(View v){
        Intent it =new Intent(this,guidance.class);
        startActivity(it);
    }

    @Override
    public void onResume(){
        super.onResume();
        checkIfIsInChatRoom();
        settler = getSharedPreferences("set", 0);


        int colo = settler.getInt("color",1);
        if(colo==1) {
            if(chatData.getBoolean(CHAT_STATE, false)){
                post.setImageResource(R.drawable.blue_chatstatus);
            }
            else{
                post.setImageResource(R.drawable.blue_post);
            }
           
            logout.setImageResource(R.drawable.blue_logout);
            history_btn.setImageResource(R.drawable.blue_histroy);
            guide_btn.setImageResource(R.drawable.blue_use);
            about_btn.setImageResource(R.drawable.blue_person);
            setting_btn.setImageResource(R.drawable.blue_settings);
        }
        else if(colo==2){
            if(chatData.getBoolean(CHAT_STATE, false)){
                post.setImageResource(R.drawable.red_chatstatus);
            }
             else{
              post.setImageResource(R.drawable.red_post);
            }
            
            logout.setImageResource(R.drawable.red_logout);
            history_btn.setImageResource(R.drawable.red_histroy);
            guide_btn.setImageResource(R.drawable.red_use);
            about_btn.setImageResource(R.drawable.red_person);
            setting_btn.setImageResource(R.drawable.red_settings);
        }
        else if(colo==3){
            if(chatData.getBoolean(CHAT_STATE, false)){
                post.setImageResource(R.drawable.yellow_chatstatus);
            }
            else{
                  post.setImageResource(R.drawable.yellow_post);
            }
            logout.setImageResource(R.drawable.yellow_logout);
            history_btn.setImageResource(R.drawable.yellow_histroy);
            guide_btn.setImageResource(R.drawable.yellow_use);
            about_btn.setImageResource(R.drawable.yellow_person);
            setting_btn.setImageResource(R.drawable.yellow_settings);
        }
        else if(colo==4){
            if(chatData.getBoolean(CHAT_STATE, false)){
                post.setImageResource(R.drawable.purple_chatstatus);
            }
            else{
                 post.setImageResource(R.drawable.purple_post);
            }
            logout.setImageResource(R.drawable.purple_logout);
            history_btn.setImageResource(R.drawable.purple_histroy);
            guide_btn.setImageResource(R.drawable.purple_use);
            about_btn.setImageResource(R.drawable.purple_person);
            setting_btn.setImageResource(R.drawable.purple_settings);
        }


    }
    private void checkIfIsInChatRoom(){
        if (chatData.getBoolean(CHAT_STATE, false)) {
            intent = new Intent(main_page.this, ChatroomActivity.class);
        } else {
            intent = new Intent(main_page.this, SendRequest.class);
        }
    }


}
