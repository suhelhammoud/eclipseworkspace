import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;


public class CalcWhichClusterReducer extends MapReduceBase implements Reducer<Text,Text,Text,Text> {

	Text outKey=new Text("cluster");
	public void reduce(Text key, Iterator<Text> values,
			OutputCollector<Text,Text> output, Reporter reporter) throws IOException {
		// key is the cluster point
		//value is a collection of points in this cluster
		Point cluster=new Point(key.toString());
		
		//temp list to hold the sums of x,y,z ..... of the points
		List<Double> totalXYZ_Sums=new ArrayList<Double>();
		for (Double x : cluster.xyz) {
			totalXYZ_Sums.add(0.0);
		}
		
		int howManyPoints=0;
		//foreach point in values add x,y,z to the sums 
		while (values.hasNext()) {
			Point point=new Point(values.next().toString());
			
			for (int i = 0; i < point.xyz.size(); i++) {
				totalXYZ_Sums.set(i, totalXYZ_Sums.get(i)+ point.xyz.get(i)) ;
			}
			howManyPoints++;
			
		}
		
		//calculate the average of each x,y,z .... save it in the sums list again
		for (int i = 0; i < totalXYZ_Sums.size(); i++) {
			System.out.print("\t"+ totalXYZ_Sums.get(i));
			totalXYZ_Sums.set(i,totalXYZ_Sums.get(i)/howManyPoints);
		}
		System.out.println();
		
		Point outCluster=new Point(totalXYZ_Sums);
		
		//emmit "cluster", new cluster point
		output.collect(outKey, new Text(outCluster.toString()));
		
	}

}
