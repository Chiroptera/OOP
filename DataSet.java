package dataset;

import java.io.BufferedReader; //reads text from a character-input stream
import java.io.FileNotFoundException; //signal that an attempt to open the file has failed
import java.io.FileReader; //convenience class for read char files
import java.io.IOException; //signals that an I/O exception of some sort has occurred

public class DataSet {
	
	public void run(String Filename) {
		  
	String csvFile = "/Users/antoniofitas/Documents/JAVA_projects/NaiveBayesClassifier/";
	BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
	int i = 0, sSize, j=0;
	
	csvFile += Filename + ".csv";
	 
	try {
		br = new BufferedReader(new FileReader(csvFile));
		while ((line = br.readLine()) != null) {
 
		    // use comma as separator
			String[] lineparse = line.split(cvsSplitBy);
			sSize = lineparse.length;
			
			BayesNode[] NodeList = new BayesNode[sSize];

 
		if(i == 0){
			
			System.out.print("Variable: ");
			
			while(j < sSize){
				
				//last element array represents C node
				if(j == sSize-1){
					NodeList[j] = new ClassifierNode(lineparse[j]);
				}
				
				//create nodes Xi in array
				else {
					
					NodeList[j] = new VariableNode(lineparse[j], j);
					
				}
				System.out.print("|" + lineparse[j] + " ");
				j++;
			}
		}
		else {
			System.out.print("Values: ");
			while(j < sSize){
				
				if(j == sSize-1){
					NodeList[j].UpdateSR(Integer.parseInt(lineparse[j]));
				}else{
					NodeList[j].UpdateSR(Integer.parseInt(lineparse[j]));

				}
				System.out.print("|" + lineparse[j] + " ");
				j++;
				
			}
		}
		
		System.out.println();
		j = 0;
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

	
  public static void main(String[] args) {
	
	DataSet obj = new DataSet();
	obj.run(args[0]);
	
	
	
  }
  
}
