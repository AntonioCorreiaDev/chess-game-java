package pt.isec.pa.chess.model.data;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

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
}

