import javax.swing.JFrame;
import java.awt.Font;
import java.applet.*;
import java.awt.Color;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import java.awt.Graphics;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;import java.net.UnknownHostException;import java.net.InetAddress;
public class Game extends JComponent implements KeyListener, Runnable{
	public static final long serialVersionUID = 1L;
	Ship ship;
	public static final int WIDTH=1280;
	public static final int HEIGHT=700;
	public static int score=0;
	private static int lives=3;	//TO-DO!
	private boolean paused=false;
	private boolean gameOver=false;
	private static boolean registered=false;
	private static String name=null;
	private long last;
	private final long threshold = 160; // 500msec = half second
	public static JFrame window;
	public static Game game;
	private Thread thread=null;
	private static ArrayList<Asteroid> asteroids=new ArrayList<Asteroid>();
	private static boolean spacePressed=false;
	static AudioClip pew;
	AudioClip bg=Applet.newAudioClip(getClass().getResource("Assets/back.wav"));
	AudioClip boom=Applet.newAudioClip(getClass().getResource("Assets/boom.wav"));
	AudioClip rip=Applet.newAudioClip(getClass().getResource("Assets/riplol.wav"));
	public static void main(String args[]){
		game =new Game();
		window=new JFrame("Asteroids Game.");
		name=JOptionPane.showInputDialog(window,"Type username for the leaderboard or nothing to play offline.");
		if(!name.equals("")){
			registered=true;
			Client client=new Client("localhost",93604585+" "+name+" "+score);
			boolean establishedConnection=client.startRunning();
			if(!establishedConnection)
				JOptionPane.showMessageDialog(window,"Please visit the site and get latest version."
						+"\nIf this is the latest version the servers are down,please try again later."
						+"\nScores won't be on the leaderboard.");
		}
		game.createAssets();
	}
	public void run(){
		while(!gameOver){
			if(!paused){
				window.getContentPane().setForeground(Color.WHITE);
				collisionCheck();
				moveAssets();
				repaint();
				try{
				 	Thread.sleep(17);
				}catch(InterruptedException e){}
				if(asteroids.isEmpty()){
					window.getContentPane().setForeground(Color.GREEN);
					gameOver=true;
				}
			}else
				window.getContentPane().setForeground(Color.GRAY);
		}
		if(!asteroids.isEmpty()){
			window.getContentPane().setForeground(Color.RED);
			bg.stop();
			rip.play();
		}
		if(registered&&lives<=0){
			Client client=new Client("localhost",93604585+" "+name+" "+score);
			boolean establishedConnection=client.startRunning();
			if(!establishedConnection)
				JOptionPane.showMessageDialog(window,"Please visit the site and get latest version."
						+"\nIf this is the latest version the servers are down,please try again later."
						+"\nScores won't be on the leaderboard.");
		}
	}
	public void createAssets(){
		pew=Applet.newAudioClip(getClass().getResource("Assets/pew.wav"));
		last = System.currentTimeMillis();
		bg.loop();
		ship=new Ship();
		Random x = new Random();
		//Random y = new Random();
		Random angle = new Random();
		for(int i=0;i<10;i++){
			int xc = x.nextInt(WIDTH-1) + 1;
			//int yc = y.nextInt(HEIGHT-1) + 1;
			double th = angle.nextDouble();
			Asteroid ast=new Asteroid(xc,0,th);
			asteroids.add(ast);
		}
		initComponents();
		thread=new Thread(game);
		thread.start();
	}
	public void destroyAssets(){
		ship=null;
		asteroids.clear();
	}
	public void collisionCheck(){
		for(int j=0;j<asteroids.size();j++)
			for(int i=0;i<ship.bullets.size();i++){
				ArrayList<Asteroid> returned=null;
				if(j<asteroids.size()&&i<ship.bullets.size())
					returned=asteroids.get(j).shotCollision(ship.bullets.get(i));
				if(returned!=null&&returned.size()==2){
					asteroids.add(returned.get(0));
					asteroids.add(returned.get(1));
					asteroids.remove(asteroids.get(j));
					ship.bullets.remove(i);
					score+=50;
					boom.play();
				}else if(returned!=null&&returned.size()==1){
					asteroids.remove(asteroids.get(j));
					ship.bullets.remove(i);
					score+=75;
					boom.play();
					Random x = new Random();
					Random angle = new Random();	//Create another Asteroid.
					int xc = x.nextInt(WIDTH-1) + 1;
					double th = angle.nextDouble();
					Asteroid ast=new Asteroid(xc,0,th);
					asteroids.add(ast);
				}
			}
		for(int i=0;i<asteroids.size();i++)
			if(asteroids.get(i).shipCollision(ship)){
				lives--;
				gameOver=true;
			}
	}
	public void moveAssets(){
		for(int i=0;i<ship.bullets.size();i++)
			if(ship.bullets.get(i).move())
					ship.bullets.remove(i);
		for(int i=0;i<asteroids.size();i++)
			asteroids.get(i).move();
		ship.move();
	}
	public void initComponents(){
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(WIDTH,HEIGHT);
		window.setResizable(false);
		window.getContentPane().setBackground(Color.BLACK);
		window.getContentPane().setForeground(Color.WHITE);
		window.add(game);
		window.addKeyListener(game);
		window.setVisible(true);
	}
	public void paintComponent(Graphics g){super.paintComponent(g);	
		g.setFont(new Font("TimesRoman",Font.PLAIN,20));
		g.drawString("Score: "+score,5,20);
		g.drawString("Lives: "+lives,5,40);
		ship.draw(g);
		for(int i=0;i<ship.bullets.size();i++)
			ship.bullets.get(i).draw(g);
		for(int i=0;i<asteroids.size();i++)
			asteroids.get(i).draw(g);
	}
	public void keyReleased(KeyEvent e){
		int key=e.getKeyCode();
		if(!paused&&!gameOver){
			if (key == KeyEvent.VK_UP){
				ship.zeroAcceleration();
				ship.accelerating=false;
			}
			if (key == KeyEvent.VK_LEFT)
				ship.turningLeft=false;
			if(key == KeyEvent.VK_RIGHT) 
				ship.turningRight=false;
	    	}
    	}
	public void keyPressed(KeyEvent e){
		int key=e.getKeyCode();
		if(!paused&&!gameOver){
			if (key == KeyEvent.VK_UP)
				ship.accelerating=true;
			else if (key == KeyEvent.VK_LEFT)
				ship.turningLeft=true;
			else if (key == KeyEvent.VK_RIGHT)
				ship.turningRight=true;
	      		else if (key == KeyEvent.VK_SPACE){
				long now = System.currentTimeMillis();			
				if(now-last>=threshold){
					ship.shoot();
					last=now;
					spacePressed=true;
				}
			}
		}
		if(!gameOver)
			if (key ==KeyEvent.VK_P){
				if(!paused)
					paused=true;
				else
					paused=false;
			}
       		if (key ==KeyEvent.VK_ENTER){
			if(lives<=0){
				lives=3;
				score=0;
			}
			if(gameOver){
				gameOver=false;
				game.destroyAssets();
				game.createAssets();
			}
        	}
	}
	public void keyTyped(KeyEvent e){
	}

}
