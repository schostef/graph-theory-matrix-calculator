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
		
		int[] r1 = {0,1,1,0,1};
		int[] r2 = {1,0,0,1,0};
		int[] r3 = {1,0,0,0,1};
		int[] r4 = {0,1,0,0,1};
		int[] r5 = {1,0,1,1,0};
		int[][] m = {r1,r2,r3,r4,r5};
		
		Matrix ma = new Matrix(m);
		Graph graph = new Graph(ma);
		graph.calculateExponentialMatrizes();
		System.out.println(graph.getAdjacencyMatrix(1));
		System.out.println(graph.getAdjacencyMatrix(2));
		System.out.println(graph.getAdjacencyMatrix(3));
		System.out.println(graph.getAdjacencyMatrix(4));
		System.out.println(graph);
		
		
	}

}
