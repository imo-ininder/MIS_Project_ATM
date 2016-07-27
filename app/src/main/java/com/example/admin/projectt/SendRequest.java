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
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.Places;


/**
 * Created by imo on 2016/6/29.
 */
public class SendRequest extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Double mLongitude,mLatitude;
    SharedPreferences setting;
    String id;
    final int PLACE_PICKER_REQUEST = 1;
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) { }
    @Override
    public void onConnected(Bundle connectionHint) { }
    @Override
    public void onConnectionSuspended(int i) { }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_news);
        Firebase.setAndroidContext(this);
        final Firebase ref =new Firebase("https://mis-atm.firebaseio.com/");
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        final Button btn_submit_task = (Button) findViewById(R.id.btn_submit_task);
        final Button btnPlacePicker = (Button) findViewById(R.id.btnPlacePicker);
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

        btnPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(SendRequest.this), PLACE_PICKER_REQUEST);
                }catch (GooglePlayServicesNotAvailableException e){
                    e.printStackTrace();
                }catch (GooglePlayServicesRepairableException e){
                    e.printStackTrace();
                }
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this,data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }
}
