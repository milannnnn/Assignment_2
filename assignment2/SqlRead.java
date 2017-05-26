package assignment2;

public class SqlRead {
	
	public static void main(String[] args){
		//SQLprinter newPrinter = new SQLprinter("root","Callandor14");
		SQLprinter newPrinter = new SQLprinter("root","root");
		String[] colName = {"name","time","value","sub_rdfid"};
		String[][] resData = newPrinter.readTable("measurements", colName);
		newPrinter.printRes(resData, colName);
		newPrinter.exit();
	}

}
