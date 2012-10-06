/**
 * @file
 */
package graph;

/**
 * Interface to be implemented by any kind of graph class
 * @author Marshall Farrier
 * @date 11/22/10
 *
 */
public interface Graph {
	public static final int NIL_VERTEX = 'A' - 'z' - 1;
	// For vertex data fields
	public static final int COLOR = 0;
	public static final int PARENT = 1;
	public static final int DISTANCE = 2;	// BFS
	public static final int DISCOVERY_TIME = 2;	// DFS
	public static final int FINISH_TIME = 3;	// DFS
	public static final int TREE_NUMBER = 4;	// DFS (to identify components)
	
	// Edge properties
	public static final int EDGE_TYPE = 0;
	
	// Colors
	public static final int WHITE = 0;
	public static final int GRAY = 1;
	public static final int BLACK = 2;
	
	// Edge types
	public static final int TREE_EDGE = 1;
	public static final int BACK_EDGE = 2;
	public static final int FORWARD_EDGE = 3;
	public static final int CROSS_EDGE = 4;
	
	// returns true iff insertion successful
	public boolean insert(int _from, int _to);
	public boolean insert(Edge _e);
	// returns true iff removal successful
	public boolean remove(int _from, int _to);
	public boolean remove(Edge _e);
	// returns the number of edges in the graph
	public int edges();
	// returns an array consisting of all edges in the graph
	public Edge[] getEdges();
	// returns the edges from a given vertex
	public Edge[] getEdges(int _from);
	// returns an array consisting of all vertices u s.t. (_from, u) in G.E
	public int [] getAdjacencies(int _from);
	// determines whether a given edge is in the graph
	public boolean hasEdge(Edge e);
	public boolean hasEdge(int _from, int _to);
	// number of vertices
	public int vertices();
	// returns the transpose of the calling graph
	public Graph transpose();
}
