package gui;

import java.awt.*;
import javax.swing.*;

public class Gui extends JFrame {
	private final int MIN_VERTEX=3;
	private final int MAX_VERTEX=15;

	public Gui() throws HeadlessException {
		super("Matrix Calculator");
		setSize(800, 600);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		mainMenu();
	}
	
	public void mainMenu() {
		SpinnerModel model;
		model = new SpinnerNumberModel(3, 3, 15, 1);
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLACK);
		getContentPane().add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JSpinner vertexCount = new JSpinner();
		panel.add(vertexCount);
		
		JButton initGraph = new JButton("Start");
		panel.add(initGraph);
		
		
		
	}
	
	

}
