package gui;

import arraytools.*;
import graph.Graph;
import matrix.*;

import java.awt.EventQueue;
import java.util.Random;

import javax.swing.JFrame;

public class Runtime{

	

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		/*
		Graph graph = new Graph();
		graph.addVertex();
		graph.addVertex();
		graph.addVertex();
		graph.addVertex();
		graph.addVertex();
		
		graph.addEdge(graph.getVertex(1), graph.getVertex(2));
		graph.addEdge(graph.getVertex(1), graph.getVertex(3));
		graph.addEdge(graph.getVertex(1), graph.getVertex(5));
		graph.addEdge(graph.getVertex(2), graph.getVertex(1));
		graph.addEdge(graph.getVertex(2), graph.getVertex(4));
		graph.addEdge(graph.getVertex(3), graph.getVertex(1));
		graph.addEdge(graph.getVertex(3), graph.getVertex(5));
		graph.addEdge(graph.getVertex(4), graph.getVertex(2));
		graph.addEdge(graph.getVertex(4), graph.getVertex(5));
		graph.addEdge(graph.getVertex(5), graph.getVertex(1));
		graph.addEdge(graph.getVertex(5), graph.getVertex(3));
		graph.addEdge(graph.getVertex(5), graph.getVertex(4));
		
		graph.createAdjacencyMatrix();
		System.out.println(graph.getAdjacencyMatrix(1));
		*/
		
		
		int[] r1 = {0,1,1,1,1};
		int[] r2 = {1,0,1,1,0};
		int[] r3 = {1,1,0,1,1};
		int[] r4 = {1,1,1,0,1};
		int[] r5 = {1,0,1,1,0};
		int[][] m = {r1,r2,r3,r4,r5};
		
		
		
		/*
		int[] r1 =  {0,1,0,0,1,0,0,0,0,0,0,0,0};
		int[] r2 =  {1,0,0,0,1,1,0,0,0,0,0,0,0};
		int[] r3 =  {0,0,0,0,0,1,1,0,0,0,0,0,0};
		int[] r4 =  {0,0,0,0,1,1,0,0,0,0,0,0,0};
		int[] r5 =  {1,1,0,1,0,1,0,0,0,0,0,0,0};
		int[] r6 =  {0,1,1,1,1,0,1,1,1,1,0,0,0};
		int[] r7 =  {0,0,1,0,0,1,0,0,0,1,0,0,0};
		int[] r8 =  {0,0,0,0,0,1,0,0,1,0,0,0,0};
		int[] r9 =  {0,0,0,0,0,1,0,1,0,0,0,0,0};
		int[] r10 = {0,0,0,0,0,1,1,0,0,0,1,0,0};
		int[] r11 = {0,0,0,0,0,0,0,0,0,1,0,1,0};
		int[] r12 = {0,0,0,0,0,0,0,0,0,0,1,0,0};
		int[] r13 = {0,0,0,0,0,0,0,0,0,0,0,0,0};
		int[][] m = {r1,r2,r3,r4,r5,r6,r7,r8,r9,r10,r11,r12,r13};
		*/
		
		
		Matrix ma = new Matrix(m);
		Graph graph = new Graph(ma);
		graph.calculateExponentialMatrizes();
		graph.initializeDistanceMatrix();
		graph.initializePathMatrix();
		graph.calculateDistancePathMatrix();
		graph.calculateEccentricities();
		graph.findArticulations();
		graph.findBridges();
		graph.findEulerPath();
		//graph.findBlocks();
		System.out.println("Adjazenzmatrix: \n"+graph.getAdjacencyMatrix(1));
		//System.out.println("A^2: \n"+graph.getAdjacencyMatrix(2));
		//System.out.println("A^3: \n"+graph.getAdjacencyMatrix(3));
		//System.out.println("A^4: \n"+graph.getAdjacencyMatrix(4));
		//System.out.println("A^5: \n"+graph.getAdjacencyMatrix(5));
		//System.out.println("A^6: \n"+graph.getAdjacencyMatrix(6));
		//System.out.println("A^7: \n"+graph.getAdjacencyMatrix(7));
		System.out.println("Wegmatrix: \n"+graph.getPathMatrix());
		System.out.println("Distanzmatrix: \n"+graph.getDistanceMatrix());
		System.out.println("Kontrollmatrix: \n"+graph.getControlMatrix());
		System.out.println(graph);
		/*
		int[] r1 = {0,1,1,0,1};
		int[] r2 = {1,0,0,1,0};
		int[] r3 = {1,0,0,0,1};
		int[] r4 = {0,1,0,0,1};
		int[] r5 = {1,0,1,1,0};
		int[][] m = {r1,r2,r3,r4,r5};
		Matrix ma = new Matrix(m);
		
		Matrix bm = Matrix.searchAndReport(ma, 1);
		System.out.println(ma);
		System.out.println(bm);
		*/
		

		
		
	}
	
	

}
