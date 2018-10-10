package xxr.project.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Wall {
	private int x,y,w,h;//墙的坐标和宽度高度
	TankClient tc;
	public Wall(int x, int y, int w, int h, TankClient tc) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tc = tc;
	}
	public void draw(Graphics g)
	{
		Color c = g.getColor();
		g.setColor(Color.GRAY);
		g.fillRect(x, y, w, h);
		g.setColor(c);
	}
	public Rectangle getRect()
	{
		return new Rectangle(x, y, w, h);
	}
}
