package com.GhostlyRunes;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


//TODO: multilenguaje
public class MainActivity extends AppCompatActivity implements MessageReceiver{
    //Messages
    final String ACCELID="Accelerometer";
    final String TOUCHID="Screen Touch";

    boolean hasAccel=true;
    boolean first_touch=false;

    ImageView splat;
    int tada, blow;

    SensorManager mSensorManager;

    boolean splat_gone=true;

    public AcceleratorHandler AH= new AcceleratorHandler(this, ACCELID);
    public SoundHandler sound;
    public TouchHandler touch;
    Vibrator vib;

    //TODO Quitar supressWarnings y controlar versiones
    //@SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //splat = (ImageView) findViewById(R.id.splat);

        //TODO cargar antes
        sound = new SoundHandler(getApplicationContext());
        tada=sound.load(R.raw.tada);
        blow=sound.load(R.raw.blow);

        vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);

        touch= new TouchHandler(this, TOUCHID);
        //touch.stopChecking();

        //TODO Para ignorar la splat y testear. Borrar al final
        //splat.setAlpha(0.0f);
        //touch.startChecking();
    }

    @Override //TODO Cambiar comentarios y cosillas
    protected void onResume() {
        super.onResume();
        //registro del listener
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        if(!hasAccel || sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)==null){
          Log.w("ACCELEROMETER", "NOT FOUND");
            if(hasAccel) Toast.makeText(getApplicationContext(), "Esta apliación no soporta dispositivos sin acelerómetro.", Toast.LENGTH_LONG).show();
            hasAccel=false;
        }else if(!splat_gone){
            sm.registerListener(AH,sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
        }
        //TODO Solucionar que no entre en giroscopio sino lo tiene
    }

    @Override
    protected void onStop() { //anular el registro del listener
        super.onStop();
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        if(hasAccel && !splat_gone) sm.unregisterListener(AH);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!first_touch) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                splat.setAlpha(1.0f);

                first_touch = true;
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
                mSensorManager.unregisterListener(AH);
                touch.startChecking(); //Starts Minigame
                vib.vibrate(500);

            } else if (message == "moveSoft") {
                Toast.makeText(getApplicationContext(), "¡Tienes que agitar más fuerte para que se vaya el splata!", Toast.LENGTH_LONG).show();

            }
                break;
            case TOUCHID:
                if(message == "pathStart") {
                    vib.vibrate(100);
                }else if(message == "destinyReached"){
                    Toast.makeText(getApplicationContext(), "¡Bien, has desbloqueado la runa!", Toast.LENGTH_LONG).show();
                    sound.play(tada);
                    //Creating new activity
                    Intent intent = new Intent(this, StarPatternActivity.class);
                    startActivity(intent);
                }else if(message == "pathLost"){
                    vib.vibrate(200);
                }
                break;
        }

        return true;
    }
}
