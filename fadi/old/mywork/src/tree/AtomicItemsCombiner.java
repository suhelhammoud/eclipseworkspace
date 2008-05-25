package tree;

import java.io.IOException;
import java.util.Iterator;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import utils.SetWritable;

public class AtomicItemsCombiner extends MapReduceBase  implements Reducer<IntWritable,SetWritable,IntWritable,SetWritable> {

	public void reduce(IntWritable key, Iterator<SetWritable> values,
			OutputCollector<IntWritable, SetWritable> output, Reporter reporter) throws IOException {
		
		SetWritable v=values.next();
		
		while (values.hasNext()) {
			SetWritable set =  values.next();
			v.addAll(set);
		}
		output.collect(key, v);
	}

}

