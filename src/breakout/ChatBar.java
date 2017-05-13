package breakout;

import java.awt.Font;
import java.awt.Graphics2D;

//チャットメッセージでコントロールするタイプのBar。
//打った文章がラケットバーになる。一回、打ち返すと消えてしまう。
public class ChatBar extends Bar{
	private String message = "Hello!";
	private int CharWidth = 30;

	public ChatBar(double x, double y, Ball b){
		super(x, y, b);
		this.setMessage(message);
	}

	//文章設定。
	public void setMessage(String msg){
		System.out.println("setMessage " + message);
		this.width = msg.length() * CharWidth;
		message = msg;
		this.isExist = true;
	}

	public String getMessage(){
		String ret = message;;
		if(message.equals("")) ret = "/EMPTY";
		return ret;
	}

	@Override
	public void draw(Graphics2D g) {
		if(message.length() != 0)System.out.println("drawChat " + message);
		g.fillRect((int)x, (int)y, width, height);
		g.setFont(new Font("Arial", Font.BOLD, 50));
		g.drawString(message, (int)x, (int)y);
	}

	//打ち返したあとなど、消すときにつかう。
	public void delete(){
		message = "";
		this.isExist = false;
		this.width = 1;
	}

	//打ち返したとき(ボールが当たったとき)に消す。
	@Override
	public int collision(Ball b){
		int ret = super.collision(b);
		if(ret != -1 )this.delete();
		return ret;
	}


}
