package com;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Jumper implements Commons{

    private int x, y;                   //x, y holds the position of the Jumper
    private int previousX, previousY;   //previousX, previousY hold the last position of the Jumper
    private int width, height;          //width, height holds the dimensions of the Jumper
    private Rectangle hitbox;           //hitbox holds a rectangle around the Jumper
    private int health = 1;             //health holds the health of the Jumper
    private String type;                //type hols whether or not this Jumper will be a "boss" or not

    private double atkAng;              //atkAng holds the angle that the Jumper will need to follow to reach the player
    private boolean jumping = false;    //jumping holds whether or not the jumper is moving or not
    private boolean hit = false;        //hit holds whether or not the Jumper has hit something
    private int atkCoolDown;            //atkCoolDown holds the amount of time between attacks
    private int OG_atkCoolDown;         //OG_atkCoolDown holds the original amount of time between attacks
    private int atkDmg = 2;             //atkDmg holds the amount of damage that the attack does
    private int dashSpeed;              //dashSpeed holds how fast the Jumper will move
    private Rectangle targetBox;        //targetBox holds the hitbox of where the player was
    private boolean obstacle = false;   //obstacle holds whether or not the Jmper has encountered and enemy

    private Audio jumpSound;            //jumpSound holds the sound for the jumping

    private HashMap<String, Image> images = new HashMap<String, Image>();   //images holds all of the sprites for the Jumper
    private Image currentSprite;        //currentSprite holds the sprite that is to be displayed

    public Jumper(int x, int y, int as, int s, String t) throws IOException{
        this.x = x;
        this.previousX = x;
        this.y = y;
        this.previousY = y;
        this.atkCoolDown = as;
        this.OG_atkCoolDown = as;
        this.dashSpeed = s;
        this.type = t;

        jumpSound = new Audio("Sounds/Jump.wav");

        if(t.equalsIgnoreCase("boss")){
            health = 20;
            atkDmg *= 2;
        }

        loadImages();
        changeSpriteTo("jumperJump5");
    }

    //This loads the animations for the Jumper
    private void loadImages() throws IOException{
        if(type.equalsIgnoreCase("boss")){
            for(int i = 0; i < 6; i++){
                Image ii = ImageIO.read(new File("jumperSprites/jumperJump/jumperJump" + i + ".png"));
                images.put("jumperJump" + i, ii.getScaledInstance((int) Math.round(ii.getWidth(null)*5), (int) Math.round(ii.getHeight(null)*5), Image.SCALE_SMOOTH));
            }
        }
        else{
            for(int i = 0; i < 6; i++){
                Image ii = ImageIO.read(new File("jumperSprites/jumperJump/jumperJump" + i + ".png"));
                images.put("jumperJump" + i, ii.getScaledInstance((int) Math.round(ii.getWidth(null)*1.5), (int) Math.round(ii.getHeight(null)*1.5), Image.SCALE_SMOOTH));
            }
        }
    }

    //This method decides what the Jumper will do
    public void action(Rectangle playerHitbox){
        if(jumping){
            jump();
            changeSpriteTo("jumperJump2");
        }
        else if(atkCoolDown == 0){
            jump(playerHitbox);
            setHit(false);
            changeSpriteTo("jumperJump5");
        }
        else{
            tickAtkCoolDown();
            changeSpriteTo("jumperJump5");
        }
    }

    //This method changes the currentSprite and the hitbox accordingly
    private void changeSpriteTo(String spriteName){
        currentSprite = images.get(spriteName);
        width = currentSprite.getWidth(null);
        height = currentSprite.getHeight(null);
        hitbox = new Rectangle(x, y, width, height);
    }

    //This method calculates the angle of the Jumper attack
    private void jump(Rectangle playerRect){
        jumpSound.play();
        targetBox = new Rectangle(playerRect.x + 5, playerRect.y + 5, playerRect.width - 10, playerRect.height - 10);

        this.atkAng = Math.atan2(playerRect.x - this.x, (playerRect.y + (int) playerRect.height/2) - this.y);

        this.jumping = true;
        obstacle = false;
    }

    //This method move the Jumper closer to the targetBox
    private void jump(){
        previousX = x;
        previousY = y;

        //Note: If the Jumper passes the the targetBox (due to rounding), the jumper will move in one direction until it reaches the targetBox
        if(!hitbox.intersects(targetBox) && !obstacle){
            if(Math.abs(this.x - this.targetBox.x) <= 3){
                if(this.y > this.targetBox.y){
                    this.y -= this.dashSpeed;
                }
                else if(this.y < this.targetBox.y){
                    this.y += this.dashSpeed;
                }
            }
            else if(Math.abs(this.y - this.targetBox.y) <= 3){
                if(this.x > this.targetBox.x){
                    this.x -= this.dashSpeed;
                }
                else if(this.x < this.targetBox.x){
                    this.x += this.dashSpeed;
                }
            }
            else{
                this.x += Math.round(this.dashSpeed*Math.sin(atkAng));
                this.y += Math.round(this.dashSpeed*Math.cos(atkAng));
            }
        }
        else{
            this.jumping = false;
            this.atkCoolDown = this.OG_atkCoolDown;
        }

        hitbox = new Rectangle(this.x, this.y, width, height);
    }

    //This method decreases the atkCoolDown of this Jumper
    public void tickAtkCoolDown(){
        this.atkCoolDown--;
    }
    //This method subtracts damage from the player's health
    public void takeDmg(int d){
        health -= d;
    }
    //This method sets the obstacle boolean to be true
    public void hitObstacle(){
        obstacle = true;
    }

    //These are the accessor methods
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getPreviousX(){
        return previousX;
    }
    public int getPreviousY(){
        return previousY;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public Rectangle getHitbox() {
        return hitbox;
    }
    public int getAtkDmg(){
        return atkDmg;
    }
    public boolean getHit(){
        return hit;
    }
    public int getHealth(){
        return health;
    }
    public Image getCurrentSprite() {
        return currentSprite;
    }

    //These are the setter methods
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setHit(Boolean h){
        hit = h;
    }
}
