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

	LinkedList<int[]> parsedData = new LinkedList<int[]>();
	Map<List<Integer>,Integer> NijkcTable = new HashMap<List<Integer>,Integer>();
	Map<List<Integer>,Integer> Nikc_JTable = new HashMap<List<Integer>,Integer>();
	Map<List<Integer>,Integer> Nijc_KTable = new HashMap<List<Integer>,Integer>();

	Map<List<Integer>, Float> edgeWeight = new HashMap<List<Integer>, Float>();

	VariableNode[] NodeList;
	ClassifierNode ClassNode;
	
	private int NT;
	int NX;

	DataSet(){
		
		
		
		System.err.println("Hello Dataset!");
		
		
		
	}
	
	public int GetNT() {
		
		return NT;
	}
	
	public int GetNX() {
		
		return NX;
	}

	public VariableNode[] getVaribleList(){
		/* if list hasn't been assigned yet, return null*/
		if(NodeList == null) return null;
		VariableNode[] outList = new VariableNode[NodeList.length-1];
		for(int i=0;i<NodeList.length-1;i++){
			outList[i]=(VariableNode)NodeList[i];
		}
		return outList;
	}
	
	public void parse(String Filename) {
		//System.err.println("parse:");
		String csvFile = (Filename + ".csv");

		
		
		BufferedReader br = null;
		String line;
		String cvsSplitBy = ",";
		int sSize;
		
		try {
			br = new BufferedReader(new FileReader(csvFile));
			
			String[] lineparse;
			int[] temp;

			/* while exists lines*/

			/* if first line not null */
			if((line = br.readLine()) != null){

			    // use comma as separator
				lineparse = line.split(cvsSplitBy); /* split string by commas */
				sSize = lineparse.length; /* number of variables + 1 (class variable)*/
				this.NX = sSize -1;
				
				NodeList = new VariableNode[this.NX+1];

				/* go through all strings in line (seperated by commas)*/
				for(int j=0;j<this.NX;j++){
					
					NodeList[j] = new VariableNode(lineparse[j], j);
					System.out.println("Variable " + j + " name: " + NodeList[j].getName());
				}

				ClassNode = new ClassifierNode(lineparse[sSize-1]);
				
				/* while there are other lines*/
				while ((line = br.readLine()) != null) {

				    // use comma as separator
					lineparse = line.split(cvsSplitBy); /* split string by commas */
					sSize = lineparse.length; /* number of variables + 1 (class variable)*/

					temp = new int[sSize];
					System.out.println("Size: " + sSize );
						for(int j=0 ; j<sSize;j++){
							
							temp[j] = Integer.parseInt(lineparse[j]);
							
							if( j == sSize-1){
								System.out.println("C:"+ Integer.parseInt(lineparse[sSize-1]));
								ClassNode.UpdateSR(Integer.parseInt(lineparse[sSize-1]));
								parsedData.add(temp);
								continue;
							}
								
							NodeList[j].UpdateSR((temp[j]));
							
						}
						this.NT ++;

					}

					System.out.println();
			}	

		} catch (FileNotFoundException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		ClassNode.CreateNC();
  }

	public void buildTable(){
	    /*for(Iterator iter = parsedData.iterator();iter.hasNext();){*/
		List<Integer> key,keyInv;
		Integer value;
		int classe;
				

		/* for each line of parsed data*/
		for(int[] dataLine : parsedData){
			classe = dataLine[this.NX];
			
			System.out.println("classe: " + classe);
			
			ClassNode.UpdateNC(classe);

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

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ElapsedTime timeElapsed = new ElapsedTime();

		System.err.println("Hello hashTable!");
		DataSet obj = new DataSet();
		obj.parse(args[0]);
		obj.buildTable();
		
		Graph graphB = new Graph(obj.NodeList, obj.ClassNode, obj.NT,
				obj.NijkcTable,obj.Nijc_KTable, obj.Nikc_JTable, obj.edgeWeight);
		
		
		graphB.buildmatrix();
		
		
		System.out.println("Building the classifier: " +  timeElapsed.toString());

	
	}  
}

