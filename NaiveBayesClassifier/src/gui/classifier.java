package gui;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;

import dataset.DataSet;
import dataset.ElapsedTime;
import dataset.Main;
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

public class classifier {
	
	private boolean trained = false;
	
	private String trainfile=null,testfile=null,score;
	
	private JLabel lblNewLabel,lblNewLabel_1;
	
	private DefaultListModel listModel,listModelScore;
	
	private JList list, listScore;

	private JFrame frame;
	private JButton btnGo;
	private JTextField textField;


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
			    FileFilter filter = new FileNameExtensionFilter("c files", "c");
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
			    FileFilter filter = new FileNameExtensionFilter("c files", "c");
			    fileopen.addChoosableFileFilter(filter);

			    int ret = fileopen.showDialog(null, "Open file");

			    if (ret == JFileChooser.APPROVE_OPTION) {
			      File file = fileopen.getSelectedFile();
			      testfile = file.toString();
			      String[] toShow = testfile.split("/");
			      lblNewLabel_1.setText(toShow[toShow.length-1]);
			      listModel.remove(0);
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


		list = new JList(listModel);
		list.setBorder(new CompoundBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)), null));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBounds(12, 210, 155, 174);
		frame.getContentPane().add(list);
		
		btnGo = new JButton("Train");
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main jg = new Main();
				String[] args = new String[]{trainfile,testfile,score};
				
				if(trainfile==null) JOptionPane.showMessageDialog(frame, "You must provide a train file.");
				
				/* select score in list*/
				if(listScore.getSelectedIndex() == -1) JOptionPane.showMessageDialog(frame, "You must choose a score (LL or MDL).");
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
					
					ElapsedTime time = new ElapsedTime();
					time.start();
					ParseLearnCSV trainSet = new ParseLearnCSV(args[0]);
					TrainDataSet trainData = new TrainDataSet();
					trainSet.parse(trainData);
					NaiveBayesClassification BNClass = new NaiveBayesClassification(args[2]);
					BNClass.Train(trainData);
					ParseCSV testSet = new ParseCSV(args[1]);
					DataSet testData = new DataSet();
					testSet.parse(testData);
					BNClass.Test(testData);
					time.stop();
					System.out.println("Building the classifier: " + time.toString());
	

				}
			}
		});
		btnGo.setBounds(12, 111, 117, 25);
		frame.getContentPane().add(btnGo);
		
		JButton btnTest = new JButton("Test");
		btnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(trained == false) JOptionPane.showMessageDialog(frame, "You must train first.");
				if(testfile == null) if(trainfile==null) JOptionPane.showMessageDialog(frame, "You must provide a test file.");
				
			}
		});
		btnTest.setBounds(12, 148, 117, 25);
		frame.getContentPane().add(btnTest);
		
		JButton btnShowClass = new JButton("Show class");
		btnShowClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		
		
	}
}
