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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InitMapper extends MapReduceBase implements
Mapper<WritableComparable, Text, Text, IntWritable> {



	private IntWritable one=new IntWritable(1);
	private Text word=new Text();

	public void map(WritableComparable key, Text values,
			OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		reporter.incrCounter(Driver.myCounters .NUMBER_OF_ROWS , 1);
		System.out.println(key.toString()+"\t"+values.toString());
		String[] items = values.toString().trim().split("[ ,\t\n]");
		for (String item : items) {
			if(item.equals(""))continue;
			word.set(item);
			output.collect(word, one);
		}	  
	}

}
