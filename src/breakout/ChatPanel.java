package breakout;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatPanel extends JPanel
	implements Runnable, KeyListener{
	public static final int WIDTH = 300;
	public static final int HEIGHT = 500;

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
		updateChatLog("SYSTEM", "start chat.");
		chatLog.setPreferredSize(new Dimension(this.HEIGHT -20, this.WIDTH));
		//area.setPreferredSize(new Dimension(100, 200));
		scrollpane = new JScrollPane(chatLog);
		this.add(this.scrollpane);

		//チャット用テキストフィールドを追加
		this.chatInputField = new JTextField(8);
		this.add(this.chatInputField);

		chatInputField.addKeyListener(this);
		//Frame設定
		this.setFocusable(true);
		this.addKeyListener(this);
		this.setSize(WIDTH, HEIGHT);

		init();
		new Thread(this).start();
	}

	//パーツの配置
	private void init(){
		final ChatBar bar = gamePanel.bar;
		//this.setFocusable(true);
		chatInputField.addActionListener(new java.awt.event.ActionListener() {
			//チャット用テキストフィールドでEnterが押された場合に実行
			public void actionPerformed(java.awt.event.ActionEvent e) {
				//バーにチャットメッセージをセットなど
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

	public void start(){
		Thread thread = new Thread(this);
		thread.start();
	}

	//画面上のすべてのパーツを更新
	public void update(){
	}



	//画面描画。全てのパーツをここで描画する
	public void draw(Graphics2D g){
	}


	//ゲーム実行中はスレッドでこれが常に繰り返し実行される
	@Override
	public void run() {
		//敵方がチャット送って来てないかをしらべて更新(エレガントとは程遠い)
		String lastEnemyMessage="";
		int i=0;
		while(true){
			System.out.println("ChatLog updated");
			i++;
			i %= 64;
			if(!gamePanel.bar_enemy.getMessage().equals(lastEnemyMessage)){
				lastEnemyMessage = gamePanel.bar_enemy.getMessage();
				updateChatLog("ENEMY", lastEnemyMessage);
				System.out.println("ChatLog updated");
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

}

