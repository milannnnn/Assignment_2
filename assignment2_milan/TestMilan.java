package assignment2_milan;

import java.util.ArrayList;

public class TestMilan {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FillStates fillings = new FillStates();
		//ArrayList<SystemState> allStates = fillings.getStates("root", "Callandor14", "measurements");
		ArrayList<SystemState> allStates = fillings.getStates("root", "root", "measurements");
		KmeansClustering kmeanTest = new KmeansClustering(allStates, 1e-16, 1000);
		ArrayList<ArrayList<SystemState>> Clusters= kmeanTest.clusterData(16,4,"forgy");
		kmeanTest.CSV(Clusters, 4);
		
		String[][] genPairs = new String[3][2];
		genPairs[0][0] = "_2BCC3D5923464FED9E08EB12EC388BD7"; //CLAR
		genPairs[0][1] = "_58F637D8B03A4B12A67DF2E5797F9B6A"; //BOWM
		genPairs[1][0] = "_4273A87151C0409780237BFD866C23DA"; //AMHE
		genPairs[1][1] = "_95D3CD0256FB4C9DB2860CFEFA45CD57"; //WAUT
		genPairs[2][0] = "_4F715892155341C8A76534537F095B49"; //WINL
		genPairs[2][1] = "_7AC1BC6CDFAF4F26A97AD322E9F5AD31"; //MAPL
		
		double[] clusGenDiff = analyzeGenOutage(Clusters, genPairs);
		System.out.println("Generators:");
		for(int k=0; k<Clusters.size(); k++){
			System.out.println(Clusters.get(k).size() +" - "+clusGenDiff[k]);
		}
		
		String[][] linePairs = new String[9][2];
		linePairs[0][0] = "_2BCC3D5923464FED9E08EB12EC388BD7"; //CLAR
		linePairs[0][1] = "_58F637D8B03A4B12A67DF2E5797F9B6A"; //BOWM
		
		linePairs[1][0] = "_4273A87151C0409780237BFD866C23DA"; //AMHE
		linePairs[1][1] = "_95D3CD0256FB4C9DB2860CFEFA45CD57"; //WAUT
		
		linePairs[2][0] = "_4F715892155341C8A76534537F095B49"; //WINL
		linePairs[2][1] = "_7AC1BC6CDFAF4F26A97AD322E9F5AD31"; //MAPL
		
		linePairs[3][0] = "_58F637D8B03A4B12A67DF2E5797F9B6A"; //BOWM
		linePairs[3][1] = "_7324D6723635494784A4D8A9578FCE8A"; //TROY
		
		linePairs[4][0] = "_58F637D8B03A4B12A67DF2E5797F9B6A"; //BOWM
		linePairs[4][1] = "_9D8BB8E8B5DB40F6ABF515042B7DFF97"; //CROS
		
		linePairs[5][0] = "_7AC1BC6CDFAF4F26A97AD322E9F5AD31"; //MAPL
		linePairs[5][1] = "_7324D6723635494784A4D8A9578FCE8A"; //TROY
		
		linePairs[6][0] = "_7AC1BC6CDFAF4F26A97AD322E9F5AD31"; //MAPL
		linePairs[6][1] = "_7DD325DCEFC248989B72AAD58D3DD4E9"; //GRAN
		
		linePairs[7][0] = "_95D3CD0256FB4C9DB2860CFEFA45CD57"; //WAUT
		linePairs[7][1] = "_7DD325DCEFC248989B72AAD58D3DD4E9"; //GRAN
		
		linePairs[8][0] = "_95D3CD0256FB4C9DB2860CFEFA45CD57"; //WAUT
		linePairs[8][1] = "_9D8BB8E8B5DB40F6ABF515042B7DFF97"; //CROS
		
		double[] clusLineDiff = analyzeLineOutage(Clusters, linePairs);
		System.out.println("Lines:");
		for(int k=0; k<Clusters.size(); k++){
			System.out.println(Clusters.get(k).size() +" - "+clusLineDiff[k]);
		}
	}
	
	
	public static double[] analyzeGenOutage(ArrayList<ArrayList<SystemState>> Clusters, String[][] genPairs){
		double[] clusGenDiff = new double[Clusters.size()];
		
		//### Over each cluster
		for(int k=0; k<Clusters.size(); k++){
			//### Over each state
			for(int j=0; j<Clusters.get(k).size(); j++){
				double minStateDiff = 0;
				//### Over each bus pair
				for(int m=0; m<genPairs.length; m++){
					double tmpGenDist = Clusters.get(k).get(j).calcDiffOfBuses(genPairs[m][0], genPairs[m][1]);
					if(m==0){
						minStateDiff = tmpGenDist;
					}
					else{
						if(tmpGenDist<minStateDiff){
							minStateDiff = tmpGenDist;
						}
					}
				}
				clusGenDiff[k] += minStateDiff/Clusters.get(k).size();
			}
		}
		return clusGenDiff;
	}
	
	public static double[] analyzeLineOutage(ArrayList<ArrayList<SystemState>> Clusters, String[][] linePairs){
		double[] clusGenDiff = new double[Clusters.size()];
		
		//### Over each cluster
		for(int k=0; k<Clusters.size(); k++){
			//### Over each state
			for(int j=0; j<Clusters.get(k).size(); j++){
				double maxStateDiff = 0;
				//### Over each bus pair
				for(int m=0; m<linePairs.length; m++){
					double tmpGenDist = Clusters.get(k).get(j).calcDiffOfBuses(linePairs[m][0], linePairs[m][1]);
					if(m==0){
						maxStateDiff = tmpGenDist;
					}
					else{
						if(tmpGenDist>maxStateDiff){
							maxStateDiff = tmpGenDist;
						}
					}
				}
				clusGenDiff[k] += maxStateDiff/Clusters.get(k).size();
			}
		}
		return clusGenDiff;
	}
	
	
}
