package com.example.admin.projectt;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;


public class settings_page extends AppCompatActivity {
    Intent serviceIntent;
    RadioButton r1,r2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);
        serviceIntent = new Intent();
        Bundle b = new Bundle();
        b.putBoolean("flag", true);
        serviceIntent.putExtras(b);
        serviceIntent.setClass(this, RetrievePostService.class);
        r1 =(RadioButton)findViewById(R.id.radioButton);
        r2 =(RadioButton)findViewById(R.id.radioButton2);
        r1.setClickable(false);
        r2.setClickable(false);
        Switch s = (Switch)findViewById(R.id.switchRetrieve);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    startService(serviceIntent);
                    r1.setClickable(true);
                    r2.setClickable(true);
                }
                else{
                    stopService(serviceIntent);
                    r1.setClickable(false);
                    r2.setClickable(false);
                }
            }
        });
    }


    public void changecolor(View v) {
        // TODO Auto-generated method stub
    }

}
