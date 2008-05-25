package tree;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.SequenceFileInputFormat;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

import utils.SetWritable;
import utils.SuperSetWritable;

public class ToLineDriver {

	public static void main(String[] args) {
		runToLine(1);
	}

	public static void runToLine(int iteration) {
		JobClient client = new JobClient();
		JobConf conf = new JobConf(ToLineDriver.class);
		conf.setInt("SUPPORT",0);
		conf.setInt("ITERATION", iteration);
		
		// TODO: specify output types
		conf.setOutputKeyClass(IntWritable.class);
		conf.setOutputValueClass(SuperSetWritable.class);

		conf.setMapOutputKeyClass(IntWritable.class);
		conf.setMapOutputValueClass(SetWritable.class);
		
		conf.setInputFormat(SequenceFileInputFormat.class);
		conf.setOutputFormat(SequenceFileOutputFormat.class);

		// TODO: specify input and output DIRECTORIES (not files)
		conf.setInputPath(new Path("data/tree/items/"+(iteration-1)));

		Path outpath=new Path("data/tree/lines/"+iteration);
		conf.setOutputPath(outpath);
	
		try {
			FileSystem fs = FileSystem.get(conf);
			if (fs.exists(outpath)) {
				fs.delete(outpath);
			}
		} catch (Exception e) {e.printStackTrace();}

		conf.setMapperClass(ToLineMapper.class);
		conf.setReducerClass(ToLineReducer.class);

		client.setConf(conf);
		try {
			JobClient.runJob(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
