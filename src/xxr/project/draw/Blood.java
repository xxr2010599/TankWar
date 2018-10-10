package xxr.project.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Blood {
	private int x,y,w = 10,h = 10;
	private TankClient tc;
	private boolean live = true;
	
	private int step=0;
	private int[][] dirs = {{350,270},{370,300},{370,350},{340,330},{330,300}};
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	public TankClient getTc() {
		return tc;
	}
	public void setTc(TankClient tc) {
		this.tc = tc;
	}
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	
	public void draw(Graphics g)
	{
		if(!this.live) return;
		Color c = g.getColor();
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, w, h);
		g.setColor(c);
		
		move();
	}
	public Rectangle getRect()
	{
		return new Rectangle(x, y, w, h);
	}
	public void move()
	{
		
		if(step>4)//4应该用常量代替
			step = 0;
		x = dirs[step][0];
		y = dirs[step][1];
		step++;
	}
}
