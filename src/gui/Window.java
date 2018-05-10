package gui;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Action;

public class Window extends JFrame implements ActionListener{
	JPanel panel;
	JLabel lblKnotenanzahl;
	JSpinner vertexAmount;
	JButton createGraph;

	/**
	 * Create the application.
	 */
	public Window() {
		super();
		
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		newGraphMenu();
		
	}
	
	private void newGraphMenu() {
		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		
		lblKnotenanzahl = new JLabel("Knotenanzahl: ");
		panel.add(lblKnotenanzahl);
		
		vertexAmount = new JSpinner();
		vertexAmount.setModel(new SpinnerNumberModel(3, 3, 15, 1));
		panel.add(vertexAmount);
		
		createGraph = new JButton("Start");
		panel.add(createGraph);
		createGraph.addActionListener(this);
	}
	
	private void hideNewGraphMenu() {
		panel.setVisible(false);
	}
	
	private void showAdjacencyMatrix(int vertizes) {
		ArrayList matrix = new ArrayList<JButton>();
		JPanel aMatrixPanel = new JPanel();
		getContentPane().add(aMatrixPanel, BorderLayout.CENTER);
		
		for(int i = 0; i < vertizes; i++) {
			for(int j = 0; j < vertizes; j++) {
				//matrix.add(new JButton("F"));
				aMatrixPanel.add(new JButton("F"));
			}
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Start")) {
			hideNewGraphMenu();
			showAdjacencyMatrix((int)vertexAmount.getValue());
		}
		
	}
	
}
