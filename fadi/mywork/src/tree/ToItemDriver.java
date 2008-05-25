package tree;


import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.Counters;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.mapred.SequenceFileInputFormat;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

import utils.SetWritable;
import utils.SuperSetWritable;

public class ToItemDriver {

	public static void main(String[] args) {
		runToItem(2, 0);
	}


	public static int runToItem(int iteration,int support) {
		JobClient client = new JobClient();
		JobConf conf = new JobConf(ToItemDriver.class);
		conf.setInt("SUPPORT",support);
		conf.setInt("ITERATION", iteration);
		
		// TODO: specify output types
		conf.setOutputKeyClass(SetWritable.class);
		conf.setOutputValueClass(SetWritable.class);

		conf.setMapOutputKeyClass(SetWritable.class);
		conf.setMapOutputValueClass(SetWritable.class);
		
		conf.setInputFormat(SequenceFileInputFormat.class);
		conf.setOutputFormat(SequenceFileOutputFormat.class);

		// TODO: specify input and output DIRECTORIES (not files)
		conf.setInputPath(new Path("data/tree/lines/"+iteration));

		Path outpath=new Path("data/tree/items/"+iteration);
		conf.setOutputPath(outpath);
	
		try {
			FileSystem fs = FileSystem.get(conf);
			if (fs.exists(outpath)) {
				fs.delete(outpath);
			}
		} catch (Exception e) {e.printStackTrace();}

		conf.setMapperClass(ToItemMapper.class);
		conf.setCombinerClass(ToItemCominer.class);
		conf.setReducerClass(ToItemReducer.class);

		client.setConf(conf);
		try {
			RunningJob rj=JobClient.runJob(conf);

			Counters counters=rj.getCounters();

			long c=counters.getCounter(JobDriver.myCounters.ITEMS_LEFT );
			return (int)c;
			
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		
	}

}
