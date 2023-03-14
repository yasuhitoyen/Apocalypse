
/*
 * Michael Yen
 * A P O C A L Y P S E
 */
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import javax.swing.Timer;
import javax.swing.*;

public class ControlPanel extends JPanel implements ActionListener {
	/*
	 * Essentially what this class does is it monitors which
	 * of the panels it will show. SO FAR there are 3 panels
	 * in the card layout. The "In-Game" panel, the "Equipment",
	 * and the "Levels".
	 * 
	 */
	final int FRAME_WIDTH = 1370, FRAME_HEIGHT = 750, PLAYER_WIDTH = 68, PLAYER_HEIGHT = 100, FRAME_DIFFERENCE = 40,
			PLAYER_X = FRAME_WIDTH / 2 - FRAME_DIFFERENCE - PLAYER_WIDTH / 2,
			PLAYER_Y = FRAME_HEIGHT / 2 - FRAME_DIFFERENCE;
	final int PLAYER_HITBOX_X = PLAYER_X, PLAYER_HITBOX_Y = PLAYER_Y + 70, PLAYER_HITBOX_WIDTH = PLAYER_WIDTH,
			PLAYER_HITBOX_HEIGHT = 30;

	Scanner scan = new Scanner(System.in);
	GameClient client;
	GameServer server;
	PlayerOne playerOne;

	int optionPlayerTwo;
	PlayerTwo playerTwo;
	EquipmentShop equipmentShop;
	int p1MouseX, p1MouseY, p2MouseX, p2MouseY, zombieLimit;
	String mapName;
	LevelSelect levelSelect;
	String[] p2GameChat;
	int playerType, gameType, gameMode;
	String test;
	Timer gameTick = new Timer(2, this);
	CardLayout panelLayout;
	boolean resetPlayerTwo;
	boolean connected;
	boolean singlePlayer;

	public ControlPanel(int gameType, GameClient client) throws IOException {
		/*
		 * This is one of the two constructors. This constructor is only for player two
		 * because client is for player two and server is for player one. I then set
		 * the client to the client I created in this class so I still have an
		 * established connection between the two computers (or else I would have to
		 * type the IP address and do all that garbo)
		 * 
		 */
		/*
		 * 3 panels with:
		 * - In game panel
		 * - Equipping different guns panel
		 * - Level select panel
		 * 
		 * Adds it to the main panel with card layout
		 */
		optionPlayerTwo = 2;
		gameMode = 2;
		this.client = client;
		this.gameType = gameType;
		equipmentShop = new EquipmentShop();
		playerTwo = new PlayerTwo(client);
		panelLayout = new CardLayout();
		levelSelect = new LevelSelect();
		setLayout(panelLayout);
		add(playerTwo, "In-Game");
		add(equipmentShop, "Equipment");
		add(levelSelect, "Levels");
		gameTick.start();
		panelLayout.show(this, "Levels");
	}

	public ControlPanel(int gameType, GameServer server) throws IOException {
		/*
		 * This is for player one because it has a server and servers are connected
		 * to player one. The gameType is not very important now. I will be using it
		 * later for when the control panel is created, I can choose which panel i want
		 * to show first. (level select, equipment, in game etc.)
		 * 
		 */
		/*
		 * 3 panels with:
		 * - In game panel
		 * - Equipping different guns panel
		 * - Level select panel
		 * 
		 * Add it to the main panel with card layout
		 */

		gameMode = 2;
		setLayout(new CardLayout());
		this.server = server;
		this.gameType = gameType;
		playerOne = new PlayerOne(server);
		playerOne.singlePlayer = false;
		resetPlayerTwo = false;
		equipmentShop = new EquipmentShop();
		panelLayout = new CardLayout();
		levelSelect = new LevelSelect();
		setLayout(panelLayout);
		add(playerOne, "In-Game");
		add(equipmentShop, "Equipment");
		add(levelSelect, "Levels");
		gameTick.start();
		panelLayout.show(this, "Levels");
		gameTick.start();
	}

	public ControlPanel() throws IOException {
		gameMode = 2;
		setLayout(new CardLayout());
		gameType = 0;
		playerOne = new PlayerOne(true);
		resetPlayerTwo = false;
		equipmentShop = new EquipmentShop();
		panelLayout = new CardLayout();
		levelSelect = new LevelSelect();
		setLayout(panelLayout);
		add(playerOne, "In-Game");
		add(equipmentShop, "Equipment");
		add(levelSelect, "Levels");
		gameTick.start();
		panelLayout.show(this, "Levels");
		gameTick.start();
		singlePlayer = true;
		levelSelect.singlePlayer = true;
		playerOne.singlePlayer = true;
	}

	public void actionPerformed(ActionEvent e) {
		/*
		 * Every 2 ms it checks if the back button in the equipment button is pressed
		 * from the level select panel.
		 * 
		 * Also creates the levels and sets the guns depending on what level is pressed
		 * and wwhat boolean is set to true etc in the level select class and the gun
		 * equipment class
		 */
		if (!singlePlayer) {
			if (gameType == 1) {
				levelSelect.p2MouseX = server.p2MouseX;
				levelSelect.p2MouseY = server.p2MouseY;
				levelSelect.connected = server.connectionStatus;
				equipmentShop.connected = server.connectionStatus;
				if (gameMode == 1) {
					server.p1MouseX = playerOne.mouseX;
					server.p1MouseY = playerOne.mouseY;
				} else if (gameMode == 2) {
					server.p1MouseX = levelSelect.mouseX;
					server.p1MouseY = levelSelect.mouseY;
				}
			}
			levelSelect.gameType = gameType;
			if (gameType == 1) {
				server.gameMode = gameMode;
				server.mapName = mapName;
				playerOne.p1GunType = equipmentShop.gunType;
				levelSelect.recievedMessageNum = server.messageNum;
				levelSelect.gotText = server.text;
				server.gameChat = levelSelect.chat.toArray(new String[levelSelect.chat.size()]);

			} else if (gameType == 2) {

				equipmentShop.connected = client.connectionStatus;
				levelSelect.connected = client.connectionStatus;
				mapName = client.mapName;
				playerTwo.background = new ImageIcon(mapName).getImage();
				levelSelect.p1MouseX = client.p1MouseX;
				levelSelect.p1MouseY = client.p1MouseY;
				levelSelect.p2GameChat = client.gameChat;
				if (client.gameMode == 1) {
					if (!client.gameEnded)
						playerTwo.gotGold = false;

					if (playerTwo.fadeInGame < 0.7)
						gameMode = client.gameMode;
				}
				client.messageNumber = levelSelect.messageNum;
				client.text = levelSelect.oldMessage;
				if (gameMode == 1) {
					client.p2MouseX = playerTwo.mouseX;
					client.p2MouseY = playerTwo.mouseY;
					playerTwo.p2GunType = equipmentShop.gunType;
				} else {
					client.p2MouseX = levelSelect.mouseX;
					client.p2MouseY = levelSelect.mouseY;
				}
				if (gameMode == 3) {
					client.p2UsingSettings = true;
				} else {
					client.p2UsingSettings = false;
				}
			}
			if (gameMode == 1) {
				if (gameType == 1) {
					panelLayout.show(this, "In-Game");
				} else {
					if (client.fadeInGame < 0.8) {
						panelLayout.show(this, "In-Game");
					}
				}
				if (gameType == 2 && !resetPlayerTwo) {
					resetPlayerTwo = true;
					playerTwo.p2X = 1355;
					playerTwo.p2Y = 920;
					playerTwo.spectatePlayerOne = false;
					playerTwo.gameMode = gameMode;
				}
			} else if (gameMode == 2) {
				resetPlayerTwo = false;
				panelLayout.show(this, "Levels");
				if (gameType == 1) {
					server.showInfo = levelSelect.showInfo;
					server.level = levelSelect.level;
				}
				if (gameType == 2) {
					playerTwo.fadeInGame = 1.0f;
					levelSelect.showInfo = client.showInfo;
					levelSelect.level = client.level;
				} else if (gameType == 1) {
					server.gameEnded = true;
				}
			} else if (gameMode == 3) {
				resetPlayerTwo = false;
				panelLayout.show(this, "Equipment");
			}
			if (levelSelect.equipPressed) {
				if (!levelSelect.showInfo) {
					if (gameType == 1) {
						levelSelect.equipPressed = false;
						panelLayout.show(this, "Equipment");
						gameMode = 3;
					}

				} else {
					levelSelect.equipPressed = false;
				}

				if (gameType == 2) {
					optionPlayerTwo = 3;
					levelSelect.equipPressed = false;
				}
			}
			if (equipmentShop.backPressed) {
				if (gameType == 1) {
					equipmentShop.backPressed = false;
					panelLayout.show(this, "Levels");
					gameMode = 2;
				} else {
					optionPlayerTwo = 2;
					equipmentShop.backPressed = false;
				}
			}
			if (gameMode == 1) {
				if (gameType == 1) {
					if (playerOne.backToLevelSelect) {
						equipmentShop.money += playerOne.gold;
						playerOne.backToLevelSelect = false;
						gameMode = 2;
					}
				}
			}
			if (levelSelect.selectedLevel && gameType == 1) {
				if (!server.p2UsingSettings) {
					levelSelect.selectedLevel = false;
					playerOne.fadeInGame = 1.0f;
					playerOne.resetGame();
					if (levelSelect.level + 1 == 1) {
						mapName = "Map2.png";
						playerOne.background = new ImageIcon("../images/Map2.png").getImage();
						playerOne.objects.add(new Objects("Tree", 10, 10));
						playerOne.objects.add(new Objects("Tree", 500, 30));
						playerOne.objects.add(new Objects("Tree", 130, 100));
						playerOne.objects.add(new Objects("Tree", 420, 850));
						playerOne.objects.add(new Objects("Tree", 300, 680));
						playerOne.objects.add(new Objects("Tree", 0, 700));
						playerOne.objects.add(new Objects("Tree", 510, 10));
						playerOne.objects.add(new Objects("Tree", 1520, 1800));
						playerOne.objects.add(new Objects("Tree", 520, 2000));
						playerOne.objects.add(new Objects("Tree", 270, 900));
						playerOne.objects.add(new Objects("Tree", 800, 1500));
						playerOne.objects.add(new Objects("Tree", 1500, 500));
						playerOne.objects.add(new Objects("Tree", 500, 2700));
						playerOne.objects.add(new Objects("Tree", 1200, 1800));
						playerOne.objects.add(new Objects("Tree", 1500, 1000));
						playerOne.objects.add(new Objects("Tree", 2300, 1800));
						playerOne.objects.add(new Objects("Tree", 2100, 900));
						playerOne.objects.add(new Objects("Tree", 1220, 1600));
						playerOne.objects.add(new Objects("Tree", 1900, 1500));
						playerOne.objects.add(new Objects("Tree", 1600, 2000));
						playerOne.objects.add(new Objects("SevenEleven", 0, 0));
						playerOne.objects.add(new Objects("Powerup", (int) (FRAME_WIDTH * 3.0 / 2.0) + 100,
								(int) (FRAME_HEIGHT * 3.0 / 2.0)));

						playerOne.survivors.add(new Survivor(1355, 890, 4, true));
						playerOne.survivors.add(new Survivor(700, 1300, 2, true));
						playerOne.survivors.add(new Survivor(3000, 670, 3, true));
						playerOne.survivors.add(new Survivor(900, 600, 2, true));
						playerOne.zombieLimit = 1;

						playerOne.zombiesAtOnce = 15;
					} else if (levelSelect.level + 1 == 2) {
						mapName = "Map1.png";
						playerOne.background = new ImageIcon("../images/Map1.png").getImage();
						playerOne.objects.add(new Objects("Tree", 10, 10));
						playerOne.objects.add(new Objects("Tree", 130, 100));
						playerOne.objects.add(new Objects("Tree", 420, 850));
						playerOne.objects.add(new Objects("Tree", 300, 680));
						playerOne.objects.add(new Objects("Tree", 1520, 1800));
						playerOne.objects.add(new Objects("Tree", 520, 2000));
						playerOne.objects.add(new Objects("Tree", 270, 900));
						playerOne.objects.add(new Objects("Tree", 800, 1500));
						playerOne.objects.add(new Objects("Tree", 500, 2700));
						playerOne.objects.add(new Objects("Tree", 1500, 1000));
						playerOne.objects.add(new Objects("Tree", 2100, 900));

						playerOne.survivors.add(new Survivor(700, 500, 4, true));
						playerOne.survivors.add(new Survivor(2000, 700, 1, true));
						playerOne.survivors.add(new Survivor(2700, 1400, 1, true));
						playerOne.survivors.add(new Survivor(650, 900, 3, true));
						playerOne.survivors.add(new Survivor(1500, 1250, 2, true));
						playerOne.survivors.add(new Survivor(2300, 1200, 2, true));

						playerOne.zombieLimit = 2;

						playerOne.zombiesAtOnce = 18;
					} else if (levelSelect.level + 1 == 3) {
						mapName = "Map3.png";
						playerOne.background = new ImageIcon("../images/Map3.png").getImage();
						playerOne.objects.add(new Objects("Tree", 10, 10));
						playerOne.objects.add(new Objects("Tree", 130, 100));
						playerOne.objects.add(new Objects("Tree", 420, 850));
						playerOne.objects.add(new Objects("Tree", 300, 680));
						playerOne.objects.add(new Objects("Tree", 1520, 1800));
						playerOne.objects.add(new Objects("Tree", 520, 2000));
						playerOne.objects.add(new Objects("Tree", 270, 900));
						playerOne.objects.add(new Objects("Tree", 800, 1500));
						playerOne.objects.add(new Objects("Tree", 500, 2700));
						playerOne.objects.add(new Objects("Tree", 1500, 1000));
						playerOne.objects.add(new Objects("Tree", 2100, 900));

						playerOne.survivors.add(new Survivor(1300, 890, 4, true));
						playerOne.survivors.add(new Survivor(700, 700, 1, true));
						playerOne.survivors.add(new Survivor(1500, 1600, 1, true));
						playerOne.survivors.add(new Survivor(1900, 1100, 1, true));
						playerOne.survivors.add(new Survivor(650, 900, 3, true));
						playerOne.survivors.add(new Survivor(1500, 1250, 2, true));
						playerOne.survivors.add(new Survivor(2300, 1200, 2, true));

						playerOne.zombieLimit = 3;

						playerOne.zombiesAtOnce = 20;
					} else if (levelSelect.level + 1 == 4) {
						mapName = "Map4.png";
						playerOne.background = new ImageIcon("../images/Map4.png").getImage();
						playerOne.objects.add(new Objects("TelephoneBooth", 250, 1200));
						playerOne.objects.add(new Objects("Tree", 10, 10));
						playerOne.objects.add(new Objects("Tree", 130, 100));
						playerOne.objects.add(new Objects("Tree", 420, 850));
						playerOne.objects.add(new Objects("Tree", 300, 680));
						playerOne.objects.add(new Objects("Tree", 1520, 1800));
						playerOne.objects.add(new Objects("Tree", 520, 2000));
						playerOne.objects.add(new Objects("Tree", 270, 900));
						playerOne.objects.add(new Objects("Tree", 800, 1500));
						playerOne.objects.add(new Objects("Tree", 500, 2700));
						playerOne.objects.add(new Objects("Tree", 1500, 1000));
						playerOne.objects.add(new Objects("Tree", 2100, 900));

						playerOne.survivors.add(new Survivor(1355, 890, 4, true));
						playerOne.survivors.add(new Survivor(2000, 500, 4, true));
						playerOne.survivors.add(new Survivor(1200, 700, 1, true));
						playerOne.survivors.add(new Survivor(1500, 1600, 1, true));
						playerOne.survivors.add(new Survivor(950, 900, 3, true));
						playerOne.survivors.add(new Survivor(2200, 2000, 2, true));
						playerOne.survivors.add(new Survivor(780, 500, 3, true));
						playerOne.survivors.add(new Survivor(2000, 700, 1, true));
						playerOne.survivors.add(new Survivor(2700, 1100, 1, true));
						playerOne.survivors.add(new Survivor(1500, 1250, 2, true));
						playerOne.survivors.add(new Survivor(2300, 1200, 2, true));

						playerOne.zombieLimit = 4;
						playerOne.zombiesAtOnce = 30;
					} else if (levelSelect.level + 1 == 5) {
						mapName = "Map5.png";
						playerOne.background = new ImageIcon("../images/Map5.png").getImage();
						playerOne.objects.add(new Objects("TelephoneBooth", 500, 1000));
						playerOne.objects.add(new Objects("Tree", 10, 10));
						playerOne.objects.add(new Objects("Tree", 130, 100));
						playerOne.objects.add(new Objects("Tree", 420, 850));
						playerOne.objects.add(new Objects("Tree", 300, 680));
						playerOne.objects.add(new Objects("Tree", 1520, 1800));
						playerOne.objects.add(new Objects("Tree", 520, 2000));
						playerOne.objects.add(new Objects("Tree", 270, 900));
						playerOne.objects.add(new Objects("Tree", 800, 1500));
						playerOne.objects.add(new Objects("Tree", 500, 2700));
						playerOne.objects.add(new Objects("Tree", 1500, 1000));
						playerOne.objects.add(new Objects("Tree", 2100, 900));

						playerOne.survivors.add(new Survivor(1355, 890, 4, true));
						playerOne.survivors.add(new Survivor(2000, 500, 4, true));
						playerOne.survivors.add(new Survivor(770, 700, 1, true));
						playerOne.survivors.add(new Survivor(1500, 1600, 1, true));
						playerOne.survivors.add(new Survivor(950, 900, 3, true));
						playerOne.survivors.add(new Survivor(900, 1400, 2, true));
						playerOne.survivors.add(new Survivor(2200, 2000, 2, true));
						playerOne.survivors.add(new Survivor(700, 500, 3, true));
						playerOne.survivors.add(new Survivor(2000, 700, 1, true));
						playerOne.survivors.add(new Survivor(2700, 1100, 1, true));
						playerOne.survivors.add(new Survivor(870, 900, 3, true));
						playerOne.survivors.add(new Survivor(1500, 1250, 2, true));
						playerOne.survivors.add(new Survivor(2300, 1200, 2, true));

						playerOne.zombieLimit = 6;
						playerOne.zombiesAtOnce = 60;
					}

					playerOne.objects.add(new Objects("Border1", 0, 0));
					playerOne.objects.add(new Objects("Border2", 0, 0));
					playerOne.objects.add(new Objects("Border3", 0, 0));
					playerOne.objects.add(new Objects("Border4", 0, 0));
					playerOne.objects.add(new Objects("Helicopter", (int) (FRAME_WIDTH * 3.0 / 2.0),
							(int) (FRAME_HEIGHT * 3.0 / 2.0)));
					playerOne.overallSurvivors = playerOne.survivors.size();
					playerOne.numRescued = playerOne.survivors.size();
					playerOne.gameEnded = false;
					panelLayout.show(this, "In-Game");
					gameMode = 1;
				} else {
					levelSelect.selectedLevel = false;
				}

			}
			if (gameType == 2) {
				if (client.gameMode == 2 || client.gameMode == 3) {

					gameMode = optionPlayerTwo;
				}
				if (client.gameMode == 1 && client.gameEnded) {
					if (!playerTwo.gotGold) {
						equipmentShop.money += client.gold;
						playerTwo.gotGold = true;
					}
				}
			}
		} else {
			playerOne.p1GunType = equipmentShop.gunType;
			equipmentShop.singlePlayer = true;
			equipmentShop.connected = true;
			if (levelSelect.selectedLevel) {
				levelSelect.selectedLevel = false;
				gameMode = 1;

				playerOne.survivors.clear();
				levelSelect.selectedLevel = false;
				playerOne.fadeInGame = 1.0f;
				playerOne.resetGame();
				if (levelSelect.level + 1 == 1) {
					mapName = "Map2.png";
					playerOne.background = new ImageIcon("../images/Map2.png").getImage();
					playerOne.objects.add(new Objects("Tree", 10, 10));
					playerOne.objects.add(new Objects("Tree", 500, 30));
					playerOne.objects.add(new Objects("Tree", 130, 100));
					playerOne.objects.add(new Objects("Tree", 420, 850));
					playerOne.objects.add(new Objects("Tree", 300, 680));
					playerOne.objects.add(new Objects("Tree", 0, 700));
					playerOne.objects.add(new Objects("Tree", 510, 10));
					playerOne.objects.add(new Objects("Tree", 1520, 1800));
					playerOne.objects.add(new Objects("Tree", 520, 2000));
					playerOne.objects.add(new Objects("Tree", 270, 900));
					playerOne.objects.add(new Objects("Tree", 800, 1500));
					playerOne.objects.add(new Objects("Tree", 1500, 500));
					playerOne.objects.add(new Objects("Tree", 500, 2700));
					playerOne.objects.add(new Objects("Tree", 1200, 1800));
					playerOne.objects.add(new Objects("Tree", 1500, 1000));
					playerOne.objects.add(new Objects("Tree", 2300, 1800));
					playerOne.objects.add(new Objects("Tree", 2100, 900));
					playerOne.objects.add(new Objects("Tree", 1220, 1600));
					playerOne.objects.add(new Objects("Tree", 1900, 1500));
					playerOne.objects.add(new Objects("Tree", 1600, 2000));
					playerOne.objects.add(new Objects("SevenEleven", 0, 0));
					playerOne.objects.add(new Objects("Powerup", (int) (FRAME_WIDTH * 3.0 / 2.0) + 100,
							(int) (FRAME_HEIGHT * 3.0 / 2.0)));

					playerOne.survivors.add(new Survivor(1355, 890, 4, true));
					playerOne.survivors.add(new Survivor(700, 1300, 2, true));
					playerOne.survivors.add(new Survivor(3000, 670, 3, true));
					playerOne.survivors.add(new Survivor(900, 600, 2, true));

					playerOne.zombieLimit = 1;

					playerOne.zombiesAtOnce = 15;
				} else if (levelSelect.level + 1 == 2) {
					mapName = "Map1.png";
					playerOne.background = new ImageIcon("../images/Map1.png").getImage();
					playerOne.objects.add(new Objects("Tree", 10, 10));
					playerOne.objects.add(new Objects("Tree", 130, 100));
					playerOne.objects.add(new Objects("Tree", 420, 850));
					playerOne.objects.add(new Objects("Tree", 300, 680));
					playerOne.objects.add(new Objects("Tree", 1520, 1800));
					playerOne.objects.add(new Objects("Tree", 520, 2000));
					playerOne.objects.add(new Objects("Tree", 270, 900));
					playerOne.objects.add(new Objects("Tree", 800, 1500));
					playerOne.objects.add(new Objects("Tree", 500, 2700));
					playerOne.objects.add(new Objects("Tree", 1500, 1000));
					playerOne.objects.add(new Objects("Tree", 2100, 900));

					playerOne.survivors.add(new Survivor(700, 500, 4, true));
					playerOne.survivors.add(new Survivor(2000, 700, 1, true));
					playerOne.survivors.add(new Survivor(2700, 1400, 1, true));
					playerOne.survivors.add(new Survivor(650, 900, 3, true));
					playerOne.survivors.add(new Survivor(1500, 1250, 2, true));
					playerOne.survivors.add(new Survivor(2300, 1200, 2, true));

					playerOne.zombieLimit = 2;

					playerOne.zombiesAtOnce = 12;
				} else if (levelSelect.level + 1 == 3) {
					mapName = "Map3.png";
					playerOne.background = new ImageIcon("../images/Map3.png").getImage();
					playerOne.objects.add(new Objects("Tree", 10, 10));
					playerOne.objects.add(new Objects("Tree", 130, 100));
					playerOne.objects.add(new Objects("Tree", 420, 850));
					playerOne.objects.add(new Objects("Tree", 300, 680));
					playerOne.objects.add(new Objects("Tree", 1520, 1800));
					playerOne.objects.add(new Objects("Tree", 520, 2000));
					playerOne.objects.add(new Objects("Tree", 270, 900));
					playerOne.objects.add(new Objects("Tree", 800, 1500));
					playerOne.objects.add(new Objects("Tree", 500, 2700));
					playerOne.objects.add(new Objects("Tree", 1500, 1000));
					playerOne.objects.add(new Objects("Tree", 2100, 900));

					playerOne.survivors.add(new Survivor(1355, 890, 4, true));
					playerOne.survivors.add(new Survivor(700, 700, 1, true));
					playerOne.survivors.add(new Survivor(1500, 1600, 1, true));
					playerOne.survivors.add(new Survivor(2700, 1100, 1, true));
					playerOne.survivors.add(new Survivor(650, 900, 3, true));
					playerOne.survivors.add(new Survivor(1500, 1250, 2, true));
					playerOne.survivors.add(new Survivor(2300, 1200, 2, true));

					playerOne.zombieLimit = 3;

					playerOne.zombiesAtOnce = 15;
				} else if (levelSelect.level + 1 == 4) {
					mapName = "Map4.png";
					playerOne.background = new ImageIcon("../images/Map4.png").getImage();
					playerOne.objects.add(new Objects("TelephoneBooth", 250, 1200));
					playerOne.objects.add(new Objects("Tree", 10, 10));
					playerOne.objects.add(new Objects("Tree", 130, 100));
					playerOne.objects.add(new Objects("Tree", 420, 850));
					playerOne.objects.add(new Objects("Tree", 300, 680));
					playerOne.objects.add(new Objects("Tree", 1520, 1800));
					playerOne.objects.add(new Objects("Tree", 520, 2000));
					playerOne.objects.add(new Objects("Tree", 270, 900));
					playerOne.objects.add(new Objects("Tree", 800, 1500));
					playerOne.objects.add(new Objects("Tree", 500, 2700));
					playerOne.objects.add(new Objects("Tree", 1500, 1000));
					playerOne.objects.add(new Objects("Tree", 2100, 900));

					playerOne.survivors.add(new Survivor(1355, 890, 4, true));
					playerOne.survivors.add(new Survivor(2000, 500, 4, true));
					playerOne.survivors.add(new Survivor(1200, 700, 1, true));
					playerOne.survivors.add(new Survivor(1500, 1600, 1, true));
					playerOne.survivors.add(new Survivor(950, 900, 3, true));
					playerOne.survivors.add(new Survivor(2100, 800, 2, true));
					playerOne.survivors.add(new Survivor(780, 500, 3, true));
					playerOne.survivors.add(new Survivor(2000, 700, 1, true));
					playerOne.survivors.add(new Survivor(2700, 1100, 1, true));
					playerOne.survivors.add(new Survivor(1500, 1250, 2, true));
					playerOne.survivors.add(new Survivor(2300, 1200, 2, true));

					playerOne.zombieLimit = 4;
					playerOne.zombiesAtOnce = 25;
				} else if (levelSelect.level + 1 == 5) {
					mapName = "Map5.png";
					playerOne.background = new ImageIcon("../images/Map5.png").getImage();
					playerOne.objects.add(new Objects("TelephoneBooth", 500, 1000));
					playerOne.objects.add(new Objects("Tree", 10, 10));
					playerOne.objects.add(new Objects("Tree", 130, 100));
					playerOne.objects.add(new Objects("Tree", 420, 850));
					playerOne.objects.add(new Objects("Tree", 300, 680));
					playerOne.objects.add(new Objects("Tree", 1520, 1800));
					playerOne.objects.add(new Objects("Tree", 520, 2000));
					playerOne.objects.add(new Objects("Tree", 270, 900));
					playerOne.objects.add(new Objects("Tree", 800, 1500));
					playerOne.objects.add(new Objects("Tree", 500, 2700));
					playerOne.objects.add(new Objects("Tree", 1500, 1000));
					playerOne.objects.add(new Objects("Tree", 2100, 900));

					playerOne.survivors.add(new Survivor(1355, 890, 4, true));
					playerOne.survivors.add(new Survivor(2000, 500, 4, true));
					playerOne.survivors.add(new Survivor(770, 700, 1, true));
					playerOne.survivors.add(new Survivor(1500, 1600, 1, true));
					playerOne.survivors.add(new Survivor(950, 900, 3, true));
					playerOne.survivors.add(new Survivor(900, 1400, 2, true));
					playerOne.survivors.add(new Survivor(2700, 900, 2, true));
					playerOne.survivors.add(new Survivor(700, 500, 3, true));
					playerOne.survivors.add(new Survivor(2000, 700, 1, true));
					playerOne.survivors.add(new Survivor(1900, 1100, 1, true));
					playerOne.survivors.add(new Survivor(870, 900, 3, true));
					playerOne.survivors.add(new Survivor(1500, 1250, 2, true));
					playerOne.survivors.add(new Survivor(2300, 1200, 2, true));

					playerOne.zombieLimit = 6;
					playerOne.zombiesAtOnce = 60;
				}

				playerOne.objects.add(new Objects("Border1", 0, 0));
				playerOne.objects.add(new Objects("Border2", 0, 0));
				playerOne.objects.add(new Objects("Border3", 0, 0));
				playerOne.objects.add(new Objects("Border4", 0, 0));
				playerOne.objects.add(
						new Objects("Helicopter", (int) (FRAME_WIDTH * 3.0 / 2.0), (int) (FRAME_HEIGHT * 3.0 / 2.0)));
				playerOne.overallSurvivors = playerOne.survivors.size();
				playerOne.numRescued = playerOne.survivors.size();
				playerOne.gameEnded = false;
				panelLayout.show(this, "In-Game");
				gameMode = 1;

			}

			if (gameMode == 1) {
				panelLayout.show(this, "In-Game");
				if (playerOne.backToLevelSelect) {
					equipmentShop.money += playerOne.gold;
					playerOne.backToLevelSelect = false;
					gameMode = 2;
				}
			}
			if (gameMode == 2) {
				panelLayout.show(this, "Levels");
				if (levelSelect.equipPressed) {
					levelSelect.equipPressed = false;
					gameMode = 3;
				}
			}
			if (gameMode == 3) {
				panelLayout.show(this, "Equipment");
				if (equipmentShop.backPressed) {
					equipmentShop.backPressed = false;
					gameMode = 2;
				}
			}
		}
	}
}