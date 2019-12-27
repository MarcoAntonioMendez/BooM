package user_interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import characters.Demon;
import constants_across_classes.ImagesPaths;
import constants_across_classes.Language;
import engine.ImageChangingLabel;

public class InstructionsScreen {
	//Constants
	public static final Color INSTRUCTIONS_FOREGROUND_COLOR = new Color(237,237,237);
	//Variables
	private String language;
	private JLabel firstInstruction,secondInstruction,thirdInstruction,fourthInstruction;
	private JPanel instructionsPanel,demonPanel;
	private ImagePanel mainPanel;
	private JFrame mainFrame;
	private Cursor off_peephole,on_peephole,shootPeephole;
	private ImageChangingLabel demon,deadDemon;
	private volatile boolean toContinue;
	private InstructionsScreenHandler handler;
	private JButton goBackButton;
	private SoundClip menuSong;
	
	public InstructionsScreen(JFrame mainFrame,ImagePanel mainPanel,String language,SoundClip menuSong){
		this.language = language;
		this.mainPanel = mainPanel;
		this.mainFrame = mainFrame;
		this.menuSong = menuSong;
		toContinue = true;
		setInstructions();
		setText();
		initializeCursors();
		
		handler = new InstructionsScreenHandler(this);
		
		start();
	}
	
	private void start() {
		Runnable runnable = new Runnable() {
			@Override
			public void run(){
				while(toContinue){
					try {Thread.sleep(200);}catch(InterruptedException e){e.printStackTrace();}
					
					if(handler.getDemonDeath()){
						demonPanel.removeAll();
						demonPanel.add(deadDemon);
					}else {
						demonPanel.removeAll();
						demonPanel.add(demon);
					}
					
					mainFrame.requestFocus();
					mainPanel.repaint();
					mainFrame.repaint();
					mainFrame.setVisible(true);
					
					if(handler.getDemonFound()){
						setCursorToOnPeephole();
					}else {
						setCursorToOffPeephole();
					}
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}
	
	private void setInstructions() {
		Font instructionsFont = new Font("Times New Roman",Font.PLAIN,45);
		//Creating firstInstruction
		firstInstruction = new JLabel();
		firstInstruction.setFont(instructionsFont);
		firstInstruction.setForeground(INSTRUCTIONS_FOREGROUND_COLOR);
		JPanel firstInstructionPanel = new JPanel();
		firstInstructionPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		firstInstructionPanel.setOpaque(false);
		firstInstructionPanel.add(firstInstruction);
		
		//Creating the secondInstruction
		secondInstruction = new JLabel();
		secondInstruction.setFont(instructionsFont);
		secondInstruction.setForeground(INSTRUCTIONS_FOREGROUND_COLOR);
		JPanel secondInstructionPanel = new JPanel();
		secondInstructionPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		secondInstructionPanel.setOpaque(false);
		secondInstructionPanel.add(secondInstruction);
		
		// Creating the thirdInstruction
		thirdInstruction = new JLabel();
		thirdInstruction.setFont(instructionsFont);
		thirdInstruction.setForeground(INSTRUCTIONS_FOREGROUND_COLOR);
		JPanel thirdInstructionPanel = new JPanel();
		thirdInstructionPanel.setLayout(new FlowLayout());
		thirdInstructionPanel.setOpaque(false);
		thirdInstructionPanel.add(thirdInstruction);
		
		// Creating the demon
		String[] demonImages = {ImagesPaths.PINKY_IDLE_FRAME_1,
				                ImagesPaths.PINKY_IDLE_FRAME_2,
				                ImagesPaths.PINKY_IDLE_FRAME_3,
				                ImagesPaths.PINKY_IDLE_FRAME_4};
		demon = new ImageChangingLabel(demonImages,100,Demon.DEMON_WIDTH,
													   Demon.DEMON_HEIGHT);
		demon.start();
		demonPanel = new JPanel(new FlowLayout());
		demonPanel.setOpaque(false);
		demonPanel.add(demon);
		String[] deadDemonImages = {ImagesPaths.PINKY_DEATH_FRAME_1,
                					ImagesPaths.PINKY_DEATH_FRAME_2,
                					ImagesPaths.PINKY_DEATH_FRAME_3,
                					ImagesPaths.PINKY_DEATH_FRAME_4,
                					ImagesPaths.PINKY_DEATH_FRAME_5,
                					ImagesPaths.PINKY_DEATH_FRAME_6};
		deadDemon = new ImageChangingLabel(deadDemonImages,200,Demon.DEMON_WIDTH,
									                           Demon.DEMON_HEIGHT);
		deadDemon.setLapsToTravel(1);
		
		// Creating goBackButton
		Font buttonsFont = new Font("Times New Roman",Font.PLAIN,45);
		goBackButton = new JButton("Back");
		goBackButton.setFont(buttonsFont);
		paintButtonWhenMouseIsOut(goBackButton);
		
		JPanel goBackButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		goBackButtonPanel.setOpaque(false);
		goBackButtonPanel.add(goBackButton);
		
		// Creating the fourth instruction
		fourthInstruction = new JLabel();
		fourthInstruction.setFont(instructionsFont);
		fourthInstruction.setForeground(INSTRUCTIONS_FOREGROUND_COLOR);
		JPanel fourthInstructionPanel = new JPanel(new FlowLayout());
		fourthInstructionPanel.setOpaque(false);
		fourthInstructionPanel.add(fourthInstruction);
		
		//Initializing the insutructionsPanel
		instructionsPanel = new JPanel();
		instructionsPanel.setOpaque(false);
		BoxLayout layout = new BoxLayout(instructionsPanel,BoxLayout.Y_AXIS);
		instructionsPanel.setLayout(layout);
		
		//Adding all the components to the instructionsPanel
		instructionsPanel.add(Box.createRigidArea(new Dimension(10,100)));
		instructionsPanel.add(firstInstructionPanel);
		instructionsPanel.add(secondInstructionPanel);
		instructionsPanel.add(thirdInstructionPanel);
		instructionsPanel.add(fourthInstructionPanel);
		instructionsPanel.add(demonPanel);
		instructionsPanel.add(goBackButtonPanel);
		
		mainPanel.add(instructionsPanel,BorderLayout.CENTER);
	}
	
	private void setText(){
		if(language.equals(Language.SPANISH)){
			firstInstruction.setText("Habrá varias hordas de demonios. Por cada horda tendrás un tiempo límite.");
			secondInstruction.setText("Tu objetivo será matar a todos los demonios por horda antes de que el tiempo acabe.");
			thirdInstruction.setText("Para matar a un demonio, mueve tu mouse hasta apuntarlo y presiona la barra espaciadora");
			fourthInstruction.setText("(Mata al demonio para regresar al menú)");
			
		}else if(language.equals(Language.ENGLISH)){
			firstInstruction.setText("Demons will appear in the territory         ");
			
		}else if(language.equals(Language.FRENCH)){
			firstInstruction.setText("Les démons vont apparaître sur le territoire");
		}
	}
	
	private void initializeCursors(){
		off_peephole =  new Cursor(Cursor.CROSSHAIR_CURSOR);
		
		// Initializing the on_peephole cursor		
		on_peephole = Toolkit.getDefaultToolkit().createCustomCursor(
						new ImageIcon(ImagesPaths.ON_PEEPHOLE_PATH).getImage(),
						new Point(0,0),
						"");
		// Inictializing the shootPeephole cursos
		shootPeephole = Toolkit.getDefaultToolkit().createCustomCursor(
						new ImageIcon(ImagesPaths.SHOOT_PEEPHOLE_PATH).getImage(),
						new Point(0,0),
						"");
		
		setCursorToOffPeephole();
	}
	
	/**
	 * When user is not pointing a demon, this cursor shows.
	 */
	public void setCursorToOffPeephole(){
		mainFrame.setCursor(off_peephole);
	}
	
	/**
	 * When user is pointing a demon, this cursor shows.
	 */
	public void setCursorToOnPeephole(){
		mainFrame.setCursor(on_peephole);
	}
	
	/**
	 * When user shoots a demon, this cursos show just for a fraction
	 */
	public void setCursusToShoot() {
		mainFrame.setCursor(shootPeephole);
	}
	
	public ImageChangingLabel getDemon() {
		return demon;
	}
	
	public void stopInstructions() {
		toContinue = false;
	}
	
	public JFrame getMainFrame() {
		return mainFrame;
	}
	
	public ImageChangingLabel getDeadDemon() {
		return deadDemon;
	}
	
	public JButton getGoBackButton() {
		return goBackButton;
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
	
	public SoundClip getMenuSong() {
		return menuSong;
	}
}
