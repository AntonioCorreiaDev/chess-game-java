package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.Command.CommandManager;
import pt.isec.pa.chess.model.Command.MovePieceCommand;
import pt.isec.pa.chess.model.data.ChessGame;
import pt.isec.pa.chess.model.data.ChessGameSerialization;
import pt.isec.pa.chess.model.data.Piece;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.List;

public class ChessGameManager {


    private ChessGame chessGame;
    PropertyChangeSupport pcs;
    CommandManager cm;

    public static final String BOARD_STATE = "board_state";
    public static final String CURRENT_PLAYER = "current_player";

    public ChessGameManager() {
        this.chessGame = new ChessGame();
        cm = new CommandManager();
        pcs = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener){
        pcs.addPropertyChangeListener(property, listener);
    }
    //sempre q alterar um valor no modelo de dados
    // fazer o pcs.firePropertyChange(valor a alterar, antigo, modelo para ir buscar o valor);
    public ChessGameManager(String filePath) {
        this.chessGame = new ChessGame(filePath);
    }

    public boolean movePieceCoordinates(int startCol, int startRow, int endCol, int endRow) {
        //boolean result = chessGame.executeMove(endCol, endRow, startCol, startRow);
        boolean result = cm.invokeCommand(new MovePieceCommand(chessGame, endCol, endRow, startCol, startRow));


        // String oldBoardState = chessGame.getQueryState();
        if(result) {
            pcs.firePropertyChange(BOARD_STATE, null, chessGame.getQueryState());
            pcs.firePropertyChange(CURRENT_PLAYER, null, chessGame.getCurrentPlayer());
        }
        return result;
    }

    public boolean checkPiece(int col, int row) {
        boolean result = chessGame.checkPiece(col, row);
        return result;
    }

    public String getCurrentPlayer() {
        return chessGame.getCurrentPlayer();
    }

    public String getPlayer1() {
        return chessGame.getPlayer1();
    }

    public String getPlayer2() {
        return chessGame.getPlayer2();
    }

    public String getWinner() {
        if (chessGame.getWinner() == 1) {
            return "WHITE";
        }else if (chessGame.getWinner() == 0) {
            return "BLACK";
        }
        return null;
    }

    public List<int[]> getValidMoves(int col, int row) {
        return chessGame.getValidMoves(col, row);
    }

    public String getGameState() {
        return chessGame.getQueryState();
    }

    public void resetGame() {
        chessGame.resetGame();
    }
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
    public void saveGame(String filePath) {
        try {
            //chessGame.saveGame(filePath);
            ChessGameSerialization.serialize(chessGame, filePath);
            System.out.println("Jogo em: " + filePath);
        } catch (IOException e) {
            System.err.println("Erro ao guardar jogo: " + e.getMessage());
        }
    }

    public void setPlayersNames(String n1, String n2){ chessGame.setPlayersNames(n1, n2);}

    public void exportGameCsv(String filePath) {chessGame.exportCsv(filePath);}

    public void importGameCsv(String filePath) {
        chessGame.importCsv(filePath);
        pcs.firePropertyChange(BOARD_STATE, null, chessGame.getQueryState());
        pcs.firePropertyChange(CURRENT_PLAYER, null, chessGame.getCurrentPlayer());
    }
    public int getBoardSize(){return chessGame.getBoardSize();}
    public String getPieceImageString(int row, int col){return chessGame.getPieceImageString(row, col);}
    public String getPieceSoundString(int row, int col){return chessGame.getPieceSoundString(row, col);}

    public void setShowPossibleMoves(boolean show){chessGame.setShowPossibleMoves(show);}

    public boolean getShowPossibleMoves(){return chessGame.getShowPossibleMoves();}

    public void setLearningMode(boolean show){chessGame.setLearningMode(show);}

    public boolean getLearningMode(){return chessGame.getLearningMode();}
    public void setSoundOn(boolean show){chessGame.setSoundOn(show);}

    public boolean getSoundOn(){return chessGame.getSoundOn();}

    //Command

    public boolean hasUndo() {
        return cm.hasUndo();
    }

    public boolean undo() {
        boolean result = cm.undo();
        pcs.firePropertyChange(BOARD_STATE, null, chessGame.getQueryState());
        pcs.firePropertyChange(CURRENT_PLAYER, null, chessGame.getCurrentPlayer());
        return result;
    }

    public boolean hasRedo() {
        return cm.hasRedo();
    }

    public boolean redo() {
        boolean result = cm.redo();
        pcs.firePropertyChange(BOARD_STATE, null, chessGame.getQueryState());
        pcs.firePropertyChange(CURRENT_PLAYER, null, chessGame.getCurrentPlayer());
        return result;
    }
}