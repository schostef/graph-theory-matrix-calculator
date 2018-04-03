/**
 * @author Stefan Schoeberl
 * @version 0.1
 * @modified 2018-03-23
 * 
 * @Class Vertex
 * Graph related vertex operations
 */

package graph;
import arraytools.*;


public class Vertex {
	
	private int name = 0; 
	//private char cName = 'A';
	private String description = "";
	private int posX = 0,posY = 0;
	private int degree = 0; 
	private boolean isIsolated = true;
	private boolean isArticulation = false;
	private boolean isCenter = false;
	private boolean isVisited = false;
	private Vertex[] neighbors; //Adjacent Vertices
	private Edge[] edges; //Incident Edges

	/*
	 * ************************************************************
	 * Constructors
	 * ************************************************************
	 */
	/**
	 * Names are mandatory.
	 * Only use the Vertex(int) constructor
	 * @see Vertex(int name)
	 */
	public Vertex() {
		
	}
	
	/**
	 * Create a new Vertex
	 * @param name 
	 */
	public Vertex(int name) {
		this.name = name;
		edges = new Edge[0];
		neighbors = new Vertex[0];
	}
	
	/*
	 * ************************************************************
	 */
	
	/*
	 * ************************************************************
	 * Getters
	 * ************************************************************
	 */
	
	public Edge[] getUnvisitedEdges() {
		Edge[] edges = new Edge[0];
		for (int i = 0; i < this.edges.length; i++) {
			if(!this.edges[i].isVisited()) {
				edges = GraphTools.push(edges, this.edges[i]);
			}
		}
		
		return edges;
	}
	
	public boolean isArticulation() {
		return isArticulation;
	}

	public boolean isCenter() {
		return isCenter;
	}
	public int getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getPosX() {
		return posX;
	}
	
	public int getPosY() {
		return posY;
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
	
	public Vertex[] getNeighbors() {
		return neighbors;
	}
	
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
	
	/*
	 * **************************************************************
	 */
	
	/*
	 * **************************************************************
	 * Setters
	 * **************************************************************
	 */	
	
	public void setVisited(boolean b) {
		isVisited = b;
	}
	
	public void setArticulation(boolean b) {
		isArticulation = b;
	}

	public void setDescription(String description) {
		this.description = description;
	}	

	public void setPosX(int posX) {
		this.posX = posX;
	}	

	public void setPosY(int posY) {
		this.posY = posY;
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
	
	public void switchCenter() {
		isCenter = !isCenter;
	}
	
	
	
	/*
	 * **************************************************************
	 */
	
	/*
	 * **************************************************************
	 * Adjacency Operations
	 * **************************************************************
	 */
	
	/**
	 * Add an adjacent Vertex as neighbor.
	 * For proper use, this method should be called through the
	 * Graph Class. No edge will be added calling this method.
	 * @param v Vertex to be added as neighbor
	 */
	public void addNeighbor(Vertex v) {
		//Vertex names (numbers) must be unique. Prevent duplicates.
		// Afterwards increase degree by one and push the neighbor vertex to the
		//neighbor array. Remove isolation flag.
		if (!neighborExists(v)) {
			Vertex[] tempNeighbor = GraphTools.push(neighbors,v);
			degree++;
			checkIsolation();
			neighbors = tempNeighbor;
			
		}
		
	}
	
	public void addEdge(Edge e) {
		Edge[] tempEdges = push(e);
		edges = null;
		edges = tempEdges;
	}
	
	public Edge[] push(Edge e) {
		Edge[] tempEdges = new Edge[edges.length+1];
		for (int i = 0; i < edges.length; i++) {
			tempEdges[i] = edges[i];
		}
		tempEdges[edges.length] = e;
		return tempEdges;
	}
	
	/*
	 * ****************************************************************
	 */
	
	/*
	 * ****************************************************************
	 * Controlling methods
	 * ****************************************************************
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
		return ArrayTools.isDuplicate(neighborNames,v.getName());
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
	 * ****************************************************************
	 */
	
	/*
	 * ****************************************************************
	 * Output Methods
	 * ****************************************************************
	 */
	public String toString() {
		String text = "";
		for (int i = 0; i < edges.length; i++) {
			text += edges[i].getName()+", ";
		}
		return "Vertex: " + name;
	}
	
	/*
	 * *****************************************************************
	 */
}