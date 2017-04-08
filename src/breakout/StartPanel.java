package breakout;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StartPanel extends JPanel{
	public static final int WIDTH = 800;
	public static final int HEIGHT = 500;
	JLabel paneltitle = new JLabel("SettingPanel");
	public StartPanel(){
		this.setFocusable(true);
		this.setSize(WIDTH, HEIGHT);

		//this.setLayout(null);
		paneltitle.setBounds(0, 5, 400, 30);
        this.add(paneltitle);
        this.setBackground(Color.getHSBColor(205, 0.5f, 0.8f));
	}
}
