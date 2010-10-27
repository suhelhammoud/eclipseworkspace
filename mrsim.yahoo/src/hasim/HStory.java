package hasim;

import eduni.simjava.Sim_system;
import hasim.HTask.Status;
import hasim.core.CPU;
import hasim.core.Datum;
import hasim.core.HDD;
import hasim.core.HMachine;
import hasim.json.JsonAlgorithm;
import hasim.json.JsonJob;

public abstract class HStory implements HLoggerInterface{

	final public static double REF_2=2.0;

	public static JobInfo jobinfo;
	public static JsonJob job;
	public static JsonAlgorithm alg;
	
	public static HCounter mcounters;
	public static HCounter rcounters;
	
	
	protected Status status=Status.idle;
	
	public int location;
	

	protected HTask task;
	final public int id;
	
	protected double START_TIME,STOP_TIME;
	
	public HTask getTask() {
		return task;
	}

	public void setTask(HTask task) {
		this.task = task;
	}

	public HStory(int id)  {
			this.id=id;
			//TODO static set jobinfo, hlog, job, alg
	}
	
	public void generate(HTask task){
		setTask(task);
//		hlog.info("Assign story to task "+ task.get_name() + 
//				" location ="+task.location);
	}
	
	abstract public void taskStart(HTask task);

	abstract public void taskProcess(HTask task);
	
	abstract public void taskCleanUp(HTask task);
	
	
	synchronized public void addScheduledCopy(int loc){
	}

	
	public JobInfo getJobinfo() {
		return jobinfo;
	}

	


	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
	
	@Override
	public String toString() {
		return "story "+ id;
	}

	public HTask getHTask() {
		return task;
	}

	/**
	 * @param spill
	 * @return
	 */
	public Datum map(double size, double records){
		
		mcounters.inc(CTag.HDFS_BYTES_READ, size);
		mcounters.inc(CTag.MAP_INPUT_RECORDS,  records);
		mcounters.inc(CTag.MAP_INPUT_SIZE,  size);

		HMachine machine=HMachine.get(task.location);
	

		HCopier copier=task.getJobTracker().getCopier();
		
		double mapOutAvRecordSize=alg.getMapOutAvRecordSize();
		if(job.getNumberOfReducers()>0){
			mapOutAvRecordSize=(alg.getMapOutAvRecordSize()+ REF_2);
		}
		
		
		// double mapOutPutBytes= alg.getMapOutAvRecordSize()* spill.records;

		double cpuCost = records * alg.getMapCost();
		double mapOutRecords = records * alg.getMapRecords();
		double mapOutSize = mapOutRecords * mapOutAvRecordSize;

		//process the map
		machine.work(cpuCost, task.get_id(), HTAG.cpu_split.id(), id);
		Integer cpuReturn=(Integer)Datum.collectOne(task, HTAG.cpu_split.id);
		assert cpuReturn==id;
		


		machine.writeHdd(mapOutSize, task, HTAG.hdd_split.id(), id);
		Integer returnHDD= (Integer)Datum.collectOne(task ,	HTAG.hdd_split.id);
		assert returnHDD == id;

		mcounters.inc(CTag.MAP_OUTPUT_BYTES, mapOutSize);
		mcounters.inc(CTag.MAP_OUTPUT_RECORDS, mapOutRecords);

		mcounters.inc(CTag.FILE_BYTES_WRITTEN, mapOutSize);
		mcounters.inc(CTag.SPILLED_RECORDS, mapOutRecords);

		
		Datum outSpill =new Datum(mapOutSize, mapOutRecords);
		outSpill.setInMemory(false);

		return outSpill;
	}
	
	public Datum reduce(double size, double records){
		
		HMachine machine=HMachine.get(task.location);

		HCopier copier=task.getJobTracker().getCopier();

		double cpuCost=records* alg.getReduceCost();
		double outRecords=records*alg.getReduceRecords();
		double outSize=outRecords* alg.getReduceOutAvRecordSize();
		
		

		machine.work(cpuCost, task.get_id(), HTAG.reducer_CPU_reduce.id(), id);
		
		copier.hdfsReplicate(job.getReplication(), task.location,
				outSize, task, HTAG.reduce_HDFS_reduce.id(), id);
//		hdd.write(outSize, task.get_id(), HTAG.reduce_HDFS_reduce.id(), dToReduce);
		
		
		Datum.collect(task,	HTAG.reducer_CPU_reduce.id(), HTAG.reduce_HDFS_reduce.id());
		
		rcounters.inc(CTag.REDUCE_INPUT_RECORDS, records);
		rcounters.inc(CTag.REDUCE_SHUFFLE_BYTES, size);
		
		rcounters.inc(CTag.REDUCE_OUTPUT_RECORDS, outRecords);

		rcounters.inc(CTag.HDFS_BYTES_WRITTEN, outSize);
		
		Datum result=new Datum(outSize,outRecords);
		return result;
	}

}
