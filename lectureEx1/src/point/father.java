package point;

public class father {
	
	int y;
	
	father(){
		y=22;
	}
	
	father(int x){
		y=x;
	}
	
	void printHello(){
		System.out.println("Hello, father!" + "y=" + y);
	}
}
