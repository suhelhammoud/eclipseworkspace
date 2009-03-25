
import org.apache.log4j.Logger;
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

	public static void doScanning() throws Exception{
		List<String> jobs=new ArrayList<String>();

		String[] files=new File("download").list();
		for (String file : files) {
			if( new File(file).isFile() || new File(file+"/error").exists())
				continue;
			jobs.add(file);
		}

		for (String file : jobs) {
			logger.info("working with dir "+ file);
			//String working_dir="work/";
			String ebook="work/"+file+"/ebook.pdc";
			String info="work/"+file+"/info.txt";
			String jpg="work/"+file+"/jpg";



			logger.info("scanning file "+file);
			//String currentDir="download/s"+file;
			//File job=new File("scanning-"+file.getName());
			//move to working dir
			boolean b=new File("download/"+file).renameTo(new File("work/"+file));
			if(! b){
				new File("download/"+file+"/error").mkdir();
				return;
			}

			if(! new File(ebook).exists())continue;
			if(! new File(info).exists())continue;
			//if(new File(jpg).exists())continue;



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

			boolean success=new Scanner().doWork(ebook,jpg,fromPage, toPage, 200);
			if (! success){

				b=new File("work/"+file).renameTo(new File("work/error_scan_"+file));
				if(! b)new File("work/"+file+"/error").mkdir();
			}else{
				String suffix="";
				if ( new File("scanned/"+file).exists()) suffix="_"+System.nanoTime();
				b=new File("work/"+file).renameTo(new File("scanned/"+file+suffix));
				if(! b){
					copyDirectory(new File("work/"+file),new File("scanned/"+file+suffix));
					new File("work/"+file+"/error").mkdir();
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
			//String working_dir="work/";
			String ebook="work/"+file+"/ebook.pdc";
			String info="work/"+file+"/info.txt";
			String jpg="work/"+file+"/jpg";
			String zip="work/"+file+"/zip";


			//String currentDir="download/s"+file;
			//File job=new File("scanning-"+file.getName());
			//move to working dir
			boolean b=new File("scanned/"+file).renameTo(new File("work/"+file));
			if(! b){
				new File("scanned/"+file+"/error").mkdir();
				return;
			}

			//if(! new File(ebook).exists())continue;
			//if(! new File(info).exists())continue;
			if(! new File(jpg).exists())continue;


			List<String> zippedFiles=ZipToFolder.zipAndSplit(jpg, zip, 9000000);

			if(zippedFiles.size() ==0){
				logger.error("no file is zipped");
				b=new File("work/"+file).renameTo(new File("work/error_zip_"+file));
				if(! b)new File("work/"+file+"/error").mkdir();

			}else{
				String suffix="";
				if ( new File("zipped/"+file).exists()) suffix="_"+System.nanoTime();


				b =new File("work/"+file).renameTo(new File("zipped/"+file+suffix));

				if(! b){
					copyDirectory(new File("work/"+file),new File("zipped/"+file+suffix));
					logger.error("could not move file "+file);
					new File("work/"+file+"/error").mkdir();
				}
				logger.info("test");
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
			//String working_dir="work/";
			String info="work/"+file+"/info.txt";
			String zip="work/"+file+"/zip";



			//move to working dir
			boolean b=new File("zipped/"+file).renameTo(new File("work/"+file));
			if(! b || ! new File(zip).exists() || ! new File(info).exists()){
				new File("zipped/"+file+"/error").mkdir();
				return;
			}


			Map<String,String> infoMap=readInfoFile(info);
			String fromEmail=null;
			String subject=null;
			try {
				fromEmail=infoMap.get("from");
				subject=infoMap.get("subject");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("from  are not working for id:"+ infoMap.get("id"));
				new File("work/"+file+"/error").mkdir();
				return;
			}

			String[] zippedFiles= new File(zip).list();
			for (int i = 0; i < zippedFiles.length; i++) {
				try {
					String attachfile=zip+"/"+zippedFiles[i];
					logger.info("attach file :"+ attachfile);
					new SimpleMail().sendMail(fromEmail, subject+" part "+ i,
							printInfoMap(infoMap)+"zip part : "+ i,
							"pdc.to.jpg@gmail.com",attachfile);
				} catch (Exception e) {
					e.printStackTrace();
					new File("work/"+file+"/error").mkdir();
					logger.error("exit emailing parts");
					return;
				}

			}
			String suffix="";
			if ( new File("emailed/"+file).exists()) suffix="_"+System.nanoTime();
			b =new File("work/"+file).renameTo(new File("emailed/"+file+suffix));


			if(! b){
				copyDirectory(new File("work/"+file),new File("emailed/"+file+suffix));
				logger.error("could not move file "+file);
				new File("work/"+file+"/error").mkdir();
			}

		}
	}

	public static void main(String[] args) {
		//threadScanning(2000);
		//threadZipping(5000);
		//threadEmailing(3000);
		try {
			doEmailing();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//threadZipAndEmail(3000);
	}

	public static void doZipAndEmail()throws Exception{
		List<String> jobs=new ArrayList<String>();

		String[] files=new File("download").list();
		for (String file : files) {
			if( new File(file).isFile() || ! file.startsWith("ur") )
				continue;
			jobs.add(file);
		}

		for (String file : jobs) {
			logger.info("zipAndemail with dir "+ file);
			//String ebook="download/"+file+"/ebook.pdc";
			String info="download/"+file+"/info.txt";
			String jpg="download/"+file+"/jpg";
			String zip="download/"+file+"/zip";

			if(! new File(info).exists())continue;
			if(! new File(jpg).exists())continue;

			logger.info("zipAndemail ing  file "+file);
			String currentDir="download/v"+file;
			//File job=new File("scanning-"+file.getName());
			new File("download/"+file).renameTo(new File(currentDir));
			Thread.sleep(10);

			//ebook=currentDir+"/ebook.pdc";
			info=currentDir+"/info.txt";
			jpg=currentDir+"/jpg";
			zip=currentDir+"/zipping";

			if (new File(zip).exists() || new File(currentDir+"/zip").exists()){
				System.err.println("another zip process in running");
				return;
			}
			new File(zip).mkdir();
			Map<String,String> infoMap=readInfoFile(info);
			String from=null;
			String subject=null;
			try {
				from=infoMap.get("from");
				subject=infoMap.get("subject");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("from  are not working for id:"+ infoMap.get("id"));
			}


			boolean success=SimpleMail.zipAndSend(jpg, zip, from, subject, infoMap.toString());
			if (! success){
				new File(currentDir).renameTo(new File("download/w"+file));
			}else{
				new File(zip).renameTo(new File(currentDir+"/zip"));
				new File(currentDir).renameTo(new File("download/z"+file));
			}
		}
		// TODO Auto-gener		
	}

	public static void threadScanning(final int delay) {

		new Thread(){
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(delay);
					} catch (Exception e) {
						logger.error("can't sleep");
					}

					logger.info("try scanning available folders");

					try {
						doScanning();

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
					logger.info("try zipping available folders");
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
					logger.info("try zipping available folders");
					try {
						doEmailing();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	public static void threadZipAndEmail(final int delay) {
		new Thread(){
			@Override
			public void run() {
				while(true){

					try {
						Thread.sleep(delay);
					} catch (Exception e) {
						logger.error("zip can't sleep");
					}
					logger.info("try zip and email available folders");
					try {
						doZipAndEmail();

					} catch (Exception e) {
						e.printStackTrace();
					}


				}
			}
		}.start();

	}
	/**
	 * 
		id:1237918332
		from:eepgssh@gmail.com
		fromPage:3
		toPage6
		subject:scan-3-6
		date:Tue, 24 Mar 2009 16:54:57 +0000
		fileName:e.pdc
	 */
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

//id=1237988801,
//	subject=ebook2-1-200,
//	fromPage=1, 
//	fileName=ebook (2).pdc,
//	toPage=200, 
//	from=eepgssh@gmail.com,
//	date=Wed, 25 Mar 2009 13}
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

}
