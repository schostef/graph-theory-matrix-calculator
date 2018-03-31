/**
 * @author Stefan Schoeberl
 * @version 0.1
 * @modified 2018-03-23
 * 
 * @Class ArrayTools
 * A collection of static methods for Array manipulation
 */

package arraytools;

public class ArrayTools {

	/*
	 * ******************************************************* 
	 * SORTING
	 * *******************************************************
	 */

	/**
	 * Sorting integer values. Algorithm consists of a mixture of the Hoare
	 * Fragmentation and Bubble Sort
	 * 
	 * @param inputArray Array to sort
	 * @return Sorted Array
	 */
	public static int[] sort(int[] inputArray) {
		if (isEmpty(inputArray)) {
			return inputArray;
		}

		return quickSort(inputArray, 0, inputArray.length - 1);
	}
	
	/**
	 * Sorting characters by their numeric values. Algorithm consists of a mixture of the Hoare
	 * Fragmentation and Bubble Sort
	 * 
	 * @param inputArray Array to sort
	 * @return Sorted Array
	 */
	public static char[] sort(char[] inputArray) {
		if(isEmpty(inputArray)) {
			return inputArray;
		}
		
		//Typecast the array to int, sort it and cast it back to char
		int[] array = toInt(inputArray);
		return toChar(quickSort(array, 0, array.length - 1));
		
		
	}

	/**
	 * Quicksort using the Hoare algorithm with the exception that the lower and
	 * higher index will always cover the full Array.
	 * 
	 * @param inputArray
	 * @param indexLow
	 * @param indexHigh
	 * @return
	 */
	private static int[] quickSort(int[] inputArray, int indexLow, int indexHigh) {
		int i = indexLow;
		int j = indexHigh;
		int[] array = inputArray;
		int pivot = array[indexLow + (indexHigh - indexLow) / 2]; // Pivot is chosen as the middle Index

		// The values on the left and right side of the pivot are compared
		// If two values don't match the pivots perspective, they will be swapped
		while (i < j) {

			while (array[i] <= pivot) {
				i++;
			}

			while (array[j] > pivot) {
				j--;
			}

			if (i < j) {
				array = swapValues(array, i, j);
			}
		}

		// When from the pivot's perspective all values are sorted a bubble sort
		// algorithm will take over
		if (!isSorted(array)) {
			array = bubbleSort(array);
			// recursively run quicksort again
			quickSort(array, 0, array.length - 1);
		}

		return array;
	}

	/**
	 * Simple one time bubble sort. Compare two neighboring values to each other. If
	 * they are not sorted, swap them.
	 * 
	 * @param inputArr
	 * @return
	 */
	private static int[] bubbleSort(int[] inputArr) {
		if (isEmpty(inputArr)) {
			return inputArr;
		}

		int[] array = inputArr;

		for (int i = 1; i < array.length; i++) {
			if (array[i - 1] > array[i]) {
				array = swapValues(array, i - 1, i);
			}
		}

		return array;

	}

	/*
	 * *******************************************************************
	 */

	/*
	 * ****************************************************************** 
	 * Value Manipulation
	 * ******************************************************************
	 */

	/**
	 * Swap two values providing their indizes.
	 * 
	 * @param arr Input Array
	 * @param idxL First Index
	 * @param idxH Second Index
	 * @return Array
	 */
	public static int[] swapValues(int[] arr, int idxL, int idxH) {
		if (isEmpty(arr) || idxL < 0 || idxL > arr.length || idxH < 0 || idxH > arr.length) {
			return arr;
		}
		int temp = arr[idxL];
		arr[idxL] = arr[idxH];
		arr[idxH] = temp;
		return arr;

	}
	
	/**
	 * Typecast an array from char to int
	 * @param inputArr Type char input array
	 * @return Type int output array
	 */
	public static int[] toInt(char[] inputArr) {
		if(isEmpty(inputArr)) {
			return new int[0];
		}
		
		int[] castArr = new int[inputArr.length];
		for (int i = 0; i < castArr.length; i++) {
			castArr[i] = (int)inputArr[i];
		}
		
		return castArr;
		
	}
	
	/**
	 * Typecast an array from int to char
	 * @param inputArr Type int input array
	 * @return Type char output array
	 */
	public static char[] toChar(int[] inputArr) {
		if(isEmpty(inputArr)) {
			return new char[0];
		}
		
		char[] castArr = new char[inputArr.length];
		for (int i = 0; i < castArr.length; i++) {
			castArr[i] = (char)inputArr[i];
		}
		
		return castArr;
	}
	
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

	/*
	 * *******************************************************************
	 */

	/*
	 * ******************************************************************* 
	 * Control Mechanisms
	 * *******************************************************************
	 */

	/**
	 * Checks if an Array is sorted ascending
	 * 
	 * @param inputArr
	 * @return boolean Array sorted
	 */
	public static boolean isSorted(int[] inputArr) {
		if (isEmpty(inputArr)) {
			return false;
		}

		for (int i = 1; i < inputArr.length; i++) {
			if (inputArr[i - 1] > inputArr[i]) {
				return false;
			}
		}

		return true;
	}

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
	 * Checks if an Array is NULL or empty
	 * 
	 * @param inputArr
	 * @return boolean
	 */
	public static boolean isEmpty(char[] inputArr) {
		if (inputArr == null || inputArr.length == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if a value already exists in a given array
	 * @param inputArr Array to check
	 * @param v The value to search
	 * @return true if the value was found in the array
	 */
	public static boolean isDuplicate(int[] inputArr, int v) {
		if(isEmpty(inputArr)) {
			return false;
		}
		boolean duplicationFound = false;
		for (int i = 0; i < inputArr.length; i++) {
			if(inputArr[i] == v) {
				duplicationFound = true;
			}
		}
		return duplicationFound;
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

	/*
	 * *******************************************************************
	 */
	
	/*
	 * *******************************************************************
	 * Dimension changing methods
	 * *******************************************************************
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
	 * Like push, but sort the array afterwards
	 * @see push
	 * @param arr
	 * @param v
	 * @return
	 */
	public static int[] pushAndSort(int[] arr, int v) {
		return sort(push(arr, v));
	}
	
	/*
	 * *******************************************************************
	 */
	
	/*
	 * *******************************************************************
	 * Calculations
	 * *******************************************************************
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
	 * *******************************************************************
	 */

	/*
	 * *******************************************************************
	 * Output Methods
	 * *******************************************************************
	 */
	
	/**
	 * Print array to console
	 * @param arr
	 */
	public static void printOnConsole(int[] arr) {
		if (isEmpty(arr)) {
			System.out.println("");
		} else {
			for (int i = 0; i < arr.length; i++) {
				System.out.print(arr[i] + ", ");
			}
			System.out.println();
		}
	}
	
	/**
	 * Print array to console
	 * @param arr
	 */
	public static void printOnConsole(char[] arr) {
		if (isEmpty(arr)) {
			System.out.println("");
		} else {
			for (int i = 0; i < arr.length; i++) {
				System.out.print(arr[i] + ", ");
			}
			System.out.println();
		}
	}

}
