package graph;
import matrix.*;

public class Graph {
	private boolean vertexNamesAreInt = true; //Vertex names should either be Letters or Numbers, not both
	private Vertex[] vertices;
	private Edge[] edges;
	private char name = 'G';
	private int vertexSum = 0;
	private int edgeSum = 0;

	public Graph() {
		vertices = new Vertex[0];
		edges = new Edge[0];
	}
	
	public void addVertex(int name) {
		Vertex[] tV = new Vertex[vertexSum + 1];
		tV = vertices;
		tV[vertexSum + 1] = new Vertex(name);
		vertices = tV;
		//sortVertices();
		vertexSum++;
	}
	
	public void addEdge(Vertex v1, Vertex v2) {
		Edge[] tedge = new Edge[edgeSum + 1];
		tedge = edges;
		v1.addNeighbor(v2);
		tedge[vertexSum + 1] = new Edge(v1,v2);
		
		edges = tedge;
		edgeSum++;
	}

	private void sortVertices() {
		
		
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
