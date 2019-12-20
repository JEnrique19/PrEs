package com.example.pres;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class fotoActivity extends AppCompatActivity implements SensorEventListener {
    SurfaceView surfaceView;
    Button btnCapture;
    CameraSource cameraSource;

    private static SensorManager sensorManager;
    private Sensor sensor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);
        checkCameraPermission();

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        surfaceView = (SurfaceView) findViewById(R.id.vistaSurface);
        btnCapture = findViewById(R.id.btnCapture);

        final GlobalClass globalClass = (GlobalClass)getApplicationContext();
        btnCapture.setText(globalClass.getTextoPrueba()+"");

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Toast.makeText(fotoActivity.this, "Problemas con camara...", Toast.LENGTH_LONG).show();
        } else {
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setAutoFocusEnabled(true)
                    .build();

            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {
                        cameraSource.start(surfaceView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });
        }

        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {

            }
        });

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) fotoActivity.this.getSystemService(Context.LOCATION_SERVICE);

                LocationListener locationListener = new LocationListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.i("UBICACION","\nLAT: "+location.getLatitude()+" LON: "+location.getLongitude());
                        globalClass.setLat(location.getLatitude());
                        globalClass.setLon(location.getLongitude());


                        //Toast.makeText(fotoActivity.this, "LAT: "+globalClass.getLat()+"\nLON: "+globalClass.getLon(), Toast.LENGTH_LONG).show();
                        finish();
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
                };

                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }
        });

    }

    private void checkCameraPermission() {
        int camPermissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA);
        int gpsPermissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (camPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para la camara!.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 225);
        }else if (gpsPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 225);
        }else {
            Log.i("Mensaje", "Tienes permiso para usar la camara.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (sensor != null){
            sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
        }else{
            Toast.makeText(this,"No soportado",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float azimuth = Math.round(sensorEvent.values[0]);//Norte Sur Este Oeste
        float pitch = Math.round(sensorEvent.values[1]);//Inclinacion
        float roll = Math.round(sensorEvent.values[2]);//Giro a lo largo

        double tesla = Math.sqrt((azimuth * azimuth) + (pitch * pitch) + (roll * roll));

        GlobalClass globalClass = (GlobalClass)getApplicationContext();
        //String texto = String.format("%.0f"+tesla);
        globalClass.setGrados("\nAZ: "+azimuth+"\nPitch: "+pitch+"\nRoll: "+roll);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
