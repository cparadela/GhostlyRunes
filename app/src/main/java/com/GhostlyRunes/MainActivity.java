package com.GhostlyRunes;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


//TODO: multilenguaje
public class MainActivity extends AppCompatActivity implements MessageReceiver{
    //Messages
    final String ACCELID="Accelerometer";
    final String TOUCHID="Screen Touch";
    final String GYROID="Gyroscope";
    final String COMPID="Compass";

    boolean hasCompass=true, hasAccel=true,hasGyro=true;

    ImageView niebla;
    int tada, blow;

    public SensorManager mSensorManager; //TODO Convierto la variable en publica para poder llamarla
    private Sensor mSensor;

    private boolean ectoplasma_gone=true;



    //TODO poner string en constructor?
    public AcceleratorHandler AH= new AcceleratorHandler(this, ACCELID);
    public GyroHandler GH = new GyroHandler(this,GYROID);
    public SoundHandler sound;
    public TouchHandler touch;
    //TODO Quitar supressWarnings y controlar versiones
    //@SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        //TODO borrar puntos en comentario, no se utilizan
        //p1 = (ImageView) findViewById(R.id.Point1);
        //p2 = (ImageView) findViewById(R.id.Point2);
        niebla = (ImageView) findViewById(R.id.niebla);

        sound = new SoundHandler(getApplicationContext());
        tada=sound.load(R.raw.tada);
        blow=sound.load(R.raw.blow);

        //TODO quitar String de constructor?
        View view = findViewById(R.id.activity_main);
        touch= new TouchHandler(this, TOUCHID);
        view.setOnTouchListener(touch);

        //TODO Para ignorar la niebla y testear. Borrar al final
        //niebla.setAlpha(0.0f);
        //touch.startChecking();



    }

    @Override //TODO Cambiar comentarios y cosillas
    protected void onResume() {
        super.onResume(); //registro del listener
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        if(!hasAccel || sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)==null){
          Log.w("ACCELEROMETER", "NOT FOUND");
            if(hasAccel) Toast.makeText(getApplicationContext(), "Esta apliación no soporta dispositivos sin acelerómetro.", Toast.LENGTH_LONG).show();
            hasAccel=false;
        }else if(!ectoplasma_gone){
            sm.registerListener(AH,sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
        }
        //TODO Solucionar que no entre en giroscopio sino lo tiene

/*
        if(!hasGyro || sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE)==null){
            Log.w("GYROSCOPE", "NOT FOUND");
            if(hasGyro) Toast.makeText(getApplicationContext(), "Este dispositivo no tiene giroscopio. Algunas funciones de esta aplicación no estarán disponibles.", Toast.LENGTH_LONG).show();
            hasGyro=false;
        }else{
            sm.registerListener(GH,sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE),SensorManager.SENSOR_DELAY_GAME);
        }
        */

    }

    @Override
    protected void onStop() { //anular el registro del listener
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        if(hasAccel) sm.unregisterListener(AH);
        if(hasGyro) sm.unregisterListener(GH);
        super.onStop();
    }

    @Override
    public boolean transmitMessage(String sender,String message) throws InterruptedException {
        switch (sender) {
            case ACCELID:
            if (message == "moveStrong") {
                sound.play(blow);
                Thread.sleep(1200);
                niebla.setAlpha(0.0f);
                ectoplasma_gone = true;
                touch.startChecking(); //Starts Minigame
                Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                vib.vibrate(500);

            } else if (message == "moveSoft") {
                Toast.makeText(getApplicationContext(), "¡Tienes que agitar más fuerte para que se vaya el ectoplasma!", Toast.LENGTH_LONG).show();

            }
                break;
            case TOUCHID:
                if(message == "pathStart") {
                    Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                    vib.vibrate(100);
                }else if(message == "destinyReached"){
                    Toast.makeText(getApplicationContext(), "¡Bien, has desbloqueado la runa!", Toast.LENGTH_LONG).show();
                    sound.play(tada);
                    //Creating new activity
                    Intent intent = new Intent(this, StarPatternActivity.class);
                    startActivity(intent);
                }else if(message == "pathLost"){
                    Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                    vib.vibrate(200);
                }
                break;
            case GYROID:
                if(message == "doPulsation") {
                    Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                    vib.vibrate(50);
                }else if(message == "ghostFound"){
                    Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                    vib.vibrate(500);
                }
                break;

            case COMPID:
                if(message == "doPulsation") {
                    Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                    vib.vibrate(50);
                }else if(message == "ghostFound"){
                    Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                    vib.vibrate(500);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);

                }
                break;
        }

        return true;
    }
}
