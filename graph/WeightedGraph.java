/**
 * @file
 */
package graph;

/**
 * @author Marshall Farrier
 * @date 11/23/10
 *
 */
public interface WeightedGraph {
	public void setEdgeWeight(int _f, int _t, double _w);
	public void setEdgeWeight(Edge _e, double _w);
	public void setEdgeProperty(int from, int to, int property, int value);
	public void setEdgeProperty(Edge e, int property, int value);
	public double getEdgeWeight(int _f, int _t);
	public double getEdgeWeight(Edge _e);
	public WeightedEdge[] getWeightedEdges();
	public WeightedEdge[] getWeightedEdges(int from);
	public int getEdgeProperty(int from, int to, int property);
	public int getEdgeProperty(Edge e, int property);
	// Number of edge properties
	public int edgeProperties();
	public void setVertexWeight(int _v, double _w);
	public double getVertexWeight(int _v);
	public boolean insert(int _f, int _t, double _w);
	public boolean insert(Edge _e, double _w);
}
