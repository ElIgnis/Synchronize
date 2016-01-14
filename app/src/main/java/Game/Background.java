package Game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import Math.Vector2;

public class Background {

    private Bitmap bg_Image;
    private Bitmap bg_Scaled;
    Vector2 position = new Vector2();
    Vector2 screenSize = new Vector2();
    private int dy = -5;

    //Constructor
    public Background(Bitmap newBackground, int screenWidth, int screenHeight){
        bg_Image = newBackground;
        screenSize.x = screenWidth;
        screenSize.y = screenHeight;
        bg_Scaled = Bitmap.createScaledBitmap(bg_Image, screenWidth, screenHeight, true);
    }

    public void update(){
        position.y += dy;

        //Reset image pos if it goes off the screen
        if(position.y < -screenSize.y)
            position.y = 0;
    }

    public void setScrollAmount(int newScrollAmount){
        this.dy = newScrollAmount;
    }

    public void draw(Canvas newCanvas){
        newCanvas.drawBitmap(bg_Scaled, position.x, position.y, null);
        if(position.y < 0)
            newCanvas.drawBitmap(bg_Scaled, position.x, position.y  + screenSize.y, null);
    }
}
