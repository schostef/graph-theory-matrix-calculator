package graph;
import arraytools.*;
public class Vertex {
	
	private String name = "V",description = "";
	private int posX = 0,posY = 0;
	private int degree = 0;
	private boolean isIsolated = true;
	private boolean isArticulation = false;
	private Vertex[] neighbors;
	private Edge[] edges;

	public Vertex() {
		
	}
	
	public Vertex(String name) {
		this.name = name;
		edges = new Edge[0];
		neighbors = new Vertex[0];
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public boolean isIsolated() {
		return isIsolated;
	}

	public void setIsolated(boolean isIsolated) {
		this.isIsolated = isIsolated;
	}

	public Vertex[] getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(Vertex[] neighbors) {
		this.neighbors = neighbors;
	}
	
	public void addNeighbor(Vertex v) {
		if (!neighborExists(v)) {
			Vertex[] tempNeighbor = new Vertex[neighbors.length+1];
			tempNeighbor = neighbors;
			tempNeighbor[neighbors.length+1]=v;
			checkIsolation();
			degree++;
			v.addNeighbor(this);
			neighbors = tempNeighbor;
		}
		
	}
	
	public boolean neighborExists(Vertex v) {
		for (int i = 0; i < neighbors.length ;i++ ){
			if (neighbors[i].getName().equals( v.getName() )) {
				return true;
			}
		}
		return false;
	}
	
	public void checkIsolation(){
		if(neighbors.length > 0) {
			isIsolated = false;
		}else {
			isIsolated = true;
		}
	}
	
	public String toString() {
		String text = "";
		for (int i = 0; i < neighbors.length; i++) {
			text += neighbors[i].getName()+", ";
		}
		return "Vertex: " + name + ", Grad: "+ degree + ", Verbunden mit: "+text;
	}
	
	/*public void drawEdge(Vertex v) {
		Vertex[] tempNeighbors = new Vertex[degree+1];
		Edge[] tempEdges = new Edge[degree + 1];
		
		tempNeighbors = neighbors;
		tempNeighbors[degree + 1] = v;
		tempEdges = edges;
		
		for (int i = 0; i < degree; i++) {
			
		}
	}*/

}
