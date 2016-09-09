package com.example.admin.projectt;

import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.Firebase;


/**
 * Created by imo on 2016/8/4.
 */
public class NFCPageActivity extends AppCompatActivity  implements Constant {
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

    protected void onResume(){
        super.onResume();
        if (mNfcAdapter != null)  mNfcAdapter.setNdefPushMessage(msg,this);
    }

}
