package pt.isec.pa.chess.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.ModelLog;

public class PlayersInfoPane extends VBox {

    private final ChessGameManager chessGame;
    private final Label player1Label = new Label();
    private final Label player2Label = new Label();
    private final Label nJogadas = new Label();
    private final Label currentPlayerLabel = new Label();
    private final Label winnerLabel = new Label();
    private int jogadas = 0;

    public PlayersInfoPane(ChessGameManager chessGame) {
        this.chessGame = chessGame;
        createViews();
        registerListeners();
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
        nJogadas.setText("Jogada: " + jogadas);
        currentPlayerLabel.setText("Current Player: " + chessGame.getCurrentPlayer());

        if (chessGame.getWinner() != null) {
            winnerLabel.setText("WINNER: " + chessGame.getWinner());
        }else{
            winnerLabel.setText("EM JOGO");
            jogadas ++;
        }
    }

    private void registerListeners() {
        chessGame.addPropertyChangeListener(ChessGameManager.CURRENT_PLAYER, evt -> {
          update();
        });
    }

    public void reset() {
        jogadas = 0;
        update();
    }
}

