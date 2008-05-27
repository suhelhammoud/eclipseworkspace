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

import utils.Item;
import utils.IntArrayWritable;
import utils.SetWritable;

public class AtomicItemsMapper extends MapReduceBase implements Mapper<WritableComparable,Text,IntWritable,SetWritable> {
	
	IntWritable k=new IntWritable();
	IntWritable v=new IntWritable();
	
	public void map(WritableComparable key, Text values,
			OutputCollector <IntWritable,SetWritable> output, Reporter reporter) throws IOException {
		String[] txt=values.toString().split(":");
		String[] items = txt[1].trim().split("[ ,\t\n]");
		
		IntWritable k=new IntWritable();
		SetWritable v=new SetWritable();
		v.add(Integer.valueOf(txt[0]));
		

		for (int i = 0; i < items.length; i++) {
			k.set(Integer.parseInt(items[i]));
			output.collect(k, v);
		}
		
	}
	
	public static void main(String[] args){
	}

}
