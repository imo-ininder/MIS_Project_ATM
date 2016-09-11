package com.example.admin.projectt;


import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.widget.*;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;

import com.firebase.client.Firebase;

import android.os.Build;
import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.Toast;

public class ChatroomActivity extends AppCompatActivity implements ChatConstant,Constant {

    Firebase ref, chatRef, historyRef;
    SharedPreferences chatData, setting;
    EditText et;
    TextView tvContent;
    Button chat, confirm, cancel;
    String myId,messageBuffer="";
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    Toolbar toolbar;
    ActionBarDrawerToggle mDrawerToggle;
    ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[9];
    Boolean running;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        Firebase.setAndroidContext(this);
        Log.d("TEST","ChatRoomOnCreateFlag");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //initialization Firebase root url & Chat SharedPreferences
        chatData = getSharedPreferences(CHAT_SHAREDPREFERENCES, 0);
        setting = getSharedPreferences(LOGIN_SHAREDPREFERENCE, 0);
        myId = setting.getString(LOGIN_ID,"");
        ref = new Firebase("https://mis-atm.firebaseio.com/chat");
        historyRef = new Firebase("https://mis-atm.firebaseio.com/history")
                .child(setting.getString(LOGIN_ID, ""));
        chatRef = ref.child(chatData.getString(CHAT_PATH, ""));
        running = true;
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("dataFromService"));

        //initialization UI object
        et = (EditText) findViewById(R.id.chatEditText);
        tvContent = (TextView) findViewById(R.id.chatroomContent);
        chat = (Button) findViewById(R.id.chatBtn);
        confirm = (Button) findViewById(R.id.chatConfirm);
        cancel = (Button) findViewById(R.id.chatCancel);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(chatData.getString(CHAT_TITLE, "ATM"));
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
        chatData.edit().putBoolean(CHAT_STATE, true).apply();

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
        if (chatData.getBoolean(CHAT_IS_DOUBLE_CHECKED, false)) {
            confirm.setVisibility(View.INVISIBLE);
            cancel.setVisibility(View.INVISIBLE);
        } else {
            confirm.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
        }

        //Double checked btn
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm.setVisibility(View.INVISIBLE);
                cancel.setVisibility(View.INVISIBLE);
                chatData.edit().putBoolean(CHAT_IS_DOUBLE_CHECKED, true).apply();
            }
        });

        //Cancel and leave chat btn
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatRef.child(CHAT_IS_CANCELED).setValue(myId);
                //TODO
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

        SharedPreferences color = getSharedPreferences("set", 0);          //換顏色
        setColor(color.getInt("color", 1));

    }

    @Override
    protected void onStart() {

        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!RetrieveChatDataService.getIsAvailable()) {
            backToMainPage();
        } else {
            running = true;
            tvContent.append(messageBuffer);
            messageBuffer="";
        }
    }
    @Override
    protected void onPause(){
        running = false;
        super.onPause();
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
                                chatRef.child(CHAT_IS_CANCELED).setValue(myId);
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
                if (adapter != null && adapter.isEnabled()) {
                    startActivity(new Intent(ChatroomActivity.this, NFCPageActivity.class));
                } else {
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

    private void setColor(int color) {

        switch (color) {
            case 1:

                toolbar.setBackgroundColor(Color.parseColor("#3bb6d2"));

                break;
            case 2:

                toolbar.setBackgroundColor(Color.parseColor("#eb1346"));

                break;
            case 3:

                toolbar.setBackgroundColor(Color.parseColor("#fabf0c"));

                break;
            case 4:

                toolbar.setBackgroundColor(Color.parseColor("#673AB7"));

                break;

        }

    }

    public void backToMainPage(){
            Intent main = new Intent(this,main_page.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("Data");
            switch (message){
                case CHAT_IS_CANCELED:
                    if (running)
                        backToMainPage();
                    break;
                case AUTH_SUCCESSFUL:
                    backToMainPage();
                    Toast.makeText(ChatroomActivity.this,"任務已完成",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    if (running)
                        tvContent.append(message);
                    else
                        messageBuffer += message;
                    break;
            }
        }

    };
}