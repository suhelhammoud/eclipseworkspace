package pkg;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;


public class MyReduce extends MapReduceBase implements Reducer {
	private int supp=0;
	private double conf=0.0;
	
	public void reduce(WritableComparable key, Iterator values,
			OutputCollector output, Reporter reporter) throws IOException {


		Map<String,Integer> rests=new HashMap<String, Integer>();
		while (values.hasNext()) {
			String str= ((Text)values.next()).toString();
			if (rests.containsKey(str)){
				rests.put(str, rests.get(str)+1);
			}else{
				rests.put(str, 1);				
			}
		}
		output.collect(key, new IntWritable(rests.size()));
	}
}


