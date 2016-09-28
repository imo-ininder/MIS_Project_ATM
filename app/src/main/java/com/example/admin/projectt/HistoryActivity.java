package com.example.admin.projectt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.admin.projectt.adapter.HistoryAdapter;
import com.example.admin.projectt.adapter.MessageAdapter;
import com.example.admin.projectt.view.History;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imo on 2016/8/13.
 */
public class HistoryActivity extends AppCompatActivity implements Constant {


    Firebase ref;
    SharedPreferences setting;
    RecyclerView mHistoryContainer;
    HistoryAdapter mHistoryHolderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Firebase.setAndroidContext(this);
        setting = getSharedPreferences(LOGIN_SHAREDPREFERENCE,0);
        ref = new Firebase("https://mis-atm.firebaseio.com/history");
        mHistoryContainer = (RecyclerView) findViewById(R.id.history_container);
        mHistoryHolderAdapter = new HistoryAdapter();
        mHistoryContainer.setAdapter(mHistoryHolderAdapter);
        mHistoryContainer.setLayoutManager(new LinearLayoutManager(this));

        getData();

    }

    private void getData(){
        ref.child(setting.getString(LOGIN_ID,""))
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d :dataSnapshot.getChildren()){
                    final History h =d.getValue(History.class);
                    mHistoryHolderAdapter.addHistory(h);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
