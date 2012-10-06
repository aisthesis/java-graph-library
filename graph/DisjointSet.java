/**
 * @file
 */
package graph;

/**
 * Implements operations of CLRS, ch. 21
 * using linked list representation presented in 21.2
 * (not the faster but more complicated technique of 21.4).
 * Used for Kruskal's algorithm.
 * 
 * @author Marshall Farrier
 * @date 11/21/10
 *
 */
class DisjointSet {

	private final int NIL = -1;
	private class Node {
		int next;
		int head;
		/**
		 * tail and setSize will in general only be accurate for the 
		 * representative / head element of each set so that we only 
		 * have to update one element on union operations
		 */
		int tail;		
		int setSize;
		Node() {
			next = NIL;
			head = NIL;
			tail = NIL;
			setSize = NIL;
		}
	}

	private Node [] sets;
	
	/**
	 * The constructor obviates the makeSet() operation
	 * by creating size disjoint sets with 1 member each
	 * @param size
	 */
	public DisjointSet(int size) {
		if (size < 0) {
			throw new IllegalArgumentException("Set cannot have negative size");
		}
		sets = new Node[size];
		for (int i = 0; i < size; ++i) {
			sets[i] = new Node();
			sets[i].next = NIL;
			sets[i].head = i;
			sets[i].tail = i;
			sets[i].setSize = 1;
		}
	}
	
	public int findSet(int i) {
		return sets[i].head;
	}
	
	/**
	 * Returns false if the 2 sets are already identical.
	 * Returns true and performs the union if the sets are distinct.
	 * @param i
	 * @param j
	 * @return
	 */
	public boolean union(int i, int j) {
		int first = findSet(i);
		int second = findSet(j);
		int tmp;
		if (first == second) return false;
		// make first the larger set
		if (sets[first].setSize < sets[second].setSize) {
			tmp = first;
			first = second;
			second = tmp;
		}
		// connect the tail element of first set to head element of second
		sets[sets[first].tail].next = second; 
		// make first the rep of all elements in second
		tmp = second;
		while (sets[tmp].next != NIL) {
			sets[tmp].head = first;
			tmp = sets[tmp].next;
		}
		// while loop does not set head for last element of second set
		sets[tmp].head = first;
		sets[first].setSize += sets[second].setSize;
		return true;		
	}

}
