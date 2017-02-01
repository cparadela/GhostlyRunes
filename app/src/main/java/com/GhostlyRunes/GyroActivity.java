package com.GhostlyRunes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Charlie 28/01/2017.
 */

public class GyroActivity extends AppCompatActivity implements SensorEventListener {
    //DEBUG VARIABLES
    int count=0;
    int maxcount=20;

    // Create a constant to convert nanoseconds to seconds.
    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    private float timestamp;
    private final float EPSILON=  0.000001f;


    //Game variables
    float ghost=1.1f; //Where the ghost is
    boolean found=false; //Did it found the ghost?
    float error =0.2f;  //Error in pointing at
    long t0;            //Time pointing at ghost already
    boolean checking=true; //If we are playing with the ghost
    float time_not_vib=0;   //Time to check vibration pulses
    boolean f = false;      //If we are currently pointing at the ghost.

    public float [] rotationCurrent = new float[9];

    //Imageviews
    private ImageView arrow;

    //Services
    Vibrator vib;
    SensorManager mSensorManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyro);
        vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        rotationCurrent[0]=1.0f;
        rotationCurrent[4]=1.0f;
        rotationCurrent[8]=1.0f;
        newGhost();

        arrow = (ImageView) findViewById(R.id.arrow);
    }

    @Override
    public void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE ), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onStop(){
        super.onStop();
        mSensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent event) {
        //SECTION: PREPROCESSING OF GYROSCOPE
        // This timestep's delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.


        if (timestamp != 0) {
            final float dT = (event.timestamp - timestamp) * NS2S;
            time_not_vib+=dT;
            // Axis of the rotation sample, not normalized yet.
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            // Calculate the angular speed of the sample
            float omegaMagnitude = (float) Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);

            // Normalize the rotation vector if it's big enough to get the axis
            // (that is, EPSILON should represent your maximum allowable margin of error)
            if (omegaMagnitude > EPSILON) {
                axisX /= omegaMagnitude;
                axisY /= omegaMagnitude;
                axisZ /= omegaMagnitude;

            }

            // Integrate around this axis with the angular speed by the timestep
            // in order to get a delta rotation from this sample over the timestep
            // We will convert this axis-angle representation of the delta rotation
            // into a quaternion before turning it into the rotation matrix.
            float thetaOverTwo = omegaMagnitude * dT / 2.0f;
            float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
            float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);
            deltaRotationVector[0] = sinThetaOverTwo * axisX;
            deltaRotationVector[1] = sinThetaOverTwo * axisY;
            deltaRotationVector[2] = sinThetaOverTwo * axisZ;
            deltaRotationVector[3] = cosThetaOverTwo;
        }

        //TODO esto no deberia ir al principio antes del if??
        timestamp = event.timestamp;
        float[] deltaRotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
        // User code should concatenate the delta rotation we computed with the current rotation
        // in order to get the updated rotation.
        //rotationCurrent = rotationCurrent * deltaRotationMatrix;
        float[] tempMatrix = new float[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tempMatrix[i * 3 + j] = 0;
                for (int z = 0; z < 3; z++) {
                    tempMatrix[i * 3 + j] += rotationCurrent[i * 3 + z] * deltaRotationMatrix[z * 3 + j];

                }
            }

        }

        rotationCurrent = tempMatrix;
        float[] gyroscopeOrientation = new float[3];
        SensorManager.getOrientation(rotationCurrent,
                gyroscopeOrientation);


        //SECTION: PHANTOM GAME
    if(checking){
        float ghost_dist =Math.abs(gyroscopeOrientation[0] - ghost);
        if(ghost_dist>Math.PI){
            ghost_dist= 2*((float)Math.PI)-ghost_dist;
        }
        if(ghost_dist<30) {
            arrow.setAlpha(1 - ghost_dist / 30);
        }
        else {
            arrow.setAlpha(0.0f);
        }

        if(!found && ghost_dist < time_not_vib){
            vib.vibrate(50);
            time_not_vib=0;
        }
        if (ghost_dist < error) {

            if (f == false) {
                f = true;
                t0=event.timestamp;
            }
            if(!found && f==true && t0>0 && (event.timestamp-t0)*NS2S>2.5f){
                Log.d("FANTASMA", "ENCONTRADO");
                found=true;
                checking=false;
                //newGhost();
                vib.vibrate(500);
            }


        } else {
            if (f == true) {
                Log.d("FANTASMA", "FUERA");
                f = false;
                t0=-1;
            }

        }
        }
    }
    void newGhost(){
        ghost=(float) (Math.random()*6-3);
        found=false;
        Log.d("CREADO GYRO","FANTASMA"+ghost);
    }

    void startChecking(){
        checking=true;
    }
    void stopChecking(){
        checking=false;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}

