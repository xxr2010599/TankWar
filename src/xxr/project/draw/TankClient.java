package xxr.project.draw;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import xxr.project.draw.Tank.Direction;

public class TankClient extends Frame {
	public static final int GAME_WIDTH =800,GAME_HEIGHT = 600;
	Tank myTank = new Tank(50,50,true,Direction.STOP,this);
	
	Wall w1 = new Wall(600,200,30,300,this);
	Wall w2 = new Wall(100,400,400,30,this);
	
	Blood b = new Blood();
	
	List<Tank> tanks = new ArrayList<Tank>();
	List<Missile> missiles = new ArrayList<Missile>();
	
	List<Explode> explodes = new ArrayList<Explode>();
	
	Image offScreenImage = null;
	@Override
	public void paint(Graphics g) {//�ع��Ļ�ͼ
		g.drawString("fire count:"+missiles.size(), 10, 45);
		g.drawString("explode count:"+explodes.size(), 80, 45);
		g.drawString("tanks count:"+tanks.size(), 200, 45);
		
		g.drawString("mytank blood:"+myTank.getBlood(), 400, 45);
		myTank.draw(g);
		
		myTank.CollidesWall(w1);
		myTank.CollidesWall(w2);
		myTank.CollideTanks(tanks);
		myTank.eatBlood(b);
		
		w1.draw(g);
		w2.draw(g);
		
		b.draw(g);
		
		if(tanks.size()==0)
		{
			for(int i=0;i<10;i++)
			{
				tanks.add(new Tank(50+40*(i+1),50,false,Direction.STOP,this));
			}
		}
		
		for(int i=0;i<tanks.size();i++)
		{
			Tank tank = tanks.get(i);
			tank.CollidesWall(w1);
			tank.CollidesWall(w2);
			tank.CollideTanks(tanks);
			tank.draw(g);
		}
		
		for(int i =0;i<missiles.size();i++)
		{
//			if(!missiles.get(i).isLive())
//			{
//				missiles.remove(i);
//			}
//			else
				Missile m = missiles.get(i);
				m.isHit(myTank);//��һ������Ҫ��ע�Ⱑ���������ķŵ�������,����hitTanksҲӦ�÷ŵ�����
				m.hitTanks(tanks);
				m.draw(g);
				
				m.hitWall(w1);
				m.hitWall(w2);
		}
		
		for(int i=0;i<explodes.size();i++)
		{
			explodes.get(i).draw(g);
		}
		
	}

	@Override
	public void update(Graphics g) {//ͨ��update����ʵ��˫���棬������˸
		if(offScreenImage == null)
		{
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);	
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.BLACK);//���������
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	
	public void launch()
	{
		
		
		
		this.setLocation(400, 300);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		this.setResizable(false);//���ɸı��С
		this.setBackground(Color.GREEN);//���ñ���ɫ//�������û������
		this.setVisible(true);
		
		this.addKeyListener(new KeyMonitor());//������̼����¼�
		new Thread(new PaintThread()).start();//�����ػ��߳�
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TankClient tc = new TankClient();
		tc.launch();
	}
	private class PaintThread implements  Runnable {//�Զ��ػ�
		public void run() {
			while(true)
			{
				repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}

	private class KeyMonitor extends KeyAdapter
	{
		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
		
	}
}
