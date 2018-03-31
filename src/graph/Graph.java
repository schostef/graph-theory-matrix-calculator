/**
 * @author Stefan Schoeberl
 * @version 0.1
 * @modified 2018-03-23
 * 
 * @Class Graph
 * Main class of the Graph.
 */

package graph;
import arraytools.ArrayTools;
import arraytools.GraphTools;
import matrix.*;

public class Graph {
	private boolean vertexNamesAreInt = true; //Vertex names should either be Letters or Numbers, not both
	//Store vertices and edges here
	protected Vertex[] vertices;
	protected Edge[] edges;
	protected Matrix[] adjacencyMatrizes; //Adjacency Matrix at Index 0, exponentialmatrizes onwards, where Index + 1 = exponent
	private Matrix pathMatrix;
	private Matrix distanceMatrix;
	private Matrix controlMatrix;
	private Vector eccentricity;
	private int radius,diameter;
	private boolean isCohesive = true;
	private char name = 'G';
	protected int vertexSum = 0; //total number of Vertices
	protected int edgeSum = 0; //Total number of edges
	private Subgraph[] components;
	private int componentAmount = 1;

	/*
	 * ************************************************************************
	 * Constructors
	 * ************************************************************************
	 */
	/**
	 * Empty Graph
	 */
	public Graph() {
		reinitialize();
	}
	
	/**
	 * Create a new Graph by providing an adjacency Matrix
	 * @param m Adjacency Matrix
	 */
	public Graph(Matrix m) {
		reinitialize();
		drawFromMatrix(m);
		adjacencyMatrizes = new Matrix[vertexSum -1];
		adjacencyMatrizes[0] = m;
		
	}
	
	/*
	 * ************************************************************************
	 */
	
	
	/**
	 * Full reset. Clear all Vertices and Edges.
	 * Delete all Matrizes.
	 */
	public void reinitialize() {
		vertices = null;
		edges = null;
		adjacencyMatrizes = null;
		controlMatrix = null;
		vertexSum = 0;
		edgeSum = 0;
		vertices = new Vertex[0];
		edges = new Edge[0];
		adjacencyMatrizes = new Matrix[0];
	}
	
	/*
	 * ************************************************************************
	 * Getters
	 * ************************************************************************
	 */
	
	/**
	 * Get the Vertex Object by providing it's unique name
	 * @param name Identifying Vertex name
	 * @return Vertex Object
	 */
	public Vertex getVertex(int name) {
		if(vertices != null && vertices.length != 0) {
			for (int i = 0; i < vertices.length; i++) {
				if (vertices[i].getName() == name) {
					return vertices[i];
				}
			}
		}
		return null;
	}
	
	/**
	 * Return the exponential Matrix of exponent
	 * @param exponent starting at 1 to maximum of vertex count - 1
	 * @return Exponentialmatrix
	 */
	public Matrix getAdjacencyMatrix(int exponent) {
		return adjacencyMatrizes[exponent - 1];
	}
	
	public Matrix getPathMatrix() {
		return pathMatrix;
	}
	
	public Matrix getDistanceMatrix() {
		return distanceMatrix;
	}
	
	public Matrix getControlMatrix() {
		return controlMatrix;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public int getDiameter() {
		return diameter;
	}
	
	public int getCenter() {
		
	}
	
	
	
	/*
	 * ************************************************************************
	 * Dimension Manipulating/Drawing/Constructing Methods
	 * ************************************************************************
	 */
	
	/**
	 * Draws Vertices and Edges with a provided Adjacency Matrix.
	 * Reinitializes the Graph.
	 * Adds the Adjacency Matrix to the adjacencyMatrizes[0] Index
	 * @param m Adjacency Matrix
	 */
	public void drawFromMatrix(Matrix m) {
		//Clear everything and draw from start
		reinitialize();
		int vertexNumber= 0;
		int stepper = 0;
		
		// First start adding default vertices provided through the Matrix's dimension
		for (int i = 0; i < m.size; i++) {
			addVertex();
		}
		
		//If the Matrix is symmetric (The graph is bidirectional) only half the matrix needs to be processed
		if(m.isSymmetric()) {
			while(vertexNumber < m.size) {
				stepper = vertexNumber;
				while(stepper < m.size) {
					if(m.getValueAt(vertexNumber, stepper) == 1) {
						addEdge(vertices[vertexNumber], vertices[stepper]);
					}
					stepper++;
				}
				vertexNumber++;
			}
		}
	}
	
	/**
	 * Adds a new vertex
	 * @param name
	 */
	public void addVertex(int name) {
		Vertex[] tV = GraphTools.expand(vertices);
		tV[vertexSum + 1] = new Vertex(name);
		vertices = tV;
		//sortVertices();
		vertexSum++;
	}
	
	/**
	 * Adds a new vertex
	 */
	public void addVertex() {
		Vertex[] tV = GraphTools.expand(vertices);
		tV[vertexSum] = new Vertex(vertexSum+1);
		vertices = tV;
		//sortVertices();
		vertexSum++;
	}
	
	/**
	 * Add a new Edge
	 */
	public void addEdge(Vertex v1, Vertex v2) {
		Edge[] tedge = GraphTools.expand(edges);
		tedge[edges.length] = new Edge(edgeSum,v1,v2,"["+v1.getName()+","+v2.getName()+"]");
		v1.addEdge(tedge[edges.length]);
		v2.addEdge(tedge[edges.length]);
		edges = tedge;
		edgeSum++;
	}
	/*
	 * *************************************************************************
	 */
	
	/*
	 * *************************************************************************
	 * Matrix methods
	 * *************************************************************************
	 */
	/**
	 * Calculate all Exponentialmatrizes to the maximum Number of vertex count - 1
	 */
	public void calculateExponentialMatrizes() {
		for(int i = 1; i < vertexSum; i++) {
			adjacencyMatrizes[i-1] = adjacencyMatrizes[0].exponentiate(i);
		}
	}
	
	/**
	 * Construct the Adjacency Matrix from the drawn Graph and add it to the
	 * adjacencyMatrizes[0] index
	 */
	public void createAdjacencyMatrix() {
		refreshAdjacencyMatrizes();
		adjacencyMatrizes[0] = new Matrix(vertexSum);
		for (int i = 0; i < vertexSum; i++) {
			for (int j = 0; j < vertexSum; j++) {
				if(vertices[i].neighborExists(vertices[j])) {
					adjacencyMatrizes[0].setValueAt(i, j, 1);
				}else {
					adjacencyMatrizes[0].setValueAt(i, j, 0);
				}
			}
		}
		adjacencyMatrizes[0].vectorize();
	}
	
	/**
	 * clear the Adjacency Matrix and exponential Matrizes
	 */
	public void refreshAdjacencyMatrizes() {
		Matrix[] tempAMatrizes = new Matrix[vertexSum-1];
		adjacencyMatrizes = tempAMatrizes;
	}
	
	/**
	 * Create the Pathmatrix out of the Adjacency Matrix
	 * Copy Adjacency Matrix and set Vertex's Path to itself to 1
	 */
	public void initializePathMatrix() {
		if(adjacencyMatrizes[0] != null) {
			pathMatrix = new Matrix(adjacencyMatrizes[0].getMatrix());
			for(int i = 0; i < vertexSum; i++) {
				pathMatrix.setValueAt(i, i, 1);
			}
			pathMatrix.vectorize();
			initializeControlMatrix();
		}
	}
	
	public void initializeDistanceMatrix() {
		if(adjacencyMatrizes[0] != null) {
			distanceMatrix = new Matrix(adjacencyMatrizes[0].getMatrix());
			distanceMatrix.vectorize();
		}
		
		
	}
	
	/**
	 * Create a binaryMatrix flagging all 1s with true and all 0s with false
	 */
	public void initializeControlMatrix(){
		clearControlMatrix();
		controlMatrix = Matrix.searchAndReport(pathMatrix,1);
	}
	
	/**
	 * Reset the Controlmatrix flagging all values to false
	 */
	public void clearControlMatrix() {
		controlMatrix = null;
		controlMatrix = new Matrix(vertexSum,false);
	}
	
	public void calculateDistancePathMatrix() {
		Matrix lastControlMatrix = new Matrix(controlMatrix.getBinMatrix());
		int pathlength = 2;
		do {
			lastControlMatrix = null;
			lastControlMatrix = new Matrix(controlMatrix.getBinMatrix());;
			for(int vector = 0; vector < vertexSum; vector++) {
				for (int position = 0; position < vertexSum; position++) {
					if(!controlMatrix.getBinMatrix()[vector][position]) {
						if(adjacencyMatrizes[pathlength-1].getValueAt(vector, position) != 0) {
							pathMatrix.setValueAt(vector, position, 1);
							distanceMatrix.setValueAt(vector, position, pathlength);
							controlMatrix.setFlagAt(vector, position);
						}
					}
				}
			}
			pathlength++;
			controlMatrix.vectorize(controlMatrix.getBinMatrix());
		}while(!Matrix.isIdentical(controlMatrix, lastControlMatrix) && pathlength < vertexSum);
		
		if(!Matrix.isIdentical(controlMatrix, new Matrix(vertexSum,true))) {
			for(int vector = 0; vector < vertexSum; vector++) {
				for (int position = 0; position < vertexSum; position++) {
					if(!controlMatrix.getBinMatrix()[vector][position]) {
						distanceMatrix.setValueAt(vector, position, -1);
					}
				}
			}
			isCohesive = false;
		}
		
		pathMatrix.vectorize();
		distanceMatrix.vectorize();
		controlMatrix.vectorize(controlMatrix.getBinMatrix());
		if(isCohesive) {
			components = new Subgraph[1]; 
			components[0] = new Subgraph(this,vertices,edges,adjacencyMatrizes[0]);
		}else {
			calculateComponents();
		}
		
	}
	
	private void calculateComponents() {
		/*
		 * 1. aus der Wegmatrix heraus erstelle eine Liste der Knotennamen, 
		 * die miteinander pro Zeile verbunden sind
		 */
		Vector[] vertexConnections = new Vector[vertexSum];
		int[] vertexList = new int[0];
		for (int vector = 0; vector < vertexSum; vector++) {
			for (int position = 0; position < vertexSum; position++) {
				if(controlMatrix.getBinMatrix()[vector][position]) {
					vertexList = ArrayTools.push(vertexList, position+1);
				}
			}
			vertexConnections[vector] = new Vector(vertexList);
			vertexList = null;
			vertexList = new int[0];
		}
		
		/*
		 * 2. Mit der zuvor erstellten Liste, vergleiche die Knotennamen untereinander,
		 * gibt es eine übereinstimmung, so handelt es sich um eine bestehende Komponente.
		 * Gibt es keine Übereinstimmung, so wird eine neue Komponente erstellt.
		 * Am Schluss haben wir eine Liste an Komponenten mit ihren Knotennamen: componentList
		 */
		int componentCounter = 0;
		int vector = 0;
		Vector[] componentList = new Vector[1];
		componentList[0] = vertexConnections[0];
		while(vector < vertexSum) {
			if(componentCounter >= componentList.length) {
				componentList = GraphTools.push(componentList, vertexConnections[vector]);
				componentCounter = 0;
			}
			while(vector < vertexSum && componentList[componentCounter].isEqual(vertexConnections[vector])) {
				vector++;
				componentCounter = 0;
			}
			componentCounter++;
			
		}
		
		/*
		 * 3. Sättigen der einzelnen Komponenten aus componentList. Dies müssen Teilgraphen von G sein.
		 * Die Knoten und Kantenmengen dürfen keine neuen Objekte sein, sondern müssen Referenzen auf den Obergraph darstellen.
		 */
		this.components = new Subgraph[componentList.length];
		for(int i = 0; i < componentList.length; i++) {
			this.components[i] = saturate(componentList[i]);
		}
		
	}

	/*
	 * Sättige den bestehenden Obergraphen mit den Knotennamen in der Liste
	 */
	private Subgraph saturate(Vector vector) {
		int[] vertexIndizes = new int[vector.size];
		for (int i = 0; i < vector.size; i++) {
			vertexIndizes[i] = vector.getValueAt(i)-1; 
		}
		Matrix subAdjacencyMatrix = adjacencyMatrizes[0].shrinkByIndizes(vertexIndizes);
		Vertex[] subVertices = new Vertex[subAdjacencyMatrix.size];
		
		for(int i = 0; i < subVertices.length; i++) {
			subVertices[i] = getVertex(vector.getValueAt(i));
		}
		
		Edge[] subEdges = new Edge[0];
		
		int rowPosition = 0;
		int columnPosition = 0;
		
		while(rowPosition < subAdjacencyMatrix.size) {
			while(columnPosition < subAdjacencyMatrix.size) {
				if(subAdjacencyMatrix.getValueAt(rowPosition, columnPosition) == 1) {
					subEdges = GraphTools.push(subEdges, subVertices[rowPosition].getEdge(subVertices[columnPosition]));
				}
				columnPosition++;
			}
			rowPosition++;
			columnPosition = rowPosition;			
		}
		
		Subgraph saturatedSubgraph = new Subgraph(this,subVertices, subEdges, subAdjacencyMatrix);
		return saturatedSubgraph;
	}
	
	private Subgraph spann(Vector vector) {
		return null;
	}

	public void calculateEccentricities() {
		if(isCohesive) {
			eccentricity = distanceMatrix.vectorHighestValues(true);
			diameter = eccentricity.getMax();
			radius = eccentricity.getMin();
			eccentricity.findValueAndSet(eccentricity, radius, true);
			for(int i = 0; i < eccentricity.size; i++) {
				if(eccentricity.getBinValueAt(i)) {
					vertices[i].switchCenter();
				}
			}
		}else {
			eccentricity = new Vector(vertexSum, -1);
		}
		
	}
	
	
	/*
	 * *************************************************************************
	 */

	
	
	/*
	 * *************************************************************************
	 * Output Methods
	 * *************************************************************************
	 */
	
	
	public String toString() {
		String text = "";
		for (int i = 0; i < vertices.length; i++) {
			text += vertices[i]+"\n";
		}
		for (int j = 0; j < edges.length; j++) {
			text += edges[j]+"\n";
		}
		
		text += "\n Exzentrizitäten: \n";
		for (int k = 0; k < vertexSum; k++) {
			text += "Knoten: "+(k+1)+": "+eccentricity.getValueAt(k)+"\n";
		}
		
		text += "\n Radius: " + radius;
		text += "\n Durchmesser: " + diameter;
		
		text += "\n Zentrum: [";
		for (int l = 0; l < vertexSum; l++) {
			if(vertices[l].isCenter()) {
				text += vertices[l].getName() + ",";
			}
		}
		text += "]";
		
		text += "\n Componenten: \n";
		for(int i = 0; i < components.length; i++) {
			text += components[i];
		}
		
		return text;
	}
	
	

}
