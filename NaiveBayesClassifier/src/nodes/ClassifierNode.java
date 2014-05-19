package nodes;


/**
 * Class that extends the BayesNode class and contains information about the classification,
 * including the number of classification and the occurrence of each one,
 */

public class ClassifierNode extends BayesNode {
	
	private int[] NC;

	public ClassifierNode(String nameNew) {
		super(nameNew);
	}
	/**
	 * @return NC Vector with the occurrences for each different classification.
	 */
	public int GetNC(int index){
		
		return this.NC[index];
	}
	
	/**
	 * Method that creates the vector of occurrences.
	 */
	public void createNC(){

		int i;
		
		NC = new int[this.rs];
		
		for(i=0; i < this.rs;i++){
			NC[i] = 0;
		}
	}
	
	/**
	 * Method that updates the number of occurrencies for each classification value.
	 */
	public void updateNC(int index){
		
		NC[index] = NC[index] + 1;
	}

}
