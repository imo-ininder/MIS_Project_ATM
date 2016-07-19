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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;


public class first_page extends AppCompatActivity {
    private UserData userData;
    private ProgressDialog pd;
    private Boolean loginFlag = false;
    File file;
    Firebase ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        file = new File("/data/data/com.example.admin.projectt/shared_prefs","LoginData.xml");
        if(file.exists()){
            SharedPreferences setting = getSharedPreferences("LoginData",0);
            if(setting.getString("CONFIRM","").equals("SUCCESS")){
                Intent i = new Intent(first_page.this,main_page.class);
                startActivity(i);
                first_page.this.finish();
            }
        }
        setContentView(R.layout.first_page_layout);
        Firebase.setAndroidContext(this);
        ref = new Firebase("https://mis-atm.firebaseio.com/userdata");
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
    public void pwd_help(View view){

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
                SharedPreferences setting = getSharedPreferences("LoginData",0);
                setting.edit().putString("email",userData.getEmail())
                        .putString("id",userData.getId())
                        .putString("name",userData.getName())
                        .putString("pwdHint",userData.getPasswordHint())
                        .putString("CONFIRM","SUCCESS")
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

}
