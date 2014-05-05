package dataset;


public class ElapsedTime {
	
	long startTime = System.nanoTime();  

	public String toString() {
		return ((long) ((System.nanoTime() - startTime) * Math.pow (10,-9))) + " seconds.";
	}


}
