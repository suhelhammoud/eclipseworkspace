package tree;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.VLongWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class LineMapper extends MapReduceBase implements Mapper<WritableComparable,Text,IntWritable,Text> {
	
	int count=1;
	public void map(WritableComparable key, Text values,
			OutputCollector <IntWritable,Text> output, Reporter reporter) throws IOException {
		String[] items = values.toString().trim().split("[ ,\t\n]");
		StringBuffer sb=new StringBuffer();
		for (String item : items) {
			if(item.equals(""))continue;
			sb.append(item+" ");
		}	  
		
		output.collect(new IntWritable(count++), new Text(sb.toString().trim()));
	}

}
