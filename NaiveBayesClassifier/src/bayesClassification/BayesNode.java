package bayesClassification;

public abstract class BayesNode {

	private String name; //name
	
	BayesNode(String nameNew){
		
		this.name = nameNew;
	}
	
	public String getName(){
		
		return this.name;
	}
	
	
	public abstract void UpdateSR(int rs);
	public abstract int GetSR();

}