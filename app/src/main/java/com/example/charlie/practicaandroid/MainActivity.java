package com.example.charlie.practicaandroid;

import android.content.Context;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MessageReceiver{
    ImageView p1,p2;
    float x1,x2,y1,y2;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    public SensorHandler SR= new SensorHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        p1 = (ImageView) findViewById(R.id.Point);
        p2 = (ImageView) findViewById(R.id.Point2);



    }


    @Override //TODO Cambiar comentarios y cosillas
    protected void onResume() {
        super.onResume(); //registro del listener
        x1=p1.getX();
        y1=p1.getY();
        x2=p2.getX();
        y2=p2.getY();
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


    //TODO
    @Override
    @TargetApi(19) //TODO borrar
    public boolean onTouchEvent(MotionEvent event){

            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                MotionEvent.PointerCoords current_coords = new MotionEvent.PointerCoords();
                event.getPointerCoords(0, current_coords);

                for (int pos = 0; pos < event.getHistorySize(); pos++) {
                    MotionEvent.PointerCoords historical_coords = new MotionEvent.PointerCoords();
                    event.getHistoricalPointerCoords(0, pos, historical_coords);
                    Log.v("Historical Coords", "" + pos + " " + historical_coords.x + " " + historical_coords.y);
                }
                Log.d("Current_coords: ", "" + current_coords.x + " " + current_coords.y);
                Log.d("Action2Str", MotionEvent.actionToString(event.getAction()));
                //Log.d("Presion",""+presion);
                //Log.d("Coordenadas","");
            }

            return true;
        }

    @Override
    public boolean transmitMessage(String sender,String message){


        return true;
    }
}
