package assignment2_matteo;

import java.util.*;

import assignment2.SystemState;


public class KNNmethod {

	public static ArrayList<String> KNN(int k, ArrayList<SystemState> testList, ArrayList<SystemState> learnList, int numLabels ){
		// ArrayList of labels for each SystemState
		ArrayList<String> labels = new ArrayList<String>();
		// loop through each SystemState
		for(SystemState tempSystemState : testList){
			// extract values
			double[] query = tempSystemState.values(); 
			// create list of K closest neighbors 
			ArrayList<Result> NeighborsList = createNeighborList(query, learnList, k);
			// find the most common label within the neighbors
			String label = sortState( NeighborsList, query, numLabels);
			labels.add(label);
		}
//		System.out.println("tested List");
//		FileLoad.printResults(TestedflowerList);
//		
//		double errorCalc = errorCalc(testList, TestedflowerList)*100;
//		System.out.println("error % " + errorCalc);
		return labels;
	}
	
	// ##################################################################################
	// euclidean distance
	 private static double EuDistance(double[] X1, double[] X2){
		double distance;
		double sum = 0;
		for(int i=0; i<X1.length; i++){
			sum = sum + Math.pow((X2[i] - X1[i]),2); 
		}
		distance = Math.sqrt(sum);
		return distance;
	}
	//##################################################################################
	// create neighborList
	 private static ArrayList<Result> createNeighborList(double[] query, ArrayList<SystemState> learnList, int k){
		
		ArrayList<Result> NeighborsList = new ArrayList<Result>();
		ArrayList<Result> resultList = new ArrayList<Result>();
			
		for(SystemState newSystemState : learnList){
					
			// calculate variable dist � square of Euclidean distance from query measurements to flower�s dimensional parameters
			double distance = EuDistance(query, newSystemState.values());
			// replace null with label of the state
			resultList.add(new Result(distance, "label_1"));
		}
		
//		sort the results from closest to further
		Collections.sort(resultList, new DistanceComparator());
//		ArrayList<Result> resultList1 = new ArrayList<Result>(resultList);
		
		for(int i=0; i<k; i++){
			NeighborsList.add(resultList.get(i));
		}
		
//		for(int x = 0; x < resultList.size(); x++){
//			System.out.println(resultList.get(x).type + "�" + resultList.get(x).distance);
//		}
//		for(int x = 0; x < NeighborsList.size(); x++){
//			System.out.println(NeighborsList.get(x).type + "�" + NeighborsList.get(x).distance);
//		}
		
		return NeighborsList;
	 }
	 
	//##################################################################################
	// determine the label of the System State
	 private static String  sortState(ArrayList<Result> NeighborsList, double[] query, int numLabels){
		 String label = "";
		 double[] labelArray = new double[numLabels];
		 // counts how many System States belong to each cluster
		 for(Result neighbor : NeighborsList){
			 label = neighbor.label;
			 for(int i=0; i<numLabels; i++){
				 if(label.equals("label_" + (i+1))){
	//						System.out.println("hello bitch");
				 	labelArray[i]++;
				 }
			 }
		 }
		 // determine the most common cluster
		 int maxIndex = 0;
		 double maxValue = 0;
		 for(int i=0; i<labelArray.length; i++){
			 if(labelArray[i]>maxValue){
				 maxValue = labelArray[i];
				 maxIndex = i;
			 }
		 }
		 // check if the solution is unique
		 int nonUniqueness = 0;
		 for(int i=0; i<labelArray.length; i++){
			 if(labelArray[i]==maxValue){
				nonUniqueness++;
			 }
		 }
		 if(nonUniqueness>1){
			 System.out.println("The solution is not unique! Change number of neighbors!");
		 }
		 
		 // create labeled SystemState
		 label = "label_" + (maxIndex+1);
		 return label;
	 }
}



