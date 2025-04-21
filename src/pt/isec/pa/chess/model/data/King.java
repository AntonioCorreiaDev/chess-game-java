package pt.isec.pa.chess.model.data;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(boolean isWhite, int col, int row) { super(isWhite, col, row);}
    public King(boolean isWhite, int col, int row, boolean hasMoved) { super(isWhite, col, row, hasMoved);}

    @Override
    protected int[][] getDirections() {
        return new int[][]{
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1},  {1, 0},  {1, 1}
        };
    }

    @Override
    public String getSymbol() {
        return isWhite ? "K" : "k";
    }
}
