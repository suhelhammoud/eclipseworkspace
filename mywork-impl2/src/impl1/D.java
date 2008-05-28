package impl1;

import java.util.Date;

import org.apache.hadoop.mapred.JobConf;
import org.apache.log4j.Logger;




public class D {
	static Logger log=Logger.getLogger(D.class);


	public static void main(String[] args) {

//		int totalSize=9;
//		double rSupport=0.0;
//		double confidence=0.00;
		
		int totalSize=990002;
		double rSupport=0.1;
		double confidence=0.10;

		int support=(int)(totalSize* rSupport);
		System.out.println("min support ="+ support);

		//support=2;

		String freqDir="data/freqs";
		String candidtatesDir="data/candidates";

		//get 
		Param param=new Param();
		param.numOfMappers=2;
		param.numOfReduces=1;
		param=null;
		
		long t_start=D.tic();
		long t=D.tic();
		InputMapper.runJob("data/input_lined", "data/input", param);
		System.out.println("InputMapper ,"+D.toc(t)+", "+D.toc(t_start));

		t=D.tic();
		ZeroMapper.runJob("data/input", freqDir+"/1", support, param);
		System.out.println("ZerroMapper ,"+D.toc(t)+", "+D.toc(t_start));

		t=D.tic();
		RemoveNotFrequentMapper.runJob("data/input","data/input_removed",freqDir, param);
		System.out.println("RemoveNotFrequentMapper ,"+D.toc(t)+", "+D.toc(t_start));

		
		for(int i=2; ; i++){
			
			t=D.tic();
			int candidates_sets=prepare(freqDir, candidtatesDir, i);
			System.out.println("prepare "+i+" candidate sets "+ candidates_sets+ " "+D.toc(t)+" "+D.toc(t_start));

			t=D.tic();
			int itemsLeft=IMapper.runJob("data/input_removed", freqDir, candidtatesDir , i, support, param);
			System.out.println("IMapper.runJob "+i+","+D.toc(t)+", "+D.toc(t_start));
			
			System.out.println("rows left "+itemsLeft );

			if(itemsLeft < 1)break;


		}

		t=D.tic();
		RMapper.runJob("data/freqs/*", "data/rules", totalSize, (float)confidence, param);
		System.out.println("RMapper.runJob , " +D.toc(t)+", "+D.toc(t_start));

		t=D.tic();
		RSortMapper.runJob("data/rules", "data/rules_sorted", param);
		System.out.println("RSortMapper.runJob , " +D.toc(t)+", "+D.toc(t_start));


	}

	public static int prepare(String dataIn, String dataOut,int iteration){
		JobConf conf=new JobConf();
		ItemMap im=new ItemMap();
		im.load(dataIn+"/"+(iteration-1), conf);
		return im.prepareNext(dataOut+"/"+iteration,conf );
		
	}
	public static long tic(){
		return new Date().getTime();
	}
	public static long toc(long tic){
		return (new Date().getTime())-tic;
	}

}
