package breakout;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;

public class ClientGamePanel extends AbstractOnlineGamePanel{
	NetworkManager networkManager;
	public ClientGamePanel(int port, String... host){
		super();
		start(port, host);
	}

	public void start(int port, String... host){
		//ネットワークマネージャをクライアントモードで作成
		networkManager = new NetworkManager(false, port, host);
		super.start();
	}

	//画面上のすべてのパーツを更新
	public void update(){
		//現在の状態を送信
		networkManager.out.println(createSendData());
		networkManager.out.flush();

		//受信
		try {
			digestRecieveData(networkManager.in.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}

		super.update();

		//ballが画面外に出るなどして存在しなくなった場合、ゲームオーバー
		if(!ball.isExist()){
			networkManager.disconect();
		}
	}

	@Override
	//送信用データ作成
	public String createSendData(){
		//現在の状態を送信。プレイヤーバーの位置とこちらのメッセージ。
		int relativeBarX = GamePanel.WIDTH -(int) bar.x - bar.width;
		int relativeBarY = GamePanel.HEIGHT -(int) bar.y + bar.height;
		return relativeBarX + " " + relativeBarY + " " + bar.getMessage();
	}

	@Override
	//受信データ解釈
	public void digestRecieveData(String receievedData){
		//受信。エネミーバーの位置と相手のメッセージ。
		if(receievedData != null){
			String[] inputTokens = receievedData.split(" ");
			bar_enemy.x = Double.parseDouble(inputTokens[0]) ;
			//y軸座標は今のところ変わらないのでいらない
			//bar_enemy.y = Double.parseDouble(inputTokens[1]) ;
			ball.x = Double.parseDouble(inputTokens[2]);
			ball.y = Double.parseDouble(inputTokens[3]);
			if(inputTokens[4].equals("/EMPTY"))bar_enemy.setMessage("");
			else bar_enemy.setMessage(inputTokens[4]);
		}
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
		if(!anime) g.drawString("GAME OVER", GamePanel.WIDTH/2, GamePanel.HEIGHT/2);
	}
}
