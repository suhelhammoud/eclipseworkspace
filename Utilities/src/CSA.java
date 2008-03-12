import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class CSA {

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

	public  static long download(String ftpFile, String localFileName) {
		OutputStream out = null;
		URLConnection conn = null;
		InputStream  in = null;
		long numWritten = 0;

		try {
			URL url = new URL(ftpFile);
			out = new BufferedOutputStream(
					new FileOutputStream(localFileName));
			conn = url.openConnection();
			in = conn.getInputStream();
			byte[] buffer = new byte[1024];
			int numRead;
			while ((numRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, numRead);
				numWritten += numRead;
			}
			System.out.println(localFileName + "\t" + numWritten);
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
	public static void csa(String dirname){
		String cmdString=dirname+"\\dosbox.exe "+dirname+"\\CSA.EXE";
		System.out.println(cmdString);
		try{
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(cmdString);
			// any error message?
		} catch (Throwable t)
		{
			t.printStackTrace();
		}
	}

	static void send(String location,String datafile) throws Exception{
//		String location="http://192.168.1.3:8080/csa2/a";
//		String data="suhelsss";
//		String location="http://www.google.com";
		String stringToReverse = URLEncoder.encode(readfile(datafile), "UTF-8");
		URL url = new URL(location);
		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);

		OutputStreamWriter out = new OutputStreamWriter(
				connection.getOutputStream());
		out.write("data=" + stringToReverse);
		out.close();

		BufferedReader in = new BufferedReader(
				new InputStreamReader(connection.getInputStream()));

		String decodedString;

		while ((decodedString = in.readLine()) != null) {
			//System.out.println(decodedString);
		}
		in.close();
	}


	public static String readfile(String filename){
		String result="";
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String str;
			while ((str = in.readLine()) != null) {
				result+=str+"\n";
			}
			in.close();
		} catch (IOException e) {
		}

		return result;
	}

	public  static void unzip(String outdir,String fileName){
		final int BUFFER = 2048;
		try {
			BufferedOutputStream dest = null;
			BufferedInputStream is = null;
			ZipEntry entry;
			ZipFile zipfile = new ZipFile(fileName);

			Enumeration e = zipfile.entries();

			while(e.hasMoreElements()) {
				entry = (ZipEntry) e.nextElement();
				System.out.println("Extracting: " +entry);
				is = new BufferedInputStream
				(zipfile.getInputStream(entry));
				int count;
				byte data[] = new byte[BUFFER];

				FileOutputStream fos = new 
				FileOutputStream(outdir+"/"+entry.getName());
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

	public static void cmd(String cmdString){
		//String cmd=dirname+"\\dosbox.exe "+dirname+"\\CSA.EXE";
		System.out.println(cmdString);
		try{
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(cmdString);

			final BufferedReader brError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			final BufferedReader brOut   = new BufferedReader(new InputStreamReader(proc.getInputStream()));

			new Thread(){
				public void run(){
					try {
						while(brError.readLine() != null);						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();

			new Thread(){
				public void run(){
					try {
						while(brOut.readLine() != null);						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();

			int exitVal = proc.waitFor();
			System.out.println("ExitValue: " + exitVal);        

			// any error message?
		} catch (Throwable t)
		{
			t.printStackTrace();
		}

	}
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		if(args==null)return;
		String cmd=args[0];

		//delete directory
		if(cmd.toLowerCase().equals("del")){
			System.out.println("deleting dir");
			deleteDirectory(new File(args[1]));
			return;
		}

		//download file to local file system
		if(cmd.toLowerCase().equals("download")){
			System.out.println("download");
			download(args[1], args[2]);
			return;
		}

		//mkdir 
		if(cmd.toLowerCase().equals("mkdir")){
			System.out.println("mdir");
			(new File(args[1])).mkdirs();
			return;
		}

		//csa 
		if(cmd.toLowerCase().equals("csa")){
			System.out.println("csa");
			String cmdString=args[1]+"\\dosbox.exe "+args[1]+"\\CSA.EXE";			
			cmd(cmdString);
			//csa(args[1]);
			return;
		}

		//send 
		if(cmd.toLowerCase().equals("send")){
			System.out.println("send");
			send(args[1], args[2]);
			return;
		}

		//unzip 
		if(cmd.toLowerCase().equals("unzip")){
			System.out.println("unzip");
			unzip(args[1], args[2]);
			return;
		}

		//cp 
		if(cmd.toLowerCase().equals("cp")){
			System.out.println("cp");
			copyFile(new File(args[1]), new File(args[2]));
			return;
		}

		//cpdir 
		if(cmd.toLowerCase().equals("cpdir")){
			System.out.println("cpdir");
			copyDirectory(new File(args[1]), new File(args[2]));
			return;
		}

		//cmd 
		if(cmd.toLowerCase().equals("cmd")){
			System.out.println("cmd");
			cmd(args[1]);
			return;
		}
		
		System.out.println("no command choosed");

	}

}
