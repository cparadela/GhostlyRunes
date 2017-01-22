package com.example.charlie.practicaandroid;

import android.annotation.TargetApi;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    int x1,y1,x2,y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView p1 = (ImageView) findViewById(R.id.Point);
        ImageView p2 = (ImageView) findViewById(R.id.Point2);

    }

    @Override
    //TODO Casitodo
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    @TargetApi(19) //TODO borrar
    public boolean onTouchEvent(MotionEvent event){
        if (event.getAction()==MotionEvent.ACTION_MOVE) {
            float presion=event.getPressure();
            MotionEvent.PointerCoords current_coords = new MotionEvent.PointerCoords();
            event.getPointerCoords(0,current_coords);

            for(int pos=0; pos<event.getHistorySize(); pos++){
                MotionEvent.PointerCoords historical_coords = new MotionEvent.PointerCoords();
                event.getHistoricalPointerCoords(0,pos,historical_coords);
                Log.v("Historical Coords", ""+pos+" "+ historical_coords.x+" "+historical_coords.y);
            }
            Log.d("Current_coords: ", ""+current_coords.x + " "+current_coords.y);
            Log.d("Action2Str",MotionEvent.actionToString(event.getAction()));
            //Log.d("Presion",""+presion);
            //Log.d("Coordenadas","");
        }

        return true;
    }
}
