package pt.isec.pa.chess.model.data;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    public Bishop(boolean isWhite, int col, int row) { super(isWhite, col, row);}

    @Override
    public List<int[]> getPossibleMoves(Board board) {
        List<int[]> moves = new ArrayList<>();

        //todos os movimentos do bispo
        int[][] directions = {
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        for (int[] dir : directions) {
            int newCol = col;
            int newRow = row;

            while(true){
                newCol += dir[0];
                newRow += dir[1];

                if (!isValidMove(board, newCol, newRow)) {
                    break;
                }

                moves.add(new int[]{newCol, newRow});

                //caso encontre uma peça nao vai para alem dela
                if(board.getPiece(newCol, newRow) != null){
                    break;
                }
            }
        }
        return moves;
    }

    @Override
    public String getSymbol() {
        return isWhite ? "B" : "b";
    }
}
