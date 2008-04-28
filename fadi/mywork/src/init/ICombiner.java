package init;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;
import java.util.Iterator;

public class ICombiner extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
	
	private IntWritable freq=new IntWritable();


	public void reduce(Text key, Iterator<IntWritable> values,
			OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {

		int sum=0;
		while (values.hasNext()) {
			sum+=values.next().get();

		}
		freq.set(sum);
		output.collect(key, freq);
	}

}
