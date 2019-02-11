import java.util.Random;

public class TestFileGenerator {
    public static void main(String[] args) {
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            System.out.print((rand.nextInt(100)+rand.nextInt(100)+rand.nextInt(100)+rand.nextInt(100))/4+" ");
        }
    }
}
