package com.example.charlie.practicaandroid;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by Charlie on 28/01/2017.
 */

public class CompassHandler implements SensorEventListener {
    float x,y,z;

    MessageReceiver MR;

    String COMPASSID;

    CompassHandler(MessageReceiver MR, String id){
        this.MR=MR;
        this.COMPASSID=id;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        x=event.values[0];
        y=event.values[1];
        z=event.values[2];

        //Log.d("COMPASS","X:"+x+" Y:"+y+" Z:"+z);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



}
