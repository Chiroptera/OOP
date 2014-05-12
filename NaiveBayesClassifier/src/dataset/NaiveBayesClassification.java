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
	
	NaiveBayesClassification(String score, DataSet traindata){
		try {
			checkScore(score);
		} catch (NBCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.Train(traindata);
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
		
		traindata.printData();
		
		System.out.println("Building tables...");		
		traindata.buildTable();
		
		if(true){
			
			/* print Nijkc*/
			System.out.println("\nNijkc:\nKeys:\t\tValues:\n");
			for (List<Integer> key : traindata.NijkcTable.keySet()){
				for(Integer iKey : key) System.out.print(String.valueOf(iKey) + ",");
				System.out.println("\t\t" + traindata.NijkcTable.get(key));
			}
			/* print Nikc_J*/
			System.out.println("\nNikc_J:\nKeys:\t\tValues:\n");
			for (List<Integer> key : traindata.Nikc_JTable.keySet()){
				for(Integer iKey : key) System.out.print(String.valueOf(iKey) + ",");
				System.out.println("\t\t" + traindata.Nikc_JTable.get(key));
			}
			/* print Nijc_K*/
			System.out.println("\nNijc_K:\nKeys:\t\tValues:\n");
			for (List<Integer> key : traindata.Nijc_KTable.keySet()){
				for(Integer iKey : key) System.out.print(String.valueOf(iKey) + ",");
				System.out.println("\t\t" + traindata.Nijc_KTable.get(key));
			}
		
		}
		
		System.out.println("Creating graph...");	
		Graph graph = new Graph(traindata);
		
		System.out.println("Weighting edges...");
		graph.weightEdges(traindata);
		
		for (List<Integer> key : graph.edgeWeight.keySet()){
			for(Integer iKey : key) System.out.print(String.valueOf(iKey) + ",");
			System.out.println("\t\t" + graph.edgeWeight.get(key));
		}
		
		System.out.println("Kruskal...");
		graph.Kruskal(graph.edgeWeight);
		
		System.out.println("Final treeing...");
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
