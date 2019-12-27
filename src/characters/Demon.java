package characters;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JLabel;

import constants_across_classes.ImagesPaths;
import engine.ImageChangingLabel;
import user_interface.PlayScreen;
import user_interface.PlayScreenHandler;
import user_interface.SoundClip;

public class Demon{
	private static final long serialVersionUID = 1L;
	public static final float TIME_CONSTANT = 0.05f;
	public static final int DEMON_WIDTH = 100;
	public static final int DEMON_HEIGHT = 100;
	public static final int DEMON_APPEAR_WAIT_TIME = 100;
	public static final int PINKY_DEMON_TYPE = 0;
	public static final int SOLDIER_DEMON_TYPE = 1;
	public static final int LOST_SOUL_DEMON_TYPE = 2;
	public static final int HELL_KNIGHT_DEMON_TYPE = 3;
	public static final int BARON_OF_HELL_DEMON_TYPE = 4;
	public static final int REVENANT_DEMON_TYPE = 5;
	public static final int CYBER_DEMON_TYPE = 6;
	public static final int[] DEMON_TYPES = {PINKY_DEMON_TYPE,SOLDIER_DEMON_TYPE
			                                ,HELL_KNIGHT_DEMON_TYPE,
			                                LOST_SOUL_DEMON_TYPE,
			                                BARON_OF_HELL_DEMON_TYPE,
			                                REVENANT_DEMON_TYPE,
			                                CYBER_DEMON_TYPE};
	public static final int[] DEMONS_HEALTH = {100,200,150,400,500,300,700};
	public static final int[] DEMONS_SCORE_VALUE = {1000,2000,1500,4000,5000,3000,7000};
	public static final int IDLE_STATE = 0;
	public static final int APPEAR_STATE = 1;
	public static final int SCREAM_STATE = 2;
	public static final int ATTACK_STATE = 3;
	public static final int DEATH_STATE = 4;
	
	protected volatile int health,state,cell,aiConstant,timeItTookToBeKilled;
	protected volatile int scoreValue,posInArray;
	protected volatile boolean active,demonBegun,deathPlayed;
	protected volatile int allowedConsecutiveShots,consecutiveShots,demonType;
	protected ImageChangingLabel appearanceAnim,idleAnim,attackAnim,deathAnim;
	private ImageChangingLabel stunnedAnim,screamAnim;
	private Random random;
	
	public Demon(int posInArray){
		random = new Random();
		consecutiveShots = 0;
		allowedConsecutiveShots = 0;
		timeItTookToBeKilled = 0;
		demonBegun = false;
		this.posInArray = posInArray;
		
		health = -1;
	}
	
	public void activateDemon(int demonType,int aiConstant){
		active = true;
		deathPlayed = false;
		this.demonType = demonType;
		this.aiConstant = aiConstant;
		health = DEMONS_HEALTH[demonType];
		scoreValue = DEMONS_SCORE_VALUE[demonType];
		setAllAnimations();
		state = APPEAR_STATE;
	}
	
	public void deactivateDemon(){
		active = false;
	}
	
	public int getScoreValue() {
		return scoreValue;
	}
	
	public void receiveAttack(int attack){
		health -= (attack);
		consecutiveShots++;
		
	}
	
	private void flick() {
		switch(state){
		case IDLE_STATE:
			idleAnim.setBackground(Color.WHITE);
		case APPEAR_STATE:
			//appearanceAnim.setBackground(Color.WHITE);
		case SCREAM_STATE:
			//screamAnim.setBackground(Color.WHITE);
		case ATTACK_STATE:
			//attackAnim.setBackground(Color.WHITE);
		case DEATH_STATE:
			
		default:
			idleAnim.setBackground(Color.WHITE);
		}
	}
	
	int DIRECTIONS = 4;
	public static final int UP_DIRECTION = 0;
	public static final int DOWN_DIRECTION = 1;
	public static final int LEFT_DIRECTION = 2;
	public static final int RIGHT_DIRECTION = 3;
	public int move(ArrayList<String> availableCells,PlayScreenHandler handler){
		int position = 0;
		int direction = random.nextInt(DIRECTIONS);
		switch(direction) {
		case UP_DIRECTION:
			position = cell - PlayScreen.DEMON_GRID_COLUMNS;
			if(position <= 0 || !availableCells.contains(String.valueOf(position))){
				position = cell;
			}else {
				if(handler.getPointedDemon() == posInArray){
					handler.setDemonFound(false);
				}
			}
		break;
		case DOWN_DIRECTION:
			position = cell + PlayScreen.DEMON_GRID_COLUMNS;
			if(position >= (PlayScreen.DEMON_GRID_COLUMNS*PlayScreen.DEMON_GRID_ROWS)
					|| !availableCells.contains(String.valueOf(position)) ){
				position = cell;
			}else {
				if(handler.getPointedDemon() == posInArray){
					handler.setDemonFound(false);
				}
			}
		break;
		case LEFT_DIRECTION:
			position = cell-1;
			if(position <= 0 || !availableCells.contains(String.valueOf(position))){
				position = cell;
			}else {
				if(handler.getPointedDemon() == posInArray){
					handler.setDemonFound(false);
				}
			}
		break;
		case RIGHT_DIRECTION:
			position = cell+1;
			if(position >= (PlayScreen.DEMON_GRID_COLUMNS*PlayScreen.DEMON_GRID_ROWS)
					|| !availableCells.contains(String.valueOf(position))){
				position = cell;
			}else {
				if(handler.getPointedDemon() == posInArray){
					handler.setDemonFound(false);
				}
			}
		break;
		}
		return position;
	}
	
	private synchronized boolean shouldDemonMove(ArrayList<String> availableCells,
												 PlayScreenHandler handler){
		boolean shouldItMove;
		allowedConsecutiveShots = 15;
		int randNumber = random.nextInt(100);
		if(consecutiveShots>=allowedConsecutiveShots || randNumber<=aiConstant){
			shouldItMove = true;
			consecutiveShots = 0;
		}else {
			shouldItMove = false;
		}
		
		if(shouldItMove){
			// Making the current cell available again
			int lastCell = cell;
			cell = move(availableCells,handler);
			availableCells.add(String.valueOf(lastCell));
			
			// Making that selectedCell now not available
			availableCells.remove(String.valueOf(cell));
		}else{
			// Jejejejeje la vida es bella
		}
		
		
		return shouldItMove;
	}
	
	public float getHealth(){
		return health;
	}
	
	public boolean shouldDemonScream(){
		int number = random.nextInt(100);
		
		if(number <= 1){
			System.out.println("GRITANDO");
			return true;
		}
		return false;
	}
	
	public synchronized void update(ArrayList<String> availableCells,
									SoundClip demonScream,PlayScreenHandler handler){
		if(!isActive()) {
			return;
		}
		switch(state){
		case IDLE_STATE:
			timeItTookToBeKilled++;
			if(shouldDemonMove(availableCells,handler)){
				appearanceAnim.setLapsToTravel(2);
				demonBegun = true;
			}
		break;	
		case APPEAR_STATE:
			timeItTookToBeKilled++;
			if(appearanceAnim.getTraveledLaps() == 0){
				appearanceAnim.start();
			}else{
				if(!appearanceAnim.isActive()){
					state = IDLE_STATE;
					idleAnim.start();
				}
			}
		break;
		case SCREAM_STATE:
			if(!screamAnim.isActive()){
				screamAnim.start();
			}else{
				if(shouldDemonScream()){
					demonScream.play();
				}
			}
		break;
		case ATTACK_STATE:
		break;
		case DEATH_STATE:
			if(!deathAnim.isActive() && !deathPlayed){
				deathAnim.setLapsToTravel(1);
				deathPlayed = true;
				deathAnim.start();
			}
		break;
		default:
			
		}
	}
	
	public ImageChangingLabel getCurrentState() {
		switch(state){
		case IDLE_STATE:
			return idleAnim;
		case APPEAR_STATE:
			return appearanceAnim;
		case SCREAM_STATE:
			return screamAnim;
		case ATTACK_STATE:
			return attackAnim;
		case DEATH_STATE:
			return deathAnim;
		default:
			return idleAnim;
		}
	}
	
	public int getState() {
		return state;
	}
	
	public void setState(int state){
		this.state = state;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public synchronized boolean isAlive() {
		if(health <= 0){
			return false;
		}
		return true;
	}
	
	public int getCell() {
		return cell;
	}
	
	public int getTimeItTookToBeKilled() {
		return timeItTookToBeKilled;
	}
	
	public void setCell(int cell) {
		this.cell = cell;
	}
	
	public void setAllAnimations() {
		String[] appearImgsPaths = {ImagesPaths.DEMON_APPEARANCE_FRAME_1,
									ImagesPaths.DEMON_APPEARANCE_FRAME_2};
		appearanceAnim = null;
		appearanceAnim = new ImageChangingLabel(appearImgsPaths,
												DEMON_APPEAR_WAIT_TIME,
												DEMON_WIDTH,
												DEMON_HEIGHT);
		appearanceAnim.setLapsToTravel(1);
		
		switch(demonType){
		case PINKY_DEMON_TYPE:
			setPinkyAnimations();
		break;
		case SOLDIER_DEMON_TYPE:
			setSoldierAnimations();
		break;
		case LOST_SOUL_DEMON_TYPE:
			setLostSoulAnimations();
		break;
		case HELL_KNIGHT_DEMON_TYPE:
			setHellKnightAnimations();
		break;
		case BARON_OF_HELL_DEMON_TYPE:
			setBaronOfHellAnimations();
		break;
		case REVENANT_DEMON_TYPE:
			setRevenantAnimations();
		break;
		case CYBER_DEMON_TYPE:
			setCyberDemonAnimations();
		break;
		default:
			setPinkyAnimations();
		}
	}
	
	private void setPinkyAnimations(){
		String[] idleAnimPaths = {ImagesPaths.PINKY_IDLE_FRAME_1,
								  ImagesPaths.PINKY_IDLE_FRAME_2,
								  ImagesPaths.PINKY_IDLE_FRAME_3,
								  ImagesPaths.PINKY_IDLE_FRAME_4};
		idleAnim = new ImageChangingLabel(idleAnimPaths,150,DEMON_WIDTH,
															DEMON_HEIGHT);
		
		String[] screamAnimPaths = {ImagesPaths.PINKY_SCREAM_FRAME_1,
									ImagesPaths.PINKY_SCREAM_FRAME_2,
									ImagesPaths.PINKY_SCREAM_FRAME_3};
		screamAnim = new ImageChangingLabel(screamAnimPaths,150,DEMON_WIDTH,
																DEMON_HEIGHT);
		
		String[] attackAnimPaths = {ImagesPaths.PINKY_ATTACK_FRAME_1,
									ImagesPaths.PINKY_ATTACK_FRAME_2};
		attackAnim = new ImageChangingLabel(attackAnimPaths,100,DEMON_WIDTH,
																DEMON_HEIGHT);
		
		String[] deathAnimPaths = {ImagesPaths.PINKY_DEATH_FRAME_1,
								   ImagesPaths.PINKY_DEATH_FRAME_2,
								   ImagesPaths.PINKY_DEATH_FRAME_3,
								   ImagesPaths.PINKY_DEATH_FRAME_4,
								   ImagesPaths.PINKY_DEATH_FRAME_5,
								   ImagesPaths.PINKY_DEATH_FRAME_6};
		deathAnim = new ImageChangingLabel(deathAnimPaths,150,DEMON_WIDTH,
															  DEMON_HEIGHT);
	}
	
	private void setSoldierAnimations() {
		String[] idleImgsPaths = {ImagesPaths.SOLDIER_IDLE_FRAME_1,
								  ImagesPaths.SOLDIER_IDLE_FRAME_2,
								  ImagesPaths.SOLDIER_IDLE_FRAME_3,
								  ImagesPaths.SOLDIER_IDLE_FRAME_4};
		idleAnim = new ImageChangingLabel(idleImgsPaths,150,DEMON_WIDTH,
															DEMON_HEIGHT);
		
		String[] screamImgsPaths = {ImagesPaths.SOLDIER_SCREAM_FRAME_1,
									ImagesPaths.SOLDIER_SCREAM_FRAME_2,
									ImagesPaths.SOLDIER_SCREAM_FRAME_3,
									ImagesPaths.SOLDIER_SCREAM_FRAME_4};
		screamAnim = new ImageChangingLabel(screamImgsPaths,150,DEMON_WIDTH,
																DEMON_HEIGHT);
		
		String[] attackImgsPaths = {ImagesPaths.SOLDIER_ATTACK_FRAME_1,
									ImagesPaths.SOLDIER_ATTACK_FRAME_2};
		attackAnim = new ImageChangingLabel(attackImgsPaths,150,DEMON_WIDTH,
																DEMON_HEIGHT);
		
		String[] deathImgsPaths = {ImagesPaths.SOLDIER_DEATH_FRAME_1,
								   ImagesPaths.SOLDIER_DEATH_FRAME_2,
								   ImagesPaths.SOLDIER_DEATH_FRAME_3,
								   ImagesPaths.SOLDIER_DEATH_FRAME_4,
								   ImagesPaths.SOLDIER_DEATH_FRAME_5,
								   ImagesPaths.SOLDIER_DEATH_FRAME_6,
								   ImagesPaths.SOLDIER_DEATH_FRAME_7,
								   ImagesPaths.SOLDIER_DEATH_FRAME_8};
		deathAnim = new ImageChangingLabel(deathImgsPaths,150,DEMON_WIDTH,
															  DEMON_HEIGHT);
	}
	
	private void setLostSoulAnimations() {
		String[] idleImgsPaths = {ImagesPaths.LOST_SOUL_IDLE_FRAME_1,
								  ImagesPaths.LOST_SOUL_IDLE_FRAME_2};
		idleAnim = new ImageChangingLabel(idleImgsPaths,100,DEMON_WIDTH,
											                DEMON_HEIGHT);

		String[] screamImgsPaths = {ImagesPaths.LOST_SOUL_SCREAM_FRAME_1,
					                ImagesPaths.LOST_SOUL_SCREAM_FRAME_2};
		screamAnim = new ImageChangingLabel(screamImgsPaths,100,DEMON_WIDTH,
									                 			DEMON_HEIGHT);

		String[] attackImgsPaths = {ImagesPaths.LOST_SOUL_ATTACK_FRAME_1,
				 	                ImagesPaths.LOST_SOUL_ATTACK_FRAME_2};
		attackAnim = new ImageChangingLabel(attackImgsPaths,100,DEMON_WIDTH,
												                DEMON_HEIGHT);

		String[] deathImgsPaths = {ImagesPaths.LOST_SOUL_DEATH_FRAME_1,
				                   ImagesPaths.LOST_SOUL_DEATH_FRAME_2,
				                   ImagesPaths.LOST_SOUL_DEATH_FRAME_3,
				                   ImagesPaths.LOST_SOUL_DEATH_FRAME_4,
				                   ImagesPaths.LOST_SOUL_DEATH_FRAME_5,
				                   ImagesPaths.LOST_SOUL_DEATH_FRAME_6};
		deathAnim = new ImageChangingLabel(deathImgsPaths,150,DEMON_WIDTH,
											                  DEMON_HEIGHT);
	}

	private void setHellKnightAnimations() {
		String[] idleImgsPaths = {ImagesPaths.HELLKNIGHT_IDLE_FRAME_1,
								  ImagesPaths.HELLKNIGHT_IDLE_FRAME_2,
								  ImagesPaths.HELLKNIGHT_IDLE_FRAME_3,
								  ImagesPaths.HELLKNIGHT_IDLE_FRAME_4,
								  ImagesPaths.HELLKNIGHT_IDLE_FRAME_5,
								  ImagesPaths.HELLKNIGHT_IDLE_FRAME_6,
								  ImagesPaths.HELLKNIGHT_IDLE_FRAME_7,
								  ImagesPaths.HELLKNIGHT_IDLE_FRAME_8,
								  ImagesPaths.HELLKNIGHT_IDLE_FRAME_9,
								  ImagesPaths.HELLKNIGHT_IDLE_FRAME_10};
		idleAnim = new ImageChangingLabel(idleImgsPaths,150,DEMON_WIDTH,
							                                DEMON_HEIGHT);

		String[] screamImgsPaths = {ImagesPaths.HELLKNIGHT_SCREAM_FRAME_1,
                   	                ImagesPaths.HELLKNIGHT_SCREAM_FRAME_2};
		screamAnim = new ImageChangingLabel(idleImgsPaths,150,DEMON_WIDTH,
					                 			                DEMON_HEIGHT);

		String[] attackImgsPaths = {ImagesPaths.HELLKNIGHT_ATTACK_FRAME_1,
                 	                ImagesPaths.HELLKNIGHT_ATTACK_FRAME_2};
		attackAnim = new ImageChangingLabel(attackImgsPaths,100,DEMON_WIDTH,
								                                DEMON_HEIGHT);

		String[] deathImgsPaths = {ImagesPaths.HELLKNIGHT_DEATH_FRAME_1,
                                   ImagesPaths.HELLKNIGHT_DEATH_FRAME_2,
                                   ImagesPaths.HELLKNIGHT_DEATH_FRAME_3,
                                   ImagesPaths.HELLKNIGHT_DEATH_FRAME_4,
                                   ImagesPaths.HELLKNIGHT_DEATH_FRAME_5,
                                   ImagesPaths.HELLKNIGHT_DEATH_FRAME_6,
                                   ImagesPaths.HELLKNIGHT_DEATH_FRAME_7};
		deathAnim = new ImageChangingLabel(deathImgsPaths,150,DEMON_WIDTH,
							                                  DEMON_HEIGHT);
	}
	
	private void setBaronOfHellAnimations() {
		String[] idleImgsPaths = {ImagesPaths.BARON_OF_HELL_IDLE_FRAME_1,
				                  ImagesPaths.BARON_OF_HELL_IDLE_FRAME_2,
				                  ImagesPaths.BARON_OF_HELL_IDLE_FRAME_3,
				                  ImagesPaths.BARON_OF_HELL_IDLE_FRAME_4};
		idleAnim = new ImageChangingLabel(idleImgsPaths,150,DEMON_WIDTH,
			                                                DEMON_HEIGHT);

		String[] screamImgsPaths = {ImagesPaths.BARON_OF_HELL_SCREAM_FRAME_1,
 	                                ImagesPaths.BARON_OF_HELL_SCREAM_FRAME_2,
 	                                ImagesPaths.BARON_OF_HELL_SCREAM_FRAME_3,
	                                ImagesPaths.BARON_OF_HELL_SCREAM_FRAME_4};
		screamAnim = new ImageChangingLabel(screamImgsPaths,150,DEMON_WIDTH,
	                 			                                DEMON_HEIGHT);

		String[] attackImgsPaths = {ImagesPaths.BARON_OF_HELL_ATTACK_FRAME_1,
	                                ImagesPaths.BARON_OF_HELL_ATTACK_FRAME_2};
		attackAnim = new ImageChangingLabel(attackImgsPaths,100,DEMON_WIDTH,
				                                                DEMON_HEIGHT);

		String[] deathImgsPaths = {ImagesPaths.BARON_OF_HELL_DEATH_FRAME_1,
                                   ImagesPaths.BARON_OF_HELL_DEATH_FRAME_2,
                                   ImagesPaths.BARON_OF_HELL_DEATH_FRAME_3	,
                                   ImagesPaths.BARON_OF_HELL_DEATH_FRAME_4,
                                   ImagesPaths.BARON_OF_HELL_DEATH_FRAME_5,
                                   ImagesPaths.BARON_OF_HELL_DEATH_FRAME_6};
		deathAnim = new ImageChangingLabel(deathImgsPaths,150,DEMON_WIDTH,
			                                                  DEMON_HEIGHT);

	}

	private void setRevenantAnimations() {
		String[] idleImgsPaths = {ImagesPaths.REVENANT_IDLE_FRAME_1,
                                  ImagesPaths.REVENANT_IDLE_FRAME_2,
                                  ImagesPaths.REVENANT_IDLE_FRAME_3,
                                  ImagesPaths.REVENANT_IDLE_FRAME_4,
                                  ImagesPaths.REVENANT_IDLE_FRAME_5,
                                  ImagesPaths.REVENANT_IDLE_FRAME_6,
                                  ImagesPaths.REVENANT_IDLE_FRAME_7,
                                  ImagesPaths.REVENANT_IDLE_FRAME_8};
		idleAnim = new ImageChangingLabel(idleImgsPaths,150,DEMON_WIDTH,
                                                            DEMON_HEIGHT);

		String[] screamImgsPaths = {ImagesPaths.REVENANT_SCREAM_FRAME_1,
                                    ImagesPaths.REVENANT_SCREAM_FRAME_2};
		screamAnim = new ImageChangingLabel(idleImgsPaths,150,DEMON_WIDTH,
   		                    	                                DEMON_HEIGHT);

		String[] attackImgsPaths = {ImagesPaths.REVENANT_ATTACK_FRAME_1,
                                    ImagesPaths.REVENANT_ATTACK_FRAME_2};
		attackAnim = new ImageChangingLabel(attackImgsPaths,100,DEMON_WIDTH,
                                                                DEMON_HEIGHT);

		String[] deathImgsPaths = {ImagesPaths.REVENANT_DEATH_FRAME_1,
                                   ImagesPaths.REVENANT_DEATH_FRAME_2,
                                   ImagesPaths.REVENANT_DEATH_FRAME_3,
                                   ImagesPaths.REVENANT_DEATH_FRAME_4,
                                   ImagesPaths.REVENANT_DEATH_FRAME_5};
		deathAnim = new ImageChangingLabel(deathImgsPaths,150,DEMON_WIDTH,
                                                              DEMON_HEIGHT);
	}

	private void setCyberDemonAnimations() {
		String[] idleImgsPaths = {ImagesPaths.CYBERDEMON_IDLE_FRAME_1,
								  ImagesPaths.CYBERDEMON_IDLE_FRAME_2,
								  ImagesPaths.CYBERDEMON_IDLE_FRAME_3,
								  ImagesPaths.CYBERDEMON_IDLE_FRAME_4};
		idleAnim = new ImageChangingLabel(idleImgsPaths,150,DEMON_WIDTH,
															DEMON_HEIGHT);
		
		String[] screamImgsPaths = {ImagesPaths.CYBERDEMON_SCREAM_FRAME_1,
									ImagesPaths.CYBERDEMON_SCREAM_FRAME_2};
		screamAnim = new ImageChangingLabel(screamImgsPaths,150,DEMON_WIDTH,
														        DEMON_HEIGHT);
		
		String[] attackImgsPaths = {ImagesPaths.CYBERDEMON_ATTACK_FRAME_1,
									ImagesPaths.CYBERDEMON_ATTACK_FRAME_2};
		attackAnim = new ImageChangingLabel(attackImgsPaths,100,DEMON_WIDTH,
																DEMON_HEIGHT);
		
		String[] deathImgsPaths = {ImagesPaths.CYBERDEMON_DEATH_FRAME_1,
								   ImagesPaths.CYBERDEMON_DEATH_FRAME_2,
								   ImagesPaths.CYBERDEMON_DEATH_FRAME_3,
								   ImagesPaths.CYBERDEMON_DEATH_FRAME_4,
								   ImagesPaths.CYBERDEMON_DEATH_FRAME_5,
								   ImagesPaths.CYBERDEMON_DEATH_FRAME_6,
								   ImagesPaths.CYBERDEMON_DEATH_FRAME_7,
								   ImagesPaths.CYBERDEMON_DEATH_FRAME_8};
		deathAnim = new ImageChangingLabel(deathImgsPaths,150,DEMON_WIDTH,
															  DEMON_HEIGHT);
	}

	/**
	 * Returns the appearance animation that plays when the demon first pops up
	 * in the screen or when the demon changes places.
	 * @return An ImageChangingLabel object.
	 */
	public ImageChangingLabel getAppearanceAnimation() {
		return appearanceAnim;
	}	
	
	/**
	 * Returns the idle animation that plays when the demon is just standing
	 *  there without attacking.
	 * @return An ImageChangingLabel object.
	 */
	public ImageChangingLabel getIdleAnimation() {
		return idleAnim;
	}
	
	/**
	 * Returns the attack animation that plays when the demon is telegraphing
	 * an attacking.
	 * @return An ImageChangingLabel object.
	 */
	public ImageChangingLabel getAttackAnimation() {
		return attackAnim;
	}
	
	/**
	 * Returns the death animation that plays when the demon is killed. 
	 * @return An ImageChangingLabel object.
	 */
	public ImageChangingLabel getDeathAnim() {
		return deathAnim;
	}
	
	/**
	 * Returns the stunned animation that plays when demons is stunned.
	 * @return An ImageChangingLaebl object.
	 */
	public ImageChangingLabel getStunnedAnimation() {
		return stunnedAnim;
	}
	
	/**
	 * Returns the screan animation that plays when the demon is screaming.
	 * @return An ImageChangingLabel object.
	 */
	public ImageChangingLabel getScreamAnimation() {
		return screamAnim;
	}
}
