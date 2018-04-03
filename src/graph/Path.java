package graph;
import arraytools.GraphTools;
public class Path {

	private Vertex[] vertices;
	private Edge[] edges;
	public boolean isCircle = false;
	
	public Path(Vertex[] vertices) {
		if(vertices != null && vertices.length != 0) {
			buildPath(vertices);
		}
			
	}
	
	//An isolated Vertex is a Path of 0
	public Path(Vertex vertex) {
		this.vertices = new Vertex[1];
		this.edges = new Edge[0];
		vertices[0] = vertex;
	}
	
	
	public void buildPath(Vertex[] vertices) {
		this.vertices = null;
		this.edges = null;
		//start at 1st vertex
		this.vertices = new Vertex[vertices.length];
		this.edges = new Edge[0];
		int nextNeighbor = 1;
		for(int i = 1; i < vertices.length && !isCircle; i++) {
			//ask who in the array is it's neighbor
			while(!vertices[i-1].neighborExists(vertices[nextNeighbor]) && nextNeighbor < vertices.length) {
				nextNeighbor++;
			}
			//if the last edge connects to the first vertex, it's a circle
			if(vertices[0].getName() == vertices[nextNeighbor].getName()) {
				isCircle = true;
			}
			if(i == 1) {
				this.vertices[i-1] = vertices[i-1];
			}
			this.vertices[i] = vertices[nextNeighbor];
			//get the edge that connects the neighbor
			this.edges = GraphTools.push(this.edges, this.vertices[i-1].getEdge(this.vertices[i]));
			nextNeighbor = i++;
		}
	}
	
	public void addVertex(Vertex v) {
		Vertex[] temp = new Vertex[vertices.length+1];
		temp = getVertices();
		temp[vertices.length] = v;
		buildPath(temp);
	}
	
	public Vertex getNextVertex(Vertex vertex) {
		for(int i = 0; i < vertices.length; i++) {
			if(vertex.getName() == vertices[i].getName()) {
				return vertices[i++];
			}
		}
		return null;
	}
	
	public int getVertexCount() {
		return vertices.length;
	}
	
	public Vertex[] getVertices() {
		return vertices;
	}
	
	public Edge getEdgeOf(Vertex vertex) {
		for(int i = 0; i < edges.length; i++) {
			if(edges[i].hasVertex(vertex)) {
				return edges[i];
			}
		}
		
		return null;
	}
	
	public Vertex getLast() {
		return vertices[vertices.length-1];
	}

	public Edge[] getUnvisitedEdges() {
		Edge[] edges = new Edge[0];
		for (int i = 0; i < this.edges.length; i++) {
			if(!this.edges[i].isVisited()) {
				edges = GraphTools.push(edges, this.edges[i]);
			}
		}
		return edges;
	}
	
	public boolean hasEdge(Edge edge) {
		for (int i = 0; i < edges.length; i++) {
			if(edges[i].getID() == edge.getID()) {
				return true;
			}
		}
		return false;
	}

}
