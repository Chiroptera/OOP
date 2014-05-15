package bayesClassification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dataset.*;

public class NaiveBayesClassification {
	
	private boolean verbose = false;
	
	private String scoreType;
	

	protected double Nl=0.5;
	
	
	protected Graph mygrah;
	protected Map<List<Integer>,Double> varParameters;
	protected double[] classParameters;
	
	/**
	 * Score can either be log-likelihood (LL) or minimum description length (MDL).
	 * @throws NBCException.
	 * @param String score
	 * @throws NBCException 
	 * */
	public NaiveBayesClassification(String score) throws NBCException{

			checkScore(score);
		
	}
	/**
	 * 
	 * @param Nl
	 */
	
	public void setNl(double Nl){
		this.Nl=Nl;
	}

	/**
	 * Check if the input score is valid.
	 * @param String args
	 * @throws NBCException Exception of score not valid.	
	 */
	public void checkScore(String args) throws NBCException{
		
		if(args.equals("LL") || args.equals("MDL")){
			scoreType = args;
		}else{
			 throw new NBCException(args);
		}	
	}
	
	/**
	 *  This 
	 * @param traindata A traindata with data.
	 */
	public void Train(TrainDataSet traindata){
		/*
		 * 1. Compute edge weight
		 * 2. build undirected graph
		 * 3. make graph directed
		 */
		
		if (verbose){
			System.out.println("Data received is:");
			traindata.printData();
		}
		
		System.out.println("Building tables...");		
		traindata.buildTable();
		
		if(verbose){
			
			traindata.printNijkc();
			traindata.printNijc();
			traindata.printNikc();
		
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

	/**
	 * 
	 * @param traindata
	 */
	protected void computeParameters(TrainDataSet traindata){
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
							
							if(verbose) System.out.println("VarParam" +
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
		/* iterate over every value of class */
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
	/**
	 * This method should only be called after calling the method <b>train()</b>.
	 * This method receives a DataSet containing the test data and, using the <b>Graph</b> built during training, 
	 * the computes the most probable classifier for each test instance.
	 * @param test A DataSet containing the test data.
	 */
	public void Test(DataSet test){
		
		double paramsSumOverClass;
		
		/* array for the parameters of the class values */
		double[] joints = new double[mygrah.getClassVariable().GetSR()];

		double maxProb;
		int chosenC=0;
		int intNb=0;
		
		int[] dataLine;
		
		
		/* iterate over each test line */
		for(Iterator<int[]> iterLine = test.iterator(); iterLine.hasNext();){
			dataLine = iterLine.next();
			
			/* reset sum */
			paramsSumOverClass=0;
			
			if(verbose) System.out.println("dataline = " + Arrays.toString(dataLine));
			
			/* iterate over each possible value for the class variable and compute the joint probability of instance for that class */
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
			
			/* if test is TestDataSet, then we can tell it its assignment*/
			if (test instanceof TestDataSet){
				((TestDataSet) test).classifyInstance(intNb++, chosenC);
			}
			
			if(verbose) System.out.println("instance " + Arrays.toString(dataLine) + "\t->\t" + 
											String.valueOf(chosenC) + "\t\t" + 
											String.valueOf(joints[chosenC]));
			
		
		}
		
		
		
	}
	

	
	
	/**
	 * Uses the information of the TAN's <b>Graph</b> containing the tree variables.
	 * @param varValues Array of integers equivalent to a line of test data,
	 * @param c Valid value for a class.
	 * @return Returns a double containing the jointProbability
	 */
	protected double jointProbabiliy(int[] varValues,int c){
		
		/* create product and assign the parameter of root node */
		/* variabl ID, variable value, class value */
		double varParamsProduct=1;
		
		
		for(int i = 1; i < varValues.length; i++){
			/* variabl ID, variable value, parent value, class value */
			try {
				varParamsProduct *= varParameters.get(Arrays.asList(i,varValues[i],varValues[mygrah.getParentID(i)],c));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				varParamsProduct *= varParameters.get(Arrays.asList(i,varValues[i],c));
			}
		}
		
		return varParamsProduct * classParameters[c];
		
		
	}
	
	public String getScoreType(){
		
		return scoreType;
	}
	
	
	
}
