
import org.apache.log4j.Logger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
			if( new File(file).isFile() || ! file.startsWith("r") )
				continue;
			jobs.add(file);
		}

		for (String file : jobs) {
			logger.info("working with dir "+ file);
			String ebook="download/"+file+"/ebook.pdc";
			String info="download/"+file+"/info.txt";
			String jpg="download/"+file+"/jpg";

			if(! new File(ebook).exists())continue;
			if(! new File(info).exists())continue;
			if(new File(jpg).exists())continue;

			logger.info("scanning file "+file);
			String currentDir="download/s"+file;
			//File job=new File("scanning-"+file.getName());
			new File("download/"+file).renameTo(new File(currentDir));

			ebook=currentDir+"/ebook.pdc";
			info=currentDir+"/info.txt";
			jpg=currentDir+"/jpging";

			if (new File(jpg).exists() || new File(currentDir+"/jpg").exists()){
				System.err.println("another scanning process in running");
				return;
			}
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

			boolean success=new PdcConverter().doWork(ebook,jpg,fromPage, toPage, 2000);
			if (! success){
				new File(currentDir).renameTo(new File("download/t"+file));
			}else{
				new File(jpg).renameTo(new File(currentDir+"/jpg"));
				new File(currentDir).renameTo(new File("download/u"+file));
			}
		}
		// TODO Auto-generated method stub

	}
	public static void main(String[] args) {
		threadScanning(30);
		threadZipAndEmail(30);
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

					logger.info("try scanning available folders");

					try {
						doScanning();

					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						Thread.sleep(delay);
					} catch (Exception e) {
						logger.error("can't sleep");
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

					logger.info("try zip and email available folders");

					try {
						doZipAndEmail();

					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						Thread.sleep(delay);
					} catch (Exception e) {
						logger.error("zip can't sleep");
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

}
