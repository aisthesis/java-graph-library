/**
 * @file
 */
package driver;
import java.util.LinkedList;
import java.util.ListIterator;

import graph.*;

/**
 * Testing for graph classes
 * @author Marshall Farrier
 * @date 11/22/10
 *
 */
public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		Edge e1 = new Edge('a', 'b');
		Edge e2 = new Edge('a', 'b', true);
		System.out.println("e1 is " + e1);
		System.out.println("e2 is " + e2);
		LinkedListGraph g1 = new LinkedListGraph('Z' - 'W' + 1, true, 'W');
		g1.insert('W', 'X');
		g1.insert('W', 'Y');
		g1.insert('W', 'Z');
		g1.insert('X', 'Y');
		g1.insert('Z', 'X');
		System.out.println(g1);
		g1.remove('W', 'Y');
		System.out.println(g1);
		
		LinkedListGraph g2 = new LinkedListGraph('i' - 'a' + 1, false, 'a', 14);
		g2.setDataFieldLabel(11, 'c');
		g2.setDataFieldLabel(11, 'z');
		g2.setDataFieldLabel(6, 'w');
		g2.showVertexData();
		
		// example from CLRS, p. 596
		LinkedListGraph g3 = new LinkedListGraph('y' - 'r' + 1, false, 'r');
		g3.insert('r', 'v');
		g3.insert('r', 's');
		g3.insert('s', 'w');
		g3.insert('w', 't');
		g3.insert('w', 'x');
		g3.insert('t', 'x');
		g3.insert('t', 'u');
		g3.insert('x', 'u');
		g3.insert('x', 'y');
		g3.insert('y', 'u');
		System.out.println(g3);
		System.out.println("Initial graph has " + g3.edges() + " edges");
		
		LinkedListGraph g3Bfs = g3.breadthFirstSearch('s');
		System.out.println("Breadth first search graph:");
		System.out.println(g3Bfs);
		System.out.println("Breadth first search vertex data:");
		g3Bfs.showVertexData();
		System.out.println("'p' only:");
		g3Bfs.showVertexData('p');
		g3Bfs.showVertexData('p', 'r');
		System.out.println("BFS graph has " + g3Bfs.edges() + " edges");
		
		g3Bfs.printPath('s', 'r');
		System.out.println();
		
		MatrixGraph g4 = new MatrixGraph(g3);
		System.out.println(g4);
		System.out.println("Matrix copy of initial graph has " + g4.edges() + " edges");
		
		
		LinkedListGraph g5 = new LinkedListGraph(g4);
		System.out.println(g5);
		System.out.println("Copying back to linked list we have " + g5.edges() + " edges");
		
		double p = 3.14159;
		int _precision = 3;
		System.out.println(String.format("(%." + _precision + "f)", p));
		
		WeightedLinkedListGraph g6 = new WeightedLinkedListGraph(g3Bfs);
		System.out.println(g6.toString(0));
		System.out.println(g3Bfs);
		System.out.println("Weighted copy of BFS has " + g6.edges() + " edges");
		*/
		
		/*
		System.out.println("Testing depth first search");
		// Graph from CLRS, p. 605
		LinkedListGraph g = new LinkedListGraph('z' - 'u' + 1, true, 'u');
		g.insert('u', 'v');
		g.insert('u', 'x');
		g.insert('v', 'y');
		g.insert('w', 'y');
		g.insert('w', 'z');
		g.insert('z', 'z');
		g.insert('x', 'v');
		g.insert('y', 'x');
		System.out.println(g);
		
		LinkedListGraph dfsTree = g.depthFirstSearch();
		System.out.println("DFS tree:");
		System.out.println(dfsTree);
		*/
		
		/*
		System.out.println("Testing topological sort:");
		// Graph from CLRS, p. 615
		final int SIZE = 'z' - 'm' + 1;
		LinkedListGraph g = new LinkedListGraph(SIZE, true, 'm', 1);
		int i;
		for (i = 0; i < SIZE; ++i) {
			g.setVertexData('m' + i, 0, i);
		}
		g.insert('m', 'q');
		g.insert('m', 'r');
		g.insert('m', 'x');
		g.insert('n', 'o');
		g.insert('n', 'q');
		g.insert('n', 'u');
		g.insert('o', 'r');
		g.insert('o', 's');
		g.insert('o', 'v');
		g.insert('p', 's');
		g.insert('p', 'z');
		g.insert('p', 'o');
		g.insert('q', 't');
		g.insert('r', 'u');
		g.insert('r', 'y');
		g.insert('s', 'r');
		g.insert('u', 't');
		g.insert('v', 'x');
		g.insert('v', 'w');
		g.insert('w', 'z');
		g.insert('y', 'v');
		System.out.println(g);
		LinkedListGraph dfs = g.depthFirstSearch(0, SIZE, true);
		LinkedList<Integer> ts = g.topologicalSort();
		ListIterator<Integer> it = ts.listIterator();

		/* The output here differs from the answer to 22.4-1 because vertices
		 * are explored not necessarily in alphabetical order but in the
		 * order in which they occur in the respective adjacency list.
		 */
		/*
		while (it.hasNext()) {
			i = it.next();
			System.out.println((char) i);
		}
		dfs.showVertexData();
		/*
		int[] arr = {7, 6, 5, 4, 3, 2, 1, 0};
		LinkedList<Integer> adj = new LinkedList<Integer>();
		adj.add(1);
		adj.add(4);
		adj.add(6);
		System.out.println("Original list:\n" + adj);
		AbstractGraph.sortAdjacencyList(adj, arr);
		System.out.println("Sorted list:\n" + adj);
		*/
		/*
		System.out.println("Test strongly connected components");
		// CLRS, p. 616
		final int VERTICES = 'h' - 'a' + 1;
		LinkedListGraph g = new LinkedListGraph(VERTICES, true, 'a');
		g.insert('a', 'b');
		g.insert('b', 'e');
		g.insert('b', 'c');
		g.insert('b', 'f');
		g.insert('c', 'g');
		g.insert('c', 'd');
		g.insert('d', 'c');
		g.insert('d', 'h');
		g.insert('e', 'a');
		g.insert('e', 'f');
		g.insert('f', 'g');
		g.insert('g', 'f');
		g.insert('g', 'h');
		g.insert('h', 'h');
		System.out.println(g);
		LinkedListGraph scc = g.stronglyConnectedComponents();
		scc.showVertexData(Graph.TREE_NUMBER);
		scc.showVertexData();
		*/
		/*
		// CLRS, p. 611
		// Test depthFirstSearch() on WeightedLinkedListGraph
		final int VERTICES = 'z' - 'q' + 1;
		final int DATA_FIELDS = 1;
		final int ORDER_FIELD = 0;
		WeightedLinkedListGraph g = new WeightedLinkedListGraph(VERTICES, true, 'q', DATA_FIELDS);
		g.insert('q', 's');
		g.insert('q', 'w');
		g.insert('q', 't');
		g.insert('r', 'u');
		g.insert('r', 'y');
		g.insert('s', 'v');
		g.insert('t', 'x');
		g.insert('t', 'y');
		g.insert('u', 'y');
		g.insert('v', 'w');
		g.insert('w', 's');
		g.insert('x', 'z');
		g.insert('y', 'q');
		g.insert('z', 'x');
		for (int i = 0; i < VERTICES; ++i) {
			g.setVertexData('q' + i, ORDER_FIELD, i);
		}
		System.out.println(g);
		
		LinkedListGraph dfs = g.depthFirstSearch(ORDER_FIELD, 2 * VERTICES, true);
		System.out.println(dfs);
		System.out.println("Tree edges in original graph:");
		LinkedList<Edge> treeEdges = g.getEdgesByProperty(Graph.EDGE_TYPE, Graph.TREE_EDGE);
		ListIterator<Edge> it = treeEdges.listIterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
		System.out.println("Back edges:");
		LinkedList<Edge> backEdges = g.getEdgesByProperty(Graph.EDGE_TYPE, Graph.BACK_EDGE);
		it = backEdges.listIterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
		System.out.println("Forward edges:");
		LinkedList<Edge> fwdEdges = g.getEdgesByProperty(Graph.EDGE_TYPE, Graph.FORWARD_EDGE);
		it = fwdEdges.listIterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
		System.out.println("Cross edges:");
		LinkedList<Edge> crossEdges = g.getEdgesByProperty(Graph.EDGE_TYPE, Graph.CROSS_EDGE);
		it = crossEdges.listIterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		
		}
		*/
		
		// Test Kruskal, CLRS, p. 632
		final int VERTICES = 'i' - 'a' + 1;
		final boolean DIRECTED = false;
		WeightedLinkedListGraph g = new WeightedLinkedListGraph(VERTICES, DIRECTED, 'a');
		
		g.insert('a', 'b', 4.0);
		g.insert('a', 'h', 8.0);
		g.insert('b', 'c', 8.0);
		g.insert('b', 'h', 11.0);
		g.insert('c', 'd', 7.0);
		g.insert('c', 'f', 4.0);
		g.insert('c', 'i', 2.0);
		g.insert('d', 'e', 9.0);
		g.insert('d', 'f', 14.0);
		g.insert('e', 'f', 10.0);
		if (g.insert('a', 'b', 1.0)) System.out.println("(a, b) was allowed re-insertion");
		else System.out.println("Reinsertion prevented");
		g.insert('f', 'g', 2.0);
		g.insert('g', 'h', 1.0);
		g.insert('g', 'i', 6.0);
		g.insert('h', 'i', 7.0);
		System.out.println(g);
		WeightedLinkedListGraph mst = g.minSpanningTreeKruskal();
		System.out.println("Minimum spanning tree:");
		System.out.println(mst);
		System.out.println("Vertices in original graph: " + g.vertices());
		System.out.println("Edges in minimum spanning tree: " + mst.edges());
		WeightedEdge[] edges = mst.getWeightedEdges();
		for (int i = 0; i < edges.length; ++i) System.out.println(edges[i]);
		
		System.out.println("Now using Prim on the same graph");
		mst = g.minSpanningTreePrim('a');
		System.out.println("Minimum spanning tree:");
		System.out.println(mst);
		System.out.println("Vertices in original graph: " + g.vertices());
		System.out.println("Edges in minimum spanning tree: " + mst.edges());
		edges = mst.getWeightedEdges();
		for (int i = 0; i < edges.length; ++i) System.out.println(edges[i]);
	}

}
