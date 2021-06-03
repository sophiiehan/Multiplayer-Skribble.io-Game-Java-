import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import java.io.File;
import javax.swing.*;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.io.*;
import java.net.*;

public class ServerScreen extends JPanel implements ActionListener, MouseListener, MouseMotionListener {

	private MyHashMap<Location, Tile> gridMap;
	private JScrollPane scrollPane;
	private int playerPoints;
	private String guess;
	private DLList<String> wordBank;
	private boolean instructions;
	private JButton startGameButton;
	private Square[][] grid;
	private Square[][] colors;
	private int row;
	private int column;
	private Color color;
	private boolean reset;
	private BufferedImage colorPalette;
	private BufferedImage savedImage;
	private String word;

	private JButton saveImageButton;
	private JButton clearButton;
	private boolean imageSaved;
	private PrintWriter out;
	private String chats;

	private JTextPane chatPane;

	private ObjectOutputStream outObject;
	private ObjectInputStream inObject;
	private PushbackInputStream pin ;

	public ServerScreen(){
		playerPoints = 0;
		instructions = true;
		reset = false;
		guess = "";
		gridMap = new MyHashMap<Location, Tile>();
		for(int r = 10; r<540;r+=15){
			for(int c = 10; c<510; c+=15){
				gridMap.put(new Location(r, c), new Tile(255,255,255));
			}
		}

		wordBank = new DLList<String>();
		wordBank.add("apple");
		wordBank.add("avocado");
		wordBank.add("banana");
		wordBank.add("blueberry");
		wordBank.add("cherry");
		wordBank.add("grape");
		wordBank.add("kiwi");
		wordBank.add("lemon");
		wordBank.add("mango");
		wordBank.add("peach");
		wordBank.add("dragonfruit");
		wordBank.add("pineapple");
		wordBank.add("raspberry");
		wordBank.add("strawberry");
		wordBank.add("watermelon");
		wordBank.add("orange");
		wordBank.add("pear");
		wordBank.add("tomato");
		wordBank.add("horse");

		int r= (int)(Math.random()*wordBank.size());
		word = wordBank.get(r);
		//System.out.println(wordBank.get(2));

		startGameButton = new JButton("Start");
	 	startGameButton.setBounds(350,500, 200, 100);
	 	this.add(startGameButton);
	 	startGameButton.addActionListener(this);

		chats = "";

		try{
			colorPalette = ImageIO.read(new File("Colors.png"));
		}catch(IOException e){
			System.out.println(e);
		}

		reset = false;
		imageSaved = false;


		chatPane = new JTextPane();
		chatPane.setBounds(558, 300,224,100);
    add(chatPane);
		chatPane.setText(chats);

	 scrollPane = new JScrollPane(chatPane);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(558,300,224,100);
		this.add(scrollPane);



		color = new Color(0,0,0);
		addMouseListener(this);
		addMouseMotionListener(this);
		this.setLayout(null);
		this.setFocusable(true);
	}


	public Dimension getPreferredSize() {

		return new Dimension(800,600);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);


		if(instructions == false){
			scrollPane.setVisible(true);
			g.drawImage(colorPalette,555,8,null);
			chatPane.setText(chats);
			int x = 10;
			int y = 10;

			for(int i = 0; i<gridMap.getKeys().size(); i++){
				Location l = gridMap.getKeys().get(i);
				gridMap.get(l).drawMe(g, l.getX(), l.getY());
			}

			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.BOLD, 30));
			g.drawString("Your word is: " +word, 100, 550);
				g.setFont(new Font("Arial", Font.BOLD, 15));
			g.drawString("Words Guessed Correctly: "+ playerPoints, 100, 570);

			g.setColor(color);
			g.fillRect(558,450,230,130);
			g.setColor(Color.BLACK);
			g.drawRect(558,450,230,130);
		}
		if(instructions == true){
			scrollPane.setVisible(false);
			g.setFont(new Font("Arial", Font.BOLD, 15));
			g.drawString("Welcome to Sophie's version of Pictionary! (Walmart Skribble.io) ", 100, 100);
			g.drawString("How to Play (Drawer): ",250,125);
			g.drawString("1. Select a Color on the color pallete, and draw your assigned word on the canvas. ", 100, 150);
			g.drawString("2.Player guesses will show up in the chat box on the right. ", 100,175);
			g.drawString("3.When the player guesses the word correctly, you will be given a new word.", 100, 200);

			g.drawString("How to Play (Guesser): ",250,250);
			g.drawString("1. Drawing of the artist will appear on your grid ", 100, 275);
			g.drawString("2.Enter in your guess in the text box (lower case)", 100,300);
			g.drawString("3.When the player guesses the word correctly, your canvas should clear. Wait for next round.", 100, 325);
		}


		//chatPane.setText(chats);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == startGameButton){
			instructions = false;
			startGameButton.setVisible(false);
		}
		repaint();
	}
	public void reset(){
		if(guess.equals(word)){
			gridMap = new MyHashMap<Location, Tile>();
			for(int r = 10; r<540;r+=15){
				for(int c = 10; c<510; c+=15){
					gridMap.put(new Location(r, c), new Tile(255,255,255));
				}
			}

			try {

				outObject.writeObject(gridMap.toString());
			//	outObject.writeObject("reset");
				// reset = true;
				// outObject.writeObject(reset);
			}
			catch (UnknownHostException x) {
				System.err.println("Host unkown: ");
				System.exit(1);
			} catch (IOException x) {
				System.err.println("Couldn't get I/O for the connection to ");
				System.exit(1);
			}

			int r= (int)(Math.random()*wordBank.size());
			word = wordBank.get(r);

			reset = true;
			playerPoints++;
			repaint();
		}
	}

	public void poll() throws IOException {
		int portNumber = 1024;

		ServerSocket serverSocket = new ServerSocket(portNumber);
		Socket clientSocket = serverSocket.accept();

		try {
			outObject = new ObjectOutputStream(clientSocket.getOutputStream());
			pin = new PushbackInputStream(clientSocket.getInputStream());
			 inObject = new ObjectInputStream (clientSocket.getInputStream());
			System.out.println("connected");
			chats = (String) inObject.readObject();
			chatPane.setText(chats);
			guess = (String)inObject.readObject();
			reset();



			 while (true){
					chats = (String) inObject.readObject();
					chatPane.setText(chats);
					guess = (String)inObject.readObject();
					reset();
					repaint();
			}
			// }


		} catch (IOException e) {
          System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
          System.out.println(e.getMessage());
    }catch(ClassNotFoundException e){
				System.err.println("ction to ");
				System.exit(1);
		}
	}

	public int getGridX(int mouseX){
		int val = (int)((mouseX-10)/15);
		return val;
	}

	public int getGridY(int mouseY){
		int val = (int)((mouseY-10)/15);
		return val;
	}
	public int[] getRGB(int rgb){
		int[] rgbArray = new int[3];
		int rgbNumber = rgb;
		rgbArray[0]   = (rgbNumber & 0x00ff0000) >> 16;
		rgbArray[1] = (rgbNumber & 0x0000ff00) >> 8;
		rgbArray[2]  =  rgbNumber & 0x000000ff;
		return rgbArray;
	}


	public void mouseMoved(MouseEvent e){}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseClicked(MouseEvent e) {}

	public void mousePressed(MouseEvent e){
		//Print location of x and y


		if(e.getX() >= 555 && e.getX() <= 785 && e.getY() >= 10 && e.getY() <= 270){
			int p = colorPalette.getRGB(e.getX()-555,e.getY()-10);
			int[] array = getRGB(p);
			color = new Color(array[0], array[1], array[2]);
		}

		if(e.getX() >= 10 && e.getX() <= 550 && e.getY() >= 10 && e.getY() <= 580){
			row = getGridX(e.getX());
			column = getGridY(e.getY());

			//bad code apparently? :(
			for(int i = 0; i<gridMap.getKeys().size(); i++){
				Location currentKey = gridMap.getKeys().get(i);
				if(currentKey.getX() / 15 == row && currentKey.getY() / 15 == column){
					gridMap.get(currentKey).setColor(color);
					break;
				}
			}
		}
		//System.out.println(wordBank.size());

		try {
			outObject.reset();
			outObject.writeObject(gridMap.toString());
		}
		catch (UnknownHostException x) {
			System.err.println("Host unkown: ");
			System.exit(1);
		} catch (IOException x) {
			System.err.println("Couldn't get I/O for the connection to ");
			System.exit(1);
		}
		repaint();
	}

	public void mouseDragged(MouseEvent e){
		if(e.getX() >= 555 && e.getX() <= 785 && e.getY() >= 10 && e.getY() <= 270){
			int p = colorPalette.getRGB(e.getX()-555,e.getY()-10);
			int[] array = getRGB(p);
			color = new Color(array[0], array[1], array[2]);
		}
		if(e.getX() >= 10 && e.getX() <= 550 && e.getY() >= 10 && e.getY() <= 580){
			row = getGridX(e.getX());
			column = getGridY(e.getY());

			for(int i = 0; i<gridMap.getKeys().size(); i++){
				Location currentKey = gridMap.getKeys().get(i);
				if(currentKey.getX() / 15 == row && currentKey.getY() / 15 == column){
					gridMap.get(currentKey).setColor(color);
					break;
				}
			}
		}

		try {
			outObject.reset();
			outObject.writeObject(gridMap.toString());
		}
		catch (UnknownHostException x) {
			System.err.println("Host unkown: ");
			System.exit(1);
		} catch (IOException x) {
			System.err.println("Couldn't get I/O for the connection to ");
			System.exit(1);
		}
		repaint();

	}



}
