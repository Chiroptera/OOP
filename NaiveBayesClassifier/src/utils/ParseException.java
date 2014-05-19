package utils;


public class ParseException extends Exception{

private static final long serialVersionUID = 1L;

public ParseException(int rSize, int lineLength,int cSize) {
super("Line " + (rSize+1) + " has incorrect size of " + lineLength + ". Should be " + cSize);
}

}