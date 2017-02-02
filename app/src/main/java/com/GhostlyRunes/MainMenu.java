package com.GhostlyRunes;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    private Button start_gyro;
    private Button start_compass;
    SensorManager sm;
    MessageReceiver MR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        start_gyro = (Button) findViewById(R.id.start_gyro);
        start_gyro.setOnClickListener(this);

        start_compass = (Button) findViewById(R.id.start_compass);
        start_compass.setOnClickListener(this);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


    }

    public void onClick(View v){
        if (v==start_gyro){
            //Checks if gyroscope is available
             if(sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE)==null){
                    Log.w("GYROSCOPE", "NOT FOUND");
                    Toast.makeText(getApplicationContext(), "Su dispositivo no tiene giroscopio.", Toast.LENGTH_LONG).show();
             }else{

                  Intent intent = new Intent(this, GyroActivity.class);
                  startActivity(intent);
             }

        }
        if (v==start_compass){
            //Checks if gyroscope is available
             if(sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)==null) {
                    Log.w("COMPASS", "NOT FOUND");
                    Toast.makeText(getApplicationContext(), "Su dispositivo no tiene brújula.", Toast.LENGTH_LONG).show();
             }else {
                 Intent intent = new Intent(this, CompassActivity.class);
                 startActivity(intent);
             }
        }
    }
}

