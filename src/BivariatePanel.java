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
    private JToggleButton toggle;

    private boolean showRegression;
    //CONSTRUCTOR
    public BivariatePanel(boolean expanded) {
        //set size
        if (expanded)
            setPreferredSize( new Dimension( 1900, 500) );
        else
            setPreferredSize( new Dimension(1900, 15) );
        //init variables
        field1 = new JTextField("Enter first variable positive numbers here, separated by spaces");
        field1.setPreferredSize(new Dimension(1607, 25));
        field1.addFocusListener(new ClearFieldAction(field1));
        field2 = new JTextField("Enter second variable positive numbers here, separated by spaces");
        field2.setPreferredSize(new Dimension(1500, 25));
        field2.addFocusListener(new ClearFieldAction(field2));
        enter = new JButton("Enter");
        enter.setPreferredSize(new Dimension(100, 25));
        enter.setBackground(Color.LIGHT_GRAY);
        enter.addActionListener(this);
        toggle = new JToggleButton("Show Regression", false);
        toggle.setPreferredSize(new Dimension(300, 20));
        toggle.addActionListener(new ToggleSensor(this));

        //draw border
        TitledBorder title = BorderFactory.createTitledBorder("Bivariate Analysis");
        this.setBorder(title);

        //set layout
        setLayout(new FlowLayout(FlowLayout.LEFT));

        JPanel buffer = new JPanel();
        buffer.setPreferredSize(new Dimension(1900, 375));

        //add init variables
        add(field1);
        add(field2);
        add(enter);
        add(buffer);
        add(toggle);
    }

    //MOUSE ACTION METHODS
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    protected void toggleRegression() {
        showRegression = !showRegression;
        paint(this.getGraphics());
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

            ArrayList<Double> input2list = new ArrayList<>();
            String typedInput2 = field2.getText();
            st = new StringTokenizer(typedInput2);
            while (st.hasMoreTokens())
                input2list.add(Double.parseDouble(st.nextToken()));
            double[] input2 = new double[input2list.size()];
            for (int i = 0; i < input2.length; i++)
                input2[i] = input2list.get(i);

            //draw statistics inside box
            g.drawString("Correlation Coefficient: "+Functions.calcCorrelCoef(input1, input2), 1110, 125);
            g.drawString("Coefficient of Determination:"+Functions.calcDetermCoef(input1, input2), 1110, 140);

            //draw scatterplot inside box
            double input1Min = Functions.calcMinimum(input1); //input 1 drawn along x axis
            double input2Min = Functions.calcMinimum(input2);
            double input1Max = Functions.calcMaximum(input1); //input 2 drawn along y axis
            double input2Max = Functions.calcMaximum(input2);

            int yMinPixel = 125;
            int yMaxPixel = 445;
            int xMinPixel = 25;
            int xMaxPixel = 495;

            g.drawLine(xMinPixel, yMinPixel, xMinPixel, yMaxPixel); //y axis
            g.drawLine(xMinPixel, yMaxPixel, xMaxPixel, yMaxPixel); //x axis
            g.setFont(new Font("Serif", Font.BOLD, 10));
            for (int i = 0; i < 11; i++) { //draw 10 tick marks along y axis
                g.drawLine(xMinPixel-5, yMinPixel+i*((yMaxPixel-yMinPixel)/10), xMinPixel, yMinPixel+i*((yMaxPixel-yMinPixel)/10));
                String toDraw = input2Max-i*((input2Max-input2Min)/10)+"";
                if (toDraw.length()>4)
                    toDraw = toDraw.substring(0, 4);
                g.drawString(toDraw, xMinPixel-12, yMinPixel+i*((yMaxPixel-yMinPixel)/10)-7);
            }
            for (int i = 0; i < 11; i++) { //draw 10 tick marks along the x axis
                g.drawLine(xMinPixel+i*((xMaxPixel-xMinPixel)/10), yMaxPixel+5, xMinPixel+i*((xMaxPixel-xMinPixel)/10), yMaxPixel);
                String toDraw = input1Min+i*((input1Max-input1Min)/10)+"";
                if (toDraw.length()>4)
                    toDraw = toDraw.substring(0, 4);
                g.drawString(toDraw, xMinPixel+i*((xMaxPixel-xMinPixel)/10)-7, yMaxPixel+15);
            }
            int minLength = Math.min(input1.length, input2.length);
            for (int i = 0; i < minLength; i++) {
                int x = (int)(xMinPixel + (xMaxPixel-xMinPixel)*((input1[i]-input1Min)/(input1Max-input1Min)));
                int y = (int)(yMaxPixel - (yMaxPixel-yMinPixel)*((input2[i]-input2Min)/(input2Max-input2Min)));
                g.fillOval(x-5, y-5, 10, 10);
            }

            if (showRegression) {
                LinearRegression regressLine = new LinearRegression(input1, input2);
                int y1 = (int) (yMaxPixel - (yMaxPixel - yMinPixel) * ((regressLine.predict(Functions.calcMinimum(input1)) - input2Min) / (input2Max - input2Min)));
                int x1 = xMinPixel;
                int y2 = (int) (yMaxPixel - (yMaxPixel - yMinPixel) * ((regressLine.predict(Functions.calcMaximum(input1)) - input2Min) / (input2Max - input2Min)));
                int x2 = xMaxPixel;
                if (y2 < yMinPixel) {
                    double slope = (y2 + 0.0 - y1) / (x2 + 0.0 - x1);
                    int intersectX = (int) (((yMinPixel + 0.0 - y2) / slope) + x2);
                    x2 = intersectX;
                    y2 = yMinPixel;
                }
                if (y2 > yMaxPixel) {
                    int intersectX = (int)((yMaxPixel+0.0-y1)*((x2+0.0-x1)/(y2+0.0-y1))+0.0+x1);
                    x2 = intersectX;
                    y2 = yMaxPixel;
                }
                if (y1 < yMinPixel) {
                    int intersectX = (int)(x1+0.0- ((y1+0.0 - yMinPixel)*((x1+0.0-x2)/(y1+0.0-y2))));
                    x1 = intersectX;
                    y1 = yMinPixel;
                }
                if (y1 > yMaxPixel) {
                    int intersectX = (int)(x2+0.0 - ((y2+0.0-yMaxPixel)*((x2+0.0-x1)/(y2+0.0-y1))));
                    x1 = intersectX;
                    y1 = yMaxPixel;
                }
                g.drawLine(x1, y1, x2, y2);
                g.drawString("Regression Line: " + regressLine.toString(), xMinPixel+300, yMaxPixel + 30);

                //draw residual plot inside box

                double[] residuals = new double[minLength];
                for (int i = 0; i < minLength; i++) {
                    residuals[i] = input2[i] - regressLine.predict(input1[i]);
                }
                double residualMax = Functions.calcMaximum(residuals); //input 2 drawn along y axis
                double residualMin = Functions.calcMinimum(residuals);
                yMinPixel = 125;
                yMaxPixel = 445;
                xMinPixel = 575;
                xMaxPixel = 1025;
                int yMedPixel = (yMaxPixel + yMinPixel) / 2;
                g.drawLine(xMinPixel, yMinPixel, xMinPixel, yMaxPixel); //y axis
                g.drawLine(xMinPixel, yMedPixel, xMaxPixel, yMedPixel); //x axis
                g.setFont(new Font("Serif", Font.BOLD, 10));
                for (int i = 0; i < 11; i++) { //draw 10 tick marks along y axis
                    g.drawLine(xMinPixel - 5, yMinPixel + i * ((yMaxPixel - yMinPixel) / 10), xMinPixel, yMinPixel + i * ((yMaxPixel - yMinPixel) / 10));
                    String toDraw = residualMax - i * ((residualMax - residualMin) / 10) + "";
                    if (toDraw.length() > 4)
                        toDraw = toDraw.substring(0, 4);
                    g.drawString(toDraw, xMinPixel - 12, yMinPixel + i * ((yMaxPixel - yMinPixel) / 10) - 7);
                }
                for (int i = 0; i < 11; i++) { //draw 10 tick marks along the x axis
                    g.drawLine(xMinPixel + i * ((xMaxPixel - xMinPixel) / 10), yMedPixel + 5, xMinPixel + i * ((xMaxPixel - xMinPixel) / 10), yMedPixel);
                    String toDraw = input1Min + i * ((input1Max - input1Min) / 10) + "";
                    if (toDraw.length() > 4)
                        toDraw = toDraw.substring(0, 4);
                    g.drawString(toDraw, xMinPixel + i * ((xMaxPixel - xMinPixel) / 10) - 7, yMedPixel + 15);
                }

                for (int i = 0; i < minLength; i++) {
                    int x = (int) (xMinPixel + (xMaxPixel - xMinPixel) * ((input1[i] - input1Min) / (input1Max - input1Min)));
                    int y = yMedPixel; //only stays this if residual==0

                    if (residuals[i] > 0) {
                        double prop = (residualMax+0.0-residuals[i])/(residualMax+0.0);
                        y = (int)(prop * (yMedPixel-yMinPixel) + yMinPixel);
                    } else if (residuals[i] < 0) {
                        double prop = (residuals[i]+0.0-residualMin)/(Math.abs(residualMin));
                        y = (int)(prop * (yMaxPixel-yMedPixel) + yMedPixel);
                    }

                    g.fillOval(x - 5, y - 5, 10, 10);
                }
            }
        }
    }
}
