package com.example.admin.projectt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class ChatroomActivity extends AppCompatActivity implements ChatConstant,Constant {
    private Firebase ref ,chatRef;
    SharedPreferences chatData,setting;
    EditText et;
    TextView tvTitle,tvContent;
    Button chat,confirm,cancel;
    String myId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        Firebase.setAndroidContext(this);

        //initialization Firebase root url & Chat SharedPreferences
        chatData = getSharedPreferences(CHAT_SHAREDPREFERENCES,0);
        setting  = getSharedPreferences(LOGIN_SHAREDPREFERENCE,0);
        ref     = new Firebase("https://mis-atm.firebaseio.com/chat");
        chatRef = ref.child(chatData.getString(CHAT_PATH,""));
        myId  = setting.getString(LOGIN_ID,"");

        //initialization UI object
        et      = (EditText)findViewById(R.id.chatEditText);
        tvTitle   = (TextView)findViewById(R.id.chatroomTitle);
        tvContent = (TextView)findViewById(R.id.chatroomContent);
        chat      = (Button)findViewById(R.id.chatBtn);
        confirm   = (Button)findViewById(R.id.chatConfirm);
        cancel    = (Button)findViewById(R.id.chatCancel);
        tvTitle.setText(chatData.getString(CHAT_TITLE,""));
        //end of initialization

        //Change btn in main activity from "POST" to "CHAT"
        chatData.edit().putBoolean(CHAT_STATE,true).apply();

        //Send message btn
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message m = new Message(myId,et.getText().toString());
                chatRef.push().setValue(m);
                et.setText("");
            }
        });

        //Don't show btn if double checked.
        if(chatData.getBoolean(CHAT_IS_DOUBLE_CHECKED,false)){
            confirm.setVisibility(View.INVISIBLE);
            cancel.setVisibility(View.INVISIBLE);
        }

        //Double checked btn
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm.setVisibility(View.INVISIBLE);
                cancel.setVisibility(View.INVISIBLE);
                chatData.edit().putBoolean(CHAT_IS_DOUBLE_CHECKED,true).apply();
            }
        });

        //Cancel and leave chat btn
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatData.edit().putString(CHAT_PATH,"")
                        .putString(CHAT_TITLE,"")
                        .putBoolean(CHAT_STATE,false).apply();
                chatRef.child("cancel").setValue(myId);
                Intent i = new Intent(ChatroomActivity.this,main_page.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        //Set up ChildEventListener to retrieve message
        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Check if counterpart leave chat room.
                if (dataSnapshot.getKey().equals("cancel") ) {
                    if(dataSnapshot.getValue().equals(myId))
                        return;

                    chatRef.removeValue();
                    Toast.makeText(ChatroomActivity.this,"對方已經離開聊天室",Toast.LENGTH_SHORT)
                            .show();
                    Intent i = new Intent(ChatroomActivity.this,main_page.class);
                    startActivity(i);
                    finish();
                } else {
                    Message m = dataSnapshot.getValue(Message.class);
                    tvContent.append(m.getAuthor() + ":" + m.getMessage() + "\n");
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }
}
