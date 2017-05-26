package assignment2_matteo;

import java.util.ArrayList;

import assignment2.FillStates;
import assignment2.SystemState;

public class TestMat {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FillStates fillings = new FillStates();
		ArrayList<SystemState> allStates = fillings.getStates("root", "Callandor14", "measurements");
		Kmean kmeanTest = new Kmean(allStates, 1e-6);
		ArrayList<ArrayList<SystemState>> Clusters= kmeanTest.kMeanClustering(4);

	}

}
