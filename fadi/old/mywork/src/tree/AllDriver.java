package tree;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

import utils.MyWritable;
import utils.IntArrayWritable;
import utils.TextArray;

public class AllDriver {

	public static void main(String[] args) {
		
		lineAndMap(2);
	}

	public static void lineAndMap(int iteration) {
		
		JobClient client = new JobClient();
		JobConf conf = new JobConf(AllDriver.class);
	
		// TODO: specify output types
		conf.setOutputKeyClass(IntWritable.class);
		conf.setOutputValueClass(IntArrayWritable.class);
		conf.setOutputFormat(SequenceFileOutputFormat.class);
		
		//mapper
		conf.setMapOutputKeyClass(IntWritable.class);
		conf.setMapOutputValueClass(IntArrayWritable.class);
	
	
		// TODO: specify input and output DIRECTORIES (not files)
		conf.setInputPath(new Path("data/raw_input"));
		Path outpath=new Path("data/tree/data");
		conf.setOutputPath(outpath);
	
		try {
			FileSystem fs = FileSystem.get(conf);
			if (fs.exists(outpath)) {
				fs.delete(outpath);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		conf.setMapperClass(LineAndMapMapper.class);
		//conf.setReducerClass(Line3Reducer.class);
	
		// TODO: specify a reducer
	
		client.setConf(conf);
		try {
			JobClient.runJob(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void hadoop_line2(int iteration) {
		JobClient client = new JobClient();
		JobConf conf = new JobConf(AllDriver.class);

		// TODO: specify output types
		conf.setOutputKeyClass(IntWritable.class);
		conf.setOutputValueClass(TextArray.class);
		conf.setOutputFormat(SequenceFileOutputFormat.class);
		
		//mapper
		conf.setMapOutputKeyClass(IntWritable.class);
		conf.setMapOutputValueClass(TextArray.class);


		// TODO: specify input and output DIRECTORIES (not files)
		conf.setInputPath(new Path("data/raw_input"));
		Path outpath=new Path("data/tree/lined2/"+iteration);
		conf.setOutputPath(outpath);

		try {
			FileSystem fs = FileSystem.get(conf);
			if (fs.exists(outpath)) {
				fs.delete(outpath);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		conf.setMapperClass(AtomicItemsMapper.class);

		// TODO: specify a reducer

		client.setConf(conf);
		try {
			JobClient.runJob(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void hadoop_line() {
		JobClient client = new JobClient();
		JobConf conf = new JobConf(AllDriver.class);
	
		// TODO: specify output types
		conf.setOutputKeyClass(IntWritable.class);
		conf.setOutputValueClass(Text.class);
		conf.setOutputFormat(TextOutputFormat.class);
		
		//mapper
		conf.setMapOutputKeyClass(IntWritable.class);
		conf.setMapOutputValueClass(Text.class);
	
	
		// TODO: specify input and output DIRECTORIES (not files)
		conf.setInputPath(new Path("data/raw_input"));
		Path outpath=new Path("data/tree/lined");
		conf.setOutputPath(outpath);
	
		try {
			FileSystem fs = FileSystem.get(conf);
			if (fs.exists(outpath)) {
				fs.delete(outpath);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		conf.setMapperClass(LineMapper.class);
	
		// TODO: specify a reducer
	
		client.setConf(conf);
		try {
			JobClient.runJob(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
