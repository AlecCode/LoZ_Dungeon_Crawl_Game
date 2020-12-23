package com;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PowerUp{

    private int x, y;           //x, y holds the position of the PowerUp
    private int width, height;  //width, height hold the dimensions of the PowerUp
    private Rectangle hitbox;   //hitbox holds the rectangle around the PowerUp
    private String type;        //type holds what kind of PowerUp this PowerUp will be
    private Image currentSprite;//currentSprite holds the sprite of the PowerUp

    public PowerUp(int x, int y, String type) throws IOException{
        this.x = x;
        this.y = y;
        this.type = type;

        loadImage();
    }

    //This method loads the sprite for the PowerUp
    private void loadImage() throws IOException{
        if(type.equalsIgnoreCase("restoreHealth")){
            currentSprite = ImageIO.read(new File("powerUpSprites/healthRestore.png"));
        }
        else if(type.equalsIgnoreCase("healthUpgrade")){
            currentSprite = ImageIO.read(new File("powerUpSprites/healthUpgrade.png"));
        }
        else if(type.equalsIgnoreCase("projectilesUp")){
            currentSprite = ImageIO.read(new File("powerUpSprites/projectilesUp.png"));
        }
        else{
            type = "dmgUp";
            currentSprite = ImageIO.read(new File("powerUpSprites/dmgUp.png"));
        }

        width = currentSprite.getWidth(null);
        height = currentSprite.getHeight(null);
        hitbox = new Rectangle(x , y ,width, height);
    }

    //These are the accessor methods
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public String getType() {
        return type;
    }
    public Rectangle getHitbox() {
        return hitbox;
    }
    public Image getCurrentSprite() {
        return currentSprite;
    }
}
