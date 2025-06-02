package pt.isec.pa.chess.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import pt.isec.pa.chess.model.ChessGameManager;
import java.util.Objects;
public class PlayersInfoPane extends VBox {

    private final ChessGameManager chessGame;

    private final Label player1Label = new Label();

    private final Label player2Label = new Label();

    private final Label nJogadas = new Label();

    private final Label currentPlayerLabel = new Label();

    private final Label winnerLabel = new Label();

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
        player1Label.setText("WHITE: " + chessGame.getPlayer1());
        player2Label.setText("BLACK: " + chessGame.getPlayer2());
        nJogadas.setText("Jogada: " + chessGame.getJogadas());
        currentPlayerLabel.setText("Current Player: " + chessGame.getCurrentPlayer());

        if (Objects.equals(chessGame.getWinner(), "CHECK")){
            winnerLabel.setText("CHECK!");
        }else if(Objects.equals(chessGame.getWinner(), "EMPATE")){
            winnerLabel.setText("Empate!");
        }else if (chessGame.getWinner() != null) {
            winnerLabel.setText("WINNER: " + chessGame.getWinner());
        }else{
            winnerLabel.setText("EM JOGO");
        }
    }

    private void registerListeners() {
        chessGame.addPropertyChangeListener(ChessGameManager.CURRENT_PLAYER, evt -> {
          update();
        });
    }
}

