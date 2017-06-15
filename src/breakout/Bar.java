package breakout1;

//長方形型のRectクラスのサブクラスとしてのバー。プレイヤーの分身
public class Bar extends Rect{
	//public static final int HEIGHT = 60;
	public static final int WIDTH = 480;
	public static final int HEIGHT = 15;
	public Bar(double x, double y, Ball b) {
		super(x, y);
		this.width = WIDTH;
		this.height = HEIGHT;
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
