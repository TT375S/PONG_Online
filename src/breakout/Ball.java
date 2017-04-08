package breakout;

import java.awt.Graphics2D;


public class Ball extends Sprite{
	int r;
	int R;
	final static double VMAX = 7;
	final static double VMAX2 = VMAX*VMAX;
	final static double XMAX = VMAX - 1;
	double vx;
	double vy;

	public Ball(double x, double y) {
		super(x, y);
		this.r = 10;
		this.R = r*2;
		vx = 3;
		vy = Math.sqrt(VMAX2-vx*vx);
	}

	@Override
	public void update() {
//		double oldx = x;
//		double oldy = y;
		x += vx;
		y += vy;
		checkOver();
	}

	//ボールが衝突した時に反射する。反射する角度は常に入射角=反射角
	//動いてるバーなど、当たったものの速度も角度や速度に反映すると良いかも
	public void changeV(boolean isx){
		if(isx) vx = -vx;
		else vy = -vy;
	}

	//画面からはみ出てないかのチェック
	//ボールは特定の方向に画面から出たら消える運命にある
	@Override
	public void checkOver() {
		//(左)
		if(x < r){
			x = r;
			changeV(false);
		}
		//(右)
		else if(x > MainFrame.WIDTH-r){
			x = MainFrame.WIDTH-r;
			changeV(false);
		}
		//ボールが画面の上下のワクに達したら、ボールは消える
		//(上)
		else if(y < r){
			y = r;
			changeV(true);
			delete();
		}
		//(下)
		else if(y > MainFrame.HEIGHT-r){
			y = MainFrame.HEIGHT-r;
			changeV(true);
			delete();
		}
	}

	public void delete(){
		isExist = false;
	}

	//強制的にY座標設定
	public void setYon(double y){
		this.y = y-r;
	}
	//横の壁は跳ね返る。そのため別チェック。上のcheckOver()にて行う。
//	private boolean checkWall(){
//		return x < r || x > MainFrame.WIDTH-r || y < r || y > MainFrame.HEIGHT-r;
//	}

	@Override
	public void draw(Graphics2D g) {
		g.fillOval((int)(x-r), (int)(y-r), R, R);
	}
	//衝突判定は同じく、他のパーツ側での判定にした
//	@Override
//	public boolean collision(Ball b) {
//
//		return false;
//	}
}
