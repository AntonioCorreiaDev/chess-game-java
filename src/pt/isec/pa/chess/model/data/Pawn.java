package pt.isec.pa.chess.model.data;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    private boolean enPassantVulnerable = false;

    public Pawn(boolean isWhite, int col, int row) { super(isWhite, col, row); }

    public Pawn(boolean isWhite, int col, int row, boolean hasMoved) {super(isWhite, col, row, hasMoved);}

    @Override
    protected int[][] getDirections() {
        return null;
    }

    @Override
    public List<int[]> getPossibleMoves(Board board) {
        List<int[]> moves = new ArrayList<>();
        int direction = isWhite ? 1 : -1;

        // Avançar uma casa
        int newRow = row + direction;
        if (board.isValidPosition(col, newRow) && board.getPiece(col, newRow) == null) {
            moves.add(new int[]{col, newRow});

            // Avançar duas casas, se for a primeira jogada
            if (!hasMoved) {
                int doubleRow = row + (2 * direction);
                if (board.isValidPosition(col, doubleRow) && board.getPiece(col, doubleRow) == null) {
                    moves.add(new int[]{col, doubleRow});
                }
            }
        }

        // Captura na diagonal esquerda
        int leftCol = col - 1;
        if (board.isValidPosition(leftCol, newRow)) {
            Piece piece = board.getPiece(leftCol, newRow);
            if (piece != null && piece.isWhite() != this.isWhite) {
                moves.add(new int[]{leftCol, newRow});
            }
        }

        // Captura na diagonal direita
        int rightCol = col + 1;
        if (board.isValidPosition(rightCol, newRow)) {
            Piece piece = board.getPiece(rightCol, newRow);
            if (piece != null && piece.isWhite() != this.isWhite) {
                moves.add(new int[]{rightCol, newRow});
            }
        }

        // Captura enPassant na diagonal esquerda
        if (board.isValidPosition(leftCol, newRow)) {
            Piece piece = board.getPiece(leftCol, row);
            if (piece != null && piece.isWhite() != this.isWhite && piece instanceof Pawn && piece.isEnPassantVulnerable()) {
                if((this.isWhite && newRow == 5) || (!this.isWhite && newRow == 2)) {
                    moves.add(new int[]{leftCol, newRow});
                }
            }
        }

        // Captura enPassant na diagonal direita
        if (board.isValidPosition(rightCol, newRow)) {
            Piece piece = board.getPiece(rightCol, row);
            if (piece != null && piece.isWhite() != this.isWhite && piece instanceof Pawn && piece.isEnPassantVulnerable()) {
                if((this.isWhite && newRow == 5) || (!this.isWhite && newRow == 2)) {
                    moves.add(new int[]{rightCol, newRow});
                }
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return isWhite ? "P" : "p";
    }

    @Override
    public String getType() {
        return "pawn";
    }

    @Override
    public boolean isEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    public void setEnPassantVulnerable(boolean enPassantVulnerable) {
        this.enPassantVulnerable = enPassantVulnerable;
    }
}

