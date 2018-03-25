
public class Graveyard {
	//Erstelle eine binäre Matrix um Nullen als true darzustellen
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
}
