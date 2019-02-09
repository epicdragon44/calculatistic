import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatFrame extends JFrame implements ActionListener {
    private JButton showPanel;
    public StatFrame(String frameName, boolean whichToCollapse) {
        super(frameName);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920,1000);

        if (whichToCollapse)
            showPanel = new JButton("Do Bivariate Analysis");
        else
            showPanel = new JButton("Do Univariate Analysis");
        showPanel.setBackground(Color.LIGHT_GRAY);
        showPanel.addActionListener(this);
        showPanel.setPreferredSize(new Dimension(1900, 25));
        add(showPanel);

        if (whichToCollapse) {
            UnivariatePanel panel1 = new UnivariatePanel(true);
            add(panel1);
            BivariatePanel panel2 = new BivariatePanel(false);
            add(panel2);
        }
        else {
            UnivariatePanel panel1 = new UnivariatePanel(false);
            add(panel1);
            BivariatePanel panel2 = new BivariatePanel(true);
            add(panel2);
        }
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        this.setVisible(false);

        if (showPanel.getText().equals("Do Bivariate Analysis")) {
            showPanel.setText("Do Univariate Analysis");
            new StatFrame("Stat Calculator", false);
        }
        else if (showPanel.getText().equals("Do Univariate Analysis")) {
            showPanel.setText("Do Bivariate Analysis");
            new StatFrame("Stat Calculator", true);
        }
    }

    public static void main(String[] args) {
        new StatFrame("Stat Calculator", true);
    }
}
