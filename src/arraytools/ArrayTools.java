package arraytools;

public class ArrayTools {

	
	
 
    /*
	 * *******************************************************
	 * HOARE: Sortierung nach dem Hoare Partitionierungsalgorithmus
	 * *******************************************************
	 */
    /*
     * sort
     * Übernimmt ein int array
     * Prüfung auf befüllung, sonst abbruch
     * wenn daten vorhanden weitergabe an quicksort
     */
    public static void sort(int[] inputArr) {
    	int array[] = inputArr;
        int length = inputArr.length;
        if (inputArr == null || inputArr.length == 0) {
            return;
        }
     
        array = quickSort(inputArr,0, length - 1);
    }
 
    /*
     * Derzeit wird standardmäßig das gesamte Array sortiert.
     * Es soll aber die Möglichkeit vorhanden sein auch nur Teile des
     * Arrays zu sortieren, lower und higher index geben diese Grenze an
     * Als Pivot wird die Mitte des Arrays verwendet. Es kann jede Zahl
     * als Pivot verwendet werden. Die Erfolgsquote der Sortierung ist vom Wert des Pivots abhängig.
     * Worst case ist ein bereits sortierter Array. Dies sollte vorab vermieden werden.
     */
    private static int[] quickSort(int[] array, int lowerIndex, int higherIndex) {
         
        int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot number, I am taking pivot as middle index number
        int pivot = array[lowerIndex+(higherIndex-lowerIndex)/2];
        // Divide into two arrays
        while (i <= j) {
            /**
             * In each iteration, we will identify a number from left side which 
             * is greater then the pivot value, and also we will identify a number 
             * from right side which is less then the pivot value. Once the search 
             * is done, then we exchange both numbers.
             */
            while (array[i] < pivot) {
                i++;
            }
            while (array[j] > pivot) {
                j--;
            }
            if (i <= j) {
                exchangeNumbers(i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j)
            quickSort(lowerIndex, j);
        if (i < higherIndex)
            quickSort(i, higherIndex);
    }
 
    private static void exchangeNumbers(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
     
    /*
     * ******************************************************************
     * END HOARE
     * ******************************************************************
     */
    
    /*
     * ******************************************************************
     * SORTIERT: Prüfe, ob ein Array bereits sortiert ist
     * returns: boolean
     * ******************************************************************
     */
    	public static boolean isSorted(int[] inputArr) {
    		
    		for (int i = 0; i < inputArr.length; i++) {
    			if(inputArr[i] > inputArr[i+1]) {
    				return false;
    			}
    		}
    		
    		return true;
    	}
    
    /*
     * ******************************************************************
     * END SORTIERT?
     * ******************************************************************
     */
    	
    /*
     * ******************************************************************
     * Vergrößerungsfunktionen:
     * expand: arraygröße + 1
     * push: wert anhängen als neuer index
     * ******************************************************************
     */
    	public static int[] expand(int[] arr) {
    		int[] ta = new int[arr.length+1];
    		ta = arr;
    		return ta;
    	}
    	
    	public static int[] push(int[] arr,int v) {
    		int[] ta = new int[arr.length+1];
    		ta[arr.length+1]=v;
    		return ta;
    	}
    	
    	public static int[] pushAndSort(int[] arr,int v) {
    		return sort(push(arr,v));
    	}

}
