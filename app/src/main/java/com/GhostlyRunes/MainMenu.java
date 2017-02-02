package com.GhostlyRunes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;


public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    private Button start_gyro;
    private Button start_compass;
    private Button credits;
    private Button instructions;
    private Button spanish, english, german;
    SensorManager sm;
    MessageReceiver MR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        start_gyro = (Button) findViewById(R.id.start_gyro);
        start_gyro.setOnClickListener(this);

        start_compass = (Button) findViewById(R.id.start_compass);
        start_compass.setOnClickListener(this);

        credits = (Button) findViewById(R.id.credit_button);
        credits.setOnClickListener(this);

        instructions = (Button) findViewById(R.id.instr_button);
        instructions.setOnClickListener(this);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


    }

    public void onClick(View v){
        if (v==start_gyro){
            //Checks if gyroscope is available
             if(sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE)==null){
                    Log.w("GYROSCOPE", "NOT FOUND");
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noGyroscope), Toast.LENGTH_LONG).show();
             }else{

                  Intent intent = new Intent(this, GyroActivity.class);
                  startActivity(intent);
             }

        }
        else if (v==start_compass){
            //Checks if gyroscope is available
             if(sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)==null) {
                    Log.w("COMPASS", "NOT FOUND");
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noCompass), Toast.LENGTH_LONG).show();
             }else {
                 Intent intent = new Intent(this, CompassActivity.class);
                 startActivity(intent);
             }
        }
        else if (v==credits){
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.creditTitle))
                    .setMessage(getResources().getString(R.string.creditMessage))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })

                    .setIcon(android.R.drawable.ic_dialog_email)
                    .show();

        }
        else if (v==instructions){
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.instructionTitle))
                    .setMessage(getResources().getString(R.string.instructionMessage))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })

                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();

        }else if (v==spanish){
            Locale localees = new Locale("es");
            Locale.setDefault(localees);
            Configuration configes = new Configuration();
            configes.locale = localees;
            getBaseContext().getResources().updateConfiguration(configes, getBaseContext().getResources().getDisplayMetrics());
        }else if (v==english){
            Locale localeen = new Locale("en");
            Locale.setDefault(localeen);
            Configuration configen = new Configuration();
            configen.locale = localeen;
            getBaseContext().getResources().updateConfiguration(configen, getBaseContext().getResources().getDisplayMetrics());
        }else if (v==german){
            Locale localede = new Locale("de");
            Locale.setDefault(localede);
            Configuration configde = new Configuration();
            configde.locale = localede;
            getBaseContext().getResources().updateConfiguration(configde, getBaseContext().getResources().getDisplayMetrics());
        }
    }
}

