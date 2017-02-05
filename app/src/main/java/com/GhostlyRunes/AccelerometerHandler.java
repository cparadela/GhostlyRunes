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
    private boolean move=false;
    private boolean enter=true, mist_half_gone=false, mist_gone=false;
    private String ACCELID;
    public float [] rotationCurrent = new float[9];





    //------------- ACCELEROMETER VARIABLES END ---------------------

    //------------- OTHER VARIABLES BEGIN ---------------------

    MessageReceiver MR;
    //------------- OTHER VARIABLES END ---------------------



    public AccelerometerHandler(MessageReceiver MR, String accelid){
        rotationCurrent[0]=1.0f;
        rotationCurrent[4]=1.0f;
        rotationCurrent[8]=1.0f;
        this.MR=MR;
        this.ACCELID=accelid;
        mist_gone=false;
        mist_half_gone=false;
    }


    public void onSensorChanged(SensorEvent event) {

        //------------- GYROSCOPE BEGIN---------------------
        /*

        // This timestep's delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.
        if (timestamp != 0) {
            final float dT = (event.timestamp - timestamp) * NS2S;
            // Axis of the rotation sample, not normalized yet.
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            // Calculate the angular speed of the sample
            float omegaMagnitude = (float) Math.sqrt(axisX*axisX + axisY*axisY + axisZ*axisZ);

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
        timestamp = event.timestamp;
        float[] deltaRotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
        // User code should concatenate the delta rotation we computed with the current rotation
        // in order to get the updated rotation.
        //rotationCurrent = rotationCurrent * deltaRotationMatrix;
        float [] tempMatrix = new float[9];
        for (int i=0; i<3; i++ ) {
            for (int j=0; j<3; j++ ) {
                tempMatrix[i*3 + j]=0;
                for (int z = 0; z < 3; z++) {
                    tempMatrix[i*3 + j] += rotationCurrent[i*3+z]*deltaRotationMatrix[z*3+j];

                }
            }

        }

        rotationCurrent=tempMatrix;
        Log.d("Rotation Current:", " "+rotationCurrent[0]+" "+rotationCurrent[1]+" "+rotationCurrent[2]);
        float[] gyroscopeOrientation = new float[3];
        SensorManager.getOrientation(rotationCurrent,
                gyroscopeOrientation);
        /*

        Log.d( "Orientacion","\n\nOrientation X (Roll) :" + (new DecimalFormat("#.##").format((double)gyroscopeOrientation[0]))
                + "\n\n" + "Orientation Y (Pitch) :"
                + (new DecimalFormat("#.##").format((double)gyroscopeOrientation[1]))+ "\n\n"
                + "Orientation Z (Yaw) :" + (new DecimalFormat("#.##").format((double)gyroscopeOrientation[2])));
        */

        //------------- GYROSCOPE END ---------------------

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

