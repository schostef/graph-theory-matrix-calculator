package gui;

import arraytools.*;
import graph.Graph;
import matrix.*;
import java.util.Random;

public class Runtime {

	public static void main(String[] args) {
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
		
		/*
		int[] r1 = {0,1,1,0,1};
		int[] r2 = {1,0,0,1,0};
		int[] r3 = {1,0,0,0,1};
		int[] r4 = {0,1,0,0,1};
		int[] r5 = {1,0,1,1,0};
		int[][] m = {r1,r2,r3,r4,r5};
		*/
		
		int[] r1 = {0,1,0,0,1,0};
		int[] r2 = {1,0,1,0,1,1};
		int[] r3 = {0,1,0,1,0,1};
		int[] r4 = {0,0,1,0,0,0};
		int[] r5 = {1,1,0,0,0,0};
		int[] r6 = {0,1,1,0,0,0};
		int[][] m = {r1,r2,r3,r4,r5,r6};
		
		Matrix ma = new Matrix(m);
		Graph graph = new Graph(ma);
		graph.calculateExponentialMatrizes();
		graph.initializeDistanceMatrix();
		graph.initializePathMatrix();
		graph.calculateDistancePathMatrix();
		graph.calculateEccentricities();
		graph.findBlocks();
		System.out.println("Adjazenzmatrix: \n"+graph.getAdjacencyMatrix(1));
		System.out.println("A^2: \n"+graph.getAdjacencyMatrix(2));
		System.out.println("A^3: \n"+graph.getAdjacencyMatrix(3));
		System.out.println("A^4: \n"+graph.getAdjacencyMatrix(4));
		System.out.println("A^5: \n"+graph.getAdjacencyMatrix(5));
		System.out.println("A^6: \n"+graph.getAdjacencyMatrix(6));
		System.out.println("A^7: \n"+graph.getAdjacencyMatrix(7));
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
