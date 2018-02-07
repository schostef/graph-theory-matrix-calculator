package matrix;

/*
 * 
 * Diese Klasse bietet Rechenoperationen einer Matrix
 * Subklassen sind: Vector
 * Diese Klasse berechnet ausschlie�lich quadratische Matrizen
 */
public class Matrix {

	public int[][] matrix;
	private boolean[][] isZero;
	protected int size;
	
	// Übergeben eines 2 dimensionalen Arrays
	// Constructor wäre ohne Angabe der Größe aufrufbar...
	// Bearbeitung überdenken
	public Matrix(int[][] matrix) {
		this.matrix = matrix;
		this.size = matrix.length;
		createZeroMatrix();
	}
	
	//Initialisiert eine leere Matrix
	public Matrix(int size){
		matrix = new int[size][size];
		this.size = size;
	}
	
	//Erstelle eine bin�re Matrix um Nullen als true darzustellen
	private void createZeroMatrix() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if(matrix[i][j] == 0) {
					isZero[i][j] = true;
				}else {
					isZero[i][j] = false;
				}
			}
		}		
	}
	
	//Gib die Anzahl der 0 Werte in der angegebenen Zeile oder Spalte aus
	//#################################################
	public int fetchNumOfZerosRow(int zeile) {
		int s = 0;
		for (int i = 0; i < size; i++) {
			if(isZero[zeile][i]) {
				s++;
			}			
		}
		return s;
	}
	
	public int fetchNumOfZerosColumn(int spalte) {
		int s = 0;
		for (int i = 0; i < size; i++) {
			if(isZero[i][spalte]) {
				s++;
			}			
		}
		return s;
	}
	//###################################################
	
	// Gib die Zeilen oder Spaltennummer mit den meisten 0 Werten aus
	//###################################################
	public int getMostZerosRow() {
		int r = 0;
		for (int i = 1; i < size; i++) {
			if(fetchNumOfZerosRow(i) < fetchNumOfZerosRow(i-1)) {
				r = i;
			}
		}
		
		return r;
	}
	
	public int getMostZerosColumn() {
		int c = 0;
		for (int i = 1; i < size; i++) {
			if(fetchNumOfZerosColumn(i) < fetchNumOfZerosColumn(i-1)) {
				c = i;
			}
		}
		
		return c;
	}
	//#####################################################

	
	
	//Ausgabe an Console
	public void consolePrintMatrix() {
		for (int zeile = 0; zeile < size; zeile++){
			for (int spalte = 0; spalte < size; spalte++){
				System.out.print(getValueAt(zeile,spalte) + " ");
			}
			System.out.println();
		}
	}
	
	// Return wert an a(i,j)
	public int getValueAt(int i, int j){
		return matrix[i][j];
	}
	
	//Setter für a(i,j)
	public void setValueAt(int i, int j, int value){
		if(value == 0) {
			isZero[i][j] = true;
		} else {
			isZero[i][j] = false;
		}
		matrix[i][j] = value;
	}
	
	//Berechne die Zeilensumme von Zeile i
	public int rowSum(int row) {
		int sum = 0;
		
		for (int i = 0; i < size; i++) {
			sum += matrix[row][i];
		}
		
		return sum;
		
	}
	
	//Berechne die Spaltensumme von Spalte j
	public int columnSum(int column) {
		int sum = 0;
		
		for (int i = 0; i < size; i++) {
			sum += matrix[i][column];
		}
		
		return sum;
		
	}
	
	// Gibt die Zeilennummer mit der kleinsten Zeilensumme zurück
	public int getLowestRow() {
		int rowNumber = rowSum(0);
		for (int i = 1; i < size; i++) {
			if(rowSum(i) < rowSum(rowNumber)) {
				rowNumber = i;
			}
		}
		
		return rowNumber;
	}
	
	//Gibt die Zeilennummer mit der größten Zeilensumme zurück
	public int getHighestRow() {
		int rowNumber = rowSum(0);
		for (int i = 1; i < size; i++) {
			if(rowSum(i) > rowSum(rowNumber)) {
				rowNumber = i;
			}
		}
		
		return rowNumber;
	}
	
	//Gibt die Spaltennummer mit der kleinsten Spaltensumme zurück
	public int getLowestColumn() {
		int columnNumber = columnSum(0);
		for (int i = 1; i < size; i++) {
			if(columnSum(i) < columnSum(columnNumber)) {
				columnNumber = i;
			}
		}
		
		return columnNumber;
	}
	
	//Gibt die Spaltennummer mit der größten Spaltensumme zurück
	public int getHighestColumn() {
		int columnNumber = columnSum(0);
		for (int i = 1; i < size; i++) {
			if(columnSum(i) > columnSum(columnNumber)) {
				columnNumber = i;
			}
		}
		
		return columnNumber;
	}
	
	public int determinate() {
	
		//1. Entferne die Zeile und Spalte mit der größten Summe
		//2. Wie viele determinanten müssen berechnet werden, bis zu einer 3x3 Matrix
		//3. Überlegen.... wohin speichert man die Zwischenmatrixen?
		
		//Bevor �berhaupt Determinanten berechnet werden k�nnen m�ssen folgende Variablen vorab initialisiert werden:
		// -Wie viele Determinanten werden ben�tigt um auf eine 3x3 Matrix zu kommen
		// -Welche Zeilen, bzw. Spalten kommen in frage bis zur 3x3 Matrix
		// -Wie viele Zeilen bzw. Spalten enthalten keine 0 = Anzahl der Multiplikatoren
		//	und Submatrizen
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
		
		while(determinateMatrix.size > 3) {
			// compare Zeros on first row / column
			// Choose row or column with most zeros
			// size - numofzeros = num of multiplikators
			// fill the first array with multiplikators * negationoverlay starting at [0]
			// permanently remove chosen row / column
			// temporarily remove column/ row with multiplikator position
			// store new matrizes in the n-th section of determinateMatrizes
			// n -- for the while loop, can't use size of single matrix since there can be multiple
			// but the dimension doesn't change for each of the determinateMatrizes
			// also the size can be fixed already... S(Size) - 3 = x(numOfDeterminations)
		}
		
		Matrix[] determinanteMatrizes = new Matrix[size - 1];
		Matrix negationOverlay = new Matrix(size - 1);
		int removeFlag = 0;
		
		//erstelle das "Schachbrettmuster"
		// summe indizes = 	gerade -> +1
		//					ungerade -> -1
		negationOverlay.setValueAt(0, 0, 1);
		for (int i = 0; i < negationOverlay.size; i++) {
			for (int j = 0; j < negationOverlay.size; j++) {
				if((i+j)%2 == 0) {
					negationOverlay.setValueAt(i, j, 1);
				}else {
					negationOverlay.setValueAt(i, j, -1);
				}
			}
		}
		
		//Determiniere nach der kleinsten Zeilensumme
		//Sonst determiniere nach der kleinsten Spaltensumme
		if(getMostZerosRow() <= getMostZerosColumn()) {
			/*
			 * Finde Nullen
			 * Erstelle Determinante
			 * Vorzeichen beachten
			 */
		}else {
			
		}
		
		return 0;
	}
	
	
	
	
}
