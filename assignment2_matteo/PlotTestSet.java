package assignment2_matteo;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

//import assignment2.FillStates;
//import assignment2.Kmean;
//import assignment2.SystemState;


public class PlotTestSet extends Frame {

	public PlotTestSet(final String title, ArrayList<SystemState> testSet) {

	    super(title);
	    // legend
	    final XYSeriesCollection data = new XYSeriesCollection();
	    for(int i=0; i<testSet.size(); i++){
	    	String namePlot = "measurement " + (i+1) + " " +testSet.get(i).label;
	    final XYSeries series = new XYSeries(namePlot);
	    meanOneState( testSet.get(i), series);
	    data.addSeries(series);
	    }
	   
		final JFreeChart chart = ChartFactory.createScatterPlot(
	        "Test Set",
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
//	    setContentPane(chartPanel);
	    JOptionPane.showMessageDialog(null, chartPanel, "", JOptionPane.PLAIN_MESSAGE);

	}
	
	// ##################################################################################
	// mean voltage and angle one cluster
	private static void meanOneState(SystemState testState, XYSeries series){
		double meanV = 0;
		double meanAng = 0;
		double[] values =  testState.values();
		for(int ii=0; ii < values.length/2 ; ii ++ ){
			meanV = meanV + values[2*ii+1]/(values.length/2);
			meanAng = meanAng + values[2*ii]/(values.length/2);
		}
		series.add(meanAng,meanV);
	}
}