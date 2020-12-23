package com;

import javax.imageio.ImageIO;
import javax.lang.model.type.ArrayType;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.XMLFormatter;

public class Projectile {

    private int x,y;            //x, y hold the position of the Projectile
    private double eX, eY;      //eX, eY hold the exact value of the x and y coordinate
    private int width, height;  //width, height hold the dimensions of the Projectile
    private String type;        //type holds the type of Projectile
    private double ang;         //ang holds the direction of the Projectile
    private int speed;          //speed holds the speed of the Projectile
    private int damage;         //damage holds how much damage the Projectile will do

    private Rectangle hitbox;   //hitbox holds a rectangle around the Projectile

    private ArrayList<Image> images = new ArrayList<Image>();   //Images holds the animations of the Projectile
    private Image currentSprite;            //currentSprite holds the sprite to be displayed
    private int spriteCounter = 0;          //spriteCounter keeps track of which sprite is to be displayed
    private int adjustedSpriteNumber = 0;   //adjustedSpriteNumber holds the slowed down spriteCounter

    public Projectile(int x, int y, String t, double a, int s, int d) throws IOException{
        this.x = x;
        this.eX = x;
        this.y = y;
        this.eY = y;
        this.type = t;
        this.ang = a;
        this.speed = s;
        this.damage = d;

        loadImages(type);

        currentSprite = images.get(0);
        this.width = currentSprite.getWidth(null);
        this.height = currentSprite.getHeight(null);
        this.hitbox = new Rectangle(this.x, this.y, width, height);
    }

    //This loads the animations for the Projectile
    private void loadImages(String type) throws IOException{
        if(type.equalsIgnoreCase("player")){
            for(int i = 0; i < 16; i++){
                Image ii = ImageIO.read(new File("projectiles/playerProjectile/playerProjectile" + i + ".png"));
                images.add(ii.getScaledInstance((int) (Math.round(ii.getWidth(null)*1.4)), (int) (Math.round(ii.getHeight(null))*1.4), Image.SCALE_SMOOTH));
            }
        }
        else if(type.equalsIgnoreCase("blaster")){
            Image ii = ImageIO.read(new File("projectiles/cannonball.png"));
            images.add(ii);
        }
    }

    //This method chooses which sprite the Projectile will display
    private void animateProjectile(){
        spriteCounter++;

        if(type.equalsIgnoreCase("player")){
            adjustedSpriteNumber = Math.round(spriteCounter ) % 16;
            currentSprite = images.get(adjustedSpriteNumber);
        }
        else if(type.equalsIgnoreCase("blaster")){
            currentSprite = images.get(0);
        }
    }

    //This method move the Projectile
    public void move(){
        this.eX += this.speed * Math.sin(ang);
        this.eY += this.speed * Math.cos(ang);

        this.x = (int) Math.round(eX);
        this.y = (int) Math.round(eY);

        animateProjectile();

        this.width = currentSprite.getWidth(null);
        this.height = currentSprite.getHeight(null);
        this.hitbox = new Rectangle(this.x, this.y, width, height);
    }

    //These are the accessor methods
    public int getX(){
        return x;
    }
    public int getY() {
        return y;
    }
    public Rectangle getHitbox(){
        return hitbox;
    }
    public int getDmg(){
        return damage;
    }
    public String getType(){
        return type;
    }
    public Image getImage(){
        return currentSprite;
    }
}
