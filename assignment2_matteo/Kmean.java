package assignment2_matteo;

import java.util.ArrayList;
import java.util.Random;

import assignment2.SystemState;

public class Kmean {
	
	// SystemList has to be an ArrayList of Objects
	// replace double[] with object
	public static ArrayList<SystemState> SystemList;
	public static double tol ;
	
	// ##################################################################################
	// constructor
	public Kmean(ArrayList<SystemState> SystemList, double tol){
		Kmean.SystemList = SystemList;
		Kmean.tol = tol;
	}
	
	
	// ##################################################################################
	
	public static ArrayList<ArrayList<SystemState>> kMeanClustering(int k) {
			
			// arbitrarily assign centroids from within the dataset
			// create an arrayList of clusters, which are arraylists of SystemState
			ArrayList<ArrayList<SystemState>> Clusters = new ArrayList<ArrayList<SystemState>>(k);
			// create initial centroid using forgy method
			ArrayList<double[]> Centroid =  forgy(k);
			
			boolean check = true;
			// to count the iteration
			int keepTime = 0;
			// start the loop
			while(check){
				keepTime++;
				for(int ii= 0; ii<SystemList.size(); ii++){
					// assigned cluster
					int assignedClusterIndex = 0; 
					double minDistance = 1e9;
					// extract values of ii-th state
					// ######
					double[] values = SystemList.get(ii).values();
//					double[] values = new double[18];
					// ######
					// i is the number of the cluster
					for(int i=0; i<k; i++ ){
						// calculate distance of each value from the centroids
						double newDistance = EuDistance(values,Centroid.get(i));
						if(newDistance < minDistance){
							minDistance = newDistance;
							 assignedClusterIndex = i;
						}
					}
					// place the object in the closest cluster
					Clusters.get(assignedClusterIndex).add(SystemList.get(ii));
				}
				// calculate the new centroids
				ArrayList<double[]> newCentroid = calCentroids(Clusters);
				// calculate distance old and new centroid
				check = false;
				for(int i=0; i<k; i++){
					double delta = EuDistance( Centroid.get(i), newCentroid.get(i));
					System.out.println("Delta cluster " + (i+1) + " " + delta );
					if(delta > tol){
						check=true;
					}
				}
				Centroid= newCentroid;
				// if difference is less than a specified tolerance clustering is done
			System.out.println(" iterations: " + keepTime);
			}
			return Clusters;
		}
	// ##################################################################################
    // forgy method
	public static ArrayList<double[]> forgy(int k){
		// create a random number to assign centroid
		ArrayList<Integer> bowl = new ArrayList<Integer>();
		// fill the bowl with number from 0 to SystemList.size()
		for(int i=0; i<SystemList.size();i++){
			bowl.add(i);
		}
		ArrayList<double[]> Centroid = new ArrayList<double[]>();
		// extract k numbers from the bowl
		for(int i=0; i<k ;i++){
			Random rand = new Random();
			// determine random position to extract from the bowl
			int  ranNumindex = rand.nextInt(bowl.size());
			// take the number in that position from the bowl
			int ranNum = bowl.get(ranNumindex);
			// add the ranNum number element to centroid
			// ######
			double[] newValueSet = SystemList.get(ranNum).values();
			// ######
//			double[] newValueSet = new double[18];
			Centroid.add(newValueSet);
			// remove the number from the bowl
			bowl.remove(ranNumindex);
		}
		return Centroid;
	}
	// ##################################################################################
    // random partition method
	public static ArrayList<double[]> RPM(int k){
		ArrayList<double[]> Centroid = new ArrayList<double[]>();
		ArrayList<ArrayList<SystemState>> Clusters = new ArrayList<ArrayList<SystemState>>(k);
		// create a random number to assign centroid
		ArrayList<Integer> bowl = new ArrayList<Integer>();
		// fill the bowl with number from 0 to SystemList.size()
		for(int i=0; i<SystemList.size();i++){
			bowl.add(i);
		}
		// assign each object to a cluster randomly
		for(int i=0; i<SystemList.size();i++){
			Random rand = new Random();
			int  ranNumindex = rand.nextInt(bowl.size());
			int  ranNClusindex = rand.nextInt(k);
			// insert the ranNumindex element from SystemList to the ranNClusindex Cluster
			Clusters.get(ranNClusindex).add(SystemList.get(ranNumindex));
			bowl.remove(ranNumindex);
		}
		Centroid = calCentroids(Clusters);
		return Centroid;
	}
	
	// ##################################################################################
	// calculate new centroids
	private static  ArrayList<double[]> calCentroids(ArrayList<ArrayList<SystemState>> Clusters){
		 ArrayList<double[]> Centroid = new ArrayList<double[]>();
		for(int i=0; i< Clusters.get(0).size(); i++){
			Centroid.add(MeanOneCluster(Clusters.get(i)));
		}
		return Centroid;
	}
		
	// ##################################################################################
	// calculate euclidian distance
	public static double EuDistance(double[] X1, double[] X2){
		double distance;
		double sum = 0;
		for(int i=0; i<X1.length; i++){
			sum = sum + Math.pow((X2[i] - X1[i]),2); 
		}
		distance = Math.sqrt(sum);
		return distance;
	}
	// ##################################################################################
	// calculate the average value of the cluster formed by the ArrayList values
		private static double[] MeanOneCluster(ArrayList<SystemState> clusterElements){
			// #####
//			double[] mean = new double[clusterElements.get(0).values.length];
			// ######
			double[] mean = new double[18];
			for(int i=0; i<clusterElements.size();i++){
				// #####
				double[] newValues = clusterElements.get(i).values();
				// #####
//				double[] newValues = new double[18];
				mean = ArraySum(newValues,mean,'+');
			}
			for(int i=0; i<mean.length;i++){
				mean[i] = mean[i]/clusterElements.size();
			}
			return mean;
		}
		
	// ##################################################################################
	// perform the sum between two arrays
		private static double[] ArraySum(double[] A1, double[] A2, char sign){
			if(sign=='-'){
				double[] result = new double[A1.length]; 
				for(int i=0; i<A1.length;i++){
					result[i] = A1[i] - A2[i];
				}
				return result;
			}else{
				double[] result = new double[A1.length]; 
				for(int i=0; i<A1.length;i++){
					result[i] = A1[i] + A2[i];
				}
				return result;
			}
		}
}
