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

import java.util.Objects;

public class AskName extends Stage {
    ChessGameManager chessGameManager;
    TextField tfName1, tfName2;
    Button btnConfirm, btnCancel;
    private boolean confirmed = false;

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
        Label lb1 = new Label("WHITE:");
        Label lb2 = new Label("BLACK:");
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
        btnCancel.setOnAction(actionEvent -> {
            confirmed = false;
            this.close();
        });
        btnConfirm.setOnAction(actionEvent -> {
            chessGameManager.setPlayersNames(tfName1.getText(), tfName2.getText());
            if(!Objects.equals(tfName1.getText(), tfName2.getText())){
                confirmed = true;
                this.close();
            }else{
                tfName1.clear();
                tfName2.clear();
                System.out.println("Os nomes não podem ser iguais");
            }
        });
    }

    private void update() {
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
