/**
 * @author Stefan Schoeberl
 * @version 1.0
 * @modified 2018-08-09
 * 
 * @Class Subgraph
 * Built from Vertex and Edge Parts of a Graph Type Object.
 * Calculation Functions are reduced to only return a component Amount
 * Optionally full calculations except for Euler Paths and Excentricities are possible
 */

package graph;

import arraytools.GraphTools;
import matrix.Vector;

public class Subgraph extends Graph {
	/*
	 * Constructors
	 */
	
	/**
	 * Create a new Subgraph out of passed Vertices.
	 * Incident connected Edges will be extracted. Unconnected Edges will be ignored
	 * @param vertices Vertices to build the Subgraph
	 */
	public Subgraph(Vertex[] vertices) {
		super();
		this.vertices = vertices;
		vertexSum = vertices.length;
		this.edges = new Edge[0];
		getIncidentEdges();
	}
	
	/**
	 * Create a new Subgraph out of passed edges and vertices
	 * @param vertices
	 * @param edges
	 */
	public Subgraph(Vertex[] vertices, Edge[] edges) {
		super();
		this.vertices = vertices;
		this.edges = edges;
		vertexSum = vertices.length;
		edgeSum = edges.length;
		//adjacencyMatrizes = new Matrix[vertexSum -1];
		createAdjacencyMatrix();
	}
	
	/**
	 * Create a new Subgraph by passing a series of Edges.
	 * Unconnected Vertices will be ignored
	 * @param edges
	 */
	public Subgraph(Edge[] edges) {
		super();
		this.edges = edges;
		Vertex[] vertices = new Vertex[0];
		for(Edge e: edges) {
			Vertex v1 = e.getVertices()[0];
			Vertex v2 = e.getVertices()[1];
			if(!GraphTools.containsVertex(vertices, v1)) {
				vertices = GraphTools.push(vertices, v1);
			}
			if(!GraphTools.containsVertex(vertices, v2)) {
				vertices = GraphTools.push(vertices, v2);
			}
		}
		this.vertices = vertices;
		vertexSum = vertices.length;
		edgeSum = edges.length;
		createAdjacencyMatrix();
	}
	
	/*
	 * -> end Constructors
	 */
	
	/*
	 * Getters
	 */
	/**
	 * Get incident connected Edges of all stored vertices.
	 * Only edges connected to an existing vertex inside the subgraph will be used
	 */
	public void getIncidentEdges() {
		for(Vertex v: vertices) {
			Edge[] eA = v.getAllEdges();
			for(Edge e:eA ) {
				if(!contains(e)) {
					Vertex x = e.getOppositeVertex(v);
					if(contains(x)) {
						edges = GraphTools.push(edges, e);
					}					
				}
			}
		}
		edgeSum = edges.length;
	}
	
	/**
	 * Check if a Vertex is stored in the Subgraph
	 * @param v 
	 * @return true if Vertex found
	 */
	public boolean contains(Vertex v) {
		for(Vertex x: vertices) {
			if(v.getName() == x.getName()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check if an Edge is stored in the Subgraph
	 * @param e
	 * @return true if Edge found
	 */
	public boolean contains(Edge e) {
		for(Edge f: edges) {
			if(f.isEqual(e)) {
				return true;
			}
		}
		return false;
	}
	/*
	 * -> end Getters
	 */
	
	/* 
	 * Overloads from Parent Class Graph
	 */
	/**
	 * Calculates the Subgraph's components
	 * @param storeComponents true to store the components for later use, false only stores the component amount
	 */
	protected void calculateComponents(boolean storeComponents) {
		createAdjacencyMatrix();
		initializeDistanceMatrix();
		initializePathMatrix();
		calculateDistancePathMatrix();
		if(!storeComponents) {
			if(!isCohesive) {			
				Vector[] pathMatch = pathMatrix.fetchUniqueRows();
				//pathMatch = GraphTools.removeDuplicates(pathMatch);
				int[][] componentIndizes = new int[pathMatch.length][0];
				for(int i = 0; i < pathMatch.length; i++) {
					componentIndizes[i] = pathMatch[i].getPositionOfValue(1);
				}
				
				this.componentAmount = pathMatch.length;
			}
		}else {
			if(isCohesive) {
				components = new Subgraph[1]; 
				components[0] = new Subgraph(vertices,edges);
				componentAmount = 1;
			}else {			
				Vector[] pathMatch = pathMatrix.fetchUniqueRows();
				//pathMatch = GraphTools.removeDuplicates(pathMatch);
				int[][] componentIndizes = new int[pathMatch.length][0];
				for(int i = 0; i < pathMatch.length; i++) {
					componentIndizes[i] = pathMatch[i].getPositionOfValue(1);
				}
				
				this.componentAmount = pathMatch.length;
				this.components = new Subgraph[componentAmount];
				for(int i = 0; i < componentAmount; i++) {
					Vertex[] v = new Vertex[componentIndizes[i].length];
					for(int j = 0; j < componentIndizes[i].length; j++) {
						v[j] = vertices[componentIndizes[i][j]];
					}
					this.components[i] = new Subgraph(v);
				}
			}
		}		
	}
	
	/**
	 * Default call override
	 * Only calculates the component amount
	 */
	public void calculateAll() {
		calculateComponents(false);
		
	}
	/*
	 * -> end Overloads from Parent Class Graph
	 */
	
	/*
	 * Output
	 */
	public String toString() {
		String text = "";
		for (int i = 0; i < vertices.length; i++) {
			text += vertices[i]+"\n";
		}
		for (int j = 0; j < edges.length; j++) {
			text += edges[j]+"\n";
		}
		
		return text;
	}
	/*
	 * -> end Output
	 */
}
