import java.io.*;
import java.util.*;
import java.util.zip.*;

public class Zipper
{
	//private static final long MAX_FILE_SIZE = 5000000; // Whatever size you want


	public static void main(String a[])
	{
		System.out.println(zipAndSplit("data/in", "data/out4",2000000).toString()); 
	}

	public static List<String> zipAndSplit(String inName,String outName,long maxFileSize) {
		List<String> result=new ArrayList<String>();
		
		if(! new File(inName).exists())return result;
		File dir=new File(outName);
		if(!dir.exists())dir.mkdirs();
		
		try{
		
			File inFolder=new File(inName);

			File outFolder;
			ZipOutputStream out=null ;
			BufferedInputStream in = null;
			byte[] data    = new byte[1000];
			String files[] = inFolder.list();
			long currentSize=Long.MAX_VALUE;

			int part=0;
			for (int i=0  ; i<files.length; i++){
				if(currentSize > maxFileSize){
					//System.out.println("current size "+currentSize);
					currentSize=0;
					if(out!=null){
						out.flush();
						out.close();
					}
					String partition=outName+"/"+(part++)+".zip";
					result.add(partition);
					outFolder=new File(partition);
					out= new ZipOutputStream(new 	BufferedOutputStream(new FileOutputStream(outFolder)));
				}
				in = new BufferedInputStream(new FileInputStream
						(inFolder.getPath() + "/" + files[i]), 1000);   
				ZipEntry entry= new ZipEntry(files[i]);
				out.putNextEntry(entry); 

				int count;
				while((count = in.read(data,0,1000)) != -1)
				{
					out.write(data, 0, count);
				}
				out.closeEntry();
				currentSize += entry.getCompressedSize();


			}
			out.flush();
			out.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new ArrayList<String>();
		}
		return result;
	}
} 