package com.GhostlyRunes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class SlimerPatternActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slimer_pattern);

        splat = (ImageView) findViewById(R.id.splat);
        splat.setAlpha(0.0f);

        splat_gone=false;


        int[] pattern = new int[6];
        pattern[0]=R.id.Point1;
        pattern[1]=R.id.Point2;
        pattern[2]=R.id.Point3;
        pattern[3]=R.id.Point4;
        pattern[4]=R.id.Point5;
        pattern[5]=R.id.Point6;

        View view = findViewById(R.id.activity_slimer_pattern);

        touch= new TouchHandler(this, TOUCHID);

        view.setOnTouchListener(touch);

        touch.setPattern(pattern);


    }
}
