package bayesClassification;


/**
 * Abstract class that represents the common aspects of the variable and classification node.
 */

public abstract class BayesNode {

	private String name; //name
	
	BayesNode(String nameNew){
		
		this.name = nameNew;
	}
	
	/**
	 * @return name Name of the variable/classification.
	 */
	
	public String getName(){
		
		return this.name;
	}
	
	/**
	 * Update the different values possible to the variable/classification.
	 */
	public abstract void UpdateSR(int rs);
	
	/**
	 * @return Different values possible of the variable/classification,
	 */
	public abstract int GetSR();

}
