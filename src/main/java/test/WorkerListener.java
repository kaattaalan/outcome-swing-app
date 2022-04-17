package test;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class WorkerListener implements PropertyChangeListener {

    private JProgressBar progress;

    public WorkerListener(JProgressBar progress) {
        this.progress = progress;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())) {
            int value = (int) evt.getNewValue();
            progress.setValue(value);
        }
    }
}
