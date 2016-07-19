package com.example.admin.projectt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class ChatroomActivity extends AppCompatActivity {
    private Firebase ref ,chatRef;
    SharedPreferences chatData,setting;
    EditText et;
    TextView tvTitle,tvContent;
    Button chat,confirm,cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        Firebase.setAndroidContext(this);
        chatData = getSharedPreferences("ATM_chatData",0);
        setting  = getSharedPreferences("LoginData",0);

        ref     = new Firebase("https://mis-atm.firebaseio.com/chat");
        chatRef = ref.child(chatData.getString("path",""));
        et      = (EditText)findViewById(R.id.chatEditText);

        tvTitle   = (TextView)findViewById(R.id.chatroomTitle);
        tvContent = (TextView)findViewById(R.id.chatroomContent);
        chat      = (Button)findViewById(R.id.chatBtn);
        confirm   = (Button)findViewById(R.id.chatConfirm);
        cancel    = (Button)findViewById(R.id.chatCancel);
        tvTitle.setText(chatData.getString("title",""));

        chatData.edit().putBoolean("chatState",true).apply();

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message m = new Message(setting.getString("id",""),et.getText().toString());
                chatRef.push().setValue(m);
                et.setText("");
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm.setVisibility(View.INVISIBLE);
                cancel.setVisibility(View.INVISIBLE);
                chatData.edit().putBoolean("doubleChecked",true).apply();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatData.edit().putString("path","")
                        .putString("title","")
                        .putBoolean("chatState",false).apply();
                ref.child("cancel").setValue("Bye Bye");
                Intent i = new Intent(ChatroomActivity.this,main_page.class);
                startActivity(i);
                finish();
            }
        });
        if(chatData.getBoolean("doubleChecked",false)){
            confirm.setVisibility(View.INVISIBLE);
            cancel.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals("cancel")) {
                    chatRef.removeValue();
                } else {
                    Message m = dataSnapshot.getValue(Message.class);
                    tvContent.append(m.getAuthor() + ":" + m.getMessage() + "\n");
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
