package assignment2_milan;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.Math;
import java.util.Random;

public class KmeansClustering {
	
	public ArrayList<SystemState> allStates;
	public double tol;
	public int maxIters;
	
	public KmeansClustering(ArrayList<SystemState> allStates1, double tol1, int maxIter1){
		this.allStates 	= allStates1;
		this.tol 		= tol1;
		this.maxIters 	= maxIter1;
	}
	
	public ArrayList<ArrayList<SystemState>> clusterData(int k, int kOrg, String initMethod){

		boolean runLoop = true;
		
		//### Initialize the Centroids (based on desired initMethod)
		ArrayList<double[]> centroids = new ArrayList<double[]>();
		if(initMethod.equals("RPM")){
			centroids = pickCentroidsRPM(allStates, k);
		}
		else{
			centroids = pickCentroids(allStates, k);
		}
		ArrayList<double[]> newCentroids;
		ArrayList<ArrayList<SystemState>> clusters = new ArrayList<ArrayList<SystemState>>();
		
		int loopCounter = 0;
		while(runLoop){
			loopCounter++;
			clusters = splitClusters(allStates, centroids);
			newCentroids = calcNewCent(clusters);
			double diff = calcCentDiff(centroids,newCentroids);
			centroids = newCentroids;
			if(diff<tol || loopCounter>maxIters){
				runLoop = false;
			}
			System.out.printf("Itteration %d (difference %f)\n",loopCounter,diff);
		}
		
		//### Downscaling to Original Number of Clusters:
		clusters = downscaleClusters(clusters, kOrg);
		
		//CSV(clusters,kOrg);
		
		return clusters;
	}
	
	//##### Forgy Method for Picking Initial Centroids:
	private ArrayList<double[]> pickCentroids(ArrayList<SystemState> states, int n){
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
	private ArrayList<double[]> pickCentroidsRPM(ArrayList<SystemState> states, int n){
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
	private double euclDist(double[] f1, double[] f2){
		double dist = 0;
		for(int k=0; k<f1.length; k++){
			dist+=(f1[k]-f2[k])*(f1[k]-f2[k]);
		}
		dist = Math.sqrt(dist);
		return dist;
	}
	
	//##### Method for Finding Closest Centroid for a State
	private int findClosestCent(SystemState fl, ArrayList<double[]> centroids){
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
	
	private ArrayList<ArrayList<SystemState>> splitClusters(ArrayList<SystemState> allStates, ArrayList<double[]> centroids){
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
	
	private ArrayList<double[]> calcNewCent(ArrayList<ArrayList<SystemState>> clusters){
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
	
	private double calcCentDiff(ArrayList<double[]> oldCentroids, ArrayList<double[]> newCentroids){
		double dist = 0;
		for(int k=0; k<oldCentroids.size(); k++){
			dist += euclDist(oldCentroids.get(k),newCentroids.get(k));
		}
		return dist;
	}
	
	private ArrayList<ArrayList<SystemState>> downscaleClusters(ArrayList<ArrayList<SystemState>> clusters, int n){
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

	public void CSV(ArrayList<ArrayList<SystemState>> Clusters , int k) {
        PrintWriter pw;
		try {
			ArrayList<PrintWriter> pwArray = new ArrayList<PrintWriter>();
			for(int i=0; i < k ;i++){
				String name = "cluster_" + (i+1) + ".csv";
				pw = new PrintWriter(new File(name));
				pwArray.add(pw);
			}
			
			for(int ii=0; ii<Clusters.size(); ii++){
				StringBuilder sb = new StringBuilder();
				for(int i=0; i<Clusters.get(ii).size(); i++){
					sb.append(Clusters.get(ii).get(i).stringValues());
				}
				pw = pwArray.get(ii);
				pw.write(sb.toString());
		        pw.close();
			}

	        System.out.println("CSV created!");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}