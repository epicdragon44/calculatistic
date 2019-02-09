import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.*;

public class UnivariatePanel extends JPanel implements ActionListener {
    private JTextField field;
    private JButton enter;
    private JSlider slider; //for histogram
    private int numOfCols; //for histogram
    //CONSTRUCTOR
    public UnivariatePanel(boolean expanded) {
        //set size
        if (expanded)
            setPreferredSize( new Dimension( 1900, 500) );
        else
            setPreferredSize( new Dimension(1900, 15) );
        //init variables
        field = new JTextField("Enter numbers here, separated by spaces");
        field.setPreferredSize(new Dimension(1500, 25));
        field.addFocusListener(new ClearFieldAction(field));
        enter = new JButton("Enter");
        enter.setBackground(Color.LIGHT_GRAY);
        enter.addActionListener(this);
        slider = new JSlider(10, 100, 15);
        numOfCols = slider.getValue();
        slider.addChangeListener(new SliderShiftSensor(this, slider));
        slider.setPreferredSize(new Dimension(500, 25));
        slider.setLocation(10, 500);

        //draw border
        TitledBorder title = BorderFactory.createTitledBorder("Univariate Analysis");
        this.setBorder(title);

        //set layout
        setLayout(new FlowLayout());

        //add init variables
        add(field);
        add(enter);
        add(slider);
    }

    //MOUSE ACTION METHODS
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    protected void changeHistogram(int cols) {
        numOfCols = cols;
        repaint();
    }

    //DRAW THE GRAPHS
    public void paint(Graphics g) {
        super.paint(g);
        //draw a histogram
        g.drawString("Histogram", 10, 100);
        g.drawRect(10, 110, 500, 350);
        //draw a boxplot
        g.drawString("Boxplot", 550, 100);
        g.drawRect(550, 110, 500, 350);
        //draw the numbers
        g.drawString("Statistics", 1100, 100);
        g.drawRect(1100, 110, 500, 350);
        //draw stuff inside it if the user typed stuff in and hit enter
        if (!(field.getText().equals("Enter numbers here, separated by spaces"))) {
            //get array
            ArrayList<Double> inputList = new ArrayList<>();
            String typedInput = field.getText();
            StringTokenizer st = new StringTokenizer(typedInput);
            while (st.hasMoreTokens())
                inputList.add(Double.parseDouble(st.nextToken()));
            double[] input = new double[inputList.size()];
            for (int i = 0; i < input.length; i++)
                input[i] = inputList.get(i);
            Arrays.sort(input);

            //draw histogram inside box
            double margin = 1.0/numOfCols;

            double[] brackets = new double[numOfCols+1]; //ten brackets for our histogram
            int[] pixelBrackets = new int[numOfCols+1]; //where to draw each x position of bracket lines
            brackets[0] = Functions.calcMinimum(input);
            brackets[brackets.length-1] = Functions.calcMaximum(input)+margin;
            for (int i = 1; i < numOfCols; i++) {
                brackets[i] = ((i)*margin)*(brackets[brackets.length-1]-brackets[0])+brackets[0];
            }
            boolean odd = true;
            for (int i = 0; i < pixelBrackets.length; i++) {
                pixelBrackets[i] = (int)((i*margin)*450)+35;
                g.drawLine(pixelBrackets[i], 115, pixelBrackets[i], 425);
                if (odd) {
                    String toDraw = (brackets[i] + "");
                    if (toDraw.length() > 5)
                        toDraw = toDraw.substring(0, 5) + "";
                    g.drawString(toDraw, pixelBrackets[i] - 15, 445);
                }
                odd = !odd;
            }
            int[] freqCount = new int[numOfCols];
            for (int i = 1; i < brackets.length; i++) {
                for (double num : input) {
                    if (num >= brackets[i-1] && num<brackets[i]) {
                        freqCount[i-1]++;
                    }
                }
            }
            int maxHeight = 0;
            for (int freq : freqCount) {
                if (freq>maxHeight) maxHeight = freq;
            }
            for (int i = 0; i < numOfCols; i++) {
                int y = (int)(425-((310)*((freqCount[i]+0.0)/maxHeight)));
                g.setColor(Color.GRAY);
                g.fillRect(pixelBrackets[i], y, pixelBrackets[i+1]-pixelBrackets[i], 425-y);
            }
            for (int i = 0; i < pixelBrackets.length; i++) {
                g.setColor(Color.BLACK);
                g.drawLine(pixelBrackets[i], 115, pixelBrackets[i], 425);
            }

            for (int i = 0; i < 5; i++) {
                g.setColor(Color.BLACK);
                g.drawLine(pixelBrackets[0], 115+62*i, pixelBrackets[pixelBrackets.length-1], 115+62*i);
                String label = maxHeight - (i * ((maxHeight + 0.0) / 5)) + "";
                if (label.length() > 5) label = label.substring(0, 5);
                g.drawString(label, pixelBrackets[0] - 22, 115 + 62 * i + 10);
            }

            //draw boxplot inside box
            g.setColor(Color.BLACK);
            g.drawLine(575, 275, 1025, 275);
            g.drawLine(575, 200, 575,350);
            g.drawString(Functions.calcMinimum(input)+"", 575, 375);
            g.drawLine(1025, 200, 1025, 350);
            g.drawString(Functions.calcMaximum(input)+"", 1025, 375);
            int leftX = (int)(((Functions.calcFirstQ(input)/Functions.calcMaximum(input))*450)+575);
            int rightX = (int)(((Functions.calcThirdQ(input)/Functions.calcMaximum(input))*450)+575);
            int midX = (int)(((Functions.calcMedian(input)/Functions.calcMaximum(input))*450)+575);
            g.setColor(new Color(238, 238, 238));
            g.fillRect(leftX, 200, rightX-leftX, 150);
            g.setColor(Color.BLACK);
            g.drawLine(leftX, 200, leftX, 350);
            g.drawString(Functions.calcFirstQ(input)+"", leftX, 375);
            g.drawLine(rightX, 200, rightX, 350);
            g.drawLine(leftX, 200, rightX, 200);
            g.drawLine(leftX, 350, rightX, 350);
            g.drawString(Functions.calcThirdQ(input)+"", rightX, 375);
            g.drawLine(midX, 200, midX, 350);
            g.drawString(Functions.calcMedian(input)+"", midX, 375);

            //draw statistics inside box
            g.drawString("Five # Summary: "+Functions.calcMinimum(input)+" "+Functions.calcFirstQ(input)+" "+Functions.calcMedian(input)+" "+Functions.calcThirdQ(input)+" "+Functions.calcMaximum(input), 1110, 125);
            g.drawString("Average: "+Functions.calcAvg(input), 1110, 140);
            g.drawString("Standard Deviation: "+Functions.calcStandardDev(input), 1110, 155);
            g.drawString("Variance: "+Functions.calcVariance(input), 1110, 170);
            g.drawString("IQR: "+Functions.calcIQR(input), 1110, 185);
            g.drawString("Upper Fence: "+Functions.calcUpperFence(input), 1110, 200);
            g.drawString("Lower Fence: "+Functions.calcLowerFence(input), 1110, 215);
            g.drawString("Set of Outliers: " + Functions.calcOutliers(input), 1110, 230);
            g.drawString("Sum: " + Functions.calcSum(input), 1110, 245);
            g.drawString("Range: "+Functions.calcRange(input), 1110, 260);
        }
    }
}