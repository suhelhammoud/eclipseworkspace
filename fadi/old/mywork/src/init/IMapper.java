package init;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.log4j.Logger;

import utils.MapTools;

import com.sun.org.apache.bcel.internal.generic.FMUL;

import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IMapper extends MapReduceBase implements
Mapper<WritableComparable, Text, Text, IntWritable> {

	static Logger log=Logger.getLogger(IMapper.class);
	private FMap fMap;

	private static final IntWritable one=new IntWritable(1);
	private Text word=new Text();

	public void map(WritableComparable key, Text values,
			OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {

		String[] items = values.toString().trim().split("[ \t\n]");
		fMap.emitte2(output, items);
		//emitte2(fmap.keySet(), output, items, 0, ITERATION);
	}
	
//	public static void out(OutputCollector<Text,IntWritable> output,Set<String> set,String[] arr,List<Integer> list,int index, int len)
//	throws IOException{
//		String last=MapTools.join(SEP, arr,list);
//		if (len==1){
//			if( set.contains(last) )
//				for (int i = index; i < arr.length; i++) {
//					if (MapTools.passPrune(set, arr, list, i))
//						if(output==null)
//							System.out.println(last+SEP+arr[i]);
//						else{
//							output.collect(new Text(last+SEP+arr[i]), new IntWritable(1));
//						}
//				}
//		}
//		else
//			for (int i = index; i < arr.length-len+1; i++) {
//				if( ! set.contains(last) ) continue;
//				ArrayList<Integer> nlst=new ArrayList<Integer>(list);
//				nlst.add(i);
//				out(output,set, arr,nlst, i+1, len-1);				
//			}
//	}
//
//	static void emitte2(Set<String> set,OutputCollector<Text, IntWritable> output,String[] items,int index,int len)throws
//	IOException{
//
//		ArrayList<Integer> tempList=new ArrayList<Integer>();
//		log.info(""+MapTools.SetToString(set)+Arrays.toString(items)+ITERATION);
//		out(output, set, items, tempList, 0,ITERATION);
//	}
//

	@Override
	public void configure(JobConf conf) {
		super.configure(conf);
		fMap=new FMap();
		fMap.configure(conf);
//		
//		// TODO Auto-generated method stub
//		SUPPORT=conf.getInt("SUPPORT", -1);
//		ITERATION=conf.getInt("ITERATION", -1);
//
//		log.info("\tITERATION="+ITERATION);
//		log.info("\tSUPPORT="+SUPPORT);
//
//		if(SUPPORT == -1 || ITERATION == -1){
//			log.error("can't get the support or iteration value");
//		}
//
//		String 	freqPath=conf.get("freq1Path", "data/freqs/");
//
//		fmap= getFMap(conf,freqPath+(ITERATION-1));
//	}
//		
//	public static HashMap<String, Integer> getFMap(JobConf job,String mapDir) {
//		HashMap<String, Integer> result=new HashMap<String, Integer>();
//		try {
//			FileSystem fs = FileSystem.get(job);
//			Path path = new Path("data/"+(ITERATION-1)+"/part-00000");
//			Path path = new Path(mapDir+"/part-00000");
//			if (! fs.exists(path)) {
//				log.error("No map file found");
//				return result;
//			}
//
//			SequenceFile.Reader reader = new SequenceFile.Reader(fs, path, job);
//			try {
//				Text key = new Text();
//				IntWritable value = new IntWritable();
//				while (reader.next(key, value)) {
//					String itm=key.toString().trim();
//					result.put(itm, value.get());
//					//TODO added this bit to map one single items
//					String[] items=itm.trim().split("[ ,\t\n]");
//					for (String item : items) {
//						if (result.containsKey(item))continue;
//						result.put(item, -1);
//					}
//				}
//				result.put("", -1);//needed for IMapper.output()
//			
//				log.info("fmap Iteration "+result.size());
//				return result;
//
//			} finally {
//				reader.close();
//			}
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//	}

	}
}

