package utils;


public class NBCException extends Exception{

	private static final long serialVersionUID = 1L;

	public NBCException(String message) { 
		 super("Score type " + "\"" + message + "\"" + " is not valid. Choose either \"LL\" or \"MDL\"."); 
	 }

}
