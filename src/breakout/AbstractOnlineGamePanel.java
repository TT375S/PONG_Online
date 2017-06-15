package breakout1;
//ゲームパネルのオンライン版
public abstract class AbstractOnlineGamePanel extends GamePanel{
	public  AbstractOnlineGamePanel() {
		super();
	}

	//なぜかアクセスできない...
	protected NetworkManager networkManager;
	public ChatPanel chatPanel;
	//送信用データ作成
	public abstract String createSendData();
	//受信データ解釈
	public abstract void digestRecieveData(String receievedData);
}
