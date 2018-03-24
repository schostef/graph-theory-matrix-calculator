package graph;

public class Edge {
	private String name = "", description = "";
	private int weight = 0;
	private Vertex[] vertices = new Vertex[2];
	private boolean isDirectional = false; //true = vertex[0] -> vertex[1]
	private boolean isBridge = false;

	public Edge(Vertex v1, Vertex v2) {
		v1.addNeighbor(v2);
		v2.addNeighbor(v1);
		vertices[0] = v1;
		vertices[1] = v2;
	}
	
	public Edge(Vertex v1, Vertex v2, boolean directional) {
		vertices[0] = v1;
		vertices[1] = v2;
		isDirectional = directional;
	}
	
	public String toString() {
		return "Kante "+name+", verbunden mit Knoten: "+ vertices[0].getName()+", "+vertices[1].getName();
	}

}
