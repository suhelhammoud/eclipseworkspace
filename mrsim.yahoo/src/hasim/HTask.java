package hasim;

import hasim.core.HMachine;
import static hasim.Tools.format;
import org.apache.log4j.Logger;

import eduni.simjava.Sim_entity;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;

public abstract class HTask extends Sim_entity implements HLoggerInterface{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(HTask.class);

	public static enum Status { idle, running, finished, sort, reduce, copying};
	
	final static public HCounter counters = new HCounter();
	public static  HJobTracker jobTracker;
	
	public final int location;


	final public static HLogger hlog=new HLogger("HTASK");
	protected Status status = Status.idle;

	
	
	protected HStory story;

	public HTask(String name, int location) {
		super(name);
		this.location = location;

	}

	
	
	@Override
	public HLogger getHlog() {
		// TODO Auto-generated method stub
		return hlog;
	}

	
	@Override
	public String toString() {
		return get_name();
	}
	
	public void setStatus(Status status) {
		this.status = status;
		hlog.info("setStatus("+status.name()+")");
	}
	
//	double oneStoryStartTime=0;
	
	public void submitStory() {
		assert story != null;
		assert story.getStatus() != Status.finished;
		assert story.getStatus() != Status.idle;
		
//		oneStoryStartTime=Sim_system.clock();
		
		hlog.info("submitStory");
		
		setStatus(Status.running);
		story.setStatus(Status.running);
		
		hlog.info("story generate ");
		
		story.generate(this);
		
		sim_schedule(get_id(), 0.0, HTAG.task_start_local.id());

		counters.put(CTag.START_TIME, Sim_system.clock());

	}
	
	public HStory getStory() {
		return story;
	}

	public void setStory(HStory story) {
		this.story = story;
	}

	public HCounter getCounters() {
		return counters;
	}

	public HJobTracker getJobTracker() {
		return jobTracker;
	}

	

	

	public Status getStatus() {
		return status;
	}

	


	protected void taskStart(){
		hlog.debug(getName()+" task start");
		story.taskStart(this);
	}
	
	protected void taskProcess() {
		hlog.debug(getName()+" start task process");
		story.taskProcess(this);
	}
	
	protected void taskCleanUp() {
		hlog.debug(getName()+" taskCleanUp");				
		story.taskCleanUp(this);
	}
	
	@Override
	public void body() {
		while (Sim_system.running()) {
			Sim_event ev = new Sim_event();
			sim_get_next(ev);
			int tag = ev.get_tag();



			if (tag == HTAG.END_OF_SIMULATION) {
				hlog.debug(getName()+" END_OF_SIMULATION");
//				logger.info(get_name()+" END_OF_SIMULATION "+ Sim_system.clock());
				break;
			}

			if (tag == HTAG.task_start_local.id()) {
				assert getStatus() ==Status.running;
				assert story.getStatus()==Status.running;
				
				taskStart();
				taskProcess();
				taskCleanUp();

				story.setStatus(Status.finished);
				setStatus(Status.idle);
				
//				{//counting
//					double duration=Sim_system.clock()-oneStoryStartTime;
//					oneStoryStartTime=0;
//					counters.inc(CTag.process_time, duration);
//					hlog.info("duration "+ format(duration)+ 
//							", total process:"+ format(counters.get(CTag.process_time)));
//				}
				continue;
			}

		}
	}
	
	public void stopEntity(boolean saveResults) {
		sim_schedule(get_id() , 0.0, HTAG.END_OF_SIMULATION);
		hlog.info(getName()+" counters: "+ counters);
		if(saveResults)
			hlog.save();
	}
}
