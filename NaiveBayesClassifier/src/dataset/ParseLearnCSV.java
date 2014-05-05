package dataset;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

//TO DO: extends this to accept any number or object
//extends the class that puts a csv file with integer entries to 
//treat the NaiveBayesClass variables
public class ParseTrainCSV extends ParseCSV {
	
	
	
	
	ParseTrainCSV(){
		super();
	}
	
	public void parse(FileReader toParse){
		
		BufferedReader br = null;
		String line;
		String cvsSplitBy = ",";
		
		try {
			br = new BufferedReader(toParse);
			
			String[] lineparse;
			int[] temp;

			/* if first line not null */
			if((line = br.readLine()) != null){
				
				lineparse = line.split(cvsSplitBy); /* split string by commas */
				sSize = lineparse.length; /* number of variables + 1 (class variable)*/
				firstLine(lineparse);				
			}
				
				/* while there are other lines*/
				while ((line = br.readLine()) != null) {

				    // use comma as separator
					lineparse = line.split(cvsSplitBy); /* split string by commas */
					temp = new int[sSize];
					for(int j=0 ; j<sSize;j++){
						temp[j] = Integer.parseInt(lineparse[j]);
						middleLine(temp[j], j);
					}
					parsedData.add(temp);

				}
		//TO DO: BETTER TREATMENT TO EXCEPTION
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
	}
	
	//by default the first line is ignored, only used to know column size
	//can be extended to not ignore first line, just by redefining method to do something
	public void firstLine(String[] line, VariableNode[] VariableList, ClassifierNode ClassN, int sSize){
		
		VariableList = new VariableNode[sSize];

		/* go through all strings in line (seperated by commas)*/
		for(int j=0 ; j < sSize-1 ; j++){
			
			VariableList[j] = new VariableNode(line[j], j);
		}
	}
	
	
	
	
	

}
