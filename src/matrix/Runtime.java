package matrix;

public class Runtime {

	
	public Runtime() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args){
		
		Graph g1 = new Graph(8);
		int[][] a1 = new int[8][8];
		
		a1[0][0] = 0; a1[0][1] = 1; a1[0][2] = 1; a1[0][3] = 1; a1[0][4] = 0; a1[0][5] = 0; a1[0][6] = 0; a1[0][7] = 0;
		a1[1][0] = 1; a1[1][1] = 0; a1[1][2] = 0; a1[1][3] = 1; a1[1][4] = 1; a1[1][5] = 0; a1[1][6] = 0; a1[1][7] = 0;
		a1[2][0] = 1; a1[2][1] = 0; a1[2][2] = 0; a1[2][3] = 1; a1[2][4] = 0; a1[2][5] = 1; a1[2][6] = 0; a1[2][7] = 0;
		a1[3][0] = 1; a1[3][1] = 1; a1[3][2] = 1; a1[3][3] = 0; a1[3][4] = 0; a1[3][5] = 0; a1[3][6] = 1; a1[3][7] = 1;
		a1[4][0] = 0; a1[4][1] = 1; a1[4][2] = 0; a1[4][3] = 0; a1[4][4] = 0; a1[4][5] = 0; a1[4][6] = 0; a1[4][7] = 1;
		a1[5][0] = 0; a1[5][1] = 0; a1[5][2] = 1; a1[5][3] = 0; a1[5][4] = 0; a1[5][5] = 0; a1[5][6] = 1; a1[5][7] = 0;
		a1[6][0] = 0; a1[6][1] = 0; a1[6][2] = 0; a1[6][3] = 1; a1[6][4] = 0; a1[6][5] = 1; a1[6][6] = 0; a1[6][7] = 1;
		a1[7][0] = 0; a1[7][1] = 0; a1[7][2] = 0; a1[7][3] = 1; a1[7][4] = 1; a1[7][5] = 0; a1[7][6] = 1; a1[7][7] = 0;
		
		g1.createA0Matrix(a1);
		
		g1.calculateAdjazenzMatrizen();
		g1.consolePrintAdjazenzmatrizen();
		
		System.out.println(g1.adjazenzMatrizen[0].lineSum(0));
		System.out.println(g1.adjazenzMatrizen[0].rowSum(3));
	}

}
