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

public class Vector {

	private int[] vector;
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
		
		for(int i = 0; i < this.size; i++) {
			if(vector[i] != v.getVector()[i]) {
				return false;
			}
		}
		return true;
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
		
		for (int i = 0; i < size; i++) {
			text += vector[i]+" ";
		}
		
		return text;
	}

	
	
	
	/*
	 * **********************************************************************************
	 */

}
