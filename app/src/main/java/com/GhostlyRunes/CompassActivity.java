package com.GhostlyRunes;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;


/**
 * Created by Miguel on 28/01/2017.
 */

public class CompassActivity extends AppCompatActivity implements SensorEventListener {


    private ImageView needle;
    private float currentDegree =0f;
    private ImageView arrow;

    private static final float NS2S = 1.0f / 1000000000.0f;
    private String mode;

    int ghost=0;
    boolean found=false;
    boolean f = false, last_vib=false;
    long t0;
    int error=7;
    int next_ghost=0;
    int prev_ghost=0;
    Random ran;


    long time_not_vib=0;

    //Services
    SensorManager mSensorManager;
    Vibrator vib;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        needle = (ImageView) findViewById(R.id.needle);
        arrow = (ImageView) findViewById(R.id.arrow);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        ran= new Random();
        vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.lookGhost), Toast.LENGTH_LONG).show();


        newGhost(); //Asignamos pos al ghost



    }

    @SuppressWarnings("deprecation")
    protected void onResume() {
        super.onResume();
        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION ), SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    protected void onPause() { //parar el registro del listener
        mSensorManager.unregisterListener(this);
        super.onPause();
    }




    @Override
    public void onSensorChanged(SensorEvent event) {

        int degree = Math.round(event.values[0]);  //We get the direction the phone is pointing

        Log.d("Compass", "ROUND: "+degree);

        // We create an animation where we will move our compass, making sure it's always pointing north
        RotateAnimation anim = new RotateAnimation(currentDegree,-degree,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);

        // Time it will take the animation to finish
        anim.setDuration(210);

        // set the animation after the end of the reservation status
        anim.setFillAfter(true);


        // Start the animation
        needle.startAnimation(anim);
        //We change the value of the currentDegree (place the animation is pointing)
        currentDegree = -degree;

        //We check if we are getting close to finding the ghost


        float dist_ghost=Math.abs(degree - ghost);
        if (dist_ghost > 180) {  //We adjust the value so that it detects distance properly. You can only be 180 degrees away from target
            dist_ghost =360-dist_ghost;
        }
        if(dist_ghost<30) {
            arrow.setAlpha(1 - dist_ghost / 30);
        }
        else {
            arrow.setAlpha(0.0f);
        }
        if (!found && (event.timestamp-time_not_vib)*NS2S > (0.03f*dist_ghost)) { //Para evitar vibracion constante
            if (last_vib == false) {
                last_vib = true;
                time_not_vib=event.timestamp;
            }else{
                if (last_vib == true) {
                    Log.d("ghost", "FAR");
                    last_vib = false;
                    time_not_vib = 0;
                }
            }
            Log.d("ghost", "---->CLOSE:"+ ((event.timestamp-time_not_vib)*NS2S));
            ghostNearby();

        }
        if (dist_ghost < error) {

            if (f == false) {
                f = true;
                t0=event.timestamp;
            }
            if(!found && f==true && t0>0 && (event.timestamp-t0)*NS2S>2.5f){
                Log.d("ghost", "FOUND");
                found=true;
                //newGhost();
                ghostFound();

            }


        } else {
            if (f == true) {
                Log.d("ghost", "OUT");
                f = false;
                t0=-1;
            }

        }



    }

    void newGhost(){
        Random rand= new Random();
        ghost=rand.nextInt(360);
        Log.d("CREATE COMPASS","ghost: "+ghost+"\n\n");
    }

    void ghostNearby(){
        Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        vib.vibrate(50);


    }

    void ghostFound(){
        vib.vibrate(500);
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.ghostFound), Toast.LENGTH_LONG).show();

        prev_ghost=getIntent().getIntExtra("ID",-1);
        next_ghost=ran.nextInt(4);
        if(prev_ghost==next_ghost){
            next_ghost=(prev_ghost+1)%4;
        }
        Intent intent;
        switch(next_ghost){
            case 0: intent = new Intent(this, StarPatternActivity.class);
                break;
            case 1: intent = new Intent(this, SemiCirclePatternActivity.class);
                break;
            case 2: intent = new Intent(this, ZigZagPatternActivity.class);
                break;
            case 3: intent = new Intent(this, SlimerPatternActivity.class);
                break;
            default: intent = new Intent(this, MainMenu.class);
        }

        intent.putExtra("Mode",2);
        startActivity(intent);
        finish();
    }




    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



}
