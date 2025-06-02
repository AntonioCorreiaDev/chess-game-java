package pt.isec.pa.chess.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe singleton responsável por registar e notificar alterações no log do modelo
 * Permite adicionar mensagens de log e escutar alterações através do padrão Observer (PropretyChangeSupport)
 *
 * @author Afonso Reis, António Correia, Pedro Vieira
 */
public class ModelLog {

    /**
     * Evento disparado quando uma nova mensagem de log é adicionada
     */
    public static final String NEW_LOG = "NEW_LOG";

    /**
     * Evento disparado quando o log é limpo
     */
    public static final String CLEAR_LOGS = "CLEAR_LOGS";

    private static ModelLog instance;
    private final PropertyChangeSupport pcs;
    private final List<String> logs;

    /**
     * Construtor privado para garantir padrão Singleton
     */
    private ModelLog() {
        logs = new ArrayList<>();
        pcs = new PropertyChangeSupport(this);
    }

    /**
     * Obtém a instância única da classe ModelLog
     *
     * @return instância singleton do ModelLog
     */
    public static ModelLog getInstance() {
        if (instance == null)
            instance = new ModelLog();
        return instance;
    }

    /**
     * Adicionar uma mensagem ao log e notifica os ouvintes
     *
     * @param message mensagem de log a adicionar
     */
    public void log(String message) {
        logs.add(message);
        pcs.firePropertyChange("NEW_LOG", null, message);
    }

    /**
     * Devolve uma cópia da lista atual de mensagens de log
     * A cópia evita alterações externas à lista interna
     *
     * @return lista de mensagens de log
     */
    public List<String> getLogs() {
        return new ArrayList<>(logs); // evitar modificar diretamente
    }

    /**
     * Limpa todas as mensagens de log e notifica os ouvintes
     */
    public void clear() {
        logs.clear();
        pcs.firePropertyChange("CLEAR_LOGS", null, null);
    }

    /**
     * Adiciona um ouvinte para um determinado tipo de evento de propriedade
     *
     * @param propertyName nome da propriedade a escutar (por exemplo, NEW_LOG ou CLEAR_LOGS)
     * @param listener instância que irá reagir às mudanças
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }
}