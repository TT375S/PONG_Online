package breakout;

import java.awt.Graphics2D;

public class Ball extends Sprite{
	int r;
	int R;
	final static double VMAX = 3;
	final static double VMAX2 = VMAX*VMAX;
	final static double XMAX = VMAX - 1;
	double vx;
	double vy;

	public Ball(double x, double y) {
		super(x, y);
		this.r = 10;
		this.R = r*2;
		vx = 1;
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
		System.out.println("changeV: " + isx);
		if(isx) vx = -vx;
		else vy = -vy;
	}

	//画面からはみ出てないかのチェック
	//ボールは特定の方向に画面から出たら消える運命にある
	@Override
	public void checkOver() {
		this.checkOverBoundary();
	}

	//画面からはみ出てる方向を調べる
	public int checkOverBoundary() {
		//(左)
		if(x < r){
			System.out.println("WallHit_L");
			x = r;
			changeV(true);
			return 1;
		}
		//(右)
		else if(x > GamePanel.WIDTH-r){
			System.out.println("WallHit_R");
			x = GamePanel.WIDTH-r;
			changeV(true);

			return 2;
		}
		//ボールが画面の上下のワクに達したら、ボールは消える
		//(上)
		else if(y <= r){
			System.out.println("WallHit_U");
			y = r;
			changeV(false);
			delete();

			return 3;
		}
		//(下)
		else if(y >= GamePanel.HEIGHT-r){
			System.out.println("WallHit_D");
			y = GamePanel.HEIGHT-r;
			changeV(false);
			delete();

			return 4;
		}
		System.out.printf("boundaryCheck: (%4.5f, %4.5f) \n",this.x , this.y);
		return 0;
	}

	public void delete(){
		isExist = false;
	}

	//強制的にY座標設定。バーにヒットした時強制的に上に載せるのに使用する。敵バーのときは変えるべきだけど面倒なのでそのまま
	public void setYon(double y){
		this.y = y-r;
	}
	//横の壁は跳ね返る。そのため別チェック。上のcheckOver()にて行う。
//	private boolean checkWall(){
//		return x < r || x > MainFrame.WIDTH-r || y < r || y > MainFrame.HEIGHT-r;
//	}

	public boolean ismyturn(){
 		return this.vy>0;
 	}

	@Override
	public void draw(Graphics2D g) {
		//Ballクラスのx,yは中心座標だが、fillOvalの第一・第二引数は左上の座標になる。
		g.fillOval((int)(x-r), (int)(y-r), R, R);
	}
	//衝突判定は同じく、他のパーツ側での判定にした
//	@Override
//	public boolean collision(Ball b) {
//
//		return false;
//	}
}
