package server;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class MyFile implements Serializable {
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MyFile.class);

	public int id;
	int segment;
	String fileName=null;
	ArrayList<Byte> data=new ArrayList<Byte>();

	
	/**
	 * 
	 * @param myFile
	 * @return false if could not add myfile
	 */
	private boolean addMyFile(MyFile myFile){
		if(id != myFile.id) return false;
		if(myFile.size() == 0) return false;
		for (Byte b : myFile.data) {
			this.data.add(b);
		}
		return true;
	}
	
	public ArrayList<MyFile> split(int partSize ){
		ArrayList<MyFile> result= new ArrayList<MyFile>(partSize);
		int index=0;
		int partSegment=0;
		while ( index < data.size()) {
			ArrayList<Byte> byteList=new ArrayList<Byte>(size()+1);
			for (int j = 0; j < partSize; j++) {
				byteList.add(data.get(index++));
				if(index == data.size())break;
			}
			result.add(new MyFile(id, partSegment++,fileName, byteList));
		}
		return result;
	}
	public ArrayList<MyFile> split(int numberOfSplits,String test ){
		int partSize= data.size()/numberOfSplits +1;
		logger.info("part size:"+ partSize +" totalsize:"+data.size());
		return split(partSize);
	}
	
	public int size(){
		return data.size();
	}
	
	public void readFromFile(File file, String fileName){
		this.fileName=fileName;
		data=new ArrayList<Byte>();
		try {
			FileInputStream fin=new FileInputStream(file);
			byte b=(byte)fin.read();
			while(b != -1){
				data.add(b);
				b=(byte)fin.read();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void readFromFile(String absPath){				
		File file=new File(absPath);
		readFromFile(file, file.getName());
	}

	public byte[] getData(){
	    byte[] result=new byte[data.size()];
	    for (int i = 0; i < result.length; i++) {
			result[i]=data.get(i);
		}
		return result;

	}
	@Override
	public String toString() {
		StringBuffer result=new StringBuffer("id: "+id);
		result.append("\nsegment :"+segment);
		result.append("\nfileName :"+fileName);
		result.append("\nsize: "+data.size());
		if(data.size() < 5000){
			result.append("\ndata[]:");
			result.append(new String(getData()));

		}

		return result.toString();
	}

	private void writeObject(ObjectOutputStream s)	throws IOException {
		// save the dimension
		s.writeInt(id);
		s.writeInt(segment);
		s.writeUTF(fileName);
		s.writeInt(data.size());
		s.write(getData());
	}

	private void readObject(ObjectInputStream s)throws IOException, ClassNotFoundException  {
		id=s.readInt();
		segment=s.readInt();
		fileName=s.readUTF();
		int size=s.readInt();
		data=new ArrayList<Byte>(size);		
		for (int i = 0; i < size; i++) {
			data.add(s.readByte());
		}
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {


			MyFile myFile=new MyFile(777);
			myFile.readFromFile("d://test.txt");
			System.out.println(myFile);

			FileOutputStream fos = new FileOutputStream("ispace/myFile.out");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(myFile);
			oos.flush();
			oos.close();
			fos.close();

			//MyFile myFile2=new MyFile(666);

			FileInputStream fis = new FileInputStream("ispace/myFile.out");
			ObjectInputStream ois = new ObjectInputStream(fis);
			// read the objects from the input stream (the file name.out)
			MyFile mf = (MyFile) ois.readObject();

			System.out.println("\nReade\n"+mf);

			ois.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//testReadFromFile();		
	}

	public MyFile(int id, int segmet, String fileName){
		this(id, segmet, fileName, new ArrayList<Byte>());
	}
	public MyFile(int id, int segment, String fileName, byte[] data) {
		this(id,segment,fileName,Arrays.asList(new Byte("1")) );
	}
	public MyFile(int id, int segment, String fileName, List<Byte> data) {
		this.id = id;
		this.segment=segment;
		this.fileName = fileName;
		this.data=new ArrayList<Byte>(data);
	}
	public MyFile(int id) {
		this(id,0,"filename",new ArrayList<Byte>());
	}
	/**
	 * get list of MyFile segments, sort them then merge
	 * @param myFileSegments
	 */
	public MyFile(List<MyFile> myFileSegments){
		TreeMap<Integer, MyFile> sortedSegments=new TreeMap<Integer, MyFile>();
		for (MyFile mf : myFileSegments) {
			sortedSegments.put(mf.segment, mf);
		}
		
		Map.Entry<Integer, MyFile> entry=sortedSegments.firstEntry();
		this.id= entry.getValue().id;
		this.fileName=entry.getValue().fileName;
		this.segment=entry.getValue().segment;
		
		for (MyFile mf : sortedSegments.values()) {
			this.addMyFile(mf);
		}
		
	}
	
}
