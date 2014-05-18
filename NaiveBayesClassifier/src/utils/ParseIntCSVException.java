package utils;


public class ParseIntCSVException extends Exception{

	private static final long serialVersionUID = 1L;

	public ParseIntCSVException(int rowSize, int j) { 
		 super("The value in line " + (rowSize+1) + " and in collumn " + (j+1) + " must be an integer."); 
	 }

}
