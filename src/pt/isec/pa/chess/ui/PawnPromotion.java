package pt.isec.pa.chess.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.isec.pa.chess.model.ChessGameManager;

import java.util.Objects;

public class PawnPromotion extends Stage {

    ChessGameManager chessGameManager;

    Button btnQueen, btnRook, btnKnight, btnBishop;

    public PawnPromotion(ChessGameManager data) {
        this.chessGameManager = data;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        btnQueen = new Button();
        btnQueen.setMaxWidth(Double.MAX_VALUE);
        btnQueen.setMaxHeight(Double.MAX_VALUE);
        btnRook = new Button();
        btnRook.setMaxWidth(Double.MAX_VALUE);
        btnRook.setMaxHeight(Double.MAX_VALUE);
        btnKnight = new Button();
        btnKnight.setMaxWidth(Double.MAX_VALUE);
        btnKnight.setMaxHeight(Double.MAX_VALUE);
        btnBishop = new Button();
        btnBishop.setMaxWidth(Double.MAX_VALUE);
        btnBishop.setMaxHeight(Double.MAX_VALUE);

        String color = chessGameManager.getCurrentPlayer();

        try {
            if (color.equals("WHITE")) {
                color = "W";
            }else{
                color = "B";
            }

            Image queenImage = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/pt/isec/pa/chess/ui/res/images/pieces/queen" + color + ".png")));
            ImageView queenView = new ImageView(queenImage);
            queenView.setFitWidth(80);
            queenView.setFitHeight(80);
            btnQueen.setGraphic(queenView);

            Image rookImage = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/pt/isec/pa/chess/ui/res/images/pieces/rook" + color + ".png")));
            ImageView rookView = new ImageView(rookImage);
            rookView.setFitWidth(80);
            rookView.setFitHeight(80);
            btnRook.setGraphic(rookView);

            Image knightImage = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/pt/isec/pa/chess/ui/res/images/pieces/knight" + color + ".png")));
            ImageView knightView = new ImageView(knightImage);
            knightView.setFitWidth(80);
            knightView.setFitHeight(80);
            btnKnight.setGraphic(knightView);

            Image bishopImage = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/pt/isec/pa/chess/ui/res/images/pieces/bishop" + color + ".png")));
            ImageView bishopView = new ImageView(bishopImage);
            bishopView.setFitWidth(80);
            bishopView.setFitHeight(80);
            btnBishop.setGraphic(bishopView);

        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
            // caso as imagens n carreguem usa o texto
            btnQueen.setText("Queen");
            btnRook.setText("Rook");
            btnKnight.setText("Knight");
            btnBishop.setText("Bishop");
        }

        Label lb1 = new Label("Queen");
        Label lb2 = new Label("Rook");
        Label lb3 = new Label("Knight");
        Label lb4 = new Label("Bishop");
        lb1.setMinWidth(50);
        lb2.setMinWidth(50);
        lb3.setMinWidth(50);
        lb4.setMinWidth(50);

        VBox vbQueen = new VBox(btnQueen, lb1);
        VBox vbRook = new VBox(btnRook, lb2);
        VBox vbKnight = new VBox(btnKnight, lb3);
        VBox vbBishop = new VBox(btnBishop, lb4);
        vbQueen.setAlignment(Pos.CENTER);
        vbRook.setAlignment(Pos.CENTER);
        vbKnight.setAlignment(Pos.CENTER);
        vbBishop.setAlignment(Pos.CENTER);

        HBox root = new HBox(vbQueen, vbRook, vbBishop, vbKnight);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(5);
        root.setPadding(new Insets(16));
        Scene scene = new Scene(root, 420, 150);
        this.setTitle("Pawn Promotion");
        this.setScene(scene);
        this.setResizable(false);
        this.initModality(Modality.APPLICATION_MODAL);
    }

    private void registerHandlers() {
        this.setOnCloseRequest(event -> {
            System.out.println("Escolha uma peça");
            event.consume(); // impossivel fechar sem escolher uma opção
        });

        btnQueen.setOnAction(actionEvent -> {
            chessGameManager.setEvolution("queen");
            this.close();
        });
        btnRook.setOnAction(actionEvent -> {
            chessGameManager.setEvolution("rook");
            this.close();
        });
        btnBishop.setOnAction(actionEvent -> {
            chessGameManager.setEvolution("bishop");
            this.close();
        });
        btnKnight.setOnAction(actionEvent -> {
            chessGameManager.setEvolution("knight");
            this.close();
        });
    }

    private void update() {
    }
}