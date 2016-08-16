package com.example.admin.projectt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.Map;

/**
 * Created by imo on 2016/8/4.
 */
public class ReciveNFCActivity extends AppCompatActivity implements ChatConstant{
    TextView NFCtestTV;
    SharedPreferences chatData;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recivenfc);
        Firebase.setAndroidContext(this);
        chatData = getSharedPreferences(CHAT_SHAREDPREFERENCES,0);
    }
    @Override
    public void onResume() {
        super.onResume();
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
        NFCtestTV = (TextView) findViewById(R.id.NFCtestTV);
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        String textMessage = new String(msg.getRecords()[0].getPayload());
        NFCtestTV.setText(textMessage);
        if(textMessage.equals(CHAT_NFC_CHECK_MSG)){
            Firebase chatRef = new Firebase("https://mis-atm.firebaseio.com/chat")
                    .child(chatData.getString(CHAT_PATH,""));
            chatRef.child(CHAT_NFC_CHECK_MSG).setValue(CHAT_NFC_CHECK_MSG);
            chatData.edit().putBoolean(CHAT_STATE,false).apply();
            finish();

            startActivity(new Intent(ReciveNFCActivity.this,main_page.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            Toast.makeText(ReciveNFCActivity.this,"任務完成",Toast.LENGTH_LONG).show();
        }
    }
}
