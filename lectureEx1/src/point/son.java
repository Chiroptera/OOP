package point;

public class son extends father {
	int x;
	int y;
	
	son(){
		//super(y);
		y*=2;
	}
	
	son(int x){
		y*=2;
	}
	
	void printHello(){
		System.out.println("Hello, son!" + "y=" + y);
	}
}
