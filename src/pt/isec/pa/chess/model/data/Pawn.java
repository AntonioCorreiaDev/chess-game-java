package pt.isec.pa.chess.model.data;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(boolean isWhite, int col, int row) { super(isWhite, col, row); }


    @Override
    public List<int[]> getPossibleMoves(Board board) {
        List<int[]> moves = new ArrayList<>();

        // direção para os brancos é diferente dos pretos
        int direction = isWhite ? 1 : -1;

        // avançar
        int newRow = row + direction;
        if (board.isValidPosition(col, newRow) && board.getPiece(col ,newRow) == null) {
            moves.add(new int[] { col, newRow });

            // pode avançar duas casas se for a primeira jogada
            if (!hasMoved) {
                int doubleRow = row + (2 * direction);
                if (board.isValidPosition(col, doubleRow) && board.getPiece(col, doubleRow) == null) {
                    moves.add(new int[] { col, doubleRow });
                }
            }
        }
        return moves;
    }

    @Override
    public String getSymbol() {
        if (isWhite) {
            return "P" + row + col;
        } else {
            return "p" + row + col;
        }
    }
}

