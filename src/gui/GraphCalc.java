package gui;

import graph.Graph;
import javafx.application.Application;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import matrix.Matrix;

public class GraphCalc extends Application {

	private static final int VERTEX_MIN = 3;
	private static final int VERTEX_MAX = 15;
	private static final int VERTEX_INIT = 5;
	
	private Graph graph;
	private BorderPane mainWindow;
	private TextArea infoArea;

	@Override
	public void start(Stage primaryStage) {
		mainWindow = new BorderPane();
		MenuBar menuBar = new MenuBar();
		
		
		Menu menuFile = new Menu("Datei");
		MenuItem mNew = new MenuItem("Neuer Graph");
		MenuItem mRand = new MenuItem("Neuer Zufallsgraph");
		MenuItem mLoad = new MenuItem("Graph laden");
		MenuItem mSave = new MenuItem("Graph speichern");
		menuFile.getItems().add(mNew);
		menuFile.getItems().add(mRand);
		menuFile.getItems().add(mLoad);
		menuFile.getItems().add(mSave);
		
		Menu menuView = new Menu("Ansicht");
		
		
		
		
		mNew.setOnAction(event -> {
			graph = new Graph(new Matrix(VERTEX_INIT));
			initializeGraphWindow();
		});
				
		menuBar.getMenus().add(menuFile);
		menuBar.getMenus().add(menuView);
		
		mainWindow.setTop(menuBar);
		
		
		infoArea = new TextArea();
		infoArea.setEditable(false);
		mainWindow.setCenter(infoArea);
		
		
		
		primaryStage.setScene(new Scene(mainWindow));
		primaryStage.show();
	}
	
	public Graph getGraph() {
		return graph;
	}

	private void initializeGraphWindow() {
		ModuleMatrix aMatrix = new ModuleMatrix(VERTEX_INIT,this);
		Spinner<Integer> vertexAmount = new Spinner<>(VERTEX_MIN, VERTEX_MAX, VERTEX_INIT);
		VBox matrixBox = new VBox(vertexAmount,aMatrix);
		
		vertexAmount.valueProperty().addListener(event -> {
			aMatrix.resizeEditableMatrix(vertexAmount.getValue());
		});
		mainWindow.setLeft(matrixBox);
		
	}
	
	public void refreshGraph() {
		graph.calculateAll();
		infoArea.setText(graph.toString());
		
	}

	public static void main(String[] args) {
		launch(args);
	}

	
}
