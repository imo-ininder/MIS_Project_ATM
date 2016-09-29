package com.attmm.admin.projectt;



import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;



public class RetrievePostService extends Service
        implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener, Constant {
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    LocationRequest mLocationRequest;
    Double mLongitude, mLatitude, distance;
    SharedPreferences setting,chatData;
    NotificationManager mNotificationManager;
    NotificationCompat.Builder mbuilder;
    private boolean mInProgress; // Flag that indicates if a request is underway.
    final double fDistance = 0.005; // 經緯度換算後約500m
    private Firebase ref;
    IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public RetrievePostService getServerInstance() {
            return RetrievePostService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Service is started", Toast.LENGTH_SHORT).show();
        mInProgress = false;
        setting = getSharedPreferences(LOGIN_SHAREDPREFERENCE,0);
        chatData = getSharedPreferences(CHAT_SHAREDPREFERENCES,0);
        Firebase.setAndroidContext(this);
        ref = new Firebase("https://mis-atm.firebaseio.com/task");
        mNotificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mbuilder = new NotificationCompat.Builder(this)
                .setContentTitle("ATM")
                .setContentText("有新的請求喔")
                .setSmallIcon(R.drawable.atm);
        buildGoogleApiClientIfNeeded();
        createLocationRequest();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChild) {
                if(dataSnapshot !=null) {
                    Task rTask = dataSnapshot.getValue(Task.class);
                    if (rTask.getId().equals(setting.getString(LOGIN_ID,"")) || mCurrentLocation == null)
                        return;

                    String path = dataSnapshot.getKey();
                    mLatitude = rTask.getLatitude();
                    mLongitude = rTask.getLongitude();
                     distance =Math.max(Math.abs(mLongitude - mCurrentLocation.getLongitude()),
                                        Math.abs(mLatitude - mCurrentLocation.getLatitude()));
                    if (distance <= fDistance && !chatData.getBoolean(CHAT_STATE,false)) {
                        Intent intent = new Intent(
                                RetrievePostService.this,
                                CustomDialogActivity.class
                        );
                        intent.putExtra(DELIVER_TASK_CONTENT, rTask.getTaskContent())
                            .putExtra(DELIVER_TASK_TITLE, rTask.getTaskTittle())
                            .putExtra(DELIVER_TASK_ID, rTask.getId())
                            .putExtra(DELIVER_TASK_PATH, path)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        if(setting.getBoolean(LOGIN_NOTIFICATION, false)){
                                PendingIntent resultPendingIntent =
                                        PendingIntent.getActivity(RetrievePostService.this,
                                                0,
                                                intent,
                                                PendingIntent.FLAG_UPDATE_CURRENT);
                                mbuilder.setContentIntent(resultPendingIntent);
                                mNotificationManager.notify(1,mbuilder.build());
                        }else {
                            startActivity(intent);
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        if (mGoogleApiClient.isConnected() || mInProgress)
            return START_STICKY;
        buildGoogleApiClientIfNeeded();
        if (!mGoogleApiClient.isConnected() || !mGoogleApiClient.isConnecting() && !mInProgress) {
            mInProgress = true;
            mGoogleApiClient.connect();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mInProgress = false;
        if (this.mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        if (this.mGoogleApiClient != null) {
            this.mGoogleApiClient.unregisterConnectionCallbacks(this);
            this.mGoogleApiClient.unregisterConnectionFailedListener(this);
            this.mGoogleApiClient.disconnect();
            // Destroy the current location client
            this.mGoogleApiClient = null;
        }
        Toast.makeText(this, "Service is closed!", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void buildGoogleApiClientIfNeeded() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        createLocationRequest();
        mCurrentLocation = new Location("myCurrentLocation");
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mInProgress = false;
        mGoogleApiClient = null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mInProgress = false;
        Toast.makeText(this, "Connect Fail!!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
    }
}
