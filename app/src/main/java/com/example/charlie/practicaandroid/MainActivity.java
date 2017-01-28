package com.example.charlie.practicaandroid;

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

import java.util.List;

//TODO borrar esto
//  pq me mientes git??

import static java.lang.Math.abs;
//TODO: multilenguaje
public class MainActivity extends AppCompatActivity implements MessageReceiver{
    //TODO Borrar comentario
    //ImageView p1,p2;


    //Messages
    final String ACCELID="Accelerometer";
    final String TOUCHID="Screen Touch";
    final String COMPASSID="Compass";

    ImageView niebla;
    int tada, blow;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private boolean mist_gone=false;

    //TODO poner string en constructor?
    public AcceleratorHandler AH= new AcceleratorHandler(this, ACCELID);
    public CompassHandler CH = new CompassHandler(this,COMPASSID);
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

        if(sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)==null){
          Log.w("ACCELEROMETER", "NOT FOUND");
            Toast.makeText(getApplicationContext(), "Esta apliación no soporta dispositivos sin acelerómetro.", Toast.LENGTH_LONG).show();
        }else{
            sm.registerListener(AH,sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
        }

        if(sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)==null){
            Log.w("COMPASS", "NOT FOUND");
            Toast.makeText(getApplicationContext(), "Este dispositivo no tiene brújula. Algunas funciones de esta aplicación no estarán disponibles.", Toast.LENGTH_LONG).show();

        }else{
            sm.registerListener(CH,sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_GAME);
        }

        if(sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE)==null){
            Log.w("GYROSCOPE", "NOT FOUND");
            Toast.makeText(getApplicationContext(), "Este dispositivo no tiene giroscopio. Algunas funciones de esta aplicación no estarán disponibles.", Toast.LENGTH_LONG).show();

        }else{
            sm.registerListener(CH,sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_GAME);
        }



        /*
        List<Sensor> acc_sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);  //se utilizará el acelerómetro como sensor
        List<Sensor> mf_sensors = sm.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
        if (acc_sensors.size() > 0) {
            //indicar tasa de lectura de datos:
            //"SensorManager.SENSOR_DELAY_GAME" que es la velocidad mínima para que el acelerómetro pueda usarse
            sm.registerListener(AH, acc_sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
        }
        if (mf_sensors.size() > 0) {
            //indicar tasa de lectura de datos:
            //"SensorManager.SENSOR_DELAY_GAME" que es la velocidad mínima para que el acelerómetro pueda usarse
            sm.registerListener(CH, mf_sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
        }*/
    }

    @Override
    protected void onStop() { //anular el registro del listener
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.unregisterListener(AH);
        sm.unregisterListener(CH);
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
                Toast.makeText(getApplicationContext(), "¡Tienes que agitar más fuerte para que se vaya toda la niebla!", Toast.LENGTH_LONG).show();

            }
                break;
            case TOUCHID:
                if(message == "pathStart") {
                    Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                    vib.vibrate(100);
                }else if(message == "destinyReached"){
                    Toast.makeText(getApplicationContext(), "¡Bien, has desbloqueado la runa!", Toast.LENGTH_LONG).show();

                    //Creating new activity
                    Intent intent = new Intent(this, StarPatternActivity.class);
                    startActivity(intent);
                }else if(message == "pathLost"){
                    Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                    vib.vibrate(200);
                }

        }

        return true;
    }
}
