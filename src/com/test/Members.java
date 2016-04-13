package com.test;
import java.util.Vector;

public class Members 
{

}
class Tank
{
	int x,y;
	String direct=null;
	int speed;
	String color=null;
	boolean isLive=true;
	Vector<Bullet> ss=new Vector<Bullet>();

	public String getDirect() {
		return direct;
	}
	public void setDirect(String direct) {
		this.direct = direct;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
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

	public void Shot()
	{
		Bullet bullet = null;

		if(this.direct.equals("u"))
		{
			bullet = new Bullet(x+9,y,"u");
			ss.add(bullet);
		}
		else if(this.direct.equals("d"))
		{
			bullet = new Bullet(x+9,y+30,"d");
			ss.add(bullet);
		}
		else if(this.direct.equals("l"))
		{
			bullet = new Bullet(x-5,y+14,"l");
			ss.add(bullet);
		}
		else if(this.direct.equals("r"))
		{
			bullet = new Bullet(x+25,y+14,"r");
			ss.add(bullet);
		}
		Thread t=new Thread(bullet);
		t.start();
	}
	public Tank(int x,int y,String direct,int speed,String color)
	{
		this.x=x;
		this.y=y;
		this.direct=direct;
		this.speed=speed;
		this.color=color;
	}
	public void moveup()
	{
		if(y>0)
		{
			this.y-=this.speed;
		}
		/*		System.out.print(y);*/
	}
	public void movedown()
	{
		if(y<265)
		{
			this.y+=this.speed;
		}
	}
	public void moveright()
	{
		if(x<375)
		{
			this.x+=this.speed;
		}
	}
	public void moveleft()
	{
		if(x>8)
		{
			this.x-=this.speed;
		}
	}
}

class Hero extends Tank
{
	public Hero(int x,int y,String direct,int speed,String color)
	{
		super(x,y,direct,speed,color);
	}	
}

class EnemyTank extends Tank implements Runnable
{

	public EnemyTank(int x,int y,String direct,int speed,String color)
	{
		super(x,y,direct,speed,color);
	}
	

	@Override
	public void run() {

		while(true)
		{
			int r = (int)(Math.random()*4)%4;
			int k = (int)(Math.random()*30);

			switch(r)
			{
			case 0:
				this.direct="u";
				for(int i=0;i<k;i++)
				{
					this.moveup();
					int etb =(int)(Math.random()*88)%88;
					if(etb==8&&this.ss.size()<4) this.Shot();

					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	

				}
				break;
			case 1:
				this.direct="d";			
				for(int i=0;i<k;i++)
				{
					this.movedown();
					int etb =(int)(Math.random()*88)%88;
					if(etb==8&&this.ss.size()<4) this.Shot();

					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
				}			
				break;
			case 2:
				this.direct="l";
				for(int i=0;i<k;i++)
				{
					this.moveleft();
					int etb =(int)(Math.random()*88)%88;
					if(etb==8&&this.ss.size()<4) this.Shot();

					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}	
				break;
			case 3:
				this.direct="r";
				for(int i=0;i<k;i++)
				{
					this.moveright();
					int etb =(int)(Math.random()*88)%88;
					if(etb==8&&this.ss.size()<4) this.Shot();

					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		
				}				
				break;
			}
			if(this.isLive==false)
			{
				//坦克死亡后，退出线程
				break;
			}
			
			//敌人发子弹
//			int etb =(int)(Math.random()*3)%3;
//			if(etb==2) this.Shot();
		}
	}
}

class Bullet implements Runnable
{
	int x=0;
	int y=0;
	String direct=null;
	int speed=2;
	boolean isLive=true;

	public Bullet(int x,int y,String direct)
	{
		this.x=x;
		this.y=y;
		this.direct=direct;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int d = 0;
		if(this.direct.equals("u"))
		{
			d=0;
		}else if(this.direct.equals("d"))
		{
			d=1;
		}else if(this.direct.equals("l"))
		{
			d=2;
		}else if(this.direct.equals("r"))
		{
			d=3;
		}

		while(true)
		{
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch(d)
			{
			case 0:
				y-=speed;
				break;
			case 1:
				y+=speed;
				break;
			case 2:
				x-=speed;
				break;
			case 3:
				x+=speed;
				break;
			}
			System.out.println("x="+x+"y="+y);
			if(x<0||x>400||y<0||y>300)
			{
				this.isLive=false;

				break;
			}
		}
	}
}

class bomb
{
	boolean isLive=true;
	int life=10;
	int x,y;
	public  bomb(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	public void lifedown()
	{
		if(life>0) life--;
		else isLive=false;
	}
}