package impl1;

import java.util.Date;

import org.apache.log4j.Logger;




public class D {
	static Logger log=Logger.getLogger(D.class);
	//public static enum myCounters {NUMBER_OF_ROWS,ROWS_LEFT} 


	public static void main(String[] args) {

		int totalSize=1692082;
		double rSupport=0.05;
		double confidence=0.40;
		int support=(int)(totalSize* rSupport);
		System.out.println("min support ="+ support);

		//support=2;

		String freqDir="data/freqs";

		//get 
		Param param=new Param();
		param.numOfMappers=2;
		param.numOfReduces=1;
		//TODO to be deleted later 
		param=null;

		InputMapper.runJob("data/input_lined", "data/input", param);


		ZeroMapper.runJob("data/input", freqDir+"/1", support, param);

		RemoveNotFrequentMapper.runJob("data/input","data/input_removed",freqDir, param);

		for(int i=2; ; i++){
			int itemsLeft=IMapper.runJob("data/input_removed", "data/freqs" , i, support, param);
			System.out.println("rows left "+itemsLeft );

			if(itemsLeft < 1)break;

		}

		RMapper.runJob("data/freqs/*", "data/rules", totalSize, (float)confidence, param);
		RSortMapper.runJob("data/rules", "data/rules_sorted", param);


	}


	public static long tic(){
		return new Date().getTime();
	}
	public static long toc(long tic){
		return (new Date().getTime())-tic;
	}

}
