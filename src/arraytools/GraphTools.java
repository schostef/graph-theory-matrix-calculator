/**
 * @author Stefan Schoeberl
 * @version 1.0
 * @modified 2018-08-09
 * 
 * @Class GraphTools
 * Miscellaneous Array Methods related to the graph package
 */

package arraytools;

import graph.Edge;
import graph.EulerPath;
import graph.Vertex;
import matrix.Vector;

public class GraphTools {
	
	/*
	 * Dimension changing methods
	 */
	
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

	/**
	 * Remove a vertex from an Array.
	 * This does not call any deletion of the Vertex itself.
	 * @param arr Input Array
	 * @param v Vertex to delete
	 * @return Array without v
	 */
	public static Vertex[] delete(Vertex[] arr, Vertex v) {
		Vertex[] ta = new Vertex[arr.length-1];
		int deleteIndex = Integer.MIN_VALUE;
		
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
	
	/**
	 * Remove an edge out of an array of edges
	 * Does not call for complete deletion of the provided edge
	 * @param arr Edge array
	 * @param e Edge to remove
	 * @return Array without Edge e
	 */
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
	
	/**
	 * Increases the size of an Array by 1. Last value being set to null.
	 * @param arr Array to expand
	 * @return arr +1 in size
	 */
	public static Vector[] expand(Vector[] arr) {
		Vector[] ta = new Vector[arr.length + 1];
		for (int i = 0; i < arr.length; i++) {
			ta[i] = arr[i];
		}
		return ta;
	}
	
	/**
	 * Increase an Array's size by 1. Store the provided Value into the last index
	 * @param arr Array to expand
	 * @param v Value to store at the last index
	 * @return arr +1 with v at last index
	 */
	public static Vector[] push(Vector[] arr, Vector v) {
		Vector[] ta = expand(arr);
		ta[arr.length] = v;
		return ta;
	}

	/**
	 * Scan an array for any duplicate values and remove them.
	 * Creating a smaller array with only unique values
	 * @param v Array to scan
	 * @return Shrinked Array with unique Values
	 */
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
	
	/**
	 * Insert one Edge Array into another Edge Array using afterIndex as position (inclusive)
	 * @param target Array to insert into
	 * @param source Source Array inserted into the target Array
	 * @param afterIndex The inserting position.
	 * For afterIndex = 0 the source array will be inserted in front of the target array
	 * For afterIndex = target.size-1 the source array will be appended at the end of target
	 * For 1 < afterIndex < target.size-1 the source array will be inserted after afterIndex
	 * @return The combined array of target + source
	 */
	public static Edge[] insert(Edge[] target, Edge[] source, int afterIndex) {
		Edge[] result = new Edge[target.length+source.length];
		int idx = 0;
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
	
		
		return result;
	}

	/**
	 * Remove a provided EulerPath out of an EulerPath Array.
	 * This does not fully delete the provided EulerPath
	 * @param arr Array to scan
	 * @param ep EulerPath to remove
	 * @return reduced Array without ep
	 */
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
	
	/**
	 * Remove all NULL values out of an array
	 * @param arr
	 * @return Array without NULL values
	 */
	public static Edge[] removeNullValues(Edge[] arr) {
		Edge[] an = new Edge[0];
		for(Edge e: arr) {
			if(e != null) {
				an = GraphTools.push(an, e);
			}
		}
		
		return an;
	}
	
	
	/*
	 * -> End Dimension changing methods
	 */
	
	/*
	 * Control Mechanisms
	 */
	
	/**
	 * Find a Vertex inside an Array.
	 * @param arr
	 * @param value
	 * @return true on Vertex found
	 */
	public static boolean containsVertex(Vertex[] arr, Vertex value) {
		for (int i = 0; i < arr.length ; i++) {
			if(arr[i].getName() == value.getName()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if an Edge is inside of an array of Edges
	 * @param arr Edge array
	 * @param value Edge to search for
	 * @return true on Edge found
	 */
	public static boolean hasValue(Edge[] arr, Edge value) {
		for (int i = 0; i < arr.length ; i++) {
			if(arr[i].isEqual(value)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Compare two edge Arrays
	 * @param e1
	 * @param e2
	 * @return true if equal
	 */
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
	
	/*
	 * -> End Control Mechanisms
	 */
	
	/*
	 * Calculations
	 */
	
	/**
	 * How often does a given vertex appear in an Array of Edges
	 * @param arr Edge array
	 * @param vertex Vertex to count
	 * @return counted Vertex
	 */
	public static int countVertex(Edge[] arr, Vertex vertex) {
		int counter = 0;
		for (int i = 0; i < arr.length; i++) {
			if(arr[i].hasVertex(vertex)) {
				counter++;
			}
		}
		return counter;
	}
	
	/*
	 * -> End Calculations
	 */
	
	/*
	 * Getters
	 */
	
	/**
	 * Get the first edge in an Array, that contains a given Vertex
	 * @param arr Edge array to scan
	 * @param v Vertex to search for
	 * @return First Edge containing Vertex v
	 */
	public static Edge getEdge(Edge[] arr, Vertex v) {
		for(Edge e: arr) {
			if(e.hasVertex(v)) {
				return e;
			}
		}
		return null;
	}
	
	

	/**
	 * Search an Array for all occurrences of a given Vertex
	 * The matches are returned as an Array of indices
	 * @param p1 Edge Array to scan
	 * @param intersection Vertex to look for
	 * @return Array of matching indices in p1
	 */
	public static int[] indexOf(Edge[] p1, Vertex intersection) {
		int[] index = new int[0];
		for(int i = 0; i < p1.length; i++) {
			if(p1[i].hasVertex(intersection)) {
				index = ArrayTools.push(index, i);
			}
		}
		return index;
	}
	
	/*
	 * -> End Getters
	 */
	
}
