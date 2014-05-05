package dataset;


public class Main {
	
	public static void main(String[] args) {

		if (args.length != 3) {
			System.out.println("Incorrect usage.");
			return;
		}
		
		NaiveBayesClassification BNClass = new NaiveBayesClassification();
		
		try {
			BNClass.parseArg(args);
		} catch (NBCException e) {
			System.out.println("[NBClassification] " + "'" + e.getMessage() + "'" + " not a correct score specification, please choose between MDL or LL.");
		return;
		} catch (Exception e) {
			System.out.println("[NBClassification] Unexpected error during parsing " + e.getMessage());
			e.printStackTrace();
			return;
		}
		
		ElapsedTime time = new ElapsedTime();
		
		
		//trying to open the files, if not, error and return.
		//deal with parsing?
		//save files on class open files, with method to close?

		
		System.out.println("Building the classifier: " + time.toString());
		
	}
}
