package engine;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import characters.DoomSlayer;
import constants_across_classes.ImagesPaths;

public class ScorePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private JLabel[] numbers;
	private ImageIcon[] numbersImages;
	private DoomSlayer doomSlayer;
	private JLabel scoreLabel;

	public ScorePanel(String[] numbersImagesPaths,DoomSlayer doomSlayer){
		this.setOpaque(false);
		this.doomSlayer = doomSlayer;
		numbers = new JLabel[10];
		numbersImages = new ImageIcon[10];
		
		for(int x = 0; x < 10; x++){
			numbersImages[x] = new ImageIcon(numbersImagesPaths[x]);
			numbers[x] = new JLabel();
		}
		
		// Initializing the scorelabel image
		scoreLabel = new JLabel();
		ImageIcon scoreLabelIcon = new ImageIcon(ImagesPaths.SCORE_LABEL_PATH);
		scoreLabel.setIcon(scoreLabelIcon);
	}
	
	public void update() {
		reset();
		String scoreStr = String.valueOf(doomSlayer.getScore());
		this.add(scoreLabel);
		
		for(int x = 0; x < scoreStr.length(); x++){
			numbers[x].setIcon(numbersImages[Integer.parseInt(String.valueOf(scoreStr.charAt(x)))]);
			this.add(numbers[x]);
		}
	}
	
	private void reset(){
		removeAll();
	}
}
