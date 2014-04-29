package dataset;

public class ClassifierNode extends BayesNode {
	
	private int s; //number of classifications
	private int[] Nc;

	ClassifierNode(String nameNew) {
		super(nameNew);
	}
	
	public int GetS(){
		
		return this.s;
	}
	
	public int GetNc(int sClass){
		
		return this.Nc[sClass];
	}
	
	public void UpdateNcVector(int sClass){
		
		
		this.Nc[sClass]++;
	}
	
	public void UpdateSR(int sUp){
		
		if(sUp > this.s){
			this.s = sUp;
		}		
	}
	
	public static void main(String[] args) {	
		
		ClassifierNode test1 = new ClassifierNode("name");
		test1.UpdateSR(4);
		test1.UpdateSR(2);


		
		System.out.println(test1.GetS());
		
	}
	
}
