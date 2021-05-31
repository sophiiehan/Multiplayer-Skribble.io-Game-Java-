import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.io.*;
import java.net.*;

public class ClientScreen extends JPanel implements ActionListener{

	private MyHashMap<Location, Tile> gridMap;
	private String chats;
	private PrintWriter out;
  private JTextField textInput;
	private JButton sendButton;
	private JButton newGameButton;
	private String newGameText;
	private boolean pressed;
	private String gridString;
	private int points;

	private int row;
	private int column;
	private Color color;
	private boolean reset;
	private BufferedImage colorPalette;
	private BufferedImage savedImage;

	private JButton saveImageButton;
	private JButton clearButton;
	private boolean imageSaved;
	// private PrintWriter out;

  private JTextPane chatPane;

	private ObjectOutputStream outObject;
	private ObjectInputStream inObject;

	public ClientScreen(){
		newGameText = "";
		reset = false;
		points = 0;
		pressed = false;
		chats = "";
		this.setLayout(null);

		chatPane = new JTextPane();
		chatPane.setBounds(558,10,224,400);
    add(chatPane);
		chatPane.setText(chats);

		textInput = new JTextField();
		textInput.setBounds(558,420, 224, 30);
		this.add(textInput);
		textInput.addActionListener(this);

		gridMap = new MyHashMap<Location, Tile>();
		for(int r = 10; r<540;r+=15){
			for(int c = 10; c<510; c+=15){
				gridMap.put(new Location(r, c), new Tile(255,255,255));
			}
		}

		sendButton = new JButton("Send");
	 	sendButton.setBounds(558,470, 200, 100);
	 	this.add(sendButton);
	 	sendButton.addActionListener(this);

		newGameButton = new JButton("New Game");
		newGameButton.setBounds(400,550, 200, 100);
		this.add(newGameButton);
		newGameButton.addActionListener(this);

		this.setFocusable(true);

	}



	public void paintComponent(Graphics g){
		super.paintComponent(g);
		// g.drawString( chats, 100, 50 );
		if(savedImage == null)
			savedImage = (BufferedImage)(createImage(560, 590));

		Graphics gImage = savedImage.createGraphics();

		g.drawImage(colorPalette,555,8,null);
		int x = 10;
		int y = 10;

		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.drawString("Points: "+ points, 100,550);

		for(int i = 0; i<gridMap.getKeys().size(); i++){
			Location l = gridMap.getKeys().get(i);
			gridMap.get(l).drawMe(g, l.getX(), l.getY());
		}
	}

	public Dimension getPreferredSize() {
		return new Dimension(800,600);
	}

	public void reset(){}

	public void poll() throws IOException{

		String hostName = "localhost";
		int portNumber = 1024;
		Socket serverSocket = new Socket(hostName, portNumber);

		//listens for inputs
		try {
			outObject = new ObjectOutputStream(serverSocket.getOutputStream());
		 	inObject= new ObjectInputStream (serverSocket.getInputStream());

			gridString = (String)inObject.readObject();
			gridMap = makeGrid();
			 reset = Boolean.parseBoolean((String)inObject.readObject());
			 System.out.println("reset");
			repaint();

		while(true){
		 	gridString = (String)inObject.readObject();
			gridMap = makeGrid();
			 reset = Boolean.parseBoolean((String)inObject.readObject());
			 System.out.println("reset");
			repaint();
			 }


			}
	 	catch (UnknownHostException e) {
			System.err.println("Host unkown: " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + hostName);
			System.err.println(e);
			System.exit(1);
		}catch(ClassNotFoundException e){
				System.err.println("ction to ");
				System.exit(1);
		}
	}
	public MyHashMap<Location,Tile> makeGrid(){
		MyHashMap<Location,Tile> map = new MyHashMap<Location,Tile>();
		String[] divideStringbyLine = gridString.split("_");
		String[] locations = new String[divideStringbyLine.length];
		String[] colors = new String[divideStringbyLine.length];

		for(int i = 0; i<divideStringbyLine.length;i++){
			String[] temp = divideStringbyLine[i].split("-");
			locations[i] = temp[0];
			colors[i] = temp[1];
		}

		for(int i = 0; i<locations.length;i++){
			String[] locations2 = locations[i].split("\\.");

			//System.out.println(locations2[0]);
			int x = Integer.parseInt(locations2[0]);
			int y = Integer.parseInt(locations2[1]);

			String[] colors2 = colors[i].split("\\.");
		//	System.out.println(colors2[0]);
			int r = Integer.parseInt(colors2[0]);
			int g = Integer.parseInt(colors2[1]);
			int b = Integer.parseInt(colors2[2]);

			map.put(new Location(x,y), new Tile(r,g,b));
		}
		return map;

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == sendButton) {
			String sendText = textInput.getText();
      chats += "Player: " + sendText + "\n";
			//gridMap = makeGrid();

			try {
				chatPane.setText(chats);
				//outObject.reset();
				outObject.writeObject(chats);
				outObject.writeObject(sendText);
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
}
