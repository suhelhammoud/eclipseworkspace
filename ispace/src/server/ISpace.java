package server;

import org.apache.log4j.Logger;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ISpace extends Thread {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ISpace.class);

	public static boolean deleteDir(File dir) {
		//logger.info("deleting dir:"+dir.getAbsolutePath());
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}
	/**
	 * 
	 * @param serversIps: list of the servers address ex. 192.168.1.5:8080
	 * @param fileID as stored in the database
	 * @return MyFile instance contains all the segments sorted and merged.
	 */
	public static MyFile downLoadMyFile(List<String> serversIps,String fileID){
		List<MyFile> myFiles=new ArrayList<MyFile>();
		for (String address : serversIps) {
			myFiles.add(ISpace.downloadMyFileSegment(address, fileID));
		}
		return new MyFile(myFiles);
	}

	public static MyFile downLoadMyFile(String fileID){
		List<String> serversIps=ISpaceConfig.getList("remote_download_servers");
		return downLoadMyFile(serversIps, fileID);
	}
	public static MyFile downloadMyFileSegment(String address, String fileID){
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		MyFile myFile = null;

		Socket socket = null;

		String[] ipAddress= address.split(":");
		String serverIp=ipAddress[0];
		int serverPort=Integer.parseInt(ipAddress[1].trim());
		try {
			// open a socket connection
			socket = new Socket(serverIp, serverPort);
			// open I/O streams for objects
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			// read an object from the serve

			logger.info("send request:"+ fileID);
			oos.writeUTF(fileID);
			oos.flush();

			myFile = (MyFile)ois.readObject();
			oos.writeUTF("completed");
			logger.info("getMyFile: "+ myFile.id+" from "+ serverIp+":"+serverPort);

			oos.flush();

			oos.close();
			ois.close();

		} catch(Exception e) {
			e.printStackTrace()	 ;
		}
		return myFile;
	}

	public static MyFile getLocalMyFileSegment(String id, String dirName){
		MyFile myFile=null;
		try {

			FileInputStream fis = new FileInputStream(dirName+"/"+id);
			ObjectInputStream ois = new ObjectInputStream(fis);
			// read the objects from the input stream (the file name.out)
			myFile = (MyFile) ois.readObject();

			logger.info("Reade myFile: "+myFile.id + " from dir: "+ dirName);

			ois.close();
			fis.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return myFile;
	} 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ISpace.runServers();

	}

	public static void runServers() {
		try {
			//			File dir=new File("files");
			//			ISpace.deleteDir(dir);
			//			logger.info("delete dir:"+dir.getAbsolutePath());

			List<String> localDownloadServers= ISpaceConfig.getList("local_download_servers");
			//start download server listening
			for (int i = 0; i < localDownloadServers.size(); i++) {
				new ISpace(Integer.parseInt(localDownloadServers.get(i)),
						"download",ISpaceConfig.get("data_store")+"/"+i);
			}

			List<String> localUploadServers= ISpaceConfig.getList("local_upload_servers");
			//start upload server listening
			for (int i = 0; i < localUploadServers.size(); i++) {
				new ISpace(Integer.parseInt(localUploadServers.get(i)),
						"upload",ISpaceConfig.get("data_store")+"/"+i);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String saveLocal(MyFile myFile, String dirName){
		String result=null;
		try {

			File mydir=new File(dirName);
			if(! mydir.exists()){
				mydir.mkdirs();
				logger.info("create new dir :"+ dirName);
			}
			result=myFile.id+"_"+myFile.fileName;
			FileOutputStream fos = new FileOutputStream(dirName+"/"+result);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.write(myFile.getData());
			logger.info("Write file: "+myFile.id+" to dir: "+ dirName);
			oos.flush();
			oos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void saveLocalMyFileSegment(MyFile myFile, String dirName){
		try {

			File mydir=new File(dirName);
			if(! mydir.exists()){
				mydir.mkdirs();
				logger.info("create new dir :"+ dirName);
			}
			FileOutputStream fos = new FileOutputStream(dirName+"/"+myFile.id);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(myFile);
			logger.info("Write myFile: "+myFile.id+" to dir: "+ dirName);
			oos.flush();
			oos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get alist of serversIp addresses then split myFile and upload it to them.
	 * @param serversIps
	 * @param myFile
	 */
	public static void uploadMyFile(List<String> serversIps,MyFile myFile){
		List<MyFile> myFiles=myFile.split(serversIps.size(),"numberOfSplits");
		if(myFiles.size() != serversIps.size()){
			logger.error("myFiles size:"+ myFiles.size() +
					" should be equal to servers size:"+
					serversIps.size());
			return;
		}
		for (int i = 0; i < serversIps.size(); i++) {
			ISpace.uploadMyFileSegment(serversIps.get(i), myFiles.get(i));
		}
	}

	/**
	 * Get alist of serversIp addresses then split myFile and upload it to them.
	 * @param listOfServersFileName . "config/remote_upload_servers"
	 * @param fileID
	 * @return MyFile instance contains all the segments sorted and merged.
	 */
	public static void uploadMyFile(MyFile myFile){
		List<String> serversIps=ISpaceConfig.getList("remote_upload_servers");
		uploadMyFile(serversIps, myFile);
	}

	public static void uploadMyFileSegment(String address, MyFile myFile){
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		Socket socket = null;

		String[] ipAddress= address.split(":");
		String serverIp=ipAddress[0];
		int serverPort=Integer.parseInt(ipAddress[1].trim());
		try {
			socket = new Socket(serverIp, serverPort);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());

			oos.writeObject(myFile);
			logger.info("confirm "+ois.readUTF());
			logger.info("sendMyFile: "+ myFile.id+" to "+ serverIp+":"+serverPort);

			ois.close();
			oos.flush();
			oos.close();
			socket.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	private ServerSocket dateServer;



	private int port;
	

	private String serverType="upload";
	String dirName;

	public ISpace(int port,String serverType, String dirName) throws Exception {
		dateServer = new ServerSocket(port);
		this.port = port;
		this.serverType=serverType;
		this.dirName=dirName;

		logger.info(serverType + " Server listening on port "+port);
		this.start();
	}


	public void run() {
		while(true) {
			try {
				Socket client = dateServer.accept();
				logger.info("Server "+port+" Accepted a connection from: "+
						client.getInetAddress());

				if (serverType.equals("upload")){ 
					logger.info("Call uploadConnect with dirName:"+dirName);
					new UploadConnect(client, dirName);
				}
				else{
					logger.info("Call downloadConnect with dirName:"+dirName);
					new DownloadConnect(client, dirName);
				}
			} catch(Exception e) {}
		}
	}

}
