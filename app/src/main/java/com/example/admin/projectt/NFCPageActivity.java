package com.example.admin.projectt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by imo on 2016/8/4.
 */
public class NFCPageActivity extends AppCompatActivity  implements Constant{
    NfcAdapter mNfcAdapter;
    Button go;
    SharedPreferences setting;
    NdefMessage msg;
    SharedPreferences chatData;
    Firebase historyRef;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcpage);

        Firebase.setAndroidContext(this);
        chatData = getSharedPreferences(CHAT_SHAREDPREFERENCES,0);
        setting = getSharedPreferences(LOGIN_SHAREDPREFERENCE,0);

        historyRef = new Firebase("https://mis-atm.firebaseio.com/history")
                .child(setting.getString(LOGIN_ID,""))
                .child(chatData.getString(CHAT_TITLE,""));

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        msg = new NdefMessage(new NdefRecord[]{
                NdefRecord.createMime("text/plain"
                        ,CHAT_NFC_CHECK_MSG.getBytes())
        });

        if (mNfcAdapter != null) {
            Toast.makeText(this, "already set ndef message", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "adapter is null", Toast.LENGTH_LONG).show();
        }

    }
    protected void onStart(){
        super.onStart();
        Firebase ref = new Firebase("https://mis-atm.firebaseio.com/chat");
        final Firebase charRef = ref.child(chatData.getString(CHAT_PATH,""));
        charRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals(CHAT_NFC_CHECK_MSG)){
                    chatData.edit().putBoolean(CHAT_STATE,false)
                            .apply();
                    charRef.removeValue();
                    Map<String, Object> state = new HashMap<String, Object>();
                    state.put("state","已完成");
                    historyRef.updateChildren(state);
                    finish();
                    startActivity(new Intent(NFCPageActivity.this,main_page.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    Toast.makeText(NFCPageActivity.this,"任務完成",Toast.LENGTH_LONG).show();
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
    }

    protected void onResume(){
        super.onResume();
        if (mNfcAdapter != null)  mNfcAdapter.setNdefPushMessage(msg,this);
    }

}
