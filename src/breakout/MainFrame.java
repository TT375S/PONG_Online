package breakout;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

//主となるフレーム。ウィンドウみたいなもん
public class MainFrame extends JFrame{
	final GamePanel  gamePanel = new GamePanel();
	final StartPanel startPanel = new StartPanel();
	final ChatPanel chatPanel = new ChatPanel(gamePanel);
	public MainFrame(){
		//スタート画面を見えるようにし、ゲーム画面を見えないようにする
		gamePanel.setVisible(false);
		startPanel.setVisible(true);

		this.add(startPanel);
		//this.add(gamePanel);

		//フレームの各種設定
		this.setTitle("ball");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
//		this.setBounds(100, 100, Mainthis.WIDTH+15, Mainthis.HEIGHT+50);
		this.setBounds(200, 200, GamePanel.WIDTH+ chatPanel.WIDTH+ 15, GamePanel.HEIGHT+39);

		//スタート画面のボタン類の設定。他のパネルにアクセスする必要があるので、StartPanelクラスではなくここでやってしまっている
		startPanel.btn_offline.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	//オフラインボタンが押されたら、ゲーム画面を見えるようにし、スタート画面を見えなくし、ゲームを開始する。
            	initGameDisplay();
                //ポート番号が-1のときはオフラインモード
                gamePanel.start(true, -1);
            }
        });

        startPanel.btn_server.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	initGameDisplay();
                gamePanel.start(true, Integer.parseInt(startPanel.field_port.getText()));
            }
        });

        startPanel.btn_client.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	initGameDisplay();
                gamePanel.start(false, Integer.parseInt(startPanel.field_port.getText()), startPanel.field_hostName.getText());
            }
        });


	}


	private void initGameDisplay(){
		startPanel.setVisible(false);
		this.remove(startPanel);
        //this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setLayout(new GridLayout(1, 2));
		//setLayout(new FlowLayout());
//		Panel p1 = new Panel();
//		p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
//		p1.add(gamePanel);
//		p1.add(chatPanel);
//        this.add(p1);
		this.add(gamePanel);
		this.add(chatPanel);
        gamePanel.setVisible(true);
        chatPanel.setVisible(true);

        //chatPanel.start();
	}

}
