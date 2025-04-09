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

        if(directions == null){
            if(this instanceof Pawn)
                moves.addAll(getPawnMoves(board));
            else if(this instanceof Knight)
                moves.addAll(getKnightMoves(board));
            else if(this instanceof King)
                moves.addAll(getKingMoves(board));
        }else{
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
        }
        return moves;
    }
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

        //impossivel comer peças da msm cor
        if(pieceAtTarget != null && pieceAtTarget.getColor() == getColor()) return false;

        return true;
    }

    private List<int[]> getKingMoves(Board board) {
        List<int[]> moves = new ArrayList<>();
        int[][] kingDirections = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1},  {1, 0},  {1, 1}
        };

        for (int[] dir : kingDirections) {
            int newCol = col + dir[0];
            int newRow = row + dir[1];

            // Verifica se o movimento é válido E se a casa de destino não está sob ataque
            if (isValidMove(board, newCol, newRow) &&
                    !board.isSquareUnderAttack(newCol, newRow, !isWhite)) {
                moves.add(new int[]{newCol, newRow});
            }
        }

        return moves;
    }

    private List<int[]> getPawnMoves(Board board) {
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

    private List<int[]> getKnightMoves(Board board) {
        List<int[]> moves = new ArrayList<>();

        int[][] knightMoves = {
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
}
