package breakout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class MainFrame extends JFrame{
	public MainFrame(){
		JFrame frame = new JFrame();
		final GamePanel  panel = new GamePanel();
		final StartPanel startPanel = new StartPanel();

		panel.setVisible(false);
		startPanel.setVisible(true);


		frame.add(startPanel);
		frame.add(panel);

		frame.setTitle("ball");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
//		frame.setBounds(100, 100, MainFrame.WIDTH+15, MainFrame.HEIGHT+50);
		frame.setBounds(200, 200, GamePanel.WIDTH+15, GamePanel.HEIGHT+39);

		//スタート画面に設定値入力用のフィールドを追加
	    final JTextField field_port = new JTextField(10);
	    final JTextField field_hostName = new JTextField(15);
	    field_port.setText("8080");
	    field_hostName.setText("localhost");
	    startPanel.add(field_port);
	    startPanel.add(field_hostName);

		//スタート画面にボタンを追加
		//TODO;あるべき場所にこいつらを移動する
		JButton btn = new JButton("オフライン");
		btn.setBounds(50, 50, 200, 40);
        btn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                panel.setVisible(true);
                startPanel.setVisible(false);
                panel.start(true, -1);
            }
        });
        startPanel.add(btn);

		JButton btn_server = new JButton("サーバ");
		btn_server.setBounds(100, 50, 200, 40);
        btn_server.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                panel.setVisible(true);
                startPanel.setVisible(false);
                panel.start(true, Integer.parseInt(field_port.getText()));
            }
        });
        startPanel.add(btn_server);

		JButton btn_client = new JButton("クライアント");
		btn_client.setBounds(100, 50, 200, 40);
        btn_client.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                panel.setVisible(true);
                startPanel.setVisible(false);
                panel.start(false, Integer.parseInt(field_port.getText()), field_hostName.getText());
            }
        });
        startPanel.add(btn_client);
	}

	private void init(){

	}

}
