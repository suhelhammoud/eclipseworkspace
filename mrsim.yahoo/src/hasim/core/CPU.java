package hasim.core;


public interface CPU {
	public void work(double size, int user,int returnTag, Object object);
	public void work(double size, int user,int returnTag, Object object, double priority);

	
}



