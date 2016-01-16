package com.sidm.synchronize;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/*Main thread to run the game*/
public class GameThread extends Thread{

    // Surface holder that can access the physical surface
    private SurfaceHolder surfaceHolder;

    // The actual view that handles inputs and draws to the surface
    private PlayScene playScene;

    private int m_iFPS_Limit = 60;
    private float m_fAverageFPS;

    // Flag to hold game state
    private boolean m_bRun;
    private boolean m_bPaused = false;
    public static Canvas canvas;

    //Constructor
    public GameThread(SurfaceHolder newSurfaceHolder, PlayScene newPlayScene)
    {
        super();
        this.surfaceHolder = newSurfaceHolder;
        this.playScene = newPlayScene;
    }

    //Overriding thread run
    @Override
    public void run(){

        //Cap FPS at 30?
        long m_lStartTime;
        long m_lTimeMillis;
        long m_lWaitTime;
        long m_lTotalTime = 0;
        int m_iFrameCount = 0;
        long targetTime = 1000/m_iFPS_Limit;

        //Game loop
        while(m_bRun) {

            while (!m_bPaused) {
                m_lStartTime = System.nanoTime();
                canvas = null;

                //Write to  buffer
                try {
                    canvas = this.surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        //Updates and draws scene
                        this.playScene.update(m_fAverageFPS);
                        this.playScene.render(canvas);
                    }

                } catch (Exception e) {
                } finally {
                    if (canvas != null) {
                        try {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                m_lTimeMillis = (System.nanoTime() - m_lStartTime) / 1000000;
                m_lWaitTime = targetTime - m_lTimeMillis;

                try {
                    sleep(m_lWaitTime);
                } catch (Exception e) {
                }

                m_lTotalTime += System.nanoTime() - m_lStartTime;
                m_iFrameCount++;

                //Calculate fps
                if (m_iFrameCount == m_iFPS_Limit) {
                    m_fAverageFPS = 1000 / ((float) (m_lTotalTime / m_iFrameCount) / 1000000);
                    m_iFrameCount = 0;
                    m_lTotalTime = 0;
                }
            }
        }
    }
    public void runThread(boolean runFlag){
        m_bRun = runFlag;
    }

    public void pauseThread(boolean pauseFlag){
        m_bPaused = pauseFlag;
    }
}
