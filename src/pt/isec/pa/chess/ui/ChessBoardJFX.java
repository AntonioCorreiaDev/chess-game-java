package pt.isec.pa.chess.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.ui.res.ImageManager;

import java.util.List;

public class ChessBoardJFX extends Canvas {

    private static final double SQUARE_SIZE = 40;
    private static final double MARGIN = 20;

    private final ChessGameManager chessGame;
    private PlayersInfoPane infoPane;
    private int[] selectedPosition = null;


    public ChessBoardJFX(ChessGameManager game) {
        super(game.getBoardSize() * SQUARE_SIZE + 2 * MARGIN,
                game.getBoardSize() * SQUARE_SIZE + 2 * MARGIN);
        this.chessGame = game;
        createViews();
        registerHandlers();
        update();
    }

    public void setPlayersInfoPane(PlayersInfoPane infoPane) {
        this.infoPane = infoPane;
    }

    private int[] getBoardPosition(double x, double y) {
        x -= MARGIN;
        y -= MARGIN;

        if (x < 0 || y < 0) return null;

        int col = (int)(x / SQUARE_SIZE);
        int row = (int)(y / SQUARE_SIZE);

        if (col >= chessGame.getBoardSize() || row >= chessGame.getBoardSize())
            return null;

        int invertedRow = chessGame.getBoardSize() - 1 - row; // inverter coordenada Y
        return new int[] {invertedRow, col};
    }


    private void createViews() {
        drawBoard();
        if(infoPane != null) {
            infoPane.update();
        }
    }

    private void registerHandlers() {
        this.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();

            System.out.println("Selecionada peça: " + x + ", " + y);
            handleClick(x, y);
        });
    }

    public void update() {
        drawBoard();
    }

    private void drawBoard() {
        System.out.println("Aqui");
        GraphicsContext gc = getGraphicsContext2D();
        int size = chessGame.getBoardSize();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                boolean isLight = (row + col) % 2 == 0;
                gc.setFill(isLight ? Color.BEIGE : Color.SADDLEBROWN);
                gc.fillRect(MARGIN + col * SQUARE_SIZE, MARGIN + row * SQUARE_SIZE,
                        SQUARE_SIZE, SQUARE_SIZE);


                int invertedRow = size - 1 - row; // pq a nossa 0,0 é o canto inferior esquerdo
                String piece = chessGame.getPieceImageString(invertedRow, col);
                if (piece != null && !piece.isBlank()) {
                    String filename = piece + ".png";
                    Image image = ImageManager.getImage(filename);
                    if (image != null) {
                        gc.drawImage(image,
                                MARGIN + col * SQUARE_SIZE,
                                MARGIN + row * SQUARE_SIZE,
                                SQUARE_SIZE, SQUARE_SIZE);
                    }
                }
            }
        }

        //CODIGO DO HIGHLIGHT
        if (selectedPosition != null) {
            int selRow = size - 1 - selectedPosition[0]; // converter para coordenada de desenho
            int selCol = selectedPosition[1];

            gc.setFill(new Color(1, 0, 0, 0.3)); // vermelho transparente
            gc.fillRect(
                    MARGIN + selCol * SQUARE_SIZE,
                    MARGIN + selRow * SQUARE_SIZE,
                    SQUARE_SIZE,
                    SQUARE_SIZE
            );

            gc.setStroke(Color.RED);
            gc.setLineWidth(3);
            gc.strokeRect(
                    MARGIN + selCol * SQUARE_SIZE,
                    MARGIN + selRow * SQUARE_SIZE,
                    SQUARE_SIZE,
                    SQUARE_SIZE
            );

            List<int[]> validMoves = chessGame.getValidMoves(selectedPosition[1], selectedPosition[0]);
            //System.out.printf("\npeça em: " + selectedPosition[1] + ", " + selectedPosition[0] + "\n");
            for (int[] move : validMoves) {
                //System.out.printf("-> [%d, %d]%n", move[0], move[1]);
                int moveCol = move[0];
                int moveRow = size - 1 - move[1]; // inverter para coordenadas certas do tabuleiro no ecra

                gc.setFill(new Color(0, 1, 0, 0.3)); // verde transparente
                gc.fillRect(MARGIN + moveCol * SQUARE_SIZE, MARGIN + moveRow * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

                gc.setStroke(Color.GREEN);
                gc.setLineWidth(2);
                gc.strokeRect(MARGIN + moveCol * SQUARE_SIZE, MARGIN + moveRow * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }

        for (int col = 0; col < size; col++) {
            String letter = String.valueOf((char) ('a' + col));
            double x = MARGIN + col * SQUARE_SIZE + SQUARE_SIZE / 2 - 4;
            double yTop = MARGIN - 5;
            double yBottom = MARGIN + size * SQUARE_SIZE + 15;

            gc.setFill(Color.BLACK);
            gc.fillText(letter, x, yTop);
            gc.fillText(letter, x, yBottom);
        }

        for (int row = 0; row < size; row++) {
            String number = String.valueOf(8 - row);
            double y =MARGIN + row * SQUARE_SIZE + SQUARE_SIZE / 2 + 5;
            double xLeft = MARGIN - 15;
            double xRight = MARGIN + size * SQUARE_SIZE + 5;

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

        String piece = chessGame.getPieceImageString(row, col);

        if (selectedPosition == null) {
            if (piece != null && !piece.isBlank()) {
                selectedPosition = pos;
                //System.out.printf("Selecionada %s em [%d,%d]%n", piece,row, col);

                update();
            }
        } else {
            boolean moved = chessGame.movePieceCoordinates(col, row, selectedPosition[1], selectedPosition[0]);
            if (moved) {
                System.out.printf("Movida de [%d,%d] para [%d,%d]%n",
                        selectedPosition[0], selectedPosition[1], row, col);
            } else {
                System.out.printf("Movimento inválido de [%d,%d] para [%d,%d]%n",
                        selectedPosition[0], selectedPosition[1], row, col);
            }
            selectedPosition = null;
            update();
            infoPane.update();
        }
    }
}


