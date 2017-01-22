package com.example.charlie.practicaandroid;

import android.content.Context;
import android.annotation.TargetApi;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    private SensorManager mSensorManager;
    private Sensor mSensor;

    public SensorHandler SR= new SensorHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        ImageView p1 = (ImageView) findViewById(R.id.Point);
        ImageView p2 = (ImageView) findViewById(R.id.Point2);

    }

    @Override
    protected void onResume() {
        super.onResume(); //registro del listener
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);  //se utilizar� el aceler�metro como sensor
        if (sensors.size() > 0) {
            //indicar tasa de lectura de datos:
            //�SensorManager.SENSOR_DELAY_GAME� que es la velocidad m�nima para que el aceler�metro pueda usarse
            sm.registerListener(SR, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
        }

    @Override
    protected void onStop() { //anular el registro del listener
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.unregisterListener(SR);
        super.onStop();
    }

    @Override
    @TargetApi(19) //TODO borrar
    public boolean onTouchEvent(MotionEvent event){
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                float presion = event.getPressure();
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

}
