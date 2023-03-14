
/*
 * Michael Yen
 * A P O C A L Y P S E
 */
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.Timer;

public class LevelSelect extends JPanel implements MouseListener, ActionListener, MouseMotionListener, KeyListener {
	/*
	 * This is the level select panel. You choose which level to
	 * select. THIS CLASS IS NOT USED YET AND WILL BE IMPLEMENTED
	 * LATER.
	 */
	Timer levelSelectTimer = new Timer(5, this);
	int gameType, numMessages;
	int gunType, mouseX, mouseY, mouseFromMiddleX, mouseFromMiddleY, level;
	Date dateToday;
	Image p1Mouse;
	final int FRAME_WIDTH = 1370, FRAME_HEIGHT = 750;
	int readingInfo, messageNum, numMessagesRecieved, recievedMessageNum;
	boolean equipPressed, selectedLevel, showInfo;
	boolean singlePlayer;
	Rectangle goEquip;
	Rectangle row1[], row2[], row3[], acceptMission, declineMission;
	ArrayList<String> chat;
	Rectangle connectionLostExitClient;
	Image exitClientSign;
	Image missionBox, info, goEquipButton, levelSelectBackground;
	String text, gotText, oldMessage;
	SimpleDateFormat formatter;
	int hour, minute, second;
	Image openMissionBox;
	String time;
	String p2GameChat[];
	GameClient client;
	GameServer server;
	int p1MouseX, p2MouseX;
	int p1MouseY, p2MouseY;
	boolean connected;
	private Image p2Mouse;

	public LevelSelect() {
		/*
		 * Adds mouse listener and mouse motion listener.
		 * It also initializes all variables including the
		 * rows of rectangles which will be the hitbox of
		 * the level select. It also starts the timer.
		 * 
		 */
		p2GameChat = new String[0];
		formatter = new SimpleDateFormat("HH:mm:ss");
		chat = new ArrayList<String>();
		missionBox = new ImageIcon("../images/MissionBox1.png").getImage();
		openMissionBox = new ImageIcon("../images/MissionBox2.png").getImage();
		info = new ImageIcon("../images/InfoPage1").getImage();
		goEquipButton = new ImageIcon("../images/GoEquipButton.png").getImage();
		levelSelectBackground = new ImageIcon("../images/LevelSelectBackgroundFade.png").getImage();
		p1Mouse = new ImageIcon("../images/PlayerOneMouse.png").getImage();
		p2Mouse = new ImageIcon("../images/PlayerTwoMouse.png").getImage();
		exitClientSign = new ImageIcon("../images/EXITCLIENT.png").getImage();

		missionBox = new ImageIcon("../images/MissionBox1.png").getImage();
		openMissionBox = new ImageIcon("../images/MissionBox2.png").getImage();
		info = new ImageIcon("../images/InfoPage1").getImage();
		goEquipButton = new ImageIcon("../images/GoEquipButton.png").getImage();
		levelSelectBackground = new ImageIcon("../images/LevelSelectBackgroundFade.png").getImage();
		p1Mouse = new ImageIcon("../images/PlayerOneMouse.png").getImage();
		p2Mouse = new ImageIcon("../images/PlayerTwoMouse.png").getImage();
		exitClientSign = new ImageIcon("../images/EXITCLIENT.png").getImage();
		readingInfo = -1;
		selectedLevel = false;
		showInfo = false;
		text = "";
		level = 0;
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		equipPressed = false;
		goEquip = new Rectangle(1100, 580, 200, 100);
		declineMission = new Rectangle(731, 215, 30, 30);
		acceptMission = new Rectangle(537, 635, 170, 35);
		row1 = new Rectangle[6];
		row2 = new Rectangle[6];
		row3 = new Rectangle[6];
		connectionLostExitClient = new Rectangle(585, 340, 160, 75);

		for (int i = 0; i < 5; i++) {
			row1[i] = new Rectangle(50 + i * 250, 100, 200, 140);
			row2[i] = new Rectangle(50 + i * 250, 250, 240, 140);
			row3[i] = new Rectangle(50 + i * 250, 400, 240, 140);
		}
		numMessagesRecieved = 0;

		levelSelectTimer.start();

	}

	public void paintComponent(Graphics g) {
		/*
		 * Paints the rows of level selects and
		 * the "Equip" button in red
		 */
		setBackground(Color.GRAY);
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Sans serif", Font.BOLD, 15));

		for (int i = chat.size() - 1; i >= 0; i--) {
			if (!(chat.get(i) == null)) {
				g2d.drawString(chat.get(i), 75, 650 + (chat.size() - i) * -16);
			}
		}

		for (int i = p2GameChat.length - 1; i >= 0; i--) {
			if (!(p2GameChat[i] == null)) {
				g2d.drawString(p2GameChat[i], 75, 650 + (p2GameChat.length - i) * -16);
			}
		}

		g2d.drawImage(levelSelectBackground, 0, -20, FRAME_WIDTH, FRAME_HEIGHT, null);
		for (int i = 0; i < 5; i++) {
			g2d.setFont(new Font("Sans serif", Font.BOLD, 33));
			if (!(level == i) || !showInfo) {
				g2d.drawImage(missionBox, row1[i].x, row1[i].y, row1[i].width, row1[i].height, null);
				g2d.setColor(Color.white);
				g2d.drawString("Mission " + (i + 1), row1[i].x + 20, row1[i].y + 64);
			} else {
				g2d.drawImage(openMissionBox, row1[i].x - 68, row1[i].y, (int) (row1[i].width * 1.35),
						(int) (row1[i].height * 1.7), null);
			}
		}
		for (int i = 0; i < 5; i++) {
			if (showInfo && i == level) {
				info = new ImageIcon("../images/InfoPage" + (i + 1) + ".png").getImage();
				g2d.drawImage(info, 465, 215, 300, 475, null);

				if (i == 4) {
					info = new ImageIcon("../images/InfoPage6.png").getImage();
					g2d.drawImage(info, 775, 215, 300, 475, null);
				}
			}
		}

		g2d.setColor(Color.red);
		g2d.drawImage(goEquipButton, goEquip.x, goEquip.y, goEquip.width, goEquip.height, null);
		g2d.setFont(new Font("Arial", Font.BOLD, 10));

		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Sans serif", Font.BOLD, 15));

		g2d.drawString(text, 75, 670);

		if (!singlePlayer) {
			if (gameType == 2) {
				g2d.drawImage(p1Mouse, p1MouseX, p1MouseY, 50, 50, null);
			}
			if (gameType == 1) {
				g2d.drawImage(p2Mouse, p2MouseX, p2MouseY, 50, 50, null);
			}
		}
		if (!singlePlayer) {
			if (!connected) {
				g2d.drawImage(exitClientSign, 420, 210, 500, 240, null);
			}
		}

	}

	public void mouseClicked(MouseEvent e) {
		requestFocus();
		/*
		 * Sets mouseX and mouseY to the mouse x position and
		 * mouse y position and checks if the mouse is inside
		 * the equip button
		 */
		mouseX = e.getX();
		mouseY = e.getY();
		if (connected || singlePlayer) {
			if (goEquip.contains(mouseX, mouseY)) {
				equipPressed = true;
			}
			if (gameType == 1 || gameType == 0) {
				if (!showInfo) {
					for (int i = 0; i < row1.length; i++) {
						if (!(row1[i] == null)) {
							if (row1[i].contains(mouseX, mouseY)) {
								level = i;
								showInfo = true;
							}
						}

					}
				}
				if (showInfo) {
					if (acceptMission.contains(mouseX, mouseY)) {
						selectedLevel = true;
						showInfo = false;
					} else if (declineMission.contains(mouseX, mouseY)) {
						showInfo = false;
					}
				}
			}
		} else {
			if (connectionLostExitClient.contains(mouseX, mouseY)) {
				System.exit(1);
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void actionPerformed(ActionEvent e) {
		/*
		 * Calls findDistanceFromMiddle and repaint method
		 */
		dateToday = new Date();
		time = formatter.format(dateToday);

		if (gameType == 1) {
			if (numMessagesRecieved == recievedMessageNum) {
				chat.add(gotText);
				numMessagesRecieved++;
			}
		}
		findDistanceFromMiddle();
		repaint();
	}

	public void mouseDragged(MouseEvent e) {
		requestFocus();
		// Sets mouse position
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		requestFocus();
		// Sets mouse position
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void findDistanceFromMiddle() {
		requestFocus();
		// Checks the distance from middle to mouse
		mouseFromMiddleX = mouseX - FRAME_WIDTH / 2;
		mouseFromMiddleY = mouseY - FRAME_HEIGHT / 2;
	}

	public void keyPressed(KeyEvent e) {
		char typedCharacter = e.getKeyChar();

		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			if (text.length() > 0) {
				text = text.substring(0, text.length() - 1);
			}
		} else {
			if (text.length() < 24) {
				if (((int) typedCharacter >= 39 && (int) typedCharacter <= 176)
						|| e.getKeyCode() == KeyEvent.VK_SPACE) {
					text += typedCharacter;
				}
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (!text.trim().equals("")) {
				if (gameType == 1 || gameType == 0) {
					chat.add("[" + time + "] Player 1: " + text);
					text = "";

					if (gameType == 0)
						chat.add("[" + time + "] Bot: Play with a friend to chat");
				} else {
					numMessages++;
					messageNum = numMessages;
					oldMessage = "[" + time + "] Player 2: " + text;
					text = "";
				}
			}
		}

	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {

	}

}