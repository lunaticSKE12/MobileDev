package com.example.mr_ja.loginfirebase;

import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ViewLocation extends AppCompatActivity {

    private Button getLocation,stopGPS;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    GPSTracker gps;
    private int counter = 1;

    Handler h = new Handler();
    Thread task;
    private long startTime;
    private String timeString;
    private TextView timerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewlocation);
        timerView = (TextView) findViewById(R.id.timerView_id);
        getLocation = (Button) findViewById(R.id.getLocation_id);
        stopGPS = (Button) findViewById(R.id.stopLocation_id);

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission) != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{mPermission}, REQUEST_CODE_PERMISSION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        stopGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopHandlerTask();
                timerView.setText("Location Service is Stopped");
            }
        });

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                gps = new GPSTracker(ViewLocation.this);
//
//                if (gps.canGetLocation()){
//                    double latitude = gps.getLatitude();
//                    double longitude = gps.getLongitude();
//
//                    Toast.makeText(getApplicationContext(), counter + "\nCurrent location is \n Lat: " + latitude
//                    + "\nLong: " + longitude, Toast.LENGTH_SHORT).show();
//                }else {
//                    gps.showSettingAlert();
//                }
//                counter++;
                startTimer();
                counter = 1;
            }
        });
    }

    public void startTimer(){
        startTime = System.currentTimeMillis();
        task = new Thread(){
            @Override
            public void run() {
               long millis = System.currentTimeMillis() - startTime;
               long secs = millis / 1000 % 60; //0-59
                gps = new GPSTracker(ViewLocation.this);

                if (gps.canGetLocation()){
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    Toast.makeText(getApplicationContext(), counter + "\nCurrent location is \n Lat: " + latitude
                            + "\nLong: " + longitude, Toast.LENGTH_SHORT).show();
                }else {
                    gps.showSettingAlert();
                }

                timeString = String.format("%02d", secs);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timerView.setText(timeString);
                    }
                });
                h.postDelayed(task,1000);
            }
        };
        h.postDelayed(task, 1000);

    }

    //To call whenever you have to stop the task
    public void stopHandlerTask(){
        h.removeCallbacks(task);

    }
}

