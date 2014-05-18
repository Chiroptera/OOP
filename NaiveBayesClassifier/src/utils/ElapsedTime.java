package utils;


public class ElapsedTime {
	
	protected long time;
	
	public ElapsedTime(){
		
	}

	public void start(){
		time = System.nanoTime();
	}
	
	public void stop(){
		time = System.nanoTime() - time;
	}
	

	public String toString() {
		return String.valueOf(time/1000000) + " miliseconds.";
//		return ((long) (time * Math.pow (10,-9))) + " seconds.";
	}


}
