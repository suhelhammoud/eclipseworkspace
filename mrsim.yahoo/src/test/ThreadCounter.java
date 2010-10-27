package test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.omg.SendingContext.RunTime;

public class ThreadCounter extends Thread{
	public static AtomicInteger ID=new AtomicInteger();
	final public int id;
	public ThreadCounter() {
		this.id=ID.incrementAndGet();
	}
	@Override
	public void run() {
		try {
			Thread.sleep(200);
//			System.out.print("\t"+id);
//			boolean newLine= (id%20)==0;
//			if(newLine)System.out.println();
		} catch (Exception e) {
			System.err.println("at thread "+ id);
		}
	}
	
	public static void main(String[] args) {
		
		List<ThreadCounter> list=new ArrayList<ThreadCounter>();
		for (int i = 0; i < 100000; i++) {
			ThreadCounter thc=new ThreadCounter();
			list.add(thc);
		}
		for (int i = 0; i < list.size(); i++) {
			ThreadCounter thc =list.get(i);
			try {
				thc.start();
				
			} catch (Exception e) {
				System.err.println("error at thread "+ i);;
			}
			if(i%10==0){
				long free=Runtime.getRuntime().freeMemory();
				long total=Runtime.getRuntime().totalMemory();
				double ratio=(0.0+free)/total;
				System.out.println(i+"\t\t"+free+"/ "+ total+" = "+ ratio);
			}

		}
	}

}
