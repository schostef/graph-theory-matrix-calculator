package matrix;

public class Graph {
	
	private int anzahlKnoten;			
	public Matrix[] adjazenzMatrizen;	//Array der A^V-1 Matrizen

	// Ein Graph wird durch die Eingabe der Knotenanzahl initialisiert
	public Graph(int knoten) {
		anzahlKnoten = knoten;
		adjazenzMatrizen = new Matrix[knoten];

	}
	
	// Die Methode der Adjazenzmatrix ist momentan der einzige Weg der Grapherstellung
	// ToDo: Über ein graphisches Interface die A Matrix übermitteln
	public void createA0Matrix(int[][] a1Matrix){
		Matrix a1 = new Matrix(a1Matrix, anzahlKnoten);
		adjazenzMatrizen[0] = a1;
	}
	
	//Derzeit arbeitet die Methode bis zur V-1ten A Matrix
	//Aus Performance gründen sollte ein Abbruchkriterium (alternativmethode) kreirt werden
	//welches bei vollständigen wegen abbricht.
	public void calculateAdjazenzMatrizen(){
		for (int i = 0; i < anzahlKnoten-1; i++){
			if(i == 0){
				adjazenzMatrizen[i+1] = multiplyMatrix(adjazenzMatrizen[0], adjazenzMatrizen[0]);
			}else{
				adjazenzMatrizen[i+1] = multiplyMatrix(adjazenzMatrizen[0], adjazenzMatrizen[i]);
			}
		}
	}
	
	public void consolePrintAdjazenzmatrizen(){
		for (int i = 0; i < adjazenzMatrizen.length; i++){
			System.out.println("A^" + (i+1) + " Matrix: ");
			adjazenzMatrizen[i].consolePrintMatrix();
			System.out.println();
		}
	}
	
	//Multiplizieren von 2 Matrizen	
	public Matrix multiplyMatrix(Matrix m1, Matrix m2){
		Matrix ergebnisMatrix = new Matrix(anzahlKnoten);
		int value = 0;
		for (int zeile = 0; zeile < anzahlKnoten; zeile++){
			for (int spalte = 0; spalte < anzahlKnoten; spalte++){
				for (int inkrement = 0; inkrement < anzahlKnoten; inkrement++){
					value += m1.getValueAt(zeile, inkrement)*m2.getValueAt(inkrement, spalte);
				}
				ergebnisMatrix.setValueAt(zeile, spalte, value);
				value = 0;
			}
		}
		return ergebnisMatrix;
	}

}
