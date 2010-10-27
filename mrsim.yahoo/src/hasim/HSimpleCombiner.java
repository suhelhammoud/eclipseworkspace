package hasim;

import hasim.core.Datum;

public class HSimpleCombiner implements HCombiner {

	final private int groups;
	final private double combineCost;
	
	public HSimpleCombiner(int groups,double combineCost) {
		this.groups = groups;
		this.combineCost=combineCost;
	}
	
	
	public int getGroups() {
		return groups;
	}

	



	@Override
	public Datum combine(Datum... d1) {
		return combine(groups,d1);
	}

	/**
	 * combine size cost is not considered now. To be added later.
	 * @param groups
	 * @param varDatum
	 * @return
	 */
	public static Datum combine(int groups, Datum ... varDatum  ){		
		return null;
		
	}
	
	public static void main(String[] args) {
		
		HCombiner combiner = new HSimpleCombiner(100000,30.0);
		Datum d=new Datum( 10000, 10000);
		
		System.out.println(d);
		
		System.out.println(combiner.combine(d));
		System.out.println(combiner.combine(d,d));
//		System.out.println(combiner.combine(d));
//		System.out.println(combiner.combine(d));
	}


	@Override
	public double cost(Datum d) {
		return combineCost*d.records;
	}
}
