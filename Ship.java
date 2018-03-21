import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.util.ArrayList;
public class Ship{
	private int x,y;
	private static final double VELOCITY_DECAY=0.6;
	private static final int ACCELERATION=2;
	public boolean accelerating=false;
	public boolean turningRight=false;
	public boolean turningLeft=false;
	private double vx=0,vy=0;
	private double angle=-Math.PI/2;
	private double rotationalSpeed=0.20;
	protected ArrayList<Shot> bullets=new ArrayList<Shot>();
	public int radius=10;
	public Ship(){
		x=Game.WIDTH/2;
		y=Game.HEIGHT/2;
	}
	public void move(){
		if(turningLeft) //this is backwards from typical polar coordinates
			angle-=rotationalSpeed; //because positive y is downward.
		if(turningRight) //Because of that, adding to the angle is
			angle+=rotationalSpeed; //rotating clockwise (to the right)
		if(angle>(2*Math.PI)) //Keep angle within bounds of 0 to 2*PI
			angle-=(2*Math.PI); 
		else if(angle<0) 
			angle+=(2*Math.PI); 
		if(accelerating){ //adds accel to velocity in direction pointed
			//calculates components of accel and adds them to velocity
			vx+=ACCELERATION*Math.cos(angle); 
			vy+=ACCELERATION*Math.sin(angle);  
		}      
		y=(int)(y+vy);
		x=(int)(x+vx);
		vx*=VELOCITY_DECAY;
		vy*=VELOCITY_DECAY;
		
		
		
		if(x>=Game.WIDTH)
			x=1;
		if(x<=0)
			x=Game.WIDTH;
		if(y>=Game.HEIGHT)
			y=1;
		if(y<=0)
			y=Game.HEIGHT;
	}
	public void draw(Graphics g){
		Graphics2D g2=(Graphics2D) g;
		int xPoint[]={(int)(14*Math.cos(angle)+x+0.5),(int)(-10*Math.cos(angle)+8*Math.sin(angle)+x+0.5),(int)(-10*Math.cos(angle)-8*Math.sin(angle)+x+0.5)};
		int yPoint[]={(int)(14*Math.sin(angle)+y+0.5),(int)(-10*Math.sin(angle)-8*Math.cos(angle)+y+0.5),(int)(-10*Math.sin(angle)+8*Math.cos(angle)+y+0.5)};
		g2.setStroke(new BasicStroke(2));
		g2.drawPolygon(xPoint,yPoint,3);
	}
	public void zeroAcceleration(){
		vy=0;
		vx=0;
	}
	public void shoot(){
		Shot shot=new Shot(x,y,angle);
		shot.move();
		bullets.add(shot);
	}
	public int getx(){return x;}
	public int gety(){return y;}
}
