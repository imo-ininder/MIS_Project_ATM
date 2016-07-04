package com.example.admin.projectt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class message extends AppCompatActivity {                            //為了要顯示完整的訊息的內容做的activity
    TextView getmessage;
    static String messagekeep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        getmessage = (TextView) findViewById(R.id.messageContext);
        Bundle bundle = getIntent().getExtras();                        //收傳過來的bundle
        if(bundle!=null){                                               //和about_page是同一個做法
            getmessage .setText(bundle.getString("message"));

            messagekeep=getmessage.getText().toString();
        }
        else{
            getmessage .setText(messagekeep);
        }

    }



}
