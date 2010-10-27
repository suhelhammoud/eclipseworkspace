package hasim;

import org.apache.log4j.Logger;


import java.util.Map;

import eduni.simjava.Sim_entity;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;
import hasim.CopyObject.Step;
import hasim.CopyObject.Type;
import hasim.core.CPU;
import hasim.core.HDD;
import hasim.core.HMachine;

public class HCopier extends Sim_entity implements HLoggerInterface{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(HCopier.class);


	final private HLogger hlog;
	
	public HCopier(String name) {
		super(name);
		hlog=new HLogger("hcopier");
	}
	
	public void hdfsReplicate(int replication, int from, double size, Sim_entity user,
			int returnTag, Object object){
		CircularList<Integer> cl=new CircularList<Integer>(HMachine.getAll());
		cl.remove(from);
		
		HDD localHdd=HMachine.get(from).getHdd();
		localHdd.writeHdd( replication* size, user, returnTag, object);
//		localHdd.read(size, HDD.NONE, HDD.NONE, object);
		
//		if(replication==1)
//			return;
//		
//		if(replication > taskTrackers.size()) 
//			return;
//		for (int i = 0; i < replication-1; i++) {
//			String to=cl.next();
//			copy(from, to, size	, null, returnTag, object, Type.mem_hard);
//			from=to;
//		}

		
	}
	public CopyObject copy(int from,int to, double size, Sim_entity user,
			int returnTag, Object object, Type type){
//		assert taskTrackers.keySet().contains(from);
//		assert taskTrackers.keySet().contains(to);
//		
		CopyObject cpo=new CopyObject(size, type, from, to, user, returnTag, object);
		logger.debug("copy "+ cpo);
		copy(cpo);
		return cpo;
	}
	

	

	private boolean copyIsValid(int from, int to){
//		assert taskTrackers.keySet().contains(from);
//		assert taskTrackers.keySet().contains(to);
		
		if( HMachine.get(from)==null || HMachine.get(to)== null){
			return false;
		}else
			return true;
	}

	public boolean copy(CopyObject cpo){
		assert cpo != null;
		if(! copyIsValid(cpo.from, cpo.to)){
			logger.error("copy is not valid "+ cpo);
			return false;
		}else{
			cpo.start_Time=Sim_system.clock();
			sim_schedule(get_id(), 0.0, HTAG.cp_add_object.id(), cpo);
		return true;
		}

	}
	@Override
	public HLogger getHlog() {
		return hlog;
	}

	@Override
	public void body() {

		while (Sim_system.running()) {

			Sim_event ev=new Sim_event();
			sim_get_next(ev);

			int tag= ev.get_tag();
			
			if( tag == HTAG.END_OF_SIMULATION){
				logger.info(get_name()+" END_OF_SIMULATION "+ Sim_system.clock());
				hlog.info("END OF SIMULATION");
				break;
			}

			if( tag == HTAG.cp_add_object.id()){
				CopyObject cpo=(CopyObject)ev.get_data();
				
				if(!(cpo.isHard_from && cpo.isHard_to && cpo.isNet)){
					sendBackAck(cpo);
					continue;
				}
				
				if(cpo.isNet) 
					submitNet(cpo);
				
				if(cpo.isHard_from)
					submitHddFrom(cpo);
				
				if(cpo.isHard_to)
					submitHddTo(cpo);
			
				continue;
			}
			

			if( tag == HTAG.cp_net.id()){
				CopyObject cpo=(CopyObject)ev.get_data();
				
//				cpo.getHlog().debug("return net");
//				hlog.info("return net "+ cpo);
				double speed=cpo.size/(Sim_system.clock()-cpo.start_Time);
//				hlog.info("speed "+ speed);

				cpo.isNet=false;
				if(!(cpo.isHard_from && cpo.isHard_to && cpo.isNet))
					sendBackAck(cpo);
				
//				logger.debug("cp_net "+ cpo+ ", "+Sim_system.clock());
			};
			
			if( tag == HTAG.cp_hard_from.id()){
				CopyObject cpo=(CopyObject)ev.get_data();
//				cpo.getHlog().debug("return hard_from");
//				hlog.info("return hard_from "+ cpo );
				double speed=cpo.size/(Sim_system.clock()-cpo.start_Time);
//				hlog.info("speed "+ speed);

				cpo.isHard_from=false;
				if(!(cpo.isHard_from && cpo.isHard_to && cpo.isNet))
					sendBackAck(cpo);
//				logger.debug("cp_hard_from "+ cpo + ", "+Sim_system.clock());

			}
			if( tag == HTAG.cp_hard_to.id()){
				CopyObject cpo=(CopyObject)ev.get_data();
//				cpo.getHlog().debug("return hard_to");
//				hlog.info("return hard_to "+ cpo);
				double speed=cpo.size/(Sim_system.clock()-cpo.start_Time);
//				hlog.info("speed "+ speed);

				cpo.isHard_to=false;
				if(!(cpo.isHard_from && cpo.isHard_to && cpo.isNet))
					sendBackAck(cpo);

//				logger.debug("cp_hard_to "+ cpo+ ", "+Sim_system.clock());
				
			}
			

		}

	}

	private void sendBackAck(CopyObject cpo){
//		cpo.getHlog().info("retrun all ");
//		hlog.info("return all "+ cpo);
		if(cpo.user !=null){
			sim_schedule(cpo.user.get_id(), 0.0, cpo.returnTag, cpo.object);
//			logger.info("cpo.id = "+cpo.id+" ,speed test "+ cpo.type+" ="+ speed);
		}
	}
	private void submitNet(CopyObject cpo){
//		getNetEHdd(cpo.getFrom()).
//			sendData(getNetEHdd(cpo.getTo()), cpo.getSize(), netlet);
//		
		HMachine.get(cpo.from).sim_msg(HMachine.get(cpo.to).get_id(),
				cpo.size, get_id(),
				HTAG.cp_net.id(), cpo, 0.0);
		
//		cpo.getHlog().debug("submitNet");
		hlog.debug("submitNet "+ cpo);
	}
	private void submitHddFrom(CopyObject cpo){
		
		HMachine.get(cpo.from).readHdd(cpo.size,this,
				HTAG.cp_hard_from.id() , cpo);
//		cpo.getHlog().debug("submitHddFrom");
		hlog.debug("submitHddFrom "+cpo);

	}
	private void submitHddTo(CopyObject cpo){
		
		HMachine.get(cpo.to).writeHdd(cpo.size,this, 
				HTAG.cp_hard_to.id(), cpo);
//		cpo.getHlog().debug("submitHddTo");
		hlog.debug("submitHddTo "+cpo);
	}
	
	

}
