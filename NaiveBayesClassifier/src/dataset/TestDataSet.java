package dataset;



public class TestDataSet extends DataSet {

	int[] classes=null;
	
	public TestDataSet() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Classifies the instance identified by <b>instanceNb<b> with the class value <b>c<b>.
	 * 
	 * @param instanceNb Identifying int of instance (the order of the instances in the instance list is never changed).
	 * @param c Class value of instance.
	 */
	public void classifyInstance(int instanceNb, int c){
		if (classes==null) classes  = new int[super.nInstances];
 		classes[instanceNb]=c;
	}
	
	/**
	 * Returns the class value of the instance identified by <b<instID<b>.
	 * @param instID Identifying int of instance (the order of the instances in the instance list is never changed).
	 * @return Classe value (<b>int<b>)
	 */
	public int getInstanceClass(int instID){
		return classes[instID];
	}
	
	/**
	 * Returns entire array of instance classification. The classification values are on the positions of the array corresponding to the instance number.
	 * @return int[] classes 
	 */
	public int[] getInstanceClassArray(){
		return classes;
	}
	
	public String getStringOfInstance(int instID){
		String stringResult = "";
		int[] instance = super.parsedDataList.get(instID);
		for (int i=0;i<instance.length-1;i++){
			stringResult += String.valueOf(instance[i]) + ",";
		}
		stringResult += String.valueOf(instance[instance.length-1]);
		return stringResult;
	}
	
	
	/**
	 * Prints all instances and corresponding classifications in the format <p>
	 *  <b>Instance [intance number] -> [tab][tab] [instance classification]<b><p>
	 *  e.g. <b>Instance 1 -> 		 3<b>
	 */
	public void printInstancesWithClass(){
		for(int i=0;i<super.nInstances;i++){
			System.out.println("-> instance " + (i+1) + ":\t\t" + classes[i]);
		}

		
	}

	

}

