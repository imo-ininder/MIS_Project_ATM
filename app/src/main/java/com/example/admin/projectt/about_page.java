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
    Button btn;
  /* String[] msgcounts = getResources().getStringArray(R.array.test);*/




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_page);
        email = (TextView) findViewById(R.id.textemail);
        nickname =(TextView) findViewById(R.id.textnickname);
        pwdhint=(TextView)findViewById(R.id.textpwdhint);
        /*btn=(Button)findViewById(R.id.messagebtn);
        btn.setText(""+msgcounts.length);*/
            Bundle bundle = getIntent().getExtras();
        if(bundle!=null){



            email.setText(bundle.getString("email"));

            nickname.setText(bundle.getString("nickname"));

            pwdhint.setText(bundle.getString("pwdhint"));
            temp1=email.getText().toString();
            temp2=nickname.getText().toString();
            temp3=pwdhint.getText().toString();


        }
        else{
            email.setText(temp1);

            nickname.setText(temp2);

            pwdhint.setText(temp3);


        }


    }

    public void goback(View v){
        Intent it =new Intent(this,main_page.class);
        startActivity(it);
    }
    public void gotomessagecenter(View v){
        Intent it =new Intent(this,messagecenter.class);
        startActivity(it);
    }





}
