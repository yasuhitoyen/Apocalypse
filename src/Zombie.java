
/*
 * Michael Yen
 * A P O C A L Y P S E
 */
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Zombie {
	/*
	 * Keeps track of the zombie xpos, ypos, hitbox, images etc.
	 * The player creates instances of this and adds it in the
	 * zombies array list where it can be used to print out
	 * the images and also check if the zombies are eating the player
	 * 
	 */
	Image rightZombieImage, leftZombieImage, rightHurtZombieImage, leftHurtZombieImage;
	double xPos, yPos;
	int type, health;
	double distanceFromP1, distanceFromP2;
	int p1X, p1Y, p2X, p2Y;
	int width, height, hitboxX, hitboxY, hitboxWidth, hitboxHeight, hurtTimer;
	int zombieDamage;
	int p1InvisTimer, p2InvisTimer;
	boolean lookRight, die, hurt;
	Image exitClientSign;
	double speed;
	ArrayList<Objects> objects;
	Rectangle2D zombieHitbox = new Rectangle2D.Double();

	public Zombie(int type, int xPos, int yPos) {
		/*
		 * Sets the position of the zombie and also
		 * creates the images. The objects are passed
		 * in from the player so the zombie can check
		 * if the zombie is hitting objects such as
		 * trees. Depending on the type, different
		 * variables are initialized differently
		 * depending on how strong the zombie is,
		 * how fast it is etc.
		 */
		this.type = type;
		this.xPos = xPos;
		this.yPos = yPos;
		die = false;
		objects = new ArrayList<Objects>();
		hurt = false;
		if (this.type == 1) {
			height = 100;
			width = 68;
			rightZombieImage = new ImageIcon("../images/ZombieNormal1.png").getImage();
			leftZombieImage = new ImageIcon("../images/ZombieNormal2.png").getImage();
			rightHurtZombieImage = new ImageIcon("../images/ZombieNormalHurt1.png").getImage();
			leftHurtZombieImage = new ImageIcon("../images/ZombieNormalHurt2.png").getImage();
			speed = 1.9 + Math.random() * 1.2;
			health = 30;
			hitboxX = xPos;
			hitboxY = yPos + 70;
			hitboxHeight = 30;
			hitboxWidth = 68;
			zombieDamage = 1;
		} else if (this.type == 2) {
			height = 130;
			width = 72;
			rightZombieImage = new ImageIcon("../images/ZombieCone1.png").getImage();
			leftZombieImage = new ImageIcon("../images/ZombieCone2.png").getImage();
			rightHurtZombieImage = new ImageIcon("../images/ZombieConeHurt1.png").getImage();
			leftHurtZombieImage = new ImageIcon("../images/ZombieConeHurt2.png").getImage();
			speed = 1.8 + Math.random();
			health = 60;
			hitboxX = xPos;
			hitboxY = yPos + 70;
			hitboxHeight = 30;
			hitboxWidth = 68;
			zombieDamage = 2;
		} else if (this.type == 3) {
			height = 135;
			width = 90;
			rightZombieImage = new ImageIcon("../images/ZombieBucket1.png").getImage();
			leftZombieImage = new ImageIcon("../images/ZombieBucket2.png").getImage();
			rightHurtZombieImage = new ImageIcon("../images/ZombieBucketHurt1.png").getImage();
			leftHurtZombieImage = new ImageIcon("../images/ZombieBucketHurt2.png").getImage();
			speed = 1.5 + Math.random() * .5;
			health = 130;
			hitboxX = xPos;
			hitboxY = yPos + 105;
			hitboxHeight = 30;
			hitboxWidth = 68;
			zombieDamage = 2;
		} else if (this.type == 4) {
			height = 135;
			width = 90;
			rightZombieImage = new ImageIcon("../images/ZombieAxe1.png").getImage();
			leftZombieImage = new ImageIcon("../images/ZombieAxe2.png").getImage();
			rightHurtZombieImage = new ImageIcon("../images/ZombieAxeHurt1.png").getImage();
			leftHurtZombieImage = new ImageIcon("../images/ZombieAxeHurt2.png").getImage();
			speed = 1.9 + Math.random();
			health = 60;
			hitboxX = xPos;
			hitboxY = yPos + 105;
			hitboxHeight = 30;
			hitboxWidth = 68;
			zombieDamage = 5;
		} else if (this.type == 5) {
			height = 100;
			width = 68;
			rightZombieImage = new ImageIcon("../images/ZombieFork1.png").getImage();
			leftZombieImage = new ImageIcon("../images/ZombieFork2.png").getImage();
			rightHurtZombieImage = new ImageIcon("../images/ZombieForkHurt1.png").getImage();
			leftHurtZombieImage = new ImageIcon("../images/ZombieForkHurt2.png").getImage();
			speed = 2.4 + Math.random() * 2;
			health = 30;
			hitboxX = xPos;
			hitboxY = yPos + 70;
			hitboxHeight = 30;
			hitboxWidth = 68;
			zombieDamage = 4;
		} else if (this.type == 6) {
			height = 140;
			width = 130;
			rightZombieImage = new ImageIcon("../images/ZombieSamurai1.png").getImage();
			leftZombieImage = new ImageIcon("../images/ZombieSamurai2.png").getImage();
			rightHurtZombieImage = new ImageIcon("../images/ZombieSamuraiHurt1.png").getImage();
			leftHurtZombieImage = new ImageIcon("../images/ZombieSamuraiHurt2.png").getImage();
			speed = 2 + Math.random();
			health = 200;
			hitboxX = xPos;
			hitboxY = yPos + 110;
			hitboxHeight = 30;
			hitboxWidth = 68;
			zombieDamage = 5;
		}

	}

	public Zombie(int type, int xPos, int yPos, int health, boolean hurt, boolean lookRight) {
		/*
		 * This constructor is hard to explain but
		 * to simplify, the 2 players need different
		 * constructors when creating zombies. One is
		 * for creating actual zombies that move and
		 * one for recreating a zombie that was created
		 * in the player one class. This is the constructor
		 * that recreates a zombie from the player one
		 * class.
		 */
		this.lookRight = lookRight;
		this.hurt = hurt;
		this.health = health;
		this.type = type;
		this.xPos = xPos;
		this.yPos = yPos;
		die = false;
		objects = new ArrayList<Objects>();
		hurt = false;
		if (this.type == 1) {
			height = 100;
			width = 68;
			rightZombieImage = new ImageIcon("../images/ZombieNormal1.png").getImage();
			leftZombieImage = new ImageIcon("../images/ZombieNormal2.png").getImage();
			rightHurtZombieImage = new ImageIcon("../images/ZombieNormalHurt1.png").getImage();
			leftHurtZombieImage = new ImageIcon("../images/ZombieNormalHurt2.png").getImage();
			speed = 2.2 + Math.random() * 1.6;
			hitboxX = xPos;
			hitboxY = yPos + 70;
			hitboxHeight = 30;
			hitboxWidth = 68;

		} else if (this.type == 2) {
			height = 130;
			width = 72;
			rightZombieImage = new ImageIcon("../images/ZombieCone1.png").getImage();
			leftZombieImage = new ImageIcon("../images/ZombieCone2.png").getImage();
			rightHurtZombieImage = new ImageIcon("../images/ZombieConeHurt1.png").getImage();
			leftHurtZombieImage = new ImageIcon("../images/ZombieConeHurt2.png").getImage();
			speed = 2.2 + Math.random();
			hitboxX = xPos;
			hitboxY = yPos + 100;
			hitboxHeight = 30;
			hitboxWidth = 68;
		} else if (this.type == 3) {

			height = 135;
			width = 90;
			rightZombieImage = new ImageIcon("../images/ZombieBucket1.png").getImage();
			leftZombieImage = new ImageIcon("../images/ZombieBucket2.png").getImage();
			rightHurtZombieImage = new ImageIcon("../images/ZombieBucketHurt1.png").getImage();
			leftHurtZombieImage = new ImageIcon("../images/ZombieBucketHurt2.png").getImage();
			speed = 1.7 + Math.random();
			health = 130;
			hitboxX = xPos;
			hitboxY = yPos + 105;
			hitboxHeight = 30;
			hitboxWidth = 68;
			zombieDamage = 2;

		} else if (this.type == 4) {
			height = 135;
			width = 80;
			rightZombieImage = new ImageIcon("../images/ZombieAxe1.png").getImage();
			leftZombieImage = new ImageIcon("../images/ZombieAxe2.png").getImage();
			rightHurtZombieImage = new ImageIcon("../images/ZombieAxeHurt1.png").getImage();
			leftHurtZombieImage = new ImageIcon("../images/ZombieAxeHurt2.png").getImage();
			speed = 1.7 + Math.random();
			health = 200;
			hitboxX = xPos;
			hitboxY = yPos + 105;
			hitboxHeight = 30;
			hitboxWidth = 68;
			zombieDamage = 3;
		} else if (this.type == 5) {
			height = 100;
			width = 68;
			rightZombieImage = new ImageIcon("../images/ZombieFork1.png").getImage();
			leftZombieImage = new ImageIcon("../images/ZombieFork2.png").getImage();
			rightHurtZombieImage = new ImageIcon("../images/ZombieForkHurt1.png").getImage();
			leftHurtZombieImage = new ImageIcon("../images/ZombieForkHurt2.png").getImage();
			speed = 2.4 + Math.random() * 2;
			health = 30;
			hitboxX = xPos;
			hitboxY = yPos + 70;
			hitboxHeight = 30;
			hitboxWidth = 68;
			zombieDamage = 4;
		} else if (this.type == 6) {
			height = 140;
			width = 130;
			rightZombieImage = new ImageIcon("../images/ZombieSamurai1.png").getImage();
			leftZombieImage = new ImageIcon("../images/ZombieSamurai2.png").getImage();
			rightHurtZombieImage = new ImageIcon("../images/ZombieSamuraiHurt1.png").getImage();
			leftHurtZombieImage = new ImageIcon("../images/ZombieSamuraiHurt2.png").getImage();
			speed = 2 + Math.random();
			health = 200;
			hitboxX = xPos;
			hitboxY = yPos + 110;
			hitboxHeight = 30;
			hitboxWidth = 68;
			zombieDamage = 6;
		}
	}

	public void update(int p1X, int p1Y, int p2X, int p2Y, ArrayList<Objects> objects, int p1InvisTimer,
			int p2InvisTimer) {
		/*
		 * Is called by the action performed in the
		 * player class and actually moves the zombie
		 * and checks if the zombie hits an object
		 * so it stops moving
		 */
		this.p1InvisTimer = p1InvisTimer;
		this.p2InvisTimer = p2InvisTimer;
		this.objects = objects;
		this.p1X = p1X;
		this.p1Y = p1Y;
		this.p2X = p2X;
		this.p2Y = p2Y;
		// = new Rectangle2D((int)xPos, (int)yPos, width, height);
		if (type == 1) {
			hitboxX = (int) xPos;
			hitboxY = (int) yPos + 70;
			hitboxHeight = 30;
			hitboxWidth = 68;
		} else if (type == 2) {
			hitboxX = (int) xPos;
			hitboxY = (int) yPos + 70;
			hitboxHeight = 30;
			hitboxWidth = 68;
		} else if (type == 3) {
			hitboxX = (int) xPos;
			hitboxY = (int) yPos + 130;
			hitboxHeight = 30;
			hitboxWidth = 68;
		} else if (type == 4) {
			hitboxX = (int) xPos;
			hitboxY = (int) yPos + 130;
			hitboxHeight = 30;
			hitboxWidth = 68;
		} else if (type == 5) {
			hitboxX = (int) xPos;
			hitboxY = (int) yPos + 70;
			hitboxHeight = 30;
			hitboxWidth = 68;
		} else if (type == 6) {
			hitboxX = (int) xPos;
			hitboxY = (int) yPos + 110;
			hitboxHeight = 30;
			hitboxWidth = 68;
		}

		if (health <= 0) {
			die = true;
		}
		if (findNearestPlayer() == 1) {
			if (xPos + width / 2 < p1X - 2) {
				xPos += speed;
				lookRight = true;
				for (Objects object : objects) {
					if (!object.image.contains("Border"))
						if (hitboxY < object.hitboxY + object.hitboxHeight && hitboxY + hitboxHeight > object.hitboxY
								&& hitboxX < object.hitboxX + object.hitboxWidth + 5
								&& hitboxX + hitboxWidth > object.hitboxX - 5) {
							xPos -= speed;
						}
				}
			}
			if (xPos + width / 2 > p1X + 2) {
				xPos -= speed;
				lookRight = false;

				for (Objects object : objects) {
					if (!object.image.contains("Border"))
						if (hitboxY < object.hitboxY + object.hitboxHeight && hitboxY + hitboxHeight > object.hitboxY
								&& hitboxX < object.hitboxX + object.hitboxWidth + 5
								&& hitboxX + hitboxWidth > object.hitboxX - 5) {
							xPos += speed;
						}
				}
			}
			if (yPos + height / 2 < p1Y - 2) {
				yPos += speed;
				for (Objects object : objects) {
					if (!object.image.contains("Border"))
						if (hitboxY < object.hitboxY + object.hitboxHeight + 5
								&& hitboxY + hitboxHeight > object.hitboxY - 5
								&& hitboxX < object.hitboxX + object.hitboxWidth
								&& hitboxX + hitboxWidth > object.hitboxX) {

							yPos -= speed;
						}
				}
			}
			if (yPos + width / 2 > p1Y + 2) {
				yPos -= speed;
				for (Objects object : objects) {
					if (hitboxY < object.hitboxY + object.hitboxHeight + 5
							&& hitboxY + hitboxHeight > object.hitboxY - 5
							&& hitboxX < object.hitboxX + object.hitboxWidth
							&& hitboxX + hitboxWidth > object.hitboxX) {
						if (!object.image.contains("Border"))
							yPos += speed;
					}
				}
			}
		} else if (findNearestPlayer() == 2) {
			if (xPos + width / 2 < p2X - 2) {
				xPos += speed;
				lookRight = true;
				for (Objects object : objects) {
					if (hitboxY < object.hitboxY + object.hitboxHeight && hitboxY + hitboxHeight > object.hitboxY
							&& hitboxX < object.hitboxX + object.hitboxWidth + 5
							&& hitboxX + hitboxWidth > object.hitboxX - 5) {
						if (!object.image.contains("Border"))
							xPos -= speed;
					}
				}
			}
			if (xPos + width / 2 > p2X + 2) {
				xPos -= speed;
				lookRight = false;
				for (Objects object : objects) {
					if (hitboxY < object.hitboxY + object.hitboxHeight && hitboxY + hitboxHeight > object.hitboxY
							&& hitboxX < object.hitboxX + object.hitboxWidth + 5
							&& hitboxX + hitboxWidth > object.hitboxX - 5) {
						if (!object.image.contains("Border"))
							xPos += speed;
					}
				}
			}
			if (yPos + height / 2 < p2Y - 2) {
				yPos += speed;
				for (Objects object : objects) {
					if (hitboxY < object.hitboxY + object.hitboxHeight + 5
							&& hitboxY + hitboxHeight > object.hitboxY - 5
							&& hitboxX < object.hitboxX + object.hitboxWidth
							&& hitboxX + hitboxWidth > object.hitboxX) {
						if (!object.image.contains("Border"))
							yPos -= speed;
					}
				}
			}
			if (yPos + width / 2 > p2Y + 2) {
				yPos -= speed;
				for (Objects object : objects) {
					if (hitboxY < object.hitboxY + object.hitboxHeight + 5
							&& hitboxY + hitboxHeight > object.hitboxY - 5
							&& hitboxX < object.hitboxX + object.hitboxWidth
							&& hitboxX + hitboxWidth > object.hitboxX) {
						if (!object.image.contains("Border"))
							yPos += speed;
					}
				}
			}
		}

	}

	public int findNearestPlayer() {
		/*
		 * Finds the nearest player a^2 + b^2 = c^2
		 * Use a triangle between the player and zombie
		 */
		int nearestPlayer;
		distanceFromP1 = Math.sqrt((xPos - p1X) * (xPos - p1X) + (yPos - p1Y) * (yPos - p1Y));
		distanceFromP2 = Math.sqrt((xPos - p2X) * (xPos - p2X) + (yPos - p2Y) * (yPos - p2Y));
		if (Math.min(distanceFromP1, distanceFromP2) == distanceFromP1) {
			if (p1InvisTimer < 2) {
				nearestPlayer = 1;
			} else if (p2InvisTimer < 2) {
				nearestPlayer = 2;
			} else {
				nearestPlayer = 0;
			}
		}

		else {
			if (p2InvisTimer < 2) {
				nearestPlayer = 2;
			} else if (p1InvisTimer < 1) {
				nearestPlayer = 1;
			} else {
				nearestPlayer = 0;
			}
		}

		return nearestPlayer;
	}
}
