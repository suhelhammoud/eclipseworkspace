package hasim.core;

 
import java.util.concurrent.atomic.AtomicInteger;
   
public class HSimMsg{
	private static AtomicInteger ID=new AtomicInteger();
	final public int id;

	final public int from,to; 
	final public double deltaSize;
	final public int user;
	final public int returnTag;
	final public Object object;
	final public double orgSize;
	
	public boolean isLocal=true;
	
	private double size,rSize;
	private int times,rTimes;
	
	
	public int getTimes(){
		return times;
	}
	public int getRTimes(){
		return rTimes;
	}
	
	public double decRAndGet(){
		rTimes++;
		if(rSize>= deltaSize){
			rSize-=deltaSize;
			return deltaSize;
		}else{
			double last=rSize;
			rSize=0;
			return last;
		}
	}
	
	public double decTimesAndGet(){
		times++;
		if(size>= deltaSize){
			size-=deltaSize;
			return deltaSize;
		}else{
			double last=size;
			size=0;
			return last;
		}
	}
	
	public boolean hasRNext(){
		return rSize > 0;
	}
	
	public boolean hasNext(){
		return size > 0;
	}
//	public double getSize(){
//		return size;
//	}
//	
	public HSimMsg(int from, int to, double size, double deltaSize, 
			int user, int returnTag, Object object) {
		this.id=ID.incrementAndGet();
		
		this.from=from;
		this.to=to;
		this.size=size; rSize=size;
		this.orgSize=size;
		this.deltaSize=deltaSize;
		this.user=user;
		this.returnTag=returnTag;
		this.object=object;
		
//		lastValue=deltaSize;
		
//		//added to test yahoo terasort
//		String s= Sim_system.get_entity(from).get_name();
//		fromR = getRack(s);
//		fromM = getMachine(s);
//		
//		s= Sim_system.get_entity(to).get_name();
//		toR = getRack(s);
//		toM = getMachine(s);
		
		
		
	}

	@Override
	public String toString() {
		return "LocalMsg"+ id+", totalTime:"+size+" , times:"+times;
	}
}
