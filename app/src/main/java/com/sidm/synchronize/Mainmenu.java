package com.sidm.synchronize;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import Game.SoundManager;

public class Mainmenu extends Activity implements View.OnClickListener {

	private Button btn_gameplay;
	private Button btn_options;
    private Button btn_ranking;
    private Button btn_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        //Show layout on screen(page == view)
        setContentView(R.layout.activity_mainmenu);

        if(!SoundManager.BGM.isPlaying())
        {
            SoundManager.BGM.reset();
            SoundManager.BGM = MediaPlayer.create(this, R.raw.bgm);
            SoundManager.BGM.setVolume(SoundManager.BGMVolume, SoundManager.BGMVolume);
            SoundManager.BGM.start();
            SoundManager.BGM.setLooping(true);
        }

        /*Define buttons*/
        //Start button
        btn_gameplay = (Button)findViewById(R.id.btn_start);
        btn_gameplay.setOnClickListener(this);

        btn_options = (Button)findViewById(R.id.btn_options);
        btn_options.setOnClickListener(this);

        btn_ranking = (Button)findViewById((R.id.btn_ranking));
        btn_ranking.setOnClickListener(this);

        btn_help = (Button)findViewById((R.id.btn_help));
        btn_help.setOnClickListener(this);
    }

    public void onClick(View v){
        Intent intent = new Intent();

        //Detect btn_start
        if(v == btn_gameplay) {
            //Set to go to next class
            intent.setClass(this, PlayState.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        else if(v == btn_options){
            intent.setClass(this, Options.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        else if (v == btn_ranking){
            intent.setClass(this, Ranking.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        else if (v == btn_help){
            intent.setClass(this, Help.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        SoundManager.SFX.start();
        startActivity(intent);
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
