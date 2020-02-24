package com.example.andriod.emergencytest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class TimerActivity extends AppCompatActivity {
    FusedLocationProviderClient fusedLocationProviderClient;
    double lat,lon;
    String uri;
    private TextView cdt;
    private Button cdb;
    private TextView tv;
    private CountDownTimer count;
    private long timeleft=6000;
    boolean timerrunning;
    Location l;
    private static final int REQUEST_ACCESS_FINE_LOCATION = 111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        cdt = findViewById(R.id.countdown_txt);
        cdb = findViewById(R.id.countdown_btn);
        tv = findViewById(R.id.alert);
        updateTimer();
        startTimer();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }
    private void fetchLastLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    l = location;
                    Toast.makeText(getApplicationContext(),"hi"+l.toString(),Toast.LENGTH_LONG).show();
                    Log.i("Location",location.toString());
                    lat=l.getLatitude();
                    lon=l.getLongitude();
                    String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lon + " (" + "yo" + ")";
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("+918806627745", null, geoUri, null, null);
                }
            }
        });
        Toast.makeText(this,"SMS Sent",Toast.LENGTH_SHORT).show();
    }
    public void startTimer(){
            count=new CountDownTimer(timeleft,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeleft=millisUntilFinished;
                    updateTimer();
                }
                @Override
                public void onFinish() {
                    fetchLastLocation();
             //       SmsManager smsManager = SmsManager.getDefault();
             //       smsManager.sendTextMessage("+918806627745", null, "hi"+uri, null, null);
             //       Toast.makeText(getApplicationContext(), "SMS sent.",
             //               Toast.LENGTH_LONG).show();
                    cdt.setVisibility(View.GONE);
                    cdb.setVisibility(View.GONE);
                    tv.setVisibility(View.VISIBLE);
                }
            }.start();
            timerrunning=true;
    }
    public void Cancelcd(View view) {
            count.cancel();
            timerrunning=false;
        String a = "Alert Canceled";
        tv.setText(a);
            cdt.setVisibility(View.GONE);
            cdb.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
    }
    public void updateTimer(){
        int secounds=(int)timeleft%6000/1000;
        String timelefttext=""+secounds;
        cdt.setText(timelefttext);
    }

}
