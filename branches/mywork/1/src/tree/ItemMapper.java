package tree;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class ItemMapper extends MapReduceBase implements Mapper<IntWritable,Text,Text,IntWritable> {

	public void map(IntWritable key, Text values,
			OutputCollector<Text,IntWritable> output, Reporter reporter) throws IOException {
		
	}

}
