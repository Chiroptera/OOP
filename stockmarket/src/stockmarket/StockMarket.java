package stockmarket;

import java.util.LinkedList;
import java.util.Random;

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
	
	
	
	public boolean buy(StockOwner owner, Corporation corp, int quantity){
		int actualQuantity=quantity;

//		owner.getInfo();
		System.out.println(owner.name + " wants to buy " + quantity + " shares from " + corp.name);
		
		if(quantity == 0) return false;
		if(owner.money == 0) return false;
		
		/* compute actual amount to buy based on the money available*/
		if (corp.getValue() * quantity > owner.money){
			actualQuantity = ((int) corp.getValue() * quantity) / (int) owner.money;
		}

		/* if stock owner doesn't have money exit */
		if (actualQuantity == 0){ 
			return false;
		}

		/* list of available shares to buy*/
		LinkedList<Share> availableShares = new LinkedList<Share>();
		int availableQuantity=0;
		
		for (int i=0;i<market.size();i++){
			Share shareToBuy = market.get(i);
			
			/* add shares whose owner is the company they belong to*/
			if (shareToBuy.corpOfShare == corp && shareToBuy.ownerOfShare == corp){
				availableShares.add(shareToBuy);
				availableQuantity += shareToBuy.quantity;
			}
		}
		
		/* if no available shares, exit*/
		if (availableQuantity == 0){ 
			return false;
		}
		else if(availableQuantity < actualQuantity){
			actualQuantity = availableQuantity;
		}

		/* act of buying*/
		
		System.out.println(owner.name + " will buy " + quantity + " shares from " + corp.name);
		
		Share shareToBuy = new Share(owner,corp,actualQuantity);
		
		/* cycle that goes through all the list of available shares while the actual quantity hasn't reached 0 */
		for (int i = 0; i < availableShares.size() && actualQuantity!=0; i++){
			Share currentShare = availableShares.get(i);
			
//			System.out.println("i=" + i + "\t actual=" + actualQuantity + "\tcurrent=" + currentShare.quantity + "\tavailable="+availableQuantity);
			
			/* update actual quantity if available is less than what the owner wants/can buy */
			if (currentShare.quantity > actualQuantity){
				currentShare.update(-actualQuantity);
				break;
			}
			else if (currentShare.quantity <= actualQuantity){
				actualQuantity -= currentShare.quantity;
				market.remove(currentShare);
				corp.removeShare(currentShare);
			}
		}
				
		
		this.putInMarket(shareToBuy);
		owner.addShare(shareToBuy);
		
		/* update owner and corp money */
		owner.debit(actualQuantity * corp.getValue());
		corp.credit(actualQuantity * corp.getValue());
		
		
		return true;


	}

	
	@Override
	public String toString() {
		
		String output = "Owner" + "\t" + "Corp" + "\t" +  "Qt." + "\n";
		for(int i=0; i<market.size();i++){
			output += market.get(i).ownerOfShare.name + "\t" + market.get(i).corpOfShare.name + "\t" +  market.get(i).quantity + "\n";
		}
		return output;
	}

	/**
	 * @param args
	 */
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
			corps[i] = new Corporation("corp"+Integer.toString(i),startMoney*(i+1),startValue*(i+1));
			psi20.putInMarket(corps[i].createStock(500));
		}
		
		/* create more stock so it is not all together */
		for (int i=0;i<10;i++){
			psi20.putInMarket(corps[i].createStock(500));
		}
		
		/* create owners*/
		for (int i=0;i<20;i++){
			owners[i] = new StockOwner("owner"+Integer.toString(i),startMoney*(i+1));
		}
		
		/* add corps to list of owners */
		for (int i=20;i<30;i++){
			owners[i] = corps[i-20];
		}
		
		System.out.println(psi20.toString());
		
		/*
		 * 
		 * START OF SIMULATION
		 * 
		 * */
		
		Random randomCorp = new Random();
		randomCorp.nextInt(10);
		
		for (int i=0; i<owners.length; i++){
			psi20.buy(owners[i], corps[randomCorp.nextInt(10)], randomCorp.nextInt(500));
		}
		System.out.println("------------------------");
		System.out.println(psi20.toString());
		
		for (int i=0; i<owners.length; i++){
			psi20.buy(owners[i], corps[randomCorp.nextInt(10)], randomCorp.nextInt(500));
		}
		System.out.println("------------------------");
		System.out.println(psi20.toString());
		
		
		for (int i=0; i<owners.length; i++){
			psi20.buy(owners[i], corps[randomCorp.nextInt(10)], randomCorp.nextInt(500));
		}
		System.out.println("------------------------");
		System.out.println(psi20.toString());
		
		
		for (int i=0; i<owners.length; i++){
			psi20.buy(owners[i], corps[randomCorp.nextInt(10)], randomCorp.nextInt(500));
		}
		System.out.println("------------------------");
		System.out.println(psi20.toString());
		

	}

}
