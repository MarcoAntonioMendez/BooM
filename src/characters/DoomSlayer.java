package characters;

public class DoomSlayer {
	private int gunPower;
	private int score;
	private float health;
	
	public DoomSlayer() {
		health = 100.0f;
		gunPower = 20;
		score = 0;
	}
	
	public void update(){
		
	}
	
	public int getGunPower() {
		return gunPower;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score){
		this.score = score;
	}
	
	public boolean isAlive() {
		if(health > 0.0f){
			return true;
		}
		return false;
	}
}
