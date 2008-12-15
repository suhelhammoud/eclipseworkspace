package server;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RunClient {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RunClient.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testDownloadMyFile();
		//testUploadFile();
	}
	public static void testUploadFile() {
		
		MyFile myFile=new MyFile(6666);
		
		myFile.readFromFile("input/test_00.txt");
		logger.info("read myFile: "+ myFile);		
		
		ISpace.uploadMyFile( myFile);
		
	}
	
	public static void testDownloadMyFile(){
		MyFile myFile = ISpace.downLoadMyFile("6666");
		logger.info("Download Myfile "+ myFile.id+ ": "+ myFile);
	}
	
	
	
}
