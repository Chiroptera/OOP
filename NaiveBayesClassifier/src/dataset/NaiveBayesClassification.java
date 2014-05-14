package dataset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaiveBayesClassification {
	
	private boolean verbose = true;
	
	private String scoreType;
	
	//
	private VariableNode[] VariableArray;
	private ClassifierNode ClassNode;
	//
	
	private int nVariable;
	private int nInstances;
	protected double Nl=0.5;
	
	
	protected Graph mygrah;
	protected Map<List<Integer>,Double> varParameters;
	protected double[] classParameters;
	
	
	NaiveBayesClassification(String score){
		
		try {
			checkScore(score);
		} catch (NBCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		
		mygrah = graph;
		
		

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
		
		computeParameters(traindata);
		
		
	
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
				 * for each class
				 * */
				for(int c=0;c<traindata.getClassVariable().GetSR();c++){
					/*
					 * for each possibe configuration (value) w_ij of parent of variable i 
					 * */
					if (i.GetParent() != null){
						for(int j=0;j<i.GetParent().GetSR();j++){
							parameterKey=Arrays.asList(i.getID(),k,j,c);
							occurrIJKC=traindata.getNijkc(i.getID(), i.GetParent().getID(), k, j, c); 
							occurrIJC=traindata.getNijc(i.getID(), i.GetParent().getID(), j, c);
							parameterValue = (occurrIJKC + Nl) / (occurrIJC + i.GetSR() * Nl);
							varParameters.put(parameterKey, parameterValue);
							
							if(verbose)
							System.out.println("VarParam" +
												parameterKey + 
												"=" + String.valueOf(parameterValue)
												);
						}
					}
					else{
						parameterKey=Arrays.asList(i.getID(),k,c);
						occurrIJKC=traindata.getNikc(i.getID(), k,c); 
						occurrIJC=traindata.getClassVariable().GetNC(c);
						parameterValue = (occurrIJKC + Nl) / (occurrIJC + i.GetSR() * Nl);
						varParameters.put(parameterKey, parameterValue);
						
						if(verbose)
						System.out.println("VarParam" +
								parameterKey + 
								"=" + String.valueOf(parameterValue)
								);
					}
				}
			}
		}
		
		classParameters = new double[traindata.getClassVariable().GetSR()];
		for(int c=0;c<traindata.getClassVariable().GetSR();c++){
			parameterValue= (traindata.getClassVariable().GetNC(c) + Nl) / 
							(traindata.getnInstances() + traindata.getClassVariable().GetSR() * Nl);
			classParameters[c]= parameterValue;
			
			if (verbose)
			System.out.println("ClassParam[" +
					String.valueOf(c) + 
					"]=" + String.valueOf(parameterValue)
					);
		}
		
	}
	
	public void Test(DataSet test){
		
		double paramsSumOverClass;
		double[] joints = new double[mygrah.getClassVariable().GetSR()];

		double maxProb;
		int chosenC=0;
		
		/* iterate over each test line */
		for(int[] dataLine : test.parsedDataList){
			paramsSumOverClass=0;
			
			if(verbose) System.out.println("dataline = " + Arrays.toString(dataLine));
			
			/* iterate over each possibl value for the class variable */
			for(int c=0; c<mygrah.getClassVariable().GetSR();c++){
				joints[c]=jointProbabiliy(dataLine,c);
				paramsSumOverClass += joints[c];
				
				if(verbose) System.out.println("joint[" + String.valueOf(c) + "]=" + String.valueOf(joints[c]));
			}
			
			if(verbose) System.out.println("joints sum = " + String.valueOf(paramsSumOverClass));
			
			maxProb=0;
			
			/* compute and iterate over the array of computed conditional probabilities*/
			for(int c=0;c<joints.length;c++){
				joints[c] /= paramsSumOverClass;
				/* compute maximum & classify*/
				if (joints[c] > maxProb ){
					maxProb = joints[c];
					chosenC = c;
				}
			}
			
			if(verbose) System.out.println("instance " + Arrays.toString(dataLine) + "\t->\t" + 
											String.valueOf(chosenC) + "\t\t" + 
											String.valueOf(joints[chosenC]));
			
		}
		
		
		
	}
	
	
	public ClassifierNode getClassNode(){
		return ClassNode;
	}
	
	
	
	protected double jointProbabiliy(int[] varValues,int c){
		
		/* create product and assign the parameter of root node */
		/* variabl ID, variable value, class value */
		double varParamsProduct=varParameters.get(Arrays.asList(0,varValues[0],c));
		
		
		for(int i = 1; i < varValues.length; i++){
			/* variabl ID, variable value, parent value, class value */
			varParamsProduct *= varParameters.get(Arrays.asList(i,varValues[i],varValues[mygrah.getParentID(i)],c));
		}
		
		return varParamsProduct * classParameters[c];
		
		
	}
	
	public String getScoreType(){
		
		return scoreType;
	}
	
	
	
}
