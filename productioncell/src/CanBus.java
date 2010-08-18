import java.util.ArrayList;
import java.util.List;

import eduni.simjava.Sim_entity;


public class CanBus  {

	private double busDelay=0;
	public CanBus( double busDelay) {
		this.busDelay=busDelay;
	}

	List<Sim_entity> busItems=new ArrayList<Sim_entity>();
	
	public void addBusItem(Sim_entity entt){
		busItems.add(entt);
	};
	

	public void msg(Sim_entity entt, int tag ){
		msg(entt, tag, null);
	}

	public void msg(Sim_entity srcEntity, int tag, Object data ){
		for (Sim_entity busItem : busItems) {
			if(busItem== srcEntity)continue;
			srcEntity.sim_schedule(busItem.get_id(), busDelay, tag,  data);
		}
	}
}
