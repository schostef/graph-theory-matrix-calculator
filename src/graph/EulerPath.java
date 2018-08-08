package graph;
import arraytools.ArrayTools;
import arraytools.GraphTools;
public class EulerPath {

	private Vertex[] vertices;
	private Edge[] edges;
	private Vertex startFrom;
	private Vertex goTo;
	private Edge[] path;
	private String[] pathToString;
	public boolean isCircle = false;
	
	public EulerPath (Vertex[] vertices) throws PathException{
		if(vertices != null && vertices.length != 0) {
			buildPath(vertices);
		}
			
	}
	
	public EulerPath (Vertex[] vertices, Edge[] edges) throws PathException {
		this.vertices = vertices;
		this.edges = edges;
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
		
		if(getUnevenDegreeAmount() == 0) {
			isCircle = true;
			goTo = startFrom = vertices[0];
			path = assembleOpenEulerPath(edges,startFrom,startFrom);
		}else {
			isCircle = false;
			Vertex[] unevenDegreeVertices = getUnevenDegreeVertices();
			startFrom = unevenDegreeVertices[0];
			goTo = unevenDegreeVertices[1];
			if(!hasBridges()) {
				path = assembleOpenEulerPath(edges,startFrom,goTo);
			}else if(countBridges() == edges.length){
				System.out.println("\nVollständige Brücken. Verbinde\n===============================\n\n");
				path = assembleOpenEulerPath(edges,startFrom,goTo);
				//EulerPath[] partitions = partition();
				//path = EulerPath.stitch(partitions);
			}else {
				System.out.println("\nBrücken vorhanden. Partitioniere\n===============================\n\n");
				EulerPath[] partitions = partition();
				path = stitch(partitions);
			}
		}
		
	}
	
	private void setStartEndPoint(Vertex v) {
		goTo = startFrom = v;
	}
	
	private void setGoTo(Vertex e) {
		goTo = e;
		
	}

	private void setStartFrom(Vertex s) {
		startFrom = s;
		
	}
	
	private Vertex getGoTo() {
		return goTo;
	}
	
	private Vertex getStartFrom() {
		return startFrom;
	}
	
	private void refresh() {
		path = assembleOpenEulerPath(edges,startFrom,goTo);
	}
	
	
	
	private Edge[] stitch(EulerPath[] partitions) {
		EulerPath stitchedEuler = null;
		int idx = 0;
		// Set the first startpoint and partition
		
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
		return stitchedEuler.getPath();
	}
	
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

	private boolean isCircle() {
		return isCircle;
	}
	
	private void setCircle(boolean b) {
		isCircle = b;
	}
	
	private void setPath(Edge[] newPath) {
		path = newPath;
	}

	private static EulerPath connect(EulerPath ep1, EulerPath ep2, Vertex intersection) {
		Edge[] newPath = new Edge[0];
		Vertex[] newVertices;
		EulerPath newEuler = null;
		if(ep1.isCircle && ep2.isCircle) {
			Edge [] combinedPath = EulerPath.combine(ep1.getPath(),ep2.getPath());
			newVertices = EulerPath.combine(ep1.getVertices(),ep2.getVertices());
			try {
				newEuler = new EulerPath(newVertices, combinedPath);	
			} catch (PathException e) {
				// TODO Auto-generated catch block
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
				newEuler = connect(ep1,ep2,intersection);
			}
		}
		return newEuler;
	}
	
	

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

	private static Edge[] insertInto(Edge[] target, Edge[] source, Vertex intersection) {
		int[] intersectionIndex = GraphTools.indexOf(target,intersection);
		return GraphTools.insert(target,source,intersectionIndex[0]);		
	}

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

	private static Edge[] combine(Edge[] e1, Edge[] e2) {
		Edge[] eNew = new Edge[e1.length+e2.length];
		int idx = 0;
		for(Edge e : e1) {
			eNew[idx] = e;
			idx++;
		}
		
		for(Edge e : e2) {
			eNew[idx] = e;
			idx++;
		}
		return eNew;
	}

	private boolean intersects(EulerPath ep) {
		for(Vertex v: vertices) {
			if(GraphTools.containsVertex(ep.getVertices(), v)) {
				return true;
			}
		}
		return false;
	}
	
	private Vertex getIntersection(EulerPath ep) {
		for(Vertex v: vertices) {
			if(GraphTools.containsVertex(ep.getVertices(), v)) {
				return v;
			}
		}
		return null;
	}

	private EulerPath[] partition() {
		//Alle Brücken zwischenspeichern
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
		
		System.out.println(connectedBridges.length + " Brücken Wege gefunden: \n\n");
		for(EulerPath ep: connectedBridges) {
			System.out.println("\n##################\n "+ep);
		}
		
		//Brücken aus der Gesamtmenge entfernen und aus den Überbleibseln Euler entwickeln
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
		
		System.out.println(connectedBridges.length + " Brückenlose Wege gefunden: \n\n");
		for(EulerPath ep: bridgeLessEulers) {
			System.out.println("\n##################\n "+ep);
		}
		
		//Alle Partitionen in ein Gesamtarray zusammenfügen und returnen
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

	public boolean hasBridges() {
		for(Edge e:edges) {
			if(e.isBridge()) {
				return true;
			}
		}
		return false;
	}
	
	public int countBridges() {
		int c = 0;
		for(Edge e:edges) {
			if(e.isBridge()) {
				c++;
			}
		}
		return c;
	}
	
	public Edge[] retrieveBridges() {
		Edge[] bridges = new Edge[0];
		for(Edge e:edges) {
			if(e.isBridge()) {
				bridges = GraphTools.push(bridges, e);
			}
		}
		return bridges;
	}
	
	public Edge[] getPath() {
		return path;
	}
	
	private Edge[] assembleOpenEulerPath(Edge[] edges, Vertex startPoint, Vertex endPoint) {
		System.out.println("\n\nBEGINNE EULER: \n ============================================= \n\n");
		System.out.println("Setze Startpunkt auf: "+ startPoint + " und Endpunkt auf: " +endPoint);
		Edge[] path = new Edge[edges.length];
		Vertex position = startPoint;
		int pathlength = 0;
		int[][] edgesLeft = new int[vertices.length][2]; //Verbleibende Kanten für [0] = startpunkt und [1] = endpunkt
		Edge[] blacklist = new Edge[0];
		boolean cancel = false;
		int cycle = 0;
		System.out.println("Erstelle Euler Pfad aus Kantensumme: ");
		for(Edge e: edges) {
			System.out.print(e + ",");
		}
		System.out.println("\n");
		for(int i = 0; i < vertices.length; i++) {
			edgesLeft[i][0] = vertices[i].getName();
			edgesLeft[i][1] = GraphTools.countVertex(edges, vertices[i]);
		}
		while(edges.length > 0 && !cancel) {
			boolean edgeFound = false;
			for(int i = 0; i < edgesLeft.length; i++) {
				if(edgesLeft[i][1] == 1) {
					Edge e = GraphTools.getEdge(edges,vertices[i]);
					if(!GraphTools.hasValue(blacklist, e)) {
						blacklist = GraphTools.push(blacklist, e);
						System.out.println("Schreibe Kante "+e+" in die Blacklist");
					}
				}
			}
			for(Edge e: edges) {
				if(e.hasVertex(position) && !GraphTools.hasValue(blacklist, e)) {
					System.out.println("Position " + position + " in Kante " + e +" gefunden.");
					path[pathlength] = e;
					System.out.println("Füge "+e+" zu Pfad hinzu.\n");
					position = e.getOppositeVertex(position);
					for(int[] arr:edgesLeft) {
						if(arr[0] == e.getVertices()[0].getName()) {
							arr[1]--;
						}
						if(arr[0] == e.getVertices()[1].getName()) {
							arr[1]--;
						}
					}
					System.out.println("Neue Position: "+position);
					System.out.println("Lösche " + e + "aus: ");
					for(Edge f: edges) {
						System.out.print(f+",");
					}
					edges = GraphTools.delete(edges, e);
					pathlength++;
					System.out.println("\nPfadlänge "+pathlength);
					edgeFound = true;
					break;
				}
			}			
			
			if(!edgeFound){
				System.out.println("Alle Kanten bearbeitet, bearbeite nun Blacklist");
				for(Edge e: blacklist) {
					if(GraphTools.hasValue(edges, e) && e.hasVertex(position)) {
						System.out.println("Füge "+e+" zu Pfad hinzu.");
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
						System.out.println("Neue Position: "+position);
						System.out.println("Lösche " + e + "aus: ");
						for(Edge f: edges) {
							System.out.print(f+",");
						}
						edges = GraphTools.delete(edges, e);
						pathlength++;
						System.out.println("Pfadlänge "+pathlength);
						break;
					}
				}
			}
			cycle++;
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
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.print("Euler Fehlgeschlagen! \n Pfad: \n");
				for (Edge e: path) {
					System.out.print(e + ",");
				}
				System.out.print("\n Verbleibende Kanten: \n");
				for(Edge e: edges) {
					System.out.print(e + ",");
				}
			}
		}
		return path;
	}
	
	private Edge[] assembleClosedEulerPath(Edge[] edges, Vertex startFrom) {
		Edge[] path = new Edge[edges.length];
		Vertex position = startFrom;
		int pathlength = 0;
		while(edges.length > 0) {
			for(Edge e: edges) {
				if(e.hasVertex(position)) {
					path[pathlength] = e;
					position = e.getOppositeVertex(position);
					edges = GraphTools.delete(edges, e);
					pathlength++;
					break;
				}
			}
		}
		
		return path;
		
		
	}
	
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
	
	public void buildEulerPath() throws EulerNotPossibleException {
		if(!isCircle && getUnevenDegreeAmount() > 2) {
			throw new EulerNotPossibleException("Euler nicht möglich, mehr als 2 Kanten mit ungeraden Grad vorhanden");
		}
		
		Vertex currentPosition = startFrom;
		
		stepForward()
		stepBack()
		switchLane()
		
		//Something like this....
		try {
			stepForward() //loop this
		}catch (PathStuckException ex){
			stepBack()
			try {
				switchLane()
			}catch (NoAlternativeRouteAvaibleException ex) {
				stepBack() /// and so on
			}
		}
	}
	
	public int getUnevenDegreeAmount() {
		int unevenDegreeCounter = 0;
		for(int i = 0 ; i < vertices.length; i++) {
			if(GraphTools.countVertex(edges, vertices[i])%2 != 0) {
				unevenDegreeCounter++;
			}
		}
		return unevenDegreeCounter;
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

	public boolean isEqual(EulerPath ep) {
		if(edges.length != ep.getEdges().length || !GraphTools.isEqual(edges,ep.getEdges())) {
			return false;
		}
		return true;
	}

	private Edge[] getEdges() {
		return edges;
	}

}
