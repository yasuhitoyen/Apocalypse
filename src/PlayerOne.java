/*
 * Michael Yen
 * A P O C A L Y P S E
 */

import javax.swing.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.*;
import java.io.*;

public class PlayerOne extends JPanel implements KeyListener, ActionListener, MouseMotionListener, MouseListener {
  /*
   * This class is the game itself. This is where all the players, bullets, rain,
   * health bar etc are printed. The player of the SERVER is created and this
   * class is mainly
   * where the game is controlled. (In game processes not level select, gun select
   * etc.)
   * This class also sends information and gets information from the server. The
   * server
   * connects to Player One where it can also connect to the client and send
   * information.
   * Player One -> Server -> Client -> Player Two
   * <- <- <-
   */
  Timer gameTick;
  int zombiesKilled;
  int[] p2GunBulletsID;
  int[] playerTwoStepsXArray, playerTwoStepsYArray;
  int p1InvisTimer, p2InvisTimer, defeatedTime;
  int overallSurvivors;
  String time;

  Rectangle levelSelectButton;
  boolean backToLevelSelect;
  boolean victory;
  boolean showMap;
  int gold;
  String timeString;
  Bullet bullet;
  boolean spectatePlayerTwo;
  boolean gameEnded;
  boolean p1ShowLaser;
  boolean acceptedInfo;
  int tick;
  int p1X, p1Y, p2X, p2Y, mouseX, mouseY, p1Health, p2Health;
  boolean upKey, downKey, leftKey, rightKey, tagged, p1It, waitUntilNotTouching, p1RightLooked, p1LeftLooked,
      p2LeftLooked, p2RightLooked, touchingObject, stillTouching;
  boolean pressMouse, p1ShowBullet, p2ShowBullet, oneBulletHit, atLeastOneZombieHurtP1, atLeastOneZombieHurtP2;
  final int FRAME_WIDTH = 1370, FRAME_HEIGHT = 750, PLAYER_WIDTH = 68, PLAYER_HEIGHT = 100, FRAME_DIFFERENCE = 40,
      PLAYER_X = FRAME_WIDTH / 2 - FRAME_DIFFERENCE - PLAYER_WIDTH / 2, PLAYER_Y = FRAME_HEIGHT / 2 - FRAME_DIFFERENCE;
  final int PLAYER_HITBOX_X = PLAYER_X, PLAYER_HITBOX_Y = PLAYER_Y + 70, PLAYER_HITBOX_WIDTH = PLAYER_WIDTH,
      PLAYER_HITBOX_HEIGHT = 30;
  int bulletWidth, bulletLength, p1GunType, p2GunType, mouseFromMiddleX, mouseFromMiddleY;
  int raindropCounter;
  int hurtMultipleTimes;
  int zombiesAtOnce;
  float fadeInGame;
  int gameMode, p2RemoveBullet;
  int survivorCount;
  int sidesSpawn, p1BulletDamage;
  int p1HurtPerMS, p2HurtPerMS;
  int p2BulletDamage, zombieLimit;
  int numRescued;
  boolean spawnZombiesUp, spawnZombiesRight, playerOneInvis, playerTwoInvis;
  Image okDefeat, okVictory;
  double p1GunAngle, p2GunAngle;
  Rectangle gunShot, playerHitbox;
  Image background, playerRightImage, playerLeftImage, rightMicroUzi, leftMicroUzi, leftHandgun, rightHandgun, rightAWM,
      leftAWM, playerRightBlueImage, playerLeftBlueImage, raindropImage;
  Image rightLasergun, leftLasergun, rightAK, leftAK, rightBeretta, leftBeretta, rightDesertEagle, leftDesertEagle,
      bulletImage, healthBar, regularZombie, bloodStainScreen, regularZombieRight, regularZombieLeft;
  Image movingHealthBar;
  Polygon p1Bullet, p2Bullet;
  int p1BulletCoorX[], p1BulletCoorY[], p2BulletCoorX[], p2BulletCoorY[];
  int p1SurvivorsRescued, deleteBulletID;
  ArrayList<Objects> objects = new ArrayList<Objects>();
  boolean p2ShowLaser;
  ArrayList<Integer> survivorXPos = new ArrayList<Integer>();
  ArrayList<Integer> survivorYPos = new ArrayList<Integer>();
  ArrayList<Boolean> survivorLookRight = new ArrayList<Boolean>();
  ArrayList<Integer> survivorType = new ArrayList<Integer>();

  ArrayList<Integer> objectsXPos = new ArrayList<Integer>();
  ArrayList<Integer> objectsYPos = new ArrayList<Integer>();
  ArrayList<String> objectsName = new ArrayList<String>();

  ArrayList<Integer> p1GunBulletXPos = new ArrayList<Integer>();
  ArrayList<Integer> p1GunBulletYPos = new ArrayList<Integer>();
  ArrayList<Boolean> p1GunBulletPlayerRightLooked = new ArrayList<Boolean>();
  ArrayList<Double> p1GunBulletGunAngle = new ArrayList<Double>();
  ArrayList<Integer> p1GunBulletXPosFromPlayer = new ArrayList<Integer>();
  ArrayList<Integer> p1GunBulletYPosFromPlayer = new ArrayList<Integer>();

  ArrayList<RainDropletsGame> rain = new ArrayList<RainDropletsGame>();
  int p2GunBulletXPos[];
  int p2GunBulletYPos[];
  boolean p2GunBulletPlayerRightLooked[];
  double p2GunBulletGunAngle[];
  int p2GunBulletXPosFromPlayer[];
  int p2GunBulletYPosFromPlayer[];
  int p1BulletDistance;
  int p2SurvivorsRescued;
  int[] playerTwoBulletsID;
  boolean singlePlayer;
  Image victoryScreen, defeatScreen;
  ArrayList<GunBullet> p1GunBullets = new ArrayList<GunBullet>();
  ArrayList<GunBullet> p2GunBullets = new ArrayList<GunBullet>();
  Rectangle connectionLostExitClient;
  ArrayList<Zombie> zombies = new ArrayList<Zombie>();
  ArrayList<Survivor> survivors = new ArrayList<Survivor>();

  ArrayList<Integer> zombieXPos, zombieYPos, zombieHealth, zombieType;
  ArrayList<Boolean> zombieHurt, zombieLookRight;

  ArrayList<Integer> playerOneStepsX, playerOneStepsY, playerTwoStepsX, playerTwoStepsY;
  Objects[] objectsInArray;

  GameServer server;
  /*
   * Card Layout
   * Gamemodes:
   * 1 = Level Select
   * 2 = Gun Select / Shop
   * 3 = In Game
   * 3/4 = Victory / Defeat screen
   * 
   * - card layout
   * 
   */
  Image invisScreen;
  Image exitClientSign;

  /*
   * Gun Type
   * 0: Glock 17
   * 1: Desert Eagle
   * 2: Mini Uzi
   * 3: AK-47
   * 4: PM-12
   * 5: AWM
   * 6: Lasergun
   */
  public PlayerOne(GameServer server) throws IOException {
    /*
     * Constructor for PlayerOne. Starts the
     * timer of the actual game. Adds MouseMotionListener,
     * MouseListener, and KeyListener to this class.
     * Also initializes all the variables
     */
    addKeyListener(this);
    addMouseMotionListener(this);
    addMouseListener(this);
    fadeInGame = 0f;
    connectionLostExitClient = new Rectangle(420, 210, 904, 300);
    // Sign (420, 210, 904, 300);

    /*
     * objects.add(new Objects("Tree", 10, 10));
     * objects.add(new Objects("Tree", 220, 30));
     * objects.add(new Objects("Tree", 130, 100));
     * objects.add(new Objects("Tree", 420, 220));
     * objects.add(new Objects("Tree", 220, 30));
     * objects.add(new Objects("Tree", 300, 500));
     * objects.add(new Objects("Tree", 580, 200));
     * objects.add(new Objects("Tree", 720, 320));
     * objects.add(new Objects("Tree", 300, 680));
     * objects.add(new Objects("Tree", 0, 700));
     */

    // objects.add(new Objects("Telephone Booth", 50, 200));

    // */
    setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
    p1GunType = 2;
    playerOneStepsX = new ArrayList<Integer>();
    playerOneStepsY = new ArrayList<Integer>();
    playerTwoStepsX = new ArrayList<Integer>();
    playerTwoStepsY = new ArrayList<Integer>();
    playerTwoStepsXArray = new int[10];
    playerTwoStepsYArray = new int[10];
    this.server = server;
    bulletWidth = 10;
    bulletLength = 4000;
    p1It = true;
    gameMode = 1;
    bullet = new Bullet();
    p1RightLooked = true;
    stillTouching = false;
    p1LeftLooked = false;

    waitUntilNotTouching = false;
    touchingObject = false;
    p1Bullet = new Polygon();
    p2Bullet = new Polygon();

    if (!singlePlayer)
      server.p1Bullet = p1Bullet;

    playerHitbox = new Rectangle(PLAYER_X + mouseFromMiddleX / -20, PLAYER_Y + mouseFromMiddleY / -20, PLAYER_WIDTH,
        PLAYER_HEIGHT);
    p1BulletCoorX = new int[4];
    p1BulletCoorY = new int[4];
    p2BulletCoorX = new int[4];
    p2BulletCoorY = new int[4];
    playerTwoBulletsID = new int[0];

    zombiesAtOnce = 15;
    p1Health = 100;
    p2Health = 100;
    p1HurtPerMS = 10;
    p2GunBulletXPos = new int[0];
    p2GunBulletYPos = new int[0];
    p2GunBulletPlayerRightLooked = new boolean[0];
    p2GunBulletGunAngle = new double[0];
    bullet.reloadTime = 4;
    p1BulletDamage = 1;
    zombieXPos = new ArrayList<Integer>();
    zombieYPos = new ArrayList<Integer>();
    zombieHealth = new ArrayList<Integer>();
    zombieHurt = new ArrayList<Boolean>();
    zombieType = new ArrayList<Integer>();
    zombieLookRight = new ArrayList<Boolean>();

    objectsInArray = new Objects[100];
    upKey = downKey = leftKey = rightKey = false;
    levelSelectButton = new Rectangle(540, 610, 210, 70);
    // Gun images
    /// *
    singlePlayer = false;
    okDefeat = new ImageIcon("../images/OkDefeat.png").getImage();
    okVictory = new ImageIcon("../images/OkVictory.png").getImage();
    defeatScreen = new ImageIcon("../images/DefeatScreen.png").getImage();
    victoryScreen = new ImageIcon("../images/VictoryScreen.png").getImage();
    background = new ImageIcon("../images/Map2.png").getImage();
    playerRightImage = new ImageIcon("../images/PlayerRight.png").getImage();
    playerLeftImage = new ImageIcon("../images/PlayerLeft.png").getImage();
    playerRightBlueImage = new ImageIcon("../images/PlayerRightBlue.png").getImage();
    playerLeftBlueImage = new ImageIcon("../images/PlayerLeftBlue.png").getImage();
    rightMicroUzi = new ImageIcon("../images/Micro Uzi 1.png").getImage();
    leftMicroUzi = new ImageIcon("../images/Micro Uzi 2.png").getImage();
    rightHandgun = new ImageIcon("../images/Handgun 1.png").getImage();
    leftHandgun = new ImageIcon("../images/Handgun 2.png").getImage();
    rightAK = new ImageIcon("../images/AK-47 1.png").getImage();
    leftAK = new ImageIcon("../images/AK-47 2.png").getImage();
    rightBeretta = new ImageIcon("../images/Beretta PM12 1.png").getImage();
    leftBeretta = new ImageIcon("../images/Beretta PM12 2.png").getImage();
    rightDesertEagle = new ImageIcon("../images/Desert Eagle 1.png").getImage();
    leftDesertEagle = new ImageIcon("../images/Desert Eagle 2.png").getImage();
    rightAWM = new ImageIcon("../images/AWM 1.png").getImage();
    leftAWM = new ImageIcon("../images/AWM 2.png").getImage();
    rightLasergun = new ImageIcon("../images/Lasergun 1.png").getImage();
    leftLasergun = new ImageIcon("../images/Lasergun 2.png").getImage();
    bulletImage = new ImageIcon("../images/BulletImage.png").getImage();
    healthBar = new ImageIcon("../images/HealthContainer.png").getImage();
    raindropImage = new ImageIcon("../images/RainDrops.png").getImage();
    regularZombie = new ImageIcon("../images/ZombieNormal1.png").getImage();
    bloodStainScreen = new ImageIcon("../images/BloodStainScreen.png").getImage();
    movingHealthBar = new ImageIcon("../images/OtherPlayerHealthBar.png").getImage();
    invisScreen = new ImageIcon("../images/InvisScreen.png").getImage();
    regularZombieRight = new ImageIcon("../images/ZombieNormal1.png").getImage();
    regularZombieLeft = new ImageIcon("../images/ZombieNormal2.png").getImage();
    exitClientSign = new ImageIcon("../images/EXITCLIENT.png").getImage();

    connectionLostExitClient = new Rectangle(585, 340, 160, 75);

    for (int i = 0; i < 200; i++) {
      playerOneStepsX.add(p1X * -1 + PLAYER_X);
      playerOneStepsY.add(p1Y * -1 + PLAYER_Y);
    }
    for (int i = 0; i < 200; i++) {
      playerTwoStepsX.add(0);
      playerTwoStepsY.add(0);
    }

    survivorCount = survivors.size();
    p1SurvivorsRescued = 0;
    numRescued = 0;
    gameTick = new Timer(5, this);

    gameTick.start();
  }

  public PlayerOne(boolean singlePlayer) throws IOException {
    /*
     * Constructor for PlayerOne. Starts the
     * timer of the actual game. Adds MouseMotionListener,
     * MouseListener, and KeyListener to this class.
     * Also initializes all the variables
     */
    this.singlePlayer = singlePlayer;
    addKeyListener(this);
    addMouseMotionListener(this);
    addMouseListener(this);
    fadeInGame = 0f;
    connectionLostExitClient = new Rectangle(420, 210, 904, 300);
    // Sign (420, 210, 904, 300);

    /*
     * objects.add(new Objects("Tree", 10, 10));
     * objects.add(new Objects("Tree", 220, 30));
     * objects.add(new Objects("Tree", 130, 100));
     * objects.add(new Objects("Tree", 420, 220));
     * objects.add(new Objects("Tree", 220, 30));
     * objects.add(new Objects("Tree", 300, 500));
     * objects.add(new Objects("Tree", 580, 200));
     * objects.add(new Objects("Tree", 720, 320));
     * objects.add(new Objects("Tree", 300, 680));
     * objects.add(new Objects("Tree", 0, 700));
     */

    // objects.add(new Objects("Telephone Booth", 50, 200));

    // */
    setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
    p1GunType = 2;
    playerOneStepsX = new ArrayList<Integer>();
    playerOneStepsY = new ArrayList<Integer>();
    playerTwoStepsX = new ArrayList<Integer>();
    playerTwoStepsY = new ArrayList<Integer>();
    playerTwoStepsXArray = new int[10];
    playerTwoStepsYArray = new int[10];
    bulletWidth = 10;
    bulletLength = 4000;
    p1It = true;
    gameMode = 1;
    bullet = new Bullet();
    p1RightLooked = true;
    stillTouching = false;
    p1LeftLooked = false;

    waitUntilNotTouching = false;
    touchingObject = false;
    p1Bullet = new Polygon();
    p2Bullet = new Polygon();
    if (!singlePlayer)
      server.p1Bullet = p1Bullet;
    playerHitbox = new Rectangle(PLAYER_X + mouseFromMiddleX / -20, PLAYER_Y + mouseFromMiddleY / -20, PLAYER_WIDTH,
        PLAYER_HEIGHT);
    p1BulletCoorX = new int[4];
    p1BulletCoorY = new int[4];
    p2BulletCoorX = new int[4];
    p2BulletCoorY = new int[4];
    playerTwoBulletsID = new int[0];

    zombiesAtOnce = 15;
    p1Health = 100;
    p2Health = 100;
    p1HurtPerMS = 10;
    p2GunBulletXPos = new int[0];
    p2GunBulletYPos = new int[0];
    p2GunBulletPlayerRightLooked = new boolean[0];
    p2GunBulletGunAngle = new double[0];
    bullet.reloadTime = 4;
    p1BulletDamage = 1;
    zombieXPos = new ArrayList<Integer>();
    zombieYPos = new ArrayList<Integer>();
    zombieHealth = new ArrayList<Integer>();
    zombieHurt = new ArrayList<Boolean>();
    zombieType = new ArrayList<Integer>();
    zombieLookRight = new ArrayList<Boolean>();

    objectsInArray = new Objects[100];
    upKey = downKey = leftKey = rightKey = false;
    levelSelectButton = new Rectangle(540, 610, 210, 70);
    // Gun images
    /// *
    okDefeat = new ImageIcon("../images/OkDefeat.png").getImage();
    okVictory = new ImageIcon("../images/OkVictory.png").getImage();
    defeatScreen = new ImageIcon("../images/DefeatScreen.png").getImage();
    victoryScreen = new ImageIcon("../images/VictoryScreen.png").getImage();
    background = new ImageIcon("../images/Map2.png").getImage();
    playerRightImage = new ImageIcon("../images/PlayerRight.png").getImage();
    playerLeftImage = new ImageIcon("../images/PlayerLeft.png").getImage();
    playerRightBlueImage = new ImageIcon("../images/PlayerRightBlue.png").getImage();
    playerLeftBlueImage = new ImageIcon("../images/PlayerLeftBlue.png").getImage();
    rightMicroUzi = new ImageIcon("../images/Micro Uzi 1.png").getImage();
    leftMicroUzi = new ImageIcon("../images/Micro Uzi 2.png").getImage();
    rightHandgun = new ImageIcon("../images/Handgun 1.png").getImage();
    leftHandgun = new ImageIcon("../images/Handgun 2.png").getImage();
    rightAK = new ImageIcon("../images/AK-47 1.png").getImage();
    leftAK = new ImageIcon("../images/AK-47 2.png").getImage();
    rightBeretta = new ImageIcon("../images/Beretta PM12 1.png").getImage();
    leftBeretta = new ImageIcon("../images/Beretta PM12 2.png").getImage();
    rightDesertEagle = new ImageIcon("../images/Desert Eagle 1.png").getImage();
    leftDesertEagle = new ImageIcon("../images/Desert Eagle 2.png").getImage();
    rightAWM = new ImageIcon("../images/AWM 1.png").getImage();
    leftAWM = new ImageIcon("../images/AWM 2.png").getImage();
    rightLasergun = new ImageIcon("../images/Lasergun 1.png").getImage();
    leftLasergun = new ImageIcon("../images/Lasergun 2.png").getImage();
    bulletImage = new ImageIcon("../images/BulletImage.png").getImage();
    healthBar = new ImageIcon("../images/HealthContainer.png").getImage();
    raindropImage = new ImageIcon("../images/RainDrops.png").getImage();
    regularZombie = new ImageIcon("../images/ZombieNormal1.png").getImage();
    bloodStainScreen = new ImageIcon("../images/BloodStainScreen.png").getImage();
    movingHealthBar = new ImageIcon("../images/OtherPlayerHealthBar.png").getImage();
    invisScreen = new ImageIcon("../images/InvisScreen.png").getImage();
    regularZombieRight = new ImageIcon("../images/ZombieNormal1.png").getImage();
    regularZombieLeft = new ImageIcon("../images/ZombieNormal2.png").getImage();
    exitClientSign = new ImageIcon("../images/EXITCLIENT.png").getImage();

    connectionLostExitClient = new Rectangle(585, 340, 160, 75);

    for (int i = 0; i < 200; i++) {
      playerOneStepsX.add(p1X * -1 + PLAYER_X);
      playerOneStepsY.add(p1Y * -1 + PLAYER_Y);
    }
    survivorCount = survivors.size();
    p1SurvivorsRescued = 0;
    numRescued = 0;

    gameTick = new Timer(30, this);

    gameTick.start();
  }

  public void actionPerformed(ActionEvent e) {
    /*
     * Calls setUpBulletCoordinatesPlayerOne and
     * setUpBulletCoordinatesPlayerTwo and setObjectPosition
     * and playerMovement. It also sends and receives information
     * to and from the server.
     * 
     */
    p1ShowBullet = bullet.showBullet;

    if (gameEnded && !victory) {
      gold = 0;
    }
    tick++;

    if (!gameEnded) {
      if (singlePlayer)
        defeatedTime += 4;
      else
        defeatedTime += 5;
    }
    if (!singlePlayer) {
      if (p2Health <= 0 && p1Health <= 0) {
        gameEnded = true;
        victory = false;

      } else if (0 == numRescued) {
        gameEnded = true;
        victory = true;
      }
    } else {
      if (p1Health < 0) {
        gameEnded = true;
        victory = false;
      } else if (0 == numRescued) {
        gameEnded = true;
        victory = true;
      }
    }

    if (gameEnded) {
      p1X = (int) (FRAME_WIDTH * 3.0 / 2.0);
      p1Y = (int) (FRAME_HEIGHT * 3.0 / 2.0);

      p2X = (int) (FRAME_WIDTH * 3.0 / 2.0);
      p2Y = (int) (FRAME_HEIGHT * 3.0 / 2.0);
    }

    if (!singlePlayer) {
      if (server.gameMode == 1) {
        if (!gameEnded) {
          if (p2HurtPerMS < 10) {
            p2HurtPerMS++;
          }
          if (p1HurtPerMS < 10) {
            p1HurtPerMS++;
          }
        } else {
          p1HurtPerMS = 10;
          p2HurtPerMS = 10;
        }
        if (p1HurtPerMS == 10) {
          atLeastOneZombieHurtP1 = false;
          for (Zombie zombie : zombies) {
            if (zombie.hitboxX + zombie.hitboxWidth + p1X * -1 > PLAYER_HITBOX_X
                && zombie.hitboxX + p1X * -1 < PLAYER_HITBOX_X + PLAYER_HITBOX_WIDTH
                && zombie.hitboxY + zombie.hitboxHeight + p1Y * -1 > PLAYER_HITBOX_Y
                && zombie.hitboxY + p1Y * -1 < PLAYER_HITBOX_Y + PLAYER_HEIGHT) {
              if (!gameEnded) {
                p1Health -= zombie.zombieDamage;
                atLeastOneZombieHurtP1 = true;
              }
            }
          }
          if (atLeastOneZombieHurtP1) {
            p1HurtPerMS = 0;
          }
        }
        if (p2HurtPerMS == 10) {
          atLeastOneZombieHurtP2 = false;
          for (Zombie zombie : zombies) {
            if (zombie.hitboxX + zombie.hitboxWidth + p1X * -1 > PLAYER_HITBOX_X + p1X * -1 + p2X
                && zombie.hitboxX + p1X * -1 < PLAYER_HITBOX_X + PLAYER_HITBOX_WIDTH + p1X * -1 + p2X
                && zombie.hitboxY + zombie.hitboxHeight + p1Y * -1 > PLAYER_HITBOX_Y + p1Y * -1 + p2Y
                && zombie.hitboxY + p1Y * -1 < PLAYER_HITBOX_Y + PLAYER_HEIGHT + p1Y * -1 + p2Y) {
              if (!gameEnded) {
                p2Health -= zombie.zombieDamage;
                atLeastOneZombieHurtP2 = true;
              }
            }
          }
          if (atLeastOneZombieHurtP2) {
            p2HurtPerMS = 0;
          }
        }
        if (defeatedTime % 60000 / 6000 >= 10) {
          gameEnded = true;
          victory = false;
        }
        if (p2Health <= 0) {
          for (Survivor survivor : survivors) {
            if (survivor.following == 2) {
              if (survivor.saved) {
                survivor.following = 0;
                survivor.saved = false;
              }
            }
          }

        }
        if (p1Health < 0 && !gameEnded) {
          spectatePlayerTwo = true;
          p1X = -100000;
          p1Y = -100000;
          for (Survivor survivor : survivors) {
            if (survivor.following == 1) {
              if (survivor.saved) {
                survivor.following = 0;
                survivor.saved = false;
              }
            }
          }
        } else {
          spectatePlayerTwo = false;
        }

        if (fadeInGame > 0.05) {
          fadeInGame -= 0.01f;
        }

        if (p1InvisTimer > 1) {
          p1InvisTimer -= 1;
        }
        if (p2InvisTimer > 1) {
          p2InvisTimer -= 1;
        }

        setUpBulletCoordinatesPlayerOne();
        playerMovement();
        if (!singlePlayer) {
          setUpBulletCoordinatesPlayerTwo();
          getVariablesFromServer();
          setVariablesInServer();
        }
        updateAllObjects();
        updateAllBullets();
        updateAllSurvivors();

        updateAllZombies();
        bulletSense();
        updateGunSettings();
        // updateAllRainDroplets();

      }
    } else {
      bullet.pressMouse = pressMouse;
      if (p1InvisTimer > 1) {
        p1InvisTimer -= 1;
      }
      if (!gameEnded) {
        if (p1HurtPerMS < 10) {
          p1HurtPerMS++;
        }
      } else {
        p1HurtPerMS = 10;
      }
      if (p1HurtPerMS == 10) {
        atLeastOneZombieHurtP1 = false;
        for (Zombie zombie : zombies) {
          if (zombie.hitboxX + zombie.hitboxWidth + p1X * -1 > PLAYER_HITBOX_X
              && zombie.hitboxX + p1X * -1 < PLAYER_HITBOX_X + PLAYER_HITBOX_WIDTH
              && zombie.hitboxY + zombie.hitboxHeight + p1Y * -1 > PLAYER_HITBOX_Y
              && zombie.hitboxY + p1Y * -1 < PLAYER_HITBOX_Y + PLAYER_HEIGHT) {
            if (!gameEnded) {
              p1Health -= zombie.zombieDamage;
              atLeastOneZombieHurtP1 = true;
            }
          }
        }
        if (atLeastOneZombieHurtP1) {
          p1HurtPerMS = 0;
        }
      }

      if (defeatedTime % 60000 / 6000 >= 10) {
        gameEnded = true;
        victory = false;
      }

      if (p1Health < 0 && !gameEnded) {
        gameEnded = true;
        for (Survivor survivor : survivors) {
          if (survivor.following == 1) {
            if (survivor.saved) {
              survivor.following = 0;
              survivor.saved = false;
            }
          }
        }
      } else {
        spectatePlayerTwo = false;
      }

      if (fadeInGame > 0.05) {
        fadeInGame -= 0.01f;
      }

      p2Health = -1000;
      p2X = -10000;
      p2Y = -10000;

      setUpBulletCoordinatesPlayerOne();
      playerMovement();
      updateAllObjects();
      updateAllBullets();
      updateAllSurvivors();
      updateAllZombies();
      bulletSense();
      updateGunSettings();
      // updateAllRainDroplets();

    }
    repaint();
  }

  public void paintComponent(Graphics g) {
    /*
     * Draws all the objects, player one, player two,
     * gun of player one, bullet, the background, and
     * the information on the top left.
     */
    setBackground(Color.WHITE);
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    g2d.setColor(Color.blue);

    // g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
    // (float)((Math.sin(tick * 8) * 4) / 10)));
    if (!spectatePlayerTwo) {
      g2d.drawImage(background, p1X * -1 + mouseFromMiddleX / -20, p1Y * -1 + mouseFromMiddleY / -20, FRAME_WIDTH * 3,
          FRAME_HEIGHT * 3, null);
      // g2d.drawImage(background, mouseFromMiddleX / -20 + p1X * -1 +
      // mouseFromMiddleX / -20, mouseFromMiddleY / -20 + p1Y * -1 + mouseFromMiddleY
      // / -20, FRAME_WIDTH * 3, FRAME_HEIGHT * 3, null);
      g2d.setColor(new Color((int) ((30 + (Math.sin(tick * 300) * 6) / 10) * 1.3),
          50 + (int) ((30 + (Math.sin(tick * 300) * 6) / 10) * 1.3),
          10 + (int) ((30 + (Math.sin(tick * 300) * 6) / 10) * 1.3)));

      if (p1ShowLaser) {
        g2d.setColor(Color.CYAN);
        g2d.fill(p1Bullet);
      }
      if (p2ShowLaser) {
        g2d.setColor(Color.CYAN);
        g2d.fill(p2Bullet);
      }

      for (int i = 0; i < objects.size(); i++) {
        if (objects.get(i).image.contains("Border")) {
          g2d.setColor(Color.red);
          g2d.fillRect(objects.get(i).xPos + p1X * -1 + mouseFromMiddleX / -20,
              objects.get(i).yPos + p1Y * -1 + mouseFromMiddleY / -20, objects.get(i).width, objects.get(i).height);
        }
      }
      // Layer Printing
      for (GunBullet currentBullet : p2GunBullets) {
        if (currentBullet.playerRightLooked) {
          g2d.rotate(Math.toRadians(currentBullet.gunAngle),
              currentBullet.xPos + p1X * -1 + mouseFromMiddleX / -20 + PLAYER_X + PLAYER_WIDTH / 2,
              currentBullet.yPos + p1Y * -1 + mouseFromMiddleY / -20 + PLAYER_Y + PLAYER_HEIGHT / 2);
          g2d.drawImage(bulletImage,
              currentBullet.xPos + p1X * -1 + mouseFromMiddleX / -20 + PLAYER_X + PLAYER_WIDTH / 2,
              currentBullet.yPos + p1Y * -1 + mouseFromMiddleY / -20 + PLAYER_Y + PLAYER_HEIGHT / 2, 14, 7, null);
          g2d.rotate(Math.toRadians(360 - currentBullet.gunAngle),
              currentBullet.xPos + p1X * -1 + mouseFromMiddleX / -20 + PLAYER_X + PLAYER_WIDTH / 2,
              currentBullet.yPos + p1Y * -1 + mouseFromMiddleY / -20 + PLAYER_Y + PLAYER_HEIGHT / 2);
        } else if (currentBullet.gunAngle != 90 || currentBullet.gunAngle != -90) {
          g2d.rotate(Math.toRadians(currentBullet.gunAngle + 180),
              currentBullet.xPos + p1X * -1 + mouseFromMiddleX / -20 + PLAYER_X + PLAYER_WIDTH / 2,
              currentBullet.yPos + p1Y * -1 + mouseFromMiddleY / -20 + PLAYER_Y + PLAYER_HEIGHT / 2);
          g2d.drawImage(bulletImage,
              currentBullet.xPos + p1X * -1 + mouseFromMiddleX / -20 + PLAYER_X + PLAYER_WIDTH / 2 + 10,
              currentBullet.yPos + p1Y * -1 + mouseFromMiddleY / -20 + PLAYER_Y + PLAYER_HEIGHT / 2, 14, 7, null);
          g2d.rotate(Math.toRadians(180 - currentBullet.gunAngle),
              currentBullet.xPos + p1X * -1 + mouseFromMiddleX / -20 + PLAYER_X + PLAYER_WIDTH / 2,
              currentBullet.yPos + p1Y * -1 + mouseFromMiddleY / -20 + PLAYER_Y + PLAYER_HEIGHT / 2);
        }
      }
      for (GunBullet currentBullet : p1GunBullets) {
        if (currentBullet.playerRightLooked) {
          g2d.rotate(Math.toRadians(currentBullet.gunAngle),
              currentBullet.xPosFromPlayer + p1X * -1 + mouseFromMiddleX / -20,
              currentBullet.yPosFromPlayer + p1Y * -1 + mouseFromMiddleY / -20);
          g2d.drawImage(bulletImage, currentBullet.xPosFromPlayer + p1X * -1 + mouseFromMiddleX / -20,
              currentBullet.yPosFromPlayer + p1Y * -1 + mouseFromMiddleY / -20, 17, 7, null);
          g2d.rotate(Math.toRadians(360 - currentBullet.gunAngle),
              currentBullet.xPosFromPlayer + p1X * -1 + mouseFromMiddleX / -20,
              currentBullet.yPosFromPlayer + p1Y * -1 + mouseFromMiddleY / -20);
        } else if (currentBullet.gunAngle != 90 || currentBullet.gunAngle != -90) {
          g2d.rotate(Math.toRadians(currentBullet.gunAngle + 180),
              currentBullet.xPosFromPlayer + p1X * -1 + mouseFromMiddleX / -20,
              currentBullet.yPosFromPlayer + p1Y * -1 + mouseFromMiddleY / -20);
          g2d.drawImage(bulletImage, currentBullet.xPosFromPlayer + p1X * -1 + mouseFromMiddleX / -20 + 10,
              currentBullet.yPosFromPlayer + p1Y * -1 + mouseFromMiddleY / -20, 14, 7, null);
          g2d.rotate(Math.toRadians(180 - currentBullet.gunAngle),
              currentBullet.xPosFromPlayer + p1X * -1 + mouseFromMiddleX / -20,
              currentBullet.yPosFromPlayer + p1Y * -1 + mouseFromMiddleY / -20);
        }

      }
      for (int y = -500; y < FRAME_HEIGHT + 500; y++) {
        if (PLAYER_Y + p2Y + p1Y * -1 + mouseFromMiddleY / -20 + PLAYER_HEIGHT == y && !gameEnded) {
          g2d.setColor(Color.red);
          if (!gameEnded)
            g2d.fillRect(p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + 1,
                p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y - 25, (int) (68 * p2Health / 100), 20);
          g2d.drawImage(movingHealthBar, p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X,
              p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y - 25, 70, 20, null);

          if (p2RightLooked) {
            g2d.drawImage(playerRightBlueImage, p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X,
                p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y, PLAYER_WIDTH, PLAYER_HEIGHT, null);
          } else if (p2LeftLooked) {
            g2d.drawImage(playerLeftBlueImage, p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X,
                p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y, PLAYER_WIDTH, PLAYER_HEIGHT, null);
          }
          if (p2GunType == 0) {
            if (p2RightLooked) {
              g2d.rotate(Math.toRadians(p2GunAngle),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(rightHandgun, p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2 + 10,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_WIDTH / 2 + 10, 45, 30, null);
              g2d.rotate(Math.toRadians(360 - p2GunAngle),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
            } else if (p2GunAngle != 90 || p2GunAngle != -90) {
              g2d.rotate(Math.toRadians(p2GunAngle + 180),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(leftHandgun, p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2 + 10,
                  p1Y * -1 + mouseFromMiddleY / -20 + PLAYER_Y + p2Y + PLAYER_WIDTH / 2 - 5, 45, 30, null);
              g2d.rotate(Math.toRadians(180 - p2GunAngle),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
            }
          }
          if (p2GunType == 1) {
            if (p2RightLooked) {
              g2d.rotate(Math.toRadians(p2GunAngle),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(rightDesertEagle,
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2 + 10,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_WIDTH / 2 + 10, 50, 30, null);
              g2d.rotate(Math.toRadians(360 - p2GunAngle),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
            } else if (p2GunAngle != 90 || p2GunAngle != -90) {
              g2d.rotate(Math.toRadians(p2GunAngle + 180),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(leftDesertEagle, p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2 + 10,
                  p1Y * -1 + mouseFromMiddleY / -20 + PLAYER_Y + p2Y + PLAYER_WIDTH / 2 - 5, 50, 30, null);
              g2d.rotate(Math.toRadians(180 - p2GunAngle),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
            }
          }
          if (p2GunType == 2) {
            if (p2RightLooked) {
              g2d.rotate(Math.toRadians(p2GunAngle),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(rightMicroUzi, p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2 + 10,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_WIDTH / 2 + 10, 60, 50, null);
              g2d.rotate(Math.toRadians(360 - p2GunAngle),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
            } else if (p2GunAngle != 90 || p2GunAngle != -90) {
              g2d.rotate(Math.toRadians(p2GunAngle + 180),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(leftMicroUzi, p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2 + 10,
                  p1Y * -1 + mouseFromMiddleY / -20 + PLAYER_Y + p2Y + PLAYER_WIDTH / 2 - 20, 60, 50, null);
              g2d.rotate(Math.toRadians(180 - p2GunAngle),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
            }
          }
          if (p2GunType == 3) {
            if (p2RightLooked) {
              g2d.rotate(Math.toRadians(p2GunAngle),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(rightAK, p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2 - 20,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_WIDTH / 2 + 10, 110, 35, null);
              g2d.rotate(Math.toRadians(360 - p2GunAngle),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
            } else if (p2GunAngle != 90 || p2GunAngle != -90) {
              g2d.rotate(Math.toRadians(p2GunAngle + 180),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(leftAK, p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2 - 20,
                  p1Y * -1 + mouseFromMiddleY / -20 + PLAYER_Y + p2Y + PLAYER_WIDTH / 2 - 5, 110, 35, null);
              g2d.rotate(Math.toRadians(180 - p2GunAngle),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
            }
          }
          if (p2GunType == 4) {
            if (p2RightLooked) {
              g2d.rotate(Math.toRadians(p2GunAngle),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(rightBeretta, p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2 - 20,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_WIDTH / 2 + 10, 110, 45, null);
              g2d.rotate(Math.toRadians(360 - p2GunAngle),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
            } else if (p2GunAngle != 90 || p2GunAngle != -90) {
              g2d.rotate(Math.toRadians(p2GunAngle + 180),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(leftBeretta, p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2 - 20,
                  p1Y * -1 + mouseFromMiddleY / -20 + PLAYER_Y + p2Y + PLAYER_WIDTH / 2 - 15, 110, 45, null);
              g2d.rotate(Math.toRadians(180 - p2GunAngle),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
            }
          }
          if (p2GunType == 5) {
            if (p2RightLooked) {
              g2d.rotate(Math.toRadians(p2GunAngle),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(rightAWM, p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2 - 20,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_WIDTH / 2 + 10, 130, 30, null);
              g2d.rotate(Math.toRadians(360 - p2GunAngle),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
            } else if (p2GunAngle != 90 || p2GunAngle != -90) {
              g2d.rotate(Math.toRadians(p2GunAngle + 180),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(leftAWM, p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2 - 20,
                  p1Y * -1 + mouseFromMiddleY / -20 + PLAYER_Y + p2Y + PLAYER_WIDTH / 2 - 5, 130, 30, null);
              g2d.rotate(Math.toRadians(180 - p2GunAngle),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
            }
          }
          if (p2GunType == 6) {
            if (p2RightLooked) {
              g2d.rotate(Math.toRadians(p2GunAngle),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(rightLasergun, p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2 - 40,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_WIDTH / 2 + 7, 160, 50, null);
              g2d.rotate(Math.toRadians(360 - p2GunAngle),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
            } else if (p2GunAngle != 90 || p2GunAngle != -90) {
              g2d.rotate(Math.toRadians(p2GunAngle + 180),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(leftLasergun, p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2 - 40,
                  p1Y * -1 + mouseFromMiddleY / -20 + PLAYER_Y + p2Y + PLAYER_WIDTH / 2 - 20, 160, 50, null);
              g2d.rotate(Math.toRadians(180 - p2GunAngle),
                  p1X * -1 + mouseFromMiddleX / -20 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
                  p1Y * -1 + mouseFromMiddleY / -20 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
            }
          }

        }
        if (PLAYER_Y + PLAYER_HEIGHT == y) {

          if (p1RightLooked) {
            g2d.drawImage(playerRightImage, PLAYER_X + mouseFromMiddleX / -20, PLAYER_Y + mouseFromMiddleY / -20,
                PLAYER_WIDTH, PLAYER_HEIGHT, null);
          } else {
            g2d.drawImage(playerLeftImage, PLAYER_X + mouseFromMiddleX / -20, PLAYER_Y + mouseFromMiddleY / -20,
                PLAYER_WIDTH, PLAYER_HEIGHT, null);
          }
          if (p1GunType == 0) {
            if (p1RightLooked) {
              g2d.rotate(Math.toRadians(p1GunAngle), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
              g2d.drawImage(rightHandgun, PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2 + 10,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_WIDTH / 2 + 10, 45, 30, null);
              g2d.rotate(Math.toRadians(360 - p1GunAngle), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
            } else if (p1GunAngle != 90 || p1GunAngle != -90) {
              g2d.rotate(Math.toRadians(p1GunAngle + 180), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
              g2d.drawImage(leftHandgun, PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2 + 10,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_WIDTH / 2 - 5, 45, 30, null);
              g2d.rotate(Math.toRadians(180 - p1GunAngle), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
            }
          } else if (p1GunType == 1) {
            if (p1RightLooked) {
              g2d.rotate(Math.toRadians(p1GunAngle), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
              g2d.drawImage(rightDesertEagle, PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2 + 10,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_WIDTH / 2 + 10, 50, 30, null);
              g2d.rotate(Math.toRadians(360 - p1GunAngle), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
            } else if (p1GunAngle != 90 || p1GunAngle != -90) {
              g2d.rotate(Math.toRadians(p1GunAngle + 180), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
              g2d.drawImage(leftDesertEagle, PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2 + 10,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_WIDTH / 2 - 5, 50, 30, null);
              g2d.rotate(Math.toRadians(180 - p1GunAngle), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
            }
          } else if (p1GunType == 2) {
            if (p1RightLooked) {
              g2d.rotate(Math.toRadians(p1GunAngle), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
              g2d.drawImage(rightMicroUzi, PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_WIDTH / 2 + 10, 60, 50, null);
              g2d.rotate(Math.toRadians(360 - p1GunAngle), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
            } else if (p1GunAngle != 90 || p1GunAngle != -90) {
              g2d.rotate(Math.toRadians(p1GunAngle + 180), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
              g2d.drawImage(leftMicroUzi, PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_WIDTH / 2 - 20, 60, 50, null);
              g2d.rotate(Math.toRadians(180 - p1GunAngle), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
            }
          } else if (p1GunType == 3) {
            if (p1RightLooked) {
              g2d.rotate(Math.toRadians(p1GunAngle), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
              g2d.drawImage(rightAK, PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2 - 20,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_WIDTH / 2 + 10, 110, 35, null);
              g2d.rotate(Math.toRadians(360 - p1GunAngle), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
            } else if (p1GunAngle != 90 || p1GunAngle != -90) {
              g2d.rotate(Math.toRadians(p1GunAngle + 180), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
              g2d.drawImage(leftAK, PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2 - 20,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_WIDTH / 2 - 5, 110, 35, null);
              g2d.rotate(Math.toRadians(180 - p1GunAngle), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
            }
          } else if (p1GunType == 4) {
            if (p1RightLooked) {
              g2d.rotate(Math.toRadians(p1GunAngle), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
              g2d.drawImage(rightBeretta, PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2 - 20,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_WIDTH / 2 + 10, 110, 45, null);
              g2d.rotate(Math.toRadians(360 - p1GunAngle), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
            } else if (p1GunAngle != 90 || p1GunAngle != -90) {
              g2d.rotate(Math.toRadians(p1GunAngle + 180), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
              g2d.drawImage(leftBeretta, PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2 - 20,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_WIDTH / 2 - 15, 110, 45, null);
              g2d.rotate(Math.toRadians(180 - p1GunAngle), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
            }
          } else if (p1GunType == 5) {
            if (p1RightLooked) {
              g2d.rotate(Math.toRadians(p1GunAngle), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
              g2d.drawImage(rightAWM, PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2 - 20,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_WIDTH / 2 + 10, 130, 30, null);
              g2d.rotate(Math.toRadians(360 - p1GunAngle), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
            } else if (p1GunAngle != 90 || p1GunAngle != -90) {
              g2d.rotate(Math.toRadians(p1GunAngle + 180), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
              g2d.drawImage(leftAWM, PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2 - 20,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_WIDTH / 2 - 5, 130, 30, null);
              g2d.rotate(Math.toRadians(180 - p1GunAngle), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
            }
          } else if (p1GunType == 6) {
            if (p1RightLooked) {
              g2d.rotate(Math.toRadians(p1GunAngle), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
              g2d.drawImage(rightLasergun, PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2 - 40,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_WIDTH / 2 + 7, 160, 50, null);
              g2d.rotate(Math.toRadians(360 - p1GunAngle), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
            } else if (p1GunAngle != 90 || p1GunAngle != -90) {
              g2d.rotate(Math.toRadians(p1GunAngle + 180), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
              g2d.drawImage(leftLasergun, PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2 - 40,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_WIDTH / 2 - 20, 160, 50, null);
              g2d.rotate(Math.toRadians(180 - p1GunAngle), PLAYER_X + mouseFromMiddleX / -20 + PLAYER_WIDTH / 2,
                  PLAYER_Y + mouseFromMiddleY / -20 + PLAYER_HEIGHT / 2);
            }
          }

          // AWM

        }
        for (int i = 0; i < objects.size(); i++) {
          if (objects.get(i).yPos + p1Y * -1 + mouseFromMiddleY / -20 + objects.get(i).height == y) {
            g2d.drawImage(objects.get(i).objectImage, objects.get(i).xPos + p1X * -1 + mouseFromMiddleX / -20,
                objects.get(i).yPos + p1Y * -1 + mouseFromMiddleY / -20, objects.get(i).width, objects.get(i).height,
                null);

          }

        }
        /*
         * for(GunBullet currentBullet : p1GunBullets)
         * {
         * if(currentBullet.yPos == y)
         * {
         * if(currentBullet.playerRightLooked)
         * {
         * g2d.rotate(Math.toRadians(currentBullet.gunAngle), currentBullet.xPos,
         * currentBullet.yPos);
         * g2d.drawImage(bulletImage, currentBullet.xPos, currentBullet.yPos, 14, 7,
         * null);
         * g2d.rotate(Math.toRadians(360 - currentBullet.gunAngle), currentBullet.xPos,
         * currentBullet.yPos);
         * }
         * else if(currentBullet.gunAngle != 90 || currentBullet.gunAngle != -90)
         * {
         * g2d.rotate(Math.toRadians(currentBullet.gunAngle + 180), currentBullet.xPos,
         * currentBullet.yPos);
         * g2d.drawImage(bulletImage, currentBullet.xPos + 10, currentBullet.yPos, 14,
         * 7, null);
         * g2d.rotate(Math.toRadians(180 - currentBullet.gunAngle), currentBullet.xPos,
         * currentBullet.yPos);
         * }
         * }
         * }
         */

        for (Zombie zombie : zombies) {
          if (y == (int) (zombie.yPos + zombie.height + p1Y * -1 + mouseFromMiddleY / -20)) {
            if (!zombie.hurt) {
              if (zombie.lookRight) {
                g2d.drawImage(zombie.rightZombieImage, (int) zombie.xPos + p1X * -1 + mouseFromMiddleX / -20,
                    (int) zombie.yPos + p1Y * -1 + mouseFromMiddleY / -20, zombie.width, zombie.height, null);
              } else {
                g2d.drawImage(zombie.leftZombieImage, (int) zombie.xPos + p1X * -1 + mouseFromMiddleX / -20,
                    (int) zombie.yPos + p1Y * -1 + mouseFromMiddleY / -20, zombie.width, zombie.height, null);
              }
            } else {
              if (zombie.lookRight) {
                g2d.drawImage(zombie.rightHurtZombieImage, (int) zombie.xPos + p1X * -1 + mouseFromMiddleX / -20,
                    (int) zombie.yPos + p1Y * -1 + mouseFromMiddleY / -20, zombie.width, zombie.height, null);
              } else {
                g2d.drawImage(zombie.leftHurtZombieImage, (int) zombie.xPos + p1X * -1 + mouseFromMiddleX / -20,
                    (int) zombie.yPos + p1Y * -1 + mouseFromMiddleY / -20, zombie.width, zombie.height, null);
              }
            }
          }
        }
        for (Survivor survivor : survivors) {
          if (survivor.yPos + p1Y * -1 + mouseFromMiddleY / -20 + survivor.height == y) {
            if (survivor.lookRight) {
              g2d.drawImage(survivor.rightSurvivorImage, survivor.xPos + p1X * -1 + mouseFromMiddleX / -20,
                  survivor.yPos + p1Y * -1 + mouseFromMiddleY / -20, survivor.width, survivor.height, null);
            } else {
              g2d.drawImage(survivor.leftSurvivorImage, survivor.xPos + p1X * -1 + mouseFromMiddleX / -20,
                  survivor.yPos + p1Y * -1 + mouseFromMiddleY / -20, survivor.width, survivor.height, null);
            }
          }
        }
      }

      g2d.setColor(Color.red);
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
      if (!gameEnded)
        g2d.fillRect(15, 0, (int) (p1Health * 4.5), 80);
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
      if (!gameEnded)
        g2d.drawImage(healthBar, 0, 0, 500, 90, null);
      g2d.setFont(new Font("Sans serif", Font.BOLD, 15));
      g2d.setColor(Color.GRAY);
      if (p1GunType == 0 || p1GunType == 6)
        g2d.drawString("Infinite", 85, 75);
      else
        g2d.drawString((bullet.maxAmmo - bullet.ammoAmount) + "", 85, 75);

      g2d.setColor(Color.BLUE);
      if (p1InvisTimer > 1) {
        g2d.drawImage(invisScreen, 0, 0, FRAME_WIDTH, FRAME_HEIGHT, null);
      }

      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
      g2d.setFont(new Font("Sans serif", Font.BOLD, 25));
      g2d.setFont(new Font("Arial", Font.PLAIN, 10));

      g2d.setFont(new Font("Sans serif", Font.BOLD, 25));
      g2d.setColor(Color.black);
      if (bullet.currentlyReloading) {
        g2d.drawString("Reloading...", PLAYER_X - 20, PLAYER_Y);
      }
      for (RainDropletsGame currentDrop : rain) {
        g2d.drawImage(raindropImage, currentDrop.xPos + p2X * -1, currentDrop.yPos + p1Y * -1 + mouseFromMiddleY / -20,
            13, 25, null);
      }
      g2d.setColor(Color.black);
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
      g2d.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);

      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f * (10 - p1HurtPerMS)));
      g2d.drawImage(bloodStainScreen, 0, -30, FRAME_WIDTH, FRAME_HEIGHT + 110, null);
      g2d.setFont(new Font("Sans serif", Font.PLAIN, 10));

    } else {
      g2d.drawImage(background, p2X * -1, p2Y * -1, (int) FRAME_WIDTH * 3, FRAME_HEIGHT * 3, null);
      g2d.setColor(Color.YELLOW);

      for (GunBullet currentBullet : p2GunBullets) {
        if (currentBullet.playerRightLooked) {
          g2d.rotate(Math.toRadians(currentBullet.gunAngle), currentBullet.xPosFromPlayer + p2X * -1,
              currentBullet.yPosFromPlayer + p2Y * -1);
          g2d.drawImage(bulletImage, currentBullet.xPosFromPlayer + p2X * -1, currentBullet.yPosFromPlayer + p2Y * -1,
              14, 7, null);
          g2d.rotate(Math.toRadians(360 - currentBullet.gunAngle), currentBullet.xPosFromPlayer + p2X * -1,
              currentBullet.yPosFromPlayer + p2Y * -1);
        } else if (currentBullet.gunAngle != 90 || currentBullet.gunAngle != -90) {
          g2d.rotate(Math.toRadians(currentBullet.gunAngle + 180), currentBullet.xPosFromPlayer + p2X * -1,
              currentBullet.yPosFromPlayer + p2Y * -1);
          g2d.drawImage(bulletImage, currentBullet.xPosFromPlayer + p2X * -1 + 10,
              currentBullet.yPosFromPlayer + p2Y * -1, 14, 7, null);
          g2d.rotate(Math.toRadians(180 - currentBullet.gunAngle), currentBullet.xPosFromPlayer + p2X * -1,
              currentBullet.yPosFromPlayer + p2Y * -1);
        }
      }

      for (int y = -500; y < FRAME_HEIGHT + 500; y++) {
        for (Zombie zombie : zombies) {
          g2d.setColor(Color.white);
          g2d.setFont(new Font("Sans serif", Font.BOLD, 15));
          if ((int) (zombie.yPos + p2Y * -1 + zombie.height) == y) {
            g2d.drawString("Health: " + zombie.health, (int) zombie.xPos + p2X * -1, (int) zombie.yPos + p2Y * -1);
            if (!zombie.hurt) {
              if (zombie.lookRight) {
                g2d.drawImage(zombie.rightZombieImage, (int) zombie.xPos + p2X * -1, (int) zombie.yPos + p2Y * -1,
                    zombie.width, zombie.height, null);
              } else {
                g2d.drawImage(zombie.leftZombieImage, (int) zombie.xPos + p2X * -1, (int) zombie.yPos + p2Y * -1,
                    zombie.width, zombie.height, null);
              }
            } else {
              if (zombie.lookRight) {
                g2d.drawImage(zombie.rightHurtZombieImage, (int) zombie.xPos + p2X * -1, (int) zombie.yPos + p2Y * -1,
                    zombie.width, zombie.height, null);
              } else {
                g2d.drawImage(zombie.leftHurtZombieImage, (int) zombie.xPos + p2X * -1, (int) zombie.yPos + p2Y * -1,
                    zombie.width, zombie.height, null);
              }
            }
          }
        }
        if (PLAYER_Y + PLAYER_HEIGHT == y && !gameEnded) {

          g2d.drawRect(PLAYER_X + mouseFromMiddleX / -20, PLAYER_Y + mouseFromMiddleY / -20, 50, 50);
          if (p2RightLooked) {
            g2d.drawImage(playerRightBlueImage, PLAYER_X + mouseFromMiddleX / -20, PLAYER_Y + mouseFromMiddleY / -20,
                PLAYER_WIDTH, PLAYER_HEIGHT, null);
          } else {
            g2d.drawImage(playerLeftBlueImage, PLAYER_X + mouseFromMiddleX / -20, PLAYER_Y + mouseFromMiddleY / -20,
                PLAYER_WIDTH, PLAYER_HEIGHT, null);
          }

          if (p2GunType == 0) {
            if (p2RightLooked) {
              g2d.rotate(Math.toRadians(p2GunAngle), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(rightHandgun, PLAYER_X + PLAYER_WIDTH / 2 + 10, PLAYER_Y + PLAYER_WIDTH / 2 + 10, 45, 30,
                  null);
              g2d.rotate(Math.toRadians(360 - p2GunAngle), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
            } else if (p2GunAngle != 90 || p2GunAngle != -90) {
              g2d.rotate(Math.toRadians(p2GunAngle + 180), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(leftHandgun, PLAYER_X + PLAYER_WIDTH / 2 + 10, PLAYER_Y + PLAYER_WIDTH / 2 - 5, 45, 30,
                  null);
              g2d.rotate(Math.toRadians(180 - p2GunAngle), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
            }
          } else if (p2GunType == 1) {
            if (p2RightLooked) {
              g2d.rotate(Math.toRadians(p2GunAngle), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(rightDesertEagle, PLAYER_X + PLAYER_WIDTH / 2 + 10, PLAYER_Y + PLAYER_WIDTH / 2 + 10, 50,
                  30, null);
              g2d.rotate(Math.toRadians(360 - p2GunAngle), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
            } else if (p2GunAngle != 90 || p2GunAngle != -90) {
              g2d.rotate(Math.toRadians(p2GunAngle + 180), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(leftDesertEagle, PLAYER_X + PLAYER_WIDTH / 2 + 10, PLAYER_Y + PLAYER_WIDTH / 2 - 5, 50, 30,
                  null);
              g2d.rotate(Math.toRadians(180 - p2GunAngle), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
            }
          } else if (p2GunType == 2) {
            if (p2RightLooked) {
              g2d.rotate(Math.toRadians(p2GunAngle), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(rightMicroUzi, PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_WIDTH / 2 + 10, 60, 50, null);
              g2d.rotate(Math.toRadians(360 - p2GunAngle), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
            } else if (p2GunAngle != 90 || p2GunAngle != -90) {
              g2d.rotate(Math.toRadians(p2GunAngle + 180), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(leftMicroUzi, PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_WIDTH / 2 - 20, 60, 50, null);
              g2d.rotate(Math.toRadians(180 - p2GunAngle), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
            }
          } else if (p2GunType == 3) {
            if (p2RightLooked) {
              g2d.rotate(Math.toRadians(p2GunAngle), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(rightAK, PLAYER_X + PLAYER_WIDTH / 2 - 20, PLAYER_Y + PLAYER_WIDTH / 2 + 10, 110, 35, null);
              g2d.rotate(Math.toRadians(360 - p2GunAngle), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
            } else if (p2GunAngle != 90 || p2GunAngle != -90) {
              g2d.rotate(Math.toRadians(p2GunAngle + 180), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(leftAK, PLAYER_X + PLAYER_WIDTH / 2 - 20, PLAYER_Y + PLAYER_WIDTH / 2 - 5, 110, 35, null);
              g2d.rotate(Math.toRadians(180 - p2GunAngle), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
            }
          } else if (p2GunType == 4) {
            if (p2RightLooked) {
              g2d.rotate(Math.toRadians(p2GunAngle), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(rightBeretta, PLAYER_X + PLAYER_WIDTH / 2 - 20, PLAYER_Y + PLAYER_WIDTH / 2 + 10, 110, 45,
                  null);
              g2d.rotate(Math.toRadians(360 - p2GunAngle), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
            } else if (p2GunAngle != 90 || p2GunAngle != -90) {
              g2d.rotate(Math.toRadians(p2GunAngle + 180), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(leftBeretta, PLAYER_X + PLAYER_WIDTH / 2 - 20, PLAYER_Y + PLAYER_WIDTH / 2 - 15, 110, 45,
                  null);
              g2d.rotate(Math.toRadians(180 - p2GunAngle), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
            }
          } else if (p2GunType == 5) {
            if (p2RightLooked) {
              g2d.rotate(Math.toRadians(p2GunAngle), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(rightAWM, PLAYER_X + PLAYER_WIDTH / 2 - 20, PLAYER_Y + PLAYER_WIDTH / 2 + 10, 130, 30,
                  null);
              g2d.rotate(Math.toRadians(360 - p2GunAngle), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
            } else if (p2GunAngle != 90 || p2GunAngle != -90) {
              g2d.rotate(Math.toRadians(p2GunAngle + 180), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(leftAWM, PLAYER_X + PLAYER_WIDTH / 2 - 20, PLAYER_Y + PLAYER_WIDTH / 2 - 5, 130, 30, null);
              g2d.rotate(Math.toRadians(180 - p2GunAngle), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
            }
          } else if (p2GunType == 6) {
            if (p2RightLooked) {
              g2d.rotate(Math.toRadians(p2GunAngle), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(rightLasergun, PLAYER_X + PLAYER_WIDTH / 2 - 40, PLAYER_Y + PLAYER_WIDTH / 2 + 7, 160, 50,
                  null);
              g2d.rotate(Math.toRadians(360 - p2GunAngle), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
            } else if (p2GunAngle != 90 || p2GunAngle != -90) {
              g2d.rotate(Math.toRadians(p2GunAngle + 180), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
              g2d.drawImage(leftLasergun, PLAYER_X + PLAYER_WIDTH / 2 - 40, PLAYER_Y + PLAYER_WIDTH / 2 - 20, 160, 50,
                  null);
              g2d.rotate(Math.toRadians(180 - p2GunAngle), PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2);
            }
          }

        }

        for (int i = 0; i < objects.size(); i++) {
          if (objects.get(i).yPos + objects.get(i).height + p2Y * -1 == y) {
            g2d.drawImage(objects.get(i).objectImage, objects.get(i).xPos + p2X * -1, objects.get(i).yPos + p2Y * -1,
                objects.get(i).width, objects.get(i).height, null);
          }
        }
        for (Survivor survivor : survivors) {
          if (survivor.yPos + survivor.height + p2Y * -1 == y) {
            if (survivor.lookRight) {
              g2d.drawImage(survivor.rightSurvivorImage, survivor.xPos + p2X * -1, survivor.yPos + p2Y * -1,
                  survivor.width, survivor.height, null);
            } else {
              g2d.drawImage(survivor.rightSurvivorImage, survivor.xPos + p2X * -1, survivor.yPos + p2Y * -1,
                  survivor.width, survivor.height, null);
            }
          }
        }

      }

      for (int i = 0; i < survivors.size(); i++) {
        g2d.setFont(new Font("Sans serif", Font.PLAIN, 10));
      }
      g2d.setColor(Color.red);
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
      if (!gameEnded)
        g2d.fillRect(15, 0, (int) (p2Health * 4.5), 80);
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

      if (!gameEnded)
        g2d.drawImage(healthBar, 0, 0, 500, 90, null);

      g.setFont(new Font("Sans serif", Font.BOLD, 25));

      for (RainDropletsGame currentDrop : rain) {
        g2d.drawImage(raindropImage, currentDrop.xPos + p1X * -1, currentDrop.yPos + p1Y * -1, 13, 25, null);
      }
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

      if (!gameEnded)
        g2d.drawImage(healthBar, 0, 0, 500, 90, null);

      g2d.setColor(Color.BLUE);
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f * (10 - p2HurtPerMS)));
      g2d.drawImage(bloodStainScreen, 0, -30, FRAME_WIDTH, FRAME_HEIGHT + 110, null);
      g2d.setColor(Color.black);
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
      g2d.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
      g2d.setColor(Color.BLUE);
      g2d.setFont(new Font("Arial", Font.BOLD, 10));
    }
    g2d.setColor(Color.yellow);
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));// fadeInGame));
    g2d.setFont(new Font("Sans serif", Font.BOLD, 50));
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeInGame));// fadeInGame));
    g2d.setColor(Color.BLACK);
    g2d.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));// fadeInGame));

    timeString = "";
    if (defeatedTime % 60000 / 6000 > 9) {
      timeString += defeatedTime % 60000 / 6000 + ":";
    } else {
      timeString += "0" + defeatedTime % 60000 / 6000 + ":";
    }
    if (defeatedTime % 6000 / 100 > 9) {
      timeString += defeatedTime % 6000 / 100 + ":";
    } else {
      timeString += "0" + defeatedTime % 6000 / 100 + ":";
    }
    if (defeatedTime % 6000 % 100 > 9) {
      timeString += defeatedTime % 6000 % 100;
    } else {
      timeString += "0" + defeatedTime % 6000 % 100;
    }
    if (gameEnded) {
      g2d.fill(levelSelectButton);
      if (victory) {
        g2d.drawImage(victoryScreen, 400, 100, 500, 600, null);
        g2d.drawString("Time: " + timeString, 420, 260);
        if (zombiesKilled < 100) {
          g2d.drawString("Killed: " + zombiesKilled, 420, 310);
        } else {
          g2d.drawString("Killed: 100+", 420, 310);
        }
        g2d.drawString("Money: " + gold, 420, 360);
        g2d.drawImage(okVictory, levelSelectButton.x, levelSelectButton.y, levelSelectButton.width,
            levelSelectButton.height, null);
      } else {
        gold = 0;
        g2d.drawImage(defeatScreen, 400, 100, 500, 600, null);
        g2d.drawString("Time: " + timeString, 420, 260);
        if (zombiesKilled < 100) {
          g2d.drawString("Killed: " + zombiesKilled, 420, 310);
        } else {
          g2d.drawString("Killed: 100+", 420, 310);
        }
        g2d.drawString("Money: " + gold, 420, 360);
        g2d.drawImage(okDefeat, levelSelectButton.x, levelSelectButton.y, levelSelectButton.width,
            levelSelectButton.height, null);
      }
      g2d.setFont(new Font("Sans serif", Font.PLAIN, 15));
    }
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));// fadeInGame));

    /////////////////////////////////////////////

    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));// fadeInGame));
    if (!singlePlayer)
      if (!server.connectionStatus)
        g2d.drawImage(exitClientSign, 420, 210, 500, 240, null);

  }

  public void updateAllSurvivors() {
    /*
     * updates all the survivors with player coordinates
     */

    for (int i = 199; i > 0; i--) {
      playerOneStepsX.set(i, playerOneStepsX.get(i - 1));
    }
    playerOneStepsX.set(0, p1X * -1);

    for (int i = 199; i > 0; i--) {
      playerOneStepsY.set(i, playerOneStepsY.get(i - 1));
    }
    playerOneStepsY.set(0, p1Y * -1);
    if (!singlePlayer) {
      for (int i = 0; i < playerTwoStepsXArray.length; i++) {
        playerTwoStepsX.set(i, playerTwoStepsXArray[i]);
        playerTwoStepsY.set(i, playerTwoStepsYArray[i]);
      }
    }
    survivorXPos.clear();
    survivorYPos.clear();
    survivorType.clear();
    survivorLookRight.clear();
    for (int i = 0; i < objects.size(); i++) {
      for (int x = 0; x < survivors.size(); x++) {
        if (survivors.get(x).hitboxX + survivors.get(x).hitboxWidth + 10 > objects.get(i).hitboxX
            && survivors.get(x).hitboxX - 10 < objects.get(i).hitboxX + objects.get(i).width) {
          if (survivors.get(x).hitboxY - 10 < objects.get(i).hitboxY + objects.get(i).hitboxHeight
              && survivors.get(x).hitboxY + survivors.get(x).hitboxHeight + 5 > objects.get(i).hitboxY)
            if (objects.get(i).image.equals("Helicopter") || objects.get(i).image.equals("Helicopter")) {
              if (survivors.get(x).following == 1)
                p1SurvivorsRescued--;
              else if (survivors.get(x).following == 2)
                p2SurvivorsRescued--;

              if (survivors.get(x).type == 4) {
                gold += 55;
              } else {
                gold += 30;
              }

              survivors.remove(x);
              numRescued--;

            }
        }
      }
    }

    for (Survivor survivor : survivors) {
      survivor.update(p1X, p1Y, p2X, p2Y, playerOneStepsX, playerOneStepsY, playerTwoStepsX, playerTwoStepsY,
          p1SurvivorsRescued, p2SurvivorsRescued, survivorCount);
      if (survivor.saved && !survivor.countedSurvivor) {
        survivor.countedSurvivor = true;
        if (survivor.following == 1)
          p1SurvivorsRescued++;
        else
          p2SurvivorsRescued++;
      }
      survivorXPos.add(survivor.xPos);
      survivorYPos.add(survivor.yPos);
      survivorType.add(survivor.type);
      survivorLookRight.add(survivor.lookRight);
    }

    if (!singlePlayer) {
      server.survivorXPos = new int[survivorXPos.size()];
      server.survivorYPos = new int[survivorYPos.size()];
      server.survivorType = new int[survivorType.size()];
      server.survivorLookRight = new boolean[survivorLookRight.size()];

      for (int i = 0; i < survivorXPos.size(); i++) {
        server.survivorXPos[i] = survivorXPos.get(i);
        server.survivorYPos[i] = survivorYPos.get(i);
        server.survivorType[i] = survivorType.get(i);
        server.survivorLookRight[i] = survivorLookRight.get(i);
      }
    }
  }

  public void updateAllZombies() {
    /*
     * updates all zombies with player coordinates
     * also spawns zombies from all 4 sides
     */
    if (zombies.size() < zombiesAtOnce) {
      sidesSpawn = (int) (Math.random() * 4);
      if (Math.random() * 100 < 40) {
        if (sidesSpawn == 0) {
          zombies.add(new Zombie(1 + (int) (Math.random() * zombieLimit), (int) (Math.random() * FRAME_WIDTH * 3), 0));
        } else if (sidesSpawn == 1) {
          zombies.add(new Zombie(1 + (int) (Math.random() * zombieLimit), FRAME_WIDTH * 3,
              (int) (Math.random() * FRAME_HEIGHT * 3)));
        } else if (sidesSpawn == 2) {
          zombies.add(new Zombie(1 + (int) (Math.random() * zombieLimit), (int) (Math.random() * FRAME_WIDTH * 3),
              FRAME_HEIGHT * 3));
        } else {
          zombies.add(new Zombie(1 + (int) (Math.random() * zombieLimit), 0, (int) (Math.random() * FRAME_HEIGHT * 3)));
        }
      }
    }

    for (Zombie zombie : zombies) {
      zombie.update(p1X + PLAYER_X + PLAYER_WIDTH / 2, p1Y + PLAYER_Y + PLAYER_HEIGHT / 2,
          p2X + PLAYER_X + PLAYER_WIDTH / 2, p2Y + PLAYER_Y + PLAYER_HEIGHT / 2, objects, p1InvisTimer, p2InvisTimer);
      zombie.zombieHitbox.setFrame(p1X * -1 + (int) zombie.xPos, p1Y * -1 + (int) zombie.yPos, zombie.width,
          zombie.height);// = new Rectangle2D(p1X * -1 + (int)zombie.xPos, p1Y * -1 + (int)zombie.yPos,
                         // zombie.width, zombie.height);
    }
    for (int i = 0; i < zombies.size(); i++) {
      if (zombies.get(i).die) {
        if (zombies.get(i).type == 1) {
          gold += 30;
        }
        if (zombies.get(i).type == 2) {
          gold += 35;
        }
        if (zombies.get(i).type == 3) {
          gold += 35;
        }
        if (zombies.get(i).type == 4) {
          gold += 40;
        }
        if (zombies.get(i).type == 5) {
          gold += 60;
        }
        if (zombies.get(i).type == 4) {
          gold += 80;
        }

        zombies.remove(i);
        zombiesKilled++;
      }
    }
    zombieXPos.clear();
    zombieYPos.clear();
    zombieHurt.clear();
    zombieHealth.clear();
    zombieType.clear();
    zombieLookRight.clear();
    for (Zombie zombie : zombies) {
      zombieXPos.add((int) zombie.xPos);
      zombieYPos.add((int) zombie.yPos);
      zombieHealth.add(zombie.health);
      zombieHurt.add(zombie.hurt);
      zombieType.add(zombie.type);
      zombieLookRight.add(zombie.lookRight);
    }
    if (!singlePlayer) {
      server.zombieXPos = new int[zombies.size()];
      server.zombieYPos = new int[zombies.size()];
      server.zombieHealth = new int[zombies.size()];
      server.zombieHurt = new boolean[zombies.size()];
      server.zombieType = new int[zombies.size()];
      server.zombieLookRight = new boolean[zombies.size()];
      for (int i = 0; i < zombies.size(); i++) {
        server.zombieXPos[i] = zombieXPos.get(i);
        server.zombieYPos[i] = zombieYPos.get(i);
        server.zombieHealth[i] = zombieHealth.get(i);
        server.zombieHurt[i] = zombieHurt.get(i);
        server.zombieType[i] = zombieType.get(i);
        server.zombieLookRight[i] = zombieLookRight.get(i);
      }
    }
  }

  public void updateAllRainDroplets() {
    /*
     * This makes sure every 3 ms, a bullet is created
     * and is added to the array list rain. The timer
     * calls the update method for each rain so that
     * the position of each raindrop changes.
     */
    if (Math.random() * 100 < 10)
      rain.add(new RainDropletsGame());
    for (RainDropletsGame drop : rain) {
      drop.update();
    }
    for (int i = 0; i < rain.size(); i++) {
      if (rain.get(i).deleteThisClone) {
        rain.remove(i);
      }
    }
  }

  public void setUpBulletCoordinatesPlayerOne() {
    /*
     * Sets the angle of the gun through geometry.
     * I thought of an idea where I can draw a
     * right triangle from the middle of the screen
     * to the mouse. Then I can use atan to find
     * the angle of the gun. Also checks if the
     * player is looking right or left
     */
    if (mouseX >= PLAYER_X + PLAYER_WIDTH / 2) {
      p1RightLooked = true;
      p1LeftLooked = false;
    } else {
      p1LeftLooked = true;
      p1RightLooked = false;
    }

    if (mouseX - PLAYER_X != 0) {
      p1GunAngle = Math.toDegrees(Math.atan(
          (((double) mouseY - PLAYER_Y - PLAYER_HEIGHT / 2) / (((double) mouseX - PLAYER_X - PLAYER_WIDTH / 2)))));
    }
    if (bullet.showBullet) {
      p1GunBullets.add(new GunBullet(p1GunAngle, p1X, p1Y, PLAYER_X + PLAYER_WIDTH / 2 + p1X,
          PLAYER_Y + PLAYER_HEIGHT / 2 + p1Y, p1RightLooked, p1BulletDistance));
      bullet.showBullet = false;
    }
    if (p1RightLooked) {
      p1BulletCoorX[0] = PLAYER_X + PLAYER_WIDTH / 2;
      p1BulletCoorY[0] = PLAYER_Y + PLAYER_HEIGHT / 2;

      p1BulletCoorX[1] = PLAYER_X + PLAYER_WIDTH / 2 + (int) (bulletLength * Math.cos(Math.toRadians(p1GunAngle)));
      p1BulletCoorY[1] = PLAYER_Y + PLAYER_HEIGHT / 2 + (int) (bulletLength * Math.sin(Math.toRadians(p1GunAngle)));

      p1BulletCoorX[2] = PLAYER_X + PLAYER_WIDTH / 2 + (int) (bulletLength * Math.cos(Math.toRadians(p1GunAngle)))
          + (int) (bulletWidth * Math.cos(Math.toRadians(p1GunAngle + 90)));
      p1BulletCoorY[2] = PLAYER_Y + PLAYER_HEIGHT / 2 + (int) (bulletLength * Math.sin(Math.toRadians(p1GunAngle))
          + (int) (bulletWidth * Math.sin(Math.toRadians(p1GunAngle + 90))));

      p1BulletCoorX[3] = PLAYER_X + PLAYER_WIDTH / 2 + (int) (bulletWidth * Math.cos(Math.toRadians(p1GunAngle + 90)));
      p1BulletCoorY[3] = PLAYER_Y + PLAYER_HEIGHT / 2 + (int) (bulletWidth * Math.sin(Math.toRadians(p1GunAngle + 90)));
    } else if (p1LeftLooked && p1GunAngle != -90 && p1GunAngle != 90) {
      p1BulletCoorX[0] = PLAYER_X + PLAYER_WIDTH / 2;
      p1BulletCoorY[0] = PLAYER_Y + PLAYER_HEIGHT / 2;

      p1BulletCoorX[1] = PLAYER_X + PLAYER_WIDTH / 2
          + ((int) (bulletLength * Math.cos(Math.toRadians(p1GunAngle)))) * -1;
      p1BulletCoorY[1] = PLAYER_Y + PLAYER_HEIGHT / 2
          + ((int) (bulletLength * Math.sin(Math.toRadians(p1GunAngle)))) * -1;

      p1BulletCoorX[2] = PLAYER_X + PLAYER_WIDTH / 2 + ((int) (bulletLength * Math.cos(Math.toRadians(p1GunAngle)))
          + (int) (bulletWidth * Math.cos(Math.toRadians(p1GunAngle + 90)))) * -1;
      p1BulletCoorY[2] = PLAYER_Y + PLAYER_HEIGHT / 2 + ((int) (bulletLength * Math.sin(Math.toRadians(p1GunAngle))
          + (int) (bulletWidth * Math.sin(Math.toRadians(p1GunAngle + 90))))) * -1;

      p1BulletCoorX[3] = PLAYER_X + PLAYER_WIDTH / 2
          + ((int) (bulletWidth * Math.cos(Math.toRadians(p1GunAngle + 90)))) * -1;
      p1BulletCoorY[3] = PLAYER_Y + PLAYER_HEIGHT / 2
          + ((int) (bulletWidth * Math.sin(Math.toRadians(p1GunAngle + 90)))) * -1;
    }
    p1Bullet = new Polygon(p1BulletCoorX, p1BulletCoorY, 4);
  }

  public void setUpBulletCoordinatesPlayerTwo() {
    /*
     * This sets the bullet coordinates of the OLD bullet.
     * This is the bullet that is a straight line. I do
     * not show this bullet anymore because I have a better
     * bullet.
     */
    p2BulletCoorX[0] = server.p2BulletCoorX[0] + p2X + p1X * -1;
    p2BulletCoorY[0] = server.p2BulletCoorY[0] + p2Y + p1Y * -1;

    p2BulletCoorX[1] = server.p2BulletCoorX[1] + p2X + p1X * -1;
    p2BulletCoorY[1] = server.p2BulletCoorY[1] + p2Y + p1Y * -1;

    p2BulletCoorX[2] = server.p2BulletCoorX[2] + p2X + p1X * -1;
    p2BulletCoorY[2] = server.p2BulletCoorY[2] + p2Y + p1Y * -1;

    p2BulletCoorX[3] = server.p2BulletCoorX[3] + p2X + p1X * -1;
    p2BulletCoorY[3] = server.p2BulletCoorY[3] + p2Y + p1Y * -1;

    p2Bullet = new Polygon(p2BulletCoorX, p2BulletCoorY, 4);
  }

  public void playerMovement() {
    /*
     * This is where the movement is controlled.
     * When the keys are pressed, I make the xpos
     * and ypos of the player move. I also sense
     * if the player is touching each object
     * through a for loop.
     */

    /*
     * for(int i = 0; i < objects.size(); i++)
     * {
     * if(objects.get(i).hitbox.intersects(playerHitbox))
     * {
     * touchingObject = true;
     * }
     * else
     * {
     * touchingObject = false;
     * }
     * }
     */
    /*
     * 
     * 
     */
    if (!gameEnded) {
      if (upKey) {
        p1Y -= 4;
        for (int i = 0; i < objects.size(); i++) {
          if (objects.get(i).hitboxX + p1X * -1 < PLAYER_HITBOX_X + PLAYER_HITBOX_WIDTH
              && objects.get(i).hitboxX + objects.get(i).hitboxWidth + p1X * -1 > PLAYER_HITBOX_X
              && objects.get(i).hitboxY + objects.get(i).hitboxHeight + p1Y * -1 + 1 > PLAYER_HITBOX_Y
              && objects.get(i).hitboxY + p1Y * -1 - 1 < PLAYER_HITBOX_Y + PLAYER_HITBOX_HEIGHT) {
            if (!objects.get(i).image.equals("Powerup") && !objects.get(i).image.equals("Powerup")) {
              p1Y += 4;
            }
            if (objects.get(i).image.equals("Powerup") || objects.get(i).image.equals("Powerup")) {
              objects.remove(i);
              p1InvisTimer = 100;
            }
          }
        }
      }

      if (downKey) {
        p1Y += 4;
        for (int i = 0; i < objects.size(); i++) {
          if (objects.get(i).hitboxX + p1X * -1 < PLAYER_HITBOX_X + PLAYER_HITBOX_WIDTH
              && objects.get(i).hitboxX + objects.get(i).hitboxWidth + p1X * -1 > PLAYER_HITBOX_X
              && objects.get(i).hitboxY + objects.get(i).hitboxHeight + p1Y * -1 + 1 > PLAYER_HITBOX_Y
              && objects.get(i).hitboxY + p1Y * -1 - 1 < PLAYER_HITBOX_Y + PLAYER_HITBOX_HEIGHT) {
            if (!objects.get(i).image.equals("Powerup") && !objects.get(i).image.equals("Powerup")) {
              p1Y -= 4;
            }
            if (objects.get(i).image.equals("Powerup") || objects.get(i).image.equals("Powerup")) {
              objects.remove(i);
              p1InvisTimer = 100;
            }
          }
        }
      }
      if (leftKey) {
        p1X -= 4;
        for (int i = 0; i < objects.size(); i++) {
          if (objects.get(i).hitboxX + p1X * -1 - 1 < PLAYER_HITBOX_X + PLAYER_HITBOX_WIDTH
              && objects.get(i).hitboxX + objects.get(i).hitboxWidth + p1X * -1 + 1 > PLAYER_HITBOX_X
              && objects.get(i).hitboxY + objects.get(i).hitboxHeight + p1Y * -1 > PLAYER_HITBOX_Y
              && objects.get(i).hitboxY + p1Y * -1 < PLAYER_HITBOX_Y + PLAYER_HITBOX_HEIGHT) {
            if (!objects.get(i).image.equals("Powerup") && !objects.get(i).image.equals("Powerup")) {
              p1X += 4;
            }
            if (objects.get(i).image.equals("Powerup") || objects.get(i).image.equals("Powerup")) {
              objects.remove(i);
              p1InvisTimer = 100;
            }

          }
        }
      }
      if (rightKey) {
        p1X += 4;
        for (int i = 0; i < objects.size(); i++) {
          if (objects.get(i).hitboxX + p1X * -1 - 1 < PLAYER_HITBOX_X + PLAYER_HITBOX_WIDTH
              && objects.get(i).hitboxX + objects.get(i).hitboxWidth + p1X * -1 + 1 > PLAYER_HITBOX_X
              && objects.get(i).hitboxY + objects.get(i).hitboxHeight + p1Y * -1 > PLAYER_HITBOX_Y
              && objects.get(i).hitboxY + p1Y * -1 < PLAYER_HITBOX_Y + PLAYER_HITBOX_HEIGHT) {
            if (!objects.get(i).image.equals("Powerup") && !objects.get(i).image.equals("Powerup")) {
              p1X -= 4;
            }
            if (objects.get(i).image.equals("Powerup") || objects.get(i).image.equals("Powerup")) {
              objects.remove(i);
              p1InvisTimer = 100;
            }
          }
        }
      }
      for (int i = 0; i < objects.size(); i++) {
        if (objects.get(i).hitboxX + p1X * -1 - 1 < PLAYER_HITBOX_X + PLAYER_HITBOX_WIDTH + p1X * -1 + p2X
            && objects.get(i).hitboxX + objects.get(i).hitboxWidth + p1X * -1 + 1 > PLAYER_HITBOX_X + p1X * -1 + p2X
            && objects.get(i).hitboxY + objects.get(i).hitboxHeight + p1Y * -1 > PLAYER_HITBOX_Y + p1Y * -1 + p2Y
            && objects.get(i).hitboxY + p1Y * -1 < PLAYER_HITBOX_Y + PLAYER_HITBOX_HEIGHT + p1Y * -1 + p2Y) {
          if (objects.get(i).image.equals("Powerup") || objects.get(i).image.equals("Powerup")) {
            objects.remove(i);
            p2InvisTimer = 100;
          }
        }
      }
    }
  }

  public void resetGame() {
    p1SurvivorsRescued = 0;
    p2SurvivorsRescued = 0;
    objects.clear();
    zombies.clear();
    objects.clear();
    backToLevelSelect = false;
    gameEnded = false;
    acceptedInfo = false;
    // Deletes all objects like trees and stuff. Not used yet.
    p1Health = 100;
    p2Health = 100;
    zombiesKilled = 0;
    playerOneInvis = false;
    playerTwoInvis = false;
    gold = 0;
    bullet.ammoAmount = 0;
    p1X = 1355;
    p1Y = 920;

    p2X = (int) ((double) FRAME_WIDTH * 3.0 / 2.0);
    p2Y = (int) ((double) FRAME_HEIGHT * 3.0 / 2.0);

    defeatedTime = 0;

  }

  public void updateAllObjects() {
    /*
     * The xpos and ypos are updated in the server
     * so the client can recieve an updated version
     * of the objects including the xpos, ypos, and
     * the name of the object which determines what
     * type of object is created.
     */
    objectsXPos.clear();
    objectsYPos.clear();
    objectsName.clear();

    for (Objects ob : objects) {
      objectsXPos.add(ob.xPos);
      objectsYPos.add(ob.yPos);
      objectsName.add(ob.name);
    }
    if (!singlePlayer) {
      server.objectsXPos = new int[objectsXPos.size()];
      server.objectsYPos = new int[objectsYPos.size()];
      server.objectsName = new String[objectsName.size()];

      for (int i = 0; i < objectsName.size(); i++) {
        server.objectsXPos[i] = objectsXPos.get(i);
        server.objectsYPos[i] = objectsYPos.get(i);
        server.objectsName[i] = objectsName.get(i);
      }
    }
  }

  public void updateAllBullets() {
    /*
     * Updates the xpos, ypos, player right looked,
     * gun angle, xpos from player, and ypos from player
     * to the server. The client recieves it and
     * the bullets can be created.
     */
    for (GunBullet gb : p1GunBullets) {
      gb.update();
    }
    for (int i = 0; i < p1GunBullets.size(); i++) {
      if (p1GunBullets.get(i).deleteThisClone) {
        p1GunBullets.remove(i);
      }
    }

    p1GunBulletXPos.clear();
    p1GunBulletYPos.clear();
    p1GunBulletPlayerRightLooked.clear();
    p1GunBulletGunAngle.clear();
    p1GunBulletXPosFromPlayer.clear();
    p1GunBulletYPosFromPlayer.clear();

    for (GunBullet gb : p1GunBullets) {
      p1GunBulletXPos.add(gb.xPos);
      p1GunBulletYPos.add(gb.yPos);
      p1GunBulletPlayerRightLooked.add(gb.playerRightLooked);
      p1GunBulletGunAngle.add(gb.gunAngle);
      p1GunBulletXPosFromPlayer.add(gb.xPosFromPlayer);
      p1GunBulletYPosFromPlayer.add(gb.yPosFromPlayer);
    }

    if (!singlePlayer) {
      server.p1GunBulletXPos = new int[p1GunBulletXPos.size()];
      server.p1GunBulletYPos = new int[p1GunBulletYPos.size()];
      server.p1GunBulletGunAngle = new double[p1GunBulletGunAngle.size()];
      server.p1GunBulletPlayerRightLooked = new boolean[p1GunBulletPlayerRightLooked.size()];
      server.p1GunBulletXPosFromPlayer = new int[p1GunBulletXPosFromPlayer.size()];
      server.p1GunBulletYPosFromPlayer = new int[p1GunBulletYPosFromPlayer.size()];

      for (int gb = 0; gb < p1GunBullets.size(); gb++) {
        server.p1GunBulletXPos[gb] = p1GunBulletXPos.get(gb);
        server.p1GunBulletYPos[gb] = p1GunBulletYPos.get(gb);
        server.p1GunBulletGunAngle[gb] = p1GunBulletGunAngle.get(gb);
        server.p1GunBulletPlayerRightLooked[gb] = p1GunBulletPlayerRightLooked.get(gb);
        server.p1GunBulletXPosFromPlayer[gb] = p1GunBulletXPosFromPlayer.get(gb);
        server.p1GunBulletYPosFromPlayer[gb] = p1GunBulletYPosFromPlayer.get(gb);
      }

      p2GunBullets.clear();

      p2GunBulletGunAngle = server.p2GunBulletGunAngle;
      p2GunBulletPlayerRightLooked = server.p2GunBulletPlayerRightLooked;
      p2GunBulletXPos = server.p2GunBulletXPos;
      p2GunBulletYPos = server.p2GunBulletYPos;
      p2GunBulletXPosFromPlayer = server.p2GunBulletXPosFromPlayer;
      p2GunBulletYPosFromPlayer = server.p2GunBulletYPosFromPlayer;

      for (int gb = 0; gb < p2GunBulletGunAngle.length; gb++) {
        p2GunBullets.add(new GunBullet(p2GunBulletGunAngle[gb], p2GunBulletXPos[gb], p2GunBulletYPos[gb],
            p2GunBulletXPosFromPlayer[gb], p2GunBulletYPosFromPlayer[gb], p2GunBulletPlayerRightLooked[gb],
            p1BulletDistance));
      }
    }

  }

  public void bulletSense() {
    /*
     * Checks if bullets are touching zombies
     * if it is, it sets the zombie hurt
     * to true to make it turn red in paint
     * component
     */

    for (Zombie zombie : zombies) {
      oneBulletHit = false;

      for (int i = 0; i < p1GunBullets.size(); i++) {
        if (p1GunBullets.get(i).xPosFromPlayer + p1X * -1 < zombie.xPos + zombie.width + p1X * -1
            && p1GunBullets.get(i).xPosFromPlayer + 7 + p1X * -1 > zombie.xPos + p1X * -1
            && p1GunBullets.get(i).yPosFromPlayer + p1Y * -1 < zombie.yPos + zombie.height + p1Y * -1
            && p1GunBullets.get(i).yPosFromPlayer + 7 + p1Y * -1 > zombie.yPos + p1Y * -1) {
          zombie.health -= p1BulletDamage;
          ;
          oneBulletHit = true;
          if (p1GunType == 0 || p1GunType == 1 || p1GunType == 2) {
            p1GunBullets.remove(i);
          }
        }

      }
      if (p1ShowLaser) {
        if (p1Bullet.intersects(zombie.zombieHitbox)) {
          zombie.health -= p1BulletDamage;
          oneBulletHit = true;
        }
      }
      if (p2ShowLaser) {
        if (p2Bullet.intersects(zombie.zombieHitbox)) {
          zombie.health -= p2BulletDamage;
          oneBulletHit = true;
        }
      }
      /*
       * for(GunBullet p1GunBullet : p1GunBullets)
       * {
       * if(p1GunBullet.xPosFromPlayer + p1X * -1 < zombie.xPos + zombie.width + p1X *
       * -1 && p1GunBullet.xPosFromPlayer + 7 + p1X * -1 > zombie.xPos + p1X * -1 &&
       * p1GunBullet.yPosFromPlayer + p1Y * -1 < zombie.yPos + zombie.height + p1Y *
       * -1 && p1GunBullet.yPosFromPlayer + 7 + p1Y * -1 > zombie.yPos + p1Y * -1)
       * {
       * zombie.health -= p1BulletDamage;;
       * oneBulletHit = true;
       * }
       * }
       */
      for (int i = 0; i < p2GunBullets.size(); i++) {
        if (p2GunBullets.get(i).xPos + PLAYER_X + PLAYER_WIDTH / 2 + p1X * -1 < zombie.xPos + zombie.width + p1X * -1
            && p2GunBullets.get(i).xPos + PLAYER_X + PLAYER_WIDTH / 2 + 7 + p1X * -1 > zombie.xPos + p1X * -1
            && p2GunBullets.get(i).yPos + PLAYER_Y + PLAYER_HEIGHT / 2 + p1Y * -1 < zombie.yPos + zombie.height
                + p1Y * -1
            && p2GunBullets.get(i).yPos + PLAYER_Y + PLAYER_HEIGHT / 2 + 7 + p1Y * -1 > zombie.yPos + p1Y * -1) {
          hurtMultipleTimes = 0;
          oneBulletHit = true;
          if (playerTwoBulletsID[i] != deleteBulletID) {
            hurtMultipleTimes++;
            zombie.health -= p2BulletDamage;
          }
          if (p2GunType == 0 || p2GunType == 1 || p2GunType == 2) {
            deleteBulletID = playerTwoBulletsID[i];
          }
        }
      }

      if (oneBulletHit) {
        zombie.hurt = true;
      } else {
        zombie.hurt = false;
      }
    }
  }

  public void updateGunSettings() {
    // bullet.p1GunType = p1GunType;
    if (p1GunType == 0) {
      p1BulletDistance = 550;
      bullet.reloadTime = 30;
      p1BulletDamage = 9;
      bullet.ammoAmount = 0;
      bullet.maxAmmo = 1000000;
      bullet.ammoReloadTime = 80;
    } else if (p1GunType == 1) {
      p1BulletDistance = 550;
      bullet.reloadTime = 30;
      p1BulletDamage = 11;
      bullet.maxAmmo = 20;
    } else if (p1GunType == 2) {
      p1BulletDistance = 550;
      bullet.reloadTime = 10;
      p1BulletDamage = 3;
      bullet.maxAmmo = 20;
      bullet.ammoReloadTime = 75;
    } else if (p1GunType == 3) {
      p1BulletDistance = 650;
      bullet.reloadTime = 6;
      p1BulletDamage = 1;
      bullet.maxAmmo = 40;
      bullet.ammoReloadTime = 75;
    } else if (p1GunType == 4) {
      p1BulletDistance = 700;
      bullet.reloadTime = 4;
      p1BulletDamage = 2;
      bullet.maxAmmo = 60;
      bullet.ammoReloadTime = 90;
    } else if (p1GunType == 5) {
      p1BulletDistance = 1400;
      bullet.reloadTime = 10;
      p1BulletDamage = 200;
      bullet.maxAmmo = 1;
      bullet.ammoReloadTime = 45;
    } else if (p1GunType == 6) {
      bullet.reloadTime = 1000000000;
      bullet.ammoAmount = 0;
      p1BulletDamage = 10;
    }

  }

  public void setVariablesInServer() {
    /*
     * Sends most of the variables to the server
     * in which the client can receive to process
     * the information.
     */
    server.timeString = timeString;
    server.victory = victory;
    server.gold = gold;
    server.zombiesKilled = zombiesKilled;
    server.p1ShowLaser = p1ShowLaser;
    server.p2InvisTimer = p2InvisTimer;
    server.p1X = p1X;
    server.p1Y = p1Y;
    server.tagged = tagged;
    server.p1It = p1It;
    server.p2HurtPerMS = p2HurtPerMS;
    server.p1BulletCoorX = p1BulletCoorX;
    server.p1BulletCoorY = p1BulletCoorY;
    server.p1Health = p1Health;
    server.p2Health = p2Health;
    server.p1GunAngle = p1GunAngle;
    server.p1LeftLooked = p1LeftLooked;
    server.p1RightLooked = p1RightLooked;
    server.p1ShowBullet = p1ShowBullet;
    server.deleteBulletID = deleteBulletID;
    server.p1GunType = p1GunType;
    server.fadeInGame = fadeInGame;
    server.gameEnded = gameEnded;
    server.p1MouseX = mouseX;
    server.p1MouseY = mouseY;
  }

  public void getVariablesFromServer() {
    /*
     * This method gets the variables
     * from the server which originally
     * came from the client and uses it
     * to print images from the client.
     */
    p2X = server.p2X;
    p2Y = server.p2Y;

    p2GunBulletsID = server.p2GunBulletsID;

    // objectsInArray = objects.toArray(new Objects[objects.size()]);
    p2BulletDamage = server.p2BulletDamage;
    p2ShowLaser = server.p2ShowLaser;
    p2BulletCoorX = server.p2BulletCoorX;
    p2BulletCoorY = server.p2BulletCoorY;
    playerTwoBulletsID = server.playerTwoBulletsID;
    p2GunType = server.p2GunType;

    bullet.pressMouse = pressMouse;

    p2LeftLooked = server.p2LeftLooked;
    p2RightLooked = server.p2RightLooked;

    p2ShowBullet = server.p2ShowBullet;
    p2GunAngle = server.p2GunAngle;

    playerTwoStepsXArray = server.playerTwoStepsXArray;
    playerTwoStepsYArray = server.playerTwoStepsYArray;
  }

  public void keyPressed(KeyEvent e) {
    requestFocus();
    if (!gameEnded) {

      if (e.getKeyCode() == KeyEvent.VK_W)
        upKey = true;
      if (e.getKeyCode() == KeyEvent.VK_S)
        downKey = true;
      if (e.getKeyCode() == KeyEvent.VK_A)
        leftKey = true;
      if (e.getKeyCode() == KeyEvent.VK_D)
        rightKey = true;

      if (e.getKeyCode() == KeyEvent.VK_C)
        showMap = true;
    }
  }

  public void keyReleased(KeyEvent e) {
    requestFocus();

    if (e.getKeyCode() == KeyEvent.VK_W)
      upKey = false;
    if (e.getKeyCode() == KeyEvent.VK_S)
      downKey = false;
    if (e.getKeyCode() == KeyEvent.VK_A)
      leftKey = false;
    if (e.getKeyCode() == KeyEvent.VK_D)
      rightKey = false;

    if (e.getKeyCode() == KeyEvent.VK_C)
      showMap = false;
  }

  public void keyTyped(KeyEvent e) {
    /*
     * Checks if any keys are typed.
     */
    requestFocus();
    if (e.getKeyChar() == '1') {
      p1GunType = 0;
    } else if (e.getKeyChar() == '2') {
      p1GunType = 1;
    } else if (e.getKeyChar() == '3') {
      p1GunType = 2;
    } else if (e.getKeyChar() == '4') {
      p1GunType = 3;
    } else if (e.getKeyChar() == '5') {
      p1GunType = 4;
    } else if (e.getKeyChar() == '6') {
      p1GunType = 5;
    } else if (e.getKeyChar() == '7') {
      p1GunType = 6;
    }

  }

  public void mouseDragged(MouseEvent e) {
    requestFocus();
    mouseX = e.getX();
    mouseY = e.getY();

    mouseFromMiddleX = mouseX - (this.getWidth() / 2);
    mouseFromMiddleY = mouseY - (this.getHeight() / 2);
  }

  public void mouseMoved(MouseEvent e) {
    requestFocus();
    mouseX = e.getX();
    mouseY = e.getY();

    mouseFromMiddleX = mouseX - (this.getWidth() / 2);
    mouseFromMiddleY = mouseY - (this.getHeight() / 2);
  }

  public void mouseClicked(MouseEvent e) {
    if (!singlePlayer) {
      if (server.connectionStatus) {
        if (gameEnded) {
          if (levelSelectButton.contains(mouseX, mouseY)) {
            backToLevelSelect = true;
          }
        }
      } else {
        if (connectionLostExitClient.contains(mouseX, mouseY)) {
          System.exit(1);
        }

      }
    } else {
      if (gameEnded) {
        if (levelSelectButton.contains(mouseX, mouseY)) {
          backToLevelSelect = true;
        }
      }
    }
  }

  public void mouseEntered(MouseEvent e) {
  }

  public void mouseExited(MouseEvent e) {
  }

  public void mousePressed(MouseEvent e) {
    if (!gameEnded) {
      requestFocus();
      pressMouse = true;
      if (p1GunType == 6) {
        p1ShowLaser = true;

      }
    }

  }

  public void mouseReleased(MouseEvent e) {
    requestFocus();
    pressMouse = false;
    if (p1GunType == 6) {
      p1ShowLaser = false;
    }
  }
}