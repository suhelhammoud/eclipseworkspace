package impl1;

import init.Driver;

import java.awt.ItemSelectable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SortedMapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.VLongWritable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.log4j.Logger;

import utils.MapTools;
import utils.SetWritable;

public class ItemMap extends TreeMap<SetWritable, Integer>{

	static Logger log=Logger.getLogger(ItemMap.class);
	final private static String SEP=" ";

	public  static int SUPPORT;
	public  static int ITERATION;

	String freqDir;
	String candidtatesDir;

	private static final IntWritable one=new IntWritable(1);

	JobConf job;

	public ItemMap(){
		
	}
	
	public void configure(JobConf conf) {
		SUPPORT=conf.getInt("SUPPORT", -1);
		ITERATION=conf.getInt("ITERATION", -1);

		if(SUPPORT == -1 || ITERATION == -1){
			log.error("can't get the support or iteration value");
		}
		this.freqDir =conf.get("freqDir", "data/freqs");
		this.candidtatesDir=conf.get("candidtatesDir","data/candidates");
	}


	
	
	public static void subsets(Integer[] arr,int index, int len,SetWritable last,List<SetWritable> result){
		if (len==1 ){
			for (int i = index; i < arr.length; i++) {
				SetWritable newSet=new SetWritable(last);
				newSet.add(arr[i]);
				result.add(newSet);
			}
		}
		else
			for (int i = index; i <  arr.length -len+1; i++) {
				SetWritable newSet=new SetWritable(last);
				newSet.add(arr[i]);
				ItemMap.subsets(arr,i+1,len-1,newSet,result);
			} 
	}
	
	public void getSubsets(Integer[] arr,int index, int len,SetWritable last,List<SetWritable> result){
		if (len==1 ){
			if (  ! containsKey(last)) return;
			for (int i = index; i < arr.length; i++) {
				SetWritable newSet=new SetWritable();
				newSet.add(arr[i]);
				if(! containsKey(newSet))continue;
				newSet.addAll(last);
				result.add(newSet);

			}
		}
		else
			for (int i = index; i <  arr.length -len+1; i++) {
				SetWritable newSet=new SetWritable();
				newSet.add(arr[i]);
				if (  ! containsKey(newSet))continue;
				newSet.addAll(last);
				getSubsets(arr,i+1,len-1,newSet,result);
			} 
	}
	public static List<SetWritable> subsets(Integer[] arr, int index, int len){
		List<SetWritable> result=new ArrayList<SetWritable>();
		ItemMap.subsets(arr, index, len, new SetWritable(), result);
		return result;
	}
	public static List<SetWritable> subsets(SetWritable sw, int index, int len){
		List<SetWritable> result=new ArrayList<SetWritable>();
		Integer[] arr=sw.toArray(new Integer[0]);
		ItemMap.subsets(arr, index, len, new SetWritable(), result);
		return result;
	}

	public List<SetWritable> getSubsets(Integer[] arr, int index, int len){
		List<SetWritable> result=new ArrayList<SetWritable>();
		getSubsets( arr, index, len, new SetWritable(), result);
		return result;
	}
	public List<SetWritable> getSubsets(SetWritable sw, int index, int len){
		List<SetWritable> result=new ArrayList<SetWritable>();
		Integer[] arr=sw.toArray(new Integer[0]);
		getSubsets( arr, index, len, new SetWritable(), result);
		return result;
	}
	public static void main(String[] args)throws Exception{

		Integer[] arr={1,2,3,5,6};
		SetWritable sw=new SetWritable(Arrays.asList(arr));

		ItemMap im=new ItemMap();
		im.load("../mywork/data/freqs/2",new JobConf());
		System.out.println(im.toString());
		
		for (SetWritable jitem : im.keySet()) {
			if (sw.containsAll(jitem))
				System.out.println("contains "+jitem.toString());
		}

		//		nested iteration
//		Integer[] arr={1,2,3,4,5,6};
//		SetWritable sw=new SetWritable(Arrays.asList(arr));
//		for (Integer iter1 : sw) {
//			for (Integer iter2 : sw) {
//				if (iter1== iter2)continue;
//				System.out.println(""+iter1+ ", "+iter2);
//			}
//		}
//		sw.remove(1);
//		Integer[] a2=sw.toArray(new Integer[0]);
//		
//		System.out.println(Arrays.toString(a2));
	}


	private static void testSubSets() {
		ItemMap map=new ItemMap();

		//map.put(1, )
		Integer[] arr={0,1,2,3,4,5};
//		out(null, arr, 0, 3, new SetWritable());
		//List<SetWritable> result=subsets(arr, 0, 3);
		System.out.println("-1--------------");

		List<SetWritable> result=ItemMap.subsets(arr, 0, 3);

		for (SetWritable itm : result) {
			System.out.println(itm);
		}

		for (int i = 1; i < arr.length; i++) {
			SetWritable st=new SetWritable();
			st.add(i);
			map.put(st, -1);
		}
		map.put(new SetWritable(), 0);
		List<SetWritable> l2=ItemMap.subsets(arr, 0, 2);
		for (SetWritable i2 : l2) {
			map.put(i2, -2);
		}

		System.out.println("map=="+map+"\n--end map");
		result=map.getSubsets(arr, 0, 3);
		System.out.println("-2---------");

		for (SetWritable stw : result) {
			System.out.println(stw);
		}
	}


	private static void testSetWritableConstructor() {
		Integer[] arr={1,2,3,4,5,6};		
		SetWritable s1=new SetWritable(Arrays.asList(arr));
		System.out.println("s1="+s1);
		SetWritable s2=new SetWritable(s1);
		System.out.println("s2="+s2);
		s2.remove(1);
		System.out.println("after remove");
		System.out.println("s1="+s1);
		System.out.println("s2="+s2);
	}

	public void load(JobConf job){
		load(candidtatesDir+"/"+ITERATION ,job);
	}


	@Override
	public String toString() {

		StringBuffer sb=new StringBuffer();
		// TODO Auto-generated method stub
		for (SetWritable s : keySet()) {
			sb.append("\n"+s.toString()+":"+get(s));
		}
		return sb.toString();
	}


	public void load(String srcDir,JobConf job) {
		//clear();
		configure(job);
		try {
			FileSystem fs = FileSystem.get(job);
			Path srcPath=new Path(srcDir);
			if (! fs.exists(srcPath)) {
				log.error("No map file found");
				return ;
			}

			Path[] paths=fs.listPaths(srcPath);
			
			for (int i = 0; i < paths.length; i++) {
				log.info("path[i] "+paths[i].toString());
				SequenceFile.Reader reader = new SequenceFile.Reader(fs, paths[i], job);

				SetWritable key = new SetWritable();
				IntWritable value = new IntWritable();
				while (reader.next(key, value)) {

					put(new SetWritable(key), value.get());
					
				}
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<SetWritable> join(Set<SetWritable> lset){
		List<SetWritable> result=new ArrayList<SetWritable>();
		for (SetWritable iter1 : lset) {
			for (SetWritable iter2 : lset) {
				SetWritable mset=iter1.merg(iter2);
				if (mset!=null)result.add(mset);
			}
		}
		return result;
	}
	
	public static TreeSet<SetWritable> prune(List<SetWritable> joinSet, Set<SetWritable> lset){
		TreeSet<SetWritable> result=new TreeSet<SetWritable>();
		for (SetWritable jItem : joinSet) {
			List<SetWritable> subset_1=subset_remove_one(jItem);
			boolean isInLi_1=true;
			for (SetWritable subJItem : subset_1) {
				if (! lset.contains(subJItem)){
					isInLi_1=false;
					break;
				}
			}
			if (isInLi_1)
				result.add(jItem);
		}
		return result;
	}
	
	public static List<SetWritable> subset_remove_one(SetWritable st){
		List<SetWritable> result=new ArrayList<SetWritable>();
		for (Integer item : st) {
			SetWritable ts=new SetWritable(st);
			ts.remove(item);
			result.add(ts);
		}
		return result;
	}
	
	
	public void save(String outDir,JobConf conf){
		
		try {
			
			FileSystem fs = FileSystem.get(conf);
			Path outPath = new Path(outDir+"/part-00000");
			SequenceFile.Writer writer = SequenceFile.createWriter(fs, conf, outPath, SetWritable.class, IntWritable.class);
			try {				
				for (Map.Entry<SetWritable, Integer> entry : entrySet()) {
					writer.append(entry.getKey(), new IntWritable(entry.getValue()));
				}
			} finally {
				writer.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
	
	public void prepareNext(String outDir,JobConf conf){
		ItemMap itemMap=new ItemMap();
		//itemMap.putAll(this);
		List<SetWritable> j=ItemMap.join(this.keySet());
		TreeSet<SetWritable> c=ItemMap.prune(j, this.keySet());
		int counter=0;
		for (SetWritable sw : c) {
			itemMap.put(sw, counter++);
		}
		itemMap.save(outDir, conf);
		
	}
	

}
