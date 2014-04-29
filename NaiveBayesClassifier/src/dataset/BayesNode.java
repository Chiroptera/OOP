package dataset;

public abstract class BayesNode {

	private String name; //name
	private int value; //??
	
	BayesNode(String nameNew){
		
		this.name = nameNew;
	}
	
	public String getName(){
		
		return this.name;
	}
	
	public int getValue(){
		
		return this.value;
	}
	
}
