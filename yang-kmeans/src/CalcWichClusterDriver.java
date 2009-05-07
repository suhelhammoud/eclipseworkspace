import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;


public class CalcWichClusterDriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
	}
	public static void runJob(String pointsInputFile, String clustersInputFile, String clustersOutputFile){
		try {
			JobConf conf = new JobConf(CalcWhichClusterMapper.class);
			conf.setJobName("CalcWhichClusterMapper");
			
			conf.set("clusterFile", clustersInputFile);
			Path outPath = new Path(clustersOutputFile);
			FileSystem fs = FileSystem.get(conf);
			//TODO remove later,
			if (fs.exists(outPath)) {
				fs.delete(outPath);
			}

			conf.setOutputKeyClass(Text.class);
			conf.setOutputValueClass(Text.class);

			conf.setInputPath(new Path(pointsInputFile));
			conf.setOutputPath(outPath);

			conf.setMapperClass(CalcWhichClusterMapper.class);
			
			// conf.setCombinerClass(ICombiner.class);
			conf.setReducerClass(CalcWhichClusterReducer.class);

			conf.setInputFormat(TextInputFormat.class);
		
			conf.setOutputFormat(SequenceFileOutputFormat.class);
			//conf.setNumReduceTasks(0);//TODO change it later

			RunningJob rj= JobClient.runJob(conf);


		} catch (Exception e) {
			e.printStackTrace();

		}               

	}


}
