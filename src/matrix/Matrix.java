/**
 * @author Stefan Schoeberl
 * @version 1.0
 * @modified 2018-08-09
 * 
 * @Class Matrix
 * Matrix calculations for an MxM mirrored Matrix.
 * Most calculations are done with the Vector Objects inside this class.
 * After every change made in the matrix, the method vectorize() should be called to update the matrix.
 */

package matrix;

import arraytools.*;

public class Matrix {

	private int[][] matrix;
	private boolean[][] binMatrix;
	public Vector[] verticalVectors, horizontalVectors;
	private boolean isSymmetric = true;
	public int size = 0;

	/*
	 * Constructors
	 */

	/**
	 * Construct a new Matrix with a 2 dimensional Array
	 * 
	 * @param matrix 2 Dimensional int Array
	 */
	public Matrix(int[][] matrix) {
		this.matrix = new int[matrix.length][matrix.length];
		for(int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				this.matrix[i][j] = matrix[i][j];
			}
		}
		this.size = matrix.length;
		verticalVectors = new Vector[matrix[0].length];
		horizontalVectors = new Vector[matrix.length];
		vectorize(this.matrix);
		
	}
	
	/**
	 * New boolean type matrix
	 * @param m
	 */
	public Matrix(boolean[][] m) {
		binMatrix = m;
		this.size = m.length;
		verticalVectors = new Vector[size];
		horizontalVectors = new Vector[size];
		vectorize(binMatrix);
		
	}

	/**
	 * Construct a new MXM Matrix
	 * 
	 * @param size Value of M
	 */
	public Matrix(int size) {
		matrix = new int[size][size];
		verticalVectors = new Vector[size];
		horizontalVectors = new Vector[size];
		this.size = size;
		vectorize(matrix);
	}
	
	/**
	 * Construct a new binary Matrix and set all Values to the value provided.
	 * @param size Matrix size
	 * @param value Flag to set to all fields
	 */
	public Matrix(int size, boolean value) {
		binMatrix = new boolean[size][size];
		verticalVectors = new Vector[size];
		horizontalVectors = new Vector[size];
		this.size = size;
		fillAll(value);
		vectorize(binMatrix);
		
	}
	
	/**
	 * New Matrix by passing an array of vectors
	 * @param v
	 */
	public Matrix(Vector[] v) {
		this.size = v[0].size;
		matrix = new int[size][size];
		verticalVectors = new Vector[size];
		horizontalVectors = new Vector[size];
		
		for(int i = 0; i < size; i++) {
			verticalVectors[i] = new Vector(v[i].getVector());
			horizontalVectors[i] = new Vector(v[i].getVector());
			matrix[i] = v[i].getVector();
		}
	}

	/*
	 * -> End Constructors
	 */

	/* 
	 * Getters
	 */

	public boolean isSymmetric() {
		return isSymmetric;
	}

	public int[][] getMatrix() {
		return matrix;
	}
	
	public boolean[][] getBinMatrix(){
		return binMatrix;
	}
	
	public int getValueAt(int i, int j) {
		return matrix[i][j];
	}
	
	/**
	 * Get a single vertical or horizontal Vector
	 * @param index Vector index
	 * @param isHorizontal true = horizontal, false = vertical
	 * @return Vector at index
	 */
	public Vector getVector(int index, boolean isHorizontal) {
		if (isHorizontal) {
			return horizontalVectors[index];
		}else {
			return verticalVectors[index];
		}
	}

	

	/*
	 * -> End Getters
	 */
	
	
	
	/*
	 * Setters
	 */
	
	/**
	 * Set value at position i,j
	 * @param i
	 * @param j
	 * @param value
	 */
	public void setValueAt(int i, int j, int value) {
		matrix[i][j] = value;
	}
	
	/*
	 * -> End Setters
	 */
	
	
	/*
	 * Manipulation and Creation
	 */

	/**
	 * Split the matrix into horizontal and vertical Vectors:
	 * horizontalVectors[] , verticalVectors[]
	 * Check the Symmetry and set the isSymmetric flag accordingly
	 * @param matrix Pass a matrix Object when called externally
	 */
	public void vectorize(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			horizontalVectors[i] = new Vector(cutVector(matrix, i, true));
			verticalVectors[i] = new Vector(cutVector(matrix, i, false));
			if (!(isSymmetric && horizontalVectors[i].isEqual(verticalVectors[i]))) {
				isSymmetric = false;
			}
		}
	}
	
	/**
	 * Split the matrix into horizontal and vertical Vectors:
	 * horizontalVectors[] , verticalVectors[]
	 * Check the Symmetry and set the isSymmetric flag accordingly
	 * @see vectorize(int[][] matrix)
	 * @param matrix matrix to vectorize
	 */
	public void vectorize(boolean[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			horizontalVectors[i] = new Vector(cutVector(matrix, i, true));
			verticalVectors[i] = new Vector(cutVector(matrix, i, false));
			if (!(isSymmetric && horizontalVectors[i].isEqual(verticalVectors[i]))) {
				isSymmetric = false;
			}
		}
	}
	
	/**
	 * Split the matrix into horizontal and vertical Vectors:
	 * horizontalVectors[] , verticalVectors[]
	 * Check the Symmetry and set the isSymmetric flag accordingly
	 */
	public void vectorize() {
		for (int i = 0; i < matrix.length; i++) {
			horizontalVectors[i] = new Vector(cutVector(matrix, i, true));
			verticalVectors[i] = new Vector(cutVector(matrix, i, false));
			if (!(isSymmetric && horizontalVectors[i].isEqual(verticalVectors[i]))) {
				isSymmetric = false;
			}
		}
	}
	
	/**
	 * Cuts a single vector out of a Matrix Object.
	 * @param matrix Matrix Object to cut Vector out of
	 * @param position The index of the vector
	 * @param isHorizontal True if the vector is a N (horizontal Vector), false for M (vertical)
	 * @return Array with the Vector values
	 */
	public int[] cutVector(int[][] matrix, int position, boolean isHorizontal) {
		int[] tempArray = new int[matrix.length];
		if (isHorizontal) {
			for (int i = 0; i < matrix.length; i++) {
				tempArray[i] = matrix[position][i];
			}
		} else {
			for (int i = 0; i < matrix.length; i++) {
				tempArray[i] = matrix[i][position];
			}
		}
		return tempArray;
	}
	
	/**
	 * Get a Vector (of type boolean[]) out of a provided 2-Dimensional Array
	 * @param matrix 2 - Dimensional array
	 * @param position - index of the Vector
	 * @param isHorizontal - True if the vector is a N (horizontal Vector), false for M (vertical)
	 * @return 1 Dimensional Array of target Vector
	 */
	public boolean[] cutVector(boolean[][] matrix, int position, boolean isHorizontal) {
		boolean[] tempArray = new boolean[matrix.length];
		if (isHorizontal) {
			for (int i = 0; i < matrix.length; i++) {
				tempArray[i] = matrix[position][i];
			}
		} else {
			for (int i = 0; i < matrix.length; i++) {
				tempArray[i] = matrix[i][position];
			}
		}
		return tempArray;
	}
	
	/**
	 * Set every field in the binMatrix to the boolean value
	 * @param value
	 */
	public void fillAll(boolean value) {
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				binMatrix[i][j] = value;
			}
		}
	}
	
	/**
	 * Search for an Integer value in the provided Matrix
	 * @param matrix Integer type Matrix
	 * @param i Value to search for in matrix
	 * @return A binary Matrix . true = value found , false = value not found
	 */
	public static Matrix searchAndReport(Matrix matrix, int i) {
		Matrix resultMatrix = new Matrix(matrix.size,false);
		//Check for symmetry
		if (matrix.isSymmetric()) {
			
			int vertexNumber = 0;
			while (vertexNumber < matrix.size) {
				resultMatrix.verticalVectors[vertexNumber].findValueAndSet(matrix.verticalVectors[vertexNumber], i, true);
				resultMatrix.getVector(vertexNumber, true).setBinVector(resultMatrix.getVector(vertexNumber, false).binGetVector());
				vertexNumber++;
			}
			resultMatrix.refreshBinMatrix();
		}
		resultMatrix.vectorize(resultMatrix.binMatrix);
		return resultMatrix;		
	}
	
	/**
	 * Write the Vectors into the binMatrix
	 */
	public void refreshBinMatrix() {
		if(isSymmetric) {
			for (int i = 0; i < size; i++) {
				binMatrix[i] = getVector(i,true).getBinVector();
			}
		}
	}	
	
	/**
	 * Delete a row and it's identical column
	 * @param index
	 * @return
	 */
	public Matrix removeCross(int index) {
		Vector[] shortenedVectors = new Vector[size-1];
		int pos = 0;
		if(isSymmetric) {
			for(int i = 0; i< size; i++) {
				if(i != index) {
					shortenedVectors[pos] = horizontalVectors[i].remove(index);
					pos++;
				}
			}
		}
		
		Matrix tempMatrix = new Matrix(shortenedVectors);
		return tempMatrix;
		
	}
	
	/**
	 * Create a binary matrix, which sets true on a given value
	 * @param setTrueOnValue
	 */
	public void makeBitMap(int setTrueOnValue) {
		binMatrix = searchAndReport(this, setTrueOnValue).getBinMatrix();
	}
	/*
	 * -> End Manipulation and Creation
	 */
	
	/*
	 * Calculations
	 */
	
	/**
	 * @deprecated
	 * Not being used in the Graph Class. Still usable.
	 * Calculate exponential cross product
	 * @param exponent exponent
	 * @return Cross Product Matrix
	 */
	public Matrix exponentiate(int exponent) {
		Matrix result = this;
		for(int i = 1; i < exponent; i++) {
			result = result.multiply(this);
		}
		return result;
	}

	/**
	 * Creates the cross Product between this Matrix and a provided one
	 * Checks if both matrizes are of equal dimension
	 * Checks if both matrizes are symmetric
	 * @param m Matrix to multiply with
	 * @return
	 */
	public Matrix multiply(Matrix m) {
		Matrix resultMatrix = new Matrix(size);
		//Check for symmetry
		if (this.isSymmetric() && m.isSymmetric() && this.size == m.size) {
			
			int vertexNumber = 0;
			int stepper = 0;
			int result = 0;

			//Due to the symmetry of both matrices, multiplication can be reduced to columns only.
			while (vertexNumber < size) {
				stepper = vertexNumber;
				while (stepper < m.size) {
					result = this.verticalVectors[vertexNumber].multiply(m.verticalVectors[stepper]);
					if(stepper != vertexNumber) {
						resultMatrix.setValueAt(stepper, vertexNumber, result);
					}
					resultMatrix.setValueAt(vertexNumber, stepper, result);
					stepper++;
				}
				vertexNumber++;
			}
		}
		resultMatrix.vectorize();
		return resultMatrix;
	}	
	
	/**
	 * Get the highest values of all rows or columns
	 * @param isHorizontal
	 * @return Result of highest values
	 */
	public Vector vectorHighestValues(boolean isHorizontal) {
		Vector result = new Vector(size);
		
		for(int i = 0; i < size; i++) {
			if(isHorizontal) {
				result.setValueAt(i, horizontalVectors[i].getMax());
			}else {
				result.setValueAt(i, verticalVectors[i].getMax());
			}
		}
		
		return result;
	}
	
	/**
	 * Count how often a specific value appears in the matrix.
	 * @param value value to search for
	 * @param alongDiagonal true to ignore mirrored values along the diagonal (only count once per row)
	 * @return the sum of value appearing in the matrix
	 */
	public int countValue(int value,boolean alongDiagonal) {
		int counter = 0;
		if(alongDiagonal) {
			for(int i = 0; i < size; i++) {
				counter += verticalVectors[i].countValue(i, size-1, value);
			}
		}else {
			for(Vector v:verticalVectors) {
				counter += v.countValue(value);
			}
		}
		
		return counter;
	}
	
	/*
	 * -> End Calculations
	 */
	
	/*
	 * Control Mechanisms
	 */
	
	/**
	 * Check if two matrices are identical to each other
	 * @param m1
	 * @param m2
	 * @return
	 */
	public static boolean isIdentical(Matrix m1, Matrix m2) {
		if(m1 != null && m2 != null && m1.size == m2.size) {
			for (int i = 0; i < m1.size; i++) {
				if(!m1.verticalVectors[i].isEqual(m2.verticalVectors[i])) {
					return false;
				}
			}
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Check if another matrix is identical to this one
	 * @param m1
	 * @return
	 */
	public boolean isIdentical(Matrix m1) {
		return isIdentical(this,m1);
	}	
	
	/**
	 * Return the coordinates containing the provided value
	 * @param i Value to search for
	 * @param b true for: Skip mirrored duplicates ([i,j] for [j,i])
	 * @return coordinates[entry No][i,j]
	 */
	public int[][] getCoordinatesOfValue(int i, boolean b) {
		int[] currentRow = new int[0];
		int[] currentCoordinate = new int[2];
		int[][] coordinates;
		int entryNo = 0;
		if(isSymmetric) {
			if(b) {
				int entryCount = countValue(i, true);
				coordinates = new int[entryCount][2];
				for(int j = 0; j < size; j++) {
					currentRow = verticalVectors[j].getPositionOfValue(j, size-1, i);
					currentCoordinate[0] = j;
					for(int k = 0; k < currentRow.length; k++) {
						currentCoordinate[1] = currentRow[k];
						coordinates[entryNo][0]=j;
						coordinates[entryNo][1]=currentCoordinate[1];
						entryNo++;
					}
				}
			}else {
				int entryCount = countValue(i, false);
				coordinates = new int[entryCount][2];
				for(int j = 0; j < size; j++) {
					currentRow = verticalVectors[j].getPositionOfValue(i);
					currentCoordinate[0] = j;
					for(int k = 0; k < currentRow.length; k++) {
						currentCoordinate[1] = currentRow[k];
						coordinates[entryNo]=currentCoordinate;
						entryNo++;
					}
				}
			}
		}else {
			//Non-symmetric Matrizes not supported at the moment
			return null;
		}
		
		return coordinates;
	}
	
	/**
	 * Logical XOR Operation on each value with the same index between two matrices.
	 * Requires a binary matrix
	 * @param m1
	 * @param m2
	 * @return logical XOR result
	 */
	public static Matrix bitOperationXOR(Matrix m1, Matrix m2) {
		if(m1.size == m2.size) {
			Matrix compare = new Matrix(m1.size,false);
			for(int i = 0; i < m1.size; i++) {
				for(int j = 0; j < m1.size; j++) {
					compare.getBinMatrix()[i][j] = (m1.getBinMatrix()[i][j] && !m2.getBinMatrix()[i][j]) || 
							(!m1.getBinMatrix()[i][j] && m2.getBinMatrix()[i][j]);
				}
			}
			return compare;
		}else {
			return null;
		}
		
	}
	
	/**
	 * Logical AND Operation on each value with the same index between two matrices.
	 * Requires a binary matrix
	 * @param m1
	 * @param m2
	 * @return logical AND result
	 */
	public static Matrix bitOperationAND(Matrix m1, Matrix m2) {
		if(m1.size == m2.size) {
			Matrix compare = new Matrix(m1.size,false);
			for(int i = 0; i < m1.size; i++) {
				for(int j = 0; j < m1.size; j++) {
					compare.getBinMatrix()[i][j] = m1.getBinMatrix()[i][j] && m2.getBinMatrix()[i][j];
				}
			}
			return compare;
		}else {
			return null;
		}
	}
	
	/**
	 * Compares all rows in a Matrix with each other and filters out unique rows.
	 * Discards duplicates.
	 * @return Vector type array with unique rows
	 */
	public Vector[] fetchUniqueRows() {
		Vector[] result = new Vector[0];
		vectorize();
		result = GraphTools.push(result, verticalVectors[0]);
		for(int i = 1; i < size; i++) {
			boolean match = false;
			for(int j = 0; j < result.length && !match; j++) {
				if(result[j].isEqual(verticalVectors[i])) {
					match = true;
				}
			}
			if(!match) {
				result = GraphTools.push(result, verticalVectors[i]);
			}
		}
		return result;
	}
	

	
	/*
	 * -> End Control Mechanisms
	 */


	/*
 	 * Output
	 */

	public String toString() {
		String text = "";
		for (int i = 0; i < horizontalVectors.length; i++) {
			text += horizontalVectors[i] + "\n";
		}

		return text;
	}
	
	/**
	 * Convert the matrix to char type.
	 * Replaces -1 with the infinity character
	 * @return
	 */
	public char[][] convertToChar(){
		char[][] c = new char[size][size];
		int REDIX=10; 
		
		for(int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if(matrix[i][j] == -1) {
					c[i][j] = '\u221E';
				}else {
					c[i][j] = Character.forDigit(matrix[i][j],REDIX);
				}
				    
			}
		}
		
		return c;
	}	

	/*
	 * -> End Output
	 */

}
