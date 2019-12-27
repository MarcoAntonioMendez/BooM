package user_interface;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

import characters.Demon;
import constants_across_classes.ImagesPaths;
import engine.ImageChangingLabel;
import sounds.SoundsPaths;

public class InstructionsScreenHandler implements ActionListener,MouseListener,KeyListener{
	public static final int TIMES_SHOTGUN_NEEDS_TO_BE_FIRED = 6;
	private InstructionsScreen screen;
	private ImageChangingLabel demon;
	private volatile boolean demonFound;
	private volatile SoundClip shotgunSound,menuSong;
	private volatile int timesShotgunFired;
	private JButton goBackButton;
	private JFrame mainFrame;
	
	public InstructionsScreenHandler(InstructionsScreen screen){
		this.screen = screen;
		this.mainFrame = screen.getMainFrame();
		goBackButton = screen.getGoBackButton();
		demon = this.screen.getDemon();
		demonFound = false;
		shotgunSound = new SoundClip(SoundsPaths.SHOTGUN_SOUND);
		shotgunSound.setVolume(-20.0f);
		timesShotgunFired = 0;
		menuSong = screen.getMenuSong(); 
		
		this.screen.getMainFrame().addKeyListener(this);
		goBackButton.addActionListener(this);
		goBackButton.addMouseListener(this);
		
		demon.addMouseListener(this);
	}
	
	public boolean getDemonFound() {
		return demonFound;
	}

	@Override
	public void actionPerformed(ActionEvent event){
		if(event.getSource() == goBackButton){
			if(getDemonDeath()){
				screen.stopInstructions();
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
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stu
	}

	@Override
	public void mouseEntered(MouseEvent event){
		if(event.getSource() == demon){
			screen.setCursorToOnPeephole();
			demonFound = true;
		}else if(event.getSource() == goBackButton){
			screen.paintButtonWhenMouseIsIn(goBackButton);
		}
	}

	@Override
	public void mouseExited(MouseEvent event){
		if(event.getSource() == demon){
			screen.setCursorToOffPeephole();
			demonFound = false;
		}else if(event.getSource() == goBackButton){
			screen.paintButtonWhenMouseIsOut(goBackButton);
		}
	}

	@Override
	public void mousePressed(MouseEvent event){
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent event){
		if(event.getKeyCode() == KeyEvent.VK_SPACE && demonFound){
			screen.setCursusToShoot();
			shotgunSound.stop();
			shotgunSound.play();
			timesShotgunFired++;
			if(timesShotgunFired == TIMES_SHOTGUN_NEEDS_TO_BE_FIRED){
				screen.getDeadDemon().start();
			}
		}
	}

	public boolean getDemonDeath() {
		if(timesShotgunFired >= TIMES_SHOTGUN_NEEDS_TO_BE_FIRED){
			return true;
		}
		return false;
	}
	
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}	
