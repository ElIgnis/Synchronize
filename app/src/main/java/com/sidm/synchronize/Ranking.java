package com.sidm.synchronize;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import Game.HighscoreManager;
import Game.SoundManager;

public class Ranking extends Activity implements View.OnClickListener{
    Button btn_back;
    SharedPreferences prefs;
    private static TextView First_Text, Second_Text, Third_Text, Fourth_Text, Fifth_Text;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        //Show layout on screen(page == view)
        setContentView(R.layout.activity_ranking);

        /*Define buttons*/
        btn_back = (Button)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        //Sets the highscore based on data loaded
        First_Text = (TextView)findViewById(R.id.First);
        Second_Text = (TextView)findViewById(R.id.Second);
        Third_Text = (TextView)findViewById(R.id.Third);
        Fourth_Text = (TextView)findViewById(R.id.Fourth);
        Fifth_Text = (TextView)findViewById(R.id.Fifth);

        First_Text.setText(String.valueOf(HighscoreManager.HighScore_List[0]));
        Second_Text.setText(String.valueOf(HighscoreManager.HighScore_List[1]));
        Third_Text.setText(String.valueOf(HighscoreManager.HighScore_List[2]));
        Fourth_Text.setText(String.valueOf(HighscoreManager.HighScore_List[3]));
        Fifth_Text.setText(String.valueOf(HighscoreManager.HighScore_List[4]));
    }



    public void onClick(View v){
        Intent intent = new Intent();

        //Detect btn_start
        if(v == btn_back) {
            //Set to go to next class
            intent.setClass(this, Mainmenu.class);
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
