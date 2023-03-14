
/*
 * Michael Yen
 * A P O C A L Y P S E
 */
import java.awt.event.*;
import javax.swing.Timer;
import javax.swing.*;

public class Bullet extends JPanel implements ActionListener {
	Timer bulletTick = new Timer(10, this);
	int bulletTimer, reloadTime, tempShowTime;
	final int BULLET_SHOW_TIME = 1;
	int ammoAmount;
	int maxAmmo, ammoTime, ammoReloadTime;
	boolean showBullet;
	boolean pressMouse, availableToShoot, currentlyReloading;

	public Bullet() {
		/*
		 * I do not need to pass in anything
		 * for this class through a constructor because I don't need information
		 * from when a bullet is created. DO NOT MISTAKE THIS WITH GUNBULLET.
		 * GunBullet is the literal bullet but this bullet keeps track
		 * of when the gun is reloading and when the player can actually shoot.
		 */
		pressMouse = false;
		reloadTime = 50;
		showBullet = false;
		bulletTimer = 0;
		bulletTick.start();
		availableToShoot = false;
		ammoAmount = 0;
		maxAmmo = 10;
		ammoTime = 0;
		ammoReloadTime = 100;
	}

	public void actionPerformed(ActionEvent e) {
		/*
		 * First of all, I keep track of how much time has passed
		 * since the mouse was last pressed. So I do !pressMouse
		 * so when the mouse is not pressed I can reset everything.
		 * Here I keep adding 1 every 10 ms and every 50ms, I tell
		 * the player, "hey bro u can shoot now" and then i shoot
		 * a bullet from that class, and from that class i set
		 * showBullet to false. showBullet is the boolean to
		 */
		if (ammoAmount >= maxAmmo) {
			ammoTime++;
			currentlyReloading = true;
			if (ammoTime > ammoReloadTime) {
				ammoAmount = 0;
				ammoTime = 0;
				currentlyReloading = false;
			}
		} else {
			ammoTime = 0;
		}

		bulletTimer++;
		if (!(ammoAmount >= maxAmmo)) {
			if (pressMouse && bulletTimer % reloadTime == 0) {
				showBullet = true;
				ammoAmount++;
			}
		}

	}
}