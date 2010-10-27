package addition;

import org.apache.log4j.Logger;

import dfs.Pair;

import hasim.CTag;
import hasim.HCounter;
import hasim.HJobTracker;
import hasim.HLogger;
import hasim.HLoggerInterface;
import hasim.HMapperStory;
import hasim.HMergeQueue;
 import hasim.HReducerTask;
import hasim.HStory;
import hasim.HTAG;
import hasim.HTask;
import hasim.JobInfo;
import hasim.HTask.Status;
import hasim.core.Datum;
import hasim.core.HDD;
import hasim.core.HMachine;
import hasim.json.JsonJob;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import eduni.simjava.Sim_entity;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_predicate;
import eduni.simjava.Sim_system;
import eduni.simjava.Sim_type_p;

public class ReduceCopier extends HStory implements HLoggerInterface{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ReduceCopier.class);

	public static HLogger hlog;

	final public static double INIT_TIME=3.0;
	

	public Lock lock=new ReentrantLock(true);
	
	public final Lock memoryLock=new ReentrantLock(true);
	
	public int progress;
	
	// JsonJob conf;
	//TODO set values
	public static int numMaps;


	public int getNumMaps() {
		return numMaps;
	}



    static HCounterValue reduceShuffleBytes; 

	static HCounterValue reduceFileWrittenBytes;
	static HCounterValue reduceSpilledRecords;

	

	// A sorted set for keeping a set of map output files on disk

	Lock onDiskLock = new ReentrantLock(true);
	
	private Comparator<Datum> mapOutputFileComparator = new Comparator<Datum>() {
		public int compare(Datum a, Datum b) {
			
			double dif=a.size-b.size;
			if(dif != 0)
				return (int)Math.signum(dif);
			else{
				return a.id-b.id;
			}
		}
	};

	// A sorted set for keeping a set of map output files on disk

	public final SortedSet<Datum> mapOutputFilesOnDisk = new TreeSet<Datum>(
			mapOutputFileComparator);

	 
	/**
	 * our reduce task instance
	 */
//	HTask reduceTask;

	/**
	 * the list of map outputs currently being copied
	 */
	final List<Integer> scheduledCopies;
	public static Pair<Double, Double> mapResultSizeRecords=null;
	
	public 

	/**
	 * the results of dispatched copy attempts
	 */
	final List<Integer> copyResults;

	/**
	 * the number of outputs to copy in parallel
	 */
	public static int numCopiers;

	/**
	 * a number that is set to the max #fetches we'd schedule and then pause the
	 * schduling
	 */
	static private int maxInFlight;

	/**
	 * the set of unique hosts from which we are copying
	 */
	private Set<String> uniqueHosts;

	/**
	 * A reference to the RamManager for writing the map outputs to.
	 */

	final protected ShuffleRamManager ramManager;

	public ShuffleRamManager getRamManager() {
		return ramManager;
	}


	/**
	 * Number of files to merge at a time
	 */
	static int ioSortFactor;

	/**
	 * A flag to indicate when to exit localFS merge
	 */
	boolean exitLocalFSMerge = false;




	/**
	 * Maximum memory usage of map outputs to merge from memory into the reduce,
	 * in bytes.
	 */
	static double maxInMemReduce;

	/**
	 * The set of required map outputs
	 */
	final Set<Datum> copiedMapOutputs = Collections
	.synchronizedSet(new TreeSet<Datum>());

	

	

	/**
	 * The interval for logging in the shuffle
	 */
	private static final double MIN_LOG_TIME = 6.0;

	/**
	 * List of in-memory map-outputs.
	 */
//	final List<Datum> mapOutputsFilesInMemory = Collections
//	.synchronizedList(new LinkedList<Datum>());
	final AtomicInteger mapOutputsFilesInMemory=new AtomicInteger();

	//	int nextMapOutputCopierId = 0;

	public static void setParams(JsonJob job){
		
		numMaps=job.getNumberOfMappers();
		
		numCopiers=job.getMapReduceParallelCopies();
		maxInFlight = 4 * numCopiers;
		// this.maxBackoff = conf.getInt("mapred.reduce.copy.backoff", 300);
//		HCounterValue combineInputCounter = counters
//		.hValue(CTag.COMBINE_INPUT_RECORDS);

		

		ioSortFactor = job.getIoSortFactor();
		// the exponential backoff formula
		// backoff (t) = init * base^(t-1)
		// so for max retries we get
		// backoff(1) + .... + backoff(max_fetch_retries) ~ max
		// solving which we get
		// max_fetch_retries ~ log((max * (base - 1) / init) + 1) / log(base)
		// for the default value of max = 300 (5min) we get max_fetch_retries =
		// 6
		// the order is 4,8,16,32,64,128. sum of which is 252 sec = 4.2 min

		// optimizing for the base 2




		// conf.getFloat("mapred.job.reduce.input.buffer.percent", 0f);
		final double maxRedPer = job.getMapredJobReduceInputBufferPercent();

		assert maxRedPer <= 1.0 &&  maxRedPer >= 0.0;

		maxInMemReduce = job.getMapredChildJavaOpts() * maxRedPer;

		// hostnames


		//init counters
		reduceShuffleBytes = rcounters
		.hValue(CTag.SHUFFLE);

		reduceFileWrittenBytes = rcounters
		.hValue(CTag.FILE_BYTES_WRITTEN);

		reduceSpilledRecords = rcounters
		.hValue(CTag.SPILLED_RECORDS);


		//reduceCombineOutputCounter =
		// counters.hValue(CTag.COMBINE_OUTPUT_RECORDS);
	}
	public ReduceCopier(int name) {
		super(name);



		this.scheduledCopies = new ArrayList<Integer>(100);
		this.copyResults = new ArrayList<Integer>(100);

		this.ramManager = new ShuffleRamManager(job,this.numCopiers, hlog);

	

	}

	Lock schedCopyLock = new ReentrantLock(true);

	//TODO set inmemroy to false
	private double remaining=0;
	public static double RATIO=1.0;
	public int mapsAdded=0;
	synchronized public void addScheduledCopy(int resultLocation) {
		hlog.info("add map to copy , datum location =" + resultLocation);
		remaining+= RATIO;
//		System.out.println(remaining+ ", r="+RATIO);
		
		schedCopyLock.lock();
		mapsAdded+=(int)remaining;
		for (int i = 0; i < (int)remaining; i++) {
			scheduledCopies.add(resultLocation);
		}
		remaining-= (int)remaining;
		schedCopyLock.unlock();
	}
	
	synchronized public void shuffleCopies(){
		schedCopyLock.lock();
		Collections.shuffle(scheduledCopies);
		schedCopyLock.unlock();
	}

	synchronized public int removeScheduledCopy() {
		schedCopyLock.lock();
//		hlog.info("remove scheduledCopy "+ scheduledCopies.size());
		if (scheduledCopies.size() == 0)
			return Datum.LOCATION_NONE;
		Integer result = scheduledCopies.remove(0);
		schedCopyLock.unlock();
		return result;
	}

//	synchronized public int getScheduledCopisSize() {
//		schedCopyLock.lock();
//		int result = scheduledCopies.size();
//		schedCopyLock.unlock();
//		return result;
//	}

	private boolean busyEnough(int numInFlight) {
		return numInFlight > maxInFlight;
	}


	// int mapsCopied

	public void process() {

		
		int parallelCopies = numCopiers;

		final double heartbeat = 1.0+task.getJobTracker().getHeartbeat()*2;

		hlog.info("parallelCopies=" + parallelCopies + ", heartbeat="
				+ heartbeat + ",numMaps" + numMaps);
		// fetch flight
		int numInFlight = 0, numCopied = 0;
		// start the clock for bandwidth measurement
		double startTime = Sim_system.clock();
		double currentTime = startTime;
		double lastOutputTime = 0;
		int lastLog=Integer.MAX_VALUE;



		while (Sim_system.running()) {

			task.sim_schedule(task.get_id(), heartbeat,
					HTAG.reducer_check_mappers_out.id());

			Sim_event ev = new Sim_event();
			Sim_predicate p=new Sim_type_p(
					HTAG.reducer_check_mappers_out.id(),
					HTAG.shuffle_return.id(),
					HTAG.END_OF_SIMULATION);
			task.sim_get_next(p,ev);
			//			task.sim_get_next(ev);

			int tag = ev.get_tag();
			
			if (tag == HTAG.END_OF_SIMULATION){
				logger.info(task.get_name()+" END_OF_SIMULATION "+ Sim_system.clock());
				return;
			}

			if (tag == HTAG.reducer_check_mappers_out.id()) {


				if (copiedMapOutputs.size() < numMaps) {

					currentTime = Sim_system.clock();
					boolean logNow = false;
					if (currentTime - lastOutputTime > MIN_LOG_TIME &&
							lastLog != copiedMapOutputs.size()) {
						lastOutputTime = currentTime;
						lastLog=copiedMapOutputs.size();
						logNow = true;
						
					}
					if (logNow) {
						String logMsg = " Need another "
						+ (numMaps - copiedMapOutputs.size())
						+ " map output(s) " + "where " + numInFlight
						+ " is already in progress";
						hlog.info(logMsg);
					}
				} else {
					hlog.info("copiedMapOUtputs.size()==numMaps=="+copiedMapOutputs.size()+" , break");
					break;
				}

				while (parallelCopies > 0 && scheduledCopies.size() > 0) {

					int mapOutputLoc = removeScheduledCopy();
					hlog.info("remove scheduledCopy "+ scheduledCopies.size()+
							", parallelCopies="+parallelCopies+
							", pendingRequest="+ramManager.getNumPendingRequests()+
							", percent="+ramManager.getPercentUsed()+
							", percentSize="+ramManager.getPercentSize());

					assert mapOutputLoc != Datum.LOCATION_NONE;

					parallelCopies--;


					// We will put a file in memory if it meets certain
					// criteria:
					// 1. The size of the (decompressed) file should be less
					// than 25% of
					// the total inmem fs
					// 2. There is space available in the inmem fs

					// Check if this map-output can be saved in-memory
					boolean shuffleInMemory = ramManager
					.canFitInMemory(mapResultSizeRecords.getK());

					// Shuffle
					if (shuffleInMemory) {
						hlog.info("Shuffling " + mapResultSizeRecords.getK() 
								+ " bytes into RAM from "+ mapOutputLoc);

						task.sim_schedule(inMemFSMergeThread.get_id(),
								0.0, HTAG.shuffle.id(), mapOutputLoc);

					} else {
						hlog.info("Shuffling " + mapResultSizeRecords.getK() 
								+ " bytes into Local-FS from "+ mapOutputLoc);

						task.sim_schedule(localFSMergerThread.get_id(),
								0.0, HTAG.shuffle.id(), mapOutputLoc);
					}

				}

				continue;
			}


			if (tag == HTAG.shuffle_return.id()) {

				hlog.info("shuffle return mapOutputFilesOnDisk.size():"+mapOutputFilesOnDisk.size());
				Pair<Integer, Boolean> cr = (Pair) ev.get_data();

				numCopied++;
				reduceShuffleBytes.inc(ReduceCopier.mapResultSizeRecords.getK());
				if(! cr.getV()){
					reduceFileWrittenBytes.inc(ReduceCopier.mapResultSizeRecords.getK());
					reduceSpilledRecords.inc(ReduceCopier.mapResultSizeRecords.getV());
				}

				double secsSinceStart = Sim_system.clock() - startTime;

				double mbs = (reduceShuffleBytes.get()) / (1024 * 1024);
				double transferRate = mbs / secsSinceStart;

				numInFlight--;

				parallelCopies++;

				if(numCopied ==numMaps){
					
					ramManager.close();
					hlog.info("copier break");
					break;
				}

					progress=numCopied;
				continue;

			}


		}

		exitLocalFSMerge = true;
		ramManager.close();

		// Do a merge of in-memory files (if there are any)
		
		
		// Wait for the on-disk merge to complete
		hlog.info("Interleaved on-disk merge complete: "
				+ mapOutputFilesOnDisk.size() + " files left.");
//		Datum dDisk=localFSMergerThread.lastMergeAndJoin("msg");
//		addFileOnDisk(dDisk);

		// wait for an ongoing merge (if it is in flight) to complete
		hlog.info("In-memory merge complete: "
				+ mapOutputsFilesInMemory + " files left.");
//		Datum dRam=inMemFSMergeThread.lastMergeAndJoin("msg");
//		
		
		int numMemDiskSegments=mapOutputsFilesInMemory.get();
		double inMemToDiskBytes=mapOutputsFilesInMemory.get() * mapResultSizeRecords.getK();
		
		
		if (mapOutputsFilesInMemory.get() > 0 &&
	              ioSortFactor > mapOutputFilesOnDisk.size()){
			
	          // must spill to disk, but can't retain in-mem for intermediate merge
			
			List<Datum> mapOutputInMemList=new ArrayList<Datum>(mapOutputsFilesInMemory.get());
			for (int i = 0; i < mapOutputsFilesInMemory.get(); i++) {
				Datum d=new Datum(mapResultSizeRecords.getK(), mapResultSizeRecords.getV());
				d.setInMemory(true);
				mapOutputInMemList.add(d);
			}
			
			hlog.info("Merged " + numMemDiskSegments + " segments, " +
	                   inMemToDiskBytes + " bytes to disk to satisfy " +
	                   "reduce memory limit");
			
			Datum outd = HMergeQueue.mergeToHard(ioSortFactor,
					mapOutputInMemList.size(), task, hlog, HMachine.get(task.location) , rcounters,
							mapOutputInMemList, null);// jobinfo.getCombiner()
			mapOutputInMemList.clear();
			mapOutputsFilesInMemory.set(0);
			outd.setInMemory(false);
			addFileOnDisk(outd);
			
		}else{
			  hlog.info("Keeping " + numMemDiskSegments + " segments, " +
	                   inMemToDiskBytes + " bytes in memory for " +
	                   "intermediate, on-disk merge");
			  for (int i=0; i< mapOutputsFilesInMemory.get(); i++) {
				  Datum inmemdat=new Datum(mapResultSizeRecords.getK(), mapResultSizeRecords.getV());
				inmemdat.setInMemory(true);
				addFileOnDisk(inmemdat);
			}
			
		}
		
		//merge memory and disk interleaved data to mem, for reducing
		Datum outToReduce=HMergeQueue.mergeToMem(ioSortFactor, 	numMemDiskSegments(mapOutputFilesOnDisk),
				task, hlog, HMachine.get(task.location),
				rcounters, mapOutputFilesOnDisk, null);//jobinfo.getCombiner()
		
		
		/////
		hlog.info("reduce phase:"+outToReduce);
		Datum result=reduce(outToReduce.size, outToReduce.records);
		hlog.info("reduce result:"+ result);
		setStatus(Status.finished);

	}
	
	

	LocalFSMerger localFSMergerThread = null;
	InMemFSMergeThread inMemFSMergeThread = null;

	public static int numMemDiskSegments(Collection<Datum> segmets){
		int result=0;
		for (Datum d : segmets) {
			if(d.isInMemory())
				result++;
		}
		return result;
	}

	
	/**
	 * Save the map taskid whose output we just copied. This function assumes
	 * that it has been synchronized on ReduceTask.this.
	 * 
	 * @param taskId
	 *            map taskid
	 */
	synchronized public void noteCopiedMapOutput(Datum did) {
		copiedMapOutputs.add(did);
		ramManager.setNumRequiredMapOutputs(numMaps - copiedMapOutputs.size());
	}

	@Override
	public void generate(HTask rtask) {
		super.generate(rtask);

		HReducerTask task = (HReducerTask) rtask;
		
		setTask(rtask);
		
		this.localFSMergerThread = task.getLocalFSMerger();
		this.inMemFSMergeThread = task.getInMemFSMergeThread();

		inMemFSMergeThread.newStory(this);
		localFSMergerThread.newStory(this);
	}

	@Override
	public void taskCleanUp(HTask task) {
//		hlog.info("clean up");

		STOP_TIME= Sim_system.clock();

		
		double duration= STOP_TIME- START_TIME;
		
		rcounters.inc(CTag.DURATION, duration);
		
		setStatus(Status.finished);
	}

	@Override
	public void taskProcess(HTask rtask) {
		assert rtask==task;
		process();
		setStatus(Status.finished);
	}

	@Override
	public void taskStart(HTask rtask) {
//		hlog.result("start on task "+ task);
		setStatus(Status.running);
		START_TIME =Sim_system.clock();
		
		
		rtask.sim_process(INIT_TIME);

	}

	public int getNumCopiers() {
		return numCopiers;
	}

	synchronized public void addFileOnDisk(Datum outmrg) {
		assert outmrg != null;
		hlog.info("addFileOnDisk before size:"+ mapOutputFilesOnDisk.size());
		hlog.info("add "+ outmrg);
		hlog.info("to "+ mapOutputFilesOnDisk);

		lock.lock();
		mapOutputFilesOnDisk.add(outmrg);
		hlog.info("addFileOnDisk after size:"+ mapOutputFilesOnDisk.size());
		lock.unlock();		
	}
	@Override
	public HLogger getHlog() {
		return hlog;
	}

}
