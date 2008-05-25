package tree;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.log4j.Logger;

import utils.SetWritable;
import utils.SuperSetWritable;

public class ToLineReducer extends MapReduceBase implements Reducer<IntWritable,SetWritable,IntWritable,SuperSetWritable> {

	static Logger log=Logger.getLogger(ToLineReducer.class);
	
	public void reduce(IntWritable key, Iterator<SetWritable> values,
			OutputCollector<IntWritable,SuperSetWritable> output, Reporter reporter) throws IOException {

		SuperSetWritable superSet=new SuperSetWritable();
		
		HashMap<SetWritable, Integer> map=new HashMap<SetWritable, Integer>();
		HashSet<SetWritable> oldValues=new HashSet<SetWritable>(); 
		
		while (values.hasNext()) {
			
			SetWritable value=values.next();
			
			//log.info("value "+value+ ", oldValues "+oldValues.size());
			if(oldValues.size()== 0){
				oldValues.add(value);
				continue;
			}
			
			for (SetWritable item : oldValues) {
				//log.info("test merg "+value+ "^"+item);
				SetWritable mrgSet=value.merg(item);
				if(mrgSet == null)continue;
				//log.info("mrgset "+mrgSet);
				if(map.containsKey(mrgSet)){
					map.put(mrgSet, map.get(mrgSet)+1);
					continue;
				}
				map.put(mrgSet, 1);
				superSet.add(mrgSet);
			}
			
			oldValues.add(value);
			//superSet.add(values.next());
		}
		if(superSet.size() !=0)
			output.collect(key, superSet);
		//log.info(key+"->"+superSet);
		
	}

}
