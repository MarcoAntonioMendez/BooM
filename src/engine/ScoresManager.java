package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ScoresManager{
	public static final String GOLD = "GOLD";
	public static final String SILVER = "SILVER";
	public static final String BRONZE = "BRONZE";
	public static final String NOT_RANKED = "NOT_RANKED";
	public static final String GOLDEN_SCORE_FILE_NAME = "resources/scores/golden_score.txt";
	public static final String SILVER_SCORE_FILE_NAME = "resources/scores/silver_score.txt";
	public static final String BRONZE_SCORE_FILE_NAME = "resources/scores/bronze_score.txt";
	
	public static final String readRankingFile(String rankToRead){
		Scanner scanner = null;
		File file = new File(rankToRead);
		try{scanner = new Scanner(file);}catch(FileNotFoundException e){e.printStackTrace();}
		return scanner.nextLine();
	}
	
	public static final String getScoreRanking(int score){
		String goldenFileContent,silverFileContent,bronzeFileContent;
		int goldenScore,silverScore,bronzeScore;
		//Extracting current golden score
		goldenFileContent = readRankingFile(GOLDEN_SCORE_FILE_NAME);
		goldenScore = getScoreFromTextLine(goldenFileContent);
		
		//Extracting current silver score
		silverFileContent = readRankingFile(SILVER_SCORE_FILE_NAME);
		silverScore = getScoreFromTextLine(silverFileContent);
			
		//Extracting current bronze score
		bronzeFileContent = readRankingFile(BRONZE_SCORE_FILE_NAME);
		bronzeScore = getScoreFromTextLine(bronzeFileContent);
		
		// Checking if user surpassed the goldenScore,silverScore or bronzeScore
		if(score > goldenScore){
			return GOLD;
		}else if(score > silverScore){
			return SILVER;
		}else if(score > bronzeScore){
			return BRONZE;
		}
		
		return NOT_RANKED;
	}
		
	public static final String getNameFromTextLine(String file){
		String name = "";
		for(int x = 0; x < file.length(); x++){
			if(file.charAt(x) == '-'){
				x = file.length();
			}else{
				name += String.valueOf(file.charAt(x));
			}
		}
		
		return name;
	}
	
	public static final int getScoreFromTextLine(String file){
		// Format name-100
		String scoreStr = "";
		boolean delimiterFound = false;
		for(int x = 0; x < file.length(); x++){
			if(file.charAt(x) == '-'){
				delimiterFound = true;
			}else{
				if(delimiterFound){
					scoreStr += String.valueOf(file.charAt(x));
				}
			}
		}
		
		return Integer.parseInt(scoreStr);
	}
	
	private static final void updateFile(String fileName,String fileContent){
		try {
			FileWriter goldFileWriter = new FileWriter(fileName);
			goldFileWriter.write(fileContent);
			goldFileWriter.close();
		}catch(IOException e){e.printStackTrace();}
	}
	
	public static final void updateGoldenScore(String newName,int score){
		// Saving the golden file content
		String lastGoldenFile = readRankingFile(GOLDEN_SCORE_FILE_NAME);
		
		// Saving the silver file content
		String lastSilverFile = readRankingFile(SILVER_SCORE_FILE_NAME);
		
		// Updating the golden file content
		String newGoldenFile = newName+"-"+String.valueOf(score);
		updateFile(GOLDEN_SCORE_FILE_NAME,newGoldenFile);
		
		// Updating the silver file content
		updateFile(SILVER_SCORE_FILE_NAME,lastGoldenFile);
		
		// Updating the bronze file content
		updateFile(BRONZE_SCORE_FILE_NAME,lastSilverFile);
	}
	
	public static final void updateSilverScore(String newName,int score){
		// Saving the silver file content
		String lastSilverFile = readRankingFile(SILVER_SCORE_FILE_NAME);
		
		// Updating the silver file content
		String newSilverFile = newName+"-"+String.valueOf(score);
		updateFile(SILVER_SCORE_FILE_NAME,newSilverFile);
		
		// Updating the bronze file content
		updateFile(BRONZE_SCORE_FILE_NAME,lastSilverFile);
	}
	
	public static final void updateBronzeScore(String newName,int score){
		String newBronzeFile = newName+"-"+String.valueOf(score);
		updateFile(BRONZE_SCORE_FILE_NAME,newBronzeFile);
	}
	
}
