import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderShiftSensor implements ChangeListener {
    private UnivariatePanel panel;
    private JSlider slider;
    public SliderShiftSensor(UnivariatePanel panel, JSlider slider) {
        this.panel = panel;
        this.slider = slider;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        panel.changeHistogram(slider.getValue());
    }
}
