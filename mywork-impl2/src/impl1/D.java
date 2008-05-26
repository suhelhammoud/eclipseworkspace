package impl1;

import java.util.Date;

import org.apache.hadoop.mapred.JobConf;
import org.apache.log4j.Logger;




public class D {
	static Logger log=Logger.getLogger(D.class);
	//public static enum myCounters {NUMBER_OF_ROWS,ROWS_LEFT} 


	public static void main(String[] args) {

		int totalSize=9;
		double rSupport=0.0;
		double confidence=0.00;
		int support=(int)(totalSize* rSupport);
		System.out.println("min support ="+ support);

		//support=2;

		String freqDir="data/freqs";
		String candidtatesDir="data/candidates";

		//get 
		Param param=new Param();
		param.numOfMappers=2;
		param.numOfReduces=1;

		//InputMapper.runJob("data/input_lined", "data/input", param);


		ZeroMapper.runJob("data/input", freqDir+"/1", support, param);

		//RemoveNotFrequentMapper.runJob("data/input","data/input_removed",freqDir, param);

		
		for(int i=2; ; i++){
			prepare(freqDir, candidtatesDir, i);
			int itemsLeft=IMapper.runJob("data/input_removed", freqDir, candidtatesDir , i, support, param);
			System.out.println("rows left "+itemsLeft );

			if(itemsLeft < 1)break;
			


		}

		RMapper.runJob("data/freqs/*", "data/rules", totalSize, (float)confidence, param);
		RSortMapper.runJob("data/rules", "data/rules_sorted", param);


	}

	public static void prepare(String dataIn, String dataOut,int iteration){
		JobConf conf=new JobConf();
		ItemMap im=new ItemMap();
		im.load(dataIn+"/"+(iteration-1), conf);
		im.prepareNext(dataOut+"/"+iteration,conf );
		
	}
	public static long tic(){
		return new Date().getTime();
	}
	public static long toc(long tic){
		return (new Date().getTime())-tic;
	}

}
