package com.example.admin.projectt;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;



public class sign_up extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        Button b1 = (Button)findViewById( R.id.button4);

        b1.setOnClickListener(new Button.OnClickListener() {      // Perform action on click
            public void onClick(View v) {
                EditText nicknamee=(EditText)findViewById(R.id.edit_nickname);
                EditText emaill=(EditText)findViewById(R.id.edit_pwd_hint);
                EditText pwdhintt=(EditText)findViewById(R.id.edit_email);
                Intent it = new Intent(sign_up.this, about_page.class );

                Bundle information = new Bundle();                                        //送個人資訊要用到的information
                information.putString("nickname", nicknamee.getText().toString());        //把綽號放進去bundle，識別碼是nickname
                information.putString("email", emaill.getText().toString());              //把電子信箱放進去bundle，識別碼是email
                information.putString("pwdhint",  pwdhintt.getText().toString());         //把密碼提示放進去bundle，識別碼是pwdhint
                it.putExtras(information);                                                //把bundle放進去intent
                startActivity(it);                                                        //傳送intent
            }
        });
    }








}
