package com.example.admin.projectt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by imo on 2016/8/13.
 */
public class HistoryDeatialActivity extends AppCompatActivity {
    TextView tv1,tv2;
    Bundle extras;
    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_historydetail);

        extras = getIntent().getExtras();
        tv1 = (TextView) findViewById(R.id.textView22);
        tv2 = (TextView) findViewById(R.id.textView23);

        tv1.setText(extras.getString("title","ATM"));
        String detail = "地點:" + extras.getString("location") + "\n"
                + "任務內容" + extras.getString("content");
        tv2.setText(detail);

    }
}
