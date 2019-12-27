package engine;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class WhiteFontMaker {
	private static final String[] alphabet = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
	private static final String[] ALPHABET = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	private static final String[] others = {"1","2","3","4","5","6","7","8","9","0"," "};
	
	public static final ArrayList<Component> getComponents(String name,
														   int score){
		ArrayList<Component> components = new ArrayList<Component>();
		int alphabetIndex = -1;
		int ALPHABETIndex = -1;
		int othersIndex = -1;
		// Adding the letters
		for(int x = 0; x < name.length(); x++){
			alphabetIndex = getIndex(String.valueOf(name.charAt(x)),alphabet);
			ALPHABETIndex = getIndex(String.valueOf(name.charAt(x)),ALPHABET);
			othersIndex = getIndex(String.valueOf(name.charAt(x)),others);
			if(alphabetIndex != -1){
				Component component = getLetterComponent(alphabetIndex);
				components.add(component);
			}else if(ALPHABETIndex != -1){
				Component component = getLetterComponent(ALPHABETIndex);
				components.add(component);
			}else if(othersIndex != -1){
				Component component = getOthersComponent(othersIndex);
				components.add(component);
			}
		}
		
		// Adding the []-[]
		components.add(Box.createRigidArea(new Dimension(30,1)));
		components.add(new JLabel(new ImageIcon("resources/images/alphabet/guion.png")));
		components.add(Box.createRigidArea(new Dimension(30,1)));
		
		// Adding the numbers
		String scoreStr = String.valueOf(score);
		System.out.println("ScoreStr: "+scoreStr);
		for(int x = 0; x < scoreStr.length(); x++){
			othersIndex = getIndex(String.valueOf(scoreStr.charAt(x)),others);
			Component component = getOthersComponent(othersIndex);
			components.add(component);
		}
		
		return components;
	}
	
	private static final int getIndex(String element,String[] array){
		for(int x = 0; x < array.length; x++){
			if(element.equals(array[x])){
				return x;
			}
		}
		return -1;
	}
	
	public static final JLabel getLetterComponent(int index){
		return new JLabel(new ImageIcon(letters[index]));
	}
	
	public static final Component getOthersComponent(int index){
		if(index != (numbers.length-1)){
			return new JLabel(new ImageIcon(numbers[index]));
		}else {
			return Box.createRigidArea(new Dimension(20,1));
		}
	}
	
	public static final String[] letters = {"resources/images/alphabet/A.png",
											"resources/images/alphabet/B.png",
											"resources/images/alphabet/C.png",
											"resources/images/alphabet/D.png",
											"resources/images/alphabet/E.png",
											"resources/images/alphabet/F.png",
											"resources/images/alphabet/G.png",
											"resources/images/alphabet/H.png",
											"resources/images/alphabet/I.png",
											"resources/images/alphabet/J.png",
											"resources/images/alphabet/K.png",
											"resources/images/alphabet/L.png",
											"resources/images/alphabet/M.png",
											"resources/images/alphabet/N.png",
											"resources/images/alphabet/O.png",
											"resources/images/alphabet/P.png",
											"resources/images/alphabet/Q.png",
											"resources/images/alphabet/R.png",
											"resources/images/alphabet/S.png",
											"resources/images/alphabet/T.png",
											"resources/images/alphabet/U.png",
											"resources/images/alphabet/V.png",
											"resources/images/alphabet/W.png",
											"resources/images/alphabet/X.png",
											"resources/images/alphabet/Y.png",
											"resources/images/alphabet/Z.png"};
	
	public static final String[] numbers = {"resources/images/alphabet/1.png",
											"resources/images/alphabet/2.png",
											"resources/images/alphabet/3.png",
											"resources/images/alphabet/4.png",
											"resources/images/alphabet/5.png",
											"resources/images/alphabet/6.png",
											"resources/images/alphabet/7.png",
											"resources/images/alphabet/8.png",
											"resources/images/alphabet/9.png",
											"resources/images/alphabet/0.png",
											"NULL"};
}
