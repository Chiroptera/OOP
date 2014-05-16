package dataset;

import java.io.FileNotFoundException;
import java.io.IOException;

import bayesClassification.NBCException;
import bayesClassification.NaiveBayesClassification;
import parseCSV.*;

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
		
		try {
			trainSet.parseTo(trainData);

		} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("Train file not found. Please input a correct file.");
				System.exit(1);
		} catch (IOException e) {
				e.printStackTrace();
				System.out.println("There was a problem reading the train file. Retrying...");
				try {
					trainSet.parseTo(trainData);
				} catch (FileNotFoundException ef) {
						e.printStackTrace();
						System.out.println("Train file not found. Please input a correct file.");
						System.exit(1);
				} catch (IOException ef) {
						e.printStackTrace();
						System.out.println("There was a problem reading the train file.");
						System.exit(1);
				} catch (Exception ef) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("Some lines in your train file don't have the same number of elements. Please input a correct file.");
					System.exit(1);
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Some lines in your train file don't have the same number of elements. Please input a correct file.");
			System.exit(1);
		}
		
		NaiveBayesClassification BNClass = null;
		try {
			BNClass = new NaiveBayesClassification(args[2]);
		} catch (NBCException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("The score is not valid. Choose either \"LL\" or \"MDL\".");
			System.exit(1);
		}
		
		if (BNClass != null) BNClass.Train(trainData);
		
		ParseTestCSV testSet = new ParseTestCSV(args[1]);
		
		TestDataSet testData = new TestDataSet();
		
		try {
			testSet.parseTo(testData);

		} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("Test file not found. Please input a correct file.");
				System.exit(1);
		} catch (IOException e) {
				e.printStackTrace();
				System.out.println("There was a problem reading the test file. Retrying...");
				try {
					testSet.parseTo(testData);
				} catch (FileNotFoundException ef) {
						e.printStackTrace();
						System.out.println("Test file not found. Please input a correct file.");
						System.exit(1);
				} catch (IOException ef) {
						e.printStackTrace();
						System.out.println("There was a problem reading the test file.");
						System.exit(1);
				} catch (Exception ef) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("Some lines in your test file don't have the same number of elements. Please input a correct file.");
					System.exit(1);
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Some lines in your test file don't have the same number of elements. Please input a correct file.");
			System.exit(1);
		}
		

		if (BNClass != null) BNClass.Test(testData);

		testData.printInstancesWithClass();


		time.stop();
		System.out.println("Building the classifier: " + time.toString());
		
		
		
	}
	
	
	//MUDANCAS
	//constructor nao chama parse - parse agora e 
}
