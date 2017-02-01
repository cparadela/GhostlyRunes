package com.GhostlyRunes;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainMenu extends MainActivity implements View.OnClickListener{

    private Button start_gyro;
    private Button start_compass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        start_gyro = (Button) findViewById(R.id.start_gyro);
        start_gyro.setOnClickListener(this);

        start_compass = (Button) findViewById(R.id.start_compass);
        start_compass.setOnClickListener(this);
    }

    public void onClick(View v){
        if (v==start_gyro){

                if(!hasGyro || mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)==null){
                    Log.w("GYROSCOPE", "NOT FOUND");
                    if(hasGyro) Toast.makeText(getApplicationContext(), "Este dispositivo no tiene giroscopio. Algunas funciones de esta aplicación no estarán disponibles.", Toast.LENGTH_LONG).show();
                    hasGyro=false;
                }else{
                    mSensorManager.registerListener(GH,mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }

        }
        if (v==start_compass){
         if(!hasCompass || mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)==null) {
                Log.w("COMPASS", "NOT FOUND");
                if (hasCompass)
                    Toast.makeText(getApplicationContext(), "Este dispositivo no tiene brújula. Algunas funciones de esta aplicación no estarán disponibles.", Toast.LENGTH_LONG).show();
                hasCompass = false;
            }
            else {
             Intent intent = new Intent(this, CompassHandler.class);
             startActivity(intent);
         }
        }


    }




}

