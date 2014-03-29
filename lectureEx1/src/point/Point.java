package point;
import java.util.HashSet;
public class Point {

	int x, y; /* visibility? package */

	/*
	 * CONSTRUCTORS
	 */
	
	public Point () {
		x=y=0; /* unnecessary because by default properties (not variables) are initialized with 0 */
	}
	public Point (int x) {
		this.x = x;		
	}
	public Point (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass()) /* if obj is of different class from self*/
			return false;
		Point other = (Point) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		System.out.println("Cleaning the house...");
		super.finalize();
	}
	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		Point o0 = new Point();
//		Point o1 = new Point();
//		Point o2 = new Point(2,3);
//		
//		System.out.println("-------------B1-------------------");
//		System.out.println(o0);
//		System.out.println(o1);
//		System.out.println(o2);
//		System.out.print("o0 hashCode\t");System.out.println(o0.hashCode());
//		System.out.print("o1 hashCode\t");System.out.println(o1.hashCode());
//		System.out.print("o0==o1\t\t");System.out.println(o0==o1);
//		System.out.print("o0.equals(o1)\t");System.out.println(o0.equals(o1));
//		
//		System.out.print("o2 hashCode\t");System.out.println(o2.hashCode());
//		System.out.print("o2.equals(o1)\t");System.out.println(o2.equals(o1));
//		
//		HashSet<Point> hso1 = new HashSet<Point>();
//		hso1.add(o0);
//		hso1.add(o1);
//		hso1.add(o2);
//		
//		System.out.println("-------------B2-------------------");
//		
//		System.out.print("hashSet\t");System.out.println(hso1);
//		o0=null;
//		System.gc(); /* DO NOT TRY THIS AT HOME */
		
		father ob = new father();
		son s1 = new son();
		
		ob.printHello();
		s1.printHello();
		
	}

	
	
}
