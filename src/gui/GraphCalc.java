/**
 * @author Stefan Schoeberl
 * @version 1.0
 * @modified 2018-08-09
 * 
 * @Class GraphCalc
 * Main Application
 */

package gui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import graph.Graph;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import matrix.Matrix;

public class GraphCalc extends Application {
	
	/*
	 * Config
	 */
	private static final int VERTEX_MIN = 3;
	private static final int VERTEX_MAX = 15;
	private static final int VERTEX_INIT = 5;
	private static final double MAIN_WINDOW_X_POSITION = 50.0;
	private static final double MAIN_WINDOW_Y_POSITION = 50.0;
	private static final double MAIN_WINDOW_X_SIZE = 1280.0;
	private static final double MAIN_WINDOW_Y_SIZE = 720.0;
	private static final String MAIN_WINDOW_TITLE = "Graphen Kalkulation";
	/*
	 * -> End Config
	 */	
	
	private Graph graph;
	private BorderPane mainWindow;
	private TextArea infoArea;
	private ModuleMatrix aMatrix,pMatrix,dMatrix;
	private VBox matrixBox,dpMatrixBox,vbD,vbP;
	private ArrayList<VBox> dpMatrix;
	private Button calculateButton;
	private Label lPMatrix = new Label("\nWegmatrix\n");
	private Label lDMatrix = new Label("\nDistanzmatrix\n");
	ProgressBar bar = new ProgressBar();
	private Random rand = new Random();
	PrintWriter out; 

	
	@Override
	public void start(Stage primaryStage) {
		//Menu Bar
		mainWindow = new BorderPane();
		MenuBar menuBar = new MenuBar();		
		Menu menuFile = new Menu("Datei");
		MenuItem mNew = new MenuItem("Neuer Graph");
		MenuItem mRand = new MenuItem("Neuer Zufallsgraph");
		menuFile.getItems().add(mNew);
		menuFile.getItems().add(mRand);				
		menuBar.getMenus().add(menuFile);		
		mainWindow.setTop(menuBar);
		mNew.setOnAction(event -> {
			initializeGraphWindow();
		});		
		mRand.setOnAction(event -> {
			initializeRandomGraphWindow();
		});
		
		//Text Area for Graph information
		infoArea = new TextArea();
		infoArea.setEditable(false);
		mainWindow.setCenter(infoArea);		
		
		//Stage
		primaryStage.setScene(new Scene(mainWindow));
		primaryStage.show();
		primaryStage.setTitle(MAIN_WINDOW_TITLE);
		primaryStage.setX(MAIN_WINDOW_X_POSITION);
		primaryStage.setY(MAIN_WINDOW_Y_POSITION);
		primaryStage.setHeight(MAIN_WINDOW_Y_SIZE);
		primaryStage.setWidth(MAIN_WINDOW_X_SIZE);
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter("graph.log")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Getters
	 */
	public Graph getGraph() {
		return graph;
	}
	
	/*
	 * -> End Getters
	 */
	
	/**
	 * Create a random Graph
	 */
	private void initializeRandomGraphWindow() {
		//Randomize an adjacency Matrix
		int mSize = rand.nextInt(13);
		mSize += 3;
		int[][] mFill = new int[mSize][mSize];
		for(int i = 0; i < mSize; i++) {
			for(int j = i; j < mSize; j++) {
				if(i != j) {
					int v = rand.nextInt(2);
					mFill[i][j] = v;
					mFill[j][i] = v;
				}
				
			}
		}
		Matrix matrix = new Matrix(mFill);
		
		//Put the adjacency Matrix, calculate and randomize button on the left side of the Borderpane
		aMatrix = new ModuleMatrix(mSize);
		aMatrix.createEditableMatrix(matrix);
		calculateButton = new Button("Berechnen");
		Button shuffleButton = new Button("Zufall");
		Spinner<Integer> vertexAmount = new Spinner<>(VERTEX_MIN, VERTEX_MAX, mSize);
		HBox buttons = new HBox(calculateButton,shuffleButton);
		matrixBox = new VBox(buttons,vertexAmount,aMatrix);
		
		vertexAmount.valueProperty().addListener(event -> {
			aMatrix.resizeEditableMatrix(vertexAmount.getValue());
		});
		
		calculateButton.setOnAction(event -> {
			graph = new Graph(aMatrix.translateToMatrix());
			refreshGraph();
		});
		
		shuffleButton.setOnAction(event -> {
			/* Thousand graph test
			for(int i = 0; i < 1000; i++) {
				long start = System.currentTimeMillis();
				initializeRandomGraphWindow();
				long end = System.currentTimeMillis();
				long elapsed = (end - start)/1000;
				out.println("Graph Nr.: "+i+"\n "+ "Time: "+ elapsed + " Seconds");
				
				if(graph.getEulerPath() != null) {
										
					out.println(graph.getAdjacencyMatrix(1));
					out.println("-----------------------------");
					out.println(graph+"\n\n");
				}				
			}
			*/			
			initializeRandomGraphWindow();
			
		});
		
		//Put distance and Path Matrix on the right side of the borderpane
		dMatrix = new ModuleMatrix(new Matrix(mSize),true);
		pMatrix = new ModuleMatrix(new Matrix(mSize),true);
		vbD = new VBox(lDMatrix,dMatrix);
		vbP = new VBox(lPMatrix,pMatrix);
		dpMatrix = new ArrayList<>();		
		dpMatrixBox = new VBox();
		dpMatrixBox.getChildren().addAll(dpMatrix);		
		
		//Attach to the borderpane
		mainWindow.setLeft(matrixBox);
		mainWindow.setRight(dpMatrixBox);
		
		//Create new Graph with random Matrix	
		graph = new Graph(matrix);
		refreshGraph();		
	}

	/**
	 * Create a new empty custom Graph
	 */
	private void initializeGraphWindow() {
		//Adjacency Matrix with buttons and spinner
		aMatrix = new ModuleMatrix(VERTEX_INIT);
		calculateButton = new Button("Berechnen");
		Spinner<Integer> vertexAmount = new Spinner<>(VERTEX_MIN, VERTEX_MAX, VERTEX_INIT);
		matrixBox = new VBox(calculateButton,vertexAmount,aMatrix);
		vertexAmount.valueProperty().addListener(event -> {
			aMatrix.resizeEditableMatrix(vertexAmount.getValue());
		});
		
		//When button calculate is pressed make a new graph
				calculateButton.setOnAction(event -> {
					graph = new Graph(aMatrix.translateToMatrix());
					refreshGraph();
				});
		
		
		//Path and distance matrix		
		dMatrix = new ModuleMatrix(new Matrix(VERTEX_INIT),true);
		pMatrix = new ModuleMatrix(new Matrix(VERTEX_INIT),true);
		vbD = new VBox(lDMatrix,dMatrix);
		vbP = new VBox(lPMatrix,pMatrix);
		dpMatrix = new ArrayList<>();		
		dpMatrixBox = new VBox();
		dpMatrixBox.getChildren().addAll(dpMatrix);		
		
		//Attach to borderpane
		mainWindow.setLeft(matrixBox);
		mainWindow.setRight(dpMatrixBox);		
		
	}
	
	/**
	 * Redraw the GUI elements
	 */
	public void refreshGraph() {
		dpMatrixBox.getChildren().removeAll(dpMatrix);
		dpMatrix.clear();
		pMatrix = new ModuleMatrix(graph.getPathMatrix(),true);
		dMatrix = new ModuleMatrix(graph.getDistanceMatrix(),true);
		vbD.getChildren().clear();
		vbD.getChildren().add(lDMatrix);
		vbD.getChildren().add(dMatrix);
		vbP.getChildren().clear();
		vbP.getChildren().add(lPMatrix);
		vbP.getChildren().add(pMatrix);
		dpMatrix.add(vbD);
		dpMatrix.add(vbP);
		dpMatrixBox.getChildren().addAll(dpMatrix);
		
		infoArea.setText(graph.toString());
		
	}	

	public static void main(String[] args) {
		launch(args);
	}
	
}
