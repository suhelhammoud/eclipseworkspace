package init;


import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class IReducer extends MapReduceBase implements
Reducer<Text, IntWritable, Text, IntWritable> {

	static Logger log=Logger.getLogger(IReducer.class);

	private  static int SUPPORT;

	private IntWritable freq=new IntWritable();


	public void reduce(Text key, Iterator<IntWritable> values,
			OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {

		int sum=0;
		while (values.hasNext()) {
			sum+=values.next().get();

		}
		if (sum >= SUPPORT){
		    reporter.incrCounter(Driver.myCounters .ROWS_LEFT , 1);
			freq.set(sum);
			//System.out.println("out :"+key.toString()+" :\t"+sum);
			output.collect(key, freq);
		}
	}

	@Override
	public void configure(JobConf job) {
		// TODO Auto-generated method stub
		super.configure(job);
		// TODO Auto-generated method stub
		SUPPORT=job.getInt("SUPPORT", -1);
		//System.out.println("Support at reducer="+SUPPORT);
		if(SUPPORT == -1 ){
			log.error("can't get the support or iteration value");
		}

	}


}
