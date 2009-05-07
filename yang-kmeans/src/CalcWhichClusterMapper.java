import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;


public class CalcWhichClusterMapper extends MapReduceBase implements Mapper<WritableComparable,Text,Text,Text> {

	@Override
	public void configure(JobConf job) {
		// TODO Auto-generated method stub
		String clusterFile=job.get("clusterFile");
		clusters=Kmeans.loadClusters(clusterFile);
	}

	List<Point> clusters=new ArrayList<Point>();
	
	public void map(WritableComparable key, Text values,
			OutputCollector<Text,Text> output, Reporter reporter) throws IOException {
		//read the line and create  point instance from it
		Point point =new Point(values.toString());
		
		//get the closest cluster point among the list(clusters)
		Point cluster= point.whichCluster(clusters);
		
		//emmitt key = closest cluster Point
		//	     value= this point		
		output.collect(new Text(cluster.toString()), new Text(point.toString()));
	}

}
