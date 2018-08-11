/**
 * @author Stefan Schoeberl
 * @version 1.0
 * @modified 2018-08-09
 * 
 * @Class ArrayTools
 * A collection of static methods for Array manipulation
 */

package arraytools;

public class ArrayTools {

	/*
	 * Value Manipulation
	 */

	/**
	 * Fills an Array with a given Value
	 * @param inputArr Input Array
	 * @param value Value to fill the Array with
	 * @return
	 */
	public static int[] fill(int[] inputArr, int value) {
		if(isEmpty(inputArr)) {
			return inputArr;
		}
		
		int[] tempArr = new int[inputArr.length];
		
		for(int i = 0; i < inputArr.length; i++) {
			tempArr[i] = value;
		}
		
		return tempArr;
	}
	
	/**
	 * Search an array for a given value. Return the Index of the first Match.
	 * @param arr Array to search in
	 * @param val Value to search for
	 * @return Index of first find or -1 on nothing found
	 */
	public static int indexOf(int[] arr, int val) {
		for(int i = 0; i < arr.length; i++) {
			if(arr[i] == val) {
				return i;
			}
		}
		return -1;
	}

	/*
	 * -> End Value Manipulation
	 */

	/* 
	 * Control Mechanisms
	 */

	/**
	 * Checks if an Array is NULL or empty
	 * 
	 * @param inputArr
	 * @return boolean
	 */
	public static boolean isEmpty(int[] inputArr) {
		if (inputArr == null || inputArr.length == 0) {
			return true;
		}
		return false;
	}	
	
	/**
	 * Searches the Array for a value and provides a boolean Array with the
	 * findings
	 * @param inputArr Array to search
	 * @param v Value to search for
	 * @return Flagged Array, true for Value found, false for Value not found
	 */
	public static boolean[] findAllDuplicates(int[] inputArr, int v) {
		if (isEmpty(inputArr)) {
			return new boolean[0];
		}
		boolean[] duplications = new boolean[inputArr.length];
		
		for(int i = 0; i < inputArr.length; i++) {
			if (inputArr[i] == v) {
				duplications[i] = true;
			}else {
				duplications[i] = false;
			}
		}
		return duplications;
	}
	
	/**
	 * Checks if a value already exists in a given array
	 * @param arr Array to check
	 * @param val The value to search
	 * @return true if the value was found in the array
	 */
	public static boolean contains(int[] arr, int val) {
		for(int i:arr) {
			if(i == val) {
				return true;
			}
		}
		return false;
	}

	/*
	 * -> End Control Mechanisms
	 */
	
	/*
	 * Dimension changing methods
	 */
	
	/**
	 * Increase the size of an array by 1
	 * @param arr Input Array
	 * @return Input Array with length + 1
	 */
	public static int[] expand(int[] arr) {
		int[] ta = new int[arr.length + 1];
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
	public static int[] push(int[] arr, int v) {
		int[] ta = expand(arr);
		ta[arr.length] = v;
		return ta;
	}

	/**
	 * Delete Value at given Index in an Array. Reduce the size of the Array, shifting indizes back to front.
	 * @param arr Target Array
	 * @param index 
	 * @return 
	 */
	public static int[] delete(int[] arr, int index) {
		int[] ta = new int[arr.length-1];
		int pos = 0;
		for ( int i = 0; i < arr.length; i++) {
			if(i != index) {
				ta[pos] = arr[i];
				pos++;
			}
		}
		return ta;
	}
	
	/*
	 * -> End Dimension changing methods
	 */
	
	/*
	 * Calculations
	 */
	
	/**
	 * Return the sum of the input Array
	 * @param inputArr
	 * @return Sum
	 */
	public static int sum(int[] inputArr) {
		if(isEmpty(inputArr)) {
			return 0;
		}
		
		int sum = 0;
		for(int i = 0; i < inputArr.length; i++) {
			sum += inputArr[i];
		}
		
		return sum;
	}
	
	/**
	 * Return the sum of the input Array between two indizes
	 * @param fromIndex Starting Index inclusive
	 * @param toIndex Ending index exclusive
	 * @param inputArr Target Array
	 * @return Sum
	 */
	public static int sum(int[] inputArr, int fromIndex, int toIndex) {
		if(isEmpty(inputArr)) {
			return 0;
		}
		
		int sum = 0;
		for(int i = fromIndex; i < toIndex; i++) {
			sum += inputArr[i];
		}
		
		return sum;
	}
	
	/**
	 * Find the highest value inside an Array
	 * @param inputArr
	 * @return
	 */
	public static int maxOf(int[] inputArr) {
		if(isEmpty(inputArr)) {
			return 0;
		}
		
		int max = Integer.MIN_VALUE;
		
		for (int i = 0; i < inputArr.length; i++) {
			if(inputArr[i] > max) {
				max = inputArr[i];
			}
		}
		
		return max;
	}
	
	/**
	 * Find the lowest value inside an Array
	 * @param inputArr
	 * @return
	 */
	public static int minOf(int[] inputArr) {
		if(isEmpty(inputArr)) {
			return 0;
		}
		
		int min = Integer.MAX_VALUE;
		
		for (int i = 0; i < inputArr.length; i++) {
			if(inputArr[i] < min) {
				min = inputArr[i];
			}
		}
		
		return min;
	}
	
	/*
	 * -> End Calculations
	 */

}
