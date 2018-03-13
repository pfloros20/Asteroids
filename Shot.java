import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
public class Shot{
	private int x,y;
	private double vx=0,vy=0;
	private static final int SPEED=8;
	private static final int LIFE_SPAN=96;
	private double angle=0;
	private int life=0;
	public Shot(int x,int y,double angle){
		this.x=x;
		this.y=y;
		this.angle=angle;
		Game.pew.play();
	}
	public boolean move(){
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
		life++;
		if(life>=LIFE_SPAN)
			return true;
		return false;
	}
	public void draw(Graphics g){
		Graphics2D g2=(Graphics2D) g;
		int xPoint[]={(int)(14*Math.cos(angle)+x+0.5),(int)(20*Math.cos(angle)+x+0.5)};
		int yPoint[]={(int)(14*Math.sin(angle)+y+0.5),(int)(20*Math.sin(angle)+y+0.5)};
		g2.setStroke(new BasicStroke(4));
		g2.drawPolygon(xPoint,yPoint,2);
	}
	
	public int getx(){return x;}
	public int gety(){return y;}
}
