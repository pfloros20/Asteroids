import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.util.ArrayList;
public class Asteroid{
	private int x,y;
	private double vx=0,vy=0;
	private static final int SPEED=8;
	private double angle=0;
	private static int delay=0;
	private double radius;
	private boolean splitted=false;
	private double multiplier=1;

	public Asteroid(int x,int y,double angle){
		
			this.x=x;
			this.y=y;
			this.angle=angle;
			this.radius=37;
		
	}
	public Asteroid(int x,int y,double angle,double multiplier,boolean splitted){
		this.x=x;
		this.y=y;
		this.angle=angle;
		this.multiplier=multiplier;
		radius=18;
		this.splitted=splitted;
	}
	public void move(){
		if(angle>(2*Math.PI)) //Keep angle within bounds of 0 to 2*PI
			angle-=(2*Math.PI); 
		else if(angle<0) 
			angle+=(2*Math.PI); 
		
		vx=SPEED*Math.cos(angle); 
		vy=SPEED*Math.sin(angle);  
		      
		y=(int)(y+vy);
		x=(int)(x+vx);
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
		int xPoint[]=new int[12];//{(int)(14*Math.cos(angle)+x+0.5),(int)(20*Math.cos(angle)+x+0.5)};
		int yPoint[]=new int[12];//{(int)(14*Math.sin(angle)+y+0.5),(int)(20*Math.sin(angle)+y+0.5)};
		int xOrigin[]={40, 40, 25, 15,  0, -5,-15,-25,-30,-35,15, 15};
		int yOrigin[]={10,-15,-30,-35, -5,-45,-40,-15, 10, 25,35,  0};
		if(multiplier!=1)
			for(int i=0;i<xOrigin.length;i++){
				xOrigin[i]*=multiplier;
				yOrigin[i]*=multiplier;
			}
		for(int i=0;i<xPoint.length;i++){
			xPoint[i]=(int)(xOrigin[i]*Math.cos(angle)+x-yOrigin[i]*Math.sin(angle)+0.5);
			yPoint[i]=(int)(xOrigin[i]*Math.sin(angle)+yOrigin[i]*Math.cos(angle)+y+0.5);
		}
		g2.setStroke(new BasicStroke(2));
		g2.drawPolygon(xPoint,yPoint,12);
		//g2.drawOval((int)(x-radius+0.5),(int)(y-radius+0.5),(int)(2*radius),(int)(2*radius)); 
	}
	public static Asteroid createAsteroid(int x,int y,double angle){
		
		Asteroid asteroid=null;
		
		if(delay==0){
			asteroid=new Asteroid(x,y,angle);
			delay=5;
		}else
			delay--;
		
		return asteroid;
	}

	public boolean shipCollision(Ship ship){
		boolean collided=false;
		double xdist=Math.pow((double)(this.x-ship.getx()),2.0);
		double ydist=Math.pow((double)(this.y-ship.gety()),2.0);
		double distance=Math.sqrt(xdist+ydist);
		if(distance<=this.radius+ship.radius)
			collided=true;
		return collided;
	}
	public ArrayList<Asteroid> shotCollision(Shot shot){
		double xdist=Math.pow((double)(x-shot.getx()),2.0);
		double ydist=Math.pow((double)(y-shot.gety()),2.0);
		double distance=Math.sqrt(xdist+ydist);
		if(distance<=radius){
			if(!this.splitted){
				Asteroid first=new Asteroid(x,y,angle-Math.PI/2,0.5,true);
				Asteroid second=new Asteroid(x,y,angle+Math.PI/2,0.5,true);
				ArrayList<Asteroid> list=new ArrayList<Asteroid>();
				list.add(first);
				list.add(second);
				return list;
			}else{
				ArrayList<Asteroid> list=new ArrayList<Asteroid>();
				list.add(null);
				return list;
			}
		}
		return null;		
	}

	public static void restartDelay(){
		delay=0;
	}

}