
/*
 * Michael Yen
 * A P O C A L Y P S E
 */
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class GunBullet {
	/*
	 * This class keeps track of individual bullets.
	 * It updates every time the action performed
	 * is called in the player
	 */
	Rectangle bulletRect;
	int xPosFromPlayer, yPosFromPlayer, xPos, yPos, distance, finalDistance;
	double gunAngle;
	boolean playerRightLooked, deleteThisClone;
	final int FRAME_WIDTH = 1370, FRAME_HEIGHT = 750, PLAYER_WIDTH = 68, PLAYER_HEIGHT = 100, FRAME_DIFFERENCE = 40,
			PLAYER_X = FRAME_WIDTH / 2 - FRAME_DIFFERENCE - PLAYER_WIDTH / 2,
			PLAYER_Y = FRAME_HEIGHT / 2 - FRAME_DIFFERENCE;
	final int PLAYER_HITBOX_X = PLAYER_X, PLAYER_HITBOX_Y = PLAYER_Y + 70, PLAYER_HITBOX_WIDTH = PLAYER_WIDTH,
			PLAYER_HITBOX_HEIGHT = 30;
	Rectangle bulletHitbox;

	public GunBullet(double gunAngle, int xPos, int yPos, int playerX, int playerY, boolean playerRightLooked,
			int finalDistance) {
		/*
		 * Constructor for a SINGLE BULLET. You told me to change
		 * the code so instead of a line coming out of the gun,
		 * there is a bullet coming out so I have a class dedicated
		 * to a single bullet and I create multiple instances of it
		 * and store it as an ArrayList so I can easily delete it
		 * and add it and draw it easily. Anyways, the constructor gets
		 * the xPos, yPos, gunAngle, and if the player is looking
		 * right or not.
		 */
		this.gunAngle = gunAngle;
		this.xPosFromPlayer = playerX;
		this.yPosFromPlayer = playerY;

		this.xPos = xPos;
		this.yPos = yPos;

		this.finalDistance = finalDistance;
		this.playerRightLooked = playerRightLooked;

		deleteThisClone = false;
		bulletHitbox = new Rectangle();
		distance = 0;
	}

	public void update() {

		/*
		 * Every 5 ms, this actionPerformed is called and every time I change
		 * the position of the bullet depending on the gunAngle that is seen
		 * in the constructor. I do not constantly get the gunAngle because
		 * then whenever the player moves their gun the bullet will also
		 * move. The reason I have to do playerRightLooked is because if the
		 * player is looking right I can normally use sin and cos to find the distance
		 * using an angle because I can create a right triangle from the middle of the
		 * screen to my mouse. But, there is one problem to that method. The top right
		 * and lower left would be the same because the lower left, is atan(opp/adj)
		 * but the lower left would be atan(-y/-x) and would be the same as the top
		 * right
		 * atan(y/x) which is why I had to multiply -1 in order for it to go from the
		 * top right to the bottom left when the player is looking left. There is more
		 * detail on getting the angle of the gun in PlayerOne
		 */
		if (playerRightLooked) {
			/*
			 * Increment the xPos and yPos by 25 units from the center of the screen (or
			 * player if
			 * it is not the main player)
			 */
			xPos += (int) (25 * Math.cos(Math.toRadians(gunAngle)));
			yPos += (int) (25 * Math.sin(Math.toRadians(gunAngle)));

			xPosFromPlayer += (int) (25 * Math.cos(Math.toRadians(gunAngle)));
			yPosFromPlayer += (int) (25 * Math.sin(Math.toRadians(gunAngle)));

		} else if (gunAngle != 90) {/*
									 * Increment the xPos and yPos by 25 units from the center of the screen (or
									 * player if
									 * it is not the main player)
									 */
			xPos += (int) (25 * Math.cos(Math.toRadians(gunAngle))) * -1;
			yPos += (int) (25 * Math.sin(Math.toRadians(gunAngle))) * -1;

			xPosFromPlayer += (int) (25 * Math.cos(Math.toRadians(gunAngle))) * -1;
			yPosFromPlayer += (int) (25 * Math.sin(Math.toRadians(gunAngle))) * -1;
		}
		if (distance >= finalDistance) {
			deleteThisClone = true;
		}
		distance += 15;
		bulletRect = new Rectangle(xPos, yPos, 7, 7);
	}
}
