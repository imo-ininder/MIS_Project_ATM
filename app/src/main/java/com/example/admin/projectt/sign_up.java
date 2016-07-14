package com.example.admin.projectt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class sign_up extends AppCompatActivity {
    EditText email,password,passwordHint,passwordConfirm,name,id;
    RadioGroup radioGroup;
    private boolean emailFlag ,idFlag;
    private ProgressDialog pd;
    Firebase ref = new Firebase("https://mis-atm.firebaseio.com/userdata");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        Firebase.setAndroidContext(this);
        final Button submitBtn = (Button) findViewById(R.id.submitUserData);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = (EditText)findViewById(R.id.edit_email);
                password = (EditText)findViewById(R.id.edit_pwd);
                passwordHint = (EditText)findViewById(R.id.edit_pwd_hint);
                passwordConfirm = (EditText)findViewById(R.id.edit_pwd_check);
                name = (EditText)findViewById(R.id.edit_name);
                id = (EditText)findViewById(R.id.edit_nickname);
                radioGroup = (RadioGroup) findViewById(R.id.genderGroup);
                new Checking().execute("email","id",email.getText().toString(),id.getText().toString());
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    class Checking extends AsyncTask<String,Void,Boolean>{
        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(sign_up.this,"Checking","Checking",true,false);
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(final String... param) {
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data: dataSnapshot.getChildren()) {
                        if(param[2].equals(data.child(param[0]).getValue().toString())) {
                            emailFlag = false;
                            break;
                        }else{
                            emailFlag = true;
                        }
                        if(param[3].equals(data.child(param[1]).getValue().toString())){
                            idFlag = false;
                            break;
                        }else{
                            idFlag = true;
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
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            pd.dismiss();
            if (!emailFlag) {
                Toast.makeText(sign_up.this, "信箱已經有人註冊", Toast.LENGTH_SHORT).show();
            } else if (!idFlag) {
                Toast.makeText(sign_up.this, "暱稱已經有人使用", Toast.LENGTH_SHORT).show();
            }else if(!password.getText().toString().equals(passwordConfirm.getText().toString())){
                Toast.makeText(sign_up.this, "密碼不一致", Toast.LENGTH_SHORT).show();
            }else if(emailFlag && idFlag){
                int checkedId = radioGroup.getCheckedRadioButtonId();
                RadioButton r = (RadioButton) findViewById(checkedId);
                UserData userData = new UserData(email.getText().toString()
                        ,password.getText().toString()
                        ,passwordHint.getText().toString()
                        ,name.getText().toString()
                        ,id.getText().toString()
                        ,r.getText().toString());
                ref.push().setValue(userData);
                Toast.makeText(sign_up.this, "註冊成功", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(sign_up.this,first_page.class);
                i.putExtra("email",email.getText().toString());
                i.putExtra("password",password.getText().toString());
                startActivity(i);
            }

        }
    }

}
