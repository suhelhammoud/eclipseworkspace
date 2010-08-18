
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

import eduni.simjava.Sim_entity;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_predicate;
import eduni.simjava.Sim_system;
import eduni.simjava.Sim_type_p;


public class Conveyor extends Sim_entity{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Conveyor.class);

	List<Double> items=new ArrayList<Double>();
	
	double max=100;
	double speed=20;
	boolean isMoving=false;
	double startTime=0;
	Sensor inSensor, outSensor;
	public Conveyor(String name, Sensor inSensor, Sensor outSensor) {
		super(name);
		this.inSensor=inSensor;
		this.outSensor=outSensor;
	}

	@Override
	public void body() {
		while(Sim_system.running()){
			Sim_event ev=new Sim_event();
			sim_get_next(ev);
			int tag=ev.get_tag();
			
			if(tag== MTAG.END_OF_SIMULATION){
				logger.info("END OF SIMULATION");
				break;
			}
			
			if(tag == MTAG.convAddItem.id){
				items.add(0.0);
				inSensor.put();
				
				if(isMoving){
					sim_schedule(get_id(), max/speed, MTAG.convCheck.id);
					inSensor.pick();
				}
				continue;
			}
			
			if( tag== MTAG.convRemoveItem.id){
				assert items.size()>0;
				assert max- items.get(0)  < 1e-4;
				items.remove(0);
				outSensor.pick();
				continue;
			}
			
			
			
			if( tag == MTAG.convMove.id){
				isMoving=true;
				startTime=Sim_system.clock();
				if(items.size()>0){
					double firstItem= items.get(0);
					sim_schedule(get_id(), (max-firstItem)/speed, MTAG.convReach.id);
				}
				
				double lastItem=items.get(items.size()-1);
				if(lastItem < 1e-4){
					inSensor.pick();
				}
				
				continue;
			}
			
			if(tag == MTAG.convReach.id){
				assert items.size()>0;
				
				outSensor.put();
				
				
				if(isMoving && items.size()>2){
					double firstItem= items.get(1);
					sim_schedule(get_id(), (max-firstItem)/speed, MTAG.convReach.id);

				}
				continue;
				
			}
			
			if( tag == MTAG.convStop.id){
				isMoving=false;
				double deltaTime=Sim_system.clock()- startTime;
				double deltaPos=speed*deltaTime;

				//cancel future events
				Sim_predicate evp=new Sim_type_p(MTAG.convReach.id);
				sim_cancel(evp, new Sim_event());
				
				for (int i = 0; i < items.size(); i++) {
					items.set(i, items.get(i)+deltaPos);
				}				
				
			}
		}
	}
	
	public void  moveConveyor() {
		sim_schedule(get_id(), 0.0, MTAG.convMove.id);
	}
	public void stopConveyor(){
		sim_schedule(get_id(), 0.0, MTAG.convStop.id);
		
	}
	public void addToItems(){
		sim_schedule(get_id(), 0.0, MTAG.convAddItem.id);
	}
	public void removeItem(){
		sim_schedule(get_id(), 0.0, MTAG.convRemoveItem.id);

	}
}
