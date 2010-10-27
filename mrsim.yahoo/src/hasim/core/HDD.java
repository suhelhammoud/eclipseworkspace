package hasim.core;

import eduni.simjava.Sim_entity;

import hasim.HLoggerInterface;




public interface HDD {
	public void readHdd(double size, Sim_entity user,int returnTag, Object object);
	public void writeHdd(double size, Sim_entity user,int returnTag, Object object);

}




