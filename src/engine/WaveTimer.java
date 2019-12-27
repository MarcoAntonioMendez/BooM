package engine;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import constants_across_classes.ImagesPaths;
import sounds.SoundsPaths;
import user_interface.SoundClip;

public class WaveTimer extends JPanel{
	private static final long serialVersionUID = 1L;
	private volatile int leftTime;
	private JLabel[] numbers;
	private ImageIcon[] numbersImages;
	private volatile boolean isActive;
	private JLabel timeLabel;
	private SoundClip beepSound;
	
	public WaveTimer(String[] numbersImagesPaths){
		this.setOpaque(false);
		isActive = false;
		numbers = new JLabel[10];
		numbersImages = new ImageIcon[10];
		
		for(int x = 0; x < 10; x++){
			numbersImages[x] = new ImageIcon(numbersImagesPaths[x]);
			numbers[x] = new JLabel();
		}
		
		timeLabel = new JLabel();
		ImageIcon icon = new ImageIcon(ImagesPaths.TIME_LABEL_PATH);
		timeLabel.setIcon(icon);
		
		beepSound = new SoundClip(SoundsPaths.MOUSE_IN_BUTTON_SOUND);
	}
	
	public WaveTimer(int leftTime,String[] numbersImagesPaths){
		this.leftTime = leftTime;
		this.setOpaque(false);
		isActive = false;
		numbers = new JLabel[10];
		numbersImages = new ImageIcon[10];
		
		for(int x = 0; x < 10; x++){
			numbersImages[x] = new ImageIcon(numbersImagesPaths[x]);
			numbers[x] = new JLabel();
		}
	}
	
	public void start() {
		isActive = true;
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				while(isActive){
					try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}
					if(!isOver()){
						reset();
						update();
						try{Thread.sleep(1000);}catch(InterruptedException e){e.printStackTrace();}
						if(leftTime <= 10 && leftTime >= 1){
							beepSound.play();
						}
					}
				}
			}
			
		};
		
		Thread thread = new Thread(runnable);
		thread.start();
	}
	
	private void update(){
		String leftTimeStr = String.valueOf(leftTime);
		leftTime--;
		this.add(timeLabel);
		for(int x = 0; x < leftTimeStr.length(); x++){
			numbers[x].setIcon(numbersImages[Integer.parseInt(String.valueOf(leftTimeStr.charAt(x)))]);
			this.add(numbers[x]);
		}
	}
	
	public void stop() {
		isActive = false;
	}
	
	public boolean isOver() {
		if(leftTime <= 0) {
			return true;
		}
		return false;
	}
	
	public int getLeftTime() {
		return leftTime;
	}
	
	public void setLeftTime(int leftTime){
		this.leftTime = leftTime;
	}
	
	private void reset(){
		removeAll();
	}
	
	public JLabel[] getNumbers(){
		return numbers;
	}
	
	public ImageIcon[] getNumbersImages() {
		return numbersImages;
	}
}
