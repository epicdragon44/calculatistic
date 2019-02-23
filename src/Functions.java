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

class LinearRegression {
    private final double intercept, slope;
    private final double r2;
    private final double svar0, svar1;

    /**
     * Performs a linear regression on the data points {@code (y[i], x[i])}.
     *
     * @param  x the values of the predictor variable
     * @param  y the corresponding values of the response variable
     * @throws IllegalArgumentException if the lengths of the two arrays are not equal
     */
    public LinearRegression(double[] x, double[] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("array lengths are not equal");
        }
        int n = x.length;

        // first pass
        double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
        for (int i = 0; i < n; i++) {
            sumx  += x[i];
            sumx2 += x[i]*x[i];
            sumy  += y[i];
        }
        double xbar = sumx / n;
        double ybar = sumy / n;

        // second pass: compute summary statistics
        double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
        for (int i = 0; i < n; i++) {
            xxbar += (x[i] - xbar) * (x[i] - xbar);
            yybar += (y[i] - ybar) * (y[i] - ybar);
            xybar += (x[i] - xbar) * (y[i] - ybar);
        }
        slope  = xybar / xxbar;
        intercept = ybar - slope * xbar;

        // more statistical analysis
        double rss = 0.0;      // residual sum of squares
        double ssr = 0.0;      // regression sum of squares
        for (int i = 0; i < n; i++) {
            double fit = slope*x[i] + intercept;
            rss += (fit - y[i]) * (fit - y[i]);
            ssr += (fit - ybar) * (fit - ybar);
        }

        int degreesOfFreedom = n-2;
        r2    = ssr / yybar;
        double svar  = rss / degreesOfFreedom;
        svar1 = svar / xxbar;
        svar0 = svar/n + xbar*xbar*svar1;
    }

    /**
     * @return the intercept of the line
     */
    public double intercept() {
        return intercept;
    }

    /**
     * @return the slope of the line
     */
    public double slope() {
        return slope;
    }

    /**
     * @return the coefficient of determination
     *         which is a real number between 0 and 1
     */
    public double R2() {
        return r2;
    }

    /**
     * @return the standard error of the estimate for the intercept
     */
    public double interceptStdErr() {
        return Math.sqrt(svar0);
    }

    /**
     * @return the standard error of the estimate for the slope
     */
    public double slopeStdErr() {
        return Math.sqrt(svar1);
    }

    /**
     * @param x
     * @return return predicted y value for a certain x value
     */
    public double predict(double x) {
        return slope*x + intercept;
    }
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("%.2f n + %.2f", slope(), intercept()));
        s.append("  (R^2 = " + String.format("%.3f", R2()) + ")");
        return s.toString();
    }
}
