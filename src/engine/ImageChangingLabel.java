package engine;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ImageChangingLabel extends JLabel implements Runnable{
	private static final long serialVersionUID = 1L;
	private volatile boolean active;
	private volatile int waitTime;
	private volatile int indexInTurn;
	private volatile int imageWidth;
	private volatile int imageHeight;
	private volatile ImageIcon[] images; 
	private volatile int lapsToTravel;
	private volatile int lapsTraveled;
	private volatile Thread thread;

	public ImageChangingLabel(String[] imagesPaths,int waitTime,int imageWidth,
															   int imageHeight){
		this.waitTime = waitTime;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		images = new ImageIcon[imagesPaths.length];
		indexInTurn = 1;
		lapsToTravel = -1;
		lapsTraveled = 0;
		for(int x = 0; x < imagesPaths.length; x++){
			
			images[x] = new ImageIcon(imagesPaths[x]);
			Image resizedImage = images[x].getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
			images[x].setImage(resizedImage);
		}
		this.setIcon(images[0]);
		
		thread = new Thread(this);
	}
	
	// DESCRIPTION: Sets a number of specific laps for the animation to play,
	// Example: if the animation has 6 images, and the laps are 2; the images
	// will be displayed two times and then, the animation will stop.
	public void setLapsToTravel(int lapsToTravel){
		this.lapsToTravel = lapsToTravel;
		lapsTraveled = 0;
	}
	
	public int getLaps() {
		return lapsToTravel;
	}
	
	public int getTraveledLaps() {
		return lapsTraveled;
	}
	
	@Override
	public void run() {
		while(active){
			// Checking if animation has a finite number of laps to travel or
			// It is supposed to run infinitely
			if(lapsToTravel != -1){
				// Checking if animation has ran the specified number of laps
				if(lapsTraveled < lapsToTravel){
					runAnimation();
				}else {
					stop();
				}
				
			}else{
				runAnimation();
			}
			
		}
		
	}
	
	private void runAnimation() {
		try {Thread.sleep(waitTime);}catch (InterruptedException e){e.printStackTrace();}
		
		this.setIcon(images[indexInTurn]);
		indexInTurn++;
		if(indexInTurn > (images.length-1)){
			indexInTurn = 0;
			lapsTraveled++;
		}
		
		//lapsTraveled++;
	}
	
	// DESCRIPTION: Changes the current set of images so we can have a different
	// animation without creating a new object.
	public void setImages(String[] imagesPaths){
		indexInTurn = 1;
		for(int x = 0; x < imagesPaths.length; x++){
			images[x] = new ImageIcon(imagesPaths[x]);
			Image resizedImage = images[x].getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
			images[x].setImage(resizedImage);
		}
		this.setIcon(images[0]);
	}
	
	// DESCRIPTION: Starts the animation.
	public void start(){
		active = true;
		thread = new Thread(this);
		thread.start();
	}
	// DESCRIPTION: returns if the animation is active.
	public boolean isActive() {
		return active;
	}

	// DESCRIPTION: Stops the animation.
	public void stop(){
		thread = null;
		active = false;
	}
}
