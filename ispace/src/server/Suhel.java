package server;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.TreeMap;

public class Suhel implements Serializable {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Suhel.class);

	private int i;
	TreeMap<Integer, Integer> map=new TreeMap<Integer, Integer>();
	public Suhel(int i){
		this.i= i;
		map.put(1, 11);
	}
	public Suhel(){
		
	}
	public String say(){
		
		logger.info("test");
		return "hello world   4";
	}
	public static void main(String[] args) {
		System.out.println(new Suhel(4).say());

	}

}
