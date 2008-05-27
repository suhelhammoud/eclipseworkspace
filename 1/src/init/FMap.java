package init;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.log4j.Logger;

import utils.MapTools;

public class FMap {
	static Logger log=Logger.getLogger(FMap.class);
	final private static String SEP=" ";

	private HashMap<String, Integer> fmap=new HashMap<String, Integer>();
	private  static int SUPPORT;
	private  static int ITERATION;

	private static final IntWritable one=new IntWritable(1);
	private Text word=new Text();

	public void configure(JobConf conf) {

		// TODO Auto-generated method stub
		SUPPORT=conf.getInt("SUPPORT", -1);
		ITERATION=conf.getInt("ITERATION", -1);

		log.info("\tITERATION="+ITERATION);
		log.info("\tSUPPORT="+SUPPORT);

		if(SUPPORT == -1 || ITERATION == -1){
			log.error("can't get the support or iteration value");
		}

		String 	freqPath=conf.get("freq1Path", "data/freqs/");

		fmap= getFMap(conf,freqPath+(ITERATION-1));


	}
	public static HashMap<String, Integer> getFMap(JobConf job,String mapDir) {
		HashMap<String, Integer> result=new HashMap<String, Integer>();
		try {
			FileSystem fs = FileSystem.get(job);
//			Path path = new Path("data/"+(ITERATION-1)+"/part-00000");
			Path path = new Path(mapDir+"/part-00000");
			if (! fs.exists(path)) {
				log.error("No map file found");
				return result;
			}

			SequenceFile.Reader reader = new SequenceFile.Reader(fs, path, job);
			try {
				Text key = new Text();
				IntWritable value = new IntWritable();
				while (reader.next(key, value)) {
					String itm=key.toString().trim();
					result.put(itm, value.get());
					//TODO added this bit to map one single items
					String[] items=itm.trim().split("[ ,\t\n]");
					for (String item : items) {
						if (result.containsKey(item))continue;
						result.put(item, -1);
					}
				}
				result.put("", -1);//needed for IMapper.output()

				log.info("fmap Iteration "+result.size());
				return result;

			} finally {
				reader.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void out(OutputCollector<Text,IntWritable> output,Set<String> set,String[] arr,List<Integer> list,int index, int len)
	throws IOException{
		String last=MapTools.join(SEP, arr,list);
		if (len==1){
			if( set.contains(last) )
				for (int i = index; i < arr.length; i++) {
					if (MapTools.passPrune(set, arr, list, i))
						if(output==null)
							System.out.println(last+SEP+arr[i]);
						else{
							output.collect(new Text(last+SEP+arr[i]), new IntWritable(1));
						}
				}
		}
		else
			for (int i = index; i < arr.length-len+1; i++) {
				if( ! set.contains(last) ) continue;
				ArrayList<Integer> nlst=new ArrayList<Integer>(list);
				nlst.add(i);
				out(output,set, arr,nlst, i+1, len-1);				
			}
	}

	void emitte2(OutputCollector<Text, IntWritable> output,String[] items)throws
	IOException{

		ArrayList<Integer> tempList=new ArrayList<Integer>();
		//log.info(""+MapTools.SetToString(fmap.keySet())+Arrays.toString(items)+ITERATION);
		out(output,fmap.keySet(), items, tempList, 0,ITERATION);
	}

	public static void key(String[] arr,int index,int len,String last){
		if (len==1)
			for (int i = index; i < arr.length; i++) {
				System.out.println(last+arr[i]+",");				
			}
		else
			for (int i = index; i < arr.length-len+1; i++) {
				key(arr, i+1, len-1, last+arr[i]+",");				
			}
	}
	public static void k(String[] arr,int index,int len,String last){
		if(len<1)return;
			for (int i = index; i < arr.length; i++) {
				System.out.println(last+arr[i]);						
				k(arr, i+1, len-1, last+arr[i]+",");				
			}
	}	
	public static void subset(String[] arr,int index,int len,String last,int[] count){
		if (len <0 )return;
			for (int i = index; i < arr.length-1; i++) {
				System.out.println(""+(count[0]++)+"\t"+last+arr[i]);
				subset(arr, i+1, len-1, last+arr[i]+",",count);	
			}
	}
	public static void twoSubsets(String[] arr,int index,int len,List<Integer> list){
		if (len <0 )return;
			for (int i = index; i < arr.length; i++) {
				List<Integer> tlst=new ArrayList<Integer>(list);
				tlst.add(i);

				String[] result=MapTools.joinSplit(",", arr, tlst);
				System.out.println(result[0]+"\t----->\t"+result[1]);
				twoSubsets(arr, i+1, len-1, tlst);	
			}
	}

	public static void main(String[] args){
		String[] arr={"1","2","3","4","5","6"};
		
		List<Integer> list=new ArrayList<Integer>();
		twoSubsets(arr, 0, arr.length, list);
		//subset(arr, 0, arr.length, "",new int[1]);
		System.out.println("-------------------------");
		//k(arr, 0, arr.length+1, "");
	}

}
