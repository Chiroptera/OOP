package dataset;

public class ClassifierNode extends BayesNode {
	
	private int s; //number of classifications
	private int[] Nc;

	ClassifierNode(String nameNew, int sNew) {
		super(nameNew);
		this.s = sNew;
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
	
	public void IncrementsNew(){
		
		this.s++;
	}
	
	public static void main(String[] args) {	
		
		ClassifierNode test1 = new ClassifierNode("name", 1);
		test1.UpdateNcVector(0);
		test1.UpdateNcVector(3);
		test1.IncrementsNew();

		
		System.out.println(test1.GetS());
		
	}
	
}
