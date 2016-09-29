package com.attmm.admin.projectt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.attmm.admin.projectt.view.History;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;


/**
 * Created by imo on 2016/6/29.
 */
public class SendRequest extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, Constant {
    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
    Double mLongitude,mLatitude;
    SharedPreferences setting;
    String id;
    LocationManager manager;
    final int PLACE_PICKER_REQUEST = 1;
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) { }
    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = new Location("myLocation");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitude = mLastLocation.getLatitude();
            mLongitude = mLastLocation.getLongitude();
        }
    }
    @Override
    public void onConnectionSuspended(int i) { }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_news);

        Logger.getAnonymousLogger().warning("clicked");
        Firebase.setAndroidContext(this);
        final Firebase ref =new Firebase("https://mis-atm.firebaseio.com/");
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setting = getSharedPreferences(LOGIN_SHAREDPREFERENCE,0);
        id = setting.getString(LOGIN_ID,"");

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        final Button btn_submit_task = (Button) findViewById(R.id.btn_submit_task);
        final ImageView btnPlacePicker = (ImageView) findViewById(R.id.btnPlacePicker);

        btn_submit_task.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                Firebase taskRef= ref.child("task");
                final Firebase historyRef = ref.child("history").child(setting.getString(LOGIN_ID,"")).push();
                final Firebase newPostRef = taskRef.push();
                final EditText title = (EditText)findViewById(R.id.taskTitle);
                final EditText location = (EditText)findViewById(R.id.taskLocation);
                final EditText content = (EditText)findViewById(R.id.taskContent);
                //檢查有沒有未輸入的資料
                if(TextUtils.isEmpty(title.getText())){
                    Toast.makeText(SendRequest.this, "請輸入標題",
                            Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(location.getText())){
                    Toast.makeText(SendRequest.this, "請輸入地點",
                            Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(content.getText())){
                    Toast.makeText(SendRequest.this, "請輸入描述",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                checkIfTaskIsOngoing(new CheckIfTaskIsOngoingCallBack(){
                    @Override
                    public void onFirebaseFinish(Boolean b){
                        if(b){
                            Toast.makeText(SendRequest.this,"一次只能發一個任務喔",Toast.LENGTH_SHORT).show();
                        }else{
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                            //get current date time with Date()
                            Date date = new Date();
                            Task t = new Task(title.getText().toString(),
                                    location.getText().toString(),
                                    content.getText().toString(),
                                    id,
                                    mLongitude,
                                    mLatitude) ;
                            History h = new History(title.getText().toString(),
                                    content.getText().toString(),
                                    "等待中",
                                    dateFormat.format(date));
                            newPostRef.setValue(t);
                            historyRef.setValue(h);
                            Toast.makeText(SendRequest.this, "發送成功!", Toast.LENGTH_SHORT).show();

                            //開啟Service等待回應,回到主畫面
                            Intent iMain = new Intent(SendRequest.this,main_page.class);
                            Intent iService = new Intent(SendRequest.this,WaitingService.class);

                            iService.putExtra(DELIVER_TASK_PATH,newPostRef.getKey());
                            iService.putExtra(DELIVER_HISTORY_PATH,historyRef.getKey());
                            iService.putExtra(DELIVER_TASK_TITLE,title.getText().toString());


                            startService(iService);
                            startActivity(iMain);
                            Log.d("Debug Path",newPostRef.getKey());
                        }
                    }
                });


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
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                LatLng ll;
                Place place = PlacePicker.getPlace(this,data);
                ll = place.getLatLng();
                mLatitude = ll.latitude;
                mLongitude = ll.longitude;
            }
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS沒有打開喔 要開啟GPS嗎?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        startActivity(new Intent(SendRequest.this,main_page.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void checkIfTaskIsOngoing(final CheckIfTaskIsOngoingCallBack c){
        new Firebase("https://mis-atm.firebaseio.com/task")
                .orderByChild("id")
                .startAt(setting.getString(LOGIN_ID,""))
                .endAt(setting.getString(LOGIN_ID,""))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            c.onFirebaseFinish(true);
                        }
                        else {
                            c.onFirebaseFinish(false);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
    }

    private class CheckIfTaskIsOngoingCallBack{
        public void onFirebaseFinish(Boolean b){

        }
    }
}

