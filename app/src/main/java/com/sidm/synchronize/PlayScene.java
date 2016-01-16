package com.sidm.synchronize;

//Import resources
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;

//Import other classes
import java.util.ArrayList;
import java.util.Random;

import Game.Background;
import Game.Entity;
import Game.HighscoreManager;
import Game.Player;
import Game.SoundManager;
import Game.Tile;

public class PlayScene extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener{

    //Pre Game Setups
    public int m_iDifficulty;
    public int m_iGameMode;
    public int m_iPlayerColor;

    public void setDifficulty(int difficulty){
        this.m_iDifficulty = difficulty;
    }

    public void setGameMode(int gameMode){
        this.m_iGameMode = gameMode;
    }

    public void setColor(int color) {this.m_iPlayerColor = color;}

    private boolean InitVars;

    //FPS
    double FPS;
    View v;
    Intent intent = new Intent();

    //Variables for the play scene
    private GameThread gameThread;
    private Background playScene_BG;
    Bitmap bm_gamebg = BitmapFactory.decodeResource(getResources(), R.drawable.game_bg);
    private boolean isPaused, newHighScore, checkHighScore;

    //Rect for buttons
    Rect Rect_MoveLeft, Rect_MoveRight;
    Rect Rect_Laser;
    Rect Rect_Pause, Rect_Play;
    Rect Rect_Back;

    Bitmap bm_MoveLeft = BitmapFactory.decodeResource(getResources(), R.drawable.leftbutton);
    Bitmap bm_MoveRight = BitmapFactory.decodeResource(getResources(), R.drawable.rightbutton);
    Bitmap bm_Synchro_icon =  BitmapFactory.decodeResource(getResources(), R.drawable.btn_synchro_ready);
    Bitmap bm_Synchro_icon_unready = BitmapFactory.decodeResource(getResources(), R.drawable.btn_synchro_unready);
    Bitmap bm_Pause = BitmapFactory.decodeResource(getResources(), R.drawable.pausebutton);
    Bitmap bm_Play = BitmapFactory.decodeResource(getResources(), R.drawable.playbutton);
    Bitmap bm_Back = BitmapFactory.decodeResource(getResources(), R.drawable.backbutton);

    //width and height of screen
    public int screenWidth, screenHeight;

    //Initialise player
    private Player player;

    Bitmap Spr_Player_Idle;
    Bitmap Spr_Player_MoveLeft;
    Bitmap Spr_Player_MoveRight;
    Bitmap Spr_Player_Fall;

    //Array of tiles for game usage
    private ArrayList<Tile> tile_list;
    Bitmap Spr_RedTile = BitmapFactory.decodeResource(getResources(), R.drawable.tile_red);
    Bitmap Spr_BlueTile = BitmapFactory.decodeResource(getResources(), R.drawable.tile_blue);
    Bitmap Spr_GreenTile = BitmapFactory.decodeResource(getResources(), R.drawable.tile_green);
    Bitmap Spr_YellowTile = BitmapFactory.decodeResource(getResources(), R.drawable.tile_yellow);
    Bitmap Spr_PurpleTile = BitmapFactory.decodeResource(getResources(), R.drawable.tile_purple);
    private int playSpeed = 5;
    private int numFrames = 6;

    //Accelerometer
    private final SensorManager sensor;
    private float[] values = {0, 0, 0};
    private long lastTime = System.currentTimeMillis();

    //Shared preferences
    SharedPreferences prefs;

    //Custom font
    Typeface font;

    //Default constructor to take in context from PlayState
    public PlayScene(Context context) {
        super(context);

        //Add callback for surfaceholder for events
        getHolder().addCallback(this);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        //Init a new thread
        gameThread = new GameThread(getHolder(), this);

        //Allow Scene to handle events
        setFocusable(true);

        //Initialise array lists
        tile_list = new ArrayList<Tile>();
        InitVars = false;
        isPaused = false;
        checkHighScore = true;
        newHighScore = false;

        SoundManager.BGM.stop();
        SoundManager.BGM.reset();
        SoundManager.BGM = MediaPlayer.create(context, R.raw.background_music);
        SoundManager.BGM.setVolume(SoundManager.BGMVolume, SoundManager.BGMVolume);
        SoundManager.BGM.start();
        SoundManager.BGM.setLooping(true);
        SoundManager.SFX = MediaPlayer.create(context, R.raw.click);

        sensor = (SensorManager)getContext().getSystemService(Context.SENSOR_SERVICE);
        sensor.registerListener(this, sensor.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0), SensorManager.SENSOR_DELAY_NORMAL);

        //for debug to clear hs
/*        prefs = getContext().getSharedPreferences("HighscoreData", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt("FirstPlace", 0);
        prefEditor.putInt("SecondPlace", 0);
        prefEditor.putInt("ThirdPlace", 0);
        prefEditor.putInt("FourthPlace", 0);
        prefEditor.putInt("FifthPlace", 0);
        prefEditor.commit();*/
    }

    //Gameover
    Bitmap bm_GameOver = BitmapFactory.decodeResource(getResources(), R.drawable.gameover_overlay);
    Rect Rect_Menu;

    public void startVibrate(){
        long pattern[] = {0, 50, 0};
        PlayState.vib.vibrate(pattern, -1);
        //SoundManager.SFX.start();
    }

    public void stopVibrate(){
        PlayState.vib.cancel();
    }

    //Overriding surface methods
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height){
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder){
        boolean m_bRetry = true;

        //Destroy thread
        if(gameThread.isAlive()){
            gameThread.runThread(false);
            stopVibrate();
            Exit();
        }

        //Stops thread
        while(m_bRetry){
            try{
                gameThread.join();
                m_bRetry = false;
            }
            //Catch any interruption occurred to thread and retry
            catch(InterruptedException e){e.printStackTrace();}
        }
    }

    @Override
    //Initialise all variables here
    public void surfaceCreated(SurfaceHolder surfaceHolder){
        if(!InitVars){
            //Initialise font
            font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Gemcut.otf");

            //Initialise background
            playScene_BG = new Background(bm_gamebg, screenWidth, screenHeight);
            playScene_BG.setScrollAmount(-5);

            //Initialise player
            player = new Player(550, 350, 150, 150);
            loadColouredImages(m_iPlayerColor);
            LoadPlayerSprites();
            player.setBounds(0, screenWidth, 0, screenHeight, false);

            GenerateTile(4, m_iDifficulty, screenHeight);

            //Create buttons
            int buttonTop = screenHeight - screenHeight / 5;
            Rect_MoveLeft = new Rect(0, buttonTop, bm_MoveLeft.getWidth(), buttonTop + bm_MoveLeft.getHeight());
            Rect_MoveRight = new Rect(screenWidth - bm_MoveRight.getWidth(), buttonTop,screenWidth, buttonTop + bm_MoveRight.getHeight());
            Rect_Laser = new Rect(screenWidth / 2 - bm_Synchro_icon.getWidth() / 2, buttonTop, screenWidth / 2 - bm_Synchro_icon.getWidth() / 2 + bm_Synchro_icon.getWidth(), buttonTop + bm_Synchro_icon.getHeight());
            Rect_Pause = new Rect(screenWidth - 195, screenHeight / 2, screenWidth - 200 + bm_Pause.getWidth(), screenHeight / 2 + bm_Pause.getHeight());
            Rect_Play = new Rect(screenWidth - 195, screenHeight / 2, screenWidth - 200 + bm_Play.getWidth(), screenHeight / 2 +  bm_Play.getHeight());
            Rect_Back = new Rect(screenWidth - 200, screenHeight / 3, screenWidth - 200 + bm_Back.getWidth(), screenHeight / 3  +  bm_Back.getHeight());
            Rect_Menu = new Rect(screenWidth / 3 - bm_Back.getWidth(), screenHeight /2 + 100, screenWidth / 3, screenHeight / 2 + 100 + bm_Back.getHeight());

            //Set to true to allow init only when created
            InitVars = true;
        }

        //Begin thread
        if(!gameThread.isAlive()){
            gameThread = new GameThread(getHolder(), this);
            gameThread.runThread(true);
            gameThread.start();
        }
    }

    public void Exit(){

        //Stops thread and changes to menu
        gameThread.pauseThread(true);
        intent.setClass(getContext(), Mainmenu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        SoundManager.BGM.stop();
        getContext().startActivity(intent);
        SoundManager.SFX.start();

        //Clean up loaded bitmaps
        bm_Back.recycle();
        bm_MoveRight.recycle();
        bm_MoveLeft.recycle();
        bm_Back.recycle();
        bm_Synchro_icon.recycle();
        bm_Synchro_icon_unready.recycle();
        bm_Play.recycle();
        bm_Pause.recycle();
        bm_gamebg.recycle();
        bm_GameOver.recycle();
        Spr_RedTile.recycle();
        Spr_BlueTile.recycle();
        Spr_GreenTile.recycle();
        Spr_YellowTile.recycle();
        Spr_PurpleTile.recycle();
        Spr_Player_Idle.recycle();
        Spr_Player_MoveLeft.recycle();
        Spr_Player_MoveRight.recycle();
        Spr_Player_Fall.recycle();

        bm_Back = null;
        bm_MoveRight = null;
        bm_MoveLeft = null;
        bm_Back = null;
        bm_Synchro_icon = null;
        bm_Synchro_icon_unready = null;
        bm_Play = null;
        bm_Pause = null;
        bm_gamebg = null;
        bm_GameOver = null;
        Spr_RedTile = null;
        Spr_BlueTile = null;
        Spr_GreenTile = null;
        Spr_YellowTile = null;
        Spr_PurpleTile = null;
        Spr_Player_Idle = null;
        Spr_Player_MoveLeft = null;
        Spr_Player_MoveRight = null;
        Spr_Player_Fall = null;

        playScene_BG.Exit();
    }

    public void loadColouredImages(int color){
        switch(color){
            case 1:
                Spr_Player_Idle = BitmapFactory.decodeResource(getResources(), R.drawable.player_idle_red);
                Spr_Player_MoveLeft = BitmapFactory.decodeResource(getResources(), R.drawable.player_moveleft_red);
                Spr_Player_MoveRight = BitmapFactory.decodeResource(getResources(), R.drawable.player_moveright_red);
                Spr_Player_Fall = BitmapFactory.decodeResource(getResources(), R.drawable.player_fall_red);
                break;
            case 2:
                Spr_Player_Idle = BitmapFactory.decodeResource(getResources(), R.drawable.player_idle_green);
                Spr_Player_MoveLeft = BitmapFactory.decodeResource(getResources(), R.drawable.player_moveleft_green);
                Spr_Player_MoveRight = BitmapFactory.decodeResource(getResources(), R.drawable.player_moveright_green);
                Spr_Player_Fall = BitmapFactory.decodeResource(getResources(), R.drawable.player_fall_green);
                break;
            case 3:
                Spr_Player_Idle = BitmapFactory.decodeResource(getResources(), R.drawable.player_idle_blue);
                Spr_Player_MoveLeft = BitmapFactory.decodeResource(getResources(), R.drawable.player_moveleft_blue);
                Spr_Player_MoveRight = BitmapFactory.decodeResource(getResources(), R.drawable.player_moveright_blue);
                Spr_Player_Fall = BitmapFactory.decodeResource(getResources(), R.drawable.player_fall_blue);
                break;
            case 4:
                Spr_Player_Idle = BitmapFactory.decodeResource(getResources(), R.drawable.player_idle_yellow);
                Spr_Player_MoveLeft = BitmapFactory.decodeResource(getResources(), R.drawable.player_moveleft_yellow);
                Spr_Player_MoveRight = BitmapFactory.decodeResource(getResources(), R.drawable.player_moveright_yellow);
                Spr_Player_Fall = BitmapFactory.decodeResource(getResources(), R.drawable.player_fall_yellow);
                break;
            case 5:
                Spr_Player_Idle = BitmapFactory.decodeResource(getResources(), R.drawable.player_idle_purple);
                Spr_Player_MoveLeft = BitmapFactory.decodeResource(getResources(), R.drawable.player_moveleft_purple);
                Spr_Player_MoveRight = BitmapFactory.decodeResource(getResources(), R.drawable.player_moveright_purple);
                Spr_Player_Fall = BitmapFactory.decodeResource(getResources(), R.drawable.player_fall_purple);
                break;
            default:
                break;
        }
    }

    public void LoadPlayerSprites(){
        player.InitSprite_Idle(Spr_Player_Idle, //Bitmap image
                9, this.m_iPlayerColor,       //Num frames, Start color
                1, true);   //Play speed, Repeat

        player.InitSprite_MoveLeft(Spr_Player_MoveLeft, //Bitmap image
                9, this.m_iPlayerColor,       //Num frames, Start color
                1, true);   //Play speed, Repeat

        player.InitSprite_MoveRight(Spr_Player_MoveRight, //Bitmap image
                9, this.m_iPlayerColor,       //Num frames, Start color
                1, true);   //Play speed, Repeat

        player.InitSprite_Fall(Spr_Player_Fall, //Bitmap image
                4, this.m_iPlayerColor,       //Num frames, Start color
                1, true);   //Play speed, Repeat
    }

    public int RNG(){
        Random rng = new Random();
        int rngNumber = rng.nextInt(5) + 1;
        return rngNumber;
    }

    public void GenerateTile(int numRows, int numCols, int startY){
        //Creates 5x3 tiles for the game
        for(int rows = 0; rows < numRows; ++rows) {
            //Resets matching color every row
            boolean matchingColor = false;
            for (int cols = 0; cols < numCols; ++cols) {

                int m_iRNGResult = RNG();

                if (m_iRNGResult == player.getColor()) {
                    matchingColor = true;
                }

                Tile tile = new Tile(cols * screenWidth / numCols, startY + (screenHeight / 4) * rows, screenWidth / numCols, 50);

                //If at the last column there are no matching colors
                if (cols == numCols - 1 && matchingColor == false) {

                    //Colors
                    //1 - Red
                    //2 - Green
                    //3 - Blue
                    //4 - Yellow
                    //5 - Purple
                    switch (player.getColor()) {
                        //Red color
                        case 1:
                            tile.InitSprite(Spr_RedTile, //Bitmap image
                                    numFrames, player.getColor(),       //Num frames, Start color
                                    playSpeed, true);   //Play speed, Repeat
                            break;
                        //Green color
                        case 2:
                            tile.InitSprite(Spr_GreenTile, //Bitmap image
                                    numFrames, player.getColor(),       //Num frames, Start color
                                    playSpeed, true);   //Play speed, Repeat
                            break;
                        //Blue color
                        case 3:
                            tile.InitSprite(Spr_BlueTile, //Bitmap image
                                    numFrames, player.getColor(),       //Num frames, Start color
                                    playSpeed, true);   //Play speed, Repeat
                            break;
                        //yellow color
                        case 4:
                            tile.InitSprite(Spr_YellowTile, //Bitmap image
                                    numFrames, player.getColor(),       //Num frames, Start color
                                    playSpeed, true);   //Play speed, Repeat
                            break;
                        //purple color
                        case 5:
                            tile.InitSprite(Spr_PurpleTile, //Bitmap image
                                    6, player.getColor(),       //Num frames, Start color
                                    playSpeed, true);   //Play speed, Repeat
                            break;
                        default:
                            break;
                    }
                }
                //Generate a random color
                else {
                    switch (m_iRNGResult) {
                        //Red color
                        case 1:
                            tile.InitSprite(Spr_RedTile, //Bitmap image
                                    numFrames, 1,       //Num frames, Start color
                                    playSpeed, true);   //Play speed, Repeat
                            break;
                        //Green color
                        case 2:
                            tile.InitSprite(Spr_GreenTile, //Bitmap image
                                    numFrames, 2,       //Num frames, Start color
                                    playSpeed, true);   //Play speed, Repeat
                            break;
                        //Blue color
                        case 3:
                            tile.InitSprite(Spr_BlueTile, //Bitmap image
                                    numFrames, 3,       //Num frames, Start color
                                    playSpeed, true);   //Play speed, Repeat
                            break;
                        //Yellow color
                        case 4:
                            tile.InitSprite(Spr_YellowTile, //Bitmap image
                                    numFrames, 4,       //Num frames, Start color
                                    playSpeed, true);   //Play speed, Repeat
                            break;
                        //Purple color
                        case 5:
                            tile.InitSprite(Spr_PurpleTile, //Bitmap image
                                    numFrames, 5,       //Num frames, Start color
                                    playSpeed, true);   //Play speed, Repeat
                            break;
                        default:
                            break;
                    }
                }
                tile_list.add(tile);
            }
        }
    }

    public void renderTextOnScreen(Canvas newCanvas, String text, int posX, int posY, int size){
        if(newCanvas != null && text.length() != 0) {
            Paint paint = new Paint();
            paint.setARGB(255, 255, 255, 255);
            paint.setStrokeWidth(100);
            paint.setTextSize(size);
            paint.setTypeface(font);
            newCanvas.drawText(text, posX, posY, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        short touchX = (short) event.getX();
        short touchY = (short) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Left arrow
                if (touchX >= Rect_MoveLeft.left && touchX <= Rect_MoveLeft.right
                        && touchY >= Rect_MoveLeft.top && touchY <= Rect_MoveLeft.bottom) {
                    player.setMoveLeft(true);
                }
                //Right arrow
                if (touchX >= Rect_MoveRight.left && touchX <= Rect_MoveRight.right
                        && touchY >= Rect_MoveRight.top && touchY <= Rect_MoveRight.bottom) {
                    player.setMoveRight(true);
                }
                //Laser
                if (touchX >= Rect_Laser.left && touchX <= Rect_Laser.right
                        && touchY >= Rect_Laser.top && touchY <= Rect_Laser.bottom) {
                    player.useSynchro();
                }
                //Pause
                if (touchX >= Rect_Pause.left && touchX <= Rect_Pause.right
                        && touchY >= Rect_Pause.top && touchY <= Rect_Pause.bottom
                        && isPaused == false) {
                    isPaused = true;
                    gameThread.pauseThread(true);
                    SoundManager.SFX.start();
                }
                //Resume
                else if (touchX >= Rect_Play.left && touchX <= Rect_Play.right
                        && touchY >= Rect_Play.top && touchY <= Rect_Play.bottom
                        && isPaused == true) {
                    isPaused = false;
                    gameThread.pauseThread(false);
                    SoundManager.SFX.start();
                }
                //Back to menu
                if ((touchX >= Rect_Back.left && touchX <= Rect_Back.right
                        && touchY >= Rect_Back.top && touchY <= Rect_Back.bottom)
                        || (touchX >= Rect_Menu.left && touchX <= Rect_Menu.right
                        && touchY >= Rect_Menu.top && touchY <= Rect_Menu.bottom))
                {
                    gameThread.pauseThread(true);
                    intent.setClass(getContext(), Mainmenu.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    SoundManager.BGM.stop();
                    getContext().startActivity(intent);
                    SoundManager.SFX.start();
                }
                return true;
            case MotionEvent.ACTION_UP:
                player.setMoveLeft(false);
                player.setMoveRight(false);
        }
        return false;
    }

    public void update(float fps){

        if(!player.getDead())
        {
            this.FPS = fps;
            playScene_BG.update();
            player.update();
            updateTiles();
        }
        else{
            CheckHighScore(player.getScore());
        }
    }

    public void updateTiles(){
        boolean respawnTiles = false;

        //Update list of tiles
        for(int i = tile_list.size()-1; i >= 0; i--) {
            tile_list.get(i).changeSpeed(-player.getMultiplier());
            tile_list.get(i).update();

            //Removes tiles that are off the screen
            if((tile_list.get(i).getPosition().y) < 0) {
                tile_list.remove(i);
                respawnTiles = true;
            }

            //Adds score when tile is destroyed
            if(tile_list.get(i).getActive() == false)
            {
                tile_list.remove(i);
                if(player.getDead() == false){
                    player.addScore(1);
                    continue;
                }
            }
        }

        //Generate tiles relative to any position of the lowest tile
        if (respawnTiles == true) {
            if (tile_list.size() != 0) {
                GenerateTile(1, m_iDifficulty, tile_list.get(tile_list.size() - 1).getPosition().y + (screenHeight / 4));
            }
            else {
                GenerateTile(1, m_iDifficulty, screenHeight);
            }
        }

        //Activate synchronize effect
        if(player.getSynchroStatus()){
            for(Tile tile: tile_list){
                tile.setBreak(true);
            }
            player.setSynchroStatus(false);
            GenerateTile(4, m_iDifficulty, screenHeight);
        }

        //Update list of tiles
        for(int i = 0; i < tile_list.size(); ++i) {
            //Run distance check test
/*            int dist = tile_list.get(i).getPosition().y - player.getPosition().y;
            int maxDist = player.getScale().y + player.getVelocity().y + tile_list.get(i).getScale().y;*/

            //Run collision test
            if (checkCollide(player, tile_list.get(i))) {
                //If collided, move along with tiles
                player.setFall(false);
                player.addPosition(0, tile_list.get(i).getVelocity().y);

                if(InsideTile(player, tile_list.get(i)) && tile_list.get(i).getActive()){
                    //Kill player
                    player.setDead(true);
                }

                //Player must be fully on tile
                if (player.getPosition().x + player.getScale().x < tile_list.get(i).getPosition().x + tile_list.get(i).getScale().x) {
                    //Destroy tiles that are same color as player
                    if(tile_list.get(i).getColor() == player.getColor()) {
                        tile_list.get(i).setBreak(true);
                        startVibrate();
                    }
                }
                break;
            }
            player.setFall(true);
            //Dont check against tiles that are way below
/*            if(dist > maxDist) {
                //break;
            }*/
        }
    }

    public boolean checkCollide(Entity a, Entity b){
        return Rect.intersects(a.getDimensions(), b.getDimensions());
    }

    public boolean InsideTile(Entity a, Entity b){
        return a.getRight() + a.getVelocity().x > b.getLeft() &&
                (a.getBottom() + a.getVelocity().y * 10) > (b.getTop() + (b.getScale().y / 2));
    }

    public void CheckHighScore(int score){

        if(checkHighScore) {
            for (int i = 0; i < HighscoreManager.HighScore_List.length - 1; ++i) {
                //Replaces the lowest score if won
                if (score > HighscoreManager.HighScore_List[4] && checkHighScore) {
                    newHighScore = true;
                    //Replaces the lowest score
                    HighscoreManager.HighScore_List[4] = score;

                    //Sorts the score
                    for (int j = HighscoreManager.HighScore_List.length - 1; j > 0; --j) {
                        if (HighscoreManager.HighScore_List[j] > HighscoreManager.HighScore_List[j - 1]) {
                            int temp = HighscoreManager.HighScore_List[j];
                            HighscoreManager.HighScore_List[j] = HighscoreManager.HighScore_List[j - 1];
                            HighscoreManager.HighScore_List[j - 1] = temp;
                        }
                    }

                    checkHighScore = false;
                    //Saves the score
                    prefs = getContext().getSharedPreferences("HighscoreData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor prefEditor = prefs.edit();
                    prefEditor.putInt("FirstPlace", HighscoreManager.HighScore_List[0]);
                    prefEditor.putInt("SecondPlace", HighscoreManager.HighScore_List[1]);
                    prefEditor.putInt("ThirdPlace", HighscoreManager.HighScore_List[2]);
                    prefEditor.putInt("FourthPlace", HighscoreManager.HighScore_List[3]);
                    prefEditor.putInt("FifthPlace", HighscoreManager.HighScore_List[4]);
                    prefEditor.commit();
                }
            }
        }
        checkHighScore = false;
    }

    //@Override
    public void render(Canvas newCanvas) {
        //Clears canvas for new frame
        newCanvas.drawColor(Color.BLACK);

        //Renders a background scaled to phone dimensions
        if(newCanvas != null) {
            //Background
            playScene_BG.draw(newCanvas);

            //Tiles
            for(Tile tile : tile_list) {
                tile.draw(newCanvas);
            }

            //Player related
            player.draw(newCanvas);

            //UI portion
            //Arrow Buttons
            newCanvas.drawBitmap(bm_MoveLeft, Rect_MoveLeft.left, Rect_MoveLeft.top, null);
            newCanvas.drawBitmap(bm_MoveRight, Rect_MoveRight.left, Rect_MoveRight.top, null);

            //Laser Button
            if(player.getSynchroReady()){
                newCanvas.drawBitmap(bm_Synchro_icon, Rect_Laser.left, Rect_Laser.top, null);
            }
            else{
                newCanvas.drawBitmap(bm_Synchro_icon_unready, Rect_Laser.left, Rect_Laser.top, null);
            }

            //Score, Multiplier, FPS
            renderTextOnScreen(newCanvas, "Score: " + player.getScore(), 50, 175, 80);
            renderTextOnScreen(newCanvas, "Multiplier: X" + player.getMultiplier() , 50, 100, 50);
            renderTextOnScreen(newCanvas, "FPS: " + (float)FPS, 0, 50, 25);

            //Gameover overlay
            if(player.getDead()){

                newCanvas.drawBitmap(bm_GameOver, 150, screenHeight / 4, null);
                newCanvas.drawBitmap(bm_Back, Rect_Menu.left, Rect_Menu.top, null);

                renderTextOnScreen(newCanvas, "Game", screenWidth / 5 - 20, screenHeight / 3 + 50, 200);
                renderTextOnScreen(newCanvas, "Over!", screenWidth / 5 - 20, screenHeight / 3 + 200, 200);
                renderTextOnScreen(newCanvas, " - Back to menu", Rect_Menu.right, Rect_Menu.bottom - 50, 80);

                if(newHighScore) {
                    renderTextOnScreen(newCanvas, "New High Score: " + player.getScore() + "!", Rect_Menu.left, Rect_Menu.top - 50, 80);
                }
                else{
                    renderTextOnScreen(newCanvas, "Final Score: " + player.getScore(), Rect_Menu.left, Rect_Menu.top - 50, 80);
                }
            }
            else{
                if(isPaused){
                    newCanvas.drawBitmap(bm_Play, Rect_Play.left, Rect_Play.top, null);
                }
                else{
                    newCanvas.drawBitmap(bm_Pause, Rect_Pause.left, Rect_Pause.top, null);
                }
                newCanvas.drawBitmap(bm_Back, Rect_Back.left, Rect_Back.top, null);
            }

        }
    }

    @Override
    public void onSensorChanged(SensorEvent SenseEvent) {
        values = SenseEvent.values;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void SensorMove(){
        //Temp variables
        float tempX, tempY;

        tempX = player.getPosition().x + (values[1]  * ((System.currentTimeMillis() - lastTime)/1000 ));
        tempY = player.getPosition().y + (values[0]  * ((System.currentTimeMillis() - lastTime)/1000 ));

        if(tempX <= player.getSprite().getWidth()/2 || tempX >= screenWidth - player.getSprite().getWidth()/2)
        {
            if(tempY > player.getSprite().getHeight()/2 && tempY < screenHeight - player.getSprite().getHeight()/2)
            {
                player.setPosition(player.getPosition().x, (int)tempY);
            }
        }
        else if(tempY <= player.getSprite().getHeight()/2 || tempX >= screenHeight - player.getSprite().getHeight()/2)
        {
            if(tempX > player.getSprite().getWidth()/2 && tempX < screenWidth - player.getSprite().getWidth()/2)
            {
                player.setPosition((int)tempX, player.getPosition().y);
            }
        }
        else
        {
            player.setPosition((int)tempX, (int)tempY);
        }
    }
}