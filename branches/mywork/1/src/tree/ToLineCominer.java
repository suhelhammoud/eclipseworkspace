package tree;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class ToLineCominer extends MapReduceBase implements Reducer {

	public void reduce(WritableComparable key, Iterator values,
			OutputCollector output, Reporter reporter) throws IOException {
	}

}
