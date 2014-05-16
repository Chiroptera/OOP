package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;

import dataset.DataSet;
import dataset.ElapsedTime;
import dataset.Main;
import dataset.TestDataSet;
import dataset.TrainDataSet;

import javax.swing.JComboBox;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.ListModel;

import bayesClassification.*;
import parseCSV.*;
import dataset.*;
import javax.swing.JScrollBar;

public class classifier {
	
	private boolean trained = false;
	
	private String trainfile=null,testfile=null,score=null;
	
	private JLabel lblNewLabel,lblNewLabel_1;
	
	private DefaultListModel listModel,listModelScore;
	
	private JList list, listScore;

	private JFrame frame;
	private JButton btnGo;
	private JTextField textField;
	
	int[] instanceClasses;
	TestDataSet globalTestData;
	
	ElapsedTime time;
	NaiveBayesClassification BNClass = null;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					classifier window = new classifier();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public classifier() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 434);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("Train file");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    JFileChooser fileopen = new JFileChooser();
			    FileFilter filter = new FileNameExtensionFilter("CSV files", ".csv");
			    fileopen.addChoosableFileFilter(filter);

			    int ret = fileopen.showDialog(null, "Open file");

			    if (ret == JFileChooser.APPROVE_OPTION) {
			      File file = fileopen.getSelectedFile();
			      trainfile = file.toString();
			      String[] toShow = trainfile.split("/");
			      lblNewLabel.setText(toShow[toShow.length-1]);
			    }
			}
		});
		btnNewButton.setBounds(12, 22, 117, 25);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Test File");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			    JFileChooser fileopen = new JFileChooser();
			    FileFilter filter = new FileNameExtensionFilter("CSV files", ".csv");
			    fileopen.addChoosableFileFilter(filter);

			    int ret = fileopen.showDialog(null, "Open file");

			    if (ret == JFileChooser.APPROVE_OPTION) {
			      File file = fileopen.getSelectedFile();
			      testfile = file.toString();
			      String[] toShow = testfile.split("/");
			      lblNewLabel_1.setText(toShow[toShow.length-1]);
			    }
			}
		});
		btnNewButton_1.setBounds(12, 59, 117, 25);
		frame.getContentPane().add(btnNewButton_1);
		
		lblNewLabel = new JLabel("No file chosen");
		lblNewLabel.setBounds(147, 27, 269, 15);
		frame.getContentPane().add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("No File Chosen");
		lblNewLabel_1.setBounds(147, 64, 269, 15);
		frame.getContentPane().add(lblNewLabel_1);
		
		
		listModel = new DefaultListModel();
		listModel.addElement("Jane Doe");
		listModel.addElement("John Smith");
		listModel.addElement("Kathy Green");

		
		btnGo = new JButton("Train");
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(trainfile==null) JOptionPane.showMessageDialog(frame, "You must provide a train file.");
				
				/* select score in list*/
				else if(listScore.getSelectedIndex() == -1) JOptionPane.showMessageDialog(frame, "You must choose a score (LL or MDL).");
				else{
					switch(listScore.getSelectedIndex()){
						case -1:{
							
							break;
						}
						case 0:{
							score = "LL";
							break;
						}
						case 1:{
							score = "MDL";
							break;
						}
					}
					
					/**************************************************************************************/
					
					time = new ElapsedTime();
					time.start();
					
					//nao convem chamar metodos nao privados/finais no constructor
					//http://www.javaspecialists.eu/archive/Issue210.html
					
					ParseLearnCSV trainSet = new ParseLearnCSV(trainfile);
					
					TrainDataSet trainData = new TrainDataSet();
					
					boolean trainfileOpened = true;
					
					try {
						trainSet.parseTo(trainData);

					} catch (FileNotFoundException ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(frame,"Train file not found. Please input a correct file.");
							trainfileOpened = false;
					} catch (IOException ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(frame,"There was a problem reading the train file. Retrying...");
							try {
								trainSet.parseTo(trainData);
							} catch (FileNotFoundException ef) {
									ex.printStackTrace();
									JOptionPane.showMessageDialog(frame, "Train file not found. Please input a correct file.");
									trainfileOpened = false;	
							} catch (IOException ef) {
									ex.printStackTrace();
									JOptionPane.showMessageDialog(frame, "There was a problem reading the train file.");
									trainfileOpened = false;
							} catch (Exception ef) {
								// TODO Auto-generated catch block
								ex.printStackTrace();
								JOptionPane.showMessageDialog(frame, "Some lines in your train file don't have the same number of elements. Please input a correct file.");
								trainfileOpened = false;
							}
					} catch (Exception ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
						JOptionPane.showMessageDialog(frame, "Some lines in your train file don't have the same number of elements. Please input a correct file.");
						trainfileOpened = false;
					}
					
					if (trainfileOpened){
						BNClass = null;
						try {
							BNClass = new NaiveBayesClassification(score);
							
							/* este ctach nunca vai acontecer pois está a ser feita uma verificação no inicio*/
						} catch (NBCException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							}
						
						BNClass.Train(trainData);
						time.stop();
						trained = true;

					}
					
					/****************************************************************************************/
					

	

				}
			}
		});
		btnGo.setBounds(12, 111, 117, 25);
		frame.getContentPane().add(btnGo);
		
		JButton btnTest = new JButton("Test");
		btnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(trained == false) JOptionPane.showMessageDialog(frame, "You must train first.");
				else if(testfile == null) if(trainfile==null) JOptionPane.showMessageDialog(frame, "You must provide a test file.");
				else{
					System.out.println("Creating ParseTestCSV...");
					ParseTestCSV testSet = new ParseTestCSV(testfile);
					System.out.println("Creating TestDataSet...");
					TestDataSet testData = new TestDataSet();
					boolean parsedFile = true;
					
					try {
						System.out.println("Parsing...");
						testSet.parseTo(testData);

					} catch (FileNotFoundException ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(frame, "Test file not found. Please input a correct file.");
							parsedFile = false;
					} catch (IOException ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(frame, "There was a problem reading the test file. Retrying...");
							try {
								testSet.parseTo(testData);
							} catch (FileNotFoundException ef) {
									ex.printStackTrace();
									JOptionPane.showMessageDialog(frame, "Test file not found. Please input a correct file.");
									parsedFile = false;
							} catch (IOException ef) {
									ex.printStackTrace();
									JOptionPane.showMessageDialog(frame, "There was a problem reading the test file.");
									parsedFile = false;
							} catch (Exception ef) {
								// TODO Auto-generated catch block
								ef.printStackTrace();
								JOptionPane.showMessageDialog(frame, "Some lines in your test file don't have the same number of elements. Please input a correct file.");
								parsedFile = false;
							}
					} catch (Exception ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
						JOptionPane.showMessageDialog(frame, "Some lines in your test file don't have the same number of elements. Please input a correct file.");
						parsedFile = false;
					}
					
					if(parsedFile){
						System.out.println("Testing...");
						BNClass.Test(testData);
						testData.printInstancesWithClass();
						globalTestData = testData;
						listModel.clear();
	//					int counter=0;
	//					for (Iterator<int[]> instIter = testData.iterator();instIter.hasNext();){
	//						listModel.addElement(new String(counter++ + ". " + instIter.next().toString()));
	//					}
						
						for (int i=0;i<testData.getnInstances();i++){
							listModel.addElement(i);
						}
					}

				}
				
			}
		});
		btnTest.setBounds(12, 148, 117, 25);
		frame.getContentPane().add(btnTest);
		
		JButton btnShowClass = new JButton("Show class");
		btnShowClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listModel	.clear();
				for (int i=0;i<5000;i++){
					listModel.addElement(i);
				}
			}
		});
		btnShowClass.setBounds(197, 226, 117, 25);
		frame.getContentPane().add(btnShowClass);
		
		textField = new JTextField();
		textField.setBounds(200, 278, 114, 25);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		listModelScore = new DefaultListModel();
		listModelScore.addElement("LL");
		listModelScore.addElement("MDL");
		
		listScore = new JList(listModelScore);
		listScore.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listScore.setBorder(new CompoundBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)), null));
		listScore.setBounds(147, 114, 155, 57);
		listScore.setSelectedIndex(0);		
		frame.getContentPane().add(listScore);
		
		JPanel panel = new JPanel();
		panel.setBounds(12, 199, 161, 193);
		panel.setBorder(panel.getBorder());
		frame.getContentPane().add(panel);
		
				list = new JList(listModel);
				panel.add(list);
				list.setBorder(panel.getBorder());
				list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
				list.setVisibleRowCount(-1);
				panel.add(new JScrollPane(list));
		
		
	}
}
