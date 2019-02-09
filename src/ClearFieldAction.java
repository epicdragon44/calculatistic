import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ClearFieldAction implements FocusListener {
    private JTextField field;
    public ClearFieldAction(JTextField field) {
        this.field = field;
    }
    public void focusGained(FocusEvent e) {
        if (field.getText().equals("Enter numbers here, separated by spaces")) field.setText("");
    }
    public void focusLost(FocusEvent e) {
    }
}