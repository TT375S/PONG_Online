package breakout1;

import java.awt.Dimension;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

//主となるフレーム。ウィンドウみたいなもん
public class MainFrame extends JFrame{
	GamePanel  gamePanel ;
	final StartPanel startPanel = new StartPanel();
	 ChatPanel chatPanel;
	public MainFrame(){
		//スタート画面を見えるようにし、ゲーム画面を見えないようにする
		startPanel.setVisible(true);

		this.add(startPanel);
		//this.add(gamePanel);

		//フレームの各種設定
		this.setTitle("ball");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
//		this.setBounds(100, 100, Mainthis.WIDTH+15, Mainthis.HEIGHT+50);
		this.setBounds(200, 200, GamePanel.WIDTH+ ChatPanel.WIDTH+ 15, GamePanel.HEIGHT+39);

		//スタート画面のボタン類の設定。他のパネルにアクセスする必要があるので、StartPanelクラスではなくここでやってしまっている
		startPanel.btn_offline.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	//オフラインボタンが押されたら、ゲーム画面を見えるようにし、スタート画面を見えなくし、ゲームを開始する。
        		startPanel.setVisible(false);
        		gamePanel = new GamePanel();
                gamePanel.start();
            }
        });

		//サーバモードで起動ボタン
        startPanel.btn_server.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	gamePanel = new ServerGamePanel(Integer.parseInt(startPanel.field_port.getText()), startPanel.field_hostName.getText());
            	chatPanel = new ChatPanel(gamePanel);
            	((ServerGamePanel)gamePanel).chatPanel = chatPanel;
            	initGameDisplay();
            }
        });

        //クライアントモードで起動ボタン
        startPanel.btn_client.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	gamePanel = new ClientGamePanel(Integer.parseInt(startPanel.field_port.getText()), startPanel.field_hostName.getText());
            	chatPanel = new ChatPanel(gamePanel);
            	((ClientGamePanel)gamePanel).chatPanel = chatPanel;
            	initGameDisplay();
            }
        });
	}

	//画面の並べ方や表示をセット
	private void initGameDisplay(){
		//メニュー画面には消えてもらう
		startPanel.setVisible(false);
		this.remove(startPanel);
		//パネル上でレイアウトする
		Panel p1 = new Panel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));

		//setMaximumSizeをやらなくちゃダメらしい
		gamePanel.setPreferredSize(new Dimension(GamePanel.WIDTH, GamePanel.HEIGHT));
		gamePanel.setMaximumSize(new Dimension(GamePanel.WIDTH, GamePanel.HEIGHT));
		chatPanel.setPreferredSize(new Dimension(ChatPanel.WIDTH, ChatPanel.HEIGHT));
		chatPanel.setMaximumSize(new Dimension( ChatPanel.WIDTH, ChatPanel.HEIGHT));

		p1.add(gamePanel);
		p1.add(chatPanel);

        this.add(p1);

//		this.add(gamePanel);
//		this.add(chatPanel);
        gamePanel.setVisible(true);
        chatPanel.setVisible(true);
	}

}
