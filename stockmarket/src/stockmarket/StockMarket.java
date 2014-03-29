package stockmarket;

import java.util.Iterator;
import java.util.LinkedList;

public class StockMarket {

	LinkedList<Share> market = new LinkedList<Share>();
	StockMarket(){
		
	}
	
	public void putInMarket(Share newShare){
		market.add(newShare);
	}
	
//	public void mergeShares(){
//		for (int i=0;i<market.size();i++){
//			for (int j=i;j<market.size();j++){
//				if 
//			}
//		}
//	}
	
	public Share buy(StockOwner owner, Corporation corp, int quantity){
		int actualQuantity=quantity;

		/* compute actual ammount to buy based on the money available*/
		if (corp.getValue() * quantity > owner.money){
			actualQuantity = ((int) corp.getValue() * quantity) / (int) owner.money;
		}

		/* if stockowner doesn't have money exit */
		if (actualQuantity == 0){ 
			return null;
		}

		/* list of available shares to buy*/
		LinkedList<Share> availableShares = new LinkedList<Share>();
		int availableQuantity=0;
		for (int i=0;i<market.size();i++){
			Share shareToBuy = market.get(i);
			if (shareToBuy.corpOfShare == corp && shareToBuy.ownerOfShare == corp){
				availableShares.add(shareToBuy);
				availableQuantity += shareToBuy.quantity;
			}
		}
		
		/* if no available shares, exit*/
		if (availableQuantity == 0){ 
			return null;
		}
		else if(availableQuantity < actualQuantity){
			actualQuantity = availableQuantity;
		}

		/* remove money from stockowner*/
		//owner.debit(availableQuantity*corp.getValue())
		
		/* act of buying*/
		
		for (int i = 0; i<market.size() && actualQuantity!=0; i++){
			Share currentShare = market.get(i);
			if (currentShare.quantity > actualQuantity){
				currentShare.update(currentShare.quantity-actualQuantity);
				break;
			}
			else if (currentShare.quantity <= actualQuantity){
				actualQuantity -= currentShare.quantity;
				market.remove(currentShare);
				corp.removeShare(currentShare);
			}
		}
		
		Share shareToBuy = new Share(owner,corp,actualQuantity);
		this.putInMarket(shareToBuy);
		//owner.addShare(shareToBuy);
		
		owner.debit(actualQuantity*corp.getValue());
		corp.credit(actualQuantity*corp.getValue());
		
		
		return null;


	}

	
	@Override
	public String toString() {
		
		String output = "Corp" + "\t" + "Owner" + "\t" + "Qt." + "\n";
		for(int i=0; i<market.size();i++){
			output += market.get(i).corpOfShare.name + "\t" + market.get(i).ownerOfShare.name + "\t" + market.get(i).quantity + "\n";
		}
		return output;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		double startMoney = 500;
		double startValue = 0.5;
		
		StockMarket psi20 = new StockMarket();
		
		Corporation[] corps = new Corporation[10];
		
		StockOwner[] owners = new StockOwner[30];
		
		/* create corps and initial stock*/
		for (int i=0;i<10;i++){
			corps[i] = new Corporation("corp"+Integer.toString(i),startMoney*i,startValue*i);
			psi20.putInMarket(corps[i].createStock(500));
		}
		
		/* create more stock so it is not all together */
		for (int i=0;i<10;i++){
			psi20.putInMarket(corps[i].createStock(500));
		}
		
		/* create owners*/
		for (int i=0;i<20;i++){
			owners[i] = new StockOwner("owner"+Integer.toString(i),startMoney*i);
		}
		
		/* add corps to list of owners */
		for (int i=20;i<30;i++){
			owners[i] = corps[i-20];
		}
		
		System.out.print(psi20.toString());

	}

}
