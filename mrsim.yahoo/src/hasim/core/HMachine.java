package hasim.core;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;


import eduni.simjava.Sim_entity;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;
import hasim.CTag;
import hasim.CircularList;
import hasim.HCounter;
import hasim.HJobTracker;
import hasim.HLogger;
import hasim.HLoggerInterface;
import hasim.HTAG;
import hasim.core.LocalMsg.OP_TYPE;
import hasim.json.JsonCpu;
import hasim.json.JsonHardDisk;
import hasim.json.JsonMachine;

public class HMachine extends Sim_entity implements CPU, HDD, NetEnd{

 	public final double baudeRate;
	final public JsonMachine jsonMachine;

	public final static Map<Integer, HMachine> machines = new LinkedHashMap<Integer, HMachine>();

	public static HMachine get(int id){
		return machines.get(id);
	}
	public static Set<Integer> getAll( ){
		return machines.keySet();
	}
	public static HMachine put(int id, HMachine machine){
		return machines.put(id, machine);
	}

	public HMachine(String name, JsonMachine m) throws Exception {
		super(name);

		this.jsonMachine=m;

		this.maxMappers = m.getMaxMapper();
		this.maxRedcuers= m.getMaxReducer();

		JsonHardDisk jsnhdd=m.getHardDisk();
		JsonCpu jsnCpu=m.getCpu();

		this.cores=jsnCpu.getCores();
		this.speed=jsnCpu.getSpeed();

		this.readSpeed=jsnhdd.getRead();
		this.writeSpeed=jsnhdd.getWrite();

		
		this.baudeRate=m.getBaudRate()/8.0;//byte per seco
	}

	private static final Logger logger = Logger.getLogger(HMachine.class);
	private static double DELTACPU=1;



	public static double getDELTACPU() {
		return DELTACPU;
	}
	public static void setDELTACPU(double dELTA) {
		HMachine.DELTACPU = dELTA;
	}

	private final int cores;

	final private  double speed;//MIPS
	//


	LinkedList<LocalMsg> msgsCpu=new LinkedList<LocalMsg>();
	public void submitCpu(LocalMsg msg){
		if( msg==null)return;
		sim_schedule(get_id(), 0.0, HTAG.engine_add_cpu.id(), msg);
		//		logger.debug("submit "+ msg+", time "+ Sim_system.clock());
	}

	public void work(double size, int user,int returnTag, Object object){
		work(size, user, returnTag, object,1.0);//default priority =1
	}

	public void work(double size, int user,int returnTag, Object object, double priority){
		double totalTime = size  / speed;
		double deltaTime = DELTACPU * priority/ speed ;

		if(totalTime==0){
			sim_schedule(user, 0.0, returnTag,object);
			return;
		}

		LocalMsg msg=new LocalMsg(totalTime, deltaTime, user, returnTag, object,OP_TYPE.CPU_WORK);
		submitCpu(msg);
	}

	@Override
	public void body() {
//		assert localRack != null;

		boolean isIdle=true;
		CircularList<LocalMsg> msgsHdd=new CircularList<LocalMsg>(1000);

		boolean netOutIsIdle=true;
		boolean netInIsIdle=true;
		LinkedList<HSimMsg> msgNetOut=new LinkedList<HSimMsg>();
		LinkedList<HSimMsg> msgNetIn=new LinkedList<HSimMsg>();
		
			int slots=cores;

			while (Sim_system.running()) {

				Sim_event ev=new Sim_event();
				sim_get_next(ev);
				int tag= ev.get_tag();

//				logger.info(" ev:"+get_name() +" time: "+Sim_system.clock()+"\t"+ HTAG.get(tag)+"\t o:"+ev.get_data());

				if( tag == HTAG.END_OF_SIMULATION){
					logger.info(get_name()+" END_OF_SIMULATION "+ Sim_system.clock());
					break;
				}

				if( tag == HTAG.engine_add_cpu.id){
					LocalMsg msg=(LocalMsg)ev.get_data();
					msgsCpu.add(msg);

					while(slots >0 && msgsCpu.size()>0){
						LocalMsg msgToSend=msgsCpu.removeFirst();
						sim_schedule(get_id(), msgToSend.decTimesAndGet(), 
								HTAG.engine_check_cpu.id(), msgToSend);
						slots--;

					}
					continue;
				}
				if( tag == HTAG.engine_check_cpu.id){

					//					logger.info("data "+ ev.get_data()+" , time:"+ Sim_system.clock() );
					LocalMsg msg=(LocalMsg)ev.get_data();
					//logger.info("hasNext: "+ msg.hasNext());
					if(! msg.hasNext()){

						//notify
						if(msg.user != -1){
							sim_schedule(msg.user, 0.0, msg.returnTag,
									msg.object);
							//							logger.debug(msg+" , count: "+msg.getTimes() );
						}

						//take to new msg if available
						if(msgsCpu.size()>0){
							LocalMsg msgToP=msgsCpu.removeFirst();
							assert msgToP.hasNext();
							sim_schedule(get_id(), msgToP.decTimesAndGet(), 
									HTAG.engine_check_cpu.id(), msgToP);
						}else{
							slots++;
						}
					}else{
						//msg.hasNext = true

						if(msgsCpu.size()>0){
							//switch to new msgToP
							msgsCpu.add(msg);
							LocalMsg msgToP=msgsCpu.removeFirst();
							assert msgToP.hasNext();
							sim_schedule(get_id(), msgToP.decTimesAndGet(), 
									HTAG.engine_check_cpu.id(), msgToP);

						}else{
							//keep processing same msg
							sim_schedule(get_id(), msg.decTimesAndGet(), 
									HTAG.engine_check_cpu.id(), msg);

						}
					}
					continue;
				}


				//////hdd
				if( tag == HTAG.engine_add_hdd.id){
					LocalMsg msg=(LocalMsg)ev.get_data();
					msgsHdd.add(msg);
					if(msg.opType==OP_TYPE.WRITE){
						numWrites++;
						percent=adjustPercentHdd(numWrites);
						//					logger.info("percent ="+ percent);
					}
					//hlog.info(HTAG.toString(tag)+ ", "+ msg);
					if(isIdle ){
						isIdle=false;
						sim_schedule(get_id(), 0.0, HTAG.engine_check_hdd.id);


					}
					continue;
				}
				if( tag == HTAG.engine_check_hdd.id){
					assert msgsHdd.size()>0;
					//				logger.info("data "+ ev.get_data()+" , time:"+ Sim_system.clock() );
					LocalMsg msg=msgsHdd.next();

					sim_process(msg.decTimesAndGet()*percent);
					//logger.info("hasNext: "+ msg.hasNext());
					if(! msg.hasNext()){

						msgsHdd.remove(msg);
						if(msg.opType==OP_TYPE.WRITE){
							numWrites--;
							percent=adjustPercentHdd(numWrites);

							//						logger.info("id=" + get_name()+ ",percent ="+ percent);
						}
						//notify
						if(msg.user != HMachine.NONEHDD){
							sim_schedule(msg.user, 0.0, msg.returnTag,
									msg.object);
						}

					}
					if(msgsHdd.size()>0)
						sim_schedule(get_id(), 0.0, HTAG.engine_check_hdd.id);
					else{
						isIdle=true;
						assert numWrites==0;
					}

					continue;
				}
				

				/// netend   /////////////////new addition by suhel
				HSimMsg msgT=(HSimMsg)ev.get_data();

//				logger.info(get_name() +" time: "+Sim_system.clock()+"\t"+ HTAG.get(tag)+"\t id:"+msgT.id+ "\to="+
//						msgT.object+ ", out:"+netOutIsIdle+ " ,in: "+netInIsIdle);

				
				
				

				if(tag == HTAG.sim_msg_send.id()){
					HSimMsg msg=(HSimMsg)ev.get_data();
					
					double interval= msg.orgSize/baudeRate;
					double delay=0;
					int chuncks = (int) (msg.orgSize/msg.deltaSize);
					
					for (int i = 0; i < chuncks+1; i++) {
						sim_schedule(get_id(), delay, HTAG.sim_msg_out.id, msg);
						delay+= interval;
					}

					continue;
					//logger.debug(get_name()+" receive id:"+ o.id()+ " from:"+ev.get_src()+" at time : "+Sim_system.clock());
				}
				if(tag == HTAG.sim_msg_out.id){
					HSimMsg msg =(HSimMsg) ev.get_data();
					
					if(netOutIsIdle){
						assert msgNetOut.isEmpty();
						netOutIsIdle =false;
						double deltaTime = msg.decTimesAndGet()/baudeRate;
						sim_schedule(get_id(), deltaTime, HTAG.sim_msg_out_flush.id, msg);
					}else{

						msgNetOut.add(msg);
					}
					continue;
				}
				if(tag == HTAG.sim_msg_in.id){
					HSimMsg msg =(HSimMsg) ev.get_data();
					
					if(netInIsIdle){
						assert msgNetIn.isEmpty();
						netInIsIdle=false;
						double deltaTime = msg.decRAndGet()/baudeRate;
						sim_schedule(get_id(), deltaTime, HTAG.sim_msg_in_flush.id, msg);
					}else{
						msgNetIn.add(msg);
					}
					continue;
				}
				
			
				
				if(tag == HTAG.sim_msg_out_flush.id){
					HSimMsg msg=(HSimMsg)ev.get_data();
					sim_schedule(msg.to, 0.0, HTAG.sim_msg_in.id, msg);

					if(! msgNetOut.isEmpty()){
						HSimMsg nextMsg=msgNetOut.removeFirst();
						assert nextMsg.hasNext();
						double deltaTime = nextMsg.decTimesAndGet()/baudeRate;
						sim_schedule(get_id(), deltaTime, HTAG.sim_msg_out_flush.id,nextMsg);
						netOutIsIdle=false;
						
					}else{
						netOutIsIdle=true;
					}
					continue;
				}
				
				if(tag == HTAG.sim_msg_in_flush.id()){
					HSimMsg msg=(HSimMsg)ev.get_data();
					if(! msg.hasRNext() ){
						//last msg packet
						totalLast.incrementAndGet();
						if( msg.user != USER_NONE)
							sim_schedule(msg.user,0.0, msg.returnTag, msg.object);
					}
					total.incrementAndGet();
					
//					logger.info(get_name()+" receive "+ totalLast.get() +"/ "+ total.get());
					if(!msgNetIn.isEmpty()){
						HSimMsg nextMsg =msgNetIn.removeFirst();
						assert nextMsg.hasRNext();
						double deltaTime = nextMsg.decRAndGet()/baudeRate;
						sim_schedule(get_id(), deltaTime, HTAG.sim_msg_in_flush.id, nextMsg);
						netInIsIdle =false;
					}else{
						netInIsIdle =true;
					}

//					logger.info(" receive: "+get_name() +" time: "+Sim_system.clock()+"\t"+ HTAG.get(tag)+"\t id:"+msg.id+ "\to="+msg.object);
					continue;
				}
//				
				
			
		}



	}
	
	public static AtomicInteger totalLast=new AtomicInteger();
	public static AtomicInteger total=new AtomicInteger();
	
	
	@Override
	public String toString() {
		return get_name();
	}


	public void stopEntity() {
		sim_schedule(get_id(), 0.0, HTAG.END_OF_SIMULATION);
	}


	////////////////////hdd
	double utilization;
	double percent=1.0;
	int numWrites=0;


	final public static int NONEHDD=-10;
	private static double DELTAHDD=Double.MAX_VALUE;

	public static double getDELTAHDD() {
		return DELTAHDD;
	}
	public static void setDELTAHDD(double DELTA) {
		assert DELTA > 0;
		HMachine.DELTAHDD = DELTA;
	}

	static double adjustPercentHdd(int currentWrites){
		//		if(true)return 1.0;
		double min=3, max=60, slop=100;
		assert currentWrites >=0;

		if(currentWrites<min)
			return 1.0;
		else if(currentWrites < max)
			return 1.0-(currentWrites-min)/slop;
		return 1.0- (max-min)/slop;
	}

	final double readSpeed;
	final double writeSpeed;

	public void readHdd(double size, Sim_entity user,int returnTag, Object object){
		double totalTime = size  / readSpeed;
		double deltaTime = DELTAHDD / readSpeed;

		//		assert totalTime>0;
		if(totalTime==0){
			user.sim_schedule(user.get_id(), 0.0, returnTag,object);
			return;
		}

		LocalMsg msg=new LocalMsg(totalTime, deltaTime, user.get_id(),
				returnTag, object, OP_TYPE.READ);
		msg.setTag(HTAG.hdd_read.id());


		submitHdd(msg);

	}
	public void writeHdd(double size, Sim_entity user,int returnTag, Object object){
		double totalTime = size  / writeSpeed;
		double deltaTime = DELTAHDD / writeSpeed;

		if(totalTime==0){
			user.sim_schedule(user.get_id(), 0.0, returnTag,object);
			return;
		}
		assert size>0;

		LocalMsg msg=new LocalMsg(totalTime, deltaTime, user.get_id(),
				returnTag, object, OP_TYPE.WRITE);
		msg.setTag(HTAG.hdd_write.id());


		submitHdd(msg);
	}

	private void submitHdd(LocalMsg msg){
		if( msg==null)return;
		assert msg.hasNext();

		sim_schedule(get_id(), 0.0, HTAG.engine_add_hdd.id, msg);

		//		logger.debug("submit "+ msg+", time "+ Sim_system.clock());
	}

	//////netend
	final public static int USER_NONE=-1; 
	//public static double delay=0.1;

	private static double DELTANET=100000000;
	public static double getDELTANET() {
		return DELTANET;
	}
	public static void setDELTANET(double dELTA) {
		HMachine.DELTANET = dELTA;
	}



	public void sim_msg(int tos, double size,
			int user, int returnTag, Object o){
		int to=machines.get(tos).get_id();
		sim_msg(to, size, user, returnTag, o, 0.0);
	}	
	public void sim_msg(int to, double size,
			int user, int returnTag, Object o, double delay){
		HSimMsg msg=new HSimMsg(get_id(),to, size, DELTANET, user, returnTag, o);
		sim_msg(msg,delay);		
	}
	public void sim_msg( HSimMsg msg,double delay){
		//TODO add delay 
		
	
		
		sim_schedule(get_id(), 0.0, HTAG.sim_msg_send.id(),msg);
	}


	//////htasktracker
	private final int maxMappers, maxRedcuers;

	private HJobTracker jobTracker;

	public HJobTracker getJobTracker() {
		return jobTracker;
	}
	

	public CPU getCpu() {
		return this;
	}
	public HDD getHdd() {
		return this;
	}
	public int getMaxMappers() {
		return maxMappers;
	}
	public int getMaxRedcuers() {
		return maxRedcuers;
	}

	public int getaMapperSlots() {
		return maxMappers;
	}



	public int getaReducerSlots() {
		return maxRedcuers;
	}


	public String toStringInfo() {
		StringBuffer sb=new StringBuffer("tracker:"+get_name());
		sb.append("\taMappers:"+ maxMappers+ "aReducers:"+ maxRedcuers);
		return sb.toString();
	}

	public HMachine getNetend(){
		return this;
	}
	

	
}
