/**
 * @author Stefan Schoeberl
 * @version 1.0
 * @modified 2018-08-09
 * 
 * @Class Vector
 * Used in relation to the Matrix class
 * Matrix splits itself up into vectors.
 * Simple Vector based Math operations
 */

package matrix;

import arraytools.ArrayTools;

public class Vector {

	private int[] vector;
	private boolean[] binVector;
	public int size;
	
	/*
	 * Constructors
	 */

	/**
	 * Create a Vector based on a finished int array
	 * @param vector 
	 */
	public Vector(int[] vector) {
		this.vector = vector;
		this.size = this.vector.length;
	}

	/**
	 * Creates an empty Vector Object with a fixed size
	 * @param size
	 */
	public Vector(int size) {
		this.size = size;
		vector = new int[size]; 
	}
	
	/**
	 * Creates a boolean type Vector
	 * @param vector vector values
	 */
	public Vector(boolean[] vector) {
		binVector = vector;
		this.size = vector.length;
	}
	
	public Vector(int size, int value) {
		this.size = size;
		vector = new int[size]; 
		fill(value);
	}
	
	/*
	 * -> End Constructors
	 */
	
	/*
	 * Getters
	 */
	
	public int[] getVector() {
			return vector;	
	}
	
	public boolean[] getBinVector() {
		return binVector;	
}
	
	public boolean[] binGetVector() {
		return binVector;
	}
	
	public boolean getBinValueAt(int index) {
		return binVector[index];
	}
	
	/*
	 * -> End Getters
	 */
	
	/*
	 * Setters
	 */
	
	public void setBinVector(boolean[] vector) {
		this.binVector = vector;
		
	}
	
	public void setValueAt(int index, int value) {
		vector[index] = value;
	}
	
	/*
	 * -> End Setters
	 */
	
	/*
	 * Control Mechanisms
	 */
	
	/**
	 * Compares this Vector with another one
	 * @param v Vector to compare to
	 * @return true if both vectors are exactly the same
	 */
	public boolean isEqual(Vector v) {
		if (v.size != this.size) {
			return false;
		}
		
		if (vector != null) {
			for(int i = 0; i < this.size; i++) {
				if(vector[i] != v.getVector()[i]) {
					return false;
				}
			}
			return true;
		}else if (binVector != null) {
			for(int i = 0; i < this.size; i++) {
				if(binVector[i] != v.getBinVector()[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Get the index, where a value is stored
	 * @param x value to search for
	 * @return array with index locations
	 */
	public int[] getPositionOfValue(int x) {
		int[] list = new int[0];
		for(int i = 0; i < size; i++) {
			if(vector[i] == x) {
				list = ArrayTools.push(list, i);
			}
		}
		return list;
	}
	
	/**
	 * Get the index, where a value is stored
	 * @see getPositionOfValue(int x)
	 * @param x value to search for
	 * @param start checking from here
	 * @param end until here (inclusive)
	 * @return array with index locations
	 */
	public int[] getPositionOfValue(int start,int end,int x) {
		int[] list = new int[0];
		for(int i = start; i <= end; i++) {
			if(vector[i] == x) {
				list = ArrayTools.push(list, i);
			}
		}
		return list;
	}
	
	/**
	 * Search a int type vector for a value. If the value is found write the boolean value inside the binVector
	 * @param vector int vector to search for
	 * @param value value to search for
	 * @param binSet value of a success to write in the binVector
	 */
	public void findValueAndSet(Vector vector, int value, boolean binSet) {
		if(!binSet) {
			boolean[] negation = ArrayTools.findAllDuplicates(vector.getVector(), value);
			for (int i = 0; i < negation.length; i++) {
				negation[i] = !negation[i];
			}
			binVector = negation;			
		}else {
			binVector = ArrayTools.findAllDuplicates(vector.getVector(), value);
		}
		
	}
	
	/**
	 * Count how often a value appears
	 * @param start start here
	 * @param end until here (inclusive
	 * @param value value to count
	 * @return 
	 */
	public int countValue(int start, int end, int value) {
		return getPositionOfValue(start, end, value).length;		
	}
	
	/**
	 * Count how often a value appears
	 * @param value value to count
	 * @return
	 */
	public int countValue(int value) {
		return countValue(0,size,value);
	}	
	
	/*
	 * -> End Control Mechanisms
	 */
	
	/*
	 * Manipulators
	 */
	
	/**
	 * Fill the entire Vector with a given value
	 * @param value
	 */
	public void fill (int value) {
		vector = ArrayTools.fill(vector, value);
	}
	
	/**
	 * Delete a value out of the vector
	 * @param index delete value at this index
	 * @return Vector with size - 1
	 */
	public Vector remove(int index) {
		return new Vector(ArrayTools.delete(vector,index));		
	}
	
	/*
	 * -> End Manipulators
	 */
	
	/*
	 * Calculations
	 */
	
	/**
	 * Multiplies this vector with another one
	 * @param vector2
	 * @return Multiplication result
	 */
	public int multiply(Vector vector2) {
		int result = 0;
		for (int i = 0; i < size; i++) {
			//If either one of the values is 0 skip the multiplication
			if(this.vector[i] != 0 && vector2.getVector()[i] != 0) {
				result += this.vector[i] * vector2.getVector()[i];
			}
		}
		return result;
	}
	
	/**
	 * Get the highest value
	 * @return
	 */
	public int getMax() {
		return ArrayTools.maxOf(vector);
	}
	
	/**
	 * Get the lowest value
	 * @return
	 */
	public int getMin() {
		return ArrayTools.minOf(vector);
	}
	
	/*
	 * -> End Calculations
	 */
	
	
	
	/*
	 * Output
	 */
	
	public String toString() {
		String text = "";
		if(vector != null) {		
			for (int i = 0; i < size; i++) {
				text += vector[i]+" ";
			}
		}else if (binVector != null) {
			for (int i = 0; i < size; i++) {
				text += binVector[i]+" ";
			}
		}
		
		return text;
	}	
	
	/*
	 * -> End Output
	 */

}
