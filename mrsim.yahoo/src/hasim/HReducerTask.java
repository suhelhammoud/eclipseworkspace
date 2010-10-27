package hasim;

import org.apache.log4j.Logger;


import addition.InMemFSMergeThread;
import addition.LocalFSMerger;

import hasim.core.HMachine;

public class HReducerTask extends HTask{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(HReducerTask.class);


	protected InMemFSMergeThread inMemFSMergeThread;
	public void setInMemFSMergeThread(InMemFSMergeThread inMemFSMergeThread) {
		this.inMemFSMergeThread = inMemFSMergeThread;
	}
//	public void setLocalFSMerger(LocalFSMerger localFSMerger) {
//		this.localFSMerger = localFSMerger;
//	}

	protected final LocalFSMerger localFSMerger;
	

	public HReducerTask(String name, int location) throws Exception {
		super(name, location);
		
		localFSMerger=new LocalFSMerger(name+"-localFSMerger");
		inMemFSMergeThread=new InMemFSMergeThread(name+"-inMemFSMergeThread");
	}
	public InMemFSMergeThread getInMemFSMergeThread() {
		return inMemFSMergeThread;
	}
	public LocalFSMerger getLocalFSMerger() {
		return localFSMerger;
	}

        
	
	
	
	public void stopEntity() {
		
		inMemFSMergeThread.stopEntity();
		localFSMerger.stopEntity();

		sim_schedule(get_id(), 0.0, HTAG.END_OF_SIMULATION);

		hlog.info("counters: "+ counters);
		hlog.save();
	}
	
	
}
