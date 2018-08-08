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
	//Store vertices and edges here
	protected Vertex[] vertices;
	protected Edge[] edges;
	private EulerPath eulerPath;
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
	private String error = "";

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
		drawFromMatrix(m);
		
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
	
	public Matrix getControlMatrix() {
		return controlMatrix;
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
	
	
	
	/*
	 * ************************************************************************
	 */
	
	/*
	 * ************************************************************************
	 * Setters
	 * ************************************************************************
	 */
	
	
	
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
	 * Adds a new vertex
	 * @param name
	 */
	/*
	public void addVertex(int name) {
		Vertex[] tV = GraphTools.expand(vertices);
		tV[vertexSum + 1] = new Vertex(name);
		vertices = tV;
		//sortVertices();
		vertexSum++;
		createAdjacencyMatrix();
	}
	*/
	
	public void addVertex(int amount) {
		for(int i = 0; i < amount; i++) {
			addVertex();
		}
	}
	
	/**
	 * Adds a new vertex
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
		tedge[edges.length] = new Edge(edgeSum,v1,v2,"["+v1.getName()+","+v2.getName()+"]");
		edges = tedge;
		edgeSum++;
	}	
	
	public void deleteVertex(Vertex v) {
		for(Edge e : v.getAllEdges()) {
			deleteEdge(e);
		}
		vertices = GraphTools.delete(vertices, v);
		vertexSum--;
	}
	
	public void deleteEdge(Edge e) {
		Vertex v1 = e.getVertices()[0];
		v1.deleteEdge(e);
		edges = GraphTools.delete(edges, e);
		edgeSum--;
	}
	
	public Subgraph subgraphDeleteVertex(Vertex v) {
		Subgraph sg = new Subgraph(vertices,edges);
		sg.deleteVertex(v);
		return sg;
	}
	
	public void findArticulations() {
		Subgraph[] subgraphs = new Subgraph[vertices.length];
		
		//Only check if Vertex is not isolated
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
				
				subgraphs[i] = new Subgraph(reducedGraph);
				subgraphs[i].calculateAll();
				if(subgraphs[i].getComponentAmount() > this.componentAmount) {
					vertices[i].setArticulation(true);
				}else {
					vertices[i].setArticulation(false);
				}
			}
		}
		// For every Vertex, remove the vertex.
		// Except for isolated ones.
		// check components of the subgraph
		// if it's more than before
		// mark the vertex as articulation
	}
	
	public void findBridges() {
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
		/*
		//0. Prüfen, ob euler überhaupt möglich ist und unterscheiden, zwischen geschlossen und offen
		if(isCohesive && getUnevenDegreeAmount() < 3) {
			if(getUnevenDegreeAmount() == 0) {
				eulerPath = assembleClosedEulerPath(edges,vertices[0]);
			} else {
				//1. Teilgraph erstellen ohne Brücken, Brücken separat speichern
				Edge[] edgesWithoutBridges = new Edge[edgeSum-getBridgeAmount()];
				Edge[] bridges = new Edge[getBridgeAmount()];
				int idxA = 0;
				int idxB = 0;
				for(int i = 0; i < edgeSum; i++) {
					if(!edges[i].isBridge()) {
						edgesWithoutBridges[idxA] = edges[i];
						idxA++;
					}else {
						bridges[idxB] = edges[i];
						idxB++;
					}
				}				
				
				Subgraph bridgelessSubgraph = new Subgraph(vertices,edgesWithoutBridges);
				
				//2. Komponenten berechnen und als Untergraphen speichern
				
				bridgelessSubgraph.calculateComponents(true);
				
				//3. Von jeder einzelnen Komponente den eulerschen weg erstellen
				Edge[] partitionedClosedEulers = new Edge
				for(Subgraph c:bridgelessSubgraph.getComponents()) {
					if (c.isCohesive) {
						
					}
				}
				//4. Brücken so anordnen, dass sie start und endpunkt und eine verbindung zu dne komponenten herstellen
			}
		}
		*/
		
		
		
	}
	
	

	public int getUnevenDegreeAmount() {
		int unevenDegreeCounter = 0;
		for (Vertex v: vertices) {
			if(v.getDegree()%2 != 0) {
				unevenDegreeCounter++;
			}
		}
		return unevenDegreeCounter;
	}
	
	public static int getUnevenDegreeAmount(Vertex[] vertices) {
		int unevenDegreeCounter = 0;
		for (Vertex v: vertices) {
			if(v.getDegree()%2 != 0) {
				unevenDegreeCounter++;
			}
		}
		return unevenDegreeCounter;
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
		for(int i = 1; i < vertexSum-1 ; i++) {
			adjacencyMatrizes[i] = adjacencyMatrizes[0].exponentiate(i+1);
		}
	}
	
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
	
	protected void calculateComponents() {
		createAdjacencyMatrix();
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
				componentIndizes[i] = pathMatch[i].getIndizesof(1);
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
	
	/*
	 * **************************************************************************************
	 * Combined Methods
	 * **************************************************************************************
	 */
	
	public void calculateAll() {
		calculateComponents();
		calculateEccentricities();
		findArticulations();
		findBridges();
		findEulerPath();
		
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
					vertices[i].setCenter(true);
				}else {
					vertices[i].setCenter(false);
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
				if(getUnevenDegreeAmount() == 0)
					text += "\t\t geschlossen\n";
				else
					text += "\t\t offen\n";
				text += eulerPath;
				text += "}";
			}else {
				text += "\t\t nicht möglich\n"+error;
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
		
		
		
		/*
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
		
		text += "\n\n Brï¿½cken: \n";
		text += getBridgeAmount() + "\n";
		Edge[] bridges = getBridges();
		for (int i = 0; i < bridges.length; i++) {
			text += bridges[i];
		}
		
		return text;
		*/
		return text;
	}


	
	

}
