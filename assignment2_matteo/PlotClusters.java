package assignment2_matteo;

	
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import assignment2.FillStates;
import assignment2.Kmean;
import assignment2.SystemState;


public class PlotClusters extends ApplicationFrame {

	public PlotClusters(final String title, ArrayList<ArrayList<SystemState>> Clusters) {

	    super(title);
	    // legend
	    final XYSeriesCollection data = new XYSeriesCollection();
	    for(int i=0; i<Clusters.size(); i++){
	    	String namePlot = "Cluster " + (i+1);
	    final XYSeries series = new XYSeries(namePlot);
	    ArrayList<SystemState> newCluster = Clusters.get(i);
	    meanOneCluster( newCluster, series);
	    data.addSeries(series);
	    }
	   
		final JFreeChart chart = ChartFactory.createScatterPlot(
	        "Clusters",
	        "average state angle", 
	        "avarage state voltage", 
	        data,
	        PlotOrientation.VERTICAL,
	        true,
	        true,
	        false
	    );
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double widthScreen = screenSize.getWidth();
		double heightScreen = screenSize.getHeight();
		chart.getPlot().setBackgroundPaint( Color.WHITE );
		chart.getXYPlot().setDomainGridlinesVisible(true);
		chart.getXYPlot().setRangeGridlinePaint(Color.GRAY);
		chart.getXYPlot().setDomainGridlinePaint(Color.GRAY);
	    final ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new java.awt.Dimension( (int) (widthScreen*0.6), (int) (heightScreen*0.6)));
	    setContentPane(chartPanel);

	}
	
	// ##################################################################################
	// mean voltage and angle one cluster
	private static void meanOneCluster(ArrayList<SystemState> newCluster, XYSeries series){
		ArrayList<Double> meanVOneCluster = new ArrayList<Double>();
		ArrayList<Double> meanAngOneCluster = new ArrayList<Double>();
		for(int i=0; i < newCluster.size(); i++){
			double[] values =  newCluster.get(i).values();
			double meanV = 0;
			double meanAng = 0;
			for(int ii=0; ii < values.length/2 ; ii ++ ){
				meanV = meanV + values[2*ii+1]/(values.length/2);
				meanAng = meanAng + values[2*ii]/(values.length/2);
			}
			meanVOneCluster.add(meanV);
			meanAngOneCluster.add(meanAng);
			series.add(meanAng,meanV);
		}
	}
	// ##################################################################################
	public static void main(final String[] args) {

		FillStates fillings = new FillStates();
		ArrayList<SystemState> allStates = fillings.getStates("root", "Callandor14", "measurements");
		Kmean kmeanTest = new Kmean(allStates, 1e-16, 1000);
		ArrayList<ArrayList<SystemState>> Clusters= kmeanTest.kMeanClustering(16,4,"forgy");
		for(int ii=0; ii<Clusters.size(); ii++){
			System.out.println("Cluster number " + (ii+1) + " Cluster size " + Clusters.get(ii).size());
		}
	    final PlotClusters demo = new PlotClusters("Clusters plot",Clusters);
	    demo.pack();
	    RefineryUtilities.centerFrameOnScreen(demo);
	    demo.setVisible(true);
	}
}


