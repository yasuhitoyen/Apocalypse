
/*
 * Michael Yen
 * A P O C A L Y P S E
 */
import java.net.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.util.*;

public class MainGame extends JFrame implements ActionListener {
	/*
	 * This is just the main frame. When a connection
	 * is established, the frame takes out the
	 * menu and adds the control panel which contains
	 * multiple panels through a card layout.
	 */
	Timer mainTimer;
	int gameMode = 0;
	boolean addedControlPanel;
	final int FRAME_WIDTH = 1370, FRAME_HEIGHT = 750;
	Rectangle playButton, settingButton;
	PlayerOne playerOne;
	PlayerTwo playerTwo;
	ControlPanel controlPanel;
	MenuScreen ms;

	public static void main(String args[]) {
		/*
		 * This creates the main FRAME
		 * and only contains the panel that controls
		 * other panels. Keep in mind this is just
		 * the body and does not actually do anything.
		 * 
		 */
		MainGame mainFrame = new MainGame();
	}

	public MainGame() {
		/*
		 * Constructor that starts the timer
		 * for checking if a connection between
		 * two computers are created and creates
		 * a new menu screen (panel) that the users
		 * can interact with.
		 * 
		 */
		mainTimer = new Timer(1, this);
		addedControlPanel = false;
		ms = new MenuScreen();
		add(ms);
		mainTimer.start();
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		/*
		 * Checks if the control panel is
		 * created. If it is, then it just doesn't
		 * do anything. If it hasn't, then I create a
		 * control panel and add it to this frame
		 * IF AND ONLY IF A CONNECTION HAS BEEN
		 * ESTABLISHED BETWEEN THE TWO COMPUTERS
		 * 
		 */
		if (!addedControlPanel) {
			if (!ms.mouseInSinglePlayer) {
				if (ms.connected) {
					addedControlPanel = true;
					remove(ms);
					mainTimer.stop();
					revalidate();

					if (ms.playerType == 1) {
						try {
							controlPanel = new ControlPanel(1, ms.server);
						} catch (IOException error) {
							// error.printStackTrace();
						}
						this.setTitle("Player One");
						add(controlPanel);

					} else if (ms.playerType == 2) {
						try {
							controlPanel = new ControlPanel(2, ms.client);

						} catch (IOException error) {

							// error.printStackTrace();
						}
						this.setTitle("Player Two");
						add(controlPanel);

						System.out.println(ms.client.connectionStatus);
					}
					ms = null;
					revalidate();

				}

			} else {

				addedControlPanel = true;
				remove(ms);
				mainTimer.stop();
				revalidate();

				try {
					controlPanel = new ControlPanel();
				} catch (IOException error) {
					// error.printStackTrace();
				}
				this.setTitle("Player One");
				add(controlPanel);

				ms = null;
				revalidate();

			}

		}
	}
}
