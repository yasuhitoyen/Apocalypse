
/*
 * Michael Yen
 * Period 7
 * A P O C A L Y P S E
 */
import java.awt.*;
import javax.swing.*;

public class Objects {
	/*
	 * This class keeps track of the position
	 * of a single object. It also sets the hitbox
	 * and the position of the object.
	 */
	final int FRAME_WIDTH = 1370, FRAME_HEIGHT = 750, PLAYER_WIDTH = 68, PLAYER_HEIGHT = 100, FRAME_DIFFERENCE = 40,
			PLAYER_X = FRAME_WIDTH / 2 - FRAME_DIFFERENCE - PLAYER_WIDTH / 2,
			PLAYER_Y = FRAME_HEIGHT / 2 - FRAME_DIFFERENCE;
	final int PLAYER_HITBOX_X = PLAYER_X, PLAYER_HITBOX_Y = PLAYER_Y + 70, PLAYER_HITBOX_WIDTH = PLAYER_WIDTH,
			PLAYER_HITBOX_HEIGHT = 30;
	Image objectImage;
	String name;
	int xPos, yPos, width, height, hitboxX, hitboxY, hitboxWidth, hitboxHeight;
	Rectangle hitbox;
	String image;

	public Objects(String image, int xPos, int yPos) {
		hitbox = new Rectangle();
		objectImage = new ImageIcon(image + ".png").getImage();
		this.xPos = xPos;
		this.yPos = yPos;
		this.image = image;

		name = image;
		/*
		 * This is just getting the image name and seeing if the
		 * object can actually be put on the map like if it was a tree
		 * then yes, if it is something like a rock, this class
		 * doesnt have that so it doesn't do anything. I just have
		 * xPos of the tree and the xpos of the tree hitbox bc
		 * the tree itself isn't the hitbox.
		 */
		if (image.equals("Tree") || image.equals("Tree")) {
			/*
			 * This is one instance of an object that
			 * is going on the map and I will create more
			 * later on like buildings or telephone booths
			 * 
			 * Right now I have a telephone booth, 7-11, helicopter, a tree, and a powerup
			 */
			width = 140;
			height = 180;
			hitboxX = xPos + 40;
			hitboxY = yPos + 150;
			hitboxWidth = 60;
			hitboxHeight = 30;
		}
		if (image.equals("TelephoneBooth") || image.equals("TelephoneBooth")) {
			width = 90;
			height = 120;
			hitboxX = xPos;
			hitboxY = yPos + 100;
			hitboxWidth = 90;
			hitboxHeight = 40;
		}
		if (image.equals("Helicopter") || image.equals("Helicopter")) {
			width = 400;
			height = 210;
			hitboxX = xPos + 90;
			hitboxY = yPos + 120;
			hitboxWidth = 310;
			hitboxHeight = 70;
		}
		if (image.equals("SevenEleven") || image.equals("SevenEleven")) {
			width = 400;
			height = 350;
			hitboxX = xPos;
			hitboxY = yPos + 70;
			hitboxWidth = width;
			hitboxHeight = 280;
		}
		if (image.equals("Powerup") || image.equals("Powerup")) {
			width = 70;
			height = 60;
			hitboxX = xPos;
			hitboxY = yPos + 50;
			hitboxWidth = width;
			hitboxHeight = 10;
		}
		if (image.equals("Border1")) {
			width = FRAME_WIDTH * 3 - PLAYER_WIDTH * 2 - PLAYER_X * 2;
			height = 10;
			hitboxWidth = width;
			hitboxHeight = height;
			this.xPos = PLAYER_X + PLAYER_WIDTH;
			this.yPos = PLAYER_Y + PLAYER_HEIGHT;
			hitboxX = this.xPos;
			hitboxY = this.yPos;
		}
		if (image.equals("Border2")) {
			width = 10;
			height = FRAME_HEIGHT * 3 - PLAYER_Y * 2 - PLAYER_HEIGHT;
			hitboxWidth = width;
			hitboxHeight = height;
			this.xPos = PLAYER_X + PLAYER_WIDTH;
			this.yPos = PLAYER_Y + PLAYER_HEIGHT;
			hitboxX = this.xPos;
			hitboxY = this.yPos;
		}
		if (image.equals("Border3")) {
			width = FRAME_WIDTH * 3 - PLAYER_WIDTH * 2 - PLAYER_X * 2;
			height = 10;
			hitboxWidth = width;
			hitboxHeight = height;
			this.xPos = PLAYER_X + PLAYER_WIDTH;
			this.yPos = FRAME_HEIGHT * 3 - PLAYER_Y;
			hitboxX = this.xPos;
			hitboxY = this.yPos;
		}
		if (image.equals("Border4")) {
			width = 10;
			height = FRAME_HEIGHT * 3 - PLAYER_Y * 2 - PLAYER_HEIGHT;
			hitboxWidth = width;
			hitboxHeight = height;
			this.xPos = FRAME_WIDTH * 3 - PLAYER_X - PLAYER_WIDTH;
			this.yPos = PLAYER_Y + PLAYER_HEIGHT + 10;
			hitboxX = this.xPos;
			hitboxY = this.yPos;
		}

	}

	public void setPlayerPosition(int px, int py) {
		/*
		 * This method is a test
		 * IGNORE IGNORE IGNROE
		 */
		hitbox = new Rectangle(hitboxX + px * -1, hitboxY + py * -1, hitboxWidth, hitboxHeight);
	}
}
