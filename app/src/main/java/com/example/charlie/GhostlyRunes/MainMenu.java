package com.example.charlie.GhostlyRunes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    private Button start_gyro;
    private Button start_compass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        start_gyro = (Button) findViewById(R.id.start_gyro);
        start_gyro.setOnClickListener(this);

        start_compass = (Button) findViewById(R.id.start_compass);
        start_compass.setOnClickListener(this);
    }

    public void onClick(View v){
        if (v==start_gyro){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if (v==start_compass){
            Intent intent = new Intent(this, CompassHandler.class);
            startActivity(intent);
        }


    }




}

