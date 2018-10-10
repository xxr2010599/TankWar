package xxr.project.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import xxr.project.draw.Tank.Direction;

public class Missile {
	public final static int POWDER_X_SPEED = 15;//炮弹水平移动的速度
	public final static int POWDER_Y_SPEED = 15;
	
	public final static int WIDTH = 10;//子弹的宽度和高度
	public final static int HEIGHT = 10;
	
	private int x;
	private int y;
	private Tank.Direction dir ;
	
	private TankClient tc;
	private boolean live = true;
	private boolean good ;
	
	
	public boolean isGood() {
		return good;
	}
	public void setGood(boolean good) {
		this.good = good;
	}
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
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
	public Tank.Direction getDir() {
		return dir;
	}
	public void setDir(Tank.Direction dir) {
		this.dir = dir;
	}
	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	
	public Missile(int x, int y, boolean good,Direction dir, TankClient tc) {
		this.x = x;
		this.y = y;
		this.good = good;
		this.dir = dir;
		this.tc = tc;
	}
	public void draw(Graphics g)
	{
		if(!this.live)
		{
			tc.missiles.remove(this);
			return;
		}
		Color c = g.getColor();
		if(good)
			g.setColor(Color.GREEN);
		else
			g.setColor(Color.WHITE);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		
		
		
		move();
	}
	
	private void move() {
		// TODO Auto-generated method stub
		switch(dir)
		{
		case U:
			y -= POWDER_Y_SPEED;
			break;
		case UR:
			y -= POWDER_Y_SPEED;
			x += POWDER_X_SPEED;
			break;
		case R:
			x += POWDER_X_SPEED;
			break;
		case DR:
			y += POWDER_Y_SPEED;
			x += POWDER_X_SPEED;
			break;
		case D:
			y += POWDER_Y_SPEED;
			break;
		case DL:
			y += POWDER_Y_SPEED;
			x -= POWDER_X_SPEED;
			break;
		case L:
			x -= POWDER_X_SPEED;
			break;
		case UL:
			y -= POWDER_Y_SPEED;
			x -= POWDER_X_SPEED;
			break;
		case STOP:
			break;
		}
		
		if(x<0 || y<0 || x>TankClient.GAME_WIDTH || y>TankClient.GAME_HEIGHT)
		{
			live = false;
		}
			
	}
	
	public Rectangle getRect()
	{
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public boolean isHit(Tank t)
	{
		
		if(this.getRect().intersects(t.getRect()) && t.isLife() && this.good != t.isGood() && this.isLive())
		{
			t.setBlood(t.getBlood()-20);
			if(t.isGood())
			{
				if(t.getBlood()<=0)
				{
					t.setLife(false);
				}
			}
			else
				t.setLife(false);
			
			Explode e = new Explode(x,y,tc);
			tc.explodes.add(e);
			this.live=false;
			return true;
		}
		return false;
	}
	
	public boolean hitWall(Wall w)
	{
		if(this.live && this.getRect().intersects(w.getRect()))
		{
			this.live = false;
			return true;
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks)
	{
		for(int i=0;i<tanks.size();i++)
		{
			if(this.isHit(tanks.get(i)))
			{
				this.live=false;
				return true;
			}
		}
		
		return false;
		
	}
	
}
