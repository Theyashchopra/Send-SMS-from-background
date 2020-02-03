package com.example.andriod.emergencytest;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TimerActivity extends AppCompatActivity {
    private TextView cdt;
    private Button cdb;
    private TextView tv;
    private CountDownTimer count;
    private long timeleft=6000;
    boolean timerrunning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        cdt = findViewById(R.id.countdown_txt);
        cdb = findViewById(R.id.countdown_btn);
        tv = findViewById(R.id.alert);
        updateTimer();
        startTimer();
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
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("+918806627745", null, "hi", null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
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
