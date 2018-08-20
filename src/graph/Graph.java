/**
 * @author Stefan Schoeberl
 * @version 1.0
 * @modified 2018-08-09
 * 
 * @Class Graph
 * Main class of the Graph. All Methods for adding, deleting Vertices and Edges, creating and translating an adjacency Matrix
 * and all calculations are handled in this class. Editing can go in two ways. Either adding vertices and edges manually and
 * creating an adjacency matrix afterwards. Or passing an adjacency matrix and edit the edge and vertex objects.
 */

package graph;

import arraytools.ArrayTools;
import arraytools.GraphTools;
import matrix.*;

public class Graph {
	//Main components
	protected Vertex[] vertices;
	protected Edge[] edges;
	protected Matrix[] adjacencyMatrizes; //Adjacency Matrix at Index 0, Exponential Matrices onwards, where Index + 1 = exponent
	protected int vertexSum = 0; //total number of Vertices
	protected int edgeSum = 0; //Total number of edges
	protected char name = 'G';
	//From calculations
	private EulerPath eulerPath;	
	protected Matrix pathMatrix;
	protected Matrix distanceMatrix;
	protected Vector eccentricity;
	protected int radius,diameter;
	protected boolean isCohesive = true;	
	protected Subgraph[] components;
	protected int componentAmount = 1;
	private String error = "";

	/*
	 * Constructors
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
		drawFromMatrix(m);
		
	}
	
	/*
	 * -> End Constructors
	 */	
	
	/*
	 * Getters
	 */
	
	public Edge[] getEulerPath() {
		return eulerPath.getPath();
	}
	public Edge[] getEdges() {
		return edges;
	}
	
	public Vertex[] getVertices() {
		return vertices;
	}
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
	 * Get the connecting edge between 2 vertices
	 * @param vertex
	 * @param vertex2
	 * @return
	 */
	public Edge getEdge(Vertex vertex, Vertex vertex2) {
		for(Edge e : edges) {
			if(e.connects(vertex,vertex2)) {
				return e;
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
	
	public int getRadius() {
		return radius;
	}
	
	public int getDiameter() {
		return diameter;
	}
	
	public int getComponentAmount() {
		return this.componentAmount;
	}
	
	public Subgraph[] getComponents() {
		return components;
	}
	
	private boolean edgeExists(Vertex vertex, Vertex vertex2) {
		for (Edge e: edges) {
			if(e.connects(vertex, vertex2)) {
				return true;
			}
		}
		return false;
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
	/*
	 * -> End Getters
	 */	
	
	/*
	 * Dimension Manipulating/Drawing/Constructing Methods
	 */
	
	/**
	 * Full reset. Clear all Vertices and Edges.
	 * Delete all Matrices.
	 */
	public void reinitialize() {
		vertices = null;
		edges = null;
		adjacencyMatrizes = null;
		vertexSum = 0;
		edgeSum = 0;
		vertices = new Vertex[0];
		edges = new Edge[0];
		adjacencyMatrizes = new Matrix[0];
	}
	
	/**
	 * Draws Vertices and Edges with a provided Adjacency Matrix.
	 * Reinitializes the Graph.
	 * Adds the Adjacency Matrix to the adjacencyMatrizes[0] Index
	 * @param m Adjacency Matrix
	 */
	public void drawFromMatrix(Matrix m) {
		boolean recalculate = false;
		if(adjacencyMatrizes == null || adjacencyMatrizes.length == 0) {
			reinitialize();
			addVertex(m.size);
			int[][] edgeIndizes = m.getCoordinatesOfValue(1,true);
			for(int[] c:edgeIndizes) {
				edges = GraphTools.push(edges, new Edge(getVertex(c[0]+1),getVertex(c[1]+1)));
			}
			recalculate = true;
		}else {
			if(vertexSum == m.size) {
				if(!adjacencyMatrizes[0].isIdentical(m)) {
					adjacencyMatrizes[0].makeBitMap(1);
					m.makeBitMap(1);
					Matrix changeMap = Matrix.bitOperationXOR(m,adjacencyMatrizes[0]);
					for(int i = 0; i < vertexSum; i++) {
						for(int j = i; j < vertexSum; j++) {
							if(changeMap.getBinMatrix()[i][j]) {
								if(m.getValueAt(i, j) == 1 && !edgeExists(getVertex(i+1), getVertex(j+1))) {
									addEdge(getVertex(i+1), getVertex(j+1));
								}else if (m.getValueAt(i, j) == 0 && edgeExists(getVertex(i+1), getVertex(j+1))){
									deleteEdge(getEdge(getVertex(i+1), getVertex(j+1)));
								}
							}
						}
					}
					recalculate = true;
				}
			}else {
				if(m.size > vertexSum) {
					addVertex(m.size-vertexSum);
					recalculate = true;
				}else {
					boolean deletedVertexFound = false;
					int index = 0;
					while(!deletedVertexFound || index < m.size) {
						Matrix tm = adjacencyMatrizes[0].removeCross(index);
						tm.vectorize();
						m.vectorize();
						if(tm.isIdentical(m)) {
							deleteVertex(getVertex(index+1));
							deletedVertexFound = true;
						}
						index++;
					}
					recalculate = true;
					if(!deletedVertexFound) {
						reinitialize();
						drawFromMatrix(m);
					}
					
				}
			}
		}
		adjacencyMatrizes = new Matrix[m.size];
		adjacencyMatrizes[0] = m;
		edgeSum = edges.length;
		if(recalculate) {
			calculateAll();
		}
	}	

	/**
	 * Add a series of new vertices
	 * @param amount Amount of vertices to create
	 */
	public void addVertex(int amount) {
		for(int i = 0; i < amount; i++) {
			addVertex();
		}
	}
	
	/**
	 * Adds a new vertex. Names will be incremented
	 */
	public void addVertex() {
		Vertex[] tV = GraphTools.expand(vertices);
		tV[vertexSum] = new Vertex(vertexSum+1);
		vertices = tV;
		vertexSum++;
	}
	
	/**
	 * Add a new Edge
	 */
	public void addEdge(Vertex v1, Vertex v2) {
		Edge[] tedge = GraphTools.expand(edges);
		tedge[edges.length] = new Edge(v1,v2);
		edges = tedge;
		edgeSum++;
	}	
	
	/**
	 * Deletes a Vertex and all incident edges
	 * @param v Vertex to delete
	 */
	public void deleteVertex(Vertex v) {
		for(Edge e : v.getAllEdges()) {
			deleteEdge(e);
		}
		vertices = GraphTools.delete(vertices, v);
		vertexSum--;
	}
	
	/**
	 * Deletes an Edge. Updates the information inside the connected vertices
	 * @param e
	 */
	public void deleteEdge(Edge e) {
		Vertex v1 = e.getVertices()[0];
		v1.deleteEdge(e);
		edges = GraphTools.delete(edges, e);
		edgeSum--;
	}
	
	/**
	 * Calculates the articulations in the current graph
	 */
	public void findArticulations() {
		// By removing each Vertex and recalculating the component amount in a subgraph
		// it can be determined if a vertex breaks the graph after being removed.
		// Isolated vertices can be ignored
		Subgraph[] subgraphs = new Subgraph[vertices.length];
		
		//Check isolation and store the new sum of vertices
		for (int i = 0; i < vertices.length; i++) {
			if(!vertices[i].isIsolated()) {
				Vertex[] reducedGraph = new Vertex[vertices.length-1];
				int position = 0;
				for(int j = 0; j < vertices.length; j++) {
					if(j != i) {
						reducedGraph[position] = vertices[j];
						position++;
					}
				}
				
				// calculate the new component amount
				subgraphs[i] = new Subgraph(reducedGraph);
				subgraphs[i].calculateAll();
				if(subgraphs[i].getComponentAmount() > this.componentAmount) {
					vertices[i].setArticulation(true);
				}else {
					vertices[i].setArticulation(false);
				}
			}
		}
	}
	
	/**
	 * Calculates the bridges in the current graph
	 */
	public void findBridges() {
		//Remove each edge and recalculate the component amount
		//Mark the edge if it breaks the graph
		for(int i = 0; i < edgeSum; i++) {
			Edge[] reducedGraph = new Edge[edgeSum-1];
			int position = 0;
			for(int j = 0; j < edgeSum; j++) {
				if(j != i) {
					reducedGraph[position] = edges[j];
					position++;
				}
			}
			Subgraph sg = new Subgraph(vertices,reducedGraph);
			sg.calculateAll();
			if(sg.getComponentAmount() > this.componentAmount) {
				edges[i].setBridge(true);
			} else {
				edges[i].setBridge(false);
			}
		}
	}
	
	
	
	/**
	 * Attempt to find a Euler Path of the graph
	 * Field eulerPath will store the path or remain null if there is no path possible
	 * Field error will store the reason why no path is possible
	 */
	public void findEulerPath() {
		try {
			EulerPath eulerPath = new EulerPath(vertices,edges);
			this.eulerPath = eulerPath;
		} catch(InputGraphNotCohesiveException ex) {
			error = ex.getMessage();
		}catch(EulerNotPossibleException ex) {
			error = ex.getMessage();
		}catch(PathException ex) {
			error = ex.getMessage();
		}		
	}		
	

	/*
	 * -> Dimension Manipulating/Drawing/Constructing Methods
	 */
	
	/*
	 * Matrix methods
	 */
		
	public void calculateExponentialMatrix(int exponent) {
		for(int i = 1; i < exponent; i++) {
			if(adjacencyMatrizes[i] == null) {
				adjacencyMatrizes[i] = adjacencyMatrizes[0].multiply(adjacencyMatrizes[i-1]);
			}
		}
	}
	
	/**
	 * Construct the Adjacency Matrix from the drawn Graph and add it to the
	 * adjacencyMatrizes[0] index
	 */
	public void createAdjacencyMatrix() {
		if(vertexSum > 1) {
			refreshAdjacencyMatrizes();
			adjacencyMatrizes[0] = new Matrix(vertexSum);
			int[] vertexIndex = new int[vertexSum];
			for(int i = 0; i < vertexSum; i++) {
				vertexIndex[i] = vertices[i].getName();
			}
			
			for (Edge e: edges) {
				int v1 = e.getVertices()[0].getName();
				int v2 = e.getVertices()[1].getName();
				if(ArrayTools.contains(vertexIndex,v1) && ArrayTools.contains(vertexIndex,v2)) {
					int x = ArrayTools.indexOf(vertexIndex,v1);
					int y = ArrayTools.indexOf(vertexIndex,v2);
					
					adjacencyMatrizes[0].setValueAt(x, y, 1);
					adjacencyMatrizes[0].setValueAt(y, x, 1);
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
		pathMatrix = new Matrix(adjacencyMatrizes[0].getMatrix());
		for(int i = 0; i < vertexSum; i++) {
			pathMatrix.setValueAt(i, i, 1);
		}
		pathMatrix.vectorize();
		pathMatrix.makeBitMap(0);
		pathMatrix.vectorize(pathMatrix.getBinMatrix());
	}
	
	public void initializeDistanceMatrix() {
		distanceMatrix = new Matrix(adjacencyMatrizes[0].getMatrix());
		distanceMatrix.vectorize();		
	}	
	
	/*
	 * -> End Matrix methods
	 */
	
	/*
	 * Calculations
	 */
	
	/**
	 * Calculate individual components of the current Graph.
	 * Components will be stored in the components field.
	 */
	protected void calculateComponents() {
		//createAdjacencyMatrix();
		initializeDistanceMatrix();
		initializePathMatrix();
		calculateDistancePathMatrix();
		if(isCohesive) {
			components = new Subgraph[1]; 
			components[0] = new Subgraph(vertices,edges);
			componentAmount = 1;
		}else {			
			Vector[] pathMatch = pathMatrix.fetchEqualRows();
			pathMatch = GraphTools.removeDuplicates(pathMatch);
			int[][] componentIndizes = new int[pathMatch.length][0];
			for(int i = 0; i < pathMatch.length; i++) {
				componentIndizes[i] = pathMatch[i].getPositionOfValue(1);
			}
			
			this.componentAmount = pathMatch.length;
			this.components = new Subgraph[componentAmount];
			for(int i = 0; i < componentAmount; i++) {
				Vertex[] v = new Vertex[componentIndizes[i].length];
				for(int j = 0; j < componentIndizes[i].length; j++) {
					v[j] = getVertex(componentIndizes[i][j]+1);
				}
				this.components[i] = new Subgraph(v);
			}
		}		
	}
	
	/**
	 * Calculate the Distance and Path Matrix and store them in the distanceMatrix and pathMatrix field.
	 */
	public void calculateDistancePathMatrix() {
		if(vertexSum > 2) {
			int pathlength = 2;
			Matrix lastPathMatrix;
			Matrix changeMatrix;
			do {
				lastPathMatrix = new Matrix(pathMatrix.getBinMatrix());
				calculateExponentialMatrix(pathlength);
				adjacencyMatrizes[pathlength-1].makeBitMap(0);
				adjacencyMatrizes[pathlength-2].makeBitMap(0);
				//distanceMatrix.makeBitMap(0);
				changeMatrix = Matrix.bitOperationAND(lastPathMatrix, (Matrix.bitOperationXOR(adjacencyMatrizes[pathlength-2], adjacencyMatrizes[pathlength-1])));
				for(int i = 0; i < vertexSum; i++) {
					for( int j = 0; j < vertexSum; j++) {
						if(changeMatrix.getBinMatrix()[i][j]) {
							pathMatrix.setValueAt(i, j, 1);
							distanceMatrix.setValueAt(i, j, pathlength);
						}
					}
				}
				pathlength++;
				pathMatrix.vectorize();
				lastPathMatrix.vectorize(lastPathMatrix.getBinMatrix());
				pathMatrix.makeBitMap(0);
				pathMatrix.vectorize(pathMatrix.getBinMatrix());
			}while(!Matrix.isIdentical(pathMatrix, lastPathMatrix) && pathlength < vertexSum);
		}
		
		if(!Matrix.isIdentical(pathMatrix, new Matrix(vertexSum,false))) {
			for(int i = 0; i < vertexSum; i++) {
				for (int j = 0; j < vertexSum; j++) {
					if(pathMatrix.getBinMatrix()[i][j]) {
						distanceMatrix.setValueAt(i, j, -1);
					}
				}
			}
			isCohesive = false;
		}else {
			isCohesive = true;
		}
		
		pathMatrix.vectorize();
		distanceMatrix.vectorize();
		
		
	}
	
	/**
	 * Calculate Eccentricities of the Graph.
	 * Store radius in field: radius. Diameter in field diameter.
	 * Mark center Vertices.
	 */
	public void calculateEccentricities() {
		if(isCohesive) {
			eccentricity = distanceMatrix.vectorHighestValues(true);
			diameter = eccentricity.getMax();
			radius = eccentricity.getMin();
			eccentricity.findValueAndSet(eccentricity, radius, true);
			for(int i = 0; i < eccentricity.size; i++) {
				if(eccentricity.getBinValueAt(i)) {
					vertices[i].setCenter(true);
				}else {
					vertices[i].setCenter(false);
				}
			}
		}else {
			eccentricity = new Vector(vertexSum, -1);
		}
		
	}
	
	public void calculateAll() {
		calculateComponents();
		calculateEccentricities();
		findArticulations();
		findBridges();
		findEulerPath();
		
	}
	/*
	 * -> End Calculations
	 */
	
	
		
	/*
	 * Output Methods
	 */
	
	
	public String toString() {
		String text = "";
		text += "Anzahl der Knoten: \t\t"+vertexSum+"\nAnzahl der Kanten: \t\t"+edgeSum+"\n\n";
		text += "Zusammenhaengend: \t";
		if(isCohesive) {
			text += "Ja";
			text += "\n \nRadius: \t\t\t\t"+radius;
			text += "\n \nDurchmesser: \t\t\t" + diameter;
			
			text += "\n \nZentrum: \t\t\t\t{";
			Vertex[] center = new Vertex[0];
			for (int i = 0; i < vertexSum; i++) {
				if(vertices[i].isCenter()) {
					center = GraphTools.push(center, vertices[i]);
				}
			}
			for (int i = 0; i < center.length; i++) {
				if(i == center.length-1) {
					text += center[i].getName();
				}else {
					text += center[i].getName() + " , ";
				}
			}
			text += "}";
			text += "\n\nEuler'sche Linie: ";
			if(eulerPath != null) {
				if(eulerPath.getUnevenDegreeAmount() == 0)
					text += "\t\t geschlossen\n";
				else
					text += "\t\t offen\n";
				text += eulerPath;
				text += "}";
			}else {
				text += "\t\t nicht moeglich\n"+error;
			}
		}else {
			text += "Nein";
			text += "\n \nAnzahl Komponenten: \t" + componentAmount;
		}
		int articulationAmount = getArticulationAmount();
		text += "\n \nArtikulationen: \t\t";
		if(articulationAmount == 0) {
			text += "Keine";
		}else {
			text += articulationAmount+"\n{";
			Vertex[] arts = getArticulations();
			for(int i = 0; i < arts.length; i++) {
				if(i==arts.length-1) {
					text += arts[i];
				}else {
					text += arts[i]+" , ";
				}				
			}
			text += "}";
		}
		
		int bridgeAmount = getBridgeAmount();
		text += "\n \nBruecken: \t\t\t";
		if(bridgeAmount == 0) {
			text += "Keine";
		}else {
			text += bridgeAmount+"\n{";
			Edge[] bridges = getBridges();
			for (int i = 0; i < bridges.length; i++) {
				if(i == bridges.length-1) {
					text += bridges[i];
				}else {
					text += bridges[i]+" , ";
				}
				
			}
			text += "}";
		}
		return text;
	}
	/*
	 * -> End Output Methods
	 */
}

