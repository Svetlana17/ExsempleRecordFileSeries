package com.arkadygamza.shakedetector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static java.lang.Math.sin;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sqrt;

public class GiroscopeActivity extends AppCompatActivity {
    TextView textX, textY, textZ;
    TextView tv_accX, tv_accY, tv_accZ;
    TextView matrix0, matrix1, matrix2, matrix3;
    SensorManager gyroManager, accManager;
    Sensor gyroSensor, accSensor;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];//отметка времени частного плавания ;
    private float timestamp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giroscope);
        gyroManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroSensor = gyroManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        accManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = accManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

     //   textX = (TextView) findViewById(R.id.textX);
       // textY = (TextView) findViewById(R.id.textY);
        //textZ = (TextView) findViewById(R.id.textZ);

//        tv_accX = (TextView) findViewById(R.id.accX);
//        tv_accY = (TextView)findViewById(R.id.accY);
//        tv_accZ = (TextView) findViewById(R.id.accZ);

        matrix0 = (TextView) findViewById(R.id.Mat0);
        matrix1 = (TextView) findViewById(R.id.Math1);
        matrix2 = (TextView) findViewById(R.id.Math2);
        matrix3 = (TextView) findViewById(R.id.Math3);


    }

    public void onResume() {
        super.onResume();
        gyroManager.registerListener(gyroListener, gyroSensor,
                SensorManager.SENSOR_DELAY_NORMAL);

        accManager.registerListener(accListener, accSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        super.onStop();
        gyroManager.unregisterListener(gyroListener);
       /// accManager.unregisterListener(accListener);

    }
//гирскоп
    public SensorEventListener gyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

//            textX.setText("X : " + x + " rad/s");
////            textY.setText("Y : " + y + " rad/s");
////            textZ.setText("Z : " + z + " rad/s");

        }
    };
//акселерометр
    public SensorEventListener accListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
////---------
            if(timestamp!=0){
                final float dT = (event.timestamp - timestamp) * NS2S;
                // Axis of the rotation sample, not normalized yet.
                float axisX = event.values[0];
                float axisY = event.values[1];
                float axisZ = event.values[2];

                // Calculate the angular speed of the sample//=ускорению свободного падениЯ по акслерометру
                float omegaMagnitude = (float) sqrt(axisX*axisX + axisY*axisY + axisZ*axisZ);

                // Normalize the rotation vector if it's big enough to get the axis
//                if (omegaMagnitude > EPSILON) {
//                    axisX /= omegaMagnitude;
//                    axisY /= omegaMagnitude;
//                    axisZ /= omegaMagnitude;
//                }

                // Integrate around this axis with the angular speed by the time step
                // in order to get a delta rotation from this sample over the time step
                // We will convert this axis-angle representation of the delta rotation
                // into a quaternion before turning it into the rotation matrix.
                // Интегрируем вокруг этой оси угловую скорость по временному шагу
                // // чтобы получить дельта-вращение из этого образца за временной шаг
                // / Преобразуем это угловое представление дельта-вращения
                // в кватернион перед тем, как превратить его в матрицу вращения.

                float thetaOverTwo = omegaMagnitude * dT / 2.0f;
                float sinThetaOverTwo = (float) sin(thetaOverTwo);
                float cosThetaOverTwo = (float) cos(thetaOverTwo);
                deltaRotationVector[0] = sinThetaOverTwo * axisX;
                deltaRotationVector[1] = sinThetaOverTwo * axisY;
                deltaRotationVector[2] = sinThetaOverTwo * axisZ;
                deltaRotationVector[3] = cosThetaOverTwo;
            }
            timestamp = event.timestamp;
            float[] deltaRotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
            // User code should concatenate the delta rotation we computed with the current
            // rotation in order to get the updated rotation.
           // rotationCurrent = rotationCurrent * deltaRotationMatrix;

//            tv_accX.setText("X Acc : " + x + " m/s2");
//            tv_accY.setText("Y Acc: " + y + " m/s2");
//            tv_accZ.setText("Z Acc: " + z + " m/s2");

            matrix0.setText("Math0 : " + deltaRotationVector[0]);
            matrix1.setText("Math1: " + deltaRotationVector[1]);
            matrix2.setText("Math2 " + deltaRotationVector[2]);
            matrix3.setText("Math3: " + deltaRotationVector[3]);


        }






        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
