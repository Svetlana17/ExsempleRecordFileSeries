package com.arkadygamza.shakedetector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityTextView extends AppCompatActivity {
    TextView textX, textY, textZ;
    TextView tv_accX, tv_accY, tv_accZ;
    TextView filtr_acc_x, filtr_acc_y, filtr_acc_z;
    TextView filtr_gur_x, filtr_gur_y, filtr_gyr_z;
    SensorManager gyroManager, accManager;
    Sensor gyroSensor, accSensor;
    float alpha = 0.09f;
    private float xaf, yaf, zaf;
    private float xgf, ygf, zgf;
    private float acc_for_gir_x, acc_for_gir_y,  acc_for_gir_z;
    private float res_kom_filtr_x, res_kom_filtr_y,  res_kom_filtr_z;
    private SensorEvent gyrEvent;
    private SensorEvent accEvent;

    private float k = 0.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view);
        gyroManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroSensor = gyroManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        accManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = accManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        textX = (TextView) findViewById(R.id.textX);
        textY = (TextView) findViewById(R.id.textY);
        textZ = (TextView) findViewById(R.id.textZ);

        tv_accX = (TextView) findViewById(R.id.accX);
        tv_accY = (TextView) findViewById(R.id.accY);
        tv_accZ = (TextView) findViewById(R.id.accZ);

        filtr_acc_x = (TextView) findViewById(R.id.faccX);
        filtr_acc_y = (TextView) findViewById(R.id.faccY);
        filtr_acc_z = (TextView) findViewById(R.id.faccZ);

//для комплементарного ФИЛЬТРА
        filtr_gur_x= (TextView) findViewById(R.id.filtrKomX);
        filtr_gur_y=(TextView) findViewById(R.id.filtrKomY);
        filtr_gyr_z=(TextView) findViewById(R.id.filtrKomZ);

    }

    public void onResume() {
        super.onResume();
        gyroManager.registerListener(gyroListener, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
        accManager.registerListener(accListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        super.onStop();
        gyroManager.unregisterListener(gyroListener);
        accManager.unregisterListener(accListener);}

    public SensorEventListener gyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
           /// xgf = ((1 - k) * event.values[0]+k*accEvent.values[0]);
            textX.setText("X : " + x + " rad/s");
            textY.setText("Y : " + y + " rad/s");
            textZ.setText("Z : " + z + " rad/s");
            filtr_gur_x.setText("KomplF:" + res_kom_filtr_x + "rad/s");
            filtr_gur_y.setText("KomplF:" + res_kom_filtr_y + "rad/s");
            filtr_gyr_z.setText("KomplF:" + res_kom_filtr_z + "rad/s");
        }
    };
    public SensorEventListener accListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
             xaf=xaf+alpha*(x-xaf);
             yaf=yaf+alpha*(y-yaf);
             zaf=zaf+alpha*(z-zaf);

             acc_for_gir_x=k*event.values[0];
             acc_for_gir_y=k*event.values[1];
             acc_for_gir_z=k*event.values[2];

             res_kom_filtr_x=(1-k)*event.values[0]+acc_for_gir_x; //результат комплемнтарного фильтра
             res_kom_filtr_y=(1-k)*event.values[1]+acc_for_gir_y; //результат комплемнтарного фильтра
             res_kom_filtr_z=(1-k)*event.values[2]+acc_for_gir_z; //результат комплемнтарного фильтра

             tv_accX.setText("X Acc : " + x + " m/s2");
            tv_accY.setText("Y Acc: " + y + " m/s2");
            tv_accZ.setText("Z Acc: " + z + " m/s2");
            filtr_acc_x.setText("Xfilt"+xaf+"m/s2");
            filtr_acc_y.setText("Yfilt"+yaf+"m/s2");
            filtr_acc_z.setText("Zfilt"+yaf+"m/s2");


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        class SensorData {

            private SensorEvent gyrEvent;
            private SensorEvent accEvent;

            public void setGyr(SensorEvent gyrEvent) {
                this.gyrEvent = gyrEvent;
            }

            public void setAcc(SensorEvent accEvent) {
                this.accEvent = accEvent;
            }

            public boolean isAccDataExists() {
                return accEvent != null;
            }

            public boolean isGyrDataExists() {
                return gyrEvent != null;
            }

            public void clear() {
                gyrEvent = null;
                accEvent = null;
            }

            public String getStringData() {
                xaf = xaf + alpha * (accEvent.values[0] - xaf);
                yaf = yaf + alpha * (accEvent.values[1] - yaf);
                zaf = zaf + alpha * (accEvent.values[2] - zaf);

                //комплемнтарный
                xgf = (1 - k) * gyrEvent.values[0]+k*accEvent.values[0];
                ygf = (1 - k) * gyrEvent.values[1]+k*accEvent.values[1];
                zgf = (1 - k) * gyrEvent.values[2]+k*accEvent.values[2];
                return String.format("%d; %f; %f; %f; %f; %f; %f; %f; %f; %f; %f; %f; %f;\n", gyrEvent.timestamp,
                        accEvent.values[0], accEvent.values[1], accEvent.values[2], xaf, yaf, zaf,
                        gyrEvent.values[0], gyrEvent.values[1], gyrEvent.values[2], xgf, ygf, zgf);
            }
        }
    };
}