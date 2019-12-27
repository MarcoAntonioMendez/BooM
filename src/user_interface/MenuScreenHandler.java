/*class: MenuScreenHandler
Purpose: This class will handle all the events that happen in the MenuScreen
        
Last Modified: 30/09/2019 */
package user_interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JFrame;

public class MenuScreenHandler implements ActionListener,MouseListener{
	//Variables
	private JButton startButton,instructionsButton,scoresButton;
  	private JButton settingsButton,creditsButton;
	private String language;
	private MenuScreen screen;
	private JFrame mainFrame;
	private ImagePanel mainPanel;
	
	public MenuScreenHandler(MenuScreen screen){
		this.screen = screen;
		
		language = screen.getLanguage();
		mainFrame = screen.getMainFrame();
		mainPanel = screen.getMainPanel();
		prepareButtons(screen);
	}
	
	// DESCRIPTION: removes all components from MenuScreen and prepares
	// everything to start playing the game.
	private void startGame() {
		screen.removeAllComponents();
		
		new PlayScreen(mainFrame,screen.getMouseInButtonSound());
	}
	
	private void showInstructionsScreen() {
		screen.removeMenuFromMainPanel();
		
		new InstructionsScreen(mainFrame,mainPanel,language,screen.getMenuSong());
	}
	
	private void showScoresScreen() {
		screen.removeMenuFromMainPanel();
		
		new ScoresScreen(screen.getMainFrame(),screen.getMainPanel(),
						 screen.getMouseInButtonSound(),
						 screen.getMenuSong());
	}
	
	private void showSettingsScreen() {
		
	}
	
	private void showCreditsScreen() {
		
	}
	
	@Override
	public void actionPerformed(ActionEvent event){
		screen.playMouseInButtonSound();
		if(event.getSource() == startButton){
			startGame();
			
		}else if(event.getSource() == instructionsButton){
			showInstructionsScreen();
			
		}else if(event.getSource() == scoresButton){
			showScoresScreen();
			
		}else if(event.getSource() == settingsButton){
			showSettingsScreen();
			
		}else if(event.getSource() == creditsButton){
			showCreditsScreen();
			
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent event){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent event){
		if(event.getSource() == startButton){
			screen.paintButtonWhenMouseIsIn(startButton);
			screen.playMouseInButtonSound();
			
		}else if(event.getSource() == instructionsButton){
			screen.paintButtonWhenMouseIsIn(instructionsButton);
			screen.playMouseInButtonSound();
			
		}else if(event.getSource() == scoresButton){
			screen.paintButtonWhenMouseIsIn(scoresButton);
			screen.playMouseInButtonSound();
			
		}else if(event.getSource() == settingsButton){
			screen.paintButtonWhenMouseIsIn(settingsButton);
			screen.playMouseInButtonSound();
			
		}else if(event.getSource() == creditsButton){
			screen.paintButtonWhenMouseIsIn(creditsButton);
			screen.playMouseInButtonSound();
		}
		
	}

	@Override
	public void mouseExited(MouseEvent event){
		if(event.getSource() == startButton){
			screen.paintButtonWhenMouseIsOut(startButton);
			
		}else if(event.getSource() == instructionsButton){
			screen.paintButtonWhenMouseIsOut(instructionsButton);
			
		}else if(event.getSource() == scoresButton){
			screen.paintButtonWhenMouseIsOut(scoresButton);
			
		}else if(event.getSource() == settingsButton){
			screen.paintButtonWhenMouseIsOut(settingsButton);
			
		}else if(event.getSource() == creditsButton){
			screen.paintButtonWhenMouseIsOut(creditsButton);
		}
		
	}

	@Override
	public void mousePressed(MouseEvent event){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent event){
		// TODO Auto-generated method stub
		
	}
	
	private void prepareButtons(MenuScreen screen){
		startButton = screen.getStartButton();
		startButton.addActionListener(this);
		startButton.addMouseListener(this);
		
		instructionsButton = screen.getInstructionsButton();
		instructionsButton.addActionListener(this);
		instructionsButton.addMouseListener(this);
		
		scoresButton = screen.getScoresButton();
		scoresButton.addActionListener(this);
		scoresButton.addMouseListener(this);
		
		settingsButton = screen.getSettingsButton();
		settingsButton.addActionListener(this);
		settingsButton.addMouseListener(this);
		
		creditsButton = screen.getCreditsButton();
		creditsButton.addActionListener(this);
		creditsButton.addMouseListener(this);
	}
}
