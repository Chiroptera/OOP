//TO DO
//VERFY

package parseCSV;

import dataset.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class ParseCSV implements Iterable<String[]>{
	
	protected LinkedList<String[]> parsedDataList;
	protected int colSize=0;
	protected int rowSize=0;
//	protected DataSet data;
	protected FileReader toParse;
	
	ParseCSV(){
		
		parsedDataList = new LinkedList<String[]>();
	}
	
	public ParseCSV(String filename){
		
		parsedDataList = new LinkedList<String[]>();
	
		try {
			toParse = new FileReader(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	/**
	 * Returns a list with all the parsed data (each list entry has a String array).
	 * @return LinkedList
	 */
	public LinkedList<String[]> getParsedData(){
		
		return parsedDataList;
	}
	
	/**
	 * Returns the number of elements in each entry.
	 * @return int
	 */
	public int getcolSize(){
		
		return colSize;
	}

	/**
	 * Tries to open a file, parse it to the data list and saves the number of instances.
	 * Calls the <b>firstline()<b> and <b>middleline()<b> methods that do nothing.
	 * The DataSet is passed to mentioned methods, and the only thing done to it in this method
	 * is set the number of instances.
	 * @param data DataSet
	 * @throws IOException Error reading the file.
	 * @throws Exception Inconsistency of number of elements in one of the file lines.
	 * @throws FileNotFoundException File not found.
	 */
	public void parseTo(DataSet data) throws IOException, Exception, FileNotFoundException{
		
			/*************************************************************************************/
			/* this was outside try/catch block before*/
			BufferedReader br = null;
			String line;
			String cvsSplitBy = ",";
			
			br = new BufferedReader(toParse);
			
			String[] lineparse;

			/* if first line not null */
			if((line = br.readLine()) != null){

				lineparse = line.split(cvsSplitBy); /* split string by commas */
				colSize = lineparse.length; /* number of variables + 1 (class variable)*/
				firstLine(lineparse,data);				
			}
				
			/* while there are other lines*/
			while ((line = br.readLine()) != null) {

			    // use comma as separator
				lineparse = line.split(cvsSplitBy); /* split string by commas */
				parsedDataList.add(lineparse);
				middleLine(lineparse, data);
				
				rowSize++;
			}
			data.setnInstances(rowSize);
				
			/***********************************************************************************/
			
	}
	
	/**
	 * Does nothing.
	 * @param line
	 * @param data
	 */
	public void firstLine(String[] line, DataSet data){

	}
	
	/**
	 * Does nothing.
	 * @param line
	 * @param data
	 * @throws Exception
	 */
	public void middleLine(String[] line, DataSet data) throws Exception{

		
	}

	@Override
	public Iterator<String[]> iterator() {
		// TODO Auto-generated method stub
		return parsedDataList.iterator();
	}
}


