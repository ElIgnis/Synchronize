package com.sidm.synchronize;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

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
    private static int m_iGameType = 0;

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
    Button btn_mode_accel;
    Button btn_mode_back;

    //Game type selection
    Button btn_type_classic;
    Button btn_type_splashy;
    Button btn_type_back;

    //Custom font
    Typeface font;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar
        font = Typeface.createFromAsset(getAssets(), "fonts/Gemcut.otf");
        SelectMode();
        vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void onClick(View v){
        //Back button
        if(v == btn_back || v == btn_mode_back) {
            //Set to go to next class
            intent.setClass(this, Mainmenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            SoundManager.BGM.stop();
            startActivity(intent);
        }

        //Game mode selection
        if (v == btn_mode_normal) {
            m_iGameMode = 0;
            SelectGameType();
            //SelectDifficulty();
        }
        if (v == btn_mode_accel) {
            m_iGameMode = 1;
            SelectGameType();
            //SelectDifficulty();
        }

        //Game type selection
        if (v == btn_type_classic){
            m_iGameType = 0;
            SelectDifficulty();
        }
        if (v == btn_type_splashy){
            m_iGameType = 1;
            SelectDifficulty();
        }
        if (v == btn_type_back){
            SelectMode();
        }

        //Difficulty selection
        if (v == btn_diff_easy) {
            m_iDifficulty = 3;

            //Classic mode proceed to color selection
            if (m_iGameType == 0) {
                SelectColor();
            }
            //Splashy mode starts game instantly
            if (m_iGameType == 1){
                InitGame();
            }
        }
        if (v == btn_diff_normal) {
            m_iDifficulty = 4;

            if (m_iGameType == 0) {
                SelectColor();
            }
            //Splashy mode starts game instantly
            if (m_iGameType == 1){
                InitGame();
            }
        }
        if (v == btn_diff_hard) {
            m_iDifficulty = 5;

            if (m_iGameType == 0) {
                SelectColor();
            }
            //Splashy mode starts game instantly
            if (m_iGameType == 1){
                InitGame();
            }
        }
        if(v == btn_diff_back){
            SelectGameType();
        }

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

        //Custom font
        TextView txt_header = (TextView) findViewById(R.id.text_difficulty);

        txt_header.setTypeface(font);
        btn_diff_easy.setTypeface(font);
        btn_diff_normal.setTypeface(font);
        btn_diff_hard.setTypeface(font);
        btn_diff_back.setTypeface(font);
    }

    void SelectMode(){
        setContentView(R.layout.activity_game_mode);

        btn_mode_normal = (Button)findViewById(R.id.btn_mode_normal);
        btn_mode_normal.setOnClickListener(this);

        btn_mode_accel = (Button)findViewById(R.id.btn_mode_accel);
        btn_mode_accel.setOnClickListener(this);

        btn_mode_back = (Button)findViewById(R.id.btn_mode_back);
        btn_mode_back.setOnClickListener(this);

        //Custom font
        TextView txt_header = (TextView) findViewById(R.id.text_mode);
        TextView txt_ctrl1 = (TextView) findViewById(R.id.text_mode_normal);
        TextView txt_ctrl2 = (TextView) findViewById(R.id.text_mode_accel);
        TextView txt_ctrl3 = (TextView) findViewById(R.id.text_mode_accel2);

        txt_header.setTypeface(font);
        txt_ctrl1.setTypeface(font);
        txt_ctrl2.setTypeface(font);
        txt_ctrl3.setTypeface(font);
        btn_mode_normal.setTypeface(font);
        btn_mode_accel.setTypeface(font);
        btn_mode_back.setTypeface(font);
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

        //Custom font
        TextView txt_header = (TextView) findViewById(R.id.text_color);

        txt_header.setTypeface(font);
        btn_color_red.setTypeface(font);
        btn_color_green.setTypeface(font);
        btn_color_blue.setTypeface(font);
        btn_color_yellow.setTypeface(font);
        btn_color_purple.setTypeface(font);
        btn_color_back.setTypeface(font);
    }

    void SelectGameType(){
        setContentView(R.layout.activity_game_type);

        btn_type_classic = (Button)findViewById(R.id.btn_type_classic);
        btn_type_classic.setOnClickListener(this);

        btn_type_splashy = (Button)findViewById(R.id.btn_type_splashy);
        btn_type_splashy.setOnClickListener(this);

        btn_type_back = (Button)findViewById(R.id.btn_type_back);
        btn_type_back.setOnClickListener(this);

        //Custom font
        TextView txt_header = (TextView) findViewById(R.id.text_type);
        TextView txt_ctrl1 = (TextView) findViewById(R.id.text_type_classic);
        TextView txt_ctrl2 = (TextView) findViewById(R.id.text_type_splashy);

        txt_header.setTypeface(font);
        txt_ctrl1.setTypeface(font);
        txt_ctrl2.setTypeface(font);

        btn_type_classic.setTypeface(font);
        btn_type_splashy.setTypeface(font);
        btn_type_back.setTypeface(font);
    }

    void InitGame(){
        //Show layout on screen(page == view)
        playScene = new PlayScene(this);
        playScene.setDifficulty(m_iDifficulty);
        playScene.setGameMode(m_iGameMode);
        playScene.setGameType(m_iGameType);

        if (m_iGameType == 0) {
            playScene.setColor(m_iColor);
        }
        //Generate random number for Splashy mode
        if (m_iGameType == 1){
            Random rng = new Random();
            int rngNumber = rng.nextInt(5) + 1;
            playScene.setColor(rngNumber);
        }

        setContentView(playScene);
    }

    protected void onPause(){super.onPause();}

    protected void onStop(){
        super.onStop();
    }

    protected void onDestroy(){
        super.onDestroy();
    }
}

