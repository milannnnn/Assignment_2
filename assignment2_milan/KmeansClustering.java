package assignment2_milan;

import java.util.ArrayList;
import java.lang.Math;
import java.util.Random;

// 

public class KmeansClustering {
	
	
	public static void main(String[] args){
		
		double tol = 1e-16;
		boolean runLoop = true;
		
		int k = 4;
		FillStates fillings = new FillStates();
		ArrayList<SystemState> allStates = fillings.getStates("root", "root", "measurements");
		//ArrayList<double[]> centroids = pickCentroids(allStates, k);
		ArrayList<double[]> centroids = pickCentroidsRPM(allStates, k);
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
	
	private static double euclDist(double[] f1, double[] f2){
		double dist = 0;
		for(int k=0; k<f1.length; k++){
			dist+=(f1[k]-f2[k])*(f1[k]-f2[k]);
		}
		dist = Math.sqrt(dist);
		return dist;
	}
	
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

}

