package com.example.admin.projectt;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
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
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}
    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
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
        setContentView(R.layout.post_news);
        Firebase.setAndroidContext(this);
        final Firebase ref =new Firebase("https://mis-atm.firebaseio.com/");
        super.onCreate(savedInstanceState);
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
                EditText title = (EditText)findViewById(R.id.taskTitle);
                EditText location = (EditText)findViewById(R.id.taskLocation);
                EditText content = (EditText)findViewById(R.id.taskContent);
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
                Task t = new Task(title.getText().toString(),location.getText().toString(),content.getText().toString(),100.0,100.0) ;
                taskRef.push().setValue(t);
                Toast.makeText(SendRequest.this, "Send successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SendRequest.this,main_page.class);
                startActivity(i);
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
