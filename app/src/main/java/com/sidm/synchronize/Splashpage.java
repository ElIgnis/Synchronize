package com.sidm.synchronize;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import Game.SoundManager;

public class Splashpage extends Activity {

    protected boolean _active = true;
    protected int _splashTime = 5000; // ms

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        //Show layout on screen(page == view)
        setContentView(R.layout.activity_splashpage);

        SoundManager.BGM = MediaPlayer.create(this, R.raw.bgm);
        SoundManager.SFX = MediaPlayer.create(this, R.raw.click);

        //thread for displaying the Splash Screen
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                SoundManager.BGM.start();
                SoundManager.BGM.setLooping(true);

                //if user taps screen
                RelativeLayout rLayout =  (RelativeLayout) findViewById(R.id.splashpage);
                rLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Add codes
                        SoundManager.SFX.start();
                        Intent intent = new Intent(Splashpage.this, Mainmenu.class);
                        startActivity(intent);
                    }
                });
            }
        };

        splashTread.start();
    }

    public boolean OnTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            _active = false;
        }
        return true;
    }
    protected void onPause(){
        super.onPause();
    }

    protected void onStop(){
        super.onStop();
    }

    protected void onDestroy(){
        super.onDestroy();
    }
}