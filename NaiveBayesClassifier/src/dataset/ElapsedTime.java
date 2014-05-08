package dataset;


public class ElapsedTime {
	
	protected long time;
	
	ElapsedTime(){
		
	}

	public void start(){
		time = System.nanoTime();
	}
	
	public void stop(){
		time = System.nanoTime() - time;
	}

	public String toString() {
		return ((long) (time * Math.pow (10,-9))) + " seconds.";
	}


}
