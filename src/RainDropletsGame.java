
/*
 * Michael Yen
 * A P O C A L Y P S E
 */
import javax.swing.Timer;
import java.awt.event.*;

public class RainDropletsGame {
	/*
	 * This is the class that keeps track of
	 * the position of an individual rain.
	 */
	int xPos, yPos;
	boolean deleteThisClone;

	public RainDropletsGame() {
		/*
		 * Constructor for every rain drop. There
		 * are not many variables in this class.
		 */
		deleteThisClone = false;

		yPos = 0;
		xPos = (int) (Math.random() * 4110);
	}

	public void update() {
		/*
		 * Putting a separate timer for each rain drop
		 * is going to make the game lag so I decided
		 * to use a variable that updates every time
		 * the timer of the player is called.
		 */
		xPos -= 3;
		yPos += 10;

		if (yPos >= 2450) {
			deleteThisClone = true;
		}
	}
}
