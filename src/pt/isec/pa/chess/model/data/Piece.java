package pt.isec.pa.chess.model.data;
import java.util.List;

public abstract class Piece {

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

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void setPosition(int col, int row){
        this.col = col;
        this.row = row;
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

    public abstract List<int []> getPossibleMoves(Board board);
    public abstract String getSymbol();

    public String getString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getSymbol());
        sb.append('a' + col);
        sb.append(row + 1);
        if (hasMoved) sb.append("*");
        return sb.toString();
    };

    protected boolean isValidMove(Board board, int toCol, int toRow){
        if(!board.isValidPosition(toCol, toRow)) return false;

        Piece pieceAtTarget = board.getPiece(toCol, toRow);

        //impossivel comer peças da msm cor
        if(pieceAtTarget != null && pieceAtTarget.isWhite() == isWhite) return false;

        return true;
    }

}
