package tree;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import utils.IntArrayWritable;

public class Line3Reducer extends MapReduceBase implements Reducer <IntWritable,IntArrayWritable,IntWritable, IntArrayWritable>{

	public void reduce(IntWritable key, Iterator<IntArrayWritable> values,
			OutputCollector<IntWritable,IntArrayWritable> output, Reporter reporter) throws IOException {
		while (values.hasNext()) {
			output.collect(key, values.next());
		}

	}

}
