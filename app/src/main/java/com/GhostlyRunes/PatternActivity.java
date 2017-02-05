package com.GhostlyRunes;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;


public class PatternActivity extends AppCompatActivity implements MessageReceiver{
    //Messages
    final String ACCELID="Accelerometer";
    final String TOUCHID="Screen Touch";

    protected int PATTERNID;

    boolean hasAccel=true;
    boolean first_touch=false;

    ImageView splat;
    int tada, blow, piano;

    SensorManager sm;

    boolean splat_gone=true;

    public AccelerometerHandler AH= new AccelerometerHandler(this, ACCELID);
    public SoundHandler sound;
    public TouchHandler touch;
    Vibrator vib;

    //@SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        sound = new SoundHandler(getApplicationContext());
        tada=sound.load(R.raw.tada);
        blow=sound.load(R.raw.blow);
        piano=sound.load(R.raw.piano);

        vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);

        touch= new TouchHandler(this, TOUCHID);
        touch.stopChecking();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //registro del listener
        sound.play(piano);
        if(!hasAccel || sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)==null){
          Log.w("ACCELEROMETER", "NOT FOUND");
            if(hasAccel) Toast.makeText(getApplicationContext(),getResources().getString(R.string.noAccelerometer) , Toast.LENGTH_LONG).show();
            hasAccel=false;
        }else if(!splat_gone){
            sm.registerListener(AH,sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onPause() { //anular el registro del listener
        super.onPause();
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        if(hasAccel && !splat_gone) sm.unregisterListener(AH);
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!first_touch) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                splat.setAlpha(1.0f);
                splat_gone=false;
                first_touch = true;
                sm.registerListener(AH,sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.splatAppears), Toast.LENGTH_LONG).show();

                Log.d("E", "TOCADO Y HUNDIDO");

            }
        }

        return super.onTouchEvent(event);

    }

    @Override
    public boolean transmitMessage(String sender,String message) {
        switch (sender) {
            case ACCELID:
            if (message == "moveStrong") {
               sound.play(blow);
                try {
                    Thread.sleep(1200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                splat.setAlpha(0.0f);
                splat_gone = true;
                sm.unregisterListener(AH);
                touch.startChecking(); //Starts Minigame
                vib.vibrate(500);

            } else if (message == "moveSoft") {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.shakeStronger), Toast.LENGTH_LONG).show();

            }
                break;
            case TOUCHID:
                if(message == "pathStart") {
                    vib.vibrate(100);
                }else if(message == "destinyReached"){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.ghostDefeated), Toast.LENGTH_LONG).show();
                   sound.play(tada);
                    //Creating new activity
                    if (getIntent().getIntExtra("Mode",0)==1){
                        Intent intent = new Intent(this, GyroActivity.class);
                        intent.putExtra("ID", PATTERNID);
                        startActivity(intent);
                    }
                    else{

                        Intent intent = new Intent(this, CompassActivity.class);
                        intent.putExtra("ID", PATTERNID);
                        startActivity(intent);
                    }
                    finish();

                }else if(message == "pathLost"){
                    vib.vibrate(200);
                }
                break;
        }

        return true;
    }

}
