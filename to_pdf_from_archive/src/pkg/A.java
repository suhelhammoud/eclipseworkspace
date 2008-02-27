package pkg;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class A {
	private LogFrame logger;
	public A(LogFrame logger){
		this.logger=logger;
		init();
	}
	public String linkFile="";
	List<String> currentLinks=new ArrayList<String>();
	List<List <String >> links=new ArrayList<List<String>>();
	File file=null;
	Map<String,List<String>> map=new HashMap<String, List<String>>();

	boolean addLink(String url){
		url=url.trim();
		int index=getLinkIndex(url);
		if(index > -1)return false;
		currentLinks.add(url);
		if(! map.containsKey(url))
			map.put(url, null);

		return true;
	}


	int getLinkIndex(String url){
		for (int i = 0; i < currentLinks.size(); i++) {
			if(currentLinks.get(i).equalsIgnoreCase(url)){
				return i;
			}
		}
		return -1;
	}
	boolean deleteLink(String url){
		for (int i = 0; i < currentLinks.size(); i++) {
			if(currentLinks.get(i).equalsIgnoreCase(url)){
				currentLinks.remove(i);
				return true;
			}
		}
		return false;
	}
	List<String> getInfo(String url){
		List<String> result=map.get(url);
		if(result==null){
			result=link(url);
			if(result==null)
				deleteLink(url);
			else
				map.put(url, result);
		}
		return result;
	}

	public String getLinkFile() {
		return linkFile;
	}

	public void setLinkFile(String linkFile) {
		this.linkFile = linkFile;
	}

	void init(){
		File dir = new File(".");

		// This filter only returns directories
		FileFilter fileFilter = new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		};

		File[] files = dir.listFiles(fileFilter);
		String[] names=new String[files.length];
		int dd=-1 ,uz=-1 ,pf =-1;
		for (int i = 0; i < files.length; i++) {
			names[i]=files[i].getName();
			if( names[i].equals( "downloads")) dd=i;
			if( names[i].equals( "unzipped")) uz=i;
			if( names[i].equals( "pdf")) pf=i;
		}
		if(dd==-1)log("creat downloads :"+ (new File("downloads")).mkdirs());
		if(uz==-1)log("creat unzipped  :"+ (new File("unzipped")).mkdirs());
		if(pf==-1)log("creat pdf :"+ (new File("pdf")).mkdirs());

	}

	static String getFtpDir(String txt){
		if((txt==null) && (txt.equals("null"))|| (txt.equals("n/a"))) return null;
		String[] arr=txt.split("ftp");
		if(arr==null || arr.length==1)return null;
		String ftpLink=arr[1].trim().split("FTP")[0].trim();
		ftpLink="ftp"+ftpLink.substring(0, ftpLink.length()-2).trim();
		return ftpLink;
	}

	static String getFtpFile(String ftpDir){
		String result="";
		String[] arr=ftpDir.split("/");
		String filename=arr[arr.length-1].trim();
		result=ftpDir+"/"+filename+"_PTIFF.zip";

		return result;
	}

	public static String getMetaDataFile(String ftpDir){
		String result="";
		String[] arr=ftpDir.split("/");
		String filename=arr[arr.length-1].trim();
		result=ftpDir+"/"+filename+"_metadata.xml";

		return result;
	}

	public static String downloadMetaData(String urlstirng){
		OutputStream out = null;
		URLConnection conn = null;
		InputStream  in = null;
		ByteArrayOutputStream resutl=new ByteArrayOutputStream();
		String r="";
		try {
			URL url = new URL(urlstirng);

			out = new BufferedOutputStream(resutl);
			conn = url.openConnection();
			in = conn.getInputStream();
			byte[] buffer = new byte[1024];
			int numRead;
			long numWritten = 0;
			while ((numRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, numRead);
				numWritten += numRead;
			}
			out.flush();
			r=resutl.toString("UTF-8");
			//System.out.println("result"+r);
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException ioe) {
			}
		}

		return r;

	}

	public static String getURLContent(String link)  {
		StringBuffer result=new StringBuffer("n/a");
		BufferedReader buffer=null;      
		String linie=null; 
		URLConnection URLcon=null; 
		URL url=null; 
		InputStream IS=null; 
		try { 

			url=new URL(link);               
			URLcon = url.openConnection(); 

			IS=URLcon.getInputStream(); 
			buffer=new BufferedReader(new InputStreamReader(IS));
			result=new StringBuffer();
			while((linie=buffer.readLine())!=null){                
				result.append(linie);
				//System.out.println(linie); 
			}                
			buffer.close(); 
		}catch(MalformedURLException e)
		{System.out.println("Eroare:"+e.getMessage());
		}catch(IOException e) {
			System.out.println("Eroare:"+e.getMessage());
		}   
		return result.toString();
	} 

	public static String getTag(String txt, String startTag, String endTag){

		int start = txt.indexOf(startTag) + startTag.length();
		if(start==-1)return "n/a";
		int end = txt.indexOf(endTag);
		if(end== -1)return "n/a";
		String result = txt.substring(start, end);
		if(result.length()==0)return "n/a";
		return result;
	}

	public static String getTag(String txt,String startTag){
		String start="<"+startTag+">";
		String end="</"+startTag+">";
		return getTag(txt, start,end);
	}

	static List<String> getMetaAsList(String txt){

		List<String> list=new ArrayList<String>();
		list.add(getTag(txt, "ID"));
		list.add(getTag(txt, "name"));
		list.add(getTag(txt, "title"));
		list.add(getTag(txt, "author"));
		list.add(getTag(txt, "publisher"));
		list.add( getTag(txt, "publication_date"));
		list.add( getTag(txt, "pages"));
		list.add( getTag(txt, "language_code"));
		list.add( getTag(txt, "color"));
		list.add( getTag(txt, "scanned_pages"));
		list.add( getTag(txt, "keywords"));
		list.add( getTag(txt, "scan_digitizer"));
		list.add( getTag(txt, "condition"));
		list.add( getTag(txt, "process_digitizer"));
		list.add( getTag(txt, "ocr_digitizer"));
		list.add( getTag(txt, "copyright"));
//		list.add( getTag(txt, ""));
		return list;
	}


	public static List<String> urlLinks(String fileName)throws Exception{
		File file=new File(fileName);
		return urlLinks(file);
	}
	public static List<String> urlLinks(File file)throws Exception{
		BufferedReader inputStream = null;
		List<String> links=new ArrayList<String>();
		int counter=0;
		int line=0;

		try {
			inputStream = new BufferedReader(new FileReader(file));
			String l;
			while ((l = inputStream.readLine()) != null) {
				l=l.trim();
				if(! l.startsWith("http://www.ar") )continue;
				links.add(l);
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return links;
	}

	public static List<String> extractLinks(String txt)throws Exception{
		BufferedReader inputStream = null;
		List<String> links=new ArrayList<String>();
		int counter=0;
		int line=0;

		try {
			inputStream = new BufferedReader( new StringReader(txt));
			String l;
			while ((l = inputStream.readLine()) != null) {
				l=l.trim();
				if(! l.startsWith("http://www.ar") )continue;
				links.add(l);
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return links;
	}

	public static List<List<String>> getLinks()throws IOException{
		List<List<String>> data=new ArrayList<List<String>>();
		int counter=1;
		try {
			List<String> links=urlLinks("data/links.txt");
			for (String link : links) {
				List<String> item=link(link);
				data.add(item);	
//				log("get link :"+(counter++));
			}

		}catch(Exception e){

		}
		return data;
	}
	static void save(List<List<String>> items)throws IOException{

		File file=null;
		BufferedWriter out = new BufferedWriter(
				new OutputStreamWriter(
						new FileOutputStream(new File("out.txt")), "Unicode"));
		int counter=1;
		for (List<String> item : items) {
			for (String iter : item) {
				out.write(iter+"\n");
			}
			out.write("\n");
//			log("counter :"+(counter++));
		}
		out.close();
	}

	static List<String> link(String url){
		List<String> result=null;
		//http://www.archive....
		String content= getURLContent(url);
		if(content.equals("n/a") || content==null)return null;
		String ftpdir= getFtpDir(content);
		if(ftpdir==null)return null;
		String metadataFile= getMetaDataFile(ftpdir);
		String txt= downloadMetaData(metadataFile);
		result=getMetaAsList(txt);
		result.add(0, ftpdir);//ftp://us.archive.
		result.add(0,url);
		return result;
	}
	/**
	 * 
	 * @param fileName
	 * downloads directory
	 * unzipped directory
	 */
	public  void unzip(String fileName){
		final int BUFFER = 2048;
		try {
			BufferedOutputStream dest = null;
			BufferedInputStream is = null;
			ZipEntry entry;
			ZipFile zipfile = new ZipFile("downloads/"+fileName+".zip");

			Enumeration e = zipfile.entries();
			while(e.hasMoreElements()) {
				entry = (ZipEntry) e.nextElement();
				log("Extracting: " +entry);
				is = new BufferedInputStream
				(zipfile.getInputStream(entry));
				int count;
				byte data[] = new byte[BUFFER];
				(new File("unzipped/"+fileName)).mkdirs();
				FileOutputStream fos = new 
				FileOutputStream("unzipped/"+fileName+"/"+entry.getName());
				dest = new 
				BufferedOutputStream(fos, BUFFER);
				while ((count = is.read(data, 0, BUFFER)) 
						!= -1) {
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
				is.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	static public boolean deleteDirectory(File path) {
		if( path.exists() ) {
			File[] files = path.listFiles();
			for(int i=0; i<files.length; i++) {
				if(files[i].isDirectory()) {
					deleteDirectory(files[i]);
				}
				else {
					files[i].delete();
				}
			}
		}
		return( path.delete() );
	}

	void tif2pdf_old(String fileName ) throws IOException {
		Runtime runtime = Runtime.getRuntime();
		Process proc = runtime.exec( 
				"tiff2pdf/tiff2pdf -i unzipped/"+fileName+"/*.tif -o pdf/"+fileName+".pdf");
		try {
			if (proc.waitFor() != 0) {
				System.err.println("exit value = " +proc.exitValue());
			}else 
				log("Generate pdf :"+fileName+".pdf");
		}
		catch (InterruptedException e) {
			System.err.println(e);
		}
	}
	static String getFileName(String url){
		String result=url.split("/details/")[1].trim();
		return result;
	}

	public  long download(String ftpFile, String localFileName) {
		OutputStream out = null;
		URLConnection conn = null;
		InputStream  in = null;
		long numWritten = 0;

		try {
			URL url = new URL(ftpFile);
			out = new BufferedOutputStream(
					new FileOutputStream("downloads/"+localFileName+".zip"));
			conn = url.openConnection();
			in = conn.getInputStream();
			byte[] buffer = new byte[1024];
			int numRead;
			while ((numRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, numRead);
				numWritten += numRead;
			}
			log(localFileName + "\t" + numWritten);
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException ioe) {
			}
		}
		return numWritten;
	}

	public String processItem(String url){
		String fileName=getFileName(url);
		log(fileName);


		String content= getURLContent(url);

		String ftpDir= getFtpDir(content);
		log(ftpDir);

		String ftpFile=getFtpFile(ftpDir);
		log(ftpFile);

		log("going to download"+ ftpFile);
		download(ftpFile, fileName);

		log("going to unzip file" + fileName);
		unzip(fileName);

		log("going to pdf file: "+ fileName);

		tif2pdf(fileName);



		log("trying to delete unzipped");
		File dirfile=new File("unzipped/"+fileName);
		deleteDirectory(dirfile);

//		log("trying to delete zipp file");
//		File file=new File("data/downloads/"+fileName+".zip");
//		file.delete();

		return "ok";
	}



	public void tif2pdf(String fileName){
		String cmd="tiff2pdf/tiff2pdf -i unzipped/"+fileName+"/*.tif -o pdf/"+fileName+".pdf";
		try{

			Runtime rt = Runtime.getRuntime();
			log("excetuting");
			Process proc = rt.exec(cmd);
			// any error message?
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");            

			// any output?
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");

			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			// any error???
			int exitVal = proc.waitFor();
			log("ExitValue: " + exitVal);        
		} catch (Throwable t)
		{
			t.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			A a=new A(null);
			a.init();
			List<String> list=urlLinks("in.txt");
			a.log("Number of Links"+ list.size());
			for (String item : list) {
				a.log("Procces Item "+item);
				a.processItem(item);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	void  log(String str){
		if(logger!= null)
			logger.log(str);
		else
			System.out.println(str);
	}
}

class StreamGobbler extends Thread
{
	InputStream is;
	String type;

	StreamGobbler(InputStream is, String type)
	{
		this.is = is;
		this.type = type;
	}

	public void run()
	{
		try
		{
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line=null;
			while ( (line = br.readLine()) != null)
				line+=" ";
			//System.out.println(type + ">" + line);    
		} catch (IOException ioe)
		{
			ioe.printStackTrace();  
		}
	}
}