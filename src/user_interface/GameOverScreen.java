package user_interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import characters.DoomSlayer;
import constants_across_classes.ImagesPaths;
import engine.ScoresManager;

public class GameOverScreen {
	private JFrame mainFrame;
	private JLabel boomLogo_Boo,boomLogo_M,yourScoreLabel;
	private JPanel mainPanel;
	private DoomSlayer doomSlayer;
	private JLabel[] numbers;
	private ImageIcon[] numbersImages;
	private JButton playAgainButton,backToMenuButton;
	private SoundClip endGameSong;
	private String userResult;
	private JTextField newScoreName;
	
	public GameOverScreen(JFrame mainFrame,JPanel mainPanel,DoomSlayer doomSlayer,
						  JLabel[] numbers,ImageIcon[] numbersImages,
						  SoundClip endGameSong){
		this.mainFrame = mainFrame;
		this.mainPanel = mainPanel;
		this.doomSlayer = doomSlayer;
		this.numbers = numbers;
		this.numbersImages = numbersImages;
		this.endGameSong = endGameSong;
		
		setLogo();
		setUsersScore();
		
		showScreen();
		new GameOverScreenHandler(this);
	}
	
  	private void setLogo(){
  		ImageIcon boomLogo_Boo_Image = new ImageIcon(ImagesPaths.BOOM_LOGO_BOO);
    	boomLogo_Boo = new JLabel(boomLogo_Boo_Image);
    	
    	ImageIcon boomLogo_M_Image = new ImageIcon(ImagesPaths.BOOM_LOGO_M);
    	boomLogo_M = new JLabel(boomLogo_M_Image);

    	JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    	logoPanel.setOpaque(false);
    	logoPanel.add(boomLogo_Boo);
    	logoPanel.add(boomLogo_M);

    	mainPanel.add(logoPanel,BorderLayout.NORTH);
  	}

  	private void setUsersScore() {
  		Font buttonsFont = new Font("Times New Roman",Font.PLAIN,45);
  		// Setting the yourScoreLabel panel
  		ImageIcon yourScoreIcon = new ImageIcon(ImagesPaths.YOUR_SCORE_PATH);
  		yourScoreLabel = new JLabel();
  		yourScoreLabel.setIcon(yourScoreIcon);
  		JPanel yourScoreLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
  		yourScoreLabelPanel.setOpaque(false);
  		yourScoreLabelPanel.add(yourScoreLabel);
  		
  		// Setting the panel that will hold the actual score
  		String actualScoreStr = String.valueOf(doomSlayer.getScore());
		JPanel actualScorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		actualScorePanel.setOpaque(false);
		for(int x = 0; x < actualScoreStr.length(); x++){
			numbers[x].setIcon(numbersImages[Integer.parseInt(String.valueOf(actualScoreStr.charAt(x)))]);
			actualScorePanel.add(numbers[x]);
		}
  		
		// Setting the playAgainButton
		playAgainButton = new JButton("Play");
		playAgainButton.setFont(buttonsFont);
    	paintButtonWhenMouseIsOut(playAgainButton);
    	JPanel playAgainButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    	playAgainButtonPanel.setOpaque(false);
    	playAgainButtonPanel.add(playAgainButton);
    	
    	// Setting the backToMenuButton
    	backToMenuButton = new JButton("Menu");
    	backToMenuButton.setFont(buttonsFont);
    	paintButtonWhenMouseIsOut(backToMenuButton);
    	JPanel backToMenuButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    	backToMenuButtonPanel.setOpaque(false);
    	backToMenuButtonPanel.add(backToMenuButton);
    	
    	// Setting the textfild
    	newScoreName = new JTextField(30);
    	newScoreName.setFont(buttonsFont);
    	newScoreName.setText("Has Logrado Un Nuevo Score, Ingresa Tu Nombre");
    	JPanel newScoreNamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    	newScoreNamePanel.setOpaque(false);
    	newScoreNamePanel.add(newScoreName);
		
  		// Setting the panel that will hold the word "Score" and the number
  		// representing the score
  		JPanel usersScorePanel = new JPanel();
  		BoxLayout boxLayout = new BoxLayout(usersScorePanel,BoxLayout.Y_AXIS);
  		usersScorePanel.setLayout(boxLayout);
  		usersScorePanel.setOpaque(false);
  		
  		usersScorePanel.add(Box.createRigidArea(new Dimension(10,200)));
  		usersScorePanel.add(yourScoreLabelPanel);
  		usersScorePanel.add(actualScorePanel);
  		
  		// Checking if user should enter his name
  		if(didUserSurpassAnyScore()){
  			usersScorePanel.add(Box.createRigidArea(new Dimension(10,50)));
  			usersScorePanel.add(newScoreNamePanel);
  	  		usersScorePanel.add(Box.createRigidArea(new Dimension(10,30)));
  	  		usersScorePanel.add(playAgainButtonPanel);
  	  		usersScorePanel.add(backToMenuButtonPanel);
  	  		usersScorePanel.add(Box.createRigidArea(new Dimension(10,50)));
  		}else{
  	  		usersScorePanel.add(Box.createRigidArea(new Dimension(10,50)));
  	  		usersScorePanel.add(playAgainButtonPanel);
  	  		usersScorePanel.add(backToMenuButtonPanel);
  	  		usersScorePanel.add(Box.createRigidArea(new Dimension(10,50)));
  		}
  		
  		mainPanel.add(usersScorePanel,BorderLayout.CENTER);
  	}
  	
  	private boolean didUserSurpassAnyScore(){
  		userResult =ScoresManager.getScoreRanking(doomSlayer.getScore());
  		if(userResult.equals(ScoresManager.GOLD)){
  			return true;
  		}else if(userResult.equals(ScoresManager.SILVER)){
  			return true;
  		}else if(userResult.equals(ScoresManager.BRONZE)){
  			return true;
  		}
  		return false;
  	}
  	
  	/**
  	 * Changes the look of the button given as a parameter, this method is 
  	 * supposed to be called when the user positions the mouse out of a button.
  	 * @param buttonToPaint - The button whose look will change.
  	 */
	public void paintButtonWhenMouseIsOut(JButton buttonToPaint){
		buttonToPaint.setBackground(MenuScreen.MOUSE_OUT_BUTTON_BACKGROUND_COLOR);
		buttonToPaint.setForeground(MenuScreen.MOUSE_OUT_BUTTON_FOREGROUND_COLOR);
		buttonToPaint.setBorder(BorderFactory.createLineBorder(
				MenuScreen.MOUSE_OUT_BUTTON_BORDER_COLOR,
				MenuScreen.MOUSE_IN_BUTTON_BORDER_THICKNESS));
	}
	
	/**
	 * Changes the look of button given as a parameter, this methods is supposed
	 * to be called when the user positions the mouse inside a button.
	 * @param buttonToPaint - The button whose look will change. 
	 */
	public void paintButtonWhenMouseIsIn(JButton buttonToPaint){
		buttonToPaint.setBorder(BorderFactory.createLineBorder(
				MenuScreen.MOUSE_IN_BUTTON_BORDER_COLOR,
				MenuScreen.MOUSE_IN_BUTTON_BORDER_THICKNESS));
	}
  	
  	public void showScreen() {
  		mainPanel.repaint();
  		mainFrame.setVisible(true);
  	}
  	
  	public JButton getPlayAgainButton() {
  		return playAgainButton;
  	}
  	
  	public JButton getBackToMenuButton() {
  		return backToMenuButton;
  	}
  	
  	public JFrame getMainFrame() {
  		return mainFrame;
  	}
  	
  	public SoundClip getEndGameSong() {
  		return endGameSong;
  	}
  	
  	public DoomSlayer getDoomSlayer() {
  		return doomSlayer;
  	}
  	
  	public String getUsersResult() {
  		return userResult;
  	}
  	
  	public JTextField getNewScoreName() {
  		return newScoreName;
  	}
}
