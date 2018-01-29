package matrix;

public class Vector extends Matrix {

	private int[] vector;
	public Vector() {
		// TODO Auto-generated constructor stub
	}

	public Vector(int[] vector, int size) {
		this.vector = vector;
		this.size = size;
	}

	public Vector(int size) {
		this.size = size;
		vector = new int[size]; 
	}
	
	public int rowSum() {
		int sum = 0;
		for (int i = 0; i < size; i++) {
			sum += vector[i];
		}
		
		return sum;
	}

}
