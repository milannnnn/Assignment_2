package assignment2;

import java.util.ArrayList;

// ### SIMPLE Object Container Class - for Power System State Representation
public class SystemState {
	
	class SimpleObject{
		public String busID;
		public double voltage;
		public double angle;
		public SimpleObject(){
			busID 	= "";
			voltage = 0;
			angle 	= 0;
		}
	}
	
	public int time;

	public ArrayList<SimpleObject> buses = new ArrayList<SimpleObject>(); // Bus ID, Voltage, Angle
	
	// ###########################################################
	// Initialize a New System State
	public SystemState(){}
	public SystemState(String[] readData){
		// readData[0] - Name column
		// readData[1] - Time column
		// readData[2] - Value column
		// readData[3] - Bus ID column

		time = Integer.parseInt(readData[1]);
		SimpleObject tmpBus = new SimpleObject();
		tmpBus.busID = readData[3];
		if(isAngle(readData[0])){
			tmpBus.angle = Double.parseDouble(readData[2]);
		}
		else{
			tmpBus.voltage = Double.parseDouble(readData[2]);
		}
		buses.add(tmpBus);
	}
	
	//### Add New Data to Current State
	public void addData(String[] readData){
		// readData[0] - Name column
		// readData[1] - Time column
		// readData[2] - Value column
		// readData[3] - Bud ID column
		
		//### Search for a matching bus within the current state
		int pos = -1;
		for(int k=0; k<buses.size(); k++){
			if(buses.get(k).busID.equals(readData[3])){
				pos = k;
				break;
			}
		}
		
		//### If we have not found a matching bus, add a new one
		if(pos==-1){
			SimpleObject tmpBus = new SimpleObject();
			tmpBus.busID = readData[3];
			if(isAngle(readData[0])){
				tmpBus.angle = Double.parseDouble(readData[2]);
			}
			else{
				tmpBus.voltage = Double.parseDouble(readData[2]);
			}
			buses.add(tmpBus);
		}
		
		//### If we have found a matching bus, just add the volt / angle
		else{
			if(isAngle(readData[0])){
				buses.get(pos).angle   = Double.parseDouble(readData[2]);
			}
			else{
				buses.get(pos).voltage = Double.parseDouble(readData[2]);
			}
		}
	}
	
	// ###########################################################
	// Check if Measurement Is an Angle
	private boolean isAngle(String name){
		if(name.substring(name.length()-3, name.length()).equals("ANG")){
			return true;
		}
		return false;
	}
	
	// ###############################################################################################
	// Sorts Buses in Given Order of Appearance, and Eliminates all Buses Not Included in Sorting List
	public void sortBuses(String[] busOrder){
		ArrayList<SimpleObject> tmpBuses = new ArrayList<SimpleObject>();
		for(int k=0; k<busOrder.length; k++){
			for(int j=0; j<buses.size(); j++){
				if(busOrder[k].equals(buses.get(j).busID)){
					tmpBuses.add(buses.get(j));
					break;
				}
			}
		}
		buses.clear();
		buses.addAll(tmpBuses);
	}
	
	
	// ###########################################################
	// Normalize all State Variables with Min and Max Values
	public void normalize(double[] minAngles, double[] maxAngles, double[] minVolts, double[] maxVolts){
		for(int k=0; k<minAngles.length; k++){
			if(maxAngles[k]!=minAngles[k]){
				buses.get(k).angle   = (buses.get(k).angle-minAngles[k])/(maxAngles[k]-minAngles[k]);
			}
			else{
				buses.get(k).angle = 1;
			}
			
			if(maxAngles[k]!=minAngles[k]){
				buses.get(k).voltage = (buses.get(k).voltage-minVolts[k])/(maxVolts[k]-minVolts[k]);
			}
			else{
				buses.get(k).voltage = 1;
			}
		}
	}
	
	// ###########################################################
	// Returns the Vector of State Variables (angles and voltages)
	public double[] values(){
		double[] vals = new double[buses.size()*2];
		for(int k=0; k<buses.size(); k++){
			vals[2*k]   = buses.get(k).angle;
			vals[2*k+1] = buses.get(k).voltage;
		}
		return vals;
	}
	
	// ###########################################################
	// Prints the Vector of State Variables (angles and voltages)
	public void printValues(){
		for(int k=0; k<buses.size(); k++){
			System.out.print(buses.get(k).angle  +"\t");
			System.out.print(buses.get(k).voltage);
			if(k!=(buses.size()-1)){
				System.out.print("\t");
			}
		}
		System.out.print("\n");
	}
	
	public String stringValues(){
		String s = "";
		for(int k=0; k<buses.size(); k++){
			s += Double.toString(buses.get(k).angle)+",";
			s += Double.toString(buses.get(k).voltage);
			if(k!=(buses.size()-1)){
				System.out.print("\t");
				s += ",";
			}
		}
		s += "\n";
		return s;
	}
	
}
