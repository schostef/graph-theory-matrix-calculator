/**
 * @author Stefan Schoeberl
 * @version 1.0
 * @modified 2018-08-09
 * 
 * @Class Vertex
 * Single Point inside a Graph.
 * Stores information about it's degree, connected edges, center and isolation status
 */

package graph;
import arraytools.*;


public class Vertex {
	
	private int name = 0; 
	private int degree = 0; 
	private boolean isIsolated = true;
	private boolean isArticulation = false;
	private boolean isCenter = false;
	private Vertex[] neighbors; //Adjacent Vertices
	private Edge[] edges; //Incident Edges

	/*
	 * Constructors
	 */
	
	/**
	 * Create a new Vertex. Names should be numeric and unique
	 * @param name unique numeric name
	 */
	public Vertex(int name) {
		this.name = name;
		edges = new Edge[0];
		neighbors = new Vertex[0];
	}
	
	/*
	 * -> End Constructors
	 */
	
	/*
	 * Getters
	 */
	
	/**
	 * Returns if the vertex is an articulation. (Breaking the graph when removed)
	 * @return Articulation
	 */
	public boolean isArticulation() {
		return isArticulation;
	}

	/**
	 * Returns if the vertex is part of the center.
	 * Eccentricities need to be calculated beforehand
	 * @return
	 */
	public boolean isCenter() {
		return isCenter;
	}
	
	public int getName() {
		return name;
	}
	
	public int getDegree() {
		return degree;
	}

	public void setName(int name) {
		this.name = name;
	}
	
	public boolean isIsolated() {
		return isIsolated;
	}
	
	/**
	 * Get the adjacent vertices of this vertex
	 * @return adjacent vertices
	 */
	public Vertex[] getNeighbors() {
		return neighbors;
	}
	
	/**
	 * Get the edge that connects this vertex to the passed vertex
	 * @param toVertex adjacent vertex
	 * @return connecting edge between this vertex and toVertex
	 */
	public Edge getEdge(Vertex toVertex) {
		for (int i = 0; i < edges.length; i++) {
			if(toVertex.getName() == edges[i].getVertices()[0].getName() ||
					toVertex.getName() == edges[i].getVertices()[1].getName()) {
				return edges[i];
			}
		}
		return null;
	}
	
	public Edge getEdge(int index) {
		return edges[index];
	}
	
	/**
	 * Get all incident edges
	 * @return incident edges
	 */
	public Edge[] getAllEdges() {
		return edges;
	}
	
	/*
	 * -> End Getters
	 */
	
	/*
	 * Setters
	 */	
	
	public void setArticulation(boolean b) {
		isArticulation = b;
	}	

	public void setDegree(int degree) {
		this.degree = degree;
	}	

	public void setIsolated(boolean isIsolated) {
		this.isIsolated = isIsolated;
	}	

	public void setNeighbors(Vertex[] neighbors) {
		this.neighbors = neighbors;
	}
	
	public void setCenter(boolean b) {
		isCenter = b;
	}
	
	
	
	/*
	 * -> End Setters
	 */
	
	/*
	 * Adjacency Operations
	 */
	
	/**
	 * Add an adjacent Vertex as neighbor.
	 * An according edge needs to be added separately.
	 * This function is best to be handled in the Graph class
	 * @param v Vertex to be added as neighbor
	 */
	public void addNeighbor(Vertex v) {
		if (!neighborExists(v)) {
			Vertex[] tempNeighbor = GraphTools.push(neighbors,v);
			checkIsolation();
			neighbors = tempNeighbor;
			
		}
		
	}
	
	/**
	 * Remove an adjacent Vertex from the internal neighbor list
	 * This function will not delete edges
	 * Function should be handled through the Graph class
	 * @param v Neighbor to delete
	 */
	public void deleteNeighbor(Vertex v) {
		for(Vertex vn: neighbors) {
			if(vn.getName() == v.getName()) {
				neighbors = GraphTools.delete(neighbors, v);
			}
		}
	}
	
	/**
	 * Add an edge to the internal list and increase the degree by one.
	 * @param e Edge to add
	 */
	public void addEdge(Edge e) {
		edges = GraphTools.push(edges, e);
		degree = edges.length;
	}
	
	/**
	 * Delete an edge from this vertex. Remove the neighbor and lower the degree
	 * @param e
	 */
	public void deleteEdge(Edge e) {
		boolean found = false;
		for(int i = 0; i < edges.length && !found; i++) {
			if(e.isEqual(edges[i])) {
				deleteNeighbor(e.getOppositeVertex(this));
				edges = GraphTools.delete(edges, e);
				e.getOppositeVertex(this).deleteEdge(e);	
				degree = edges.length;
			}
		}
	}
	
	/*
	 * -> End Adjacency Operations
	 */
	
	/*
	 * Controlling methods
	 */
	
	/**
	 * Check if provided Vertex is already registered as neighbor of this Vertex
	 * @param v Vertex to check for
	 * @return true if Vertex is already a neighbor
	 */
	public boolean neighborExists(Vertex v) {
		if(neighbors.length == 0) {
			return false;
		}
		int[] neighborNames = new int[neighbors.length];
		for(int i = 0; i < neighbors.length; i++) {
			neighborNames[i] = neighbors[i].getName();
		}
		return ArrayTools.contains(neighborNames,v.getName());
	}
	
	/**
	 * If the degree of this vertex is greater 0 it is not flagged as isolated anymore
	 */
	public void checkIsolation(){
		if(degree > 0) {
			isIsolated = false;
		}else {
			isIsolated = true;
		}
	}
	
	/*
	 * -> End Controlling methods
	 */
	
	/*
	 * Output Methods
	 */
	public String toString() {
		return ""+name;
	}
	
	/*
	 * -> Output Methods
	 */
}