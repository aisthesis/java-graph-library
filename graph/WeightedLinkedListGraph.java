/**
 * @file
 */
package graph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.PriorityQueue;

/**
 * @author Marshall Farrier
 * @date 11/23/10
 *
 */
public class WeightedLinkedListGraph extends AbstractGraph implements Graph, WeightedGraph {
	
	private int e;	// Number of edges
	private int edgeProperties;	// Number of edge properties	
	
	// default weight is 1.0
	private class EdgeTarget {
		int v;
		double weight;
		int[] properties;
		EdgeTarget(int _v) {
			v = _v;
			weight = 1.0;
			if (edgeProperties > 0) properties = new int[edgeProperties];
			else properties = null;
		}
		EdgeTarget(int _v, double _w) {
			v = _v;
			weight = _w;
			if (edgeProperties > 0) properties = new int[edgeProperties];
			else properties = null;
		}
		EdgeTarget(int vert, double wt, int[] prop) {
			v = vert;
			weight = wt;
			properties = prop;
		}
		
		public EdgeTarget clone() {
			EdgeTarget et = new EdgeTarget(v, weight);
			for (int i = 0; i < edgeProperties; ++i) {
				et.properties[i] = this.properties[i];
			}
			return et;
		}
		/**
		 * Used implicitly when lists are checked using contains() method
		 * @param et
		 * @return
		 */
		@Override
		public boolean equals(Object et) {
			return v == ((EdgeTarget) et).v;
		}
	}
	private LinkedList<EdgeTarget> [] adj;
	// needed for temporary storage in several algorithms
	private double [] vertexWeight;
	
// constructors	
	public WeightedLinkedListGraph(int _v) {
		super(_v);
		e = 0;
		adj = new LinkedList[_v];
		for (int i = 0; i < _v; ++i) {
			adj[i] = new LinkedList<EdgeTarget>();
		}
		vertexWeight = new double[_v];
		edgeProperties = 0;
	}
	
	public WeightedLinkedListGraph(int _v, boolean _directed) {
		super(_v, _directed);
		e = 0;
		adj = new LinkedList[_v];
		for (int i = 0; i < _v; ++i) {
			adj[i] = new LinkedList<EdgeTarget>();
		}
		vertexWeight = new double[_v];
		edgeProperties = 0;
	}
	
	public WeightedLinkedListGraph(int _v, boolean _directed, int _offset) {
		super(_v, _directed, _offset);
		e = 0;
		adj = new LinkedList[_v];
		for (int i = 0; i < _v; ++i) {
			adj[i] = new LinkedList<EdgeTarget>();
		}
		vertexWeight = new double[_v];
		edgeProperties = 0;
	}
	
	public WeightedLinkedListGraph(int _v, boolean _directed, int _offset, int _dataFields) {
		super(_v, _directed, _offset, _dataFields);
		e = 0;
		adj = new LinkedList[_v];
		for (int i = 0; i < _v; ++i) {
			adj[i] = new LinkedList<EdgeTarget>();
		}
		vertexWeight = new double[_v];
		edgeProperties = 0;
	}
	
	public WeightedLinkedListGraph(int v, boolean dir, int off, int df, int edgeProp) {
		super(v, dir, off, df);
		if (edgeProp < 0) {
			throw new IllegalArgumentException("Number of edge properties cannot be negative");
		}
		e = 0;
		adj = new LinkedList[v];
		for (int i = 0; i < v; ++i) {
			adj[i] = new LinkedList<EdgeTarget>();
		}
		vertexWeight = new double[v];
		edgeProperties = edgeProp;
	}
	
	/**
	 * Edges in _g are assigned a default weight of 1.0
	 * @param _g
	 */
	public WeightedLinkedListGraph(LinkedListGraph _g) {
		super(_g);
		int _v = vertices();
		e = 0;
		edgeProperties = 0;
		adj = new LinkedList[_v];
		for (int i = 0; i < _v; ++i) {
			adj[i] = new LinkedList<EdgeTarget>();
		}
		vertexWeight = new double[_v];
		
		// insert edges
		Edge [] edges = _g.getEdges();
		int len = edges.length;
		int _offset = offset();
		for (int i = 0; i < len; ++i) {
			adj[edges[i].from() - _offset].add(new EdgeTarget(edges[i].to() - _offset));
			if (!directed()) adj[edges[i].to() - _offset].add(new EdgeTarget(edges[i].from() - _offset));
			++e;
		}
	}
	
	public WeightedLinkedListGraph(LinkedListGraph g, int edgeProp) {
		super(g);
		if (edgeProp < 0) {
			throw new IllegalArgumentException("Number of edge properties cannot be negative");
		}
		final int VERTICES = vertices();
		final int OFFSET = offset();
		final boolean DIRECTED = directed();
		
		edgeProperties = edgeProp;
		adj = new LinkedList[VERTICES];
		for (int i = 0; i < VERTICES; ++i) {
			adj[i] = new LinkedList<EdgeTarget>();
		}
		vertexWeight = new double[VERTICES];
		
		// insert edges
		Edge[] edges = g.getEdges();
		e = edges.length;
		int i;
		
		if (OFFSET == 0) {
			if (DIRECTED) {
				for (i = 0; i < e; ++i) {
					adj[edges[i].from()].add(new EdgeTarget(edges[i].to()));
				}				
			}
			else {
				for (i = 0; i < e; ++i) {
					adj[edges[i].from()].add(new EdgeTarget(edges[i].to()));
					adj[edges[i].to()].add(new EdgeTarget(edges[i].from()));
				}
			}
		}
		else {
			if (DIRECTED) {
				for (i = 0; i < e; ++i) {
					adj[edges[i].from() - OFFSET].add(new EdgeTarget(edges[i].to() - OFFSET));
				}
			}
			else {
				for (i = 0; i < e; ++i) {
					adj[edges[i].from() - OFFSET].add(new EdgeTarget(edges[i].to() - OFFSET));
					adj[edges[i].to() - OFFSET].add(new EdgeTarget(edges[i].from() - OFFSET));
				}
			}			
		}
	}
	
	/**
	 * Edges in _g are assigned a default weight of 1.0
	 * @param _g
	 */
	public WeightedLinkedListGraph(MatrixGraph _g) {
		super(_g);
		int _v = vertices();
		e = 0;
		edgeProperties = 0;
		adj = new LinkedList[_v];
		for (int i = 0; i < _v; ++i) {
			adj[i] = new LinkedList<EdgeTarget>();
		}
		vertexWeight = new double[_v];
		
		// insert edges
		Edge [] edges = _g.getEdges();
		int len = edges.length;
		int _offset = offset();
		for (int i = 0; i < len; ++i) {
			adj[edges[i].from() - _offset].add(new EdgeTarget(edges[i].to() - _offset));
			if (!directed()) adj[edges[i].to() - _offset].add(new EdgeTarget(edges[i].from() - _offset));
			++e;
		}
	}
	
	public WeightedLinkedListGraph(MatrixGraph g, int edgeProp) {
		super(g);
		if (edgeProp < 0) {
			throw new IllegalArgumentException("Number of edge properties cannot be negative");
		}
		final int VERTICES = vertices();
		final boolean DIRECTED = directed();
		
		edgeProperties = edgeProp;
		adj = new LinkedList[VERTICES];
		for (int i = 0; i < VERTICES; ++i) {
			adj[i] = new LinkedList<EdgeTarget>();
		}
		vertexWeight = new double[VERTICES];
		
		// insert edges
		int i, j;
		final int MAX = VERTICES - 1;
		e = g.edges();
		
		
		if (DIRECTED) {
			for (i = 0; i < VERTICES; ++i) {
				for (j = 0; j < VERTICES; ++j) {
					if (g.adj(i, j) != 0) adj[i].add(new EdgeTarget(j));
				}
			}				
		}
		else {
			for (i = 0; i < MAX; ++i) {
				for (j = i + 1; j < VERTICES; ++j) {
					if (g.adj(i, j) != 0) {
						adj[i].add(new EdgeTarget(j));
						adj[j].add(new EdgeTarget(i));
					}
				}
			}
		}
		
	}
	
	public WeightedLinkedListGraph(WeightedMatrixGraph g) {
		super(g);
		final int VERTICES = vertices();
		final int MAX = VERTICES - 1;
		final boolean DIRECTED = directed();
		int i, j, k;
		int[] prop, prop2;
		double tmp;
		e = g.edges();
		edgeProperties = g.edgeProperties();
		adj = new LinkedList[VERTICES];
		vertexWeight = new double[VERTICES];
		for (i = 0; i < VERTICES; ++i) {
			adj[i] = new LinkedList<EdgeTarget>();
			// synchronize vertex weights
			vertexWeight[i] = g.vertexWeightNoOffset(i);
		}		
		
		// Insert edges
		if (DIRECTED) {
			if (edgeProperties == 0) {
				for (i = 0; i < VERTICES; ++i) {
					for (j = 0; j < VERTICES; ++j) {
						if (g.adj(i, j) != 0) {
							adj[i].add(new EdgeTarget(j, g.edgeWeightNoOffset(i, j)));
						}
					}
				}
			}
			else {
				for (i = 0; i < VERTICES; ++i) {
					for (j = 0; j < VERTICES; ++j) {
						if (g.adj(i, j) != 0) {
							prop = new int[edgeProperties];
							for (k = 0; k < edgeProperties; ++k) {
								prop[k] = g.edgePropertyNoOffset(i, j, k);
							}
							adj[i].add(new EdgeTarget(j, g.edgeWeightNoOffset(i, j), prop));
														
						}
					}
				}
			}			
		}
		else {
			if (edgeProperties == 0) {
				for (i = 0; i < MAX; ++i) {
					for (j = i + 1; j < VERTICES; ++j) {
						if (g.adj(i, j) != 0) {
							tmp = g.edgeWeightNoOffset(i, j);
							adj[i].add(new EdgeTarget(j, tmp));
							adj[j].add(new EdgeTarget(i, tmp));
						}
					}
				}
			}
			else {
				for (i = 0; i < MAX; ++i) {
					for (j = i + 1; j < VERTICES; ++j) {
						if (g.adj(i, j) != 0) {
							tmp = g.edgeWeightNoOffset(i, j);
							prop = new int[edgeProperties];
							prop2 = new int[edgeProperties];
							for (k = 0; k < edgeProperties; ++k) {
								prop[k] = g.edgePropertyNoOffset(i, j, k);
								prop2[k] = prop[k];
							}
							adj[i].add(new EdgeTarget(j, tmp, prop));
							adj[j].add(new EdgeTarget(i, tmp, prop2));							
						}
					}
				}
			}		
		}	
	}
	
	/**
	 * Resulting graph will initialize all edge properties to 0
	 * @param g
	 * @param edgeProp
	 */
	public WeightedLinkedListGraph(WeightedMatrixGraph g, int edgeProp) {
		super(g);
		if (edgeProp < 0) {
			throw new IllegalArgumentException("Number of edge properties cannot be negative");
		}
		final int VERTICES = vertices();
		final int MAX = VERTICES - 1;
		final boolean DIRECTED = directed();
		int i, j;
		double tmp;
		
		e = g.edges();
		edgeProperties = edgeProp;
		
		adj = new LinkedList[VERTICES];
		vertexWeight = new double[VERTICES];
		for (i = 0; i < VERTICES; ++i) {
			adj[i] = new LinkedList<EdgeTarget>();
			// synchronize vertex weights
			vertexWeight[i] = g.vertexWeightNoOffset(i);
		}
		
		// Insert edges
		if (DIRECTED) {		
			for (i = 0; i < VERTICES; ++i) {
				for (j = 0; j < VERTICES; ++j) {
					if (g.adj(i, j) != 0) {
						adj[i].add(new EdgeTarget(j, g.edgeWeightNoOffset(i, j)));
					}
				}
			}
		}
		else {
			for (i = 0; i < MAX; ++i) {
				for (j = i + 1; j < VERTICES; ++j) {
					if (g.adj(i, j) != 0) {
						tmp = g.edgeWeightNoOffset(i, j);
						adj[i].add(new EdgeTarget(j, tmp));
						adj[j].add(new EdgeTarget(i, tmp));
					}
				}
			}
		}
	}
	
	public WeightedLinkedListGraph(WeightedLinkedListGraph _g) {
		super(_g);
		int _v = vertices(), i;
		e = _g.e;
		edgeProperties = _g.edgeProperties;
		adj = new LinkedList[_v];
		vertexWeight = new double[_v];
		for (i = 0; i < _v; ++i) {
			adj[i] = new LinkedList<EdgeTarget>();
			// synchronize vertex weights
			vertexWeight[i] = _g.vertexWeight[i];
		}		
		
		// insert edges
		ListIterator<EdgeTarget> it;
		for (i = 0; i < _v; ++i) {
			it = _g.adj[i].listIterator();
			while (it.hasNext()) {
				adj[i].add(it.next());
			}
		}	
	}
	
	public WeightedLinkedListGraph(WeightedLinkedListGraph g, int edgeProp) {
		super(g);
		if (edgeProp < 0) {
			throw new IllegalArgumentException("Number of edge properties cannot be negative");
		}
		final int VERTICES = vertices();
		ListIterator<EdgeTarget> it;
		EdgeTarget et;
		
		e = g.e;
		edgeProperties = edgeProp;
		
		adj = new LinkedList[VERTICES];
		vertexWeight = new double[VERTICES];
		
		// Copy vertex properties and edges
		for (int i = 0; i < VERTICES; ++i) {
			adj[i] = new LinkedList<EdgeTarget>();
			// synchronize vertex weights
			vertexWeight[i] = g.vertexWeight[i];
			// Create adjacency list
			it = g.adj[i].listIterator();
			while (it.hasNext()) {
				et = it.next();
				adj[i].add(new EdgeTarget(et.v, et.weight));
			}
		}
	}
	
// methods inherited from Graph interface
	/**
	 * The inserted edge will have weight 1.0 by default
	 */
	@Override
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
		EdgeTarget _et = new EdgeTarget(_to);
		if (adj[_from].contains(_et)) return false;	// edge already present
		++e;
		adj[_from].add(_et);
		if (!_directed) {
			adj[_to].add(new EdgeTarget(_from));
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
		boolean found = adj[_from].remove(new EdgeTarget(_to));
		if (found) {
			--e;
			if (!_directed)	adj[_to].remove(new EdgeTarget(_from));
		}
		return found;
	}
	
	public boolean remove(Edge _e) {
		return remove(_e.from(), _e.to());
	}
	
	public int edges() { return e; }

	public Edge[] getEdges() {
		char _offset = offset();
		int _v =  vertices();
		Edge [] result = new Edge[e];
		if (e == 0) return result;
		int counter = 0;
		ListIterator<EdgeTarget> it;
		if (directed()) {
			for (int i = 0; i < _v; ++i) {
				it = adj[i].listIterator();
				while (it.hasNext()) {
					result[counter++] = new Edge(i + _offset, it.next().v + _offset, charRep());
				}
			}
		}
		else {
			int u, max = _v - 1;
			for (int i = 0; i < max; ++i) {
				it = adj[i].listIterator();
				while (it.hasNext()) {
					u = it.next().v;
					if (i < u) result[counter++] = new Edge(i + _offset, u + _offset, charRep());
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
		ListIterator<EdgeTarget> it = adj[_from].listIterator();
		while (it.hasNext()) {
			result[counter++] = new Edge(_from + _offset, it.next().v + _offset, charRep());
		}
		return result;
	}
	
	@Override
	public WeightedEdge[] getWeightedEdges() {
		final char OFFSET = offset();
		final int VERTICES =  vertices();
		final int MAX = VERTICES - 1;
		final boolean DIRECTED = directed();
		final boolean CHAR_REP = charRep();
		
		WeightedEdge[] result = new WeightedEdge[e];
		if (e == 0) return result;
		
		int counter = 0, i;
		ListIterator<EdgeTarget> it;
		EdgeTarget et;
		
		if (DIRECTED) {
			if (OFFSET == 0) {
				for (i = 0; i < VERTICES; ++i) {
					it = adj[i].listIterator();
					while (it.hasNext()) {
						et = it.next();
						result[counter++] = new WeightedEdge(i, et.v, et.weight);
					}
				}
			}
			else {
				for (i = 0; i < VERTICES; ++i) {
					it = adj[i].listIterator();
					while (it.hasNext()) {
						et = it.next();
						result[counter++] = new WeightedEdge(i + OFFSET, et.v + OFFSET, 
								CHAR_REP, et.weight);
					}
				}
			}
			
		}
		else {	// graph is undirected
			if (OFFSET == 0) {
				for (i = 0; i < MAX; ++i) {
					it = adj[i].listIterator();
					while (it.hasNext()) {
						et = it.next();
						if (i < et.v) {
							result[counter++] = new WeightedEdge(i, et.v, et.weight);
						}						
					}
				}
			}
			else {
				for (i = 0; i < MAX; ++i) {
					it = adj[i].listIterator();
					while (it.hasNext()) {
						et = it.next();
						if (i < et.v) {
							result[counter++] = new WeightedEdge(i + OFFSET, et.v + OFFSET, 
								CHAR_REP, et.weight);
						}
					}
				}
			}
		}
		return result;
	}
	
	@Override
	public WeightedEdge[] getWeightedEdges(int from) {
		final char OFFSET = offset();
		final boolean CHAR_REP = charRep();
		int vertex = from - OFFSET;
		final int SIZE = adj[vertex].size();
		
		if (vertex < 0 || vertices() <= vertex) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		
		WeightedEdge[] result = new WeightedEdge[SIZE];
		if (SIZE == 0) return result;
		
		int counter = 0;
		ListIterator<EdgeTarget> it = adj[vertex].listIterator();
		EdgeTarget et;
		
		if (OFFSET == 0) {
			while (it.hasNext()) {
				et = it.next();
				result[counter++] = new WeightedEdge(vertex, et.v, et.weight);
			}
		}
		else {
			while (it.hasNext()) {
				et = it.next();
				result[counter++] = new WeightedEdge(vertex + OFFSET, 
						et.v + OFFSET, CHAR_REP, et.weight);
			}
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
		ListIterator<EdgeTarget> it = adj[_from].listIterator();
		while (it.hasNext()) {
			result[counter++] = it.next().v + _offset;
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
		return adj[_from].contains(new EdgeTarget(_to));
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
		WeightedLinkedListGraph result = new WeightedLinkedListGraph(_v, _directed, _offset, _d);
		ListIterator<EdgeTarget> it;
		int j;
		EdgeTarget _et;
		for (int i = 0; i < _v; ++i) {
			// transfer transposed edges
			it = adj[i].listIterator();
			while (it.hasNext()) {
				_et = it.next();
				result.adj[_et.v].add(new EdgeTarget(i, _et.weight));
			}
			// copy vertex data
			result.vertexWeight[i] = vertexWeight[i];
			for (j = 0; j < _d; ++j) {
				result.setData(i, j, getData(i, j));
			}
		}
		return result;
	}
	
// methods inherited from WeightedGraph
	@Override
	public int edgeProperties() { return edgeProperties; }
	@Override
	public void setEdgeWeight(int _f, int _t, double _w) {
		char _offset = offset();
		boolean _directed = directed();
		int _v = vertices();
		int _from = _f - _offset;
		int _to = _t - _offset;
		if (_from < 0 || _v <= _from) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		if (_to < 0 || _v <= _to) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		boolean found = adj[_from].remove(new EdgeTarget(_to));
		if (!found) {
			throw new IllegalArgumentException("Invalid edge");
		}
		adj[_from].add(new EdgeTarget(_to, _w));
		if (!_directed) {
			adj[_to].remove(new EdgeTarget(_from));
			adj[_to].add(new EdgeTarget(_from, _w));
		}
	}
	public void setEdgeWeight(Edge _e, double _w) {
		setEdgeWeight(_e.from(), _e.to(), _w);
	}
	@Override
	public void setEdgeProperty(int from, int to, int property, int value) {
		final char OFFSET = offset();
		final boolean DIRECTED = directed();
		final int VERTICES = vertices();
		int f = from - OFFSET;
		int t = to - OFFSET;
		
		if (f < 0 || VERTICES <= f) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		if (t < 0 || VERTICES <= t) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		if (property < 0 || edgeProperties <= property) {
			throw new IllegalArgumentException("Invalid property");
		}
		
		ListIterator<EdgeTarget> it = adj[f].listIterator();
		EdgeTarget et;
		while (it.hasNext()) {
			et = it.next();
			if (et.v == t) {
				// Change the given property
				et.properties[property] = value;
				// If the graph is undirected, change the other adjacency list
				if (!DIRECTED) {
					it = adj[t].listIterator();
					et = it.next();
					if (et.v == f) et.properties[property] = value;
				}
				return;
			}
		}
		// If we reach this point, the edge was not found
		throw new IllegalArgumentException("Invalid edge");
	}
	@Override
	public void setEdgeProperty(Edge e, int property, int value) {
		setEdgeProperty(e.from(), e.to(), property, value);
	}
	
	@Override
	public double getEdgeWeight(int _f, int _t) {
		char _offset = offset();
		int _from = _f - _offset;
		int _to = _t - _offset;
		int _v = vertices();
		if (_from < 0 || _v <= _from) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		if (_to < 0 || _v <= _to) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		ListIterator<EdgeTarget> it = adj[_from].listIterator();
		EdgeTarget _et;
		while (it.hasNext()) {
			_et = it.next();
			if (_et.v == _to) return _et.weight;
		}
		throw new IllegalArgumentException("Invalid edge");
	}
	@Override
	public double getEdgeWeight(Edge _e) {
		return getEdgeWeight(_e.from(), _e.to());
	}
	@Override
	public int getEdgeProperty(int from, int to, int property) {
		final char OFFSET = offset();
		final int VERTICES = vertices();
		int f = from - OFFSET;
		int t = to - OFFSET;
		
		if (f < 0 || VERTICES <= f) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		if (t < 0 || VERTICES <= t) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		if (property < 0 || edgeProperties <= property) {
			throw new IllegalArgumentException("Invalid property");
		}
		
		ListIterator<EdgeTarget> it = adj[f].listIterator();
		EdgeTarget et;
		while (it.hasNext()) {
			et = it.next();
			if (et.v == t) return et.properties[property];
		}
		throw new IllegalArgumentException("Invalid edge");
	}
	@Override
	public int getEdgeProperty(Edge e, int property) {
		return getEdgeProperty(e.from(), e.to(), property);
	}
	
	@Override
	public void setVertexWeight(int _v, double _w) {
		int _vert = _v - offset();
		if (_vert < 0 || vertices() <= _vert) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		vertexWeight[_vert] = _w;
	}
	
	public double getVertexWeight(int _v) {
		int _vert = _v - offset();
		if (_vert < 0 || vertices() <= _vert) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		return vertexWeight[_vert];
	}
	
	/**
	 * no offset, no validation
	 * @param _v
	 * @return
	 */
	protected double uncheckedVertexWeight(int _v) {
		return vertexWeight[_v];
	}
	
	// returns false and does nothing if edge already present
	public boolean insert(int _f, int _t, double _w) {
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
		EdgeTarget _et = new EdgeTarget(_to, _w);
		if (adj[_from].contains(_et)) return false;	// edge already present
		++e;
		adj[_from].add(_et);
		if (!_directed) {
			adj[_to].add(new EdgeTarget(_from, _w));
		}
		return true;
	}
	public boolean insert(Edge _e, double _w) {
		return insert(_e.from(), _e.to(), _w);
	}
	/**
	 * Completely unchecked insertion for maximum speed.
	 * @param from
	 * @param et
	 */
	protected void insert(int from, int to, double wt, int[] prop) {
		adj[from].add(new EdgeTarget(to, wt, prop));
		if (!directed()) {
			
			// Make a copy of prop
			final int SIZE = prop.length;
			int[] tmp = new int[SIZE];
			for (int i = 0; i < SIZE; ++i) {
				tmp[i] = prop[i];
			}
			adj[to].add(new EdgeTarget(from, wt, tmp));
		}
		++e;
	}

// methods inherited from Object
	// returns a deep copy of calling object
	public WeightedLinkedListGraph clone() {
		return new WeightedLinkedListGraph(this);
	}
	
	public String toString() {
		char _offset = offset();
		int _v =  vertices();
		boolean _charRep = charRep();
		String result = "";
		int i, j, lim = _v - 1;
		ListIterator<EdgeTarget> it;
		if (_charRep) {
			for (i = 0; i < _v; ++i) {
				result += (char)(i + _offset) + ": ";
				it = adj[i].listIterator();
				if (it.hasNext()) {
					result += (char)(it.next().v + _offset);
					while (it.hasNext()) {
						result += " -> " + (char)(it.next().v + _offset);
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
					result += it.next().v;
					while (it.hasNext()) {
						result += " -> " + it.next().v;
					}					
				}			
				if (i < lim) result += "\n";
			}
		}
		return result;
	}
	
	/**
	 * Displays edges as well as weights
	 * @param _precision Precision with which to display edge weights
	 * @return
	 */
	public String toString(int _precision) {
		if (_precision < 0) {
			throw new IllegalArgumentException("Precision cannot be negative");
		}
		char _offset = offset();
		int _v =  vertices();
		boolean _charRep = charRep();
		String result = "";
		int i, j, lim = _v - 1;
		ListIterator<EdgeTarget> it;
		EdgeTarget _et;
		if (_charRep) {
			for (i = 0; i < _v; ++i) {
				result += (char)(i + _offset) + ": ";
				it = adj[i].listIterator();
				if (it.hasNext()) {
					_et = it.next();
					result += (char)(_et.v + _offset);
					result += String.format("(%." + _precision + "f)", _et.weight);
					while (it.hasNext()) {
						_et = it.next();
						result += " -> " + (char)(_et.v + _offset);
						result += String.format("(%." + _precision + "f)", _et.weight);
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
					result += it.next().v;
					while (it.hasNext()) {
						result += " -> " + it.next().v;
					}					
				}			
				if (i < lim) result += "\n";
			}
		}
		return result;		
	}
	
	/**
	 * Returns a linked list of all edges for which the specified property
	 * has the given value
	 * @param property
	 * @param value
	 * @return
	 */
	public LinkedList<Edge> getEdgesByProperty(int property, int value) {
		if (property < 0 || edgeProperties <= property) {
			throw new IllegalArgumentException("Invalid property");
		}
		
		final boolean DIRECTED = directed();
		final int VERTICES = vertices();
		final int OFFSET = offset();
		final boolean CHAR_REP = charRep();
		final int MAX = VERTICES - 1;
		LinkedList<Edge> result = new LinkedList<Edge>();
		ListIterator<EdgeTarget> it;
		EdgeTarget et;
		int i;
		
		// Get edges
		if (DIRECTED) {
			if (CHAR_REP) {
				for (i = 0; i < VERTICES; ++i) {
					it = adj[i].listIterator();
					while (it.hasNext()) {
						et = it.next();
						if (et.properties[property] == value) {
							result.add(new Edge(i + OFFSET, et.v + OFFSET, CHAR_REP));
						}
					}
				}
			}
			else {
				for (i = 0; i < VERTICES; ++i) {
					it = adj[i].listIterator();
					while (it.hasNext()) {
						et = it.next();
						if (et.properties[property] == value) {
							result.add(new Edge(i, et.v));
						}
					}
				}
			}			
		}
		else {
			if (CHAR_REP) {
				for (i = 0; i < MAX; ++i) {
					it = adj[i].listIterator();
					while (it.hasNext()) {
						et = it.next();
						if (i < et.v && et.properties[property] == value) {
							result.add(new Edge(i + OFFSET, et.v + OFFSET, CHAR_REP));
						}
					}
				}
			}
			else {
				for (i = 0; i < MAX; ++i) {
					it = adj[i].listIterator();
					while (it.hasNext()) {
						et = it.next();
						if (i < et.v && et.properties[property] == value) {
							result.add(new Edge(i, et.v));
						}
					}
				}
			}			
		}
		return result;
	}
	
	/**
	 * CLRS, p. 604
	 * The most complete version of DFS in the sense of providing 
	 * the most information.
	 * Note here that all edge properties in the calling object are lost!!!
	 * They are replaced by the single edge property EDGE_TYPE, which
	 * classifies each edge as a tree, back, forward or cross edge.
	 * If the graph contains important edge data, this method should
	 * be run only on a copy of the original graph.
	 * @return
	 */
	public LinkedListGraph depthFirstSearch(int orderField, int orderMax, boolean asc) {
		final int VERTICES = vertices();
		final char OFFSET = offset();
		final boolean DIRECTED = false;
		final int DATA_FIELDS = 5;
		edgeProperties = 1;
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
	 * This version also classifies edges in the original graph
	 * as tree, back, forward or cross edges
	 * @param res The result graph from depthFirstSearch()
	 * @param v The vertex being explored
	 * @param treeNum
	 */
	private void dfsVisit(LinkedListGraph res, int v, int treeNum) {
		res.setData(v, DISCOVERY_TIME, ++time);
		res.setData(v, COLOR, GRAY);
		res.setData(v, TREE_NUMBER, treeNum);
		
		ListIterator<EdgeTarget> it = adj[v].listIterator();
		EdgeTarget adjacency;
		while (it.hasNext()) {
			adjacency = it.next();
			if (res.getData(adjacency.v, COLOR) == WHITE) {
				// Show edge as tree edge (in calling graph)
				adjacency.properties = new int[edgeProperties];
				adjacency.properties[EDGE_TYPE] = TREE_EDGE;
				
				// Insert edge into result graph
				res.uncheckedInsert(v, adjacency.v);
				// Set parent of adjacency to vertex v
				res.setData(adjacency.v, PARENT, v);
				dfsVisit(res, adjacency.v, treeNum);
			}
			else if (res.getData(adjacency.v, COLOR) == GRAY) {
				if (directed() || res.getData(adjacency.v, PARENT) != v) {
					adjacency.properties = new int[edgeProperties];
					adjacency.properties[EDGE_TYPE] = BACK_EDGE;
				}
				else {
					adjacency.properties = new int[edgeProperties];
					adjacency.properties[EDGE_TYPE] = TREE_EDGE;
				}
			}
			else {	// vertex color is black
				if (res.getData(adjacency.v, TREE_NUMBER) == treeNum) {
					adjacency.properties = new int[edgeProperties];
					adjacency.properties[EDGE_TYPE] = FORWARD_EDGE;
				}
				else {
					adjacency.properties = new int[edgeProperties];
					adjacency.properties[EDGE_TYPE] = CROSS_EDGE;
				}
			}
		}
		// Blacken vertex v
		res.setData(v, COLOR, BLACK);
		// Set finish time for v
		res.setData(v, FINISH_TIME, ++time);
	}
	
	/**
	 * CLRS, p. 631
	 * @return
	 */
	public WeightedLinkedListGraph minSpanningTreeKruskal() {
		final int VERTICES = vertices();
		// Tree will be undirected
		final boolean DIRECTED = false;
		final int OFFSET = offset();
		final int NUM_EDGES = edges();
		int i, from, to;
		
		// This empty graph corresponds to A in CLRS
		WeightedLinkedListGraph result = new WeightedLinkedListGraph(VERTICES, DIRECTED,
				OFFSET);
		
		DisjointSet s = new DisjointSet(VERTICES);
		WeightedEdge[] edges = getWeightedEdges();
		Arrays.sort(edges);
		
		if (OFFSET == 0) {
			for (i = 0; i < NUM_EDGES && result.e < VERTICES; ++i) {
				if (s.findSet(edges[i].from()) != s.findSet(edges[i].to())) {
					from = edges[i].from();
					to = edges[i].to();
					// Insert edge into spanning tree
					result.adj[from].add(new EdgeTarget(to,	edges[i].weight()));
					result.adj[to].add(new EdgeTarget(from,	edges[i].weight()));
					++result.e;
					s.union(from, to);
				}
			}
		}
		else {
			for (i = 0; i < NUM_EDGES && result.e < VERTICES; ++i) {
				if (s.findSet(edges[i].from() - OFFSET) != s.findSet(edges[i].to() - OFFSET)) {
					from = edges[i].from() - OFFSET;
					to = edges[i].to() - OFFSET;
					// Insert edge into spanning tree
					result.adj[from].add(new EdgeTarget(to,	edges[i].weight()));
					result.adj[to].add(new EdgeTarget(from,	edges[i].weight()));
					++result.e;
					s.union(from, to);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * CLRS, p. 634
	 * @return
	 */
	public WeightedLinkedListGraph minSpanningTreePrim(int start) {
		final int VERTICES = vertices();
		final int OFFSET = offset();
		int r = start - OFFSET;
		if (r < 0 || VERTICES <= r) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		// Tree will be undirected
		final boolean DIRECTED = false;
		
		// Prim's algorithm also provides a parent in the resulting tree
		final int DATA_FIELDS = 2;
		int i;
		ListIterator<EdgeTarget> it;
		EdgeTarget et;
		
		// Empty graph corresponds to A in CLRS
		WeightedLinkedListGraph result = new WeightedLinkedListGraph(VERTICES, DIRECTED,
				OFFSET, DATA_FIELDS);
		PriorityQueue<WeightedVertex> q = new PriorityQueue<WeightedVertex>(VERTICES,
				new WeightedVertex.Heavier());
		// For fast retrieval of weight
		double[] weights = new double[VERTICES];
		boolean[] inQueue = new boolean[VERTICES];
		
		for (i = 0; i < VERTICES; ++i) {
			q.add(new WeightedVertex(i, Double.MAX_VALUE));
			weights[i] = Double.MAX_VALUE;
			inQueue[i] = true;
			result.setData(i, PARENT, NIL_VERTEX);
		}
		
		q.remove(new WeightedVertex(r));
		q.add(new WeightedVertex(r, 0.0));
		
		while (q.size() != 0) {
			i = q.poll().vertex();
			inQueue[i] = false;
			it = adj[i].listIterator();
			while (it.hasNext()) {
				et = it.next();
				if (inQueue[et.v] && et.weight < weights[et.v]) {
					result.setData(et.v, PARENT, i);
					weights[et.v] = et.weight;
					q.remove(new WeightedVertex(et.v));
					q.add(new WeightedVertex(et.v, et.weight));
				}
			}
		}
		
		// Edges still need to be inserted
		for (i = 0; i < r; ++i) {
			result.adj[i].add(new EdgeTarget(result.getData(i, PARENT), weights[i]));
			result.adj[result.getData(i, PARENT)].add(new EdgeTarget(i, weights[i]));
			++result.e;
		}
		for (i = r + 1; i < VERTICES; ++i) {
			result.adj[i].add(new EdgeTarget(result.getData(i, PARENT), weights[i]));
			result.adj[result.getData(i, PARENT)].add(new EdgeTarget(i, weights[i]));
			++result.e;
		}
		
		return result;
	}
	
	/**
	 * Uses insertion sort, which should in sum be faster than counting
	 * sort if adjacencies is small relative to the number of vertices--i.e., if the
	 * graph is sparse.
	 * The adjacency list (adjacencies) is assumed to contain no duplicates
	 * @param adj
	 * @param orderArr
	 */
	private static void sortAdjacencyList(LinkedList<EdgeTarget> adjacencies, int[] vertexLookup) {
		final int SIZE = adjacencies.size();
		ListIterator<EdgeTarget> it = adjacencies.listIterator();
		int i, j;
		
		// Dump adjacencies into an array
		EdgeTarget[] adjArr = new EdgeTarget[SIZE];
		i = 0;
		while (it.hasNext()) {
			adjArr[i++] = it.next();
		}
		
		// Sort the array according to the ordering of vertexLookup
		EdgeTarget tmp;
		for (i = 1; i < SIZE; ++i) {
			tmp = adjArr[i];
			j = i - 1;
			while (j >= 0 && vertexLookup[adjArr[j].v] > vertexLookup[tmp.v]) {
				adjArr[j + 1] = adjArr[j--];
			}
			adjArr[j + 1] = tmp;
		}
		
		// Reset the adjacency list
		it = adjacencies.listIterator();
		i = 0;
		while (it.hasNext()) {
			it.next();
			it.set(adjArr[i++]);
		}
	}
}
