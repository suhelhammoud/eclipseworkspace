/* Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package init;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.log4j.Logger;

import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdateDataMapper extends MapReduceBase implements
Mapper<WritableComparable, Text, Text, Text> {

	static Logger log=Logger.getLogger(UpdateDataMapper.class);
	private HashMap<String, Integer> fmap=new HashMap<String, Integer>();
	//private final static int supp=0;

	private IntWritable one=new IntWritable(1);
	private Text word=new Text();
	private static Text noneWord=new Text();

	public void map(WritableComparable key, Text values,
			OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
		
		String[] items = values.toString().split("[ \t\n]");
		if(items.length==0)return;
		StringBuffer result=new StringBuffer();
		int itemLength=0;
		for (String item : items) {
			if(! fmap.containsKey(item))continue;
			if(item.equals(""))continue;			
			result.append(item+" ");
			itemLength++;
		}
//		log.info(result);
//		log.info("lenght ="+items.length );
		if(itemLength==0){
			log.debug(result);
			return;
		}
		result.deleteCharAt(result.length()-1);

		word.set(result.toString());
		output.collect(word, noneWord);
		
	}

	@Override
	public void configure(JobConf job) {
		// TODO Auto-generated method stub
		super.configure(job);
		fmap=FMap.getFMap(job, job.get("freqPath", "data/freqs/1"));		
	}


}
