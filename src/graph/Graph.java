/**
 * @author Stefan Schoeberl
 * @version 0.1
 * @modified 2018-03-23
 * 
 * @Class Graph
 * Main class of the Graph.
 */

package graph;
import java.time.chrono.IsoChronology;

import arraytools.ArrayTools;
import arraytools.GraphTools;
import matrix.*;

public class Graph {
	private boolean vertexNamesAreInt = true; //Vertex names should either be Letters or Numbers, not both
	//Store vertices and edges here
	protected Vertex[] vertices;
	protected Edge[] edges;
	private Edge[] eulerPath;
	protected Matrix[] adjacencyMatrizes; //Adjacency Matrix at Index 0, exponentialmatrizes onwards, where Index + 1 = exponent
	protected Matrix pathMatrix;
	protected Matrix distanceMatrix;
	protected Matrix controlMatrix;
	protected Vector eccentricity;
	protected int radius,diameter;
	protected boolean isCohesive = true;
	protected char name = 'G';
	protected int vertexSum = 0; //total number of Vertices
	protected int edgeSum = 0; //Total number of edges
	protected Subgraph[] components;
	protected int componentAmount = 1;

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
	
	public Vertex[] getArticulations() {
		Vertex[] arts = new Vertex[0];
		for (int i = 0; i < vertices.length; i++) {
			if(vertices[i].isArticulation()) {
				arts = GraphTools.push(arts, vertices[i]);
			}
		}
		
		return arts;
	}
	
	public Edge[] getBridges() {
		Edge[] bridges = new Edge[0];
		for(int i = 0; i < edges.length; i++) {
			if(edges[i].isBridge()) {
				bridges = GraphTools.push(bridges, edges[i]);
			}
		}
		return bridges;
	}
	
	public int getIndexOf(int vertexName) {
		for(int i = 0; i < vertices.length; i++) {
			if(vertices[i].getName() == vertexName) {
				return i;
			}
		}
		return -1;
	}
	
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
	
	private Vertex[] getIsolatedVertices() {
		Vertex[] isolatedVertices = new Vertex[0];
		for(int i = 0; i < vertices.length; i++) {
			if(vertices[i].isIsolated()) {
				isolatedVertices = GraphTools.push(isolatedVertices, vertices[i]);
			}
		}
		return isolatedVertices;
	}
	
	private Vertex[] getVerticesByDegree(int degree) {
		Vertex[] vertexList = new Vertex[0];
		for(int i = 0; i < vertices.length; i++) {
			if(vertices[i].getDegree() == degree) {
				vertexList = GraphTools.push(vertexList, vertices[i]);
			}
		}
		return vertexList;
	}
	
	private Vertex[] getVerticesByDegreeHigherThan(int degree) {
		Vertex[] vertexList = new Vertex[0];
		for(int i = 0; i < vertices.length; i++) {
			if(vertices[i].getDegree() > degree) {
				vertexList = GraphTools.push(vertexList, vertices[i]);
			}
		}
		return vertexList;
	}
	
	/*
	 * ************************************************************************
	 */
	
	/*
	 * ************************************************************************
	 * Setters
	 * ************************************************************************
	 */
	
	private void setAllEdgesVisited(boolean b) {
		for(int i = 0; i < edges.length; i++) {
			edges[i].setVisited(b);
		}
		
	}

	private void setAllVerticesVisited(boolean b) {
		for(int i = 0; i < vertices.length; i++) {
			vertices[i].setVisited(b);
		}
		
	}
	
	/*
	 * *************************************************************************
	 */
	
	
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
	
	public Subgraph removeVertex(Vertex v) {
		// get the incident edges of v
		Edge[] tempEdges = new Edge[edges.length-v.getDegree()];
		int currentIndex = 0;
		for(int i = 0; i < edges.length; i++) {
			if(!v.hasEdge(edges[i])) {
				tempEdges[currentIndex] = edges[i];
				currentIndex++;
			}
		}
		
		currentIndex = 0;
		Vertex[] tempVertices = new Vertex[vertexSum-1];
		for(int i = 0; i < vertices.length; i++) {
			if(vertices[i].getName() != v.getName()) {
				tempVertices[currentIndex]=vertices[i];
				currentIndex++;
			}
		}
		
		Matrix tempMatrix = adjacencyMatrizes[0].removeCross(v.getName()-1);
		return new Subgraph(this,tempVertices,tempEdges,tempMatrix);
	}
	
	public void findArticulations() {
		Subgraph[] subgraphs = new Subgraph[vertices.length];
		
		for (int i = 0; i < vertices.length; i++) {
			subgraphs[i] = removeVertex(vertices[i]);
			subgraphs[i].calculateAll();
			if(subgraphs[i].getComponentAmount() > this.componentAmount) {
				vertices[i].setArticulation(true);
			}
		}
		// For every Vertex, remove the vertex.
		// Except for isolated ones.
		// check components of the subgraph
		// if it's more than before
		// mark the vertex as articulation
	}
	
	public void findBridges() {
		Subgraph[] subgraphs = new Subgraph[edgeSum];
		for (int i = 0; i < edgeSum; i++) {
			Edge[] edges = new Edge[edgeSum-1];
			int pos = 0;
			for(int j = 0; j < edgeSum; j++) {
				if(j != i) {
					edges[pos] = this.edges[j];
					pos++;
				}
			}
			subgraphs[i] = span(edges);
			subgraphs[i].calculateAll();
			if(subgraphs[i].getComponentAmount() > this.componentAmount) {
				this.edges[i].setBridge(true);
			}
			
		}
	}
	
	public int getBridgeAmount() {
		int bridgeCounter = 0;
		for (int i = 0; i < edgeSum; i++) {
			if(edges[i].isBridge()) {
				bridgeCounter++;
			}
		}
		return bridgeCounter;
	}
	
	public int getArticulationAmount() {
		int articulationCounter = 0;
		for (int i = 0; i < vertexSum; i++) {
			if(vertices[i].isArticulation()) {
				articulationCounter++;
			}
		}
		return articulationCounter;
	}
	
	public void findEulerPath() {
		int startingVertex = -1;
		int endingVertex = -1;
		if(isEulerClosed()) {
			startingVertex = 1;
			endingVertex = 1;
		}else if(isEulerOpen()) {
			int vertexCount = 1;
			while(startingVertex == -1 || endingVertex == -1) {
				if(vertices[vertexCount].getDegree()%2 != 0) {
					if (startingVertex < 0) {
						startingVertex = vertices[vertexCount].getName();
					}else {
						endingVertex = vertices[vertexCount].getName();
					}
				}
				vertexCount++;
			}
		}
		
		if(startingVertex > 0) {
			// Copy all Edges
			Edge[] eulerBuilder = new Edge[edgeSum];
			for(int i = 0; i < edgeSum; i++) {
				eulerBuilder[i] = edges[i];
			}
			
			//Pick the first edge at startingVertex
			int vertindex = getIndexOf(startingVertex);
			eulerPath = new Edge[edgeSum];
			eulerPath[0] = vertices[vertindex].getEdge(0);
			
			//delete it from the eulerBuilder
			eulerBuilder = GraphTools.delete(eulerBuilder, eulerPath[0]);
			
			//Build a path until there are no edges left in the eulerBuilder
			Vertex pathPosition = eulerPath[0].getOppositeVertex(vertices[getIndexOf(startingVertex)]);
			int pathLength = 1;
			while(eulerBuilder.length > 1) {
				boolean nextEdge = false;
				int edgeCounter = 0;
				while(!nextEdge) {
					if(GraphTools.hasValue(eulerBuilder, pathPosition.getEdge(edgeCounter))) {
						if(pathPosition.getEdge(edgeCounter).getOppositeVertex(pathPosition) == getVertex(endingVertex) && 
								GraphTools.countVertex(eulerBuilder,getVertex(endingVertex)) <= 1) {
							edgeCounter++;
						}else {
							eulerPath[pathLength] = pathPosition.getEdge(edgeCounter);
							eulerBuilder = GraphTools.delete(eulerBuilder, pathPosition.getEdge(edgeCounter));
							pathPosition = eulerPath[pathLength].getOppositeVertex(pathPosition);
							pathLength++;
							nextEdge = true;
						}
					}else {
						edgeCounter++;
					}
				}
			}
			eulerPath[pathLength] = eulerBuilder[0];
		}
		
	}
	
	public boolean isEulerClosed() {
		int unevenDegreeCounter = 0;
		int vertexCount = 0;
		if(isCohesive) {
			while(unevenDegreeCounter == 0) {
				if(vertexCount >= vertexSum) {
					return true;
				}
				if(vertices[vertexCount].getDegree()%2 != 0) {
					unevenDegreeCounter++;
				}
				vertexCount++;
				
			}
		}
		return false;
	}
	
	public boolean isEulerOpen() {
		int unevenDegreeCounter = 0;
		int vertexCount = 0;
		if(isCohesive && !isEulerClosed()) {
			while(unevenDegreeCounter <= 2) {
				if(vertexCount >= vertexSum) {
					return true;
				}
				if(vertices[vertexCount].getDegree()%2 != 0) {
					unevenDegreeCounter++;
				}
				vertexCount++;
				
			}
		}
		return false;
	}
	
	public void calculateBlocks() {
		
		//Start with articulations
		//Create a new subgraph with each vertex removed
		for (int i = 0; i < vertexSum; i++) {
			
		}
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
			lastControlMatrix = new Matrix(controlMatrix.getBinMatrix());
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
	
	protected void calculateComponents() {
		/*
		 * 1. aus der Wegmatrix heraus erstelle eine Liste der Knotennamen, 
		 * die miteinander pro Zeile verbunden sind
		 */
		Vector[] vertexConnections = new Vector[vertexSum];
		int[] vertexList = new int[0];
		for (int vector = 0; vector < vertexSum; vector++) {
			for (int position = 0; position < vertexSum; position++) {
				if(controlMatrix.getBinMatrix()[vector][position]) {
					vertexList = ArrayTools.push(vertexList, vertices[position].getName());
				}
			}
			vertexConnections[vector] = new Vector(vertexList);
			vertexList = null;
			vertexList = new int[0];
		}
		
		/*
		 * 2. Mit der zuvor erstellten Liste, vergleiche die Knotennamen untereinander,
		 * gibt es eine ï¿½bereinstimmung, so handelt es sich um eine bestehende Komponente.
		 * Gibt es keine ï¿½bereinstimmung, so wird eine neue Komponente erstellt.
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
		 * 3. Sï¿½ttigen der einzelnen Komponenten aus componentList. Dies mï¿½ssen Teilgraphen von G sein.
		 * Die Knoten und Kantenmengen dï¿½rfen keine neuen Objekte sein, sondern mï¿½ssen Referenzen auf den Obergraph darstellen.
		 */
		this.componentAmount = componentList.length;
		this.components = new Subgraph[componentAmount];
		for(int i = 0; i < componentAmount; i++) {
			this.components[i] = saturate(componentList[i]);
		}
		
	}
	
	/*
	 * **************************************************************************************
	 * Combined Methods
	 * **************************************************************************************
	 */
	
	public void calculateAll() {
		calculateExponentialMatrizes();
		initializeDistanceMatrix();
		initializePathMatrix();
		calculateDistancePathMatrix();
		calculateEccentricities();
		
	}
	
	/*
	 * **************************************************************************************
	 */

	/*
	 * Sï¿½ttige den bestehenden Obergraphen mit den Knotennamen in der Liste
	 */
	protected Subgraph saturate(Vector vector) {
		int[] vertexIndizes = new int[vector.size];
		for (int i = 0; i < vector.size; i++) {
			vertexIndizes[i] = getIndexOf(vector.getValueAt(i)); 
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
	
	private Subgraph span(Edge[] tree) {
		Matrix subMatrix = new Matrix(vertexSum);
		int row = 0;
		int column = 0;
		for (int i = 0; i < tree.length; i++) {
			row = getIndexOf(tree[i].getVertices()[0].getName());
			column = getIndexOf(tree[i].getVertices()[1].getName());
			subMatrix.setValueAt(row, column, 1);
			subMatrix.setValueAt(column, row, 1);
		}
		
		Subgraph spanningSubgraph = new Subgraph(this,vertices,tree,subMatrix);
		return spanningSubgraph;
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
		
		text += "\n Exzentrizitï¿½ten: \n";
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
			text += "Komponente " + (i+1) +": \n";
			text += components[i];
		}
		
		text += "\n Artikulationen: \n";
		text += getArticulationAmount() +"\n";
		Vertex[] arts = getArticulations();
		for(int i = 0; i < arts.length; i++) {
			text += arts[i];
		}
		
		text += "\n\n Brücken: \n";
		text += getBridgeAmount() + "\n";
		Edge[] bridges = getBridges();
		for (int i = 0; i < bridges.length; i++) {
			text += bridges[i];
		}
		
		return text;
	}
	
	

}
