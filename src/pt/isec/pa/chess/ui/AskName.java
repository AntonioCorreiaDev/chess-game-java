package pt.isec.pa.chess.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.isec.pa.chess.model.ChessGameManager;

public class AskName extends Stage {
    ChessGameManager chessGameManager;

    TextField tfName1, tfName2;
    Button btnConfirm, btnCancel;

    public AskName(ChessGameManager data) {
        this.chessGameManager=data;

        createViews();
        registerHandlers();
        update();
    }
    private void createViews() {
        tfName1 = new TextField();
        tfName1.setPrefWidth(200);
        tfName2 = new TextField();
        tfName2.setPrefWidth(200);
        Label lb1 = new Label("Player 1:");
        Label lb2 = new Label("Player 2:");
        lb1.setMinWidth(50);
        lb2.setMinWidth(50);
        HBox field1 = new HBox(lb1,tfName1);
        HBox field2 = new HBox(lb2, tfName2);
        field1.setAlignment(Pos.BASELINE_LEFT);
        field1.setSpacing(10);
        field2.setAlignment(Pos.BASELINE_LEFT);
        field2.setSpacing(10);
        btnConfirm = new Button("Confirm");
        btnConfirm.setPrefWidth(9999);
        btnCancel  = new Button("Cancel");
        btnCancel.setPrefWidth(9999);
        HBox btns = new HBox(btnCancel,btnConfirm);
        btns.setSpacing(20);
        VBox root = new VBox(field1, field2,btns);
        root.setSpacing(10);
        root.setPadding(new Insets(16));
        Scene scene = new Scene(root,250,130);
        this.setScene(scene);
        this.setResizable(false);
    }

    private void registerHandlers() {
        btnCancel.setOnAction(actionEvent -> this.close());
        btnConfirm.setOnAction(actionEvent -> {
            chessGameManager.setPlayersNames(tfName1.getText(), tfName2.getText());
            this.close();
        });
    }

    private void update() {
    }
}
