/**
 * @author Stefan Schoeberl
 * @version 1.0
 * @modified 2018-08-09
 * 
 * @Class EulerPath
 * A Euler Path is a Path that includes every Edge of the Graph.
 * It can be initialized directly by providing a series of vertices and edges.
 * The path will be built automatically, no further method calls necessary.
 * Note: PathExceptions must be caught as they will be thrown if an Euler Path isn't possible
 */

package graph;
import arraytools.GraphTools;
public class EulerPath {
	private Vertex[] vertices;
	private Edge[] edges;
	private Vertex startFrom;
	private Vertex goTo;
	private Edge[] path;
	private String[] pathToString;
	public boolean isCircle = false;
	
	/*
	 * Constructors
	 */
	
	/**
	 * Attempt to construct an Euler Path out of a series of vertices and edges.
	 * If successful, the path will be stored in an Edge Array path.
	 * Before any attempt is made two conditions will be checked:
	 * 1st: Is the given Graph cohesive?
	 * 2nd: Are there less than 2 Vertices with an uneven degree
	 * If both conditions apply a series of checks will decide on how to proceed:
	 * Is the path a circle? (All vertices having an even degree):
	 * Directly build the path.
	 * Is the path not a circle?
	 * -a: Does it have Bridges? Then partition
	 * -b: No Bridges? Build the path
	 * @see getPath	
	 * @param vertices Series of vertices from the graph
	 * @param edges Series of edges from the graph
	 * @throws PathException 
	 * InputGraphNotCohesiveException: The Graph is not cohesive
	 * EulerNotPossibleException: More than 2 vertices with an uneven degree found
	 */
	public EulerPath (Vertex[] vertices, Edge[] edges) throws PathException {
		this.vertices = vertices;
		this.edges = edges;
		
		//Calculate components out of the provided vertices and edges and check if the graph is cohesive
		//and no more than 2 Vertices with an uneven Degree are provided
		Subgraph s = new Subgraph(vertices, edges);
		int unevenDegreeVertexAmount = getUnevenDegreeAmount();
		s.calculateAll();
		if(s.getComponentAmount() > 1) {
			throw new InputGraphNotCohesiveException("Übergebener Graph nicht zusammenhängend");
		}
		
		if(unevenDegreeVertexAmount > 2) {
			throw new EulerNotPossibleException("Euler nicht möglich, mehr als 2 Kanten mit ungeraden Grad vorhanden");
		}
		
		pathToString = new String[edges.length];
		
		//Depending on the Amount of Vertices with an uneven Degree and the amount of bridges
		//further construction will be decided
		
		//everything is of an even degree == It's a circle
		// Start and end point of the path are the same and set to the first vertex
		if(getUnevenDegreeAmount() == 0) {
			isCircle = true;
			goTo = startFrom = vertices[0];
			path = assembleEulerPath(edges,startFrom,startFrom);
		}else {
			//otherwise it's not a circle and the start and end point of the path need to be set to
			// the two vertices with an uneven degree
			isCircle = false;
			Vertex[] unevenDegreeVertices = getUnevenDegreeVertices();
			startFrom = unevenDegreeVertices[0];
			goTo = unevenDegreeVertices[1];
			// If the path has no bridges or only consists of bridges, construction can begin right away
			// otherwise if the path is mixed. It will be partitioned, calculated separately and stitched back together
			if(!hasBridges()) {
				path = assembleEulerPath(edges,startFrom,goTo);
			}else if(countBridges() == edges.length){
				path = assembleEulerPath(edges,startFrom,goTo);
			}else {
				EulerPath[] partitions = partition();
				EulerPath stitchedPath = stitch(partitions);
				path = stitchedPath.getPath();
				startFrom = stitchedPath.getStartFrom();
				goTo = stitchedPath.getGoTo();
				isCircle = stitchedPath.isCircle();
			}
		}
		
	}
	
	/*
	 * -> End Constructors
	 */
	
	/*
	 * Setters
	 */
	
	private void setStartEndPoint(Vertex v) {
		goTo = startFrom = v;
	}
	
	private void setGoTo(Vertex e) {
		goTo = e;
		
	}

	private void setStartFrom(Vertex s) {
		startFrom = s;
		
	}
	
	private void setCircle(boolean b) {
		isCircle = b;
	}
	
	private void setPath(Edge[] newPath) {
		path = newPath;
	}
	
	/*
	 * -> End Setters
	 */
	
	/*
	 * Getters
	 */
	
	private Vertex getGoTo() {
		return goTo;
	}
	
	private Vertex getStartFrom() {
		return startFrom;
	}
	
	private boolean isCircle() {
		return isCircle;
	}
	
	/**
	 * Checks if a EulerPath intersects with this EulerPath
	 * @param ep
	 * @return true if intersection found
	 */
	private boolean intersects(EulerPath ep) {
		for(Vertex v: vertices) {
			if(GraphTools.containsVertex(ep.getVertices(), v)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if a EulerPath intersects with this EulerPath.
	 * @param ep
	 * @return the Vertex of the Intersection. null if no Intersection found.
	 */
	private Vertex getIntersection(EulerPath ep) {
		for(Vertex v: vertices) {
			if(GraphTools.containsVertex(ep.getVertices(), v)) {
				return v;
			}
		}
		return null;
	}
	
	/**
	 * Check if there are Bridges
	 * @return true if yes
	 */
	public boolean hasBridges() {
		for(Edge e:edges) {
			if(e.isBridge()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Count the number of Bridges
	 * @return Number of Bridges
	 */
	public int countBridges() {
		int c = 0;
		for(Edge e:edges) {
			if(e.isBridge()) {
				c++;
			}
		}
		return c;
	}
	
	/**
	 * Get all Edges, which are Bridges
	 * @return
	 */
	public Edge[] retrieveBridges() {
		Edge[] bridges = new Edge[0];
		for(Edge e:edges) {
			if(e.isBridge()) {
				bridges = GraphTools.push(bridges, e);
			}
		}
		return bridges;
	}
	
	/**
	 * Get the Euler Path
	 * @return Edges as Path
	 */
	public Edge[] getPath() {
		return path;
	}
	
	
	
	/**
	 * Get the vertices with an uneven degree
	 * @return vertices with an uneven degree
	 */
	public Vertex[] getUnevenDegreeVertices() {
		Vertex[] vA = new Vertex[getUnevenDegreeAmount()];
		int next = 0;
		for(int i = 0 ; i < vertices.length; i++) {
			if(GraphTools.countVertex(edges, vertices[i])%2 != 0) {
				vA[next] = vertices[i];
				next++;
			}
		}
		return vA;
	}	
	
	/**
	 * Get the amount of vertices with an uneven degree
	 * @return Vertex amount
	 */
	public int getUnevenDegreeAmount() {
		int unevenDegreeCounter = 0;
		for(int i = 0 ; i < vertices.length; i++) {
			if(GraphTools.countVertex(edges, vertices[i])%2 != 0) {
				unevenDegreeCounter++;
			}
		}
		return unevenDegreeCounter;
	}	
	
	public Vertex[] getVertices() {
		return vertices;
	}
	
	/**
	 * Compare to another EulerPath
	 * @param ep input Euler Path
	 * @return true if equal
	 */
	public boolean isEqual(EulerPath ep) {
		if(edges.length != ep.getEdges().length || !GraphTools.isEqual(edges,ep.getEdges())) {
			return false;
		}
		return true;
	}

	private Edge[] getEdges() {
		return edges;
	}
	
	/*
	 * -> End Getters
	 */
	
	/*
	 * Euler Construction
	 */
	
	private void refresh() {
		path = assembleEulerPath(edges,startFrom,goTo);
	}
	
	
	/**
	 * Reconnect the partitions to create a complete Euler Path
	 * @param partitions A series of partial Euler Paths
	 * @return A complete Euler Path 'stitched' together out of the partial Paths
	 */
	private EulerPath stitch(EulerPath[] partitions) {
		//Compare all partitions with each other and recombine them
		// Circles can be added anywhere and inserted into the path
		// Non circles need to match start and endpoints of their counterparts
		// The direction of the path is essential, so if start and endpoints don't match
		// one partition will be reversed
		// The stitched togheter partition will have a new endpoint
		// the overall startpoint needs to match with the complete euler
		EulerPath stitchedEuler = null;
		
		for(EulerPath ep: partitions) {
			if(startFrom.getName() == ep.startFrom.getName()) {
				stitchedEuler = ep;
				partitions = GraphTools.delete(partitions, ep);
				break;
			}else if(startFrom.getName() == ep.goTo.getName()) {
				ep.setGoTo(ep.getStartFrom());
				ep.setStartFrom(startFrom);
				ep.refresh();
				stitchedEuler = ep;
				partitions = GraphTools.delete(partitions, ep);
				break;
			}
		}
		
		while(partitions.length > 0) {
			for(EulerPath ep: partitions) {
				if(stitchedEuler.intersects(ep)) {
					Vertex v = stitchedEuler.getIntersection(ep);
					stitchedEuler = EulerPath.connect(stitchedEuler, ep, v);
					partitions = GraphTools.delete(partitions, ep);
					break;
				}
			}
		}
		return stitchedEuler;
	}
	
	/**
	 * Connect one Euler Path with another. This method is internally called by the stitch method
	 * @see stitch(EulerPath[] partitions)
	 * @param ep1 First partition
	 * @param ep2 Second partition
	 * @param intersection The 'seam'. Point where both partitions are connected
	 * @return combined Euler Path
	 */
	private static EulerPath connect(EulerPath ep1, EulerPath ep2, Vertex intersection) {
		/*
		 * Truth table:
		 * p = isCircle
		 * e1 = first partition
		 * e2 = second partition
		 * 
		 * e1	e2		result
		 * 1	1		throw all edges and vertices together and recalculate the Euler Path
		 * 1	0		Attach e1 to e2 at the intersection 
		 * 0	1		Attach e2 to e1 at the intersection
		 * 0	0		Attach End Point (e1/e2) to Start Point (e2/e1) ... check direction first		 * 
		 */
		Edge[] newPath = new Edge[0];
		Vertex[] newVertices;
		EulerPath newEuler = null;
		if(ep1.isCircle && ep2.isCircle) {
			Edge [] combinedPath = EulerPath.append(ep1.getPath(),ep2.getPath());
			newVertices = EulerPath.combine(ep1.getVertices(),ep2.getVertices());
			try {
				newEuler = new EulerPath(newVertices, combinedPath);	
			} catch (PathException e) {
				e.printStackTrace();
			}
						
		}
		if(ep1.isCircle && !ep2.isCircle) {
			ep1.setStartEndPoint(intersection);
			ep1.refresh();
			newEuler = ep2;
			newPath = EulerPath.insertInto(ep2.getPath(),ep1.getPath(),intersection);
			newEuler.setPath(newPath);
			newEuler.addVertex(ep1.getVertices());
			newEuler.addEdges(ep1.getEdges());
			newEuler.setCircle(false);
		}
		if(!ep1.isCircle && ep2.isCircle) {
			ep2.setStartEndPoint(intersection);
			ep2.refresh();
			newEuler = ep1;
			newPath = EulerPath.insertInto(ep1.getPath(),ep2.getPath(),intersection);
			newEuler.setPath(newPath);
			newEuler.addVertex(ep2.getVertices());
			newEuler.addEdges(ep2.getEdges());
			newEuler.setCircle(false);
		}
		if(!ep1.isCircle && !ep2.isCircle) {
			newEuler = ep1;
			if(ep1.getGoTo().getName() == ep2.getStartFrom().getName()) {
				newPath = EulerPath.append(ep1.getPath(),ep2.getPath());
				newEuler.setPath(newPath);
				newEuler.setGoTo(ep2.getGoTo());
				newEuler.addEdges(ep2.getEdges());
				newEuler.addVertex(ep2.getVertices());
				newEuler.setCircle(false);
			}else if(ep2.getGoTo().getName() == ep1.getStartFrom().getName()) {
				newPath = EulerPath.append(ep2.getPath(),ep1.getPath());
				newEuler.setPath(newPath);
				newEuler.setStartFrom(ep2.getStartFrom());
				newEuler.addEdges(ep2.getEdges());
				newEuler.addVertex(ep2.getVertices());
				newEuler.setCircle(false);
			}else {
				Vertex s = ep1.getGoTo();
				Vertex e = ep1.getStartFrom();
				ep1.setStartFrom(s);
				ep1.setGoTo(e);
				ep1.refresh();
				//newEuler.setStartFrom(s);
				//newEuler.setGoTo(ep2.getGoTo());
				newEuler = connect(ep1,ep2,intersection);
			}
		}
		return newEuler;
	}
	
	/**
	 * Partitioning is called internally when the Graph is a mixture of bridges, circles and non circles
	 * The graph will be split into the component types of bridges only and the remaining components when
	 * those bridges are removed. Each partition will have it's own EulerPath, which will then be stitched
	 * together later.
	 * @return An Array of partial Euler Paths
	 */
	private EulerPath[] partition() {
		// Split into bridges and the remaining edges
		// Create subgraphs, form components and create partitions out of these components
		// Then create Euler Paths for each partition
		
		//Split------
		//Bridges
		Edge[] bridges = retrieveBridges();
		Subgraph s = new Subgraph(bridges);
		s.calculateComponents(true);
		EulerPath[] connectedBridges = new EulerPath[s.getComponentAmount()];
		for(int i = 0; i < connectedBridges.length; i++) {
			Vertex[] tempV =s.getComponents()[i].getVertices();
			Edge[] tempE = s.getComponents()[i].getEdges();
			try {
				connectedBridges[i] = new EulerPath(tempV, tempE);
			} catch (PathException e) {
				System.out.println(e.getMessage());
			}
		}
		
		//Non Bridges
		Edge[] bridgeLessEdges = new Edge[edges.length - countBridges()];
		int idx = 0;
		for(Edge e:edges) {
			if(!e.isBridge()) {
				bridgeLessEdges[idx] = e;
				idx++;
			}
		}
		
		s = new Subgraph(bridgeLessEdges);
		s.calculateComponents(true);
		EulerPath[] bridgeLessEulers = new EulerPath[s.getComponentAmount()];
		for(int i = 0; i < bridgeLessEulers.length; i++) {
			Vertex[] tempV =s.getComponents()[i].getVertices();
			Edge[] tempE = s.getComponents()[i].getEdges();
			try {
				bridgeLessEulers[i] = new EulerPath(tempV, tempE);
			} catch (PathException e) {
				System.out.println(e.getMessage());
			}
		}
		
		//return partitions
		EulerPath[] partitions = new EulerPath[connectedBridges.length+bridgeLessEulers.length];
		idx = 0;
		for(EulerPath ep: connectedBridges) {
			partitions[idx] = ep;
			idx++;
		}
		for(EulerPath ep: bridgeLessEulers) {
			partitions[idx] = ep;
			idx++;
		}
		
		return partitions;		
	}
	
	/**
	 * Put edges together to form a path. All edges must be used to form an Euler Path.
	 * The direction of the path will be set with the start and end point
	 * @param edges Edges to form a path of
	 * @param startPoint Start from this position (On circles: startPoint = endPoint)
	 * @param endPoint End at this position (On circles: startPoint = endPoint)
	 * @return The finished path in form of an Edge Array
	 */
	private Edge[] assembleEulerPath(Edge[] edges, Vertex startPoint, Vertex endPoint) {
		// Every used edge of the input array will be moved to a new array
		Edge[] path = new Edge[edges.length];
		Vertex position = startPoint; // current position
		int pathlength = 0;
		int[][] edgesLeft = new int[vertices.length][2]; // remaining degree of every vertex
		Edge[] blacklist = new Edge[0]; // if any vertex has a degree of 1, the remaining edge will be blacklisted
		boolean cancel = false; 
		int cycle = 0;
		
		//initialize vertex degrees
		for(int i = 0; i < vertices.length; i++) {
			edgesLeft[i][0] = vertices[i].getName();
			edgesLeft[i][1] = GraphTools.countVertex(edges, vertices[i]);
		}
		
		while(edges.length > 0 && !cancel) {
			boolean edgeFound = false;
			
			//check degrees and update blacklist
			for(int i = 0; i < edgesLeft.length; i++) {
				if(edgesLeft[i][1] == 1) {
					Edge e = GraphTools.getEdge(edges,vertices[i]);
					if(!GraphTools.hasValue(blacklist, e)) {
						blacklist = GraphTools.push(blacklist, e);
					}
				}
			}
			
			//Check everything that is not blacklisted first and add the next edge to the path
			for(Edge e: edges) {
				if(e.hasVertex(position) && !GraphTools.hasValue(blacklist, e)) {
					path[pathlength] = e;
					position = e.getOppositeVertex(position);
					for(int[] arr:edgesLeft) {
						if(arr[0] == e.getVertices()[0].getName()) {
							arr[1]--;
						}
						if(arr[0] == e.getVertices()[1].getName()) {
							arr[1]--;
						}
					}
					edges = GraphTools.delete(edges, e);
					pathlength++;
					edgeFound = true;
					break;
				}
			}			
			
			//If nothing has been found in the edge array, check the blacklist and add the next edge to the path
			if(!edgeFound){
				for(Edge e: blacklist) {
					if(GraphTools.hasValue(edges, e) && e.hasVertex(position)) {
						edgeFound = true;
						path[pathlength] = e;
						position = e.getOppositeVertex(position);
						for(int[] arr:edgesLeft) {
							if(arr[0] == e.getVertices()[0].getName()) {
								arr[1]--;
							}
							if(arr[0] == e.getVertices()[1].getName()) {
								arr[1]--;
							}
						}
						edges = GraphTools.delete(edges, e);
						pathlength++;
						break;
					}
				}
				//If nothing can be found in the edge array and the blacklist,
				//the path is broken and needs to be recalculated
			}
			cycle++;
			
			//If the path fails, put the remaining "lost" edges to the front of the edge array and restart the process
			if(cycle > this.edges.length) {
				cancel = true;
				path = GraphTools.removeNullValues(path);
				Edge[] shuffle = new Edge[edges.length+path.length];
				int idx = 0;
				for(Edge e: edges) {
					shuffle[idx] = e;
					idx++;
				}
				for (Edge e: path) {
					shuffle[idx] = e;
					idx++;
				}
				try {
					EulerPath nEP = new EulerPath(vertices, shuffle);
					nEP.setStartFrom(startPoint);
					nEP.setGoTo(endPoint);
					nEP.refresh();
					path = nEP.getPath();
				} catch (PathException e1) {
					e1.printStackTrace();
				}
			}
		}
		return path;
	}
	
	/*
	 * -> End Euler Construction
	 */
	
	/*
	 * Manipulation and Dimension Changing Methods
	 */
	private void addEdges(Edge[] e) {
		edges = EulerPath.append(edges, e);
		
	}

	private void addVertex(Vertex[] vertices) {
		for(Vertex v: vertices) {
			if(!GraphTools.containsVertex(this.vertices, v)) {
				this.vertices = GraphTools.push(this.vertices, v);
			}
		}
		
	}
	
	/**
	 * Add a Path (Edge array) to the end of another path
	 * @param p1 First Path
	 * @param p2 Second Path
	 * @return p1 + p2
	 */
	private static Edge[] append(Edge[] p1, Edge[] p2) {
		Edge[] np = new Edge[p1.length+p2.length];
		int idx = 0;
		for(Edge e:p1) {
			np[idx] = e;
			idx++;
		}
		for(Edge e:p2) {
			np[idx] = e;
			idx++;
		}
		return np;
	}

	/**
	 * Insert a path into another path at the intersection Vertex
	 * @see GraphTools.insert(Edge[] target, Edge[] source, int afterIndex)
	 * @param target Target Path
	 * @param source Source Path
	 * @param intersection Intersecting Vertex
	 * @return combined Path
	 */
	private static Edge[] insertInto(Edge[] target, Edge[] source, Vertex intersection) {
		int[] intersectionIndex = GraphTools.indexOf(target,intersection);
		return GraphTools.insert(target,source,intersectionIndex[0]);		
	}

	/**
	 * Combines two Vertex Arrays
	 * @param v1 First vertex array
	 * @param v2 Second vertex array
	 * @return combined vertex array
	 */
	private static Vertex[] combine(Vertex[] v1, Vertex[] v2) {
		Vertex[] vNew = new Vertex[v1.length+v2.length];
		int idx = 0;
		for(Vertex v : v1) {
			vNew[idx] = v;
			idx++;
		}
		
		for(Vertex v : v2) {
			vNew[idx] = v;
			idx++;
		}
		return vNew;
	}
	
	/*
	 * -> Manipulation and Dimension Changing Methods
	 */
	
	/*
	 * Output
	 */
	
	public String toString() {
		String message = "{";
		int position = startFrom.getName();
		if(path[0].getVertices()[0].getName() == position) {
			position = path[0].getVertices()[1].getName();
			pathToString[0] = "["+startFrom.getName()+","+position +"]";
		}else {
			position = path[0].getVertices()[0].getName();
			pathToString[0] = "["+startFrom.getName()+","+ position+"]";
		}
		for(int i = 1; i < path.length; i++) {
			if(position == path[i].getVertices()[0].getName()) {
				position = path[i].getVertices()[1].getName();
				pathToString[i] = "["+path[i].getVertices()[0].getName()+","+position +"]";
			}else {
				position = path[i].getVertices()[0].getName();
				pathToString[i] = "["+path[i].getVertices()[1].getName()+","+position +"]";
			}
		}
		int idx = 0;
		for(String s: pathToString) {
			if(idx % 10 == 0 && idx != 0) {
				message += "\n";
			}
			if(idx == edges.length - 1) {
				message += s;
			}else {
				message += s+" , ";
			}
			
			idx++;
		}
		return message;
	}
	
	/*
	 * -> End Output
	 */
}
