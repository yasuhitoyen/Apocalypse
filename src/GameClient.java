
/*
 * Michael Yen
 * A P O C A L Y P S E
 */
import java.net.*;
import java.io.*;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.util.*;

public class GameClient implements ActionListener {
	/*
	 * This class gets variables from the server
	 * that is then sent to the client. The client
	 * then sends it to player two where it can
	 * use the information to print images out
	 * based like p1X, p1Y, p1 bullets etc.
	 * 
	 */
	int p1MouseX, p1MouseY, p2MouseX, p2MouseY;
	boolean p2UsingSettings;
	String timeString;
	int p1InvisTimer, p2InvisTimer;
	float fadeInGame;
	String gameChat[];
	int[] p2GunBulletsID;
	int movementType, shootType;
	boolean p1ShowLaser, p2ShowLaser;
	int[] playerTwoStepsXArray, playerTwoStepsYArray;
	Scanner scan = new Scanner(System.in);
	final int port = 1234;
	int gameMode, messageNumber, gold, zombiesKilled;
	boolean victory;
	boolean messageSent;
	int p2BulletDamage, p1GunType, p2GunType;
	Timer clientTick = new Timer(1, this);
	Socket connection;
	String text;

	int p2X = 0, p2Y = 0, p1X = 0, p1Y = 0;
	int sendingTurn = 0;
	double p1GunAngle, p2GunAngle;
	ObjectInputStream recieveServer;
	ObjectOutputStream sendServer;
	String serverIP;
	Polygon p1Bullet, p2Bullet;
	boolean connectionStatus, p2LeftLooked, p2RightLooked, p1RightLooked, p1LeftLooked, p1ShowBullet, p2ShowBullet;
	int p1BulletCoorX[], p1BulletCoorY[], p2BulletCoorX[], p2BulletCoorY[];
	boolean p2It = false, tagged, p1It;

	ArrayList<Objects> objects = new ArrayList<Objects>();
	int[] objectsXPos;
	int[] objectsYPos;
	String[] objectsName;
	int p1GunBulletXPos[];
	int p1GunBulletYPos[];
	int p1GunBulletXPosFromPlayer[];
	int p1GunBulletYPosFromPlayer[];
	boolean p1GunBulletPlayerRightLooked[];
	double p1GunBulletGunAngle[];

	int p2GunBulletXPos[];
	int p2GunBulletYPos[];
	int p2GunBulletXPosFromPlayer[];
	int p2GunBulletYPosFromPlayer[];
	boolean p2GunBulletPlayerRightLooked[];
	double p2GunBulletGunAngle[];
	int p1Health, p2Health;
	int zombieXPos[];
	int zombieYPos[];
	int zombieHealth[];
	boolean zombieHurt[];
	boolean gameEnded;
	int zombieType[];
	boolean zombieLookRight[];
	int[] playerTwoBulletsID;
	int deleteBulletID;
	int p2HurtPerMS;
	int[] survivorXPos;
	int[] survivorYPos;
	int[] survivorType;
	boolean[] survivorLookRight;
	boolean showInfo;
	int level;
	String mapName;

	public GameClient(String serverIP) {
		/*
		 * Gets the server IP that player
		 * two created and receives it and
		 * uses it to find the socket for the
		 * server it needs to connect to
		 */
		p2BulletDamage = 0;

		survivorYPos = new int[0];
		survivorXPos = new int[0];
		survivorType = new int[0];
		survivorLookRight = new boolean[0];

		zombieXPos = new int[0];
		zombieYPos = new int[0];
		zombieHealth = new int[0];
		zombieHurt = new boolean[0];
		zombieType = new int[0];
		zombieLookRight = new boolean[0];

		playerTwoBulletsID = new int[0];

		playerTwoStepsXArray = new int[10];
		playerTwoStepsYArray = new int[10];

		p1BulletCoorX = new int[4];
		p1BulletCoorY = new int[4];
		p2BulletCoorX = new int[4];
		p2BulletCoorY = new int[4];

		p1GunAngle = 0;
		p2GunAngle = 0;
		p1GunType = 0;
		p1GunAngle = 0;

		p1GunBulletXPos = new int[0];
		p1GunBulletYPos = new int[0];
		p1GunBulletPlayerRightLooked = new boolean[0];
		p1GunBulletGunAngle = new double[0];
		p1GunBulletXPosFromPlayer = new int[0];
		p1GunBulletYPosFromPlayer = new int[0];

		p2GunBulletXPos = new int[0];
		p2GunBulletYPos = new int[0];
		p2GunBulletPlayerRightLooked = new boolean[0];
		p2GunBulletGunAngle = new double[0];
		p2GunBulletXPosFromPlayer = new int[0];
		p2GunBulletYPosFromPlayer = new int[0];

		this.serverIP = serverIP;
		try {
			connection = new Socket(InetAddress.getByName(serverIP), port);
			handleInformation();
		} catch (Exception error) {
			// System.out.println("u hav no friends");
		}
		clientTick.start();
	}

	public void run() {
		/*
		 * This start the timer for the information handling
		 */
	}

	public void handleInformation() throws IOException, ClassNotFoundException {
		/*
		 * First off, I throw IOException because IOException
		 * handles inputs and outputs and checks if something can
		 * be received as an input or sent as an output. I throw
		 * the ClassNotFoundException because receive arrays to the server.
		 * Keep in mind arrays are objects and all objects are
		 * technically classes. When I use an ObjectOutputStream and
		 * ObjectInputStream, I can only send and receive data types,
		 * strings, and objects. So when I receive an object through
		 * ObjectInputStream, I get the array as an object. To make sure
		 * I don't cast the wrong object type(class) to the object I
		 * get, I throw ClassNotFoundException.
		 * 
		 * 
		 */
		if (connection.isConnected()) {
			sendServer = new ObjectOutputStream(connection.getOutputStream());
			recieveServer = new ObjectInputStream(connection.getInputStream());
			connectionStatus = true;
		} else {
			connection.shutdownInput();
			connection.shutdownOutput();
			sendServer.close();
			recieveServer.close();
			connectionStatus = false;
		}

		if (connection.isConnected()) {
			/*
			 * Over here is where I receive information such as the
			 * other player's coordinates, the objects on the map,
			 * the gun angle etc. I have to match the receive to
			 * what the server sends.
			 *
			 * so like:
			 * 
			 * sendServer.writeInt(p2X) and the other class -> p2X =
			 * recieveClient.readInt();
			 * sendServer.writeInt(p2Y) and the other class -> p2Y =
			 * recieveClient.readInt();
			 * sendServer.writeInt(p2RightLooked) and the other class -> p2RightLooked =
			 * recieveClient.boolean();
			 * sendServer.writeInt(p2...
			 */
			sendServer.writeInt(p2MouseX);
			sendServer.flush();
			sendServer.writeInt(p2MouseY);
			sendServer.flush();
			sendServer.writeBoolean(p2UsingSettings);
			sendServer.flush();
			sendServer.writeInt(p2X);
			sendServer.flush();
			sendServer.writeInt(p2Y);
			sendServer.flush();
			sendServer.writeBoolean(p2RightLooked);
			sendServer.flush();
			sendServer.writeBoolean(p2LeftLooked);
			sendServer.flush();
			sendServer.writeObject(p2BulletCoorX);
			sendServer.flush();
			sendServer.writeObject(p2BulletCoorY);
			sendServer.flush();
			sendServer.writeDouble(p2GunAngle);
			sendServer.flush();
			sendServer.writeBoolean(p2ShowBullet);
			sendServer.flush();
			sendServer.writeObject(p2GunBulletGunAngle);
			sendServer.flush();
			sendServer.writeObject(p2GunBulletPlayerRightLooked);
			sendServer.flush();
			sendServer.writeInt(p2GunType);
			sendServer.flush();
			sendServer.writeInt(p2BulletDamage);
			sendServer.flush();
			sendServer.writeObject(p2GunBulletXPos);
			sendServer.flush();
			sendServer.writeObject(p2GunBulletYPos);
			sendServer.flush();
			sendServer.writeObject(p2GunBulletXPosFromPlayer);
			sendServer.flush();
			sendServer.writeObject(p2GunBulletYPosFromPlayer);
			sendServer.flush();
			sendServer.writeObject(playerTwoStepsXArray);
			sendServer.flush();
			sendServer.writeObject(playerTwoStepsYArray);
			sendServer.flush();
			sendServer.writeObject(playerTwoBulletsID);
			sendServer.flush();
			sendServer.writeInt(messageNumber);
			sendServer.flush();
			sendServer.writeObject(text);
			sendServer.flush();
			sendServer.writeBoolean(p2ShowLaser);
			sendServer.flush();
			sendServer.writeObject(p2GunBulletsID);
			sendServer.flush();

			p1MouseX = recieveServer.readInt();
			p1MouseY = recieveServer.readInt();
			gameMode = recieveServer.readInt();
			fadeInGame = recieveServer.readFloat();
			p2InvisTimer = recieveServer.readInt();
			gameEnded = recieveServer.readBoolean();
			p1X = recieveServer.readInt();
			p1Y = recieveServer.readInt();
			p1Health = recieveServer.readInt();
			p2Health = recieveServer.readInt();
			p2HurtPerMS = recieveServer.readInt();
			tagged = recieveServer.readBoolean();
			p1It = recieveServer.readBoolean();
			p1RightLooked = recieveServer.readBoolean();
			p1LeftLooked = recieveServer.readBoolean();
			p1BulletCoorX = (int[]) recieveServer.readObject();
			p1BulletCoorY = (int[]) recieveServer.readObject();
			p1GunAngle = recieveServer.readDouble();
			p1ShowBullet = recieveServer.readBoolean();
			objectsName = (String[]) recieveServer.readObject();
			objectsXPos = (int[]) recieveServer.readObject();
			objectsYPos = (int[]) recieveServer.readObject();
			p1GunBulletGunAngle = (double[]) recieveServer.readObject();
			p1GunBulletPlayerRightLooked = (boolean[]) recieveServer.readObject();
			p1GunBulletXPos = (int[]) recieveServer.readObject();
			p1GunBulletYPos = (int[]) recieveServer.readObject();
			p1GunBulletXPosFromPlayer = (int[]) recieveServer.readObject();
			p1GunBulletYPosFromPlayer = (int[]) recieveServer.readObject();
			deleteBulletID = recieveServer.readInt();
			zombieXPos = (int[]) recieveServer.readObject();
			zombieYPos = (int[]) recieveServer.readObject();
			zombieHealth = (int[]) recieveServer.readObject();
			zombieHurt = (boolean[]) recieveServer.readObject();
			zombieType = (int[]) recieveServer.readObject();
			zombieLookRight = (boolean[]) recieveServer.readObject();
			survivorXPos = (int[]) recieveServer.readObject();
			survivorYPos = (int[]) recieveServer.readObject();
			survivorType = (int[]) recieveServer.readObject();
			survivorLookRight = (boolean[]) recieveServer.readObject();
			p1GunType = recieveServer.readInt();
			gameChat = (String[]) recieveServer.readObject();
			p1ShowLaser = recieveServer.readBoolean();
			victory = recieveServer.readBoolean();
			gold = recieveServer.readInt();
			zombiesKilled = recieveServer.readInt();
			timeString = (String) recieveServer.readObject();
			showInfo = recieveServer.readBoolean();
			level = recieveServer.readInt();
			mapName = (String) recieveServer.readObject();
		}
	}

	public void actionPerformed(ActionEvent e) {
		/*
		 * This method is the action performed and
		 * calls the handleInformation method. I cast
		 * a try-catch block for in case the socket in
		 * the server is not open and cannot connect.
		 */
		try {
			handleInformation();
		} catch (Exception error) {
			connectionStatus = false;
		}
	}
}