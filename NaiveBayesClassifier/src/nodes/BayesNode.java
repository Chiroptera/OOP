package nodes;


/**
 * Abstract class that represents the common aspects of the variable and classification node.
 */

public abstract class BayesNode {

	protected String name; //name
	protected int rs;
	
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
	public void updateSR(int rsUp){
		if(rsUp + 1 > this.rs){
			this.rs = rsUp + 1;
		}	
	}
	
	/**
	 * @return Different values possible of the variable/classification,
	 */
	public int getSR(){
		return rs;
	}

}
