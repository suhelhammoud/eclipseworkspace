package tree;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import utils.SetWritable;

public class ToLineMapper extends MapReduceBase implements Mapper<SetWritable,SetWritable,IntWritable,SetWritable> {

	public void map(SetWritable key, SetWritable values,
			OutputCollector<IntWritable,SetWritable> output, Reporter reporter) throws IOException {
		
		IntWritable v=new IntWritable();
		for(Integer ln: values){
			v.set(ln);
			output.collect(v, key);
		}
	}

}
