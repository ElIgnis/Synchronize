package Game;

import android.graphics.Rect;
import Math.Vector2;

public abstract class Entity {
    protected Vector2 position = new Vector2();
    protected Vector2 velocity = new Vector2();
    protected Vector2 scale = new Vector2();

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        this.position.x = x;
        this.position.y = y;
    }
    public void addPosition(int x, int y){
        this.position.x += x;
        this.position.y += y;
    }

    public void setVelocity (int x, int y){
        this.velocity.x = x;
        this.velocity.y = y;
    }

    public void addVelocity(int x, int y){
        this.velocity.x += x;
        this.velocity.y += y;
    }

    public Vector2 getVelocity(){
        return velocity;
    }

    public Vector2 getScale() {
        return scale;
    }

    public void setScale(int x, int y) {
        this.scale.x = x;
        this.scale.y = y;
    }

    public Rect getDimensions(){
        return new Rect(position.x, position.y  + velocity.y, position.x + scale.x, position.y + scale.y);
    }

    public float getLeft(){
        return position.x;
    }

    public float getRight(){
        return position.x + scale.x;
    }

    public float getTop(){
        return position.y;
    }

    public float getBottom(){
        return position.y + scale.y;
    }
}
