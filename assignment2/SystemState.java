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
	
	public SystemState(){}
	
	//### Initialize a New System State
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
	
	//### Check if Measurement Is an Angle
	private boolean isAngle(String name){
		if(name.substring(name.length()-3, name.length()).equals("ANG")){
			return true;
		}
		return false;
	}
	
	//### Sorts Buses in Given Order of Appearance, and Eliminates all Buses Not Included in Sorting List
	public void sortBuses(String[] busOrder){
		ArrayList<SimpleObject> tmpBuses = new ArrayList<SimpleObject>();
		for(int k=0; k<busOrder.length; k++){
			for(int j=0; j<buses.size(); j++){
				if(busOrder[k].equals(buses.get(j).busID)){
					tmpBuses.add(buses.get(j));
					break;
				}
			}
//			buses.clear();
//			buses.addAll(tmpBuses);
		}
		
	}
}
