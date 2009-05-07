import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.LineRecordReader;
import org.apache.hadoop.mapred.loadhistory_jsp;
import org.apache.hadoop.mapred.LineRecordReader.LineReader;

import com.sun.org.apache.bcel.internal.generic.LoadClass;



public class Kmeans {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		runApplication();



	}

	private static void runApplication() {
		// TODO Auto-generated method stub
		

		String pointsFile="data/in";
		String clustersFile="data/out/1/part-00000";

		
		//List<Point> clusters=loadClusters(clustersFile);

		
		CalcWichClusterDriver.runJob("data/in", "data/out/3/part-00000", "data/out/4");
		loadClusters("data/out/4/part-00000");
	}

	private static void recalculateTheCentersOfClusters() {
		// TODO Auto-generated method stub

	}

	//for each point in the input
	static void calcWhichCluster( String clustersFile, String pointsFile, String outClusterFile){
		
	}

	public static List<Point> loadClusters(String clusterFile) {
		List<Point> result=new ArrayList<Point>();

		List<String> lines=new ArrayList<String>();
		//clear();
		JobConf job=new JobConf();
		//configure(job);
		try {
			FileSystem fs = FileSystem.get(job);
			Path srcPath=new Path(clusterFile);
			if (! fs.exists(srcPath)) {
				System.out.println("No map file found");
				return result;
			}

			SequenceFile.Reader reader = new SequenceFile.Reader(fs, srcPath, job);

			Text key = new Text();
			Text value = new Text();
			while (reader.next(key, value)) {
				System.out.println("key "+key.toString()+" , value = "+value.toString());
				lines.add(value.toString());

			}

			reader.close();
		} catch (IOException e) {
		e.printStackTrace();
		return result;
	}
	
		for (String	line : lines) {
			line=line.trim();
			result.add(new Point(line));
			
		}
	System.out.println("resutl lines: "+ result);
	return result;

}

	public static List<Point> loadClusters2(String clusterFile) {
		List<Point> result=new ArrayList<Point>();

		List<String> lines=new ArrayList<String>();
		//clear();
		JobConf job=new JobConf();
		//configure(job);
		try {
			FileSystem fs = FileSystem.get(job);
			Path srcPath=new Path(clusterFile);
			if (! fs.exists(srcPath)) {
				System.out.println("No map file found");
				return result;
			}
			SequenceFile.Reader reader = new SequenceFile.Reader(fs, srcPath, job);

			Text key = new Text();
			Text value = new Text();
			while (reader.next(key, value)) {
				System.out.println("key "+key.toString()+" , value = "+value.toString());
				lines.add(value.toString());

			}

			reader.close();
		} catch (IOException e) {
		e.printStackTrace();
		return result;
	}
	
		for (String	line : lines) {
			line=line.trim();
			result.add(new Point(line));
			
		}
	System.out.println("resutl lines: "+ result);
	return result;

}

}

class Point{
	List<Double> xyz=new ArrayList<Double>();

	public Point(String raw){
		String[] strXyz=raw.split(" ");
		for (String string : strXyz) {
			double d=Double.valueOf(string);
			xyz.add(d);
		}
	}
	public Point(double[] xyz){
		for (int i=0;i<xyz.length;i++) {
			this.xyz.add(xyz[i]);
		}
	}
	public Point(List<Double> xyz){
		this.xyz=xyz;
	}

	double distance(Point point){
		double sum=0;
		for (int i = 0; i < xyz.size(); i++) {
			double tempD=xyz.get(i)-point.xyz.get(i);
			sum+=  tempD * tempD;
		}
		return Math.sqrt(sum);
	}

	public Point whichCluster( List<Point> clusters){
		double minD=Double.MAX_VALUE;
		Point result=clusters.get(0);

		for (Point point : clusters) {
			double d=distance(point);
			if ( d< minD){
				minD=d;
				result=point;
			}
		}
		return result;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer result=new StringBuffer(""+xyz.get(0));
		for (int i = 1; i < xyz.size(); i++) {
			result.append(" "+ xyz.get(i));
		}
		return result.toString();
	}
}
