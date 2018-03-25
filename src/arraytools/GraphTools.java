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
import graph.Vertex;

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
	public static Edge[] push(Edge[] arr, Edge v) {
		Edge[] ta = expand(arr);
		ta[arr.length] = v;
		return ta;
	}
}
