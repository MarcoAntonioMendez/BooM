package user_interface;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import constants_across_classes.ImagesPaths;
import engine.ScoresManager;
import engine.WhiteFontMaker;

public class ScoresScreen {
	public static final int MEDAL_WIDTH = 100;
	public static final int MEDAL_HEIGHT = 100;
	private JPanel mainPanel;
	private JFrame mainFrame;
	private SoundClip buttonSound;
	private JButton goBackButton;
	private String goldenName,silverName,bronzeName;
	private int goldenScore,silverScore,bronzeScore;
	private JPanel goldenMedalPanel,goldenNameAndScorePanel,silverMedalPanel;
	private JPanel silverNameAndScorePanel,bronzeMedalPanel,bronzeNameAndScorePanel;
	private JPanel goBackButtonPanel;
	
	public ScoresScreen(JFrame mainFrame,JPanel mainPanel, SoundClip buttonSound,SoundClip menuSong){
		this.mainFrame = mainFrame;
		this.mainPanel = mainPanel;
		this.buttonSound = buttonSound;
		
		setUpScores();
		
		new ScoresScreenHandler(this,menuSong);
		
		this.mainPanel.repaint();
		this.mainFrame.setVisible(true);
	}
	
	private void setUpScores() {
		// Reading scores from txt files
		readScores();
		
		// Initializing goldenPanel
		setUpGoldenMedalAndGoldMedalHolder();
		
		// Initializing silverPanel
		setUpSilverMedalAndSilverMedalHolder();
		
		// Initializing bronzePanel
		setUpBronzeMedalAndSilverMedalHolder();
		
		// Initalizing goBackButton
		setUpGoBackButton();
		
		// Adding everything to mainPanel
		JPanel componentsPanel = new JPanel();
		BoxLayout layout = new BoxLayout(componentsPanel,BoxLayout.Y_AXIS);
		componentsPanel.setOpaque(false);
		componentsPanel.setLayout(layout);
		
		componentsPanel.add(Box.createRigidArea(new Dimension(10,20)));
		componentsPanel.add(goldenMedalPanel);
		componentsPanel.add(goldenNameAndScorePanel);
		componentsPanel.add(silverMedalPanel);
		componentsPanel.add(silverNameAndScorePanel);
		componentsPanel.add(bronzeMedalPanel);
		componentsPanel.add(bronzeNameAndScorePanel);
		componentsPanel.add(goBackButtonPanel);
		componentsPanel.add(Box.createRigidArea(new Dimension(10,40)));
		
		mainPanel.add(componentsPanel,BorderLayout.CENTER);
	}
	
	private void setUpGoBackButton() {
		Font buttonsFont = new Font("Times New Roman",Font.PLAIN,45);
		goBackButton = new JButton("Back");
		goBackButton.setFont(buttonsFont);
		paintButtonWhenMouseIsOut(goBackButton);
		
		goBackButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		goBackButtonPanel.setOpaque(false);
		goBackButtonPanel.add(goBackButton);
	}
	
	private void setUpBronzeMedalAndSilverMedalHolder() {
		// Creating medal
		ImageIcon icon = new ImageIcon(ImagesPaths.BRONZE_MEDAL_PATH);
		Image resImage = icon.getImage().getScaledInstance(MEDAL_WIDTH,
														   MEDAL_HEIGHT,
														   Image.SCALE_SMOOTH);
		icon.setImage(resImage);
		JLabel bronzeMedalLabel = new JLabel(icon);
		bronzeMedalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		bronzeMedalPanel.setOpaque(false);
		bronzeMedalPanel.add(bronzeMedalLabel);
		
		// Initializing goldenName and goldenScore
		ArrayList<Component> components = WhiteFontMaker.getComponents(bronzeName,bronzeScore);
		bronzeNameAndScorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		bronzeNameAndScorePanel.setOpaque(false);
		for(int x = 0; x < components.size(); x++){
			bronzeNameAndScorePanel.add(components.get(x));
		}
	}
	
	private void setUpSilverMedalAndSilverMedalHolder() {
		// Creating medal
		ImageIcon icon = new ImageIcon(ImagesPaths.SILVER_MEDAL_PATH);
		Image resImage = icon.getImage().getScaledInstance(MEDAL_WIDTH,
														   MEDAL_HEIGHT,
														   Image.SCALE_SMOOTH);
		icon.setImage(resImage);
		JLabel silverMedalLabel = new JLabel(icon);
		silverMedalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		silverMedalPanel.setOpaque(false);
		silverMedalPanel.add(silverMedalLabel);
		
		// Initializing goldenName and goldenScore
		ArrayList<Component> components = WhiteFontMaker.getComponents(silverName,silverScore);
		silverNameAndScorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		silverNameAndScorePanel.setOpaque(false);
		for(int x = 0; x < components.size(); x++){
			silverNameAndScorePanel.add(components.get(x));
		}
	}
	
	private void setUpGoldenMedalAndGoldMedalHolder() {
		// Creating medal
		ImageIcon icon = new ImageIcon(ImagesPaths.GOLDEN_MEDAL_PATH);
		Image resImage = icon.getImage().getScaledInstance(MEDAL_WIDTH,
														   MEDAL_HEIGHT,
														   Image.SCALE_SMOOTH);
		icon.setImage(resImage);
		JLabel goldMedalLabel = new JLabel(icon);
		goldenMedalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		goldenMedalPanel.setOpaque(false);
		goldenMedalPanel.add(goldMedalLabel);
		
		// Initializing goldenName and goldenScore
		ArrayList<Component> components = WhiteFontMaker.getComponents(goldenName,goldenScore);
		goldenNameAndScorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		goldenNameAndScorePanel.setOpaque(false);
		for(int x = 0; x < components.size(); x++){
			goldenNameAndScorePanel.add(components.get(x));
		}
	}
	
	private void readScores() {
		// Getting golden name and score
		String goldenFile = ScoresManager.readRankingFile(
										  ScoresManager.GOLDEN_SCORE_FILE_NAME);
		goldenName = ScoresManager.getNameFromTextLine(goldenFile);
		goldenScore = ScoresManager.getScoreFromTextLine(goldenFile);
		
		// Getting silver name and score
		String silverFile = ScoresManager.readRankingFile(
										  ScoresManager.SILVER_SCORE_FILE_NAME);
		silverName = ScoresManager.getNameFromTextLine(silverFile);
		silverScore = ScoresManager.getScoreFromTextLine(silverFile);
		
		// Getting bronze name and score
		String bronzeFile = ScoresManager.readRankingFile(
										  ScoresManager.BRONZE_SCORE_FILE_NAME);
		bronzeName = ScoresManager.getNameFromTextLine(bronzeFile);
		bronzeScore = ScoresManager.getScoreFromTextLine(bronzeFile);
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
	
	public JButton getGoBackButton() {
		return goBackButton;
	}
	
	public void playButtonSound() {
		buttonSound.play();
	}
	
	public JFrame getMainFrame() {
		return mainFrame;
	}
}
