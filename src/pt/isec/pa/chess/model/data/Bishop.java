package pt.isec.pa.chess.model.data;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    public Bishop(boolean isWhite, int col, int row) { super(isWhite, col, row);}
    public Bishop(boolean isWhite, int col, int row, boolean hasMoved) { super(isWhite, col, row, hasMoved);}


    @Override
    protected int[][] getDirections() {
        return new int[][]{
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };
    }

    @Override
    public String getSymbol() {
        return isWhite ? "B" : "b";
    }

}
