package pt.isec.pa.chess.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class ModelLog {

    public static final String NEW_LOG = "NEW_LOG";
    public static final String CLEAR_LOGS = "CLEAR_LOGS";

    private static ModelLog instance;
    private final PropertyChangeSupport pcs;
    private final List<String> logs;

    private ModelLog() {
        logs = new ArrayList<>();
        pcs = new PropertyChangeSupport(this);
    }

    public static ModelLog getInstance() {
        if (instance == null)
            instance = new ModelLog();
        return instance;
    }

    public void log(String message) {
        logs.add(message);
        pcs.firePropertyChange("NEW_LOG", null, message);
    }

    public List<String> getLogs() {
        return new ArrayList<>(logs); // evitar modificar diretamente
    }

    public void clear() {
        logs.clear();
        pcs.firePropertyChange("CLEAR_LOGS", null, null);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }
}