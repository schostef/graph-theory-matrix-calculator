/**
 * @author Stefan Schoeberl
 * @version 0.1
 * @modified 2018-03-23
 * 
 * @Class Edge
 * Graph related Edge Operations
 */

package graph;

public class Edge {
	private int id = 0;
	private String name = "", description = "";
	private int weight = 0;
	private Vertex[] vertices = new Vertex[2]; //The vertices this Edge is connected to
	private boolean isDirectional = false; //for a directional edge, mind the order: true = vertex[0] -> vertex[1]
	private boolean isBridge = false;
	private boolean isVisited = false;

	/*
	 * *****************************************************************************
	 * Constructors
	 * *****************************************************************************
	 */
	
	/**
	 * Non-directional
	 * @param v1 Source Vertex
	 * @param v2 Destination Vertex
	 */
	public Edge(Vertex v1, Vertex v2) {
		v1.addNeighbor(v2);
		v2.addNeighbor(v1);
		vertices[0] = v1;
		vertices[1] = v2;
	}
	
	public Edge(int id, Vertex v1, Vertex v2, String name) {
		this.id = id;
		v1.addNeighbor(v2);
		v2.addNeighbor(v1);
		vertices[0] = v1;
		vertices[1] = v2;
		this.name = name;
	}
	
	/**
	 * Directional
	 * @param v1 Source Vertex
	 * @param v2 Destination Vertex
	 * @param directional provide true if v1 goes to v2
	 */
	public Edge(Vertex v1, Vertex v2, boolean directional) {
		vertices[0] = v1;
		vertices[1] = v2;
		isDirectional = directional;
	}
	
	/*
	 * *****************************************************************************
	 */
	
	/*
	 * *****************************************************************************
	 * Getters
	 * *****************************************************************************
	 */
	
	public String getName() {
		return name;
	}
	
	public Vertex[] getVertices() {
		return vertices;
	}
	
	public boolean isBridge() {
		return isBridge;
	}
	
	public boolean isVisited() {
		return isVisited;
	}
	
	public Vertex getOppositeVertex(Vertex v) {
		if(vertices[0].getName() == v.getName()) {
			return vertices[1];
		}else if(vertices[1].getName() == v.getName()) {
			return vertices[0];
		}
		
		return null;
	}
	
	public boolean hasVertex(Vertex vertex) {
		if(vertices[0].getName() == vertex.getName() || 
				vertices[1].getName() == vertex.getName()) {
			return true;
		}
		return false;
	}
	
	public boolean hasVertex(Vertex vertex1, Vertex vertex2) {
		if(vertices[0].getName() == vertex1.getName()) {
			if(vertices[1].getName() == vertex2.getName()) {
				return true;
			}
		}
		if(vertices[0].getName() == vertex2.getName()) {
			if(vertices[1].getName() == vertex1.getName()) {
				return true;
			}
		}
		return false;
	}
	
	public int getID() {
		return id;
	}
	
	/*
	 * *****************************************************************************
	 */
	
	/*
	 * *****************************************************************************
	 * Setters
	 * *****************************************************************************
	 */
	
	public void setVisited(boolean b) {
		isVisited = b;
	}
	
	public void setBridge(boolean b) {
		isBridge = b;
	}
	
	/*
	 * *****************************************************************************
	 */
	

	/*
	 * *****************************************************************************
	 * Output Methods
	 * *****************************************************************************
	 */
	
	public String toString() {
		return "Kante "+name;
	}
	
	/*
	 * ****************************************************************************
	 */

}
