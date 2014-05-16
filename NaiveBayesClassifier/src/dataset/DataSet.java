package dataset;

import java.util.Iterator;
import java.util.LinkedList;
import bayesClassification.VariableNode;

public class DataSet implements Iterable<int[]>{
	
	protected LinkedList<int[]> parsedDataList = new LinkedList<int[]>();

	protected VariableNode[] variableArray; /* variable list */
	
	protected int nInstances;
	protected int nVariables;

	public DataSet (){
		
	}
	
	/**
	 * Adds an instance of data (an array of ints witht length of number of variable plus 1 for the class node).
	 * @param dataline int[]
	 */
	public void addDataLine(int[] dataline){
		parsedDataList.add(dataline);
	}
	
	/**
	 * Print all the instances.
	 */
	public void printData(){
		for(int[] line:parsedDataList){
			for(int val:line){
				System.out.print(String.valueOf(val)+",");
			}
			System.out.println();
			
		}
	}
	
	/**
	 * Receives the data all at once and sets the number of instances.
	 * @param parsedDataList LinkedList<int[]>
	 */
	public void setData(LinkedList<int[]> parsedDataList){
		this.parsedDataList=parsedDataList;
		this.nInstances = parsedDataList.size();
	}
	

	public void createVarArray(int size){
		
//check if not null
		this.variableArray = new VariableNode[size];
	}
	
	/**
	 * Adds a VariableNode to the variable array.
	 * @param index ID int of the variable.
	 * @param name String with the name of the variable.
	 */
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
	
	/**
	 * Returns the number of instances.
	 * @return int
	 */
	public int getnInstances() {
		
		return nInstances;
	}
	
	/**
	 * Returns the number of variables.
	 * @return int
	 */
	public int getnVariables() {
		
		return nVariables;
	}

	/**
	 * Returns the VariableNode array if it exists, null otherwise.
	 * @return VariableNode[]
	 */
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

