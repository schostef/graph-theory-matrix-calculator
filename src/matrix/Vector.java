package matrix;

public class Vector extends Matrix {

	private int[] vector;

	public Vector(int[] vector) {
		this.vector = vector;
		this.size = this.vector.length;
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
