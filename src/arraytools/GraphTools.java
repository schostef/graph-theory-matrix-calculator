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
	
	public static boolean hasValue(Vertex[] arr, Vertex value) {
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
		Edge[] ta = new Edge[arr.length-1];
		int deleteIndex = Integer.MIN_VALUE;
		//Suche e
		for ( int i = 0; i < arr.length || deleteIndex < 0; i++) {
			if(arr[i].getID() == e.getID()) {
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
	
	public static boolean hasValue(Edge[] arr, Edge value) {
		for (int i = 0; i < arr.length ; i++) {
			if(arr[i].getID() == value.getID()) {
				return true;
			}
		}
		return false;
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

	

	
}
