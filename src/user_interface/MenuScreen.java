/*class: MenuScreen
Purpose: This class will display the first screen that the user
        will see when he/she starts the game, in it, the user will be able 
        to choose one option from menu:
        Start
        Instructions 
		Scores
		Settings
		Credits	
        
Last Modified: 30/09/2019 */

package user_interface;
import javax.swing.JFrame;
import javax.swing.JPanel;

import constants_across_classes.ImagesPaths;
import constants_across_classes.Language;
import sounds.SoundsPaths;

import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;

public class MenuScreen{
	//Constants
	public static final Color MOUSE_OUT_BUTTON_FOREGROUND_COLOR = new Color(237,237,237);
	public static final Color MOUSE_OUT_BUTTON_BACKGROUND_COLOR = new Color(31,31,31);
	public static final Color MOUSE_OUT_BUTTON_BORDER_COLOR = new Color(31,31,31);
	public static final Color MOUSE_IN_BUTTON_BORDER_COLOR = new Color(252,255,245);
	public static final int MOUSE_IN_BUTTON_BORDER_THICKNESS = 2;
	public static final float MENU_SCREEN_SONG_INITIAL_VOLUME = -5.0f;
	public static final float MOUSE_IN_BUTTON_SOUND_INITIAL_VOLUME = -10.0f;
	//Variables
	private JFrame mainFrame;
  	private ImagePanel mainPanel;
  	private JLabel boomLogo_Boo,boomLogo_M;
  	private JButton startButton,instructionsButton,scoresButton;
  	private JButton settingsButton,creditsButton;
  	private String language;
  	private SoundClip menuScreenSong,mouseInButtonSound;
  	private JPanel buttonsPanel;
  	
  	public MenuScreen(JFrame mainFrame){
    	language = Language.ENGLISH;
    	mainFrame.repaint();
    	mainFrame.setVisible(true);
    	this.mainFrame = mainFrame;
	
    	mainPanel = new ImagePanel(ImagesPaths.SPACE_BACKGROUND,mainFrame);
    	mainPanel.setLayout(new BorderLayout());
    	
    	setSounds();
    	setLogo();
    	setMenu();
    	setText();
    	
    	new MenuScreenHandler(this);
    	
    	menuScreenSong.loop();
  		
    	this.mainFrame.add(mainPanel);
    	this.mainFrame.setVisible(true);
  	}
  	
  	/**
  	 * Initializes the background music of the menu screen and the sound that
  	 * plays when user hovers the mouse over the buttons.
  	 */
  	private void setSounds(){
  		menuScreenSong = new SoundClip(SoundsPaths.MENU_SCREEN_SONG);
  		menuScreenSong.setVolume(MENU_SCREEN_SONG_INITIAL_VOLUME);
  		mouseInButtonSound = new SoundClip(SoundsPaths.MOUSE_IN_BUTTON_SOUND);
  		mouseInButtonSound.setVolume(MOUSE_IN_BUTTON_SOUND_INITIAL_VOLUME);
  		
  		System.out.println(menuScreenSong.getVolume());
  		System.out.println(mouseInButtonSound.getVolume());
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

  	/**
  	 * Changes the look of the button given as a parameter, this method is 
  	 * supposed to be called when the user positions the mouse out of a button.
  	 * @param buttonToPaint - The button whose look will change.
  	 */
	public void paintButtonWhenMouseIsOut(JButton buttonToPaint){
		buttonToPaint.setBackground(MOUSE_OUT_BUTTON_BACKGROUND_COLOR);
		buttonToPaint.setForeground(MOUSE_OUT_BUTTON_FOREGROUND_COLOR);
		buttonToPaint.setBorder(BorderFactory.createLineBorder(
								MOUSE_OUT_BUTTON_BORDER_COLOR,
								MOUSE_IN_BUTTON_BORDER_THICKNESS));
	}
	
	/**
	 * Changes the look of button given as a parameter, this methods is supposed
	 * to be called when the user positions the mouse inside a button.
	 * @param buttonToPaint - The button whose look will change. 
	 */
	public void paintButtonWhenMouseIsIn(JButton buttonToPaint){
		buttonToPaint.setBorder(BorderFactory.createLineBorder(
								MOUSE_IN_BUTTON_BORDER_COLOR,
								MOUSE_IN_BUTTON_BORDER_THICKNESS));
	}
	
  	private void setMenu(){
		Font buttonsFont = new Font("Times New Roman",Font.PLAIN,55);
		//Creating the start button
    	startButton = new JButton();
    	startButton.setFont(buttonsFont);
    	paintButtonWhenMouseIsOut(startButton);
    	JPanel startButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    	startButtonPanel.setOpaque(false);
    	startButtonPanel.add(startButton);
    	
    	//Creating the instructions button
    	instructionsButton = new JButton();
		instructionsButton.setFont(buttonsFont);
		paintButtonWhenMouseIsOut(instructionsButton);
		JPanel instructionsButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		instructionsButtonPanel.setOpaque(false);
		instructionsButtonPanel.add(instructionsButton);

		//Creating the scores button
   		scoresButton = new JButton();
		scoresButton.setFont(buttonsFont);
		paintButtonWhenMouseIsOut(scoresButton);
		JPanel scoresButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		scoresButtonPanel.setOpaque(false);
		scoresButtonPanel.add(scoresButton);

		//Creating the settings button
    	settingsButton = new JButton();
    	settingsButton.setFont(buttonsFont);
    	paintButtonWhenMouseIsOut(settingsButton);
    	JPanel settingsButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    	settingsButtonPanel.setOpaque(false);
    	settingsButtonPanel.add(settingsButton);
    	settingsButton.setVisible(false);
		
    	//Creating the credits button
    	creditsButton = new JButton();
    	creditsButton.setFont(buttonsFont);
    	paintButtonWhenMouseIsOut(creditsButton);
    	JPanel creditsButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    	creditsButtonPanel.setOpaque(false);
    	creditsButtonPanel.add(creditsButton);
    	creditsButton.setVisible(false);
    	
    	//Creating the JPanel will hold all the buttons vertically
    	buttonsPanel = new JPanel();
    	BoxLayout buttonsLayout = new BoxLayout(buttonsPanel,BoxLayout.PAGE_AXIS);
    	buttonsPanel.setLayout(buttonsLayout);
    	buttonsPanel.setOpaque(false);
    	buttonsPanel.add(Box.createRigidArea(new Dimension(10,50)));
    	buttonsPanel.add(startButtonPanel);
    	buttonsPanel.add(instructionsButtonPanel);
    	buttonsPanel.add(scoresButtonPanel);
    	buttonsPanel.add(settingsButtonPanel);
    	buttonsPanel.add(creditsButtonPanel);
    	buttonsPanel.add(Box.createRigidArea(new Dimension(10,300)));
    	
    	mainPanel.add(buttonsPanel,BorderLayout.CENTER);
  	}

  	private void setText(){
    	if(language.equals(Language.SPANISH)){
    		startButton.setText("Jugar");
    		instructionsButton.setText("Instrucciones");
    		scoresButton.setText("Puntuaciones");
    		settingsButton.setText("Configuraciones");
    		creditsButton.setText("Créditos");
    		
    	}else if(language.equals(Language.ENGLISH)){
    		startButton.setText("Play ");
    		instructionsButton.setText("Instructions ");
    		scoresButton.setText("Scores      ");
    		settingsButton.setText("Settings       ");
    		creditsButton.setText("Credits ");
    		
    	}else if(language.equals(Language.FRENCH)){
    		startButton.setText("Jouer");
    		instructionsButton.setText("Instructions ");
    		scoresButton.setText("Scores      ");
    		settingsButton.setText("Configurations ");
    		creditsButton.setText("Crédits ");
    		
    	}
  	}
  	
  	public void removeMenuFromMainPanel(){
  		mainPanel.remove(buttonsPanel);
  		
  		mainFrame.repaint();
  		mainFrame.setVisible(true);
  	}
  	
  	public void removeAllComponents() {
  		menuScreenSong.stop();
  		
  		mainFrame.remove(mainPanel);
  		mainFrame.repaint();
  		mainFrame.setVisible(true);
  	}

  	public void playMouseInButtonSound() {
  		mouseInButtonSound.play();
  	}
  	
  	public String getLanguage() {
  		return language;
  	}
  	
  	public JButton getStartButton() {
  		return startButton;
  	}
  	
  	public JButton getInstructionsButton() {
  		return instructionsButton;
  	}
  	
  	public JButton getScoresButton() {
  		return scoresButton;
  	}
  	
  	public JButton getSettingsButton() {
  		return settingsButton;
  	}
  	
  	public JButton getCreditsButton() {
  		return creditsButton;
  	}
  	
  	public JFrame getMainFrame() {
  		return mainFrame;
  	}
  	
  	public SoundClip getMouseInButtonSound() {
  		return mouseInButtonSound;
  	}
  	
  	public ImagePanel getMainPanel(){
  		return mainPanel;
  	}
  	
  	public SoundClip getMenuSong(){
  		return menuScreenSong;
  	}
}
