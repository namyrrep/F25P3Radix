import java.io.*;

// The Radix Sort implementation
// -------------------------------------------------------------------------
/**
 *
 * @author {Your Name Here}
 * @version {Put Something Here}
 */
public class Radix {
	
	private RandomAccessFile file;
	private PrintWriter writer;
  private static final int N = 112500;

    /**
     * Create a new Radix object.
     * @param theFile The RandomAccessFile to be sorted
     * @param s The stats PrintWriter
     *
     * @throws IOException
     */
    public Radix(RandomAccessFile theFile, PrintWriter s)
        throws IOException
    {
    	file = theFile;
    	writer = s;
    	this.radixSort();
    }


    /**
     * Do a Radix sort
     *
     * @throws IOException
     */
    /**
     * Do a Radix sort
     *
     * @throws IOException
     */
     private void radixSort() throws IOException {
    	Integer[] array = new Integer[N];
    	int len = (int)this.file.length();
    	//Gets information from file.
    	for (int n = 0; n < len; n++)
    		array[n] = this.file.readInt();
    	//Sorts information from file.
    	radix(array, 32, 10);
    	//Prints information from new array.
    	for (int n = 0; n < len; n++)
    		this.writer.print(array[n]);
    	
    	writer.flush();
    	writer.close();
    	file.close();
    }
    
    
    /**
     * 
     * @param A Array that we are sorting.
     * @param k	total bits per data in A
     * @param r bits per digit sorted by (sort 2 ints at a time for 8 bits)
     */
    static void radix(Integer[] A, int k, int r) {
    	  Integer[] B = new Integer[A.length];
    	  int[] count = new int[r];     // Count[i] stores number of records with digit value i
    	  int i, j, rtok;

    	  for (i=0, rtok=1; i<k; i++, rtok*=r) { // For k digits
    	    for (j=0; j<r; j++) { count[j] = 0; }    // Initialize count

    	    // Count the number of records for each bin on this pass
    	    for (j=0; j<A.length; j++) { count[(A[j]/rtok)%r]++; }

    	    // After processing, count[j] will be index in B for first slot of bin j.
    	    int total = A.length;
    	    for (j=r-1; j>=0; j--) { total -= count[j]; count[j] = total; }

    	    // Put records into bins, working from left to right
    	    for (j=0; j<A.length; j++) {
    	      B[count[(A[j]/rtok)%r]] = A[j];
    	      count[(A[j]/rtok)%r] = count[(A[j]/rtok)%r] + 1;
    	    }
    	    for (j=0; j<A.length; j++) { A[j] = B[j]; } // Copy B back
    	  }
    	}
}
