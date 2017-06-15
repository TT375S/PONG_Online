package breakout1;

import java.awt.Graphics2D;

//長方形部品のスーパークラスとなるRect。
//四角い部品はほぼなんでもコレ
public class Rect extends Sprite{
	public int width;
	public int height;

	public Rect(double x, double y) {
		super(x, y);
	}

	@Override
	public void update() {

	}

	//画面からハミ出てないかチェックする
	@Override
	public void checkOver() {
//		if(x < 0) x = 0;
//		else if(x >= MainFrame.WIDTH-width) x = MainFrame.WIDTH-width;
		if(y < height) y = height;
		else if(y >= GamePanel.HEIGHT-height) y = GamePanel.HEIGHT-height;
	}

	@Override
	public void draw(Graphics2D g) {
		g.fillRect((int)x, (int)y, width, height);
	}

//	//当たり判定。長方形部品は傾いたりしないので、x,y座標の判別だけ。
//	public int collision(Ball b) {
//	//処理の簡略化のため、明らかに離れてれば衝突してないことにする
//	//部品のサイズをバカでかくする際は注意
//	//if(Math.abs(b.x - x)+Math.abs(b.y - y) > 100) return -1;
//
//	if(x-b.r < b.x && x+width + b.r > b.x && y-b.r < b.y && y+height+b.r > b.y){
//		System.out.println("RectCollision");
//		return 1;
//	}
//	return -1;
//}

	//このゲームでは、長方形部品とはボールでなく部品側で衝突判定する。ベクトルでの矩形-円の衝突判定
	//4辺のどこに当たったかを返す。当たらなければ-1
	public int collision(Ball b) {
		//処理の簡略化のため、明らかに離れてれば衝突してないことにする
		//部品のサイズをバカでかくする際は注意->　画面サイズ大きくしたのでこの値は設定し直すべき
		//if(Math.abs(b.x - x)+Math.abs(b.y - y) > 100) return -1;

		//長方形の四隅
		double[] px = new double[4];
		double[] py = new double[4];
		px[0] = x;
		py[0] = y;
		px[1] = x + width;
		py[1] = y;
		px[2] = x + width;
		py[2] = y + height;
		px[3] = x;
		py[3] = y + height;

		//http://1st.geocities.jp/shift486909/program/collision.html　らしい...
		for(int i=0; i<4; i++){
			double dist = (b.x-px[i])*(b.x-px[i]) + (b.y-py[i])*(b.y-py[i]);
//			if(dist < b.r*b.r) return i;
			double pqx = px[(i+1)%4] - px[i];
			double pqy = py[(i+1)%4] - py[i];
			double pmx = b.x - px[i];
			double pmy = b.y - py[i];
			double k = ((pqx*pmx) + (pqy*pmy))/((pqx*pqx) + (pqy*pqy));
			if(0 < k && k < 1){
				double mh = dist - k*((pqx*pmx) + (pqy*pmy));

				if(mh < b.r*b.r) return i;
			}
		}
		return -1;
	}
}
