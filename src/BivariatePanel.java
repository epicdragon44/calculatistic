import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class BivariatePanel extends JPanel implements ActionListener {
    private JTextField field1;
    private JTextField field2;
    private JButton enter;
    //CONSTRUCTOR
    public BivariatePanel(boolean expanded) {
        //set size
        if (expanded)
            setPreferredSize( new Dimension( 1900, 750) );
        else
            setPreferredSize( new Dimension(1900, 15) );
        //init variables
        field1 = new JTextField("Enter first variable numbers here, separated by spaces");
        field1.setPreferredSize(new Dimension(1607, 25));
        field1.addFocusListener(new ClearFieldAction(field1));
        field2 = new JTextField("Enter second variable numbers here, separated by spaces");
        field2.setPreferredSize(new Dimension(1500, 25));
        field2.addFocusListener(new ClearFieldAction(field2));
        enter = new JButton("Enter");
        enter.setPreferredSize(new Dimension(100, 25));
        enter.setBackground(Color.LIGHT_GRAY);
        enter.addActionListener(this);

        //draw border
        TitledBorder title = BorderFactory.createTitledBorder("Bivariate Analysis");
        this.setBorder(title);

        //set layout
        setLayout(new FlowLayout());

        //add init variables
        add(field1);
        add(field2);
        add(enter);
    }

    //MOUSE ACTION METHODS
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    //DRAW THE GRAPHS
    public void paint(Graphics g) {
        super.paint(g);
        //draw a scatterplot
        g.drawString("Scatterplot", 10, 100);
        g.drawRect(10, 110, 500, 350);
        //draw a residual plot
        g.drawString("Residual Plot", 550, 100);
        g.drawRect(550, 110, 500, 350);
        //draw the numbers
        g.drawString("Statistics", 1100, 100);
        g.drawRect(1100, 110, 500, 350);
        //draw stuff inside it if the user typed stuff in and hit enter
        if (!(field1.getText().contains("separated by spaces") || field2.getText().contains("separated by spaces"))) {
            //get arrays
            ArrayList<Double> input1list = new ArrayList<>();
            String typedInput1 = field1.getText();
            StringTokenizer st = new StringTokenizer(typedInput1);
            while (st.hasMoreTokens())
                input1list.add(Double.parseDouble(st.nextToken()));
            double[] input1 = new double[input1list.size()];
            for (int i = 0; i < input1.length; i++)
                input1[i] = input1list.get(i);
            Arrays.sort(input1);

            ArrayList<Double> input2list = new ArrayList<>();
            String typedInput2 = field1.getText();
            st = new StringTokenizer(typedInput2);
            while (st.hasMoreTokens())
                input2list.add(Double.parseDouble(st.nextToken()));
            double[] input2 = new double[input2list.size()];
            for (int i = 0; i < input2.length; i++)
                input2[i] = input2list.get(i);
            Arrays.sort(input2);

            //draw scatterplot inside box

            //draw residual plot inside box

            //draw statistics inside box
            g.drawString("Variable One:", 1110, 125);
            g.drawString("Five # Summary: "+Functions.calcMinimum(input1)+" "+Functions.calcFirstQ(input1)+" "+Functions.calcMedian(input1)+" "+Functions.calcThirdQ(input1)+" "+Functions.calcMaximum(input1), 1110, 140);
            g.drawString("Average: "+Functions.calcAvg(input1), 1110, 155);
            g.drawString("Standard Deviation: "+Functions.calcStandardDev(input1), 1110, 170);
            g.drawString("Variance: "+Functions.calcVariance(input1), 1110, 185);
            g.drawString("IQR: "+Functions.calcIQR(input1), 1110, 200);
            g.drawString("Upper Fence: "+Functions.calcUpperFence(input1), 1110, 215);
            g.drawString("Lower Fence: "+Functions.calcLowerFence(input1), 1110, 230);
            g.drawString("Set of Outliers: " + Functions.calcOutliers(input1), 1110, 245);
            g.drawString("Sum: " + Functions.calcSum(input1), 1110, 260);
            g.drawString("Range: "+Functions.calcRange(input1), 1110, 270);
            g.drawString("Variable Two:", 1110, 300);
            g.drawString("Five # Summary: "+Functions.calcMinimum(input2)+" "+Functions.calcFirstQ(input2)+" "+Functions.calcMedian(input2)+" "+Functions.calcThirdQ(input2)+" "+Functions.calcMaximum(input2), 1110, 315);
            g.drawString("Average: "+Functions.calcAvg(input2), 1110, 330);
            g.drawString("Standard Deviation: "+Functions.calcStandardDev(input2), 1110, 345);
            g.drawString("Variance: "+Functions.calcVariance(input2), 1110, 360);
            g.drawString("IQR: "+Functions.calcIQR(input2), 1110, 370);
            g.drawString("Upper Fence: "+Functions.calcUpperFence(input2), 1110, 385);
            g.drawString("Lower Fence: "+Functions.calcLowerFence(input2), 1110, 400);
            g.drawString("Set of Outliers: " + Functions.calcOutliers(input2), 1110, 415);
            g.drawString("Sum: " + Functions.calcSum(input2), 1110, 430);
            g.drawString("Range: "+Functions.calcRange(input2), 1110, 445);
            g.drawString("Bivariate Statistics:" , 1110, 515);
            g.drawString("Correlation Coefficient: "+Functions.calcCorrelCoef(input1, input2), 1110, 530);
            g.drawString("Coefficient of Determination:"+Functions.calcDetermCoef(input1, input2), 1110, 545);
        }
    }
}
