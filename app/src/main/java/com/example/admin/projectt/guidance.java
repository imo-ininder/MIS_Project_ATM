package com.example.admin.projectt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class guidance extends AppCompatActivity {
    SharedPreferences settler;
    ImageView rule,guide_icon,atm_icon,step_btn,nfc_btn,giveup_btn,report_btn,cancel_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        settler = getSharedPreferences("set", 0);
        rule= (ImageView) findViewById(R.id.imageView3);
        guide_icon= (ImageView) findViewById(R.id.imageView4);
        atm_icon= (ImageView) findViewById(R.id.imageView9);

        step_btn= (ImageView) findViewById(R.id.imageView2);
        nfc_btn= (ImageView) findViewById(R.id.imageView6);
        giveup_btn= (ImageView) findViewById(R.id.imageView7);
        report_btn = (ImageView) findViewById(R.id.imageView8);
        cancel_btn= (ImageView) findViewById(R.id.imageView5);



        int colo = settler.getInt("color",1);
        if(colo==1) {
            rule.setImageResource(R.drawable.blue_rule);
            guide_icon.setImageResource(R.drawable.blue_guide2);
            atm_icon.setImageResource(R.drawable.blue_atm);
            step_btn.setImageResource(R.drawable.blue_step);
            nfc_btn.setImageResource(R.drawable.blue_nfc);
            giveup_btn.setImageResource(R.drawable.blue_giveup);
            report_btn.setImageResource(R.drawable.blue_report);
            cancel_btn.setImageResource(R.drawable.blue_cancell);


        }
        else if(colo==2){
            rule.setImageResource(R.drawable.red_rule);
            guide_icon.setImageResource(R.drawable.red_guide2);
            atm_icon.setImageResource(R.drawable.red_atm);
            step_btn.setImageResource(R.drawable.red_step);
            nfc_btn.setImageResource(R.drawable.red_nfc);
            giveup_btn.setImageResource(R.drawable.red_giveup);
            report_btn.setImageResource(R.drawable.red_report);
            cancel_btn.setImageResource(R.drawable.red_cancell);

        }
        else if(colo==3){
            rule.setImageResource(R.drawable.yellow_rule);
            guide_icon.setImageResource(R.drawable.yellow_guide2);
            atm_icon.setImageResource(R.drawable.yellow_atm);
            step_btn.setImageResource(R.drawable.yellow_step);
            nfc_btn.setImageResource(R.drawable.yellow_nfc);
            giveup_btn.setImageResource(R.drawable.yellow_giveup);
            report_btn.setImageResource(R.drawable.yellow_report);
            cancel_btn.setImageResource(R.drawable.yellow_cancell);

        }
        else if(colo==4){
            rule.setImageResource(R.drawable.purple_rule);
            guide_icon.setImageResource(R.drawable.purple_guide2);
            atm_icon.setImageResource(R.drawable.purple_atm);
            step_btn.setImageResource(R.drawable.purple_step);
            nfc_btn.setImageResource(R.drawable.purple_nfc);
            giveup_btn.setImageResource(R.drawable.purple_giveup);
            report_btn.setImageResource(R.drawable.purple_report);
            cancel_btn.setImageResource(R.drawable.purple_cancell);

        }




    }


    public void gotonfcteach(View v){
        Intent it =new Intent(this,bb_nfc.class);
        startActivity(it);
    }

    public void gotogiveupteach(View v){
        Intent it =new Intent(this,bb_giveupp.class);
        startActivity(it);
    }
    public void gotoreportteach(View v){
        Intent it =new Intent(this,bb_reportt.class);
        startActivity(it);
    }

    public void gotocancelteach(View v){
        Intent it =new Intent(this,bb_cancel.class);
        startActivity(it);
    }
    public void gotoruleteach(View v){
        Intent it =new Intent(this,bb_rulee.class);
        startActivity(it);
    }

    public void gotostepteach(View v){
        Intent it =new Intent(this,bb_stepteach.class);
        startActivity(it);
    }

}
