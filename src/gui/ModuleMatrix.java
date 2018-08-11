/**
 * @author Stefan Schoeberl
 * @version 1.0
 * @modified 2018-08-09
 * 
 * @Class ModuleMatrix
 * Matrix GUI Element
 */
package gui;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import matrix.Matrix;

public class ModuleMatrix extends GridPane {
	
	private IndexButton[][] indexButtons;
	private IndexLabel[][] indexLabels;
	private int size;
	
	/*
	 * Constructors
	 */
	/**
	 * Create a new ModuleMatrix Element with n Buttons
	 * @param size Size of the Matrix
	 */
	public ModuleMatrix(int size) {
		super();
		this.size = size;
		indexButtons = new IndexButton[size][size];
		createEditableMatrix(0);		
	}
	
	/**
	 * Create a new ModuleMatrix Element out of a given Matrix Object
	 * @param readonly true for a Matrix with Labels, false for a Matrix with IndexButtons
	 */
	public ModuleMatrix(Matrix matrix, boolean readonly) {
		super();
		this.size = matrix.size;
		if(readonly) {
			indexLabels = new IndexLabel[size][size];
			createReadOnlyMatrix(matrix);
		}else {
			createEditableMatrix(matrix);
		}
		
		
	}
	
	/*
	 * -> End Constructors
	 */

	/*
	 * GUI drawing
	 */
	/**
	 * A Matrix with Labels.
	 * @param matrix Matrix Object with values
	 */
	private void createReadOnlyMatrix(Matrix matrix) {
		char[][] m = matrix.convertToChar();
		for(int i = 0; i < size; i ++) {
			for(int j = i ; j < size; j++) {
				IndexLabel l1 = new IndexLabel(Character.toString(m[i][j]),i,j);
				IndexLabel l2 = l1.createMirroredObject();
				l1.setPrefSize(20, 20);
				l1.setAlignment(Pos.CENTER);
				l2.setPrefSize(20, 20);
				l2.setAlignment(Pos.CENTER);
				if(i == j) {
					
					indexLabels[i][i] = l1;
					
					add(l1, i, i);					
				}else {
					indexLabels[i][j] = l1;
					indexLabels[j][i] = l2;
					add(indexLabels[i][j], i, j);
					add(indexLabels[j][i], j, i);
					
				}
			}
			
		}
		this.setGridLinesVisible(true);		
	}
	
	/**
	 * A Matrix with IndexButtons
	 * @param matrix Matrix Object with values
	 */
	public void createEditableMatrix(Matrix matrix) {
		for(int j = 0; j < size; j++) {
			IndexLabel l = new IndexLabel(""+(j+1),j,0);
			IndexLabel l2 = l.createMirroredObject();
			l.setPrefSize(20, 20);
			l2.setPrefSize(20, 20);
			l.setAlignment(Pos.BASELINE_CENTER);
			l2.setAlignment(Pos.BASELINE_CENTER);
			add(l,j+1,0);
			add(l2,0,j+1);
		}
		
		for(int i = 0; i < size; i++) {
			for(int j = i; j < size; j++) {
				IndexButton b1 = new IndexButton(i,j);
				b1.setText(Integer.toString(matrix.getValueAt(i, j)));
				IndexButton b2 = b1.createMirroredObject();
				if(i == j) {
					b1.setDisable(true);
					indexButtons[i][i] = b1;
					add(b1, i+1, i+1);
				}else {
					indexButtons[i][j] = b1;
					indexButtons[j][i] = b2;
					add(b2, i+1, j+1);
					add(b1, j+1, i+1);
					
					indexButtons[i][j].setOnAction(event -> {
						flipButtons(b1.getRow(),b1.getCol());						
					});
					
					indexButtons[j][i].setOnAction(event -> {
						flipButtons(b2.getRow(),b2.getCol());
					});
				}
			}
		}
	}
	
	/**
	 * If the size of the Editable Matrix is increased, add the additional amount of buttons.
	 * @param startFrom Start adding Buttons from this position
	 */
	private void createEditableMatrix(int startFrom) {
		int stepper = 0;
		for(int j = startFrom; j < size; j++) {
				IndexLabel l = new IndexLabel(""+(j+1),j,stepper);
				IndexLabel l2 = l.createMirroredObject();
				l.setPrefSize(20, 20);
				l2.setPrefSize(20, 20);
				l.setAlignment(Pos.BASELINE_CENTER);
				l2.setAlignment(Pos.BASELINE_CENTER);
				add(l,j+1,stepper);
				add(l2,stepper,j+1);
		}
		for(int i = startFrom; i < size; i ++) {
			if(startFrom == 0) {
				stepper = i;
			}else {
				stepper = 0;
			}
			while(stepper < size) {
				IndexButton b1 = new IndexButton(i,stepper);
				IndexButton b2 = b1.createMirroredObject();
				if(i == stepper) {
					b1.setDisable(true);
					indexButtons[i][i] = b1;
					add(b1, i+1, i+1);
				}else {
					indexButtons[i][stepper] = b1;
					indexButtons[stepper][i] = b2;
					add(b2, i+1, stepper+1);
					add(b1, stepper+1, i+1);
					
					indexButtons[i][stepper].setOnAction(event -> {
						flipButtons(b1.getRow(),b1.getCol());						
					});
					
					indexButtons[stepper][i].setOnAction(event -> {
						flipButtons(b2.getRow(),b2.getCol());
					});
					
				}
				stepper++;
			}
			
		}
	
	}
	
	/**
	 * Resize the editable Matrix.
	 * @param newSize
	 */
	public void resizeEditableMatrix(int newSize) {
		int oldSize = size;
		this.size = newSize;
		IndexButton[][] tempArr = new IndexButton[size][size];
		
		if(oldSize < newSize) {
			for(int i = 0; i < oldSize; i++) {
				for(int j = 0; j < oldSize; j++) {
					tempArr[i][j] = indexButtons[i][j];
				}
			}
			
			indexButtons = tempArr;
			createEditableMatrix(oldSize);
		}else {
			getChildren().clear();
			createEditableMatrix(translateToMatrix());
			
		
		}
	}
	
	/**
	 * Flip a 0 to 1 and a 1 to 0 on the button and it's mirror along the diagonal
	 * @param i X-index
	 * @param j Y-index
	 */
	private void flipButtons(int i, int j) {
		indexButtons[i][j].flip();
		indexButtons[j][i].flip();
		
	}
	
	/*
	 * -> End GUI drawing
	 */
	
	

	/*
	 * Output
	 */
	/**
	 * Create a Matrix object of the GUI Element
	 * @return Matrix
	 */
	public Matrix translateToMatrix() {
		int[][] arr = new int[size][size];
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				arr[i][j] = Integer.valueOf((indexButtons[i][j].getText()));
			}
		}
		
		return new Matrix(arr);
	}
	
	/*
	 * -> End Output
	 */
	
}
