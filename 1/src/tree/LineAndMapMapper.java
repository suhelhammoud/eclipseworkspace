package tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.VLongWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import utils.MyWritable;
import utils.IntArrayWritable;

public class LineAndMapMapper extends MapReduceBase implements Mapper<WritableComparable,Text,IntWritable,IntArrayWritable> {
	
	int count=1;
	
	public void map(WritableComparable key, Text values,
			OutputCollector <IntWritable,IntArrayWritable> output, Reporter reporter) throws IOException {
		String[] items = values.toString().trim().split("[ ,\t\n]");
		List<String> lst=new ArrayList<String>();

		for (String item : items) {
			if(item.equals(""))continue;
			lst.add(item);

		}
		
		IntWritable[] iarr=new IntWritable[lst.size()];
		
		for (int i = 0; i < lst.size(); i++) {
			String string = lst.get(i);
			iarr[i]=new IntWritable(Integer.valueOf(string));
			
		}
		
		
		//IntArray  ia=new IntArray();
		IntArrayWritable outArray=new IntArrayWritable();
		outArray.set(iarr);
		
		
		output.collect(new IntWritable(count++), outArray);
	}
	
	public static void main(String[] args){
	}

}
