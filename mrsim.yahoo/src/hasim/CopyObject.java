package hasim;

import hasim.core.Datum;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import eduni.simjava.Sim_entity;

public class CopyObject {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CopyObject.class);

	public static enum Type {
		hard_hard, hard_mem, mem_hard, mem_mem
	};

	public static enum Step {
		hard_from, hard_to, net
	};

	private static AtomicInteger ID = new AtomicInteger();

	final public int id;
//	final public Set<Step> steps = new LinkedHashSet<Step>();
	
	boolean isHard_from=false, isHard_to=false, isNet=false;
	public final double size;
	public final int from;
	public final int to;
	public final Sim_entity user;
	public final int returnTag;
	public final Object object;
	public final Type type;

	public double start_Time = 0;

	public CopyObject(double size, Type type, int from, int to,
			Sim_entity user, int returnTag, Object o) {

		assert from != Datum.LOCATION_NONE;
		assert to != Datum.LOCATION_NONE;

		this.id = ID.incrementAndGet();
		this.from = from;
		this.to = to;
		this.size = size;
		this.user = user;
		this.returnTag = returnTag;
		this.object = o;

		this.type = type;

		if (to == from) {
			switch (type) {
			case hard_mem:
				isHard_from=true;
				break;
			case mem_hard:
				isHard_to=true;
			default:
				break;
			}
		} else {
			switch (type) {
			case hard_hard:
				isHard_from=true;
				isHard_to=true;
				isNet=true;
				break;
			case hard_mem:
				isHard_from=true;
				isNet=true;
				break;
			case mem_mem:
				isNet=true;
				break;
			case mem_hard:
				isNet=true;
				isHard_to=true;
				break;
			default:
				logger.error(type.toString() + " is not included");
				break;
			}
		}
	}

	@Override
	public String toString() {
		String result = "cpo " + id + " ,from:" + from + " ,to:" + to
				+ ", size:" + size + ", type:" + type + ", steps:" + isHard_from+","+isNet+", "+isHard_to;
		return result;
	}
}
