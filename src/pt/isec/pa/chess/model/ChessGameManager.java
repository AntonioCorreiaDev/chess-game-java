package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.Command.CommandManager;
import pt.isec.pa.chess.model.Command.MovePieceCommand;
import pt.isec.pa.chess.model.data.ChessGame;
import pt.isec.pa.chess.model.data.ChessGameSerialization;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.List;

/**
 * Classe responsável por gerir a lógica do jogo, interligando a lógica e dados com a ui.
 * Age como uma facade que lida com as interações com o modelo de dados.
 *
 * @author Afonso Reis, António Correia, Pedro Vieira
 */

public class ChessGameManager {

    /**
     * Classe com os dados e a logica do jogo
     */
    private ChessGame chessGame;

    /**
     * Observer que permite que outros objetos sejam notificados quando uma propriedade muda (property change support)
     */
    PropertyChangeSupport pcs;

    /**
     * Classe que gere os comandos(undo/redo)
     */
    CommandManager cm;

    /**
     * Constantes udadas para identificar as mudanças de estado
     */
    public static final String BOARD_STATE = "board_state";
    public static final String CURRENT_PLAYER = "current_player";

    /**
     * String que guarda o tipo de evolução escolhida pelo jogador
     */
    String evolution;

    /**
     * Contador para as jogadas
     */
    int jogadas = 0;

    /**
     * Construtor por defeito. Inicia um novo jogo de xadrez
     */

    public ChessGameManager() {
        this.chessGame = new ChessGame();
        cm = new CommandManager();
        pcs = new PropertyChangeSupport(this);
    }

    /**
     * Adiciona um listener para alterações de propriedade
     * @param property Nome da propriedade a observar
     * @param listener Ouvinte a ser notificado
     */
    public void addPropertyChangeListener(String property, PropertyChangeListener listener){
        pcs.addPropertyChangeListener(property, listener);
    }

    /**
     * Move uma peça de uma posição para outra, validando o movimento e udando MovePieceCommand
     * @param startCol Coluna de origem
     * @param startRow Linha de origem
     * @param endCol Coluna de destino
     * @param endRow Linha de destino
     * @return true se o movimento for válido e executado
     */
    public boolean movePieceCoordinates(int startCol, int startRow, int endCol, int endRow) {
        boolean result = cm.invokeCommand(new MovePieceCommand(chessGame, startCol, startRow, endCol, endRow));

        if(result) {
            jogadas++;
            pcs.firePropertyChange(BOARD_STATE, null, chessGame.getQueryState());
            pcs.firePropertyChange(CURRENT_PLAYER, null, chessGame.getCurrentPlayer());
        }
        return result;
    }

    /**
     * Verifica se existe uma peça na posição especificada e se é possivel move-la
     * @param col Coluna
     * @param row Linha
     * @return true se houver peça valida na posição
     */
    public boolean checkPieceLegal(int col, int row) {
        return chessGame.checkPieceLegal(col, row);
    }

    /**
     * Define a peça de promoção (rainha, torre, etc.)
     * @param evo String com o nome da peça para promoção
     */
    public void setEvolution(String evo){
        evolution = evo;
    }

    /**
     * Promove um peão na posição indicada e avisa que houve uma alteração no estado de jogo
     * @param col Coluna do peão
     * @param row Linha do peão
     * @param white true se for peça branca; false se preta
     */
    public void promotePawn(int col, int row, boolean white) {
        chessGame.promotePawn(col, row, evolution, white);
        pcs.firePropertyChange(BOARD_STATE, null, chessGame.getQueryState());
    }

    /**
     * Obtém o jogador atual
     * @return "WHITE" ou "BLACK"
     */
    public String getCurrentPlayer() {
        return chessGame.getCurrentPlayer();
    }

    /**
     * Obtém o numero de jogadas atual
     * @return int (jogadas)
     */
    public int getJogadas() {
        return jogadas;
    }

    /**
     * Reinicia o número de jogadas.
     */
    public void reset() {
        jogadas = 0;
    }

    /**
     * Obtém o nome do jogador 1 (brancas)
     * @return Nome do jogador 1
     */
    public String getPlayer1() {
        return chessGame.getPlayer1();
    }

    /**
     * Obtém o nome do jogador 2 (pretas)
     * @return Nome do jogador 2
     */
    public String getPlayer2() {
        return chessGame.getPlayer2();
    }

    /**
     * Determina o estado atual do jogo
     * @return "WHITE", "BLACK", "CHECK", "EMPATE" ou null
     */
    public String getWinner() {
        if (chessGame.getWinner() == 1) {
            return "WHITE";
        } else if (chessGame.getWinner() == 0) {
            return "BLACK";
        } else if (chessGame.getWinner() == 2) {
            return "CHECK";
        }else if(chessGame.getWinner() == 3)
            return "EMPATE";

        return null;
    }

    /**
     * Obtém os movimentos válidos para uma peça numa posição
     * @param col Coluna da peça
     * @param row Linha da peça
     * @return Lista de posições válidas como array de inteiros [col, row]
     */
    public List<int[]> getValidMoves(int col, int row) {
        return chessGame.getValidMoves(col, row);
    }

    /**
     * Reinicia o jogo e avisa para que seja atualizado
     */
    public void resetGame() {
        chessGame.resetGame();
        pcs.firePropertyChange(BOARD_STATE, null, chessGame.getQueryState());
        pcs.firePropertyChange(CURRENT_PLAYER, null, chessGame.getCurrentPlayer());

    }

    /**
     * Carrega um jogo de um ficheiro
     * @param filePath Caminho do ficheiro a carregar
     */
    public void loadGame(String filePath) {
        try {
            //this.chessGame = ChessGame.loadGame(filePath);
            this.chessGame = ChessGameSerialization.deserialize(filePath);
            pcs.firePropertyChange(BOARD_STATE, null, chessGame.getQueryState());
            pcs.firePropertyChange(CURRENT_PLAYER, null, chessGame.getCurrentPlayer());
            System.out.println("Jogo  de: " + filePath);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar jogo: " + e.getMessage());
        }
    }

    /**
     * Guarda o estado atual do jogo num ficheiro
     * @param filePath Caminho do ficheiro onde guardar
     */
    public void saveGame(String filePath) {
        try {
            //chessGame.saveGame(filePath);
            ChessGameSerialization.serialize(chessGame, filePath);
            System.out.println("Jogo em: " + filePath);
        } catch (IOException e) {
            System.err.println("Erro ao guardar jogo: " + e.getMessage());
        }
    }

    /**
     * Define os nomes dos jogadores
     * @param n1 Nome do jogador 1
     * @param n2 Nome do jogador 2
     */
    public void setPlayersNames(String n1, String n2){ chessGame.setPlayersNames(n1, n2);}

    /**
     * Exporta o histórico do jogo para ficheiro CSV
     * @param filePath
     */
    public void exportGameCsv(String filePath) {chessGame.exportCsv(filePath);}

    /**
     * Importa um jogo a partir de um ficheiro CSV
     * @param filePath
     */
    public void importGameCsv(String filePath) {
        chessGame.importCsv(filePath);
        pcs.firePropertyChange(BOARD_STATE, null, chessGame.getQueryState());
        pcs.firePropertyChange(CURRENT_PLAYER, null, chessGame.getCurrentPlayer());
    }

    /**
     * Obtém o tamanho do tabuleiro
     * @return Tamanho (largura/altura)
     */
    public int getBoardSize(){return chessGame.getBoardSize();}

    /**
     * Obtém a representação da imagem de uma peça numa posição
     * @param row Linha
     * @param col Coluna
     * @return String com identificador da imagem
     */
    public String getPieceImageString(int row, int col){return chessGame.getPieceImageString(row, col);}

    /**
     * Obtém a representação de uma peça numa posição
     * @param row Linha
     * @param col Coluna
     * @return String com identificação da peça
     */
    public String getPieceName(int row, int col){
        String pieceString = chessGame.getPieceImageString(row, col);

        // Verificações de segurança
        if (pieceString == null || pieceString.length() < 2) {
            return null;
        }

        return pieceString.substring(0, pieceString.length() - 1);
    }

    /**
     * Obtém a cor da peça numa determinada posição
     * @param row Linha
     * @param col Coluna
     * @return "white" ou "black", ou null se inválido
     */
    public String getPieceColor(int row, int col){
        String pieceString = chessGame.getPieceImageString(row, col);

        if (pieceString == null || pieceString.length() < 2) {
            return null;
        }

        String colorChar1 = pieceString.substring(pieceString.length() - 1);

        return colorChar1.equals("W") ? "white" : "black";
    }

    /**
     * Verifica se a peça está vulnerável a en passant
     * @param row Linha
     * @param col Coluna
     * @return true se vulnerável; false caso contrário
     */
    public Boolean getPieceEnPassantVulnerable(int row, int col){
        return chessGame.getPieceEnPassantVulnerable(row, col);
    }

    /**
     * Desfaz a última ação realizada
     * @return true se a ação foi desfeita
     */
    public boolean undo() {
        boolean result = cm.undo();
        if(result){
            jogadas--;
        }
        pcs.firePropertyChange(BOARD_STATE, null, chessGame.getQueryState());
        pcs.firePropertyChange(CURRENT_PLAYER, null, chessGame.getCurrentPlayer());
        return result;
    }

    /**
     * Refaz a última ação desfeita
     * @return
     */
    public boolean redo() {
        boolean result = cm.redo();
        if(result){
            jogadas++;
        }
        pcs.firePropertyChange(BOARD_STATE, null, chessGame.getQueryState());
        pcs.firePropertyChange(CURRENT_PLAYER, null, chessGame.getCurrentPlayer());
        return result;
    }
}