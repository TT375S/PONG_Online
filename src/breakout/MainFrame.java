package breakout;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JPanel;

//TODO:フレームじゃなくてパネルだから改名すべき
public class MainFrame extends JPanel
	implements Runnable, KeyListener{
	public static final int WIDTH = 800;
	public static final int HEIGHT = 500;
	public static final boolean DEBUG = true;
	private Ball ball;
	private Block[] block;
	private Bar bar;
	private Bar bar_enemy;
	private boolean anime;
	private int key;
	private NetworkManager networkManager;

	public MainFrame(){
		this.setFocusable(true);
		this.addKeyListener(this);
		this.setSize(WIDTH, HEIGHT);

		//networkManager = new NetworkManager(true);

		init();
	}

	public void start(){
		Thread thread = new Thread(this);
		thread.start();
		System.out.println("Thread start");
		anime = true;
	}

	//パーツの配置
	public void init(){
		System.out.println("init");
		ball = new Ball(300, 200);
		block = new Block[12];
		for(int i=0; i<3; i++){
			for(int j=0; j<4; j++){
				block[i*4+j] = new Block(51+j*50, 52+i*20);
			}
		}
		bar = new Bar(WIDTH/2-Bar.WIDTH/2, 400, ball);
		bar_enemy = new Bar(WIDTH/2-Bar.WIDTH/2, 30, ball);
		if(!DEBUG)		networkManager = new NetworkManager(true, 8080);
	}

	//画面上のすべてのパーツを更新
	public void update(){
		//-----DEBUG FOR STAND ALONE------
		if(DEBUG){
			keyCheck();
			ball.update();

			for(int i=0; i<12; i++){
//				block[i].update();
				if(!block[i].isExist()) continue;
				int check = block[i].collision(ball);
				if(check == -1) continue;
				//ブロックのどの面と当たったかで反射方向を変える
				if(check == 0 || check == 2) ball.changeV(false);
				else ball.changeV(true);
				block[i].delete();
			}
			bar.update();
			bar_enemy.update();
			int check = bar.collision(ball);
			if(check != -1){
				System.out.println("HIT1");
				ball.changeV(false);
				//強制的にバーの上に移動させる
				ball.setYon(400);
			}
//			else if(check != -1) ball.changeV(true);

			check = bar_enemy.collision(ball);
			//敵バーの衝突
			if(check != -1){
				System.out.println("HIT2");
				ball.changeV(false);
				//強制的にバーの上に移動させる(位置に注意！上下反転してるので厚さも考える)
				ball.setYon(70);
			}

			//ballが画面外に出るなどして存在しなくなった場合、ゲームオーバー
			if(!ball.isExist()) anime = false;
			return;
		}
		//-----DEBUG FOR STAND ALONE-----

		if(networkManager.isServer){
			keyCheck();
			bar.update();
			ball.update();

			//現在の状態を送信。プレイヤーバーとボールの位置だけ。
			networkManager.out.println(bar.x + " " + bar.y + " " + ball.x + " " + ball.y);
			networkManager.out.flush();

			for(int i=0; i<12; i++){
//				block[i].update();
				if(!block[i].isExist()) continue;
				int check = block[i].collision(ball);
				if(check == -1) continue;
				//ブロックのどの面と当たったかで反射方向を変える
				if(check == 0 || check == 2) ball.changeV(false);
				else ball.changeV(true);
				block[i].delete();
			}

			bar_enemy.update();

			//受信。エネミーバーの位置だけ。
			String inputLine = null;
			try {
				inputLine = networkManager.in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(inputLine != null){
				String[] inputTokens = inputLine.split(" ");
				bar_enemy.x = Double.parseDouble(inputTokens[0]) ;
				bar_enemy.y = Double.parseDouble(inputTokens[1]) ;
			}

			int check = bar.collision(ball);
			if(check != -1){
				System.out.println("HIT1");
				ball.changeV(false);
				//強制的にバーの上に移動させる
				ball.setYon(400);
			}
//			else if(check != -1) ball.changeV(true);

			check = bar_enemy.collision(ball);
			//敵バーの衝突
			if(check != -1){
				System.out.println("HIT2");
				ball.changeV(false);
				//強制的にバーの上に移動させる(位置に注意！上下反転してるので厚さも考える)
				ball.setYon(70);
			}

			//ballが画面外に出るなどして存在しなくなった場合、ゲームオーバー
			if(!ball.isExist()) anime = false;
		}
		//クライアント側のアップデート処理。ボールの位置計算などはホスト側でやってもらう。
		else{
			keyCheck();
			bar.update();
			//現在の状態を送信。プレイヤーバーの位置だけ。
			networkManager.out.println(bar.x + " " + bar.y);
			networkManager.out.flush();
			//受信。エネミーバーとボールの位置だけ。
			String inputLine = null;
			try {
				inputLine = networkManager.in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(inputLine != null){
				String[] inputTokens = inputLine.split(" ");
				bar_enemy.x = Double.parseDouble(inputTokens[0]) ;
				bar_enemy.y = Double.parseDouble(inputTokens[1]) ;
				ball.x = Double.parseDouble(inputTokens[2]);
				ball.y = Double.parseDouble(inputTokens[3]);
			}

			//サーバーの時はball.update()でやっちゃつてるバウンダリチェックを別にやってる
			ball.checkOver();
			//ballが画面外に出るなどして存在しなくなった場合、ゲームオーバー
			if(!ball.isExist()) anime = false;
		}

	}

	//TODO:同時押しに対応させるべき
	public void keyCheck(){
//		int moveDist = 0;
//		if(key == KeyEvent.VK_LEFT) moveDist += -5;
//		else if(key == KeyEvent.VK_RIGHT) moveDist += 5;
//		bar.move(moveDist);
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
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		key = e.getKeyCode();
//		System.out.println(key);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		key = 0;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

	}

	//画面描画。全てのパーツをここで描画する
	public void draw(Graphics2D g){
		//背景黒塗り
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		//パーツのカラー設定
		g.setColor(Color.WHITE);

		if(ball.isExist()) ball.draw(g);
		for(int i=0; i<12; i++){
			if(!block[i].isExist()) continue;
			block[i].draw(g);
		}
		bar.draw(g);
		bar_enemy.draw(g);
		//ゲームオーバー処理
		if(!anime) g.drawString("GAME OVER", MainFrame.WIDTH/2, MainFrame.HEIGHT/2);
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

}
