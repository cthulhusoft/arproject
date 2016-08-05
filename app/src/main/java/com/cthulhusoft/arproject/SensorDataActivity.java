package com.cthulhusoft.arproject;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class SensorDataActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Sensor mMagnetometerSensor;
    private SensorEventListener mAccelerometerListener;
    private SensorEventListener mMagnetoListener;
    private TextView mSensorInfo, mSensor1, mSensor2;
    private float[] mRotationMatrix = new float[9];
    private float[] mRotationAngles = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_data);

        mSensorInfo = (TextView) findViewById(R.id.sensor_info);
        mSensorInfo.setText("Getting sensor info...");

        mSensor1 = (TextView) findViewById(R.id.sensor1);
        mSensor1.setText("Awaiting sensor data...");

        mSensor2 = (TextView) findViewById(R.id.sensor2);
        mSensor2.setText("Awaiting sensor data...");

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.getSensorInfo();
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        mAccelerometerListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                mSensor1.setText(String.format("%.1f", sensorEvent.values[0]) + ", "
                        + String.format("%.1f", sensorEvent.values[1]) + ", "
                        + String.format("%.1f", sensorEvent.values[2]));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        mMagnetoListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                mSensor2.setText(String.format("%.1f", sensorEvent.values[0]) + ", "
                        + String.format("%.1f", sensorEvent.values[1]) + ", "
                        + String.format("%.1f", sensorEvent.values[2]));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

    private void getSensorInfo() {
        List<Sensor> accelSensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        List<Sensor> magnetoSensors = mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
        String accelSensorInfo = "Accelerometer sensors:\n";
        for (Sensor sensor : accelSensors) {
            accelSensorInfo = accelSensorInfo + "\t" + sensor.getName() + ": "
                    + sensor.getVendor() + " " + sensor.getVersion() + "\n";
        }
        String magnetoSensorInfo = "\nMagneto sensors:\n";
        for (Sensor sensor : magnetoSensors) {
            magnetoSensorInfo = magnetoSensorInfo + "\t" + sensor.getName() + ": "
                    + sensor.getVendor() + " " + sensor.getVersion() + "\n";
        }

        String cameraInfo = "\nCameras:\n";
        try {
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            String[] camIds = cameraManager.getCameraIdList();
            for (String id : camIds) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(id);
                int lensFacing = characteristics.get(CameraCharacteristics.LENS_FACING);
                String facing = "";
                if (lensFacing == CameraCharacteristics.LENS_FACING_FRONT) {
                    facing = "Front";
                }
                else if (lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                    facing = "Back";
                }
                cameraInfo = cameraInfo + id + ": " + facing + "\n";
            }
        }
        catch (CameraAccessException e) {
            cameraInfo = cameraInfo + "Failed to get camera info:\n" + e.getMessage();
        }

        mSensorInfo.setText(accelSensorInfo + magnetoSensorInfo + cameraInfo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mAccelerometerListener, mAccelerometerSensor, 2000000);
        mSensorManager.registerListener(mMagnetoListener, mMagnetometerSensor, 2000000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mAccelerometerListener);
        mSensorManager.unregisterListener(mMagnetoListener);
    }
}
