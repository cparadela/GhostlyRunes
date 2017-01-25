package com.example.charlie.practicaandroid;

import android.content.Context;
import android.annotation.TargetApi;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;


import static java.lang.Math.abs;
//TODO: multilenguaje
public class MainActivity extends AppCompatActivity implements MessageReceiver{
    //TODO Borrar comentario
    //ImageView p1,p2;


    //Messages
    final String ACCELID="Accelerometer";
    final String TOUCHID="Screen Touch";

    ImageView niebla;
    int tada, blow;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private boolean mist_gone=false;

    //TODO poner string en constructor?
    public SensorHandler SR= new SensorHandler(this);
    public SoundHandler sound;
    public TouchHandler touch;
    //TODO Quitar supressWarnings y controlar versiones
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        //TODO borrar puntos en comentario, no se utilizan
        //p1 = (ImageView) findViewById(R.id.Point);
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
        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);  //se utilizará el acelerómetro como sensor
        if (sensors.size() > 0) {
            //indicar tasa de lectura de datos:
            //"SensorManager.SENSOR_DELAY_GAME" que es la velocidad mínima para que el acelerómetro pueda usarse
            sm.registerListener(SR, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onStop() { //anular el registro del listener
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.unregisterListener(SR);
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
                mist_gone = true;
                touch.startChecking(); //Starts Minigame
                Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                vib.vibrate(500);

            } else if (message == "moveSoft") {
                sound.play(blow);
                Thread.sleep(1200);
                niebla.setAlpha(0.4f);
                Toast.makeText(getApplicationContext(), "¡Tienes que agitar más fuerte para que se vaya toda la niebla!", Toast.LENGTH_LONG).show();

            }
                break;
            case TOUCHID:
                if(message == "pathStart") {
                    Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                    vib.vibrate(100);
                }else if(message == "destinyReached"){
                    Toast.makeText(getApplicationContext(), "¡Bien, has desbloqueado la runa!", Toast.LENGTH_LONG).show();
                }else if(message == "pathLost"){
                    Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                    vib.vibrate(200);
                }

        }

        return true;
    }
}
