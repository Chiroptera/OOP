package sorting;

import java.util.Arrays;
import java.util.Random;


public abstract class SortInteger {

	private final int[] values;
	private final SortMetrics metrics = new SortMetrics();
	
	/* final says that extended classes can't redefine this method*/
	protected final int compare(int i, int j){
		metrics.compareCnt++;
		int i1=values[i], i2=values[j];
		if (i1 == i2) return 0;
		else return (i1 < i2 ? -1 : 1);
	}
	
	protected final void swap(int i, int j){
		metrics.swapCnt++;
		int temp=i;
		values[i] = values[j];
		values[j] = temp;
		
	}
	
	/* abstract modifier makes extended classes implement this method */
	protected abstract void sort();
	
	public SortInteger(int[] v) {
		this.values = v;
	}
	


	public void doSort(){
		metrics.init();
		sort();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SortInteger [values=" + Arrays.toString(values) + ", metrics="
				+ metrics + "]";
	}
	
	protected int getSize(){
		return values.length;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if (args.length == 0){
			System.out.println("Usage: SortInteger <num>");
			System.exit(1);
		}
		
		int[] v = new int[Integer.parseInt(args[0])];
		Random random = new Random();
		for (int j=0;j<v.length;j++){
			v[j] = random.nextInt(v.length*100+1);
		}
		
		System.out.println("array" + Arrays.toString(v));
		
		
	}
	

}
