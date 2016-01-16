package com.sidm.synchronize;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import Game.SoundManager;

public class Options extends Activity implements View.OnClickListener{

    Button btn_back, btn_save;
    private static SeekBar SFXSlider, BGMSlider;
    private static TextView SFX_Text, BGM_Text;
    SharedPreferences prefs;

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

        btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

        prefs = getSharedPreferences("OptionsData", Context.MODE_PRIVATE);
        SoundManager.SFXVolume = prefs.getFloat("SFXValue", 100);
        SoundManager.BGMVolume = prefs.getFloat("BGMValue", 100);

        //Custom font
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Gemcut.otf");

        TextView txt_header = (TextView) findViewById(R.id.text_options);
        TextView txt_sfx = (TextView) findViewById(R.id.text_sfx);
        TextView txt_bgm = (TextView) findViewById(R.id.text_bgm);
        TextView txt_sfx_value = (TextView) findViewById(R.id.SFX_Value);
        TextView txt_bgm_value = (TextView) findViewById(R.id.BGM_Value);
        txt_header.setTypeface(font);
        txt_sfx.setTypeface(font);
        txt_bgm.setTypeface(font);
        txt_sfx_value.setTypeface(font);
        txt_bgm_value.setTypeface(font);

        btn_back.setTypeface(font);
        btn_save.setTypeface(font);

        SeekBarUpdate();
    }

    void SeekBarUpdate(){
        //Slider bar
        SFXSlider = (SeekBar)findViewById(R.id.SFX_SeekBar);
        BGMSlider = (SeekBar)findViewById(R.id.BGM_SeekBar);
        SFX_Text = (TextView)findViewById(R.id.SFX_Value);
        BGM_Text = (TextView)findViewById(R.id.BGM_Value);
        SFX_Text.setText(String.valueOf((int)SoundManager.SFXVolume));
        BGM_Text.setText(String.valueOf((int)SoundManager.BGMVolume));

        SFXSlider.setProgress((int)SoundManager.SFXVolume);
        BGMSlider.setProgress((int)SoundManager.BGMVolume);

        //Slider for SFX
        SFXSlider.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        SoundManager.SFXVolume = progress;
                        SFX_Text.setText(String.valueOf((int)SoundManager.SFXVolume));
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
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        SoundManager.BGMVolume = progress;
                        BGM_Text.setText(String.valueOf((int) SoundManager.BGMVolume));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });


    }

    public void onClick(View v){
        Intent intent = new Intent();

        //Save modified changes
        if (v == btn_save){
            SharedPreferences.Editor prefEditor = prefs.edit();
            prefEditor.putFloat("BGMValue", SoundManager.BGMVolume);
            prefEditor.putFloat("SFXValue", SoundManager.SFXVolume);
            prefEditor.commit();

            SoundManager.BGM.setVolume(SoundManager.BGMVolume / 100, SoundManager.BGMVolume / 100);
            SoundManager.SFX.setVolume(SoundManager.SFXVolume / 100, SoundManager.SFXVolume / 100);
        }

        intent.setClass(this, Mainmenu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

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
