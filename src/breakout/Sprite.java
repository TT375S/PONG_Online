package breakout;

import java.awt.Graphics2D;

//このゲームの画面上の構成部品の基礎となるスプライト。
public abstract class Sprite {
	public double x;
	public double y;
	double vx;
	double vy;
	boolean isExist;

	public Sprite(double x, double y){
		this.x = x;
		this.y = y;
		isExist = true;
	}

	public abstract void checkOver();

	public abstract void update();

	public abstract void draw(Graphics2D g);

//	public abstract boolean collision(Ball b);

	public boolean isExist(){
		return isExist;
	}
}
