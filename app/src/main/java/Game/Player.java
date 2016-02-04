package Game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Player extends Entity{

    //Player variables
    private int m_iScore;
    private int m_iMultiplier;
    private int m_iMultCounter;
    private int m_iGravity = 10;
    private int m_iCurrentColor;

    private boolean m_bMoveLeft = false;
    private boolean m_bMoveRight = false;
    private boolean m_bSynchroReady = false;    //For icon
    private boolean m_bSynchroActivate = false; //For usage
    private int m_iLeftBound;
    private int m_iRightBound;
    private int m_iTopBound;
    private int m_iBottomBound;
    private int m_iMoveSpeed;

    private boolean m_bFall = true;
    private boolean m_bDead = false;

    private SpriteAnimation _SA_Idle;
    private SpriteAnimation _SA_MoveLeft;
    private SpriteAnimation _SA_MoveRight;
    private SpriteAnimation _SA_Fall;

    //Constructor
    public Player(int pos_X, int pos_Y, int sprWidth, int sprHeight){
        setPosition(pos_X, pos_Y);
        setScale(sprWidth, sprHeight);
        this.m_iScore = 0;
        this.m_iMultiplier = 1;
        this.m_iMoveSpeed = 10;
    }

    public void InitSprite_Idle(Bitmap newSprite, int numFrames, int startColor, int playSpeed, boolean repeat){
        this.m_iCurrentColor = startColor;
        _SA_Idle = new SpriteAnimation(newSprite, playSpeed, numFrames, repeat);
    }

    public void InitSprite_MoveLeft(Bitmap newSprite, int numFrames, int startColor, int playSpeed, boolean repeat){
        this.m_iCurrentColor = startColor;
        _SA_MoveLeft = new SpriteAnimation(newSprite, playSpeed, numFrames, repeat);
    }

    public void InitSprite_MoveRight(Bitmap newSprite, int numFrames, int startColor, int playSpeed, boolean repeat){
        this.m_iCurrentColor = startColor;
        _SA_MoveRight = new SpriteAnimation(newSprite, playSpeed, numFrames, repeat);
    }

    public void InitSprite_Fall(Bitmap newSprite, int numFrames, int startColor, int playSpeed, boolean repeat){
        this.m_iCurrentColor = startColor;
        _SA_Fall = new SpriteAnimation(newSprite, playSpeed, numFrames, repeat);
    }

    public void setMoveLeft(boolean moveLeft){
        this.m_bMoveLeft = moveLeft;
    }
    public void setMoveRight(boolean moveRight){
        this.m_bMoveRight = moveRight;
    }
    public boolean getMoveLeft(){
        return m_bMoveLeft;
    }
    public boolean getMoveRight(){
        return m_bMoveRight;
    }

    public void setFall(boolean fall){
        this.m_bFall = fall;
    }
    public void setDead(boolean dead){ this.m_bDead = dead;}
    public boolean getDead(){return m_bDead;}
    public int getColor(){
        return m_iCurrentColor;
    }

    public void setColor(int newColor){
        this.m_iCurrentColor = newColor;
    }

    public void setBounds(int left, int right, int top, int bottom, boolean wrap){

        this.m_iLeftBound = left;
        this.m_iRightBound = right;
        this.m_iTopBound = top;
        this.m_iBottomBound = bottom;
    }

    public void update(){
        m_iMoveSpeed = 10 + m_iMultiplier;

        if(getPosition().x - getVelocity().x < m_iLeftBound) {
            this.m_bMoveLeft = false;
        }

        if(getPosition().x + getScale().x + getVelocity().x > m_iRightBound){
            this.m_bMoveRight = false;
        }

        if(getPosition().y < m_iTopBound){
            this.m_bDead = true;
        }

        if(m_bMoveLeft){
            setVelocity(-m_iMoveSpeed, 0);
            _SA_MoveLeft.update();
        }

        if(m_bMoveRight){
            setVelocity(m_iMoveSpeed, 0);
            _SA_MoveRight.update();
        }
        if(m_bFall) {
            this.m_iGravity = m_iMoveSpeed;
            _SA_Fall.update();
        }
        if(!m_bFall || getPosition().y + getScale().y > m_iBottomBound){
            this.m_iGravity = 0;
        }

        if(!m_bMoveLeft && !m_bMoveRight){
            setVelocity(0, 0);
            _SA_Idle.update();
        }

        if(!m_bDead){
            addPosition(getVelocity().x, m_iGravity);
        }

        if(m_iMultCounter > 30){
            m_iMultCounter = 0;
            ++m_iMultiplier;
            m_bSynchroReady = true;
        }
    }

    public void addScore(int addAmt){
        this.m_iScore += addAmt * m_iMultiplier;
        m_iMultCounter += addAmt;
    }

    public boolean useSynchro(){
        if(m_iMultiplier > 1){
            --m_iMultiplier;

            //Add timer
            if(m_iMultiplier == 1){
                m_bSynchroReady = false;
            }
            this.m_bSynchroActivate = true;
            return true;
        }
        else
            return false;
    }

    public void setSynchroStatus(boolean newStatus){
        this.m_bSynchroActivate = newStatus;
    }

    public boolean getSynchroReady(){
        return m_bSynchroReady;
    }

    public boolean getSynchroStatus(){
        return m_bSynchroActivate;
    }

    public int getScore(){
        return m_iScore;
    }

    public int getMultiplier() {return m_iMultiplier; }

    public void draw(Canvas newCanvas){
        if(m_bMoveLeft) {
            _SA_MoveLeft.onDraw(newCanvas, getPosition().x, getPosition().y, getScale().x, getScale().y);
        }
        else if (m_bMoveRight) {
            _SA_MoveRight.onDraw(newCanvas, getPosition().x, getPosition().y, getScale().x, getScale().y);
        }
        else if(m_bFall){
            _SA_Fall.onDraw(newCanvas, getPosition().x, getPosition().y, getScale().x, getScale().y);
        }
        else
        _SA_Idle.onDraw(newCanvas, getPosition().x, getPosition().y, getScale().x, getScale().y);
    }

    public SpriteAnimation getSprite(){
        if(m_bMoveLeft) {
            return _SA_MoveLeft;
        }
        else if (m_bMoveRight) {
            return _SA_MoveRight;
        }
        else if(m_bFall){
            return _SA_Fall;
        }
        else
            return _SA_Idle;

    }
}
