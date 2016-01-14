package com.sidm.synchronize;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Help extends Activity implements View.OnClickListener{

    Button btn_help_back;

    MediaPlayer click;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        //Show layout on screen(page == view)
        setContentView(R.layout.activity_help);

        click = MediaPlayer.create(this, R.raw.click);

        /*Define buttons*/
        btn_help_back = (Button)findViewById(R.id.btn_help_back);
        btn_help_back.setOnClickListener(this);
    }

    public void onClick(View v){
        Intent intent = new Intent();

        //Detect btn_start
        if(v == btn_help_back) {
            //Set to go to next class
            intent.setClass(this, Mainmenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        click.start();
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
