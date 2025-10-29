import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import student.TestCase;

/**
 * This class was designed to test the Radix class by generating a random
 * ascii and binary file, sorting both and then checking each one with a file
 * checker.
 *
 * @author {Your Name Here}
 * @version {Put Something Here}
 */
public class RadixProjTest extends TestCase
{
    private CheckFile fileChecker;

    /**
     * This method sets up the tests that follow.
     */
    public void setUp()
    {
        fileChecker = new CheckFile();
    }


    /**
     * Fail a sort
     *
     * @throws Exception
     *             either a IOException or FileNotFoundException
     */
    public void testFailSort()
        throws Exception
    {
        FileGenerator it = new FileGenerator();
        it.generateFile("input.txt", 1, "b");
        assertFalse(fileChecker.checkFile("input.txt"));
        System.out.println("Done testFailSort");
    }
    
    /**
     * Make a random file and test it.
     * @throws Exception 
     */
    public void testRadix() throws Exception
    {
    	FileGenerator it = new FileGenerator();
    	it.generateFile("input.txt", 1, "b");
    	RandomAccessFile testFile = new RandomAccessFile("input.txt", "rw");
    	PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("testStats.txt", true)));
    	Radix rad = new Radix(testFile, writer);
    	assertTrue(fileChecker.checkFile(testFile.readLine()));
    	testFile.close();
    	writer.flush();
    	writer.close();
    }
}
