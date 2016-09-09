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
import android.view.ViewGroup;
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


public class settings_page extends AppCompatActivity implements Constant {
    Intent serviceIntent;
    RadioButton notifaction,alertDialog;
    RadioGroup mRadioGroup;
    SharedPreferences setting;
    SharedPreferences settler;
    SharedPreferences getmyhash;
    EditText  pwd_change,pwd_old;
    SharedPreferences getemail;
    Firebase reff;
    Switch s;
    View mNotificationTypeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);
        Firebase.setAndroidContext(this);
        setting = getSharedPreferences(LOGIN_SHAREDPREFERENCE,0);
        reff = new Firebase("https://mis-atm.firebaseio.com/userdata");
        settler = getSharedPreferences("set",0);
        getmyhash= getSharedPreferences("hashkey",0);
        getemail = getSharedPreferences("LoginData",0);

        serviceIntent = new Intent();
        Bundle b = new Bundle();
        b.putBoolean("flag", true);
        final LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        serviceIntent.putExtras(b);
        serviceIntent.setClass(this, RetrievePostService.class);
        notifaction =(RadioButton)findViewById(R.id.radioButton);
        alertDialog =(RadioButton)findViewById(R.id.radioButton2);
        mNotificationTypeView = findViewById(R.id.notification_types);
        s = (Switch)findViewById(R.id.switchRetrieve);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        buildAlertMessageNoGps();
                        s.setChecked(false);
                        return;
                    } else {
                        startService(serviceIntent);
                        setting.edit().putBoolean(LOGIN_RETRIEVE_SERVICE,true).apply();
                        notifaction.setClickable(true);
                        alertDialog.setClickable(true);
                    }
                } else {
                    stopService(serviceIntent);
                    setting.edit().putBoolean(LOGIN_RETRIEVE_SERVICE,false).apply();
                    notifaction.setClickable(false);
                    alertDialog.setClickable(false);
                }

                if (mNotificationTypeView != null)
                    mNotificationTypeView.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });
        s.setChecked(false);
        mRadioGroup = (RadioGroup)findViewById(R.id.radioGroup);

        pwd_change=(EditText) findViewById(R.id.editText);
        pwd_old=(EditText) findViewById(R.id.editText2);

        if(setting.getBoolean(LOGIN_RETRIEVE_SERVICE,false)) {
            s.setChecked(true);
        }else{
            s.setChecked(false);
            alertDialog.setClickable(false);
            notifaction.setClickable(false);
        }
        if(setting.getBoolean(LOGIN_NOTIFICATION,false)){
            notifaction.setChecked(true);
        }else{
            alertDialog.setChecked(true);
        }

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

    public void chooseBlue(View v){
        SharedPreferences.Editor editor=settler.edit();
        editor.putInt("color",1).apply();
        Toast.makeText(settings_page.this,"成功更改顏色",Toast.LENGTH_SHORT).show();
    }

    public void chooseRed(View v){
        SharedPreferences.Editor editor=settler.edit();
        editor.putInt("color",2).apply();
        Toast.makeText(settings_page.this,"成功更改顏色",Toast.LENGTH_SHORT).show();
    }

    public void chooseYellow(View v){
        SharedPreferences.Editor editor=settler.edit();
        editor.putInt("color",3).apply();
        Toast.makeText(settings_page.this,"成功更改顏色",Toast.LENGTH_SHORT).show();
    }

    public void choosePurple(View v){
        SharedPreferences.Editor editor=settler.edit();
        editor.putInt("color",4).apply();
        Toast.makeText(settings_page.this,"成功更改顏色",Toast.LENGTH_SHORT).show();
    }


    String emailstr,pwdstr,pwdhintstr,namestr,idstr,rstr;

    public void updatepwd(View v){
        if ("".equals(pwd_old.getText().toString().trim())){
                Toast.makeText(settings_page.this,"請輸入原本的密碼",Toast.LENGTH_SHORT)
                        .show();
            }
            else{



            reff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot d:dataSnapshot.getChildren()) {

                        if(!d.child("email").getValue().toString().equals(getemail.getString("email"," "))){
                                continue;
                        }
                        else{
                            String myhash= d.getKey();
                            if ("".equals(pwd_change.getText().toString().trim())){
                                Toast.makeText(settings_page.this,"新密碼不能為空白",Toast.LENGTH_SHORT)
                                        .show();
                                return;
                            }

                            if(pwd_old.getText().toString().equals(d.child("password").getValue().toString())) {


                                String newpwd = pwd_change.getText().toString();
                                emailstr = d.child("email").getValue().toString();
                                pwdstr = d.child("password").getValue().toString();
                                pwdhintstr = d.child("passwordHint").getValue().toString();
                                namestr = d.child("name").getValue().toString();
                                idstr = d.child("id").getValue().toString();
                                rstr = d.child("gender").getValue().toString();
                                UserData userDatachange = new UserData(emailstr
                                        , newpwd
                                        , pwdhintstr
                                        , namestr
                                        , idstr
                                        , rstr);
                                Firebase user = reff.child(myhash);
                                user.setValue(userDatachange);
                                Toast.makeText(settings_page.this, "密碼修改完成", Toast.LENGTH_SHORT)
                                        .show();
                                break;
                            }
                            else{
                                Toast.makeText(settings_page.this, "和原密碼不相符，修改失敗", Toast.LENGTH_SHORT)
                                        .show();
                                break;
                            }

                        }
                    }

                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
            }

    }

    public void gotomain(View v){
        Intent it =new Intent(this,main_page.class);
        startActivity(it);
    }
}
