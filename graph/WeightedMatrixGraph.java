/**
 * @file
 */
package graph;

/**
 * @author Marshall Farrier
 * @date 11/24/10
 *
 */
public class WeightedMatrixGraph extends MatrixGraph implements Graph, WeightedGraph {
	private double[][] edgeWeight;
	private double[] vertexWeight;
	private int edgeProperties;
	private int[][][] properties;
	
// constructors
	public WeightedMatrixGraph(int _v) {
		super(_v);
		edgeWeight = new double[_v][_v];
		vertexWeight = new double[_v];
		properties = null;
	}
	public WeightedMatrixGraph(int _v, boolean _directed) {
		super(_v, _directed);
		edgeWeight = new double[_v][_v];
		vertexWeight = new double[_v];
		properties = null;
	}	
	public WeightedMatrixGraph(int _v, boolean _directed, int _offset) {
		super(_v, _directed, _offset);
		edgeWeight = new double[_v][_v];
		vertexWeight = new double[_v];
		properties = null;
	}
	public WeightedMatrixGraph(int _v, boolean _directed, int _offset, int _dataFields) {
		super(_v, _directed, _offset, _dataFields);
		edgeWeight = new double[_v][_v];
		vertexWeight = new double[_v];
		properties = null;
	}
	public WeightedMatrixGraph(int v, boolean dir, int off, int df, int edgeProp) {
		super(v, dir, off, df);
		if (edgeProp < 0) {
			throw new IllegalArgumentException("Number of edge properties cannot be negative");
		}
		edgeWeight = new double[v][v];
		vertexWeight = new double[v];
		properties = new int[edgeProp][v][v];
	}
	/**
	 * Edge weights are set by default to 0
	 * in this representation (1.0 for WeightedLinkedListGraph)
	 * @param _g
	 */
	public WeightedMatrixGraph(MatrixGraph _g) {
		super(_g);
		int _v = _g.vertices();
		edgeWeight = new double[_v][_v];
		vertexWeight = new double[_v];
	}
	public WeightedMatrixGraph(WeightedMatrixGraph _g) {
		super(_g);
		int _v = _g.vertices();
		edgeWeight = new double[_v][_v];
		vertexWeight = new double[_v];
		int i, j;
		for (i = 0; i < _v; ++i) {
			vertexWeight[i] = _g.vertexWeight[i];
			for (j = 0; j < _v; ++j) {
				edgeWeight[i][j] = _g.edgeWeight[i][j];
			}
		}
	}
	/**
	 * Edge weights are set by default to 0
	 * in this representation (1.0 for WeightedLinkedListGraph)
	 * @param _g
	 */
	public WeightedMatrixGraph(LinkedListGraph _g) {
		super(_g);
		int _v = _g.vertices();
		edgeWeight = new double[_v][_v];
		vertexWeight = new double[_v];
	}
	public WeightedMatrixGraph(WeightedLinkedListGraph _g) {
		super(_g);
		int _v = _g.vertices();
		edgeWeight = new double[_v][_v];
		vertexWeight = new double[_v];
		Edge [] edges = _g.getEdges();
		int len = edges.length, i;
		for (i = 0; i < len; ++i) {
			insert(edges[i].from(), edges[i].to(), _g.getEdgeWeight(edges[i]));
		}
		for (i = 0; i < _v; ++i) {
			vertexWeight[i] = _g.uncheckedVertexWeight(i);
		}
	}
	
// methods inherited from MatrixGraph
	@Override
	public boolean remove(int _f, int _t) {
		boolean found = super.remove(_f, _t);
		char _offset = offset();
		int _from = _f - _offset, _to = _t - _offset;
		
		if (found) {
			edgeWeight[_from][_to] = 0;
			if (!directed()) {
				edgeWeight[_to][_from] = 0;
			}
		}
		return found;
	}
	@Override
	public boolean remove(Edge _e) {
		return remove(_e.from(), _e.to());
	}
	
// methods inherited from WeightedGraph
	@Override
	public WeightedEdge[] getWeightedEdges() {
		final char OFFSET = offset();
		final int VERTICES =  vertices();
		final int MAX = VERTICES - 1;
		final boolean DIRECTED = directed();
		final boolean CHAR_REP = charRep();
		final int EDGES = edges();
		int i, j, counter = 0;
		
		WeightedEdge[] result = new WeightedEdge[EDGES];
		if (EDGES == 0) return result;
		
		if (DIRECTED) {
			if (OFFSET == 0) {
				for (i = 0; i < VERTICES; ++i) {
					for (j = 0; j < VERTICES; ++j) {
						if (adj(i, j) == 1) {
							result[counter++] = new WeightedEdge(i, j, edgeWeight[i][j]);
						}
					}
				}
			}
			else {
				for (i = 0; i < VERTICES; ++i) {
					for (j = 0; j < VERTICES; ++j) {
						if (adj(i, j) == 1) {
							result[counter++] = new WeightedEdge(i + OFFSET, j + OFFSET,
									CHAR_REP, edgeWeight[i][j]);
						}
					}
				}
			}
		}
		else {
			if (OFFSET == 0) {
				for (i = 0; i < MAX; ++i) {
					for (j = i + 1; j < VERTICES; ++j) {
						if (adj(i, j) == 1) {
							result[counter++] = new WeightedEdge(i, j, edgeWeight[i][j]);
						}
					}
				}
			}
			else {
				for (i = 0; i < MAX; ++i) {
					for (j = i + 1; j < VERTICES; ++j) {
						if (adj(i, j) == 1) {
							result[counter++] = new WeightedEdge(i + OFFSET, j + OFFSET,
									CHAR_REP, edgeWeight[i][j]);
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
		final int VERTICES = vertices();
		int vertex = from - OFFSET;
		int i, counter = 0;
		
		// Determine size of result array
		int size = 0;
		for (i = 0; i < VERTICES; ++i) {
			if (adj(vertex, i) == 1) ++size;
		}
		
		// Create array
		WeightedEdge[] result = new WeightedEdge[size];
		// Update values
		if (OFFSET == 0) {
			for (i = 0; i < VERTICES; ++i) {
				if (adj(vertex, i) == 1) {
					result[counter++] = new WeightedEdge(vertex, i, edgeWeight[vertex][i]);
				}				
			}
		}
		else {
			for (i = 0; i < VERTICES; ++i) {
				if (adj(vertex, i) == 1) {
					result[counter++] = new WeightedEdge(vertex + OFFSET, i + OFFSET,
							CHAR_REP, edgeWeight[vertex][i]);
				}				
			}
		}
		return result;
	}
	@Override
	public int edgeProperties() { return edgeProperties; }
	@Override
	public void setEdgeWeight(int _f, int _t, double _w) {
		int _offset = offset();
		int _from = _f  - _offset;
		int _to = _t - _offset;
		int _v = vertices();
		if (_from < 0 || _v <= _from) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		if (_to < 0 || _v <= _to) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		if (adj(_from, _to) == 0) {
			throw new IllegalArgumentException("Invalid edge");
		}
		edgeWeight[_from][_to] = _w;
		if (!directed()) {
			edgeWeight[_to][_from] = _w;
		}
	}
	@Override
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
		if (adj(f, t) == 0) {
			throw new IllegalArgumentException("Invalid edge");
		}
		if (property < 0 || edgeProperties <= property) {
			throw new IllegalArgumentException("Invalid property");
		}
		properties[property][f][t] = value;
		if (!DIRECTED) properties[property][t][f] = value;
	}
	@Override
	public void setEdgeProperty(Edge e, int property, int value) {
		setEdgeProperty(e.from(), e.to(), property, value);
	}
	
	public double getEdgeWeight(int _f, int _t) {
		int _offset = offset();
		int _from = _f  - _offset;
		int _to = _t - _offset;
		int _v = vertices();
		if (_from < 0 || _v <= _from) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		if (_to < 0 || _v <= _to) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		if (adj(_from, _to) == 0) {
			throw new IllegalArgumentException("Invalid edge");
		}
		return edgeWeight[_from][_to];
	}
	
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
		if (adj(f, t) == 0) {
			throw new IllegalArgumentException("Invalid edge");
		}
		if (property < 0 || edgeProperties <= property) {
			throw new IllegalArgumentException("Invalid property");
		}
		return properties[property][f][t];
	}
	
	@Override
	public int getEdgeProperty(Edge e, int property) {
		return getEdgeProperty(e.from(), e.to(), property);
	}
	
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
	// for use within package
	protected double vertexWeightNoOffset(int _v) {
		return vertexWeight[_v];
	}
	protected double edgeWeightNoOffset(int from, int to) {
		return edgeWeight[from][to];
	}
	protected int edgePropertyNoOffset(int from, int to, int prop) {
		return properties[prop][from][to];
	}
	
	// returns false and does nothing if edge already present
	public boolean insert(int _f, int _t, double _w) {
		if (!insert(_f, _t)) return false;		
		int _offset = offset();
		int _from = _f - _offset;
		int _to = _t - _offset;
		edgeWeight[_from][_to] = _w;
		if (!directed()) {
			edgeWeight[_to][_from] = _w;
		}		
		return true;
	}
	
	public boolean insert(Edge _e, double _w) {
		return insert(_e.from(), _e.to(), _w);
	}


}
