package com.example.admin.projectt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class settings_page extends AppCompatActivity implements Constant{
    Intent serviceIntent;
    RadioButton r1,r2;
    RadioGroup mRadioGroup;
    SharedPreferences setting;
    SharedPreferences settler;
    SharedPreferences getmyhash;
    EditText  pwd_change;
    SharedPreferences getemail;
    Firebase reff = new Firebase("https://mis-atm.firebaseio.com/userdata");
    Switch s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);
        setting = getSharedPreferences(LOGIN_SHAREDPREFERENCE,0);
        settler = getSharedPreferences("set",0);
        getmyhash= getSharedPreferences("hashkey",0);
        getemail = getSharedPreferences("LoginData",0);

        serviceIntent = new Intent();
        Bundle b = new Bundle();
        b.putBoolean("flag", true);
        final LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        serviceIntent.putExtras(b);
        serviceIntent.setClass(this, RetrievePostService.class);
        r1 =(RadioButton)findViewById(R.id.radioButton);
        r2 =(RadioButton)findViewById(R.id.radioButton2);
        s = (Switch)findViewById(R.id.switchRetrieve);
        mRadioGroup = (RadioGroup)findViewById(R.id.radioGroup);

        pwd_change=(EditText) findViewById(R.id.editText);


        if(setting.getBoolean(LOGIN_RETRIEVE_SERVICE,false)) {
            s.setChecked(true);
        }else{
            r1.setClickable(false);
            r2.setClickable(false);
        }
        if(setting.getBoolean(LOGIN_NOTIFICATION,false)){
            r2.setChecked(true);
        }else{
            r1.setChecked(true);
        }

        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                        buildAlertMessageNoGps();
                        s.setChecked(false);
                    }else{
                    startService(serviceIntent);
                    setting.edit().putBoolean(LOGIN_RETRIEVE_SERVICE,true).apply();
                    r1.setClickable(true);
                    r2.setClickable(true);
                    }
                }
                else{
                    stopService(serviceIntent);
                    setting.edit().putBoolean(LOGIN_RETRIEVE_SERVICE,false).apply();
                    r1.setClickable(false);
                    r2.setClickable(false);
                }
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i==R.id.radioButton) { // Notification
                    setting.edit().putBoolean(LOGIN_NOTIFICATION, true).apply();
                }else{
                    setting.edit().putBoolean(LOGIN_NOTIFICATION, false).apply();
                }
            }
        });

    }


    public void changecolor(View v) {
        // TODO Auto-generated method stub
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    protected void chooseblue(View v){
        SharedPreferences.Editor editor=settler.edit();
        editor.putInt("color",1).apply();
    }

    protected void choosered(View v){
        SharedPreferences.Editor editor=settler.edit();
        editor.putInt("color",2).apply();
    }

    protected void chooseyellow(View v){
        SharedPreferences.Editor editor=settler.edit();
        editor.putInt("color",3).apply();
    }

    protected void choosepurple(View v){
        SharedPreferences.Editor editor=settler.edit();
        editor.putInt("color",4).apply();
    }


    String emailstr,pwdstr,pwdhintstr,namestr,idstr,rstr;

    public void updatepwd(View v){

            reff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot d:dataSnapshot.getChildren()) {

                        if(!d.child("email").getValue().toString().equals(getemail.getString("email"," "))){
                            return;
                        }
                        else{
                            String myhash= d.getKey();
                            if (pwd_change.getText().equals("")){
                                Toast.makeText(settings_page.this,"新密碼不能為空白",Toast.LENGTH_SHORT)
                                        .show();
                                return;
                            }
                            String newpwd= pwd_change.getText().toString();
                            emailstr= d.child("email").getValue().toString();
                            pwdstr= d.child("password").getValue().toString();
                            pwdhintstr= d.child("passwordHint").getValue().toString();
                            namestr=d.child("name").getValue().toString();
                            idstr=d.child("id").getValue().toString();
                            rstr=d.child("gender").getValue().toString();
                            UserData userDatachange = new UserData(emailstr
                                    ,newpwd
                                    ,pwdhintstr
                                    ,namestr
                                    ,idstr
                                    ,rstr);
                            Firebase user=reff.child(myhash);
                            user.setValue(userDatachange);
                            Toast.makeText(settings_page.this, "密碼修改完成", Toast.LENGTH_SHORT)
                                    .show();
                            break;
                        }
                    }

                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });

    }

    public void gotomain(View v){
        Intent it =new Intent(this,main_page.class);
        startActivity(it);
    }
}
