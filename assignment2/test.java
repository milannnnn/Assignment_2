package assignment2;

import java.util.ArrayList;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		FillStates fillings = new FillStates();
		ArrayList<SystemState> allStates = fillings.getStates("root", "Callandor14", "measurements");
		for(int k=0; k<allStates.size(); k++){

		System.out.println(allStates.get(k).values()[1]);
		}
		
	}

}
