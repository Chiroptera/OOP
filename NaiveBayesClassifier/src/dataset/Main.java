package dataset;

import bayesClassification.NaiveBayesClassification;
import parseCSV.ParseCSV;
import parseCSV.ParseLearnCSV;


public class Main {
	
	public static void main(String[] args) {

		if (args.length != 3) {
			System.out.println("Incorrect usage.");
			return;
		}
		
		ElapsedTime time = new ElapsedTime();
		time.start();
		
		//nao convem chamar metodos nao privados/finais no constructor
		//http://www.javaspecialists.eu/archive/Issue210.html
		
		ParseLearnCSV trainSet = new ParseLearnCSV(args[0]);
		
		TrainDataSet trainData = new TrainDataSet();
		
		trainSet.parse(trainData);
		//Insert: Parse do Learn
		//Tested until here!!!
		

		
//		trainData.buildTable();
		
		System.out.println("********************************************");
		System.out.println("*                                          *");
		System.out.println("*           TAN                            *");
		System.out.println("*                                          *");
		System.out.println("********************************************");
		
		NaiveBayesClassification BNClass = new NaiveBayesClassification(args[2]);
		
		BNClass.Train(trainData);
		//BNClass
		
		ParseCSV testSet = new ParseCSV(args[1]);
		
		DataSet testData = new DataSet();
		
		testSet.parse(testData);
		
		
		BNClass.Test(testData);

		
//		try {
//			BNClass.parseArg(args);
//		} catch (NBCException e) {
//			System.out.println("[NBClassification] " + "'" + e.getMessage() + "'" + " not a correct score specification, please choose between MDL or LL.");
//		return;
//		} catch (Exception e) {
//			System.out.println("[NBClassification] Unexpected error during parsing " + e.getMessage());
//			e.printStackTrace();
//			return;
//		}
		//trying to open the files, if not, error and return.
		//deal with parsing?
		//save files on class open files, with method to close?

		time.stop();
		System.out.println("Building the classifier: " + time.toString());
		
		
		
	}
	
	
	//MUDANCAS
	//constructor nao chama parse - parse agora e 
}
