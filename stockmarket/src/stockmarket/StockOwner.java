package stockmarket;

import java.util.LinkedList;

public class StockOwner {

	String name;
	double money;
	
	LinkedList<Share> wallet = new LinkedList<Share>();
	
	StockOwner (String name, double money){
		this.name = name;
		this.money = money;
	}
	
	public void credit (double moreMoney){
		money += moreMoney;
	}
	
	public void debit (double lessMoney){
		money -= lessMoney;
	}
	
	public void addShare(Share newShares){
		wallet.add(newShares);
	}
	
	public void removeShare(Share removedShares){
		wallet.remove(removedShares);
	}
	
	
	
}
