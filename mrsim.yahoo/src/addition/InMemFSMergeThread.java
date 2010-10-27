package addition;

import org.apache.log4j.Logger;

import dfs.Pair;



import hasim.HCopier;
import hasim.HLogger;
import hasim.HMergeQueue;
import hasim.HStory;
import hasim.HTAG;
import hasim.CopyObject.Type;
import hasim.core.Datum;
import hasim.core.HMachine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import eduni.simjava.Sim_entity;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;

public class InMemFSMergeThread extends Sim_entity {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(InMemFSMergeThread.class);

	private ReduceCopier copier;
	final HLogger hlog;
	final double heartbeat=1.0;
	public AtomicBoolean isMerging=new AtomicBoolean(false);
	List<Datum> pendingShuffles=new ArrayList<Datum>();

	private ShuffleRamManager ramManager;
	HLogger tLog;

	public InMemFSMergeThread(String name) {
		super(name);
		this.hlog=new HLogger(name);
	}

	public void body() {
		hlog.info("start entity");

		while (Sim_system.running()) {
			// if(jobsRunning.size()==0 && jobsWaiting.size()==0)

			Sim_event ev = new Sim_event();
			sim_get_next(ev);
			int tag = ev.get_tag();


			if (tag == HTAG.END_OF_SIMULATION) {
				hlog.info("END_OF_SIMULATION TAG", Sim_system.clock());
				logger.info(get_name()+" END_OF_SIMULATION "+ Sim_system.clock());
				break;
			}

			if( tag == HTAG.START.id()){
				tLog.info("MEMORY "+get_name() + "  Thread waiting: ");
				if(oneStory())
					break;
				else
					continue;
			}

		}
	}

	public Datum doInMemMerge() {

		copier.memoryLock.lock();

		isMerging.set(true);

		List<Datum> inMemorySegments=new ArrayList<Datum>(copier.mapOutputsFilesInMemory.get());
		tLog.info("MERGING "+ inMemorySegments.size());

		if (copier.mapOutputsFilesInMemory.get() == 0) {
			return null;
		}

		//name this output file same as the name of the first file that is 
		//there in the current list of inmem files (this is guaranteed to
		//be absent on the disk currently. So we don't overwrite a prev. 
		//created spill). Also we need to create the output file now since
		//it is not guaranteed that this file will be present after merge
		//is called (we delete empty files as soon as we see them
		//in the merge method)

		//figure out the mapId 

		double mergeOutputSize =  copier.mapOutputsFilesInMemory.get()* ReduceCopier.mapResultSizeRecords.getK();
		for (int i = 0; i < copier.mapOutputsFilesInMemory.get(); i++) {
			Datum d=new Datum(ReduceCopier.mapResultSizeRecords.getK(), ReduceCopier.mapResultSizeRecords.getV());
			d.setInMemory(true);
			inMemorySegments.add(d);
		}
		
		//		int noInMemorySegments = inMemorySegments.size();


//		HMergeQueue mrgQueue=new HMergeQueue(inMemorySegments);

		//		Datum outmrg= mrgQueue.mergeMapper(copier.ioSortFactor, inMemorySegments.size(),
		//				this, tLog, copier.getTask().getTaskTracker().getHdd(), copier.getCounters());

		Datum outmrg=HMergeQueue.mergeToHard(copier.ioSortFactor,inMemorySegments.size(), 
				this, tLog, HMachine.get(copier.getTask().location), copier.rcounters, 
				inMemorySegments,null);// copier.getJobinfo().getCombiner()

		//TODO update spilled records

		//        hdd.write(outmrg)

		

		tLog.info(copier + 
				" Merge of the " + inMemorySegments.size() +
				" files in-memory complete." +
				" Local file is " + outmrg.id+ " of size " + outmrg.size);


		// Note the output of the merge
		isMerging.set(false);

		for (Datum segmemt : inMemorySegments) {
			ramManager.unreserve(segmemt.size);			
		}

		inMemorySegments.clear();
		copier.mapOutputsFilesInMemory.set(0);

		ramManager.reset();

		assert outmrg != null;

		tLog.info("MERGING DONE "+ inMemorySegments.size());

		copier.memoryLock.unlock();
		return outmrg;
	}

	public ReduceCopier getCopier() {
		return copier;
	}


	public HLogger getHlog() {
		return hlog;
	}

	//	public Datum lastMergeAndJoin(String msg) {
	//		sim_schedule(get_id(), 0.0, HTAG.all_shuffles_finished.id());
	//		Sim_predicate p = new Sim_type_p(HTAG.all_shuffles_finished_return.id());
	//		Sim_event ev = new Sim_event();
	//
	//		sim_wait_for(p, ev);
	//		return (Datum)ev.get_data();
	//	}


	public void newStory(HStory rStory){


		hlog.info("new story:"+ rStory);

		this.copier=(ReduceCopier)rStory;

		this.tLog=copier.getHlog();
		this.ramManager=copier.getRamManager();
		this.pendingShuffles.clear();

		sim_schedule(get_id(), 0.0, HTAG.START.id());
	}

	public boolean oneStory(){

		int shuffleCount=1;
		List<Integer> pendingShuffles=new ArrayList<Integer>();
		
		while (Sim_system.running()) {
			Sim_event ev = new Sim_event();
			sim_get_next(ev);
			int tag = ev.get_tag();

			if (tag == HTAG.END_OF_SIMULATION) {
				hlog.info("MEMORY END_OF_SIMULATION TAG");
				return true;
			}

			if (tag == HTAG.shuffle.id()){
				Integer mapOutputLoc=(Integer)ev.get_data();				
				ramManager.incNumPendingRequests();
				pendingShuffles.add(mapOutputLoc);
				tryMerge();
				tryShuffle(pendingShuffles);
			}


			if ( tag == HTAG.shuffle_return.id()){
				
				tLog.info("shuffle resutrn: "+ shuffleCount);
				shuffleCount++;
				
				Pair<Integer, Boolean> pair= (Pair)ev.get_data();
				Datum mapOutput = new Datum(copier.mapResultSizeRecords.getK(),
						copier.mapResultSizeRecords.getV());
				mapOutput.setLocation(pair.getK());
				assert pair.getV()==true;
				mapOutput.setInMemory(true);
				

//				Datum mapOutput=(Datum)ev.get_data();

				// Close the in-memory file
				ramManager.closeInMemoryFile(mapOutput.size);

				// Note that we successfully copied the map-output
				copier.mapOutputsFilesInMemory.incrementAndGet();

				tLog.info("MEMORY return one in-memory shuffle: "+ mapOutput);
				copier.noteCopiedMapOutput(mapOutput);

				sim_schedule(copier.getTask().get_id(), 0.0, HTAG.shuffle_return.id(), pair);
				
				tryMerge();
				tryShuffle(pendingShuffles);

			}

		}
		return false;
	}


	private void tryShuffle(List<Integer> pendingShuffles){
		for (Iterator<Integer> iterator = pendingShuffles.iterator(); iterator
		.hasNext();) {
			Integer datum = iterator.next();
			if (ramManager.canReserve(ReduceCopier.mapResultSizeRecords.getK())) {
				shuffleInMemory(datum);
				iterator.remove();
				//				ramManager.decNumPendingRequests();
			}else{
				break;
			}

		}
	}
	
	private void tryMerge(){
		if(! ramManager.waitForDataToMerge() ){
			tLog.info("MEMORY waitforDataToMerge: "+ ramManager.waitForDataToMerge());
			Datum outmrg=doInMemMerge();
			assert outmrg !=null;
			copier.addFileOnDisk(outmrg);
		}
	}

	public void setCopier(ReduceCopier copier) {
		this.copier = copier;
	}

	public void setHlog(HLogger hlog) {
		this.tLog = hlog;
	}



	private void shuffleInMemory(Integer mapOutputLoc) {
		
		double mapOutputLength= ReduceCopier.mapResultSizeRecords.getK();
		ramManager.decNumPendingRequests();
		// Reserve ram for the map-output
		boolean isReserved = ramManager.reserve(mapOutputLength);

		assert isReserved;

		// Are map-outputs compressed?
		if (copier.getJobinfo().getJob().isUseCompression()) {
			//TODO do compression code
		}

//		Datum mapOutput = new Datum(mapOutputLoc, true);
//		mapOutput.setInMemory(true);

		tLog.info("Read " + mapOutputLength + " bytes from map-output for "
				+ mapOutputLoc );

		HCopier hcopier=copier.getTask().getJobTracker().getCopier();
		hcopier.copy(mapOutputLoc, copier.getTask().location,
				mapOutputLength, this, HTAG.shuffle_return.id(),
				new Pair<Integer, Boolean>(mapOutputLoc, true), Type.hard_mem);
	}

	public void stopEntity() {
		sim_schedule(get_id(), 0.0, HTAG.END_OF_SIMULATION);
		//		hlog.info("counters: "+ counters);
		hlog.save();
	}
}



