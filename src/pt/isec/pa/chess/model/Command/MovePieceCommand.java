package pt.isec.pa.chess.model.Command;

import pt.isec.pa.chess.model.data.ChessGame;
import pt.isec.pa.chess.model.data.King;
import pt.isec.pa.chess.model.data.Pawn;
import pt.isec.pa.chess.model.data.Piece;

public class MovePieceCommand implements ICommand {
    protected ChessGame receiver;
    private final Piece piece;
    private final int fromCol, fromRow;
    private final int toCol, toRow;
    private Piece capturedPiece; // para quando for possivel a captura
    private Piece rook; // para quando for roque
    private final boolean pieceHasMovedBefore;
    private boolean kingSideRook;
    private boolean queenSideRook;
    private boolean enPassant;

    public MovePieceCommand(ChessGame receiver, int fromCol, int fromRow, int toCol, int toRow) {
        this.receiver = receiver;
        this.piece = receiver.getPiece(fromCol, fromRow);
        this.fromCol = fromCol;
        this.fromRow = fromRow;
        this.toCol = toCol;
        this.toRow = toRow;
        if(receiver.getPiece(fromCol, fromRow) != null){
            this.capturedPiece = receiver.getPiece(toCol, toRow);
        }else{
            this.capturedPiece = null;
        }
        this.rook = null;
        this.kingSideRook = false;
        this.queenSideRook = false;

        this.pieceHasMovedBefore = piece.isHasMoved();
    }

    @Override
    public boolean execute() {

        // Lógica de roque e en passant (mantém igual)
        if (piece instanceof King && Math.abs(fromCol - toCol) == 2) {
            //rooque pequeno (lado do rei)
            if (toCol > fromCol) {
                this.rook = receiver.getPiece(fromRow, 7);
                this.kingSideRook = true;
            } else {
                this.rook = receiver.getPiece(fromRow, 0);
                this.queenSideRook = true;
            }
        }

        // Verificar en passant
        if(piece instanceof Pawn){
            int direction = piece.isWhite() ? 1 : -1;
            Piece pawnEnPassantCaptured = receiver.getPiece(toCol, toRow - direction);
            if(pawnEnPassantCaptured instanceof Pawn
                    && pawnEnPassantCaptured.isWhite() != piece.isWhite()
                    && pawnEnPassantCaptured.isEnPassantVulnerable()){
                capturedPiece = pawnEnPassantCaptured;
                enPassant = true;
            }
        }

        int resultado = receiver.executeMove(fromCol, fromRow, toCol, toRow);

        return resultado > 0;
    }

    @Override
    public boolean undo() {
        piece.setPosition(fromCol, fromRow);
        piece.setHasMoved(pieceHasMovedBefore);
        receiver.removePiece(toCol, toRow); // Remove a peça
        receiver.setPiece(piece, fromCol, fromRow); // Move back
        if(capturedPiece != null) {
            receiver.setPiece(capturedPiece, capturedPiece.getCol(), capturedPiece.getRow()); // Move back capturedPiece
            if(enPassant && capturedPiece instanceof Pawn){
                ((Pawn) capturedPiece).setEnPassantVulnerable(true);
            }
        }
        if(rook != null) {
            if(kingSideRook){
                receiver.removePiece(5, fromRow); // Move back rook
                receiver.setPiece(rook, 7, fromRow);
            }else if(queenSideRook){
                receiver.removePiece(3, fromRow); // Move back rook
                receiver.setPiece(rook, 0, fromRow);
            }
            rook.setHasMoved(false);
        }
        receiver.changeCurrentPlayer();
        return true;
    }
}
