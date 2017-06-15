package breakout;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

//オフラインでのゲームパネル。全てのゲームパネルのスーパークラス
public class GamePanel extends JPanel
	implements Runnable, KeyListener, MouseListener{
	public static final int WIDTH = 800;
	public static final int HEIGHT = 500;

	protected Ball ball;
	final protected int initialX_ball = 400;
	final protected int initialY_ball = 250;

	final protected int initialYOffset_bar = 50;
	public ChatBar bar;
	final protected int initialY_bar = GamePanel.HEIGHT-initialYOffset_bar;
	public ChatBar bar_enemy;
	final protected int initialY_enemyBar = initialYOffset_bar;
	final protected int initialWidth_bar = WIDTH/2-Bar.WIDTH/2;

	protected boolean anime; /*　画面が動いているか否かのフラグ */
	protected int key; /*入力キーの情報 */

	public GamePanel(){
		super();
		//Frame設定
		this.setFocusable(true);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.setSize(WIDTH, HEIGHT);

		init();
	}

	public void start(){
		Thread thread = new Thread(this);
		thread.start();
		System.out.println("Thread start");
		anime = true;
	}

	//パーツの配置
	private void init(){
		System.out.println("init");
		ball = new Ball(initialX_ball, initialY_ball);

		bar = new ChatBar(initialWidth_bar, initialY_bar, ball);
		bar_enemy = new ChatBar(initialWidth_bar, initialY_enemyBar, ball);
	}

	//画面上のすべてのパーツを更新
	public void update(){
			keyCheck();
			ball.update();

			bar.update();
			bar_enemy.update();
			int check = bar.collision(ball);
			if(check != -1){
				System.out.println("HIT1");
				ball.changeV(false);
				//強制的にバーの上に移動させる
				ball.setYon(initialY_bar);
			}
//			else if(check != -1) ball.changeV(true);

			check = bar_enemy.collision(ball);
			//敵バーの衝突
			if(check != -1){
				System.out.println("HIT2");
				ball.changeV(false);
				//強制的にバーの上に移動させる(位置に注意！上下反転してるので厚さも考える)
				ball.setYon(initialY_enemyBar + bar_enemy.height);
			}

			//ballが画面外に出るなどして存在しなくなった場合、ゲームオーバー
			if(!ball.isExist()) anime = false;
	}

	//TODO:同時押しに対応させるべき
	public void keyCheck(){
		switch(key){
		case KeyEvent.VK_LEFT:
			bar.move(-5);
			break;
		case KeyEvent.VK_RIGHT:
			bar.move(5);
			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		key = e.getKeyCode();
//		System.out.println(key);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		key = 0;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	//画面描画。全てのパーツをここで描画する
	public void draw(Graphics2D g){
		//背景黒塗り
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		//パーツのカラー設定
		g.setColor(Color.WHITE);

		if(ball.isExist()) ball.draw(g);

		bar.draw(g);
		bar_enemy.draw(g);
		//ゲームオーバー処理
		if(!anime){
			g.drawString("GAME OVER", GamePanel.WIDTH/2, GamePanel.HEIGHT/2);
			if(ball.vy>0)g.drawString("YOU WIN", GamePanel.WIDTH*7/24, GamePanel.HEIGHT*1/2);
			else g.drawString("YOU LOSE", GamePanel.WIDTH*7/24, GamePanel.HEIGHT*1/2);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		draw((Graphics2D)g);
	}

	//ゲーム実行中はスレッドでこれが常に繰り返し実行される
	@Override
	public void run() {
		while(true){
			if(anime){
				update();
			}
			repaint();
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		this.requestFocus(true);  //クリックされたときにフォーカスを切り替える
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
