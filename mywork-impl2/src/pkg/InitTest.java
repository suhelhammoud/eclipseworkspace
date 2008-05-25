package pkg;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;


public class InitTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		JobConf jc=new JobConf();
		jc.setJobName("init job");
		jc.setOutputKeyClass(Text.class);
		jc.setOutputValueClass(Text.class);
		
		jc.setMapperClass(InitMapper.class);
		//jc.setCombinerClass(MyReduce.class);
		jc.setReducerClass(InitReduce.class);
		
		jc.setInputFormat(TextInputFormat.class);
		jc.setOutputFormat(TextOutputFormat.class);
		
		jc.setInputPath(new Path("data/input"));
		jc.setOutputPath(new Path("data/init"));
		JobClient.runJob(jc);
		
		
	}

}
