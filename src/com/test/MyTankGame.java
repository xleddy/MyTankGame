package com.test;
import java.awt.*;
import javax.imageio.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

public class MyTankGame extends JFrame{

	MyPanel mp=null;
	JMenuBar jmb = null;
	JMenu jm1 = null;
	JMenuItem jmi1 = null;
	JMenuItem jmi2 = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyTankGame mtg = new MyTankGame();
	}

	public MyTankGame()
	{
		mp=new MyPanel();
		//设置菜单
		jmb = new JMenuBar();
		jm1 = new JMenu("文件(F)");
		jm1.setMnemonic('F');
		jmi1 = new JMenuItem("打开");
		jmi1.addActionListener(mp);
		jmi1.setActionCommand("open");
		jmi2 = new JMenuItem("保存");
		jmi2.addActionListener(mp);

		Thread t=new Thread(mp);
		t.start();
		this.addKeyListener(mp);
		this.add(mp);
		//加入菜单
		this.setJMenuBar(jmb);
		jmb.add(jm1);
		jm1.add(jmi1);
		jm1.add(jmi2);

		this.setSize(415, 335);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}

class MyPanel extends JPanel implements KeyListener,Runnable,ActionListener
{
	Vector<Hero> heros = new Vector<Hero>();
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	Vector<bomb> bb=new Vector<bomb>();
	int enSize=3;
	//初始化图片
	Image image1=null;
	Image image2=null;
	Image image3=null;
	Image image4=null;
	Image image5=null;

	public MyPanel()
	{
		//自己
		Hero hero = new Hero(100,100,"u",1,"c");
		heros.add(hero);
		//敌人
		for(int i=0;i<enSize;i++)
		{
			EnemyTank et = new EnemyTank((i+1)*50,0,"d",1,"y");
			Bullet etb = new Bullet(et.getX(),et.getY(),et.getDirect());
			et.ss.add(etb);
			Thread t1 = new Thread(et);
			t1.start();
			Thread t2 = new Thread(etb);
			t2.start();
			ets.add(et);
		}

		//		try {
		//			image1 = ImageIO.read(new File("/1.png"));
		//			image2 = ImageIO.read(new File("/2.png"));
		//			image3 = ImageIO.read(new File("/3.png"));
		//			image4 = ImageIO.read(new File("/4.png"));
		//			image5 = ImageIO.read(new File("/5.png"));
		//		} catch (IOException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//准备爆炸图片
		image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/1.png"));
		image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/2.png"));
		image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/3.png"));
		image4=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/4.png"));
		image5=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/5.png"));
	}

	public void paint(Graphics g)
	{
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		//画自己
		for(int i=0;i<heros.size();i++){
			Hero hero = heros.get(i);
			if(hero.isLive==true)
			{
				this.drawTank(hero.getX(), hero.getY(), g,hero.getDirect(), hero.getColor());
			}else if(hero.isLive==false){
				heros.remove(hero);
			}
		}
		//画自己子弹
		for(int i=0;i<heros.size();i++){
			Hero hero = heros.get(i);
			for(int j=0;j<hero.ss.size();j++)
			{
				Bullet bullet = hero.ss.get(j);
				if(bullet!=null&&bullet.isLive==true)
				{
					g.draw3DRect(bullet.x, bullet.y, 1, 1, false);
				}
				else if(bullet.isLive==false) hero.ss.remove(bullet);
			}
		}
		//画敌人
		for(int i=0;i<ets.size();i++)
		{
			EnemyTank et = ets.get(i);

			if(et.isLive==true)
			{
				this.drawTank(ets.get(i).getX(), ets.get(i).getY(), g, ets.get(i).getDirect(), ets.get(i).getColor());

			}else if(et.isLive==false)
			{
				ets.remove(et);
			}
		}
		//画敌人子弹
		for(int i=0;i<ets.size();i++)
		{
			EnemyTank et = ets.get(i);

			for(int j=0;j<et.ss.size();j++)
			{
				Bullet bullet = et.ss.get(j);
				if(bullet!=null&&bullet.isLive==true)
				{
					g.draw3DRect(bullet.x, bullet.y, 1, 1, false);
				}
				else if(bullet.isLive==false) et.ss.remove(bullet);
			}
		}
		//画出爆炸

		for(int i=0;i<bb.size();i++)
		{

			bomb b = bb.get(i);

			if(b.life>8) g.drawImage(image1,b.x,b.y,30,30,this);
			else if(b.life>6) g.drawImage(image2,b.x,b.y,30,30,this);
			else if(b.life>4) g.drawImage(image3,b.x,b.y,30,30,this);
			else if(b.life>2) g.drawImage(image4,b.x,b.y,30,30,this);
			else if(b.life>0) g.drawImage(image5,b.x,b.y,30,30,this);
			b.lifedown();
			if(b.life==0)
			{
				b.isLive=false;
				bb.remove(b);
			}		

		}

		//击中敌人坦克
		for(int k=0;k<heros.size();k++)
		{
			Hero hero = heros.get(k);
			for(int i=0;i<hero.ss.size();i++)
			{
				for(int j=0;j<ets.size();j++)
				{
					Bullet bullet = hero.ss.get(i);
					EnemyTank et = ets.get(j);
					hitTank(bullet,et);
				}
			}
		}

		//击中自己坦克
		for(int i=0;i<ets.size();i++)
		{
			EnemyTank et = ets.get(i);
			for(int j=0;j<et.ss.size();j++)
			{
				Bullet bullet = et.ss.get(j);
				for(int k=0;k<heros.size();k++)
				{
					Hero hero = heros.get(k);
					hitTank(bullet,hero);
				}
			}
		}
	}

	//判断是否击中
	public void hitTank(Bullet bullet,Tank et)
	{
		//判断坦克方向
		if(et.direct.equals("u")||et.direct.equals("d"))
		{
			if(bullet.x<et.getX()+20&&bullet.x>et.getX()&&bullet.y<et.getY()+30&&bullet.y>et.getY())
			{

				bullet.isLive=false;
				et.isLive=false;
				bomb b=new bomb(et.getX(),et.getY());
				bb.add(b);

			}
		}else if(et.direct.equals("l")||et.direct.equals("r"))
		{
			if(bullet.x<et.getX()+30&&bullet.x>et.getX()&&bullet.y<et.getY()+20&&bullet.y>et.getY())
			{

				bullet.isLive=false;
				et.isLive=false;
				bomb b=new bomb(et.getX(),et.getY());
				bb.add(b);
			}
		}

	}

	public void drawTank(int x,int y,Graphics g,String d,String color){
		//the type of the tank
		if(color=="c")
			g.setColor(Color.cyan);
		else if(color=="y")
			g.setColor(Color.yellow);

		//the direct of the tank
		if(d.equals("u"))
		{
			g.fill3DRect(x, y, 5, 30,false);
			g.fill3DRect(x+15, y, 5, 30,false);
			g.fill3DRect(x+5, y+5, 10, 20,false);
			g.fillOval(x+4, y+8, 10, 10);
			g.drawLine(x+9, y, x+9, y+10);
		}else if(d.equals("d"))
		{
			g.fill3DRect(x, y, 5, 30,false);
			g.fill3DRect(x+15, y, 5, 30,false);
			g.fill3DRect(x+5, y+5, 10, 20,false);
			g.fillOval(x+4, y+8, 10, 10);
			g.drawLine(x+9, y+30, x+9, y+10);
		}else if(d.equals("l"))
		{
			g.fill3DRect(x-5, y+5, 30, 5,false);
			g.fill3DRect(x-5, y+20, 30, 5,false);
			g.fill3DRect(x, y+10, 20, 10,false);
			g.fillOval(x+5, y+9, 10, 10);
			g.drawLine(x-5, y+14, x+9, y+14);
		}else if(d.equals("r"))
		{
			g.fill3DRect(x-5, y+5, 30, 5,false);
			g.fill3DRect(x-5, y+20, 30, 5,false);
			g.fill3DRect(x, y+10, 20, 10,false);
			g.fillOval(x+5, y+9, 10, 10);
			g.drawLine(x+25, y+14, x+9, y+14);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		for(int k=0;k<heros.size();k++)
		{
			Hero hero = heros.get(k);
			if(e.getKeyCode()==KeyEvent.VK_DOWN||e. getKeyCode()==KeyEvent.VK_S)
			{
				hero.movedown();
				hero.direct="d";
			}else if(e.getKeyCode()==KeyEvent.VK_UP||e.getKeyCode()==KeyEvent.VK_W)
			{
				hero.moveup();
				hero.direct="u";
			}else if(e.getKeyCode()==KeyEvent.VK_LEFT ||e.getKeyCode()==KeyEvent.VK_A)
			{
				hero.moveleft();
				hero.direct="l";
			}else if(e.getKeyCode()==KeyEvent.VK_RIGHT||e.getKeyCode()==KeyEvent.VK_D)
			{
				hero.moveright();
				hero.direct="r";
			}else if(e.getKeyCode()==KeyEvent.VK_ADD)
			{
				hero.setSpeed(hero.getSpeed()+1);
			}
			else if(e.getKeyCode()==KeyEvent.VK_MINUS)
			{
				hero.setSpeed(hero.getSpeed()-1);
			}
			else if(e.getKeyCode()==KeyEvent.VK_F2)
			{

			}
			else if(e.getKeyCode()==KeyEvent.VK_J)
			{
				if(hero.ss.size()<=4)
				{
					hero.Shot();
				}
			}
			this.repaint();
		}
	}
	


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.repaint();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}

