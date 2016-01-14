package Game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Ignis on 8/12/2015.
 */
public class Laser extends Entity{
    private int m_iMoveSpeed;
    private boolean m_bActive;
    private SpriteAnimation _SA;

    //Constructor
    public Laser(int pos_X, int pos_Y, int sprWidth, int sprHeight){
        setPosition(pos_X, pos_Y);
        setScale(sprWidth, sprHeight);
        this.m_iMoveSpeed = 10;
    }

    public void InitSprite(Bitmap newSprite, int numFrames, int playSpeed, boolean repeat){
        _SA = new SpriteAnimation(newSprite, playSpeed, numFrames, repeat);
    }

    public void update(){
        addPosition(0, 40);
    }

    public void draw(Canvas newCanvas){
        _SA.onDraw(newCanvas, position.x, position.y, scale.x, scale.y);
    }
}
