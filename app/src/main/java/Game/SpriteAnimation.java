package Game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class SpriteAnimation {
    private Bitmap sprite;
    private int m_iCurrentFrame;
    private double m_dFrameTime = 0;
    private double m_dPlaySpeed;
    private boolean m_bRepeat;
    private Rect srcRect;
    private int m_iSprWidth;
    private int m_iSprHeight;
    private int m_iNumFrames;

    SpriteAnimation(Bitmap spriteImage, double playSpeed, int numFrames, boolean repeat){
        this.sprite = spriteImage;
        this.m_dPlaySpeed = playSpeed;
        this.m_bRepeat = repeat;
        this.m_iCurrentFrame = 0;
        this.m_iSprWidth = spriteImage.getWidth() / numFrames;
        this.m_iSprHeight = spriteImage.getHeight();
        this.m_iNumFrames = numFrames;
        this.srcRect = new Rect(0, 0, m_iSprWidth, m_iSprHeight);
    }

    //Updates sprite anim
    public void update(){
        double m_dElapsedTime = (System.nanoTime() - m_dFrameTime) * 0.000001;

        //Updates frames based on speed and resets timer
        if(m_dElapsedTime * m_dPlaySpeed > 100) {
            ++m_iCurrentFrame;
            m_dFrameTime = System.nanoTime();
        }

        //Repeats sprite frame
        if(m_iCurrentFrame == m_iNumFrames && m_bRepeat == true){
            m_iCurrentFrame = 0;
        }
        this.srcRect.left = m_iCurrentFrame * m_iSprWidth;
        this.srcRect.right = this.srcRect.left + m_iSprWidth;
    }

    public int getCurrentFrame(){
        return m_iCurrentFrame;
    }

    public int getTotalFrames(){
        return m_iNumFrames - 1;
    }

    public void onDraw(Canvas newCanvas, int pos_X, int pos_Y, int scale_X, int scale_Y) {
        Rect dst = new Rect(pos_X, pos_Y, pos_X + scale_X, pos_Y + scale_Y);
        newCanvas.drawBitmap(sprite, srcRect, dst, null);
    }

    public int getWidth(){
        return this.m_iSprWidth;
    }

    public int getHeight(){
        return this.m_iSprHeight;
    }
}
