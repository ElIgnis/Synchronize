package com.sidm.synchronize;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import Game.HighscoreManager;
import Game.SoundManager;

public class Mainmenu extends Activity implements View.OnClickListener {

	private Button btn_gameplay;
	private Button btn_options;
    private Button btn_ranking;
    private Button btn_help;
    SharedPreferences prefs;

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
            SoundManager.SFX = MediaPlayer.create(this, R.raw.click);
            SoundManager.SFX.setVolume(SoundManager.SFXVolume, SoundManager.SFXVolume);
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

        //Custom font
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Gemcut.otf");

        TextView txt_header = (TextView) findViewById(R.id.text_title2);
        txt_header.setTypeface(font);
        btn_gameplay.setTypeface(font);
        btn_options.setTypeface(font);
        btn_ranking.setTypeface(font);
        btn_help.setTypeface(font);

        DataLoader();
    }

    public void DataLoader(){
        //Load Highscores Data
        prefs = getSharedPreferences("HighscoreData", Context.MODE_PRIVATE);

        HighscoreManager.HighScore_List = new int[5];

        HighscoreManager.HighScore_List[0] = prefs.getInt("FirstPlace", 0);
        HighscoreManager.HighScore_List[1] = prefs.getInt("SecondPlace", 0);
        HighscoreManager.HighScore_List[2] = prefs.getInt("ThirdPlace", 0);
        HighscoreManager.HighScore_List[3] = prefs.getInt("FourthPlace", 0);
        HighscoreManager.HighScore_List[4] = prefs.getInt("FifthPlace", 0);
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
