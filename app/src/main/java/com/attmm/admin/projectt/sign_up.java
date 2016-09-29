package com.attmm.admin.projectt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class sign_up extends AppCompatActivity {
    EditText email,password,passwordHint,passwordConfirm,name,id;
    RadioGroup radioGroup;
    SharedPreferences userkey;
    private ProgressDialog pd;
    Firebase ref = new Firebase("https://mis-atm.firebaseio.com/userdata");
    Firebase refkey;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@cc\\.ncu\\.edu\\.tw$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        Firebase.setAndroidContext(this);
        userkey= getSharedPreferences("hashkey",0);
        final Button submitBtn = (Button) findViewById(R.id.submitUserData);
        email = (EditText)findViewById(R.id.edit_email);
        password = (EditText)findViewById(R.id.edit_pwd);
        passwordHint = (EditText)findViewById(R.id.edit_pwd_hint);
        passwordConfirm = (EditText)findViewById(R.id.edit_pwd_check);
        name = (EditText)findViewById(R.id.edit_name);
        id = (EditText)findViewById(R.id.edit_nickname);
        radioGroup = (RadioGroup) findViewById(R.id.genderGroup);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validate(email.getText().toString())){
                    Toast.makeText(sign_up.this,"請輸入正確信箱格式",Toast.LENGTH_SHORT).show();
                }else if (!checkPassword(password.getText().toString(),passwordConfirm.getText().toString())){
                    Toast.makeText(sign_up.this, "密碼不一致", Toast.LENGTH_SHORT).show();
                }else {
                    checkIfAccountExist(email.getText().toString());
                }
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


    public void checkIfAccountExist(final String email){
        pd = ProgressDialog.show(this,"檢查中","正再檢查資訊",true,false);
        ref.orderByChild("email")
                .startAt(email)
                .endAt(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    pd.dismiss();
                    Toast.makeText(sign_up.this, "信箱已經有人註冊", Toast.LENGTH_SHORT).show();
                }else{
                    pd.dismiss();
                    int checkedId = radioGroup.getCheckedRadioButtonId();
                    RadioButton r = (RadioButton) findViewById(checkedId);
                    UserData userData = new UserData(email
                            ,password.getText().toString()
                            ,passwordHint.getText().toString()
                            ,name.getText().toString()
                            ,id.getText().toString()
                            ,r.getText().toString());

                    refkey= ref.push();
                    refkey.setValue(userData);
                    Toast.makeText(sign_up.this, "註冊成功", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(sign_up.this,first_page.class);
                    i.putExtra("email",email);
                    i.putExtra("password",password.getText().toString());
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    public boolean checkPassword(String pwd1,String pwdRecheck){
        return pwd1.equals(pwdRecheck);
    }
}
