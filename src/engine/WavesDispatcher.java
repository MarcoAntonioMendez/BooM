package engine;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

import characters.Demon;
import user_interface.PlayScreenHandler;

public class WavesDispatcher{
	private static final float TIME_PER_HEALTH_UNIT = 0.05f;
	private int[] DEMONS_FOR_EACH_WAVE = {1,2,2,3,3,3,4,5,5,5,5,6,6,7,8,8,8};
	private int[] DEMONS_AI_CONSTANT = {15,15,15,15,20,20,20,20,20,20,20,20,20,20,20,20,20,25};
	private float[] TIMES_PER_HEALTH_UNITS = {0.15f,.15f,.15f,.09f,.09f,.09f,.09f,.09f,.09f,.09f,.09f,.09f,.09f,.09f,.09f,.09f,.02f,.02f};
	private ArrayList<ArrayList<Integer>> DEMONS_TYPE_FOR_EACH_WAVE;
	private ArrayList<Integer> demonsForWave;
	private volatile int currentWave;
	private volatile int demonsAlive;
	private Random random;
	
	public WavesDispatcher() {
		currentWave = -1;
		demonsAlive = 0;
		demonsForWave = new ArrayList<Integer>();
		initializeDemonsTypeForEachWave();
		random = new Random();
	}
	
	
	public synchronized void setWave(ArrayList<Demon> demons,ArrayList<JPanel> demonsPanels,
						WaveTimer waveTimer,ArrayList<String> availableCells,
						PlayScreenHandler handler){
		
		int selectedCell,timeForWave = 0,aiConstant;
		float timesPerHealthUnit = 0.0f;
		if(demonsAlive <= 0){
			cleanAllCellsAndDeactivateDemons(demonsPanels,demons,availableCells);
			
			currentWave++;
			if(currentWave == DEMONS_FOR_EACH_WAVE.length){
				currentWave = (DEMONS_FOR_EACH_WAVE.length-1);
			}
			demonsAlive = DEMONS_FOR_EACH_WAVE[currentWave];
			aiConstant = DEMONS_AI_CONSTANT[currentWave];
			timesPerHealthUnit = TIMES_PER_HEALTH_UNITS[currentWave];
			setDemonsForWave();
			
			int demonsCreated = 0;
			System.out.println("Demons To Kill " + DEMONS_FOR_EACH_WAVE[currentWave]);
			
			for(int x = 0; x < demonsAlive; x++){
				// Selcting cell in which the demon will initally be
				selectedCell = Integer.parseInt(
						       availableCells.get(
						       random.nextInt(availableCells.size())));	
				
				// Making that selectedCell now not available
				availableCells.remove(String.valueOf(selectedCell));
				
				// Activating the demon and deciding the type of demon
				demons.get(selectedCell).activateDemon(decideDemonType(x),
													   aiConstant);
				demons.get(selectedCell).setCell(selectedCell);
				handler.activateDemon(demons.get(selectedCell));
				
				timeForWave += (int)(demons.get(selectedCell).getHealth() * timesPerHealthUnit);
				demonsCreated++;
			}		
			System.out.println("Demons Created: "+demonsCreated);
			waveTimer.setLeftTime(timeForWave);
		}
	}
	
	private void cleanAllCellsAndDeactivateDemons(ArrayList<JPanel> demonsPanels
											 ,ArrayList<Demon> demons,
											 ArrayList<String> availableCells) {
		
		for(int x = 0; x < demonsPanels.size(); x++){
			demonsPanels.get(x).removeAll();
		}
		for(int x = 0; x < demons.size(); x++){
			demons.get(x).deactivateDemon();
		}
		
		availableCells.removeAll(availableCells);
		for(int x = 0; x < demons.size(); x++){
			availableCells.add(String.valueOf(x));
		}
	}
	
	public int decideDemonType(int demonInTurn){
		return demonsForWave.get(demonInTurn);
	}
	
	public void setDemonsForWave() {
		demonsForWave.removeAll(demonsForWave);
		
		// Choose arraylist for correspondant wave
		ArrayList<Integer> currentWaveDemonsTypes = 
									DEMONS_TYPE_FOR_EACH_WAVE.get(currentWave);
		int demonType = 0;
		for(demonType=0; demonType < currentWaveDemonsTypes.size(); demonType++){
			int amountOfDemons = currentWaveDemonsTypes.get(demonType);
			
			for(int amount = 0; amount < amountOfDemons; amount++){
				demonsForWave.add(demonType);
			}
		}
		
	}
	
	public int getDemonsAlive() {
		return demonsAlive;
	}
	
	public void setDemonsAlive(int demonsAlive){
		this.demonsAlive = demonsAlive;
	}
	
	public void initializeDemonsTypeForEachWave() {
		DEMONS_TYPE_FOR_EACH_WAVE = new ArrayList<ArrayList<Integer>>();
		
		// Demons for wave 0
		ArrayList<Integer> demonsPerWave = new ArrayList<Integer>();
		demonsPerWave.add(1); //One PINKY demons
		DEMONS_TYPE_FOR_EACH_WAVE.add(demonsPerWave);
		
		// Demons for wave 1
		demonsPerWave = new ArrayList<Integer>();
		demonsPerWave.add(2); //Two PINKY demons
		DEMONS_TYPE_FOR_EACH_WAVE.add(demonsPerWave);
		
		// Demons for wave 2
		demonsPerWave = new ArrayList<Integer>();
		demonsPerWave.add(1); //One PINKY demon
		demonsPerWave.add(1); //One SOLDIER demon
		DEMONS_TYPE_FOR_EACH_WAVE.add(demonsPerWave);
		
		// Demons for wave 3
		demonsPerWave = new ArrayList<Integer>();
		demonsPerWave.add(2); //Two PINKY demons
		demonsPerWave.add(1); //One SOLDIER demon
		DEMONS_TYPE_FOR_EACH_WAVE.add(demonsPerWave);
		
		// Demons for wave 4
		demonsPerWave = new ArrayList<Integer>();
		demonsPerWave.add(1); //One PINKY demon
		demonsPerWave.add(2); //Two SOLDIER demons
		DEMONS_TYPE_FOR_EACH_WAVE.add(demonsPerWave);
		
		// Demons for wave 5
		demonsPerWave = new ArrayList<Integer>();
		demonsPerWave.add(1); // One PINKY demon
		demonsPerWave.add(1); // One SOLDIER demon
		demonsPerWave.add(1); // One LOST SOUL demon
		DEMONS_TYPE_FOR_EACH_WAVE.add(demonsPerWave);
		
		// Demons for wave 6
		demonsPerWave = new ArrayList<Integer>();
		demonsPerWave.add(1); // One PINKY demon
		demonsPerWave.add(1); // One SOLDIER demon
		demonsPerWave.add(2); // Two LOST SOUL demons 
		DEMONS_TYPE_FOR_EACH_WAVE.add(demonsPerWave);
		
		// Demons for wave 7
		demonsPerWave = new ArrayList<Integer>();
		demonsPerWave.add(2); // Two PINKY demons
		demonsPerWave.add(2); // Two SOLDIER demons
		demonsPerWave.add(1); // One LOST SOUL demon
		DEMONS_TYPE_FOR_EACH_WAVE.add(demonsPerWave);
		
		// Demons for wave 8
		demonsPerWave = new ArrayList<Integer>();
		demonsPerWave.add(2); // Two PINKY demons
		demonsPerWave.add(1); // One SOLDIER demons
		demonsPerWave.add(1); // One LOST SOUL demon
		demonsPerWave.add(1); // One HELL KNIGHT demon
		DEMONS_TYPE_FOR_EACH_WAVE.add(demonsPerWave);
		
		// Demons for wave 9
		demonsPerWave = new ArrayList<Integer>();
		demonsPerWave.add(1); // One PINKY demons
		demonsPerWave.add(1); // One SOLDIER demons
		demonsPerWave.add(2); // Two LOST SOUL demon
		demonsPerWave.add(1); // One HELL KNIGHT demon
		DEMONS_TYPE_FOR_EACH_WAVE.add(demonsPerWave);
		
		// Demons for wave 10
		demonsPerWave = new ArrayList<Integer>();
		demonsPerWave.add(1); // One PINKY demons
		demonsPerWave.add(1); // One SOLDIER demons
		demonsPerWave.add(1); // One LOST SOUL demon
		demonsPerWave.add(2); // Two HELL KNIGHT demon
		DEMONS_TYPE_FOR_EACH_WAVE.add(demonsPerWave);
		
		// Demons for wave 11
		demonsPerWave = new ArrayList<Integer>();
		demonsPerWave.add(1); // One PINKY demons
		demonsPerWave.add(1); // One SOLDIER demons
		demonsPerWave.add(2); // Two LOST SOU	L demon
		demonsPerWave.add(2); // Two HELL KNIGHT demon
		DEMONS_TYPE_FOR_EACH_WAVE.add(demonsPerWave);
		
		// Demons for wave 12
		demonsPerWave = new ArrayList<Integer>();
		demonsPerWave.add(2); // Two PINKY demons
		demonsPerWave.add(1); // One SOLDIER demons
		demonsPerWave.add(1); // One LOST SOUL demon
		demonsPerWave.add(2); // Two HELL KNIGHT demon
		DEMONS_TYPE_FOR_EACH_WAVE.add(demonsPerWave);
		
		// Demons for wave 13
		demonsPerWave = new ArrayList<Integer>();
		demonsPerWave.add(2); // Two PINKY demons
		demonsPerWave.add(1); // One SOLDIER demons
		demonsPerWave.add(1); // One LOST SOUL demon
		demonsPerWave.add(2); // Two HELL KNIGHT demon
		demonsPerWave.add(1); // One BARON OF HELL demon
		DEMONS_TYPE_FOR_EACH_WAVE.add(demonsPerWave);
	
		// Demons for wave 14
		demonsPerWave = new ArrayList<Integer>();
		demonsPerWave.add(2); // Two PINKY demons
		demonsPerWave.add(1); // One SOLDIER demon
		demonsPerWave.add(1); // One LOST SOUL demon
		demonsPerWave.add(1); // One HELL KNIGHT demons
		demonsPerWave.add(1); // One BARON OF HELL demon
		demonsPerWave.add(2); // Two REVENANT demons
		DEMONS_TYPE_FOR_EACH_WAVE.add(demonsPerWave);
		
		// Demons for wave 15
		demonsPerWave = new ArrayList<Integer>();
		demonsPerWave.add(1); // One PINKY demons
		demonsPerWave.add(1); // One SOLDIER demon
		demonsPerWave.add(1); // One LOST SOUL demon
		demonsPerWave.add(1); // One HELL KNIGHT demons
		demonsPerWave.add(2); // Two BARON OF HELL demon
		demonsPerWave.add(2); // One REVENANT demons
		DEMONS_TYPE_FOR_EACH_WAVE.add(demonsPerWave);
		
		// Demons for wave 16
		demonsPerWave = new ArrayList<Integer>();
		demonsPerWave.add(1); // One PINKY demons
		demonsPerWave.add(1); // One SOLDIER demon
		demonsPerWave.add(1); // One LOST SOUL demon
		demonsPerWave.add(1); // One HELL KNIGHT demons
		demonsPerWave.add(1); // One BARON OF HELL demon
		demonsPerWave.add(2); // Two REVENANT demons
		demonsPerWave.add(1); // One CYBER DEMON demon
		DEMONS_TYPE_FOR_EACH_WAVE.add(demonsPerWave);
	}
}
