package parseCSV;

import dataset.*;

//TO DO: extends this to accept any number or object
//extends the class that puts a csv file with integer entries to 
//treat the NaiveBayesClass variables
public class ParseLearnCSV extends ParseCSV {
	
	
	ParseLearnCSV(){
		super();
	}
	
	public ParseLearnCSV(String filename){
		super(filename);		
	}
	

	
	//by default the first line is ignored, only used to know column size
	//can be extended to not ignore first line, just by redefining method to do something
	public void firstLine(String[] line, DataSet data){

		data.createVarArray(colSize-1);

		/* go through all strings in line (seperated by commas)*/
		for(int j=0 ; j < colSize-1 ; j++){
			
			data.addVarNode(j, line[j]);
		}
	
		((TrainDataSet) data).createClassNode(line[colSize-1]);
	}
	
	public void middleLine(String[] lineparse, DataSet data) throws Exception{
		/* if a middle line doesn't have the same number of elements throw exception.*/
		if (lineparse.length != colSize) throw new Exception("Line has incorrect size.");
		
		int[] temp = new int[colSize];
		for(int j=0 ; j<colSize;j++){
			temp[j] = Integer.parseInt(lineparse[j]);
			
			if(j == (colSize-1)){
				
				((TrainDataSet) data).getClassVariable().UpdateSR(temp[j]);
			}
			else{
//				System.out.println("Variable Name: " + data.getVariableArray()[j].getName());
				data.getVariableArray()[j].UpdateSR(temp[j]);
			}
		}
		data.addDataLine(temp);

	}

}
