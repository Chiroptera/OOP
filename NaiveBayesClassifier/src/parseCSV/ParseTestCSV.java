package parseCSV;

import utils.ParseException;
import dataset.*;

//TO DO: extends this to accept any number or object
//extends the class that puts a csv file with integer entries to 
//treat the NaiveBayesClass variables


/**
 * Class that parse the .csv file when the file is the one that corresponds to the test set.
 * Each line is saved in a list to further classification of the instance.
 */
public class ParseTestCSV extends ParseCSV {
	
	
	ParseTestCSV(){
		super();
	}
	
	public ParseTestCSV(String filename){
		super(filename);		
	}
	
	/**
	 * Handles the middle lines of the .csv file. In this case, the line is saved for further classification of this instance
	 * @param lineparse Line read parsed into strings, each one corresponding to each entry of that line.
	 * @param data DataSet that corresponds to the file to test.
	 * @throws e Exception that deals with the number of entries being different from the number of variables.
	 */
	
	public void middleLine(String[] lineparse, DataSet data) throws Exception{
		/* if a middle line doesn't have the same number of elements throw exception.*/
		if (lineparse.length != colSize) throw new ParseException(rowSize, lineparse.length, colSize);
		
		int[] temp = new int[colSize];
		for(int j=0 ; j<colSize;j++){
			temp[j] = Integer.parseInt(lineparse[j]);
		}
		
		data.addDataLine(temp);
	}

}
