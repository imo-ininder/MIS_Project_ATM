package com.attmm.admin.projectt;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by imo on 2016/7/12.
 */
public class CustomDialogActivity extends Activity implements Constant {
    Button btn_confirm,btn_cancel;
    TextView text_content,text_title,text_close;

     RelativeLayout title_bar;

    Firebase ref;
    Bundle extras;
    SharedPreferences setting,chatData,setcolor;
    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_custom_dialog);


        Firebase.setAndroidContext(this);

        //initialization Firebase root url & Chat SharedPreferences
        ref = new Firebase("https://mis-atm.firebaseio.com/task");
        extras = getIntent().getExtras();
        setting = getSharedPreferences(LOGIN_SHAREDPREFERENCE,0);
        chatData = getSharedPreferences(CHAT_SHAREDPREFERENCES,0);

        //initialization UI objects
        btn_confirm = (Button)findViewById(R.id.btn_confirm);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        text_close = (TextView)findViewById(R.id.text_close);
        text_title  = (TextView)findViewById(R.id.text_title);
        text_content = (TextView)findViewById(R.id.text_content);

        title_bar = (RelativeLayout) findViewById(R.id.title_bar);

        //end of init

        //get task information
        if(extras!= null){
            text_title.setText(extras.get(DELIVER_TASK_TITLE).toString());
            text_content.setText(extras.get(DELIVER_TASK_CONTENT).toString());
        }

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            final Firebase targetRef = ref.child(extras.getString(DELIVER_TASK_PATH));
            targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        //Inform counterpart task is accepted.
                        targetRef.child("acceptedBy").setValue(setting.getString(LOGIN_ID,""));

                        //Create chat room & init Chat_SharePreference.
                        chatData.edit().putString(CHAT_PATH,extras.getString(DELIVER_TASK_ID)+
                                setting.getString(LOGIN_ID,""))
                                .putString(CHAT_TITLE,extras.getString(DELIVER_TASK_TITLE))
                                .apply();
                        startActivity(new Intent(CustomDialogActivity.this,ChatroomActivity.class));
                        startService(new Intent(CustomDialogActivity.this,RetrieveChatDataService.class));
                        finish();
                    }else{
                        Toast.makeText(CustomDialogActivity.this,
                                "任務已經被接走或過期囉",
                                Toast.LENGTH_SHORT)
                                .show();
                        finish();
                    }
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomDialogActivity.this,"you click cancel!",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        text_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomDialogActivity.this,"you click close!",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        
        setcolor = getSharedPreferences("set", 0);          //換顏色
        int colorr =  setcolor.getInt("color",1);
        switch(colorr){
            case 1:

               title_bar.setBackgroundColor(Color.parseColor("#3bb6d2"));

                break;
            case 2:

                title_bar.setBackgroundColor(Color.parseColor("#eb1346"));

                break;
            case 3:

                title_bar.setBackgroundColor(Color.parseColor("#fabf0c"));

                break;
            case 4:

                title_bar.setBackgroundColor(Color.parseColor("#673AB7"));

                break;

        }

        
    }
}
