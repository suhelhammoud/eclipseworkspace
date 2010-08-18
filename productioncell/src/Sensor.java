import java.awt.Event;

import eduni.simjava.Sim_entity;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_port;
import eduni.simjava.Sim_system;


public class Sensor extends Sim_entity{

	private Sim_port out;
	boolean state=false;
	double delay=0.0;

	public Sensor(String name, double delay) {
		super(name);
		this.delay=delay;
	}
	
	@Override
	public void body() {
		
		while(Sim_system.running()){
			Sim_event ev=new Sim_event();
			sim_get_next(ev);
			
			
			int tag= ev.get_tag();
		
			if ( tag== MTAG.END_OF_SIMULATION){
				break;
			}
			if( tag== MTAG.inPad1_put.id){
				state=true;
				sim_schedule(out, delay, MTAG.ON.id);
				continue;
			}
			
			if( tag== MTAG.inPad1_pick.id){
				state=false;
				sim_schedule(out, delay, MTAG.OFF.id);
				continue;
			}
		}
		
	}
	
	public void put(){
		sim_schedule(get_id(), 0.0, MTAG.inPad1_put.id);
	}
	public void pick(){
		sim_schedule(get_id(), 0.0, MTAG.inPad1_pick.id);
	}

	public static void main(String[] args) {
		Sensor s=new Sensor("s1", 0.0);

		while ( s.state==false){
			s.put();
		}
		
	}
}
