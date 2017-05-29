package assignment2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import org.jfree.ui.RefineryUtilities;

import assignment2.SystemState;
import assignment2_matteo.PlotClusters;

public class Kmean {
	
	// SystemList has to be an ArrayList of Objects
	// replace double[] with object
	public static ArrayList<SystemState> SystemList;
	public static double tol ;
	public static int maxIter ;
	// ##################################################################################
	// constructor
	public Kmean(ArrayList<SystemState> SystemList, double tol, int maxIter){
		Kmean.SystemList = SystemList;
		Kmean.tol = tol;
		Kmean.maxIter = maxIter;
	}
	
	
	// ##################################################################################
	
	public ArrayList<ArrayList<SystemState>> kMeanClustering(int k, int kOrg, String initMethod) {
			
			// arbitrarily assign centroids from within the dataset
			// create an arrayList of clusters, which are arraylists of SystemState
			ArrayList<ArrayList<SystemState>> Clusters = new ArrayList<ArrayList<SystemState>>();
			
			for(int i=0; i<k;i++){
				Clusters.add(new ArrayList<SystemState>());
			}

			// create initial centroid using forgy method
			ArrayList<double[]> Centroid = new ArrayList<double[]>();
			if(initMethod.equals("RPM")){
				Centroid =  RPM(k);
			}else{
				Centroid =  forgy(k);
			}
			
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
				
				// deal with empty clusters
				Clusters = fillIt( Clusters, k);
				
//				// plot 
//				String titlePlot = "Clusters plot iter# " + keepTime;
//				final PlotClusters demo = new PlotClusters(titlePlot,Clusters);
//			    demo.pack();
//			    RefineryUtilities.centerFrameOnScreen(demo);
//			    demo.setVisible(true);
				
				// calculate the new centroids
				ArrayList<double[]> newCentroid = calCentroids(Clusters);
				// calculate distance old and new centroid
				check = false;
				for(int i=0; i<k; i++){
					double delta = EuDistance( Centroid.get(i), newCentroid.get(i));
//					System.out.println("Delta cluster " + (i+1) + " " + delta );
					if(delta > tol && keepTime < maxIter){
						check=true;
						// clear values
						clear(Clusters);
					}
				}
				Centroid= newCentroid;
				// if difference is less than a specified tolerance clustering is done
//			System.out.println(" iterations: " + keepTime);
			}
			System.out.println(" iterations: " + keepTime);
			Clusters = downscaleClusters(Clusters, kOrg);
			if(kOrg < k){
				System.out.println("Downscaling successfully applied!");
			}
//			// plot after down scaling
//			String titlePlot = "Clusters plot iter# " + keepTime;
//			final PlotClusters demo = new PlotClusters(titlePlot,Clusters);
//		    demo.pack();
//		    RefineryUtilities.centerFrameOnScreen(demo);
//		    demo.setVisible(true);
			
			return Clusters;
		}
	// ##################################################################################
	// deal with empty cluster
	public ArrayList<ArrayList<SystemState>> fillIt(ArrayList<ArrayList<SystemState>> Clusters, int k){
		// deal with empty clusters
		Random rand = new Random();
		for(int i=0; i<Clusters.size();i++){
			if(Clusters.get(i).isEmpty()){
				System.out.println("fill me up");
				for(int ii=0; ii<Clusters.size();ii++){
					if(Clusters.get(ii).size()>1){
						int elementIndex = rand.nextInt(Clusters.get(ii).size());
						Clusters.get(i).add(Clusters.get(ii).get(elementIndex));
						// remove from old cluster
						Clusters.get(ii).remove(elementIndex);
						break;
					}
				}
			}
		}
		return Clusters;
	}
	
	// ##################################################################################
	// clear list
	public void clear(ArrayList<ArrayList<SystemState>> Clusters){
		for(int i=0; i<Clusters.size();i++){
			Clusters.get(i).clear();
		}
	}
	// ##################################################################################
    // forgy method
	public ArrayList<double[]> forgy(int k){
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
		System.out.println("Successfully initialized with forgy method!");
		return Centroid;
	}
	// ##################################################################################
    // random partition method
	public ArrayList<double[]> RPM(int k){
		ArrayList<double[]> Centroid = new ArrayList<double[]>();
		ArrayList<ArrayList<SystemState>> Clusters = new ArrayList<ArrayList<SystemState>>();
		for(int i=0; i<k;i++){
			Clusters.add(new ArrayList<SystemState>());
		}
		// create a random number to assign centroid
		ArrayList<Integer> bowl = new ArrayList<Integer>();
		// fill the bowl with number from 0 to SystemList.size()
		for(int i=0; i<SystemList.size();i++){
			bowl.add(i);
		}
		// assign each object to a cluster randomly
		Random rand = new Random();
		for(int i=0; i<SystemList.size();i++){
			int  ranNumindex = rand.nextInt(bowl.size());
//			System.out.println("random element index " + ranNumindex);
//			int  ranNClusindex = rand.nextInt(k);
			int  ranNClusindex = 0 ;
			for(int ii=0; ii<k; ii++){
				if(i <= (SystemList.size()/(k) + SystemList.size()/(k)*ii)){
					ranNClusindex=ii;
					break;
				}
			}
//			System.out.println("random cluster index " + ranNClusindex);
			// insert the ranNumindex element from SystemList to the ranNClusindex Cluster
			Clusters.get(ranNClusindex).add(SystemList.get(ranNumindex));
			bowl.remove(ranNumindex);
		}
		
		Centroid = calCentroids(Clusters);
//		for(int i=0; i < Centroid.size(); i++ ){
//			System.out.println("Centroid # " + i);
//			for(int ii=0; ii<Centroid.get(i).length;ii++){
//				System.out.print( Centroid.get(i)[ii] + " ");
//			}
//			System.out.println();
//		}
		System.out.println("Successfully initialized with random partition method!");
		return Centroid;
	}
	
	// ##################################################################################
	// calculate new centroids
	private static  ArrayList<double[]> calCentroids(ArrayList<ArrayList<SystemState>> Clusters){
		 ArrayList<double[]> Centroid = new ArrayList<double[]>();
		for(int i=0; i< Clusters.size(); i++){
			if(Clusters.get(i).isEmpty()){
				System.out.println("cluster " + (1+i) + " is empty");
			}else{
				Centroid.add(MeanOneCluster(Clusters.get(i)));
			}
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
			double[] mean = new double[clusterElements.get(0).values().length];
			// ######
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
		
		// ##################################################################################
		//  down scale clusters
		private static ArrayList<ArrayList<SystemState>> downscaleClusters(ArrayList<ArrayList<SystemState>> clusters, int n){
			ArrayList<ArrayList<SystemState>> tmpClusters = new ArrayList<ArrayList<SystemState>>();
			tmpClusters.addAll(clusters);
			ArrayList<double[]> centroids = calCentroids(tmpClusters);
			
			while(tmpClusters.size()>n){
				double minDist = 0, tmpDist = 0;
				int q1=0, q2=0;
				for(int k=0; k<tmpClusters.size(); k++){
					for(int j=k+1; j<tmpClusters.size(); j++){
						tmpDist = EuDistance(centroids.get(k), centroids.get(j));
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
				centroids = calCentroids(tmpClusters);
			}
			return tmpClusters;
		}
		
		
		
		// ##################################################################################
		// export csv
		public static void CSV(ArrayList<ArrayList<SystemState>> Clusters , int k) {
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
				kill();
			}
	    }
		// ############################################################################################################
		// method to kill the program
		@SuppressWarnings("deprecation")
		public static void kill(){
//			try {
//				Clip clip = AudioSystem.getClip();
////				File file = new File("./src/doh.wav");
////			    AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
//			    AudioInputStream inputStream = AudioSystem.getAudioInputStream(Gui.class.getResource("/doh.wav"));
//			    clip.open(inputStream);
//			    clip.start(); 
//			} 
//			catch (Exception e) {
//				System.err.println(e.getMessage());
//			}	
			System.out.println("\n=> Program Intentionally Terminated (Kill it before it lays eggs!!!)");
			Thread.currentThread().stop();
		}
}
