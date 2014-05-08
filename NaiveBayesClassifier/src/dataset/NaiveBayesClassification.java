package dataset;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

public class NaiveBayesClassification {
	
	private String scoreType;
	
	//
	private VariableNode[] VariableArray;
	private ClassifierNode ClassNode;
	//
	
	private int nVariable;
	private int nInstances;
	
	NaiveBayesClassification(String score){
		
		try {
			checkScore(score);
		} catch (NBCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	NaiveBayesClassification(DataSet traindata){
		
	}

	public void checkScore(String args) throws NBCException{
		
		if(args.equals("LL") || args.equals("MDL")){
			scoreType = args;
		}else{
			 throw new NBCException(args);
		}
		

	}
	
	public void Train(DataSet traindata){
		/*
		 * 1. Compute edge weight
		 * 2. build undirected graph
		 * 3. make graph directed
		 */
		
		traindata.buildTable();
		
		Graph graph = new Graph(traindata);
		
		graph.weightEdges(traindata);
		
		graph.Kruskal(graph.edgeWeight);
		
		graph.makeTreeDirected();
		
		
		
		System.err.println("Edges in tree");
		for (List<Integer> edge : graph.spanningTree){
			System.err.println(edge);
		}
		
		System.err.println("hello");
		for(VariableNode var : graph.varList){
			System.err.println("Variable " + var.getName() + " has parent ");
			if (var.parent!= null){
				System.err.println(var.parent.getName());
				continue;
			}
			System.err.println("null");
		}
		
		
	
	}

	public void Test(){
		
	}
	
	public VariableNode[] getVarList(){
		return VariableArray;
	}
	
	public ClassifierNode getClassNode(){
		return ClassNode;
	}
	
	void computeParameters(){
		
		
		
	}
	
	
	public String getScoreType(){
		
		return scoreType;
	}
	
	
	
}
