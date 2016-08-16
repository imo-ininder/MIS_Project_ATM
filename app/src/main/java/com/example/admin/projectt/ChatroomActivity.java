package com.example.admin.projectt;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.widget.*;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import android.os.Build;
import android.support.v7.app.ActionBarDrawerToggle;


public class ChatroomActivity extends AppCompatActivity implements Constant {
    Firebase ref ,chatRef;
    SharedPreferences chatData,setting;
    EditText et;
    TextView tvContent;
    Button chat,confirm,cancel;
    String myId;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    Toolbar toolbar;
    ActionBarDrawerToggle mDrawerToggle;
    ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        Firebase.setAndroidContext(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //initialization Firebase root url & Chat SharedPreferences
        chatData = getSharedPreferences(CHAT_SHAREDPREFERENCES,0);
        setting  = getSharedPreferences(LOGIN_SHAREDPREFERENCE,0);
        ref     = new Firebase("https://mis-atm.firebaseio.com/chat");
        chatRef = ref.child(chatData.getString(CHAT_PATH,""));
        myId  = setting.getString(LOGIN_ID,"");

        //initialization UI object
        et      = (EditText)findViewById(R.id.chatEditText);
        tvContent = (TextView)findViewById(R.id.chatroomContent);
        chat      = (Button)findViewById(R.id.chatBtn);
        confirm   = (Button)findViewById(R.id.chatConfirm);
        cancel    = (Button)findViewById(R.id.chatCancel);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(chatData.getString(CHAT_TITLE,"ATM"));
        this.setSupportActionBar(this.toolbar);

        //新增側邊欄裡的選項
        drawerItem[0] = new ObjectDrawerItem("");
        drawerItem[1] = new ObjectDrawerItem("放棄");
        drawerItem[2] = new ObjectDrawerItem("協議取消");
        drawerItem[3] = new ObjectDrawerItem("完成");
        drawerItem[4] = new ObjectDrawerItem(" ");
        drawerItem[5] = new ObjectDrawerItem(" ");
        drawerItem[6] = new ObjectDrawerItem("拍照");
        drawerItem[7] = new ObjectDrawerItem("本機相簿");
        drawerItem[8] = new ObjectDrawerItem("google 相簿");
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
                        .putBoolean(CHAT_STATE,false)
                        .putBoolean(CHAT_IS_DOUBLE_CHECKED,false)
                        .apply();
                chatRef.child("cancel").setValue(myId);
                Intent i = new Intent(ChatroomActivity.this,main_page.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        DrawerItemCustomAdapter adapter =
                new DrawerItemCustomAdapter(this, R.layout.listview_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);

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
                    if(!dataSnapshot.getValue().toString().equals(setting.getString(LOGIN_ID,""))) {
                        Toast.makeText(ChatroomActivity.this, "對方已經離開聊天室", Toast.LENGTH_SHORT)
                                .show();
                        chatData.edit().putString(CHAT_PATH,"")
                                .putString(CHAT_TITLE,"")
                                .putBoolean(CHAT_STATE,false)
                                .putBoolean(CHAT_IS_DOUBLE_CHECKED,false)
                                .apply();
                        Intent i = new Intent(ChatroomActivity.this,main_page.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }

                } else if(!dataSnapshot.getKey().equals(CHAT_NFC_CHECK_MSG)) {
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

    @Override
    protected void onResume(){
        super.onResume();
        et.setText("");
    }

    //判斷選了哪一個選項
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    //判斷選了哪一個選項(case的數字是drawerItem陣列的索引)
    private void selectItem(int position) {

        switch (position) {

            case 1: //放棄
                new AlertDialog.Builder(ChatroomActivity.this)
                        .setTitle("ATM系統訊息")
                        .setCancelable(false)
                        .setMessage("放棄會被系統懲罰，確定要離開嗎?")
                        .setPositiveButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
                break;
            case 2: //協議取消
                new AlertDialog.Builder(ChatroomActivity.this)
                        .setTitle("ATM系統訊息")
                        .setCancelable(false)
                        .setMessage("提出協議取消嗎?")
                        .setPositiveButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
                break;
            case 3: //NFC 認證
                NfcManager manager = (NfcManager) getSystemService(Context.NFC_SERVICE);
                NfcAdapter adapter = manager.getDefaultAdapter();
                if(adapter!= null && adapter.isEnabled()){
                    Intent i;
                    if(chatData.getBoolean(CHAT_TASK_SENDER,true)){
                        i = new Intent(ChatroomActivity.this,NFCPageActivity.class);
                    }else {
                        i = new Intent(ChatroomActivity.this,ReciveNFCActivity.class);
                    }
                    startActivity(i);
                }else{
                    startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
                }
                break;
            default:
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

}
