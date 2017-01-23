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
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;


import static java.lang.Math.abs;
//TODO: multilenguaje
public class MainActivity extends AppCompatActivity implements MessageReceiver{
    ImageView p1,p2,niebla;
    float x1,x2,y1,y2;
    int tada, blow;

    boolean first_inline = true;
    boolean first_back = true;
    boolean correct_start=false;
    int checked = 0;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private boolean mist_gone=false;




    public SensorHandler SR= new SensorHandler(this);
    public SoundHandler sound;

    //TODO Quitar supressWarnings y controlar versiones
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        p1 = (ImageView) findViewById(R.id.Point);
        p2 = (ImageView) findViewById(R.id.Point2);
        niebla = (ImageView) findViewById(R.id.niebla);

        sound = new SoundHandler(getApplicationContext());
        tada=sound.load(R.raw.tada);
        blow=sound.load(R.raw.blow);



        //TODO Para ignorar la niebla y testear. Borrar al final
        //niebla.setAlpha(0.0f);



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
    //TODO viscoso pero sabroso
    private float distance(float x1,float x2, float y1, float y2){
        return (float) Math.sqrt( (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1) );

    }

    //TODO comentar código cuando funcione
    //TODO quitar posicion de puntos de onTouchEvent
    @Override
    @TargetApi(19) //TODO borrar
    public boolean onTouchEvent(MotionEvent event){
            //TODO cosa cutre para simular que los puntos están bien
        //TODO Descomentar al final. Así para testear
        if(mist_gone) {
            float x1 = 324.0f;
            float y1 = 727.0f;
            float x2 = 800.0f;
            float y2 = 1010.0f;
            int num_div = 10;
            float[][] line_points = new float[num_div + 1][2];

            line_points[0][0] = x1;
            line_points[0][1] = y1;
            line_points[num_div][0] = x2;
            line_points[num_div][1] = y2;

            for (int i = 1; i < num_div; i++) {
                float auxx = ((x1 - x2) * i) / num_div;
                float auxy = ((y1 - y2) * i) / num_div;
                line_points[i][0] = auxx;
                line_points[i][1] = auxy;
            }
            /*
              x1=p1.getX();
             y1=p1.getY();
             x2=p2.getX();
             y2=p2.getY();*/
            float error_inline = 15.0f;
            float error_back = 40.0f;
            float error_down = 50.0f;
            float min_distance = distance(line_points[0][0], line_points[1][0], line_points[0][1], line_points[1][1]);
            Log.d("Distancia puntos", "" + min_distance);

            float back_historicalx = 0.0f;
            float back_historicaly = 0.0f;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                checked = 0;

                MotionEvent.PointerCoords current_coords = new MotionEvent.PointerCoords();
                if (distance(event.getX(), x1, event.getY(), y1) <= error_down) {
                    correct_start = true;
                    Log.d("Pressed?", "Yes!-----------------------------------------------------------------------");

                } else {
                    correct_start = false;
                    Log.d("Pressed?", "No... --->" + distance(event.getX(), x1, event.getY(), y1));
                    Log.d("Pressed?", "Still no --->" + event.getX() + " " + event.getY() + " " + x1 + " " + y1);
                }

            }

            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if(correct_start) {
                    MotionEvent.PointerCoords current_coords = new MotionEvent.PointerCoords();
                    event.getPointerCoords(0, current_coords);
                    boolean closer = true;
                    boolean inline=false;
                    for(int i=checked; i<=num_div; i++ ){
                        //TODO mirar si no comprobar anterior crea error
                        //TODO mirar anterior y siguiente?
                        if(distance(current_coords.x,line_points[i][0],current_coords.y,line_points[i][1])<error_down+min_distance){
                            checked=i;
                            if (first_inline && closer) {
                                first_inline = !first_inline;
                                Toast.makeText(getApplicationContext(), "En linea", Toast.LENGTH_LONG).show();
                                i=num_div+1;
                                inline=true;
                            }

                        }

                    }
                    if(inline){

                        if (first_inline && closer) {
                            first_inline = !first_inline;
                            Toast.makeText(getApplicationContext(), "En linea", Toast.LENGTH_LONG).show();
                        }
                        if (first_back && !closer) {
                            first_back = !first_back;
                            Toast.makeText(getApplicationContext(), "Marcha atrás", Toast.LENGTH_LONG).show();
                            Log.d("NClose", "not closer (" + distance(back_historicalx, x2, back_historicaly, y2) + "<" + distance(current_coords.x, x2, current_coords.y, y2) + ")");
                            Log.d("NClose", "not closer H: (" + back_historicalx + "," + back_historicaly + ") C:(" + current_coords.x + "," + current_coords.y + ") P:(" + x2 + "," + y2 + ")");
                        }
                        Log.d("Still Line? ", "Yes");
                    } else {

                        first_inline = true;
                        first_back = true;
                        //Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();
                        Log.d("Still not Line? ", "No");
                    }
                    /*for (int pos = 0; pos < event.getHistorySize(); pos++) {
                        MotionEvent.PointerCoords historical_coords = new MotionEvent.PointerCoords();
                        event.getHistoricalPointerCoords(0, pos, historical_coords);
                        if (closer && distance(historical_coords.x, x2, historical_coords.y, y2) - error_back <= distance(current_coords.x, x2, current_coords.y, y2)) {
                            closer = false;
                            back_historicalx = historical_coords.x;
                            back_historicaly = historical_coords.y;
                        }
                        Log.v("Historical Coords", "" + pos + " " + historical_coords.x + " " + historical_coords.y);
                    }*/

                  //TODO condicion
                    //if (abs((current_coords.x - x1) / (x2 - x1) - (current_coords.y - y1) / (y2 - y1)) <= error_inline && distance(x2, y2, current_coords.x, current_coords.y) <= distance(x1, y1, x2, y2)) {
                    /* if(true){
                        if (first_inline && closer) {
                            first_inline = !first_inline;
                            Toast.makeText(getApplicationContext(), "En linea", Toast.LENGTH_LONG).show();
                        }
                        if (first_back && !closer) {
                            first_back = !first_back;
                            Toast.makeText(getApplicationContext(), "Marcha atrás", Toast.LENGTH_LONG).show();
                            Log.d("NClose", "not closer (" + distance(back_historicalx, x2, back_historicaly, y2) + "<" + distance(current_coords.x, x2, current_coords.y, y2) + ")");
                            Log.d("NClose", "not closer H: (" + back_historicalx + "," + back_historicaly + ") C:(" + current_coords.x + "," + current_coords.y + ") P:(" + x2 + "," + y2 + ")");
                        }
                        Log.d("Line?", "Yes");
                    } else {
                        first_inline = true;
                        first_back = true;
                        //Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();
                        Log.d("Line?", "No");
                    }



                    Log.d("Lineability", "" + (x2 - x1) + " " + (y2 - y1));
                    Log.d("Lineability2", "" + abs((current_coords.x - x1) / (x2 - x1) - (current_coords.y - y1) / (y2 - y1)));
                    Log.d("Current_coords: ", "" + current_coords.x + " " + current_coords.y);
                    Log.d("Action2Str", MotionEvent.actionToString(event.getAction()));
                    //Log.d("Presion",""+presion);
                    //Log.d("Coordenadas","");
                    */

                }

            }
        }

            return true;
        }

    @Override
    public boolean transmitMessage(String sender,String message) throws InterruptedException {
        if (message=="moveStrong"){
            sound.play(blow);
            Thread.sleep(1200);
            niebla.setAlpha(0.0f);
            mist_gone=true;
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(500);

        }
        else if(message=="moveSoft") {
            sound.play(blow);
            Thread.sleep(1200);
            niebla.setAlpha(0.4f);
            Toast.makeText(getApplicationContext(),"¡Tienes que agitar más fuerte para que se vaya toda la niebla!", Toast.LENGTH_LONG).show();


        }


        return true;
    }
}
