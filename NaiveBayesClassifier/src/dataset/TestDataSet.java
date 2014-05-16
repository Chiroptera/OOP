package dataset;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



public class TestDataSet extends DataSet {

	protected Map<List<Integer>,Integer> instanceClassification = new HashMap<List<Integer>,Integer>();
	int[] classes=null;
	
	public TestDataSet() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void classifyInstance(int instanceNb, int c){
		if (classes==null) classes  = new int[super.nInstances];
 		classes[instanceNb]=c;
	}
	
	public int getInstanceClass(int instID){
		return classes[instID+1];
	}
	
	public int[] getInstanceClassArray(){
		return classes;
	}
	
//	public void printInstancesWithClass(){
//		int intNb=1;
//		Iterator<Map.Entry<List<Integer>,Integer>> entries = instanceClassification.entrySet().iterator();
//		Map.Entry<List<Integer>,Integer> entry;
//		while(entries.hasNext()){
//			entry=entries.next();
//			System.out.println("-> instance " + intNb++ + ":\t\t" + entry.getValue());
//		}
//	}
	
	public void printInstancesWithClass(){
		for(int i=0;i<super.nInstances;i++){
			System.out.println("-> instance " + (i+1) + ":\t\t" + classes[i]);
		}
//		for(int[] instance : super.parsedDataList){
//			System.out.println("-> instance " + intNb + ":\t\t" + classes[intNb-1]);
//			intNb++;
//		}
		
	}
	
	

}

