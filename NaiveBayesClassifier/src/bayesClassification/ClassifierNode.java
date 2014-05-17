package bayesClassification;


/**
 * Class that extends the BayesNode class and contains information about the classification,
 * including the number of classification and the occurrence of each one,
 */

public class ClassifierNode extends BayesNode {
	
	private int s; //number of classifications
	private int[] NC;

	public ClassifierNode(String nameNew) {
		super(nameNew);
	}
	
	/**
	 * @return s Different values possible of the variable/classification.
	 */
	public int GetSR(){
		
		return this.s;
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
	public void CreateNC(){

		int i;
		
		NC = new int[this.s];
		
		for(i=0; i < this.s;i++){
			NC[i] = 0;
		}
	}
	
	/**
	 * Method that updates the number of occurrencies for each classification value.
	 */
	public void UpdateNC(int index){
		
		NC[index] = NC[index] + 1;
	}
	/**
	 * 
	 * Method that updates the number of different values of the classification.
	 */
	public void UpdateSR(int sUp){
		
		if(sUp + 1 > this.s){
			this.s = sUp + 1;
		}		
	}
	
}
