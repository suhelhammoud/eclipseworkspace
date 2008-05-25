package tree;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import utils.SetWritable;

public class ToItemCominer extends MapReduceBase implements Reducer<SetWritable,SetWritable,SetWritable,SetWritable> {

	public void reduce(SetWritable key, Iterator<SetWritable> values,
			OutputCollector<SetWritable,SetWritable> output, Reporter reporter) throws IOException {
		SetWritable v=values.next();
		
		while (values.hasNext()) {
			SetWritable set =  values.next();
			v.addAll(set);
		}
		output.collect(key, v);
		
	}

}
