package dataset;

import java.util.Iterator;
import java.util.LinkedList;
import bayesClassification.VariableNode;

public class DataSet implements Iterable<int[]>{
	
	protected LinkedList<int[]> parsedDataList = new LinkedList<int[]>();

	protected VariableNode[] variableArray; /* variable list */
	
	protected int nInstances;
	protected int nVariables;

	DataSet (){
		
	}
	
	public void addDataLine(int[] dataline){
		parsedDataList.add(dataline);
	}
	
	public void printData(){
		for(int[] line:parsedDataList){
			for(int val:line){
				System.out.print(String.valueOf(val)+",");
			}
			System.out.println();
			
		}
	}
	
	public void setData(LinkedList<int[]> parsedDataList){
		this.parsedDataList=parsedDataList;
		this.nInstances = parsedDataList.size();
	}
	
	public void createVarArray(int size){
		
//check if not null
		this.variableArray = new VariableNode[size];
	}
	
	public void addVarNode(int index, String name){
		
//check if not null, if not then do what? exception, ignore?
		this.variableArray[index] = new VariableNode(name,index);
		this.nVariables++;
	}
	
	public void setnInstances(int numI){
		
		this.nInstances = numI;
	}
	
	public void setnVariables(int numV){
		
		this.nVariables = numV;
	}
		
	public int getnInstances() {
		
		return nInstances;
	}
	
	public int getnVariables() {
		
		return nVariables;
	}

	public VariableNode[] getVariableArray(){
		/* if list hasn't been assigned yet, return null*/
		if(variableArray == null) return null;
//		VariableNode[] outList = new VariableNode[variableArray.length-1];
//		for(int i=0;i<variableArray.length-1;i++){
//			outList[i]=(VariableNode)variableArray[i];
//		}
		return variableArray;
	}
	
	@Override
	public Iterator<int[]> iterator() {
		// TODO Auto-generated method stub

		return parsedDataList.iterator();
	}
	
	
}

