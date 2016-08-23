package com.example.admin.projectt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by imo on 2016/8/4.
 */
public class NFCPageActivity extends AppCompatActivity  implements Constant{
    NfcAdapter mNfcAdapter;
    Button go;
    SharedPreferences setting;
    NdefMessage msg;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcpage);

        setting = getSharedPreferences(LOGIN_SHAREDPREFERENCE,0);
        go = (Button) findViewById(R.id.button);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        msg = new NdefMessage(new NdefRecord[]{
                NdefRecord.createMime("text/plain",setting.getString(LOGIN_ID,"").getBytes())
        });

        if (mNfcAdapter != null) {
            Toast.makeText(this, "already set ndefmessage", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "adapter is null", Toast.LENGTH_LONG).show();
        }

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NFCPageActivity.this,ReciveNFCActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    protected void onResume(){
        super.onResume();
        if (mNfcAdapter != null)  mNfcAdapter.setNdefPushMessage(msg,this);
    }

}
