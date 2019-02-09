import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class TestFileGenerator {
    public static void main(String[] args) {
        Random rand = new Random();
        int size = rand.nextInt(100);
        for (int i = 0; i < 25; i++) {
            System.out.print(rand.nextInt(100)+" ");
        }
    }
}
