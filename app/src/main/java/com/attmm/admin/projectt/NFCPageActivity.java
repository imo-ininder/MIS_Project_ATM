package com.attmm.admin.projectt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import com.firebase.client.Firebase;


/**
 * Created by imo on 2016/8/4.
 */
public class NFCPageActivity extends AppCompatActivity  implements Constant {
    NfcAdapter mNfcAdapter;
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

    }

    protected void onResume(){
        super.onResume();
        if (mNfcAdapter != null)  mNfcAdapter.setNdefPushMessage(msg,this);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present

        String textMessage = new String(msg.getRecords()[0].getPayload());
        if(textMessage.equals(CHAT_NFC_CHECK_MSG)){
            Firebase chatRef = new Firebase("https://mis-atm.firebaseio.com/chat")
                    .child(chatData.getString(CHAT_PATH,""));
            chatRef.child(CHAT_NFC_CHECK_MSG).setValue(CHAT_NFC_CHECK_MSG);
        }

    }

}
