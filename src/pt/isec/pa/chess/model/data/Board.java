package pt.isec.pa.chess.model.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class Board implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    private static final int BOARD_SIZE = 8;
    private Piece [][] board;

    public Board() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];
        setupInitialPosition();
    }

    public Board(List<String> pieces) {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];
        for (String value : pieces) {
            Piece piece = PieceType.createPieceFromString(value);
            board[piece.getRow()][piece.getCol()] = piece;
            System.out.println("Adicionei " + piece + " em (" + piece.getRow() + "," + piece.getCol() + ")");
        }
    }

        public Board(String boardState){
            board = new Piece[BOARD_SIZE][BOARD_SIZE];
            setupFromFile(boardState);
        }
        private void setupFromFile(String boardState){
            String[] piecesAndPositions = boardState.split(",\\s*");
            for (String pieceAndPosition : piecesAndPositions) {
                String pieceStr = pieceAndPosition.substring(0, pieceAndPosition.length() - 2); // Remove o asterisco ou a parte da posição
                String position = pieceAndPosition.substring(pieceAndPosition.length() - 2); // Posição da peça (ex: a5)

                int row = BOARD_SIZE - (position.charAt(1) - '0');  // Calcula a linha (invertendo as coordenadas, pois o CSV usa o formato do tipo a1, b2...)
                int col = position.charAt(0) - 'a';  // Calcula a coluna com base na letra

                // Usa o PieceType.createPiece para criar a peça
                Piece piece = PieceType.createPieceFromString(pieceStr + position);
                if (piece != null) {
                    board[row][col] = piece;  // Coloca a peça na posição correta
                }
            }
        }

    private void setupInitialPosition() {

        //peças brancas
        for (int col = 0; col < BOARD_SIZE; col++) {
            board[1][col] = new Pawn(true, col, 1);
        }

        board[0][0] = PieceType.ROOK.createPiece(PieceType.ROOK, true, 0, 0);      // a1
        board[0][1] = PieceType.KNIGHT.createPiece(PieceType.KNIGHT,true, 1, 0);    // b1
        board[0][2] = PieceType.BISHOP.createPiece(PieceType.BISHOP,true, 2, 0);    // c1
        board[0][3] = PieceType.QUEEN.createPiece(PieceType.QUEEN,true, 3, 0);     // d1
        board[0][4] = PieceType.KING.createPiece(PieceType.KING,true, 4, 0);      // e1
        board[0][5] = PieceType.BISHOP.createPiece(PieceType.BISHOP,true, 5, 0);    // f1
        board[0][6] = PieceType.KNIGHT.createPiece(PieceType.KNIGHT,true, 6, 0);    // g1
        board[0][7] = PieceType.ROOK.createPiece(PieceType.ROOK,true, 7, 0);      // h1

        //peças pretas
        for (int col = 0; col < BOARD_SIZE; col++) {
            board[6][col] = PieceType.PAWN.createPiece(PieceType.PAWN,false, col, 6);
        }

        board[7][0] = PieceType.ROOK.createPiece(PieceType.ROOK,false, 0, 7);     // a8
        board[7][1] = PieceType.KNIGHT.createPiece(PieceType.KNIGHT,false, 1, 7);   // b8
        board[7][2] = PieceType.BISHOP.createPiece(PieceType.BISHOP,false, 2, 7);   // c8
        board[7][3] = PieceType.QUEEN.createPiece(PieceType.QUEEN,false, 3, 7);    // d8
        board[7][4] = PieceType.KING.createPiece(PieceType.KING,false, 4, 7);     // e8
        board[7][5] = PieceType.BISHOP.createPiece(PieceType.BISHOP,false, 5, 7);   // f8
        board[7][6] = PieceType.KNIGHT.createPiece(PieceType.KNIGHT,false, 6, 7);   // g8
        board[7][7] = PieceType.ROOK.createPiece(PieceType.ROOK,false, 7, 7);     // h8
    }

    public boolean isValidPosition(int x, int y) {
        if(x >= BOARD_SIZE || y >= BOARD_SIZE || x < 0 || y < 0)// movimento fora do tabuleiro
            return false;

        return true;
    }

    public Piece getPiece(int col, int row){
        if (isValidPosition(col, row)) {
            return board[row][col];
        }
        return null;
    }

    public String getAllBoardText() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                if (board[x][y] != null) {
                    if (!first) {
                        sb.append(",");
                    }
                    sb.append(board[x][y].getString());
                    first = false;
                }
            }
        }

        return sb.toString();
    }

    public void removePiece(int col, int row){
        if (isValidPosition(col, row))
            board[row][col] = null;
    }

    public void setPiece(Piece piece, int col, int row){
        if(isValidPosition(col, row))
            board[row][col] = piece;
    }

    //PARTE DO CHECK AQUI
    public int[] findKingPosition(boolean isWhite) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = getPiece(col, row);
                if (piece instanceof King && piece.isWhite() == isWhite) {
                    return new int[]{col, row};
                }
            }
        }
        return null; // à partida nunca chega aqui
    }

    public boolean isSquareUnderAttack(int targetCol, int targetRow, boolean byWhite) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = getPiece(col, row);
                if (piece != null && piece.isWhite() == byWhite) {
                    // Ignora o rei para evitar recursão
                    if (piece instanceof King) continue;

                    List<int[]> moves = piece.getPossibleMoves(this);
                    for (int[] move : moves) {
                        if (move[0] == targetCol && move[1] == targetRow) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}



