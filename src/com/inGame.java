package com;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class inGame extends JPanel implements ActionListener, Commons{

    private final int DELAY = 15;           //DELAY holds the delay for the timer
    private Timer timer;                    //timer holds the clock that updates the game
    private Random rand = new Random();     //rand holds a random number generator
    private Rectangle screenBounds = new Rectangle(0, 200, S_WIDTH, S_HEIGHT - 200);    //screenBounds holds the bounds of the play area as a rectangle

    private Player player;                                          //player holds the user's player class
    private ArrayList<Jumper> jumpers = new ArrayList<Jumper>();    //jumpers holds all "jumper" enemies that are on screen
    private ArrayList<Blaster> blasters = new ArrayList<Blaster>(); //blasters holds all "blaster" enemies that are on screen
    private ArrayList<Tile> tiles = new ArrayList<Tile>();          //tiles holds all the tiles that make up the level
    private Tile[][] map = new Tile[30][55];                        //map holds all the tiles that make up the level as a two-dimensional array(for easier traversal)
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();    //projectiles holds all of the projectiles on screen
    private ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();             //powerUps holds all of the powerUps on screen

    private int score = 0;                  //score holds the score of the player
    private int enemiesOnScreen;            //enemiesOnScreen holds the number of enemies that are on screen
    private int stageLevel = 0;             //stageLevel holds the level of the current stage

    private Image heartPic;                 //heartPic holds the hearts that are used to represent the player's health
    private Image swordUpPic;               //swordUpPic holds the image used to show the "dmgUp" powerUp in the status bar
    private Image projectileUpPic;          //projectileUpPic holds the image used to show the "projectilesUp" powerUp in the status bar
    private Image healthUpPic;              //healthUpPic holds the image used to show the "healthUpgrade" powerUp in the status bar
    private Image abilityPic;               //abilityPic holds the image used to show the player's selected ability in the status bar

    private boolean changeLevel = false;    //changeLevel holds whether or not the game needs to change levels
    private boolean finalLevel = false;     //finalLevel holds whether or not the player should be facing the final boss
    private boolean bossDead = false;       //bossDead holds whether or not the player has defeated the final boss
    private boolean firstLevel = true;      //firstLevel holds whether or not this is the first level the player has played
    private String enterFrom = "none";      //enterFrom holds where the player entered into the new level from
    private String gameMode;                //gameMode holds which game-mode the player is playing (Boss Rush or Dungeon Dash)
    private String playerAbility = "none";  //playerAbility holds which ability the player chose to have

    private boolean running;                //running holds whether or not the game is still running
    private boolean alive;                  //alive holds whether or not hte player is still alive

    private Audio music;                    //music holds tha background music that plays
    private Audio pickUpSound;              //pickUpSound holds the sound that triggers when the player picks up a powerUp

    public inGame(String gameMode, String playerAbility) throws IOException{
        //This initializes key-inputs and the window
        addKeyListener(new IOAdapter());
        setFocusable(true);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(S_WIDTH, S_HEIGHT));

        running = true;
        alive = true;
        this.gameMode = gameMode;
        this.playerAbility = playerAbility;

        //This loads in the sounds
        music = new Audio("Sounds/GameMusic.wav");
        music.loop();
        pickUpSound = new Audio("Sounds/PowerUp.wav");

        //This loads the images that will be displayed in the status bar
        Image ii = ImageIO.read(new File("miscSprites/heart.png"));
        heartPic = ii;
        ii = ImageIO.read(new File("powerUpSprites/dmgUp.png"));
        swordUpPic = ii.getScaledInstance(ii.getWidth(null)*2, ii.getHeight(null)*2, Image.SCALE_SMOOTH);
        ii = ImageIO.read(new File("powerUpSprites/projectilesUp.png"));
        projectileUpPic = ii.getScaledInstance(ii.getWidth(null)*2, ii.getHeight(null)*2, Image.SCALE_SMOOTH);
        ii = ImageIO.read(new File("powerUpSprites/healthUpgrade.png"));
        healthUpPic = ii.getScaledInstance(ii.getWidth(null)*2, ii.getHeight(null)*2, Image.SCALE_SMOOTH);
        if(!playerAbility.equalsIgnoreCase("sizeDown")){
            ii = ImageIO.read(new File("miscSprites/" + playerAbility + ".png"));
            abilityPic = ii.getScaledInstance(ii.getWidth(null), ii.getHeight(null), Image.SCALE_SMOOTH);
        }
        else{
            ii = ImageIO.read(new File("miscSprites/sizeDown(Light).png"));
            abilityPic = ii.getScaledInstance(ii.getWidth(null), ii.getHeight(null), Image.SCALE_SMOOTH);
        }


        //This loads in the first level
        levelInterpreter("LevelTemplates/intro.txt");

        //This creates the player
        player = new Player( S_WIDTH / 2, S_HEIGHT / 2 + 200, playerAbility);

        //This creates all of the interactable items on the level
        createInteractables();

        //This begins the timer
        timer = new Timer(DELAY, this);
        timer.start();
    }

    //The method creates the objects that the player can interact with throughout the game
    private void createInteractables() throws IOException{
        int jumpers;        //jumpers holds the number of "Jumper" enemies that will be on screen
        int blasters;       //blasters holds the number of "Blaster" enemies that will be on screen
        int randomExtras;   //randomExtras holds the number of random additional enemies

        //These if/else statements ensure that there will be more enemies at the later levels
        if((int)(stageLevel / 2) <= 0){
            randomExtras = 0;
        }
        else{
            randomExtras = rand.nextInt((int)(stageLevel / 2));
        }

        if(stageLevel == 0){
            enemiesOnScreen = 0;
        }
        else if(stageLevel < 5){
            enemiesOnScreen = 2 + randomExtras;
            jumpers = rand.nextInt(enemiesOnScreen);
            blasters = enemiesOnScreen - jumpers;

            for(int i = 0; i < jumpers; i++){
                createJumper("basic");
            }
            for(int i = 0; i < blasters; i++){
                createBlaster("basic");
            }
        }
        else if(stageLevel < 8){
            enemiesOnScreen = 3 + randomExtras;
            jumpers = rand.nextInt(enemiesOnScreen);
            blasters = enemiesOnScreen - jumpers;

            for(int i = 0; i < jumpers; i++){
                createJumper("basic");
            }
            for(int i = 0; i < blasters; i++){
                createBlaster("basic");
            }
        }
        else{
            enemiesOnScreen = 4 + randomExtras;
            jumpers = rand.nextInt(enemiesOnScreen);
            blasters = enemiesOnScreen - jumpers;

            for(int i = 0; i < jumpers; i++){
                createJumper("basic");
            }
            for(int i = 0; i < blasters; i++){
                createBlaster("basic");
            }
        }

        //This creates all of hte powerUps on screen
        if(stageLevel != 0){
            int powerUps = rand.nextInt(5);
            for(int i = 0; i < powerUps; i++){
                createPowerUp();
            }
        }
    }

    //This method randomly creates a "Jumper" enemy
    private void createJumper(String type) throws IOException{
        boolean success = false;
        ArrayList<ArrayList<Integer>> failCoordinates  = new ArrayList<ArrayList<Integer>>();
        int possibleX = 2;
        int possibleY = 2;

        //This while loop is used to ensure that the enemy is not created in a wall or on a void
        while(!success){
            possibleX = 2 + rand.nextInt(52);
            possibleY = 2 + rand.nextInt(27);
            ArrayList<Integer> testCoordinates = new ArrayList<>();
            testCoordinates.add(possibleX);
            testCoordinates.add(possibleY);

            if(!failCoordinates.contains(testCoordinates)){
                //This is a massive if statement to check if the enemy has tried to load on a wall or void
                if(!map[possibleY][possibleX].getType().equalsIgnoreCase("wall") && !map[possibleY][possibleX+1].getType().equalsIgnoreCase("wall") && !map[possibleY+1][possibleX].getType().equalsIgnoreCase("wall") && !map[possibleY][possibleX].getType().equalsIgnoreCase("void") && !map[possibleY][possibleX+1].getType().equalsIgnoreCase("void") && !map[possibleY + 1][possibleX].getType().equalsIgnoreCase("void")){
                    success = true;
                }
                else{
                    failCoordinates.add(testCoordinates);
                }
            }
        }

        jumpers.add(new Jumper(possibleX * 20 + 6,  (possibleY + 10) * 20 + 6, 75 + rand.nextInt(125), 5 + rand.nextInt(6), "normal"));
    }
    //This method randomly creates a "Blaster" enemy
    private void createBlaster(String type) throws IOException{
        boolean success = false;
        ArrayList<ArrayList<Integer>> failCoordinates  = new ArrayList<ArrayList<Integer>>();
        int possibleX = 2;
        int possibleY = 2;

        //This while loop is used to ensure that the enemy is not created in a wall or on a void
        while(!success){
            possibleX = 1 + rand.nextInt(52);
            possibleY = 1 + rand.nextInt(27);
            ArrayList<Integer> testCoordinates = new ArrayList<>();
            testCoordinates.add(possibleX);
            testCoordinates.add(possibleY);

            if(!failCoordinates.contains(testCoordinates)){
                //This is a massive if statement to check if the enemy has tried to load on a wall or void
                if(!map[possibleY][possibleX].getType().equalsIgnoreCase("wall") && !map[possibleY][possibleX+1].getType().equalsIgnoreCase("wall") && !map[possibleY+1][possibleX].getType().equalsIgnoreCase("wall") && !map[possibleY][possibleX].getType().equalsIgnoreCase("void") && !map[possibleY][possibleX+1].getType().equalsIgnoreCase("void") && !map[possibleY + 1][possibleX].getType().equalsIgnoreCase("void")){
                    success = true;
                }
                else{
                    failCoordinates.add(testCoordinates);
                }
            }
        }

        blasters.add(new Blaster(possibleX * 20 + 6,  (possibleY + 10) * 20 + 6, 50 + rand.nextInt(125), "normal"));
    }
    //This creates a random "powerUp"
    private void createPowerUp() throws IOException{
        boolean success = false;
        ArrayList<ArrayList<Integer>> failCoordinates  = new ArrayList<ArrayList<Integer>>();
        int possibleX = 2;
        int possibleY = 2;

        //This randomly generates which type of powerUp it will be
        String type;
        int randomType = rand.nextInt(4);
        if(randomType == 0){
            type = "healthUpgrade";
        }
        else if(randomType == 1){
            type = "restoreHealth";
        }
        else if(randomType == 2){
            type = "dmgUp";
        }
        else{
            type = "projectilesUp";
        }

        //This while loop is used to ensure that the powerUp is not created in a wall or on a void
        while(!success){
            possibleX = 1 + rand.nextInt(53);
            possibleY = 1 + rand.nextInt(28);
            ArrayList<Integer> testCoordinates = new ArrayList<>();
            testCoordinates.add(possibleX);
            testCoordinates.add(possibleY);

            if(!failCoordinates.contains(testCoordinates)){
                //This is a massive if statement to check if the powerUp has tried to load on a wall or void
                if(!map[possibleY][possibleX].getType().equalsIgnoreCase("wall") && !map[possibleY][possibleX].getType().equalsIgnoreCase("void")){
                    success = true;
                }
                else{
                    failCoordinates.add(testCoordinates);
                }
            }
        }

        powerUps.add(new PowerUp(possibleX * 20,  (possibleY + 10) * 20, type));
    }

    //This method moves all projectiles and removes the projectiles that are offscreen
    private void moveProjectiles(){
        ArrayList<Projectile> removes = new ArrayList<Projectile>();

        for(Projectile p : projectiles){
            p.move();
            if(!p.getHitbox().intersects(screenBounds)){
                removes.add(p);
            }
        }
        for(Projectile p : removes){
            projectiles.remove(p);
        }
    }

    //This method takes the projectiles created by the player and by any "blaster" enemies and saves them separately
    private void getNewProjectiles(){
        ArrayList<Projectile> addedProjectiles = new ArrayList<Projectile>();

        //Add player projectiles
        for(Projectile p : player.getProjectiles()){
            if(!projectiles.contains(p)){
                projectiles.add(p);
                addedProjectiles.add(p);
            }
        }
        for(Projectile p : addedProjectiles){
            player.removeProjectile(p);
        }
        addedProjectiles.clear();

        //Add Blaster projectiles
        for(Blaster b : blasters){
            for(Projectile p : b.getProjectiles()){
                if(!projectiles.contains(p)){
                    projectiles.add(p);
                    addedProjectiles.add(p);
                }
            }
            for(Projectile p : addedProjectiles){
                b.removeProjectile(p);
            }
        }
    }

    //This method checks for collisions between all majour interactable classes
    private void checkCollisions(){
        //Jumper Colisions with the player
        for(Jumper d : jumpers){
            if(d.getHitbox().intersects(player.getHitbox()) && !d.getHit()){
                player.takeDamage(d.getAtkDmg());
                d.setHit(true);
                player.clearPowerUps();
            }
        }

        //These arrayLists hold objects that are to be deleted
        ArrayList<Jumper> jumperRemoves = new ArrayList<Jumper>();
        ArrayList<Blaster> blasterRemoves = new ArrayList<Blaster>();
        ArrayList<Projectile> projectileRemoves = new ArrayList<Projectile>();
        //Projectile collisions with the player and enemies
        for(Projectile p: projectiles){
            if(p.getType().equalsIgnoreCase("player")){
                for(Jumper j : jumpers){
                    if(p.getHitbox().intersects(j.getHitbox())){
                        j.takeDmg(p.getDmg());
                        projectileRemoves.add(p);
                    }
                    if(j.getHealth() <= 0){
                        score += 50;
                        jumperRemoves.add(j);
                    }
                }
                for(Blaster b : blasters){
                    if(p.getHitbox().intersects(b.getHitbox())){
                        b.takeDmg(p.getDmg());
                        projectileRemoves.add(p);
                    }
                    if(b.getHealth() <= 0){
                        score += 50;
                        blasterRemoves.add(b);
                    }
                }
            }
            else{
                if(p.getHitbox().intersects(player.getHitbox())){
                    player.takeDamage(p.getDmg());
                    projectileRemoves.add(p);
                    player.clearPowerUps();
                }
            }
        }
        for(Projectile p : projectileRemoves){
            projectiles.remove(p);
        }
        for(Jumper j : jumperRemoves){
            jumpers.remove(j);
        }
        for(Blaster b : blasterRemoves){
            blasters.remove(b);
        }

        //This holds powerUps that are to be removed
        ArrayList<PowerUp> powerUpRemoves = new ArrayList<PowerUp>();
        //PowerUp collisions with the player
        for(PowerUp p : powerUps){
            if(player.getHitbox().intersects(p.getHitbox())){
                if(!player.getPowerUps().contains(p.getType())){
                    if(p.getType().equalsIgnoreCase("healthUpgrade") && player.getHealth() == (player.getOGhealth()+10)){
                        score += 100;
                    }
                    if(p.getType().equalsIgnoreCase("healthRestore") && (player.getHealth() == player.getOGhealth() || player.getHealth() == player.getOGhealth()+10)){
                        score += 100;
                    }

                    pickUpSound.play();
                    player.addPowerUp(p.getType());
                    powerUpRemoves.add(p);
                }
                else{
                    score += 100;
                    powerUpRemoves.add(p);
                }
            }
        }
        for(PowerUp p : powerUpRemoves){
            powerUps.remove(p);
        }

        //Tiles collisions with "Jumper" enemies and the player
        //playerFeet is used to hold where the player's feet would be
        for(Tile t : tiles){
            if(t.getType().equalsIgnoreCase("wall")){
                if(player.getHitbox().intersects(t.getLeft()) || player.getHitbox().intersects(t.getRight())){
                    player.setX(player.getPreviousX());
                }
                if(player.getHitbox().intersects(t.getTop()) || player.getHitbox().intersects(t.getBottom())){
                    player.setY(player.getPreviousY());
                }

                for(Jumper j : jumpers){
                    Rectangle jumperWallCheck = new Rectangle(j.getX() - 5, j.getY() - 5, j.getWidth() + 10, j.getHeight() + 10);
                    if(jumperWallCheck.intersects(t.getRight()) || jumperWallCheck.intersects(t.getLeft())){
                        j.setX(j.getPreviousX());
                        j.hitObstacle();
                    }
                    if(jumperWallCheck.intersects(t.getTop()) || jumperWallCheck.intersects(t.getBottom())){
                        j.setY(j.getPreviousY());
                        j.hitObstacle();
                    }
                }
            }
            else if(t.getType().equalsIgnoreCase("void")){
                if(player.getFeet().intersects(t.getHitbox())){
                    player.setFalling(true);
                }
            }
            else if(t.getType().equalsIgnoreCase("carpet")){
                if(player.getFeet().intersects(t.getHitbox()) && enemiesOnScreen <= 0){
                    if(stageLevel+1 == 10 && gameMode.equalsIgnoreCase("bossrush")){
                        finalLevel = true;
                    }

                    boolean newLevel = true;
                    if(player.getY() <= 300 && enterFrom.equalsIgnoreCase("top")){
                        newLevel = false;
                    }
                    else if(player.getY() >= 700 && enterFrom.equalsIgnoreCase("bottom")){
                        newLevel = false;
                    }
                    if(player.getX() <= 100 && enterFrom.equalsIgnoreCase("left")){
                        newLevel = false;
                    }
                    else if(player.getX() >= 1000 && enterFrom.equalsIgnoreCase("right")){
                        newLevel = false;
                    }

                    if(newLevel){
                        changeLevel = true;
                        score += 1000;
                    }
                    else{
                        changeLevel = false;
                    }
                }
            }
        }
    }

    //This method loads a new level from a group of level templates
    private void levelInterpreter(String levelText) throws IOException{
        Scanner levelFile = new Scanner(new File(levelText));
        String[][] tiles = new String[30][55];
        map = new Tile[30][55];
        int count1 = 0;

        //This reads the file
        while (levelFile.hasNextLine()){
            int count2 = 0;
            for(String s : levelFile.nextLine().split("")){
                tiles[count1][count2] = s;
                count2++;
            }
            count1++;
        }

        //This interprets each letter as a Tile object
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[i].length; j++){
                String s = tiles[i][j];
                String type;
                if(s.equals("w")){
                    type = "wall";
                }
                else if(s.equals("f")){
                    type = "floor";
                }
                else if(s.equals("v")){
                    type = "void";
                }
                else if(s.equals("c")){
                    type = "carpet";
                }
                else{
                    type = "Placeholder";
                }

                this.tiles.add(new Tile(20*j, 200 + 20*i, type));
                map[i][j] = new Tile(20*j, 200 + 20*i, type);
            }
        }
    }

    //This method randomly generates an new level
    private void loadNewLevel() throws IOException{
        stageLevel++;       //This advances the level number

        tiles.clear();      //This clears the previous level
        powerUps.clear();   //This clears the previous powerUps

        //This randomly chooses a level
        String levelName = "LevelTemplates/" + rand.nextInt(8) + ".txt";
        levelInterpreter(levelName);

        //This randomly populates the level
        createInteractables();

        changeLevel = false;

        //These if statements change the players position depending on the exit they left from
        //This simulates the player moving from room to room
        if(player.getY() <= 300){
            player.setY(720);
            enterFrom = "bottom";
        }
        else if(player.getY() >= 700){
            player.setY(240);
            enterFrom = "top";
        }
        if(player.getX() <= 100){
            player.setX(1020);
            enterFrom = "right";
        }
        else if(player.getX() >= 1000){
            player.setX(40);
            enterFrom = "left";
        }
    }

    //The method loads the final level in the "Boss Rush" game-mode
    private void loadBossLevel() throws IOException{
        changeLevel = false;
        stageLevel++;

        tiles.clear();
        powerUps.clear();

        levelInterpreter("LevelTemplates/intro.txt");
        if(rand.nextInt(2) == 0){
            blasters.add(new Blaster(500, 400, 50, "boss"));
        }
        else{
            jumpers.add(new Jumper(500, 400, 50, 10, "boss"));
        }

        enemiesOnScreen = 1;

        //These if statements change the players position depending on the exit they left from
        //This simulates the player moving from room to room
        if(player.getY() <= 300){
            player.setY(720);
            enterFrom = "bottom";
        }
        else if(player.getY() >= 700){
            player.setY(240);
            enterFrom = "top";
        }
        if(player.getX() <= 100){
            player.setX(1020);
            enterFrom = "right";
        }
        else if(player.getX() >= 1000){
            player.setX(40);
            enterFrom = "left";
        }
    }

    //This method clears all game data
    private void clear(){
        tiles.clear();
        powerUps.clear();
        blasters.clear();
        jumpers.clear();
    }

    //This updates the game
    @Override
    public void actionPerformed(ActionEvent e){
        requestFocus();                     //this gets the computer to listen for input

        player.move();                      //this moves and animates the player

        //this moves and animates each "jumper" enemy
        for(Jumper d : jumpers){
            d.action(player.getHitbox());
        }

        //this moves and animates each "blaster" enemy
        for(Blaster b : blasters){
            b.action(player.getHitbox());
        }

        //This gets and moves all projectiles
        getNewProjectiles();
        moveProjectiles();

        enemiesOnScreen = jumpers.size() + blasters.size();
        if(enemiesOnScreen > 0) {
            firstLevel = false;
        }

        //This checks for collisions
        checkCollisions();

        //If necessary this loads an new level
        try{
            if(changeLevel && !firstLevel && !finalLevel){
                loadNewLevel();
            }
            else if(changeLevel && firstLevel && !finalLevel){
                loadNewLevel();
            }
            else if(changeLevel && !firstLevel && finalLevel){
                loadBossLevel();
            }
        }
        catch(Exception ex){}

        //This determines whether or not the player is still alive
        if(player.getHealth() <= 0 && alive){
            music.togglePause();
            running = false;
            alive = false;
            clear();
        }

        //This determines whether or not the player has beaten the "Boss Rush" game-mode
        if(finalLevel && enemiesOnScreen == 0 && stageLevel == 10){
            bossDead = true;
            music.togglePause();
            clear();
        }

        repaint();
    }

    //This method passes key input into the "player" class
    private class IOAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent k) {
            try{
                player.KeyPressed(k);
            }
            catch (Exception e){}
        }
        @Override
        public void keyReleased(KeyEvent k) {
            player.keyReleased(k);
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        //This draws the level on screen
        for(Tile t : tiles){
            g.drawImage(t.getTile(), t.getX(), t.getY(), this);
        }

        //This draws background for the status bar on the top
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, S_WIDTH, 200);

        //This displays the player's health in a two tiered heart-bar
        int topHearts;
        int botHearts;
        if(player.getHealth() > 10){
            topHearts = 10;
            botHearts = player.getHealth() - 10;
        }
        else{
            topHearts = player.getHealth();
            botHearts = 0;
        }
        for(int i = 0; i < topHearts; i++){
            g.drawImage(heartPic, 20 + i*25, 20, this);
        }
        for(int i = 0; i < botHearts; i++){
            g.drawImage(heartPic, 20 + i*25, 45, this);
        }

        //These display level data that the player may find useful
        g.setColor(Color.WHITE);
        Font bitText = new Font("8BIT WONDER", 0, 20);
        g.setFont(bitText);
        g.drawString("Score ", 850, 35);
        g.drawString(Integer.toString(score), 960, 35);
        g.drawString("Level " + Integer.toString(stageLevel), 500, 40);
        g.drawString("Enemies " + Integer.toString(enemiesOnScreen), 480, 70);

        g.drawImage(abilityPic, 850, 60, this);

        //These display whether or not the player has each powerUp
        if(player.getPowerUps().contains("dmgUp")){
            g.drawImage(swordUpPic,20, 100, this);
        }
        if(player.getPowerUps().contains("projectilesUp")){
            g.drawImage(projectileUpPic,70, 100, this);
        }
        if(player.getHealth() > player.getOGhealth()){
            g.drawImage(healthUpPic,120, 100, this);
        }

        //This draws powerUps on screen
        for(PowerUp p : powerUps){
            g.drawImage(p.getCurrentSprite(), p.getX(), p.getY(), this);
        }

        //This draws the player on screen
        g.drawImage(player.getCurrentSprite(), player.getX(), player.getY(), this);

        //This draws each "jumper" enemy on screen
        for(Jumper d : jumpers){
            g.drawImage(d.getCurrentSprite(), d.getX(), d.getY(), this);
        }

        //This draws each "blaster" enemy on screen
        for(Blaster b : blasters){
            g.drawImage(b.getCurrentSprite(), b.getX(), b.getY(), this);
        }

        //This draws each projectile on screen
        for(Projectile p : projectiles){
            g.drawImage(p.getImage(), p.getX(), p.getY(), this);
        }
    }

    //These are the accessor methods
    public boolean getRunning(){
        return running;
    }
    public boolean getBossDead(){
        return bossDead;
    }
    public int getScore(){
        return score;
    }

    //These are setter methods
    public void setRunning(boolean r){
        this.running = r;
    }
    public void setBossDead(boolean b){
        this.bossDead = b;
    }
}
