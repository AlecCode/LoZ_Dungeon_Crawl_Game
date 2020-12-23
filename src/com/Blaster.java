package com;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Blaster {

    private int x, y;           //x, y holds the position of the Blaster
    private int width, height;  //width, height holds the dimensions
    private int health = 2;     //health holds the health of the Blaster
    private Rectangle hitbox;   //hitbox holds a rectangle around the Blaster
    private String type;        //type holds whether or not this Blster will be a boss or not
    private int atkDmg = 2;     //atkDmg holds the damage that the attack will do

    private int atkCoolDown;    //damageCoolDown holds the cool-down period before the player can be damaged again
    private int OG_atkCoolDown; //damageCoolDown holds the original cool-down period before the player can be damaged again
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();    //projectiles holds the projectiles that are created

    private HashMap<String, Image> images = new HashMap<String, Image>();          //images holds all of the animation for the Blaster
    private Image currentSprite;//currentSprite holds the sprite to be displayed

    private Audio shotSound;    //shotSound holds the sound that plays when the Blaster shoots

    public Blaster(int x, int y, int as, String t) throws IOException{
        this.x = x;
        this.y = y;
        this.atkCoolDown = as;
        this.OG_atkCoolDown = as;
        this.type = t;

        shotSound = new Audio("Sounds/Shot.wav");

        if(t.equalsIgnoreCase("boss")){
            health = 20;
            atkDmg *= 2;
        }

        loadImages();
        currentSprite = images.get("blasterDown");

        width = currentSprite.getWidth(null);
        height = currentSprite.getHeight(null);
        hitbox = new Rectangle(x, y, width, height);
    }

    //This method loads the animations of the Blaster
    private void loadImages() throws IOException{
        if(type.equalsIgnoreCase("boss")){
            Image ii = ImageIO.read( new File("blasterSprites/blaster_up.png"));
            images.put("blasterUp", ii.getScaledInstance((int) (ii.getWidth(null)*5), (int) (ii.getHeight(null)*5), Image.SCALE_SMOOTH));
            ii = ImageIO.read( new File("blasterSprites/blaster_left.png"));
            images.put("blasterLeft", ii.getScaledInstance((int) (ii.getWidth(null)*5), (int) (ii.getHeight(null)*5), Image.SCALE_SMOOTH));
            ii = ImageIO.read( new File("blasterSprites/blaster_down.png"));
            images.put("blasterDown", ii.getScaledInstance((int) (ii.getWidth(null)*5), (int) (ii.getHeight(null)*55), Image.SCALE_SMOOTH));
            ii = ImageIO.read( new File("blasterSprites/blaster_right.png"));
            images.put("blasterRight", ii.getScaledInstance((int) (ii.getWidth(null)*5), (int) (ii.getHeight(null)*5), Image.SCALE_SMOOTH));
        }
        else{
            Image ii = ImageIO.read( new File("blasterSprites/blaster_up.png"));
            images.put("blasterUp", ii.getScaledInstance((int) (ii.getWidth(null)*1.5), (int) (ii.getHeight(null)*1.5), Image.SCALE_SMOOTH));
            ii = ImageIO.read( new File("blasterSprites/blaster_left.png"));
            images.put("blasterLeft", ii.getScaledInstance((int) (ii.getWidth(null)*1.5), (int) (ii.getHeight(null)*1.5), Image.SCALE_SMOOTH));
            ii = ImageIO.read( new File("blasterSprites/blaster_down.png"));
            images.put("blasterDown", ii.getScaledInstance((int) (ii.getWidth(null)*1.5), (int) (ii.getHeight(null)*1.5), Image.SCALE_SMOOTH));
            ii = ImageIO.read( new File("blasterSprites/blaster_right.png"));
            images.put("blasterRight", ii.getScaledInstance((int) (ii.getWidth(null)*1.5), (int) (ii.getHeight(null)*1.5), Image.SCALE_SMOOTH));
        }
    }

    public void action(Rectangle playerHitbox){
        if(atkCoolDown == 0){
            shoot(playerHitbox);
        }
        else{
            tickAtkCoolDown();
        }
    }

    private void shoot(Rectangle playerRect){
        shotSound.play();
        double ang = Math.atan2(playerRect.x - this.x, playerRect.y - this.y);

        try {
            projectiles.add(new Projectile(x, y, "blaster", ang, 5, atkDmg));
        }
        catch (Exception e){}
        atkCoolDown = OG_atkCoolDown;



        if(Math.abs(ang) <= Math.toRadians(45)){
            currentSprite = images.get("blasterDown");
        }
        else if(ang > Math.toRadians(45) && ang < Math.toRadians(135)){
            currentSprite = images.get("blasterRight");
        }
        else if(ang < -Math.toRadians(45) && ang > -Math.toRadians(135)){
            currentSprite = images.get("blasterLeft");
        }
        else{
            currentSprite = images.get("blasterUp");
        }

        width = currentSprite.getWidth(null);
        height = currentSprite.getHeight(null);
        hitbox = new Rectangle(x, y, width, height);
    }

    public int getX(){
        return x;
    }
    public int getY() {
        return y;
    }
    public Rectangle getHitbox() {
        return hitbox;
    }
    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }
    public int getHealth(){
        return health;
    }
    public Image getCurrentSprite(){
        return currentSprite;
    }

    public void removeProjectile(Projectile p){
        projectiles.remove(p);
    }
    public void tickAtkCoolDown(){
        atkCoolDown--;
    }
    public void takeDmg(int d){
        health -= d;
    }
}
