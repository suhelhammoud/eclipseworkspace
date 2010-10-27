package hasim.core;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.print.attribute.standard.MediaSize.Engineering;
import javax.swing.DebugGraphics;
import javax.xml.crypto.Data;

import eduni.simjava.Sim_entity;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_stat;
import eduni.simjava.Sim_system;
import hasim.CircularList;
import hasim.CTag;
import hasim.HCounter;
import hasim.HLogger;
import hasim.HLoggerInterface;
import hasim.HTAG;
import hasim.core.LocalMsg.OP_TYPE;
import hasim.json.JsonHardDisk;
import hasim.json.JsonJob;



public class CopyOfHDD extends Sim_entity implements HLoggerInterface{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CopyOfHDD.class);
	final HLogger hlog;
	double utilization;

	int numOfReads;
	int numOfWrites;
	
	Lock lock=new ReentrantLock(true);
	final private HCounter counters;
	private static double DELTA=1;

	public static double getDELTA() {
		return DELTA;
	}
	public static void setDELTA(double DELTA) {
		assert DELTA > 0;
		CopyOfHDD.DELTA = DELTA;
	}

	//	List<Disk> disks=new ArrayList<Disk>();
	//CircularList<Disk> disks=new CircularList<Disk>();
	final double readSpeed;
	final double writeSpeed;
	final int maxSlots;
	//id, progress
	//Vector< Filelet> files=new Vector< Filelet>();
	//	CircularList<Datum> files=new CircularList<Datum>();
	LinkedList<LocalMsg> msgs=new LinkedList<LocalMsg>();
	final Sim_stat stat;

	private void initStat(){

		stat.add_measure("usage", Sim_stat.STATE_BASED, 0);
		set_stat(stat);
	}
	
	public CopyOfHDD(String name, JsonHardDisk j) throws Exception{
		super(name);
		this.hlog=new HLogger(name);
		this.readSpeed=j.getRead();
		this.writeSpeed=j.getWrite();
		this.maxSlots=j.getSlots();
		this.counters=new HCounter();
		this.stat=new Sim_stat();
		initStat();
	}

	public CopyOfHDD(String name, String jsonFile) throws Exception{
		this(name, JsonJob.read(jsonFile, JsonHardDisk.class));
	}
	
	


	public void read(double size, int user,int returnTag, Object object){
		double totalTime = size  / readSpeed;
		double deltaTime = DELTA / readSpeed;

		LocalMsg msg=new LocalMsg(totalTime, deltaTime, user, 
				returnTag, object, OP_TYPE.READ);
		msg.setTag(HTAG.hdd_read.id());
		submit(msg);
		
		counters.inc(CTag.HDD_READ, size);
	}
	public void write(double size, int user,int returnTag, Object object){
		double totalTime = size  / writeSpeed;
		double deltaTime = DELTA / writeSpeed;

		LocalMsg msg=new LocalMsg(totalTime, deltaTime, user, 
				returnTag, object, OP_TYPE.WRITE);
		msg.setTag(HTAG.hdd_write.id());
		counters.inc(CTag.HDD_WRITE, size);

		submit(msg);
	}

	public void submit(LocalMsg msg){
		if( msg==null)return;
		assert msg.hasNext();

		sim_schedule(get_id(), 0.0, HTAG.engine_add_cpu.id(), msg);
		hlog.debug("submit "+ msg);
//		logger.debug("submit "+ msg+", time "+ Sim_system.clock());
	}


	public HLogger getHlog() {
		return hlog;
	}
	@Override
	public void body() {

		int slots=maxSlots;

		while (Sim_system.running()) {

			Sim_event ev=new Sim_event();
			sim_get_next(ev);

			int tag= ev.get_tag();
			if( tag == HTAG.END_OF_SIMULATION){
				logger.info(get_name()+" END_OF_SIMULATION "+ Sim_system.clock());
				hlog.info("END_OF_SIMULATION");
				break;
			}
//			monitor.log(get_name()+"-read/write", Sim_system.clock(), ""+msgs.size(), false);
//			stat.update("usage", msgs.size(), Sim_system.clock());

			if( tag == HTAG.engine_add_cpu.id()){
				LocalMsg msg=(LocalMsg)ev.get_data();
				msgs.add(msg);
				//hlog.info(HTAG.toString(tag)+ ", "+ msg);
				while(slots >0 && msgs.size()>0){
					LocalMsg msgToSend=msgs.removeFirst();
					sim_schedule(get_id(), msgToSend.decTimesAndGet(), 
							HTAG.engine_check_cpu.id(), msgToSend);
					slots--;

				}
				continue;
			}
			if( tag == HTAG.engine_check_cpu.id()){

//				logger.info("data "+ ev.get_data()+" , time:"+ Sim_system.clock() );
				LocalMsg msg=(LocalMsg)ev.get_data();
				//logger.info("hasNext: "+ msg.hasNext());
				if(! msg.hasNext()){

					//notify
					if(msg.user != -1){
						sim_schedule(msg.user, 0.0, msg.returnTag,
								msg.object);
						hlog.debug("finish "+ msg);
						
						
					}

					//take to new msg if available
					if(msgs.size()>0){
						lock.lock();
						LocalMsg msgToP=msgs.removeFirst();
						lock.unlock();
						assert msgToP.hasNext();
						sim_schedule(get_id(), msgToP.decTimesAndGet(), 
								HTAG.engine_check_cpu.id(), msgToP);
					}else{
						slots++;
					}
				}else{
					//msg.hasNext = true

					if(msgs.size()>0){
						//switch to new msgToP
						lock.lock();
						msgs.add(msg);
						LocalMsg msgToP=msgs.removeFirst();
						lock.unlock();
						//assert msgToP.hasNext();
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
		}




	}

	@Override
	public String toString() {
		return get_name();
	}

	public void stopEntity() {
		sim_schedule(get_id(), 0.0, HTAG.END_OF_SIMULATION);
		hlog.info("counters: "+ counters);
		hlog.save();
	}

}




