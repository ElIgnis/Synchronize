package com.sidm.synchronize;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import Game.SoundManager;

public class Options extends Activity implements View.OnClickListener{

    Button btn_back;
/*    private static SeekBar SFXSlider, BGMSlider;
    private static TextView SFX_Text, BGM_Text;
    private int SFXValue, BGMValue;
    public static final String PREFS_NAME = "My Prefs";*/

    SoundManager SM;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        //Show layout on screen(page == view)
        setContentView(R.layout.activity_options);

        /*Define buttons*/
        btn_back = (Button)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

/*        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SFXValue = settings.getInt("SFXValue", 0);
        BGMValue = settings.getInt("BGMValue", 0);
        SeekBarUpdate();*/
    }

    /*void SeekBarUpdate(){

        //Slider bar
        SFXSlider = (SeekBar)findViewById(R.id.SFX_SeekBar);
        BGMSlider = (SeekBar)findViewById(R.id.BGM_SeekBar);
        SFX_Text = (TextView)findViewById(R.id.SFX_Value);
        BGM_Text = (TextView)findViewById(R.id.BGM_Value);
        SFX_Text.setText(String.valueOf(SFXSlider.getProgress()));
        BGM_Text.setText(String.valueOf(BGMSlider.getProgress()));

        //Slider for SFX
        SFXSlider.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        SFXValue = progress;
                        SFX_Text.setText(String.valueOf(SFXValue));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

        //Slider for BGM
        BGMSlider.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener(){

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        BGMValue = progress;
                        BGM_Text.setText(String.valueOf(BGMValue));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });


    }*/

    public void onClick(View v){
        Intent intent = new Intent();

       if (v == btn_back){
            //Set to go to next class
            intent.setClass(this, Mainmenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

/*           SoundManager.BGM.setVolume(BGMValue, BGMValue);
           SoundManager.SFX.setVolume(SFXValue, SFXValue);*/
        }

        SM.SFX.start();
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
