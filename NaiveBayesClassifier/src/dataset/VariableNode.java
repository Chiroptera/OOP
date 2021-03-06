package dataset;

import java.util.LinkedList;


public class VariableNode extends BayesNode {
	

	private int id; //index
	private int r; //number of values in variable
	VariableNode parent; //graph built by saying who is the father
	LinkedList<VariableNode> children;
	
	VariableNode(String nameNew, int idNew) {
		super(nameNew);
		this.id = idNew;
	}
	
	void setParent(VariableNode p){
		this.parent=p;
	}
	
	VariableNode GetParent(){
		return this.parent;
	}
	
	public int GetId(){
		
		return this.id;
	}
	
	//this method can be abstract and implemented by each node variable
	public void UpdateSR(int rUp){
		
		if(rUp + 1 > this.r){
			this.r = rUp + 1;
		}		
	}
	

	public int GetSR(){
		
		return r;
	}


	public void addChild(VariableNode child){
		this.children.add(child);

	}
	
	public static void main(String[] args) {		
		
	}
}
