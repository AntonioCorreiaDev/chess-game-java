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

    public ChessGame(){
        isWhiteTurn = true;
        board = new Board();
    }

    public void setPlayersNames(String n1, String n2){
        player1 = n1;
        player2 = n2;
        System.out.println(player1);
        System.out.print(player2);
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

    public void importCsv(String filename){
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine(); // PRIMEIRA LINHA -> jogador atual
            if (line == null)
                throw new IOException("Ficheiro CSV vazio");

            this.isWhiteTurn = line.startsWith("WHITE");
            List<String> pieces = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                if (line.isBlank())
                    continue;

                String[] parts = line.split(", "); // cada peça está separada por espaço
                for (String pieceStr : parts) {
                    pieces.add(pieceStr);
                }
            }

            // Cria um novo board vazio
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

        return kingMoves.isEmpty();// Xeque-mate
    }

    public boolean executeMove(int startCol, int startRow, int endCol, int endRow) {
        Piece piece = board.getPiece(startCol, startRow);
        if(piece == null){
            System.out.println("Nao existe nada nessa posicao!");
            return false;
        }
        if(piece.isWhite() != isWhiteTurn){
            System.out.println("Peca do adversario, impossivel mover!");
            return false;
        }
        System.out.println(piece.getString());
        List<int[]> possibleMoves = piece.getPossibleMoves(board);
        System.out.print("Teste: ");
        for (int[] move : possibleMoves) {
            System.out.print(Arrays.toString(move) + " ");
        }
        System.out.println();

        board.removePiece(startCol, startRow);
        piece.setPosition(endCol, endRow);
        board.setPiece(piece, endCol, endRow);
        System.out.print(piece.getCol() + " " +piece.getRow());
        if (isCheckMate(!isWhiteTurn)) {
            System.out.println("XEQUE-MATE! Vencedor: " + (isWhiteTurn ? "WHITE" : "BLACK"));
            return true;
        }

        isWhiteTurn = !isWhiteTurn;
        System.out.println(getQueryState());

        return false;
    }

    public static void main(String[] args) {

    }
}
