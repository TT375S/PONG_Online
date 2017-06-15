package breakout;

import java.io.IOException;

//サーバーモードでのゲームパネル
public class ServerGamePanel extends AbstractOnlineGamePanel{
	NetworkManager networkManager;
	public ServerGamePanel(int port, String... host){
		super();
		start(port, host);
	}

	public void start(int port, String... host){
		networkManager = new NetworkManager(true, port, host);
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
			repaint();
			networkManager.disconect();
		}
	}

	@Override
	//送信用データ作成
	public String createSendData(){
		//現在の状態を送信。プレイヤーバーとボールの位置とこちらのメッセージ。
		//TODO:入力欄を入力途中でもリアルタイムに送信したほうがよい？
		//ボールの位置は、反対にいる相手から見ると反対側になる。
		int relativeBallX = GamePanel.WIDTH-(int)ball.x;
		int relativeBallY = GamePanel.HEIGHT-(int)ball.y;
		int relativeBarX = GamePanel.WIDTH -(int) bar.x - bar.width;
		int relativeBarY = GamePanel.HEIGHT -(int) bar.y + bar.height;
		return relativeBarX + " " + relativeBarY + " " + relativeBallX + " " + relativeBallY + " " + bar.getMessage();
	}

	private String lastEnemyMessage = ""; /*　前回の敵側のメッセージ */

	@Override
	//受信データ解釈
	public void digestRecieveData(String receievedData){
		//受信。エネミーバーの位置と相手のメッセージ。
		if(receievedData != null){
			String[] inputTokens = receievedData.split(" ");
			bar_enemy.x = Double.parseDouble(inputTokens[0]) ;
			//y軸座標は今のところ変わらないのでいらない
			//bar_enemy.y = Double.parseDouble(inputTokens[1]) ;
			//メッセージ部分を解釈。空白だと受け渡しに不都合があるので/EMPTYで置き換えてやりとりしている
			if(inputTokens[2].equals("/EMPTY"))bar_enemy.setMessage("");
			else{
				//前回と違うメッセージなら、バーにセットとかチャットログ更新とかする
				if(!lastEnemyMessage.equals(inputTokens[2])){
					bar_enemy.setMessage(inputTokens[2]);
					if(chatPanel != null)chatPanel.updateChatLog("ENEMY", lastEnemyMessage);
				}
			}
			lastEnemyMessage = inputTokens[2];
		}
	}
}
