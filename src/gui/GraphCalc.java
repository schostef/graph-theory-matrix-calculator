package gui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Timestamp;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import graph.Graph;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import matrix.Matrix;

public class GraphCalc extends Application {

	private static final int VERTEX_MIN = 3;
	private static final int VERTEX_MAX = 15;
	private static final int VERTEX_INIT = 5;
	
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
		
		mainWindow = new BorderPane();
		MenuBar menuBar = new MenuBar();
		
		
		Menu menuFile = new Menu("Datei");
		MenuItem mNew = new MenuItem("Neuer Graph");
		MenuItem mRand = new MenuItem("Neuer Zufallsgraph");
		menuFile.getItems().add(mNew);
		menuFile.getItems().add(mRand);
				
		mNew.setOnAction(event -> {
			initializeGraphWindow();
		});
		
		mRand.setOnAction(event -> {
			initializeRandomGraphWindow();
		});
				
		menuBar.getMenus().add(menuFile);
		
		mainWindow.setTop(menuBar);		
		
		infoArea = new TextArea();
		infoArea.setEditable(false);
		mainWindow.setCenter(infoArea);		
		
		primaryStage.setScene(new Scene(mainWindow));
		primaryStage.show();
		primaryStage.setTitle("Graph Kalkulation");
		primaryStage.setX(50);
		primaryStage.setY(50);
		primaryStage.setHeight(720);
		primaryStage.setWidth(1280);
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter("graph.log")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Graph getGraph() {
		return graph;
	}
	
	private void initializeRandomGraphWindow() {
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
		
		aMatrix = new ModuleMatrix(mSize,this);
		aMatrix.createEditableMatrix(matrix);
		dMatrix = new ModuleMatrix(new Matrix(mSize),true);
		pMatrix = new ModuleMatrix(new Matrix(mSize),true);
		vbD = new VBox(lDMatrix,dMatrix);
		vbP = new VBox(lPMatrix,pMatrix);
		dpMatrix = new ArrayList<>();
		calculateButton = new Button("Berechnen");
		Button shuffleButton = new Button("Zufall");
		Spinner<Integer> vertexAmount = new Spinner<>(VERTEX_MIN, VERTEX_MAX, mSize);
		HBox buttons = new HBox(calculateButton,shuffleButton);
		matrixBox = new VBox(buttons,vertexAmount,aMatrix);
		dpMatrixBox = new VBox();
		dpMatrixBox.getChildren().addAll(dpMatrix);
		
		vertexAmount.valueProperty().addListener(event -> {
			aMatrix.resizeEditableMatrix(vertexAmount.getValue());
		});
		mainWindow.setLeft(matrixBox);
		mainWindow.setRight(dpMatrixBox);
		
		calculateButton.setOnAction(event -> {
			graph = new Graph(aMatrix.translateToMatrix());
			refreshGraph();
		});
		
		shuffleButton.setOnAction(event -> {
			/* Tausend Graphen Test:
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
		
		graph = new Graph(matrix);
		refreshGraph();
		
	}

	private void initializeGraphWindow() {
		aMatrix = new ModuleMatrix(VERTEX_INIT,this);
		dMatrix = new ModuleMatrix(new Matrix(VERTEX_INIT),true);
		pMatrix = new ModuleMatrix(new Matrix(VERTEX_INIT),true);
		vbD = new VBox(lDMatrix,dMatrix);
		vbP = new VBox(lPMatrix,pMatrix);
		dpMatrix = new ArrayList<>();
		calculateButton = new Button("Berechnen");
		Spinner<Integer> vertexAmount = new Spinner<>(VERTEX_MIN, VERTEX_MAX, VERTEX_INIT);
		matrixBox = new VBox(calculateButton,vertexAmount,aMatrix);
		dpMatrixBox = new VBox();
		dpMatrixBox.getChildren().addAll(dpMatrix);
		
		vertexAmount.valueProperty().addListener(event -> {
			aMatrix.resizeEditableMatrix(vertexAmount.getValue());
		});
		mainWindow.setLeft(matrixBox);
		mainWindow.setRight(dpMatrixBox);
		
		calculateButton.setOnAction(event -> {
			graph = new Graph(aMatrix.translateToMatrix());
			refreshGraph();
		});
		
	}
	
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
