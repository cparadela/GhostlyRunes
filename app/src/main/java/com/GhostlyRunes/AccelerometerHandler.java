package com.GhostlyRunes;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

/**
 * Created by Miguel on 21/01/2017.
 */

public class AccelerometerHandler implements SensorEventListener {


    //------------- ACCELEROMETER VARIABLES BEGIN ---------------------
    private float accX, accY, accZ;
    private float max = 19, min = -19;
    private float medMax=10, medMin=-10;
    private boolean mist_half_gone=false, mist_gone=false;
    private String ACCELID;

    //------------- ACCELEROMETER VARIABLES END ---------------------

    //------------- OTHER VARIABLES BEGIN ---------------------

    MessageReceiver MR;
    //------------- OTHER VARIABLES END ---------------------



    public AccelerometerHandler(MessageReceiver MR, String accelid){

        this.MR=MR;
        this.ACCELID=accelid;
        mist_gone=false;
        mist_half_gone=false;
    }


    public void onSensorChanged(SensorEvent event) {

        //------------- ACCELEROMETER BEGIN---------------------

        //We get the values of the axis X,Y,Z of the accelerometer
        accX = event.values[0];
        accY = event.values[1];
        accZ = event.values[2];


        if(!mist_gone &&((accX> max && accY > max) || (accX > max && accZ > max) || (accY > max && accZ > max) ||
                (accX < min && accY < min) || (accX < min && accZ < min) || (accY < min && accZ < min))){
            mist_gone=true;


                MR.transmitMessage(ACCELID, "moveStrong");



            Log.d("M", "MIST GONE");

        }
        else if(!mist_gone && !mist_half_gone &&((accX> medMax && accY > medMax) || (accX > medMax && accZ > medMax) || (accY > medMax && accZ > medMax) ||
                (accX < medMin && accY < medMin) || (accX < medMin && accZ < medMin) || (accY < medMin && accZ < medMin))){
            mist_half_gone=true;

            MR.transmitMessage(ACCELID, "moveSoft");


            Log.d("M", "MIST HALF GONE");


        }

        //------------- ACCELEROMETER END ---------------------
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}

