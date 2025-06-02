package pt.isec.pa.chess.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.ModelLog;
import pt.isec.pa.chess.ui.res.ImageManager;
import pt.isec.pa.chess.ui.res.SoundManager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class ChessBoardJFX extends Canvas {


    private static final double SQUARE_SIZE = 40;
    private static final double MARGIN = 20;


    private final ChessGameManager chessGame;
    boolean soundOn;
    boolean learningMode;
    boolean showPM;
    PropertyChangeSupport pcs;
    private PlayersInfoPane infoPane;
    private int[] selectedPosition = null;
    boolean selectedPieceLegal = false;
    public static final String SELECTED_POSITION = "selected_position";

    public ChessBoardJFX(ChessGameManager game) {
        this.chessGame = game;
        this.pcs = new PropertyChangeSupport(this);
        this.soundOn = true;
        this.learningMode = false;
        this.showPM = false;

        setWidth(400);
        setHeight(400);


        registerHandlers();
        update();

    }

    public void setSelectedPosition(int[] pos) {
        int[] old = this.selectedPosition;
        this.selectedPosition = pos;
        pcs.firePropertyChange(SELECTED_POSITION, old, pos);
    }

    public void setSoundOn(boolean show){
        soundOn = show;
        System.out.println("sound " + ((soundOn) ? "on" : "off"));
    }

    public boolean getSoundOn(){
        return soundOn;
    }

    public void setShowPossibleMoves(boolean show){
        showPM = show;
        System.out.println("Possible moves " + ((showPM) ? "enabled" : "disabled"));
    }

    public boolean getShowPossibleMoves(){
        return showPM;
    }

    public void setLearningMode(boolean show){
        learningMode = show;
        System.out.println("Learning mode " + ((learningMode) ? "enabled" : "disabled"));

    }

    public boolean getLearningMode(){
        return learningMode;
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener){
        pcs.addPropertyChangeListener(property, listener);
    }

    public void setPlayersInfoPane(PlayersInfoPane infoPane) {
        this.infoPane = infoPane;
    }

    private int[] getBoardPosition(double x, double y) {
        int size = chessGame.getBoardSize();
        double boardWidth = getWidth() - 2 * MARGIN;
        double boardHeight = getHeight() - 2 * MARGIN;
        double squareSize = Math.min(boardWidth, boardHeight) / size;

        x -= MARGIN;
        y -= MARGIN;

        if (x < 0 || y < 0) return null;

        int col = (int)(x / squareSize);
        int row = (int)(y / squareSize);

        if (col >= size || row >= size)
            return null;

        int invertedRow = size - 1 - row; // inverter coordenada Y
        return new int[] {invertedRow, col};
    }

    private void registerHandlers() {
        this.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();

            handleClick(x, y);
        });
        widthProperty().addListener((_, _, _) -> update());
        heightProperty().addListener((_, _, _) -> update());

        chessGame.addPropertyChangeListener(ChessGameManager.BOARD_STATE, evt -> {
            update();
        });

        addPropertyChangeListener(SELECTED_POSITION, evt -> update());

    }

    public void update() {
        GraphicsContext gc = getGraphicsContext2D();
        double canvasWidth = getWidth();
        double canvasHeight = getHeight();

        int size = chessGame.getBoardSize();
        double boardWidth = canvasWidth - 2 * MARGIN;
        double boardHeight = canvasHeight - 2 * MARGIN;
        double squareSize = Math.min(boardWidth, boardHeight) / size;

        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                boolean isLight = (row + col) % 2 == 0;
                gc.setFill(isLight ? Color.BEIGE : Color.SADDLEBROWN);
                gc.fillRect(MARGIN + col * squareSize, MARGIN + row * squareSize,
                        squareSize, squareSize);


                int invertedRow = size - 1 - row; // pq a nossa 0,0 é o canto inferior esquerdo
                String piece = chessGame.getPieceImageString(invertedRow, col);
                if (piece != null && !piece.isBlank()) {
                    String filename = piece + ".png";
                    Image image = ImageManager.getImage(filename);
                    if (image != null) {
                        gc.drawImage(image,
                                MARGIN + col * squareSize,
                                MARGIN + row * squareSize,
                                squareSize, squareSize);
                    }
                }
            }
        }

        //CODIGO DO HIGHLIGHT
        if (selectedPosition != null) {
            int selRow = size - 1 - selectedPosition[0]; // converter para coordenada de desenho
            int selCol = selectedPosition[1];

            if(selectedPieceLegal) {
                gc.setFill(new Color(0.5, 0.5, 0.5, 0.3));
            }else{
                gc.setFill(new Color(1.0, 0.0, 0.0, 0.3));
            }
            gc.fillRect(
                    MARGIN + selCol * squareSize,
                    MARGIN + selRow * squareSize,
                    squareSize,
                    squareSize
            );

            gc.setStroke(Color.GREY);
            gc.setLineWidth(3);
            gc.strokeRect(
                    MARGIN + selCol * squareSize,
                    MARGIN + selRow * squareSize,
                    squareSize,
                    squareSize
            );

            if(selectedPieceLegal && selectedPosition !=null && getShowPossibleMoves() && getLearningMode()) {
                List<int[]> validMoves = chessGame.getValidMoves(selectedPosition[1], selectedPosition[0]);

                for (int[] move : validMoves) {
                     int moveCol = move[0];
                    int moveRow = size - 1 - move[1]; // inverter para coordenadas certas do tabuleiro no ecra

                    gc.setFill(new Color(0, 1, 0, 0.3)); // verde transparente
                    gc.fillRect(MARGIN + moveCol * squareSize, MARGIN + moveRow * squareSize, squareSize, squareSize);

                    gc.setStroke(Color.GREEN);
                    gc.setLineWidth(2);
                    gc.strokeRect(MARGIN + moveCol * squareSize, MARGIN + moveRow * squareSize, squareSize, squareSize);
                }
            }
        }

        for (int col = 0; col < size; col++) {
            String letter = String.valueOf((char) ('a' + col));
            double x = MARGIN + col * squareSize + squareSize / 2 - 4;
            double yTop = MARGIN - 5;
            double yBottom = MARGIN + size * squareSize + 15;

            gc.setFill(Color.BLACK);
            gc.fillText(letter, x, yTop);
            gc.fillText(letter, x, yBottom);
        }

        for (int row = 0; row < size; row++) {
            String number = String.valueOf(8 - row);
            double y =MARGIN + row * squareSize  + squareSize  / 2 + 5;
            double xLeft = MARGIN - 15;
            double xRight = MARGIN + size * squareSize + 5;

            gc.setFill(Color.BLACK);
            gc.fillText(number, xLeft, y);
            gc.fillText(number, xRight, y);
        }
    }

    private void soundMove(String pieceName1, String pieceName2, String color1, int fromCol, int fromRow, int toCol, int toRow) {

        char letterFinal = (char) ('a' + toCol);
        String colSoundFinal = String.valueOf(letterFinal);

        char letterInicial = (char) ('a' + fromCol);
        String colSoundInicial = String.valueOf(letterInicial);

        String winner = chessGame.getWinner();

        String color2 = null;

        if(color1.equals("white")){
            color2 = "black";
        }else{
            color2 = "white";
        }

        if (soundOn) {
            SoundManager.stop();

            if ((pieceName1 != null && pieceName2 == null) && winner == null) {
                SoundManager.playMultiple(
                        colSoundInicial + ".mp3",
                        (fromRow + 1) + ".mp3",
                        color1 + ".mp3",
                        pieceName1 + ".mp3",
                        colSoundFinal + ".mp3",
                        (toRow + 1) + ".mp3"
                );
            } else if ((pieceName1 != null && pieceName2 == null) && winner != null) {
                SoundManager.playMultiple(
                        colSoundInicial + ".mp3",
                        (fromRow + 1) + ".mp3",
                        color1 + ".mp3",
                        pieceName1 + ".mp3",
                        colSoundFinal + ".mp3",
                        (toRow + 1) + ".mp3",
                        "check.mp3"
                );
            } else if ((pieceName1 != null && pieceName2 != null) && winner == null) {
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
            } else if ((pieceName1 != null && pieceName2 != null) && winner != null) {
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
    }

    private void handleClick(double xPixel, double yPixel) {
        if(chessGame.getPlayer1() == null || chessGame.getPlayer2() == null) {
            AskName askName = new AskName(chessGame);
            askName.showAndWait();
            chessGame.resetGame();
        }


        int[] pos = getBoardPosition(xPixel, yPixel);
        if (pos == null) {
            return;
        }

        int row = pos[0], col = pos[1];
        selectedPieceLegal = chessGame.checkPieceLegal(col, row);
        String piece = chessGame.getPieceImageString(row, col);

        if (selectedPosition == null) {
            if (piece != null && !piece.isBlank()) {
                setSelectedPosition(pos);
            }
        } else if (getLearningMode() && selectedPosition != null && (pos[0] == selectedPosition[0] && pos[1] == selectedPosition[1])) {
            setSelectedPosition(null);//clicando na peça deixa de estar selecionada

        }else{
            String pieceName1 = chessGame.getPieceName(selectedPosition[0], selectedPosition[1]);
            String pieceName2 = chessGame.getPieceName(row, col);
            String pieceColor1 = chessGame.getPieceColor(selectedPosition[0], selectedPosition[1]);

            if(pieceName1.equals("pawn")){
                int direction;
                if(pieceColor1.equals("white")){
                    direction = 1;
                }else{
                    direction = -1;
                }
                String pawnEnPassantCapturedName = chessGame.getPieceName(row - direction, col);
                String pawnEnPassantCapturedColor = chessGame.getPieceColor(row - direction, col);
                boolean pawnEnPassantVulnerable = chessGame.getPieceEnPassantVulnerable(row - direction, col);
                if(pawnEnPassantCapturedName != null && pawnEnPassantCapturedName.equals(pieceName1) && !pawnEnPassantCapturedColor.equals(pieceColor1) && pawnEnPassantVulnerable){
                    pieceName2 = pawnEnPassantCapturedName;
                }
            }

            boolean moved = chessGame.movePieceCoordinates(selectedPosition[1], selectedPosition[0], col, row);
            if (moved) {
                if(soundOn){
                    soundMove(pieceName1, pieceName2, pieceColor1, selectedPosition[1], selectedPosition[0], col, row);
                }
                String logMsg = String.format(
                        "%s de [%d,%d]->[%d,%d]",
                        pieceColor1 + " " + pieceName1, selectedPosition[0], selectedPosition[1], row, col
                );
                setSelectedPosition(null);

                //promotio pawn
                if(pieceName1.equals("pawn")){
                    if(pieceColor1.equals("white")){
                        if(row == 7) {
                            PawnPromotion pp = new PawnPromotion(chessGame);
                            pp.showAndWait();
                            chessGame.promotePawn(col, row, true);
                        }
                    }else{
                        if(row == 0) {
                            PawnPromotion pp = new PawnPromotion(chessGame);
                            pp.showAndWait();
                            chessGame.promotePawn(col, row, false);
                        }
                    }
                }
                ModelLog.getInstance().log(logMsg);
            } else {
                if(pos[0] != selectedPosition[0] || pos[1] != selectedPosition[1]) {
                    String logMsg = String.format(
                            "Movimento inválido de [%d,%d] para [%d,%d]",
                            selectedPosition[0], selectedPosition[1], row, col
                    );
                    ModelLog.getInstance().log(logMsg);
                }
            }
            if(learningMode || !selectedPieceLegal){
                setSelectedPosition(null);
            }
        }
    }
}