package com.sidm.synchronize;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import Game.SoundManager;

/*This class displays the game scene*/
public class PlayState extends Activity implements View.OnClickListener{

    View v;
    Intent intent = new Intent();
    PlayScene playScene;
    public static Vibrator vib;

    //Initialise all variables for the game
    private static int m_iDifficulty = 0;
    private static int m_iGameMode = 0;
    private static int m_iColor = 0;

    //Back button
    Button btn_back;

    //Difficulty selection buttons
    Button btn_diff_easy;
    Button btn_diff_normal;
    Button btn_diff_hard;
    Button btn_diff_back;

    //Player color selection buttons
    Button btn_color_red;
    Button btn_color_green;
    Button btn_color_blue;
    Button btn_color_yellow;
    Button btn_color_purple;
    Button btn_color_back;

    //Game mode selection buttons
    Button btn_mode_normal;
    Button btn_mode_random;
    Button btn_mode_cycle;
    Button btn_mode_cyclerandom;
    Button btn_mode_back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar
        SelectDifficulty();
        vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void onClick(View v){
        //Back button
        if(v == btn_back || v == btn_diff_back) {
            //Set to go to next class
            intent.setClass(this, Mainmenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            SoundManager.BGM.stop();
            startActivity(intent);
        }

        //Difficulty selection
        if (v == btn_diff_easy) {
            m_iDifficulty = 3;
            SelectColor();
        }
        if (v == btn_diff_normal) {
            m_iDifficulty = 4;
            SelectColor();
        }
        if (v == btn_diff_hard) {
            m_iDifficulty = 5;
            SelectColor();
        }

        //Game mode selection
/*        if (v == btn_mode_normal) {
            m_iGameMode = 0;
            SelectColor();
        }
        if (v == btn_mode_random) {
            m_iGameMode = 1;
            SelectColor();
        }
        if (v == btn_mode_cycle) {
            m_iGameMode = 2;
            SelectColor();
        }
        if (v == btn_mode_cyclerandom) {
            m_iGameMode = 3;
            SelectColor();
        }
        if (v == btn_mode_back)
        {
            SelectDifficulty();
        }*/

        //Player color selection
        if (v == btn_color_red) {
            m_iColor = 1;
            InitGame();
        }
        if (v == btn_color_green) {
            m_iColor = 2;
            InitGame();
        }
        if (v == btn_color_blue) {
            m_iColor = 3;
            InitGame();
        }
        if (v == btn_color_yellow) {
            m_iColor = 4;
            InitGame();
        }
        if (v == btn_color_purple) {
            m_iColor = 5;
            InitGame();
        }
        if (v == btn_color_back){
            SelectDifficulty();
        }
        SoundManager.SFX.start();
    }

    void SelectDifficulty(){
            setContentView(R.layout.activity_game_difficulty);

            btn_diff_easy = (Button)findViewById(R.id.btn_diff_easy);
            btn_diff_easy.setOnClickListener(this);

            btn_diff_normal = (Button)findViewById(R.id.btn_diff_normal);
            btn_diff_normal.setOnClickListener(this);

            btn_diff_hard = (Button)findViewById(R.id.btn_diff_hard);
            btn_diff_hard.setOnClickListener(this);

            btn_diff_back = (Button)findViewById(R.id.btn_diff_back);
            btn_diff_back.setOnClickListener(this);
    }

    void SelectMode(){
        setContentView(R.layout.activity_game_mode);

        btn_mode_normal = (Button)findViewById(R.id.btn_mode_normal);
        btn_mode_normal.setOnClickListener(this);

        btn_mode_random = (Button)findViewById(R.id.btn_mode_random);
        btn_mode_random.setOnClickListener(this);

        btn_mode_cycle = (Button)findViewById(R.id.btn_mode_cycle);
        btn_mode_cycle.setOnClickListener(this);

        btn_mode_cyclerandom = (Button)findViewById(R.id.btn_mode_cyclerandom);
        btn_mode_cyclerandom.setOnClickListener(this);

        btn_mode_back = (Button)findViewById(R.id.btn_mode_back);
        btn_mode_back.setOnClickListener(this);
    }

    void SelectColor(){
        setContentView(R.layout.activity_game_color);

        btn_color_red = (Button)findViewById(R.id.btn_color_red);
        btn_color_red.setOnClickListener(this);

        btn_color_green = (Button)findViewById(R.id.btn_color_green);
        btn_color_green.setOnClickListener(this);

        btn_color_blue = (Button)findViewById(R.id.btn_color_blue);
        btn_color_blue.setOnClickListener(this);

        btn_color_yellow = (Button)findViewById(R.id.btn_color_yellow);
        btn_color_yellow.setOnClickListener(this);

        btn_color_purple = (Button)findViewById(R.id.btn_color_purple);
        btn_color_purple.setOnClickListener(this);

        btn_color_back = (Button)findViewById(R.id.btn_color_back);
        btn_color_back.setOnClickListener(this);
    }

    void InitGame(){
        //Show layout on screen(page == view)
        //v = getLayoutInflater().inflate(R.layout.activity_gameplay, null);
        playScene = new PlayScene(this);
        playScene.setDifficulty(m_iDifficulty);
        playScene.setGameMode(m_iGameMode);
        playScene.setColor(m_iColor);
        setContentView(playScene);

        //addContentView(v, new WindowManager.LayoutParams(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN));
        //btn_back = (Button) findViewById(R.id.btn_back);
        //btn_back.setOnClickListener(this);
    }

    protected void onPause(){super.onPause();}

    protected void onStop(){
        super.onStop();
    }

    protected void onDestroy(){
        super.onDestroy();
    }
}

