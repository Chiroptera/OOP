package dataset;


public class ElapsedTime {
	
	long startTime = System.nanoTime();  
	
	public long EndTime(){
		
		return (System.nanoTime() - startTime);
		
	}


}
