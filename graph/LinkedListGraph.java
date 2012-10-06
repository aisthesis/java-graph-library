/**
 * @file
 */
package graph;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Can be directed or undirected.
 * Directed by default.
 * Supports multiple types of integral-value satellite data for vertices.
 * These attributes will just be numbered 0, 1, 2, ... initially, so
 * if they are to have some kind of mnemonic significance, the client program
 * should use the setDataFieldLabels() method to label the fields with
 * single character names in the range 'a' to 'z'.
 * @author Marshall Farrier
 * @date 11/22/10
 *
 */
public class LinkedListGraph extends AbstractGraph implements Graph {	
	private int e;
	private LinkedList<Integer>[] adj;
	
// some protected edge methods for speed (but unchecked)
	/**
	 * This method bypasses exceptions and works without offsets
	 */
	protected boolean fastInsert(int _from, int _to) {
		if (adj[_from].contains(_to)) return false;	// edge already present
		++e;
		adj[_from].add(_to);
		if (!directed()) {
			adj[_to].add(_from);
		}
		return true;
	}
	protected boolean fastRemove(int _from, int _to) {
		boolean found = adj[_from].remove((Integer)_to);
		if (found) {
			--e;
			if (!directed()) adj[_to].remove((Integer)_from);
		}
		return found;
	}
	// Does not check for possible duplicates!
	// Use only if edge is KNOWN not to exist in calling graph
	protected void uncheckedInsert(int from, int to) {
		adj[from].add(to);
		if (!directed()) {
			adj[to].add(from);
		}
		++e;
	}
	// Gives no feedback on success or failure
	protected void uncheckedRemove(int from, int to) {
		if (adj[from].remove((Integer) to)) {
			if (!directed()) adj[to].remove((Integer) from);
			--e;
		}		
	}

// constructors	
	public LinkedListGraph(int _v) {
		super(_v);
		e = 0;
		adj = new LinkedList[_v];
		for (int i = 0; i < _v; ++i) {
			adj[i] = new LinkedList<Integer>();
		}
	}
	
	public LinkedListGraph(int _v, boolean _directed) {
		super(_v, _directed);
		e = 0;
		adj = new LinkedList[_v];
		for (int i = 0; i < _v; ++i) {
			adj[i] = new LinkedList<Integer>();
		}
	}
	
	public LinkedListGraph(int _v, boolean _directed, int _offset) {
		super(_v, _directed, _offset);
		e = 0;
		adj = new LinkedList[_v];
		for (int i = 0; i < _v; ++i) {
			adj[i] = new LinkedList<Integer>();
		}		
	}
	
	public LinkedListGraph(int _v, boolean _directed, int _offset, int _dataFields) {
		super(_v, _directed, _offset, _dataFields);
		e = 0;
		adj = new LinkedList[_v];
		for (int i = 0; i < _v; ++i) {
			adj[i] = new LinkedList<Integer>();
		}
	}
	
	public LinkedListGraph(LinkedListGraph _g) {
		super(_g);
		e = _g.edges();		
		int _v = vertices();
		adj = new LinkedList[_v];
		for (int i = 0; i < _v; ++i) {
			adj[i] = new LinkedList<Integer>();
		}
		ListIterator<Integer> it;
		for (int i = 0; i < _v; ++i) {
			// copy edges
			it = _g.adj[i].listIterator();
			while (it.hasNext()) {
				adj[i].add(it.next());
			}
		}		
	}
	
	/**
	 * This constructor ignores the contents of the data fields
	 * in the graph to be copied and creates new data fields, all
	 * of which are initialized to 0. Contents of old data fields
	 * must be copied externally if they are needed in the copy.
	 * @param g
	 * @param d
	 */
	public LinkedListGraph(LinkedListGraph g, int dataFields) {
		super(g, dataFields);
		e = g.edges();		
		final int VERTICES = vertices();
		adj = new LinkedList[VERTICES];
		for (int i = 0; i < VERTICES; ++i) {
			adj[i] = new LinkedList<Integer>();
		}
		ListIterator<Integer> it;
		for (int i = 0; i < VERTICES; ++i) {
			// copy edges
			it = g.adj[i].listIterator();
			while (it.hasNext()) {
				adj[i].add(it.next());
			}
		}
	}
	
	public LinkedListGraph(MatrixGraph _g) {
		super(_g);
		e = 0;
		int _v = vertices(), i;
		adj = new LinkedList[_v];
		for (i = 0; i < _v; ++i) {
			adj[i] = new LinkedList<Integer>();
		}
		Edge [] _edges = _g.getEdges();
		int len = _edges.length;
		for (i = 0; i < len; ++i) {
			insert(_edges[i]);
		}
	}
	
	public LinkedListGraph(WeightedLinkedListGraph _g) {
		super(_g);
		e = 0;
		int _v = vertices(), i;
		adj = new LinkedList[_v];
		for (i = 0; i < _v; ++i) {
			adj[i] = new LinkedList<Integer>();
		}
		Edge [] _edges = _g.getEdges();
		int len = _edges.length;
		for (i = 0; i < len; ++i) {
			insert(_edges[i]);
		}
	}

// methods inherited from Graph interface
	public boolean insert(int _f, int _t) {
		char _offset = offset();
		int _v =  vertices();
		boolean _directed = directed();
		int _from = _f - _offset, _to = _t - _offset;
		if (_from < 0 || _to < 0) {
			throw new IllegalArgumentException("Vertex cannot have negative index");
		}
		if (_from >= _v || _to >= _v) {
			throw new IllegalArgumentException("Invalid edge for current graph");
		}
		if (_from == _to && !_directed) {
			throw new IllegalArgumentException("Self-edge not allowed in an undirected graph");
		}
		if (adj[_from].contains(_to)) return false;	// edge already present
		++e;
		adj[_from].add(_to);
		if (!_directed) {
			adj[_to].add(_from);
		}
		return true;
	}
	
	public boolean insert(Edge _e) {
		return insert(_e.from(), _e.to());
	}
	
	public boolean remove(int _f, int _t) {
		char _offset = offset();
		int _v =  vertices();
		boolean _directed = directed();
		int _from = _f - _offset, _to = _t - _offset;
		if (_from < 0 || _to < 0) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		if (_from >= _v || _to >= _v) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		if (_from == _to && !_directed) {
			throw new IllegalArgumentException("Self-edge not allowed in an undirected graph");
		}
		boolean found = adj[_from].remove((Integer)_to);
		if (found) {
			--e;
			if (!_directed)	adj[_to].remove((Integer)_from);
		}
		return found;
	}
	
	public boolean remove(Edge _e) {
		return remove(_e.from(), _e.to());
	}
	
	public int edges() { return e; }
	
	public Edge[] getEdges() {
		char _offset = offset();
		final boolean CHAR_REP = charRep();
		int _v =  vertices();
		Edge [] result = new Edge[e];
		if (e == 0) return result;
		int counter = 0;
		ListIterator<Integer> it;
		if (_offset == 0) {
			if (directed()) {
				for (int i = 0; i < _v; ++i) {
					it = adj[i].listIterator();
					while (it.hasNext()) {
						result[counter++] = new Edge(i, it.next(), CHAR_REP);
					}
				}
			}
			else {
				int u, max = _v - 1;
				for (int i = 0; i < max; ++i) {
					it = adj[i].listIterator();
					while (it.hasNext()) {
						u = it.next();
						if (i < u) result[counter++] = new Edge(i, u, CHAR_REP);
					}
				}
			}
		}
		else {
			if (directed()) {
				for (int i = 0; i < _v; ++i) {
					it = adj[i].listIterator();
					while (it.hasNext()) {
						result[counter++] = new Edge(i + _offset, it.next() + _offset, CHAR_REP);
					}
				}
			}
			else {
				int u, max = _v - 1;
				for (int i = 0; i < max; ++i) {
					it = adj[i].listIterator();
					while (it.hasNext()) {
						u = it.next();
						if (i < u) result[counter++] = new Edge(i + _offset, u + _offset, CHAR_REP);
					}
				}
			}
		}			
		return result;
	}
	
	public Edge[] getEdges(int _f) {
		char _offset = offset();
		int _v =  vertices();
		int _from = _f - _offset;
		if (_from < 0 || _v <= _from) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		Edge [] result = new Edge[adj[_from].size()];
		int counter = 0;
		ListIterator<Integer> it = adj[_from].listIterator();
		while (it.hasNext()) {
			result[counter++] = new Edge(_from + _offset, it.next() + _offset, charRep());
		}
		return result;
	}
	
	/**
	 * If vertex labels are characters, the contents of the resulting
	 * array will need to be type-cast as char for viewing the results
	 * in the desired manner.
	 */
	public int [] getAdjacencies(int _f) {
		char _offset = offset();
		int _v =  vertices();
		int _from = _f - _offset;
		if (_from < 0 || _v <= _from) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		int [] result = new int[adj[_from].size()];
		int counter = 0;
		ListIterator<Integer> it = adj[_from].listIterator();
		while (it.hasNext()) {
			result[counter++] = it.next() + _offset;
		}
		return result;
	}
	
	public boolean hasEdge(int _f, int _t) {
		char _offset = offset();
		int _v =  vertices();
		int _from = _f - _offset, _to = _t - _offset;
		if (_from < 0 || _to < 0 || _from >= _v || _to >= _v) {
			throw new IllegalArgumentException("Invalid edge for current graph");
		}
		return adj[_from].contains(_to);
	}
	
	public boolean hasEdge(Edge _e) {
		return hasEdge(_e.from(), _e.to());
	}
	
	/**
	 * Throws an UnsupportedOperationException if calling object
	 * is undirected.
	 */
	public Graph transpose() {
		char _offset = offset();
		int _v =  vertices();
		boolean _directed = directed();
		int _d = dataFields();
		if (!_directed) {
			throw new UnsupportedOperationException("Transpose operation trivial on undirected graphs");
		}
		LinkedListGraph result = new LinkedListGraph(_v, _directed, _offset, _d);
		ListIterator<Integer> it;
		int j;
		for (int i = 0; i < _v; ++i) {
			// transfer transposed edges
			it = adj[i].listIterator();
			while (it.hasNext()) {
				result.adj[it.next()].add(i);
			}
			// copy vertex data
			for (j = 0; j < _d; ++j) {
				result.setData(i, j, getData(i, j));
			}
		}
		return result;
	}
// methods inherited from Object	
	// returns a deep copy of calling object
	@Override
	public LinkedListGraph clone() {
		return new LinkedListGraph(this);
	}
	
	@Override
	public String toString() {
		char _offset = offset();
		int _v =  vertices();
		boolean _charRep = charRep();
		String result = "";
		int i, j, lim = _v - 1;
		ListIterator<Integer> it;
		if (_charRep) {
			for (i = 0; i < _v; ++i) {
				result += (char)(i + _offset) + ": ";
				it = adj[i].listIterator();
				if (it.hasNext()) {
					result += (char)(it.next() + _offset);
					while (it.hasNext()) {
						result += " -> " + (char)(it.next() + _offset);
					}					
				}		
				if (i < lim) result += "\n";
			}
		}
		else {
			int width = String.valueOf(_v - 1).length();
			int currWidth;		
			
			for (i = 0; i < _v; ++i) {
				currWidth = width - String.valueOf(i).length();
				for (j = 0; j < currWidth; ++j) {
					result += " ";
				}
				result += i + ": ";
				it = adj[i].listIterator();
				if (it.hasNext()) {
					result += it.next();
					while (it.hasNext()) {
						result += " -> " + it.next();
					}					
				}			
				if (i < lim) result += "\n";
			}
		}
		return result;
	}	
	
	/**
	 * CLRS, p. 595
	 * 0 is white
	 * 1 is gray
	 * 2 is black
	 * @param i vertex from which to search
	 * @return
	 */
	public LinkedListGraph breadthFirstSearch(int _s) {
		char _offset = offset();
		int _v = vertices();
		int s = _s - _offset;
		
		if (s < 0 || _v <= s) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		final int DATA_FIELDS = 3;		
		
		LinkedListGraph result = new LinkedListGraph(_v, false, _offset, DATA_FIELDS);
		result.setDataFieldLabel(COLOR, 'c');
		result.setDataFieldLabel(DISTANCE, 'd');
		result.setDataFieldLabel(PARENT, 'p');
		
		
		
		int i;
		for (i = 0; i < _v; ++i) {
			// set color to white
			result.setData(i, COLOR, WHITE);
			// set distance to -1
			result.setData(i, DISTANCE, -1);
			// set parent to NIL_VERTEX
			result.setData(i, PARENT, NIL_VERTEX);			
		}
		result.setData(s, COLOR, GRAY);
		result.setData(s, DISTANCE, 0);
		
		LinkedList<Integer> q = new LinkedList<Integer>();
		ListIterator<Integer> it;
		int vert;
		q.add(s);
		while (!q.isEmpty()) {
			int u = q.remove();
			it = adj[u].listIterator();
			while (it.hasNext()) {
				vert = it.next();
				if (result.getData(vert, COLOR) == WHITE) {
					result.setData(vert, COLOR, GRAY);
					result.setData(vert, DISTANCE, result.getData(u, DISTANCE) + 1);
					result.setData(vert, PARENT, u);
					result.uncheckedInsert(u, vert);
					q.add(vert);
				}
			}
			result.setData(u, COLOR, BLACK);
		}
		return result;
	}
	
	/**
	 * CLRS, p. 601
	 * @param s Integer value of source vertex (no offset)
	 * @param v Integer value of target vertex (no offset)
	 */
	public void printPath(int source, int vert) {
		final char OFFSET = offset();
		final int VERTICES = vertices();
		int s = source - OFFSET;
		int v = vert - OFFSET;
		
		// Exceptions on invalid input
		if (s < 0 || VERTICES <= s || v < 0 || VERTICES <= v) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		if (dataFields() < 3) {
			throw new UnsupportedOperationException("Calling object must be a BFS tree");
		}
		
		printPath(s, v, 0);
		System.out.println(">");
	}
	private void printPath(int s, int v, int counter) {
		if (counter > vertices()) {
			throw new UnsupportedOperationException("Calling object must be a BFS tree");
		}
		
		final boolean CHAR_REP = charRep();
		final char OFFSET = offset();
		int source = s + OFFSET;
		int vert = v + OFFSET;
		
		if (s == v) {
			System.out.print("<");
			if (CHAR_REP) System.out.print((char) source);
			else System.out.print(s);
		}
		else if (getData(v, PARENT) == NIL_VERTEX) {
			if (CHAR_REP) {
				System.out.print("No path from " + (char) source +
						" to " + (char) vert + " exists");
			}
			else {
				System.out.print("No path from " + s +
						" to " + v + " exists");
			}
		}
		else {
			printPath(s, getData(v, PARENT), counter + 1);
			if (CHAR_REP) System.out.print(", " + (char) vert);
			else System.out.print(", " + v);
		}
	}
	
	/**
	 * CLRS, p. 604
	 * The most basic version of DFS
	 * @return
	 */
	public LinkedListGraph depthFirstSearch() {
		final int VERTICES = vertices();
		final char OFFSET = offset();
		final boolean DIRECTED = false;
		final int DATA_FIELDS = 4;
		
		LinkedListGraph result = new LinkedListGraph(VERTICES, DIRECTED, OFFSET, DATA_FIELDS);
		result.setDataFieldLabel(COLOR, 'c');		
		result.setDataFieldLabel(PARENT, 'p');
		result.setDataFieldLabel(DISCOVERY_TIME, 'd');
		result.setDataFieldLabel(FINISH_TIME, 'f');
		
		int i;		
		for (i = 0; i < VERTICES; ++i) {
			// set color to white
			result.setData(i, COLOR, WHITE);
			// set parent to NIL_VERTEX
			result.setData(i, PARENT, NIL_VERTEX);	
		}
		
		time = 0;
		for (i = 0; i < VERTICES; ++i) {
			if (result.getData(i, COLOR) == WHITE) {
				dfsVisit(result, i);
			}
		}
		return result;
	}
	
	/**
	 * This version conducts a DFS based on the content
	 * of vertex satellite data--thus allowing the ordering necessary 
	 * for running stronglyConnectedComponents (CLRS, p. 617).
	 * The orderField column is assumed to contain exclusively non-negative
	 * values and is assumed to contain a unique value for each
	 * vertex.
	 * Note: This method will normally modify the order of the
	 * vertex adjacency lists in the calling graph.
	 * If edge types (tree, back, forward cross), the graph must be
	 * converted to a WeightedLinkedListGraph and the corresponding
	 * method used in that class.
	 * @param orderField Index of data field to be used for ordering
	 * @param orderMax Maximum value for satellite data field
	 * @param asc True for sorting vertices using the satellite data
	 * in ascending order. False for sorting in descending order.
	 * @return
	 */
	public LinkedListGraph depthFirstSearch(int orderField, int orderMax, boolean asc) {
		final int VERTICES = vertices();
		final char OFFSET = offset();
		final boolean DIRECTED = false;
		final int DATA_FIELDS = 5;		
		int i;
		
		int[][] vArr = sortedVertices(orderField, orderMax, asc);
		
		// Re-order the adjacency lists of the calling graph
		for (i = 0; i < VERTICES; ++i) {
			if (!adj[i].isEmpty()) {
				sortAdjacencyList(adj[i], vArr[VERTEX_LOOKUP]);
			}
		}
		
		LinkedListGraph result = new LinkedListGraph(VERTICES, DIRECTED, OFFSET, DATA_FIELDS);
		result.setDataFieldLabel(COLOR, 'c');		
		result.setDataFieldLabel(PARENT, 'p');
		result.setDataFieldLabel(DISCOVERY_TIME, 'd');
		result.setDataFieldLabel(FINISH_TIME, 'f');
		result.setDataFieldLabel(TREE_NUMBER, 't');
				
		for (i = 0; i < VERTICES; ++i) {
			// set color to white
			result.setData(i, COLOR, WHITE);
			// set parent to NIL_VERTEX
			result.setData(i, PARENT, NIL_VERTEX);	
		}
		
		time = 0;
		int treeNum = 0;
		for (i = 0; i < VERTICES; ++i) {
			if (result.getData(vArr[VERTICES_SORTED][i], COLOR) == WHITE) {
				dfsVisit(result, vArr[VERTICES_SORTED][i], treeNum++);
			}
		}
		return result;
	}
	
	/**
	 * CLRS, p. 604
	 * @param g The result graph from depthFirstSearch()
	 * @param v The vertex being explored
	 */
	private void dfsVisit(LinkedListGraph res, int v) {
		res.setData(v, DISCOVERY_TIME, ++time);
		res.setData(v, COLOR, GRAY);
		
		ListIterator<Integer> it = adj[v].listIterator();
		int adjacency;
		while (it.hasNext()) {
			adjacency = it.next();
			if (res.getData(adjacency, COLOR) == WHITE) {
				// Insert edge into result graph
				res.adj[v].add(adjacency);
				res.adj[adjacency].add(v);
				// Set parent of adjacency to vertex v
				res.setData(adjacency, PARENT, v);
				dfsVisit(res, adjacency);
			}
		}
		// Blacken vertex v
		res.setData(v, COLOR, BLACK);
		// Set finish time for v
		res.setData(v, FINISH_TIME, ++time);
	}
	
	private void dfsVisit(LinkedListGraph res, int v, int treeNum) {
		res.setData(v, DISCOVERY_TIME, ++time);
		res.setData(v, COLOR, GRAY);
		res.setData(v, TREE_NUMBER, treeNum);
		
		ListIterator<Integer> it = adj[v].listIterator();
		int adjacency;
		while (it.hasNext()) {
			adjacency = it.next();
			if (res.getData(adjacency, COLOR) == WHITE) {
				// Insert edge into result graph
				res.adj[v].add(adjacency);
				res.adj[adjacency].add(v);
				// Set parent of adjacency to vertex v
				res.setData(adjacency, PARENT, v);
				dfsVisit(res, adjacency, treeNum);
			}
		}
		// Blacken vertex v
		res.setData(v, COLOR, BLACK);
		// Set finish time for v
		res.setData(v, FINISH_TIME, ++time);
	}
	
	private void dfsVisit(WeightedLinkedListGraph res, int v, int treeNum) {
		res.setData(v, DISCOVERY_TIME, ++time);
		res.setData(v, COLOR, GRAY);
		res.setData(v, TREE_NUMBER, treeNum);
		
		ListIterator<Integer> it = adj[v].listIterator();
		int adjacency;
		while (it.hasNext()) {
			adjacency = it.next();
			if (res.getData(adjacency, COLOR) == WHITE) {
				// Insert edge into result graph
				res.insert(v, adjacency);
				// Set parent of adjacency to vertex v
				res.setData(adjacency, PARENT, v);
				dfsVisit(res, adjacency, treeNum);
			}
		}
		// Blacken vertex v
		res.setData(v, COLOR, BLACK);
		// Set finish time for v
		res.setData(v, FINISH_TIME, ++time);
	}
	
	public LinkedList<Integer> topologicalSort() {
		final int OFFSET = offset();
		final int VERTICES = vertices();
		final int LAST_FINISH = 2 * VERTICES;
		
		// Will provide a list of vertices sorted by finish times
		int[] finishTimes = new int[LAST_FINISH + 1];
		int i;
		// Initialize array to -1 for all values
		for (i = 0; i <= LAST_FINISH; ++i) {
			finishTimes[i] = -1;
		}		
		// Get result of depth first search
		LinkedListGraph dfs = depthFirstSearch();
		// Update array values based on FINISH_TIME in dfs
		for (i = 0; i < VERTICES; ++i) {
			finishTimes[dfs.getData(i, FINISH_TIME)] = i;
		}
		LinkedList<Integer> result = new LinkedList<Integer>();
		// Insert vertices into list in proper order
		for (i = 2; i <= LAST_FINISH; ++i) {
			if (finishTimes[i] >= 0) result.addFirst(finishTimes[i] + OFFSET);
		}
		return result;
	}
	
	/**
	 * Returned graph is a forest, in which each strongly connected
	 * component of the calling graph is a distinct tree with a
	 * distinguishing tree number
	 * @return
	 */
	public LinkedListGraph stronglyConnectedComponents() {
		final int FINISH_COL = 0;
		final int VERTICES = vertices();
		LinkedListGraph dfs1 = depthFirstSearch();
		LinkedListGraph dfs2 = new LinkedListGraph((LinkedListGraph) this.transpose(), FINISH_COL + 1);
		for (int i = 0; i < VERTICES; ++i) {
			dfs2.setData(i, FINISH_COL, dfs1.getData(i, FINISH_TIME));
		}
		return dfs2.depthFirstSearch(FINISH_COL, 2 * vertices(), false);
	}
	
	/**
	 * Uses insertion sort, which should in sum be faster than counting
	 * sort if adj is small relative to the number of vertices--i.e., if the
	 * graph is sparse.
	 * The adjacency list (adj) is assumed to contain no duplicates
	 * @param adj
	 * @param orderArr
	 */
	private static void sortAdjacencyList(LinkedList<Integer> adj, int[] vertexLookup) {
		final int SIZE = adj.size();
		ListIterator<Integer> it = adj.listIterator();
		int i;
		
		// Dump adj into an array
		int[] adjArr = new int[SIZE];
		i = 0;
		while (it.hasNext()) {
			adjArr[i++] = it.next();
		}
		
		// Sort the array according to the ordering of vertexLookup
		int tmp, j;
		for (i = 1; i < SIZE; ++i) {
			tmp = adjArr[i];
			j = i - 1;
			while (j >= 0 && vertexLookup[adjArr[j]] > vertexLookup[tmp]) {
				adjArr[j + 1] = adjArr[j--];
			}
			adjArr[j + 1] = tmp;
		}
		
		// Reset the adjacency list
		it = adj.listIterator();
		i = 0;
		while (it.hasNext()) {
			it.next();
			it.set(adjArr[i++]);
		}
	}
}
