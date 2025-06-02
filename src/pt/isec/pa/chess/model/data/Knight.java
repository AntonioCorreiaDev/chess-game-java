package pt.isec.pa.chess.model.data;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight(boolean isWhite, int col, int row) {
        super(isWhite, col, row);
    }

    public Knight(boolean isWhite, int col, int row, boolean hasMoved) { super(isWhite, col, row, hasMoved);}

    @Override
    protected int[][] getDirections() {
        return new int[][]{
                {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
                {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };
    }

    @Override
    public List<int[]> getPossibleMoves(Board board) {
        List<int[]> moves = new ArrayList<>();

        int[][] knightDirections = getDirections();

        for (int[] dir : knightDirections) {
            int newCol = col + dir[0];
            int newRow = row + dir[1];

            if (isValidMove(board, newCol, newRow)) {
                moves.add(new int[]{newCol, newRow});
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return isWhite ? "N" : "n";
    }

    @Override
    public String getType() {
        return "knight";
    }
}
