package dataset;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



public class TestDataSet extends DataSet {

	protected Map<List<Integer>,Integer> instanceClassification = new HashMap<List<Integer>,Integer>();
	
	public TestDataSet() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void classifyInstance(List<Integer> instance, int c){
		instanceClassification.put(instance, c);
	}
	
	public void printInstancesWithClass(){
		int intNb=1;
		Iterator<Map.Entry<List<Integer>,Integer>> entries = instanceClassification.entrySet().iterator();
		Map.Entry<List<Integer>,Integer> entry;
		while(entries.hasNext()){
			entry=entries.next();
			System.out.println("-> instance " + intNb++ + ":\t\t" + entry.getValue());
		}
	}
	

}

