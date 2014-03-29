package stockmarket;

public class Share {

	int quantity;
	StockOwner ownerOfShare;
	Corporation corpOfShare;
	
	Share(StockOwner owner, Corporation corp, int qt) {
		// TODO Auto-generated constructor stub
		
		quantity = qt;
		ownerOfShare = owner;
		corpOfShare = corp;
	}
	
	public void update(int diff){
		quantity += diff;
	}


	
}
