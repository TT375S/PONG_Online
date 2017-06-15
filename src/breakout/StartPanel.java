package breakout;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

//ゲームの開始画面。
public class StartPanel extends JPanel{
	public static final int WIDTH = 550;
	public static final int HEIGHT = 800;
	JLabel paneltitle = new JLabel("StartPanel");
    public  JTextField field_port;
    public  JTextField field_hostName;
    public	JButton btn_offline;
    public JButton btn_server;
    public JButton btn_client;

	public StartPanel(){
		this.setFocusable(true);
		this.setSize(WIDTH, HEIGHT);

		//this.setLayout(null);
		paneltitle.setBounds(0, 5, 400, 30);
        this.add(paneltitle);
        this.setBackground(Color.getHSBColor(205, 0.5f, 0.8f));

      //スタート画面に設定値入力用のフィールドを追加
	    field_port = new JTextField(10);
	    field_hostName = new JTextField(15);
	    field_port.setText("8080");
	    field_hostName.setText("localhost");
	    this.add(field_port);
	    this.add(field_hostName);

		//スタート画面にボタンを追加
		btn_offline = new JButton("オフライン");
		btn_offline.setBounds(50, 50, 200, 40);
        this.add(btn_offline);

		btn_server = new JButton("サーバ");
		btn_server.setBounds(100, 50, 200, 40);
        this.add(btn_server);

		btn_client = new JButton("クライアント");
		btn_client.setBounds(100, 50, 200, 40);
        this.add(btn_client);

	}
}
