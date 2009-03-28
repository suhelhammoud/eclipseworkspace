/**
 * 
 * create those directories, control, downloading, download, 
 * scanning, scanned, zipping, zipped, emailing, and emailed
 * 
 */
import org.apache.log4j.Logger;

import sun.jkernel.DownloadManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Control {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Control.class);

	/**
	 * @param args
	 */
	
	public static void initDirectories(){
		String[] dirs ={"control","downloading","download",
						"scanning","scanned","zipping","zipped",
						"emailing","emailed"};
		for (String fileName : dirs) {
			File file=new File(fileName);
			if(! file.exists() || ! file.isDirectory())
				file.mkdir();	
		}
	}

	public static void doScanning(int pause) throws Exception{
		List<String> jobs=new ArrayList<String>();

		String[] files=new File("download").list();
		for (String file : files) {
			if( new File(file).isFile() || new File(file+"/error").exists())
				continue;
			jobs.add(file);
		}

		for (String file : jobs) {
			
			logger.info("scanning dir "+ file);

			String ebook="scanning/"+file+"/ebook.pdc";
			String info="scanning/"+file+"/info.txt";
			String jpg="scanning/"+file+"/jpg";

			//move to scanning dir
			boolean b=new File("download/"+file).renameTo(new File("scanning/"+file));
			if(! b){
				new File("download/"+file+"/error").mkdir();
				continue;
			}//moving was successful
			
			
			new File(jpg).mkdir();

			Map<String,String> infoMap=readInfoFile(info);
			int fromPage=1,toPage=3;
			try {
				fromPage=Integer.parseInt(infoMap.get("fromPage"));
				toPage=Integer.parseInt(infoMap.get("toPage"));
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("fromPage or toPage are not working for id:"+ infoMap.get("id"));
			}

			b =new Scanner().doWork(ebook,jpg,fromPage, toPage, pause);
			if (! b){
				continue;
			}else{
				String suffix="";
				if ( new File("scanned/"+file).exists()) suffix="_"+System.nanoTime();
				b=new File("scanning/"+file).renameTo(new File("scanned/"+file+suffix));
				if(! b){
					copyDirectory(new File("scanning/"+file),new File("scanned/"+file+suffix));
				}
			}
		}
	}

	public static void doZipping() throws IOException{
		List<String> jobs=new ArrayList<String>();

		String[] files=new File("scanned").list();
		for (String file : files) {
			if( new File(file).isFile()  || new File(file+"/error").exists() )
				continue;
			jobs.add(file);
		}

		for (String file : jobs) {
			logger.info("zipping with dir "+ file);

			String jpg="zipping/"+file+"/jpg";
			String zip="zipping/"+file+"/zip";
			//move to working dir
			boolean b=new File("scanned/"+file).renameTo(new File("zipping/"+file));
			if(! b){
				new File("scanned/"+file+"/error").mkdir();
				continue;
			}

			List<String> zippedFiles=Zipper.zipAndSplit(jpg, zip, 9000000);

			if(zippedFiles.size() ==0){
				logger.error("no file is zipped");
				continue;
			}else{
				String suffix="";
				if ( new File("zipped/"+file).exists()) suffix="_"+System.nanoTime();
				b =new File("zipping/"+file).renameTo(new File("zipped/"+file+suffix));

				if(! b){
					copyDirectory(new File("zipping/"+file),new File("zipped/"+file+suffix));
				}
			}

		}
	}

	public static void doEmailing() throws Exception{
		List<String> jobs=new ArrayList<String>();

		String[] files=new File("zipped").list();
		for (String file : files) {
			if( new File(file).isFile()  || new File(file+"/error").exists() )
				continue;
			jobs.add(file);
		}

		for (String file : jobs) {
			logger.info("emailing with dir "+ file);

			String info="emailing/"+file+"/info.txt";
			String zip="emailing/"+file+"/zip";
			
			//move to working dir
			 boolean b =new File("zipped/"+file).renameTo(new File("emailing/"+file));
			if(! b  ){
				new File("zipped/"+file+"/error").mkdir();
				continue;
			}

			Map<String,String> infoMap=readInfoFile(info);
			String fromEmail=infoMap.get("from");
			String subject=infoMap.get("subject");
			
			String[] zippedFiles= new File(zip).list();
			for (int i = 0; i < zippedFiles.length; i++) {
				try {
					String attachfile=zip+"/"+zippedFiles[i];
					
					new Emailer().sendMail(fromEmail, subject+" part "+ i,
												printInfoMap(infoMap)+"zip part : "+ i,
												"pdc.to.jpg@gmail.com",attachfile);					
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("exit emailing parts");
					return;
				}
			}
			String suffix="";
			if ( new File("emailed/"+file).exists()) suffix="_"+System.nanoTime();
			b =new File("emailing/"+file).renameTo(new File("emailed/"+file+suffix));

			if(! b){
				copyDirectory(new File("emailing/"+file),new File("emailed/"+file+suffix));
				logger.error("could not move file "+file);
			}
		}
	}

	public static void threadScanning(final int delay, final int pause) {

		new Thread(){
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(delay);
					} catch (Exception e) {
						logger.error("can't sleep");
					}
					try {
						doScanning(pause);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();

	}

	public static void threadZipping(final int delay) {
		new Thread(){
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(delay);
					} catch (Exception e) {
						logger.error("can't sleep");
					}
					try {
						doZipping();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	public static void threadEmailing(final int delay) {
		new Thread(){
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(delay);
					} catch (Exception e) {
						logger.error("can't sleep");
					}
					try {
						doEmailing();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	
	public static Map<String,String> readInfoFile(String filename){
		Map<String,String> result=new HashMap<String, String>();
		StringBuffer sb=new StringBuffer();
		try {

			BufferedReader in= new BufferedReader(new FileReader(filename));
			//read the file lines withoud the comments to the lines list
			String s;
			while( (s=in.readLine()) != null)  {

				String[] parts = s.trim().split(":");
				//date is not working with this
				result.put(parts[0].trim(), parts[1].trim());
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, String>();
		}
		return result;
	}
	// Copies all files under srcDir to dstDir.
	// If dstDir does not exist, it will be created.
	public static void copyDirectory(File srcDir, File dstDir) throws IOException {
		if (srcDir.isDirectory()) {
			if (!dstDir.exists()) {
				dstDir.mkdir();
			}

			String[] children = srcDir.list();
			for (int i=0; i<children.length; i++) {
				copyDirectory(new File(srcDir, children[i]),
						new File(dstDir, children[i]));
			}
		} else {
			// This method is implemented in e1071 Copying a File
			copyFile(srcDir, dstDir);
		}
	}
	// Copies src file to dst file.
	// If the dst file does not exist, it is created
	static void copyFile(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}


	static String printInfoMap(Map<String,String> map){
		StringBuffer result=new StringBuffer("\n    www.nadyelfikr.com\n");
		result.append("\nid : "+map.get("id"));
		result.append("\nsubject : "+map.get("subject"));
		result.append("\nfrom page : "+map.get("fromPage"));
		result.append("\nto page : "+map.get("toPage"));
		result.append("\nto page : "+map.get("toPage"));
		result.append("\ndate received : "+map.get("date"));
		result.append("\nebook file : "+map.get("fileName"));
		result.append("\n");
		
		return result.toString();
	}
	public static void main(String[] args) {
		initDirectories();
		threadScanning(17000,1000);
		threadZipping(13000);
		threadEmailing(19000);	
	}


}
