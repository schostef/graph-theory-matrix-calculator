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
	 * @param inputArray
	 * @return Sorted Array
	 */

	public static int[] sort(int[] inputArray) {
		if (isEmpty(inputArray)) {
			return inputArray;
		}

		return quickSort(inputArray, 0, inputArray.length - 1);
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
	 * Single Value Manipulation
	 * ******************************************************************
	 */

	/**
	 * Swap two values providing their indizes.
	 * 
	 * @param arr
	 *            Input Array
	 * @param idxL
	 *            First Index
	 * @param idxH
	 *            Second Index
	 * @return
	 */
	public static int[] swapValues(int[] arr, int idxL, int idxH) {
		if (arr == null || arr.length == 0 || idxL < 0 || idxL > arr.length || idxH < 0 || idxH > arr.length) {
			return arr;
		}
		int temp = arr[idxL];
		arr[idxL] = arr[idxH];
		arr[idxH] = temp;
		return arr;

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
	 * @param arr
	 * @return
	 */
	public static int[] expand(int[] arr) {
		int[] ta = new int[arr.length + 1];
		ta = arr;
		return ta;
	}
	
	/**
	 * Increase the size of an array by 1
	 * Add the supplied value to the last index
	 * @param arr
	 * @param v
	 * @return
	 */
	public static int[] push(int[] arr, int v) {
		int[] ta = new int[arr.length + 1];
		ta[arr.length + 1] = v;
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

}
