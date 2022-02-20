package com.example.myapplication;

import android.graphics.Color;

import java.util.Random;

public class Feed extends Circle {

    private static Random rand = new Random();

    Feed(float x,float y){
        super(x,y,10.0f,
                Color.argb(255,rand.nextInt(256),rand.nextInt(256),rand.nextInt(256)));
    }
}
