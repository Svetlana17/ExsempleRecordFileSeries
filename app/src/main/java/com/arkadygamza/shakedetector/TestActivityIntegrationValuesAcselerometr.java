package com.arkadygamza.shakedetector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class TestActivityIntegrationValuesAcselerometr extends AppCompatActivity {
    static final int TIMER_DONE = 2;
    static final int START = 3;

   /// private StartCatcher mStartListener;
    private XYZAccelerometer xyzAcc;
    private SensorManager mSensorManager;
    private static final long UPDATE_INTERVAL = 500;
    private static final long MEASURE_TIMES = 20;
    private Timer timer;
    private TextView tv;
    private Button testBtn;
    int counter;
    private MeasureData mdXYZ;


    /** handler for async events*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_integration_values_acselerometr);
        tv = (TextView) findViewById(R.id.txt);
        testBtn = (Button) findViewById(R.id.btn);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(xyzAcc);
        super.onPause();
    }





}
