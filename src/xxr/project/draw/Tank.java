package xxr.project.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

public class Tank {
	private int x;
	private int y;
	private int oldX;
	private int oldY;
	private BloodBar bb = new BloodBar();
	
	public static int TANK_BLOOD = 100;//坦克满血的血量
	
	public TankClient tc;
	private boolean good;//判断坦克是我方还是敌方
	public boolean isGood() {
		return good;
	}



	public void setGood(boolean good) {
		this.good = good;
	}

	private boolean life = true;//判断坦克的生死
	private int blood = 100;//坦克实际的血量
	
	public int getBlood() {
		return blood;
	}



	public void setBlood(int blood) {
		this.blood = blood;
	}



	public boolean isLife() {
		return life;
	}



	public void setLife(boolean life) {
		this.life = life;
	}

	public final static int TANK_X_SPEED = 5;//水平方向的坦克的运动速度
	public final static int TANK_Y_SPEED = 5;
	public final static int WIDTH = 30;//坦克的宽度和高度
	public final static int HEIGHT = 30;
	
	public boolean bU=false,bR=false,bD=false,bL=false;//定义4个方向的Boolean型的按键是否被按下

	public enum Direction {U,UR,R,DR,D,DL,L,UL,STOP};//用枚举类型定义8个方向
	public Direction dir=Direction.STOP;//坦克的方向
	public Direction ptDir = Direction.R;//炮筒的方向
	
	public static Random rm = new Random();
	private int step = rm.nextInt(12)+3;
	
	public Tank(int x,int y)
	{
		this.x =x ;
		this.y = y;
	}
	
	
	
	public Tank(int x, int y, boolean good,Direction dir,TankClient tc) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.dir = dir;
		this.good = good;
		this.tc = tc;
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
	
	
	public void draw(Graphics g)
	{
		if(!this.life)
		{
			if(!this.good)
				tc.tanks.remove(this);
			return;
		}
		Color c = g.getColor();
		if(good)
		{
			bb.draw(g);
			g.setColor(Color.RED);
		}
			
		else
			g.setColor(Color.BLUE);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		
		
		if(dir!=Direction.STOP)
		{
			ptDir = dir;
		}
		g.setColor(Color.WHITE);
		switch(ptDir)
		{
		case U:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x+WIDTH/2, y);
			break;
		case UR:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x+WIDTH, y);
			break;
		case R:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x+WIDTH, y+HEIGHT/2);
			break;
		case DR:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x+WIDTH, y+HEIGHT);
			break;
		case D:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x+WIDTH/2, y+HEIGHT);
			break;
		case DL:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x, y+HEIGHT);
			break;
		case L:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x, y+HEIGHT/2);
			break;
		case UL:
			g.drawLine(x+WIDTH/2, y+HEIGHT/2, x, y);
			break;
		}
		g.setColor(c);
		move();
	}

	

	private void move() {//坦克移动
		
		oldX = x;
		oldY = y;
		
		switch(dir)
		{
		case U:
			y -= TANK_Y_SPEED;
			break;
		case UR:
			y -= TANK_Y_SPEED;
			x += TANK_X_SPEED;
			break;
		case R:
			x += TANK_X_SPEED;
			break;
		case DR:
			y += TANK_Y_SPEED;
			x += TANK_X_SPEED;
			break;
		case D:
			y += TANK_Y_SPEED;
			break;
		case DL:
			y += TANK_Y_SPEED;
			x -= TANK_X_SPEED;
			break;
		case L:
			x -= TANK_X_SPEED;
			break;
		case UL:
			y -= TANK_Y_SPEED;
			x -= TANK_X_SPEED;
			break;
		case STOP:
			break;	
		}
		if(x<0) x= 0;
		if(y<30) y= 30;
		if(x>TankClient.GAME_WIDTH-Tank.WIDTH) x=TankClient.GAME_WIDTH-Tank.WIDTH;
		if(y>TankClient.GAME_HEIGHT-Tank.HEIGHT) y= TankClient.GAME_HEIGHT-Tank.HEIGHT;
		
		
		if(!good)//如果是敌人的坦克，让它随机动
		{
			Direction[] dirs = dir.values();//将枚举转化为数组，不然没法用
			if(step==0)
			{
				int randomNum = rm.nextInt(dirs.length);
				this.dir = dirs[randomNum];
				step = rm.nextInt(12)+3;
			}
			step--;
			int randomFireNum = rm.nextInt(40);
			if(randomFireNum >=38)//控制敌人发射子弹的频率
				fire();
		}	

	}

	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode();
		
		switch(key)
		{
		case KeyEvent.VK_CONTROL:
			fire();
			break;
		case KeyEvent.VK_RIGHT:
			bR=true;
			break;
		case KeyEvent.VK_LEFT:
			bL=true;
			break;
		case KeyEvent.VK_DOWN:
			bD=true;
			break;
		case KeyEvent.VK_UP:
			bU=true;
			break;
		
		}
		
		direction();
		
	}
	
	private void superFire() {
		// TODO Auto-generated method stub
		Direction[] dirs = dir.values();
		for(int i=0;i<dirs.length-1;i++)
		{
			fire(dirs[i]);
		}
	}



	private Missile fire(Direction direction) {
		// TODO Auto-generated method stub
		if(!this.life) return null;
		int x = this.x+WIDTH/2-Missile.WIDTH/2;
		int y = this.y+HEIGHT/2-Missile.HEIGHT/2;
		
		Missile m = new Missile(x,y,this.good,direction,this.tc);
		this.tc.missiles.add(m);
		return m;
	}



	private Missile fire() {
		if(!this.life) return null;//哎。
				
		// TODO Auto-generated method stub
		int x = this.x+WIDTH/2-Missile.WIDTH/2;
		int y = this.y+HEIGHT/2-Missile.HEIGHT/2;
		
		Missile m = new Missile(x,y,this.good,ptDir,this.tc);
		this.tc.missiles.add(m);
		return m;
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		
		switch(key)
		{
		case KeyEvent.VK_RIGHT:
			bR=false;
			break;
		case KeyEvent.VK_LEFT:
			bL=false;
			break;
		case KeyEvent.VK_DOWN:
			bD=false;
			break;
		case KeyEvent.VK_UP:
			bU=false;
			break;
		case KeyEvent.VK_A:
			superFire();
			break;
		case KeyEvent.VK_F2:
			{
				if(this.good && !this.life)
				{
				this.life = true;
				this.blood = 100;
				this.x = 50;
				this.y = 50;
				}
				break;
			}
		}
		
		direction();
	}
	private void direction(){
		if(!bR && !bL && !bD && bU)
			dir = Direction.U;
		else if(bR && !bL && !bD && bU)
			dir = Direction.UR;
		else if(bR && !bL && !bD && !bU)
			dir = Direction.R;
		else if(bR && !bL && bD && !bU)
			dir = Direction.DR;
		else if(!bR && !bL && bD && !bU)
			dir = Direction.D;
		else if(!bR && bL && bD && !bU)
			dir = Direction.DL;
		else if(!bR && bL && !bD && !bU)
			dir = Direction.L;
		else if(!bR && bL && !bD && bU)
			dir = Direction.UL;
		else if(!bR && !bL && !bD && !bU)
			dir = Direction.STOP;
	}

	public Rectangle getRect()
	{
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	public void stay()
	{
		x = oldX;
		y = oldY;
		
	}
	public boolean CollidesWall(Wall w)
	{
		if(this.life && this.getRect().intersects(w.getRect()))
		{
//			this.dir = Direction.STOP;//如果这样写，坦克直接沾到墙上了
			stay();
			return true;
		}
		return false;
	}
	public boolean CollideTanks(List<Tank> tanks)
	{
		for(int i=0;i<tanks.size();i++)
		{
			Tank t = tanks.get(i);
			if(this != t)
			{
				if(this.life && t.life && this.getRect().intersects(t.getRect()))
				{
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean eatBlood(Blood b)
	{
		if(this.life && b.isLive() && this.getRect().intersects(b.getRect()))
		{
			b.setLive(false);
			this.blood = TANK_BLOOD;//这里应该定义为常量。
			return true;
		}
		return false;
	}
	
	private class BloodBar
	{
		public void draw(Graphics p)
		{
			Color c = p.getColor();
			p.setColor(Color.RED);
			p.drawRect(x, y-10, WIDTH, 10);
			int w = WIDTH * blood/100;
			p.fillRect(x, y-10, w, 10);
			p.setColor(c);
			
		}
	}
}
