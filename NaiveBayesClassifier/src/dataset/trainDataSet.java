package dataset;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class trainDataSet extends DataSet {
	
	Map<List<Integer>,Integer> NijkcTable = new HashMap<List<Integer>,Integer>();
	Map<List<Integer>,Integer> Nikc_JTable = new HashMap<List<Integer>,Integer>();
	Map<List<Integer>,Integer> Nijc_KTable = new HashMap<List<Integer>,Integer>();
	
	
	public void buildTable(){
	    /*for(Iterator iter = parsedData.iterator();iter.hasNext();){*/
		List<Integer> key,keyInv;
		Integer value;
		int classe;
				

		/* for each line of parsed data*/
		for(int[] dataLine : parsedData){
			classe = dataLine[this.NX];
			
			System.out.println("classe: " + classe);
			
			ClassNode.UpdateNC(classe);

			/* for each variable excluding the class */
			for(int i=0;i<dataLine.length-1;i++){

				 // CHECKING FOR Nikc_j

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
				for(int il=0; /*il!=i &&*/ il<dataLine.length-1;il++){
					
					if(il == i) continue;
					
					 // CHECKING FOR Nijkc

					/* key = [var id, parent id, var value, parent value, class value] */
					key=Arrays.asList(i,il,dataLine[i],dataLine[il],classe);


					/* if key already exists, increment*/
					if(NijkcTable.containsKey(key)){
						value=NijkcTable.get(key).intValue()+1; /* increment Nijkc */
						NijkcTable.put(key,value); /* add entry in table*/
					}
					/* otherwise add entries to table with value 1*/
					else{

						NijkcTable.put(key, 1);
					}

					/* 
					 * 
					 *  CHECKING FOR Nijc_K
					 * 
					 * */
					key=Arrays.asList(i,il,dataLine[il],classe);
					//keyInv=Arrays.asList(il,i,dataLine[i],classe);


					/* if key already exists, increment*/
					if(Nijc_KTable.containsKey(key)){
						
						value = Nijc_KTable.get(key).intValue() + 1; /* increment Nijkc */
						Nijc_KTable.put(key,value); /* add entry in table*/
						
					}
					/* otherwise add entries to table with value 1*/
					else{
						Nijc_KTable.put(key, 1);
					}	
					
				} /* end cycle il*/
			} /* end cycle i*/
	    } /* end cycle dataLine*/		
	}

	public int getNijkc(int i, int Pi, int k, int j, int c){
		/*
		 * i	- id of variable
		 * Pi	- id of parent of variable i
		 * k	- value of i
		 * j	- value of j
		 * c	- value of class variable 
		 */
		
		Integer returnValue= NijkcTable.get(Arrays.asList(i,Pi,k,j,c));
		
		return (returnValue == null) ? 0 : returnValue.intValue();
	}
	
	public int getNikc(int i, int k, int c){
		/*
		 * i	- id of variable
		 * k	- value of i
		 * c	- value of class variable 
		 */
		Integer returnValue= Nikc_JTable.get(Arrays.asList(i,k,c));
		
		return (returnValue == null) ? 0 : returnValue.intValue();
	}
	
	public int getNijc(int i, int Pi,int j, int c){
		/*
		 * i	- id of variable
		 * Pi	- id of parent of variable i
		 * j	- value of j
		 * c	- value of class variable 
		 */
		Integer returnValue= Nijc_KTable.get(Arrays.asList(i,Pi,j,c));
	
		return (returnValue == null) ? 0 : returnValue.intValue();
	}
	
	public boolean containIKJC(int i, int Pi, int k, int j, int c){
		return NijkcTable.containsKey(Arrays.asList(i,Pi,k,j,c));
	}
	
	public boolean containIKC(int i,int k, int c){
		return Nikc_JTable.containsKey(Arrays.asList(i,k,c));
	}
	
	public boolean containIJC(int i, int Pi, int j, int c){
		return Nijc_KTable.containsKey(Arrays.asList(i,Pi,j,c));
	}
	
	

}

