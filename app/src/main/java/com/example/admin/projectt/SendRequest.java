package com.example.admin.projectt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
<<<<<<< HEAD
import android.support.annotation.NonNull;
=======
>>>>>>> origin/mai
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;



/**
 * Created by imo on 2016/6/29.
 */
public class SendRequest extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, Constant{

    GoogleApiClient mGoogleApiClient;
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
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final Firebase ref =new Firebase("https://mis-atm.firebaseio.com/");
        setting = getSharedPreferences(LOGIN_SHAREDPREFERENCE,0);
        id = setting.getString(LOGIN_ID,"");

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
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
                Firebase historyRef = ref.child("history");
                Firebase newPostRef = taskRef.push();
                EditText title = (EditText)findViewById(R.id.taskTitle);
                EditText location = (EditText)findViewById(R.id.taskLocation);
                EditText content = (EditText)findViewById(R.id.taskContent);
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
                Task t = new Task(title.getText().toString(),
                        location.getText().toString(),
                        content.getText().toString(),
                        id,
                        mLongitude,
                        mLatitude) ;
                Map<String,String> historyContent = new HashMap<String, String>();
                historyContent.put("location",t.getTaskLocation());
                historyContent.put("content",t.getTaskContent());

                Map<String,Map<String,String>> history = new HashMap<String, Map<String, String>>();
                history.put(t.getTaskTittle(),historyContent);

                newPostRef.setValue(t);
                historyRef.child(setting.getString(LOGIN_ID,"")).setValue(history);
                Toast.makeText(SendRequest.this, "發送成功!", Toast.LENGTH_SHORT).show();

                //開啟Service等待回應,回到主畫面
                Intent iMain = new Intent(SendRequest.this,main_page.class);
                Intent iService = new Intent();
                iService.setClass(SendRequest.this,WaitingService.class);
                iService.putExtra(DELIVER_TASK_PATH,newPostRef.getKey());
                iService.putExtra(DELIVER_TASK_TITLE,title.getText().toString());


                startService(iService);
                startActivity(iMain);
                Log.d("Debug Path",newPostRef.getKey());
            }
        });

        btnPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    buildAlertMessageNoGps();
                    return;
                }
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
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
