/*
 * Description: This is the Main class, the software begins here.
 */
package user_interface;

import java.awt.Toolkit;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args){
		JFrame mainFrame = new JFrame();
    	mainFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width,
    					  Toolkit.getDefaultToolkit().getScreenSize().height);
    	mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	mainFrame.setResizable(false);
		new MenuScreen(mainFrame);
	}
}
