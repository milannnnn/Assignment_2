package assignment2_milan;

import java.util.ArrayList;

public class TestMat {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FillStates fillings = new FillStates();
		ArrayList<SystemState> allStates = fillings.getStates("root", "root", "measurements");
		KmeansClustering kmeanTest = new KmeansClustering(allStates, 1e-16, 1000);
		ArrayList<ArrayList<SystemState>> Clusters= kmeanTest.clusterData(16,4,"forgy");
//		kmeanTest.CSV(Clusters, 4);
//		System.out.println(Clusters.size());
		Label myLabel = new Label();
		myLabel.declareLabelsSpecific(Clusters);
		for(int ii=0; ii<Clusters.size(); ii++){
			System.out.println(Clusters.get(ii).size()+" - "+Clusters.get(ii).get(0).label);
		}
	}
}
