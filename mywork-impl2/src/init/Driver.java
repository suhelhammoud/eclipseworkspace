package init;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.VLongWritable;
import org.apache.hadoop.mapred.Counters;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.log4j.Logger;

import com.sun.net.ssl.internal.ssl.Debug;
import com.sun.org.apache.bcel.internal.generic.DADD;

import java.io.IOException;

public class Driver {
	static Logger log=Logger.getLogger(Driver.class);
	public static enum myCounters {NUMBER_OF_ROWS,ROWS_LEFT} 

	
	public static void main(String[] args) {

		run();

		//long dataSize=hadoop_freq_0("data/raw_input", "data/test");

	}
	public static void run() {
		double rSupport=0.01;
		int support=0;

		long dataSize=hadoop_freq_0("data/raw_input", "data/0");		
		log.info("dataSize"+dataSize);
		
		support=(int)(rSupport*	dataSize);
		System.out.println("Minimum sopprot="+ support);
		freq_1("data/0", "data/freqs/1",support);

		hadoop_updateData("data/raw_input", "data/input","data/freqs/1" );

		//int rowsLeft=hadoop_freq("data/input", "data/freqs", 2, support);
		//System.out.println("Rows Left="+rowsLeft);;
		//freq_1("data/0", "data/freqs/1",support);

		for (int i = 2; ; i++) {
			 int itemsLeft=hadoop_freq("data/input", "data/freqs", i, support);
			System.out.println("ITERATION:"+i+ "  Rows Left="+itemsLeft);;
			if(itemsLeft==0)break;
		}
	}
	static int hadoop_freq(String input,String output,int iteration, int support){
		try {
			JobConf conf = new JobConf(Driver.class);

			conf.setInt("ITERATION", iteration);
			conf.setInt("SUPPORT", support);
			//conf.set("freqDir", "data/freqs");

			Path outPath = new Path(output+"/"+iteration);
			FileSystem fs = FileSystem.get(conf);
			//TODO remove later,
			if (fs.exists(outPath)) {
				fs.delete(outPath);
			}

			conf.setOutputKeyClass(Text.class);
			conf.setOutputValueClass(IntWritable.class);

			conf.setInputPath(new Path(input));
			conf.setOutputPath(outPath);

			conf.setMapperClass(IMapper.class);
			conf.setCombinerClass(ICombiner.class);
			conf.setReducerClass(IReducer.class);
			conf.setNumReduceTasks(1);//TODO change it later
			conf.setOutputFormat(SequenceFileOutputFormat.class);


			RunningJob rj= JobClient.runJob(conf);
			Counters counters=rj.getCounters();

			long c=counters.getCounter(myCounters.ROWS_LEFT );
			System.out.println("rows left "+c );

			return (int)c;


		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}		

	}

	static void freq_1(String srcMapPath, String distMapPath,int supp){
		try {
			JobConf conf=new JobConf(Driver.class);
			FileSystem fs = FileSystem.get(conf);
			Path srcPath = new Path(srcMapPath+"/part-00000");

			Path distPath = new Path(distMapPath+"/part-00000");

			SequenceFile.Reader reader = new SequenceFile.Reader(fs, srcPath, conf);
			SequenceFile.Writer writer = SequenceFile.createWriter(fs, conf, distPath, Text.class, IntWritable.class);
			try {
				Text key = new Text();
				IntWritable value = new IntWritable();
				while (reader.next(key, value)) {
					if(value.get() < supp)continue;
					writer.append(key, value);
					//System.out.println(key.toString()+":\t"+value.get());
				}
			} finally {
				reader.close();
				writer.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}


	}
	static int hadoop_freq_0(String input,String output) {
		try {
			JobConf conf=new JobConf(Driver.class);
			Path outPath = new Path(output);
			FileSystem fs = FileSystem.get(conf);
			if (fs.exists(outPath)) {
				fs.delete(outPath);
			}

			conf.setOutputKeyClass(Text.class);
			conf.setOutputValueClass(IntWritable.class);

			conf.setInputPath(new Path(input));
			conf.setOutputPath(outPath);

			conf.setMapperClass(InitMapper.class);
			conf.setCombinerClass(InitReducer.class);
			conf.setReducerClass(InitReducer.class);
			conf.setNumReduceTasks(1);//TODO change it later
			conf.setOutputFormat(SequenceFileOutputFormat.class);



			RunningJob rj= JobClient.runJob(conf);

			Counters counters=rj.getCounters();
			long c=counters.getCounter(myCounters.NUMBER_OF_ROWS );
			System.out.println("Counters "+c );

			return (int)c;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}
	static void hadoop_updateData(String input, String output,String freqPath) {
		try {
			JobConf conf=new JobConf(Driver.class);
			conf.set("freqPath", freqPath);
			Path outPath = new Path(output);
			FileSystem fs = FileSystem.get(conf);
			if (fs.exists(outPath)) {
				fs.delete(outPath);
			}
			//fs.mkdirs(outPath);

			conf.setOutputKeyClass(Text.class);
			conf.setOutputValueClass(Text.class);

			conf.setInputPath(new Path(input));
			conf.setOutputPath(outPath);

			conf.setMapperClass(UpdateDataMapper.class);
			//conf.setCombinerClass(FreqInitReducer.class);
			//conf.setReducerClass(FreqInitReducer.class);
			conf.setNumReduceTasks(1);//TODO change it later
			conf.setOutputFormat(TextOutputFormat.class);



			JobClient.runJob(conf);

			System.out.println("Done frupdatting data" );
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


}
