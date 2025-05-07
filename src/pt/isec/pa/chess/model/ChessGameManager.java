package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.ChessGame;

import java.io.IOException;
import java.util.List;

public class ChessGameManager {


    private ChessGame chessGame;

    public ChessGameManager() {
        this.chessGame = new ChessGame();
    }

    public ChessGameManager(String filePath) {
        this.chessGame = new ChessGame(filePath);
    }

    public boolean movePieceCoordinates(int startCol, int startRow, int endCol, int endRow) {
        return chessGame.executeMove(endCol, endRow, startCol, startRow);
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
        if (chessGame.getWinner() == 0) {
            return "WHITE";
        }else if (chessGame.getWinner() == 1) {
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
            this.chessGame = ChessGame.loadGame(filePath);
            System.out.println("Jogo  de: " + filePath);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar jogo: " + e.getMessage());
        }
    }
    public void saveGame(String filePath) {
        try {
            chessGame.saveGame(filePath);
            System.out.println("Jogo em: " + filePath);
        } catch (IOException e) {
            System.err.println("Erro ao guardar jogo: " + e.getMessage());
        }
    }

    public void SetPlayersNames(String n1, String n2){ chessGame.setPlayersNames(n1, n2);}
    public void exportGameCsv(String filePath) {
        chessGame.exportCsv(filePath);
    }

    public void importGameCsv(String filePath) {
        chessGame.importCsv(filePath);
    }
    public int getBoardSize(){return chessGame.getBoardSize();}
    public String getPieceImageString(int row, int col){return chessGame.getPieceImageString(row, col);}
}