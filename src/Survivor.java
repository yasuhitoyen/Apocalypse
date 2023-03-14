
/*
 * Michael Yen
 * A P O C A L Y P S E
 */
import java.awt.*;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Survivor {
	/*
	 * Class for an individual survivor. The
	 * survivors are added to an array list
	 * called survivors where the survivors
	 * can be accessed. Instances of this class
	 * are created and added to the arraylist.
	 */
	private final int FRAME_WIDTH = 1370, FRAME_HEIGHT = 750, PLAYER_WIDTH = 68, PLAYER_HEIGHT = 100,
			FRAME_DIFFERENCE = 40, PLAYER_X = FRAME_WIDTH / 2 - FRAME_DIFFERENCE - PLAYER_WIDTH / 2,
			PLAYER_Y = FRAME_HEIGHT / 2 - FRAME_DIFFERENCE;
	private final int PLAYER_HITBOX_X = PLAYER_X, PLAYER_HITBOX_Y = PLAYER_Y + 70, PLAYER_HITBOX_WIDTH = PLAYER_WIDTH,
			PLAYER_HITBOX_HEIGHT = 30;
	int p1X, p1Y, p2X, p2Y;
	boolean saved, lookRight, countedSurvivor;
	int xPos, yPos, type, followPlayer, width, height, hitboxX, hitboxY, hitboxWidth, hitboxHeight, following;
	int survivorNum = 0;
	double distanceFromP1, distanceFromP2;
	Image rightSurvivorImage, leftSurvivorImage;
	ArrayList playerOneStepsX, playerOneStepsY, playerTwoStepsX, playerTwoStepsY;

	public Survivor(int xPos, int yPos, int type, ArrayList<Integer> playerOneStepsX,
			ArrayList<Integer> playerOneStepsY, ArrayList<Integer> playerTwoStepsX,
			ArrayList<Integer> playerTwoStepsY) {
		/*
		 * Constructor for a single survivor.
		 * Sets the position of the survivor
		 * and also sets the hitbox and image
		 * based on what type of survivor it is.
		 */
		this.playerOneStepsX = playerOneStepsX;
		this.playerOneStepsY = playerOneStepsY;
		this.playerTwoStepsX = playerTwoStepsX;
		this.playerTwoStepsY = playerTwoStepsY;
		this.xPos = xPos;
		this.yPos = yPos;
		this.type = type;
		following = 0;
		hitboxX = xPos;
		hitboxY = yPos + 70;
		width = 68;
		saved = false;
		height = 100;
		hitboxWidth = width;
		hitboxHeight = 30;
		countedSurvivor = false;
		if (type == 1) {
			rightSurvivorImage = new ImageIcon("../images/NormalSurvivor1.png").getImage();
			leftSurvivorImage = new ImageIcon("../images/NormalSurvivor2.png").getImage();
		} else if (type == 2) {
			rightSurvivorImage = new ImageIcon("../images/OldSurvivor1.png").getImage();
			leftSurvivorImage = new ImageIcon("../images/OldSurvivor2.png").getImage();
		} else if (type == 3) {
			rightSurvivorImage = new ImageIcon("../images/SuitSurvivor1.png").getImage();
			leftSurvivorImage = new ImageIcon("../images/SuitSurvivor2.png").getImage();
		} else if (type == 4) {
			rightSurvivorImage = new ImageIcon("../images/KeshavSurvivor1.png").getImage();
			leftSurvivorImage = new ImageIcon("../images/KeshavSurvivor2.png").getImage();
		}

	}

	public Survivor(int xPos, int yPos, int type, boolean lookRight) {
		/*
		 * Constructor for a single survivor.
		 * Sets the position of the survivor
		 * and also sets the hitbox and image
		 * based on what type of survivor it is.
		 */
		this.lookRight = lookRight;
		this.xPos = xPos;
		this.yPos = yPos;
		this.type = type;

		hitboxX = xPos;
		hitboxY = yPos + 70;
		width = 68;
		saved = false;
		height = 100;
		hitboxWidth = width;
		hitboxHeight = 30;
		countedSurvivor = false;
		if (type == 1) {
			rightSurvivorImage = new ImageIcon("../images/NormalSurvivor1.png").getImage();
			leftSurvivorImage = new ImageIcon("../images/NormalSurvivor2.png").getImage();
		} else if (type == 2) {
			rightSurvivorImage = new ImageIcon("../images/OldSurvivor1.png").getImage();
			leftSurvivorImage = new ImageIcon("../images/OldSurvivor2.png").getImage();
		} else if (type == 3) {
			rightSurvivorImage = new ImageIcon("../images/SuitSurvivor1.png").getImage();
			leftSurvivorImage = new ImageIcon("../images/SuitSurvivor2.png").getImage();
		} else if (type == 4) {
			rightSurvivorImage = new ImageIcon("../images/KeshavSurvivor1.png").getImage();
			leftSurvivorImage = new ImageIcon("../images/KeshavSurvivor2.png").getImage();
		}

	}

	public void update(int p1X, int p1Y, int p2X, int p2Y, ArrayList<Integer> playerOneStepsX,
			ArrayList<Integer> playerOneStepsY, ArrayList<Integer> playerTwoStepsX, ArrayList<Integer> playerTwoStepsY,
			int p1SurvivorsRescued, int p2SurvivorsRescued, int survivorCount) {
		/*
		 * Called by the action performed in the
		 * player class to reduce the amount of timers
		 * to make the game less laggy. This makes
		 * the players move in the steps of the player
		 * and makes sure when it touches a helicopter
		 * it disappears and gains points for it
		 */
		this.playerOneStepsX = playerOneStepsX;
		this.playerOneStepsY = playerOneStepsY;
		this.playerTwoStepsX = playerTwoStepsX;
		this.playerTwoStepsY = playerTwoStepsY;

		this.p1X = p1X;
		this.p1Y = p1Y;
		this.p2X = p2X;
		this.p2Y = p2Y;

		hitboxX = xPos;
		hitboxY = yPos + 70;
		findNearestPlayer();
		distanceFromP1 = Math.sqrt((p1X * -1 + xPos - PLAYER_X) * (p1X * -1 + xPos - PLAYER_X)
				+ (p1Y * -1 + yPos - PLAYER_Y) * (p1Y * -1 + yPos - PLAYER_Y));
		distanceFromP2 = Math.sqrt((yPos - (p2Y + PLAYER_Y)) * (yPos - (p2Y + PLAYER_Y))
				+ (xPos - (p2X + PLAYER_X)) * (xPos - (p2X + PLAYER_X)));
		if (findNearestPlayer() == 1) {
			if (PLAYER_HITBOX_X + PLAYER_WIDTH / 2 < p1X * -1 + xPos + width / 2)///
			{
				lookRight = false;
			} else {
				lookRight = true;
			}
		} else {
			if (p1X * -1 + p2X + PLAYER_WIDTH / 2 + PLAYER_X < p1X * -1 + xPos + width / 2) {
				lookRight = false;
			} else {
				lookRight = true;
			}
		}

		if (!saved) {
			if (PLAYER_HITBOX_X + PLAYER_WIDTH > p1X * -1 + hitboxX
					&& PLAYER_HITBOX_X < p1X * -1 + hitboxX + hitboxWidth
					&& PLAYER_HITBOX_Y + PLAYER_HITBOX_HEIGHT > p1Y * -1 + hitboxY
					&& PLAYER_HITBOX_Y < p1Y * -1 + hitboxY + hitboxHeight) {
				saved = true;
				following = 1;
				survivorNum = p1SurvivorsRescued;
			} else if (p2X + PLAYER_HITBOX_X + PLAYER_WIDTH > xPos && p2X + PLAYER_HITBOX_X < xPos + width
					&& p2Y + PLAYER_HITBOX_Y + PLAYER_HITBOX_HEIGHT > hitboxY
					&& p2Y + PLAYER_HITBOX_Y < hitboxY + hitboxHeight) {
				saved = true;
				following = 2;
				survivorNum = p2SurvivorsRescued;
			}
		} else {
			if (following == 1) {
				xPos = PLAYER_X - playerOneStepsX.get(survivorNum * 10 + 10);
				yPos = PLAYER_Y - 1 - 1 * survivorNum - playerOneStepsY.get(survivorNum * 10 + 10);
			} else if (following == 2) {
				xPos = PLAYER_X - playerTwoStepsX.get(survivorNum * 10 + 10);
				yPos = PLAYER_Y - 1 - 1 * survivorNum - playerTwoStepsY.get(survivorNum * 10 + 10);
			}
		}
	}

	public int findNearestPlayer() {
		/*
		 * finds nearest player
		 */
		int nearestPlayer;

		if (Math.min(distanceFromP1, distanceFromP2) == distanceFromP1) {
			nearestPlayer = 1;
		} else {
			nearestPlayer = 2;
		}

		return nearestPlayer;
	}
}
