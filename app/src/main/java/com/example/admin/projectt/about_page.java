package com.example.admin.projectt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.*;

public class about_page extends AppCompatActivity {

    TextView email,nickname,pwdhint;
    static String temp1,temp2,temp3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_page);
        email = (TextView) findViewById(R.id.textemail);
        nickname =(TextView) findViewById(R.id.textnickname);
        pwdhint=(TextView)findViewById(R.id.textpwdhint);

            Bundle bundle = getIntent().getExtras();
        if(bundle!=null){                                                   //第一次註冊傳過來的訊息(只有第1次註冊完會送bundle)



            email.setText(bundle.getString("email"));

            nickname.setText(bundle.getString("nickname"));

            pwdhint.setText(bundle.getString("pwdhint"));
            temp1=email.getText().toString();                                //把註冊完傳過來的email string存起來
            temp2=nickname.getText().toString();                             //把註冊完傳過來的nickname string存起來
            temp3=pwdhint.getText().toString();                              //把註冊完傳過來的密碼提示的string存起來


        }
        else{                                                               //之後直接把之前存起來的string setText
            email.setText(temp1);

            nickname.setText(temp2);

            pwdhint.setText(temp3);


        }


    }

    public void goback(View v){
        Intent it =new Intent(this,main_page.class);
        startActivity(it);
    }
    public void gotomessagecenter(View v){                                  //轉去messagecenter的activity
        Intent it =new Intent(this,messagecenter.class);
        startActivity(it);
    }





}
