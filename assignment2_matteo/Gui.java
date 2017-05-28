package assignment2_matteo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;

import org.jfree.ui.RefineryUtilities;

import assignment2.FillStates;
import assignment2.Kmean;
import assignment2.SystemState;

@SuppressWarnings("serial")
public class Gui extends JFrame {
	
	// screen dimensions
	double widthScreen;
	double heightScreen;
	// Declare objects that will appear on the interface
	// TextFields
	private JTextField tf1;
	private JPasswordField tf2;
	private JTextField tf1title;
	private JTextField tf2title;
	private JTextField Customtitle;
	private JTextField KMEANtitle;
	private JTextField Scaletitle;
	private JTextField Actiontitle;
	private JTextArea errorText;
	private JTextField numTempClusters;
	private JTextField numTempClustersValue;
	private JTextField maxIterTitle;
	private JTextField maxIterText;
	// buttons
	private JRadioButton defb;
	private JRadioButton cusb;
	private JRadioButton forgybutton;
	private JRadioButton RPMbutton;
	private ButtonGroup groupDC;
	private ButtonGroup groupF_RPM;
	private JButton buttCluster;
	private JButton buttPlot;
	private JButton buttKNN;
	// checkBox
	private JCheckBox downScaleCB;
	// set boolean variable to allow the choice of custom options
	boolean	customOpt = false;
	// set default variables
	private String USER = "root";
	private String PASS = "root";
	private int tempClusters = 16;
	private String KmeanMethod = "forgy";
	ArrayList<ArrayList<SystemState>> Clusters;
	private int maxIter = 100;
	
	// ############################################################################################################
	public Gui(){
		
		// set interface title
		super("Project 2");
		// use a pre-determined layout, i.e. FlowLayout
		setLayout(new FlowLayout());
		
//		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		// get screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		widthScreen = screenSize.getWidth();
		heightScreen = screenSize.getHeight();
		// set the dimension for the console considering different screen resolutions 
		int consoleHeight, consoleWidth;
		if(widthScreen>=1920){
			widthScreen  = 1920.0;
			heightScreen = 1080.0;
			consoleHeight = (int) (0.475*heightScreen);
			consoleWidth  = (int) (0.495* widthScreen);
		}
		else if(widthScreen==1366){
			consoleHeight = (int) (0.320*heightScreen);
			consoleWidth  = (int) (0.495* widthScreen);
		}
		else{
			consoleHeight = (int) (0.250*heightScreen);
			consoleWidth  = (int) (0.495* widthScreen);
		}
		
		// set the width of text fields
		int textWidth = (int) (widthScreen/2/500*60);
		
		// create RADIO BUTTONS and TEXT TITLE
		// default/ custom radio buttons
		Customtitle = new JTextField("DEFAULT-CUSTOM OPTIONS", textWidth);
		Customtitle.setFont(new Font("Serif",Font.BOLD, 18));
		Customtitle.setHorizontalAlignment(JTextField.CENTER);
		Customtitle.setBackground(Color.GRAY);
		Customtitle.setEditable(false);
		add(Customtitle);
		defb = new JRadioButton("default options", true);
		defb.setFont(new Font("Serif",Font.BOLD, 18));
		// true is checked, false in unchecked
		cusb = new JRadioButton("custom options", false);
		cusb.setFont(new Font("Serif",Font.BOLD, 18));
		add(defb);
		add(cusb);
		// group the radiobuttons together
		groupDC = new ButtonGroup();
		groupDC.add(defb);
		groupDC.add(cusb);
		
		// create graphical objects
		// TEXTFIELDS (USER, PASSWORD, BASE POWER)
		tf1title = new JTextField("MySQL USERNAME", textWidth);
		tf1title.setEditable(false);
		tf1title.setFont(new Font("Serif",Font.BOLD, 18));
		tf1title.setHorizontalAlignment(JTextField.CENTER);
		tf1title.setBackground(Color.GRAY);
		add(tf1title);
		tf1 = new JTextField("root", 15);
		tf1.setFont(new Font("Serif",Font.PLAIN, 18));
		tf1.setToolTipText("insert MySQL USERNAME and press Enter");
		// by default they are set not editable
		tf1.setEditable(customOpt);
		add(tf1);
		
		tf2title = new JTextField("MySQL PASSWORD", textWidth);
		tf2title.setFont(new Font("Serif",Font.BOLD, 18));
		tf2title.setHorizontalAlignment(JTextField.CENTER);
		tf2title.setBackground(Color.GRAY);
		tf2title.setEditable(false);
		add(tf2title);
		tf2 = new JPasswordField("root", 15);
		tf2.setFont(new Font("Serif",Font.PLAIN, 18));
		tf2.setEditable(customOpt);
		tf2.setToolTipText("insert MySQL PASSWORD and press Enter");
		add(tf2);
		
		// random partition - forgy method radio buttons
		KMEANtitle = new JTextField("K-MEAN INITIALIZATION OPTIONS", textWidth);
		KMEANtitle.setFont(new Font("Serif",Font.BOLD, 18));
		KMEANtitle.setHorizontalAlignment(JTextField.CENTER);
		KMEANtitle.setBackground(Color.GRAY);
		KMEANtitle.setEditable(false);
		add(KMEANtitle);
		forgybutton = new JRadioButton("forgy method", true);
		forgybutton.setFont(new Font("Serif",Font.BOLD, 18));
		forgybutton.setEnabled(false);
		// true is checked, false in unchecked
		RPMbutton = new JRadioButton("random partition method", false);
		RPMbutton.setFont(new Font("Serif",Font.BOLD, 18));
		RPMbutton.setEnabled(false);
		add(forgybutton);
		add(RPMbutton);
		// group the radio buttons together
		groupF_RPM= new ButtonGroup();
		groupF_RPM.add(forgybutton);
		groupF_RPM.add(RPMbutton);
		// max iter
		maxIterTitle = new JTextField("Maximum iteration",20);
		maxIterTitle.setBorder(null);
		maxIterTitle.setHorizontalAlignment(JTextField.CENTER);
		maxIterTitle.setFont(new Font("Serif",Font.BOLD, 18));
		maxIterTitle.setEditable(false);
		maxIterTitle.setEnabled(false);
		add(maxIterTitle);
		maxIterText = new JTextField("100", 5);
		maxIterText.setFont(new Font("Serif",Font.PLAIN, 18));
		maxIterText.setHorizontalAlignment(JTextField.CENTER);
		maxIterText.setEditable(false);
		maxIterText.setEnabled(false);
		maxIterText.setToolTipText("Set number of maximum iteration and press enter");
		add(maxIterText);
		// down scaling options
		Scaletitle = new JTextField("DOWN-SCALING OPTIONS", textWidth);
		Scaletitle.setFont(new Font("Serif",Font.BOLD, 18));
		Scaletitle.setHorizontalAlignment(JTextField.CENTER);
		Scaletitle.setBackground(Color.GRAY);
		Scaletitle.setEditable(false);
		add(Scaletitle);
		// down Scale check box
		downScaleCB = new JCheckBox("Apply down-scaling");
		downScaleCB.setFont(new Font("Serif",Font.BOLD, 18));
		downScaleCB.setSelected(false);
		downScaleCB.setEnabled(false);
		add(downScaleCB);
		// number of temporary clusters
		numTempClusters = new JTextField("Temporary Clusters Number",20);
		numTempClusters.setBorder(null);
		numTempClusters.setHorizontalAlignment(JTextField.CENTER);
		numTempClusters.setFont(new Font("Serif",Font.BOLD, 18));
		numTempClusters.setEditable(false);
		numTempClusters.setEnabled(false);
		add(numTempClusters);
		numTempClustersValue = new JTextField("16", 5);
		numTempClustersValue.setFont(new Font("Serif",Font.PLAIN, 18));
		numTempClustersValue.setHorizontalAlignment(JTextField.CENTER);
		numTempClustersValue.setEditable(false);
		numTempClustersValue.setEnabled(false);
		numTempClustersValue.setToolTipText("Set number of temporary clusters and press enter");
		add(numTempClustersValue);
		
		// create buttons to execute K-mean and KNN
		Actiontitle = new JTextField("CREATION SECTION", textWidth);
		Actiontitle.setFont(new Font("Serif",Font.BOLD, 18));
		Actiontitle.setHorizontalAlignment(JTextField.CENTER);
		Actiontitle.setBackground(Color.GRAY);
		Actiontitle.setEditable(false);
		add(Actiontitle);
		buttCluster = new JButton("Create Clusters");
		buttCluster.setFont(new Font("Serif",Font.BOLD, 18));
		add(buttCluster);
		buttPlot = new JButton("Plot Clusters");
		buttPlot.setFont(new Font("Serif",Font.BOLD, 18));
		buttPlot.setEnabled(false);
		add(buttPlot);
		buttKNN = new JButton("Classify Test-set");
		buttKNN.setFont(new Font("Serif",Font.BOLD, 18));
		add(buttKNN);
		
		
		// create console to display outputs
		errorText = new JTextArea();
		// set text color to red
		errorText.setForeground(Color.RED);
		PrintStream printStream = new PrintStream(new CustomOutputStream(errorText));
		System.setOut(printStream);
		System.setErr(printStream);
		// add a scroll pane
		JScrollPane scrollPane1 = new JScrollPane(errorText, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane1.setPreferredSize(new Dimension(consoleWidth,consoleHeight));
		add(scrollPane1);
		
		// create events handlers
		// handle text fields enter insertion
		EnterHandler enterhandler = new EnterHandler();
		tf1.addActionListener(enterhandler);
		tf2.addActionListener(enterhandler);
		EnterHandlerCluster enterHandlerCluster = new EnterHandlerCluster();
		numTempClustersValue.addActionListener(enterHandlerCluster);
		// handle default-custom options with radio buttons
		ClickHandler clickhandler = new ClickHandler();
		defb.addItemListener(clickhandler);
		cusb.addItemListener(clickhandler);
		downScaleCB.addActionListener(enterhandler);
		// handle initialization of kmean
		KmeanInit kmeanInit = new KmeanInit();
		forgybutton.addItemListener(kmeanInit);
		RPMbutton.addItemListener(kmeanInit);
		
		// handle max iter
		EnterHandleriter enterHandleriter = new EnterHandleriter();
		maxIterText.addActionListener(enterHandleriter);
		// handle cluster creation button
		ButtonKmeanHandler buttonKmeanHandler = new ButtonKmeanHandler();
		buttCluster.addActionListener(buttonKmeanHandler);
		// handle plot button
		ButtonPlotHandler buttonPlotHandler = new ButtonPlotHandler();
		buttPlot.addActionListener(buttonPlotHandler);
	}

	
	
	 
	// ############################################################################################################
	// class to plot the console output in the GUI
	public class CustomOutputStream extends OutputStream {
	    private JTextArea textArea;
	     
	    public CustomOutputStream(JTextArea textArea) {
	        this.textArea = textArea;
	    }
	     
	    @Override
	    public void write(int b) throws IOException {
	        // redirects data to the text area
	        textArea.append(String.valueOf((char)b));
	        // scrolls the text area to the end of data
	        textArea.setCaretPosition(textArea.getDocument().getLength());
	    }
	}

	// ############################################################################################################
	// handle default-custom options with radio buttons
	private class ClickHandler implements ItemListener{
		public void itemStateChanged(ItemEvent event){
			// if default options are selected
			if(defb.isSelected() == true){
				// set the variable to default values
				USER = "root";
				PASS = "root";
				tempClusters = 16;
				KmeanMethod = "forgy";
				customOpt=false;
				tf1.setEditable(customOpt);
				tf2.setEditable(customOpt);
				RPMbutton.setEnabled(customOpt);
				forgybutton.setEnabled(customOpt);
				downScaleCB.setEnabled(customOpt);
				numTempClusters.setEnabled(customOpt);
				numTempClustersValue.setEnabled(customOpt);
				numTempClustersValue.setEnabled(customOpt);
				buttCluster.setEnabled(!customOpt);
				buttKNN.setEnabled(!customOpt);
				buttPlot.setEnabled(customOpt);
				maxIterText.setEditable(customOpt);
				maxIterText.setEnabled(customOpt);
				maxIterTitle.setEnabled(customOpt);
			}
			// if custom options are selected
			else if(cusb.isSelected() == true){
				customOpt=true;
				tf1.setEditable(customOpt);
				buttCluster.setEnabled(!customOpt);
				buttKNN.setEnabled(!customOpt);
				buttPlot.setEnabled(!customOpt);
			}
		}
	}
	
	// ############################################################################################################
	// handle enter press on text fields and checkbox
	private class EnterHandler implements ActionListener{
		public void actionPerformed(ActionEvent event){
			// when enter is pressed on USER field
			if(event.getSource()==tf1){
				USER = event.getActionCommand();
				JOptionPane.showMessageDialog(null, "USERNAME successfully inserted");
				tf2.setEditable(customOpt);
			}
			// when enter is pressed on PASS field
			else if(event.getSource()==tf2){
				PASS = event.getActionCommand();
				JOptionPane.showMessageDialog(null, "PASSWORD successfully inserted");
				RPMbutton.setEnabled(customOpt);
				forgybutton.setEnabled(customOpt);
				downScaleCB.setEnabled(customOpt);
				buttCluster.setEnabled(customOpt);
				buttKNN.setEnabled(customOpt);
				maxIterText.setEditable(customOpt);
				maxIterText.setEnabled(customOpt);
				maxIterTitle.setEnabled(customOpt);
			}
			else if(downScaleCB.isSelected()){
				numTempClusters.setEnabled(customOpt);
				numTempClustersValue.setEnabled(customOpt);
				numTempClustersValue.setEditable(customOpt);
				
			}
			else if(!(downScaleCB.isSelected())){
				numTempClusters.setEnabled(!customOpt);
				numTempClustersValue.setEnabled(!customOpt);
				numTempClustersValue.setEditable(!customOpt);
			}
			else if(event.getSource() == numTempClustersValue){
				tempClusters = Integer.parseInt(event.getActionCommand());
				JOptionPane.showMessageDialog(null, "Temporary clusters number successfully inserted");
			}
		}
	}// ############################################################################################################
	// handle temporary cluster's number insertion
	private class EnterHandleriter implements ActionListener{
		public void actionPerformed(ActionEvent event){
			if(event.getSource() == maxIterText){
				maxIter = Integer.parseInt(event.getActionCommand());
				JOptionPane.showMessageDialog(null, "Maximum iteration successfully inserted");
			}
	
		}
	}
	// ############################################################################################################
	// handle temporary cluster's number insertion
	private class EnterHandlerCluster implements ActionListener{
		public void actionPerformed(ActionEvent event){
			if(event.getSource() == numTempClustersValue){
				tempClusters = Integer.parseInt(event.getActionCommand());
				if(tempClusters<4){
					tempClusters = 4;
					JOptionPane.showMessageDialog(null, "Temporary clusters numberhas to be greater than 4!");
					numTempClustersValue.setText("4");
				}else{
					JOptionPane.showMessageDialog(null, "Temporary clusters number successfully inserted");
				}
			}
			
		}
	}
	// ############################################################################################################
		// handle default-custom options with radio buttons
		private class KmeanInit implements ItemListener{
			public void itemStateChanged(ItemEvent event){
				if(forgybutton.isSelected()){
					KmeanMethod = "forgy";
				}else if(RPMbutton.isSelected()){
					KmeanMethod = "RPM";
				}
			}
		}
	// ############################################################################################################
	// handle K-mean button
	private class ButtonKmeanHandler implements ActionListener{
		
		public void actionPerformed(ActionEvent event){
			// Run the main code inside a New Thread (if error occurs - only thread gets killed, and GUI stays operational)
			new Thread(){
				public void run(){
					// try to create database
					FillStates fillings = new FillStates();
					ArrayList<SystemState> allStates = fillings.getStates(USER, PASS, "measurements");
					Kmean kmeanTest = new Kmean(allStates, 1e-16, maxIter);
					Clusters= kmeanTest.kMeanClustering(tempClusters,4,KmeanMethod);
//					kmeanTest.CSV(Clusters, 4);
//					System.out.println(Clusters.size());
					for(int ii=0; ii<Clusters.size(); ii++){
						System.out.println("Cluster number " + (ii+1) + " Cluster size " + Clusters.get(ii).size());
//						System.out.println(Clusters.get(ii).size());
						for(int i=0; i<Clusters.get(ii).size(); i++){
							Clusters.get(ii).get(i).printValues();
							
						}
					}
					buttPlot.setEnabled(true);	
				}
				
			}.start();
					
		}
	}
//	 ############################################################################################################
	// handle Y-Matrix creation button
		private class ButtonPlotHandler implements ActionListener{					
			public void actionPerformed(ActionEvent event){	
			// Run the main code inside a New Thread (if error occurs - only thread gets killed, and GUI stays operational)
				new Thread(){
					public void run(){
						final PlotClusters plotClusters = new PlotClusters("Clusters plot",Clusters);
						plotClusters.pack();
					    RefineryUtilities.centerFrameOnScreen(plotClusters);
					    plotClusters.setVisible(false);
					}
				}.start();
			}
		}
	}


