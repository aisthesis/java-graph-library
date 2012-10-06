/**
 * 
 */
package graph;

/**
 * Simple edge class
 * Implementation roughly following Sedgewick
 * Edges are immutable.
 * @author Marshall Farrier
 * @date 11/22/10
 *
 */
public class Edge {
	private int from;
	private int to;
	private boolean charRep;
	
	public Edge(int _from, int _to) {
		if (_from < 0 || _to < 0) {
			throw new IllegalArgumentException("Vertex indices cannot be negative");
		}
		from = _from;
		to = _to;
		charRep = false;
	}
	
	public Edge(int _from, int _to, boolean _charRep) {
		if (_from < 0 || _to < 0) {
			throw new IllegalArgumentException("Vertex indices cannot be negative");
		}
		if (_charRep) {
			if (_from < 'A' || 'z' < _from) {
				throw new IllegalArgumentException("Invalid character");
			}
			else if ('Z' < _from && _from < 'a') {
				throw new IllegalArgumentException("Invalid character");
			}
			else if (_to < 'A' || 'z' < _to) {
				throw new IllegalArgumentException("Invalid character");
			}
			else if ('Z' < _to && _to < 'a') {
				throw new IllegalArgumentException("Invalid character");
			}
		}
		from = _from;
		to = _to;
		charRep = _charRep;
	}

	public int from() { return from; }
	public int to() { return to; }
	public boolean equals(Edge e) {
		if (from == e.from && to == e.to && charRep == e.charRep) return true;
		return false;
	}
	public boolean charRep() { return charRep; }
	public String toString() {
		if (charRep) {
			return "(" + (char)from + ", " + (char)to + ")";
		}
		return "(" + from + ", " + to + ")";
	}
}
