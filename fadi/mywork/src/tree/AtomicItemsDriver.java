package tree;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

import utils.SetWritable;

public class AtomicItemsDriver {

	public static void main(String[] args) {
		runAtomicItem();
	}

	public static void runAtomicItem() {
		JobClient client = new JobClient();
		JobConf conf = new JobConf(AtomicItemsDriver.class);
		conf.setInt("SUPPORT",0);

		// TODO: specify output types
		conf.setOutputKeyClass(SetWritable.class);
		conf.setOutputValueClass(SetWritable.class);

		conf.setMapOutputKeyClass(IntWritable.class);
		conf.setMapOutputValueClass(SetWritable.class);
		
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(SequenceFileOutputFormat.class);
		//conf.setOutputFormat(TextOutputFormat.class);

		// TODO: specify input and output DIRECTORIES (not files)
		conf.setInputPath(new Path("data/input_lined"));

		Path outpath=new Path("data/tree/items/1");
		conf.setOutputPath(outpath);
	
		try {
			FileSystem fs = FileSystem.get(conf);
			if (fs.exists(outpath)) {
				fs.delete(outpath);
			}
		} catch (Exception e) {e.printStackTrace();}

		conf.setMapperClass(AtomicItemsMapper.class);
		conf.setCombinerClass(AtomicItemsCombiner.class);
		conf.setReducerClass(AtomicItemsReducer.class);

		client.setConf(conf);
		try {
			JobClient.runJob(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
