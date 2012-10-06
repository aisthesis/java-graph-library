/**
 * @file
 */
package graph;

import java.util.Comparator;

/**
 * @author Marshall Farrier
 * @version 1.0 1/14/11
 * Allows use of Java sorting functions to sort vertices by weight.
 *
 */
public class WeightedVertex {
		private int vert;
		private double weight;
		
		public WeightedVertex(int v) {
			vert = v;
			weight = 0.0;
		}
		public WeightedVertex(int v, double w) {
			vert = v;
			weight = w;
		}
		
		public int vertex() {
			return vert;
		}
		
		public double weight() {
			return weight;
		}
		
		public void setWeight(double w) {
			weight = w;
		}
		
		@Override
		public boolean equals(Object wv) {
			return vert == ((WeightedVertex) wv).vert;
		}
		
		/**
		 * The heavier object is "greater".
		 * Use to sort vertices in order of increasing weight.
		 */
		
		public static class Heavier implements Comparator<WeightedVertex> {
			@Override
			public int compare(WeightedVertex v1, WeightedVertex v2) {
				if (v1.weight > v2.weight) return 1;
				else if (v1.weight < v2.weight) return -1;
				else return 0;
			}
		}
		
		/**
		 * The lighter object is "greater than".
		 * Use to sort vertices in order of decreasing weight.
		 */
		public static class Lighter implements Comparator<WeightedVertex> {
			@Override
			public int compare(WeightedVertex v1, WeightedVertex v2) {
				if (v1.weight < v2.weight) return 1;
				else if (v1.weight > v2.weight) return -1;
				else return 0;
			}
		}
}
