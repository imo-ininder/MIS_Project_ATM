package com.example.admin.projectt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


/**
 * Created by imo on 2016/6/29.
 */
public class SendRequest extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Double mLongitude,mLatitude;
    SharedPreferences setting;
    String id;
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}
    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation  != null) {
            mLongitude = mLastLocation.getLongitude();
            mLatitude =  mLastLocation.getLatitude();
        }
    }
    @Override
    public void onConnectionSuspended(int i) { }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_news);
        Firebase.setAndroidContext(this);
        final Firebase ref =new Firebase("https://mis-atm.firebaseio.com/");
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        final Button btn_submit_task = (Button) findViewById(R.id.btn_submit_task);
        btn_submit_task.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                Firebase taskRef= ref.child("task");
                Firebase newPostRef = taskRef.push();
                EditText title = (EditText)findViewById(R.id.taskTitle);
                EditText location = (EditText)findViewById(R.id.taskLocation);
                EditText content = (EditText)findViewById(R.id.taskContent);
                //檢查有沒有未輸入的資料
                if(TextUtils.isEmpty(title.getText())){
                    Toast.makeText(SendRequest.this, "You did not enter a title", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(location.getText())){
                    Toast.makeText(SendRequest.this, "You did not enter a location", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(content.getText())){
                    Toast.makeText(SendRequest.this, "You did not enter a description", Toast.LENGTH_SHORT).show();
                    return;
                }
                //
                setting = getSharedPreferences("LoginData",0);
                id = setting.getString("id","");
                Task t = new Task(title.getText().toString(),location.getText().toString(),content.getText().toString(),id,100.0,100.0) ;
                newPostRef.setValue(t);
                Toast.makeText(SendRequest.this, "Send successfully", Toast.LENGTH_SHORT).show();

                //開啟Service等待回應,回到主畫面
                Intent iMain = new Intent(SendRequest.this,main_page.class);
                Intent iService = new Intent();
                iService.setClass(SendRequest.this,WaitingService.class);
                iService.putExtra("path",newPostRef.getKey());
                iService.putExtra("taskTitle",title.getText().toString());
                Log.d("Debug Path",newPostRef.getKey());
                startService(iService);
                startActivity(iMain);
            }
        });

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}
