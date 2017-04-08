package breakout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;


public class Main{
	//このゲーム全体のエントリポイント
	public static void main(String[] args){

		JFrame frame = new JFrame();
		MainFrame panel = new MainFrame();
		StartPanel startPanel = new StartPanel();

		panel.setVisible(false);
		startPanel.setVisible(true);


		frame.add(startPanel);
		frame.add(panel);

		frame.setTitle("ball");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
//		frame.setBounds(100, 100, MainFrame.WIDTH+15, MainFrame.HEIGHT+50);
		frame.setBounds(200, 200, MainFrame.WIDTH+15, MainFrame.HEIGHT+39);

		JButton btn = new JButton("SubPanelに移動");
		btn.setBounds(50, 50, 200, 40);
        btn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                panel.setVisible(true);
                startPanel.setVisible(false);
                panel.start();
            }
        });
        startPanel.add(btn);
	}
}
