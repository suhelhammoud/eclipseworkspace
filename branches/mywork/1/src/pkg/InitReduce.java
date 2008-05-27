package pkg;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;


public class InitReduce extends MapReduceBase implements Reducer {

	public void reduce(WritableComparable key, Iterator values,
			OutputCollector output, Reporter reporter) throws IOException {

		HashMap<String,PkgItem> map=new HashMap<String, PkgItem>();
		while (values.hasNext()) {
			String str= ((Text)values.next()).toString();
			//System.out.println("reduce"+str);
			PkgItem item=new PkgItem(str);

			PkgItem temp=map.get(item.id());
			if(temp != null){
				temp.inc(item.getFreq());
				//System.out.println(temp+ " ,freq= +"+temp.getFreq());
				map.put(item.id(),temp);
			}else
				map.put(item.id(), item);
		}
		if (map.size() < Util.supp){
			
			output.collect(new Text("-"+key), new Text(""+map.size()));
			return;
		}else{
			for (PkgItem itm : map.values()) {

				int base=map.size();
				int supp=itm.getFreq();
				double conf=itm.confidence(base);
				String prefix=" ";
				
				if(itm.vLength()==1){
					if(supp>=Util.supp)
						if (conf >Util.conf)
							prefix="-*";
						
					else
						prefix="-";
					output.collect(itm.collect(prefix), new Text(""+conf));
				}else{
					if(supp>=Util.supp)
						if (conf >Util.conf)
							prefix="*";
						else
							prefix=" ";
					else
						prefix="-";
					output.collect(itm.collectKey(prefix), itm.collect(conf));
				}
			}
		}

	}
}
