package Game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Tile extends Entity {
    private SpriteAnimation _SA;
    private boolean m_bActive;
    private boolean m_bBreak;
    private int m_iCurrentColor;
    private int m_iNumFrames;

    public Tile(int pos_X, int pos_Y,  int sprWidth, int sprHeight){
        setPosition(pos_X, pos_Y);
        setScale(sprWidth, sprHeight);

        //Speeds start at 1
        setVelocity(0, -3);

        this.m_bActive = true;
    }

    public void InitSprite(Bitmap newSprite, int numFrames, int startColor, int playSpeed, boolean repeat){
        this.m_iCurrentColor = startColor;

        _SA = new SpriteAnimation(newSprite, playSpeed, numFrames, repeat);
        this.m_iNumFrames = numFrames;
    }

    public void changeSpeed(int addSpeed){
        setVelocity(0, -5 + addSpeed);
    }

    public void update(){
        //Tiles move upwards TODO:Move L/R for other modes?
        addPosition(0, getVelocity().y);

        if(m_bBreak){
            _SA.update();
        }
        if(_SA.getCurrentFrame() == _SA.getTotalFrames()){
            m_bActive = false;
        }
    }

    public void draw(Canvas newCanvas){
        if(m_bActive)
            _SA.onDraw(newCanvas, getPosition().x, getPosition().y, getScale().x, getScale().y);
    }

    public int getColor(){
        return m_iCurrentColor;
    }

    public boolean getActive() {
        return m_bActive;
    }

    public void setActive(boolean m_bActive) {
        this.m_bActive = m_bActive;
    }

    public void setBreak(boolean breakTile){
        this.m_bBreak = breakTile;
    }
}
