package assignment2;

public class SqlRead {
	
	public static void main(String[] args){
		SQLprinter newPrinter = new SQLprinter("root","Callandor14");
		String[] colName = {"name","time","value"};
		String[][] resData = newPrinter.readTable("measurements", colName);
		newPrinter.printRes(resData, colName);
		newPrinter.exit();
	}

}
