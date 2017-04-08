package breakout;

//長方形型のRectクラスのサブクラスとしてのバー。プレイヤーの分身
public class Bar extends Rect{
	//public static final int HEIGHT = 60;
	public static final int WIDTH = 480;
	public static final int HEIGHT = 15;
	private Ball ball;

	public Bar(double x, double y, Ball b) {
		super(x, y);
		this.width = WIDTH;
		this.height = HEIGHT;
		//衝突判定に必要なため、バー側でボールを持つ必要がある。
		this.ball = b;
	}

	@Override
	public void update() {
		//毎フレーム毎にバウンダリチェック。
		checkOver();
	}

	//今は横移動しかできない
	public void move(int dist){
		this.x += dist;
	}


}
