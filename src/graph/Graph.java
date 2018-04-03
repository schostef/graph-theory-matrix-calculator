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
	
	public void findBlocks() {
		//Set everything to not checked
		setAllVerticesVisited(false);
		setAllEdgesVisited(false);
		//We define vertexStart as all unvisited Vertices and retrieve them from the graph
		//We define edgesStart as all unchecked Edges and retrieve them from the Graph
		Vertex[] vertexStart = new Vertex[vertices.length];
		Edge[] edgesStart = new Edge[edges.length];
		for(int i = 0; i < vertexStart.length; i++) {
			vertexStart[i] = vertices[i];
		}
		for(int i = 0 ; i < edgesStart.length; i++) {
			edgesStart[i] = edges[i];
		}
		//We also define vTransfer as finished Vertices where we will move visited Vertices from vertexStart to vTransfer
		Vertex[] vTransfer = new Vertex[0];
		// Variable eTransfer will store our Edges from edgesStart as we build our path
		Edge[] eTransfer = new Edge[0];
		Edge[] eCircle = new Edge[0];
		// The Path array circles will store circles, that are found
		Path[] circles = new Path[0];
		// We also store articulations and bridges in their separate Arrays
		Vertex[] articulations = new Vertex[0];
		Edge[] bridges = new Edge[0];
		
		//First get all isolated and degree 1 Vertices
		Vertex[] isolatedVertices = getIsolatedVertices();
		Vertex[] degreeOneVertices = getVerticesByDegree(1);
		
		// Isolated Vertices can be removed from vertexStart
		// Their isolated information is already stored in the Vertex class
		// The blocks calculation will happen last
		if(isolatedVertices.length > 0) {
			for (int i = 0; i < isolatedVertices.length; i++) {
				vertexStart = GraphTools.delete(vertexStart, isolatedVertices[i]);
			}
		}
		
		// The Edges of degree 1 Vertices can be set as bridges
		// The vertex and the bridge can be set as visited
		// A bridge always ends with Articulations, except the Bridgehead is degree 1
		if(degreeOneVertices.length > 0) {
			for (int i = 0; i < degreeOneVertices.length; i++) {
				// There can only be one Neighbor and one path,
				// therefore we can directly access the path and the neighbor
				degreeOneVertices[i].getEdge(degreeOneVertices[i].getNeighbors()[0]).setVisited(true);
				degreeOneVertices[i].getEdge(degreeOneVertices[i].getNeighbors()[0]).setBridge(true);
				if(degreeOneVertices[i].getNeighbors()[0].getDegree() > 1) {
					degreeOneVertices[i].getNeighbors()[0].setArticulation(true);
				}
				//and remove the edges from startingEdges and remove the degree 1 vertex
				//as it won't make a circle at any time
				//the bridge will be added to bridges
				bridges = GraphTools.push(bridges, degreeOneVertices[i].getEdge(degreeOneVertices[i].getNeighbors()[0]));
				vertexStart = GraphTools.delete(vertexStart, degreeOneVertices[i]);
				edgesStart = GraphTools.delete(edgesStart, degreeOneVertices[i].getEdge(degreeOneVertices[i].getNeighbors()[0]));
			}
		}
		
		//We now initialize the first Vertex of Degree >= 2, that is not an Articulation as the root Vertex
		int z = 0;
		while(getVerticesByDegreeHigherThan(1)[z].isArticulation()) {
			z++;
		}
		Vertex root = getVerticesByDegreeHigherThan(1)[z];
		
		//The first run won't make a circle so we initialize:
		
		//Put root into vTransfer
		vTransfer = GraphTools.push(vTransfer, root);
		vertexStart = GraphTools.delete(vertexStart, root);
		
		//Put all it's edges into eTransfer
		//And all the neighbor Vertices into vTransfer
		for(int i = 0; i < root.getDegree(); i++) {
			eTransfer = GraphTools.push(eTransfer, root.getEdge(root.getNeighbors()[i]));
			vTransfer = GraphTools.push(vTransfer, root.getNeighbors()[i]);
			//And remove them from edgesStart and vertexStart
			edgesStart = GraphTools.delete(edgesStart, root.getEdge(root.getNeighbors()[i]));
			vertexStart = GraphTools.delete(vertexStart, root.getNeighbors()[i]);
			
			
		}
		//We also store the pathLength seperately
		int circleNumber = 0;
		
		//From now on we loop through the vTransfer Vertices and move their neighbors
		//and their edges to the Transfer Variables
		//The loop will stop, when everything has been moved
		while(vertexStart.length > 0 || edgesStart.length > 0) {
			
			// first we get all the next neighbors that are not known out of vertexStart
			for(int i = 0; i < vTransfer.length; i++) {
				for(int j = 0; j < vTransfer[i].getNeighbors().length; j++) {
					//If the neighbor is still in vertexStart
					if(GraphTools.hasValue(vertexStart, vTransfer[i].getNeighbors()[j])) {
						//and transfer the Vertex from vertexStart to vTransfer
						vTransfer = GraphTools.push(vTransfer, vTransfer[i].getNeighbors()[j]);
						vertexStart = GraphTools.delete(vertexStart, vTransfer[i].getNeighbors()[j]);						
					}
				}				
			}
			
			//after that get all Edges that are connected to Vertices in vTransfer
			//Transfer it's edges to from edgeStart to eTransfer
			for(int i = 0; i < vTransfer.length; i++) {
				for(int j = 0; j < vTransfer[i].getDegree(); j++) {
					if(GraphTools.hasValue(edgesStart, vTransfer[i].getEdge(j))) {
						eTransfer = GraphTools.push(eTransfer, vTransfer[i].getEdge(j));
						edgesStart = GraphTools.delete(edgesStart, vTransfer[i].getEdge(j));
					}
				}
				
			}
			
			//We will now check for circles
			//First we will make a counter for each Vertex in vTransfer
			int[] counter = new int[vTransfer.length];
			
			//We only consider edges that connect vertices, which are in the vTransfer List
			for (int i = 0; i < eTransfer.length; i++) {
				for(int j = 0; j < vTransfer.length; j++) {
					if(eTransfer[i].getVertices()[0].getName() == vTransfer[j].getName()) {
						for(int k = 0; k < vTransfer.length; k++) {
							if(eTransfer[i].getVertices()[1].getName() == vTransfer[k].getName()) {
								counter[j]++;
								counter[k]++;
							}
						}
					}
				}				
			}
			//All edges that will form circles will be moved to another array
			Edge[] possiblePaths = new Edge[0];
			boolean edgeFound = false;
			int startVertex = 0;
			int endVertex = 0;
			for (int i = 0; i < eTransfer.length; i++) {
				for (int j = 0; j < counter.length && eTransfer.length > 0; j++) {
					for (int k = j+1; k < counter.length && eTransfer.length > 0; k++) {
						if(eTransfer[i].hasVertex(vTransfer[j], vTransfer[k])){
							possiblePaths = GraphTools.push(possiblePaths, eTransfer[i]);
							eTransfer = GraphTools.delete(eTransfer, eTransfer[i]);
							j = 0;
							k = 0;
						}
					}
				}
			}
			
			//Start at the first edge
			Vertex startPoint = possiblePaths[0].getVertices()[0];
			Vertex endPoint = possiblePaths[0].getVertices()[1];
			Edge[] circleCollection = {possiblePaths[0]};
			//possiblePaths = GraphTools.delete(possiblePaths, possiblePaths[0]);
			int position = 0;
			//We keep track of the positioner and it's findings , pathLog[position][vertexName]
			int[][] pathLog = new int[possiblePaths.length][1];
			boolean nextFound = false;
			position = 1;
			//find the next Edge to connect to, keep repeating until startPoint is reached
			while(startPoint.getName() != endPoint.getName()) {
				for(int i = 0; i < pathLog.length; i++) {
					if(pathLog[i][0] == endPoint.getName()) {
						position = i+1;
						circleCollection = null;
						circleCollection = new Edge[1];
						circleCollection[0] = possiblePaths[0];
						endPoint = possiblePaths[0].getVertices()[1];	
						pathLog = null;
						pathLog = new int[possiblePaths.length][1];
					}
				}
				nextFound = false;
				while(!nextFound) {
					if (possiblePaths[position].hasVertex(endPoint)) {
						// If any Vertex other than the starting point shows up a second time,
						// we either found a shorter circle or an articulation
						// At this point we need to revert back to the crossed vertex and keep looking.
						//Otherwise keep going to the next edge
						pathLog[position][0]=endPoint.getName();
						endPoint = possiblePaths[position].getOppositeVertex(endPoint);
						circleCollection = GraphTools.push(circleCollection, possiblePaths[position]);
						// possiblePaths = GraphTools.delete(possiblePaths, possiblePaths[position]);
						nextFound = true;
						if(position < possiblePaths.length-1) {
							position++;
							
						}else {
							position = 1;
						}
						
					}else {
						if(position < possiblePaths.length-1) {
							position++;
							
						}else {
							position = 1;
						}
					}
				}
			}
			//circle found!
			// remove all edges that form the circle from possiblePaths
			for(int i = 0; i < circleCollection.length; i++) {
				possiblePaths = GraphTools.delete(possiblePaths, circleCollection[i]);
			}
			
			//To find out if this is the largest possible circle,
			//we look at each edge. Between the exit of the first edge and the entry of the second edge
			//[
			Vertex start = circleCollection[0].getOppositeVertex(startPoint);
			Vertex end = circleCollection[circleCollection.length-1].getOppositeVertex(startPoint);
			boolean startFound = false;
			boolean endFound = false;
			for(int i = 0; i < possiblePaths.length && startFound && endFound; i++) {
				if(possiblePaths[i].hasVertex(start)) {
					startFound = true;
				}
				
				if(possiblePaths[i].hasVertex(end)) {
					endFound = true;
				}
			}
			
			/*
			
			// We start at the first Vertex, that has a count of >= 2
			int position = Integer.MIN_VALUE;
			for (int i = 0; i < counter.length && position < 0; i++) {
				if(counter[i] >= 2) {
					position = i;
				}
			}
			// We get the first edge, that has this vertex inside
			for (int i = 0; i < eTransfer.length; i++) {
				if(eTransfer[i].hasVertex(vTransfer[position])) {
					
				}
			}
			
			// get to the next position along the edge
			// and find the next edge, that has the vertex
			// this will be done, until a circle is found
			// Now we can start constructing circles
			
			boolean circleFound = false;
			
			
			while(!circleFound) {
				if(counter[position] >= 2) {
					possiblePaths = GraphTools.push(possiblePaths, e)
				}
			}
			
			//With the counter we sort out all Vertices that have a count of lesser than 2
			for(int i = 0; i < counter.length; i++) {
				if(counter[i] < 2) {
					
				}
			}
			//Now we have all Edges that form a circle in the eCircle Array
			//We can remove these Edges from the eTransfer
			for (int i = 0; i < eCircle.length; i++) {
				eTransfer = GraphTools.delete(eTransfer, eCircle[i]);
			}
			*/
			
			
			
			
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
