/**
 * @author Stefan Schoeberl
 * @version 0.1
 * @modified 2018-03-23
 * 
 * @Class Matrix
 * Matrix calculations. This class only supports MxM Matrizes at the moment
 */

package matrix;

import arraytools.*;
import graph.Vertex;

public class Matrix {

	private int[][] matrix;
	private boolean[][] binMatrix;
	public Vector[] verticalVectors, horizontalVectors;
	private boolean[][] isZero;
	private boolean isSymmetric = true;
	public int size = 0;

	/*
	 * *****************************************************************
	 * Constructors
	 * *****************************************************************
	 */

	/**
	 * Construct a new Matrix with a prefilled Array
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
	 * *****************************************************************
	 */

	/*
	 * ***************************************************************** 
	 * Getters
	 * *****************************************************************
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
	 * *****************************************************************
	 */
	
	
	
	/*
	 * *****************************************************************
	 * Setters
	 * *****************************************************************
	 */
	
	public void setValueAt(int i, int j, int value) {
		matrix[i][j] = value;
	}
	
	public void setFlagAt(int i, int j) {
		binMatrix[i][j] = !binMatrix[i][j];
		
	}
	
	/*
	 * *****************************************************************
	 * Manipulation and Creation
	 * *****************************************************************
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
	
	
	public Matrix shrinkByIndizes(int[] vertexIndizes) {
		Matrix shrinkedMatrix = new Matrix(vertexIndizes.length);
		for (int i = 0; i < vertexIndizes.length; i++) {
			for (int j = 0; j < vertexIndizes.length; j++) {
				shrinkedMatrix.setValueAt(i, j, this.getValueAt(vertexIndizes[i], vertexIndizes[j]));
			}
		}
		return shrinkedMatrix;
	}
	
	
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
	/*
	 * ******************************************************************
	 */
	
	/*
	 * ******************************************************************
	 * Calculations
	 * ******************************************************************
	 */
	
	/**
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

			/*
			 * If both matrizes are symmetric we don't need to multiply m x n since there are only m or n vectors
			 * Therefore multiplications only need to happen on the triangle along the diagonal, reducing steps by n-1 ... 1
			 * [0,0 0,1 0,2] Copy values along diagonal: 	[  !    !   !]
			 * [ *  1,1 1,2]								[ 0,1   !   !]
			 * [ *   *  2,2]								[ 0,2  1,2  !]
			 */
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
	
	// Berechne die Zeilensumme von Zeile i
	public Vector vectorSums(boolean isHorizontal) {
		Vector result = new Vector(size);
		
		for(int i = 0; i < size; i++) {
			if(isHorizontal) {
				result.setValueAt(i, horizontalVectors[i].sumOf());
			}else {
				result.setValueAt(i, verticalVectors[i].sumOf());
			}
		}
		
		return result;
	}
	
	public int getSymmetricSum() {
		if(isSymmetric) {
			int row = 0;
			int column = 0;
			int sum = 0;
			while(row < size) {
				column = row;
				sum += horizontalVectors[row].sumOf(column, size);
				row++;
			}
			return sum;			
		}
		return -1;
	}
	
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

	// Berechne die Spaltensumme von Spalte j
	public int columnSum(int column) {
		int sum = 0;

		for (int i = 0; i < size; i++) {
			sum += matrix[i][column];
		}

		return sum;

	}
	
	public int getTotalSum() {
		int totalSum= 0;
		for(int i = 0; i < size; i++) {
			totalSum += horizontalVectors[i].sumOf(i, size);
		}
		
		return totalSum;
	}
	
	/*
	 * ********************************************************************
	 */
	
	/*
	 * ********************************************************************
	 * Control Mechanisms
	 * ********************************************************************
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
	
	/*
	 * ********************************************************************
	 */

	

	

	/**
	 * @deprecated
	 * @param zeile
	 * @return
	 */
	public int fetchNumOfZerosRow(int zeile) {
		int s = 0;
		for (int i = 0; i < size; i++) {
			if (isZero[zeile][i]) {
				s++;
			}
		}
		return s;
	}
	
	/**
	 * @deprecated
	 * @param spalte
	 * @return
	 */
	public int fetchNumOfZerosColumn(int spalte) {
		int s = 0;
		for (int i = 0; i < size; i++) {
			if (isZero[i][spalte]) {
				s++;
			}
		}
		return s;
	}
	// ###################################################

	// Gib die Zeilen oder Spaltennummer mit den meisten 0 Werten aus
	// ###################################################
	public int getMostZerosRow() {
		int r = 0;
		for (int i = 1; i < size; i++) {
			if (fetchNumOfZerosRow(i) < fetchNumOfZerosRow(i - 1)) {
				r = i;
			}
		}

		return r;
	}

	public int getMostZerosColumn() {
		int c = 0;
		for (int i = 1; i < size; i++) {
			if (fetchNumOfZerosColumn(i) < fetchNumOfZerosColumn(i - 1)) {
				c = i;
			}
		}

		return c;
	}
	// #####################################################

	// Ausgabe an Console
	public void consolePrintMatrix() {
		for (int zeile = 0; zeile < size; zeile++) {
			for (int spalte = 0; spalte < size; spalte++) {
				System.out.print(getValueAt(zeile, spalte) + " ");
			}
			System.out.println();
		}
	}

	

	

	// Gibt die Zeilennummer mit der kleinsten Zeilensumme zurück
	public int getLowestRow() {
		int rowNumber = rowSum(0);
		for (int i = 1; i < size; i++) {
			if (rowSum(i) < rowSum(rowNumber)) {
				rowNumber = i;
			}
		}

		return rowNumber;
	}

	// Gibt die Zeilennummer mit der größten Zeilensumme zurück
	public int getHighestRow() {
		int rowNumber = rowSum(0);
		for (int i = 1; i < size; i++) {
			if (rowSum(i) > rowSum(rowNumber)) {
				rowNumber = i;
			}
		}

		return rowNumber;
	}

	// Gibt die Spaltennummer mit der kleinsten Spaltensumme zurück
	public int getLowestColumn() {
		int columnNumber = columnSum(0);
		for (int i = 1; i < size; i++) {
			if (columnSum(i) < columnSum(columnNumber)) {
				columnNumber = i;
			}
		}

		return columnNumber;
	}

	// Gibt die Spaltennummer mit der größten Spaltensumme zurück
	public int getHighestColumn() {
		int columnNumber = columnSum(0);
		for (int i = 1; i < size; i++) {
			if (columnSum(i) > columnSum(columnNumber)) {
				columnNumber = i;
			}
		}

		return columnNumber;
	}
	
	
	public int determinate() {

		// 1. Entferne die Zeile und Spalte mit der größten Summe
		// 2. Wie viele determinanten müssen berechnet werden, bis zu einer 3x3 Matrix
		// 3. Überlegen.... wohin speichert man die Zwischenmatrixen?

		// Bevor �berhaupt Determinanten berechnet werden k�nnen m�ssen folgende
		// Variablen vorab initialisiert werden:
		// -Wie viele Determinanten werden ben�tigt um auf eine 3x3 Matrix zu kommen
		// -Welche Zeilen, bzw. Spalten kommen in frage bis zur 3x3 Matrix
		// -Wie viele Zeilen bzw. Spalten enthalten keine 0 = Anzahl der Multiplikatoren
		// und Submatrizen
		// -Setze vorab die Multiplikatoren mit dem negationOverlay fest
		// -erstelle die Submatrizen
		// -Anwendung des Sarrus Algorithmus
		// -Summe bilden und Determinante ausgeben

		/*
		 * Matrix[] determinantenMatrizen = new Matrix[knoten - 1];
		 * 
		 * 
		 */

		Matrix determinateMatrix = this;

		while (determinateMatrix.size > 3) {
			// compare Zeros on first row / column
			// Choose row or column with most zeros
			// size - numofzeros = num of multiplikators
			// fill the first array with multiplikators * negationoverlay starting at [0]
			// permanently remove chosen row / column
			// temporarily remove column/ row with multiplikator position
			// store new matrizes in the n-th section of determinateMatrizes
			// n -- for the while loop, can't use size of single matrix since there can be
			// multiple
			// but the dimension doesn't change for each of the determinateMatrizes
			// also the size can be fixed already... S(Size) - 3 = x(numOfDeterminations)
		}

		Matrix[] determinanteMatrizes = new Matrix[size - 1];
		Matrix negationOverlay = new Matrix(size - 1);
		int removeFlag = 0;

		// erstelle das "Schachbrettmuster"
		// summe indizes = gerade -> +1
		// ungerade -> -1
		negationOverlay.setValueAt(0, 0, 1);
		for (int i = 0; i < negationOverlay.size; i++) {
			for (int j = 0; j < negationOverlay.size; j++) {
				if ((i + j) % 2 == 0) {
					negationOverlay.setValueAt(i, j, 1);
				} else {
					negationOverlay.setValueAt(i, j, -1);
				}
			}
		}

		// Determiniere nach der kleinsten Zeilensumme
		// Sonst determiniere nach der kleinsten Spaltensumme
		if (getMostZerosRow() <= getMostZerosColumn()) {
			/*
			 * Finde Nullen Erstelle Determinante Vorzeichen beachten
			 */
		} else {

		}

		return 0;
	}

	/*
	 * *****************************************************************************
	 * **** Output
	 * *****************************************************************************
	 * ****
	 */

	public String toString() {
		String text = "";
		for (int i = 0; i < horizontalVectors.length; i++) {
			text += horizontalVectors[i] + "\n";
		}

		return text;
	}

	

	

	

	

	/*
	 * *****************************************************************************
	 * *****
	 */

}
