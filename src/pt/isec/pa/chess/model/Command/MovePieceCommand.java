package pt.isec.pa.chess.model.Command;

import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.data.ChessGame;
import pt.isec.pa.chess.model.data.Piece;
import pt.isec.pa.chess.ui.res.SoundManager;

public class MovePieceCommand implements ICommand {
    protected ChessGame receiver;
    private final Piece piece;
    private final int fromCol, fromRow;
    private final int toCol, toRow;
    private final Piece capturedPiece; // para quando for possivel a captura
    private final boolean pieceHasMovedBefore;

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
        this.pieceHasMovedBefore = piece.isHasMoved();
    }

    @Override
    public boolean execute() {
        String pieceString = receiver.getPieceImageString(fromRow, fromCol);
        String capturedPieceString = null;
        String colorChar1 = pieceString.substring(pieceString.length() - 1);
        String color1 = colorChar1.equals("W") ? "white" : "black";

        String colorChar2 = null;
        String color2 = null;

        String pieceName1 = pieceString.substring(0, pieceString.length() - 1);
        String pieceName2 = null;

        char letterFinal = (char) ('a' + toCol);
        String colSoundFinal = String.valueOf(letterFinal);

        char letterInicial = (char) ('a' + fromCol);
        String colSoundInicial = String.valueOf(letterInicial);

        if (capturedPiece != null) {
            capturedPieceString = receiver.getPieceImageString(toRow, toCol);
            colorChar2 = capturedPieceString.substring(capturedPieceString.length() - 1);
            color2 = colorChar2.equals("W") ? "white" : "black";
            pieceName2 = capturedPieceString.substring(0, capturedPieceString.length() - 1);
        }

        boolean result = receiver.executeMove(fromCol, fromRow, toCol, toRow);
        int winner = receiver.getWinner();

        if (result && pieceString.length() > 1 && receiver.getSoundOn()) {
            System.out.println("entrou");

            if (capturedPiece == null && winner == -1) {
                SoundManager.playMultiple(
                        colSoundInicial + ".mp3",
                        (fromRow + 1) + ".mp3",
                        color1 + ".mp3",
                        pieceName1 + ".mp3",
                        colSoundFinal + ".mp3",
                        (toRow + 1) + ".mp3"
                );
            }else if(capturedPiece == null && winner != -1){
                SoundManager.playMultiple(
                        colSoundInicial + ".mp3",
                        (fromRow + 1) + ".mp3",
                        color1 + ".mp3",
                        pieceName1 + ".mp3",
                        colSoundFinal + ".mp3",
                        (toRow + 1) + ".mp3",
                        "check.mp3"
                );
            }else if(capturedPiece != null && winner == -1){
                SoundManager.playMultiple(
                        colSoundInicial + ".mp3",
                        (fromRow + 1) + ".mp3",
                        color1 + ".mp3",
                        pieceName1 + ".mp3",
                        colSoundFinal + ".mp3",
                        (toRow + 1) + ".mp3",
                        "captures.mp3",
                        color2 + ".mp3",
                        pieceName2 + ".mp3"
                );
            }else if(capturedPiece != null && winner != -1){
                SoundManager.playMultiple(
                        colSoundInicial + ".mp3",
                        (fromRow + 1) + ".mp3",
                        color1 + ".mp3",
                        pieceName1 + ".mp3",
                        colSoundFinal + ".mp3",
                        (toRow + 1) + ".mp3",
                        "captures.mp3",
                        color2 + ".mp3",
                        pieceName2 + ".mp3",
                        "check.mp3"
                );
            }
        }

        return result;
    }


    @Override
    public boolean undo() {
        piece.setPosition(fromCol, fromRow);
        piece.setHasMoved(pieceHasMovedBefore);
        receiver.removePiece(toCol, toRow); // Remove a peça
        receiver.setPiece(piece, fromCol, fromRow); // Move back
        receiver.setPiece(capturedPiece, toCol, toRow); // Move back
        receiver.changeCurrentPlayer();
        return true;
    }
}
