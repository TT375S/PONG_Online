package breakout;

import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//チャットパネル。ゲームパネルの右に表示される
public class ChatPanel extends JPanel{
	public static final int WIDTH = 250;
	public static final int HEIGHT = 800;

	private GamePanel gamePanel;

	private JTextField chatInputField;
	private String message = "";
	private JTextArea chatLog;
	private JScrollPane scrollpane;

	public ChatPanel(GamePanel gamePanel){
		this.gamePanel = gamePanel;

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		//チャットログ
		chatLog = new JTextArea("...");
		chatLog.setEditable(false);  //編集不可にする
		chatLog.setFocusable(false); //フォーカス不能にする
		updateChatLog("SYSTEM", "start chat.");
		chatLog.setPreferredSize(new Dimension( ChatPanel.WIDTH, ChatPanel.HEIGHT));
		scrollpane = new JScrollPane(chatLog);
		this.add(this.scrollpane);

		//チャット用テキストフィールドを追加
		this.chatInputField = new JTextField(8);
		this.add(this.chatInputField);

		//Frame設定
		this.setFocusable(false);
		this.setSize(WIDTH, HEIGHT);

		init();
	}

	//コンストラクタでやった以外の初期化など
	private void init(){
		final ChatBar bar = gamePanel.bar;
		//this.setFocusable(true);
		chatInputField.addActionListener(new java.awt.event.ActionListener() {
			//チャット用テキストフィールドでEnterが押された場合に実行
			public void actionPerformed(java.awt.event.ActionEvent e) {
				//バーにチャットメッセージをセットしたり、チャットログをアップデートしたり
				message = chatInputField.getText();
				bar.setMessage(message);
				chatInputField.setText("");
				updateChatLog("YOU", message);
				System.out.println("chat msg is settlement.");
			}
		});
	}

	//チャットログの更新
	public void updateChatLog(String userName, String message){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//String text =  area.getText() +  "\n" + sdf.format(date) + "  <font size=2 color=#ff0000>"+ userName + "</font>:   " + message;
		chatLog.setText(chatLog.getText() +  "\n" + sdf.format(date) + "  "+ userName + ":   " + message);
	}
}

