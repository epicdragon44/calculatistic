import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Functions {
    //minimum
    public static double calcMinimum(double[] input) {
        double min = Double.MAX_VALUE;
        for (double d : input)
            if (d < min) {
                min = d;
            }
        return min;
    }
    //first quartile
    public static double calcFirstQ(double[] input) {
        double[] copy = Arrays.copyOfRange(input, 0, input.length/2);
        return calcMedian(copy);
    }
    //second quartile
    public static double calcMedian(double[] input) {
        double[] copy = Arrays.copyOfRange(input, 0, input.length);
        Arrays.sort(copy);
        if (copy.length%2!=0) {
            return copy[copy.length / 2];
        } else {
            return (copy[copy.length/2-1]+copy[copy.length/2])/2;
        }
    }
    //third quartile
    public static  double calcThirdQ(double[] input) {
        double[] copy = Arrays.copyOfRange(input, input.length/2+1, input.length);
        return calcMedian(copy);
    }
    //maximum
    public static  double calcMaximum(double[] input) {
        double max = Double.MIN_VALUE;
        for (double d : input)
            if (d>max) {
                max = d;
            }
        return max;
    }
    //average
    public static  double calcAvg(double[] input) {
        double sum = 0.0;
        for (double d : input)
            sum+=d;
        return sum/input.length;
    }

    //standard deviation
    public static  double calcStandardDev(double[] input) {
        double avg = calcAvg(input);
        double[] sds = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            sds[i] = Math.pow(input[i]-avg, 2);
        }
        return Math.sqrt(calcAvg(sds));
    }

    //variance
    public static  double calcVariance(double[] input) {
        return Math.pow(calcStandardDev(input), 2);
    }

    //IQR
    public static  double calcIQR(double[] input) {
        return calcThirdQ(input)-calcFirstQ(input);
    }

    //Lower fence
    public static  double calcLowerFence(double[] input) {
        return calcAvg(input)-(1.5*calcIQR(input));
    }

    //Upper fence
    public static  double calcUpperFence(double[] input) {
        return calcAvg(input)+(1.5*calcIQR(input));
    }

    //Set of outliers
    public static  Set<Double> calcOutliers(double[] input) {
        Set<Double> set = new HashSet<>();
        double lowerFence = calcLowerFence(input);
        double upperFence = calcUpperFence(input);
        for (double d : input) {
            if (d<lowerFence || d>upperFence)
                set.add(d);
        }
        return set;
    }

    //Sum
    public static  double calcSum(double[] input) {
        double sum = 0;
        for (double d : input)
            sum+=d;
        return sum;
    }

    //Range
    public static  double calcRange(double[] input) {
        return calcMaximum(input)-calcMinimum(input);
    }

    //Coefficient of Correlation
    public static double calcCorrelCoef(double[] input1, double[] input2) {
        int n = Math.min(input1.length, input2.length);
        double sum_X = 0, sum_Y = 0, sum_XY = 0;
        double squareSum_X = 0, squareSum_Y = 0;

        for (int i = 0; i < n; i++)
        {
            // sum of elements of array X.
            sum_X = sum_X + input1[i];

            // sum of elements of array Y.
            sum_Y = sum_Y + input2[i];

            // sum of X[i] * Y[i].
            sum_XY = sum_XY + input1[i] * input2[i];

            // sum of square of array elements.
            squareSum_X = squareSum_X + input1[i] * input1[i];
            squareSum_Y = squareSum_Y + input2[i] * input2[i];
        }

        // use formula for calculating correlation
        // coefficient.
        double corr =   (n * sum_XY - sum_X * sum_Y)/
                        (Math.sqrt((n * squareSum_X -
                        sum_X * sum_X) * (n * squareSum_Y -
                        sum_Y * sum_Y)));

        return corr;
    }

    public static double calcDetermCoef(double[] input1, double[] input2) {
        return Math.pow(calcCorrelCoef(input1, input2), 2);
    }
}
