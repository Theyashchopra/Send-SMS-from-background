package com.example.andriod.emergencytest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ACCESS_FINE_LOCATION = 111, REQUEST_SEND_SMS = 112;

    public static Location currLoc;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    String uri;
    private EditText editTextInput;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =31;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextInput= findViewById(R.id.edit_text_input);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        boolean hasPermissionLocation = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionLocation) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_ACCESS_FINE_LOCATION);
        }

        boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionWrite) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.SEND_SMS},
                    REQUEST_SEND_SMS);
        }
        fetchLastLocation();
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
                    currLoc = location;
                    Log.i("Location",location.toString());
                }
            }
        });
    }
@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
        case REQUEST_ACCESS_FINE_LOCATION: {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Permission granted.", Toast.LENGTH_SHORT).show();
                //reload my activity with permission granted or use the features what required the permission
            } else {
                Toast.makeText(MainActivity.this, "The app was not allowed to get your location. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
            }
        }
        case REQUEST_SEND_SMS: {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Permission granted.", Toast.LENGTH_SHORT).show();
                //reload my activity with permission granted or use the features what required the permission
            } else {
                Toast.makeText(MainActivity.this, "The app was not allowed to write to your sms. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
    public void checkper() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SEND_SMS);
            }
        }
    }
    public void startService(View v) {
        String input = editTextInput.getText().toString();

        Intent serviceIntent = new Intent(this, ExampleService.class);
        serviceIntent.putExtra("inputExtra", input);
        ContextCompat.startForegroundService(this, serviceIntent);
        fetchLastLocation();
        // checkper();
    }

    public void stopService(View v) {
        Intent serviceIntent = new Intent(this, ExampleService.class);
        stopService(serviceIntent);
    }
}
