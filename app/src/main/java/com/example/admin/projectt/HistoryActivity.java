package com.example.admin.projectt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imo on 2016/8/13.
 */
public class HistoryActivity extends AppCompatActivity implements Constant{
    ListView mlv;
    ListAdapter mAdapter;
    List<String> title = new ArrayList<>();
    Firebase ref;
    SharedPreferences setting;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Firebase.setAndroidContext(this);
        ref = new Firebase("https://mis-atm.firebaseio.com/history");
        setting = getSharedPreferences(LOGIN_SHAREDPREFERENCE,0);
        mlv = (ListView) findViewById(R.id.lvHistory);

        testData();
        mlv.setAdapter(mAdapter);
        mlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Intent intent = new Intent(HistoryActivity.this,HistoryDeatialActivity.class);
                final String title = adapterView.getItemAtPosition(i).toString();
                ref.child(setting.getString(LOGIN_ID,"")).child(title)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot d : dataSnapshot.getChildren()){
                                    intent.putExtra(d.getKey(),d.getValue().toString());
                                }
                                intent.putExtra("title",title);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

            }
        });
    }
    //尚未完成
    private void getData(){
        ref.child(setting.getString(LOGIN_ID,""))
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChildren())
                    return;
                title.add(dataSnapshot.getKey());
                mAdapter = new ArrayAdapter<>(HistoryActivity.this
                        ,android.R.layout.simple_list_item_1
                        ,title);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    private void testData(){
        for (int i = 0;i<10;i++){
            title.add("testData"+i);
        }
        mAdapter = new ArrayAdapter<>(HistoryActivity.this
                ,android.R.layout.simple_list_item_1
                ,title);
    }//測試用資料
}
