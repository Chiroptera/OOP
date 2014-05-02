package dataset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Graph {

	LinkedList<VariableNode> varList;
	ClassifierNode classNode;
	
	Hashtable<List<Integer>,Integer> NijkcTable;
	Hashtable<List<Integer>,Integer> Nijc_KTable;
	int[][] edgeMatrix;
	
	ArrayList<List<Integer>> spanningTree;
	
	int numberOfVars;
	
	Graph(LinkedList<VariableNode> varList_arg,ClassifierNode classNode_arg,
			int[][] edgeMatrix_arg,
			Hashtable<List<Integer>,Integer> NijkcTable_arg,Hashtable<List<Integer>,Integer> Nijc_KTable_arg){
		varList = varList_arg;
		classNode = classNode_arg;
		NijkcTable = NijkcTable_arg;
		Nijc_KTable = Nijc_KTable_arg;
		edgeMatrix = edgeMatrix_arg;
		numberOfVars = varList_arg.size();
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
		
			(a) If “Island 1” and “Island 2” belong to the same component move on to the next
			edge.
			
			(b) Otherwise add the edge to the maximum weight spanning tree. Let ic1	be the
			component of “Island 1” and	ic2	the component of “Island 2”. For every island
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
