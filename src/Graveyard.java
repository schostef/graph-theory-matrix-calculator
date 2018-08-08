import arraytools.GraphTools;
import graph.Edge;
import graph.Graph;
import graph.Path;
import graph.Vertex;
import javafx.concurrent.Task;

public class Graveyard {
	//Erstelle eine binäre Matrix um Nullen als true darzustellen
		private void createZeroMatrix() {
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if(matrix[i][j] == 0) {
						isZero[i][j] = true;
					}else {
						isZero[i][j] = false;
					}
				}
			}		
		}
}

//Start at the first Vertex that is degree >= 2 and not an articulation
		// Set this vertex as the root point
		// Also we store any circles that we find
		Vertex root = null;
		int stepper = 0;
		while(root == null) {
			if(!getVerticesByDegreeHigherThan(2)[stepper].isArticulation()) {
				root = getVerticesByDegreeHigherThan(2)[stepper];
			}
			stepper++;
		}
		
		Path[] circles = new Path[0];
		
		// Ask the root for it's neighbors and note the Path down
		Path[] checkList = new Path[root.getNeighbors().length];
		for(int i = 0; i < checkList.length; i++) {
			Vertex[] temp = {root,root.getNeighbors()[i]};
			checkList[i] = new Path(temp);
		}
		
		
		// Mark the according Edges as visited
		for(int i = 0; i < checkList.length; i++) {
			checkList[i].getEdgeOf(root).setVisited(true);;
		}
		
		//We now start to question each neighbor:
		for(int i = 0; i < checkList.length; i++) {
			// Where are you going?
			Vertex[] whereTo = checkList[i].getLast().getNeighbors();
						
			//Do you have any Edges left that are not checked?
			Edge[] toCheckUpOn = checkList[i].getUnvisitedEdges();
			if(	toCheckUpOn.length > 0) {
				//Does root know any of these Neighbors?
				for(int j = 0; j < whereTo.length; j++) {
					
					//If yes, we found a circle
					if(root.neighborExists(whereTo[j])) {
						//So we add the circle to the checklist and circles					
						Vertex[] temp = new Vertex[checkList[i].getVertexCount()];
						temp = checkList[i].getVertices();
						temp = GraphTools.push(temp, whereTo[j]);
						temp = GraphTools.push(temp, root);
						checkList[i].buildPath(temp);
						circles = GraphTools.push(circles, new Path(temp));
					}
				}
				
				//Does the Edge belong to a known Circle?
				for(int l = 0; l < toCheckUpOn.length; l++) {
					for(int k = 0; k < circles.length; k++) {
						if(circles[k].hasEdge(toCheckUpOn[l])) {
							//Then mark the edge as visited
							toCheckUpOn[l].setVisited(true);
						}else {
							//When not in a circle, does the Edge connect two Paths?
							int firstPath = i; // It must connect to the i Path
							int secondPath = 0; // It could connect to another Path
							
							while(secondPath < checkList.length) {
								if(secondPath == i) {
									secondPath++;
								}
								if(toCheckUpOn[l].hasVertex(checkList[secondPath].getLast())) {
									//If the edge connects two different Paths, it must be a circle
									//We close the circle on the checkList and add it to circles
									checkList[i].addVertex(checkList[secondPath].getLast());
									checkList[secondPath].addVertex(checkList[i].getLast());
									circles = GraphTools.push(circles, new Path(checkList[i].getVertices()));
									//Mark the edge as visited
									toCheckUpOn[l].setVisited(true);
								}
								secondPath++;
							}
						}
						
						// If not in a circle or a Path we must follow the Vertex further,
						// So we just add it to the Path
						if(!toCheckUpOn[l].isVisited()) {
							//We also flag the Edge and it's vertices as possible candidates for
							// a bridge and 2 articulations
							possibleBridges = GraphTools.push(possibleBridges, toCheckUpOn[l]);
							possibleArticulations = GraphTools.push(possibleArticulations, checkList[i].getLast());
							possibleArticulations = GraphTools.push(possibleArticulations, toCheckUpOn[l].getOppositeVertex(checkList[i].getLast()));
							checkList[i].addVertex(toCheckUpOn[l].getOppositeVertex(checkList[i].getLast()));
							//Then mark the Path as visited
							toCheckUpOn[l].setVisited(true);
						}
					}
				}				
			}else {
				//If not, we are done and can mark the vertex
				checkList[i].getLast().setVisited(true);
			}			
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
				//we look at each edge. If the two Vertices inside the edge show up in the remaining edges and lead to unknown edges
				//The circle might be extendable
				//[a,b],[b,c][c,a] --> [a,d] [d,e] [e,b] --> --(delete [a,b])-- [a,d] [d,e] [e,b] [b,c] [c,a]
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
			
			

		}
		
		
		public void refreshGraph() {
			Task task = new Task<Void>() {
				@Override public Void call() {
					updateProgress(0.1, 1.0);
					graph.createAdjacencyMatrix();
					updateProgress(0.3, 1.0);
					if(aMatrix.consistencyCheck(graph.getAdjacencyMatrix(1))) {
						graph.calculateAll();
						updateProgress(0.8, 1.0);
					}else {
						updateProgress(0.6,1.0);
						graph = new Graph(aMatrix.translateToMatrix());
					}
					infoArea.setText(graph.toString());
					return null;
				}
			};
			
			bar.progressProperty().bind(task.progressProperty());
			new Thread(task).start();
			if(task.isDone()) {
				System.out.println("Task done");
			}
			
		}