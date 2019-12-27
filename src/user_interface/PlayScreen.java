package user_interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import characters.Demon;
import characters.DoomSlayer;
import constants_across_classes.ImagesPaths;
import engine.ImageChangingLabel;
import engine.ScorePanel;
import engine.WaveTimer;
import engine.WavesDispatcher;
import sounds.SoundsPaths;

public class PlayScreen {
	public static final int COUNT_DOWN_IMGS_WIDTH = 88;
	public static final int COUNT_DOWN_IMGS_HEIGHT = 209;
	public static final int DEMON_GRID_ROWS = 5;
	public static final int DEMON_GRID_COLUMNS = 10;
	public static final String[] PLAY_SONGS = {SoundsPaths.RIP_AND_TEAR,
											   SoundsPaths.RUST_DUST_AND_GUTS,
											   SoundsPaths.FLESH_AND_METAL,
											   SoundsPaths.BFG_DIVISION,
											   SoundsPaths.DAMNATION,
											   SoundsPaths.SKULLHACKER,
											   SoundsPaths.MASTERMIND};
	private JFrame mainFrame;
	private ImagePanel mainPanel;
	private ImageChangingLabel countDownAnimation;
	private SoundClip beepSound,playSong,demonScream,endGameSong;
	private Cursor off_peephole,on_peephole,shootPeephole;
	private DoomSlayer doomSlayer;
	private volatile ArrayList<Demon> demons;
	public  volatile ArrayList<JPanel> demonsPanelsHolder;
	private volatile ArrayList<String> availableCells;
	private volatile WavesDispatcher wavesDispatcher;
	private volatile PlayScreenHandler handler;
	private volatile JPanel ultimatePanel,playerPanel;
	private volatile WaveTimer waveTimer;
	private volatile ScorePanel scorePanel;
	private volatile boolean ultimatePanelCompletelySet;
	private Random random;
	
	public PlayScreen(JFrame mainFrame,SoundClip beepSound){
		this.mainFrame = mainFrame;
		this.beepSound = beepSound;
		
		mainPanel = new ImagePanel(ImagesPaths.SPACE_BACKGROUND,this.mainFrame);
		mainPanel.setLayout(new BorderLayout());
		this.mainFrame.add(mainPanel);
		
		doomSlayer = new DoomSlayer();
		random = new Random();
		wavesDispatcher = new WavesDispatcher();
		availableCells = new ArrayList<String>();
		ultimatePanelCompletelySet = false;
		demonScream = new SoundClip(SoundsPaths.DEMONS_LAUGH);
		endGameSong = new SoundClip(SoundsPaths.AT_DOOMS_GATE);
		
		for(int x = 0; x < (DEMON_GRID_ROWS*DEMON_GRID_COLUMNS); x++) {
			availableCells.add(String.valueOf(x));
		}
		
		initializeCursors();
		prepareCountDownAnimation();
		showScreen();
	}
	
	/**
	 * Initializes the two cursor that will be used in the game, the one that
	 * appears when user is pointing a demon and the one when it is not.
	 */
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
	 * Initializes the countDownAnimation object, specifying the width, height,
	 * the images that form the animation and times it will play.
	 * At the end, the animation is played.
	 */
	private void prepareCountDownAnimation() {
		String[] countDownImgsPath = {ImagesPaths.COUNT_DOWN_ANIMATION_3,
									  ImagesPaths.COUNT_DOWN_ANIMATION_2,
									  ImagesPaths.COUNT_DOWN_ANIMATION_1};
		
		countDownAnimation = new ImageChangingLabel(countDownImgsPath,1000,
												   COUNT_DOWN_IMGS_WIDTH,
												   COUNT_DOWN_IMGS_HEIGHT);
		countDownAnimation.setLapsToTravel(1);
		
		// Creating a panel to deposit countDownAnimation
		JPanel countDownAnimationPanel = new JPanel(new FlowLayout());
		countDownAnimationPanel.setOpaque(false);
		countDownAnimationPanel.add(countDownAnimation);
		
		// Adding countDwnAnimationPanel
		mainPanel.add(Box.createRigidArea(new Dimension(10,300)),
										  BorderLayout.NORTH);
		mainPanel.add(countDownAnimationPanel,BorderLayout.CENTER);
		
		startCountDownAnimation();
	}
	
	/**
	 * Starts the count down animation (3,2,1). Every time the number changes
	 * a "beep" sound will play.
	 */
	private void startCountDownAnimation() {
		countDownAnimation.start();
		
		Runnable myRunnable = new Runnable() {
			public void run() {
				while(countDownAnimation.isActive()){
					try {Thread.sleep(1000);}catch (InterruptedException e){e.printStackTrace();}
					beepSound.play();
				}
				try {Thread.sleep(1000);}catch (InterruptedException e){e.printStackTrace();}
				mainPanel.removeAll();
				startGame();
			}
		};
		
		Thread thread = new Thread(myRunnable);
		thread.start();
		
		//Preparing everything
		Runnable myRunnable2 = new Runnable() {
			public void run() {
				demonsPanelsHolder = new ArrayList<JPanel>();
				demons = new ArrayList<Demon>();
						
				JPanel demonPanel = null;
				ultimatePanel = new JPanel();
				ultimatePanel.setOpaque(false);
				ultimatePanel.setLayout(new GridLayout(DEMON_GRID_ROWS,
													   DEMON_GRID_COLUMNS));
				Demon demon = null;
				int counter = 0;
				for(int x = 0; x < DEMON_GRID_ROWS; x++) {
					for(int y = 0; y < DEMON_GRID_COLUMNS; y++) {
						demonPanel = new JPanel();
						demonPanel.setOpaque(false);
						demonsPanelsHolder.add(demonPanel);
								
						demon = new Demon((counter));
						demons.add(demon);
						counter++;
								
						ultimatePanel.add(demonPanel);
					}
				}
				ultimatePanelCompletelySet = true;
			}
		};
		Thread thread2 = new Thread(myRunnable2);
		thread2.start();
	}
	
	/**
	 * Starts dispatching the demon waves, and it keeps checking if player is
	 * still alive.
	 */
	private synchronized void startGame(){
		playerPanel = new JPanel();
		playerPanel.setOpaque(false);
		scorePanel = new ScorePanel(ImagesPaths.NUMBERS_IN_DOOM_FONT,doomSlayer);
		playerPanel.add(scorePanel);
		
		playerPanel.add(Box.createRigidArea(new Dimension(200,10)));
		
		waveTimer = new WaveTimer(ImagesPaths.NUMBERS_IN_DOOM_FONT);
		playerPanel.add(waveTimer);
		
		Runnable myRunnable3 = new Runnable() {
			public void run() {
				boolean toContinue;
				while(!ultimatePanelCompletelySet){
					try {Thread.sleep(1);}catch (InterruptedException e){e.printStackTrace();}
				}
				playSong = decideWhichSongToPlay();
				mainPanel.add(ultimatePanel,BorderLayout.CENTER);
				mainPanel.add(playerPanel,BorderLayout.SOUTH);
				waveTimer.start();
				playSong.setVolume(-9.0f);
				playSong.loop();
				toContinue = true;
				// Game Loop
				while(toContinue){
					mainFrame.requestFocus();
					try {Thread.sleep(200);}catch (InterruptedException e){e.printStackTrace();}
					
					wavesDispatcher.setWave(demons,demonsPanelsHolder,waveTimer
							                ,availableCells,handler);
					updateDemons();
					doomSlayer.update();
					scorePanel.update();
					showScreen();
					
					//Checking if user spot a demon
					if(handler.getDemonFound()){
						setCursorToOnPeephole();
					}else{
						setCursorToOffPeephole();
					}
					toContinue = (!waveTimer.isOver());
				}
				endGame();
			}
		};
		
		handler = new PlayScreenHandler(this,wavesDispatcher);
		
		Thread thread3 = new Thread(myRunnable3);
		thread3.start();
	}
	
	private synchronized void updateDemons(){
		int demonCell;
		for(int x = 0; x < demons.size(); x++){
			demons.get(x).update(availableCells,demonScream,handler);
			
			if(demons.get(x).isActive()){
				demonCell = demons.get(x).getCell();
				demonsPanelsHolder.get(demonCell).removeAll();
				demonsPanelsHolder.get(demonCell).add(
											   demons.get(x).getCurrentState());
			}
			
			demonsPanelsHolder.get(x).setBackground(null);
		}
	}
	
	/**
	 * Plays the death animation and it shows the final score.
	 */
	@SuppressWarnings("deprecation")
	private void endGame(){
		int demonCell;
		playSong.stop();
		// Updating the demons that are alive to make them scream
		// Almost laughing at you because you were not able to kill them
		for(int x = 0; x < demons.size(); x++){
			demons.get(x).setState(Demon.SCREAM_STATE);
			demons.get(x).update(availableCells,demonScream,handler	);
			if(demons.get(x).isActive() && demons.get(x).isAlive()){
				
				demonCell = demons.get(x).getCell();
				
				demonsPanelsHolder.get(demonCell).removeAll();
				demonsPanelsHolder.get(demonCell).add(
											   demons.get(x).getCurrentState());
			
			}
			demonsPanelsHolder.get(x).setBackground(null);
		}
		showScreen();
		endGameSong.play();
		demonScream.play();
		int count = 0;
		while(endGameSong.getMicroSecondsPosition() < (12*1000000)){
			// Making demons laugh
			if(count == 2000000){
				demonScream.play();
				count = 0;
			}else {
				count++;
			}
			
		}
		mainPanel.removeAll();
		mainFrame.setCursor(Cursor.DEFAULT_CURSOR);
		// Starting the screen where
		new GameOverScreen(mainFrame,mainPanel,doomSlayer,
						   waveTimer.getNumbers(),waveTimer.getNumbersImages(),
						   endGameSong);
	}
	
	/**
	 * Refreshes the screen.
	 */
	public void showScreen() {
		mainPanel.repaint();
		mainFrame.repaint();
		mainFrame.setVisible(true);
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
	
	public JFrame getMainFrame() {
		return mainFrame;
	}
	
	public ArrayList<Demon> getDemons(){
		return demons;
	}
	
	public DoomSlayer getDoomSlayer() {
		return doomSlayer;
	}
	
	public SoundClip decideWhichSongToPlay() {
		int chosenIndex = random.nextInt(PLAY_SONGS.length);
		return new SoundClip(PLAY_SONGS[chosenIndex]);
	}
	
	public ArrayList<JPanel> getDemonsPanelsHolder(){
		return demonsPanelsHolder;
	}
}
