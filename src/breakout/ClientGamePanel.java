package breakout;

import java.io.IOException;

//クライアント側のゲームパネル
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

	double lastBallY = ball.y; /*前回更新時のボールのy位置*/
	double lastBallVY = 0; /*前回更新時のボールのy軸速度*/
	double lastBallX = 0;
	//画面上のすべてのパーツを更新(クライアント側はオフラインやサーバモードとは処理が別)
	public void update(){
		if(networkManager.isConnected() ){
			//現在の状態を送信
			networkManager.out.println(createSendData());
			networkManager.out.flush();

			//受信
			try {
				digestRecieveData(networkManager.in.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		double currentBallVY = ball.y - lastBallY;
		//ボールのY軸速度が正から負にに変わったら、打ち返したということでラケットを消す
		if(currentBallVY < 0 && lastBallVY > 0)bar.delete();
		lastBallVY = currentBallVY;
		lastBallY = ball.y;

		keyCheck();
		bar.update();
		//サーバーの時はball.update()の中でやっちゃってるバウンダリチェックを別にやってる
		switch(ball.checkOverBoundary()){
			case 3:		//上の辺に当たってゲームオーバー
				ball.vy = 1000;		//ゲームに勝った判定するため
				break;
			case 4:		//下の辺に当たってゲームオーバー
				ball.y = -1000;
				break;
			default:
				break;
		}
		//ballが画面外に出るなどして存在しなくなった場合、ゲームオーバー
		if(!ball.isExist()){
			anime = false;
			networkManager.disconect();
		}
	}

	private String lastEnemyMessage= "";/*　前回の敵側のメッセージ */

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
			//メッセージ部分を解釈。空白だと受け渡しに不都合があるので/EMPTYで置き換えてやりとりしている
			if(inputTokens[4].equals("/EMPTY"))
				bar_enemy.setMessage("");
			else{
				//前回と違うメッセージなら、バーにセットとかチャットログ更新とかする
				if(!lastEnemyMessage.equals(inputTokens[4])){
					bar_enemy.setMessage(inputTokens[4]);
					if(chatPanel != null)chatPanel.updateChatLog("ENEMY", inputTokens[4]);
				}
			}
			lastEnemyMessage = inputTokens[4];
		}
	}
}
