package com;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class Tile {

    private int x, y;                   //x, y hold the position of the Tile
    private int width, height;          //width, height hold the dimensions of the Tile
    private String type;                //type holds the type of Tile that this Tile will be
    private Rectangle hitbox;           //hitbox holds a rectangle around the Tile
    private Random rand = new Random(); //rand is a random number generator
    private Image currentTile;          //currentTile holds the sprite of the Tile

    public Tile(int x, int y, String type) throws IOException{
        this.x = x;
        this.y = y;
        this.type = type;

        loadTile();

        //This creates the hitbox
        width = currentTile.getWidth(null);
        height = currentTile.getHeight(null);
        hitbox = new Rectangle(x, y, width, height);
    }

    //This method loads the currentTile
    private void loadTile() throws IOException{
        if(type.equalsIgnoreCase("wall")){
            currentTile = ImageIO.read(new File("tiles/walls/wall" + rand.nextInt(24) + ".png"));
        }
        else if(type.equalsIgnoreCase("floor")){
            currentTile = ImageIO.read(new File("tiles/floors/dungeonFloor" + rand.nextInt(6) + ".png"));
        }
        else if(type.equalsIgnoreCase("void")){
            currentTile =ImageIO.read(new File("tiles/void.png"));
        }
        else if(type.equalsIgnoreCase("carpet")){
            currentTile = ImageIO.read(new File("tiles/carpet.png"));
        }
        else{
            currentTile = ImageIO.read(new File("tiles/placeholder0.png"));
        }
    }

    //These are the accessor methods
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public String getType(){
        return type;
    }
    public Rectangle getHitbox() {
        return hitbox;
    }
    public Rectangle getLeft() {
        return new Rectangle(x - 5, y, 5, 20);
    }
    public Rectangle getRight() {
        return new Rectangle(x + 20, y, 5, 20);
    }
    public Rectangle getTop(){
        return new Rectangle(x, y - 5, 20, 5);
    }
    public Rectangle getBottom(){
        return new Rectangle(x, y + 20, 20, 5);
    }

    public Image getTile() {
        return currentTile;
    }
}
