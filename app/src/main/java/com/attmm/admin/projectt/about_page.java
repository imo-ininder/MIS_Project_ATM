package com.attmm.admin.projectt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.*;

public class about_page extends AppCompatActivity {

    TextView email,nickname,pwdhint;
    SharedPreferences setting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_page);
        setting = getSharedPreferences("LoginData",0);
        email = (TextView) findViewById(R.id.textemail);
        nickname =(TextView) findViewById(R.id.textnickname);
        pwdhint=(TextView)findViewById(R.id.textpwdhint);

        email.setText(setting.getString("email",""));
        nickname.setText(setting.getString("id",""));
        pwdhint.setText(setting.getString("pwdHint",""));


    }

    public void goback(View v){
        Intent it =new Intent(this,main_page.class);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
        finish();
    }

}
