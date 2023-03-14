
/*
 * Michael Yen
 * A P O C A L Y P S E
 */
import java.net.*;
import java.util.ArrayList;

import javax.swing.Timer;

import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class GameServer implements ActionListener {
	/*
	 * WARNING: YOU MUST HAVE A IQ HIGHER THAN 250 TO UNDERSTAND THIS
	 * This is where all the communication happens. The variables
	 * come from the game client and then gets send to this class
	 * and then gets sent to player one. Then the player one
	 * gets the variable and uses it to draw with it.
	 */
	int p1MouseX, p1MouseY, p2MouseX, p2MouseY;
	int[] playerTwoStepsXArray, playerTwoStepsYArray;
	final int port = 1234;
	boolean messageRecieved;
	int messageNum;
	int[] p2GunBulletsID;
	String[] gameChat;
	String message;
	int p1X, p1Y, p2X, p2Y;
	boolean p1ShowLaser, p2ShowLaser;
	int p1InvisTimer, p2InvisTimer;
	int[] playerTwoBulletsID;
	int p1GunType;
	int gameMode, gold;
	int sendingTurn = 1, chaser = 1, p2BulletDamage, p2GunType;
	double p2GunAngle;
	double p1GunAngle;
	Timer serverTick = new Timer(1, this);
	ServerSocket serverSocket;
	Socket connection = new Socket();
	ObjectOutputStream sendClient;
	ObjectInputStream recieveClient;
	Polygon p1Bullet, p2Bullet;
	String GameServerAddress;
	boolean connectionStatus, p2LeftLooked, p2RightLooked, p1RightLooked, p1LeftLooked, p1ShowBullet, p2ShowBullet;
	int p1BulletCoorX[], p1BulletCoorY[], p2BulletCoorX[], p2BulletCoorY[];
	ArrayList<Objects> objects = new ArrayList<Objects>();

	int p1GunBulletXPos[];
	int p1GunBulletYPos[];
	boolean p1GunBulletPlayerRightLooked[];
	double p1GunBulletGunAngle[];
	int p1GunBulletXPosFromPlayer[];
	int p1GunBulletYPosFromPlayer[];
	int zombieXPos[];
	int zombieYPos[];
	int zombieHealth[];
	boolean zombieHurt[];
	boolean zombieLookRight[];
	int zombieType[];
	boolean p2UsingSettings;

	int p2GunBulletXPos[];
	int p2GunBulletYPos[];
	boolean p2GunBulletPlayerRightLooked[];
	double p2GunBulletGunAngle[];
	int p2GunBulletXPosFromPlayer[];
	int p2GunBulletYPosFromPlayer[];
	int p1Health, p2Health;
	int[] objectsXPos;
	int[] objectsYPos;
	String[] objectsName;
	boolean p1It, tagged = true;
	int deleteBulletID;
	int p2HurtPerMS;
	int[] survivorXPos;
	int[] survivorYPos;
	int[] survivorType;
	boolean[] survivorLookRight;
	boolean gameEnded;
	float fadeInGame;
	String text;
	boolean victory;
	int zombiesKilled;
	String timeString;
	boolean showInfo;
	int level;
	String mapName;
	int movementType;
	int shootType;

	public GameServer() {
		/*
		 * This constructor will get the IPAddress with:
		 * InetAddress ipAddr = InetAddress.getLocalHost();
		 * GameServerAddress = ipAddr.getHostAddress().trim();
		 * and create the ServerSocket so the client can connect
		 * to it.
		 */
		survivorYPos = new int[0];
		survivorXPos = new int[0];
		survivorType = new int[0];
		survivorLookRight = new boolean[0];
		gameChat = new String[0];

		playerTwoStepsXArray = new int[10];
		playerTwoStepsYArray = new int[10];
		playerTwoBulletsID = new int[0];
		p1BulletCoorX = new int[4];
		p1BulletCoorY = new int[4];
		p2BulletCoorX = new int[4];
		p2BulletCoorY = new int[4];

		objectsXPos = new int[0];
		objectsYPos = new int[0];
		objectsName = new String[0];
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

		zombieXPos = new int[0];
		zombieYPos = new int[0];
		zombieHealth = new int[0];
		zombieHurt = new boolean[0];
		zombieType = new int[0];
		zombieLookRight = new boolean[0];

		try {
			InetAddress ipAddr = InetAddress.getLocalHost();
			GameServerAddress = ipAddr.getHostAddress().trim();
		} catch (UnknownHostException e) {
			GameServerAddress = "Unknown";
		}

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("You already created a socket");
			// Says cannot create server socket when the same computer is creating same
			// socket
		}

		try {
			connection = serverSocket.accept();
			handleInformation();
		} catch (Exception e) {
			// System.out.println("cant accept");
		}
		serverTick.start();
		gameMode = 0;

	}

	public void run() {
		/*
		 * This method starts timer of this class
		 * and and makes the class handle
		 * the information through the
		 * "handleInformation" method
		 */
	}

	public void handleInformation() throws IOException, ClassNotFoundException {
		/*
		 * This method handles all the information that is received
		 * and sent through the client. I use the connection.isConnected
		 * to check if the connection is still up and information can still
		 * be sent.
		 *
		 */
		if (connection.isConnected()) {
			sendClient = new ObjectOutputStream(connection.getOutputStream());
			recieveClient = new ObjectInputStream(connection.getInputStream());
			connectionStatus = true;
		} else {
			connection.shutdownInput();
			connection.shutdownOutput();
			sendClient.close();
			recieveClient.close();
			connectionStatus = false;
		}

		if (connection.isConnected()) {
			/*
			 * WARNING: You must have an IQ higher than 250 to read this
			 * 
			 * Right here is where I send and recieve MANY variables
			 * including the player xpos, ypos, trees and other objects,
			 * arrays, etc. For objects like trees, it took me very long.
			 * First it was giving me a bunch of errors when I send an
			 * ArrayList and so I didn't know what to do. So i did my research
			 * and it turned out there is no way of sending ArrayLists. So
			 * i created an array of Objects(the class) and converted the
			 * arraylist to an array and set it to the array. It turns out
			 * I couldn't send that either. So I had to create multiple
			 * arrays for the parameters of an Object object and send it to
			 * the other class so they know what kind of objects are on the
			 * map and where they are.
			 * 
			 * 
			 * 
			 */
			sendClient.writeInt(p1MouseX);
			sendClient.flush();
			sendClient.writeInt(p1MouseY);
			sendClient.flush();
			sendClient.writeInt(gameMode);
			sendClient.flush();
			sendClient.writeFloat(fadeInGame);
			sendClient.flush();
			sendClient.writeInt(p2InvisTimer);
			sendClient.flush();
			sendClient.writeBoolean(gameEnded);
			sendClient.flush();
			sendClient.writeInt(p1X);
			sendClient.flush();
			sendClient.writeInt(p1Y);
			sendClient.flush();
			sendClient.writeInt(p1Health);
			sendClient.flush();
			sendClient.writeInt(p2Health);
			sendClient.flush();
			sendClient.writeInt(p2HurtPerMS);
			sendClient.flush();
			sendClient.writeBoolean(tagged);
			sendClient.flush();
			sendClient.writeBoolean(p1It);
			sendClient.flush();
			sendClient.writeBoolean(p1RightLooked);
			sendClient.flush();
			sendClient.writeBoolean(p1LeftLooked);
			sendClient.flush();
			sendClient.writeObject(p1BulletCoorX);
			sendClient.flush();
			sendClient.writeObject(p1BulletCoorY);
			sendClient.flush();
			sendClient.writeDouble(p1GunAngle);
			sendClient.flush();
			sendClient.writeBoolean(p1ShowBullet);
			sendClient.flush();
			sendClient.writeObject(objectsName);
			sendClient.flush();
			sendClient.writeObject(objectsXPos);
			sendClient.flush();
			sendClient.writeObject(objectsYPos);
			sendClient.flush();
			sendClient.writeObject(p1GunBulletGunAngle);
			sendClient.flush();
			sendClient.writeObject(p1GunBulletPlayerRightLooked);
			sendClient.flush();
			sendClient.writeObject(p1GunBulletXPos);
			sendClient.flush();
			sendClient.writeObject(p1GunBulletYPos);
			sendClient.flush();
			sendClient.writeObject(p1GunBulletXPosFromPlayer);
			sendClient.flush();
			sendClient.writeObject(p1GunBulletYPosFromPlayer);
			sendClient.flush();
			sendClient.writeInt(deleteBulletID);
			sendClient.flush();
			sendClient.writeObject(zombieXPos);
			sendClient.flush();
			sendClient.writeObject(zombieYPos);
			sendClient.flush();
			sendClient.writeObject(zombieHealth);
			sendClient.flush();
			sendClient.writeObject(zombieHurt);
			sendClient.flush();
			sendClient.writeObject(zombieType);
			sendClient.flush();
			sendClient.writeObject(zombieLookRight);
			sendClient.flush();
			sendClient.writeObject(survivorXPos);
			sendClient.flush();
			sendClient.writeObject(survivorYPos);
			sendClient.flush();
			sendClient.writeObject(survivorType);
			sendClient.flush();
			sendClient.writeObject(survivorLookRight);
			sendClient.flush();
			sendClient.writeInt(p1GunType);
			sendClient.flush();
			sendClient.writeObject(gameChat);
			sendClient.flush();
			sendClient.writeBoolean(p1ShowLaser);
			sendClient.flush();
			sendClient.writeBoolean(victory);
			sendClient.flush();
			sendClient.writeInt(gold);
			sendClient.flush();
			sendClient.writeInt(zombiesKilled);
			sendClient.flush();
			sendClient.writeObject(timeString);
			sendClient.flush();
			sendClient.writeBoolean(showInfo);
			sendClient.flush();
			sendClient.writeInt(level);
			sendClient.flush();
			sendClient.writeObject(mapName);
			sendClient.flush();

			p2MouseX = recieveClient.readInt();
			p2MouseY = recieveClient.readInt();
			p2UsingSettings = recieveClient.readBoolean();
			p2X = recieveClient.readInt();
			p2Y = recieveClient.readInt();
			p2RightLooked = recieveClient.readBoolean();
			p2LeftLooked = recieveClient.readBoolean();
			p2BulletCoorX = (int[]) recieveClient.readObject();
			p2BulletCoorY = (int[]) recieveClient.readObject();
			p2GunAngle = recieveClient.readDouble();
			p2ShowBullet = recieveClient.readBoolean();
			p2GunBulletGunAngle = (double[]) recieveClient.readObject();
			p2GunBulletPlayerRightLooked = (boolean[]) recieveClient.readObject();
			p2GunType = recieveClient.readInt();
			p2BulletDamage = recieveClient.readInt();
			p2GunBulletXPos = (int[]) recieveClient.readObject();
			p2GunBulletYPos = (int[]) recieveClient.readObject();
			p2GunBulletXPosFromPlayer = (int[]) recieveClient.readObject();
			p2GunBulletYPosFromPlayer = (int[]) recieveClient.readObject();
			playerTwoStepsXArray = (int[]) recieveClient.readObject();
			playerTwoStepsYArray = (int[]) recieveClient.readObject();
			playerTwoBulletsID = (int[]) recieveClient.readObject();
			messageNum = recieveClient.readInt();
			text = (String) recieveClient.readObject();
			p2ShowLaser = recieveClient.readBoolean();
			p2GunBulletsID = (int[]) recieveClient.readObject();

		}
	}

	public void actionPerformed(ActionEvent e) {
		/*
		 * Constantly sends and receives information
		 * to and from the client. I try catch it so
		 * it can check if the connection is still up
		 */
		try {
			handleInformation();
		} catch (Exception error) {
			connectionStatus = false;
		}

	}
}