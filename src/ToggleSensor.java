import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToggleSensor implements ActionListener {
    private BivariatePanel daddyPanel;
    public ToggleSensor(BivariatePanel daddyPanel) {
        this.daddyPanel = daddyPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        daddyPanel.toggleRegression();
    }
}
