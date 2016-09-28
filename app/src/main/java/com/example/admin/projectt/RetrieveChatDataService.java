package com.example.admin.projectt;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


public class RetrieveChatDataService extends Service implements ChatConstant {
    Firebase chatRef;
    SharedPreferences chatData;
    NotificationManager mNotificationManager;
    NotificationCompat.Builder mbuilder;
    ChildEventListener chatListener;
    private static boolean sIsAvailable = false;
    private static int admitCancelCounter = 0;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);

        chatData = getSharedPreferences(CHAT_SHAREDPREFERENCES,0);
        chatRef = new Firebase("https://mis-atm.firebaseio.com/chat/"
                +chatData.getString(CHAT_PATH,""));
        setIsAvailable(true);
    }

    @Override
    public void onDestroy() {
        setIsAvailable(false);

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        chatListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.getKey().equals(CHAT_IS_CANCELED) ) {
                    ifIsCanceled("任務被取消了 QQ");
                } else if(dataSnapshot.getKey().equals(CHAT_NFC_CHECK_MSG)) {
                    authNFC();
                } else if(dataSnapshot.getKey().equals("協議取消")) {
                    admitCancelCounter++;
                    admitCancel();
                }
                else {
                    Message m = dataSnapshot.getValue(Message.class);
                    sendMessageToActivity(CHAT_RECEIVED, m.getAuthor(), m.getMessage());
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                admitCancelCounter++;
                if(admitCancelCounter==2) {
                    ifIsCanceled("協議取消成功 可以開始新的任務了");
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        };
        chatRef.addChildEventListener(chatListener);

        return START_STICKY;

    }

    public void ifIsCanceled(String msg) {

        setIsAvailable(false);
        sendMessageToActivity(CHAT_IS_CANCELED);
        resetChatData();
        buildNotification(msg);
        chatRef.removeEventListener(chatListener);
        RetrieveChatDataService.this.stopSelf();

    }

    public void authNFC(){

        sendMessageToActivity(AUTH_SUCCESSFUL);
        resetChatData();
        chatRef.removeEventListener(chatListener);
        RetrieveChatDataService.this.stopSelf();

    }

    public void sendMessageToActivity(String msg){
        Intent intent = new Intent("dataFromService");
        intent.putExtra("Data",msg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void sendMessageToActivity(String msg, String... extras){
        Intent intent = new Intent("dataFromService");
        intent.putExtra("Data", msg);
        intent.putExtra("Extras", extras);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void resetChatData() {

        admitCancelCounter = 0;
        chatRef.removeValue();
        chatData.edit().putString(CHAT_PATH, "")
                .putString(CHAT_TITLE, "")
                .putBoolean(CHAT_STATE, false)
                .putBoolean(CHAT_IS_DOUBLE_CHECKED, false)
                .apply();

    }

    public void buildNotification(String msg){

        mNotificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mbuilder = new NotificationCompat.Builder(this)
                .setContentTitle("ATM")
                .setContentText(msg)
                .setSmallIcon(R.drawable.atm);
        mNotificationManager.notify(1,mbuilder.build());

    }

    private void admitCancel(){
        Intent intent = new Intent("dataFromService");
        intent.putExtra("Data","對方提出協議取消");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    static private void setIsAvailable(final boolean value) {
        sIsAvailable = value;
    }

    static public boolean getIsAvailable() {
        return sIsAvailable;
    }
}
