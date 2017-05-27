package assignment2_milan;

import java.util.ArrayList;
import java.lang.Math;
import java.util.Random;

// 

public class KmeansClustering {
	
	
	public static void main(String[] args){
		
		double tol = 1e-16;
		boolean runLoop = true;
		
		int kOrg =  4;
		int k    = 16;
		FillStates fillings = new FillStates();
		ArrayList<SystemState> allStates = fillings.getStates("root", "root", "measurements");
		ArrayList<double[]> centroids = pickCentroids(allStates, k);
		//ArrayList<double[]> centroids = pickCentroidsRPM(allStates, k);
		ArrayList<double[]> newCentroids;
		ArrayList<ArrayList<SystemState>> clusters = new ArrayList<ArrayList<SystemState>>();
		
		int loopCounter = 0;
		while(runLoop){
			loopCounter++;
			clusters = splitClusters(allStates, centroids);
			newCentroids = calcNewCent(clusters);
			double diff = calcCentDiff(centroids,newCentroids);
			centroids = newCentroids;
			if(diff<tol){
				runLoop = false;
			}
			System.out.printf("Itteration %d (difference %f)\n",loopCounter,diff);
		}
		
		//### Downscaling to Original Number of Clusters:
		clusters = downscaleClusters(clusters, kOrg);
		
		for(int j=0; j<clusters.size(); j++){
			System.out.print(clusters.get(j).size()+"\t");
		}
//		for(int j=0; j<clusters.size(); j++){
//			System.out.println("Cluser "+(j+1));
//			for(int m=0; m<clusters.get(j).size(); m++){
//				clusters.get(j).get(m).printValues();
//			}
//		}
	
	}
	
	//##### Forgy Method for Picking Initial Centroids:
	private static ArrayList<double[]> pickCentroids(ArrayList<SystemState> states, int n){
		ArrayList<double[]> centroids = new ArrayList<double[]>();
		ArrayList<double[]> positions = new ArrayList<double[]>();
		for(int k=0; k<states.size(); k++){
			positions.add(states.get(k).values());
		}
		Random rand = new Random();
		int j;
		for(int k=0; k<n; k++){
			j = rand.nextInt(positions.size());
			centroids.add(positions.get(j));
			positions.remove(j);
		}
		return centroids;
	}
	
	//##### RPM   Method for Picking Initial Centroids:
	private static ArrayList<double[]> pickCentroidsRPM(ArrayList<SystemState> states, int n){
		ArrayList<double[]> centroids = new ArrayList<double[]>();
		ArrayList<SystemState> tmpStates = new ArrayList<SystemState>();
		tmpStates.addAll(states);
		ArrayList<ArrayList<SystemState>> splits = new ArrayList<ArrayList<SystemState>>();
		for(int k=0; k<n; k++){
			splits.add(new ArrayList<SystemState>());
		}
		Random rand = new Random();
		int j;
		for(int k=tmpStates.size(); k>0; k--){
			j = rand.nextInt(tmpStates.size());
			splits.get(k%n).add(tmpStates.get(j));
			tmpStates.remove(j);
		}
		centroids = calcNewCent(splits);
		return centroids;
	}
	
	//##### Method for Calculating Euclidean Distance Between 2 Points
	private static double euclDist(double[] f1, double[] f2){
		double dist = 0;
		for(int k=0; k<f1.length; k++){
			dist+=(f1[k]-f2[k])*(f1[k]-f2[k]);
		}
		dist = Math.sqrt(dist);
		return dist;
	}
	
	//##### Method for Finding Closest Centroid for a State
	private static int findClosestCent(SystemState fl, ArrayList<double[]> centroids){
		int p=0;
		double dist, minDist;
		minDist = euclDist(fl.values(), centroids.get(0));
		for(int k=1; k<centroids.size(); k++){
			dist = euclDist(fl.values(), centroids.get(k));
			if(dist < minDist){
				minDist = dist;
				p=k;
			}
		}
		return p;
	}
	
	private static ArrayList<ArrayList<SystemState>> splitClusters(ArrayList<SystemState> allStates, ArrayList<double[]> centroids){
		ArrayList<ArrayList<SystemState>> clusters = new ArrayList<ArrayList<SystemState>>();
		// Create Array Lists of States for Each Cluster
		for(int k=0; k<centroids.size(); k++){
			clusters.add(new ArrayList<SystemState>());
		}
		for(int j=0; j<allStates.size(); j++){
			int p = findClosestCent(allStates.get(j), centroids);
			clusters.get(p).add(allStates.get(j));
		}
		
		// If we have any empty Clusters, fill them with one value from other clusters
		Random rand = new Random();
		for(int k=0; k<centroids.size(); k++){
			if(clusters.get(k).size()==0){
				for(int j=0; j<centroids.size(); j++){
					if(clusters.get(j).size()>1){
						int q = rand.nextInt(clusters.get(j).size());
						clusters.get(k).add(clusters.get(j).get(q));
						clusters.get(j).remove(q);
						break;
					}
				}
			}
		}
		return clusters;
	}
	
	private static ArrayList<double[]> calcNewCent(ArrayList<ArrayList<SystemState>> clusters){
		ArrayList<double[]> newCentroids = new ArrayList<double[]>();
		for(int k=0; k<clusters.size(); k++){
			ArrayList<SystemState> cluster = clusters.get(k);
			double total  = cluster.size();
			int numOfVar  = cluster.get(0).values().length;
			double[] dims = new double[numOfVar];
			for(int q=0; q<numOfVar; q++){
				for(int j=0; j<cluster.size(); j++){
					dims[q]+=cluster.get(j).values()[q]/total;
				}
			}
			newCentroids.add(dims);
		}
		return newCentroids;
	}
	
	private static double calcCentDiff(ArrayList<double[]> oldCentroids, ArrayList<double[]> newCentroids){
		double dist = 0;
		for(int k=0; k<oldCentroids.size(); k++){
			dist += euclDist(oldCentroids.get(k),newCentroids.get(k));
		}
		return dist;
	}
	
	private static ArrayList<ArrayList<SystemState>> downscaleClusters(ArrayList<ArrayList<SystemState>> clusters, int n){
		ArrayList<ArrayList<SystemState>> tmpClusters = new ArrayList<ArrayList<SystemState>>();
		tmpClusters.addAll(clusters);
		ArrayList<double[]> centroids = calcNewCent(tmpClusters);
		
		while(tmpClusters.size()>n){
			double minDist = 0, tmpDist = 0;
			int q1=0, q2=0;
			for(int k=0; k<tmpClusters.size(); k++){
				for(int j=k+1; j<tmpClusters.size(); j++){
					tmpDist = euclDist(centroids.get(k), centroids.get(j));
					if(k==0 && j==1){
						minDist = tmpDist;
						q1 = k;
						q2 = j;
					}
					else{
						if(tmpDist<minDist){
							minDist = tmpDist;
							q1 = k;
							q2 = j;
						}
					}
				}
			}
			tmpClusters.get(q1).addAll(tmpClusters.get(q2));
			tmpClusters.remove(q2);
			centroids = calcNewCent(tmpClusters);
		}
		return tmpClusters;
	}
}

