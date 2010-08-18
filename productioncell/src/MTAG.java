


import eduni.simjava.Sim_predicate;
import eduni.simjava.Sim_type_p;


/**
 * Logger for this class
 */
public enum MTAG {
	END_OF_SIMULATION_TAG,
	inPad1_put,
	outside, ON, inPad1_pick, OFF, oneJob, robo_picked, robo_released, 
	convAddItem, convRemoveItem, convCheck, convMove, convStop, convReach
	

	;

	public static final int END_OF_SIMULATION=-1;
	public static final int HDBASE = 1000;
	public final int id;

	public static String toString(int i){
		MTAG tag=get(i);
		return tag==MTAG.outside ? ""+i :tag.name();
	}
	public static MTAG get(int i) {
		if(i==END_OF_SIMULATION){
			return END_OF_SIMULATION_TAG;
		}
		
		int dif = i - HDBASE;
		MTAG[] arr = MTAG.values();
		if (dif < 0 || dif > arr.length)
			return MTAG.outside;
		return MTAG.values()[dif];
	}

	private MTAG() {
		this.id = ordinal() + HDBASE;
	}

	public int id() {
		return id;
	}

	public String tagName(int tagId) {
		int d = (HDBASE - tagId);
		return "" + MTAG.values()[d].name();
	}

	public Sim_predicate predicate(){
		return new Sim_type_p(id());
	}
	

}
