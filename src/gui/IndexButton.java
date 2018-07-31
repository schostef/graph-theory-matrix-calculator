package gui;

import javafx.scene.Node;
import javafx.scene.control.Button;

public class IndexButton extends Button {

	private int indexRow;
	private int indexCol;
	
	public IndexButton(int row, int col) {
		super("0");
		indexRow = row;
		indexCol = col;
	}

	public IndexButton(String value, int row, int col) {
		super(value);
		indexRow = row;
		indexCol = col;
	}
	
	public int getRow() {
		return indexRow;
	}
	
	public int getCol() {
		return indexCol;
	}
	
	public IndexButton createMirroredObject() {
		return new IndexButton(getText(),indexCol,indexRow);
	}

	public void flip() {
		if(Integer.valueOf(getText()) == 1) {
			setText("0");
		}else {
			setText("1");
		}
	}
	
	public String toString() {
		return indexRow+","+indexCol;
	}


}
