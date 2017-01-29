package com.example.charlie.practicaandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    private Button Start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        Start = (Button) findViewById(R.id.start_button);
        Start.setOnClickListener(this);
    }

    public void onClick(View v){
        if (v==Start){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }




}

