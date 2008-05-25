package tree;

public class JobDriver {

	static enum myCounters {NUMBER_OF_ROWS,ITEMS_LEFT};
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		run();
//		int iteration =5;
//		ToLineDriver.runToLine(iteration);
//		ToItemDriver.runToItem(iteration,0);
		
	}

	public static void run() {
		AtomicItemsDriver.runAtomicItem();
		for (int i = 2; ; i++) {
			int iteration=i;
			ToLineDriver.runToLine(iteration);
			int itemsLeft=ToItemDriver.runToItem(iteration,0);
			System.out.println("Iteration "+iteration+", Items left: "+itemsLeft);
			if(itemsLeft<1)break;
		}
	}
}
