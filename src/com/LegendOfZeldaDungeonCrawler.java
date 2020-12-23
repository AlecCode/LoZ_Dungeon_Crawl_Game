package com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.io.*;

public class LegendOfZeldaDungeonCrawler extends JFrame implements ActionListener, Commons{

    javax.swing.Timer timer;                //timer holds the Timer that updates the program
    final private int DELAY = 15;           //DELAY holds the time between refreshes in the "timer" Timer

    JPanel cards;                           //cards is used to hold each menu
    CardLayout cLayout = new CardLayout();  //cLayout is used to displaye each menu

    private Audio music;                    //music holds the music that plays during the menus

    //These are all menus
    private JLayeredPane mainMenu = new JLayeredPane();
    private JLayeredPane instructionMenu = new JLayeredPane();
    private JLayeredPane highScoresMenu = new JLayeredPane();
    private JLayeredPane abilityMenu = new JLayeredPane();
    private JLayeredPane winMenu = new JLayeredPane();
    private JLayeredPane gameOverMenu = new JLayeredPane();

    //These are all buttons used to navigate the menus
    private JButton dungeonDashButton;
    private JButton bossRushButton;
    private JButton instructionsButton;
    private JButton highScoresButton;
    private JButton backFromIntroButton;
    private JButton backFromHighButton;
    private JButton confirmWinSubmissionButton;
    private JButton confirmLoseSubmissionButton;
    private JButton defenseUpButton;
    private JButton speedUpButton;
    private JButton sizeDownButton;

    private JTextArea nameIn;               //nameIn holds a text box that the user uses to save their score

    //These hold names and their associated highscores
    private ArrayList<String> dungeonDashHighNames = new ArrayList<String>();
    private ArrayList<Integer> dungeonDashHighscore = new ArrayList<Integer>();
    private ArrayList<String> bossRushHighNames = new ArrayList<String>();
    private ArrayList<Integer> bossRushHighscore = new ArrayList<Integer>();

    private inGame game;                    //game holds the actual game itself
    private String gameMode;                //gameMode holds the game-mode selcted by the player (Dungeon Dash or Boss Rush)

    public static void main(String[] args) throws IOException, FontFormatException{
        LegendOfZeldaDungeonCrawler game = new LegendOfZeldaDungeonCrawler();
    }

    private LegendOfZeldaDungeonCrawler() throws IOException, FontFormatException{
        super("The Legend of Zelda: Dungeon Master");   //This changes the title of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(S_WIDTH + 6, S_HEIGHT + 35);     //This sets the size of the screen
                                                             //Note: the +6 and +35 are needed in order to offset a wrapping issue
                                                             //that occurs when switching between the menus and the game itself
        setLocationRelativeTo(null);

        //This loads and plays music on the main menu
        music = new Audio("Sounds/MainMenuMusic.wav");
        music.loop();

        //This starts the "timer" Timer
        timer = new javax.swing.Timer(DELAY, this);

        mainMenu.setLayout(null);

        //This loads in the custom 8-bit font
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("8-BIT WONDER.ttf")));

        //These load all the menus
        loadButtons();
        loadMainMenu();
        loadAbilityMenu();
        loadInstructionsMenu();
        loadHighScoreMenu();
        loadGameOverMenu();
        loadWinMenu();

        //These add all the menus to "cards" to be displayed later
        this.cards = new JPanel(cLayout);
        this.cards.add(mainMenu, "mainMenu");
        this.cards.add(highScoresMenu, "highscores");
        this.cards.add(instructionMenu, "instructions");
        this.cards.add(gameOverMenu, "gameover");
        this.cards.add(winMenu, "win");
        this.cards.add(abilityMenu, "abilities");
        this.add(cards);

        requestFocus();
        setResizable(false);
        setVisible(true);
    }

    //This method loads all of the buttons used
    private void loadButtons(){
        this.dungeonDashButton = new JButton(new ImageIcon("miscSprites/DungeonDashText.png"));
        this.dungeonDashButton.addActionListener(this);
        this.dungeonDashButton.setSize(335,30);
        this.dungeonDashButton.setLocation((S_WIDTH/2) - 168,420);
        this.dungeonDashButton.setContentAreaFilled(false);
        this.dungeonDashButton.setFocusPainted(false);
        this.dungeonDashButton.setBorderPainted(false);

        this.bossRushButton = new JButton(new ImageIcon("miscSprites/BossRushText.png"));
        this.bossRushButton.addActionListener(this);
        this.bossRushButton.setSize(249,33);
        this.bossRushButton.setLocation((S_WIDTH/2) - 125,460);
        this.bossRushButton.setContentAreaFilled(false);
        this.bossRushButton.setFocusPainted(false);
        this.bossRushButton.setBorderPainted(false);

        this.instructionsButton = new JButton(new ImageIcon("miscSprites/IntroText.png"));
        this.instructionsButton.addActionListener(this);
        this.instructionsButton.setSize(317,27);
        this.instructionsButton.setLocation((S_WIDTH/2) - 159,540);
        this.instructionsButton.setContentAreaFilled(false);
        this.instructionsButton.setFocusPainted(false);
        this.instructionsButton.setBorderPainted(false);

        this.highScoresButton = new JButton(new ImageIcon("miscSprites/HighScoresText.png"));
        this.highScoresButton.addActionListener(this);
        this.highScoresButton.setSize(290,29);
        this.highScoresButton.setLocation((S_WIDTH/2) - 145,580);
        this.highScoresButton.setContentAreaFilled(false);
        this.highScoresButton.setFocusPainted(false);
        this.highScoresButton.setBorderPainted(false);

        this.backFromIntroButton = new JButton(new ImageIcon("miscSprites/BackText.png"));
        this.backFromIntroButton.addActionListener(this);
        this.backFromIntroButton.setSize(112,27);
        this.backFromIntroButton.setLocation(50,700);
        this.backFromIntroButton.setContentAreaFilled(false);
        this.backFromIntroButton.setFocusPainted(false);
        this.backFromIntroButton.setBorderPainted(false);

        this.backFromHighButton = new JButton(new ImageIcon("miscSprites/BackText.png"));
        this.backFromHighButton.addActionListener(this);
        this.backFromHighButton.setSize(112,27);
        this.backFromHighButton.setLocation(50,700);
        this.backFromHighButton.setContentAreaFilled(false);
        this.backFromHighButton.setFocusPainted(false);
        this.backFromHighButton.setBorderPainted(false);

        this.confirmLoseSubmissionButton = new JButton(new ImageIcon("miscSprites/EnterText.png"));
        this.confirmLoseSubmissionButton.addActionListener(this);
        this.confirmLoseSubmissionButton.setSize(295,61);
        this.confirmLoseSubmissionButton.setLocation(403,600);
        this.confirmLoseSubmissionButton.setContentAreaFilled(false);
        this.confirmLoseSubmissionButton.setFocusPainted(false);
        this.confirmLoseSubmissionButton.setBorderPainted(false);

        this.confirmWinSubmissionButton = new JButton(new ImageIcon("miscSprites/EnterText.png"));
        this.confirmWinSubmissionButton.addActionListener(this);
        this.confirmWinSubmissionButton.setSize(295,61);
        this.confirmWinSubmissionButton.setLocation(403,600);
        this.confirmWinSubmissionButton.setContentAreaFilled(false);
        this.confirmWinSubmissionButton.setFocusPainted(false);
        this.confirmWinSubmissionButton.setBorderPainted(false);

        this.defenseUpButton = new JButton(new ImageIcon("miscSprites/defenseUp.png"));
        this.defenseUpButton.addActionListener(this);
        this.defenseUpButton.setSize(48,51);
        this.defenseUpButton.setLocation(251,400);
        this.defenseUpButton.setContentAreaFilled(false);
        this.defenseUpButton.setFocusPainted(false);
        this.defenseUpButton.setBorderPainted(false);

        this.speedUpButton = new JButton(new ImageIcon("miscSprites/speedUp.png"));
        this.speedUpButton.addActionListener(this);
        this.speedUpButton.setSize(48,42);
        this.speedUpButton.setLocation(526,400);
        this.speedUpButton.setContentAreaFilled(false);
        this.speedUpButton.setFocusPainted(false);
        this.speedUpButton.setBorderPainted(false);

        this.sizeDownButton = new JButton(new ImageIcon("miscSprites/sizeDown.png"));
        this.sizeDownButton.addActionListener(this);
        this.sizeDownButton.setSize(42,48);
        this.sizeDownButton.setLocation(801,400);
        this.sizeDownButton.setContentAreaFilled(false);
        this.sizeDownButton.setFocusPainted(false);
        this.sizeDownButton.setBorderPainted(false);
    }

    //This method loads the main menu of the game
    private void loadMainMenu(){
        JLabel backGround = new JLabel(new ImageIcon("miscSprites/MenuBackGround.png"));
        backGround.setSize(1920,1080);;
        backGround.setLocation(0,-100);

        JLabel DD_desc = new JLabel("Dungeon Dash is an endless dungeon crawler game");
        DD_desc.setFont(new Font("8BIT WONDER", Font.PLAIN, 15));
        DD_desc.setSize(1000,100);
        DD_desc.setLocation(40,700);
        DD_desc.setForeground(Color.WHITE);

        JLabel BR_desc = new JLabel("Boss Rush is a ten level then boss fight game");
        BR_desc.setFont(new Font("8BIT WONDER", Font.PLAIN, 15));
        BR_desc.setSize(1000,100);
        BR_desc.setLocation(40,720);
        BR_desc.setForeground(Color.WHITE);

        JLabel logo = new JLabel(new ImageIcon("miscSprites/Logo.png"));
        logo.setSize(670, 381);
        logo.setLocation((S_WIDTH/2) - 335, 20);

        this.mainMenu.add(DD_desc, 1);
        this.mainMenu.add(BR_desc, 1);
        this.mainMenu.add(logo, 2);
        this.mainMenu.add(backGround,10);
        this.mainMenu.add(dungeonDashButton,1);
        this.mainMenu.add(bossRushButton,1);
        this.mainMenu.add(instructionsButton,1);
        this.mainMenu.add(highScoresButton,1);
    }

    //This method loads the HighScore menu
    private void loadHighScoreMenu(){
        JLabel backGround = new JLabel(new ImageIcon("miscSprites/MenuBackGround.png"));
        backGround.setSize(1920,1080);
        backGround.setLocation(0,-100);

        this.highScoresMenu.add(backFromHighButton, 1);
        this.highScoresMenu.add(backGround, 2);

        loadHighScores();

        //This loads the Dungeon Dash portion of the HighScores
        JLabel dungeonDashText = new JLabel("Dungeon Dash");
        dungeonDashText.setFont(new Font("8BIT WONDER", Font.PLAIN, 30));
        dungeonDashText.setSize(400,100);
        dungeonDashText.setLocation(200,20);
        dungeonDashText.setForeground(Color.WHITE);
        this.highScoresMenu.add(dungeonDashText, 1);

        JLabel DD_highScoresText = new JLabel("High Scores");
        DD_highScoresText.setFont(new Font("8BIT WONDER", Font.PLAIN, 30));
        DD_highScoresText.setSize(400,100);
        DD_highScoresText.setLocation(200,60);
        DD_highScoresText.setForeground(Color.WHITE);
        this.highScoresMenu.add(DD_highScoresText, 1);

        JLabel DD_name1 = new JLabel("1 " + dungeonDashHighNames.get(0));
        DD_name1.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        DD_name1.setSize(400,100);
        DD_name1.setLocation(200,110);
        DD_name1.setForeground(Color.WHITE);
        this.highScoresMenu.add(DD_name1,1);
        JLabel DD_score1 = new JLabel("   " + Integer.toString(dungeonDashHighscore.get(0)));
        DD_score1.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        DD_score1.setSize(400,100);
        DD_score1.setLocation(200,150);
        DD_score1.setForeground(Color.WHITE);
        this.highScoresMenu.add(DD_score1,1);

        JLabel DD_name2 = new JLabel("2 " + dungeonDashHighNames.get(1));
        DD_name2.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        DD_name2.setSize(400,100);
        DD_name2.setLocation(200,230);
        DD_name2.setForeground(Color.WHITE);
        this.highScoresMenu.add(DD_name2,1);
        JLabel DD_score2 = new JLabel("   " + Integer.toString(dungeonDashHighscore.get(1)));
        DD_score2.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        DD_score2.setSize(400,100);
        DD_score2.setLocation(200,270);
        DD_score2.setForeground(Color.WHITE);
        this.highScoresMenu.add(DD_score2,1);

        JLabel DD_name3 = new JLabel("3 " + dungeonDashHighNames.get(2));
        DD_name3.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        DD_name3.setSize(400,100);
        DD_name3.setLocation(200,350);
        DD_name3.setForeground(Color.WHITE);
        this.highScoresMenu.add(DD_name3,1);
        JLabel DD_score3 = new JLabel("   " + Integer.toString(dungeonDashHighscore.get(2)));
        DD_score3.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        DD_score3.setSize(300,100);
        DD_score3.setLocation(200,390);
        DD_score3.setForeground(Color.WHITE);
        this.highScoresMenu.add(DD_score3,1);

        JLabel DD_name4 = new JLabel("4 " + dungeonDashHighNames.get(3));
        DD_name4.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        DD_name4.setSize(400,100);
        DD_name4.setLocation(200,470);
        DD_name4.setForeground(Color.WHITE);
        this.highScoresMenu.add(DD_name4,1);
        JLabel DD_score4 = new JLabel("   " + Integer.toString(dungeonDashHighscore.get(3)));
        DD_score4.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        DD_score4.setSize(400,100);
        DD_score4.setLocation(200,510);
        DD_score4.setForeground(Color.WHITE);
        this.highScoresMenu.add(DD_score4,1);

        JLabel DD_name5 = new JLabel("5 " + dungeonDashHighNames.get(4));
        DD_name5.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        DD_name5.setSize(400,100);
        DD_name5.setLocation(200,570);
        DD_name5.setForeground(Color.WHITE);
        this.highScoresMenu.add(DD_name5,1);
        JLabel DD_score5 = new JLabel("   " + Integer.toString(dungeonDashHighscore.get(4)));
        DD_score5.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        DD_score5.setSize(400,100);
        DD_score5.setLocation(200,610);
        DD_score5.setForeground(Color.WHITE);
        this.highScoresMenu.add(DD_score5,1);

        //This loads the Boss Rush portion of the HighScores
        JLabel bossRushText = new JLabel("Boss Rush");
        bossRushText.setFont(new Font("8BIT WONDER", Font.PLAIN, 30));
        bossRushText.setSize(400,100);
        bossRushText.setLocation(600,20);
        bossRushText.setForeground(Color.WHITE);
        this.highScoresMenu.add(bossRushText, 1);

        JLabel BR_highScoresText = new JLabel("High Scores");
        BR_highScoresText.setFont(new Font("8BIT WONDER", Font.PLAIN, 30));
        BR_highScoresText.setSize(400,100);
        BR_highScoresText.setLocation(600,60);
        BR_highScoresText.setForeground(Color.WHITE);
        this.highScoresMenu.add(BR_highScoresText, 1);

        JLabel BR_name1 = new JLabel("1 " + bossRushHighNames.get(0));
        BR_name1.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        BR_name1.setSize(400,100);
        BR_name1.setLocation(600,110);
        BR_name1.setForeground(Color.WHITE);
        this.highScoresMenu.add(BR_name1,1);
        JLabel BR_score1 = new JLabel("   " + Integer.toString(bossRushHighscore.get(0)));
        BR_score1.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        BR_score1.setSize(400,100);
        BR_score1.setLocation(600,150);
        BR_score1.setForeground(Color.WHITE);
        this.highScoresMenu.add(BR_score1,1);

        JLabel BR_name2 = new JLabel("2 " + bossRushHighNames.get(1));
        BR_name2.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        BR_name2.setSize(400,100);
        BR_name2.setLocation(600,230);
        BR_name2.setForeground(Color.WHITE);
        this.highScoresMenu.add(BR_name2,1);
        JLabel BR_score2 = new JLabel("   " + Integer.toString(bossRushHighscore.get(1)));
        BR_score2.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        BR_score2.setSize(400,100);
        BR_score2.setLocation(600,270);
        BR_score2.setForeground(Color.WHITE);
        this.highScoresMenu.add(BR_score2,1);

        JLabel BR_name3 = new JLabel("3 " + bossRushHighNames.get(2));
        BR_name3.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        BR_name3.setSize(400,100);
        BR_name3.setLocation(600,350);
        BR_name3.setForeground(Color.WHITE);
        this.highScoresMenu.add(BR_name3,1);
        JLabel BR_score3 = new JLabel("   " + Integer.toString(bossRushHighscore.get(2)));
        BR_score3.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        BR_score3.setSize(300,100);
        BR_score3.setLocation(600,390);
        BR_score3.setForeground(Color.WHITE);
        this.highScoresMenu.add(BR_score3,1);

        JLabel BR_name4 = new JLabel("4 " + bossRushHighNames.get(3));
        BR_name4.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        BR_name4.setSize(400,100);
        BR_name4.setLocation(600,470);
        BR_name4.setForeground(Color.WHITE);
        this.highScoresMenu.add(BR_name4,1);
        JLabel BR_score4 = new JLabel("   " + Integer.toString(bossRushHighscore.get(3)));
        BR_score4.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        BR_score4.setSize(400,100);
        BR_score4.setLocation(600,510);
        BR_score4.setForeground(Color.WHITE);
        this.highScoresMenu.add(BR_score4,1);

        JLabel BR_name5 = new JLabel("5 " + bossRushHighNames.get(4));
        BR_name5.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        BR_name5.setSize(400,100);
        BR_name5.setLocation(600,570);
        BR_name5.setForeground(Color.WHITE);
        this.highScoresMenu.add(BR_name5,1);
        JLabel BBR_score5 = new JLabel("   " + Integer.toString(bossRushHighscore.get(4)));
        BBR_score5.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        BBR_score5.setSize(400,100);
        BBR_score5.setLocation(600,610);
        BBR_score5.setForeground(Color.WHITE);
        this.highScoresMenu.add(BBR_score5,1);
    }

    //This method loads the high-scores and the associated names
    private void loadHighScores(){
        try { //Reading Dungeon Dash txt File
            BufferedReader in = new BufferedReader(new FileReader("DungeonDashHighScores.txt"));
            String line;
            while((line = in.readLine()) != null){              //reads a line if there are values
                String[] parts = line.split(" ");          //each line will be an array in the form of [string, int]
                this.dungeonDashHighNames.add(parts[0]);
                this.dungeonDashHighscore.add(Integer.parseInt(parts[1])); //when reading the file, order doesnt matter since the scores/names in the txt or in order when written
            }
        }
        catch (IOException e){ //If there is not enough information found this fills the "highNames" and "highscore" Arraylists with filler information
            for(int i = 0; i < 5; i++){
                this.dungeonDashHighNames.add("?????");
                this.dungeonDashHighscore.add(0);
            }
        }

        try { //Reading the Boss Rush txt file
            BufferedReader in = new BufferedReader(new FileReader("BossRushHighScores.txt"));
            String line;
            while((line = in.readLine()) != null){                      //reads a line if there are values
                String[] parts = line.split(" ");                 //each line will be an array in the form of [string, int]
                this.bossRushHighNames.add(parts[0]);
                this.bossRushHighscore.add(Integer.parseInt(parts[1])); //when reading the file, order doesnt matter since the scores/names in the txt or in order when written
            }
        }catch (IOException e){ //If there is not enough information found this fills the "highNames" and "highscore" Arraylists with filler information
            for(int i = 0; i < 5; i++){
                this.bossRushHighNames.add("?????");
                this.bossRushHighscore.add(0);
            }
        }
    }

    //This loads the instructions menu
    private void loadInstructionsMenu(){
        JLabel backGround = new JLabel(new ImageIcon("miscSprites/MenuBackGround.png"));
        backGround.setSize(1920,1080);
        backGround.setLocation(0,-100);

        JLabel instructionsText = new JLabel("Instructions");
        instructionsText.setFont(new Font("8BIT WONDER", Font.PLAIN, 40));
        instructionsText.setSize(1000,100);
        instructionsText.setLocation(60,100);
        instructionsText.setForeground(Color.WHITE);

        JLabel instruc1 = new JLabel("Use the arrow keys to move");
        instruc1.setFont(new Font("8BIT WONDER", Font.PLAIN, 30));
        instruc1.setSize(1000,100);
        instruc1.setLocation(60,250);
        instruc1.setForeground(Color.WHITE);

        JLabel instruc2 = new JLabel("Use the spacebar to attack");
        instruc2.setFont(new Font("8BIT WONDER", Font.PLAIN, 30));
        instruc2.setSize(1000,100);
        instruc2.setLocation(60,350);
        instruc2.setForeground(Color.WHITE);

        JLabel instruc3 = new JLabel("Clear the level of enemies");
        instruc3.setFont(new Font("8BIT WONDER", Font.PLAIN, 30));
        instruc3.setSize(1100,100);
        instruc3.setLocation(60,450);
        instruc3.setForeground(Color.WHITE);

        JLabel instruc4 = new JLabel("to advance");
        instruc4.setFont(new Font("8BIT WONDER", Font.PLAIN, 30));
        instruc4.setSize(1000,100);
        instruc4.setLocation(60,500);
        instruc4.setForeground(Color.WHITE);

        JLabel instruc5 = new JLabel("Warning you can fall off the levels");
        instruc5.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        instruc5.setSize(1000,100);
        instruc5.setLocation(60,550);
        instruc5.setForeground(Color.WHITE);


        this.instructionMenu.add(backFromIntroButton, 1);
        this.instructionMenu.add(instructionsText, 1);
        this.instructionMenu.add(instruc1, 1);
        this.instructionMenu.add(instruc2, 1);
        this.instructionMenu.add(instruc3, 1);
        this.instructionMenu.add(instruc4, 1);
        this.instructionMenu.add(instruc5, 1);
        this.instructionMenu.add(backGround, 20);
    }

    //This loads the abilities menu
    private void loadAbilityMenu(){
        JLabel backGround = new JLabel(new ImageIcon("miscSprites/MenuBackGround.png"));
        backGround.setSize(1920,1080);
        backGround.setLocation(0,-100);

        JLabel chooseText1 = new JLabel("choose");
        chooseText1.setFont(new Font("8BIT WONDER", Font.PLAIN, 40));
        chooseText1.setSize(390,100);
        chooseText1.setLocation(250,100);
        chooseText1.setForeground(Color.WHITE);
        JLabel chooseText2 = new JLabel("an ability");
        chooseText2.setFont(new Font("8BIT WONDER", Font.PLAIN, 40));
        chooseText2.setSize(390,100);
        chooseText2.setLocation(500,100);
        chooseText2.setForeground(Color.WHITE);

        JLabel defenseUpDesc1 = new JLabel("Take half");
        defenseUpDesc1.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        defenseUpDesc1.setSize(390,100);
        defenseUpDesc1.setLocation(191,450);
        defenseUpDesc1.setForeground(Color.WHITE);
        JLabel defenseUpDesc2 = new JLabel("damage");
        defenseUpDesc2.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        defenseUpDesc2.setSize(390,100);
        defenseUpDesc2.setLocation(211,500);
        defenseUpDesc2.setForeground(Color.WHITE);

        JLabel speedUpDesc1 = new JLabel("Move at a");
        speedUpDesc1.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        speedUpDesc1.setSize(390,100);
        speedUpDesc1.setLocation(466,450);
        speedUpDesc1.setForeground(Color.WHITE);
        JLabel speedUpDesc2 = new JLabel("higher speed");
        speedUpDesc2.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        speedUpDesc2.setSize(390,100);
        speedUpDesc2.setLocation(446,500);
        speedUpDesc2.setForeground(Color.WHITE);

        JLabel sizeDownDesc1 = new JLabel("Become");
        sizeDownDesc1.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        sizeDownDesc1.setSize(390,100);
        sizeDownDesc1.setLocation(756,450);
        sizeDownDesc1.setForeground(Color.WHITE);
        JLabel sizeDownDesc2 = new JLabel("smaller");
        sizeDownDesc2.setFont(new Font("8BIT WONDER", Font.PLAIN, 20));
        sizeDownDesc2.setSize(390,100);
        sizeDownDesc2.setLocation(751,500);
        sizeDownDesc2.setForeground(Color.WHITE);

        abilityMenu.add(chooseText1, 1);
        abilityMenu.add(chooseText2, 1);
        abilityMenu.add(defenseUpButton, 1);
        abilityMenu.add(defenseUpDesc1, 1);
        abilityMenu.add(defenseUpDesc2, 1);
        abilityMenu.add(speedUpButton, 1);
        abilityMenu.add(speedUpDesc1, 1);
        abilityMenu.add(speedUpDesc2, 1);
        abilityMenu.add(sizeDownButton, 1);
        abilityMenu.add(sizeDownDesc1, 1);
        abilityMenu.add(sizeDownDesc2, 1);
        abilityMenu.add(backGround, 12);
    }

    //This loads the game over menu
    private void loadGameOverMenu(){
        JLabel backGround = new JLabel(new ImageIcon("miscSprites/MenuBackGround.png"));
        backGround.setSize(1920,1080);
        backGround.setLocation(0,-100);

        JLabel score = new JLabel("Score ");
        score.setFont(new Font("8BIT WONDER", Font.PLAIN, 30));
        score.setSize(390,100);
        score.setLocation(0,0);
        score.setForeground(Color.WHITE);

        JLabel gameOverText = new JLabel(new ImageIcon("miscSprites/GameOverText.png"));
        gameOverText.setSize(504, 180);
        gameOverText.setLocation(298, 150);

        nameIn = new JTextArea();
        nameIn.setSize(300, 50);
        nameIn.setLocation(400, 500);
        nameIn.setFont(new Font("8BIT WONDER", Font.PLAIN, 30));

        this.gameOverMenu.add(confirmLoseSubmissionButton, 1);
        this.gameOverMenu.add(nameIn, 1);
        this.gameOverMenu.add(gameOverText, 1);
        this.gameOverMenu.add(score, 2);
        this.gameOverMenu.add(backGround, 4);
    }

    //This loads the win game menu
    private void loadWinMenu(){
        JLabel backGround = new JLabel(new ImageIcon("miscSprites/MenuBackGround.png"));
        backGround.setSize(1920,1080);
        backGround.setLocation(0,-100);

        JLabel score = new JLabel("Score ");
        score.setFont(new Font("8BIT WONDER", Font.PLAIN, 30));
        score.setSize(390,100);
        score.setLocation(0,0);
        score.setForeground(Color.WHITE);

        JLabel gameOverText = new JLabel(new ImageIcon("miscSprites/winText.png"));
        gameOverText.setSize(504, 180);
        gameOverText.setLocation(298, 150);

        nameIn = new JTextArea();
        nameIn.setSize(300, 50);
        nameIn.setLocation(400, 500);
        nameIn.setFont(new Font("8BIT WONDER", Font.PLAIN, 30));

        this.winMenu.add(confirmWinSubmissionButton, 1);
        this.winMenu.add(nameIn, 1);
        this.winMenu.add(gameOverText, 1);
        this.winMenu.add(score, 2);
        this.winMenu.add(backGround, 4);
    }

    //This method loads the actual game
    private void loadGame(String gameMode, String playerAbility){
        try{
            game = new inGame(gameMode, playerAbility);
            this.add(game);
            this.cards.add(game, "game");
            cLayout.show(this.cards,"game");
            this.timer.start();
        }catch (Exception e){}
    }

    //This gets which button has been pressed and if the game has ended or not
    public void actionPerformed(ActionEvent evt){
        Object source = evt.getSource();

        //These are the buttons that choose the game-mode
        if(source == dungeonDashButton){
            gameMode = "dungeonDash";
            cLayout.show(cards, "abilities");
        }
        if(source == bossRushButton){
            gameMode = "bossRush";
            cLayout.show(cards, "abilities");
        }

        //These are the buttons that choose the player ability
        if(source == defenseUpButton){
            music.togglePause();
            loadGame(gameMode, "defenseUp");
        }
        if(source == speedUpButton){
            music.togglePause();
            loadGame(gameMode, "speedUp");
        }
        if(source == sizeDownButton){
            music.togglePause();
            loadGame(gameMode, "sizeDown");
        }

        if(source == highScoresButton){
            loadHighScoreMenu();
            cLayout.show(cards, "highscores");
        }
        if(source == instructionsButton){
            cLayout.show(cards, "instructions");
        }
        if(source == backFromIntroButton){
            cLayout.show(cards, "mainMenu");
        }
        if(source == backFromHighButton){
            cLayout.show(cards, "mainMenu");
        }

        if(source == confirmLoseSubmissionButton || source == confirmWinSubmissionButton){
            //This writes to the "DungeonDashHighScores.txt" file
            if(gameMode.equalsIgnoreCase("dungeonDash")){
                String scoreStr = nameIn.getText();  //scoreStr holds the name of theo player
                int scoreNum = game.getScore();      //scoreNum holds the score of the player

                //These are copies of the original arrayLists to keep track of updated scores
                ArrayList<String> newHighNames = new ArrayList<>(dungeonDashHighNames);
                ArrayList<Integer> newHighscore = new ArrayList<>(dungeonDashHighscore);

                int changePos = 0; //changePos keeps track which position in the highscore arrays will be changed

                //This determines where the new score should go
                while(true) {
                    //When the score is smaller than or equal to the current score, the position will move to the next index, otherwise it stops the loop
                    if(scoreNum <= dungeonDashHighscore.get(changePos)) {
                        changePos ++;
                        if(changePos >= 4) {
                            break;
                        }
                    }
                    else if(scoreNum > dungeonDashHighscore.get(changePos)) {
                        break;
                    }
                }

                //This inserts the score into the "highscore" Arraylist and moves the other scores accordingly
                if(changePos < 4) {
                    for(int i=1; i<5-changePos; i++) {
                        //Will shift the score down starting from the bottom toward the element to be changed
                        newHighNames.set(4-i+1, dungeonDashHighNames.get(4-i));
                        newHighscore.set(4-i+1, dungeonDashHighscore.get(4-i));
                    }
                }

                //This checks if the score is great enough to be the last score on the highscore board
                if(changePos <= 4 && scoreNum > dungeonDashHighscore.get(4)) {
                    newHighNames.set(changePos, scoreStr);
                    newHighscore.set(changePos, scoreNum);
                }

                //Update the original arrayLists
                dungeonDashHighNames = new ArrayList<>(newHighNames);
                dungeonDashHighscore = new ArrayList<>(newHighscore);

                //This updates the highscore text file
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("DungeonDashHighScores.txt"));
                    for(int i=0; i<5; i++) {    //writing in the txt file, replacing it each time
                        writer.write(newHighNames.get(i) + " " + newHighscore.get(i));
                        if(i != 5) {            //does not create a new line if it is the last recorded score
                            writer.newLine();
                        }
                    }
                    writer.close();
                }
                catch(IOException e){
                    System.out.println("Scores could not be recorded");
                }
            }
            //This writes to the "BossRushHighScores.txt" file
            else if(gameMode.equalsIgnoreCase("bossRush")){
                String scoreStr = nameIn.getText();  //scoreStr holds the name of the player
                int scoreNum = game.getScore();      //scoreNum holds the score of the player

                //These are copies of the original arrayLists to keep track of updated scores
                ArrayList<String> newHighNames = new ArrayList<>(bossRushHighNames);
                ArrayList<Integer> newHighscore = new ArrayList<>(bossRushHighscore);

                int changePos = 0; //changePos holds the position in the highscore array will be changed

                //This loops through the list of highscores and determines where the new score should go
                while(true) {
                    //When the score is smaller than or equal to the current score, the position will move to the next index, otherwise it stops the loop
                    if(scoreNum <= bossRushHighscore.get(changePos)) {
                        changePos ++;
                        if(changePos >= 4) {
                            break;
                        }
                    }
                    else if(scoreNum > bossRushHighscore.get(changePos)) {
                        break;
                    }
                }

                //This inserts the score into the "highscore" Arraylist and moves the other scores accordingly
                if(changePos < 4) {
                    for(int i=1; i<5-changePos; i++) {
                        //Will shift the score down starting from the bottom toward the element to be changed
                        newHighNames.set(4-i+1, bossRushHighNames.get(4-i));
                        newHighscore.set(4-i+1, bossRushHighscore.get(4-i));
                    }
                }

                //This checks if the score is great enough to be the last score on the highscore board
                if(changePos <= 4 && scoreNum > bossRushHighscore.get(4)) {
                    newHighNames.set(changePos, scoreStr);
                    newHighscore.set(changePos, scoreNum);
                }

                //Since the new scores were inserted into copies of the original arrayLists, the originals now need to be updated
                bossRushHighNames = new ArrayList<>(newHighNames);
                bossRushHighscore = new ArrayList<>(newHighscore);

                //This updates the highscore text file
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("BossRushHighScores.txt"));
                    for(int i=0; i<5; i++) {    //writing in the txt file, replacing it each time
                        writer.write(newHighNames.get(i) + " " + newHighscore.get(i));
                        if(i != 5) {            //does not create a new line if it is the last recorded score
                            writer.newLine();
                        }
                    }
                    writer.close();
                }
                catch(IOException e){
                    System.out.println("Scores could not be recorded");
                }
            }

            //This displays the main menu
            cLayout.show(cards, "mainMenu");
        }

        if(!game.getRunning()){
            game.setRunning(true);

            //This resets the score displayed
            gameOverMenu.remove(2);

            //This displays the score to the player at the end of the game
            JLabel score = new JLabel("Score " + Integer.toString(game.getScore()));
            score.setFont(new Font("8BIT WONDER", Font.PLAIN, 30));
            score.setSize(410,100);
            score.setLocation(415,400);
            score.setForeground(Color.WHITE);
            this.gameOverMenu.add(score,2);

            music.loop();

            cLayout.show(cards, "gameover");
        }

        if(game.getBossDead()){
            game.setBossDead(true);

            //This resets the score displayed
            winMenu.remove(2);

            //This displays the score to the player at the end of the game
            JLabel score = new JLabel("Score " + Integer.toString(game.getScore()));
            score.setFont(new Font("8BIT WONDER", Font.PLAIN, 30));
            score.setSize(410,100);
            score.setLocation(415,400);
            score.setForeground(Color.WHITE);
            this.winMenu.add(score,2);

            music.loop();

            cLayout.show(cards, "win");
        }
    }
}
