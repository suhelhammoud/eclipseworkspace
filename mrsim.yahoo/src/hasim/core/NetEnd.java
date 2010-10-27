package hasim.core;

public interface NetEnd {
	
	 public void sim_msg(int tos, double size,
				int user, int returnTag, Object o);
	 public void sim_msg(int to, double size,
				int user, int returnTag, Object o, double delay);
	 public void sim_msg( HSimMsg msg,double delay);
	

	
	

	
}


