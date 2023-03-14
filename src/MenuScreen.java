
/*
 * Michael Yen
 * A P O C A L Y P S E
 */
import java.net.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;
import java.awt.*;

public class MenuScreen extends JPanel implements MouseListener, MouseMotionListener, ActionListener, KeyListener {
  Timer loginTimer;
  Image loginBackground, loginShooting, loginEyes, loginPlay1, loginPlay2, loginSettings1, loginSettings2, loginShadow,
      loginBackSetting;
  Image loginSettingBackground, loginSettingVolumeOn, loginSettingVolumeOff, loginSettingMovementWASD,
      loginSettingMovementArrow, loginSettingShootMouse;
  Image loginHostGame1, loginHostGame2, loginSettingShootSpace, loginTitle, loginJoinGame1, loginJoinGame2, ipEditBox,
      loginSinglePlayer1, loginSinglePlayer2;
  Rectangle loginPlaySensor, loginSettingSensor, loginMovementSensor, loginShootSensor, loginVolumeSensor,
      loginBackSensor, loginJoinSensor, loginHostSensor, loginSinglePlayerSensor;
  String enterIP, showIP;
  boolean mouseInPlay, mouseInSettings, mouseInMovement, mouseInShoot, mouseInVolume, mouseInBack, mouseInHost,
      connected, mouseInJoin, clientCreated, serverCreated, mouseInSinglePlayer;
  boolean updateBeforeAccept, touchingSinglePlayer;
  boolean doneInstructions;
  int mouseX, mouseY, eyeX, eyeY, backgroundX, backgroundY, shootingX, shootingY, mouseFromMiddleX, mouseFromMiddleY,
      volume, movementType, shootType;
  int menuMode, playerMovement, playerShooting, playerType;
  Font ipFont;
  Image exitClientSign, instructions;
  final int FRAME_WIDTH = 1200, FRAME_HEIGHT = 800;
  GameServer server;
  GameClient client;

  /*
   * Menu mode:
   * 1 = start, settings
   * 2 = shoot, movement, volume
   * 3 = host game, join game
   * 4 = enter ____.____._.__
   * 5 = enter ip address
   */
  public MenuScreen() {
    doneInstructions = false;
    /*
     * Constructor for the menu screen. Initializes variables
     * and gets the IPAddress of the user's computer so
     * the second player can connect to the Socket. Also
     * adds mouse listener, mouse motion listener, and
     * key listener
     */
    connected = false;
    updateBeforeAccept = false;
    clientCreated = false;
    serverCreated = false;
    playerType = 0;
    menuMode = 1;
    volume = 100;
    playerMovement = 0;
    playerShooting = 1;
    movementType = 0;
    shootType = 1;
    mouseX = 0;
    mouseY = 0;
    enterIP = "";
    showIP = "";
    ipFont = new Font("Sans serif", Font.BOLD, 60);

    instructions = new ImageIcon("../images/Instructionsz.png").getImage();
    loginBackground = new ImageIcon("../images/LoginBackground.png").getImage();
    loginShooting = new ImageIcon("../images/LoginShooting.png").getImage();
    loginEyes = new ImageIcon("../images/LoginEyes.png").getImage();
    loginPlay1 = new ImageIcon("../images/Play1.png").getImage();
    loginPlay2 = new ImageIcon("../images/Play2.png").getImage();
    loginSettings1 = new ImageIcon("../images/Settings1.png").getImage();
    loginSettings2 = new ImageIcon("../images/Settings2.png").getImage();
    loginShadow = new ImageIcon("../images/LoginShadow.png").getImage();
    loginBackSetting = new ImageIcon("../images/backSetting.png").getImage();
    loginSettingBackground = new ImageIcon("../images/SettingBackground.png").getImage();
    loginSettingVolumeOn = new ImageIcon("../images/Volume2.png").getImage();
    loginSettingVolumeOff = new ImageIcon("../images/Volume1.png").getImage();
    loginSettingMovementWASD = new ImageIcon("../images/Wasd_Arrow2.png").getImage();
    loginSettingMovementArrow = new ImageIcon("../images/Wasd_Arrow1.png").getImage();
    loginTitle = new ImageIcon("../images/APOCALYPSE.png").getImage();
    loginSettingShootMouse = new ImageIcon("../images/Mouse_Space1.png").getImage();
    loginSettingShootSpace = new ImageIcon("../images/Mouse_Space2.png").getImage();
    loginJoinGame1 = new ImageIcon("../images/JoinGame1.png").getImage();
    loginJoinGame2 = new ImageIcon("../images/JoinGame2.png").getImage();
    loginHostGame1 = new ImageIcon("../images/HostGame1.png").getImage();
    loginHostGame2 = new ImageIcon("../images/HostGame2.png").getImage();
    ipEditBox = new ImageIcon("../images/IPEditBox.png").getImage();
    exitClientSign = new ImageIcon("../images/EXITCLIENT.png").getImage();
    loginSinglePlayer1 = new ImageIcon("../images/SinglePlayer1.png").getImage();
    loginSinglePlayer2 = new ImageIcon("../images/SinglePlayer2.png").getImage();

    loginPlaySensor = new Rectangle();
    loginPlaySensor.setBounds(0, 475, 300, 70);
    loginSettingSensor = new Rectangle();
    loginSettingSensor.setBounds(0, 575, 300, 70);
    loginMovementSensor = new Rectangle();
    loginMovementSensor.setBounds(630, 120, 240, 100);
    loginBackSensor = new Rectangle();
    loginBackSensor.setBounds(565, 640, 170, 50);
    loginShootSensor = new Rectangle();
    loginShootSensor.setBounds(630, 280, 240, 100);
    loginVolumeSensor = new Rectangle();
    loginVolumeSensor.setBounds(700, 460, 95, 50);
    loginHostSensor = new Rectangle();
    loginHostSensor.setBounds(0, 475, 300, 70);
    loginJoinSensor = new Rectangle();
    loginJoinSensor.setBounds(0, 575, 300, 70);
    loginSinglePlayerSensor = new Rectangle();
    loginSinglePlayerSensor.setBounds(0, 375, 300, 70);

    InetAddress ipAddr;
    try {
      ipAddr = InetAddress.getLocalHost();
    } catch (UnknownHostException e1) {
      ipAddr = null;
    }
    showIP = ipAddr.getHostAddress().trim();

    addMouseMotionListener(this);
    addMouseListener(this);
    addKeyListener(this);
    loginTimer = new Timer(2, this);
    loginTimer.start();
    requestFocusInWindow();

  }

  public void actionPerformed(ActionEvent e) {
    /*
     * Sets the distance from the middle and
     * also checks if the connection is established
     * and then sets a variable to true for the
     * control panel to switch to the game panel.
     */
    mouseFromMiddleX = this.getWidth() / 2 - mouseX;
    mouseFromMiddleY = this.getHeight() / 2 - mouseY;

    if (menuMode == 5) {
      if (updateBeforeAccept) {
        if (!serverCreated) {
          serverCreated = true;
          server = new GameServer();
          showIP = server.GameServerAddress;
          playerType = 1;
          if (server.connectionStatus) {
            connected = true;
            server.movementType = movementType;
            server.shootType = shootType;
          }
        }
      }
    }

    senseButtons();
    repaint();
  }

  public void paintComponent(Graphics g) {
    /*
     * Paints different things depending on menu mode--
     * 
     * 1.
     * Paints the login title "Apocalypse", settings and play button.
     * It checks if the mouse is in the play or settings button and
     * changes the buttons so the players can see that the mouse
     * is hovering the button
     * 
     * 2.
     * The setting paints the WASD and ARROW key options, the
     * SPACE or MOUSE option, and the volume ON or OFF option.
     * 
     * 3.
     * Asks user to join game or host game
     * 
     * 4. User enters the IP Address for the client
     * 
     * 5. Screen prints the IP Address for the client
     * to enter
     * 
     */
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    g.drawImage(loginBackground, -20 + (mouseFromMiddleX / 60), -70 + (mouseFromMiddleY / 60),
        (int) (FRAME_WIDTH * 1.2), (int) (FRAME_HEIGHT * 1.2), null);

    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
    g2d.drawImage(loginShadow, 135 + (mouseFromMiddleX / 35), 550 + (mouseFromMiddleY / 35), 1150, 300, null);
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

    g2d.drawImage(loginShooting, 85 + (mouseFromMiddleX / 35), 190 + (mouseFromMiddleY / 35), (int) (FRAME_WIDTH * 1.1),
        740, null);
    g2d.drawImage(loginEyes, 650 - (mouseFromMiddleX / 80), 265 - (mouseFromMiddleY / 80), 90, 40, null);

    g2d.setColor(Color.black);
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
    g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));

    g2d.setColor(Color.MAGENTA);

    ///
    if (menuMode == 1) {
      g2d.drawImage(loginTitle, 400 + (mouseFromMiddleX / 14), 100 + (mouseFromMiddleY / 14), 600, 90, null);

      if (mouseInPlay) {
        g2d.drawImage(loginPlay2, 0, 450, 371, 130, null);
      } else {
        g2d.drawImage(loginPlay1, 109, 493, 100, 44, null);
      }
    } else if (menuMode == 2) {
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.96f));
      g2d.drawImage(loginSettingBackground, 550, 0, 800, 695, null);
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

      if (movementType == 1) {
        g2d.drawImage(loginSettingMovementArrow, 630, 120, 240, 100, null);
      } else {
        g2d.drawImage(loginSettingMovementWASD, 630, 120, 240, 100, null);
      }

      if (shootType == 1) {
        g2d.drawImage(loginSettingShootMouse, 630, 280, 240, 100, null);
      } else {
        g2d.drawImage(loginSettingShootSpace, 630, 280, 240, 100, null);
      }
      if (volume == 100) {
        g2d.drawImage(loginSettingVolumeOn, 705, 460, 90, 50, null);
      } else {
        g2d.drawImage(loginSettingVolumeOff, 705, 460, 90, 50, null);
      }
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
      g2d.drawImage(loginBackSetting, 570, 640, 160, 50, null);
    } else if (menuMode == 3) {
      if (mouseInHost) {
        g2d.drawImage(loginHostGame2, 0, 450, 371, 130, null);
      } else {
        g2d.drawImage(loginHostGame1, 74, 485, 220, 65, null);
      }
      if (mouseInJoin) {
        g2d.drawImage(loginJoinGame2, 0, 558, 371, 130, null);
      } else {
        g2d.drawImage(loginJoinGame1, 64, 591, 216, 62, null);
      }
      if (touchingSinglePlayer) {
        g2d.drawImage(loginSinglePlayer2, 0, 342, 371, 130, null);
      } else {
        g2d.drawImage(loginSinglePlayer1, 55, 373, 256, 72, null);
      }
    } else if (menuMode == 4) {
      g2d.drawImage(ipEditBox, 530, 330, 490, 90, null);
      g2d.setColor(Color.WHITE);
      g2d.setFont(ipFont);
      g2d.drawString(enterIP, 540, 400);
    } else if (menuMode == 5) {
      g2d.setColor(Color.WHITE);
      g2d.setFont(ipFont);
      g2d.drawString("Player 2 Code: " + showIP, 300, 400);
      updateBeforeAccept = true;
    }
    if (!doneInstructions) {
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
      g2d.setColor(Color.WHITE);
      g2d.drawImage(instructions, 0, -110, FRAME_WIDTH + 165, FRAME_HEIGHT + 230, null);
      g2d.setFont(new Font("Sans serif", Font.PLAIN, 35));
      g2d.drawString("Click to continue", 1100, 670);
    }

  }

  public void mouseDragged(MouseEvent e) {
    requestFocus();
    // Sets position of mouseX and mouseY
    mouseX = e.getX();
    mouseY = e.getY();
  }

  public void mouseMoved(MouseEvent e) {
    requestFocus();
    // Sets position of mouseX and mouseY
    mouseX = e.getX();
    mouseY = e.getY();
  }

  public void mouseClicked(MouseEvent e) {
    requestFocus();
    if (doneInstructions) {
      /*
       * Sets position of mouseX and mouseY
       * and checks if the buttons are pressed.
       * If they are, they change menu modes
       * to change the state of the menu
       */
      /*
       * If the mouse is inside the buttons
       * and is clicked, then it changes the
       * settings
       */
      mouseX = e.getX();
      mouseY = e.getY();
      if (menuMode == 1) {
        if (mouseInSettings) {
          // menuMode = 2;
        }
        if (mouseInPlay) {
          menuMode = 3;
        }
      } else if (menuMode == 2) {
        if (mouseInBack) {
          menuMode = 1;
        }
        if (mouseInMovement) {
          if (mouseX < 750) {
            movementType = 1;
          } else {
            movementType = 2;
          }
        }
        if (mouseInShoot) {
          if (mouseX < 750) {
            shootType = 1;
          } else {
            shootType = 2;
          }
        }
        if (mouseInVolume) {
          if (volume == 100) {
            volume = 0;
          } else {
            volume = 100;
          }
        }
      } else if (menuMode == 3) {
        if (mouseInJoin) {
          menuMode = 4;
        }
        if (mouseInHost) {
          menuMode = 5;
        }
      }
    } else {
      doneInstructions = true;
    }

  }

  public void mouseEntered(MouseEvent e) {
    // Sets mouse position
    mouseX = e.getX();
    mouseY = e.getY();
  }

  public void mouseExited(MouseEvent e) {
  }

  public void mousePressed(MouseEvent e) {
    // Sets mouse position
    requestFocus();
    mouseX = e.getX();
    mouseY = e.getY();
  }

  public void mouseReleased(MouseEvent e) {
    // Sets mouse position
    requestFocus();
    mouseX = e.getX();
    mouseY = e.getY();

    if (loginSinglePlayerSensor.contains(mouseX, mouseY)) {
      mouseInSinglePlayer = true;
    }
  }

  public void senseButtons() {
    // Checks if the mouse is inside the buttons and sets booleans to true
    requestFocus();
    if (loginPlaySensor.contains(mouseX, mouseY)) {
      mouseInPlay = true;
    } else {
      mouseInPlay = false;
    }

    if (loginSettingSensor.contains(mouseX, mouseY)) {
      mouseInSettings = true;
    } else {
      mouseInSettings = false;
    }

    if (loginMovementSensor.contains(mouseX, mouseY)) {
      mouseInMovement = true;
    } else {
      mouseInMovement = false;
    }

    if (loginBackSensor.contains(mouseX, mouseY)) {
      mouseInBack = true;
    } else {
      mouseInBack = false;
    }

    if (loginMovementSensor.contains(mouseX, mouseY)) {
      mouseInMovement = true;
    } else {
      mouseInMovement = false;
    }

    if (loginShootSensor.contains(mouseX, mouseY)) {
      mouseInShoot = true;
    } else {
      mouseInShoot = false;
    }

    if (loginVolumeSensor.contains(mouseX, mouseY)) {
      mouseInVolume = true;
    } else {
      mouseInVolume = false;
    }

    if (loginHostSensor.contains(mouseX, mouseY)) {
      mouseInHost = true;
    } else {
      mouseInHost = false;
    }

    if (loginJoinSensor.contains(mouseX, mouseY)) {
      mouseInJoin = true;
    } else {
      mouseInJoin = false;
    }

    if (loginSinglePlayerSensor.contains(mouseX, mouseY)) {
      touchingSinglePlayer = true;
    } else {
      touchingSinglePlayer = false;
    }

  }

  public void keyPressed(KeyEvent e) {
    /*
     * Checks if key pressed but does
     * not do anything for the menu screen
     */
  }

  public void keyReleased(KeyEvent e) {
    requestFocus();
    /*
     * When the menu mode is 4, the
     * player is able to type the characters
     * 0 ~ 9 and . to enter the IP address.
     * The longest IP Address is 16 letters
     * so I make sure it can't be above that.
     */
    char typedCharacter;
    typedCharacter = e.getKeyChar();
    if (menuMode == 4) {
      if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
        if (enterIP.length() > 0) {
          enterIP = enterIP.substring(0, enterIP.length() - 1);
        }
      } else {
        if (enterIP.length() < 16) {
          if (((int) typedCharacter >= 48 && (int) typedCharacter <= 57) || (int) typedCharacter == 46) {
            enterIP += typedCharacter;
          }
        }
      }

      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        if (!clientCreated) {
          clientCreated = true;
          client = new GameClient(enterIP);
          // checks if the ip is acceptable
          if (!client.connectionStatus) {
            client = null;
            clientCreated = false;
          } else {
            client.shootType = shootType;
            client.movementType = movementType;
            connected = true;
          }

          playerType = 2;
        }
      }
    }
  }

  public void keyTyped(KeyEvent e) {
  }
}
