package dataset;

public class ClassifierNode extends BayesNode {
	
	private int s; //number of classifications
	private int[] NC;

	ClassifierNode(String nameNew) {
		super(nameNew);
	}
	
	public int GetSR(){
		
		return this.s;
	}
	
	public int GetNC(int index){
		
		return this.NC[index];
	}
	
	
	public void CreateNC(){

		int i;
		
		NC = new int[this.s];
		
		for(i=0; i < this.s;i++){
			NC[i] = 0;
		}
	}
	
	public void UpdateNC(int index){
		
		NC[index] = NC[index] + 1;
	}
	
	public void UpdateSR(int sUp){
		
		if(sUp + 1 > this.s){
			this.s = sUp + 1;
		}		
	}
	
	public static void main(String[] args) {	
		
		ClassifierNode test1 = new ClassifierNode("name");
		test1.UpdateSR(10);
		System.out.println(test1.GetSR());

		test1.CreateNC();
		System.out.println(test1.GetNC(9));

		test1.UpdateNC(8);
		
		
		System.out.println(test1.GetNC(8));
		
	}
	
}
