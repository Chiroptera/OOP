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
	protected int colSize;
	protected int rowSize;
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
	
	
	public LinkedList<String[]> getParsedData(){
		
		return parsedDataList;
	}
	
	public int getcolSize(){
		
		return colSize;
	}

	public void parseTo(DataSet data) throws IOException, Exception, FileNotFoundException{
		
			/*************************************************************************************/
			/* this was outside try/catch block before*/
//			this.data = data;
			BufferedReader br = null;
			String line;
			String cvsSplitBy = ",";
			
//			try {
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
					//TO DO: VERIFICAR QUE TODAS AS LINHAS TEM O MESMO TAMANHO(NCOLUMS)
					lineparse = line.split(cvsSplitBy); /* split string by commas */
					parsedDataList.add(lineparse);
					middleLine(lineparse, data);
					
					rowSize ++;
					

				}
				
//				lastLine(); (?)
				
			//TO DO: BETTER TREATMENT TO EXCEPTION
//			} catch (FileNotFoundException e) {
//					e.printStackTrace();
//			} catch (IOException e) {
//					e.printStackTrace();
//			} finally {
//				if (br != null) {
//					try {
//						br.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}
			/***********************************************************************************/
			
	}
	
	//by default the first line is ignored
	//can be extended to not ignore first line, just by redefining method to do something
	public void firstLine(String[] line, DataSet data){

	}
	
	public void middleLine(String[] line, DataSet data) throws Exception{

	}

	@Override
	public Iterator<String[]> iterator() {
		// TODO Auto-generated method stub
		return parsedDataList.iterator();
	}
}


