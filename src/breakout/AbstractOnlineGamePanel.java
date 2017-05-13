package breakout;

public abstract class AbstractOnlineGamePanel extends GamePanel{
	public  AbstractOnlineGamePanel() {
		super();
	}

	@SuppressWarnings("unused")
	protected NetworkManager networkManager;
	//送信用データ作成
	public abstract String createSendData();
	//受信データ解釈
	public abstract void digestRecieveData(String receievedData);
}
