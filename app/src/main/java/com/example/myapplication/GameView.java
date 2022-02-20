package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder holder;
    private Thread thread;

    private Player player;
    private List<Feed> feedList = new ArrayList<>();
    private int maxFeed = 50;
    private int centerX, centerY;
    private float touchX, touchY;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        centerX = getWidth() / 2;
        centerY = getHeight() / 2;

        touchX = centerX;
        touchY = centerY;

        player = new Player(centerX, centerY);

        while (feedList.size() < maxFeed) {
            createFeed();
        }

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        thread = null;
    }

    @Override
    public void run() {
        while (true) {
            if (holder.getSurface().isValid()) {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    //clear canvas
                    canvas.drawColor(0, PorterDuff.Mode.CLEAR);

                    //draw player
                    Paint paint = new Paint();
                    paint.setColor(Color.WHITE);
                    canvas.drawCircle(centerX, centerY, player.getRadius(), paint);

                    //draw feed
                    for (int i = 0; i < feedList.size(); i++) {
                        Feed feed = feedList.get(i);
                        paint.setColor(feed.getColor());
                        canvas.drawCircle(feed.getX() + centerX - player.getX(), feed.getY() + centerY - player.getY(), feed.getRadius(), paint);
                    }

                    //draw score
                    paint.setColor(Color.WHITE);
                    paint.setTextSize(40);
                    canvas.drawText("SCORE : " + player.getScore(),50,50,paint);

                    holder.unlockCanvasAndPost(canvas);

                    player.move(touchX,touchY,centerX,centerY);
                    collision();

                    if(feedList.size() < maxFeed){
                        createFeed();
                    }
                }
            }
        }
    }

    public void createFeed() {
        //create feed around player
        feedList.add(new Feed((float)(player.getX() - getWidth() + Math.random() * getWidth() * 2),
                (float) (player.getY() - getHeight() + Math.random() * getHeight() * 2)));
    }

    public void collision(){
        Iterator<Feed> iterator = feedList.iterator();
        while(iterator.hasNext()){
            Feed feed = iterator.next();
            double dx = feed.getX() - player.getX();
            double dy = feed.getY() - player.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            if(distance <= player.getRadius() + feed.getRadius()){
                iterator.remove();
                player.updateScore();
            }else if(distance > Math.max(getWidth(),getHeight())){
                iterator.remove();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                touchX = event.getX();
                touchY = event.getY();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                touchX = centerX;
                touchY = centerY;
                break;
        }

        return true;
    }
}
