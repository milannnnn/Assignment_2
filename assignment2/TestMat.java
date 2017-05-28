package assignment2;

import java.util.ArrayList;

import org.jfree.ui.RefineryUtilities;

import assignment2.FillStates;
import assignment2.SystemState;
import assignment2_matteo.PlotClusters;

public class TestMat {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FillStates fillings = new FillStates();
		ArrayList<SystemState> allStates = fillings.getStates("root", "Callandor14", "measurements");
		Kmean kmeanTest = new Kmean(allStates, 1e-16, 1000);
		ArrayList<ArrayList<SystemState>> Clusters= kmeanTest.kMeanClustering(16,4,"RPM");
		kmeanTest.CSV(Clusters, 4);
//		System.out.println(Clusters.size());
		for(int ii=0; ii<Clusters.size(); ii++){
			System.out.println("Cluster number " + (ii+1) + " Cluster size " + Clusters.get(ii).size());
//			System.out.println(Clusters.get(ii).size());
//			for(int i=0; i<Clusters.get(ii).size(); i++){
//				Clusters.get(ii).get(i).printValues();
//			}
		}
		final PlotClusters demo = new PlotClusters("Clusters plot",Clusters);
	    demo.pack();
	    RefineryUtilities.centerFrameOnScreen(demo);
	    demo.setVisible(true);
	}
}
