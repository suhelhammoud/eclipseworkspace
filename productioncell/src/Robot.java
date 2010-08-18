
import org.apache.log4j.Logger;
import eduni.simjava.Sim_entity;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_port;
import eduni.simjava.Sim_system;
import eduni.simjava.distributions.Sim_normal_obj;


public class Robot extends Sim_entity{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Robot.class);

	private double hPosition=45;
	private double vPosition=0;
	private boolean isPicked=false;
	private double speed=90;
	
	private Sim_port out;
	
    private Sim_normal_obj rdelay=new Sim_normal_obj("Delay", 0.5, 0.02);;

	Sensor inSensor, outSensor;

	private boolean isIdle=true;
	
	public boolean isIdle(){
		return isIdle;
	}
	
	public Robot(String name, double speed, Sensor inSensor, Sensor outSensor) {
		super(name);
		this.speed=speed;
		this.inSensor=inSensor;
		this.outSensor=outSensor;
	}
	
	@Override
	public void body() {
		double vTime=0.5;
		while(Sim_system.running()){
			Sim_event ev=new Sim_event();
			sim_get_next(ev);
			int tag=ev.get_tag();
			
			if(tag== MTAG.END_OF_SIMULATION){
				logger.info("END OF SIMULATION");
				break;
			}
			
			if(tag == MTAG.oneJob.id){
				isIdle=false;
				
				//move to 0
				sim_process(hPosition/speed);
				hPosition=0;
				
				
				
				//down
				sim_process(vTime);
				vPosition=1;
				
				//pick
				sim_process(0.1);
				isPicked=true;
				
				//up
				sim_process(vTime);
				vPosition=0;
				inSensor.pick();
				sim_schedule(out, 0.0, MTAG.robo_picked.id);
				
				//move
				sim_process(90/speed);
				hPosition=90;
				
				//down
				sim_process(rdelay.sample());
				vPosition=1;
				
				//release
				sim_process(0.1);
				isPicked=false;
				outSensor.put();
				sim_schedule(out, 0.0, MTAG.robo_released.id);
				
				//up
				sim_process(vTime);
				vPosition=0;
				
				
				//move to default
				sim_process(45/speed);
				hPosition=45;
				
				
				isIdle=true;
				//release
			}
		}
	}

	public void doOneJob(){
		sim_schedule(get_id(), 0.0, MTAG.oneJob.id);
	}
	public String getStatus(){
		String result="Robot "+ get_name();
		result+=", hPos.="+hPosition+ ", vPos.="+vPosition+ ", idIdle="+isIdle+ ", isPicked="+isPicked;
		return result;
	}
}
