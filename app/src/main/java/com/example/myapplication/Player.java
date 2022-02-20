package com.example.myapplication;

import android.graphics.Color;

public class Player extends Circle {

    private int score = 1;
    private static int initialRadius = 20;
    private final int maxSpeed = 10;

    Player(float x, float y) {
        super(x, y, initialRadius, Color.WHITE);
    }

    void move(float tx,float ty,float cx,float cy){
        float dx = tx - cx;
        float dy = ty - cy;
        double distance = Math.sqrt(dx * dx + dy * dy);
        double radian = Math.atan2(dy,dx);
        if(distance > maxSpeed){
            distance = maxSpeed;
        }

        x += distance * Math.cos(radian);
        y += distance * Math.sin(radian);
    }

    void updateScore(){
        score++;
        radius = (float)Math.sqrt(score) * initialRadius;
    }

    public int getScore() {
        return score;
    }
}
