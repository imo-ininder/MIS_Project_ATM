package com.example.admin.projectt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;

import com.firebase.client.Firebase;

public class first_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.first_page_layout);


    }

    public void signuup(View view){
        Intent it =new Intent(this,sign_up.class);
        startActivity(it);
    }

    public void gotomainpage(View view){
        Intent it =new Intent(this,main_page.class);
        startActivity(it);
    }


    public void pwd_help(View view){

    }



}
