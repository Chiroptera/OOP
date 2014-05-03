package dataset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Graph {

	BayesNode[] varList;
	ClassifierNode classNode;
	
	Map<List<Integer>,Integer> NijkcTable;
	Map<List<Integer>,Integer> Nijc_KTable;
	Map<List<Integer>,Integer> Nikc_JTable;
	Map<List<Integer>, Float> edgeWeight;
	int numberOfInst;

	int[][] edgeMatrix;
	
	ArrayList<List<Integer>> spanningTree;
	
	int numberOfVars;
	
	Graph(BayesNode[] varList_arg,ClassifierNode classNode_arg,
			int NT_arg,
			Map<List<Integer>,Integer> NijkcTable_arg,Map<List<Integer>,Integer> Nijc_KTable_arg, Map<List<Integer>,Integer> Nikc_JTable_arg, Map<List<Integer>, Float> edgeWeight_arg){
		varList = varList_arg;
		classNode = classNode_arg;
		NijkcTable = NijkcTable_arg;
		Nijc_KTable = Nijc_KTable_arg;
		Nikc_JTable = Nikc_JTable_arg;
		edgeWeight = edgeWeight_arg; 

		numberOfVars = varList_arg.length;
		numberOfInst = NT_arg;
	}
	
	//buildmatrix()
	//method to calculate the edges weight
	public void buildmatrix(){
		
		float score, scoreMDL, scoreLL;
		float occurrIJC, occurrIJKC, occurrIKC; //number of instances of each table
		
		List<Integer> keyIJKC, keyIJC, keyIKC, edgeKey;
		
		for(int i = 0; i < numberOfVars-1; i++){
			for(int ii = 0 ; ii < numberOfVars-1; ii++){
				score = 0;
				for(int j = 0; j < varList[ii].GetSR(); j++){
					for(int k = 0; k < varList[i].GetSR(); k++){
						for(int c = 0; c < classNode.GetSR(); c++){

							//keys to access the hash map
							keyIKC = Arrays.asList(i,k,c);
							keyIJKC = Arrays.asList(i,ii,k,j,c);
							keyIJC = Arrays.asList(i,ii,j,c);
		
							//calculate the respective occurrences 
							if(Nijc_KTable.containsKey(keyIJC)){
								occurrIJC = Nijc_KTable.get(keyIJC).intValue();	
							}
							else{
								continue;
							}
							
							if( NijkcTable.containsKey(keyIJKC) ){
								occurrIJKC = NijkcTable.get(keyIJKC).intValue();
							}
							else{
								continue;
							}
							
							if( Nikc_JTable.containsKey(keyIKC) ){
								occurrIKC = Nikc_JTable.get(keyIKC).intValue();
							}
							else{
								continue;
							}

							score += (occurrIJKC / (numberOfInst)) * 
									( Math.log( (occurrIJKC * classNode.GetNC(c)) / (occurrIKC*occurrIJC )) 
									/ Math.log(2));

						}
					}	
				}

				edgeKey = Arrays.asList(i,ii);
				scoreLL = score;
				
				scoreMDL = (float) (score - (((classNode.GetSR() * (varList[i].GetSR() - 1) * (varList[ii].GetSR() - 1)) / 2) * Math.log(numberOfInst)));
				
				edgeWeight.put(edgeKey,scoreLL);
				
			}
		}
	}
	
	
	
	
	
	
	/* function that converts from Hashtable to an ordered ArrayList*/
    public static ArrayList<Map.Entry<List<Integer>, Integer>> sortValue(Hashtable<?, Integer> t){

        //Transfer as List and sort it
        ArrayList<Entry<List<Integer>, Integer>> l = new ArrayList(t.entrySet());
        Collections.sort(l, new Comparator<Map.Entry<?, Integer>>(){

          public int compare(Map.Entry<?, Integer> o1, Map.Entry<?, Integer> o2) {
             return o2.getValue().compareTo(o1.getValue());
         }});

        return l;
     }
	
	/* build Maximum Weighted Spanning Tree*/
	void buildMWST(ArrayList<Map.Entry<List<Integer>, Integer>> edges){
		/*
		 * From http://www.stats.ox.ac.uk/~konis/Rcourse/exercise1.pdf
		 * 
		Examine the edges (this should be done in a loop):
		
			(a) If Island 1 and Island 2 belong to the same component move on to the next
			edge.
			
			(b) Otherwise add the edge to the maximum weight spanning tree. Let ic1	be the
			component of Island 1 and	ic2	the component of Island 2. For every island
			with component	max(ic1, ic2), set the component to	min(ic1, ic2).
			
			(c) Stop after adding 12 edges to the maximum weight spanning tree.
		*/
		
		/* array to hold var identifier of edge*/
		int[] varsNumber = new int[numberOfVars];
		for (int i=0;i<numberOfVars;i++) {
			varsNumber[i]=i;	
		}
		
		int maxVarNumber,minVarNumber;
		
		/* array that will hold all the final tree's edges (lists)*/
		ArrayList<List<Integer>> listOfEdges = new ArrayList<List<Integer>>(numberOfVars-1);
		int edgeCounter=0;
		
		/* assumes edges is an ordered ArrayList with all the edges */
		for(Map.Entry<List<Integer>, Integer> edge : edges){
			
			/* step (a)*/
			/* if the varNumbers of the variables in this edge are equal we have a cyclic graph*/
			if ( varsNumber[edge.getKey().get(0)] == varsNumber[edge.getKey().get(1)]){
				continue;
			}
			
			/* step (b)*/			
			/* increment how many edges already in the list*/
			edgeCounter = edgeCounter + 1;
			
			/* add edge to spanning tree list*/
			listOfEdges.add(edge.getKey());
			 
			/* compute the maximum and minimum varNumbers from current edge*/
			if (varsNumber[edge.getKey().get(0)] > varsNumber[edge.getKey().get(1)]){
				maxVarNumber = varsNumber[edge.getKey().get(0)];
				minVarNumber = varsNumber[edge.getKey().get(1)];
			}
			else{
				maxVarNumber = varsNumber[edge.getKey().get(1)];
				minVarNumber = varsNumber[edge.getKey().get(0)];
			}
			
			/* change all variable's varNumbers whose varNumbers are maxVarNumber to minVarNumber*/
			for (int i=0;i<numberOfVars;i++){
				if (varsNumber[i] == maxVarNumber) varsNumber[i] = minVarNumber;
			}
			
			/* step (c) */
			/* if all the variables are connected, finish*/
			if (edgeCounter == numberOfVars-1){
				spanningTree = listOfEdges;
				break; 
			}
			
		}
		

		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
