package stockmarket;

public class Corporation extends StockOwner {

	private double value;
	
	public Corporation(String name, double money, double value) {
		super(name, money);
		// TODO Auto-generated constructor stub

		this.value = value;
		
	}
	
	public Share createStock(int quantity){
		return new Share((StockOwner) this,this,quantity);
	}

	double getValue(){
		return value;
	}
	
}
