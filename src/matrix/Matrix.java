package matrix;

/*
 * 
 * Diese Klasse bietet Rechenoperationen einer Matrix
 * Subklassen sind: Vector
 * Diese Klasse berechnet ausschließlich quadratische Matrizen
 */
public class Matrix {

	public int[][] matrix;
	protected int size;
	
	//Leere Initialisierung sinnlos
	public Matrix() {
		// TODO Auto-generated constructor stub
	}
	
	// Übergeben eines 2 dimensionalen Arrays
	// Constructor wäre ohne Angabe der Größe aufrufbar...
	// Bearbeitung überdenken
	public Matrix(int[][] matrix, int size) {
		this.matrix = matrix;
		this.size = size;
	}
	
	//Initialisiert eine leere Matrix
	public Matrix(int size){
		matrix = new int[size][size];
		this.size = size;
	}
	
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
		
		/*
		 * Matrix[] determinantenMatrizen = new Matrix[knoten - 1];
		 * 
		 * 
		 */
		
		Matrix[] determinanteMatrizes = new Matrix[size - 1];
		Matrix negationOverlay = new Matrix(size - 1);
		
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
		
		if(rowSum(getLowestRow()) < columnSum(getLowestColumn())) {
			/*
			 * Finde Nullen
			 * Erstelle Determinante
			 * Vorzeichen beachten
			 */
		}
	}
	
	
	
	
}
