package com.GhostlyRunes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SemiCirclePatternActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semi_circle_pattern);

        int[] pattern = new int[5];
        pattern[0]=R.id.Point1;
        pattern[1]=R.id.Point2;
        pattern[2]=R.id.Point3;
        pattern[3]=R.id.Point4;
        pattern[4]=R.id.Point5;
        pattern[5]=R.id.Point6;

        View view = findViewById(R.id.activity_star_pattern);

        touch= new TouchHandler(this, TOUCHID);

        view.setOnTouchListener(touch);

        touch.setPattern(pattern);
        touch.startChecking();

    }
}
