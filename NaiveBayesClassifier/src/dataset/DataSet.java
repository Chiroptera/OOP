package dataset;

import java.io.BufferedReader; //reads text from a character-input stream
import java.io.FileNotFoundException; //signal that an attempt to open the file has failed
import java.io.FileReader; //convenience class for read char files
import java.io.IOException; //signals that an I/O exception of some sort has occurred
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;




public class DataSet {

	LinkedList<int[]> parsedData = new LinkedList<int[]>();
	Hashtable<List<Integer>,Integer> NijkcTable = new Hashtable<List<Integer>,Integer>();
	Hashtable<List<Integer>,Integer> Nikc_JTable = new Hashtable<List<Integer>,Integer>();
	Hashtable<List<Integer>,Integer> Nijc_KTable = new Hashtable<List<Integer>,Integer>();
	

	Map<List<Integer>, Float> edgeWeight = new HashMap<List<Integer>, Float>();

	
	BayesNode[] NodeList;
	ClassifierNode ClassNode;
	
	private int NT;
	int NX;

	DataSet(){
		System.err.println("Hello Dataset!");
	}
	
	public int GetNT() {
		
		return NT;
	}

	public void parse(String Filename) {
		//System.err.println("parse:");
		String csvFile = (Filename);

		BufferedReader br = null;
		String line;
		String cvsSplitBy = ",";
		int sSize;
		

		//System.err.println("parse: before try");

		try {
			br = new BufferedReader(new FileReader(csvFile));
			
			String[] lineparse;
			int[] temp;

			/* while exists lines*/

			/* if first line not null */
			if((line = br.readLine()) != null){

			    // use comma as separator
				lineparse = line.split(cvsSplitBy); /* split string by commas */
				sSize = lineparse.length; /* number of variables + 1 (class variable)*/
				this.NX = sSize -1;

				NodeList = new BayesNode[sSize];

				System.out.print("Variable: ");

				/* go through all strings in line (seperated by commas)*/
				for(int j=0;j<sSize-1;j++){
					
					//System.err.println("iteration j=" + String.valueOf(j));
					NodeList[j] = new VariableNode(lineparse[j], j);
					System.out.print(" | " + NodeList[j].getName());
				}

				NodeList[sSize-1] = new ClassifierNode(lineparse[sSize-1]);
				ClassNode = (ClassifierNode)NodeList[sSize-1];
				
				System.out.print(" | " + lineparse[sSize-1] + " |");

				/* while there are other lines*/
				while ((line = br.readLine()) != null) {

				    // use comma as separator
					lineparse = line.split(cvsSplitBy); /* split string by commas */
					sSize = lineparse.length; /* number of variables + 1 (class variable)*/

	//				else {
					
					System.out.println("Values: ");
					System.out.println("line[0]: " + line.toString());
					temp = new int[sSize];


						for(int j=0 ; j<sSize ;j++){
							
							temp[j] = Integer.parseInt(lineparse[j]);
							
	//						System.err.println("iteration j=" + String.valueOf(j) 
	//								+ "\tlineparse.size = " + String.valueOf(lineparse.length)
	//								+ "\tlineparse[j]=" + lineparse[j]
	//								+ "\tlineparse[j] int =" + Integer.parseInt(lineparse[j])
	//								+ "\ttemp.size = " + String.valueOf(temp.length)
	//								+ "\ttemp[j] = " + String.valueOf(temp[j]));

							System.err.println("iteration j=" + String.valueOf(j) 
							+ "\tlineparse.length = " + String.valueOf(lineparse.length)
							+ "\tlineparse[j]=" + lineparse[j]
							+ "\tlineparse[j] int =" + Integer.parseInt(lineparse[j])
							+ "\tnodelist.length = " + String.valueOf(NodeList.length)
							+ "\tnodelist[j].id = " + NodeList[j].getName());

							NodeList[j].UpdateSR(Integer.parseInt(lineparse[j]));

							System.out.print("|" + lineparse[j] + " ");
						}
						parsedData.add(temp);
						this.NT ++;
					}

					System.out.println();
			}	
//			}

		} catch (FileNotFoundException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		ClassNode.CreateNC();
  }

	public void buildTable(){
	    /*for(Iterator iter = parsedData.iterator();iter.hasNext();){*/
		List<Integer> key,keyInv;
		Integer value;
		int classe;
				

		/* for each line of parsed data*/
		for(int[] dataLine : parsedData){
			classe = dataLine[dataLine.length-1];
			
			ClassNode.UpdateNC(classe);

			/* for each variable excluding the class */
			for(int i=0;i<dataLine.length-1;i++){

				/* 
				 * 
				 *  CHECKING FOR Nikc_J
				 * 
				 * */
				/* key = [var id, var value, class value] */
				key=Arrays.asList(i,dataLine[i],classe);

				/* if key already exists, increment*/
				if(Nikc_JTable.containsKey(key)){
					value=Nikc_JTable.get(key).intValue() + 1; /* increment Nikc_J */
					Nikc_JTable.put(key,value);
				}
				/* otherwise add entry to table with value 1*/
				else{
					Nikc_JTable.put(key, 1);
				}



				/* go through all permutations of variables and their parents (a var i can't be its own parent)
				 * and their corresponding values */
				for(int il=0;il!=i && il<dataLine.length-1;il++){

					/* 
					 * 
					 *  CHECKING FOR Nijkc
					 * 
					 * */
					/* key = [var id, parent id, var value, parent value, class value] */
					key=Arrays.asList(i,il,dataLine[i],dataLine[il],classe);
					keyInv=Arrays.asList(il,i,dataLine[il],dataLine[i],classe);


					/* if key already exists, increment*/
					if(NijkcTable.containsKey(key)){
						value=NijkcTable.get(key).intValue()+1; /* increment Nijkc */
						NijkcTable.put(key,value); /* add entry in table*/
						NijkcTable.put(keyInv,value);

					}
					/* otherwise add entries to table with value 1*/
					else{

						NijkcTable.put(key, 1);
						NijkcTable.put(keyInv, 1);
					}

					/* 
					 * 
					 *  CHECKING FOR Nijc_K
					 * 
					 * */
					key=Arrays.asList(i,il,dataLine[il],classe);
					keyInv=Arrays.asList(il,i,dataLine[i],classe);


					/* if key already exists, increment*/
					if(Nijc_KTable.containsKey(key)){
						value=Nijc_KTable.get(key).intValue()+1; /* increment Nijkc */
						Nijc_KTable.put(key,value); /* add entry in table*/
					}
					/* otherwise add entries to table with value 1*/
					else{
						Nijc_KTable.put(key,1);
					}
					
					/* if keyInv already exists, increment*/
					if(Nijc_KTable.containsKey(keyInv)){
						value=Nijc_KTable.get(key).intValue()+1; /* increment Nijkc */
						Nijc_KTable.put(keyInv,value);
					}
					/* otherwise add entries to table with value 1*/
					else{
						Nijc_KTable.put(keyInv,1);
					}
					
					

				} /* end cycle il*/
			} /* end cycle i*/
	    } /* end cycle dataLine*/
	}
	
	public void buildmatrix(){
		
		float score, scoreMDL, scoreLL;
		float scoreIJC, scoreIJKC, scoreIKC; //number of instances of each table
		
		List<Integer> keyIJKC, keyIJC, keyIKC, edgeKey;
		
		Float value;

		for(int i = 0; i < this.NX; i++){
			for(int ii = 0 ; ii < this.NX; ii++){
				score = 0;
				for(int j = 0; j < NodeList[ii].GetSR(); j++){
					for(int k = 0; k < NodeList[i].GetSR(); k++){
						for(int c = 0; c < ClassNode.GetSR(); c++){

							keyIKC = Arrays.asList(i,k,c);
							keyIJKC = Arrays.asList(i,ii,k,j,c);
							keyIJC = Arrays.asList(ii,j,c);
							
							/*
							System.out.println(keyIJC.toString());
							System.out.println(keyIJKC.toString());
							System.out.println(keyIKC.toString());
							System.out.println(Nijc_KTable.containsKey(keyIJC));	
							System.out.println(NijkcTable.containsKey(keyIJKC) );			
							System.out.println(Nikc_JTable.containsKey(keyIKC));	
							System.out.println();	
							*/		
		
							if(Nikc_JTable.containsKey(keyIJC)){
								scoreIJC = Nikc_JTable.get(keyIJC).intValue();	
								//System.out.println(Nijc_KTable.get(keyIJC).intValue());			

							}
							else{
								continue;
							}
							
							if( NijkcTable.containsKey(keyIJKC) ){
								scoreIJKC = NijkcTable.get(keyIJKC).intValue();
								//System.out.println(NijkcTable.get(keyIJKC).intValue());			

							}
							else{
								continue;
							}
							
							if( Nikc_JTable.containsKey(keyIKC) ){
								scoreIKC = Nikc_JTable.get(keyIKC).intValue();
//								System.out.print(String.valueOf(keyIKC) + ",");
//								System.out.println(Nikc_JTable.get(keyIKC).intValue());			

							}
							else{
								continue;
							}
//
							if((i == 1 && ii == 0) || (i == 0 && ii == 1)){
								System.out.print("Mid-score:");
								System.out.print("   k: " + k + " j: " + j + " c: " + c);
								System.out.print("   ScoreIJKC: " + scoreIJKC);
								System.out.print("   ScoreIJC: " + scoreIJC);
								System.out.print("   ScoreIKC: " + scoreIKC);
								System.out.print("   Score: " + score);
								System.out.println("   Class occurrences: " + ClassNode.GetNC(c));
							}


							
							score += (scoreIJKC / (this.NT)) * 
									( Math.log( (scoreIJKC * ClassNode.GetNC(c)) / (scoreIKC*scoreIJC )) 
									/ Math.log(2));
//							System.out.println("Score: " + score);
//							System.out.println();
//							System.out.println();
							
						}
					}	
				}
				
				if((i == 0 && ii == 1) || (i == 1 && ii == 0)){
					System.out.println("Score: " + score + " for i= " + i + " and i'= " + ii);
				}
				
				edgeKey = Arrays.asList(i,ii);
				scoreLL = score;
				scoreMDL = (float) (score - (((ClassNode.GetSR() * (NodeList[i].GetSR() - 1) * (NodeList[ii].GetSR() - 1))/2) * Math.log(this.NX)));
				//take decision based on argument
				value = score;
				edgeWeight.put(edgeKey,value);
				
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.err.println("Hello hashTable!");
		DataSet obj = new DataSet();
		obj.parse(args[0]);
		obj.buildTable();
		
		
		for(int i = 0;  i < obj.ClassNode.GetSR(); i++){
			System.out.println("Class " + i +  " has " + obj.ClassNode.GetNC(i) + " instances.");
		}
		System.out.println("Number of instances: " + obj.GetNT());


		/* print Nijkc*/
		System.out.println("\nNijkc:\nKeys:\t\tValues:\n");
		for (List<Integer> key : obj.NijkcTable.keySet()){
			for(Integer iKey : key) System.out.print(String.valueOf(iKey) + ",");
			System.out.println("\t\t" + obj.NijkcTable.get(key));
		}
		/* print Nikc_J*/
		System.out.println("\nNikc_J:\nKeys:\t\tValues:\n");
		for (List<Integer> key : obj.Nikc_JTable.keySet()){
			for(Integer iKey : key) System.out.print(String.valueOf(iKey) + ",");
			System.out.println("\t\t" + obj.Nikc_JTable.get(key));
		}
		/* print Nijc_K*/
		System.out.println("\nNijc_K:\nKeys:\t\tValues:\n");
		for (List<Integer> key : obj.Nijc_KTable.keySet()){
			for(Integer iKey : key) System.out.print(String.valueOf(iKey) + ",");
			System.out.println("\t\t" + obj.Nijc_KTable.get(key));
		}
		
		for (List<Integer> key : obj.Nijc_KTable.keySet()){
			for(Integer iKey : key) System.out.print(String.valueOf(iKey) + ",");
			System.out.println("\t\t" + obj.Nijc_KTable.get(key));
		}
		
		obj.buildmatrix();
		
		for (List<Integer> key : obj.edgeWeight.keySet()){
			for(Integer iKey : key) System.out.print(String.valueOf(iKey) + ",");
			System.out.println("\t\t" + obj.edgeWeight.get(key));
		}
		

		
		
		/*for (Iterator<Edge> iter = obj.edgeWeight.iterator(); iter.hasNext(); ) {
			
		    Edge element = iter.next();
		    
			System.out.println("peer1: " + element.peer[0] + ", peer2: " + element.peer[1] + ", score:" + element.score);

		    // 1 - can call methods of element
		    // 2 - can use iter.remove() to remove the current element from the list

		    // ...
		}*/
		
	
	}  
}

