package com.GhostlyRunes;

import android.app.AlertDialog;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;


public class MainMenu extends AppCompatActivity implements View.OnClickListener, ComponentCallbacks {

    private Button start_gyro;
    private Button start_compass;
    private Button credits;
    private Button instructions;
    private Button volume;
    private int piano;
    private Button spanish, english, german;

    private boolean vol=false;
    SensorManager sm;

    SoundHandler sound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        sound= new SoundHandler(getApplicationContext());
        piano= sound.load(R.raw.piano);

        start_gyro = (Button) findViewById(R.id.start_gyro);
        start_gyro.setOnClickListener(this);

        start_compass = (Button) findViewById(R.id.start_compass);
        start_compass.setOnClickListener(this);

        credits = (Button) findViewById(R.id.credit_button);
        credits.setOnClickListener(this);

        instructions = (Button) findViewById(R.id.instr_button);
        instructions.setOnClickListener(this);

        volume = (Button) findViewById(R.id.vol);
        volume.setOnClickListener(this);

        spanish = (Button) findViewById(R.id.spanish);
        spanish.setOnClickListener(this);

        english = (Button) findViewById(R.id.english);
        english.setOnClickListener(this);

        german = (Button) findViewById(R.id.german);
        german.setOnClickListener(this);




    }

    @Override
    protected void onResume() {
        super.onResume();
        sm= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm=null;
        sound.stop(piano);
    }

    public void onClick(View v) {
        if (v == start_gyro) {
            //Checks if gyroscope is available
            if (sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE) == null) {
                Log.w("GYROSCOPE", "NOT FOUND");
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.noGyroscope), Toast.LENGTH_LONG).show();
            } else {

                Intent intent = new Intent(this, GyroActivity.class);
                startActivity(intent);
                sm = null;
            }

        } else if (v == start_compass) {
            //Checks if gyroscope is available
            if (sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null) {
                Log.w("COMPASS", "NOT FOUND");
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.noCompass), Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(this, CompassActivity.class);
                startActivity(intent);
            }
        } else if (v == credits) {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.creditTitle))
                    .setMessage(Html.fromHtml("<b>" + getResources().getString(R.string.creditCarlos) + "</b>" + getResources().getString(R.string.emailCarlos)
                            + "<br><br><b>" + getResources().getString(R.string.creditMiguel) + "</b>" + getResources().getString(R.string.emailMiguel)))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })

                    .setIcon(android.R.drawable.ic_dialog_email)
                    .show();

        } else if (v == instructions) {


            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.instructionTitle))
                    .setMessage(getResources().getString(R.string.instructionMessage))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })

                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();

        } else if (v == spanish) {
            Locale localees = new Locale("es");
            Locale.setDefault(localees);
            Configuration configes = new Configuration();
            configes.locale = localees;
            getBaseContext().getResources().updateConfiguration(configes, getBaseContext().getResources().getDisplayMetrics());
            Intent intent = new Intent(this, MainMenu.class);
            startActivity(intent);
            finish();
        } else if (v == english) {
            Locale localeen = new Locale("en");
            Locale.setDefault(localeen);
            Configuration configen = new Configuration();
            configen.locale = localeen;
            getBaseContext().getResources().updateConfiguration(configen, getBaseContext().getResources().getDisplayMetrics());
            Intent intent = new Intent(this, MainMenu.class);
            startActivity(intent);
            finish();
        } else if (v == german) {
            Locale localede = new Locale("de");
            Locale.setDefault(localede);
            Configuration configde = new Configuration();
            configde.locale = localede;
            getBaseContext().getResources().updateConfiguration(configde, getBaseContext().getResources().getDisplayMetrics());
            Intent intent = new Intent(this, MainMenu.class);
            startActivity(intent);
            finish();
        } else if (v == volume) {
            if (!vol) {
                volume.setBackgroundResource(R.drawable.vol_on);
                sound.repeat(piano);
                vol = true;
            } else {
                volume.setBackgroundResource(R.drawable.vol_off);
                sound.stop(piano);
                vol = false;
            }
        }
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d("LOW MEMORY","DONE");
        finish();
    }
}

