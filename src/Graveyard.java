import arraytools.GraphTools;
import graph.Edge;
import graph.Path;
import graph.Vertex;

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