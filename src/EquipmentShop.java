
/*
 * Michael Yen
 * A P O C A L Y P S E
 */
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

public class EquipmentShop extends JPanel implements MouseListener, ActionListener, MouseMotionListener {
    /*
     * This class is not implemented yet. This
     * is where the player chooses which
     * gun to equip and also where they buy their
     * weapons.
     */
    Rectangle connectionLostExitClient;
    Image exitClientSign;
    Timer equipmentTimer = new Timer(5, this);
    Image glockBuy1, glockBuy2, glockSelect1, glockSelect2;
    Image desertEagleBuy1, desertEagleBuy2, desertEagleSelect1, desertEagleSelect2;
    Image miniUziBuy1, miniUziBuy2, miniUziSelect1, miniUziSelect2;
    Image pm12Buy1, pm12Buy2, pm12Select1, pm12Select2, backToLevelSelect;
    Image aKBuy1, aKBuy2, aKSelect1, aKSelect2;
    Image awmBuy1, awmBuy2, awmSelect1, awmSelect2;
    Image laserGunBuy1, laserGunBuy2, laserGunSelect1, laserGunSelect2;
    Image shopBackground, backShop;
    boolean singlePlayer;
    boolean backPressed;
    int gunType, mouseX, mouseY, mouseFromMiddleX, mouseFromMiddleY;
    int money;
    final int FRAME_WIDTH = 1370, FRAME_HEIGHT = 750;
    Rectangle gunRow[], backButton;
    boolean gunBought[];
    boolean gunEquipped[];
    boolean connected;

    int gunPrice[];

    public EquipmentShop() {
        /*
         * Constructor that adds mouse listener and
         * mouse motion listener. It also creates a new
         * rectangle for every rectangle in the array
         * "gunRow"
         */
        backToLevelSelect = new ImageIcon("../images/BackToLevel.png").getImage();

        glockBuy1 = new ImageIcon("../images/GlockBuy1.png").getImage();
        glockBuy2 = new ImageIcon("../images/GlockBuy2.png").getImage();
        glockSelect1 = new ImageIcon("../images/GlockSelect1.png").getImage();
        glockSelect2 = new ImageIcon("../images/GlockSelect2.png").getImage();

        desertEagleBuy1 = new ImageIcon("../images/DesertEagleBuy1.png").getImage();
        desertEagleBuy2 = new ImageIcon("../images/DesertEagleBuy2.png").getImage();
        desertEagleSelect1 = new ImageIcon("../images/DesertEagleSelect1.png").getImage();
        desertEagleSelect2 = new ImageIcon("../images/DesertEagleSelect2.png").getImage();

        miniUziBuy1 = new ImageIcon("../images/MiniUziBuy1.png").getImage();
        miniUziBuy2 = new ImageIcon("../images/MiniUziBuy2.png").getImage();
        miniUziSelect1 = new ImageIcon("../images/MiniUziSelect1.png").getImage();
        miniUziSelect2 = new ImageIcon("../images/MiniUziSelect2.png").getImage();

        aKBuy1 = new ImageIcon("../images/AKBuy1.png").getImage();
        aKBuy2 = new ImageIcon("../images/AKBuy2.png").getImage();
        aKSelect1 = new ImageIcon("../images/AKSelect1.png").getImage();
        aKSelect2 = new ImageIcon("../images/AKSelect2.png").getImage();

        pm12Buy1 = new ImageIcon("../images/PM12Buy1.png").getImage();
        pm12Buy2 = new ImageIcon("../images/PM12Buy2.png").getImage();
        pm12Select1 = new ImageIcon("../images/PM12Select1.png").getImage();
        pm12Select2 = new ImageIcon("../images/PM12Select2.png").getImage();

        awmBuy1 = new ImageIcon("../images/AWMBuy1.png").getImage();
        awmBuy2 = new ImageIcon("../images/AWMBuy2.png").getImage();
        awmSelect1 = new ImageIcon("../images/AWMSelect1.png").getImage();
        awmSelect2 = new ImageIcon("../images/AWMSelect2.png").getImage();

        laserGunBuy1 = new ImageIcon("../images/LaserGunBuy1.png").getImage();
        laserGunBuy2 = new ImageIcon("../images/LaserGunBuy2.png").getImage();
        laserGunSelect1 = new ImageIcon("../images/LaserGunSelect1.png").getImage();
        laserGunSelect2 = new ImageIcon("../images/LaserGunSelect2.png").getImage();

        shopBackground = new ImageIcon("../images/ShopBackground.png").getImage();
        backShop = new ImageIcon("../images/backSetting.png").getImage();
        exitClientSign = new ImageIcon("../images/EXITCLIENT.png").getImage();
        ///////////////////////////////////////////////////////////////
        glockBuy1 = new ImageIcon("../images/GlockBuy1.png").getImage();
        glockBuy2 = new ImageIcon("../images/GlockBuy2.png").getImage();
        glockSelect1 = new ImageIcon("../images/GlockSelect1.png").getImage();
        glockSelect2 = new ImageIcon("../images/GlockSelect2.png").getImage();

        desertEagleBuy1 = new ImageIcon("../images/DesertEagleBuy1.png").getImage();
        desertEagleBuy2 = new ImageIcon("../images/DesertEagleBuy2.png").getImage();
        desertEagleSelect1 = new ImageIcon("../images/DesertEagleSelect1.png").getImage();
        desertEagleSelect2 = new ImageIcon("../images/DesertEagleSelect2.png").getImage();

        miniUziBuy1 = new ImageIcon("../images/MiniUziBuy1.png").getImage();
        miniUziBuy2 = new ImageIcon("../images/MiniUziBuy2.png").getImage();
        miniUziSelect1 = new ImageIcon("../images/MiniUziSelect1.png").getImage();
        miniUziSelect2 = new ImageIcon("../images/MiniUziSelect2.png").getImage();

        aKBuy1 = new ImageIcon("../images/AKBuy1.png").getImage();
        aKBuy2 = new ImageIcon("../images/AKBuy2.png").getImage();
        aKSelect1 = new ImageIcon("../images/AKSelect1.png").getImage();
        aKSelect2 = new ImageIcon("../images/AKSelect2.png").getImage();

        pm12Buy1 = new ImageIcon("../images/PM12Buy1.png").getImage();
        pm12Buy2 = new ImageIcon("../images/PM12Buy2.png").getImage();
        pm12Select1 = new ImageIcon("../images/PM12Select1.png").getImage();
        pm12Select2 = new ImageIcon("../images/PM12Select2.png").getImage();

        awmBuy1 = new ImageIcon("../images/AWMBuy1.png").getImage();
        awmBuy2 = new ImageIcon("../images/AWMBuy2.png").getImage();
        awmSelect1 = new ImageIcon("../images/AWMSelect1.png").getImage();
        awmSelect2 = new ImageIcon("../images/AWMSelect2.png").getImage();

        laserGunBuy1 = new ImageIcon("../images/LaserGunBuy1.png").getImage();
        laserGunBuy2 = new ImageIcon("../images/LaserGunBuy2.png").getImage();
        laserGunSelect1 = new ImageIcon("../images/LaserGunSelect1.png").getImage();
        laserGunSelect2 = new ImageIcon("../images/LaserGunSelect2.png").getImage();

        shopBackground = new ImageIcon("../images/ShopBackground.png").getImage();
        backShop = new ImageIcon("../images/backSetting.png").getImage();
        exitClientSign = new ImageIcon("../images/EXITCLIENT.png").getImage();
        /*
         * glockBuy1 = new ImageIcon("../images/GlockBuy1.png").getImage();
         * glockBuy2 = new ImageIcon("../images/GlockBuy2.png").getImage();
         * glockSelect1 = new ImageIcon("../images/GlockSelect1.png").getImage();
         * glockSelect2 = new ImageIcon("../images/GlockSelect2.png").getImage();
         * 
         * desertEagleBuy1 = new ImageIcon("../images/DesertEagleBuy1.png").getImage();
         * desertEagleBuy2 = new ImageIcon("../images/DesertEagleBuy2.png").getImage();
         * desertEagleSelect1 = new
         * ImageIcon("../images/DesertEagleSelect1.png").getImage();
         * desertEagleSelect2 = new
         * ImageIcon("../images/DesertEagleSelect2.png").getImage();
         * 
         * miniUziBuy1 = new ImageIcon("../images/MiniUziBuy1.png").getImage();
         * miniUziBuy2 = new ImageIcon("../images/MiniUziBuy2.png").getImage();
         * miniUziSelect1 = new ImageIcon("../images/MiniUziSelect1.png").getImage();
         * miniUziSelect2 = new ImageIcon("../images/MiniUziSelect2.png").getImage();
         * 
         * aKBuy1 = new ImageIcon("../images/AKBuy1.png").getImage();
         * aKBuy2 = new ImageIcon("../images/AKBuy2.png").getImage();
         * aKSelect1 = new ImageIcon("../images/AKSelect1.png").getImage();
         * aKSelect2 = new ImageIcon("../images/AKSelect2.png").getImage();
         * 
         * pm12Buy1 = new ImageIcon("../images/PM12Buy1.png").getImage();
         * pm12Buy2 = new ImageIcon("../images/PM12Buy2.png").getImage();
         * pm12Select1 = new ImageIcon("../images/PM12Select1.png").getImage();
         * pm12Select2 = new ImageIcon("../images/PM12Select2.png").getImage();
         * 
         * awmBuy1 = new ImageIcon("../images/AWMBuy1.png").getImage();
         * awmBuy2 = new ImageIcon("../images/AWMBuy2.png").getImage();
         * awmSelect1 = new ImageIcon("../images/AWMSelect1.png").getImage();
         * awmSelect2 = new ImageIcon("../images/AWMSelect2.png").getImage();
         * 
         * laserGunBuy1 = new ImageIcon("../images/LaserGunBuy1.png").getImage();
         * laserGunBuy2 = new ImageIcon("../images/LaserGunBuy2.png").getImage();
         * laserGunSelect1 = new ImageIcon("../images/LaserGunSelect1.png").getImage();
         * laserGunSelect2 = new ImageIcon("../images/LaserGunSelect2.png").getImage();
         * 
         * shopBackground = new ImageIcon("../images/ShopBackground.png").getImage();
         * backShop = new ImageIcon("../images/backSetting.PNG").getImage();
         */

        addMouseListener(this);
        addMouseMotionListener(this);
        money = 0;
        gunType = 0;
        connectionLostExitClient = new Rectangle(585, 340, 160, 75);

        gunRow = new Rectangle[7];
        gunBought = new boolean[7];
        gunPrice = new int[7];
        gunEquipped = new boolean[7];
        for (Boolean bought : gunBought) {
            bought = false;
        }
        for (Boolean equip : gunEquipped) {
            equip = false;
        }
        for (int i = 0; i < 7; i++) {
            gunRow[i] = new Rectangle();
        }

        gunPrice[0] = 450;
        gunPrice[1] = 950;
        gunPrice[2] = 1600;
        gunPrice[3] = 2200;
        gunPrice[4] = 2500;
        gunPrice[5] = 5000;
        gunPrice[6] = 10000;

        gunBought[0] = true;
        gunEquipped[0] = true;

        equipmentTimer.start();

        backButton = new Rectangle(1150, 600, 200, 100);
    }

    public void paintComponent(Graphics g) {
        /*
         * Paints the gun selection
         * (I fill rectangles for now
         * but later on I will add an
         * image to replace the)
         */
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(shopBackground, (int) (mouseFromMiddleX * -0.15) - 105, 0, FRAME_WIDTH + 200, FRAME_HEIGHT, null);
        for (int i = 0; i < 7; i++) {
            // g2d.fill(gunRow[i]);
            if (i == 0) {
                if (!gunBought[i]) {
                    if (gunRow[i].contains(mouseX, mouseY) && mouseY > 480) {
                        g2d.drawImage(glockBuy1, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    } else {
                        g2d.drawImage(glockBuy2, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    }
                } else {
                    if (!gunEquipped[i]) {
                        g2d.drawImage(glockSelect1, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    } else {
                        g2d.drawImage(glockSelect2, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    }
                }
            } else if (i == 1) {

                if (!gunBought[i]) {
                    if (gunRow[i].contains(mouseX, mouseY) && mouseY > 480) {
                        g2d.drawImage(desertEagleBuy1, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    } else {
                        g2d.drawImage(desertEagleBuy2, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    }
                } else {
                    if (!gunEquipped[i]) {
                        g2d.drawImage(desertEagleSelect1, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    } else {
                        g2d.drawImage(desertEagleSelect2, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    }
                }

            } else if (i == 2) {
                if (!gunBought[i]) {
                    if (gunRow[i].contains(mouseX, mouseY) && mouseY > 480) {
                        g2d.drawImage(miniUziBuy1, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    } else {
                        g2d.drawImage(miniUziBuy2, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    }
                } else {
                    if (!gunEquipped[i]) {
                        g2d.drawImage(miniUziSelect1, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    } else {
                        g2d.drawImage(miniUziSelect2, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    }
                }
            } else if (i == 3) {

                if (!gunBought[i]) {
                    if (gunRow[i].contains(mouseX, mouseY) && mouseY > 480) {
                        g2d.drawImage(aKBuy1, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    } else {
                        g2d.drawImage(aKBuy2, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    }
                } else {
                    if (!gunEquipped[i]) {
                        g2d.drawImage(aKSelect1, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    } else {
                        g2d.drawImage(aKSelect2, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    }
                }

            } else if (i == 4) {
                if (!gunBought[i]) {
                    if (gunRow[i].contains(mouseX, mouseY) && mouseY > 480) {
                        g2d.drawImage(pm12Buy1, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    } else {
                        g2d.drawImage(pm12Buy2, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    }
                } else {
                    if (!gunEquipped[i]) {
                        g2d.drawImage(pm12Select1, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    } else {
                        g2d.drawImage(pm12Select2, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    }
                }

            } else if (i == 5) {
                if (!gunBought[i]) {
                    if (gunRow[i].contains(mouseX, mouseY) && mouseY > 480) {
                        g2d.drawImage(awmBuy1, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    } else {
                        g2d.drawImage(awmBuy2, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    }
                } else {
                    if (!gunEquipped[i]) {
                        g2d.drawImage(awmSelect1, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    } else {
                        g2d.drawImage(awmSelect2, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    }
                }
            } else if (i == 6) {
                if (!gunBought[i]) {
                    if (gunRow[i].contains(mouseX, mouseY) && mouseY > 480) {
                        g2d.drawImage(laserGunBuy1, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    } else {
                        g2d.drawImage(laserGunBuy2, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    }
                } else {
                    if (!gunEquipped[i]) {
                        g2d.drawImage(laserGunSelect1, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    } else {
                        g2d.drawImage(laserGunSelect2, (int) gunRow[i].getX(), (int) gunRow[i].getY(),
                                (int) gunRow[i].getWidth(), (int) gunRow[i].getHeight(), null);
                    }
                }
            } else {
                g2d.fill(gunRow[i]);
            }

        }
        g2d.drawImage(backToLevelSelect, backButton.x, backButton.y, backButton.width, backButton.height - 10, null);
        // g2d.fill(backButton);
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("DialogInput", Font.BOLD, 35));
        g2d.drawString("Money $" + money, 20, 60);

        if (!connected) {
            g2d.drawImage(exitClientSign, 420, 210, 500, 240, null);
        }
    }

    public void mouseClicked(MouseEvent e) {
        /*
         * Sets mouseX to mouse xpos and mouseY to
         * mouse ypos and checks if the mouse is in
         * any of the rectangles which is the hitbox
         * to select the weapons
         */
        mouseX = e.getX();
        mouseY = e.getY();

        if (connected) {
            for (int i = 0; i < 7; i++) {
                if (gunRow[i].contains(mouseX, mouseY)) {
                    if (!gunBought[i]) {
                        if (money >= gunPrice[i] && mouseY > 480) {
                            money -= gunPrice[i];
                            gunBought[i] = true;
                        }
                    } else {
                        if (mouseY > 480) {
                            for (int z = 0; z < gunEquipped.length; z++) {
                                gunEquipped[z] = false;
                            }
                            gunEquipped[i] = true;
                            gunType = i;
                        }
                    }

                }
            }
            if (backButton.contains(mouseX, mouseY)) {
                backPressed = true;
            }
        } else {

            if (connectionLostExitClient.contains(mouseX, mouseY)) {
                System.exit(1);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        /*
         * Calls these 3 methods. Check other methods to see what they do
         * basically find distance from middle finds the distance from
         * the middle of the screen and position equipment sets the
         * position of the rectangles and repaint just repaints the
         * images in the paintComponent
         */
        findDistanceFromMiddle();
        positionEquipment();
        repaint();
    }

    public void mouseEntered(MouseEvent e) {
        // Checks if the mouse enters the screen
        // not used for the game
    }

    public void mouseExited(MouseEvent e) {
        // Checks if the mouse exits the screen
        // not used for the game
    }

    public void mousePressed(MouseEvent e) {
        // Checks if the mouse is pressed
        // not used for the game
    }

    public void mouseReleased(MouseEvent e) {
    }

    /*
     * M O U S E L I S T E N E R J U S T S E T S P O S I T I O N O F M O U S E X A N
     * D M O U S E Y
     * S O O T H E R M E T H O D S C A N U S E I T.
     */
    public void mouseDragged(MouseEvent e) {
        if (connected) {
            mouseX = e.getX();
            mouseY = e.getY();
        }
    }

    public void mouseMoved(MouseEvent e) {
        if (connected) {
            mouseX = e.getX();
            mouseY = e.getY();
        }
    }

    ////////////////////////
    public void findDistanceFromMiddle() {
        mouseFromMiddleX = mouseX - FRAME_WIDTH / 2;
        mouseFromMiddleY = mouseY - FRAME_HEIGHT / 2;
        // Finds distance from the middle of the screen to the mouse
    }

    public void positionEquipment() {
        for (int i = 0; i < 7; i++) {
            gunRow[i] = new Rectangle((int) (mouseFromMiddleX * 0.3) * -1 + i * 200 - 115, 200, 180, 320);
            // Basically a bunch of rectangles thta I wil lbe using for separate guns that
            // can
            // be bought and selected and used in game. I do not have that right now but I
            // will
            // have it later
        }
    }
}