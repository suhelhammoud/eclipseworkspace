package server;

import org.apache.log4j.Logger;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class Connect extends Thread {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Connect.class);

	private Socket client = null;
	String dirName;
	
	//added by suhel
	ObjectOutputStream oos = null;
	ObjectInputStream ois = null;

	public Connect(Socket clientSocket,String dirName) {
		this.client = clientSocket;
		this.dirName=dirName;
		try {
			ois = new ObjectInputStream(clientSocket.getInputStream());
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch(Exception e1) {
			e1.printStackTrace();
			try {
				client.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
			return;
		}
		this.start();
	}
}

class DownloadConnect extends Connect{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DownloadConnect.class);

	public DownloadConnect(Socket clientSocket,String dirName) {
		super(clientSocket, dirName);
	}

	@Override
	public void run() {
		
		try {
			String fileID= ois.readUTF();
			logger.info("request to download file:"+fileID);
			MyFile myFile= ISpace.getLocalMyFileSegment(fileID,dirName);
			oos.writeObject(myFile);
			oos.flush();

			logger.info("end msg"+ois.readUTF());
			ois.close();
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

class UploadConnect extends Connect{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UploadConnect.class);
	
	
	
	public UploadConnect(Socket clientSocket,String dirName) {
		super(clientSocket, dirName);
	}

	@Override
	public void run() {
		MyFile myFile=null;
		try{
			myFile = (MyFile) ois.readObject();
			oos.writeUTF("complete");
			oos.flush();

			logger.info("upload myFile "+ myFile.id +":"+myFile.segment);
			ISpace.saveLocalMyFileSegment(myFile, dirName);
			
			oos.close();
			ois.close();
		} catch(Exception e) {
			logger.error(myFile);
			e.printStackTrace();
		}
	}	
}