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


/**
 * Created by imo on 2016/7/15.
 */
public class WaitingService extends Service {
    IBinder mBinder = new LocalBinder();
    CountDownTimer ct;
    Firebase ref;
    Firebase postRef;
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
        Log.d("ServiceDebug","START");
        ct = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long l) {
                String remaing = Long.toString(l/1000);
                Log.d("Second Remaining",remaing);
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
        postRef = ref.child(intent.getStringExtra("path"));
        title = intent.getStringExtra("taskTitle");
        Log.d("pathDebug",intent.getStringExtra("path"));
        Log.d("Title",title);

        postRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot d :dataSnapshot.getChildren()){
                    if(d.getKey()=="acceptedBy"){
                        setting = getSharedPreferences("LoginData",0);
                        chatData = getSharedPreferences("ATM_chatData",0);
                        chatData.edit().putString("path",setting.getString("id","") + d.getValue().toString())
                                .putString("chatTitle",title)
                                .apply();
                        postRef.removeValue();
                        Intent i = new Intent(WaitingService.this,ChatroomActivity.class);
                        startService(i);
                        WaitingService.this.stopSelf();
                    }
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
        Log.d("ServiceDebug","CLOSE");
        super.onDestroy();
    }


}
