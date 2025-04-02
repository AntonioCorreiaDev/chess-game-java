package pt.isec.pa.chess.model.data;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(boolean isWhite, int col, int row) { super(isWhite, col, row);}

    @Override
    public List<int[]> getPossibleMoves(Board board) {
        List<int[]> moves = new ArrayList<>();

        //todos os movimentos do rei
        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1},  {1, 0},  {1, 1}
        };

        for (int[] dir : directions) {
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
        return isWhite ? "K" : "k";
    }
}
