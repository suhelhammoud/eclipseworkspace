package UDPGui;

import java.util.Date;

public class TestTime {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("start");
		Date start=new Date();
		for (int i = 0; i < 1000000; i++) {
			for (int j = 0; j < 10000; j++) {
				int sum=i+j;
			}
		}
		Date stop=new Date();
		System.out.println("stop");
		long time_used=stop.getTime()-start.getTime();
		System.out.println("Time used="+ time_used);

	}

}
