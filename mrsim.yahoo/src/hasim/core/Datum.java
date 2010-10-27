package hasim.core;

import static hasim.Tools.format;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.log4j.Logger;

import eduni.simjava.Sim_entity;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_predicate;
import eduni.simjava.Sim_type_p;

public class Datum implements Comparable<Object> {

	public final static int LOCATION_NONE = 0, USER_NONE = 0;

	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(Datum.class);

	private static AtomicInteger totalId = new AtomicInteger();

	public static List<Object> collect(int times, Sim_entity entity,
			int... tags) {
		List<Object> result = new ArrayList<Object>();
		for (int i = 0; i < times; i++) {
			result.addAll(collect(entity, tags));
		}
		return result;
	}

	public static List<Object> collect(Sim_entity entity, int... tags) {
		List<Object> result = new ArrayList<Object>();
		for (int i = 0; i < tags.length; i++) {
			result.add(collectOne(entity, tags[i]));
		}
		return result;
	}

	public static Object collectAny(Sim_entity entity, int... tags) {
		Sim_event ev = new Sim_event();
		Sim_predicate p = new Sim_type_p(tags);
		entity.sim_get_next(p, ev);
		return ev.get_data();
	}

	public static Object collectOne(Sim_entity entity, int tag) {
		Sim_event ev = new Sim_event();
		entity.sim_get_next(new Sim_type_p(tag), ev);
		return ev.get_data();

	}

	// public final double delta;

	public static void main(String[] args) {
		Datum d1 = new Datum(20, 20);
		Datum d2 = new Datum(20, 20);
		// Datum d2=new Datum(19.99999, 19.99999);

		System.out.println("" + d1.compareTo(d2));
		System.out.println("" + d2.compareTo(d1));
	}

	final public int id;

	boolean inMemory = false;

	public int location;

	public final double records;

	public final double size;

	public Datum(Datum d) {
		this(d.size, d.records, d.getLocation());
		this.inMemory = d.inMemory;
	}

	public Datum(Datum mapOutputLoc, boolean inMemory) {
		this(mapOutputLoc);
		this.inMemory = inMemory;
	}

	public Datum(Datum d, double fraction) {
		this(d.size * fraction, d.records * fraction, d.getLocation());
	}

	// public int user = USER_NONE;

	public Datum(double size, double records) {
		this(size, records, LOCATION_NONE);
	}

	public Datum(double size, double records, int location) {
		this.id = totalId.incrementAndGet();
		this.size = size;
		this.records = records;
		this.location = location;
	}

	@Override
	public int compareTo(Object o) {

		Datum od = (Datum) o;
		double dif = size - od.size;
		if (dif != 0)
			return (int) Math.signum(dif);
		return id - od.id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Datum other = (Datum) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public int getLocation() {
		return location;
	}

	@Override
	public int hashCode() {
		return id;
	}

	public boolean isInMemory() {
		return inMemory;
	}

	public void setInMemory(boolean inMemory) {
		this.inMemory = inMemory;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + "\tid: " + id + ", size:"
				+ format(size) + ", r:" + format(records) + ", L:" + location;
	}
}
