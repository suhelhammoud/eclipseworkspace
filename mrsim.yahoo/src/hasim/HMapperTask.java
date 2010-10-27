package hasim;

import hasim.core.HMachine;

import org.apache.log4j.Logger;

import eduni.simjava.Sim_stat;


	

public class HMapperTask extends HTask{
//	public enum Phase {
//		MAP, REDUCE, SHUFFLE
//	}


	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(HMapperTask.class);

	//	HCombiner combiner;

	


	
	public HMapperTask(String name, int location) throws Exception {
		super(name, location);
	}
	
	
	
}
