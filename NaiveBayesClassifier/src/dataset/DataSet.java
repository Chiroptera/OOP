package dataset;

import java.io.BufferedReader; //reads text from a character-input stream
import java.io.FileNotFoundException; //signal that an attempt to open the file has failed
import java.io.FileReader; //convenience class for read char files
import java.io.IOException; //signals that an I/O exception of some sort has occurred
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;



public class DataSet {
	
	LinkedList<int[]> parsedData = new LinkedList<int[]>();
	Hashtable<int[],int> NijkcTable,Nikc_JTable,Nijc_KTable = new Hashtable<int[],int>();
	BayesNode[] NodeList;
	
	public void parse(String Filename) {
		  
		String csvFile = "/Users/antoniofitas/Documents/JAVA_projects/NaiveBayesClassifier/";
		BufferedReader br = null;
		String line;
		String cvsSplitBy = ",";
		int i = 0, sSize, j=0;
		
		csvFile += Filename + ".csv";
		 
		try {
			br = new BufferedReader(new FileReader(csvFile));
			/* while exists lines*/
			while ((line = br.readLine()) != null) {
	 
			    // use comma as separator
				String[] lineparse = line.split(cvsSplitBy); /* split string by commas */
				sSize = lineparse.length; /* number of variables + 1 (class variable)*/
				
				NodeList = new BayesNode[sSize];
				
				int[] temp;
				
				/* first line */
				if(i == 0){
					
					System.out.print("Variable: ");
					
					/* go through all strings in line (seperated by commas)*/
					while(j < sSize-1){
						NodeList[j] = new VariableNode(lineparse[j], j);
						System.out.print("|" + lineparse[j] + " ");
						j++;
					}
					NodeList[j] = new ClassifierNode(lineparse[j]);
					
				}
				/* other lines*/
				else {
					System.out.print("Values: ");
					temp = new int[sSize];
					while(j < sSize){					
						temp[j] = Integer.parseInt(lineparse[j]);
						NodeList[j].UpdateSR(temp[j]);					
						System.out.print("|" + lineparse[j] + " ");
						
						j++;
					}
					parsedData.add(temp);
					j = 0;
				}
	
				System.out.println();
				i++;
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
		 
		System.out.println("Done");
  }

	public void buildTable(){
	    /*for(Iterator iter = parsedData.iterator();iter.hasNext();){*/
		int[] key,keyInv;
		int classe,value;
		for(int[] dataLine : parsedData){
			classe=dataLine[dataLine.length-1];
			for(int i=0;i<dataLine.length-1;i++){
				key=new int[]{i,dataLine[i],classe};
				
				/*if key already exists*/
				if(Nikc_JTable.containsKey(key)){
					Nikc_JTable.get(key)++;
				}
				else{
					Nikc_JTable.put(key, 1);
				}
				
				/* go through all permutations of variables of their values*/
				for(int il=0;il!=i && il<dataLine.length-1;il++){
					key=new int[]{i,il,dataLine[i],dataLine[il],classe};
					keyInv=new int[]{il,i,dataLine[il],dataLine[i],classe};
					
					
					/*if key already exists*/
					if(NijkcTable.containsKey(key)){
						NijkcTable.get(key)++;
					}
					else{
						NijkcTable.put(key, 1);
						NijkcTable.put(keyInv, 1);
					}

				}
				/* end cycle il*/
			}
			/* end cycle i*/
	    }
		/* end cycle dataLine*/
	}
	
	public static void main(String[] args) {
	
	DataSet obj = new DataSet();
	obj.parse(args[0]);
	
	
	
  }
  
}
