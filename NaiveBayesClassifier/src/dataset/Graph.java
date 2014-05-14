package dataset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Graph {

	VariableNode[] varList;
	ClassifierNode classNode;
	VariableNode root;

	Map<List<Integer>,Integer> NijkcTable;
	Map<List<Integer>,Integer> Nijc_KTable;
	Map<List<Integer>,Integer> Nikc_JTable;
	Map<List<Integer>, Float> edgeWeight = new HashMap<List<Integer>, Float>();
	
	
	ArrayList<List<Integer>> spanningTree;
	
	int numberOfVars;
	int numberOfInst;
	
	Graph(){
		
	}
	
	Graph(DataSet data){
		varList = data.getVariableArray().clone();
		classNode = data.getClassVariable();
		numberOfVars = varList.length;
		numberOfInst = data.getnInstances();
	}
	
	Graph(VariableNode[] varList_arg,ClassifierNode classNode_arg,
			int NT_arg,
			Map<List<Integer>,Integer> NijkcTable_arg,Map<List<Integer>,Integer> Nijc_KTable_arg, Map<List<Integer>,Integer> Nikc_JTable_arg, Map<List<Integer>, Float> edgeWeight_arg){
		varList = varList_arg;
		classNode = classNode_arg;
		NijkcTable = NijkcTable_arg;
		Nijc_KTable = Nijc_KTable_arg;
		Nikc_JTable = Nikc_JTable_arg;
		numberOfVars = varList_arg.length;
		numberOfInst = NT_arg;

	}
	
	//buildmatrix()
	//method to calculate the edges weight
	public void weightEdges(DataSet data){
		
		
		
		float score, scoreMDL, scoreLL;
		float occurrIJC, occurrIJKC, occurrIKC; //number of instances of each table
		
		List<Integer> edgeKey;
		
		System.out.println("number of vars: " + numberOfVars + "Number of inst: " + this.numberOfInst);
		
		for(int i = 0;  i < numberOfVars; i++){
			System.out.println("Variable " + i +  " has " + varList[i].GetSR() + " values.");
		}

		
		for(int i = 0; i < numberOfVars; i++){
			for(int ii = i+1 ; ii < numberOfVars; ii++){
				score = 0;
				for(int j = 0; j < varList[ii].GetSR(); j++){
					for(int k = 0; k < varList[i].GetSR(); k++){
						for(int c = 0; c < classNode.GetSR(); c++){

							//calculate the respective occurrences 
							if(data.containIJC(i, ii, j, c)){
								occurrIJC = data.getNijc(i, ii, j, c);
							}
							else{
								continue;
							}
							
							if(data.containIKJC(i, ii, k, j, c)){
								occurrIJKC = data.getNijkc(i, ii, k, j, c);							}
							else{
								continue;
							}
							
							if(data.containIKC(i, k, c)){
								occurrIKC = data.getNikc(i, k, c);
							}
							else{
								continue;
							}

							score += (occurrIJKC / (this.numberOfInst)) * 
									( Math.log( (occurrIJKC * classNode.GetNC(c)) / (occurrIKC * occurrIJC )) 
									/ Math.log(2));

						}
					}	
				}

				edgeKey = Arrays.asList(i,ii);
				scoreLL = score;
				
				scoreMDL = (float) (score - (((classNode.GetSR() * (varList[i].GetSR() - 1) * (varList[ii].GetSR() - 1)) / 2) * Math.log(numberOfInst)));
				
				edgeWeight.put(edgeKey,scoreLL);
				
				System.out.println("edgekey: " + edgeKey + "score :" + score);
				
			}
		}
	}
	
	
	
	
	
	
	/* function that converts from Hashtable to an ordered ArrayList*/
    public static ArrayList<Map.Entry<List<Integer>, Float>> sortValue(Map<List<Integer>, Float> edgeWeight){

        //Transfer as List and sort it
        ArrayList<Entry<List<Integer>, Float>> l = new ArrayList(edgeWeight.entrySet());
        Collections.sort(l, new Comparator<Map.Entry<?, Float>>(){

          public int compare(Map.Entry<?, Float> o1, Map.Entry<?, Float> o2) {
             return o2.getValue().compareTo(o1.getValue());
         }});

        return l;
     }
	
	/* build Maximum Weighted Spanning Tree*/
	void Kruskal(Map<List<Integer>, Float> edgeWeight){
		/* 
		 * Algorithm Steps from http://www.stats.ox.ac.uk/~konis/Rcourse/exercise1.pdf
		 * 
		Examine the edges (this should be done in a loop):
		
			(a) If Island 1 and Island 2 belong to the same component move on to the next
			edge.
			
			(b) Otherwise add the edge to the maximum weight spanning tree. Let ic1	be the
			component of Island 1 and	ic2	the component of Island 2. For every island
			with component	max(ic1, ic2), set the component to	min(ic1, ic2).
			
			(c) Stop after adding 12 edges to the maximum weight spanning tree.
		*/
		
		/* order edges by descending weight */
		ArrayList<Entry<List<Integer>, Float>> orderedEdges = sortValue(edgeWeight);
		
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
		for(Entry<List<Integer>, Float> edge : orderedEdges){
			
			/* 
			 * 
			 * step (a)
			 * 
			 * */
			/* if the varNumbers of the variables in this edge are equal we have a cyclic graph*/
			if ( varsNumber[edge.getKey().get(0)] == varsNumber[edge.getKey().get(1)]){
				continue;
			}
			
			/* 
			 * 
			 * step (b)
			 * 
			 * */			
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
			
			/* 
			 * 
			 * step (c) 
			 * 
			 * */
			/* if all the variables are connected, finish*/
			if (edgeCounter == numberOfVars-1){
				spanningTree = listOfEdges;
				break; 
			}
			
		}
		

		
	}
	
	void makeTreeDirected(){
		
		/* pick some node for root*/
		root = varList[0];
		
		int id1,id2;
		List<Integer> edge;
		
		/* make (shallow ?) copy of spanning tree*/
		ArrayList<List<Integer>> treeCopy = new ArrayList<List<Integer>>(spanningTree);
		
		ArrayList<VariableNode> oldChilds = new ArrayList<VariableNode>(5),
								newChilds = new ArrayList<VariableNode>(5), 
								temp;
		oldChilds.add(root);
		System.err.println(newChilds.isEmpty());
		
		while(!treeCopy.isEmpty()){
			/* cycle through all the edges */
			for(Iterator<List<Integer>> i = treeCopy.iterator(); i.hasNext();){
				edge = i.next();
				
				/* cycle through all the child variables from th previous level */
				for(VariableNode var : oldChilds){
					
					/* if edge contains variable var*/
					if (edge.contains(var.getID())){
						
						/* get id of variables in edge */
						id1=edge.get(0);
						id2=edge.get(1);
						
						/* if var is in position 1 of edge, make it the parent of variable in position 2
						* and add the variable in position 1 as one of the new childs to check */
						if(id1 == var.getID()){
							varList[id2].setParent(var);
//							var.addChild(varList[id2]);
							newChilds.add(varList[id2]);
						}
						else{
							/* if var is in position 2 of edge, make it the parent of variable in position 1
							 * and add the variable in position 1 as one of the new childs to check*/
							varList[id1].setParent(var);
//							var.addChild(varList[id1]);
							newChilds.add(varList[id1]);
						}
						
						/* remove edge so it doesn't get checked again*/
						i.remove();

					}
				} /* end for of variables */
			} /* end for of edges */
			
			/* variables to be checked in next iteration are the ones in newChilds
			 * and have to be copied to oldChilds */
			temp=oldChilds;
			oldChilds=newChilds;
			
			/* clear newChilds for next iteration  */
			newChilds=temp;
			newChilds.clear();
			
		}/* end while */
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.err.println("Hello hashTable!");

		ParseLearnCSV trainSet = new ParseLearnCSV(args[0]);
		
		DataSet traindata = new DataSet();
		
		trainSet.parse(traindata);
		
		
//		for(int i = 0;  i < traindata.ClassNode.GetSR(); i++){
//			System.out.println("Class " + i +  " has " + traindata.ClassNode.GetNC(i) + " instances.");
//		}
//		System.out.println("Number of instances: " + traindata.GetNT());
//		
//		
//		for(int i = 0;  i < traindata.NX; i++){
//			System.out.println("Variable OUTSIDE" + i +  " has " + traindata.NodeList[i].GetSR() + " instances.");
//		}


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

				
//		for (List<Integer> key : traindata.edgeWeight.keySet()){
//			for(Integer iKey : key) System.out.print(String.valueOf(iKey) + ",");
//			System.out.println("\t\t" + traindata.edgeWeight.get(key));
//		}

	
//		Graph grafo = new Graph(traindata.getVaribleList(),traindata.ClassNode, 
//				traindata.GetNT(), traindata.NijkcTable,traindata.Nijc_KTable, 
//				traindata.Nikc_JTable, traindata.edgeWeight);
		Graph grafo = new Graph(traindata);

		grafo.weightEdges(traindata);
		
		
		for (List<Integer> key : grafo.edgeWeight.keySet()){
			for(Integer iKey : key) System.out.print(String.valueOf(iKey) + ",");
			System.out.println("\t\t" + grafo.edgeWeight.get(key));
		}

		grafo.Kruskal(grafo.edgeWeight);

		
		
		System.err.println("Edges in tree");
		for (List<Integer> edge : grafo.spanningTree){
			System.err.println(edge);
		}
		
		grafo.makeTreeDirected();
		
//		for(VariableNode var : grafo.varList){
//			System.err.println("Variable " + var.getName() + " has childs ");
//			for(VariableNode childVar : var.children){
//				System.err.println(childVar.getName() + ", ");
//			}
//		}
		
		System.err.println("hello");
		for(VariableNode var : grafo.varList){
			System.err.println("Variable " + var.getName() + " has parent ");
			if (var.parent!= null){
				System.err.println(var.parent.getName());
				continue;
			}
			System.err.println("null");
		}
	}

//	}

}
