package xxr.project.draw;

import java.awt.Color;
import java.awt.Graphics;

public class Explode {
	private int x,y;
	private TankClient tc;
	private boolean live = true;
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

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	private int[] dilmeter = {3,7,15,30,10,4};
	private int step = 0;
	
	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics p)
	{
		if(step==dilmeter.length )
		{
//			live = false;
//			step = 0;
			tc.explodes.remove(this);
			return ;
		}
		if(!live)
			return;
		Color c = p.getColor();
		p.setColor(Color.ORANGE);
		p.fillOval(x, y, dilmeter[step], dilmeter[step]);
		step++;
		p.setColor(c);
	}
}
