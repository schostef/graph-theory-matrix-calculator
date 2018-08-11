/**
 * @author Stefan Schoeberl
 * @version 1.0
 * @modified 2018-08-09
 * 
 * @Class IndexButton
 * Button with coordinates
 */
package gui;

import javafx.scene.control.Button;

public class IndexButton extends Button {

	private int indexRow;
	private int indexCol;
	
	/*
	 * Constructors
	 */
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
	
	/*
	 * -> End Constructors
	 */

	/*
	 * Getters
	 */
	public int getRow() {
		return indexRow;
	}
	
	public int getCol() {
		return indexCol;
	}
	/*
	 * -> End Getters
	 */
	
	/*
	 * Graph Specific
	 */
	
	/**
	 * Create a Button with x and y swapped
	 * @return
	 */
	public IndexButton createMirroredObject() {
		return new IndexButton(getText(),indexCol,indexRow);
	}

	/**
	 * Set 0 to 1 or 1 to 0
	 */
	public void flip() {
		if(Integer.valueOf(getText()) == 1) {
			setText("0");
		}else {
			setText("1");
		}
	}
	
	/*
	 * -> End Graph Specific
	 */
	
	/*
	 * Output
	 */
	public String toString() {
		return indexRow+","+indexCol;
	}
	/*
	 * -> End Output
	 */

}
