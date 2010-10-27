package hasim.json;

import org.apache.log4j.Logger;

import hasim.core.Datum;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class JsonDatum {
	private static AtomicInteger totalId = new AtomicInteger();
	public final int id;
	
	public JsonDatum() {
		this.id=totalId.incrementAndGet();
	}
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(JsonDatum.class);

	public static void main(String[] args)throws Exception {
//		JsonJob job=JsonJob.read("data/json/job.json", JsonJob.class);
//		logger.info(job.getInputSplits().get(0));
	}
	
	
	private double size=9197822290.0,records=69424600.0;
	



	public double getRecords() {
		return records;
	}





	public double getSize() {
		return size;
	}


	public void setRecords(double records) {
		this.records = records;
	}

	

	public void setSize(double size) {
		this.size = size;
	}
	
	
	@Override
	public String toString(){
		String result="";
		try {
			Class cls=Class.forName(this.getClass().getName());
			Field[] flds= cls.getDeclaredFields();
			for (Field fld : flds) 
				result+= "\n"+fld.getName()+":"+fld.get(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
