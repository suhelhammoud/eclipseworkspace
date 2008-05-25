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
import utils.SuperSetWritable;

public class ToItemMapper extends MapReduceBase implements Mapper<IntWritable,SuperSetWritable,SetWritable,SetWritable> {

	public void map(IntWritable key, SuperSetWritable values,
			OutputCollector<SetWritable,SetWritable> output, Reporter reporter) throws IOException {

		for (SetWritable set : values) {
			SetWritable lineSet=new SetWritable();
			lineSet.add(key.get());			
			output.collect(set, lineSet);
		}
	}

}
