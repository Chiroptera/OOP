package nodes;

/**
 * Class that extends the BayesNode class and contains information about each variable, including who is the parent node
 * , and the number of different values of the variable.
 * */

public class VariableNode extends BayesNode {
	

	protected VariableNode parent; //graph built by saying who is the father
	int id;
	
	VariableNode(String nameNew) {
		super(nameNew);
	}

	public VariableNode(String nameNew, int id) {
		super(nameNew);
		setID(id);
	}
	
	/**
	 * Defines the parent node.
	 * */
	public void setParent(VariableNode p){
		this.parent=p;
	}
	
	/**
	 * Returns the parent node.
	 * @return parent 
	 * */
	public VariableNode getParent(){
		return this.parent;
	}
	
	/**
	 * Defined the ID of the variable.
	 * */
	public void setID(int id){
		this.id=id;
	}
	
	/**
	 * @return id ID of the variable.
	 * */
	public int getID(){
		return id;
	}
	
}
