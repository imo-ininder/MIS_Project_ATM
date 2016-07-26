package com.example.admin.projectt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;

import com.google.android.gms.location.LocationServices;


public class settings_page extends AppCompatActivity implements Constant{
    Intent serviceIntent;
    RadioButton r1,r2;
    SharedPreferences setting;
    Switch s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);
        setting = getSharedPreferences(LOGIN_SHAREDPREFERENCE,0);
        serviceIntent = new Intent();
        Bundle b = new Bundle();
        b.putBoolean("flag", true);
        final LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        serviceIntent.putExtras(b);
        serviceIntent.setClass(this, RetrievePostService.class);
        r1 =(RadioButton)findViewById(R.id.radioButton);
        r2 =(RadioButton)findViewById(R.id.radioButton2);
        r1.setClickable(false);
        r2.setClickable(false);
        s = (Switch)findViewById(R.id.switchRetrieve);
        if(setting.getBoolean(LOGIN_RETRIEVE_SERVICE,false)){
            s.setChecked(true);
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
}
