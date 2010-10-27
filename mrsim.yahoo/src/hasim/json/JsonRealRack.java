package hasim.json;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JsonRealRack{

	public static final int MAX_MACHINE_IN_RACK= 100;
//	public static final int MACHINES_IN_RACK=40;
	
	private String name="rack 0";
	
	private String router;
	private double propDelay;

	private  double heartbeat;
	private String hlogLevel="result";
	
	public String getHlogLevel() {
		return hlogLevel;
	}
	public void setHlogLevel(String hlogLevel) {
		this.hlogLevel = hlogLevel;
	}
	public double getHeartbeat() {
		return heartbeat;
	}
	public void setHeartbeat(double heartbeat) {
		this.heartbeat = heartbeat;
	}

	private double deltaCPU,deltaHDD,deltaNEt;

	
	public double getDeltaNEt() {
		return deltaNEt;
	}
	public void setDeltaNEt(double deltaNEt) {
		this.deltaNEt = deltaNEt;
	}
	public double getDeltaCPU() {
		return deltaCPU;
	}
	public void setDeltaCPU(double deltaCPU) {
		this.deltaCPU = deltaCPU;
	}
	public double getDeltaHDD() {
		return deltaHDD;
	}
	public void setDeltaHDD(double deltaHDD) {
		this.deltaHDD = deltaHDD;
	}

	private int maxIM;
	private boolean flowType=false;
	
	
	public boolean isFlowType() {
		return flowType;
	}
	public void setFlowType(boolean flowType) {
		this.flowType = flowType;
	}
	public int getMaxIM() {
		return maxIM;
	}
	public void setMaxIM(int maxIM) {
		this.maxIM = maxIM;
	}
	public double getPropDelay() {
		return propDelay;
	}
	public void setPropDelay(double propDelay) {
		this.propDelay = propDelay;
	}

	private List<JsonMachine> machines;
	private List<List<JsonMachine>> racks=new ArrayList<List<JsonMachine>>();
	
	
	public int creatAllMachines(){
		assert numOfRacks >0;
		int result=0;
		
		//fill all racks with clone of machines in the default rack.
		for (int index = 0; index <= (int)numOfRacks; index++) {
			List<JsonMachine> listOfMachines=new ArrayList<JsonMachine>();
			int prefix = MAX_MACHINE_IN_RACK * index;//start from 1
			
			int maxMachineIndex = machines.size();
			if( index == (int) numOfRacks){
				double r=((numOfRacks-(int)numOfRacks)*machines.size());
				maxMachineIndex= (int)(Math.round(r));
//				System.out.println("machine index "+ maxMachineIndex);
			}
			for (int i = 0; i < maxMachineIndex; i++) {
				JsonMachine m=machines.get(i);
				JsonMachine nm=m.copy(prefix+m.getHostName());
				listOfMachines.add(nm);
				result++;
			}
			racks.add(listOfMachines);
		}
		return result;
	}
	public List<List<JsonMachine>> getRacks() {
		return racks;
	}
	public void setRacks(List<List<JsonMachine>> racks) {
		this.racks = racks;
	}
	public String getRouter() {
		return router;
	}
	public void setRouter(String router) {
		this.router = router;
	}
	public  List<JsonMachine> getMachines() {
		return machines;
	}
	public void setMachines(List<JsonMachine> machines) {
		this.machines = machines;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	/** added to build terasort test**/
	private double numOfRacks=1.0;
	private double baudRate;

	
	public double getNumOfRacks() {
		return numOfRacks;
	}
	public void setNumOfRacks(double numOfRacks) {
		this.numOfRacks = numOfRacks;
	}
	public double getBaudRate() {
		return baudRate;
	}
	public void setBaudRate(double baudRate) {
		this.baudRate = baudRate;
	}
	@Override
	public String toString(){
		String result="";
		try {
			@SuppressWarnings("rawtypes")
			Class cls=Class.forName(this.getClass().getName());
			Field[] flds= cls.getDeclaredFields();
			for (Field fld : flds) 
				result+= "\n"+fld.getName()+":"+fld.get(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
	
	
	public static void main(String[] args) {
		String rackfile="/media/SHARE/phd/terasort/terajobs/cluster/1/rack/rack.json";
		
		JsonRealRack rack=JsonJob.read(rackfile, JsonRealRack.class);
		rack.creatAllMachines();
		
		for (List<JsonMachine> r : rack.getRacks()) {
			System.out.println(r.size());
		}
//		System.out.println("total number is "+ rack.getRacks().size());
//		
//		for (JsonMachine m : rack.machines) {
//			if ( m.getHostName() == 0)
//				System.out.println("error "+ m.toString());
//		}
//		System.out.println(rack);
		//JsonJob.save("data/rackout", rack);
//		System.out.println(rack.getNumOfRacks());
//		System.out.println(rack.getBaudRate());
	}
}