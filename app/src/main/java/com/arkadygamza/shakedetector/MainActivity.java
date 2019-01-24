package com.arkadygamza.shakedetector;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;

public class MainActivity extends AppCompatActivity {
    private final List<SensorPlotter> mPlotters = new ArrayList<>(3);
    private Observable<?> mShakeObservable;
    private Subscription mShakeSubscription;
    public String state = "DEFAULT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupPlotters();

        mShakeObservable = ShakeDetector.create(this);
    }

    private void setupPlotters() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> gravSensors = sensorManager.getSensorList(Sensor.TYPE_GRAVITY);
        List<Sensor> accSensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        List<Sensor> linearAccSensors = sensorManager.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION);

        mPlotters.add(new SensorPlotter("GRAV", (GraphView) findViewById(R.id.graph1), SensorEventObservableFactory.createSensorEventObservable(gravSensors.get(0), sensorManager)));
        mPlotters.add(new SensorPlotter("ACC", (GraphView) findViewById(R.id.graph2), SensorEventObservableFactory.createSensorEventObservable(accSensors.get(0), sensorManager)));
     //   mPlotters.add(new SensorPlotter("LIN", (GraphView) findViewById(R.id.graph3), SensorEventObservableFactory.createSensorEventObservable(linearAccSensors.get(0), sensorManager)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Observable.from(mPlotters).subscribe(SensorPlotter::onResume);
        mShakeSubscription = mShakeObservable.subscribe((object) -> Utils.beep());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Observable.from(mPlotters).subscribe(SensorPlotter::onPause);
        mShakeSubscription.unsubscribe();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
//            case R.id.line_gyroscope:
//                state = "gyroscope";
//                Intent intent = new Intent(MainActivity.this,GyroscopeActivity.class);
//                startActivity(intent);
//                return true;

//            case R.id.line_accelerometr:
//                state = "accelerometr";
//                return true;
//
//            case R.id.line_accelerometr_geroscope:
//                Intent i = new Intent(MainActivity.this,AccelerGyrosActivity.class);
//                startActivity(i);
//                return true;
//            case R.id.record:
//                state="record";
//                Intent is = new Intent(MainActivity.this,RecordActivity.class);
//                startActivity(is);
//                return true;
//

            default:
                return true;
        }
    }

}
