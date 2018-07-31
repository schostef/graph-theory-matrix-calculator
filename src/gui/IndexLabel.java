package gui;

import javafx.scene.control.Label;

public class IndexLabel extends Label {
	private int row, col;

	public IndexLabel(String text, int row, int col) {
		super(text);
		this.row = row;
		this.col = col;
	}
	
	public IndexLabel createMirroredObject() {
		return new IndexLabel(this.getText(),col,row);
	}
}
