package pt.isec.pa.chess.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import pt.isec.pa.chess.model.ChessGameManager;

public class PlayersInfoPane extends VBox {

    private final ChessGameManager chessGame;
    private final Label player1Label = new Label();
    private final Label player2Label = new Label();
    private final Label nJogadas = new Label();
    private final Label currentPlayerLabel = new Label();
    private final Label winnerLabel = new Label();
    private int teste = 0;

    public PlayersInfoPane(ChessGameManager chessGame) {
        this.chessGame = chessGame;
        createViews();
        update();
    }

    private void createViews() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));
        this.getChildren().addAll(player1Label, player2Label, nJogadas, currentPlayerLabel, winnerLabel);
        winnerLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
    }

    public void update() {
        player1Label.setText("Player 1: " + chessGame.getPlayer1());
        player2Label.setText("Player 2: " + chessGame.getPlayer2());
        nJogadas.setText("Jogada: " + (teste - 1));
        currentPlayerLabel.setText("Current Player: " + chessGame.getCurrentPlayer());

        if (chessGame.getWinner() != null) {
            winnerLabel.setText("WINNER: " + chessGame.getWinner());
        }else{
            winnerLabel.setText("EM JOGO");
            teste ++;
        }
    }

    public void reset() {
        teste = 1;
        update();
    }
}

