package breakout;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class GamePanel extends JPanel
	implements Runnable, KeyListener{
	public static final int WIDTH = 800;
	public static final int HEIGHT = 500;
	public static boolean OFFLINE = false;


	private Ball ball;
	private Block[] block;
	private ChatBar bar;
	private ChatBar bar_enemy;
	private boolean anime;
	private int key;
	private NetworkManager networkManager;
	private JTextField chatInputField;
	private String message = "";

	public GamePanel(){
		//チャット用テキストフィールドを追加
		this.chatInputField = new JTextField(8);
		this.add(this.chatInputField);
		chatInputField.addKeyListener(this);
		//Frame設定
		this.setFocusable(true);
		this.addKeyListener(this);
		this.setSize(WIDTH, HEIGHT);

		init();
	}

	//ネットワーク設定を画面作成時にやるつもりだったけど、後の方が良さそうだったので変更した名残
//	public MainFrame(){
//		this(true, 8080);
//	}
//
//	public MainFrame(boolean isServer, int port, String... host){
//		if(!DEBUG)networkManager = new NetworkManager(isServer, port, host);
//		this.setFocusable(true);
//		this.addKeyListener(this);
//		this.setSize(WIDTH, HEIGHT);
//
//		init();
//	}

	public void start(){
		this.start(true, -1);
	}

	public void start(boolean isServer, int port, String... host){
		if(port == -1) OFFLINE = true;
		if(!OFFLINE)networkManager = new NetworkManager(isServer, port, host);
		Thread thread = new Thread(this);
		thread.start();
		System.out.println("Thread start");
		anime = true;
	}

	//パーツの配置
	public void init(){
		System.out.println("init");
		ball = new Ball(300, 200);

		bar = new ChatBar(WIDTH/2-Bar.WIDTH/2, 400, ball);
		bar_enemy = new ChatBar(WIDTH/2-Bar.WIDTH/2, 30, ball);
		//this.setFocusable(true);
		chatInputField.addActionListener(new java.awt.event.ActionListener() {
			//チャット用テキストフィールドでEnterが押された場合に実行
			public void actionPerformed(java.awt.event.ActionEvent e) {
				//バーにチャットメッセージをセットなど
				message = chatInputField.getText();
				bar.setMessage(message);
				chatInputField.setText("");
				System.out.println("chat msg is settlement.");
			}
		});
	}

	//画面上のすべてのパーツを更新
	public void update(){
		//-----DEBUG FOR STAND ALONE------
		if(OFFLINE){
			keyCheck();
			ball.update();

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

			//現在の状態を送信。プレイヤーバーとボールの位置とこちらのメッセージ。
			//TODO:入力欄を入力途中でもリアルタイムに送信したほうがよい？
			//ボールの位置は、反対にいる相手から見ると反対側になる。
			int relativeBallX = this.WIDTH-(int)ball.x;
			int relativeBallY = this.HEIGHT-(int)ball.y;
			int relativeBarX = this.WIDTH -(int) bar.x - bar.width;
			int relativeBarY = this.HEIGHT -(int) bar.y + bar.height;
			networkManager.out.println(relativeBarX + " " + relativeBarY + " " + relativeBallX + " " + relativeBallY + " " + bar.getMessage());
			networkManager.out.flush();

			bar_enemy.update();

			//受信。エネミーバーの位置と相手のメッセージ。
			String inputLine = null;
			try {
				inputLine = networkManager.in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(inputLine != null){
				String[] inputTokens = inputLine.split(" ");
				bar_enemy.x = Double.parseDouble(inputTokens[0]) ;
				//y軸座標は今のところ変わらないのでいらない
				//bar_enemy.y = Double.parseDouble(inputTokens[1]) ;
				if(inputTokens[2].equals("/EMPTY"))bar_enemy.setMessage("");
				else bar_enemy.setMessage(inputTokens[2]);
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
			if(!ball.isExist()){
				anime = false;
				networkManager.disconect();
			}
		}
		//クライアント側のアップデート処理。ボールの位置計算などはホスト側でやってもらう。
		else{
			keyCheck();
			bar.update();
			//現在の状態を送信。プレイヤーバーの位置とこちらのメッセージ。
			int relativeBarX = this.WIDTH -(int) bar.x - bar.width;
			int relativeBarY = this.HEIGHT -(int) bar.y + bar.height;
			networkManager.out.println(relativeBarX + " " + relativeBarY + " " + bar.getMessage());
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
				//y軸座標は今のところ変わらないのでいらない
				//bar_enemy.y = Double.parseDouble(inputTokens[1]) ;
				ball.x = Double.parseDouble(inputTokens[2]);
				ball.y = Double.parseDouble(inputTokens[3]);
				if(inputTokens[4].equals("/EMPTY"))bar_enemy.setMessage("");
				else bar_enemy.setMessage(inputTokens[4]);
			}

			//サーバーの時はball.update()でやっちゃつてるバウンダリチェックを別にやってる
			ball.checkOver();
			//ballが画面外に出るなどして存在しなくなった場合、ゲームオーバー

			//ここを編集
			if(!ball.isExist()){
				anime = false;
			draw(g);


				networkManager.disconect();
			}
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
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		//パーツのカラー設定
		g.setColor(Color.WHITE);

		if(ball.isExist()) ball.draw(g);
		bar.draw(g);
		bar_enemy.draw(g);
		//ゲームオーバー処理
		if(!anime){
			if(ball.ismyturn(ball)) g.drawString("YOU WIN!!", GamePanel.WIDTH*7/24, GamePanel.HEIGHT/2);
			else  g.drawString("YOU LOSE...", GamePanel.WIDTH*7/24, GamePanel.HEIGHT/2);

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

}
