/**
 * @file
 */
package graph;

/**
 * Weighted edges are also immutable
 * @author Marshall Farrier
 * @version 1.0 1/13/11
 *
 */
public class WeightedEdge extends Edge implements Comparable<WeightedEdge> {
	private double weight;
	
	public WeightedEdge(int from, int to, double wt) {
		super(from, to);
		weight = wt;
	}
	public WeightedEdge(int from, int to, boolean ch, double wt) {
		super(from, to, ch);
		weight = wt;
	}
	public double weight() { return weight; }
	
	public int compareTo(WeightedEdge e) {
		if (this.weight > e.weight) return 1;
		else if (this.weight < e.weight) return -1;
		else return 0;
	}
	
	@Override
	public String toString() {
		return super.toString() + " " + weight;
	}
}
