/**
 * @author Stefan Schoeberl
 * @version 0.1
 * @modified 2018-03-23
 * 
 * @Class GraphTools
 * Miscellaneous Array Methods related to the graph package
 */

package arraytools;

import graph.Edge;
import graph.EulerPath;
import graph.Path;
import graph.Vertex;
import matrix.Vector;

public class GraphTools {
	/**
	 * Increase the size of an array by 1
	 * @param arr Input Array
	 * @return Input Array with length + 1
	 */
	public static Vertex[] expand(Vertex[] arr) {
		Vertex[] ta = new Vertex[arr.length + 1];
		for (int i = 0; i < arr.length; i++) {
			ta[i] = arr[i];
		}
		return ta;
	}
	
	/**
	 * Increase the size of an array by 1
	 * Add the supplied value to the last index
	 * @param arr Input Array
	 * @param v The value to add to the last index
	 * @return Output Array with length + 1 and value on last index
	 */
	public static Vertex[] push(Vertex[] arr, Vertex v) {
		Vertex[] ta = expand(arr);
		ta[arr.length] = v;
		return ta;
	}	

	public static Vertex[] delete(Vertex[] arr, Vertex v) {
		Vertex[] ta = new Vertex[arr.length-1];
		int deleteIndex = Integer.MIN_VALUE;
		//Suche v
		for ( int i = 0; i < arr.length || deleteIndex < 0; i++) {
			if(arr[i].getName() == v.getName()) {
				deleteIndex = i;
			}
		}
		
		int stepper = 0;
		int position = 0;
		while(stepper < ta.length) {
			if(stepper == deleteIndex) {
				position++;
			}
			ta[stepper] = arr[position];
			stepper++;
			position++;
		}
		
		return ta;
		//Speichere position
		
		//Schreibe
		//Wenn i = position überspringe Zyklus
	}
	
	public static boolean containsVertex(Vertex[] arr, Vertex value) {
		for (int i = 0; i < arr.length ; i++) {
			if(arr[i].getName() == value.getName()) {
				return true;
			}
		}
		return false;
	}
	
	public static int countVertex(Edge[] arr, Vertex vertex) {
		int counter = 0;
		for (int i = 0; i < arr.length; i++) {
			if(arr[i].hasVertex(vertex)) {
				counter++;
			}
		}
		return counter;
	}
	
	/**
	 * Increase the size of an array by 1
	 * @param arr Input Array
	 * @return Input Array with length + 1
	 */
	public static Edge[] expand(Edge[] arr) {
		Edge[] ta = new Edge[arr.length + 1];
		for (int i = 0; i < arr.length; i++) {
			ta[i] = arr[i];
		}
		return ta;
	}
	
	/**
	 * Increase the size of an array by 1
	 * Add the supplied value to the last index
	 * @param arr Input Array
	 * @param v The value to add to the last index
	 * @return Output Array with length + 1 and value on last index
	 */
	public static Edge[] push(Edge[] arr, Edge e) {
		Edge[] ta = expand(arr);
		ta[arr.length] = e;
		return ta;
	}
	
	public static Edge[] delete(Edge[] arr, Edge e) {
		int idx = 0;
		Edge[] ta = new Edge[arr.length-1];
		for(int i = 0; i < arr.length; i++) {
			if(!arr[i].isEqual(e)) {
				ta[idx] = arr[i];
				idx++;
			}
		}
		
		return ta;
	}
	
	public static boolean hasValue(Edge[] arr, Edge value) {
		for (int i = 0; i < arr.length ; i++) {
			if(arr[i].isEqual(value)) {
				return true;
			}
		}
		return false;
	}
	
	public static Edge getEdge(Edge[] arr, Vertex v) {
		for(Edge e: arr) {
			if(e.hasVertex(v)) {
				return e;
			}
		}
		return null;
	}
	
	
	public static Vector[] expand(Vector[] arr) {
		Vector[] ta = new Vector[arr.length + 1];
		for (int i = 0; i < arr.length; i++) {
			ta[i] = arr[i];
		}
		return ta;
	}
	

	public static Vector[] push(Vector[] arr, Vector v) {
		Vector[] ta = expand(arr);
		ta[arr.length] = v;
		return ta;
	}

	public static Path[] push(Path[] arr, Path path) {
		Path[] ta = expand(arr);
		ta[arr.length] = path;
		return ta;
	}

	private static Path[] expand(Path[] arr) {
		Path[] ta = new Path[arr.length + 1];
		for (int i = 0; i < arr.length; i++) {
			ta[i] = arr[i];
		}
		return ta;
	}

	public static Vector[] removeDuplicates(Vector[] v) {
			Vector[] nv = new Vector[1];
			nv[0] = v[0];
			for(int i = 1; i < v.length; i++) {
				boolean entryFound = false;
				for(int j = 0; j < nv.length; j++) {
					if(nv[j].isEqual(v[i])) {
						entryFound = true;
					}
				}
				if(!entryFound) {
					nv = push(nv, v[i]);
				}
			}
			return nv;
	}

	public static int[] indexOf(Edge[] p1, Vertex intersection) {
		int[] index = new int[0];
		for(int i = 0; i < p1.length; i++) {
			if(p1[i].hasVertex(intersection)) {
				index = ArrayTools.push(index, i);
			}
		}
		return index;
	}

	public static Edge[] insert(Edge[] target, Edge[] source, int afterIndex) {
		Edge[] result = new Edge[target.length+source.length];
		int idx = 0;
		if(afterIndex == 0) {
			for(Edge e: source) {
				result[idx] = e;
				idx++;
			}
			for(Edge e: target) {
				result[idx] = e;
				idx++;
			}
		}else if(afterIndex == target.length-1) {
			for(Edge e: target) {
				result[idx] = e;
				idx++;
			}
			
			for(Edge e: source) {
				result[idx] = e;
				idx++;
			}
			
		}else {
			for(int i = 0; i <= afterIndex; i++) {
				result[idx] = target[i];
				idx++;
			}
			for(Edge e: source) {
				result[idx] = e;
				idx++;
			}
			for(int i = afterIndex+1; i < target.length; i++) {
				result[idx] = target[i];
				idx++;
			}
		}
		
		return result;
	}

	public static EulerPath[] delete(EulerPath[] arr, EulerPath ep) {
		EulerPath[] result = new EulerPath[arr.length-1];
		int idx = 0;
		for(EulerPath e: arr) {
			if(!e.isEqual(ep)) {
				result[idx] = e;
				idx++;
			}
		}
		return result;
	}

	public static boolean isEqual(Edge[] e1, Edge[] e2) {
		if(e1.length != e2.length) {
			return false;
		}
		for(int i = 0; i < e1.length; i++) {
			if(!e1[i].isEqual(e2[i])) {
				return false;
			}
		}
		return true;
	}

	public static Edge[] removeNullValues(Edge[] arr) {
		Edge[] an = new Edge[0];
		for(Edge e: arr) {
			if(e != null) {
				an = GraphTools.push(an, e);
			}
		}
		
		return an;
	}

	

	

	
}
