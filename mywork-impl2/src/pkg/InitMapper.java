package pkg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import javax.swing.text.StyledEditorKit.ForegroundAction;

//import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;


public class InitMapper extends MapReduceBase implements Mapper {


	public void map(WritableComparable key, Writable values,
			OutputCollector output, Reporter reporter) throws IOException {

		String line = values.toString().trim();
		PkgItem item=new PkgItem(null,line,1);

		//generate subitems 
		PkgItem[] items=item.subItems(true);
		for (PkgItem itm : items) {
			System.out.println("emmit:"+itm);
			output.collect(itm.collectKey(), itm.collect());
		}
	}

}
