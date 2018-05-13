package gui;

import graph.Graph;
import matrix.Matrix;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Window extends JFrame implements ActionListener,ChangeListener{
	JInternalFrame panel;
	JLabel lblKnotenanzahl;
	JSpinner vertexAmount;
	JButton createGraph;
	private ArrayList<ArrayList> matrixRows;
	private int gridSize =3;
	private JSpinner vSpinner;
	private JPanel aMatrix;
	private JTextPane outputBox;

	/**
	 * Create the application.
	 */
	public Window() {
		super();
		getContentPane().setBackground(Color.ORANGE);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setBounds(100, 100, 1024, 768);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//newGraphMenu();
		showAdjacencyMatrix(3);
		
	}
	
	private void newGraphMenu() {
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel = new JInternalFrame("Neuer Graph");
		getContentPane().add(panel);
		
		createGraph = new JButton("Start");
		createGraph.addActionListener(this);
		
		lblKnotenanzahl = new JLabel("Knotenanzahl: ");
		
		vertexAmount = new JSpinner();
		vertexAmount.setModel(new SpinnerNumberModel(3, 3, 15, 1));
		panel.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.getContentPane().add(createGraph);
		panel.getContentPane().add(lblKnotenanzahl);
		panel.getContentPane().add(vertexAmount);
		
	}
	
	private void hideNewGraphMenu() {
		panel.setVisible(false);
	}
	
	private void showAdjacencyMatrix(int vertizes) {
		
		JPanel topMenu = (JPanel)topMenu(vertizes);
		drawAMatrix(vertizes);
		getContentPane().add(aMatrix);
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		getContentPane().add(topMenu);
		
		JButton btnBerechnen = new JButton("Berechnen");
		getContentPane().add(btnBerechnen);
		btnBerechnen.addActionListener(this);
		
		JPanel infoPanel = new JPanel();
		getContentPane().add(infoPanel);
		
		outputBox = new JTextPane();
		
		infoPanel.add(outputBox);
		
		
		
		
		/*
		ArrayList matrix = new ArrayList<JButton>();
		JPanel aMatrixPanel = new JPanel();
		getContentPane().add(aMatrixPanel, BorderLayout.CENTER);
		
		for(int i = 0; i < vertizes; i++) {
			for(int j = 0; j < vertizes; j++) {
				//matrix.add(new JButton("F"));
				aMatrixPanel.add(new JButton("F"));
			}
		}
		*/
		
	}
	
	private void drawAMatrix(int gridSize) {
		LayoutManager layout; 
		layout = new GridLayout(0,gridSize+1,0,0);
		
		
		if(matrixRows == null) {
			aMatrix = new JPanel();
			aMatrix.setLayout(layout);
			
			matrixRows = new ArrayList<>();				
			aMatrix.add(new JLabel(""));
			for(int i = 0; i < gridSize; i++) {
				aMatrix.add(new JLabel(Integer.toString(i+1)));
				
			}			
			
			for(int i = 0; i < gridSize; i++) {
				ArrayList<JToggleButton> bMatrix = new ArrayList<>();
				aMatrix.add(new JLabel(Integer.toString(i+1)));
				
				for(int j = 0; j < gridSize; j++) {
					
					bMatrix.add(new JToggleButton("0"));
					bMatrix.get(j).addActionListener(this);
					if(i == j) {					
						bMatrix.get(j).setEnabled(false);
					}
					bMatrix.get(j).setName(i +","+j);
					aMatrix.add(bMatrix.get(j));
				
				}
				
				matrixRows.add(bMatrix);
				
			}
			

		}else if(gridSize > this.gridSize) {
			aMatrix.invalidate();
			aMatrix.removeAll();
			aMatrix.setLayout(layout);
			aMatrix.add(new JLabel(""));
			for(int i = 0; i < gridSize; i++) {
				aMatrix.add(new JLabel(Integer.toString(i+1)));
				
			}
			matrixRows.add(new ArrayList<>());
			for (int i = 0; i < gridSize; i++) {
				aMatrix.add(new JLabel(Integer.toString(i+1)));
				for (int j = 0; j < gridSize; j++) {
					JToggleButton b = new JToggleButton("0");
					if(i == gridSize-1) {						
						b.setName(i+","+j);
						b.addActionListener(this);
						matrixRows.get(i).add(b);
						aMatrix.add(b);
					}else {
						if(j < gridSize-1) {
							aMatrix.add((JToggleButton)matrixRows.get(i).get(j));
						}else {
							b.setName(i+","+j);
							b.addActionListener(this);
							matrixRows.get(i).add(b);
							aMatrix.add(b);
						}
					}
					if(i == j) {
						b.setEnabled(false);
					}
				}
			}
			/*
			for (ArrayList<JToggleButton> a : matrixRows) {
				a.add(new JToggleButton("0"));
				a.get(a.size()-1).addActionListener(this);
				a.get(a.size()-1).setName((matrixRows.indexOf(a)+1)+","+a.size());
				
				for (JToggleButton b : a) {
					aMatrix.add(b);
				}
			}*/
			
		}else if(gridSize < this.gridSize) {
			
		}
		this.gridSize = gridSize;
		aMatrix.revalidate();
		
		
		
	}
	
	private Component topMenu(int gridSize) {
		JPanel menuPanel = new JPanel();
		vSpinner = new JSpinner();
		vSpinner.setModel(new SpinnerNumberModel(gridSize, 3, 15, 1));
		vSpinner.setPreferredSize(new Dimension(50, 50));
		vSpinner.setFont(new Font("Tahoma", Font.BOLD, 16));
		menuPanel.add(vSpinner);
		vSpinner.addChangeListener(this);
		return menuPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Start")) {
			hideNewGraphMenu();
			showAdjacencyMatrix((int)vertexAmount.getValue());
		}
		
		if(e.getSource().getClass().toString().equals("class javax.swing.JToggleButton")) {
			JToggleButton button = (JToggleButton)e.getSource();
			JToggleButton button2 = null;
			String buttonName = button.getName();
			for (int i = 0; i < matrixRows.size(); i++) {
				ArrayList<JToggleButton> b = matrixRows.get(i);
				for(int j = 0; j < b.size(); j++) {
					if(b.get(j).equals(button)) {
						button2 = (JToggleButton)matrixRows.get(j).get(i);
					}
				}
			}
			System.out.println(button.getName());
			if(button.getText().equals("0")) {
				button.setText("1");
				button2.setText("1");
				button2.setSelected(true);
				
			}else {
				button.setText("0");
				button2.setText("0");
				button2.setSelected(false);
				
			}
		}
		
		if(e.getActionCommand().equals("Berechnen")) {
			int[][] t = aMatrixToArray();
			Matrix m = new Matrix(t);
			
			Graph g = new Graph(m);			
			g.calculateAll();
			System.out.println(g);
			outputBox.setText(g.toString());
			
		}
		
		/*
		JToggleButton button = (JToggleButton)e.getSource();
		System.out.println(button.getName());
		if(e.getActionCommand().equals("0")) {
			//System.out.println(e.toString());
		}
		*/
		
	}
	
	private int[][] aMatrixToArray() {
		int[][] arr = new int[gridSize][gridSize];
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				JToggleButton b = (JToggleButton) matrixRows.get(i).get(j);
				arr[i][j] = Integer.parseInt(b.getText());
			}
		}
		
		return arr;
	}

	public void stateChanged(ChangeEvent arg0) {
		if(arg0.getSource() == vSpinner) {
			
			int temp = (int)vSpinner.getValue();
			//getContentPane().removeAll();
			//repaint();
			drawAMatrix(temp);
			//revalidate();
			
			//refresh((int)vSpinner.getValue());			
			//gMatrix = gMatrix((int)vSpinner.getValue());
			//this.add(gMatrix);
			//parentWindow.repaint();
			//refresh();
			//System.out.println(gMatrix);
		}
		
	}
	
}
