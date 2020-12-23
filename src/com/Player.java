package com;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Player implements Commons{

    private int x, y;                   //x, y holds the position of the player
    private int previousX, previousY;   //previousX, previousY hold the previous position of the player
    private int width, height;          //width, height hold the dimensions of the Player
    private int vx, vy;                 //vx, vy hold the speed of the players
    private Rectangle hitbox;           //hitbox holds the rectangle that goes around the Player

    private ArrayList<String> powerUps = new ArrayList<String>();   //powerups hold hte powerups tha are applied to the Player
    private String ability;             //ability holds thespecial ability of the player
    private String direction = "down";  //direction holds the orientation of the player
    private double playerSize = 1.5;    //playerSize holds the multiplier of the size of the player
    private int speed = 5;              //speed holds how fast the player can move

    private int atkDmg = 1;             //atkDmg holds the attack damage
    private int OGatkDmg = 1;           //OGatkDmg holds the original
    private int health = 10;            //health holds the health of the Player
    private int OGhealth = 10;          //health holds the orginal health of the player
    private int damageCoolDown = 25;    //damageCoolDown holds the cool-down period before the player can be damaged again
    private int OGdamageCoolDown = 25;  //OGdamageCoolDown holds the original cool-down period before the player can be damaged again

    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();    //projectiles holds all of the sprites of the player

    private HashMap<String, Image> images = new HashMap<String, Image>();           //images hold all of the sprites of the player
    private int spriteCounter = 0;      //spriteCounter keeps track of which sprite should be on the
    private int adjustedSpriteNumber;   //adjustedSpriteNumber is used to slow down the animation
    private Image currentSprite;        //currentSprite holds the sprite of the player to ge shown
    private boolean slashing = false;   //slashing holds whether or not the player is slashing
    private boolean finalSlashFame = false; //finalSlashFrame
    private boolean falling = false;    //falling holds whether or not this weeve

    private Audio slashSound;           //slashSound holds the sound that plays for survival of the fittest

    public Player(int x, int y, String a) throws IOException{
        this.x = x;
        this.previousX = x;
        this.y = y;
        this.previousY = y;
        this.ability = a;

        slashSound = new Audio("Sounds/Slash.wav");

        if(ability.equalsIgnoreCase("sizeDown")){
            playerSize = 1;
        }

        loadImages();

        //This initializes the player sprite and hitbox
        currentSprite = images.get("playerStand_down");
        width = currentSprite.getWidth(null);
        height = currentSprite.getHeight(null);
        hitbox = new Rectangle(x, y, width, height);
    }

    //This method loads all player animations and re-sizes the sprites to be larger
    private void loadImages() throws IOException{
        //load all player slash animations
        for(int i = 0; i < 6 ; i++){
            Image ii = ImageIO.read(new File("playerSprites/playerSlash_down/playerSlash_down" + i + ".png"));
            images.put("playerSlash_down" + i, ii.getScaledInstance((int) Math.round(ii.getWidth(null)*playerSize), (int) Math.round(ii.getHeight(null)*playerSize), Image.SCALE_SMOOTH));
            ii = ImageIO.read(new File("playerSprites/playerSlash_up/playerSlash_up" + i + ".png"));
            images.put("playerSlash_up" + i, ii.getScaledInstance((int) Math.round(ii.getWidth(null)*playerSize), (int) Math.round(ii.getHeight(null)*playerSize), Image.SCALE_SMOOTH));
            ii = ImageIO.read(new File("playerSprites/playerSlash_left/playerSlash_left" + i + ".png"));
            images.put("playerSlash_left" + i, ii.getScaledInstance((int) Math.round(ii.getWidth(null)*playerSize), (int) Math.round(ii.getHeight(null)*playerSize), Image.SCALE_SMOOTH));
            ii = ImageIO.read(new File("playerSprites/playerSlash_right/playerSlash_right" + i + ".png"));
            images.put("playerSlash_right" + i, ii.getScaledInstance((int) Math.round(ii.getWidth(null)*playerSize), (int) Math.round(ii.getHeight(null)*playerSize), Image.SCALE_SMOOTH));
        }
        //load all player walking animations
        for(int i = 0; i < 8; i++){
            Image ii = ImageIO.read(new File("playerSprites/playerWalk_down/playerWalk_down" + i + ".png"));
            images.put("playerWalk_down" + i, ii.getScaledInstance((int) Math.round(ii.getWidth(null)*playerSize), (int) Math.round(ii.getHeight(null)*playerSize), Image.SCALE_SMOOTH));
            ii = ImageIO.read(new File("playerSprites/playerWalk_up/playerWalk_up" + i + ".png"));
            images.put("playerWalk_up" + i, ii.getScaledInstance((int) Math.round(ii.getWidth(null)*playerSize), (int) Math.round(ii.getHeight(null)*playerSize), Image.SCALE_SMOOTH));
            ii = ImageIO.read(new File("playerSprites/playerWalk_left/playerWalk_left" + i + ".png"));
            images.put("playerWalk_left" + i, ii.getScaledInstance((int) Math.round(ii.getWidth(null)*playerSize), (int) Math.round(ii.getHeight(null)*playerSize), Image.SCALE_SMOOTH));
            ii = ImageIO.read(new File("playerSprites/playerWalk_right/playerWalk_right" + i + ".png"));
            images.put("playerWalk_right" + i, ii.getScaledInstance((int)(Math.round(ii.getWidth(null))*playerSize), (int) Math.round(ii.getHeight(null)*playerSize), Image.SCALE_SMOOTH));
        }
        //Load all falling animations
        for(int i = 0; i < 4; i++){
            Image ii = ImageIO.read(new File("playerSprites/playerFall/playerFall" + i + ".png"));
            images.put("playerFall" + i, ii.getScaledInstance((int) Math.round(ii.getWidth(null)*playerSize), (int) Math.round(ii.getHeight(null)*playerSize), Image.SCALE_SMOOTH));
        }
        //load all standing animations
        Image ii = ImageIO.read(new File("playerSprites/playerStand/playerStand_down.png"));
        images.put("playerStand_down", ii.getScaledInstance((int) Math.round(ii.getWidth(null)*playerSize), (int) Math.round(ii.getHeight(null)*playerSize), Image.SCALE_SMOOTH));
        ii = ImageIO.read(new File("playerSprites/playerStand/playerStand_up.png"));
        images.put("playerStand_up", ii.getScaledInstance((int) Math.round(ii.getWidth(null)*playerSize), (int) Math.round(ii.getHeight(null)*playerSize), Image.SCALE_SMOOTH));
        ii = ImageIO.read(new File("playerSprites/playerStand/playerStand_left.png"));
        images.put("playerStand_left", ii.getScaledInstance((int) Math.round(ii.getWidth(null)*playerSize), (int) Math.round(ii.getHeight(null)*playerSize), Image.SCALE_SMOOTH));
        ii = ImageIO.read(new File("playerSprites/playerStand/playerStand_right.png"));
        images.put("playerStand_right", ii.getScaledInstance((int) Math.round(ii.getWidth(null)*playerSize), (int) Math.round(ii.getHeight(null)*playerSize), Image.SCALE_SMOOTH));
    }

    public void move(){
        previousX = x;
        previousY = y;

        if(ability.equalsIgnoreCase("speedUp")){
            speed = (int)(5 * 1.5);
        }

        x += vx;
        y += vy;

        if(powerUps.contains("dmgUp")){
            atkDmg = OGatkDmg * 2;
        }
        else{
            atkDmg = OGatkDmg;
        }

        animatePlayer();

        //This cooldown is used so the player can only take damage from one source at a time
        if(damageCoolDown < OGdamageCoolDown){
            damageCoolDown++;
        }
        else{
            damageCoolDown = OGdamageCoolDown;
        }
    }

    private void animatePlayer(){
        finalSlashFame = false;

        if(!slashing && !falling){
            if(this.vx == 0 && this.vy == 0){
                spriteCounter = 0;      //This resets the walking animation
                currentSprite = images.get("playerStand_" + this.direction);
            }
            else{
                spriteCounter++;        //This advances the walking animation
                adjustedSpriteNumber = (int) Math.round(spriteCounter /3) % 8;
                currentSprite = images.get("playerWalk_" + this.direction + adjustedSpriteNumber);
            }
        }
        else if(falling){
            spriteCounter++;
            adjustedSpriteNumber = (int) Math.round(spriteCounter /10) % 5;
            currentSprite = images.get("playerFall" + adjustedSpriteNumber);

            if(adjustedSpriteNumber == 3){
                falling = false;
                health = 0;
            }
        }
        else{
            spriteCounter++;
            adjustedSpriteNumber = (int) Math.round(spriteCounter / 3) % 6;
            currentSprite = images.get("playerSlash_" + this.direction + adjustedSpriteNumber);

            if(adjustedSpriteNumber == 5){
                slashing = false;
                finalSlashFame = true;
                spriteCounter = 0;
            }
        }

        try{
            width = currentSprite.getWidth(null);
            height = currentSprite.getHeight(null);
            hitbox = new Rectangle(x, y, width, height);
        }catch (Exception e){
            //System.out.println(e);
        }
    }

    private ArrayList<Projectile> shoot() throws IOException{
        slashSound.play();
        ArrayList<Projectile> newProjectiles = new ArrayList<Projectile>();

        if(!powerUps.contains("projectilesUp")){
            if(this.direction.equalsIgnoreCase("UP")){
                newProjectiles.add(new Projectile(this.x + this.width/2, this.y, "player", Math.toRadians(180), 7, atkDmg));
            }
            else if(this.direction.equalsIgnoreCase("DOWN")){
                newProjectiles.add(new Projectile(this.x + this.width/2, this.y + this.height, "player", Math.toRadians(0), 7, atkDmg));
            }
            else if(this.direction.equalsIgnoreCase("LEFT")){
                newProjectiles.add(new Projectile(this.x - 5, this.y + this.height/2, "player", Math.toRadians(270), 7, atkDmg));
            }
            else{
                newProjectiles.add(new Projectile(this.x + this.width + 5, this.y + this.height/2, "player", Math.toRadians(90), 7, atkDmg));
            }
        }
        else{
            if(this.direction.equalsIgnoreCase("UP")){
                newProjectiles.add(new Projectile(this.x + this.width/2, this.y, "player", Math.toRadians(180), 7, atkDmg));
                newProjectiles.add(new Projectile(this.x + this.width/2, this.y, "player", Math.toRadians(150), 7, atkDmg));
                newProjectiles.add(new Projectile(this.x + this.width/2, this.y, "player", Math.toRadians(210), 7, atkDmg));
            }
            else if(this.direction.equalsIgnoreCase("DOWN")){
                newProjectiles.add(new Projectile(this.x + this.width/2, this.y + this.height, "player", Math.toRadians(0), 7, atkDmg));
                newProjectiles.add(new Projectile(this.x + this.width/2, this.y + this.height, "player", Math.toRadians(330), 7, atkDmg));
                newProjectiles.add(new Projectile(this.x + this.width/2, this.y + this.height, "player", Math.toRadians(30), 7, atkDmg));
            }
            else if(this.direction.equalsIgnoreCase("LEFT")){
                newProjectiles.add(new Projectile(this.x - 5, this.y + this.height/2, "player", Math.toRadians(270), 7, atkDmg));
                newProjectiles.add(new Projectile(this.x - 5, this.y + this.height/2, "player", Math.toRadians(300), 7, atkDmg));
                newProjectiles.add(new Projectile(this.x - 5, this.y + this.height/2, "player", Math.toRadians(240), 7, atkDmg));
            }
            else{
                newProjectiles.add(new Projectile(this.x + this.width + 5, this.y + this.height/2, "player", Math.toRadians(90), 7, atkDmg));
                newProjectiles.add(new Projectile(this.x + this.width + 5, this.y + this.height/2, "player", Math.toRadians(60), 7, atkDmg));
                newProjectiles.add(new Projectile(this.x + this.width + 5, this.y + this.height/2, "player", Math.toRadians(120), 7, atkDmg));
            }
        }

        return newProjectiles;
    }

    public void takeDamage(int d){
        if(ability.equalsIgnoreCase("defenseUp")){
            d = (int)(d/2);
        }

        if(damageCoolDown == OGdamageCoolDown){
            health -= d;
            damageCoolDown = 0;
        }
    }

    public void addPowerUp(String p){
        if(p.equalsIgnoreCase("restoreHealth")){
            if((health + 5) > OGhealth && !(health > OGhealth)){
                health = OGhealth;
            }
            else if((health + 5) > (OGhealth + 10)){
                health = OGhealth + 10;
            }
            else{
                health += 5;
            }
        }
        else if(p.equalsIgnoreCase("healthUpgrade")){
            health = OGhealth + 10;
        }
        else{
            powerUps.add(p);
        }
    }
    public void clearPowerUps(){
        powerUps.clear();
    }



    public int getX(){
        int adjustedX = x;

        if(direction.equalsIgnoreCase("down") && slashing && adjustedSpriteNumber == 0){
            adjustedX -= 10;
        }
        else if(direction.equalsIgnoreCase("down") && slashing && adjustedSpriteNumber == 1){
            adjustedX -= 10;
        }
        else if(direction.equalsIgnoreCase("up") && (slashing || finalSlashFame) && adjustedSpriteNumber > 2){
            adjustedX -= 20;
        }
        else if(direction.equalsIgnoreCase("left") && slashing && adjustedSpriteNumber != 0 && adjustedSpriteNumber != 5){
            adjustedX -= 10;
        }
        else{
            adjustedX = x;
        }

        return adjustedX;
    }
    public int getY(){ return y; }
    public int getPreviousX(){ return previousX; }
    public int getPreviousY(){ return previousY; }
    public int getHealth(){ return health; }
    public int getOGhealth(){ return OGhealth; }
    public Rectangle getHitbox(){ return hitbox; }
    public Image getCurrentSprite(){ return currentSprite; }
    public ArrayList<String> getPowerUps() { return powerUps; }
    public Rectangle getFeet(){
        if((slashing || finalSlashFame) && (direction.equalsIgnoreCase("left") || direction.equalsIgnoreCase("right"))){
            return new Rectangle(x + (int)(width/2) - 5, y + height - 41, 10, 10);
        }
        else if((slashing || finalSlashFame) && direction.equalsIgnoreCase("down")){
            return new Rectangle(x + (int)(width/2) - 5, y + height - 36, 10, 10);
        }
        else{
            return new Rectangle(x + (int)(width/2) - 5, y + height - 15, 10, 10);
        }
    }
    public ArrayList<Projectile> getProjectiles() { return projectiles; }
    public void removeProjectile(Projectile p){ projectiles.remove(p); }

    public void setX(int x){ this.x = x; }
    public void setY(int y){ this.y = y; }
    public void setFalling(boolean f){ falling = f; }



    public void KeyPressed(KeyEvent k) throws IOException{
        int key = k.getKeyCode();

        if(key == KeyEvent.VK_UP){
            this.vy = -speed;
            this.direction = "up";
        }
        if(key == KeyEvent.VK_DOWN){
            this.vy = speed;
            this.direction = "down";
        }
        if(key == KeyEvent.VK_RIGHT){
            this.vx = speed;
            this.direction = "right";
        }
        if(key == KeyEvent.VK_LEFT){
            this.vx = -speed;
            this.direction = "left";
        }

        if(key == KeyEvent.VK_SPACE & !slashing){
            for(Projectile p : shoot()){
                projectiles.add(p);
            }
            slashing = true;

            spriteCounter = 0;
            adjustedSpriteNumber = 0;
        }
    }
    public void keyReleased(KeyEvent k){
        int key = k.getKeyCode();

        if(key == KeyEvent.VK_UP){
            this.vy = 0;
        }
        else if(key == KeyEvent.VK_DOWN){
            this.vy = 0;
        }

        if(key == KeyEvent.VK_RIGHT){
            this.vx = 0;
        }
        else if(key == KeyEvent.VK_LEFT){
            this.vx = 0;
        }
    }
}
