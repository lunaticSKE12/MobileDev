package com.example.mr_ja.loginfirebase;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Mr_Ja on 3/15/2018.
 */

public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;
    //this GPS enable
    boolean isGPSEnabled = false;
    //is Network enabled
    boolean isNetWorkEnabled = false;
    //is able to get location
    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meter
    private static final long MIN_TIME_BW_UPDATES = 1000*60*1;//1 min

    protected LocationManager locationManager;

    public GPSTracker(Context context){
        this.mContext = context;
        getApplication();
    }

    public Location getLocation(){
        try{
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            //gps status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //network status
            isNetWorkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!isGPSEnabled && !isNetWorkEnabled){
                //no network
            }else{
//                this.canGetLocation = true;
//                if (isNetWorkEnabled){
//                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                            MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
//                    if (locationManager != null){
//                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//                        if (location != null){
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                        }
//                    }
//                }
//                if (isGPSEnabled){
//                    if (location == null){
//                        locationManager.requestLocationUpdates(
//                                LocationManager.GPS_PROVIDER,
//                                MIN_TIME_BW_UPDATES,
//                                MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
//                        if (location != null){
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                        }
//                    }
//                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return location;
    }


    public void stopUsingGPS(){
        if (locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    public double getLatitude(){
        if (location != null){
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude(){
        if (location != null){
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public boolean isCanGetLocation(){
        return this.canGetLocation;
    }

    public void showSettingAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        //Title
        alertDialog.setTitle("GPS has been set");

        //message
        alertDialog.setMessage("GPS is not enabled");

        alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}