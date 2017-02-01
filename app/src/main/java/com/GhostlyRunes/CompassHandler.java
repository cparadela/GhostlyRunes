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

import java.util.Random;


/**
 * Created by Charlie on 28/01/2017.
 */

public class CompassHandler extends MainActivity implements SensorEventListener {

    private ImageView aguja;
    private float currentDegree =0f;


    private static final float NS2S = 1.0f / 1000000000.0f;

    int fantasma=0;
    boolean found=false;
    boolean f = false, vib=false;

    long t0;
    float timestamp;
    int error=5;


    long time_not_vib=0;

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

        newGhost(); //Asinamos pos al fantasma


    }

    @SuppressWarnings("deprecation")
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION ),
                SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    protected void onStop() { //anular el registro del listener
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        if(hasCompass) sm.unregisterListener(this);
        super.onStop();
    }



    @Override
    public void onSensorChanged(SensorEvent event) {


        timestamp = event.timestamp;
        Log.d("Tiempo", "------->: "+timestamp*NS2S);




        int degree = Math.round(event.values[0]);  //We get the direction the phone is pointing

        Log.d("Compass", "ROUND: "+degree);

        // We create an animation where we will move our compass, making sure it's always pointing north
        RotateAnimation anim = new RotateAnimation(currentDegree,-degree,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);

        // Time it will take the animation to finish
        anim.setDuration(210);

        // set the animation after the end of the reservation status
        anim.setFillAfter(true);


        // Start the animation
        aguja.startAnimation(anim);

        //We check if we are getting close to finding the ghost

        if(!found && Math.abs(degree - fantasma) < 20 ){

            if ((event.timestamp-time_not_vib)*NS2S > (0.03f*Math.abs(degree - fantasma))) { //Para evitar vibracion constante
                if (vib == false) {
                    vib = true;
                    time_not_vib=event.timestamp;
                }else{
                    if (vib == true) {
                        Log.d("FANTASMA", "ALEJADO");
                        vib = false;
                        time_not_vib = 0;
                    }

                }
                try {
                    Log.d("FANTASMA", "---->CERCA:"+ ((event.timestamp-time_not_vib)*NS2S));
                    this.transmitMessage(COMPID, "doPulsation");  //this porque extiende MainActivity que implementa Message Receiver
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (Math.abs(degree - fantasma) < error) {

            if (f == false) {
                f = true;
                t0=event.timestamp;
            }
            if(!found && f==true && t0>0 && (event.timestamp-t0)*NS2S>2.5f){
                Log.d("FANTASMA", "ENCONTRADO");
                found=true;
                //newGhost();
                try {
                    this.transmitMessage(COMPID, "ghostFound");
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        } else {
            if (f == true) {
                Log.d("FANTASMA", "FUERA");
                f = false;
                t0=-1;
            }

        }

        currentDegree = -degree;

    }

    void newGhost(){
        Random rand= new Random();
        fantasma=rand.nextInt(360);
        Log.d("CREADO BRUJULA","FANTASMA: "+fantasma+"\n\n");
    }




    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



}
