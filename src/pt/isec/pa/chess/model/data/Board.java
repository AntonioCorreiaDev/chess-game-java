package pt.isec.pa.chess.model.data;

public class Board {
    private static final int BOARD_SIZE = 8;
    private Piece [][] board;

    public Board() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];
        setupInitialPosition();
    }

    private void setupInitialPosition() {

        //peças brancas
        for (int col = 0; col < BOARD_SIZE; col++) {
            board[1][col] = new Pawn(true, col, 1);
        }

        board[0][0] = new Rook(true, 0, 0);      // a1
        board[0][1] = new Knight(true, 1, 0);    // b1
        board[0][2] = new Bishop(true, 2, 0);    // c1
        board[0][3] = new Queen(true, 3, 0);     // d1
        board[0][4] = new King(true, 4, 0);      // e1
        board[0][5] = new Bishop(true, 5, 0);    // f1
        board[0][6] = new Knight(true, 6, 0);    // g1
        board[0][7] = new Rook(true, 7, 0);      // h1

        //peças pretas
        for (int col = 0; col < BOARD_SIZE; col++) {
            board[6][col] = new Pawn(false, col, 6);
        }

        board[7][0] = new Rook(false, 0, 7);     // a8
        board[7][1] = new Knight(false, 1, 7);   // b8
        board[7][2] = new Bishop(false, 2, 7);   // c8
        board[7][3] = new Queen(false, 3, 7);    // d8
        board[7][4] = new King(false, 4, 7);     // e8
        board[7][5] = new Bishop(false, 5, 7);   // f8
        board[7][6] = new Knight(false, 6, 7);   // g8
        board[7][7] = new Rook(false, 7, 7);     // h8
    }

    public boolean isValidPosition(int x, int y) {
        if(x >= BOARD_SIZE || y >= BOARD_SIZE || x < 0 || y < 0)// movimento fora do tabuleiro
            return false;

        return true;
    }

    public Piece getPiece(int col, int row){
        if (isValidPosition(col, row)) {
            return board[row][col];
        }
        return board[row][col];
    }

    public void removePiece(int col, int row){
        if (isValidPosition(col, row))
            board[row][col] = null;
    }


}



