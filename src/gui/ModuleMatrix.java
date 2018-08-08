package gui;

import graph.Vertex;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import matrix.Matrix;

public class ModuleMatrix extends GridPane {
	
	private IndexButton[][] indexButtons;
	private IndexLabel[][] indexLabels;
	private boolean readonly;
	private int size;
	private GraphCalc app;
	
	public ModuleMatrix(int size, GraphCalc app) {
		super();
		this.app = app;
		readonly = false;
		this.size = size;
		indexButtons = new IndexButton[size][size];
		createEditableMatrix(0);
		
	}
	
	public ModuleMatrix(Matrix matrix, boolean readonly) {
		super();
		this.readonly = readonly;
		this.size = matrix.size;
		if(readonly) {
			indexLabels = new IndexLabel[size][size];
			createReadOnlyMatrix(matrix);
		}else {
			createEditableMatrix(matrix);
		}
		
		
	}

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
		//redraw();
		this.setGridLinesVisible(true);
		
	}
	
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
					//!!! Attention !!!
					//My System = [row Index] [column Index]
					//JavaFx Gridpane = [column Index] [row Index]
					indexButtons[i][stepper] = b1;
					indexButtons[stepper][i] = b2;
					add(b2, i+1, stepper+1);
					//System.out.println("Button: "+b2+" erzeugt");
					add(b1, stepper+1, i+1);
					//System.out.println("Button: "+b1+" erzeugt");
					
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
		//app.refreshGraph();
		//redraw();
		
	}
	
	private void redraw() {
		getChildren().clear();
		
		if(readonly) {
			for(int i = 0; i < size; i++) {
				for(int j = 0; j < size; j++) {
					add(indexLabels[i][j],i,j);
				}
			}
		}else {
			for(int i = 0; i < size; i++) {
				for(int j = 0; j < size; j++) {
					add(indexButtons[i][j],i,j);
				}
			}
		}
		
	}
	
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
	
	private void flipButtons(int i, int j) {
		indexButtons[i][j].flip();
		indexButtons[j][i].flip();
		
	}

	public Matrix translateToMatrix() {
		int[][] arr = new int[size][size];
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				arr[i][j] = Integer.valueOf((indexButtons[i][j].getText()));
			}
		}
		
		return new Matrix(arr);
	}
	
	public boolean consistencyCheck(Matrix m1) {
		m1.vectorize();
		Matrix m2 = translateToMatrix();
		m2.vectorize();
		return m1.isIdentical(m2);
	}
	
	public void refreshMatrix(Matrix matrix) {
		for(int i = 0; i < size; i ++) {
			for(int j = i ; j < size; j++) {
				
				if(Integer.valueOf(indexLabels[i][j].getText()) != matrix.getValueAt(i, j)) {
					if(i == j) {
						indexLabels[i][i].setText(Integer.toString(matrix.getValueAt(i, i)));
					}else {
						indexLabels[i][j].setText(Integer.toString(matrix.getValueAt(i, j)));
						indexLabels[j][i].setText(Integer.toString(matrix.getValueAt(j, i)));
					}
				}
				
			}
			
		}
	}
	
	public void resize(int size) {
		
	}
	
}
