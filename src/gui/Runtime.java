package gui;

import arraytools.*;
import java.util.Random;

public class Runtime {

	public static void main(String[] args) {
		char[] test = {'F','C','A','B','D','E'};
		test = ArrayTools.sort(test);
		ArrayTools.printOnConsole(test);
	}

}
