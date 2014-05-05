package dataset;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;

public class NaiveBayesClassification {
	
	private FileReader train;
	private FileReader test;
	
	
	private LinkedList<int[]> parsedData;
	
	private String scoreType;
	
	//
	private VariableNode[] VariableArray;
	private ClassifierNode ClassNode;
	//
	
	private int nVariable;
	private int nInstances;
	
	NaiveBayesClassification(){
		
	}

	public void parseArg(String[] args) throws FileNotFoundException, NBCException{
		
		if(args[2].equals("LL") || args[2].equals("MDL")){
			scoreType = args[2];
		}else{
			 throw new NBCException(args[2]);
		}
		
		train = new FileReader(args[0] + ".csv");
		test = new FileReader(args[1] + ".csv");
		
	}
	
	public void Train(){
		
		ParseTrainCSV trainCSV = new ParseTrainCSV();
		trainCSV.parse(this.test, this.VariableArray, this.ClassNode);
		trainCSV.getParsedData();
		
	}

	public void Test(){
		
	}
	
	public VariableNode[] getVarList(){
		return VariableArray;
	}
	
	public ClassifierNode getClassNode(){
		return ClassNode;
	}
	
	public FileReader getTrainFile(){
		
		return train;
	}
	
	public FileReader getTestFile(){
		
		return test;
	}
	
	public String getScoreType(){
		
		return scoreType;
	}
}
