package com.example.admin.projectt;
import android.widget.BaseAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.Menu;
import android.view.View;
import android.view.*;
import android.content.Intent;

public class messagecenter extends AppCompatActivity {
    ListView listview;
    static  String  delivermessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagecenter);
        listview = (ListView)findViewById(R.id.messagelist);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View choosemessage, int position,
                                    long id) {               //建立listview的監聽者
                TextView choosemessagetext=(TextView)choosemessage ;                            //把點下去的view轉成textview，識別點下去的是哪一個
                Intent intentmessage = new Intent(messagecenter.this, message.class);           //建立要拿來送text的intent
                delivermessage=choosemessagetext.getText().toString();
                Bundle deliver = new Bundle();
                deliver.putString("message",  delivermessage);                                  //把要傳送的text的內容放去bundle

                intentmessage.putExtras( deliver);                                              //把bundle放進去intent
                startActivity(intentmessage);                                                   //送intent

            }
        });


    }




}
