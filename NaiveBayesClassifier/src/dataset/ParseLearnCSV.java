package dataset;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

//TO DO: extends this to accept any number or object
//extends the class that puts a csv file with integer entries to 
//treat the NaiveBayesClass variables
public class ParseLearnCSV extends ParseCSV {
	
	
	ParseLearnCSV(){
		super();
	}
	
	ParseLearnCSV(String filename){
		super(filename);		
	}
	

	
	//by default the first line is ignored, only used to know column size
	//can be extended to not ignore first line, just by redefining method to do something
	public void firstLine(String[] line){

		data.createVarArray(colSize-1);

		/* go through all strings in line (seperated by commas)*/
		for(int j=0 ; j < colSize-1 ; j++){
			
			data.addVarNode(j, line[j]);
		}
	
		data.createClassNode(line[colSize-1]);
	}
	
	public void middleLine(int value, int index){
				
			if(index == (colSize-1)){
				
				data.getClassVariable().UpdateSR(value);
			}
			else{
				System.out.println("Variable Name: " + data.getVariableArray()[index].getName());
				data.getVariableArray()[index].UpdateSR(value);
			}

	}
	
	public void lastLine(){
		
		

	}
	
	
//	public VariableNode[] getVariableList(){
//		return this.variableArray;
//	}
//	
//	public ClassifierNode getClassVariable(){
//		return this.classNode;
//	}
//	
	

}
