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
	
	public ModuleMatrix(Matrix matrix) {
		super();
		readonly = true;
		this.size = matrix.size;
		indexLabels = new IndexLabel[size][size];
		createReadOnlyMatrix(matrix);
		
	}

	private void createReadOnlyMatrix(Matrix matrix) {
		for(int i = 0; i < size; i ++) {
			for(int j = i ; j < size; j++) {
				IndexLabel l1 = new IndexLabel(Integer.toString(matrix.getValueAt(i, j)),i,j);
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
	

	private void createEditableMatrix(int startFrom) {
		int stepper = 0;
		
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
					add(b1, i, i);
					System.out.println("Button: "+b1+" erzeugt");
				}else {
					//!!! Attention !!!
					//My System = [row Index] [column Index]
					//JavaFx Gridpane = [column Index] [row Index]
					
					indexButtons[i][stepper] = b1;
					indexButtons[stepper][i] = b2;
					add(b2, i, stepper);
					//System.out.println("Button: "+b2+" erzeugt");
					add(b1, stepper, i);
					//System.out.println("Button: "+b1+" erzeugt");
					
					indexButtons[i][stepper].setOnAction(event -> {
						flipButtons(b1.getRow(),b1.getCol());
						Vertex v1 = app.getGraph().getVertex(b1.getRow()+1);
						Vertex v2 = app.getGraph().getVertex(b1.getCol()+1);
						if(Integer.parseInt(b1.getText()) == 1) {
							app.getGraph().addEdge(v1,v2);
						}else {
							app.getGraph().deleteEdge(app.getGraph().getEdge(v1,v2));
						}
						app.refreshGraph();
						
					});
					
					indexButtons[stepper][i].setOnAction(event -> {
						flipButtons(b2.getRow(),b2.getCol());
						Vertex v1 = app.getGraph().getVertex(b2.getRow()+1);
						Vertex v2 = app.getGraph().getVertex(b2.getCol()+1);
						if(Integer.parseInt(b1.getText()) == 1) {
							app.getGraph().addEdge(v1,v2);
						}else {
							app.getGraph().deleteEdge(app.getGraph().getEdge(v1,v2));
						}
						app.refreshGraph();
					});
					
				}
				stepper++;
			}
			
		}
		app.refreshGraph();
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
			
			for(int i = 0; i < (newSize - oldSize); i++) {
				app.getGraph().addVertex();
			}
			indexButtons = tempArr;
			createEditableMatrix(oldSize);
		}else {
			getChildren().clear();
			for(int i = 0; i < size; i++) {
				for(int j = 0; j < size; j++) {
					tempArr[i][j] = indexButtons[i][j];
				}
			}
			indexButtons = tempArr;
			for(int i = 0; i < size; i++) {
				for(int j = 0; j < size; j++) {
					add(indexButtons[i][j],j,i);
				}
			}
			
			for(int i = oldSize; i > newSize; i--) {
				app.getGraph().deleteVertex(app.getGraph().getVertex(i));
			}
		}
		
		app.refreshGraph();
		
	}
	
	private void flipButtons(int i, int j) {
		indexButtons[i][j].flip();
		indexButtons[j][i].flip();
		
	}

	public Matrix translateToMatrix() {
		int[][] arr = new int[size][size];
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; i++) {
				arr[i][j] = Integer.valueOf((indexButtons[i][j].getText()));
			}
		}
		
		return new Matrix(arr);
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
