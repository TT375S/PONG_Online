package breakout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

//主となるフレーム。ウィンドウみたいなもん
public class MainFrame extends JFrame{
	public MainFrame(){

		final GamePanel  gamePanel = new GamePanel();
		final StartPanel startPanel = new StartPanel();

		//スタート画面を見えるようにし、ゲーム画面を見えないようにする
		gamePanel.setVisible(false);
		startPanel.setVisible(true);

		this.add(startPanel);
		this.add(gamePanel);

		//フレームの各種設定
		this.setTitle("ball");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
//		this.setBounds(100, 100, Mainthis.WIDTH+15, Mainthis.HEIGHT+50);
		this.setBounds(200, 200, GamePanel.WIDTH+15, GamePanel.HEIGHT+39);

		//スタート画面のボタン類の設定。他のパネルにアクセスする必要があるので、StartPanelクラスではなくここでやってしまっている
		startPanel.btn_offline.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	//オフラインボタンが押されたら、ゲーム画面を見えるようにし、スタート画面を見えなくし、ゲームを開始する。
                gamePanel.setVisible(true);
                startPanel.setVisible(false);
                //ポート番号が-1のときはオフラインモード
                gamePanel.start(true, -1);
            }
        });

        startPanel.btn_server.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                gamePanel.setVisible(true);
                startPanel.setVisible(false);
                gamePanel.start(true, Integer.parseInt(startPanel.field_port.getText()));
            }
        });

        startPanel.btn_client.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                gamePanel.setVisible(true);
                startPanel.setVisible(false);
                gamePanel.start(false, Integer.parseInt(startPanel.field_port.getText()), startPanel.field_hostName.getText());
            }
        });

	}

	private void init(){

	}

}
