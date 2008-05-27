package pkg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

//import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;


public class MyMapper extends MapReduceBase implements Mapper {

	//private final static IntWritable one = new IntWritable(1);
	public void map(WritableComparable key, Writable values,
			OutputCollector output, Reporter reporter) throws IOException {
		String[] line=values.toString().split(" ");

		//Item item=new Item(line[0]);

		//Item[] items=item.subItems();
		for (String itm : line) {
			System.out.println("emmit:"+itm);
			output.collect(new Text(itm), new Text("-"));
		}
		

	}

}
