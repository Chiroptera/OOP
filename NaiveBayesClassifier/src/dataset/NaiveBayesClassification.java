package dataset;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaiveBayesClassification {
	
	private String scoreType;
	
	//
	private VariableNode[] VariableArray;
	private ClassifierNode ClassNode;
	//
	
	private int nVariable;
	private int nInstances;
	protected double Nl=0.5;
	
	Map<List<Integer>,Double> varParameters;
	double[] classParameters;
	
	
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
	
	public void setNl(double Nl){
		this.Nl=Nl;
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
		boolean verbose = false;
		traindata.printData();
		
		System.out.println("Building tables...");		
		traindata.buildTable();
		
		if(verbose){
			
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
		
		if(verbose){
			for (List<Integer> key : graph.edgeWeight.keySet()){
				for(Integer iKey : key) System.out.print(String.valueOf(iKey) + ",");
				System.out.println("\t\t" + graph.edgeWeight.get(key));
			}
		}
		System.out.println("Kruskal...");
		graph.Kruskal(graph.edgeWeight);
		
		if(verbose) {
			System.err.println("Edges in tree");
			for (List<Integer> edge : graph.spanningTree){
				System.err.println(edge);
			}
		}
		
		System.out.println("Final treeing...");
		graph.makeTreeDirected();
		
		

		if(verbose){
			for(VariableNode var : graph.varList){
				System.err.println("Variable " + var.getName() + " has parent ");
				if (var.parent!= null){
					System.err.println(var.parent.getName());
					continue;
				}
				System.err.println("null");
			}
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
	
	protected void computeParameters(DataSet traindata){
		varParameters = new HashMap<List<Integer>,Double>();
		List<Integer> parameterKey;
		int occurrIJKC, occurrIJC;
		double parameterValue;
		
		/*
		 * For each variable i
		 * */
		for(VariableNode i : traindata.getVariableArray()){
			/*
			 * for each possibe value x_ik of variable i 
			 * */
			for(int k=0;k < i.GetSR();k++){
				/*
				 * for each possibe configuration (value) w_ij of parent of variable i 
				 * */
				for(int j=0;j<i.GetParent().GetSR();j++){
					/*
					 * for each class
					 * */
					for(int c=0;c<traindata.getClassVariable().GetSR();c++){
						parameterKey=Arrays.asList(i.getID(),k,j,c);
						occurrIJKC=traindata.getNijkc(i.getID(), i.GetParent().getID(), k, j, c); 
						occurrIJC=traindata.getNijc(i.getID(), i.GetParent().getID(), j, c);
						parameterValue = (occurrIJKC + Nl) / (occurrIJC + i.GetSR() * Nl);
						varParameters.put(parameterKey, parameterValue);
					}
				}
			}
		}
		
		classParameters = new double[traindata.getClassVariable().GetSR()];
		for(int c=0;c<traindata.getClassVariable().GetSR();c++){
			parameterValue= (traindata.getClassVariable().GetNC(c) + Nl) / 
							(traindata.GetnInstances() + traindata.getClassVariable().GetSR() * Nl);
			classParameters[c]= parameterValue;
		}
		
	}
	
	protected void jointProbabiliy(int[] varValues,int c){
		
	}
	
	public String getScoreType(){
		
		return scoreType;
	}
	
	
	
}
