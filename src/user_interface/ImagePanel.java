package user_interface;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private Image backgroundImage;
	
	public ImagePanel(String imagePath,JFrame mainFrame){
		ImageIcon imageIcon = new ImageIcon(imagePath);
		int screenWidth = mainFrame.getWidth();
		int screenHeight = mainFrame.getHeight();
		backgroundImage = imageIcon.getImage().getScaledInstance(screenWidth,screenHeight, Image.SCALE_SMOOTH);
		
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, this);
	}
}
