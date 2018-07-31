package graph;

import arraytools.GraphTools;
import matrix.Matrix;
import matrix.Vector;

public class Subgraph extends Graph {

	public Subgraph() {
		super();
	}
	
	public Subgraph(Vertex[] vertices) {
		super();
		this.vertices = vertices;
		vertexSum = vertices.length;
		this.edges = new Edge[0];
		getIncidentEdges();
	}
	
	public Subgraph(Vertex[] vertices, Edge[] edges) {
		super();
		this.vertices = vertices;
		this.edges = edges;
		vertexSum = vertices.length;
		edgeSum = edges.length;
		adjacencyMatrizes = new Matrix[vertexSum -1];
		createAdjacencyMatrix();
	}
	
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
	
	public boolean contains(Vertex v) {
		for(Vertex x: vertices) {
			if(v.getName() == x.getName()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean contains(Edge e) {
		for(Edge f: edges) {
			if(f.isEqual(e)) {
				return true;
			}
		}
		return false;
	}
	
	protected void calculateComponents() {
		createAdjacencyMatrix();
		initializeDistanceMatrix();
		initializePathMatrix();
		calculateDistancePathMatrix();
		if(!isCohesive) {			
			Vector[] pathMatch = pathMatrix.fetchEqualRows();
			pathMatch = GraphTools.removeDuplicates(pathMatch);
			int[][] componentIndizes = new int[pathMatch.length][0];
			for(int i = 0; i < pathMatch.length; i++) {
				componentIndizes[i] = pathMatch[i].getIndizesof(1);
			}
			
			this.componentAmount = pathMatch.length;
		}		
	}
	
	
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
	
	public void calculateAll() {
		calculateComponents();
		
	}

	

}
