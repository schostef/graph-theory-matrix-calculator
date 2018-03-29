/**
 * @author Stefan Schoeberl
 * @version 0.1
 * @modified 2018-03-23
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
	 * *********************************************************************
	 * Constructors
	 * *********************************************************************
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
	
	/*
	 * ***********************************************************************
	 */
	
	/*
	 * ***********************************************************************
	 * Getters
	 * ***********************************************************************
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
	
	/*
	 * ************************************************************************
	 */
	
	/*
	 * ************************************************************************
	 * Setters
	 * ************************************************************************
	 */
	
	public void setBinVector(boolean[] vector) {
		this.binVector = vector;
		
	}
	
	/*
	 * ************************************************************************
	 */
	
	/*
	 * ************************************************************************
	 * Control Mechanisms
	 * ************************************************************************
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
	 * Search a int type vector for a value. If the value is found write the boolean value inside the binVector
	 * @param vector int vecotor to search for
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
	
	
	/*
	 * ************************************************************************
	 */
	
	/**
	 * @deprecated
	 * @return
	 */
	public int rowSum() {
		int sum = 0;
		for (int i = 0; i < size; i++) {
			sum += vector[i];
		}
		
		return sum;
	}
	
	/*
	 * *********************************************************************************
	 * Calculations
	 * *********************************************************************************
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
	
	/*
	 * *********************************************************************************
	 */
	
	
	
	/*
	 * *********************************************************************************
	 * Output
	 * *********************************************************************************
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
	 * **********************************************************************************
	 */

}
