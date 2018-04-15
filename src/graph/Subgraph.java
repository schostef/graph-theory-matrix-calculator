package graph;

import matrix.Matrix;

public class Subgraph extends Graph {
	private boolean isSaturated;
	private boolean isSpanning;
	private Graph parentGraph;

	public Subgraph() {
		reinitialize();
	}
	
	public Subgraph(Graph parentGraph, Vertex[] vertices, Edge[] edges, Matrix adjacencyMatrix) {
		this.parentGraph = parentGraph;
		this.vertices = vertices;
		this.edges = edges;
		this.adjacencyMatrizes = new Matrix[vertices.length];
		this.adjacencyMatrizes[0] = adjacencyMatrix;
		this.adjacencyMatrizes[0].vectorize();
		this.vertexSum = adjacencyMatrizes[0].size;
		this.edgeSum = this.adjacencyMatrizes[0].getSymmetricSum();
		
	}
	
	public Subgraph(Graph parentGraph, Matrix adjacencyMatrix) {
		
	}

	public Subgraph(Matrix m) {
		
	}
	
	public int getComponentAmount() {
		return this.componentAmount;
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

	

}
