package dlx.client;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Joost
 */
public class PendingCalls {

    private final AtomicInteger pendingCalls = new AtomicInteger(0);
    private final PropertyChangeSupport support;

    public PendingCalls() {
        this.support = new PropertyChangeSupport(this);
    }

    public void addChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removeChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    int incrementAndGet() {
        int oldValue = pendingCalls.get();
        int newValue = pendingCalls.incrementAndGet();
        support.firePropertyChange("name", oldValue, newValue);
        return newValue;
    }

    int decrementAndGet() {
        int oldValue = pendingCalls.get();
        int newValue = pendingCalls.decrementAndGet();
        support.firePropertyChange("name", oldValue, newValue);
        return newValue;
    }

    public int get() {
        return pendingCalls.get();
    }

}
