package user_interface;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import characters.DoomSlayer;
import engine.ScoresManager;
import sounds.SoundsPaths;

public class GameOverScreenHandler implements ActionListener,MouseListener{
	private JFrame mainFrame;
	private JButton playAgainButton,backToMenuButton;
	private GameOverScreen screen;
	private SoundClip mouseInButtonSound,endGameSong;
	private String userResult;
	private DoomSlayer doomSlayer;
	private JTextField newScoreName;
	
	public GameOverScreenHandler(GameOverScreen screen){
		mouseInButtonSound = new SoundClip(SoundsPaths.MOUSE_IN_BUTTON_SOUND);
		
		this.screen = screen;
		playAgainButton = screen.getPlayAgainButton();
		backToMenuButton = screen.getBackToMenuButton();
		mainFrame = screen.getMainFrame();
		endGameSong = screen.getEndGameSong();
		userResult = screen.getUsersResult();
		doomSlayer = screen.getDoomSlayer();
		this.newScoreName = screen.getNewScoreName();
		
		playAgainButton.addActionListener(this);
		playAgainButton.addMouseListener(this);
		backToMenuButton.addActionListener(this);
		backToMenuButton.addMouseListener(this);
	}
	
	private void updateScores(){
		String newName = newScoreName.getText();
		if(userResult.equals(ScoresManager.GOLD)){
			ScoresManager.updateGoldenScore(newName,doomSlayer.getScore());
		}else if(userResult.equals(ScoresManager.SILVER)){
			ScoresManager.updateSilverScore(newName,doomSlayer.getScore());
		}else if(userResult.equals(ScoresManager.BRONZE)){
			ScoresManager.updateBronzeScore(newName,doomSlayer.getScore());
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == playAgainButton){
			updateScores();
			endGameSong.stop();
			mainFrame.removeAll();
			screen.showScreen();
			JFrame mainFrame = new JFrame();
	    	mainFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width,
	    					  Toolkit.getDefaultToolkit().getScreenSize().height);
	    	mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    	mainFrame.setResizable(false);
			new PlayScreen(mainFrame,mouseInButtonSound);
			this.mainFrame.dispatchEvent(new WindowEvent(this.mainFrame, WindowEvent.WINDOW_CLOSING));
		}else if(event.getSource() == backToMenuButton){
			updateScores();
			endGameSong.stop();
			mainFrame.removeAll();
			screen.showScreen();
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
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent event){
		screen.paintButtonWhenMouseIsIn((JButton)event.getSource());
		mouseInButtonSound.play();
	}

	@Override
	public void mouseExited(MouseEvent event){
		screen.paintButtonWhenMouseIsOut((JButton)event.getSource());
		mouseInButtonSound.play();
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
