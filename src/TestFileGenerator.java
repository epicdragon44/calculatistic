import java.util.ArrayList;
import java.util.Random;

public class TestFileGenerator {
    public static void main(String[] args) {
        Random rand = new Random();
        ArrayList<Double> nums = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            int num = rand.nextInt(100);
            System.out.print(num+" ");
            nums.add(num*Math.random());
        }
        System.out.println();
        for (int i = 0; i < 25; i++) {
            System.out.print((int)(nums.get(i).floatValue())+" ");
        }
    }
}
