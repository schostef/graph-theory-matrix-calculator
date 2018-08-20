/**
 * @author Stefan Schoeberl
 * @version 1.0
 * @modified 2018-08-09
 * 
 * @Class Edge
 * Edges connect two vertices with each other. One edge is equivalent with a value of 1 inside the adjacency Matrix.
 * This class only supports single non-directional Edges. (One line between two points)
 */

package graph;

public class Edge {
	private String name = "";
	private Vertex[] vertices = new Vertex[2]; //The vertices this Edge is connected to
	private boolean isBridge = false;

	/*
	 * Constructors
	 */
	
	/**
	 * Create a new Edge between two provided Vertices.
	 * The default name of the edge is set to [v1,v2] similar to the order of the vertices array vertices[0]=v1, vertices[1]=v2
	 * This constructor won't set a separate ID for the Edge. Edges are usually identified by their connected vertices.
	 * The degree and adjacencies inside the vertex objects will be handled from here
	 * @param v1 First Vertex
	 * @param v2 Second Vertex
	 */
	public Edge(Vertex v1, Vertex v2) {
		vertices[0] = v1;
		vertices[1] = v2;
		name = "["+v1.getName()+","+v2.getName()+"]";
		vertices[0].addEdge(this);
		vertices[1].addEdge(this);
		vertices[0].addNeighbor(v2);
		vertices[1].addNeighbor(v1);
		
	
		
	}
	
	/*
	 * -> End Constructors
	 */
	
	/*
	 * Getters
	 */
	
	/**
	 * 
	 * @return Edge Name. Default: [Vertex1,Vertex2]
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Vertices connected to the edge
	 * @return Array of size 2 where index [0] = Vertex1 and index[1] = Vertex 2
	 */
	public Vertex[] getVertices() {
		return vertices;
	}
	
	/**
	 * Is the Edge marked as a bridge (Edge that breaks the graph when disconnected)
	 * @return true when yes
	 */
	public boolean isBridge() {
		return isBridge;
	}
	
	/**
	 * Returns the connected Vertex opposite to the provided Vertex.
	 * @param v Vertex to look for inside the edge
	 * @return Vertex opposite of v. Null when v doesn't exist
	 */
	public Vertex getOppositeVertex(Vertex v) {
		if(vertices[0].getName() == v.getName()) {
			return vertices[1];
		}else if(vertices[1].getName() == v.getName()) {
			return vertices[0];
		}
		
		return null;
	}
	
	/**
	 * Check if the edge is a connection between two vertices
	 * @param vertex 
	 * @param vertex2
	 * @return true if vertex is connected to vertex2
	 */
	public boolean connects(Vertex vertex, Vertex vertex2) {
		Vertex v1 = vertices[0];
		Vertex v2 = vertices[1];
		if(vertex.getName() == v1.getName() || vertex.getName() == v2.getName()) {
			if(vertex2.getName() == v1.getName() || vertex2.getName() == v2.getName()) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Check if a vertex belongs to this edge.
	 * @param vertex
	 * @return true if yes
	 */
	public boolean hasVertex(Vertex vertex) {
		if(vertices[0].getName() == vertex.getName() || 
				vertices[1].getName() == vertex.getName()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Is this Edge equal to a given Edge? (Does it connect to the same two vertices?)
	 * @param e
	 * @return true if yes
	 */
	public boolean isEqual(Edge e) {
		return connects(e.getVertices()[0],e.getVertices()[1]);
	}
	
	/*
	 * -> End Getters
	 */
	
	/*
	 * Setters
	 */
	
	/**
	 * Mark this edge as bridge (Edge that breaks the Graph when removed)
	 * @param b true if yes
	 */
	public void setBridge(boolean b) {
		isBridge = b;
	}
	
	/*
	 * -> End Setters
	 */
	

	/*
	 * Output Methods
	 */
	
	public String toString() {
		return name;
	}
	
	/*
	 * -> End Output Methods
	 */

}
