package pt.isec.pa.chess.model.data;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Piece implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    protected int col;
    protected int row;
    protected boolean isWhite;
    protected boolean hasMoved;

    public Piece(boolean isWhite, int col, int row) {
        this.isWhite = isWhite;
        this.col = col;
        this.row = row;
        this.hasMoved = false;
    }

    public Piece(boolean isWhite, int col, int row, boolean hasMoved) {
        this.isWhite = isWhite;
        this.col = col;
        this.row = row;
        this.hasMoved = hasMoved;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void setPosition(int col, int row){

        if(!hasMoved)
            hasMoved = true;

        this.col = col;
        this.row = row;
    }
    private boolean getColor(){
        return isWhite;
    }

    public String getColorString(){
        if(isWhite)
            return "W";
        return "B";
    }

    public boolean isWhite() {
        return isWhite;
    }

    public boolean isHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public List<int []> getPossibleMoves(Board board){
        List<int[]> moves = new ArrayList<>();
        int [][] directions = getDirections();

        for (int[] dir : directions) {
            int newCol = col;
            int newRow = row;

            while (true) {
                newCol += dir[0];
                newRow += dir[1];

                if (!isValidMove(board, newCol, newRow)) {
                    break;
                }

                moves.add(new int[]{newCol, newRow});
                if (board.getPiece(newCol, newRow) != null && !(board.getPiece(newCol, newRow) instanceof King)) {
                    break;
                }
            }
        }
        return moves;
    }

    public abstract String getType();
    public abstract String getSymbol();
    protected abstract int[][] getDirections();

    public String getString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getSymbol());
        sb.append((char)('a' + col));
        sb.append(row + 1);
        if ((this instanceof King || this instanceof Rook ) && !hasMoved) sb.append("*");
        return sb.toString();
    }

    protected boolean isValidMove(Board board, int toCol, int toRow){
        if(!board.isValidPosition(toCol, toRow)) return false;

        Piece pieceAtTarget = board.getPiece(toCol, toRow);

        //impossivel comer peÃ§as da msm cor
        if(pieceAtTarget != null && pieceAtTarget.getColor() == getColor()) return false;

        return true;
    }
}