package pt.isec.pa.chess.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import pt.isec.pa.chess.model.ModelLog;

/**
 * Classe responsável por apresentar os logs da aplicação numa interface gráfica utilizando JavaFX.
 * Esta classe mostra uma lista de logs e um botão para limpar os logs.
 *
 * Os logs são obtidos a partir do singleton {@link ModelLog}.
 *
 * @author Afonso Reis, António Correia, Pedro Vieira
 */
public class LogsJFX extends VBox {

    /** Componente JavaFX que exibe os logs. */
    private ListView<String> logListView;

    /** Botão para limpar todos os logs exibidos. */
    private Button btnClearLogs;

    /**
     * Construtor da classe LogsJFX. Inicializa a interface e os handlers.
     */
    public LogsJFX() {
        createViews();
        registerHandlers();
    }

    /**
     * Inicializa os componentes visuais da interface.
     * Define o espaçamento, margem e adiciona os componentes à VBox.
     */
    private void createViews() {
        setSpacing(10);
        setPadding(new Insets(10));

        Label label = new Label("Logs");
        logListView = new ListView<>();
        btnClearLogs = new Button("Limpar Logs");

        getChildren().addAll(label, logListView, btnClearLogs);
    }

    /**
     * Regista os event handlers para a interface gráfica.
     * <ul>
     *     <li>Associa o botão para limpar os logs à ação de limpar o {@link ModelLog}.</li>
     *     <li>Adiciona listeners para atualizar a lista de logs sempre que um novo log é adicionado ou os logs forem limpos.</li>
     * </ul>
     */
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

