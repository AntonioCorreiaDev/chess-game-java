package pt.isec.pa.chess.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.data.ModelLog;
import pt.isec.pa.chess.ui.res.ImageManager;

import java.util.List;

public class ChessBoardJFX extends Canvas {

    private static final double SQUARE_SIZE = 40;
    private static final double MARGIN = 20;

    private final ChessGameManager chessGame;
    private PlayersInfoPane infoPane;
    private int[] selectedPosition = null;
    String pieceShow;
    boolean check = false;


    public ChessBoardJFX(ChessGameManager game) {
        this.chessGame = game;

        setWidth(400);
        setHeight(400);

        widthProperty().addListener((_, _, _) -> update());
        heightProperty().addListener((_, _, _) -> update());

        registerHandlers();
        update();

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

            System.out.println("Selecionada peça: " + x + ", " + y);
            handleClick(x, y);
        });

        chessGame.addPropertyChangeListener(ChessGameManager.BOARD_STATE, evt -> {
            update();
        });

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

            if(check) {
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

            if(check){
                List<int[]> validMoves = chessGame.getValidMoves(selectedPosition[1], selectedPosition[0]);
                //System.out.printf("\npeça em: " + selectedPosition[1] + ", " + selectedPosition[0] + "\n");
                for (int[] move : validMoves) {
                    //System.out.printf("-> [%d, %d]%n", move[0], move[1]);
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


    private void handleClick(double xPixel, double yPixel) {
        int[] pos = getBoardPosition(xPixel, yPixel);
        if (pos == null) {
            //System.out.println("Clique fora do tabuleiro.");
            return;
        }

        int row = pos[0], col = pos[1];
        //System.out.printf("Clicou em linha %d, coluna %d%n", row, col);
        check = chessGame.checkPiece(col, row);
        String piece = chessGame.getPieceImageString(row, col);

        if (selectedPosition == null) {
            if (piece != null && !piece.isBlank()) {
                selectedPosition = pos;
                //System.out.printf("Selecionada %s em [%d,%d]%n", piece,row, col);
                pieceShow = piece;
                update();
            }
        } else {
            boolean moved = chessGame.movePieceCoordinates(col, row, selectedPosition[1], selectedPosition[0]);
            if (moved) {
                String logMsg = String.format(
                        "%s de [%d,%d]->[%d,%d]",
                        pieceShow, selectedPosition[0], selectedPosition[1], row, col
                );
                ModelLog.getInstance().log(logMsg); // ⬅️ LOG AQUI
            } else {
                String logMsg = String.format(
                        "Movimento inválido de [%d,%d] para [%d,%d]",
                        selectedPosition[0], selectedPosition[1], row, col
                );
                ModelLog.getInstance().log(logMsg);
            }
            selectedPosition = null;
            pieceShow = null;
            //update();
            //infoPane.update();
        }
    }
}