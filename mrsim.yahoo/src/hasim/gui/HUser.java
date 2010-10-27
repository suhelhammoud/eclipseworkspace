package hasim.gui;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.lowagie.text.html.HtmlTagMap;


import hasim.CopyObject;
import hasim.CopyObject.Type;
import hasim.HJobTracker;
import hasim.HLogger;
import hasim.HLoggerInterface;
import hasim.HTAG;
import hasim.core.Datum;
import hasim.core.HMachine;
import eduni.simjava.Sim_entity;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;
import eduni.simjava.distributions.Sim_negexp_obj;

public class HUser extends Sim_entity implements HLoggerInterface{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(HUser.class);

	final HJobTracker tracker;
	final HLogger hlog;
	public HUser(String name, HJobTracker tracker) {
		super(name);
		this.tracker=tracker;
		this.hlog=new HLogger(name);
		logger.info("User created");
	}
	
	public void submitJobTest( MRSimTest1 test){
		logger.info("submit new jobt test "+ test);
		sim_schedule(get_id(), 1.0, HTAG.local_jobtest.id(), test);
	}
	
	
	public void testJobSubmit(){
		tracker.submitJob(
				"/media/SHARE/phd/terasort/terajobs/cluster/1/jobs/template-ready.json");
	}
	public void testCopier(){
		
		List<Integer> machines=new ArrayList<Integer>(HMachine.getAll());
		
		int from=  machines.get(0);
		int to= machines.get(machines.size()-1);
		
		CopyObject cpo=tracker.getCopier().copy(from, to, 100000000, this , HTAG.all_shuffles_finished.id, "Hello World",
				Type.mem_mem);
		
		logger.info("cpo= "+ cpo);
		String msg =(String)Datum.collectOne(this, HTAG.all_shuffles_finished.id);
		logger.info("testCopier msg "+ msg );
	}
	@Override
	public void body() {

//		MRSimTest1.buildRoutTables( tracker);

		sim_process(2000);
		//testCopier();
		testJobSubmit();
		
//		submitJobTest(new MRSimTest1());
		logger.info("total receives "+ HMachine.totalLast.get()+"/ "+HMachine.total+ " clock:"+ Sim_system.clock());
		
		
		

		while(Sim_system.running()){
			
			logger.info("enter the loop");
			Sim_event ev = new Sim_event();
			sim_get_next(ev);
			int tag = ev.get_tag();

			logger.info("event:" + HTAG.toString(tag)+" at time:"+ Sim_system.clock());

			if (tag == HTAG.END_OF_SIMULATION) {
				logger.info("receive end of simulation");
				logger.info(get_name()+" END_OF_SIMULATION "+ Sim_system.clock());
				tracker.stopSimulation();
				break;
			}
			
			if(tag == HTAG.local_jobtest.id()){
				
				logger.info("got now test ");
				MRSimTest1 test=(MRSimTest1)ev.get_data();

				test.testInit(this);
				logger.info("finish one test");
				continue;
			}

			
			
		}
		
	}
	
	public void stopSimulation() {
		sim_schedule(get_id(), 0.0, HTAG.END_OF_SIMULATION);
//		hlog.info("counters: "+ counters);
//		hlog.save();
	}
	@Override
	public HLogger getHlog() {
		return hlog;
	}

}
