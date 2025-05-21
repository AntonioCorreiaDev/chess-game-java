package pt.isec.pa.chess.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import pt.isec.pa.chess.model.ModelLog;

public class LogsJFX extends VBox {

    private ListView<String> logListView;
    private Button btnClearLogs;

    public LogsJFX() {
        createViews();
        registerHandlers();
    }

    private void createViews() {
        setSpacing(10);
        setPadding(new Insets(10));

        Label label = new Label("Logs");
        logListView = new ListView<>();
        btnClearLogs = new Button("Limpar Logs");

        getChildren().addAll(label, logListView, btnClearLogs);
    }

    private void registerHandlers() {

        btnClearLogs.setOnAction(e -> ModelLog.getInstance().clear());

        ModelLog.getInstance().addPropertyChangeListener(ModelLog.NEW_LOG, evt -> {
            String log = (String) evt.getNewValue();
            logListView.getItems().add(log);
            logListView.scrollTo(logListView.getItems().size() - 1);
        });

        ModelLog.getInstance().addPropertyChangeListener(ModelLog.CLEAR_LOGS, evt -> {
            logListView.getItems().clear();
        });
    }
}

