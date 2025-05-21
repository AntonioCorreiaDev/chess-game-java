package pt.isec.pa.chess.model.data;

import javafx.application.Application;
import pt.isec.pa.chess.ui.MainJFX;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChessGame implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    boolean isWhiteTurn;
    Board board;
    String player1, player2;
    boolean learningMode;
    boolean showPM;
    boolean soundOn;
    int winner = -1; // -1 sem winner, 0 winner é preto, 1 winner é branco


    public ChessGame(){
        isWhiteTurn = true;
        learningMode = false;
        showPM = false;
        soundOn = true;
        board = new Board();
    }
    public void setSoundOn(boolean show){
        soundOn = show;
        System.out.println("sound " + ((soundOn) ? "on" : "off"));
    }

    public boolean getSoundOn(){
        return soundOn;
    }
    public void setShowPossibleMoves(boolean show){
        showPM = show;
        System.out.println("Possible moves " + ((showPM) ? "enabled" : "disabled"));
    }

    public boolean getShowPossibleMoves(){
        return showPM;
    }


    public void setLearningMode(boolean show){
        learningMode = show;
        System.out.println("Learning mode aqui " + ((learningMode) ? "enabled" : "disabled"));

    }

    public boolean getLearningMode(){
        return learningMode;
    }

    public Piece getPiece(int row, int col){
        return board.getPiece(row, col);
    }

    public String getPieceImageString(int row, int col){
        return board.getPieceImageString(row, col);
    }
    public String getPieceSoundString(int row, int col){
        return board.getPieceSoundString(row, col);
    }


    public int getBoardSize(){
        return board.getBoardSize();
    }

    public void setPlayersNames(String n1, String n2){
        player1 = n1;
        player2 = n2;
        System.out.println(player1);
        System.out.print(player2);
    }

    public String getPlayer1(){
        return player1;
    }

    public String getPlayer2(){
        return player2;
    }

    public int getWinner(){
        return winner;
    }

    public List<int[]> getValidMoves(int col, int row) {
        return board.getValidMoves(col, row, isWhiteTurn);
    }

    public ChessGame(String gameState) {
        importCsv(gameState);
    }

    public void resetGame() {
        board = new Board();
        isWhiteTurn = true;
    }


    public String getCurrentPlayer(){
        if (isWhiteTurn) return "WHITE";
        return "BLACK";
    }

    public void changeCurrentPlayer(){
        isWhiteTurn = !isWhiteTurn;
    }

    public void saveGame(String filePath) throws IOException {
        ChessGameSerialization.serialize(this, filePath);
    }

    public static ChessGame loadGame(String filePath) throws IOException, ClassNotFoundException {
        return ChessGameSerialization.deserialize(filePath);
    }

    public String getQueryState(){ // Query the board state
        StringBuilder sb = new StringBuilder();
        sb.append(getCurrentPlayer());
        sb.append(",\n");
        sb.append(board.getAllBoardText());

        return sb.toString();
    }


    public void exportCsv(String filename){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(getQueryState());
            System.out.println("Estado do jogo salvo em: " + filename);
        } catch (IOException e) {
            System.err.println("Erro ao salvar o estado do jogo: " + e.getMessage());
        }
    }

    public void importCsv(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            StringBuilder fullText = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                fullText.append(line.trim()).append(" ");
            }

            String[] parts = fullText.toString().trim().split(",");

            if (parts.length == 0)
                throw new IOException("Ficheiro CSV vazio ou inválido.");

            String currentPlayer = parts[0].trim().toUpperCase();
            this.isWhiteTurn = currentPlayer.startsWith("WHITE");

            List<String> pieces = new ArrayList<>();
            for (int i = 1; i < parts.length; i++) {
                String piece = parts[i].trim();
                if (!piece.isEmpty()) {
                    pieces.add(piece);
                }
            }

            this.board = new Board(pieces);
        } catch (IOException e) {
            System.err.println("Erro ao importar o estado do jogo: " + e.getMessage());
        }
    }


    public boolean isCheckMate(boolean isWhite) {
        System.out.println("procurando o rei" + (isWhite ? "WHITE" : "BLACK"));
        int[] kingPos = board.findKingPosition(isWhite);
        if (kingPos == null) return false;

        // Verifica se o rei está em xeque
        if (!board.isSquareUnderAttack(kingPos[0], kingPos[1], !isWhite)) {
            return false;
        }
        System.out.println("Chegou aqui");
        // Obtém todos os movimentos possíveis do rei

        Piece king = board.getPiece(kingPos[0], kingPos[1]);
        List<int[]> kingMoves = king.getPossibleMoves(board);
        for (int[] move : kingMoves) {
            System.out.print(Arrays.toString(move) + " ");
        }

        if(kingMoves.isEmpty()){// Xeque-mate
            if(isWhite){
                winner = 0;
            }
            winner = 1;
            return true;
        }
        return false;
    }

    public boolean checkPiece(int col, int row) {
        Piece piece = board.getPiece(col, row);
        String pieceS = board.getPieceImageString(col, row);
        System.out.printf("no execute move %s\n", pieceS);
        if (piece == null) {
            System.out.println("Nao existe nada nessa posicao!");
            return false;
        }
        if (piece.isWhite() != isWhiteTurn) {
            System.out.println("Peca do adversario, impossivel mover!");
            return false;
        }
        return true;
    }

    public void removePiece(int col, int row) {
        board.removePiece(col, row);
    }

    public void setPiece(Piece piece, int col, int row) {
        board.setPiece(piece, col, row);
    }

    public boolean executeMove(int startCol, int startRow, int endCol, int endRow) {
        System.out.printf("Start col %d, start row %d, end col %d, end row %d\n", startCol, startRow, endCol, endRow);
        //System.out.println("Entrou aquui");
        Piece piece = board.getPiece(startCol, startRow);
        String pieceS = board.getPieceImageString(startRow, startCol);
        System.out.printf("no execute move %s\n",pieceS);

        if(piece.isWhite() != isWhiteTurn){
            System.out.println("erroCommand");
            return false;
        }

        List<int[]> possibleMoves = piece.getPossibleMoves(board);
        boolean isValidMove = false;
        for (int[] move : possibleMoves) {
            if (move[0] == endCol && move[1] == endRow) {
                isValidMove = true;
                break;
            }
        }

        if (!isValidMove) {
            System.out.println("Movimento inválido para esta peça.");
            return false;
        }

        System.out.println();
        board.removePiece(startCol, startRow);
        piece.setPosition(endCol, endRow);
        board.setPiece(piece, endCol, endRow);
        System.out.print(piece.getCol() + " " +piece.getRow());
        if (isCheckMate(!isWhiteTurn)) {
            System.out.println("Chegou aqui 227");
            System.out.println("XEQUE-MATE! Vencedor: " + (isWhiteTurn ? "WHITE" : "BLACK"));
            return true;
        }

        isWhiteTurn = !isWhiteTurn;
        if (isCheckMate(isWhiteTurn)) { // Verifica se o jogador atual está em checkmate
            winner = isWhiteTurn ? 0 : 1;
            System.out.println("XEQUE-MATE! Vencedor: " + (winner == 0 ? "BLACK" : "WHITE"));
            return true; // Jogo acabou
        }
        System.out.println(getQueryState());

        return true;
    }



    public static void main(String[] args) {

    }
}