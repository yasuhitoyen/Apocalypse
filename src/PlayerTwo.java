
/*
 * Michael Yen
 * A P O C A L Y P S E
 */
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class PlayerTwo extends JPanel implements KeyListener, ActionListener, MouseMotionListener, MouseListener {
	/*
	 * This class is also the game itself. This is where all the players, bullets,
	 * rain,
	 * health bar etc are printed. The player of the CLIENT is created and this
	 * class gets
	 * information from the server and sends some information. (In game processes
	 * not level
	 * select, gun select etc.) More information is sent from player one than this
	 * class.
	 * This class also sends information and gets information from the server. The
	 * server
	 * connects to Player One where it can also connect to the client and send
	 * information.
	 * Player One -> Server -> Client -> Player Two
	 * <- <- <-
	 */
	Timer gameTick;
	int mouseFromMiddleX, mouseFromMiddleY;
	int gameMode;
	int p1X, p1Y, p2X, p2Y, p1Health, p2Health, p2GunType, p2BulletDamage, p2BulletDistance;
	int p1InvisTimer, p2InvisTimer;
	boolean move, shoot;
	Bullet bullet;
	boolean showMap;
	boolean victory;
	boolean gotGold;
	Rectangle connectionLostExitClient;
	Image exitClientSign;
	int zombiesKilled, gold;
	Image okDefeat, okVictory, defeatScreen, victoryScreen, regularZombie, regularZombieRight, regularZombieLeft;
	String timeString;
	boolean p1ShowLaser, p2ShowLaser;
	boolean upKey, downKey, leftKey, rightKey, p2It, tagged, p2LeftLooked, p2RightLooked, p1RightLooked, p1LeftLooked,
			pressMouse, p2ShowBullet, p1ShowBullet;
	int mouseX, mouseY, bulletWidth, bulletLength, raindropCounter, shotBullets;
	final int FRAME_WIDTH = 1370, FRAME_HEIGHT = 750, PLAYER_WIDTH = 68, PLAYER_HEIGHT = 100, FRAME_DIFFERENCE = 40,
			PLAYER_X = FRAME_WIDTH / 2 - FRAME_DIFFERENCE - PLAYER_WIDTH / 2,
			PLAYER_Y = FRAME_HEIGHT / 2 - FRAME_DIFFERENCE;
	final int PLAYER_HITBOX_X = PLAYER_X, PLAYER_HITBOX_Y = PLAYER_Y + 70, PLAYER_HITBOX_WIDTH = PLAYER_WIDTH,
			PLAYER_HITBOX_HEIGHT = 30;
	GameClient client;
	int p1MouseX, p1MouseY;
	Polygon p1Bullet, p2Bullet;
	Image background, playerRightImage, playerLeftImage, rightMicroUzi, leftMicroUzi, leftHandgun, rightHandgun,
			rightAWM, leftAWM, healthBar, raindropImage;
	Image bloodStainScreen, rightLasergun, leftLasergun, rightAK, leftAK, rightBeretta, leftBeretta, rightDesertEagle,
			leftDesertEagle, bulletImage, playerRightBlueImage, playerLeftBlueImage, rainDrops;
	Image movingHealthBar;
	Image p1Mouse, p2Mouse;
	ArrayList<Integer> playerTwoStepsX, playerTwoStepsY, playerTwoSteps, playerTwoBulletsID;
	ArrayList<Objects> objects = new ArrayList<Objects>();
	boolean gameEnded;

	int[] objectsXPos;
	int[] objectsYPos;
	String[] objectsName;
	int p1BulletCoorX[], p1BulletCoorY[], p2BulletCoorX[], p2BulletCoorY[];
	double p2GunAngle, p1GunAngle;

	ArrayList<GunBullet> p2GunBullets = new ArrayList<GunBullet>();
	ArrayList<GunBullet> p1GunBullets = new ArrayList<GunBullet>();

	ArrayList<RainDropletsGame> rain = new ArrayList<RainDropletsGame>();

	ArrayList<Integer> p2GunBulletXPos = new ArrayList<Integer>();
	ArrayList<Integer> p2GunBulletYPos = new ArrayList<Integer>();
	ArrayList<Boolean> p2GunBulletPlayerRightLooked = new ArrayList<Boolean>();
	ArrayList<Double> p2GunBulletGunAngle = new ArrayList<Double>();
	ArrayList<Integer> p2GunBulletXPosFromPlayer = new ArrayList<Integer>();
	ArrayList<Integer> p2GunBulletYPosFromPlayer = new ArrayList<Integer>();

	ArrayList<Zombie> zombies = new ArrayList<Zombie>();

	int p1GunBulletXPos[];
	int p1GunBulletYPos[];
	int p1GunBulletXPosFromPlayer[];
	int p1GunBulletYPosFromPlayer[];
	int p1GunType;
	boolean p1GunBulletPlayerRightLooked[];
	double p1GunBulletGunAngle[];

	int zombieXPos[];
	int zombieYPos[];
	int zombieHealth[];
	boolean zombieHurt[];
	int zombieType[];
	boolean zombieLookRight[];
	int deleteBulletID;
	int p2HurtPerMS;
	int[] survivorXPos;
	int[] survivorYPos;
	int[] survivorType;
	boolean[] survivorLookRight;
	Rectangle levelSelectButton;
	ArrayList<Survivor> survivors;
	boolean spectatePlayerOne;
	float fadeInGame;
	Image invisScreen;

	public PlayerTwo(GameClient client) throws IOException {
		/*
		 * Constructor for PlayerTwo. Starts the
		 * timer of the actual game. Adds MouseMotionListener,
		 * MouseListener, and KeyListener to this class
		 */
		move = false;
		shoot = false;

		survivorYPos = new int[0];
		survivorXPos = new int[0];
		survivorType = new int[0];
		survivorLookRight = new boolean[0];
		connectionLostExitClient = new Rectangle(585, 340, 160, 75);
		zombieXPos = new int[0];
		zombieYPos = new int[0];
		zombieHealth = new int[0];
		zombieHurt = new boolean[0];
		zombieLookRight = new boolean[0];
		shotBullets = 0;
		this.client = client;
		gameTick = new Timer(5, this);
		upKey = downKey = leftKey = rightKey = false;
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		bullet = new Bullet();
		p1BulletCoorX = new int[4];
		p1BulletCoorY = new int[4];
		p2BulletCoorX = new int[4];
		p2BulletCoorY = new int[4];

		objectsXPos = new int[0];
		objectsYPos = new int[0];
		objectsName = new String[0];

		p1GunBulletGunAngle = new double[0];
		p1GunBulletPlayerRightLooked = new boolean[0];
		p1GunBulletXPos = new int[0];
		p1GunBulletYPos = new int[0];

		playerTwoStepsX = new ArrayList<Integer>();
		playerTwoStepsY = new ArrayList<Integer>();
		playerTwoBulletsID = new ArrayList<Integer>();

		survivors = new ArrayList<Survivor>();

		pressMouse = false;

		p1Health = 0;
		p2Health = 0;

		bulletWidth = 10;
		bulletLength = 4000;
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
		p1Mouse = new ImageIcon("../images/PlayerOneMouse.png").getImage();
		exitClientSign = new ImageIcon("../images/EXITCLIENT.png").getImage();

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

		levelSelectButton = new Rectangle(540, 610, 210, 70);

		p2It = false;
		p2LeftLooked = false;
		p2RightLooked = true;
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

		p1Bullet = new Polygon();
		p2Bullet = new Polygon();

		for (int i = 0; i < 200; i++) {
			playerTwoStepsX.add(p2X * -1 + PLAYER_X);
			playerTwoStepsY.add(p2Y * -1 + PLAYER_Y);
		}
		gameTick.start();
	}

	public void actionPerformed(ActionEvent e) {
		/*
		 * This method is the action performed and gets called by the
		 * timer every 5 ms. The player's movement is controlled here
		 * and also the detection of object such as trees. I will
		 * add more objects later but for now I have a tree.
		 */
		if (client.connectionStatus) {
			if (client.gameMode == 1) {
				if (gameEnded) {
					p2X = 1355;
					p2Y = 920;
				}
				if (p2Health < 0 && !gameEnded) {
					spectatePlayerOne = true;
					p2X = -100000;
					p2Y = -100000;
				} else {
					spectatePlayerOne = false;
				}
				if (!gameEnded) {
					if (upKey) {
						p2Y -= 4;
						for (int i = 0; i < objects.size(); i++) {
							if (objects.get(i).hitboxX + p2X * -1 < PLAYER_HITBOX_X + PLAYER_HITBOX_WIDTH
									&& objects.get(i).hitboxX + objects.get(i).hitboxWidth + p2X * -1 > PLAYER_HITBOX_X
									&& objects.get(i).hitboxY + objects.get(i).hitboxHeight + p2Y * -1
											+ 1 > PLAYER_HITBOX_Y
									&& objects.get(i).hitboxY + p2Y * -1 - 1 < PLAYER_HITBOX_Y + PLAYER_HITBOX_HEIGHT) {
								if (!objects.get(i).image.equals("Powerup")
										&& !objects.get(i).image.equals("Powerup")) {
									p2Y += 4;
								}
							}
						}
					}

					if (downKey) {
						p2Y += 4;
						for (int i = 0; i < objects.size(); i++) {
							if (objects.get(i).hitboxX + p2X * -1 < PLAYER_HITBOX_X + PLAYER_HITBOX_WIDTH
									&& objects.get(i).hitboxX + objects.get(i).hitboxWidth + p2X * -1 > PLAYER_HITBOX_X
									&& objects.get(i).hitboxY + objects.get(i).hitboxHeight + p2Y * -1
											+ 1 > PLAYER_HITBOX_Y
									&& objects.get(i).hitboxY + p2Y * -1 - 1 < PLAYER_HITBOX_Y + PLAYER_HITBOX_HEIGHT) {
								if (!objects.get(i).image.equals("Powerup")
										&& !objects.get(i).image.equals("Powerup")) {
									p2Y -= 4;
								}
							}
						}
					}
					if (leftKey) {
						p2X -= 4;
						for (int i = 0; i < objects.size(); i++) {
							if (objects.get(i).hitboxX + p2X * -1 - 1 < PLAYER_HITBOX_X + PLAYER_HITBOX_WIDTH
									&& objects.get(i).hitboxX + objects.get(i).hitboxWidth + p2X * -1
											+ 1 > PLAYER_HITBOX_X
									&& objects.get(i).hitboxY + objects.get(i).hitboxHeight + p2Y * -1 > PLAYER_HITBOX_Y
									&& objects.get(i).hitboxY + p2Y * -1 < PLAYER_HITBOX_Y + PLAYER_HITBOX_HEIGHT) {
								if (!objects.get(i).image.equals("Powerup")
										&& !objects.get(i).image.equals("Powerup")) {
									p2X += 4;
								}
							}
						}
					}
					if (rightKey) {
						p2X += 4;
						for (int i = 0; i < objects.size(); i++) {
							if (objects.get(i).hitboxX + p2X * -1 - 1 < PLAYER_HITBOX_X + PLAYER_HITBOX_WIDTH
									&& objects.get(i).hitboxX + objects.get(i).hitboxWidth + p2X * -1
											+ 1 > PLAYER_HITBOX_X
									&& objects.get(i).hitboxY + objects.get(i).hitboxHeight + p2Y * -1 > PLAYER_HITBOX_Y
									&& objects.get(i).hitboxY + p2Y * -1 < PLAYER_HITBOX_Y + PLAYER_HITBOX_HEIGHT) {
								if (!objects.get(i).image.equals("Powerup")
										&& !objects.get(i).image.equals("Powerup")) {
									p2X -= 4;
								}
							}
						}
					}
				}

				client.p2Y = p2Y;
				client.p2X = p2X;
				p1X = client.p1X;
				p1Y = client.p1Y;
				timeString = client.timeString;
				victory = client.victory;
				gold = client.gold;
				zombiesKilled = client.zombiesKilled;

				p1MouseX = client.p1MouseX;
				p1MouseY = client.p1MouseY;

				p1Health = client.p1Health;
				p2Health = client.p2Health;
				gameEnded = client.gameEnded;
				tagged = client.tagged;

				bullet.pressMouse = pressMouse;

				client.p2RightLooked = p2RightLooked;
				client.p2LeftLooked = p2LeftLooked;

				client.p2BulletCoorX = p2BulletCoorX;
				client.p2BulletCoorY = p2BulletCoorY;
				client.p2ShowLaser = p2ShowLaser;
				client.p2GunAngle = p2GunAngle;
				client.p2ShowBullet = p2ShowBullet;
				p1ShowLaser = client.p1ShowLaser;
				client.p2GunType = p2GunType;

				objectsXPos = client.objectsXPos;
				objectsYPos = client.objectsYPos;
				objectsName = client.objectsName;
				p2InvisTimer = client.p2InvisTimer;
				survivorXPos = client.survivorXPos;
				survivorYPos = client.survivorYPos;
				survivorType = client.survivorType;
				survivorLookRight = client.survivorLookRight;
				p1GunType = client.p1GunType;
				p2ShowBullet = bullet.showBullet;
				p1RightLooked = client.p1RightLooked;
				p1LeftLooked = client.p1LeftLooked;
				p1GunAngle = client.p1GunAngle;
				p1ShowBullet = client.p1ShowBullet;
				deleteBulletID = client.deleteBulletID;
				p2HurtPerMS = client.p2HurtPerMS;
				fadeInGame = client.fadeInGame;
				if (client.p1It) {
					p2It = false;
				} else {
					p2It = true;
				}
				/*
				 * if(tagged)
				 * {
				 * p2X = 1543;
				 * p2Y = 710;
				 * }
				 * 
				 * if(!p2LeftLooked && rightKey) p2RightLooked = true;
				 * if(!p2RightLooked && leftKey) p2LeftLooked = true;
				 */
				if (mouseX > PLAYER_X + PLAYER_WIDTH / 2) {
					p2RightLooked = true;
					p2LeftLooked = false;
				} else {
					p2RightLooked = false;
					p2LeftLooked = true;
				}

				setUpBulletCoordinatesPlayerOne();
				setUpBulletCoordinatesPlayerTwo();
				updateAllObjects();
				updateAllBullets();
				updateAllZombies();
				updateGunSettings();
				updateAllSurvivors();
				// updateAllRainDroplets();
			}
			repaint();
		}
	}

	public void paintComponent(Graphics g) {
		/*
		 * Draws player one, player two, and the
		 * background, bullets etc. DRAWS
		 * EVERYTHING
		 */
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(background, p2X * -1 + mouseFromMiddleX / -15, p2Y * -1 + mouseFromMiddleY / -15,
				(int) FRAME_WIDTH * 3, FRAME_HEIGHT * 3, null);
		g2d.setColor(Color.YELLOW);
		if (!spectatePlayerOne) {
			g2d.setColor(Color.cyan);
			if (p1ShowLaser) {
				g2d.fill(p1Bullet);
			}
			if (p2ShowLaser) {
				g2d.fill(p2Bullet);
			}
			for (GunBullet currentBullet : p2GunBullets) {
				if (currentBullet.playerRightLooked) {
					g2d.rotate(Math.toRadians(currentBullet.gunAngle),
							currentBullet.xPosFromPlayer + p2X * -1 + mouseFromMiddleX / -15,
							currentBullet.yPosFromPlayer + p2Y * -1 + mouseFromMiddleY / -15);
					g2d.drawImage(bulletImage, currentBullet.xPosFromPlayer + p2X * -1 + mouseFromMiddleX / -15,
							currentBullet.yPosFromPlayer + p2Y * -1 + mouseFromMiddleY / -15, 14, 7, null);
					g2d.rotate(Math.toRadians(360 - currentBullet.gunAngle),
							currentBullet.xPosFromPlayer + p2X * -1 + mouseFromMiddleX / -15,
							currentBullet.yPosFromPlayer + p2Y * -1 + mouseFromMiddleY / -15);
				} else if (currentBullet.gunAngle != 90 || currentBullet.gunAngle != -90) {
					g2d.rotate(Math.toRadians(currentBullet.gunAngle + 180),
							currentBullet.xPosFromPlayer + p2X * -1 + mouseFromMiddleX / -15,
							currentBullet.yPosFromPlayer + p2Y * -1 + mouseFromMiddleY / -15);
					g2d.drawImage(bulletImage, currentBullet.xPosFromPlayer + p2X * -1 + mouseFromMiddleX / -15 + 10,
							currentBullet.yPosFromPlayer + p2Y * -1 + mouseFromMiddleY / -15, 14, 7, null);
					g2d.rotate(Math.toRadians(180 - currentBullet.gunAngle),
							currentBullet.xPosFromPlayer + p2X * -1 + mouseFromMiddleX / -15,
							currentBullet.yPosFromPlayer + p2Y * -1 + mouseFromMiddleY / -15);
				}
			}
			for (GunBullet currentBullet : p1GunBullets) {
				if (currentBullet.playerRightLooked) {
					g2d.rotate(Math.toRadians(currentBullet.gunAngle),
							currentBullet.xPos + p2X * -1 + mouseFromMiddleX / -15 + PLAYER_X + mouseFromMiddleX / -15
									+ PLAYER_WIDTH / 2,
							currentBullet.yPos + p2Y * -1 + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
					g2d.drawImage(bulletImage,
							currentBullet.xPos + p2X * -1 + mouseFromMiddleX / -15 + PLAYER_X + mouseFromMiddleX / -15
									+ PLAYER_WIDTH / 2,
							currentBullet.yPos + p2Y * -1 + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2, 14,
							7, null);
					g2d.rotate(Math.toRadians(360 - currentBullet.gunAngle),
							currentBullet.xPos + p2X * -1 + mouseFromMiddleX / -15 + PLAYER_X + mouseFromMiddleX / -15
									+ PLAYER_WIDTH / 2,
							currentBullet.yPos + p2Y * -1 + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
				} else if (currentBullet.gunAngle != 90 || currentBullet.gunAngle != -90) {
					g2d.rotate(Math.toRadians(currentBullet.gunAngle + 180),
							currentBullet.xPos + p2X * -1 + mouseFromMiddleX / -15 + PLAYER_X + mouseFromMiddleX / -15
									+ PLAYER_WIDTH / 2,
							currentBullet.yPos + p2Y * -1 + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
					g2d.drawImage(bulletImage,
							currentBullet.xPos + p2X * -1 + mouseFromMiddleX / -15 + PLAYER_X + mouseFromMiddleX / -15
									+ PLAYER_WIDTH / 2 + 10,
							currentBullet.yPos + p2Y * -1 + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2, 14,
							7, null);
					g2d.rotate(Math.toRadians(180 - currentBullet.gunAngle),
							currentBullet.xPos + p2X * -1 + mouseFromMiddleX / -15 + PLAYER_X + mouseFromMiddleX / -15
									+ PLAYER_WIDTH / 2,
							currentBullet.yPos + p2Y * -1 + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
				}
			}
			for (int y = -500; y < FRAME_HEIGHT + 500; y++) {
				if (PLAYER_Y + PLAYER_HEIGHT == y) {
					if (p2RightLooked) {
						g2d.drawImage(playerRightBlueImage, PLAYER_X + mouseFromMiddleX / -15,
								PLAYER_Y + mouseFromMiddleY / -15, PLAYER_WIDTH, PLAYER_HEIGHT, null);
					} else {
						g2d.drawImage(playerLeftBlueImage, PLAYER_X + mouseFromMiddleX / -15,
								PLAYER_Y + mouseFromMiddleY / -15, PLAYER_WIDTH, PLAYER_HEIGHT, null);
					}
					if (p2GunType == 0) {
						if (p2RightLooked || p2GunAngle == -90) {
							g2d.rotate(Math.toRadians(p2GunAngle), PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightHandgun, PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2 + 10,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_WIDTH / 2 + 10, 45, 30, null);
							g2d.rotate(Math.toRadians(360 - p2GunAngle),
									PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
						} else if (p2GunAngle != -90) {
							g2d.rotate(Math.toRadians(p2GunAngle + 180),
									PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftHandgun, PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2 + 10,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_WIDTH / 2 - 5, 45, 30, null);
							g2d.rotate(Math.toRadians(180 - p2GunAngle),
									PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
						}
					} else if (p2GunType == 1) {
						if (p2RightLooked || p2GunAngle == -90) {
							g2d.rotate(Math.toRadians(p2GunAngle), PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightDesertEagle, PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2 + 10,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_WIDTH / 2 + 10, 50, 30, null);
							g2d.rotate(Math.toRadians(360 - p2GunAngle),
									PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
						} else if (p2GunAngle != 90) {
							g2d.rotate(Math.toRadians(p2GunAngle + 180),
									PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftDesertEagle, PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2 + 10,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_WIDTH / 2 - 5, 50, 30, null);
							g2d.rotate(Math.toRadians(180 - p2GunAngle),
									PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
						}
					} else if (p2GunType == 2) {
						if (p2RightLooked || p2GunAngle == -90) {
							g2d.rotate(Math.toRadians(p2GunAngle), PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightMicroUzi, PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_WIDTH / 2 + 10, 60, 50, null);
							g2d.rotate(Math.toRadians(360 - p2GunAngle),
									PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
						} else if (p2GunAngle != 90) {
							g2d.rotate(Math.toRadians(p2GunAngle + 180),
									PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftMicroUzi, PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_WIDTH / 2 - 20, 60, 50, null);
							g2d.rotate(Math.toRadians(180 - p2GunAngle),
									PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
						}
					} else if (p2GunType == 3) {
						if (p2RightLooked || p2GunAngle == -90) {
							g2d.rotate(Math.toRadians(p2GunAngle), PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightAK, PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2 - 20,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_WIDTH / 2 + 10, 110, 35, null);
							g2d.rotate(Math.toRadians(360 - p2GunAngle),
									PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
						} else if (p2GunAngle != 90) {
							g2d.rotate(Math.toRadians(p2GunAngle + 180),
									PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftAK, PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2 - 20,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_WIDTH / 2 - 5, 110, 35, null);
							g2d.rotate(Math.toRadians(180 - p2GunAngle),
									PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
						}
					} else if (p2GunType == 4) {
						if (p2RightLooked || p2GunAngle == -90) {
							g2d.rotate(Math.toRadians(p2GunAngle), PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightBeretta, PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2 - 20,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_WIDTH / 2 + 10, 110, 45, null);
							g2d.rotate(Math.toRadians(360 - p2GunAngle),
									PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
						} else if (p2GunAngle != 90) {
							g2d.rotate(Math.toRadians(p2GunAngle + 180),
									PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftBeretta, PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2 - 20,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_WIDTH / 2 - 15, 110, 45, null);
							g2d.rotate(Math.toRadians(180 - p2GunAngle),
									PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
						}
					} else if (p2GunType == 5) {
						if (p2RightLooked || p2GunAngle == -90) {
							g2d.rotate(Math.toRadians(p2GunAngle), PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightAWM, PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2 - 20,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_WIDTH / 2 + 10, 130, 30, null);
							g2d.rotate(Math.toRadians(360 - p2GunAngle),
									PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
						} else if (p2GunAngle != 90) {
							g2d.rotate(Math.toRadians(p2GunAngle + 180),
									PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftAWM, PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2 - 20,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_WIDTH / 2 - 5, 130, 30, null);
							g2d.rotate(Math.toRadians(180 - p2GunAngle),
									PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
						}
					} else if (p2GunType == 6) {
						if (p2RightLooked || p2GunAngle == -90) {
							g2d.rotate(Math.toRadians(p2GunAngle), PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightLasergun, PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2 - 40,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_WIDTH / 2 + 5, 160, 50, null);
							g2d.rotate(Math.toRadians(360 - p2GunAngle),
									PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
						} else if (p2GunAngle != 90) {
							g2d.rotate(Math.toRadians(p2GunAngle + 180),
									PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftLasergun, PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2 - 40,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_WIDTH / 2 - 10, 160, 50, null);
							g2d.rotate(Math.toRadians(180 - p2GunAngle),
									PLAYER_X + mouseFromMiddleX / -15 + PLAYER_WIDTH / 2,
									PLAYER_Y + mouseFromMiddleY / -15 + PLAYER_HEIGHT / 2);
						}
					}

				}
				if (PLAYER_Y + p1Y + p2Y * -1 + mouseFromMiddleY / -15 + PLAYER_HEIGHT == y) {

					g2d.setColor(Color.red);
					if (!gameEnded)
						g2d.fillRect(p2X * -1 + mouseFromMiddleX / -15 + p1X + PLAYER_X + 1,
								p2Y * -1 + mouseFromMiddleY / -15 + p1Y + PLAYER_Y - 25, (int) (68 * p1Health / 100),
								20);
					g2d.drawImage(movingHealthBar, p2X * -1 + mouseFromMiddleX / -15 + p1X + PLAYER_X,
							p2Y * -1 + mouseFromMiddleY / -15 + p1Y + PLAYER_Y - 25, 70, 20, null);

					/*
					 * for(GunBullet currentBullet : p1GunBullets)
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
					 */
					if (p1RightLooked) {
						g2d.drawImage(playerRightImage, p2X * -1 + mouseFromMiddleX / -15 + p1X + PLAYER_X,
								p2Y * -1 + mouseFromMiddleY / -15 + p1Y + PLAYER_Y, PLAYER_WIDTH, PLAYER_HEIGHT, null);
					} else if (p1LeftLooked) {
						g2d.drawImage(playerLeftImage, p2X * -1 + mouseFromMiddleX / -15 + p1X + PLAYER_X,
								p2Y * -1 + mouseFromMiddleY / -15 + p1Y + PLAYER_Y, PLAYER_WIDTH, PLAYER_HEIGHT, null);
					}
					/*
					 * ferfaerfaerf
					 */
					if (p1GunType == 0) {
						if (p1RightLooked) {
							g2d.rotate(Math.toRadians(p1GunAngle),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightHandgun,
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2 + 10,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_WIDTH / 2 + 10, 45, 30,
									null);
							g2d.rotate(Math.toRadians(360 - p1GunAngle),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
						} else if (p1GunAngle != 90 || p1GunAngle != -90) {
							g2d.rotate(Math.toRadians(p1GunAngle + 180),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftHandgun,
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2 + 10,
									p2Y * -1 + PLAYER_Y + p1Y + mouseFromMiddleY / -15 + PLAYER_WIDTH / 2 - 5, 45, 30,
									null);
							g2d.rotate(Math.toRadians(180 - p1GunAngle),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
						}
					}
					if (p1GunType == 1) {
						if (p1RightLooked) {
							g2d.rotate(Math.toRadians(p1GunAngle),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightDesertEagle,
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2 + 10,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_WIDTH / 2 + 10, 50, 30,
									null);
							g2d.rotate(Math.toRadians(360 - p1GunAngle),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
						} else if (p1GunAngle != 90 || p1GunAngle != -90) {
							g2d.rotate(Math.toRadians(p1GunAngle + 180),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftDesertEagle,
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2 + 10,
									p2Y * -1 + PLAYER_Y + p1Y + mouseFromMiddleY / -15 + PLAYER_WIDTH / 2 - 5, 50, 30,
									null);
							g2d.rotate(Math.toRadians(180 - p1GunAngle),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
						}
					}
					if (p1GunType == 2) {
						if (p1RightLooked) {
							g2d.rotate(Math.toRadians(p1GunAngle),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightMicroUzi,
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2 + 10,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_WIDTH / 2 + 10, 60, 50,
									null);
							g2d.rotate(Math.toRadians(360 - p1GunAngle),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
						} else if (p1GunAngle != 90 || p1GunAngle != -90) {
							g2d.rotate(Math.toRadians(p1GunAngle + 180),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftMicroUzi,
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2 + 10,
									p2Y * -1 + PLAYER_Y + p1Y + mouseFromMiddleY / -15 + PLAYER_WIDTH / 2 - 20, 60, 50,
									null);
							g2d.rotate(Math.toRadians(180 - p1GunAngle),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
						}
					}
					if (p1GunType == 3) {
						if (p1RightLooked) {
							g2d.rotate(Math.toRadians(p1GunAngle),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightAK,
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2 - 20,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_WIDTH / 2 + 10, 110, 35,
									null);
							g2d.rotate(Math.toRadians(360 - p1GunAngle),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
						} else if (p1GunAngle != 90 || p1GunAngle != -90) {
							g2d.rotate(Math.toRadians(p1GunAngle + 180),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftAK,
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2 - 20,
									p2Y * -1 + PLAYER_Y + p1Y + mouseFromMiddleY / -15 + PLAYER_WIDTH / 2 - 5, 110, 35,
									null);
							g2d.rotate(Math.toRadians(180 - p1GunAngle),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
						}
					}
					if (p1GunType == 4) {
						if (p1RightLooked) {
							g2d.rotate(Math.toRadians(p1GunAngle),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightBeretta,
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2 - 20,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_WIDTH / 2 + 10, 110, 45,
									null);
							g2d.rotate(Math.toRadians(360 - p1GunAngle),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
						} else if (p1GunAngle != 90 || p1GunAngle != -90) {
							g2d.rotate(Math.toRadians(p1GunAngle + 180),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftBeretta,
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2 - 20,
									p2Y * -1 + PLAYER_Y + p1Y + mouseFromMiddleY / -15 + PLAYER_WIDTH / 2 - 15, 110, 45,
									null);
							g2d.rotate(Math.toRadians(180 - p1GunAngle),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
						}
					}
					if (p1GunType == 5) {
						if (p1RightLooked) {
							g2d.rotate(Math.toRadians(p1GunAngle),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightAWM,
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2 - 20,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_WIDTH / 2 + 10, 130, 30,
									null);
							g2d.rotate(Math.toRadians(360 - p1GunAngle),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
						} else if (p1GunAngle != 90 || p1GunAngle != -90) {
							g2d.rotate(Math.toRadians(p1GunAngle + 180),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftAWM,
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2 - 20,
									p2Y * -1 + PLAYER_Y + p1Y + mouseFromMiddleY / -15 + PLAYER_WIDTH / 2 - 5, 130, 30,
									null);
							g2d.rotate(Math.toRadians(180 - p1GunAngle),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
						}
					}
					if (p1GunType == 6) {
						if (p1RightLooked) {
							g2d.rotate(Math.toRadians(p1GunAngle),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightLasergun,
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2 - 40,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_WIDTH / 2 + 5, 160, 50,
									null);
							g2d.rotate(Math.toRadians(360 - p1GunAngle),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
						} else if (p1GunAngle != 90 || p1GunAngle != -90) {
							g2d.rotate(Math.toRadians(p1GunAngle + 180),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftLasergun,
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2 - 40,
									p2Y * -1 + PLAYER_Y + p1Y + mouseFromMiddleY / -15 + PLAYER_WIDTH / 2 - 10, 160, 50,
									null);
							g2d.rotate(Math.toRadians(180 - p1GunAngle),
									p2X * -1 + p1X + mouseFromMiddleX / -15 + PLAYER_X + PLAYER_WIDTH / 2,
									p2Y * -1 + p1Y + mouseFromMiddleY / -15 + PLAYER_Y + PLAYER_HEIGHT / 2);
						}
					}
				}
				for (int i = 0; i < objects.size(); i++) {
					if (objects.get(i).yPos + p2Y * -1 + mouseFromMiddleY / -15 + objects.get(i).height == y) {
						g2d.drawImage(objects.get(i).objectImage,
								objects.get(i).xPos + p2X * -1 + mouseFromMiddleX / -15,
								objects.get(i).yPos + p2Y * -1 + mouseFromMiddleY / -15, objects.get(i).width,
								objects.get(i).height, null);
					}
				}

				for (Zombie zombie : zombies) {
					if (zombie.yPos + zombie.height + p2Y * -1 + mouseFromMiddleY / -15 == y) {
						if (!zombie.hurt) {
							if (zombie.lookRight) {
								g2d.drawImage(zombie.rightZombieImage,
										(int) zombie.xPos + p2X * -1 + mouseFromMiddleX / -15 + mouseFromMiddleX / -15,
										(int) zombie.yPos + p2Y * -1 + mouseFromMiddleY / -15, zombie.width,
										zombie.height, null);
							} else {
								g2d.drawImage(zombie.leftZombieImage,
										(int) zombie.xPos + p2X * -1 + mouseFromMiddleX / -15 + mouseFromMiddleX / -15,
										(int) zombie.yPos + p2Y * -1 + mouseFromMiddleY / -15, zombie.width,
										zombie.height, null);
							}
						} else {
							if (zombie.lookRight) {
								g2d.drawImage(zombie.rightHurtZombieImage,
										(int) zombie.xPos + p2X * -1 + mouseFromMiddleX / -15 + mouseFromMiddleX / -15,
										(int) zombie.yPos + p2Y * -1 + mouseFromMiddleY / -15, zombie.width,
										zombie.height, null);
							} else {
								g2d.drawImage(zombie.leftHurtZombieImage,
										(int) zombie.xPos + p2X * -1 + mouseFromMiddleX / -15 + mouseFromMiddleX / -15,
										(int) zombie.yPos + p2Y * -1 + mouseFromMiddleY / -15, zombie.width,
										zombie.height, null);
							}
						}
					}
				}
				for (Survivor survivor : survivors) {
					if (survivor.yPos + p2Y * -1 + mouseFromMiddleY / -15 + survivor.height == y) {
						if (survivor.lookRight) {
							g2d.drawImage(survivor.rightSurvivorImage,
									survivor.xPos + p2X * -1 + mouseFromMiddleX / -15,
									survivor.yPos + p2Y * -1 + mouseFromMiddleY / -15, survivor.width, survivor.height,
									null);
						} else {
							g2d.drawImage(survivor.rightSurvivorImage,
									survivor.xPos + p2X * -1 + mouseFromMiddleX / -15,
									survivor.yPos + p2Y * -1 + mouseFromMiddleY / -15, survivor.width, survivor.height,
									null);
						}
					}
				}

			}

			g2d.setColor(Color.red);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
			if (!gameEnded)
				g2d.fillRect(15, 0, (int) (p2Health * 4.5), 80);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

			if (!gameEnded)
				g2d.drawImage(healthBar, 0, 0, 500, 90, null);

			g2d.setFont(new Font("Sans serif", Font.BOLD, 15));
			g2d.setColor(Color.GRAY);
			if (p2GunType == 0 || p2GunType == 6)
				g2d.drawString("Infinite", 85, 75);
			else
				g2d.drawString((bullet.maxAmmo - bullet.ammoAmount) + "", 85, 75);

			// -----------------------------------------------------------------

			g.setFont(new Font("Sans serif", Font.BOLD, 25));
			/*
			 * if(p2It)
			 * g.drawString("You're it",p2X - 24, p2Y);
			 * else
			 * g.drawString("Opponent it", p1X - 40, p1Y);
			 */
			// -----------------------------------------------------------------
			for (RainDropletsGame currentDrop : rain) {
				g2d.drawImage(raindropImage, currentDrop.xPos + p1X * -1 + mouseFromMiddleX / -15,
						currentDrop.yPos + p1Y * -1, 13, 25, null);
			}

			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

			if (!gameEnded)
				g2d.drawImage(healthBar, 0, 0, 500, 90, null);

			if (bullet.currentlyReloading) {
				g2d.drawString("Reloading...", PLAYER_X + mouseFromMiddleX / -15 - 20, PLAYER_Y);
			}
			g2d.setColor(Color.BLUE);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f * (10 - p2HurtPerMS)));
			g2d.drawImage(bloodStainScreen, 0, -30, FRAME_WIDTH, FRAME_HEIGHT + 110, null);

			g2d.setColor(Color.black);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			g2d.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
			g2d.setColor(Color.BLUE);
			g2d.setFont(new Font("Arial", Font.BOLD, 10));

			g2d.setColor(Color.BLUE);
			g2d.setColor(Color.YELLOW);

			g2d.setFont(new Font("Sans serif", Font.PLAIN, 10));

			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeInGame));
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			if (p2InvisTimer > 1) {
				g2d.drawImage(invisScreen, 0, 0, FRAME_WIDTH, FRAME_HEIGHT, null);
			}
		} else {
			g2d.drawImage(background, p1X * -1, p1Y * -1, FRAME_WIDTH * 3, FRAME_HEIGHT * 3, null);

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
					g2d.fillRect(objects.get(i).xPos + p1X * -1, objects.get(i).yPos + p1Y * -1, objects.get(i).width,
							objects.get(i).height);
				}
			}
			// Layer Printing
			for (GunBullet currentBullet : p2GunBullets) {
				if (currentBullet.playerRightLooked) {
					g2d.rotate(Math.toRadians(currentBullet.gunAngle),
							currentBullet.xPos + p1X * -1 + PLAYER_X + PLAYER_WIDTH / 2,
							currentBullet.yPos + p1Y * -1 + PLAYER_Y + PLAYER_HEIGHT / 2);
					g2d.drawImage(bulletImage, currentBullet.xPos + p1X * -1 + PLAYER_X + PLAYER_WIDTH / 2,
							currentBullet.yPos + p1Y * -1 + PLAYER_Y + PLAYER_HEIGHT / 2, 14, 7, null);
					g2d.rotate(Math.toRadians(360 - currentBullet.gunAngle),
							currentBullet.xPos + p1X * -1 + PLAYER_X + PLAYER_WIDTH / 2,
							currentBullet.yPos + p1Y * -1 + PLAYER_Y + PLAYER_HEIGHT / 2);
				} else if (currentBullet.gunAngle != 90 || currentBullet.gunAngle != -90) {
					g2d.rotate(Math.toRadians(currentBullet.gunAngle + 180),
							currentBullet.xPos + p1X * -1 + PLAYER_X + PLAYER_WIDTH / 2,
							currentBullet.yPos + p1Y * -1 + PLAYER_Y + PLAYER_HEIGHT / 2);
					g2d.drawImage(bulletImage, currentBullet.xPos + p1X * -1 + PLAYER_X + PLAYER_WIDTH / 2 + 10,
							currentBullet.yPos + p1Y * -1 + PLAYER_Y + PLAYER_HEIGHT / 2, 14, 7, null);
					g2d.rotate(Math.toRadians(180 - currentBullet.gunAngle),
							currentBullet.xPos + p1X * -1 + PLAYER_X + PLAYER_WIDTH / 2,
							currentBullet.yPos + p1Y * -1 + PLAYER_Y + PLAYER_HEIGHT / 2);
				}
			}
			for (GunBullet currentBullet : p1GunBullets) {
				if (currentBullet.playerRightLooked) {
					g2d.rotate(Math.toRadians(currentBullet.gunAngle), currentBullet.xPosFromPlayer + p1X * -1,
							currentBullet.yPosFromPlayer + p1Y * -1);
					g2d.drawImage(bulletImage, currentBullet.xPosFromPlayer + p1X * -1,
							currentBullet.yPosFromPlayer + p1Y * -1, 17, 7, null);
					g2d.rotate(Math.toRadians(360 - currentBullet.gunAngle), currentBullet.xPosFromPlayer + p1X * -1,
							currentBullet.yPosFromPlayer + p1Y * -1);
				} else if (currentBullet.gunAngle != 90 || currentBullet.gunAngle != -90) {
					g2d.rotate(Math.toRadians(currentBullet.gunAngle + 180), currentBullet.xPosFromPlayer + p1X * -1,
							currentBullet.yPosFromPlayer + p1Y * -1);
					g2d.drawImage(bulletImage, currentBullet.xPosFromPlayer + p1X * -1 + 10,
							currentBullet.yPosFromPlayer + p1Y * -1, 14, 7, null);
					g2d.rotate(Math.toRadians(180 - currentBullet.gunAngle), currentBullet.xPosFromPlayer + p1X * -1,
							currentBullet.yPosFromPlayer + p1Y * -1);
				}

			}
			for (int y = -500; y < FRAME_HEIGHT + 500; y++) {
				if (PLAYER_Y + p2Y + p1Y * -1 + PLAYER_HEIGHT == y && !gameEnded) {
					g2d.setColor(Color.red);
					if (!gameEnded)
						g2d.fillRect(p1X * -1 + p2X + PLAYER_X + 1, p1Y * -1 + p2Y + PLAYER_Y - 25,
								(int) (68 * p2Health / 100), 20);
					g2d.drawImage(movingHealthBar, p1X * -1 + p2X + PLAYER_X, p1Y * -1 + p2Y + PLAYER_Y - 25, 70, 20,
							null);

					if (p2RightLooked) {
						g2d.drawImage(playerRightBlueImage, p1X * -1 + p2X + PLAYER_X, p1Y * -1 + p2Y + PLAYER_Y,
								PLAYER_WIDTH, PLAYER_HEIGHT, null);
					} else if (p2LeftLooked) {
						g2d.drawImage(playerLeftBlueImage, p1X * -1 + p2X + PLAYER_X, p1Y * -1 + p2Y + PLAYER_Y,
								PLAYER_WIDTH, PLAYER_HEIGHT, null);
					}
					if (p2GunType == 0) {
						if (p2RightLooked) {
							g2d.rotate(Math.toRadians(p2GunAngle), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightHandgun, p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2 + 10,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_WIDTH / 2 + 10, 45, 30, null);
							g2d.rotate(Math.toRadians(360 - p2GunAngle), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
						} else if (p2GunAngle != 90 || p2GunAngle != -90) {
							g2d.rotate(Math.toRadians(p2GunAngle + 180), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftHandgun, p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2 + 10,
									p1Y * -1 + PLAYER_Y + p2Y + PLAYER_WIDTH / 2 - 5, 45, 30, null);
							g2d.rotate(Math.toRadians(180 - p2GunAngle), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
						}
					}
					if (p2GunType == 1) {
						if (p2RightLooked) {
							g2d.rotate(Math.toRadians(p2GunAngle), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightDesertEagle, p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2 + 10,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_WIDTH / 2 + 10, 50, 30, null);
							g2d.rotate(Math.toRadians(360 - p2GunAngle), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
						} else if (p2GunAngle != 90 || p2GunAngle != -90) {
							g2d.rotate(Math.toRadians(p2GunAngle + 180), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftDesertEagle, p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2 + 10,
									p1Y * -1 + PLAYER_Y + p2Y + PLAYER_WIDTH / 2 - 5, 50, 30, null);
							g2d.rotate(Math.toRadians(180 - p2GunAngle), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
						}
					}
					if (p2GunType == 2) {
						if (p2RightLooked) {
							g2d.rotate(Math.toRadians(p2GunAngle), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightMicroUzi, p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2 + 10,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_WIDTH / 2 + 10, 60, 50, null);
							g2d.rotate(Math.toRadians(360 - p2GunAngle), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
						} else if (p2GunAngle != 90 || p2GunAngle != -90) {
							g2d.rotate(Math.toRadians(p2GunAngle + 180), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftMicroUzi, p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2 + 10,
									p1Y * -1 + PLAYER_Y + p2Y + PLAYER_WIDTH / 2 - 20, 60, 50, null);
							g2d.rotate(Math.toRadians(180 - p2GunAngle), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
						}
					}
					if (p2GunType == 3) {
						if (p2RightLooked) {
							g2d.rotate(Math.toRadians(p2GunAngle), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightAK, p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2 - 20,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_WIDTH / 2 + 10, 110, 35, null);
							g2d.rotate(Math.toRadians(360 - p2GunAngle), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
						} else if (p2GunAngle != 90 || p2GunAngle != -90) {
							g2d.rotate(Math.toRadians(p2GunAngle + 180), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftAK, p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2 - 20,
									p1Y * -1 + PLAYER_Y + p2Y + PLAYER_WIDTH / 2 - 5, 110, 35, null);
							g2d.rotate(Math.toRadians(180 - p2GunAngle), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
						}
					}
					if (p2GunType == 4) {
						if (p2RightLooked) {
							g2d.rotate(Math.toRadians(p2GunAngle), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightBeretta, p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2 - 20,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_WIDTH / 2 + 10, 110, 45, null);
							g2d.rotate(Math.toRadians(360 - p2GunAngle), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
						} else if (p2GunAngle != 90 || p2GunAngle != -90) {
							g2d.rotate(Math.toRadians(p2GunAngle + 180), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftBeretta, p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2 - 20,
									p1Y * -1 + PLAYER_Y + p2Y + PLAYER_WIDTH / 2 - 15, 110, 45, null);
							g2d.rotate(Math.toRadians(180 - p2GunAngle), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
						}
					}
					if (p2GunType == 5) {
						if (p2RightLooked) {
							g2d.rotate(Math.toRadians(p2GunAngle), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightAWM, p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2 - 20,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_WIDTH / 2 + 10, 130, 30, null);
							g2d.rotate(Math.toRadians(360 - p2GunAngle), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
						} else if (p2GunAngle != 90 || p2GunAngle != -90) {
							g2d.rotate(Math.toRadians(p2GunAngle + 180), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftAWM, p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2 - 20,
									p1Y * -1 + PLAYER_Y + p2Y + PLAYER_WIDTH / 2 - 5, 130, 30, null);
							g2d.rotate(Math.toRadians(180 - p2GunAngle), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
						}
					}
					if (p2GunType == 6) {
						if (p2RightLooked) {
							g2d.rotate(Math.toRadians(p2GunAngle), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightLasergun, p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2 - 40,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_WIDTH / 2 + 7, 160, 50, null);
							g2d.rotate(Math.toRadians(360 - p2GunAngle), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
						} else if (p2GunAngle != 90 || p2GunAngle != -90) {
							g2d.rotate(Math.toRadians(p2GunAngle + 180), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftLasergun, p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2 - 40,
									p1Y * -1 + PLAYER_Y + p2Y + PLAYER_WIDTH / 2 - 20, 160, 50, null);
							g2d.rotate(Math.toRadians(180 - p2GunAngle), p1X * -1 + p2X + PLAYER_X + PLAYER_WIDTH / 2,
									p1Y * -1 + p2Y + PLAYER_Y + PLAYER_HEIGHT / 2);
						}
					}

				}
				if (PLAYER_Y + PLAYER_HEIGHT == y) {

					if (p1RightLooked) {
						g2d.drawImage(playerRightImage, PLAYER_X, PLAYER_Y, PLAYER_WIDTH, PLAYER_HEIGHT, null);
					}

					else {
						g2d.drawImage(playerLeftImage, PLAYER_X, PLAYER_Y, PLAYER_WIDTH, PLAYER_HEIGHT, null);
					}
					if (p1GunType == 0) {
						if (p1RightLooked) {
							g2d.rotate(Math.toRadians(p1GunAngle), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightHandgun, PLAYER_X + PLAYER_WIDTH / 2 + 10,
									PLAYER_Y + PLAYER_WIDTH / 2 + 10, 45, 30, null);
							g2d.rotate(Math.toRadians(360 - p1GunAngle), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
						} else if (p1GunAngle != 90 || p1GunAngle != -90) {
							g2d.rotate(Math.toRadians(p1GunAngle + 180), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftHandgun, PLAYER_X + PLAYER_WIDTH / 2 + 10,
									PLAYER_Y + PLAYER_WIDTH / 2 - 5, 45, 30, null);
							g2d.rotate(Math.toRadians(180 - p1GunAngle), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
						}
					} else if (p1GunType == 1) {
						if (p1RightLooked) {
							g2d.rotate(Math.toRadians(p1GunAngle), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightDesertEagle, PLAYER_X + PLAYER_WIDTH / 2 + 10,
									PLAYER_Y + PLAYER_WIDTH / 2 + 10, 50, 30, null);
							g2d.rotate(Math.toRadians(360 - p1GunAngle), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
						} else if (p1GunAngle != 90 || p1GunAngle != -90) {
							g2d.rotate(Math.toRadians(p1GunAngle + 180), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftDesertEagle, PLAYER_X + PLAYER_WIDTH / 2 + 10,
									PLAYER_Y + PLAYER_WIDTH / 2 - 5, 50, 30, null);
							g2d.rotate(Math.toRadians(180 - p1GunAngle), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
						}
					} else if (p1GunType == 2) {
						if (p1RightLooked) {
							g2d.rotate(Math.toRadians(p1GunAngle), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightMicroUzi, PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_WIDTH / 2 + 10,
									60, 50, null);
							g2d.rotate(Math.toRadians(360 - p1GunAngle), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
						} else {
							g2d.rotate(Math.toRadians(p1GunAngle + 180), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftMicroUzi, PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_WIDTH / 2 - 20,
									60, 50, null);
							g2d.rotate(Math.toRadians(180 - p1GunAngle), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
						}
					} else if (p1GunType == 3) {
						if (p1RightLooked) {
							g2d.rotate(Math.toRadians(p1GunAngle), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightAK, PLAYER_X + PLAYER_WIDTH / 2 - 20, PLAYER_Y + PLAYER_WIDTH / 2 + 10,
									110, 35, null);
							g2d.rotate(Math.toRadians(360 - p1GunAngle), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
						} else {
							g2d.rotate(Math.toRadians(p1GunAngle + 180), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftAK, PLAYER_X + PLAYER_WIDTH / 2 - 20, PLAYER_Y + PLAYER_WIDTH / 2 - 5,
									110, 35, null);
							g2d.rotate(Math.toRadians(180 - p1GunAngle), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
						}
					} else if (p1GunType == 4) {
						if (p1RightLooked) {
							g2d.rotate(Math.toRadians(p1GunAngle), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightBeretta, PLAYER_X + PLAYER_WIDTH / 2 - 20,
									PLAYER_Y + PLAYER_WIDTH / 2 + 10, 110, 45, null);
							g2d.rotate(Math.toRadians(360 - p1GunAngle), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
						} else {
							g2d.rotate(Math.toRadians(p1GunAngle + 180), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftBeretta, PLAYER_X + PLAYER_WIDTH / 2 - 20,
									PLAYER_Y + PLAYER_WIDTH / 2 - 15, 110, 45, null);
							g2d.rotate(Math.toRadians(180 - p1GunAngle), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
						}
					} else if (p1GunType == 5) {
						if (p1RightLooked) {
							g2d.rotate(Math.toRadians(p1GunAngle), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightAWM, PLAYER_X + PLAYER_WIDTH / 2 - 20, PLAYER_Y + PLAYER_WIDTH / 2 + 10,
									130, 30, null);
							g2d.rotate(Math.toRadians(360 - p1GunAngle), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
						} else {
							g2d.rotate(Math.toRadians(p1GunAngle + 180), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftAWM, PLAYER_X + PLAYER_WIDTH / 2 - 20, PLAYER_Y + PLAYER_WIDTH / 2 - 5,
									130, 30, null);
							g2d.rotate(Math.toRadians(180 - p1GunAngle), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
						}
					} else if (p1GunType == 6) {
						if (p1RightLooked) {
							g2d.rotate(Math.toRadians(p1GunAngle), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(rightLasergun, PLAYER_X + PLAYER_WIDTH / 2 - 40,
									PLAYER_Y + PLAYER_WIDTH / 2 + 7, 160, 50, null);
							g2d.rotate(Math.toRadians(360 - p1GunAngle), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
						} else {
							g2d.rotate(Math.toRadians(p1GunAngle + 180), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
							g2d.drawImage(leftLasergun, PLAYER_X + PLAYER_WIDTH / 2 - 40,
									PLAYER_Y + PLAYER_WIDTH / 2 - 20, 160, 50, null);
							g2d.rotate(Math.toRadians(180 - p1GunAngle), PLAYER_X + PLAYER_WIDTH / 2,
									PLAYER_Y + PLAYER_HEIGHT / 2);
						}
					}

					// AWM

				}
				for (int i = 0; i < objects.size(); i++) {
					if (objects.get(i).yPos + p1Y * -1 + objects.get(i).height == y) {
						g2d.drawImage(objects.get(i).objectImage, objects.get(i).xPos + p1X * -1,
								objects.get(i).yPos + p1Y * -1, objects.get(i).width, objects.get(i).height, null);

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
					if (y == (int) (zombie.yPos + zombie.height + p1Y * -1)) {
						if (!zombie.hurt) {
							if (zombie.lookRight) {
								g2d.drawImage(zombie.rightZombieImage, (int) zombie.xPos + p1X * -1,
										(int) zombie.yPos + p1Y * -1, zombie.width, zombie.height, null);
							} else {
								g2d.drawImage(zombie.leftZombieImage, (int) zombie.xPos + p1X * -1,
										(int) zombie.yPos + p1Y * -1, zombie.width, zombie.height, null);
							}
						} else {
							if (zombie.lookRight) {
								g2d.drawImage(zombie.rightHurtZombieImage, (int) zombie.xPos + p1X * -1,
										(int) zombie.yPos + p1Y * -1, zombie.width, zombie.height, null);
							} else {
								g2d.drawImage(zombie.leftHurtZombieImage, (int) zombie.xPos + p1X * -1,
										(int) zombie.yPos + p1Y * -1, zombie.width, zombie.height, null);
							}
						}
					}
				}
				for (Survivor survivor : survivors) {
					if (survivor.yPos + p1Y * -1 + survivor.height == y) {
						if (survivor.lookRight) {
							g2d.drawImage(survivor.rightSurvivorImage, survivor.xPos + p1X * -1,
									survivor.yPos + p1Y * -1, survivor.width, survivor.height, null);
						} else {
							g2d.drawImage(survivor.leftSurvivorImage, survivor.xPos + p1X * -1,
									survivor.yPos + p1Y * -1, survivor.width, survivor.height, null);
						}
					}
				}
			}

			// arc sin opp/hyp =

			/*
			 * bulletCoorX[0] = PLAYER_X + PLAYER_WIDTH / 2;
			 * bulletCoorY[0] = PLAYER_Y + PLAYER_HEIGHT / 2;
			 * 
			 * bulletCoorX[1] = PLAYER_X + PLAYER_WIDTH / 2 + (int)(5 *
			 * Math.cos(Math.toRadians(p1GunAngle + 90)));
			 * bulletCoorY[1] = PLAYER_Y + PLAYER_HEIGHT / 2 + (int)(5 *
			 * Math.sin(Math.toRadians(p1GunAngle + 90)));
			 * 
			 * bulletCoorX[2] = PLAYER_X + PLAYER_WIDTH / 2 + (int)(5 *
			 * Math.cos(Math.toRadians(p1GunAngle + 90))) + (int)(p1BulletDistance *
			 * Math.cos(Math.toRadians(p1GunAngle)));
			 * bulletCoorY[2] = PLAYER_Y + PLAYER_HEIGHT / 2 + (int)(5 *
			 * Math.sin(Math.toRadians(p1GunAngle + 90))) + (int)(p1BulletDistance *
			 * Math.sin(Math.toRadians(p1GunAngle)));
			 * 
			 * bulletCoorX[3] = PLAYER_X + PLAYER_WIDTH / 2 + (int)(p1BulletDistance *
			 * Math.cos(Math.toRadians(p1GunAngle))) + (int)(5 *
			 * Math.cos(Math.toRadians(p1GunAngle + 90)));
			 * bulletCoorY[3] = PLAYER_Y + PLAYER_HEIGHT / 2 + (int)(p1BulletDistance *
			 * Math.sin(Math.toRadians(p1GunAngle)) + (int)(5 *
			 * Math.sin(Math.toRadians(p1GunAngle + 90))));
			 * 
			 * 
			 * g2d.drawLine(bulletCoorX[3], bulletCoorY[3], mouseX, mouseY);
			 */

			/*
			 * g2d.drawLine(PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2,
			 * PLAYER_X + PLAYER_WIDTH / 2 + (int)(p1BulletDistance *
			 * Math.cos(Math.toRadians(p1GunAngle))), PLAYER_Y + PLAYER_HEIGHT / 2 +
			 * (int)(p1BulletDistance * Math.sin(Math.toRadians(p1GunAngle))));
			 * g2d.drawLine(PLAYER_X + PLAYER_WIDTH / 2, PLAYER_Y + PLAYER_HEIGHT / 2,
			 * PLAYER_X + PLAYER_WIDTH / 2 + (int)(5 * Math.cos(Math.toRadians(p1GunAngle +
			 * 90))), PLAYER_Y + PLAYER_HEIGHT / 2 + (int)(5 *
			 * Math.sin(Math.toRadians(p1GunAngle + 90))));
			 * g2d.drawLine(PLAYER_X + PLAYER_WIDTH / 2 + (int)(5 *
			 * Math.cos(Math.toRadians(p1GunAngle + 90))), PLAYER_Y + PLAYER_HEIGHT / 2 +
			 * (int)(5 * Math.sin(Math.toRadians(p1GunAngle + 90))),PLAYER_X + PLAYER_WIDTH
			 * / 2 + (int)(5 * Math.cos(Math.toRadians(p1GunAngle + 90))) +
			 * (int)(p1BulletDistance * Math.cos(Math.toRadians(p1GunAngle))), PLAYER_Y +
			 * PLAYER_HEIGHT / 2 + (int)(5 * Math.sin(Math.toRadians(p1GunAngle + 90)) +
			 * (int)(p1BulletDistance * Math.sin(Math.toRadians(p1GunAngle)))));
			 * g2d.drawLine(PLAYER_X + PLAYER_WIDTH / 2 + (int)(p1BulletDistance *
			 * Math.cos(Math.toRadians(p1GunAngle))), PLAYER_Y + PLAYER_HEIGHT / 2 +
			 * (int)(p1BulletDistance * Math.sin(Math.toRadians(p1GunAngle))),PLAYER_X +
			 * PLAYER_WIDTH / 2 + (int)(p1BulletDistance *
			 * Math.cos(Math.toRadians(p1GunAngle))) + (int)(5 *
			 * Math.cos(Math.toRadians(p1GunAngle + 90))), PLAYER_Y + PLAYER_HEIGHT / 2 +
			 * (int)(p1BulletDistance * Math.sin(Math.toRadians(p1GunAngle)) + (int)(5 *
			 * Math.sin(Math.toRadians(p1GunAngle +90)))));
			 */
			g2d.setColor(Color.red);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
			if (!gameEnded)
				g2d.fillRect(15, 0, (int) (p1Health * 4.5), 80);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			g2d.drawImage(healthBar, 0, 0, 500, 90, null);
			g2d.setColor(Color.gray);
			g2d.setColor(Color.BLUE);
			if (p1InvisTimer > 1) {
				g2d.drawImage(invisScreen, 0, 0, FRAME_WIDTH, FRAME_HEIGHT, null);
			}

			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			g2d.setFont(new Font("Sans serif", Font.BOLD, 25));
			g2d.setFont(new Font("Arial", Font.PLAIN, 10));

			g2d.setFont(new Font("Sans serif", Font.BOLD, 25));
			g2d.setColor(Color.black);

			for (RainDropletsGame currentDrop : rain) {
				g2d.drawImage(raindropImage, currentDrop.xPos + p2X * -1, currentDrop.yPos + p1Y * -1, 13, 25, null);
			}
			g2d.setColor(Color.black);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			g2d.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);

			g2d.drawImage(bloodStainScreen, 0, -30, FRAME_WIDTH, FRAME_HEIGHT + 110, null);
			g2d.setFont(new Font("Sans serif", Font.PLAIN, 10));

		}
		if (gameEnded && fadeInGame < 0.4) {
			g2d.setFont(new Font("Sans serif", Font.BOLD, 50));
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
			g2d.drawImage(p1Mouse, p1MouseX, p1MouseY, 50, 50, null);
		}
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		if (!client.connectionStatus)
			g2d.drawImage(exitClientSign, 420, 210, 500, 240, null);
	}

	public void updateAllRainDroplets() {
		/*
		 * This makes sure every 3 ms, a bullet is created
		 * and is added to the array list rain. The timer
		 * calls the update method for each rain so that
		 * the position of each raindrop changes.
		 */
		raindropCounter++;

		if (raindropCounter % 3 == 0)
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

	public void updateAllSurvivors() {
		survivors.clear();
		for (int i = 199; i > 0; i--) {
			playerTwoStepsX.set(i, playerTwoStepsX.get(i - 1));
		}
		playerTwoStepsX.set(0, p2X * -1);

		for (int i = 199; i > 0; i--) {
			playerTwoStepsY.set(i, playerTwoStepsY.get(i - 1));
		}
		playerTwoStepsY.set(0, p2Y * -1);

		client.playerTwoStepsXArray = new int[playerTwoStepsX.size()];
		client.playerTwoStepsYArray = new int[playerTwoStepsY.size()];

		for (int i = 0; i < playerTwoStepsX.size(); i++) {
			client.playerTwoStepsXArray[i] = (int) playerTwoStepsX.get(i);
			client.playerTwoStepsYArray[i] = (int) playerTwoStepsY.get(i);
		}

		for (int i = 0; i < survivorLookRight.length; i++) {
			survivors.add(new Survivor(survivorXPos[i], survivorYPos[i], survivorType[i], survivorLookRight[i]));
		}

	}

	public void setUpBulletCoordinatesPlayerOne() {

		/*
		 * THIS METHOD IS NO LONGER USED BUT I KEEP IT JUST IN CASE
		 * I NEED IT FOR SOMETHING ELSE. THIS WAS THE ORIGINAL BULLET
		 * BUT YOU TOLD ME TO ADD PHYSICAL BULLETS COMING OUT OF
		 * THE GUN SO THIS IS NO LONGER USED.
		 */
		p1BulletCoorX[0] = client.p1BulletCoorX[0] + p1X + p2X * -1;
		p1BulletCoorY[0] = client.p1BulletCoorY[0] + p1Y + p2Y * -1;

		p1BulletCoorX[1] = client.p1BulletCoorX[1] + p1X + p2X * -1;
		p1BulletCoorY[1] = client.p1BulletCoorY[1] + p1Y + p2Y * -1;

		p1BulletCoorX[2] = client.p1BulletCoorX[2] + p1X + p2X * -1;
		p1BulletCoorY[2] = client.p1BulletCoorY[2] + p1Y + p2Y * -1;

		p1BulletCoorX[3] = client.p1BulletCoorX[3] + p1X + p2X * -1;
		p1BulletCoorY[3] = client.p1BulletCoorY[3] + p1Y + p2Y * -1;

		p1Bullet = new Polygon(p1BulletCoorX, p1BulletCoorY, 4);
	}

	public void setUpBulletCoordinatesPlayerTwo() {
		/*
		 * THIS METHOD IS NO LONGER USED BUT I KEEP IT JUST IN CASE
		 * I NEED IT FOR SOMETHING ELSE. THIS WAS THE ORIGINAL BULLET
		 * BUT YOU TOLD ME TO ADD PHYSICAL BULLETS COMING OUT OF
		 * THE GUN SO THIS IS NO LONGER USED.
		 */
		if (mouseX - PLAYER_X != 0) {
			p2GunAngle = Math.toDegrees(Math.atan((((double) mouseY - PLAYER_Y - PLAYER_HEIGHT / 2)
					/ (((double) mouseX - PLAYER_X - PLAYER_WIDTH / 2)))));
		}
		if (p2RightLooked) {
			p2BulletCoorX[0] = PLAYER_X + PLAYER_WIDTH / 2;
			p2BulletCoorY[0] = PLAYER_Y + PLAYER_HEIGHT / 2;

			p2BulletCoorX[1] = PLAYER_X + PLAYER_WIDTH / 2
					+ (int) (bulletLength * Math.cos(Math.toRadians(p2GunAngle)));
			p2BulletCoorY[1] = PLAYER_Y + PLAYER_HEIGHT / 2
					+ (int) (bulletLength * Math.sin(Math.toRadians(p2GunAngle)));

			p2BulletCoorX[2] = PLAYER_X + PLAYER_WIDTH / 2 + (int) (bulletLength * Math.cos(Math.toRadians(p2GunAngle)))
					+ (int) (bulletWidth * Math.cos(Math.toRadians(p2GunAngle + 90)));
			p2BulletCoorY[2] = PLAYER_Y + PLAYER_HEIGHT / 2 + (int) (bulletLength * Math.sin(Math.toRadians(p2GunAngle))
					+ (int) (bulletWidth * Math.sin(Math.toRadians(p2GunAngle + 90))));

			p2BulletCoorX[3] = PLAYER_X + PLAYER_WIDTH / 2
					+ (int) (bulletWidth * Math.cos(Math.toRadians(p2GunAngle + 90)));
			p2BulletCoorY[3] = PLAYER_Y + PLAYER_HEIGHT / 2
					+ (int) (bulletWidth * Math.sin(Math.toRadians(p2GunAngle + 90)));
		} else if (p2LeftLooked && p2GunAngle != -90 && p2GunAngle != 90) {
			p2BulletCoorX[0] = PLAYER_X + PLAYER_WIDTH / 2;
			p2BulletCoorY[0] = PLAYER_Y + PLAYER_HEIGHT / 2;

			p2BulletCoorX[1] = PLAYER_X + PLAYER_WIDTH / 2
					+ ((int) (bulletLength * Math.cos(Math.toRadians(p2GunAngle)))) * -1;
			p2BulletCoorY[1] = PLAYER_Y + PLAYER_HEIGHT / 2
					+ ((int) (bulletLength * Math.sin(Math.toRadians(p2GunAngle)))) * -1;

			p2BulletCoorX[2] = PLAYER_X + PLAYER_WIDTH / 2
					+ ((int) (bulletLength * Math.cos(Math.toRadians(p2GunAngle)))
							+ (int) (bulletWidth * Math.cos(Math.toRadians(p2GunAngle + 90)))) * -1;
			p2BulletCoorY[2] = PLAYER_Y + PLAYER_HEIGHT / 2
					+ ((int) (bulletLength * Math.sin(Math.toRadians(p2GunAngle))
							+ (int) (bulletWidth * Math.sin(Math.toRadians(p2GunAngle + 90))))) * -1;

			p2BulletCoorX[3] = PLAYER_X + PLAYER_WIDTH / 2
					+ ((int) (bulletWidth * Math.cos(Math.toRadians(p2GunAngle + 90)))) * -1;
			p2BulletCoorY[3] = PLAYER_Y + PLAYER_HEIGHT / 2
					+ ((int) (bulletWidth * Math.sin(Math.toRadians(p2GunAngle + 90)))) * -1;
		}
		p2Bullet = new Polygon(p2BulletCoorX, p2BulletCoorY, 4);
	}

	public void updateAllObjects() {
		/*
		 * Every 5 ms, the objects are updated in the server (The
		 * object name, object xpos, object ypos, and the amount
		 * of objects) so the server does not send an old version
		 * of the arrays and the client class will have different objects
		 * compared to the server class.
		 */
		objects.clear();
		for (int i = 0; i < objectsName.length; i++) {
			objects.add(new Objects(objectsName[i], objectsXPos[i], objectsYPos[i]));
		}
	}

	public void updateAllBullets() {
		/*
		 * This does the exact same thing as the objects, but updates
		 * the bullets for the client so the server does not get a
		 * malfunctioning bullet. This class also uses the variables
		 * (bullet parameters) from the server and creates bullets
		 * with them. You cannot send an array of objects other than
		 * Strings so I decided to send the parameters of making a bullet
		 * and create bullets using it and make the exact same bullets
		 * as the other class
		 */
		if (bullet.showBullet) {
			// public GunBullet(double gunAngle, int xPos, int yPos, int playerX, int
			// playerY, boolean playerRightLooked, int type)
			p2GunBullets.add(new GunBullet(p2GunAngle, p2X, p2Y, PLAYER_X + PLAYER_WIDTH / 2 + p2X,
					PLAYER_Y + PLAYER_HEIGHT / 2 + p2Y, p2RightLooked, p2BulletDistance));
			bullet.showBullet = false;
			playerTwoBulletsID.add(shotBullets);
			shotBullets++;
		}

		for (GunBullet gb : p2GunBullets) {
			gb.update();
		}

		for (int i = 0; i < p2GunBullets.size(); i++) {
			if (p2GunBullets.get(i).deleteThisClone) {
				p2GunBullets.remove(i);
				playerTwoBulletsID.remove(i);
			}
		}

		client.p2GunBulletsID = new int[playerTwoBulletsID.size()];

		for (int z = 0; z < playerTwoBulletsID.size(); z++) {
			client.p2GunBulletsID[z] = playerTwoBulletsID.get(z);
		}

		p2GunBulletXPos.clear();
		p2GunBulletYPos.clear();
		p2GunBulletPlayerRightLooked.clear();
		p2GunBulletGunAngle.clear();
		p2GunBulletXPosFromPlayer.clear();
		p2GunBulletYPosFromPlayer.clear();

		for (GunBullet gb : p2GunBullets) {
			p2GunBulletXPos.add(gb.xPos);
			p2GunBulletYPos.add(gb.yPos);
			p2GunBulletPlayerRightLooked.add(gb.playerRightLooked);
			p2GunBulletGunAngle.add(gb.gunAngle);
			p2GunBulletXPosFromPlayer.add(gb.xPosFromPlayer);
			p2GunBulletYPosFromPlayer.add(gb.yPosFromPlayer);
		}

		client.p2GunBulletXPos = new int[p2GunBulletXPos.size()];
		client.p2GunBulletYPos = new int[p2GunBulletYPos.size()];
		client.p2GunBulletGunAngle = new double[p2GunBulletGunAngle.size()];
		client.p2GunBulletPlayerRightLooked = new boolean[p2GunBulletPlayerRightLooked.size()];
		client.p2GunBulletXPosFromPlayer = new int[p2GunBulletXPosFromPlayer.size()];
		client.p2GunBulletYPosFromPlayer = new int[p2GunBulletYPosFromPlayer.size()];
		client.playerTwoBulletsID = new int[playerTwoBulletsID.size()];
		//
		for (int gb = 0; gb < p2GunBullets.size(); gb++) {
			client.p2GunBulletXPos[gb] = p2GunBulletXPos.get(gb);
			client.p2GunBulletYPos[gb] = p2GunBulletYPos.get(gb);
			client.p2GunBulletGunAngle[gb] = p2GunBulletGunAngle.get(gb);
			client.p2GunBulletPlayerRightLooked[gb] = p2GunBulletPlayerRightLooked.get(gb);
			client.p2GunBulletXPosFromPlayer[gb] = p2GunBulletXPosFromPlayer.get(gb);
			client.p2GunBulletYPosFromPlayer[gb] = p2GunBulletYPosFromPlayer.get(gb);
		}
		for (int i = 0; i < playerTwoBulletsID.size(); i++) {
			client.playerTwoBulletsID[i] = playerTwoBulletsID.get(i);
		}

		////////////////////////////////////////////////////
		p1GunBullets.clear();

		p1GunBulletGunAngle = client.p1GunBulletGunAngle;
		p1GunBulletPlayerRightLooked = client.p1GunBulletPlayerRightLooked;
		p1GunBulletXPos = client.p1GunBulletXPos;
		p1GunBulletYPos = client.p1GunBulletYPos;
		p1GunBulletXPosFromPlayer = client.p1GunBulletXPosFromPlayer;
		p1GunBulletYPosFromPlayer = client.p1GunBulletYPosFromPlayer;

		for (int gb = 0; gb < p1GunBulletGunAngle.length; gb++) {
			p1GunBullets.add(new GunBullet(p1GunBulletGunAngle[gb], p1GunBulletXPos[gb], p1GunBulletYPos[gb],
					p1GunBulletXPosFromPlayer[gb], p1GunBulletYPosFromPlayer[gb], p1GunBulletPlayerRightLooked[gb],
					550));
		}

		for (int i = 0; i < p2GunBullets.size(); i++) {
			if (playerTwoBulletsID.get(i) == deleteBulletID) {
				playerTwoBulletsID.remove(i);
				p2GunBullets.remove(i);
			}
		}
	}

	public void updateGunSettings() {
		// bullet.p1GunType = p1GunType;
		if (p2GunType == 0) {
			p2BulletDistance = 550;
			bullet.reloadTime = 30;
			p2BulletDamage = 9;
			bullet.ammoAmount = 0;
			bullet.maxAmmo = 1000000;
			bullet.ammoReloadTime = 80;
		} else if (p2GunType == 1) {
			p2BulletDistance = 550;
			bullet.reloadTime = 30;
			p2BulletDamage = 11;
			bullet.maxAmmo = 20;
		} else if (p2GunType == 2) {
			p2BulletDistance = 550;
			bullet.reloadTime = 10;
			p2BulletDamage = 3;
			bullet.maxAmmo = 20;
			bullet.ammoReloadTime = 75;
		} else if (p2GunType == 3) {
			p2BulletDistance = 650;
			bullet.reloadTime = 4;
			p2BulletDamage = 2;
			bullet.maxAmmo = 40;
			bullet.ammoReloadTime = 75;
		} else if (p2GunType == 4) {
			p2BulletDistance = 700;
			bullet.reloadTime = 5;
			p2BulletDamage = 2;
			bullet.maxAmmo = 60;
			bullet.ammoReloadTime = 90;
		} else if (p2GunType == 5) {
			p2BulletDistance = 1400;
			bullet.reloadTime = 10;
			p2BulletDamage = 200;
			bullet.maxAmmo = 1;
			bullet.ammoReloadTime = 45;
		} else if (p2GunType == 6) {
			bullet.reloadTime = 1000000000;
			bullet.ammoAmount = 0;
			p2BulletDamage = 10;
		}
		client.p2BulletDamage = p2BulletDamage;
	}

	public void updateAllZombies() {
		zombieXPos = client.zombieXPos;
		zombieYPos = client.zombieYPos;
		zombieHealth = client.zombieHealth;
		zombieHurt = client.zombieHurt;
		zombieType = client.zombieType;
		zombieLookRight = client.zombieLookRight;
		zombies.clear();
		for (int i = 0; i < zombieHurt.length; i++) {
			zombies.add(new Zombie(zombieType[i], zombieXPos[i], zombieYPos[i], zombieHealth[i], zombieHurt[i],
					zombieLookRight[i]));
		}
	}

	public void keyPressed(KeyEvent e) {
		/*
		 * Checks if wasd keys are pressed and sets booleans to true
		 */
		requestFocus();
		if (!gameEnded) {
			if (client.movementType == 0) {
				if (e.getKeyCode() == KeyEvent.VK_W)
					upKey = true;
				if (e.getKeyCode() == KeyEvent.VK_S)
					downKey = true;
				if (e.getKeyCode() == KeyEvent.VK_A)
					leftKey = true;
				if (e.getKeyCode() == KeyEvent.VK_D)
					rightKey = true;
			} else {
				if (e.getKeyCode() == KeyEvent.VK_UP)
					upKey = true;
				if (e.getKeyCode() == KeyEvent.VK_DOWN)
					downKey = true;
				if (e.getKeyCode() == KeyEvent.VK_LEFT)
					leftKey = true;
				if (e.getKeyCode() == KeyEvent.VK_RIGHT)
					rightKey = true;
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		/*
		 * Checks if wasd keys are released and sets booleans to false
		 */

		requestFocus();
		if (!gameEnded) {
			if (client.movementType == 0) {
				if (e.getKeyCode() == KeyEvent.VK_W)
					upKey = false;
				if (e.getKeyCode() == KeyEvent.VK_S)
					downKey = false;
				if (e.getKeyCode() == KeyEvent.VK_A)
					leftKey = false;
				if (e.getKeyCode() == KeyEvent.VK_D)
					rightKey = false;
			} else {
				if (e.getKeyCode() == KeyEvent.VK_UP)
					upKey = false;
				if (e.getKeyCode() == KeyEvent.VK_DOWN)
					downKey = false;
				if (e.getKeyCode() == KeyEvent.VK_LEFT)
					leftKey = false;
				if (e.getKeyCode() == KeyEvent.VK_RIGHT)
					rightKey = false;
			}
		}
	}

	public void keyTyped(KeyEvent e) {
		requestFocus();
		if (e.getKeyChar() == '1') {
			p2GunType = 0;
		} else if (e.getKeyChar() == '2') {
			p2GunType = 1;
		} else if (e.getKeyChar() == '3') {
			p2GunType = 2;
		} else if (e.getKeyChar() == '4') {
			p2GunType = 3;
		} else if (e.getKeyChar() == '5') {
			p2GunType = 4;
		} else if (e.getKeyChar() == '6') {
			p2GunType = 5;
		} else if (e.getKeyChar() == '7') {
			p2GunType = 6;
		}
	}

	public void mouseDragged(MouseEvent e) {
		/*
		 * Sets mouseX to xpos of mouse and mouseY to ypos of mouse so
		 * other methods can use it
		 */
		requestFocus();
		mouseX = e.getX();
		mouseY = e.getY();

		mouseFromMiddleX = mouseX - (this.getWidth() / 2);
		mouseFromMiddleY = mouseY - (this.getHeight() / 2);
	}

	public void mouseMoved(MouseEvent e) {
		/*
		 * Sets mouseX to xpos of mouse and mouseY to ypos of mouse so
		 * other methods can use it
		 */
		requestFocus();
		mouseX = e.getX();
		mouseY = e.getY();

		mouseFromMiddleX = mouseX - (this.getWidth() / 2);
		mouseFromMiddleY = mouseY - (this.getHeight() / 2);
	}

	public void mouseClicked(MouseEvent e) {
		/*
		 * Sets mouseX to xpos of mouse and mouseY to ypos of mouse so
		 * other methods can use it
		 */
		requestFocus();
		mouseX = e.getX();
		mouseY = e.getY();

		if (!client.connectionStatus) {
			if (connectionLostExitClient.contains(mouseX, mouseY)) {
				System.exit(1);
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
		/*
		 * Sets mouseX to xpos of mouse and mouseY to ypos of mouse so
		 * other methods can use it
		 */
		requestFocus();
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mouseExited(MouseEvent e) {
		/*
		 * Sets mouseX to xpos of mouse and mouseY to ypos of mouse so
		 * other methods can use it
		 */
		requestFocus();
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mousePressed(MouseEvent e) {
		/*
		 * Checks if mouse is pressed and sets a boolean to true
		 */
		requestFocus();
		pressMouse = true;

		if (!gameEnded) {
			if (p2GunType == 6) {
				p2ShowLaser = true;
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		/*
		 * Checks if mouse is released and sets a boolean to false
		 */
		requestFocus();
		pressMouse = false;
		if (p2GunType == 6) {
			p2ShowLaser = false;
		}
	}

}
