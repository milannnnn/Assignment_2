package assignment2_matteo;

import java.util.ArrayList;

import assignment2.FillStates;
import assignment2.SystemState;

public class TestMat {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FillStates fillings = new FillStates();
		ArrayList<SystemState> allStates = fillings.getStates("root", "Callandor14", "measurements");
		Kmean kmeanTest = new Kmean(allStates, 1e-8, 1000);
		ArrayList<ArrayList<SystemState>> Clusters= kmeanTest.kMeanClustering(16,4,"forgy");
//		System.out.println(Clusters.size());
		for(int ii=0; ii<Clusters.size(); ii++){
			System.out.println("Cluster number " + (ii+1));
//			System.out.println(Clusters.get(ii).size());
			for(int i=0; i<Clusters.get(ii).size(); i++){
				Clusters.get(ii).get(i).printValues();
			}
		}
	}

}
