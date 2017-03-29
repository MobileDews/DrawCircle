package com.techdew.drawcircle;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.techdew.lib.CircleTouchView;
import com.techdew.lib.OnCircleTouchListener;

public class MainActivity extends AppCompatActivity  {
    CircleTouchView circleTouchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circleTouchView = (CircleTouchView) findViewById(R.id.circle_drawer_view);
        circleTouchView.setEventListener(new OnCircleTouchListener() {
            @Override
            public void circleValue(float radius) {
                Log.d("circleValue","===>"+radius);
            }
        });

    }

}
