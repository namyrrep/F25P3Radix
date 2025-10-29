import java.io.*;
import java.nio.*;

public class Radix {

    private RandomAccessFile file;
    private PrintWriter writer;

    public Radix(RandomAccessFile theFile, PrintWriter s) throws IOException {
        this.file = theFile;
        this.writer = s;
        this.radixSort();
    }


    /**
     * Perform radix sort on file data.
     */
    private void radixSort() throws IOException {
        long length = file.length();
        int N = (int)(length / Integer.BYTES);

        // Read file bytes into buffer
        byte[] bytes = new byte[(int)length];
        file.seek(0);
        file.readFully(bytes);

        IntBuffer ib = ByteBuffer.wrap(bytes).asIntBuffer();
        Integer[] array = new Integer[N];

        // Load IntBuffer into Integer[]
        for (int i = 0; i < N; i++) {
            array[i] = ib.get();
        }

        // Sort
        radix(array, 32, 256);

        // Write sorted results back out
        File temp = File.createTempFile("sorted_", ".bin");
        try (RandomAccessFile tempFile = new RandomAccessFile(temp, "rw")) {
            ByteBuffer outBuf = ByteBuffer.allocate(N * Integer.BYTES);
            IntBuffer outIntBuf = outBuf.asIntBuffer();

            for (Integer val : array) {
                outIntBuf.put(val);
            }

            tempFile.write(outBuf.array());
            file.setLength(0); // clear old data
            file.seek(0);
            file.write(outBuf.array());
        }

        writer.flush();
    }


    /**
     * Radix sort using Integer[] instead of primitive arrays.
     *
     * @param A
     *            array of Integer objects
     * @param k
     *            total bits per data in A
     * @param r
     *            radix base (e.g., 256)
     */
    static void radix(Integer[] A, int k, int r) {
        // Determine bits per digit (works when r is power of two).
        int bitsPerDigit;
        bitsPerDigit = Integer.numberOfTrailingZeros(r);

        final int mask = r - 1;
        final int numPasses = (k + bitsPerDigit - 1) / bitsPerDigit; // ceil

        Integer[] B = new Integer[A.length];
        int[] count = new int[r];

        for (int pass = 0; pass < numPasses; pass++) {
            // zero counts
            for (int i = 0; i < r; i++)
                count[i] = 0;

            // count digits for this pass
            int shift = pass * bitsPerDigit;
            for (int j = 0; j < A.length; j++) {
                int v = A[j];
                int digit = (v >>> shift) & mask;
                count[digit]++;
            }

            // compute starting indices (stable)
            int total = 0;
            for (int i = 0; i < r; i++) {
                int c = count[i];
                count[i] = total;
                total += c;
            }

            // distribute into B in stable order
            for (int j = 0; j < A.length; j++) {
                int v = A[j];
                int digit = (v >>> shift) & mask;
                B[count[digit]] = v;
                count[digit]++;
            }

            // copy back to A (manual loop)
            for (int j = 0; j < A.length; j++) {
                A[j] = B[j];
            }
        }
    }
}
