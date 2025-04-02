package pt.isec.pa.chess.model.data;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight(boolean isWhite, int col, int row) {
        super(isWhite, col, row);
    }

    @Override
    public List<int[]> getPossibleMoves(Board board) {
        List<int[]> moves = new ArrayList<>();

        int [][] knightMoves = {
                {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
                {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };

        for (int[] move : knightMoves) {
            int newCol = col + move[0];
            int newRow = row + move[1];

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
}
