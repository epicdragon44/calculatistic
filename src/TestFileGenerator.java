import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class TestFileGenerator {
    public static void main(String[] args) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter("input.txt"));
        Random rand = new Random();
        int size = rand.nextInt(25);
        for (int i = 0; i < size; i++) {
            System.out.print(rand.nextInt(100)+" ");
        }
        pw.close();
    }
}
