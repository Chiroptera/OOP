package parseCSV;

import dataset.*;
import utils.*;

//TO DO: extends this to accept any number or object
//extends the class that puts a csv file with integer entries to 
//treat the NaiveBayesClass variables

/**
 * Class that parse the .csv file when the file is the one that corresponds to the learn set.
 * With this class are created the variables nodes and class node by reading the first line of the file
 * and with the middle lines the number of different values of each "node" are updated. Each line is
 * saved in a list to further analysis of the number of occurrencies.
 */
public class ParseLearnCSV extends ParseCSV {
	
	
	ParseLearnCSV(){
		super();
	}
	
	public ParseLearnCSV(String filename){
		super(filename);		
	}
	

	
	//by default the first line is ignored, only used to know column size
	//can be extended to not ignore first line, just by redefining method to do something
	
	/**
	 * Handles the first line of the .csv file. The variables and class nodes are created.
	 * @param line Line read.
	 * @param data DataSet that corresponds to the file to learn.
	 */
	public void firstLine(String[] line, DataSet data){

		data.createVarArray(colSize-1);

		/* go through all strings in line (seperated by commas)*/
		for(int j=0 ; j < colSize-1 ; j++){
			
			data.addVarNode(j, line[j]);
		}
	
		((TrainDataSet) data).createClassNode(line[colSize-1]);
	}
	
	/**
	 * Handles the middle lines of the .csv file. In this case, the different values of each variable and class are updated
	 * and the line is saved for further analysis.
	 * @param lineparse Line read parsed into strings, each one corresponding to each entry of that line.
	 * @param data DataSet that corresponds to the file to learn.
	 * @throws e Exception that deals with the number of entries being different from the number of variables.
	 */
	//EXCEPTION
	public void middleLine(String[] lineparse, DataSet data) throws Exception{
		
		if (lineparse.length != colSize) throw new ParseException(rowSize, lineparse.length, colSize);
		
		int[] temp = new int[colSize];
		for(int j=0 ; j<colSize;j++){
			
			try {
				temp[j] = Integer.parseInt(lineparse[j]);
			} catch (NumberFormatException e) {
					throw new ParseIntCSVException(rowSize, j);
			}
			
			if(j == (colSize-1)){
				
				((TrainDataSet) data).getClassVariable().updateSR(temp[j]);
			}
			else{
				data.getVariableArray()[j].updateSR(temp[j]);
			}
		}
		data.addDataLine(temp);

	}

}
