package pt.isec.pa.chess.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.isec.pa.chess.model.ChessGameManager;

public class MainJFX extends Application {
    ChessGameManager chessGameManager;

    public MainJFX(){
        chessGameManager = new ChessGameManager();
    }

    @Override
    public void start(Stage stage) throws Exception {
        createStage(stage);
        createStage(new Stage()); //verificar se esta sincronizado
    }

    private void createStage(Stage stage){
        RootPane root = new RootPane(chessGameManager);
        Scene scene = new Scene(root,600,400);
        stage.setScene(scene);
        stage.setTitle("ChessGamePA");
        stage.show();

        Stage st2 = new Stage();
        LogsJFX logs = new LogsJFX();
        Scene sceneLogs = new Scene(logs,180,400);
        st2.setScene(sceneLogs);
        st2.setX(stage.getX()+stage.getWidth());
        st2.setY(stage.getY());
        st2.setTitle("LogsPane");
        st2.show();
    }
}
