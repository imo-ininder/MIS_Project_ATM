package com.example.admin.projectt;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.AutoCompleteTextView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;


public class first_page extends AppCompatActivity implements Constant{
    private UserData userData;
    private ProgressDialog pd;
    private Boolean loginFlag = false;
    private AutoCompleteTextView textemail;
    
    SharedPreferences setting;
    Firebase ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setting = getSharedPreferences(LOGIN_SHAREDPREFERENCE,0);
        if(setting.getBoolean(LOGIN_STATE,false)){
            Intent i = new Intent(first_page.this,main_page.class);
            startActivity(i);
            first_page.this.finish();
        }

        setContentView(R.layout.first_page_layout);
        Firebase.setAndroidContext(this);
        ref = new Firebase("https://mis-atm.firebaseio.com/userdata");
        textemail=(AutoCompleteTextView)findViewById(R.id.loginEmail);
        Button signInBtn = (Button) findViewById(R.id.signInButton);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = (EditText) findViewById(R.id.loginEmail);
                EditText password = (EditText) findViewById(R.id.loginPassword);
                new Checking().execute("email","password",email.getText().toString(),password.getText().toString());
            }
        });
    }
    public void signuup(View view){
        Intent it =new Intent(this,sign_up.class);
        startActivity(it);
    }
    public void pwd_help(View v) {
            if ("".equals(textemail.getText().toString().trim())) {
                Toast.makeText(first_page.this, "E-mail不可為空白", Toast.LENGTH_SHORT)
                        .show();
            } else {

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int flag = 0;
                        for (DataSnapshot d : dataSnapshot.getChildren()) {

                            if (!d.child("email").getValue().toString().equals(textemail.getText().toString())) {
                                continue;
                            } else {
                                flag = 1;
                                Toast.makeText(first_page.this, "密碼提示:"+d.child("passwordHint").getValue().toString(), Toast.LENGTH_SHORT)
                                        .show();
                                break;

                            }
                        }
                        if (flag == 0) {
                            Toast.makeText(first_page.this, "找不到此帳戶", Toast.LENGTH_SHORT)
                                    .show();
                            return;

                        }


                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
            }
        }

    class Checking extends AsyncTask<String,Void,Boolean> {
        @Override
        protected void onPreExecute(){
            pd = ProgressDialog.show(first_page.this,"Checking","Checking",true,false);
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(final String ...param){
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot d:dataSnapshot.getChildren()) {

                        if(!d.child(param[0]).getValue().toString().equals(param[2])){
                            loginFlag = false;
                        }else{
                            if(d.child(param[1]).getValue().toString().equals(param[3])){
                                loginFlag = true;
                                userData = d.getValue(UserData.class);
                                break;
                            }else{
                                loginFlag = false;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
        @Override
        protected void onPostExecute(Boolean b){
            pd.dismiss();
            if(loginFlag){
                setting.edit().putString("email",userData.getEmail())
                        .putString("id",userData.getId())
                        .putString("name",userData.getName())
                        .putString("pwdHint",userData.getPasswordHint())
                        .putBoolean(LOGIN_STATE,true)
                        .apply();


                Intent i = new Intent(first_page.this,main_page.class);
                startActivity(i);
                first_page.this.finish();
            }
            else {
                Toast.makeText(first_page.this, "Login Fail", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void gotottt(View v){
        Intent it =new Intent(this,main_page.class);
        startActivity(it);
    }


}
