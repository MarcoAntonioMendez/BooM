package user_interface;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import characters.Demon;
import characters.DoomSlayer;
import engine.WavesDispatcher;
import sounds.SoundsPaths;

public class PlayScreenHandler implements MouseListener,KeyListener{
	private volatile ArrayList<Demon> demons;
	private volatile ArrayList<JPanel> demonsHolder;
	private volatile int pointedDemon;
	private volatile boolean demonFound;
	private PlayScreen screen;
	private WavesDispatcher wavesDispatcher;
	private volatile DoomSlayer doomSlayer;
	private SoundClip shotgunSoundEffect,demonDeathSound;
	
	public PlayScreenHandler(PlayScreen screen,WavesDispatcher wavesDispatcher){
		this.screen = screen;
		this.wavesDispatcher = wavesDispatcher;
		this.doomSlayer = screen.getDoomSlayer();
		screen.getMainFrame().addKeyListener(this);
		screen.getMainFrame().addMouseListener(this);
		demonFound = false;
		shotgunSoundEffect = new SoundClip(SoundsPaths.SHOTGUN_SOUND);
		shotgunSoundEffect.setVolume(-20.0f);
		demonDeathSound = new SoundClip(SoundsPaths.DEMON_DEATH_SCREAM);
		demonDeathSound.setVolume(-10.0f);
		
		demons = screen.getDemons();
		demonsHolder = screen.getDemonsPanelsHolder();
		
		for(int x = 0; x < demonsHolder.size(); x++){
			demonsHolder.get(x).addMouseListener(this);
		}
	}
	
	public synchronized void activateDemon(Demon demon){
		demon.getIdleAnimation().addMouseListener(this);
		demon.getScreamAnimation().addMouseListener(this);
	}
	
	public synchronized boolean getDemonFound() {
		return demonFound;
	}
	
	@Override
	public void keyPressed(KeyEvent event) {
		
		
	}
	
	@Override
	public synchronized void keyReleased(KeyEvent event){
		if(event.getKeyCode() == KeyEvent.VK_SPACE){
			if(demons.get(pointedDemon).isActive() && demonFound){
				// Hurting the demon
				demons.get(pointedDemon).receiveAttack(doomSlayer.getGunPower());
				
				// Showing the user he/she shot a demon, by flicking the cursor
				// and playing the shotfun sound
				screen.setCursusToShoot();
				shotgunSoundEffect.stop();
				shotgunSoundEffect.play();
				
				// Chekcing if the demon was killed
				if(!demons.get(pointedDemon).isAlive() && 
				   (demons.get(pointedDemon).getState()!=Demon.DEATH_STATE)){
					
					
					demons.get(pointedDemon).setState(Demon.DEATH_STATE);
					demonDeathSound.play();
					wavesDispatcher.setDemonsAlive(
										  (wavesDispatcher.getDemonsAlive()-1));
					
					// Updating the doom slayer score
					int pointsEarned = calculateScore(demons.get(pointedDemon));
					doomSlayer.setScore((doomSlayer.getScore()+pointsEarned));
					System.out.println("Score: "+doomSlayer.getScore());
				}
			}
		}
	}

	private int calculateScore(Demon demon){
		return demon.getScoreValue()-demon.getTimeItTookToBeKilled();
	}
	
	@Override
	public void keyTyped(KeyEvent event) {
		
	}
	
	@Override
	public void mouseClicked(MouseEvent event) {
		
	}

	@Override
	public synchronized void mouseEntered(MouseEvent event){
		demonFound = false;
		for(int x = 0; x < demons.size(); x++){
			if(event.getSource() == demons.get(x).getIdleAnimation()){
				pointedDemon = x;
				demonFound = true;
				break;
			}else if(event.getSource()==demons.get(x).getAppearanceAnimation()){
				pointedDemon = x;
				demonFound = true;
				break;
			} 	
		}
	}

	@Override
	public void mouseExited(MouseEvent event){
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		                   
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}
	
	public void setDemonFound(boolean demonFound){
		this.demonFound = demonFound;
	}
	
	public int getPointedDemon() {
		return pointedDemon;
	}
}
