package executables;

import java.io.FileNotFoundException;
import java.io.IOException;

import bayesClassification.*;
import parseCSV.*;
import utils.*;
import dataset.*;

public class Main {
	
	public static void main(String[] args) throws Exception {

		if (args.length != 3) {
			System.out.println("Incorrect usage.");
			return;
		}
		

		ParseLearnCSV trainSet=null;
		
		TrainDataSet trainData = new TrainDataSet();
		
		try {
			trainSet = new ParseLearnCSV(args[0]);
			trainSet.parseTo(trainData);

		} catch (FileNotFoundException e) {
//				e.printStackTrace();
				System.out.println("Train file not found. Please input a correct file.");
				System.exit(1);
		} catch (IOException e) {
//				e.printStackTrace();
				System.out.println("There was a problem reading the train file. Retrying...");
				try {
					trainSet.parseTo(trainData);
				} catch (FileNotFoundException ef) {
//						e.printStackTrace();
						System.out.println("Train file not found. Please input a correct file.");
						System.exit(1);
				} catch (ParseIntCSVException ef) {
//					e.printStackTrace();
					System.out.println("Values in train instances file must be an integer number.");
					System.exit(1);
				} catch (IOException ef) {
//						e.printStackTrace();
						System.out.println("There was a problem reading the train file.");
						System.exit(1);
				} catch (ParseException ef) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
					System.out.println("Some lines in your train file don't have the same number of elements. Please input a correct file.");
					System.exit(1);
				}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println("Some lines in your train file don't have the same number of elements. Please input a correct file.");
			System.exit(1);
		} catch (ParseIntCSVException e) {
//			e.printStackTrace();
			System.out.println("Values in test instances file must be an integer number.");
			System.exit(1);
		}
		
		NaiveBayesClassification BNClass = null;
		try {
			BNClass = new NaiveBayesClassification(args[2]);
		} catch (NBCException e1) {
			// TODO Auto-generated catch block
//			e1.printStackTrace();
			System.out.println("The score is not valid. Choose either \"LL\" or \"MDL\".");
			System.exit(1);
		}
		ElapsedTime time = new ElapsedTime();
		time.start();
		
		
		if (BNClass != null) BNClass.Train(trainData);
		time.stop();
		System.out.println("Building the classifier: " + time.toString());
		
		ParseTestCSV testSet = null;
		
		TestDataSet testData = new TestDataSet();
		
		try {
			testSet = new ParseTestCSV(args[1]);
			testSet.parseTo(testData);

		} catch (FileNotFoundException e) {
//				e.printStackTrace();
				System.out.println("Test file not found. Please input a correct file.");
				System.exit(1);
		} catch (IOException e) {
//				e.printStackTrace();
				System.out.println("There was a problem reading the test file. Retrying...");
				try {
					testSet.parseTo(testData);
				} catch (FileNotFoundException ef) {
//						e.printStackTrace();
						System.out.println("Test file not found. Please input a correct file.");
						System.exit(1);
				} catch (IOException ef) {
//						e.printStackTrace();
						System.out.println("There was a problem reading the test file.");
						System.exit(1);
				} catch (ParseIntCSVException ef) {
//							e.printStackTrace();
							System.out.println("Values in test instances file must be an integer number.");
							System.exit(1);
				} catch (ParseException ef) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
					System.out.println("Some lines in your test file don't have the same number of elements. Please input a correct file.");
					System.exit(1);
				}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.exit(1);
		} catch (ParseIntCSVException e) {
//			e.printStackTrace();
			System.out.println("Values in test instances file must be an integer number.");
			System.exit(1);
		}
		
		
		System.out.println("Testing the classifier:");
		if (BNClass != null) BNClass.Test(testData);

		testData.printInstancesWithClass();



		
		
		
	}
	
	
	//MUDANCAS
	//constructor nao chama parse - parse agora e 
}
