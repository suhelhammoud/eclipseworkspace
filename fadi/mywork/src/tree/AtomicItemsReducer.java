package tree;

import java.io.IOException;
import java.util.Iterator;

import javax.swing.JComboBox.KeySelectionManager;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.JobHistory.Keys;

import utils.SetWritable;

public class AtomicItemsReducer extends MapReduceBase  implements Reducer<IntWritable,SetWritable,SetWritable,SetWritable> {

	public static int SUPPORT;
	public void reduce(IntWritable key, Iterator<SetWritable> values,
			OutputCollector<SetWritable, SetWritable> output, Reporter reporter) throws IOException {
		
		SetWritable v=values.next();
		
		while (values.hasNext()) {
			SetWritable set =  values.next();
			v.addAll(set);
		}
		
		if(v.size()< SUPPORT) return;
		SetWritable keySet=new SetWritable();
		keySet.add(key.get());
		output.collect(keySet, v);
		System.out.println(keySet+"-"+v);
	}




	@Override
	public void configure(JobConf job) {
		// TODO Auto-generated method stub
		super.configure(job);
		this.SUPPORT=job.getInt("SUPPORT", -1);

	}

}
