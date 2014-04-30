package dataset;

import java.io.BufferedReader; //reads text from a character-input stream
import java.io.FileNotFoundException; //signal that an attempt to open the file has failed
import java.io.FileReader; //convenience class for read char files
import java.io.IOException; //signals that an I/O exception of some sort has occurred
import java.util.Hashtable;
import java.util.LinkedList;



public class DataSet {
	
	LinkedList<int[]> parsedData = new LinkedList<int[]>();
	Hashtable<int[],Integer> NijkcTable,Nikc_JTable,Nijc_KTable = new Hashtable<int[],Integer>();
	BayesNode[] NodeList;
	
	DataSet(){
		System.err.println("Hello Dataset!");
	}
	
	public void parse(String Filename) {
		System.err.println("parse:");
		String csvFile = "/home/chiroptera/workspace/OOP/NaiveBayesClassifier/";
		BufferedReader br = null;
		String line;
		String cvsSplitBy = ",";
		int i = 0, sSize;
		
		csvFile += Filename;
		System.err.println("parse: before try");
		 
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
				
				NodeList = new BayesNode[sSize];
				
				System.out.print("Variable: ");
				
				/* go through all strings in line (seperated by commas)*/
				for(int j=0;j<sSize-1;j++){
					System.err.println("iteration j=" + String.valueOf(j));
					NodeList[j] = new VariableNode(lineparse[j], j);
					System.out.print(" | " + NodeList[j].getName());
				}

				NodeList[sSize-1] = new ClassifierNode(lineparse[sSize-1]);
				System.out.print(" | " + lineparse[sSize-1] + " |");
		
				/* while there are other lines*/
				while ((line = br.readLine()) != null) {
					
				    // use comma as separator
					lineparse = line.split(cvsSplitBy); /* split string by commas */
					sSize = lineparse.length; /* number of variables + 1 (class variable)*/
					
	//				else {
						System.out.println("Values: ");
						System.out.println("line[0]: " + line.toString());
						temp = new int[sSize];
	
	
						for(int j=0;j<sSize;j++){
	
							temp[j] = Integer.parseInt(lineparse[j]);
	//						System.err.println("iteration j=" + String.valueOf(j) 
	//								+ "\tlineparse.size = " + String.valueOf(lineparse.length)
	//								+ "\tlineparse[j]=" + lineparse[j]
	//								+ "\tlineparse[j] int =" + Integer.parseInt(lineparse[j])
	//								+ "\ttemp.size = " + String.valueOf(temp.length)
	//								+ "\ttemp[j] = " + String.valueOf(temp[j]));
							
							System.err.println("iteration j=" + String.valueOf(j) 
							+ "\tlineparse.length = " + String.valueOf(lineparse.length)
							+ "\tlineparse[j]=" + lineparse[j]
							+ "\tlineparse[j] int =" + Integer.parseInt(lineparse[j])
							+ "\tnodelist.length = " + String.valueOf(NodeList.length)
							+ "\tnodelist[j].id = " + NodeList[j].getName());
							
							NodeList[j].UpdateSR(Integer.parseInt(lineparse[j]));
	
							System.out.print("|" + lineparse[j] + " ");
						}
						parsedData.add(temp);
					}
		
					System.out.println();
					i++;
			}	
//			}
		 
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
		 
		System.out.println("Done");
  }

	public void buildTable(){
	    /*for(Iterator iter = parsedData.iterator();iter.hasNext();){*/
		int[] key,keyInv;
		Integer value;
		int classe;
		
		/* for each line of parsed data*/
		for(int[] dataLine : parsedData){
			classe=dataLine[dataLine.length-1];
			for(int i=0;i<dataLine.length-1;i++){
				
				/* 
				 * 
				 *  CHECKING FOR Nikc_J
				 * 
				 * */
				/* key = [var id, var value, class value] */
				key=new int[]{i,dataLine[i],classe};
				
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
				for(int il=0;il!=i && il<dataLine.length-1;il++){
					
					/* 
					 * 
					 *  CHECKING FOR Nijkc
					 * 
					 * */
					/* key = [var id, parent id, var value, parent value, class value] */
					key=new int[]{i,il,dataLine[i],dataLine[il],classe};
					keyInv=new int[]{il,i,dataLine[il],dataLine[i],classe};
					
					
					/* if key already exists, increment*/
					if(NijkcTable.containsKey(key)){
						value=NijkcTable.get(key).intValue()+1; /* increment Nijkc */
						NijkcTable.put(key,value); /* add entry in table*/
						NijkcTable.put(keyInv,value);
					}
					/* otherwise add entries to table with value 1*/
					else{
						NijkcTable.put(key, 1);
						NijkcTable.put(keyInv, 1);
					}
					
					/* 
					 * 
					 *  CHECKING FOR Nijc_K
					 * 
					 * */
					key=new int[]{i,il,dataLine[il],classe};
					keyInv=new int[]{il,i,dataLine[i],classe};
					
					
					/* if key already exists, increment*/
					if(Nijc_KTable.containsKey(key)){
						value=Nijc_KTable.get(key).intValue()+1; /* increment Nijkc */
						Nijc_KTable.put(key,value); /* add entry in table*/
						Nijc_KTable.put(keyInv,value);
					}
					/* otherwise add entries to table with value 1*/
					else{
						Nijc_KTable.put(key, 1);
						Nijc_KTable.put(keyInv, 1);
					}					

				}
				/* end cycle il*/
			}
			/* end cycle i*/
	    }
		/* end cycle dataLine*/
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		System.err.println("Hello hashTable!");
		DataSet obj = new DataSet();
		obj.parse(args[0]);
		obj.buildTable();
		System.err.println("\nNijkc:\n\n"+obj.NijkcTable.toString());
		System.err.println("\nNikc_J:\n\n"+obj.Nikc_JTable.toString());
		System.err.println("\nNijc_K:\n\n"+obj.Nijc_KTable.toString());

	
	}
  
}
