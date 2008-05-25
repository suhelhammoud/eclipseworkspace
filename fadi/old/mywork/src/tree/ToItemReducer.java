package tree;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import utils.SetWritable;
import utils.SuperSetWritable;

public class ToItemReducer extends MapReduceBase implements Reducer<SetWritable,SetWritable,SetWritable,SetWritable> {

	public static int SUPPORT;

	public void reduce(SetWritable key, Iterator<SetWritable> values,
			OutputCollector<SetWritable,SetWritable> output, Reporter reporter) throws IOException {
		SetWritable v=values.next();
		
		while (values.hasNext()) {
			SetWritable set =  values.next();
			v.addAll(set);
		}		
		if(v.size()< SUPPORT) return;
		
		reporter.incrCounter(JobDriver.myCounters.ITEMS_LEFT, 1);
		output.collect(key, v);
		// TODO increase coutner of total items
		//System.out.println(key+"-"+v);
	}
	@Override
	public void configure(JobConf job) {
		// TODO Auto-generated method stub
		super.configure(job);
		this.SUPPORT=job.getInt("SUPPORT", -1);

	}
	

}
