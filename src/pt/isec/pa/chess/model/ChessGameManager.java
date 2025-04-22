package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.ChessGame;

import java.io.IOException;

public class ChessGameManager {


    private ChessGame chessGame;

    public ChessGameManager() {
        this.chessGame = new ChessGame();
    }

    public ChessGameManager(String filePath) {
        this.chessGame = new ChessGame(filePath);
    }

    public boolean movePiece(String from, String to) {
        if (from.length() != 2 || to.length() != 2 || from.charAt(0) < 'a' || from.charAt(0) > 'h') {
            throw new IllegalArgumentException("Coordenadas inválidas: " + from + " -> " + to);
        }

        int startCol = from.charAt(0) - 'a';
        int startRow = 8 - Character.getNumericValue(from.charAt(1));

        int endCol = to.charAt(0) - 'a';
        int endRow = 8 - Character.getNumericValue(to.charAt(1));

        return chessGame.executeMove(startCol, startRow, endCol, endRow);
    }

    public String getCurrentPlayer() {
        return chessGame.getCurrentPlayer();
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
}
