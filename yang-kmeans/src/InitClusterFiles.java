import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.ClusterStatus;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;


public class InitClusterFiles extends MapReduceBase implements Mapper<WritableComparable,Text,Text,Text> {

	@Override
	public void configure(JobConf job) {
		// TODO Auto-generated method stub
		super.configure(job);
	}
	Text textKey=new Text("cluster");
	public void map(WritableComparable key, Text values,
			OutputCollector<Text,Text> output, Reporter reporter) throws IOException {
		output.collect(textKey,values);
	}

	public static void main(String[] args) {
		runJob("data/out/0/points", "data/out/1");
	}
	public static void runJob(String input, String output){
		try {
			JobConf conf = new JobConf(InitClusterFiles.class);
			conf.setJobName("map test ");

			Path outPath = new Path(output);
			FileSystem fs = FileSystem.get(conf);
			//TODO remove later,
			if (fs.exists(outPath)) {
				fs.delete(outPath);
			}

			conf.setOutputKeyClass(Text.class);
			conf.setOutputValueClass(Text.class);

			conf.setInputPath(new Path(input));
			conf.setOutputPath(outPath);

			conf.setMapperClass(InitClusterFiles.class);
			// conf.setCombinerClass(ICombiner.class);
			//conf.setReducerClass(IReducer.class);

			conf.setInputFormat(TextInputFormat.class);

			conf.setOutputFormat(SequenceFileOutputFormat.class);
			conf.setNumReduceTasks(0);//TODO change it later

			RunningJob rj= JobClient.runJob(conf);


		} catch (Exception e) {
			e.printStackTrace();

		}               

	}

}


