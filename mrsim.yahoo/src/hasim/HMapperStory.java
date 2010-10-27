package hasim;

import org.apache.log4j.Logger;

import static hasim.Tools.format;
import static hasim.Tools.format2;
import hasim.CopyObject.Type;
import hasim.HTask.Status;
import hasim.core.CPU;
import hasim.core.Datum;
import hasim.core.HDD;
import hasim.core.HMachine;
import hasim.json.JsonAlgorithm;
import hasim.json.JsonDatum;
import hasim.json.JsonJob;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.codehaus.jackson.io.MergedStream;

import addition.ReduceCopier;

import dfs.MapBuffer;
import dfs.Pair;

import eduni.simjava.Sim_system;
import static hasim.Tools.nextRandomInt;

public class HMapperStory extends HStory implements HLoggerInterface{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(HMapperStory.class);

	final public static double INIT_TIME=0.0;


	public static HLogger hlog;


	public int progress=0;
	

	public HMapperStory(int name) {
		super(name);
	}

	double maxSpill_r_thr;

	Datum inputSplit=null;
	//	Datum outputSplit=null;

	//	List<Integer> spills=new ArrayList<Integer>();;
//	Integer spillLocation=Datum.LOCATION_NONE;
	static Datum spillDatum, spillDatumLast;//TODO set values
	static double numberOfSpills;


	public void setInputSplit(Datum inputSplit){
		this.inputSplit=inputSplit;
		hlog.info("setinput split "+ inputSplit.toString());
	}
	public Datum getInputSplit(){
		return inputSplit;
	}


	public static List<Integer> getRandomReplica(int replication, Collection<Integer> fromSet){
		List<Integer> replicaSet=new ArrayList<Integer>(replication);
		
		List<Integer> machines=new ArrayList<Integer>(fromSet);
		
		while( replicaSet.size() < replication){
			Integer r=machines.get(nextRandomInt(machines.size()));
			if(! replicaSet.contains(r))replicaSet.add(r);
		}

		return replicaSet;
	}
	
	public void generate(HTask task){
		super.generate(task);


		if(inputSplit.location == task.location){
			hlog.info("local import read from machine "+ task.location);
			mcounters.inc(CTag.IMPORT_MAP_LOCAL, 1.0);
		}else{

			hlog.info("remote import form "+ inputSplit.location+ ", to "+ task.location);

			if(Tools.inRack(inputSplit.location, task.location))
				mcounters.inc(CTag.IMPORT_MAP_INRACK, 1.0);
		}
		mcounters.inc(CTag.IMPORT_MAP_ALL, 1.0);

	}


	public static void setParameters(HLogger hlog, double size, double records){
		hlog.info("input split, size = "+ format(size)+
				", records =" +format(records));

		MapBuffer mb=MapBuffer.createMapBuffer(job);

		hlog.info("MapBuffer:\n"+mb);
		
		double outSpillRecords=mb.getSpillRecords(HStory.alg.getMapOutAvRecordSize());
		double inSpillRecords=outSpillRecords /HStory.alg.getMapRecords();

		hlog.info("outSpill records: "+ format(outSpillRecords)+
				", outspill size = "+format(outSpillRecords*
						(HStory.alg.getMapOutAvRecordSize())));

		numberOfSpills= records /inSpillRecords;

		double inSpillSize = size/numberOfSpills;



		spillDatum=new Datum( inSpillSize,inSpillRecords);

		double remain=numberOfSpills-(int)numberOfSpills;
		spillDatumLast= new Datum(spillDatum, remain);

		hlog.info("number of Spills = "+ format(numberOfSpills));
		hlog.info("one input task spill ="+spillDatum);

		hlog.info("generate(task:"+")");
		hlog.info("Spills.size() = "+ numberOfSpills);
	}

	@Override
	public String toString() {
		return ""+id;
	}
	public String toString2(){
		StringBuffer result=new StringBuffer();
		try {
			Class cls=Class.forName(this.getClass().getName());
			Field[] flds= cls.getDeclaredFields();
			for (Field fld : flds) 
				result.append( "\n"+fld.getName()+":"+fld.get(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public static void main(String[] args) throws Exception{

		List<String> list=new ArrayList<String>();
		list.add("m1");
		list.add("m2");
		list.add("m3");

		String s1=new String("m1");

		Set<String> set=new LinkedHashSet<String>();
		set.add("m3");
		set.add(s1);
		set.add("m2");

		System.out.println("set "+ set);
		System.out.println("lins "+ list);

		list.retainAll(set);
		System.out.println("list retainAll "+ list);
	}
	

	@Override
	public void taskStart(HTask task) {
		hlog.info("start on task "+ task);
		setStatus(Status.running);
		START_TIME =Sim_system.clock();

		task.sim_process(INIT_TIME);
	}

	@Override
	public void taskCleanUp(HTask task) {

		hlog.info("clean up");
		STOP_TIME = Sim_system.clock();
		double duration=STOP_TIME - START_TIME;
		mcounters.inc(CTag.DURATION, duration);
//		hlog.infoCounter("Counters:", mcounters);
		setStatus(Status.finished);
	}

	@Override
	public void taskProcess(HTask task) {
		hlog.info("taskProcess", Sim_system.clock());
		assert task != null;

		//		List<Datum> spills=new ArrayList<Datum>();
		List<Datum> outSpills=new ArrayList<Datum>();

		//import all
		HCopier copier=task.getJobTracker().getCopier();
		for (int i = 0; i <= (int)numberOfSpills; i++) {
			Datum s= i==(int)numberOfSpills? spillDatumLast: spillDatum;

			Datum spill=new Datum(s);
			spill.location=inputSplit.location;
			//import spill over the HDFS 
			hlog.info("import split "+spill.size);
			CopyObject cpo=copier.copy(inputSplit.location, task.location,
					spill.size, task, HTAG.import_split.id(), spill, Type.hard_mem);
			Datum returnSpill=(Datum)Datum.collectOne(task, HTAG.import_split.id);
			assert returnSpill==spill;
			//			spills.add(spill);
			hlog.info("spill imported cpo.id="+ cpo.id);
		}
		for (int i = 0; i <= (int)numberOfSpills; i++) {
			Datum s= i==(int)numberOfSpills? spillDatumLast: spillDatum;
			Datum spill=new Datum(s);

			hlog.info("map split "+ spill.id);
			Datum result=map(spill.size,spill.records);
			progress= i;

			assert ! result.isInMemory();
			outSpills.add(result);

		}

		int partitions = job.getNumberOfReducers();


		Datum outputSplit;
		hlog.info("start merging ");

		if(outSpills.size()>1){
			outputSplit=HMergeQueue.mergeToHard(job.getIoSortFactor(),0, task,
					hlog, HMachine.get(task.location), mcounters, outSpills, jobinfo.getCombiner());
		}else{
			outputSplit=new Datum(outSpills.get(0));
		}
		outputSplit.location= task.location;

		hlog.info("generate output split:"+ outputSplit);

		hlog.info("merg done");

		double fraction= 1.0/(double)partitions;


		hlog.info("add map results to addScheduled copies, " +
				"fraction = "+format2(fraction));

		//TODO check later
//		assert getJobinfo().reducersFinished.size()==0;


//		int numRedusers=job.getNumberOfReducers();

		if (ReduceCopier.mapResultSizeRecords == null){
			ReduceCopier.mapResultSizeRecords= 
				new Pair<Double, Double>
			(outputSplit.size/partitions, outputSplit.records/partitions);
		}
		//set reducers sizes
		for (HStory story : getJobinfo().reducersWaiting) {
				story.addScheduledCopy(task.location);
			((ReduceCopier)story).shuffleCopies();
		}

		for (HStory story : getJobinfo().reducersRunning) {
				story.addScheduledCopy(task.location);
			((ReduceCopier)story).shuffleCopies();

		}
		
		


	}
	@Override
	public HLogger getHlog() {
		return hlog;
	}



}