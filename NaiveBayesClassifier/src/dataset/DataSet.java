package dataset;

import java.io.BufferedReader; //reads text from a character-input stream
import java.io.FileNotFoundException; //signal that an attempt to open the file has failed
import java.io.FileReader; //convenience class for read char files
import java.io.IOException; //signals that an I/O exception of some sort has occurred
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataSet {
	
	protected LinkedList<int[]> parsedDataList;
	protected Map<List<Integer>,Integer> NijkcTable = new HashMap<List<Integer>,Integer>();
	protected Map<List<Integer>,Integer> Nikc_JTable = new HashMap<List<Integer>,Integer>();
	protected Map<List<Integer>,Integer> Nijc_KTable = new HashMap<List<Integer>,Integer>();

//	Map<List<Integer>, Float> edgeWeight = new HashMap<List<Integer>, Float>();

	private VariableNode[] variableArray; /* variable list */
	private ClassifierNode classNode; /* class node */
	
	private int nInstances;
	private int nVariables;

	DataSet (){
		
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
	
	public void createClassNode(String name){
		
		//check if not null, if not then do what? exception, ignore?
		this.classNode = new ClassifierNode(name);
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
	
	public ClassifierNode getClassVariable(){
		return this.classNode;
	}

	public void buildTable(){
	    /*for(Iterator iter = parsedDataList.iterator();iter.hasNext();){*/
		List<Integer> key;
		Integer value;
		int classe;
		classNode.CreateNC();
				

		/* for each line of parsed data*/
		for(int[] dataLine : parsedDataList){
			classe = dataLine[dataLine.length-1];
			
			System.out.println("classe: " + classe);
			
			classNode.UpdateNC(classe);

			/* for each variable excluding the class */
			for(int i=0;i<dataLine.length-1;i++){

				 // CHECKING FOR Nikc_j

				/* key = [var id, var value, class value] */
				key=Arrays.asList(i,dataLine[i],classe);

				/* if key already exists, increment*/
				if(Nikc_JTable.containsKey(key)){
					value=Nikc_JTable.get(key).intValue() + 1; /* increment Nikc_J */
					Nikc_JTable.put(key,value);
				}
				/* otherwise add entry to table with value 1*/
				else{
					Nikc_JTable.put(key, 1);
				}
				



				/* go through all permutations of variables and their parents (a var i can't be its own parent)
				 * and their corresponding values */
				for(int il=0; /*il!=i &&*/ il<dataLine.length-1;il++){
					
					if(il == i) continue;
					
					 // CHECKING FOR Nijkc

					/* key = [var id, parent id, var value, parent value, class value] */
					key=Arrays.asList(i,il,dataLine[i],dataLine[il],classe);


					/* if key already exists, increment*/
					if(NijkcTable.containsKey(key)){
						value=NijkcTable.get(key).intValue()+1; /* increment Nijkc */
						NijkcTable.put(key,value); /* add entry in table*/
					}
					/* otherwise add entries to table with value 1*/
					else{

						NijkcTable.put(key, 1);
					}

					/* 
					 * 
					 *  CHECKING FOR Nijc_K
					 * 
					 * */
					key=Arrays.asList(i,il,dataLine[il],classe);
					//keyInv=Arrays.asList(il,i,dataLine[i],classe);


					/* if key already exists, increment*/
					if(Nijc_KTable.containsKey(key)){
						
						value = Nijc_KTable.get(key).intValue() + 1; /* increment Nijkc */
						Nijc_KTable.put(key,value); /* add entry in table*/
						
					}
					/* otherwise add entries to table with value 1*/
					else{
						Nijc_KTable.put(key, 1);
					}	
					
				} /* end cycle il*/
			} /* end cycle i*/
	    } /* end cycle dataLine*/		
	}

	public int getNijkc(int i, int Pi, int k, int j, int c){
		/*
		 * i	- id of variable
		 * Pi	- id of parent of variable i
		 * k	- value of i
		 * j	- value of j
		 * c	- value of class variable 
		 */
		
		Integer returnValue= NijkcTable.get(Arrays.asList(i,Pi,k,j,c));
		
		return (returnValue == null) ? 0 : returnValue.intValue();
	}
	
	public int getNikc(int i, int k, int c){
		/*
		 * i	- id of variable
		 * k	- value of i
		 * c	- value of class variable 
		 */
		Integer returnValue= Nikc_JTable.get(Arrays.asList(i,k,c));
		
		return (returnValue == null) ? 0 : returnValue.intValue();
	}
	
	public int getNijc(int i, int Pi,int j, int c){
		/*
		 * i	- id of variable
		 * Pi	- id of parent of variable i
		 * j	- value of j
		 * c	- value of class variable 
		 */
		Integer returnValue= Nijc_KTable.get(Arrays.asList(i,Pi,j,c));
	
		return (returnValue == null) ? 0 : returnValue.intValue();
	}
	
	public boolean containIKJC(int i, int Pi, int k, int j, int c){
		return NijkcTable.containsKey(Arrays.asList(i,Pi,k,j,c));
	}
	
	public boolean containIKC(int i,int k, int c){
		return Nikc_JTable.containsKey(Arrays.asList(i,k,c));
	}
	
	public boolean containIJC(int i, int Pi, int j, int c){
		return Nijc_KTable.containsKey(Arrays.asList(i,Pi,j,c));
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ElapsedTime timeElapsed = new ElapsedTime();

		System.err.println("Hello hashTable!");
		DataSet obj = new DataSet();
//		obj.parse(args[0]);
//		obj.buildTable();
		
//		Graph graphB = new Graph(obj.variableArray, obj.ClassNode, obj.nInstances,
//				obj.NijkcTable,obj.Nijc_KTable, obj.Nikc_JTable, obj.edgeWeight);
		
		
//		graphB.buildmatrix(obj);
		
		
		System.out.println("Building the classifier: " +  timeElapsed.toString());

	
	}
	
	
}

