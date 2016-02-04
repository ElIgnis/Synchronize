package com.sidm.synchronize;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.util.Arrays;
import java.util.List;

import Game.HighscoreManager;
import Game.SoundManager;

public class Mainmenu extends FragmentActivity implements View.OnClickListener {

    //Menu buttons
	private Button btn_gameplay;
	private Button btn_options;
    private Button btn_ranking;
    private Button btn_help;

    SharedPreferences prefs;

    //Facebook portion
    private LoginButton btn_fbLogin;
    private Button btn_sharescore;
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    ShareDialog shareDialog;
    boolean loggedin = false;
    ProfilePictureView fb_prof_pic;
    TextView userName;

    int highscore = 0;

    List<String> PERMISSIONS = Arrays.asList("publish_actions");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        //Show layout on screen(page == view)
        setContentView(R.layout.activity_mainmenu);

/*        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.sidm.synchronize", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/

        prefs = getSharedPreferences("HighscoreData", Context.MODE_PRIVATE);

        highscore = prefs.getInt("CurrentScore", 0);

        if(!SoundManager.BGM.isPlaying())
        {
            SoundManager.BGM.reset();
            SoundManager.BGM = MediaPlayer.create(this, R.raw.menu_bgm);
            SoundManager.SFX = MediaPlayer.create(this, R.raw.click);
            SoundManager.BGM.start();
            SoundManager.BGM.setLooping(true);
        }

        SoundManager.BGM.setVolume(SoundManager.BGMVolume / 100, SoundManager.BGMVolume / 100);
        SoundManager.SFX.setVolume(SoundManager.SFXVolume / 100, SoundManager.SFXVolume / 100);

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

        btn_fbLogin = (LoginButton) findViewById(R.id.btn_fb_login);
        btn_fbLogin.setOnClickListener(this);

        btn_sharescore = (Button) findViewById(R.id.btn_sharescore);
        btn_sharescore.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();

        //Custom font
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Gemcut.otf");

        TextView txt_header = (TextView) findViewById(R.id.text_title2);
        txt_header.setTypeface(font);
        btn_gameplay.setTypeface(font);
        btn_options.setTypeface(font);
        btn_ranking.setTypeface(font);
        btn_help.setTypeface(font);

        DataLoader();

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {

            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                AccessToken at = AccessToken.getCurrentAccessToken();

                if (currentAccessToken == null){
                    //User logged out
                    btn_sharescore.setVisibility(View.INVISIBLE);
                    fb_prof_pic.setProfileId("");
                }
                else{
                    btn_sharescore.setVisibility(View.VISIBLE);
                    fb_prof_pic.setProfileId(Profile.getCurrentProfile().getId());
                }
            }
        };

        accessTokenTracker.startTracking();

        fb_prof_pic = (ProfilePictureView)findViewById(R.id.picture);
        btn_fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //fb_prof_pic.setProfileId(Profile.getCurrentProfile().getId());
            }

            @Override
            public void onCancel() {
                userName.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                userName.setText("Login attempt failed.");
            }
        });

        if(Profile.getCurrentProfile() != null){
            btn_sharescore.setVisibility(View.VISIBLE);
            fb_prof_pic.setProfileId(Profile.getCurrentProfile().getId());
            loggedin = true;
        }
        else{
            btn_sharescore.setVisibility(View.INVISIBLE);
            fb_prof_pic.setProfileId("");
            loggedin = false;
        }

/*        if(loggedin){
            btn_sharescore.setVisibility(View.VISIBLE);
        }
        else{
            btn_sharescore.setVisibility(View.INVISIBLE);
        }*/
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
        boolean PageChange = false;
        //Detect btn_start
        if(v == btn_gameplay) {
            //Set to go to next class
            intent.setClass(this, PlayState.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PageChange = true;
        }
        else if(v == btn_options){
            intent.setClass(this, Options.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PageChange = true;
        }

        else if (v == btn_ranking){
            intent.setClass(this, Ranking.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PageChange = true;
        }

        else if (v == btn_help){
            intent.setClass(this, Help.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PageChange = true;
        }
        else if(v == btn_fbLogin ){
            loginManager.logInWithPublishPermissions(this, PERMISSIONS);

/*            if (loggedin) {
                loggedin = false;
            }
            else {
                loggedin = true;
            }*/

        }
        else if (v == btn_sharescore) {
            btn_sharescore.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert_builder = new AlertDialog.Builder(Mainmenu.this);
                    alert_builder.setMessage("Do you want to share your score of " + String.valueOf(highscore))
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    shareScore();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = alert_builder.create();
                    alert.setTitle("Share score on facebook");
                    alert.show();
                }
            });
        }

        SoundManager.SFX.start();
        if(PageChange){
            startActivity(intent);
        }
    }

/*    private void updateLogin(){
        //Check if logged in
        if(Profile.getCurrentProfile() != null)
        {

*//*            if(Profile.getCurrentProfile().getId() == null) {

            }
            else{

            }*//*
        }
        else{

        }
    }*/

    private void shareScore(){
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption("Synchronization complete. Your final score is " + highscore)
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
