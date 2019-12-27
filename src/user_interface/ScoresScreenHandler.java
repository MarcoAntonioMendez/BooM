package user_interface;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

public class ScoresScreenHandler implements ActionListener,MouseListener{
	private JButton goBackButton;
	private ScoresScreen screen;
	private JFrame mainFrame;
	private SoundClip menuSong;
	
	public ScoresScreenHandler(ScoresScreen screen,SoundClip menuSong){
		goBackButton = screen.getGoBackButton();
		this.screen = screen;
		mainFrame = this.screen.getMainFrame();
		this.menuSong = menuSong;
		
		goBackButton.addActionListener(this);
		goBackButton.addMouseListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event){
		if(event.getSource() == goBackButton){
			menuSong.stop();
			mainFrame.removeAll();
			mainFrame.repaint();
			mainFrame.setVisible(true);
			JFrame mainFrame = new JFrame();
	    	mainFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width,
	    					  Toolkit.getDefaultToolkit().getScreenSize().height);
	    	mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    	mainFrame.setResizable(false);
			new MenuScreen(mainFrame);
			this.mainFrame.dispatchEvent(new WindowEvent(this.mainFrame, WindowEvent.WINDOW_CLOSING));

		}	
	}
	
	@Override
	public void mouseEntered(MouseEvent event){
		screen.playButtonSound();
		screen.paintButtonWhenMouseIsIn((JButton)event.getSource());	
	}

	@Override
	public void mouseExited(MouseEvent event){
		screen.playButtonSound();
		screen.paintButtonWhenMouseIsOut((JButton)event.getSource());
	}
	
	@Override
	public void mouseClicked(MouseEvent event){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
