import java.io.*;
import java.nio.*;

// The Radix Sort implementation
// -------------------------------------------------------------------------
/**
 *
 * @author {Your Name Here}
 * @version {Put Something Here}
 */
public class Radix {
	
	private static final int N = 112500;
	private RandomAccessFile file;
	private PrintWriter writer;

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
    private void radixSort() throws IOException {
    	//Gets information from file.
    	long length = file.length();
    	int N = (int)(length / 4);
    	byte[] bytes = new byte[(int)length];
    	file.seek(0);
    	file.readFully(bytes);

    	IntBuffer ib = ByteBuffer.wrap(bytes).asIntBuffer();
    	
    	int[] array = new int[N];
    	ib.get(array);
    	//Sorts information from file.
    	radix(array, 32, 10);
    	//Sorted information goes into the tempFile
    	File temp = File.createTempFile("sorted_", ".bin");
    	try (RandomAccessFile tempFile = new RandomAccessFile(temp, "rw")) {
    		ByteBuffer bb = ByteBuffer.allocate(N * Integer.BYTES);
    		bb.asIntBuffer().put(array);
    		tempFile.write(bb.array());
    	}
    	
    	writer.flush();
    	writer.close();
    	file.close();
    }
    
    /**
     * 
     * @param A Array that we are sorting.
     * @param k	total bits per data in A
     * @param r number of digits (10)
     */
    static void radix(int[] A, int k, int r) {
    	  int[] B = new int[A.length];
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
