package com.GhostlyRunes;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * Created by Charlie on 28/01/2017.
 */

public class CompassHandler extends AppCompatActivity implements SensorEventListener {

    private ImageView aguja;
    private SensorManager mSensorManager;
    private float currentDegree =0f;

    private boolean hasCompass=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        aguja = (ImageView) findViewById(R.id.aguja);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if(!hasCompass || mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)==null) {
            Log.w("COMPASS", "NOT FOUND");
            if (hasCompass)
                Toast.makeText(getApplicationContext(), "Este dispositivo no tiene brújula. Algunas funciones de esta aplicación no estarán disponibles.", Toast.LENGTH_LONG).show();
            hasCompass = false;
            Intent intent = new Intent(this, MainMenu.class);
            startActivity(intent);
        }


    }

    @SuppressWarnings("deprecation")
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION ),
                SensorManager.SENSOR_DELAY_GAME);
    }



    @Override
    public void onSensorChanged(SensorEvent event) {


        float degree = Math.round(event.values[0]);  //We get the direction the phone is pointing

        Log.d("Compass", "ROUND: "+Math.round(event.values[0]));

        // We create an animation where we will move our compass, making sure it's always pointing north
        RotateAnimation anim = new RotateAnimation(currentDegree,-degree,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);

        // Time it will take the animation to finish
        anim.setDuration(210);

        // set the animation after the end of the reservation status
        anim.setFillAfter(true);

        // Start the animation
        aguja.startAnimation(anim);
        currentDegree = -degree;

    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



}
