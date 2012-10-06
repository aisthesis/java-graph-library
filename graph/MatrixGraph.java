/**
 * @file
 */
package graph;

/**
 * @author Marshall Farrier
 * @date 11/23/10
 *
 */
public class MatrixGraph extends AbstractGraph {
	private int e;
	private int [][] adj;
	// no bounds checking, etc.
	protected int adj(int i, int j) {
		return adj[i][j];
	}
	
// constructors
	public MatrixGraph(int _v) {
		super(_v);
		e = 0;
		adj = new int[_v][_v];
	}
	public MatrixGraph(int _v, boolean _directed) {
		super(_v, _directed);
		e = 0;
		adj = new int[_v][_v];
	}	
	public MatrixGraph(int _v, boolean _directed, int _offset) {
		super(_v, _directed, _offset);
		e = 0;
		adj = new int[_v][_v];	
	}
	public MatrixGraph(int _v, boolean _directed, int _offset, int _dataFields) {
		super(_v, _directed, _offset, _dataFields);
		e = 0;
		adj = new int[_v][_v];
	}
	public MatrixGraph(MatrixGraph _g) {
		super(_g);
		int _v = _g.vertices();
		e = _g.e;
		adj = new int[_v][_v];
		int i, j;
		for (i = 0; i < _v; ++i) {
			for (j = 0; j < _v; ++j) {
				adj[i][j] = _g.adj[i][j];
			}
		}
	}
	public MatrixGraph(LinkedListGraph _g) {
		super(_g);
		int _v = _g.vertices();
		e = 0;
		adj = new int[_v][_v];
		Edge [] edges = _g.getEdges();
		int len = edges.length;
		for (int i = 0; i < len; ++i) {
			this.insert(edges[i]);
		}
	}
	public MatrixGraph(WeightedLinkedListGraph _g) {
		super(_g);
		int _v = _g.vertices();
		e = 0;
		adj = new int[_v][_v];
		Edge [] edges = _g.getEdges();
		int len = edges.length;
		for (int i = 0; i < len; ++i) {
			this.insert(edges[i]);
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
		if (adj[_from][_to] == 1) return false;	// edge already present
		++e;
		adj[_from][_to] = 1;
		if (!_directed) {
			adj[_to][_from] = 1;
		}
		return true;
	}
	public boolean insert(Edge _e) {
		return insert(_e.from(), _e.to());
	}
	
	public boolean remove(int _f, int _t) {
		char _offset = offset();
		int _v =  vertices();
		int _from = _f - _offset, _to = _t - _offset;
		boolean _directed = directed();
		if (_from < 0 || _to < 0) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		if (_from >= _v || _to >= _v) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		if (_from == _to && !_directed) {
			throw new IllegalArgumentException("Self-edge not allowed in an undirected graph");
		}
		boolean found = false;
		if (adj[_from][_to] == 1) {
			found = true;
			adj[_from][_to] = 0;
		}
		if (found) {
			--e;
			if (!_directed) adj[_to][_from] = 0;
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
		if (directed()) {
			for (int i = 0; i < _v; ++i) {
				for (int j = 0; j < _v; ++j) {
					if (adj[i][j] == 1) {
						result[counter++] = new Edge(i + _offset, j + _offset, charRep());
					}
				}
			}
		}
		else {
			int max = _v - 1;
			for (int i = 0; i < max; ++i) {
				for (int j = i + 1; j < _v; ++j) {
					if (adj[i][j] == 1) {
						result[counter++] = new Edge(i + _offset, j + _offset, charRep());
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
		int _size = 0, i;
		for (i = 0; i < _v; ++i) {
			if (adj[_from][i] == 1) ++_size;
		}
		Edge [] result = new Edge[_size];
		int counter = 0;
		for (i = 0; i < _v; ++i) {
			if (adj[_from][i] == 1) {
				result[counter++] = new Edge(_from + _offset, i + _offset, charRep());
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
		int _size = 0, i;
		for (i = 0; i < _v; ++i) {
			if (adj[_from][i] == 1) ++_size;
		}
		int [] result = new int[_size];
		int counter = 0;
		for (i = 0; i < _v; ++i) {
			if (adj[_from][i] == 1) {
				result[counter++] = i + _offset;
			}
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
		if (adj[_from][_to] == 1) return true;
		return false;
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
		MatrixGraph result = new MatrixGraph(_v, _directed, _offset, _d);
		int j;
		for (int i = 0; i < _v; ++i) {
			// transpose edges
			for (j = 0; j < _v; ++ j) {
				result.adj[j][i] = adj[i][j];
			}
			// copy vertex data
			for (j = 0; j < _d; ++j) {
				result.setData(i, j, getData(i, j));
			}
		}
		return result;
	}
	
	public String toString() {
		char _offset = offset();
		int _v =  vertices();
		boolean _charRep = charRep();
		String result = "";
		int width = 1;
		if (!_charRep) width = String.valueOf(_v - 1).length();
		int i, j, currSpaces, k;
		
		for (i = 0; i < width; ++i) {
			result += " ";
		}
		result += " "; // corresponds to ":"
		if (_charRep) {
			// column headings
			for (i = 0; i < _v; ++i) {
				result += " " + (char)(i + _offset);
			}
			result += "\n";
			// rows			
			for (i = 0; i < _v; ++i) {
				result += (char)(i + _offset) + ":";
				for (j = 0; j < _v; ++j) {
					result += " " + adj[i][j];
				}
				result += "\n";
			}
							
		}
		else {
			// column headings
			for (i = 0; i < _v; ++i) {
				currSpaces = width - String.valueOf(i).length();
				for (j = 0; j < currSpaces; ++j) {
					result += " ";
				}
				result += i;
			}
			result += "\n";
			// rows			
			for (i = 0; i < _v; ++i) {
				currSpaces = width - String.valueOf(i).length();
				for (j = 0; j < currSpaces; ++j) {
					result += " ";
				}
				result += ":";
				for (j = 0; j < _v; ++j) {
					for (k = 0; k < width; ++k) {
						result += " ";
					}
					result += adj[i][j];
				}
				result += "\n";
			}
						
		}		
		
		return result;
	}
	
	public MatrixGraph clone() {
		return new MatrixGraph(this);
	}
}
