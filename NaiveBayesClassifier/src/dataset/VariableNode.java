package dataset;


public class VariableNode extends BayesNode {
	

	private int id; //index
	private int r; //number of values in variable
	BayesNode parent; //graph built by saying who is the father
	
	VariableNode(String nameNew, int idNew) {
		super(nameNew);
		this.id = idNew;
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
	
	public static void main(String[] args) {		
		
	}
}
