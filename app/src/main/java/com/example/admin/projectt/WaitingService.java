package com.example.admin.projectt;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.logging.Logger;


/**
 * Created by imo on 2016/7/15.
 */
public class WaitingService extends Service implements ChatConstant ,Constant{
    IBinder mBinder = new LocalBinder();
    CountDownTimer ct;
    Firebase ref,postRef;
    String title;
    SharedPreferences setting,chatData;
    public class LocalBinder extends Binder {
        public WaitingService getServerInstance() {
            return WaitingService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);

        ref = new Firebase("https://mis-atm.firebaseio.com/task");
        setting = getSharedPreferences(LOGIN_SHAREDPREFERENCE,0);
        chatData = getSharedPreferences(CHAT_SHAREDPREFERENCES,0);

        Log.d("ServiceDebug","START");
        ct = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long l) {
                String remaining = Long.toString(l/1000);
                Log.d("Second Remaining",remaining);
            }
            @Override
            public void onFinish() {
                postRef.removeValue();
                Toast.makeText(WaitingService.this,"任務已過時效",Toast.LENGTH_SHORT).show();
                WaitingService.this.stopSelf();
            }
        }.start();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        //拿到自己任務的Firebase路徑
        postRef = ref.child(intent.getStringExtra(DELIVER_TASK_PATH));
        title = intent.getStringExtra(DELIVER_TASK_TITLE);

        Log.d("pathDebug",intent.getStringExtra(DELIVER_TASK_PATH));
        Log.d("Title",title);

        postRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if ("acceptedBy".equals(dataSnapshot.getKey())) {
                    chatData.edit().putString(CHAT_PATH, setting.getString(LOGIN_ID, "") +
                            dataSnapshot.getValue().toString())
                            .putString(CHAT_TITLE, title)
                            .putBoolean(CHAT_TASK_SENDER,true)
                            .apply();
                    postRef.removeValue();
                    Intent i = new Intent(WaitingService.this, ChatroomActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    WaitingService.this.stopSelf();
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
